package com.alfredposclient.popupwindow;

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

import com.alfredbase.javabean.TableInfo;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;

import java.util.Collections;
import java.util.List;

public class SelectBillWindow {
	private LayoutInflater inflater;
	private Context context;
	private View parentView;
	private static final int WIDTH = 150;
	private View contentView;
	private PopupWindow popupWindow;
	private Handler handler;
	private List<TableInfo> tableList = Collections.emptyList();

	public SelectBillWindow(Context context, View parentView, Handler handler) {
		this.context = context;
		this.parentView = parentView;
		this.handler = handler;
	}

	private void init() {
		inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.select_bill_window, null);
		ListView lv_group = (ListView) contentView.findViewById(R.id.lv_group);
		lv_group.setAdapter(new SelectGroupAdapter());
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				handler.sendMessage(handler.obtainMessage(
						MainPage.VIEW_EVENT_SELECT_BILL, tableList.get(arg2)));
				dismiss();

			}
		});
		popupWindow = new PopupWindow(parentView, WIDTH,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setAnimationStyle(R.style.setting_window_style);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	public void show(List<TableInfo> tableList) {
		if(isShowing()){
			return;
		}
		this.tableList = tableList; 
		init();
		popupWindow.showAsDropDown(parentView, -WIDTH + parentView.getWidth()/2, 10);
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
			return tableList.size();
		}

		@Override
		public Object getItem(int position) {
			return tableList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.item_select_bill, null);
				holder = new ViewHolder();
				holder.tv_text = (TextView) convertView
						.findViewById(R.id.tv_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
				holder.tv_text.setText(tableList.get(position).getName());
			return convertView;
		}

		class ViewHolder {
			public TextView tv_text;
		}

	}
}
