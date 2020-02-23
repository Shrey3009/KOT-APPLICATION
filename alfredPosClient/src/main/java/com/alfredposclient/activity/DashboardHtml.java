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
import com.alfredbase.javabean.javabeanforhtml.DashboardInfo;
import com.alfredbase.javabean.javabeanforhtml.DashboardItemDetail;
import com.alfredbase.javabean.javabeanforhtml.DashboardSales;
import com.alfredbase.javabean.javabeanforhtml.DashboardTotalDetailInfo;
import com.alfredbase.store.sql.GeneralSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.SystemUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.ReportObjectFactory;
import com.alfredposclient.global.WebViewConfig;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DashboardHtml extends BaseActivity {
	private String TAG = DashboardHtml.class.getSimpleName();
	private WebView web;
	private JavaConnectJS javaConnectJS;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_common_web);
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
					DashboardHtml.this.finish();
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
				if(!TextUtils.isEmpty(action)){
					if(JavaConnectJS.LOAD_DASHBOARD.equals(action)){
						mHandler.sendMessage(mHandler.obtainMessage(JavaConnectJS.ACTION_LOAD_DASHBOARD, param));
					}
					if(ButtonClickTimer.canClick(web)){
						if(JavaConnectJS.CLICK_BACK.equals(action)){
							mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
						}
						if(JavaConnectJS.CLICK_REFRESH.equals(action)){
							mHandler.sendMessage(mHandler.obtainMessage(JavaConnectJS.ACTION_LOAD_DASHBOARD, param));
						}
					}
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		if (SystemUtil.isZh(context))
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "dashboard_zh.html");
		else
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "dashboard.html");
	}

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_LOAD_DASHBOARD:
				String str = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(str) + "','"
						+ getLoadDashboardStr() + "')");
				LogUtil.i(TAG, "javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(str) + "','"
						+ getLoadDashboardStr() + "')");
				break;
			case JavaConnectJS.ACTION_CLICK_BACK:
					DashboardHtml.this.finish();
				break;
			default:
				break;
			}
		};
	};
	
	private String getLoadDashboardStr(){
		
		long businessDate = App.instance.getLastBusinessDate();
		
		DashboardInfo dashboardInfo = new DashboardInfo();
		try {
			//ArrayList<DashboardTotalDetailInfo> totalDetailInfos = OrderSQL.getTotalDetaiInfosForOrder();
			ArrayList<DashboardTotalDetailInfo> totalDetailInfos = ReportObjectFactory.getInstance()
																		.loadDaySalesDashBoard(businessDate);

			String cash = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_CASH, businessDate);
			String masterCard = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_MASTERCARD, businessDate);
			String unipay = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_UNIPAY, businessDate);
			String visa = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_VISA, businessDate);
			String nets = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_NETS, businessDate);
			String amx = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_AMEX, businessDate);
			String jcb = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_JCB, businessDate);
			String interMational = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_DINNER_INTERMATIONAL, businessDate);
			BigDecimal cards = BH.add(BH.getBD(masterCard), BH.getBD(unipay), false);
			cards = BH.add(cards, BH.getBD(visa), false);
			cards = BH.add(cards, BH.getBD(nets), false);
			cards = BH.add(cards, BH.getBD(amx), false);
			cards = BH.add(cards, BH.getBD(jcb), false);
			cards = BH.add(cards, BH.getBD(interMational), true);
			String billOnHold = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD, businessDate);
			String company = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_COMPANY, businessDate);
			String hoursCharge = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_HOURS_CHARGE, businessDate);
			String mvoid = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_VOID, businessDate);
			String entertainment = PaymentSettlementSQL.getSumPaidAmountByPaymentTypeId(ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT, businessDate);
			BigDecimal other = BH.add(BH.getBD(billOnHold), BH.getBD(company), false);
			other = BH.add(other, BH.getBD(hoursCharge), false);
			other = BH.add(other, BH.getBD(mvoid), false);
			other = BH.add(other, BH.getBD(entertainment), true);
			String totalChecks = PaymentSettlementSQL.getSumPaidAmount(businessDate);
			int breakfast = OrderSQL.getSumCountBySessionType(ParamConst.SESSION_STATUS_BREAKFAST);
			int lunch = OrderSQL.getSumCountBySessionType(ParamConst.SESSION_STATUS_LUNCH);
			int dinner = OrderSQL.getSumCountBySessionType(ParamConst.SESSION_STATUS_DINNER);
			int totalOrders = breakfast + lunch + dinner;
			ArrayList<DashboardItemDetail> itemList = GeneralSQL.getDashboardItemDetailToItem(businessDate);
			ArrayList<DashboardItemDetail> categoryItemList = GeneralSQL.getDashboardItemDetailToCategory(businessDate);
			ArrayList<DashboardSales> dashboardSaless = GeneralSQL.getDashboardSales(businessDate);
			int itemSum = OrderDetailSQL.getSumOrderDetailItemNumByTime(businessDate);
			dashboardInfo.setTotalDetailInfos(totalDetailInfos);
			dashboardInfo.setCash(cash);
			dashboardInfo.setCards(cards.toString());
			dashboardInfo.setOthers(other.toString());
			dashboardInfo.setTotalChecks(totalChecks);
			dashboardInfo.setBreakfast(breakfast);
			dashboardInfo.setLunch(lunch);
			dashboardInfo.setDinner(dinner);
			dashboardInfo.setTotalOrders(totalOrders);
			dashboardInfo.setItemList(itemList);
			dashboardInfo.setCategoryItemList(categoryItemList);
			dashboardInfo.setWaiterAndSales(dashboardSaless);
			dashboardInfo.setItemSum(itemSum);
		} catch (Exception e) {
			dashboardInfo = null;
		}
		Gson gson = new Gson();
		String str = gson.toJson(dashboardInfo);
		LogUtil.d(TAG, str);
		return JSONUtil.getJSONFromEncode(str);
	}
}
