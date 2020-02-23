package com.alfredbase.view;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.R;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.User;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.ToastUtils;
import com.alfredbase.view.NumerickeyboardOne.KeyBoardClickListener;
/**
 * 修改密码的dialog
 * @author Alex
 *
 */
public class ChangePasswordDialog extends Dialog implements KeyBoardClickListener {

	private static final int KEY_LENGTH = 5;
	private static final int STATE_IN_ENTER_ID = 0;
	private static final int STATE_IN_ENTER_PASSWORD = 1;
	private static final int STATE_IN_NEW_PASSWORD1 = 2;
	private static final int STATE_IN_NEW_PASSWORD2 = 3;
	public static final int DIALOG_RESPONSE = -10;
	
	public static final int UPDATE_PASSWORD_ACTION = 1000;
	/**
	 * 当前键盘输入对应的状态，0表示输入的员工ID，1表示输入的密码，2修改的密码第一次输入，3表示修改的密码第二次输入
	 */
	private int state = STATE_IN_ENTER_ID;
	
	private TextView tv_id_1;
	private TextView tv_id_2;
	private TextView tv_id_3;
	private TextView tv_id_4;
	private TextView tv_id_5;
	private NumerickeyboardOne numerickeyboardOne;
	private StringBuffer keyBuf = new StringBuffer();

	private String employee_ID;
	private String password;
	private String newPassword1;
	private String newPassword2;
	private Handler handler;
//	private String msgObject;
//	private Object obj;
	private Context context;
//	private String old_employee_ID;
//	private boolean hasLogined = false;
	private TextTypeFace textTypeFace;
	private TextView tv_title;
	
	private User user;
	public ChangePasswordDialog(Context context,Handler handler) {
		super(context, R.style.Dialog_verify);
		this.handler = handler;
		this.context = context;
		init();
	}
	
	private void init() {
		View contentView = View.inflate(getContext(),
				R.layout.dialog_input_manager_account, null);
		setContentView(contentView);
		tv_id_1 = (TextView) findViewById(R.id.tv_id_1);
		tv_id_2 = (TextView) findViewById(R.id.tv_id_2);
		tv_id_3 = (TextView) findViewById(R.id.tv_id_3);
		tv_id_4 = (TextView) findViewById(R.id.tv_id_4);
		tv_id_5 = (TextView) findViewById(R.id.tv_id_5);
		tv_title = (TextView) findViewById(R.id.tv_title);
		((TextView)findViewById(R.id.tv_auth)).setText(context.getResources().getString(R.string.change_employee_password));
		numerickeyboardOne = (NumerickeyboardOne) findViewById(R.id.numerickeyboard1);
		numerickeyboardOne.setKeyBoardClickListener(this);
		numerickeyboardOne.setParams((BaseActivity)context);
		setCancelable(true);
		initTextTypeFace(contentView);
//		setCanceledOnTouchOutside(false);
	}

	private void initTextTypeFace(View view) {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.init((BaseActivity)context);
		textTypeFace.setTrajanProRegular((TextView)view.findViewById(R.id.tv_auth));
		textTypeFace.setTrajanProRegular((TextView)view.findViewById(R.id.tv_title));
	}
	
