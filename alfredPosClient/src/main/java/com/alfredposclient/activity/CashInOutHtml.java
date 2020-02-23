package com.alfredposclient.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.VerifyDialog;
import com.alfredbase.javabean.CashInOut;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.SettingDataSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.SystemUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.global.WebViewConfig;

import org.json.JSONObject;

import java.util.Map;

public class CashInOutHtml extends BaseActivity {
	private static final String ACTION_CONTINUE = "ACTION_CONTINUE";
	private static final String CashIn = "Cash In";
	private static final String defaultCash = "0.00";
	private WebView web;
	private JavaConnectJS javaConnectJS;
	private VerifyDialog dialog;
	
	private int type;
	private String cash;
	private String comment;
	private User user;

	private int id;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_common_web);
		dialog = new VerifyDialog(context, handler);
		dialog.show("initData",null);
	}



	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case VerifyDialog.DIALOG_RESPONSE:
					Map<String, Object> map = (Map<String, Object>) msg.obj;
					user = (User) map.get("User");
				//	id = (int) map.get("Id");
					init();
					break;
				case VerifyDialog.DIALOG_DISMISS:
					CashInOutHtml.this.finish();
					break;
			}
		}
	};

	private void init(){
		web = (WebView) findViewById(R.id.web);
		WebViewConfig.setDefaultConfig(web);
		javaConnectJS = new JavaConnectJS() {
			@Override
			@JavascriptInterface
			public void send(String action, String param) {
				if (!ButtonClickTimer.canClick(web)) {
					return;
				}
				if (!TextUtils.isEmpty(action)) {
					if(JavaConnectJS.LOAD_CASH_DEFAULT.equals(action)){
						mHandler.sendMessage(mHandler.obtainMessage(JavaConnectJS.ACTION_LOAD_CASH_DEFAULT, param));
					}
					if (JavaConnectJS.CLICK_BACK.equals(action)) {
						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
					}
					if (JavaConnectJS.CLICK_CASH_SAVE.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_CLICK_CASH_SAVE, param));
					}
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		if (SystemUtil.isZh(context))
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "cashinout_zh.html");
		else
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "cashinout.html");
	}

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}
	private String getLoadCashDefault(){
		return Store.getString(context, Store.LOAD_CASH_DEFAULT);
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_LOAD_CASH_DEFAULT: {
				String str = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(str) + "','"
						+ BH.getBD(getLoadCashDefault()).toString() + "')");
			}
				break;
			case JavaConnectJS.ACTION_CLICK_BACK:
				CashInOutHtml.this.finish();
				break;
			case JavaConnectJS.ACTION_CLICK_CASH_SAVE: {
				String str = (String) msg.obj;
                CashInOut cashInOut=new CashInOut();
				try {
					JSONObject jsonObject = new JSONObject(str);
					JSONObject saveDataJsonObject = jsonObject.getJSONObject("saveData");
					String typeString = saveDataJsonObject.optString("type");
					if (typeString.equals(CashIn)) {
						type = ParamConst.CASHINOUT_TYPE_IN;
					} else {
						type = ParamConst.CASHINOUT_TYPE_OUT;
					}
					cash = saveDataJsonObject.optString("cash");
					comment = saveDataJsonObject.optString("comment");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!TextUtils.isEmpty(cash) && !cash.equals(defaultCash)) {
					if (App.instance.countryCode == ParamConst.CHINA) {
						user.setUserName(user.getLastName() + user.getFirstName());
					} else {
						user.setUserName(user.getFirstName() + "." + user.getLastName());
					}
                     cashInOut= ObjectFactory.getInstance().getCashInOut(App.instance.getRevenueCenter(),
							App.instance.getLastBusinessDate(), user, type, cash, comment);
					UIHelp.showToast(context, context.getResources().getString(R.string.save_success));
					Store.putString(context, Store.LOAD_CASH_DEFAULT, cash);
					mHandler.sendEmptyMessageDelayed(JavaConnectJS.ACTION_CLICK_BACK, 350);
				} else {
					UIHelp.showToast(context, context.getResources().getString(R.string.cash_not_empty));
					return;
				}


				Restaurant restaurant = RestaurantSQL.getRestaurant();

			String logo=	SettingDataSQL.getSettingDataByUrl(restaurant.getLogoUrl()).getLogoString();
			restaurant.setLogoUrl(logo);
//				RevenueCenter title= App.instance.getRevenueCenter();
			//	PrinterTitle title	App.instance.getRevenueCenter(),
//                PrinterTitle title = ObjectFactory.getInstance()
//                        .getPrinterTitle(
//                                App.instance.getRevenueCenter(),
//                                order,
//                                App.instance.getUser().getFirstName()
//                                        + App.instance.getUser().getLastName(),
//                               "", 1);
//
				PrinterDevice printerDevice = App.instance.getCahierPrinter();
				App.instance.kickOutCashDrawer(printerDevice);
				App.instance.printOutCashDrawer(printerDevice,cashInOut,restaurant);
			}
				break;
			default:
				break;
			}
		}
	};
	
}
