package com.alfredposclient.global;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.R;
import com.alfredbase.javabean.Order;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.activity.BOHSettlementActivity;
import com.alfredposclient.activity.CashInOutHtml;
import com.alfredposclient.activity.ClockInOROut;
import com.alfredposclient.activity.DashboardHtml;
import com.alfredposclient.activity.DevicesActivity;
import com.alfredposclient.activity.EditOrderHtml;
import com.alfredposclient.activity.EditSettlementPage;
import com.alfredposclient.activity.EntVoidReportHtml;
import com.alfredposclient.activity.Login;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.MonthlyPLUReportHtml;
import com.alfredposclient.activity.MonthlySalesReportHtml;
import com.alfredposclient.activity.NetWorkOrderActivity;
import com.alfredposclient.activity.OpenRestaruant;
import com.alfredposclient.activity.ReprintBillHtml;
import com.alfredposclient.activity.StoredCardActivity;
import com.alfredposclient.activity.SunmiActivity;
import com.alfredposclient.activity.SyncData;
import com.alfredposclient.activity.SystemSetting;
import com.alfredposclient.activity.TableSummaryActivity;
import com.alfredposclient.activity.XZReportActivity;
import com.alfredposclient.activity.kioskactivity.KioskHoldActivity;
import com.alfredposclient.activity.kioskactivity.MainPageKiosk;
import com.alfredposclient.activity.kioskactivity.subpos.ConnectMainPos;
import com.alfredposclient.activity.kioskactivity.subpos.SelectRevenue;
import com.alfredposclient.activity.kioskactivity.subpos.SubPosLogin;
import com.alfredposclient.activity.kioskactivity.subpos.SubPosManagePage;

public class UIHelp {
	public static void startLogin(BaseActivity context) {
		Intent intent = new Intent(context, Login.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.centre_open,
				R.anim.slide_bottom_out);
	}
	public static void startSubPosLogin(BaseActivity context) {
		Intent intent = new Intent(context, SubPosLogin.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.centre_open,
				R.anim.slide_bottom_out);
	}

	public static void startSelectRevenu(BaseActivity context){
		Intent intent = new Intent(context, SelectRevenue.class);
		context.startActivity(intent);
	}

	public static void startOpenRestaruant(BaseActivity context) {
		Intent intent = new Intent(context, OpenRestaruant.class);
		context.startActivity(intent);
	}

	public static void startMainPage(Context context) {
		Intent intent = new Intent(context, MainPage.class);
		context.startActivity(intent);
	}
	
	public static void startMainPageKiosk(Context context) {
		Intent intent = new Intent(context, MainPageKiosk.class);
		context.startActivity(intent);
	}

	public static void startDevicesHtml(BaseActivity context) {
		Intent intent = new Intent(context, DevicesActivity.class);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}

	public static void startEditOrderHtml(BaseActivity context) {
		Intent intent = new Intent(context, EditOrderHtml.class);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}

	public static void startBOHSettlementHtml(BaseActivity context) {
		Intent intent = new Intent(context, BOHSettlementActivity.class);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}
//	public static void startBOHSettlementHtml(BaseActivity context) {
//		Intent intent = new Intent(context, BOHSettlementHtml.class);
//		context.startActivity(intent);
////		context.overridePendingTransition(R.anim.slide_bottom_in,
////				R.anim.centre_close_70);
//	}

	public static void startReprintBillHtml(BaseActivity context) {
		Intent intent = new Intent(context, ReprintBillHtml.class);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}

	public static void startDashboardHtml(BaseActivity context) {
		Intent intent = new Intent(context, DashboardHtml.class);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}

	public static void startXZReportActivty(BaseActivity context) {
		Intent intent = new Intent(context, XZReportActivity.class);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}
	public static void startEntPluHtml(BaseActivity context) {
		Intent intent = new Intent(context, EntVoidReportHtml.class);
		intent.putExtra("from", 10);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}
	public static void startVoidPluHtml(BaseActivity context) {
		Intent intent = new Intent(context, EntVoidReportHtml.class);
		intent.putExtra("from", 20);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}
	
