package com.alfredmenu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.alfredbase.javabean.model.KotNotification;
import com.alfredbase.javabean.model.TableAndKotNotificationList;
import com.alfredmenu.R;
public class KOTNotificationAdapter extends BaseExpandableListAdapter {
	private List<TableAndKotNotificationList> notifications = new ArrayList<TableAndKotNotificationList>();
	private Context mContext = null;

	public KOTNotificationAdapter(Context context,
			List<TableAndKotNotificationList> notifications) {
		this.notifications = notifications;
		mContext = context;
	}
	public void setData(List<TableAndKotNotificationList> notifications){
		this.notifications = notifications;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// return tables.get(groupPosition).getDish(childPosition);
		return notifications.get(groupPosition).getKotNotifications()
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.kot_notification_childitem, null);
		}
		TextView tv_child_name = (TextView) view
				.findViewById(R.id.tv_child_name);
		TextView tv_child_num = (TextView) view.findViewById(R.id.tv_child_num);
		tv_child_name.setText(notifications.get(groupPosition)
				.getKotNotifications().get(childPosition).getItemName());
		tv_child_num.setText(getAllKotNotificationByorderDetailId(notifications.get(groupPosition)
						.getKotNotifications().get(childPosition)));
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// return tables.get(groupPosition).getDishCount();
		return notifications.get(groupPosition).getKotNotifications().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return notifications.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return notifications.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.kot_notification_listview, null);
		}
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(notifications.get(groupPosition).getTableName());
		title.setTextColor(0xFF0000FF);

		if (mHideGroupPos == groupPosition) {
			view.setVisibility(View.INVISIBLE);
		} else {
			view.setVisibility(View.VISIBLE);
		}

		return view;
	}

	private int mHideGroupPos = -1;

	public void hideGroup(int groupPos) {
		mHideGroupPos = groupPos;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public String getAllKotNotificationByorderDetailId(KotNotification kotNotification){
		if (kotNotification == null || kotNotification.getQty().intValue() == 0 
				|| kotNotification.getKotItemNum().intValue() == 0 ) {
			return "";
		}else {
			String result = kotNotification.getQty()+"(" 
					+ (kotNotification.getKotItemNum().intValue()-kotNotification.getUnFinishQty().intValue())+"/"
					+kotNotification.getKotItemNum().intValue() + ")";
			return result;
		}
	}
}
