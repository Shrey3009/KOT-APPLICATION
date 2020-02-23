package com.alfredposclient.activity;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserTimeSheet;
import com.alfredbase.store.sql.UserTimeSheetSQL;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.view.MoneyKeyboardForClock;
import com.alfredposclient.view.MoneyKeyboardForClock.KeyBoardClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockInOROut extends BaseActivity implements KeyBoardClickListener {
	public static final int GET_LIST_SUCCESS = 101;
	public static final int CLOCK_SUCCESS = 102;

	private MoneyKeyboardForClock moneyKeyboardForClock;
	private TextView tv_psw_1;
	private TextView tv_psw_2;
	private TextView tv_psw_3;
	private TextView tv_psw_4;
	private TextView tv_psw_5;
	private ImageButton btn_back;
	private StringBuffer keyBuf = new StringBuffer();
	private static final int KEY_LENGTH = 5;
	private List<UserTimeSheet> userTimeSheets;
	private ListView lv_user_click_in;
	private ClockInAdapter clockInAdapter;
	public static final int HTTP_FAIL = -101;
	private TextTypeFace textTypeFace;
	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_clock_inorout);
		moneyKeyboardForClock = (MoneyKeyboardForClock) findViewById(R.id.moneyKeyboardForClock);
		moneyKeyboardForClock.setKeyBoardClickListener(this);
		textTypeFace = TextTypeFace.getInstance();
		tv_psw_1 = (TextView) findViewById(R.id.tv_psw_1);
		tv_psw_2 = (TextView) findViewById(R.id.tv_psw_2);
		tv_psw_3 = (TextView) findViewById(R.id.tv_psw_3);
		tv_psw_4 = (TextView) findViewById(R.id.tv_psw_4);
		tv_psw_5 = (TextView) findViewById(R.id.tv_psw_5);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		lv_user_click_in = (ListView) findViewById(R.id.lv_user_click_in);
		userTimeSheets = new ArrayList<>();
		loadingDialog = new LoadingDialog(context);
		loadingDialog.setTitle(getString(R.string.loading));
		getList();
	}

	private void initTextTypeFace(){
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_title));

	}

	private void getList(){
		if(loadingDialog != null && (!loadingDialog.isShowing())) {
			loadingDialog.show();
		}
		long businessDate = TimeUtil.getNewBusinessDate();
		if(App.instance.getBusinessDate() > businessDate){
			businessDate = App.instance.getBusinessDate();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("businessDate", businessDate);
		SyncCentre.getInstance().getClockInUserTimeSheet(context,map, handler);
	}
	//只作为请求用
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case GET_LIST_SUCCESS: {
					dismissLoadingDialog();
					userTimeSheets = (List<UserTimeSheet>) msg.obj;
					if (clockInAdapter == null) {
						clockInAdapter = new ClockInAdapter();
						lv_user_click_in.setAdapter(clockInAdapter);
					} else {
						clockInAdapter.notifyDataSetChanged();
					}
				}
					break;
				case CLOCK_SUCCESS:
					UIHelp.showShortToast(context, getString(R.string.succeed));
					getList();
					break;
				case ResultCode.USER_LOGIN_EXIST: {
					dismissLoadingDialog();
					int tyep = (int) msg.obj;
					if (tyep == 1) {
						UIHelp.showShortToast(context, getString(R.string.user_already_clock_in));
					} else {
						UIHelp.showShortToast(context, getString(R.string.please_clock_in));
					}
				}
					break;
				case HTTP_FAIL: {
					dismissLoadingDialog();
					UIHelp.showShortToast(context, ResultCode.getErrorResultStrByCode(context,
							(Integer) msg.obj, context.getResources().getString(R.string.server)));
				}
					break;
				case ResultCode.CONNECTION_FAILED:{
					dismissLoadingDialog();
					UIHelp.showToast(context, ResultCode.getErrorResultStr(context,
							(Throwable) msg.obj, context.getResources().getString(R.string.server)));
				}
				break;
			}
		}
	};

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onKeyBoardClick(String key) {
		BugseeHelper.buttonClicked(key);
		if (keyBuf.length() > KEY_LENGTH)
			return;
		if (key.equals("Delete")) {
			if (keyBuf.length() > 0) {
				keyBuf.deleteCharAt(keyBuf.length() - 1);
			}
		} else if (key.equals("clock In")) {
			if (keyBuf.length() == KEY_LENGTH) {
//				if(clockIn(keyBuf.toString())){
//					UIHelp.showToast(context, context.getResources().getString(R.string.clock_in));
//				}else{
//					UIHelp.showToast(context, context.getResources().getString(R.string.id_error));
//				}
				clockInOut(keyBuf.toString(), 1);
				keyBuf.delete(0, keyBuf.length());
			} else {
				UIHelp.showToast(context, context.getResources().getString(R.string.id_error));
			}
		} else if (key.equals("clock Out")) {
			if (keyBuf.length() == KEY_LENGTH) {
//				if(clockOut(keyBuf.toString())){
//					UIHelp.showToast(context, context.getResources().getString(R.string.clock_out));
//				}else{
//					UIHelp.showToast(context, context.getResources().getString(R.string.id_error));
//				}
				clockInOut(keyBuf.toString(), 2);
				keyBuf.delete(0, keyBuf.length());
			} else {
				UIHelp.showToast(context, context.getResources().getString(R.string.id_error));
			}
		} else if (keyBuf.length() < KEY_LENGTH) {
			keyBuf.append(key);
		}
		setPassword(keyBuf.length());

	}
	private void clockInOut(String employyeId, int type){
		Map<String, Object> map = new HashMap<>();
		loadingDialog.show();
		long businessDate = TimeUtil.getNewBusinessDate();
		if(App.instance.getBusinessDate() > businessDate){
			businessDate = App.instance.getBusinessDate();
		}
		map.put("businessDate", businessDate);
		map.put("empId", employyeId);
		map.put("type", type);
		SyncCentre.getInstance().clockInOut(context,map,handler);
	}

	private boolean clockIn(String employyeId) {
		User user = CoreData.getInstance().getUserByEmpId(
				Integer.parseInt(employyeId));
		if (user == null) {
			return false;
		}

//		ArrayList<UserTimeSheet> mUserTimeSheets = UserTimeSheetSQL.getUserTimeSheetsByEmpId(Integer.parseInt(employyeId), App.instance.getLastBusinessDate());
//		for(UserTimeSheet userTimeSheet : mUserTimeSheets){
//			if(userTimeSheet != null && userTimeSheet.getStatus() == ParamConst.USERTIMESHEET_STATUS_LOGIN){
//				return false;
//			}
//		}
//
//		UserTimeSheet userTimeSheet = ObjectFactory.getInstance()
//				.getUserTimeSheet(App.instance.getLastBusinessDate(), user,
//						App.instance.getRevenueCenter());
//		UserTimeSheetSQL.addUser(userTimeSheet);
		return true;
	}
	
	private boolean clockOut(String employyeId) {
		User user = CoreData.getInstance().getUserByEmpId(
				Integer.parseInt(employyeId));
		if(user == null){
			return false;
		}
		UserTimeSheet userTimeSheet = UserTimeSheetSQL.getUserTimeSheetByEmpId(Integer.parseInt(employyeId), App.instance.getLastBusinessDate());
		if (userTimeSheet == null) {
			return false;
		}
		userTimeSheet.setLogoutTime(System.currentTimeMillis());
		userTimeSheet.setStatus(ParamConst.USERTIMESHEET_STATUS_LOGOUT);
		UserTimeSheetSQL.addUser(userTimeSheet);
		return true;
	}

	private void setPassword(int key_len) {
		switch (key_len) {
		case 0: {
			tv_psw_1.setText("");
			tv_psw_2.setText("");
			tv_psw_3.setText("");
			tv_psw_4.setText("");
			tv_psw_5.setText("");
			break;
		}
		case 1: {
			tv_psw_1.setText(keyBuf.charAt(0) + "");
			tv_psw_2.setText("");
			tv_psw_3.setText("");
			tv_psw_4.setText("");
			tv_psw_5.setText("");
			break;
		}
		case 2: {
			tv_psw_1.setText(keyBuf.charAt(0) + "");
			tv_psw_2.setText(keyBuf.charAt(1) + "");
			tv_psw_3.setText("");
			tv_psw_4.setText("");
			tv_psw_5.setText("");
			break;
		}
		case 3: {
			tv_psw_1.setText(keyBuf.charAt(0) + "");
			tv_psw_2.setText(keyBuf.charAt(1) + "");
			tv_psw_3.setText(keyBuf.charAt(2) + "");
			tv_psw_4.setText("");
			tv_psw_5.setText("");
			break;
		}
		case 4: {
			tv_psw_1.setText(keyBuf.charAt(0) + "");
			tv_psw_2.setText(keyBuf.charAt(1) + "");
			tv_psw_3.setText(keyBuf.charAt(2) + "");
			tv_psw_4.setText(keyBuf.charAt(3) + "");
			tv_psw_5.setText("");
			break;
		}
		case 5: {
			tv_psw_1.setText(keyBuf.charAt(0) + "");
			tv_psw_2.setText(keyBuf.charAt(1) + "");
			tv_psw_3.setText(keyBuf.charAt(2) + "");
			tv_psw_4.setText(keyBuf.charAt(3) + "");
			tv_psw_5.setText(keyBuf.charAt(4) + "");
			break;
		}
		default:
			break;
		}
	}

	class  ClockInAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userTimeSheets.size();
		}

		@Override
		public Object getItem(int position) {
			return userTimeSheets.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView == null) {
				convertView =	LayoutInflater.from(context).inflate(R.layout.clock_in_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_clock_in_time = (TextView) convertView.findViewById(R.id.tv_clock_in_time);
				viewHolder.tv_business_date = (TextView) convertView.findViewById(R.id.tv_business_date);
				viewHolder.tv_staff = (TextView) convertView.findViewById(R.id.tv_staff);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			UserTimeSheet userTimeSheet = userTimeSheets.get(position);
			viewHolder.tv_staff.setText(userTimeSheet.getEmpName());
			String loginTime = "";
			if(userTimeSheet.getLoginTime() != null && userTimeSheet.getLoginTime() > 0){
				loginTime = TimeUtil.getPrintDateTime(userTimeSheet.getLoginTime());
			}
			viewHolder.tv_clock_in_time.setText(loginTime);
			viewHolder.tv_business_date.setText(TimeUtil.getPrintDate(userTimeSheet.getBusinessDate()));
			return convertView;
		}
		class ViewHolder {
			public TextView tv_business_date;
			public TextView tv_staff;
			public TextView tv_clock_in_time;
		}

	}

}
