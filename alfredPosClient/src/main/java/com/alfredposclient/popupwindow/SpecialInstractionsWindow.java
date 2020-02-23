package com.alfredposclient.popupwindow;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;

public class SpecialInstractionsWindow implements OnClickListener {
	private static final int WIDTH = 200;
	private BaseActivity context;
	private View parentView;
	private View contentView;
	private PopupWindow popupWindow;
	private Button btn_save;
	private EditText et_special_instractions;
	private OrderDetail currentOrderDetail;
	private Handler handler;
	private InputMethodManager imm;

	public SpecialInstractionsWindow(BaseActivity context, View parentView,
			Handler handler) {
		this.context = context;
		this.parentView = parentView;
		this.handler = handler;
	}

	@SuppressWarnings("deprecation")
	private void init() {
		contentView = LayoutInflater.from(context).inflate(
				R.layout.special_instractions_window, null);
		popupWindow = new PopupWindow(parentView,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
//		popupWindow.setAnimationStyle(R.style.set_qty_window_style);
		popupWindow.setContentView(contentView);
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//		popupWindow.setOutsideTouchable(false);
		popupWindow.setFocusable(true);
		btn_save = (Button) contentView.findViewById(R.id.btn_save);
		et_special_instractions = (EditText) contentView
				.findViewById(R.id.et_special_instractions);
		btn_save.setOnClickListener(this);
		contentView.findViewById(R.id.btn_cancel).setOnClickListener(this);
	}

	public void show(View view, OrderDetail orderDetail) {
		if(isShowing()){
			return;
		}
		init();
		currentOrderDetail = orderDetail;
//		popupWindow.showAsDropDown(view,0,0);
		popupWindow
				.showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
		if(currentOrderDetail.getSpecialInstractions() != null)
			et_special_instractions.setText(currentOrderDetail.getSpecialInstractions());
		popupOutputMethidWindow();
	}
	
	
	private void popupOutputMethidWindow(){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);    
		//得到InputMethodManager的实例  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	public boolean isShowing() {
		if (popupWindow != null) {
			return popupWindow.isShowing();
		}
		return false;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_save:
			final String specialInstractions = et_special_instractions
					.getText().toString();
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (specialInstractions != null) {
						currentOrderDetail
								.setSpecialInstractions(specialInstractions);
						OrderDetailSQL.updateOrderDetail(currentOrderDetail);
						handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
					}
				}
			}).start();
			dismiss();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);    
					//得到InputMethodManager的实例  
					imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken()
							,InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}, 200);
			
			break;
		case R.id.btn_cancel:
			handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
			dismiss();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);    
					//得到InputMethodManager的实例  
					imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken()
							,InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}, 200);
			break;
		default:
			break;
		}
	}

}
