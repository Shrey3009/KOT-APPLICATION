package com.alfredposclient.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;

public class RevenueCentreListAdapter extends BaseAdapter {
	private Context context;

	private List<RevenueCenter> revenueCentres = new ArrayList<RevenueCenter>();

	private LayoutInflater inflater;
	
	private TextTypeFace textTypeFace = TextTypeFace.getInstance();

	public RevenueCentreListAdapter() {

	}

	public RevenueCentreListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		for (RevenueCenter revenueCenter : CoreData.getInstance()
				.getRevenueCenters()) {
			revenueCentres.add(revenueCenter);
		}
	}

	@Override
	public int getCount() {
		return revenueCentres.size();
	}

	@Override
	public Object getItem(int arg0) {
		return revenueCentres.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.item_revenue_centre, null);
			holder = new ViewHolder();
			holder.tv_text = (TextView) arg1.findViewById(R.id.tv_text);
			holder.tv_revenue_center_name = (TextView) arg1.findViewById(R.id.tv_revenue_center_name);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		holder.tv_text.setText((arg0+1)+"");
		holder.tv_revenue_center_name.setText(revenueCentres.get(arg0).getRevName());
		textTypeFace.setTrajanProRegular(holder.tv_text);
		textTypeFace.setTrajanProRegular(holder.tv_revenue_center_name);
		return arg1;
	}

	class ViewHolder {
		public TextView tv_text;
		public TextView tv_revenue_center_name;
	}

}
