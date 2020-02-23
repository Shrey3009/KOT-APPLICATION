package com.alfredbase.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.R;

public class ToastUtils {

	public static void showToast(BaseActivity context, String text){
		Toast toast = new Toast(context);
		LayoutInflater inflater = context.getLayoutInflater();
		View view = inflater.inflate(R.layout.toast_view, null);
		TextView tv_toast_view = (TextView) view.findViewById(R.id.tv_toast_view);
		tv_toast_view.setText(text);
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular(tv_toast_view);
		toast.setGravity(Gravity.BOTTOM, 0, 10);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
}
