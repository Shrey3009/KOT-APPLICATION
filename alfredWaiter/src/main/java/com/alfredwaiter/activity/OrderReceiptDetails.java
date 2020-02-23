package com.alfredwaiter.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.utils.WaiterUtils;

public class OrderReceiptDetails extends BaseActivity {
	private ListView lv_dishes;
	private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
	private Order currentOrder;
	private OrderDetailListAdapter adapter;
	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_order_receipt_details);
		getIntentData();
		lv_dishes = (ListView) findViewById(R.id.lv_dishes);
		adapter = new OrderDetailListAdapter(context);
		lv_dishes.setAdapter(adapter);
		refreshOrderTotal();
	}
	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		
	}
	private void getIntentData() {
		Intent intent = getIntent();
		currentOrder = (Order) intent.getExtras().get("order");
		orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
	}
	private void refreshOrderTotal(){
		((TextView)findViewById(R.id.tv_sub_total)).setText(
				context.getResources().getString(R.string.subtotal)+" : "+ App.instance.getCurrencySymbol() + currentOrder.getSubTotal());
		((TextView)findViewById(R.id.tv_discount)).setText(
				context.getResources().getString(R.string.discount_)+ App.instance.getCurrencySymbol() + currentOrder.getDiscountAmount());
		((TextView)findViewById(R.id.tv_taxes)).setText(
				context.getResources().getString(R.string.taxes)+" : "+ currentOrder.getTaxAmount());
		((TextView)findViewById(R.id.tv_grand_total)).setText(App.instance.getCurrencySymbol() + currentOrder.getTotal());
	}
	
	public void httpRequestAction(int action, Object obj) {
		if (MainPage.TRANSFER_TABLE_NOTIFICATION == action) {
			Order mOrder = (Order) obj;
			if (mOrder.getId().intValue() == currentOrder.getId().intValue()) {
				handler.sendEmptyMessage(MainPage.TRANSFER_TABLE_NOTIFICATION);
			}
		}

	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MainPage.TRANSFER_TABLE_NOTIFICATION:
				WaiterUtils.showTransferTableDialog(context);
				break;
			default:
				break;
			}
		};
	};
	
	class OrderDetailListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public OrderDetailListAdapter() {
		}

		public OrderDetailListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return orderDetails.size();
		}

		@Override
		public Object getItem(int arg0) {
			return orderDetails.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = inflater.inflate(R.layout.item_order_detail, null);
				holder = new ViewHolder();
				holder.ll_title = (LinearLayout) arg1.findViewById(R.id.ll_title);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.price = (TextView) arg1.findViewById(R.id.price);
				holder.tv_qty = (TextView) arg1.findViewById(R.id.tv_qty);
				holder.subtotal = (TextView) arg1.findViewById(R.id.subtotal);
				holder.tv_modifier = (TextView) arg1.findViewById(R.id.tv_modifier);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			final OrderDetail orderDetail = orderDetails.get(arg0);
			ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
					orderDetail.getItemId(),orderDetail.getItemName());

			List<OrderModifier> modifiers = OrderModifierSQL.getAllOrderModifierByOrderDetailAndNormal(orderDetail);
			if (modifiers.size() > 0) {
				holder.tv_modifier.setVisibility(View.VISIBLE);
				StringBuffer stringBuffer = new StringBuffer();
				for (OrderModifier orderModifier : modifiers) {
					Modifier modifier = ModifierSQL.getModifierById(orderModifier.getModifierId());
					stringBuffer = stringBuffer.append(modifier.getModifierName() + "  ");
					holder.tv_modifier.setText(stringBuffer.toString());
				}
			}else {
				holder.tv_modifier.setVisibility(View.GONE);
			}

			holder.tv_qty.setOnClickListener(null);
			holder.tv_qty.setBackgroundColor(context.getResources().getColor(R.color.gray));
			holder.ll_title.setBackgroundColor(context.getResources().getColor(R.color.gray));
			holder.name.setText(itemDetail.getItemName());
			holder.price.setText(App.instance.getCurrencySymbol() + orderDetail.getItemPrice());
			holder.tv_qty.setText(orderDetail.getItemNum() + "");
			holder.tv_qty.setTag(orderDetail);
			holder.subtotal.setText(App.instance.getCurrencySymbol() + orderDetail.getRealPrice());
			return arg1;
		}

		class ViewHolder {
			public TextView name;
			public TextView price;
			public TextView tv_qty;
			public TextView subtotal;
			public LinearLayout ll_title;
			public TextView tv_modifier;
		}
	}
}
