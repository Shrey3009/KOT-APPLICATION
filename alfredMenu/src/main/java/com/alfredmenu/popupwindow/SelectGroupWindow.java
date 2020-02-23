package com.alfredmenu.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alfredmenu.activity.OrderDetailsTotal;
import com.alfredmenu.R;

public class SelectGroupWindow {
	private LayoutInflater inflater;
	private Context context;
	private View parentView;

	private View contentView;
	private PopupWindow popupWindow;
	private Handler handler;
	private int groupCount;

	public SelectGroupWindow(Context context, View parentView, Handler handler) {
		this.context = context;
		this.parentView = parentView;
		this.handler = handler;
	}

	private void init() {
		inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.select_group_window, null);
		ListView lv_group = (ListView) contentView.findViewById(R.id.lv_group);
		lv_group.setAdapter(new SelectGroupAdapter());
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				handler.sendMessage(handler.obtainMessage(
						OrderDetailsTotal.VIEW_EVENT_SELECT_GROUP, arg2 - 1));
				dismiss();

			}
		});
		popupWindow = new PopupWindow(parentView, 100,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setAnimationStyle(R.style.setting_window_style);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	public void show(int groupCount) {
		this.groupCount = groupCount + 2;
		init();
		popupWindow.showAsDropDown(parentView, parentView.getWidth() / 2, 10);
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

	class SelectGroupAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return groupCount;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.item_select_group, null);
				holder = new ViewHolder();
				holder.tv_text = (TextView) convertView
						.findViewById(R.id.tv_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.tv_text.setText(context.getResources().getString(R.string.all));
			} else if (position == 1){
				holder.tv_text.setText("?");
			} else {
				holder.tv_text.setText((position - 1) + "");
			}
			return convertView;
		}

		class ViewHolder {
			public TextView tv_text;
		}

	}
}
