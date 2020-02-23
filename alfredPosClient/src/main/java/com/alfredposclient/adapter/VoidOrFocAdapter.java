package com.alfredposclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alfredposclient.R;

import java.util.ArrayList;
import java.util.List;


public class VoidOrFocAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater inflater;
	private List<String> items = new ArrayList<String>();
	private List<String> canotClick = new ArrayList<String>();
	private boolean[] voidOrFocChoosedFlag;
	private int itemWidth = 160;
	
	public VoidOrFocAdapter(Context context,List<String> items, boolean[] voidOrFocChoosedFlag, List<String> canotClick) {
		super();
		mContext = context;
		inflater = LayoutInflater.from(context);
		this.items = items;
		this.canotClick = canotClick;
		this.voidOrFocChoosedFlag = voidOrFocChoosedFlag;
	}
	
	/**
	 * 更改数据
	 * @param items
	 * @param voidOrFocChoosedFlag
	 */
	public void changeData(List<String> items, boolean[] voidOrFocChoosedFlag, List<String> canotClick){
		this.items = items;
		this.canotClick = canotClick;
		this.voidOrFocChoosedFlag = voidOrFocChoosedFlag;
		notifyDataSetChanged();
	}
	
	public void updateCannotClick(List<String> canotClick){
		this.canotClick = canotClick;
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setItemWidth(int itemWidth){
		this.itemWidth = itemWidth;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.void_or_foc_item_view, null);
			holder = new ViewHolder();
			holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		LayoutParams lp = holder.tv_item.getLayoutParams();
		lp.width = itemWidth;
		holder.tv_item.setLayoutParams(lp);
		
		holder.tv_item.setText(items.get(position));
		
		if (voidOrFocChoosedFlag != null && voidOrFocChoosedFlag[position] ) {
//			holder.tv_item.setBackgroundColor(mContext.getResources().getColor(R.color.title_bg));
			holder.tv_item.setBackgroundResource(R.drawable.discount_gridview_selected_bg);
			holder.tv_item.setTextColor(mContext.getResources().getColor(R.color.white));
		}else{
//			holder.tv_item.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
			holder.tv_item.setBackgroundResource(R.drawable.discount_gridview_default_bg);
			holder.tv_item.setTextColor(mContext.getResources().getColor(R.color.text_light_black));
		}
				
		for (String str : canotClick) {
			if (str.equals(items.get(position))) {
				holder.tv_item.setBackgroundResource(R.drawable.discount_gridview_selected_bg);
				holder.tv_item.setTextColor(mContext.getResources().getColor(R.color.white));
			}
		}
		
		return convertView;
	}
	
	public class ViewHolder {
		public TextView tv_item;
	}
}
