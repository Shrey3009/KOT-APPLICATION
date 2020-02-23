package com.alfredposclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.BohHoldSettlement;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.BohHoldSettlementSQL;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.global.WebViewConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BOHSettlementHtml extends BaseActivity {
	private String TAG = BOHSettlementHtml.class.getSimpleName();
	private WebView web;
	private JavaConnectJS javaConnectJS;
	private Gson gson = new Gson();
	public static final int BOH_GETBOHHOLDUNPAID_INFO = 1;
	public static final int UPLOAD_BOH_PAID = 2;
	private String jsCallBackStr;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_common_web);
		web = (WebView) findViewById(R.id.web);
		WebViewConfig.setDefaultConfig(web);
		javaConnectJS = new JavaConnectJS() {
			@Override
			@JavascriptInterface
			public void send(String action, String param) {

				if (!TextUtils.isEmpty(action)) {
					LogUtil.d(TAG, action);
					if (JavaConnectJS.LOAD_BOH_SETTLEMENT_LIST.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_BOH_SETTLEMENT_LIST,
								param));
					} else {
						if (!ButtonClickTimer.canClick(web)) {
							return;
						}
						if (JavaConnectJS.CLICK_BACK.equals(action)) {
							mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
						}
						if (JavaConnectJS.CLICK_BOH_SAVE.equals(action)) {
							mHandler.sendMessage(mHandler.obtainMessage(
									JavaConnectJS.ACTION_CLICK_BOH_SAVE, param));
						}
						if (JavaConnectJS.CLICK_LOCAL_BOH_SAVE.equals(action)) {
							mHandler.sendMessage(mHandler.obtainMessage(
									JavaConnectJS.ACTION_CLICK_LOCAL_BOH_SAVE,
									param));
						}
						if (JavaConnectJS.LOAD_LOCAL_BOH_SETTLEMENT_LIST
								.equals(action)) {
							mHandler.sendMessage(mHandler
									.obtainMessage(
											JavaConnectJS.ACTION_LOAD_LOCAL_BOH_SETTLEMENT_LIST,
											param));
						}
					}
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "bohSettlement.html");
	}

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_CLICK_BACK:
				BOHSettlementHtml.this.finish();
				break;
			case JavaConnectJS.ACTION_LOAD_BOH_SETTLEMENT_LIST: {
				jsCallBackStr = (String) msg.obj;
				SyncCentre.getInstance().getBOHSettlement(context, mHandler);
			}
				break;
			case JavaConnectJS.ACTION_CLICK_BOH_SAVE: {
				String param = (String) msg.obj;
				getUploadBOHPaidJSON(param);
			}
				break;
			case JavaConnectJS.ACTION_LOAD_LOCAL_BOH_SETTLEMENT_LIST: {
				String param = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(param) + "','"
						+ getLocalBohSettlementListJSON() + "')");
			}
				break;
			case JavaConnectJS.ACTION_CLICK_LOCAL_BOH_SAVE: {
				String param = (String) msg.obj;
				Map<String, String> bohHoldSettlementMap = new HashMap<String, String>();
				JSONObject jsonject;
				try {
					jsonject = new JSONObject(param);
					bohHoldSettlementMap = gson.fromJson(
							jsonject.getString("bohSettlement"),
							new TypeToken<Map<String, String>>() {
							}.getType());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				BohHoldSettlement bohHoldSettlement = BohHoldSettlementSQL
						.getBohHoldSettlement(Integer
								.parseInt(bohHoldSettlementMap.get("id")));
				bohHoldSettlement.setStatus(ParamConst.BOH_HOLD_STATUS_PLAY);
				bohHoldSettlement.setPaymentType(Integer
						.parseInt(bohHoldSettlementMap.get("paymentType")));
				bohHoldSettlement.setPaidDate(System.currentTimeMillis());
				BohHoldSettlementSQL.addBohHoldSettlement(bohHoldSettlement);
			}
				break;
			case BOH_GETBOHHOLDUNPAID_INFO: {
				String param = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(jsCallBackStr) + "','"
						+ JSONUtil.getJSONFromEncode(param) + "')");
				LogUtil.i(
						TAG,
						"javascript:JsConnectAndroid('"
								+ JSONUtil.getJSCallBackName(jsCallBackStr)
								+ "','" + JSONUtil.getJSONFromEncode(param)
								+ "')");
			}
				break;
			case UPLOAD_BOH_PAID:
				SyncCentre.getInstance().getBOHSettlement(context, mHandler);
				break;
			case ResultCode.CONNECTION_FAILED:
				UIHelp.showToast(context, ResultCode.getErrorResultStr(context,
						(Throwable) msg.obj, context.getResources().getString(R.string.server)));
				break;
			default:
				break;
			}
		};
	};

	private String getLocalBohSettlementListJSON() {
		ArrayList<BohHoldSettlement> bohHoldSettlements = BohHoldSettlementSQL
				.getBohHoldSettlementsByStatus(ParamConst.BOH_HOLD_STATUS_UNPLAY);
		String json = gson.toJson(bohHoldSettlements);
		LogUtil.i(TAG, json);
		return JSONUtil.getJSONFromEncode(json);
	}

	private void getUploadBOHPaidJSON(String param) {
		Map<String, Object> bohHoldSettlementMap = null;
		JSONObject json;
		try {
			json = new JSONObject(param);
			bohHoldSettlementMap = gson.fromJson(
					json.getString("bohSettlement"),
					new TypeToken<Map<String, Object>>() {
					}.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bohHoldSettlementMap == null) {
			return;
		}
		bohHoldSettlementMap.put("paidDate", System.currentTimeMillis());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bohHoldSettlement", bohHoldSettlementMap);
		SyncCentre.getInstance().upDateBOHHoldPaid(context, map, mHandler);
	}

	@Override
	public void httpRequestAction(int action, Object obj) {
		if (action == ResultCode.DEVICE_NO_PERMIT) {
			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogFactory.showOneButtonCompelDialog(context, context.getResources().getString(R.string.warning),
							ResultCode.getErrorResultStrByCode(context,
									ResultCode.DEVICE_NO_PERMIT, null),
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Store.remove(context, Store.SYNC_DATA_TAG);
									App.instance.popAllActivityExceptOne(Welcome.class);
								}
							});
				}
			});
		}
	}
}
