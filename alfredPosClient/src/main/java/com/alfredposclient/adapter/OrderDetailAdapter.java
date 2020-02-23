package com.alfredposclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends BaseAdapter{
	private List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
	private LayoutInflater inflater;
	private BaseActivity context;
	private TextTypeFace textTypeFace;
	private boolean isShowCheckBox = false;
	private List<OrderDetail> selectedOrderDetaliList = new ArrayList<OrderDetail>();
	private VoidItemCallBack voidItemCallBack;
	public OrderDetailAdapter(BaseActivity context, List<OrderDetail> orderDetailList, VoidItemCallBack voidItemCallBack) {
		this.context = context;
		this.orderDetailList = orderDetailList;
		inflater = LayoutInflater.from(context);
		textTypeFace = TextTypeFace.getInstance();
		this.voidItemCallBack = voidItemCallBack;
	}
	
	public void setList(List<OrderDetail> orderDetailList){
		this.orderDetailList = orderDetailList;
	}
	
	@Override
	public int getCount() {
		return orderDetailList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return orderDetailList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	
		
	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (currentView == null) {
			currentView = inflater.inflate(R.layout.close_orderdetail_item, null);
			holder = new ViewHolder();
			holder.tv_item_name = (TextView) currentView.findViewById(R.id.tv_item_name);
			holder.tv_item_price = (TextView) currentView.findViewById(R.id.tv_item_price);
			holder.tv_item_qty = (TextView) currentView.findViewById(R.id.tv_item_qty);
			holder.tv_item_total = (TextView) currentView.findViewById(R.id.tv_item_total);
			holder.btn_void_item = (Button) currentView.findViewById(R.id.btn_void_item);
			
			textTypeFace.setTrajanProRegular(holder.tv_item_name);
			textTypeFace.setTrajanProRegular(holder.tv_item_price);
			textTypeFace.setTrajanProRegular(holder.tv_item_qty);
			textTypeFace.setTrajanProRegular(holder.tv_item_total);
			currentView.setTag(holder);
		} else {
			holder = (ViewHolder) currentView.getTag();
		}
		final OrderDetail orderDetail = this.orderDetailList
				.get(position);
		holder.tv_item_name.setText(orderDetail.getItemName());
		holder.tv_item_price.setText(BH.formatMoney(orderDetail.getItemPrice()).toString());
		holder.tv_item_qty.setText(String.valueOf(orderDetail.getItemNum().intValue()));
		holder.tv_item_total.setText(BH.formatMoney(orderDetail.getRealPrice()).toString());
		if(isShowCheckBox){
			holder.btn_void_item.setVisibility(View.VISIBLE);
		}else{
			holder.btn_void_item.setVisibility(View.INVISIBLE);
		}
		holder.btn_void_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				voidItemCallBack.callBack(orderDetail);
			}
		});
		return currentView;
	}

	public List<OrderDetail> getSelectOrderDetaliList(){
		return  selectedOrderDetaliList;
	}

	public void clearSelected(){
		selectedOrderDetaliList.clear();
	}

	public void setIsShowCheckBox(boolean isShowCheckBox){
		this.isShowCheckBox = isShowCheckBox;
	}
	
	class ViewHolder{
		public TextView tv_item_name;
		public TextView tv_item_price;
		public TextView tv_item_qty;
		public TextView tv_item_total;
		public Button btn_void_item;
	}

	public interface VoidItemCallBack{
		void callBack(OrderDetail orderDetail);
	}
	
}
