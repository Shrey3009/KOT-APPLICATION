package com.alfredposclient.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alfredbase.BaseActivity;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.global.UIHelp;

public class ManagerWindow {
	private static final int WIDTH = 150;
	private LayoutInflater inflater;
	private Context context;
	private View parentView;

	private View contentView;
	private PopupWindow popupWindow;
	private Handler handler;

	public ManagerWindow(Context context, View parentView) {
		this.context = context;
		this.parentView = parentView;
	}

	@SuppressWarnings("deprecation")
	private void init() {
		contentView = LayoutInflater.from(context).inflate(
				R.layout.manager_window, null);
		inflater = LayoutInflater.from(context);

		popupWindow = new PopupWindow(parentView, WIDTH,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setAnimationStyle(R.style.setting_window_style);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		contentView.findViewById(R.id.tv_switch_user).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				UIHelp.showToast((BaseActivity)context, context.getResources().getString(R.string.switch_account));
				handler.sendMessageDelayed(handler.obtainMessage(MainPage.ACTION_SWITCH_USER), 400);
				dismiss();
			}
		});
	}

	public void show(Handler handler) {
		if(isShowing()){
			return;
		}
		init();
		this.handler = handler;
		popupWindow.showAsDropDown(parentView, -WIDTH + parentView.getWidth()
				/ 2, 10);
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
}
