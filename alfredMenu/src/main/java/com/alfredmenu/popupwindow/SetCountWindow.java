package com.alfredmenu.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alfredbase.global.BugseeHelper;
import com.alfredmenu.view.MoneyKeyboard;
import com.alfredmenu.R;

public class SetCountWindow implements MoneyKeyboard.KeyBoardClickListener {
	private LayoutInflater inflater;
	private Context context;
	private View parentView;

	private View contentView;
	private PopupWindow popupWindow;

	private DismissCall dismissCall;
	private int originCount;

	public SetCountWindow(Context context, View parentView) {
		this.context = context;
		this.parentView = parentView;
	}

	private void init() {
		inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.set_count_window, null);
		MoneyKeyboard moneyKeyboard = (MoneyKeyboard) contentView
				.findViewById(R.id.countKeyboard);
		moneyKeyboard.setMoneyPanel(View.GONE);
		moneyKeyboard.setKeyBoardClickListener(this);
		popupWindow = new PopupWindow(parentView,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setAnimationStyle(R.style.set_qty_window_style);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
	}

	public void show(int originCount, DismissCall dismissCall) {
		this.originCount = originCount;
		this.dismissCall = dismissCall;
		init();
		popupWindow.showAsDropDown(parentView, parentView.getWidth() / 2, 10);
//		popupWindow.showAtLocation(parentView, Gravity.CENTER, 0,0);  
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

	public interface DismissCall {
		public void call(String key, int num);
	}

	StringBuffer buffer = new StringBuffer();

	@Override
	public void onKeyBoardClick(String key) {
		BugseeHelper.buttonClicked(key);
		if ("Cancel".equals(key)) {
			dismiss();
			dismissCall.call(key, originCount);
		} else if ("Enter".equals(key)) {
			dismiss();
			if (buffer.length() > 0) {
				dismissCall.call(key, Integer.parseInt(buffer.toString()));
			} else {
				dismissCall.call(key, originCount);
			}
		} else if ("Clear".equals(key)) {
			buffer.delete(0, buffer.length());
			dismissCall.call(key, 0);
		} else {
			if (buffer.length() < 4) {
				buffer.append(key);
			}
			dismissCall.call(key, Integer.parseInt(buffer.toString()));
		}
	}
}
