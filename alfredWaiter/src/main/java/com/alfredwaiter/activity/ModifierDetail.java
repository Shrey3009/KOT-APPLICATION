package com.alfredwaiter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.utils.BH;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.utils.WaiterUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ModifierDetail extends BaseActivity {
	private ItemDetail itemDetail;
	private ArrayList<Modifier> modifierTitel = new ArrayList<Modifier>();
	private List<OrderModifier> orderModifiers = new ArrayList<OrderModifier>();
	private OrderDetail orderDetail;
	private Order order;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_modifier_detail);
		getIntentData();
		initTitle();
		Adapter adapter = new Adapter(this);

		ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.tabPageIndicator);
		indicator.setViewPager(pager);
	}
	
	public void initTitle(){
		findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_title_name)).setText(context.getResources().getString(R.string.add_modifier));
	}
	
	private void getIntentData() {
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
		orderDetail = (OrderDetail) intent.getSerializableExtra("orderDetail");
		itemDetail = (ItemDetail) intent.getSerializableExtra("itemDetail");
		orderModifiers = OrderModifierSQL.getOrderModifiers(order, orderDetail);
		List<ItemModifier> itemModifiers = CoreData.getInstance()
				.getItemModifiers(itemDetail);
		if (!itemModifiers.isEmpty()) {
			for (ItemModifier itemModifier : itemModifiers) {
				modifierTitel.add(CoreData.getInstance().getModifier(
						itemModifier));
			}
		}

	}

	class Adapter extends PagerAdapter {
		private List<View> views;

		public Adapter(Context context) {
			LayoutInflater inflater = LayoutInflater.from(context);
			views = new ArrayList<View>();
			View view = null;
			for (int i = 0; i < modifierTitel.size(); i++) {
				view = inflater.inflate(R.layout.item_modifier, null);
				ListView lv_items = (ListView) view.findViewById(R.id.lv_items);
				lv_items.setAdapter(new ItemListAdapter(context, CoreData
						.getInstance().getModifiers(modifierTitel.get(i))));
				views.add(view);
			}
		}

		@Override
		public int getCount() {
			return modifierTitel.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = views.get(position);
			((ViewPager) container).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return modifierTitel.get(position).getCategoryName();
		}
	}
	
	private Handler handler = new Handler(){
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
	
	protected void handlerClickEvent(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;

		default:
			break;
		}
	};
	
	public void httpRequestAction(int action, Object obj) {
		if(MainPage.TRANSFER_TABLE_NOTIFICATION == action){
			Order mOrder = (Order) obj;
			if(mOrder.getId().intValue() == order.getId().intValue()){
				handler.sendEmptyMessage(MainPage.TRANSFER_TABLE_NOTIFICATION);
			}
		}
	}

	class ItemListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<Modifier> modifiers = new ArrayList<Modifier>();

		public ItemListAdapter() {

		}

		public ItemListAdapter(Context context, List<Modifier> modifiers) {
			inflater = LayoutInflater.from(context);
			this.modifiers = modifiers;
		}

		@Override
		public int getCount() {
			return modifiers.size();
		}

		@Override
		public Object getItem(int arg0) {
			return modifiers.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View currentView, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			if (currentView == null) {
				currentView = inflater.inflate(R.layout.item_modifier_detail,
						null);
				viewHolder = new ViewHolder();
				viewHolder.tv_name = (TextView) currentView
						.findViewById(R.id.tv_name);
				viewHolder.tv_price = (TextView) currentView
						.findViewById(R.id.tv_price);
				viewHolder.checkBox = (CheckBox) currentView
						.findViewById(R.id.checkBox);
				currentView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) currentView.getTag();
			}
			final Modifier modifier = modifiers.get(position);
			OrderModifier orderModifier = getOrderModifier(orderModifiers,
					modifier);
			viewHolder.tv_name.setText(modifier.getModifierName());
			viewHolder.tv_price.setText(App.instance.getCurrencySymbol() + BH.getBD(modifier.getPrice()));
			if (orderModifier != null
					&& orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
				viewHolder.checkBox.setChecked(true);
			} else {
				viewHolder.checkBox.setChecked(false);
			}
			viewHolder.checkBox.setTag(orderModifier);
			viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
							CheckBox view = (CheckBox) arg0;
							OrderModifier orderModifier = (OrderModifier) arg0.getTag();
							if (orderModifier != null) {
								if (orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
									orderModifier
											.setStatus(ParamConst.ORDER_MODIFIER_STATUS_DELETE);
									orderModifier.setUpdateTime(System
											.currentTimeMillis());
									OrderModifierSQL
											.updateOrderModifierForWaiter(orderModifier);
									view.setChecked(false);
								} else {
									orderModifier.setUpdateTime(System
											.currentTimeMillis());
									orderModifier
											.setStatus(ParamConst.ORDER_MODIFIER_STATUS_NORMAL);
									OrderModifierSQL
											.updateOrderModifierForWaiter(orderModifier);
									view.setChecked(true);
								}
							} else {
								orderModifier = new OrderModifier();
								orderModifier.setId(CommonSQL
										.getNextSeq(TableNames.OrderModifier));
								orderModifier.setOrderId(order.getId());
								orderModifier.setOrderDetailId(orderDetail.getId());
								orderModifier
										.setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
								orderModifier.setUserId(order.getUserId());
								orderModifier.setItemId(orderDetail.getItemId());
								orderModifier.setModifierId(modifier.getId());
								orderModifier.setModifierNum(modifier.getQty());
								orderModifier
										.setStatus(ParamConst.ORDER_MODIFIER_STATUS_NORMAL);
								orderModifier.setModifierPrice(modifier.getPrice());
								Long time = System.currentTimeMillis();
								orderModifier.setCreateTime(time);
								orderModifier.setUpdateTime(time);
								OrderModifierSQL.addOrderModifierForWaiter(orderModifier);
								arg0.setTag(orderModifier);
								orderModifiers.add(orderModifier);
								view.setChecked(true);
							}
//						}
//					}).start();
						
				}
			});
			
			return currentView;
		}

		private OrderModifier getOrderModifier(
				List<OrderModifier> orderModifiers, Modifier modifier) {
			for (OrderModifier orderModifier : orderModifiers) {
				if (modifier.getId().intValue() == orderModifier
						.getModifierId().intValue()) {
					return orderModifier;
				}
			}
			return null;

		}

		class ViewHolder {
			public TextView tv_name;
			public TextView tv_price;
			public CheckBox checkBox;
		}
	}
	
}