	@Override
	public void onKeyBoardClick(String key) {
		if (keyBuf.length() >= KEY_LENGTH)
			return;

		BugseeHelper.buttonClicked(key);
		if (key.equals(context.getResources().getString(R.string.delete))) {
			if (keyBuf.length() > 0) {
				keyBuf.deleteCharAt(keyBuf.length() - 1);
			}
		} else if (key.equals(context.getResources().getString(R.string.back))) {
			if (state == STATE_IN_ENTER_PASSWORD) {
				((TextView)(findViewById(R.id.tv_title))).setText(context.getResources().getString(R.string.manager_id));
				state = STATE_IN_ENTER_ID;
				keyBuf.delete(0, keyBuf.length());
				employee_ID = null;
				password = null;
			} else {
				dismiss();
			}
		} else{
			keyBuf.append(key);
		}
		int key_len = keyBuf.length();
		setPassword(key_len);
		if (key_len == KEY_LENGTH) {
			switch (state) {
			case STATE_IN_ENTER_ID:{
				tv_title.setText(context.getResources().getString(R.string.password_));
				state = STATE_IN_ENTER_PASSWORD; 
				employee_ID = keyBuf.toString();
			}
				break;
			case STATE_IN_ENTER_PASSWORD:{
				password = keyBuf.toString();
				user = CoreData.getInstance().getUser(employee_ID,
						password);
				if (user == null) {
					ToastUtils.showToast((BaseActivity)context, context.getResources().getString(R.string.name_pwd_error));
					tv_title.setText(context.getResources().getString(R.string.pwd_user_id));
					state = STATE_IN_ENTER_ID;
					employee_ID = null;
					password = null;
				}else {
					tv_title.setText(context.getResources().getString(R.string.new_password));
					state = STATE_IN_NEW_PASSWORD1;
				}
			} 
				break;
			case STATE_IN_NEW_PASSWORD1:
				newPassword1 = keyBuf.toString();
				tv_title.setText(context.getResources().getString(R.string.new_password_again));
				state = STATE_IN_NEW_PASSWORD2;
				break;
			case STATE_IN_NEW_PASSWORD2:
				newPassword2 = keyBuf.toString();
				if(newPassword2.equals(newPassword1)){
					if(user != null){
//						user.setPassword(newPassword2);
//						UserSQL.addUser(user);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("user", user);
						map.put("newPassword", newPassword2);
						handler.sendMessage(handler.obtainMessage(UPDATE_PASSWORD_ACTION, map));
					}
					dismiss();
				}else{
					ToastUtils.showToast((BaseActivity)context, context.getResources().getString(R.string.new_password_no_same));
					tv_title.setText(context.getResources().getString(R.string.new_password));
					state = STATE_IN_NEW_PASSWORD1;
				}
				break;
			default:
				break;
			}
			keyBuf.delete(0, key_len);
			setPassword(keyBuf.length());
		}
	}
	@Override
	public void show() {
		tv_title.setText(context.getResources().getString(R.string.pwd_user_id));
		super.show();
	}
	
	@Override
	public void dismiss() {
		((TextView) (findViewById(R.id.tv_title)))
		.setText(context.getResources().getString(R.string.manager_id));
		state = STATE_IN_ENTER_ID;
		employee_ID = null;
		password = null;
		newPassword1 = null;
		newPassword2 = null;
		user = null;
		keyBuf.delete(0, keyBuf.length());
		setPassword(0);
		super.dismiss();
	}
	private void setPassword(int key_len) {
		switch (key_len) {
		case 0: {
			tv_id_1.setText("");
			tv_id_2.setText("");
			tv_id_3.setText("");
			tv_id_4.setText("");
			tv_id_5.setText("");
			break;
		}
		case 1: {
			tv_id_1.setText("*");
			tv_id_2.setText("");
			tv_id_3.setText("");
			tv_id_4.setText("");
			tv_id_5.setText("");
			break;
		}
		case 2: {
			tv_id_1.setText("*");
			tv_id_2.setText("*");
			tv_id_3.setText("");
			tv_id_4.setText("");
			tv_id_5.setText("");
			break;
		}
		case 3: {
			tv_id_1.setText("*");
			tv_id_2.setText("*");
			tv_id_3.setText("*");
			tv_id_4.setText("");
			tv_id_5.setText("");
			break;
		}
		case 4: {
			tv_id_1.setText("*");
			tv_id_2.setText("*");
			tv_id_3.setText("*");
			tv_id_4.setText("*");
			tv_id_5.setText("");
			break;
		}
		case 5: {
			tv_id_1.setText("*");
			tv_id_2.setText("*");
			tv_id_3.setText("*");
			tv_id_4.setText("*");
			tv_id_5.setText("*");
			break;
		}
		default:
			break;
		}
	}

}
