package com.alfredposclient.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.Store;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.adapter.RevenueCentreListAdapter;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.floatwindow.float_lib.FloatActionController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncData extends BaseActivity {
	public static final int HANDLER_LOGIN = 0;
	public static final int HANDLER_GET_RESTAURANT_INFO = 7;
	public static final int HANDLER_GET_BINDDEVICEID_INFO = 2;
	public static final int HANDLER_GET_PLACE_INFO = 3;
	public static final int HANDLER_ERROR_INFO = 4;
	public static final int HANDLER_BIND_ALREADY = 6;
	public static final int SYNC_DATA_TAG = 8;
	public static final int SYNC_SUCCEED = 1;
	public static final int SYNC_FAILURE = 0;

	public static final int HANDLER_LOGIN_QRPAYMENT = 80;

	public static final int HANDLER_QRCODE_PAY88 = 81;
	public static final int HANDLER_CHECK_STATUS_PAY88 = 82;

	public static final int HANDLER_QRCODE_PAYHALAL = 83;
	public static final int HANDLER_CHECK_STATUS_PAYHALAL = 84;

	public static final String UNKNOW_ERROR = "Unknow error";
	public static final String CONN_TIMEOUT = "Connection timeout";
	private int syncDataCount = 0;
	private LoadingDialog dialog;
	private LinearLayout ll_login;
	private LinearLayout ll_revenue_centre;
	private ListView lv_revenue_centre;
	private boolean doubleBackToExitPressedOnce = false;
	private TextView et_user_id;
	private TextView et_password;
	private TextView et_biz_id;
	private ImageView iv_user_id;
	private ImageView iv_password;
	private ImageView iv_biz_id;
	private TextView tv_title_name_one;
	private TextView tv_title_name_two;

	@Override
	protected void initView() {
		super.initView();
//		Order order = OrderSQL.getOrder(2800);
//
////		List<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(2800);
//		List<Order> orderList = OrderSQL.getAllOrderByTime(1491321600000L);
//		List<String> aaa = new ArrayList<String>();
//		Gson gson = new Gson();
//		for(Order o : orderList){
//			String a = gson.toJson(UploadSQL.getOrderInfo(o.getId().intValue()));
//			aaa.add(a);
//		}

		setContentView(R.layout.activity_sync_data);
		dialog = new LoadingDialog(context);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		ll_login = (LinearLayout) findViewById(R.id.ll_login);
		ll_revenue_centre = (LinearLayout) findViewById(R.id.ll_revenue_centre);
		
		et_user_id = (TextView) findViewById(R.id.et_user_id);
		et_password = (TextView) findViewById(R.id.et_password);
		et_biz_id = (TextView) findViewById(R.id.et_biz_id);
		iv_user_id = (ImageView) findViewById(R.id.iv_user_id);
		iv_password = (ImageView) findViewById(R.id.iv_password);
		iv_biz_id = (ImageView) findViewById(R.id.iv_biz_id);
		et_user_id.requestFocus();
		et_user_id.setFocusable(true);
		syncDataCount = 0;
		et_user_id.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					iv_user_id.setBackgroundResource(R.color.brownness);
					iv_user_id.setImageDrawable(getResources().getDrawable(R.drawable.icon_manager_click));
				}else {
					iv_user_id.setBackgroundResource(R.color.white);
					iv_user_id.setImageDrawable(getResources().getDrawable(R.drawable.icon_manager));
				}
			}
		});
		et_password.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					iv_password.setBackgroundResource(R.color.brownness);
					iv_password.setImageDrawable(getResources().getDrawable(R.drawable.icon_password_click));
				}else {
					iv_password.setBackgroundResource(R.color.white);
					iv_password.setImageDrawable(getResources().getDrawable(R.drawable.icon_password));
				}
			}
		});
		et_biz_id.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					iv_biz_id.setBackgroundResource(R.color.brownness);
					iv_biz_id.setImageDrawable(getResources().getDrawable(R.drawable.icon_biz_id_click));
				}else {
					iv_biz_id.setBackgroundResource(R.color.white);
					iv_biz_id.setImageDrawable(getResources().getDrawable(R.drawable.icon_biz_id));
				}
			}
		});
		
		tv_title_name_one = (TextView) findViewById(R.id.tv_title_name_one);
		tv_title_name_two = (TextView) findViewById(R.id.tv_title_name_two);
		findViewById(R.id.iv_sub).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
		initTextTypeFace();
		App.instance.showWelcomeToSecondScreen();
	}


	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		switch (v.getId()) {
		case R.id.btn_ok: {
			login();
			break;
		}
		case R.id.iv_sub:{
			Store.putInt(this, Store.POS_TYPE, 1);
			App.instance.setPosType(1);
			UIHelp.startSelectRevenu(SyncData.this);
		}
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_LOGIN: {
				SyncCentre.getInstance().getRestaurantInfo(context, null,
						handler);
				break;
			}
			case HANDLER_GET_RESTAURANT_INFO: {
				dialog.dismiss();
				showRevenueCentre();
				break;
			}
			case HANDLER_GET_BINDDEVICEID_INFO: {
				syncDataCount = 0;
				SyncCentre.getInstance().syncCommonData(context, handler);
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("revenueId", ((RevenueCenter)(map.get("revenueCenter"))).getId().intValue());
				SyncCentre.getInstance().getPlaceInfo(context, param
						, handler);
			}
				break;
			case HANDLER_GET_PLACE_INFO: {
				// 延迟3秒，保证数据库存储完成
				BaseApplication.postHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						StringBuffer warn = new StringBuffer();
						List<ItemMainCategory> itemMainCategories = CoreData.getInstance().getItemMainCategories();
						List<User> users = CoreData.getInstance().getUsers();
						List<ItemCategory> itemCategories = CoreData.getInstance().getItemCategories();
						List<ItemDetail> itemDetails = CoreData.getInstance().getItemDetails();
						List<Printer> printers = CoreData.getInstance().getPrinters();
//						List<Tables> tableList = CoreData.getInstance().getTableList();
//						List<Places> placeList = CoreData.getInstance().getPlaceList();
						if(users.isEmpty()){
							warn.append(context.getResources().getString(R.string.users));
						}
						if(printers.isEmpty()){
							if(warn.length() != 0){
								warn.append(",");
							}
							warn.append(context.getResources().getString(R.string.printer));
						}else{
							boolean isPrinter = false;
							boolean isPrinterGroup = false;
							for(Printer printer : printers){
								if(printer.getType() == 0){
									isPrinterGroup = true;
								}
								if(printer.getType() == 1){
									isPrinter = true;
								}
							}
							if(!(isPrinter && isPrinterGroup)){
								warn.append(context.getResources().getString(R.string.printer_printergroup));
							}
						}
						if(itemDetails.isEmpty()){
							if(warn.length() != 0){
								warn.append(",");
							}
							warn.append(context.getResources().getString(R.string.item));
						}
						if(itemCategories.isEmpty() || itemMainCategories.isEmpty()){
							if(warn.length() != 0){
								warn.append(",");
							}
							warn.append(context.getResources().getString(R.string.product_categories));
						}
						
						dialog.dismiss();
						if(warn.length() == 0){
							DialogFactory.commonTwoBtnDialog(context, "",
									getString(R.string.sync_complete),
									getString(R.string.resync),
									getString(R.string.ok), null,
									new OnClickListener() {
										@Override
										public void onClick(View arg0) {
											Store.putString(context,
													Store.SYNC_DATA_TAG,
													Store.SYNC_DATA_TAG);
											App.instance.setLocalRestaurantConfig(CoreData.getInstance().getRestaurantConfigs());
											UIHelp.startLogin(context);
											finish();
										}
									});
						}else{
							DialogFactory.showOneButtonCompelDialog(context, context.getResources().getString(R.string.item),
									context.getResources().getString(R.string.sync_date_warning_tips) + warn.toString(), null);
						}
					}
				}, 2 * 1000);
				break;
			}
			case HANDLER_ERROR_INFO:
				dialog.dismiss();
				UIHelp.showToast(SyncData.this, ResultCode
						.getErrorResultStrByCode(SyncData.this,
								(Integer) msg.obj, null));
				break;
			case ResultCode.CONNECTION_FAILED:
				dialog.dismiss();
				UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable)msg.obj, context.getResources().getString(R.string.server)));
				break;
			case HANDLER_BIND_ALREADY:
				dialog.dismiss();
				// UIHelp.showToast(SyncData.this,(String)msg.obj);