	public static void startEditSettlementHtml(BaseActivity context) {
		Intent intent = new Intent(context, EditSettlementPage.class);
		context.startActivityForResult(intent, 0);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}

	public static void startSyncData(BaseActivity context) {
		Intent intent = new Intent(context, SyncData.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in,
				R.anim.slide_left_out);
	}

	public static void startClockInOROut(BaseActivity context){
		Intent intent = new Intent(context, ClockInOROut.class);
		context.startActivity(intent);
	}
	
	public static void startCashInOut(BaseActivity context){
		Intent intent = new Intent(context, CashInOutHtml.class);
		context.startActivity(intent);
	}	
	public static void startSystemSetting(BaseActivity context){
		Intent intent = new Intent(context,SystemSetting.class);
		context.startActivityForResult(intent, 0);
	}
	
	public static void startNetWorkOrderActivity(BaseActivity context, int requestCode){
		Intent intent = new Intent(context,NetWorkOrderActivity.class);
		context.startActivityForResult(intent, requestCode);
	}
	
	public static void startMonthlySalesReport(BaseActivity context){
		Intent intent = new Intent(context,MonthlySalesReportHtml.class);
		context.startActivity(intent);
	}

	public static void startMonthlyPLUReport(BaseActivity context){
		Intent intent = new Intent(context,MonthlyPLUReportHtml.class);
		context.startActivity(intent);
	}

	public static void startSoredCardActivity(BaseActivity context, int requestCode){
		Intent intent = new Intent(context,StoredCardActivity.class);
		if(requestCode < 0){
			context.startActivity(intent);
		}else{
			context.startActivityForResult(intent, requestCode);
		}


	}
	public static void startSoredCardActivity(BaseActivity context){
		startSoredCardActivity(context, -1);
	}

//	public static void showToast(BaseActivity context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//	}
	public static void showToast(BaseActivity context, String text){
		Toast toast = new Toast(context);
		LayoutInflater inflater = context.getLayoutInflater();
		View view = inflater.inflate(R.layout.toast_view, null);
		TextView tv_toast_view = (TextView) view.findViewById(R.id.tv_toast_view);
		tv_toast_view.setText(text);
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular(tv_toast_view);
		toast.setGravity(Gravity.CENTER, 0, 10);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
	
	public static void showShortToast(BaseActivity context, String text){
		Toast toast = new Toast(context);
		LayoutInflater inflater = context.getLayoutInflater();
		View view = inflater.inflate(R.layout.toast_view, null);
		TextView tv_toast_view = (TextView) view.findViewById(R.id.tv_toast_view);
		tv_toast_view.setText(text);
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular(tv_toast_view);
		toast.setGravity(Gravity.CENTER, 0, 10);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}

	public static void startSunmiActivity(BaseActivity context) {
		Intent intent = new Intent(context, SunmiActivity.class);
		context.startActivityForResult(intent, 1);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.centre_close_70);
	}

	public static void startTableSummaryActivity(BaseActivity context) {
		Intent intent = new Intent(context, TableSummaryActivity.class);
		context.startActivity(intent);
	}

	public static void startKioskHoldActivity(BaseActivity context, boolean hasOrder, Order currentOrder){
		Intent intent = new Intent(context, KioskHoldActivity.class);
		intent.putExtra("hasOrder", hasOrder);
		intent.putExtra("currentOrder", currentOrder);
		context.startActivityForResult(intent, KioskHoldActivity.CHECK_REQUEST_CODE);
	}

	public static void startConnectMainPOS(BaseActivity context){
		Intent intent = new Intent(context, ConnectMainPos.class);
		context.startActivity(intent);
	}
	public static void startSubPosManagePage(BaseActivity context){
		Intent intent = new Intent(context, SubPosManagePage.class);
		context.startActivity(intent);
	}
}
