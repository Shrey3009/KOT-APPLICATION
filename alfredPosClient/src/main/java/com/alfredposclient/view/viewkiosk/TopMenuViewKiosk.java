package com.alfredposclient.view.viewkiosk;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.NetWorkOrderActivity;
import com.alfredposclient.activity.kioskactivity.MainPageKiosk;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.popupwindow.ManagerWindow;
import com.alfredposclient.popupwindow.SelectBillWindow;
import com.alfredposclient.view.SettingView;

public class TopMenuViewKiosk extends LinearLayout implements OnClickListener {
	private BaseActivity parent;
	private Handler handler;
	private ImageView iv_cancel;
	private LinearLayout ll_call_waiter;
	private LinearLayout ll_get_bill;
	private LinearLayout ll_net_order;
	private LinearLayout ll_kiosk_hold;
	private TextView tv_manage;
	private RelativeLayout rl_app_num;
	private TextView tv_app_num;
	private RelativeLayout rl_kiosk_hold_num;
	private TextView tv_kiosk_hold_num;

	private DrawerLayout mDrawerLayout; // activity滑动布局
	private SettingView mSettingView; // 右滑视图
	
	public TopMenuViewKiosk(Context context) {
		super(context);
		init(context);
	}

	public TopMenuViewKiosk(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setParams(BaseActivity parent, Handler handler,
			DrawerLayout drawerLayout,SettingView settingView) {
		this.parent = parent;
		this.handler = handler;
		this.mDrawerLayout = drawerLayout;
		this.mSettingView = settingView;
		showAppOrderReciving();
		showKioskHoldNum();
	}

	private void init(Context context) {
		View.inflate(context, R.layout.top_menu_kiosk_view, this);
		LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
				(int) (300 * ScreenSizeUtil.width / ScreenSizeUtil.WIDTH_POS),
				LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams ps1 = new LinearLayout.LayoutParams(
				(int) (700 * ScreenSizeUtil.width / ScreenSizeUtil.WIDTH_POS),
				LayoutParams.MATCH_PARENT);
		LinearLayout ll_blank = (LinearLayout) findViewById(R.id.ll_blank);
		ll_blank.setLayoutParams(ps);
		LinearLayout ll_search = (LinearLayout) findViewById(R.id.ll_search);
		ll_search.setLayoutParams(ps1);
		((TextView) findViewById(R.id.tv_revenue_center_name)).setText(App.instance.getRevenueCenter().getRevName());
		tv_manage = (TextView) findViewById(R.id.tv_manage);
		tv_manage.setText(App.instance.getUser().getFirstName());
		tv_manage.setOnClickListener(this);
		findViewById(R.id.iv_setting).setOnClickListener(this);
		ll_call_waiter = (LinearLayout) findViewById(R.id.ll_call_waiter);
		ll_get_bill = (LinearLayout) findViewById(R.id.ll_get_bill);
		ll_net_order = (LinearLayout) findViewById(R.id.ll_net_order);
		ll_kiosk_hold = (LinearLayout) findViewById(R.id.ll_kiosk_hold);

		ll_get_bill.setOnClickListener(this);
		ll_net_order.setOnClickListener(this);
		ll_kiosk_hold.setOnClickListener(this);
		findViewById(R.id.iv_search).setOnClickListener(this);
		initTextTypeFace();
		rl_app_num = (RelativeLayout) findViewById(R.id.rl_app_num);
		tv_app_num = (TextView) findViewById(R.id.tv_app_num);
		rl_kiosk_hold_num = (RelativeLayout) findViewById(R.id.rl_kiosk_hold_num);
		tv_kiosk_hold_num = (TextView) findViewById(R.id.tv_kiosk_hold_num);
	}

	public void showAppOrderReciving(){
		if(App.instance.getAppOrderNum() > 0){
			rl_app_num.setVisibility(View.VISIBLE);
			tv_app_num.setText(App.instance.getAppOrderNum() + "");
		}else{
			rl_app_num.setVisibility(View.INVISIBLE);
		}
	}

	public void showKioskHoldNum(){
		if(App.instance.getKioskHoldNum() > 0){
			rl_kiosk_hold_num.setVisibility(View.VISIBLE);
			tv_kiosk_hold_num.setText(App.instance.getKioskHoldNum()+"");
		}else{
			rl_kiosk_hold_num.setVisibility(View.INVISIBLE);
		}
	}

	public void refreshUserName(){
		tv_manage.setText(App.instance.getUser().getFirstName());
	}
	private void initTextTypeFace() {
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_name));
		textTypeFace.setTrajanProBlod(tv_manage);
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_bill));
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_get_bill_num));
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_waiter));
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_net_order));
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_kiosk_hold));
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_waiter_number));
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_search));
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_revenue_center_name));
	}
	
	private void showSearch() {
		Message msg = handler.obtainMessage();
		msg.what = MainPage.VIEW_EVENT_SHOW_SEARCH;
		handler.sendMessage(msg);
	}
	
	public void setGetBillNum(int num){
		if (num <= 0) {
			return;
		}
		findViewById(R.id.tv_get_bill_num).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tv_get_bill_num)).setText(String.valueOf(num));
	}

	@Override
	public void onClick(View v) {
		if (ButtonClickTimer.canClick(v)) {
			switch (v.getId()) {
			case R.id.tv_manage: {
				ManagerWindow managerWindow = new ManagerWindow(parent,
						findViewById(R.id.tv_manage));
				managerWindow.show(handler);
				break;
			}
			case R.id.iv_setting: {
//				SettingWindow settingWindow = new SettingWindow(parent,
//						findViewById(R.id.iv_setting));
//				settingWindow.show();
				// 如果左边视图显示则关闭，如果右边视图显示则关闭如果关闭则显示
				if (mDrawerLayout.isDrawerOpen(mSettingView)) {
					mDrawerLayout.closeDrawer(Gravity.END);
				} else {
					mDrawerLayout.openDrawer(Gravity.END);
				}
				break;
			}
			case R.id.iv_search: {
				showSearch();
				break;
			}
			case R.id.ll_get_bill:
				SelectBillWindow selectBillWindow = new SelectBillWindow(parent, findViewById(R.id.ll_get_bill), handler);
				selectBillWindow.show(App.instance.getGetTingBillNotifications());
				break;
			case R.id.ll_net_order:
				UIHelp.startNetWorkOrderActivity(parent, NetWorkOrderActivity.CHECK_REQUEST_CODE);
				break;
			case R.id.ll_kiosk_hold:
				Message msg = handler.obtainMessage();
				msg.what = MainPageKiosk.VIEW_EVENT_START_KIOSK_BOLD;
				handler.sendMessage(msg);
				break;
			default:
				break;
			}

		}

	}

//	public void cancelSearch() {
//		iv_cancel.setVisibility(View.INVISIBLE);
//		et_search.setText("");
//		et_search.clearFocus();
//	}

}
