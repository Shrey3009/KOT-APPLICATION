package com.alfredposclient.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.ParamHelper;
import com.alfredbase.VerifyDialog;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.temporaryforapp.ReportUserOpenDrawer;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.UserOpenDrawerRecordSQL;
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
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.WebViewConfig;
import com.alfredposclient.utils.AlertToDeviceSetting;
import com.alfredposclient.utils.DialogSelectReportPrint;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XZReportHtml extends BaseActivity {
	private String TAG = XZReportHtml.class.getSimpleName();
	private WebView web;
	private JavaConnectJS javaConnectJS;
	private ReportDaySales reportDaySales;
	private List<ReportDayTax> reportDayTaxs;
	private ArrayList<ReportPluDayItem> reportPluDayItems;
	private List<ReportPluDayItem> filteredPluDayItems;
	private List<ReportPluDayModifier> reportPluDayModifiers;
	private List<ReportHourly> reportHourlys;
	private List<ItemCategory> itemCategorys;
	private List<ItemMainCategory> itemMainCategorys;
	private List<ReportPluDayComboModifier> reportPluDayComboModifiers;
	private Map<String, Object> map = new HashMap<String, Object>();
	private long businessDate;
	private BaseActivity context;

	private static final int TODAY_REPORT_CODE = 0;
	private static final int YESTERDAY_REPORT_CODE = -1;
	private static final int BEFORE_YESTERDAY_REPORT_CODE = -2;
	public static final int LOAD_CLOUD_REPORT_COMPLETE = 80;

	// public final static String X_REPORT = "X Reading";
	// public final static String Z_REPORT = "Z Reading";

	// report type for printing purpose
	public final static int REPORT_PRINT_ALL = 0;
	public final static int REPORT_PRINT_SALES = 1;
	public final static int REPORT_PRINT_DETAILS = 2;
	public final static int REPORT_PRINT_SUMMARY = 3;
	public final static int REPORT_PRINT_HOURLY = 4;

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

					break;
			}
		}
	};
	private void init(){

		businessDate = App.instance.getLastBusinessDate();
		web = (WebView) findViewById(R.id.web);
		WebViewConfig.setDefaultConfig(web);
		javaConnectJS = new JavaConnectJS() {
			@Override
			@JavascriptInterface
			public void send(String action, String param) {
				LogUtil.i(TAG, "action" + action + "；param" + param);
				if (!TextUtils.isEmpty(action)) {
					if (JavaConnectJS.LOAD_XZ_REPORT.equals(action)) {

						Long bizDate = null;
						boolean zFlag = false;
						Integer xSessionId = null;
						JSONObject jsonObject;

						try {
							jsonObject = new JSONObject(param);
							bizDate = jsonObject.optLong("bizDate");
							zFlag = jsonObject.optBoolean("z");
							xSessionId = jsonObject.optInt("x");
							mHandler.sendMessage(mHandler.obtainMessage(
									JavaConnectJS.ACTION_LOAD_XZ_REPORT, param));

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					if (JavaConnectJS.LOAD_ALL_XZ_REPORT.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_ALL_XZ_REPORT, param));
					}
					if (JavaConnectJS.DOWNLOAD_Z_REPORT.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_DOWNLOAD_Z_REPORT, param));
					}
					if (JavaConnectJS.CLICK_BACK.equals(action)) {
						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
					}
					if (JavaConnectJS.CLICK_PRINT.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_CLICK_PRINT, param));
					}
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		if (SystemUtil.isZh(context)) {
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "xzreport.html?code="
					+ App.instance.countryCode);
		} else
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "xzreport.html?code="
					+ App.instance.countryCode);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_LOAD_XZ_REPORT: {
				String str = (String) msg.obj;
				int dayCode = 0;
				Long bizDate = null;
				boolean zFlag = false;
				Integer xSessionId = null;

				try {
					String reportData = null;

					JSONObject jsonObject;
					jsonObject = new JSONObject(str);
					dayCode = jsonObject.optInt("code");
					bizDate = jsonObject.optLong("bizDate");
					zFlag = jsonObject.optBoolean("z");
					xSessionId = jsonObject.optInt("x");
					if (zFlag) {
						reportData = getZReportData(bizDate);
					} else {
						SessionStatus session = App.instance.getSessionStatus();
						reportData = getXReportData(bizDate, session);
					}
					LogUtil.w(TAG, "businessDate" + TimeUtil.getMDY(bizDate));
					web.loadUrl(ParamConst.JS_CONNECT_ANDROID + "('"
							+ JSONUtil.getJSCallBackName(str) + "','"
							+ reportData + "')");
					LogUtil.i(TAG, ParamConst.JS_CONNECT_ANDROID + "('"
							+ JSONUtil.getJSCallBackName(str) + "','"
							+ reportData + "')");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case JavaConnectJS.ACTION_LOAD_ALL_XZ_REPORT: {
//				String str = (String) msg.obj;
//				long businessDate = (Long) Store.getLong(XZReportHtml.this,
//						Store.BUSINESS_DATE);
//				if (businessDate < 0) {
//					businessDate = TimeUtil.getNewBusinessDate();
//				}
//				Map<String, Object> data = ReportObjectFactory.getInstance()
//						.loadDaySalesXZReport(businessDate,
//								App.instance.getSessionStatus());
//				String jsonDate = new Gson().toJson(data);
//				web.loadUrl(ParamConst.JS_CONNECT_ANDROID + "('"
//						+ JSONUtil.getJSCallBackName(str) + "','" + jsonDate
//						+ "')");
				}
				break;
			case JavaConnectJS.ACTION_DOWNLOAD_Z_REPORT: {
				JSONObject jsonObject;
				String str = (String) msg.obj;
				Long bizDate = 0L;
				RevenueCenter rc = App.instance.getRevenueCenter();
				if (rc != null) {
					try {
						Map<String, Object> param = new HashMap<String, Object>();
						jsonObject = new JSONObject(str);
						bizDate = jsonObject.optLong("bizDate");
						param.put("businessDate", TimeUtil.getMDY(bizDate));
						param.put("bizDate", bizDate);
						param.put("revenueId", rc.getId().intValue());
						SyncCentre.getInstance().loadCloudDaySalesReport(
								context, param, mHandler);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
				break;
			case XZReportHtml.LOAD_CLOUD_REPORT_COMPLETE: {
				Map<String, Object> param = (Map<String, Object>) msg.obj;
				String str = new Gson().toJson(param);
				web.loadUrl(ParamConst.JS_CONNECT_ANDROID
						+ "('ShowZReportDetail','"
						+ JSONUtil.getJSONFromEncode(str) + "')");
				}
				break;
			case JavaConnectJS.ACTION_CLICK_BACK:
				XZReportHtml.this.finish();
				break;
			case JavaConnectJS.ACTION_CLICK_PRINT: {
				String str = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(str);
					final Long bizDate = jsonObject.optLong("bizDate");
					final boolean zPrint = jsonObject.optBoolean("z");
					if (reportDaySales != null) {
						DialogSelectReportPrint.show(context,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										switch (v.getId()) {
										case R.id.btn_report_all: {
											sendPrintData(
													XZReportHtml.REPORT_PRINT_ALL,
													zPrint, bizDate);
										}
											break;
										case R.id.btn_report_sales: {
											sendPrintData(
													XZReportHtml.REPORT_PRINT_SALES,
													zPrint, bizDate);
										}
											break;
										case R.id.btn_report_detail_analysis: {
											sendPrintData(
													XZReportHtml.REPORT_PRINT_DETAILS,
													zPrint, bizDate);
										}
											break;
										case R.id.btn_report_summary_analysis: {
											sendPrintData(
													XZReportHtml.REPORT_PRINT_SUMMARY,
													zPrint, bizDate);
										}
											break;
										}
									}
								});
					} else {
						DialogFactory.alertDialog(
								XZReportHtml.this,
								context.getResources().getString(
										R.string.warning),
								context.getResources().getString(
										R.string.no_sales_print));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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

	// private String loadData() {
	// Gson gson = new Gson();
	// LogUtil.i("reportDaySales", gson.toJson(reportDaySales));
	// LogUtil.i("reportDayTaxs", gson.toJson(reportDayTaxs));
	// LogUtil.i("reportPluDayItems", gson.toJson(reportPluDayItems));
	// LogUtil.i("reportPluDayModifiers", gson.toJson(reportPluDayModifiers));
	// LogUtil.i("reportHourlys", gson.toJson(reportHourlys));
	// map.put("reportDaySales", reportDaySales);
	// map.put("reportDayTaxs", reportDayTaxs);
	// map.put("reportPluDayItems", filteredPluDayItems);
	// map.put("reportPluDayModifiers", reportPluDayModifiers);
	// map.put("reportHourlys", reportHourlys);
	// String str = gson.toJson(map);
	// LogUtil.d(TAG, str);
	// return JSONUtil.getJSONFromEncode(str);
	// }

	private String getXReportData(Long bizDate, SessionStatus sessionStatus) {

		reportDaySales = ReportObjectFactoryCP.getInstance().loadXReportDaySales(
				bizDate, sessionStatus, "0.00", false);
		if (reportDaySales != null) {
			reportDayTaxs = ReportObjectFactoryCP.getInstance()
					.loadXReportDayTax(reportDaySales, bizDate, sessionStatus);
			reportPluDayItems = ReportObjectFactoryCP.getInstance()
					.loadXReportPluDayItem(bizDate, sessionStatus);

			filteredPluDayItems = ReportObjectFactoryCP.getInstance()
					.getPLUItemWithoutVoidEnt(reportPluDayItems);

			reportPluDayModifiers = ReportObjectFactoryCP.getInstance()
					.loadReportPluDayModifier(bizDate);
			reportHourlys = ReportObjectFactoryCP.getInstance()
					.loadXReportHourlys(bizDate, sessionStatus);

			itemCategorys = ItemCategorySQL.getAllItemCategoryForReport();
			itemMainCategorys = ItemMainCategorySQL
					.getAllItemMainCategoryForReport();

		} else {
			reportDayTaxs = Collections.emptyList();
			reportPluDayItems = new ArrayList<ReportPluDayItem>();
			filteredPluDayItems = new ArrayList<ReportPluDayItem>();
			reportPluDayModifiers = Collections.emptyList();
			reportHourlys = Collections.emptyList();
			itemCategorys = Collections.emptyList();
			itemMainCategorys = Collections.emptyList();
		}

		Gson gson = new Gson();
		LogUtil.i("reportDaySales", gson.toJson(reportDaySales));
		LogUtil.i("reportDayTaxs", gson.toJson(reportDayTaxs));
		LogUtil.i("reportPluDayItems", gson.toJson(reportPluDayItems));
		LogUtil.i("reportPluDayModifiers", gson.toJson(reportPluDayModifiers));
		LogUtil.i("reportHourlys", gson.toJson(reportHourlys));
		map.put("reportDaySales", reportDaySales);
		map.put("reportDayTaxs", reportDayTaxs);
		map.put("reportPluDayItems", filteredPluDayItems);
		map.put("reportPluDayModifiers", reportPluDayModifiers);
		map.put("reportHourlys", reportHourlys);
		String str = gson.toJson(map);
		LogUtil.d(TAG, str);
		return JSONUtil.getJSONFromEncode(str);
	}

	private String getZReportData(Long bizDate) {

		// ReportDaySales reportDaySales;
		// List<ReportDayTax> reportDayTaxs;
		// ArrayList<ReportPluDayItem> reportPluDayItems;
		// List<ReportPluDayItem> filteredPluDayItems;
		// List<ReportPluDayModifier> reportPluDayModifiers;
		// List<ReportHourly> reportHourlys;
		// List<ItemCategory> itemCategorys;
		// List<ItemMainCategory> itemMainCategorys;

		reportDaySales = ReportObjectFactoryCP.getInstance().loadReportDaySales(
				bizDate, false);
		if (reportDaySales != null) {
			reportDayTaxs = ReportObjectFactoryCP.getInstance().loadReportDayTax(
					reportDaySales, bizDate);
			reportPluDayItems = ReportObjectFactoryCP.getInstance()
					.loadReportPluDayItem(bizDate);

			filteredPluDayItems = ReportObjectFactoryCP.getInstance()
					.getPLUItemWithoutVoidEnt(reportPluDayItems);
			Map<String, Object> map = ReportObjectFactoryCP.getInstance().loadReportPluDayModifierInfo(bizDate);
			reportPluDayModifiers = (List<ReportPluDayModifier>) map.get("reportPluDayModifiers");
			reportHourlys = ReportObjectFactoryCP.getInstance()
					.loadReportHourlys(bizDate);
			itemCategorys = ItemCategorySQL.getAllItemCategoryForReport();
			itemMainCategorys = ItemMainCategorySQL
					.getAllItemMainCategoryForReport();
			reportPluDayComboModifiers = (List<ReportPluDayComboModifier>) map.get("reportPluDayComboModifiers");
		} else {
			reportDayTaxs = Collections.emptyList();
			reportPluDayItems = new ArrayList<ReportPluDayItem>();
			filteredPluDayItems = new ArrayList<ReportPluDayItem>();
			reportPluDayModifiers = Collections.emptyList();
			reportHourlys = Collections.emptyList();
			itemCategorys = Collections.emptyList();
			itemMainCategorys = Collections.emptyList();
			reportPluDayComboModifiers = Collections.emptyList();
		}

		Gson gson = new Gson();
		LogUtil.i("reportDaySales", gson.toJson(reportDaySales));
		LogUtil.i("reportDayTaxs", gson.toJson(reportDayTaxs));
		LogUtil.i("reportPluDayItems", gson.toJson(reportPluDayItems));
		LogUtil.i("reportPluDayModifiers", gson.toJson(reportPluDayModifiers));
		LogUtil.i("reportHourlys", gson.toJson(reportHourlys));
		map.put("reportDaySales", reportDaySales);
		map.put("reportDayTaxs", reportDayTaxs);
		map.put("reportPluDayItems", filteredPluDayItems);
		map.put("reportPluDayModifiers", reportPluDayModifiers);
		map.put("reportHourlys", reportHourlys);
		String str = gson.toJson(map);
		LogUtil.d(TAG, str);
		return JSONUtil.getJSONFromEncode(str);
	}

	private void sendPrintData(int type, boolean zPrint, long bzDate) {
		String bizDate = TimeUtil.getPrintingDate(bzDate);
		String rptType = CommonUtil.getReportType(context, 999);

		SessionStatus session = App.instance.getSessionStatus();

		String label = "YX";
		if (zPrint) {
			label = "YZ";
		} else {
			rptType = CommonUtil.getReportType(context,
					session.getSession_status());
			bizDate = TimeUtil.getPrintingDate(businessDate);
		}

		PrinterTitle title = ObjectFactory.getInstance()
				.getPrinterTitleForReport(
						App.instance.getRevenueCenter().getId(),
						label
								+ ParamHelper.getPrintOrderBillNo(
										App.instance.getIndexOfRevenueCenter(),
										0),
						App.instance.getUser().getFirstName()
								+ App.instance.getUser().getLastName(), null,
						bizDate,App.instance.getSystemSettings().getTrainType());

		PrinterDevice cashierPrinter = App.instance.getCahierPrinter();
		List<ReportUserOpenDrawer> reportUserOpenDrawers = new ArrayList<ReportUserOpenDrawer>();
		if(zPrint){
			reportUserOpenDrawers = UserOpenDrawerRecordSQL.getReportUserOpenDrawerByTime(businessDate);
		}else{
			reportUserOpenDrawers = UserOpenDrawerRecordSQL.getReportUserOpenDrawer(session.getSession_status(), bzDate);
		}
		if (cashierPrinter == null) {
			AlertToDeviceSetting.noKDSorPrinter(context, context.getResources()
					.getString(R.string.no_printer_devices));
		} else {
			if (type == XZReportHtml.REPORT_PRINT_ALL) {

				// sales report
//				App.instance.remotePrintDaySalesReport(rptType, cashierPrinter,
//						title, reportDaySales, reportDayTaxs, reportUserOpenDrawers, null);
				// detail analysis
				App.instance.remotePrintDetailAnalysisReport(rptType,
						cashierPrinter, title, reportDaySales,
						filteredPluDayItems, reportPluDayModifiers,
						reportPluDayComboModifiers, itemMainCategorys,
						itemCategorys);
				// summary
				App.instance
						.remotePrintSummaryAnalysisReport(rptType,
								cashierPrinter, title, filteredPluDayItems,
								reportPluDayModifiers, itemMainCategorys,
								itemCategorys);
				// hourly sales
				App.instance.remotePrintHourlyReport(rptType, cashierPrinter,
						title, reportHourlys);
			}
			if (type == XZReportHtml.REPORT_PRINT_SALES) {
				// sales report
//				App.instance.remotePrintDaySalesReport(rptType, cashierPrinter,
//						title, reportDaySales, reportDayTaxs, reportUserOpenDrawers, null);
			}
			if (type == XZReportHtml.REPORT_PRINT_DETAILS) {
				if (zPrint)
					App.instance.remotePrintDetailAnalysisReport(rptType,
							cashierPrinter, title, reportDaySales,
							filteredPluDayItems, reportPluDayModifiers,
							reportPluDayComboModifiers, itemMainCategorys,
							itemCategorys);
				else
					App.instance.remotePrintDetailAnalysisReport(rptType,
							cashierPrinter, title, null, filteredPluDayItems,
							reportPluDayModifiers, reportPluDayComboModifiers,
							itemMainCategorys, itemCategorys);

				// if(reportPluDayComboModifiers != null &&
				// reportPluDayComboModifiers.size() > 0) {
				// App.instance.remotePrintComboDetailAnalysisReport(rptType,
				// cashierPrinter, title,
				// filteredPluDayItems, reportPluDayComboModifiers);
				// }

			}
			if (type == XZReportHtml.REPORT_PRINT_SUMMARY) {
				// sales report
				App.instance
						.remotePrintSummaryAnalysisReport(rptType,
								cashierPrinter, title, filteredPluDayItems,
								reportPluDayModifiers, itemMainCategorys,
								itemCategorys);
			}
			if (type == XZReportHtml.REPORT_PRINT_HOURLY) {
				// hourly sales
				App.instance.remotePrintHourlyReport(rptType, cashierPrinter,
						title, reportHourlys);
			}
		}
	}
}