//				DialogFactory.alertDialog(context, "Warning", (String) msg.obj);
				DialogFactory.showOneButtonCompelDialog(context, context.getResources().getString(R.string.warning),
						(String) msg.obj, new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								
							}
						});
				break;
			case SYNC_DATA_TAG:
				int type = (Integer) msg.obj;
				if(type == SYNC_SUCCEED){
					if(syncDataCount == 9){
						handler.sendEmptyMessage(HANDLER_GET_PLACE_INFO);
					}else{

						syncDataCount++;
						LogUtil.d("syncDataCount--", syncDataCount+"----");
					}
				}else{
					handler.sendEmptyMessage(HANDLER_GET_PLACE_INFO);
				}
				
				break;
			default:
				break;
			}
		};
	};

	private void showRevenueCentre() {
		ll_login.setVisibility(View.GONE);
		tv_title_name_one.setTextColor(getResources().getColor(R.color.gray));
		tv_title_name_two.setTextColor(getResources().getColor(R.color.black));
		ll_revenue_centre.setVisibility(View.VISIBLE);
		lv_revenue_centre = (ListView) findViewById(R.id.lv_revenue_centre);
		lv_revenue_centre.setAdapter(new RevenueCentreListAdapter(context));
		lv_revenue_centre.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				dialog.setTitle(context.getResources().getString(R.string.loading));
				dialog.show();
				RevenueCenter revenueCenter = (RevenueCenter) arg0
						.getItemAtPosition(arg2);
				App.instance.setRevenueCenter(revenueCenter);
				MainPosInfo mainPosInfo = new MainPosInfo(revenueCenter
						.getRestaurantId(), revenueCenter.getId(),
						revenueCenter.getRevName(), CommonUtil
								.getLocalIpAddress(), CommonUtil
								.getLocalMacAddress(context), App.instance.getRevenueCenter().getIsKiosk());
				App.instance.setMainPosInfo(mainPosInfo);
				Store.saveObject(context, Store.MAINPOSINFO, mainPosInfo);
				Store.saveObject(context, Store.CURRENT_REVENUE_CENTER,
						revenueCenter);
				
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("revenueCenter", revenueCenter);
				SyncCentre.getInstance().getBindDeviceIdInfo(context,
						parameters, handler);
				App.instance.bindSyncService();
			}
		});
	}



	private void login() {
		String userID = ((EditText) findViewById(R.id.et_user_id)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.et_password)).getText()
				.toString();
		String bizID = ((EditText) findViewById(R.id.et_biz_id)).getText()
				.toString();
		if(TextUtils.isEmpty(userID) || TextUtils.isEmpty(password) || TextUtils.isEmpty(bizID)){
			UIHelp.showToast(context, context.getResources().getString(R.string.login_info_error));
			return;
		}
		dialog.setTitle(context.getResources().getString(R.string.loading));
		dialog.show();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userID", Integer.parseInt(userID));
		parameters.put("password", password.trim());
		parameters.put("bizID", bizID.trim());
		parameters.put("machineType", 1);
		SyncCentre.getInstance().login(context, parameters, handler);
	}
	
	@Override
	public void onBackPressed() {
		 if (doubleBackToExitPressedOnce) {
			 FloatActionController.getInstance().stopMonkServer(context);
		        super.onBackPressed();
		        return;
		    }

		    this.doubleBackToExitPressedOnce = true;
		    UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));

		BaseApplication.postHandler.postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            doubleBackToExitPressedOnce=false;                       
		        }
		    }, 2000);
	}
	
	private void initTextTypeFace() {
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod((TextView)findViewById(R.id.tv_title_name_one));
		textTypeFace.setTrajanProBlod((TextView)findViewById(R.id.tv_title_name_two));
		textTypeFace.setTrajanProBlod((TextView)findViewById(R.id.tv_login_name));
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_remind));
		textTypeFace.setTrajanProRegular(et_user_id);
		textTypeFace.setTrajanProRegular(et_password);
		textTypeFace.setTrajanProRegular(et_biz_id);
		textTypeFace.setTrajanProRegular((Button)findViewById(R.id.btn_ok));
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_app_version));
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_sync_tips));
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_sync_time_tips));
	}
}
