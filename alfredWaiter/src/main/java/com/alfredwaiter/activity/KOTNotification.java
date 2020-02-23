package com.alfredwaiter.activity;

import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.model.KotNotification;
import com.alfredbase.javabean.model.TableAndKotNotificationList;
import com.alfredwaiter.R;
import com.alfredwaiter.adapter.KOTNotificationAdapter;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.SyncCentre;
import com.alfredwaiter.global.UIHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KOTNotification extends BaseActivity {
	private ExpandableListView expandedListViewEx;
	private KOTNotificationAdapter adapter;
	private List<TableAndKotNotificationList> notifications = new ArrayList<TableAndKotNotificationList>();
	public static final int VIEW_EVENT_COLLECT_KOTITEM = 1;
	public static final int VIEW_EVENT_GET_DATA = 0;
	public static final int VIEW_EVENT_NOTIFICATION = 2;
	private int expand = -1;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_kot_notification);
		loadingDialog = new LoadingDialog(context);
		getData();
		initTitle();
		expandedListViewEx = (ExpandableListView) findViewById(R.id.expandedListViewEx);
		adapter = new KOTNotificationAdapter(context, notifications);
		expandedListViewEx.setGroupIndicator(null);
		expandedListViewEx.setAdapter(adapter);
		for (int i = 0; i < notifications.size(); i++) {
			expandedListViewEx.expandGroup(i);
		}
		expandedListViewEx.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				loadingDialog.show();
//				v.setBackgroundColor(getResources().getColor(
//						R.color.default_line_indicator_selected_color));
				Map<String, Object> parameters = new HashMap<String, Object>();
				KotNotification kotNotification = notifications.get(groupPosition).getKotNotifications().get(childPosition);
				parameters.put("kotNotification", kotNotification);
				SyncCentre.getInstance().handlerCollectKotItem(context,
						parameters, handler);
				return false;
			}
		});

//		expandedListViewEx.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//			@Override
//			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//				TableAndKotNotificationList notificationList = notifications.get(groupPosition);
//				notifications.remove(notificationList);
//				notifications.add(0, notificationList);
//				adapter.setData(notifications);
//				adapter.notifyDataSetChanged();
//
//				for (int i = 0; i < notifications.size(); i++) {
//					if (i == 0){
//						expandedListViewEx.expandGroup(0);
//					}else  {
//						expandedListViewEx.collapseGroup(i);
//					}
//				}
//
////				if (expand == -1){
////					expandedListViewEx.expandGroup(0);
////					expandedListViewEx.setSelectedGroup(0);
////					expand = 0;
////				}else if (expand == 0){
////					expandedListViewEx.collapseGroup(groupPosition);
////					expand = -1;
////				}else {
////					expandedListViewEx.collapseGroup(0);
////					expandedListViewEx.expandGroup(groupPosition);
////					expandedListViewEx.setSelectedGroup(groupPosition);
////					expand = 0;
////				}
//				return true;
//			}
//		});
	}
	
	public void initTitle(){
		findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_title_name)).setText(context.getResources().getString(R.string.notification));
		findViewById(R.id.iv_refresh).setVisibility(View.VISIBLE);
		findViewById(R.id.iv_refresh).setOnClickListener(this);
	}
	
	private void getData() {
		loadingDialog.show();
		Map<String, Object> parameters = new HashMap<String, Object>();
		SyncCentre.getInstance().getKotNotifications(context, parameters, handler);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case VIEW_EVENT_GET_DATA:
				loadingDialog.dismiss();
				notifications = (List<TableAndKotNotificationList>) msg.obj;
				adapter.setData(notifications);
				adapter.notifyDataSetChanged();
				for (int i = 0; i < notifications.size(); i++) {
					expandedListViewEx.expandGroup(i);
				}
				break;
			case VIEW_EVENT_COLLECT_KOTITEM:
				loadingDialog.dismiss();
				break;
			case VIEW_EVENT_NOTIFICATION:
				getData();
				break;
			case ResultCode.CONNECTION_FAILED:
				loadingDialog.dismiss();
				UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable)msg.obj, 
						context.getResources().getString(R.string.revenue_center)));
				break;
			default:
				break;
			}
		};
	};
	@Override
	public void httpRequestAction(int action, Object obj) {
		super.httpRequestAction(action, obj);
		if(action == App.VIEW_EVENT_SET_QTY){
			handler.sendEmptyMessage(VIEW_EVENT_NOTIFICATION);
		}
	}
	
	@Override
	protected void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.iv_refresh:
			getData();
			break;
		default:
			break;
		}
	}
}
