package com.alfredwaiter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.javabean.ItemCategoryAndDetails;
import com.alfredwaiter.popupwindow.SetItemCountWindow;
import com.alfredwaiter.view.CountView;
import com.alfredwaiter.view.CountView.OnCountChange;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class OrderAdapter extends BaseExpandableListAdapter {
	private List<ItemCategoryAndDetails> itemCategoryAndDetailsList = null;
	private Context mContext = null;
	private LayoutInflater inflater;
	private Handler handler;
	private List<OrderDetail> orderDetails;
	private int currentGroupId;
	private Order currentOrder;
	private TextTypeFace textTypeFace = TextTypeFace.getInstance();
	private SetItemCountWindow setItemCountWindow;
	private OnCountChange onCountChange;
	private DisplayImageOptions options;

	public OrderAdapter(Context context, List<ItemCategoryAndDetails> tables,
			Handler handler,SetItemCountWindow setItemCountWindow, OnCountChange onCountChange) {
		this.itemCategoryAndDetailsList = tables;
		mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//显示图片的配置  
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.default_itemmenu)
				.showImageForEmptyUri(R.drawable.default_itemmenu)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		this.handler = handler;
		this.setItemCountWindow = setItemCountWindow;
		this.onCountChange = onCountChange;
	}

	public void setParams(Order currentOrder,List<OrderDetail> orderDetails, int currentGroupId) {
		this.currentOrder = currentOrder;
		this.orderDetails = orderDetails;
		this.currentGroupId = currentGroupId;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return itemCategoryAndDetailsList.get(groupPosition).getItemDetails()
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ItemDetail itemDetail = itemCategoryAndDetailsList.get(groupPosition)
				.getItemDetails().get(childPosition);
		View view = inflater.inflate(R.layout.item_item_detail, null);
//		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_item);
//		linearLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
////				OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
////						currentOrder, itemDetail, currentGroupId,
////						ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
////				uihelp.startorderdetail((baseactivity)mcontext, currentorder, itemdetail,
////						orderDetail, currentGroupId);
//			}
//		});
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText(itemDetail.getItemName());
		textTypeFace.setTrajanProRegular(tv_name);
		TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
		tv_price.setText(App.instance.getCurrencySymbol() + itemDetail.getPrice());
		textTypeFace.setTrajanProRegular(tv_price);
		ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
		String url = itemDetail.getImgUrl();
//		if (!TextUtils.isEmpty(url)) {
			ImageLoader.getInstance().displayImage(url, img_icon, options);
//		}else {
//			img_icon.setBackgroundResource(R.drawable.default_itemmenu);
//		}

		final CountView count_view = (CountView) view
				.findViewById(R.id.count_view);
		count_view.setIsCanClick(getOrderDetailStatus(itemDetail));
		count_view.setInitCount(getItemNum(itemDetail));
		count_view.setTag(itemDetail);
		count_view.setParam(itemDetail,setItemCountWindow);
		count_view.setOnCountChange(onCountChange);
		return view;
	}

	private int getItemNum(ItemDetail itemDetail) {
		int itemNum = 0;
		for (OrderDetail orderDetail : orderDetails) {
			if (orderDetail.getItemId().intValue() == itemDetail.getId()
					.intValue()
					&& orderDetail.getGroupId().intValue() == currentGroupId) {
				itemNum += orderDetail.getItemNum();
			}
		}
		return itemNum;
	}
	
	private boolean getOrderDetailStatus(ItemDetail itemDetail){
		for (OrderDetail orderDetail : orderDetails) {
			if (orderDetail.getItemId().intValue() == itemDetail.getId()
					.intValue()
					&& orderDetail.getGroupId().intValue() == currentGroupId) {
				if(orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD){
					return false;
				}else{
					return true;
				}
			}
		}
		return true;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return itemCategoryAndDetailsList.get(groupPosition).getItemDetails()
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return itemCategoryAndDetailsList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return itemCategoryAndDetailsList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		view = inflater.inflate(R.layout.kot_notification_listview, null);
		ItemCategoryAndDetails itemCategoryAndDetails = itemCategoryAndDetailsList
				.get(groupPosition);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(itemCategoryAndDetails.getItemCategory()
				.getItemCategoryName());
		title.setTextColor(parent.getResources().getColor(R.color.black));
		textTypeFace.setTrajanProBlod(title);
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

}
