package com.alfredposclient.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.ParamHelper;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.VerifyDialog;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.ReportEntItem;
import com.alfredbase.javabean.model.ReportVoidItem;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.SystemUtil;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.ReportObjectFactory;
import com.alfredposclient.global.ReportObjectFactoryCP;
import com.alfredposclient.global.WebViewConfig;
import com.alfredposclient.utils.AlertToDeviceSetting;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntVoidReportHtml extends BaseActivity {
	private String TAG = EntVoidReportHtml.class.getSimpleName();
	private WebView web;
	private JavaConnectJS javaConnectJS;
	private ReportDaySales reportDaySales;
	private List<ReportVoidItem> reportVoidItems;
	private List<ReportEntItem> reportEntItems;

	private Map<String, Object> map = new HashMap<String, Object>();
	private long businessDate;
    private BaseActivity context;

    private int mode = 0; /* 0: void, 1:ent*/
	private static final int TODAY_REPORT_CODE = 0;
	private static final int YESTERDAY_REPORT_CODE = -1;
	private static final int BEFORE_YESTERDAY_REPORT_CODE = -2;
	public final static String ENT_PLU_REPORT = "ENT PLU";
	public final static String VOID_PLU_REPORT = "VOID PLU";

	public final static int FROM_ENT_TAP = 10;
	public final static int FROM_VOID_TAP = 20;

	private int functionMode = 10;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_common_web);
		context = this;
		VerifyDialog verifyDialog = new VerifyDialog(context, handler);
		verifyDialog.show("initData",null);
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case VerifyDialog.DIALOG_RESPONSE:
					init();
					break;
				case VerifyDialog.DIALOG_DISMISS:
					EntVoidReportHtml.this.finish();
					break;
			}
		}
	};
	private void init(){

		businessDate = App.instance.getLastBusinessDate();
		functionMode = this.getIntent().getIntExtra("from", 10);

		web = (WebView) findViewById(R.id.web);
		WebViewConfig.setDefaultConfig(web);
		javaConnectJS = new JavaConnectJS() {
			@Override
			@JavascriptInterface
			public void send(String action, String param) {
				LogUtil.i(TAG, "action" + action + "；param" + param);
				if (!TextUtils.isEmpty(action)) {
					if (JavaConnectJS.LOAD_VOID_PLU.equals(action)) {
						int dayCode = 0;
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(param);
							dayCode = jsonObject.optInt("code");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (dayCode == 0) {
							businessDate = App.instance.getLastBusinessDate();
						} else {
							businessDate = TimeUtil.getBusinessDateByDay(App.instance.getLastBusinessDate(), dayCode);
						}
						//getDataSync();
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_VOID_PLU, param));
					}
					if (JavaConnectJS.CLICK_BACK.equals(action)) {
						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
					}
					if (JavaConnectJS.CLICK_PRINT.equals(action)) {
						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_PRINT);
					}
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		if (SystemUtil.isZh(context)) {
			if (functionMode == EntVoidReportHtml.FROM_ENT_TAP)
				   web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "entVoid_zh.html");
				else
				   web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "entVoid_zh.html");
		} else {
			if (functionMode == EntVoidReportHtml.FROM_ENT_TAP)
				   web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "entVoid.html");
				else
				   web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "voidEnt.html");

		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_LOAD_VOID_PLU:
				String str = (String) msg.obj;
				int dayCode = 0;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(str);
					dayCode = jsonObject.optInt("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (dayCode == 0) {
					businessDate = App.instance.getLastBusinessDate();
				} else {
					businessDate = TimeUtil.getBusinessDateByDay(App.instance.getLastBusinessDate(), dayCode);
				}
				LogUtil.w(TAG, "businessDate" + businessDate);
				web.loadUrl(ParamConst.JS_CONNECT_ANDROID + "('"
						+ JSONUtil.getJSCallBackName(str) + "','" + loadVoidPluData()
						+ "')");
				LogUtil.i(
						TAG,
						ParamConst.JS_CONNECT_ANDROID + "('"
								+ JSONUtil.getJSCallBackName(str) + "','"
								+ loadVoidPluData() + "')");
				break;
			case JavaConnectJS.ACTION_CLICK_BACK:
				EntVoidReportHtml.this.finish();
				break;
			case JavaConnectJS.ACTION_CLICK_PRINT:
				if (reportDaySales != null) {
					sendPrintData(0);
				} else {
					DialogFactory.alertDialog(EntVoidReportHtml.this, context.getResources().getString(R.string.warning),
							context.getResources().getString(R.string.no_sales_print));
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}

	private String loadVoidPluData() {
		Gson gson = new Gson();


		this.reportEntItems = ReportObjectFactory.getInstance().loadReportEntItem(businessDate);
		this.reportVoidItems = ReportObjectFactory.getInstance().loadReportVoidItem(businessDate);
		this.reportDaySales = ReportObjectFactory.getInstance().loadReportDaySales(businessDate);

		if (reportDaySales != null) {
			map.put("total", String.valueOf(BH.add(BH.getBD(reportDaySales.getItemVoid()),
													BH.getBD(reportDaySales.getBillVoid()), true)));
		if(functionMode == FROM_ENT_TAP) {
			map.put("plu", this.reportEntItems);
		}else{
			map.put("plu", this.reportVoidItems);
		}
			map.put("bizDate", TimeUtil.getPrintingDate(businessDate));
		} else {
			map.put("total", 0);
			map.put("plu", null);
			map.put("bizDate", TimeUtil.getPrintingDate(businessDate));
		}

	    String str = gson.toJson(map);
		LogUtil.d(TAG, str);
		return JSONUtil.getJSONFromEncode(str);
	}

//	private void getDataSync() {
//		reportDaySales = ReportObjectFactory.getInstance().loadReportDaySales(
//				businessDate);
//		if (reportDaySales != null) {
//			this.reportVoidItems = ReportObjectFactory.getInstance().loadReportVoidItem(businessDate);
//		}else {
//			reportVoidItems = Collections.emptyList();
//		}
//	}

	private void sendPrintData(int type) {
        String bizDate = TimeUtil.getPrintingDate(this.businessDate);
		PrinterTitle title = ObjectFactory
				.getInstance()
				.getPrinterTitleForReport(
						App.instance.getRevenueCenter().getId(),
						"ZY"+ParamHelper.getPrintOrderBillNo(App.instance.getIndexOfRevenueCenter(), 0),
						App.instance.getUser().getFirstName()
								+ App.instance.getUser().getLastName(), null,bizDate,App.instance.getSystemSettings().getTrainType());

		PrinterDevice cashierPrinter = App.instance.getCahierPrinter();
		if (cashierPrinter == null) {
			AlertToDeviceSetting.noKDSorPrinter(context, context.getResources().getString(R.string.no_printer_devices));
		} else {
			String  rptType = CommonUtil.getReportType(context, 0);
			PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
					context);
			printerLoadingDialog.setTitle(context.getResources().getString(R.string.printing));
			printerLoadingDialog.showByTime(2000);
			if(functionMode == FROM_ENT_TAP){
				if (reportEntItems != null && reportEntItems.size() > 0)
					// Ent PLU
					App.instance.remotePrintEntItemReport(rptType, cashierPrinter,
							title, (ArrayList<ReportEntItem>) reportEntItems);
			}else{
				if (reportVoidItems != null && reportVoidItems.size() > 0)
					// Void PLU
					App.instance.remotePrintVoidItemReport(rptType, cashierPrinter,
							title, (ArrayList<ReportVoidItem>) reportVoidItems);
			}

		}
	}
}
