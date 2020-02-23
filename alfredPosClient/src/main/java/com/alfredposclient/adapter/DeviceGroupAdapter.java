package com.alfredposclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredposclient.R;

import java.util.List;

public class DeviceGroupAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	List<Printer> deptDBModelList;
	private int selectIndex;
	public DeviceGroupAdapter(Context context, List<Printer> deptDBModelList) {
		this.context = context;
		this.deptDBModelList = deptDBModelList;
		inflater = LayoutInflater.from(context);
	}
	public void setDeviceGroup(List<Printer> deptDBModelList){
		this.deptDBModelList = deptDBModelList;
		notifyDataSetChanged();
	}
	
	public int getSelectIndex() {
		return selectIndex;
	}
	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}
	@Override
	public int getCount() {
		return deptDBModelList.size();
	}

	@Override
	public Object getItem(int position) {
		return deptDBModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.device_group_item, null);
			holder = new ViewHolder();
			holder.tv_device_group_name = (TextView) convertView.findViewById(R.id.tv_device_group_name);
			holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Printer deptDBModel = deptDBModelList.get(position);
		 Log.d("DeviceGroupAdapter", deptDBModel.getIsLablePrinter()+" ---导航栏名字---"+deptDBModel.getPrinterName());
		if (deptDBModel.getType().intValue() == 1) {
			holder.tv_device_group_name.setText(deptDBModel.getPrinterName());
		}

		if(selectIndex == position){
			holder.iv_select.setVisibility(View.VISIBLE);
			holder.tv_device_group_name.setTextColor(context.getResources().getColor(R.color.black));
		}else{
			holder.iv_select.setVisibility(View.INVISIBLE);
			holder.tv_device_group_name.setTextColor(context.getResources().getColor(R.color.black));
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_device_group_name;
		ImageView iv_select;
	}
}
