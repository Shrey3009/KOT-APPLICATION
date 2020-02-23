package com.alfredbase;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.User;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.ToastUtils;
import com.alfredbase.view.NumerickeyboardOne;
import com.alfredbase.view.NumerickeyboardOne.KeyBoardClickListener;

import java.util.HashMap;
import java.util.Map;

public class VerifyDialog extends Dialog implements KeyBoardClickListener {

	private static final int KEY_LENGTH = 5;
	private static final int STATE_IN_ENTER_ID = 0;
	private static final int STATE_IN_ENTER_PASSWORD = 1;
	public static final int DIALOG_RESPONSE = -10;
	public static final int DIALOG_DISMISS = -11;
	/**
	 * 当前键盘输入对应的状态，0表示输入的员工ID，1表示输入的密码
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
	private Handler handler;
	private String msgObject;
	private Object obj;
	private Context context;
	//	private String old_employee_ID;
//	private boolean hasLogined = false;
	private TextTypeFace textTypeFace;

	public VerifyDialog(Context context,Handler handler) {
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
	public void onBackPressed() {
		super.onBackPressed();
		if(handler != null){
			handler.sendEmptyMessage(DIALOG_DISMISS);
		}
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
			if(handler != null){
				handler.sendEmptyMessage(DIALOG_DISMISS);
			}
		} else{
			keyBuf.append(key);
		}
		int key_len = keyBuf.length();
		setPassword(key_len);
		if (key_len == KEY_LENGTH) {
			if (state == STATE_IN_ENTER_ID) {
				((TextView) (findViewById(R.id.tv_title)))
						.setText(context.getResources().getString(R.string.password_));
//				state = STATE_IN_ENTER_PASSWORD;
				employee_ID = keyBuf.toString();
				keyBuf.delete(0, key_len);
				setPassword(keyBuf.length());
				int id = Integer.parseInt(employee_ID);
				User user = CoreData.getInstance().getUserByEmpId(id);
				Map<String, Object> resultObject = new HashMap<String, Object>();
				resultObject.put("MsgObject", msgObject);
				resultObject.put("Object", obj);

				if (user == null) {
					ToastUtils.showToast((BaseActivity)context, context.getResources().getString(R.string.name_pwd_error));
					((TextView) (findViewById(R.id.tv_title)))
							.setText(context.getResources().getString(R.string.manager_id));
					state = STATE_IN_ENTER_ID;
					employee_ID = null;
					password = null;
				}else {
					if (user.getType() != ParamConst.USER_TYPE_MANAGER) {
						ToastUtils.showToast((BaseActivity)context, context.getResources().getString(R.string.permission_insuff));
						((TextView) (findViewById(R.id.tv_title))).setText(context.getResources().getString(R.string.manager_id));
						state = STATE_IN_ENTER_ID;
						employee_ID = null;
						password = null;
					} else {
						((TextView) (findViewById(R.id.tv_title)))
								.setText(context.getResources().getString(R.string.manager_id));
						state = STATE_IN_ENTER_ID;
						employee_ID = null;
						password = null;
						resultObject.put("User", user);

						handler.sendMessage(handler.obtainMessage(DIALOG_RESPONSE, resultObject));
						this.dismiss();
					}
				}
			} else if (state == STATE_IN_ENTER_PASSWORD) {
				password = keyBuf.toString();
				User user = CoreData.getInstance().getUser(employee_ID,
						password);
				Map<String, Object> resultObject = new HashMap<String, Object>();
				resultObject.put("MsgObject", msgObject);
				resultObject.put("Object", obj);

				if (user == null) {
					ToastUtils.showToast((BaseActivity)context, context.getResources().getString(R.string.name_pwd_error));
					((TextView) (findViewById(R.id.tv_title)))
							.setText(context.getResources().getString(R.string.manager_id));
					state = STATE_IN_ENTER_ID;
					employee_ID = null;
					password = null;
				}else {
					if (user.getType() != ParamConst.USER_TYPE_MANAGER) {
						ToastUtils.showToast((BaseActivity)context, context.getResources().getString(R.string.permission_insuff));
						((TextView) (findViewById(R.id.tv_title))).setText(context.getResources().getString(R.string.manager_id));
						state = STATE_IN_ENTER_ID;
						employee_ID = null;
						password = null;
					} else {
						((TextView) (findViewById(R.id.tv_title)))
								.setText(context.getResources().getString(R.string.manager_id));
						state = STATE_IN_ENTER_ID;
						employee_ID = null;
						password = null;
						resultObject.put("User", user);
						handler.sendMessage(handler.obtainMessage(DIALOG_RESPONSE, resultObject));
						this.dismiss();
					}
				}
				keyBuf.delete(0, key_len);
				setPassword(keyBuf.length());
			}
		}
	}

	public void show(String msgObject,Object obj) {
		this.msgObject = msgObject;
		this.obj = obj;
		super.show();
	}

	@Override
	public void dismiss() {
		((TextView) (findViewById(R.id.tv_title)))
				.setText(context.getResources().getString(R.string.manager_id));
		state = STATE_IN_ENTER_ID;
		employee_ID = null;
		password = null;
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
