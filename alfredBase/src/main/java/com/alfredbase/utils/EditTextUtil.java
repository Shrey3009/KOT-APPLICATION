package com.alfredbase.utils;

import java.lang.reflect.Method;

import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class EditTextUtil {

	/**
	 * 自定义键盘的输入操作
	 * @param view
	 * @param key
	 */
	public static void inputAction(EditText view, String key){
		if (TextUtils.isEmpty(view.getText())) {
			view.setText(key);
		}else {
			view.setText(view.getText().toString() + key);
		}
		
		cursor(view);
	}
	
	/**
	 * 控制光标的位置
	 * @param view
	 */
	public static void cursor(EditText view){
		Editable etext = view.getText();
		Selection.setSelection(etext, etext.length());
	}
	
	public static void hideKeybord(Window window, EditText text){
		 if (android.os.Build.VERSION.SDK_INT <= 10) {
			   text.setInputType(InputType.TYPE_NULL);
		} else {
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//			   try {
//				   Class<EditText> cls = EditText.class;
//				   Method setSoftInputShownOnFocus;
//				   setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
//				   setSoftInputShownOnFocus.setAccessible(true);
//				   setSoftInputShownOnFocus.invoke(text, false);
//			   } catch (Exception e) {
//				   e.printStackTrace();
//			   }
		}
	}

	public static String formatLocale(String num) {
		String format = "%0" + num.length() + "d";
		try {
			return String.format(format, Integer.parseInt(num));
		} catch (NumberFormatException e) {
			return num;
		} catch (Exception e) {
			return num;
		}
	}

}
