package com.alfredselfhelp.popuwindow;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;
import com.alfredbase.view.Numerickeyboard.KeyBoardClickListener;
import com.alfredselfhelp.R;
import com.alfredselfhelp.utils.UIHelp;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectPrintWindow implements KeyBoardClickListener, OnFocusChangeListener{
	private PopupWindow popupWindow;
	
	private LayoutInflater inflater;
	private BaseActivity context;
	private View parentView;
	private View contentView;
	private String assignToName;
	private String js_callback;
	private String printerName;
	private String deviceName;
	private Handler handler;
	
	private RadioGroup rg_printerName;
	private RadioButton rb_TM_T81,rb_TM_U220;
	private TextView tv_enter,tv_cancel;
	private Numerickeyboard ipkeyboard;
	private EditText et_ip1,et_ip2,et_ip3,et_ip4;
	private TextTypeFace textTypeFace;
	public static final int ACTION_ASSIGN_PRINTER_DEVICE = 10053;
	public SelectPrintWindow(BaseActivity context, View parentView, Handler handler) {
		this.context = context;
		this.parentView = parentView;
		this.handler = handler;
	}
	
	private void init(){
		inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.print_keyboard, null);
		
		rg_printerName = (RadioGroup) contentView.findViewById(R.id.rg_printerName);
		rb_TM_T81 = (RadioButton) contentView.findViewById(R.id.rb_TM_T81);
		deviceName = "EPSON TM-T81";
		rb_TM_U220 = (RadioButton) contentView.findViewById(R.id.rb_TM_U220);
		rg_printerName.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb_TM_T81.getId()) {
					deviceName = "EPSON TM-T81";
				}else {
					deviceName = "EPSON TM-U220";
				}
			}
		});
		tv_enter = (TextView) contentView.findViewById(R.id.tv_enter);
		tv_enter.setOnClickListener(myOnClickListener);
		tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
		tv_cancel.setOnClickListener(myOnClickListener);
		
		ipkeyboard = (Numerickeyboard) contentView.findViewById(R.id.ipkeyboard);
		ipkeyboard.setKeyBoardClickListener(this);
		ipkeyboard.setParams(context);
		et_ip1 = (EditText) contentView.findViewById(R.id.et_ip1);
		et_ip2 = (EditText) contentView.findViewById(R.id.et_ip2);
		et_ip3 = (EditText) contentView.findViewById(R.id.et_ip3);
		et_ip4 = (EditText) contentView.findViewById(R.id.et_ip4);
		et_ip1.setOnFocusChangeListener(this);
		et_ip2.setOnFocusChangeListener(this);
		et_ip3.setOnFocusChangeListener(this);
		et_ip4.setOnFocusChangeListener(this);
		et_ip1.setInputType(InputType.TYPE_NULL);
		et_ip2.setInputType(InputType.TYPE_NULL);
		et_ip3.setInputType(InputType.TYPE_NULL);
		et_ip4.setInputType(InputType.TYPE_NULL);
		et_ip1.requestFocus();
		
		popupWindow = new PopupWindow(parentView, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		popupWindow.setAnimationStyle(R.style.Dialog_verify);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		initTextTypeFace(contentView);
	}

	private void initTextTypeFace(View view) {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular((TextView)view.findViewById(R.id.tv_printer_title));
		textTypeFace.setTrajanProRegular(rb_TM_T81);
		textTypeFace.setTrajanProRegular(rb_TM_U220);
		textTypeFace.setTrajanProRegular((TextView)view.findViewById(R.id.tv_print_ip));
		textTypeFace.setTrajanProRegular(tv_enter);
		textTypeFace.setTrajanProRegular(tv_cancel);
		textTypeFace.setTrajanProRegular(et_ip1);
		textTypeFace.setTrajanProRegular(et_ip2);
		textTypeFace.setTrajanProRegular(et_ip3);
		textTypeFace.setTrajanProRegular(et_ip4);
	}

	private OnClickListener myOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_enter:
				Map<String, String> result = new HashMap<String, String>();
				result.put("printerName", printerName);
				result.put("deviceName", deviceName);
				result.put("assignTo", assignToName);
				result.put("js_callback", js_callback);
				if (verifyIp(getInputedIP())) {
//					if(context instanceof SystemSetting){
//						handler.sendMessage(handler.obtainMessage(ACTION_ASSIGN_PRINTER_DEVICE,getInputedIP()));
//					}else{
						result.put("printerIp", getInputedIP());
						Gson gson = new Gson();
						String str = gson.toJson(result);
						handler.sendMessage(handler.obtainMessage(ACTION_ASSIGN_PRINTER_DEVICE,str));
//					}
					dismiss();
				}else {
					UIHelp.showToast(context, context.getResources().getString(R.string.invalid_ip));
					return;
				}
				break;
			case R.id.tv_cancel:
				popupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	public void show(String assignToName,String js_callback) {
		if(isShowing()){
			return;
		}
		this.js_callback = js_callback;
		this.assignToName = assignToName;
		init();
		popupWindow.showAtLocation(parentView,Gravity.CENTER_HORIZONTAL |Gravity.CENTER_VERTICAL, 
				0,0);
	}

	public void show(String printerName) {
		if(isShowing()){
			return;
		}
		this.printerName = printerName;
		init();
		popupWindow.showAtLocation(parentView,Gravity.CENTER_HORIZONTAL |Gravity.CENTER_VERTICAL,
				0,0);
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
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.et_ip1:
			if (hasFocus) {
				et_ip1.setBackgroundColor(context.getResources().getColor(R.color.gray));
			} else {
				et_ip1.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			break;
		case R.id.et_ip2:
			if (hasFocus) {
				et_ip2.setBackgroundColor(context.getResources().getColor(R.color.gray));
			} else {
				et_ip2.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			break;
		case R.id.et_ip3:
			if (hasFocus) {
				et_ip3.setBackgroundColor(context.getResources().getColor(R.color.gray));
			} else {
				et_ip3.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			break;
		case R.id.et_ip4:
			if (hasFocus) {
				et_ip4.setBackgroundColor(context.getResources().getColor(R.color.gray));
			} else {
				et_ip4.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onKeyBoardClick(String key) {
		if (TextUtils.isEmpty(key)){
			return;
		}
		BugseeHelper.buttonClicked(key);
		EditText tempEditText = getFocusView();
		if (tempEditText == null)
			return;
		if (key.equals("X")) {
			String content = tempEditText.getText().toString();
			if (content.length() > 0) {
				tempEditText
						.setText(content.substring(0, content.length() - 1));
			} else {
				setBeforeFocusView();
			}
		} else {
			tempEditText.setText(tempEditText.getText().toString() + key);
			if (tempEditText.getText().toString().length() >= 3)
				setNextFocusView();
		}
	}
	
	private EditText getFocusView() {
		if (et_ip1.hasFocus())
			return et_ip1;
		if (et_ip2.hasFocus())
			return et_ip2;
		if (et_ip3.hasFocus())
			return et_ip3;
		if (et_ip4.hasFocus())
			return et_ip4;
		return null;
	}
	
	private void setBeforeFocusView() {
		if (et_ip2.hasFocus()) {
			et_ip1.requestFocus();
		} else if (et_ip3.hasFocus()) {
			et_ip2.requestFocus();
		} else if (et_ip4.hasFocus()) {
			et_ip3.requestFocus();
		}
	}
	
	private void setNextFocusView() {
		if (et_ip1.hasFocus()) {
			et_ip2.requestFocus();
		} else if (et_ip2.hasFocus()) {
			et_ip3.requestFocus();
		} else if (et_ip3.hasFocus()) {
			et_ip4.requestFocus();
		}
	}
	
	private String getInputedIP() {
		String ipAddress = et_ip1.getText().toString()+"."
	                +et_ip2.getText().toString()+"."
	                +et_ip3.getText().toString()+"."
	                +et_ip4.getText().toString();
		return ipAddress;
	}
	
	public boolean verifyIp(String inputIp){
		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
	    Pattern pattern = Pattern.compile(ip);   
	    Matcher matcher = pattern.matcher(inputIp);   
	    if (matcher.matches()) {
	    	return true;
		} else {
			return false;
		} 
	}
	
}
