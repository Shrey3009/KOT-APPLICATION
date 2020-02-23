package com.alfredposclient.popupwindow;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.SlideExpandable.AbstractSlideExpandableListAdapter;
import com.SlideExpandable.SlideExpandableListView;
import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ColorUtils;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.view.RingTextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderSplitPrintWindow implements OnClickListener {
	private PopupWindow popupWindow;
	private LayoutInflater inflater;
	private BaseActivity context;
	private View parentView;
	private View contentView;
	private Handler handler;
	private Order order;
	private List<OrderDetail> orderDetails;
	private TextTypeFace textTypeFace;
	private TextView tv_split_print;
	private TextView tv_print;
	private SlideExpandableListView lv_order;
	private OrderAdapter orderAdapter;
	private ImageView iv_back;

	public OrderSplitPrintWindow(BaseActivity context, View parentView,
			Handler handler) {
		this.context = context;
		this.parentView = parentView;
		this.handler = handler;
		init();
	}

	private void init() {
		inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.ordersplit_printbill_window,
				null);
		popupWindow = new PopupWindow(parentView,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		popupWindow.setAnimationStyle(R.style.allBottomInOutStyle);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		tv_split_print = (TextView) contentView
				.findViewById(R.id.tv_split_print);
		tv_print = (TextView) contentView.findViewById(R.id.tv_print);
		lv_order = (SlideExpandableListView) contentView
				.findViewById(R.id.lv_order);
		iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
		tv_split_print.setOnClickListener(this);
		tv_print.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		initTextTypeFace();
	}

	private void initTextTypeFace() {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_title));
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_name_title));
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_price_title));
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_qry_title));
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_sutotal_title));
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_discount_title));
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_total_title));
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_remind));
		textTypeFace.setTrajanProRegular(tv_split_print);
		textTypeFace.setTrajanProRegular(tv_print);

	}

	public void show(BaseActivity context, Order order, Handler handler) {
		if (isShowing()) {
			return;
		}
		this.context = context;
		this.order = order;
		this.orderDetails = OrderDetailSQL.getOrderDetails(order.getId());
		this.handler = handler;
		if (orderAdapter != null) {
			orderAdapter.notifyDataSetChanged();
		} else {
			orderAdapter = new OrderAdapter();
			lv_order.setAdapter(orderAdapter);
		}
		refreshPrintButton();
		popupWindow.showAtLocation(parentView, Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL, 0, 0);
		App.instance.orderInPayment = order;
	}

	private void refresh() {
		refreshPrintButton();
		orderDetails = OrderDetailSQL.getOrderDetails(order.getId());
		orderAdapter.notifyDataSetChanged();
	}
	
	@SuppressWarnings("deprecation")
	private void refreshPrintButton(){
		int orderDetailCount = OrderDetailSQL.getOrderDetailCountByGroupId(
				ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID, order.getId());
		if(orderDetailCount < orderDetails.size()){
			tv_print.setOnClickListener(null);
			tv_print.setBackgroundResource(R.drawable.print_btn);
		}else{
			tv_print.setOnClickListener(this);
			tv_print.setBackgroundResource(R.drawable.print_btn_selector);
		}
	}

	class OrderAdapter extends AbstractSlideExpandableListAdapter {

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
				arg1 = inflater.inflate(R.layout.item_order_split, null);
				holder = new ViewHolder();
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.price = (TextView) arg1.findViewById(R.id.price);
				holder.tv_qty = (TextView) arg1.findViewById(R.id.tv_qty);
				holder.subtotal = (TextView) arg1.findViewById(R.id.subtotal);
				holder.discount = (TextView) arg1.findViewById(R.id.discount);
				holder.total = (TextView) arg1.findViewById(R.id.total);
				holder.modifier = (TextView) arg1
						.findViewById(R.id.tv_modifier);
				holder.specialInstract = (TextView) arg1
						.findViewById(R.id.tv_special_instract);
				holder.rv_split = (RingTextView) arg1
						.findViewById(R.id.rv_split);
				holder.ll_order_detail = (LinearLayout) arg1
						.findViewById(R.id.ll_order_detail);
				textTypeFace.setTrajanProRegular(holder.name);
				textTypeFace.setTrajanProRegular(holder.price);
				textTypeFace.setTrajanProRegular(holder.tv_qty);
				textTypeFace.setTrajanProRegular(holder.subtotal);
				textTypeFace.setTrajanProRegular(holder.discount);
				textTypeFace.setTrajanProRegular(holder.total);
				textTypeFace.setTrajanProRegular(holder.modifier);
				textTypeFace.setTrajanProRegular(holder.specialInstract);
				holder.ll_split = (LinearLayout) arg1
						.findViewById(R.id.ll_split);
				holder.gv_person_index = (GridView) arg1
						.findViewById(R.id.gv_person_index);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			final OrderDetail orderDetail = orderDetails.get(arg0);
			String modifiers = getItemModifiers(orderDetail);
			
			ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
					orderDetail.getItemId(),orderDetail.getItemName());
			if (modifiers != null) {
				holder.modifier.setText(modifiers);
			}
			holder.specialInstract
					.setText(orderDetail.getSpecialInstractions());
			holder.name.setText(itemDetail.getItemName());
			holder.price.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(orderDetail.getItemPrice()));
			if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
			{
				String textToSet = String.valueOf(BH.getBD(orderDetail.getItemPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
				holder.price.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
			}
			holder.tv_qty.setText(orderDetail.getItemNum() + "");
			holder.tv_qty.setBackgroundColor(context.getResources().getColor(
					R.color.white));
			holder.tv_qty.setTag(orderDetail);
			holder.subtotal.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(orderDetail.getRealPrice()));
			if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
			{
				String textToSet = String.valueOf(BH.getBD(orderDetail.getRealPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
				holder.subtotal.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
			}

			// 以下计算过程应该是不需要的，数据库的total数据是准确的，但是还没有时间测试
			if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB
			    || orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB
				|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB)
			{
				if (orderDetail.getOrderDetailType().intValue() == ParamConst.ORDERDETAIL_TYPE_FREE)
				{
					holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
					holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
					if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
					{
						String textToSet = String.valueOf(BH.getBD(ParamConst.DOUBLE_ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
						holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
						holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
					}
				}
				else
				{
					holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(orderDetail.getDiscountPrice()));
					holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol()
							+ BH.sub(BH.getBD(orderDetail.getRealPrice()),
									BH.getBD(orderDetail.getDiscountPrice()),
									true).toString());
					if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
					{
						String textToSet = String.valueOf(BH.getBD(orderDetail.getDiscountPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
						holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
						textToSet = String.valueOf(BH.sub(BH.getBD(orderDetail.getRealPrice()), BH.getBD(orderDetail.getDiscountPrice()),
								true).setScale(2, BigDecimal.ROUND_HALF_UP));
						holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
					}
				}
			}
			else if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
			            || orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
					    || orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE)
			{
				BigDecimal discount = BH.mul(
						BH.getBD(orderDetail.getRealPrice()),
						BH.getBDNoFormat(orderDetail.getDiscountRate()), true);

				if (orderDetail.getOrderDetailType().intValue() == ParamConst.ORDERDETAIL_TYPE_FREE)
				{
					holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
					holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
					if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
					{
						String textToSet = String.valueOf(BH.getBD(ParamConst.DOUBLE_ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
						holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
						holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
					}
				}
				else
				{
					holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(discount).toString());
					holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol()
							+ BH.sub(BH.getBD(orderDetail.getRealPrice()),
									discount, true));
					if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
					{
						String textToSet = String.valueOf(BH.getBD(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
						holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
						textToSet = String.valueOf(BH.sub(BH.getBD(orderDetail.getRealPrice()),
								discount, true).setScale(2, BigDecimal.ROUND_HALF_UP));
						holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
					}
				}
			}
			else
			{
				holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
				if (orderDetail.getOrderDetailType().intValue() == ParamConst.ORDERDETAIL_TYPE_FREE)
				{
					holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
				}
				else
				{
					holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(orderDetail.getRealPrice()).toString());
				}
				if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
				{
					String textToSet = String.valueOf(BH.getBD(ParamConst.DOUBLE_ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
					holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
					if (orderDetail.getOrderDetailType().intValue() == ParamConst.ORDERDETAIL_TYPE_FREE)
					{
						holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
					}
					else
					{
						textToSet = String.valueOf(BH.getBD(orderDetail.getRealPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
						holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + textToSet);
					}
				}
			}
			if (orderDetail.getGroupId().intValue() > 0) {
				holder.rv_split.setCircleColor(
						context.getResources().getColor(
								ColorUtils.ColorGroup.getColor(orderDetail
										.getGroupId().intValue())), orderDetail
								.getGroupId().intValue());
			} else {
				holder.rv_split.restoreCircleColor();
			}

			holder.ll_order_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					collapseLastOpen();
				}
			});

			final Adapter adapter = new Adapter();
			holder.gv_person_index.setAdapter(adapter);
			holder.rv_split.setTag(adapter);
			enableFor(arg1, holder.rv_split, holder.ll_split, null, arg0, arg2);
			holder.gv_person_index.setTag(holder.rv_split);
			holder.gv_person_index
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							if (arg2 == 0) {
								if (adapter.getPageIndex() == 0) {
									if (orderDetail.getGroupId().intValue() > 0) {
										OrderSplit orderSplit = OrderSplitSQL
												.getOrderSplitByOrderAndGroupId(
														order, orderDetail.getGroupId());
										if(orderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED){
											UIHelp.showToast(context, context.getResources().getString(R.string.order_split_)+ 
													orderSplit.getGroupId() + context.getResources().getString(R.string._settled));
											refresh();
											return;
										}
										int count = OrderDetailSQL
												.getOrderDetailCountByGroupId(
														orderDetail.getGroupId(), order.getId());
										((RingTextView) arg0.getTag())
												.restoreCircleColor();
										orderDetail.setGroupId(0);
										orderDetail.setOrderSplitId(0);
										OrderDetailSQL
												.updateOrderDetail(orderDetail);
										OrderDetailTaxSQL.updateOrderSplitIdbyOrderDetail(orderDetail);
										if(count > 1){
											OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
										}else{
											OrderSplitSQL.delete(orderSplit);
											RoundAmountSQL.deleteRoundAmount(orderSplit);
										}
										OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS, order.getId());
										handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
										refresh();
									}
									collapseLastOpen();
								} else {
									adapter.cutPageIndex();
									adapter.notifyDataSetChanged();
								}
							} else if (arg2 == adapter.getCount() - 1) {
								adapter.addPageIndex();
								adapter.notifyDataSetChanged();
							} else {
								int groupId = orderDetail.getGroupId()
										.intValue();
								int index = (arg2 + adapter.getPageIndex() * 6);
								orderDetail.setGroupId(index);
								OrderSplit orderSplit = ObjectFactory
										.getInstance().getOrderSplit(order,
												index, App.instance.getLocalRestaurantConfig()
												.getIncludedTax().getTax());
								if(orderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED){
									UIHelp.showToast(context   , context.getResources().getString(R.string.order_split_) + 
											index + context.getResources().getString(R.string._settled));
									refresh();
									return;
								}
								if (groupId > 0) {
									OrderSplit oldOrderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, groupId);
									if(oldOrderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED){
										UIHelp.showToast(context   , context.getResources().getString(R.string.order_split_) + 
												groupId + context.getResources().getString(R.string._settled));
										refresh();
										return;
									}
									int count = OrderDetailSQL
											.getOrderDetailCountByGroupId(
													groupId, order.getId());
									if (count == 0) {
										OrderSplitSQL
												.deleteOrderSplitByOrderAndGroupId(
														order.getId(), groupId);
										RoundAmountSQL.deleteRoundAmount(oldOrderSplit);
									}
								}
								((RingTextView) arg0.getTag()).setCircleColor(
										context.getResources().getColor(
												ColorUtils.ColorGroup
														.getColor(index)),
										index);
								orderDetail.setOrderSplitId(orderSplit.getId());
								OrderDetailSQL.updateOrderDetail(orderDetail);
								OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
								OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS, order.getId());
								OrderDetailTaxSQL.updateOrderSplitIdbyOrderDetail(orderDetail);
								collapseLastOpen();
								handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
								refresh();
							}

						}
					});

			return arg1;

		}

		class ViewHolder {
			public TextView name;
			public TextView price;
			public TextView tv_qty;
			public TextView subtotal;
			public TextView discount;
			public TextView total;
			public TextView modifier;
			public TextView specialInstract;
			public RingTextView rv_split;
			public GridView gv_person_index;
			public LinearLayout ll_split;
			public LinearLayout ll_order_detail;

		}

		private final class Adapter extends BaseAdapter {
			private int pageIndex = 0;
			private int persons = 6;

			public void addPageIndex() {
				pageIndex++;
			}

			public void cutPageIndex() {
				pageIndex--;
			}

			public int getPageIndex() {
				return pageIndex;
			};

			@Override
			public int getCount() {
				return persons + 2;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				arg1 = inflater.inflate(R.layout.item_group_id, null);
				TextView tv_text = (TextView) arg1.findViewById(R.id.tv_text);
				if (arg0 == 0 && pageIndex == 0) {
					tv_text.setText("?");
				} else if (arg0 == 0) {
					tv_text.setText("...");
				} else if (arg0 == persons + 1) {
					tv_text.setText("...");
				} else {
					tv_text.setText((arg0 + pageIndex * 6) + "");
				}
				textTypeFace.setTrajanProRegular(tv_text);
				return arg1;
			}

		}

		@Override
		public View getExpandToggleButton(View parent) {
			return parent;
		}

		@Override
		public View getExpandableView(View parent) {
			return parent.findViewById(R.id.ll_more);
		}

		@Override
		public void notifyDataSetChanged() {
			collapseLastOpen();
			super.notifyDataSetChanged();
		}

	}

	public String getItemModifiers(OrderDetail orderDetail) {
		String result = "";
		ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
				.getAllOrderModifierByOrderDetailAndNormal(orderDetail);
		for (OrderModifier orderModifier : orderModifiers) {
			Modifier modifier = ModifierSQL.getModifierById(orderModifier
					.getModifierId());
			if (modifier != null) {
				String modifierName = modifier.getModifierName();
				result += modifierName + " ";
			}
		}
		return result;
	}

	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			App.instance.orderInPayment = null;
			popupWindow.dismiss();
		}
	}

	public boolean isShowing() {
		if (popupWindow != null) {
			return popupWindow.isShowing();
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_split_print: {
			if(!ButtonClickTimer.canClick(view)){
				return;
			}
			int orderDetailCount = OrderDetailSQL.getOrderDetailCountByGroupId(
					ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID, order.getId());
			if (orderDetailCount != 0) {
				UIHelp.showToast(context,
						context.getResources().getString(R.string.assign_items_to_group));
			} else {
				PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
						context);
				printerLoadingDialog.setTitle(context.getResources().getString(R.string.bill_printing));
				printerLoadingDialog.showByTime(3000);
				List<OrderSplit> orderSplits = OrderSplitSQL
						.getOrderSplits(order);
				PrinterDevice printer = App.instance.getCahierPrinter();
				BigDecimal orderRoundBalancePrice = BH.getBD(ParamConst.DOUBLE_ZERO);

				for (OrderSplit orderSplit : orderSplits) {
					if(orderSplit.getOrderStatus().intValue() >= ParamConst.ORDERSPLIT_ORDERSTATUS_PAYED) {
						continue;
					}
					OrderBill orderBill = ObjectFactory.getInstance()
							.getOrderBillByOrderSplit(orderSplit,
									App.instance.getRevenueCenter());
					ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) OrderDetailSQL
							.getOrderDetailsByOrderAndOrderSplit(orderSplit);
					if (orderDetails.isEmpty()) {
						continue;
					}
//					RoundAmount orderSplitRoundAmount = ObjectFactory.getInstance()
//							.getRoundAmountByOrderSplit(orderSplit, orderBill, App.instance.getLocalRestaurantConfig().getRoundType(),order.getBusinessDate());
//					OrderHelper.setOrderSplitTotalAlfterRound(orderSplit, orderSplitRoundAmount);
//					orderRoundBalancePrice = BH.add(orderRoundBalancePrice, BH.getBD(orderSplitRoundAmount.getRoundBalancePrice()), true);
					List<Map<String, String>> taxMap = OrderDetailTaxSQL
							.getOrderSplitTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), orderSplit);
					ArrayList<PrintOrderItem> orderItems = ObjectFactory
							.getInstance().getItemList(orderDetails);

					PrinterTitle title = ObjectFactory.getInstance()
							.getPrinterTitleByOrderSplit(
									App.instance.getRevenueCenter(),
									order,
									orderSplit,
									App.instance.getUser().getFirstName()
											+ App.instance.getUser()
													.getLastName(),
									TableInfoSQL.getTableById(
											orderSplit.getTableId())
											.getName(), orderBill, order.getBusinessDate().toString(), 1);

						orderSplit
								.setOrderStatus(ParamConst.ORDERSPLIT_ORDERSTATUS_UNPAY);
						OrderSplitSQL.update(orderSplit);
					ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
							.getInstance().getItemModifierListByOrderDetail(
									orderDetails);
					Order temporaryOrder = new Order();
					temporaryOrder.setPersons(orderSplit.getPersons());
					temporaryOrder.setSubTotal(orderSplit.getSubTotal());
					temporaryOrder.setDiscountAmount(orderSplit.getDiscountAmount());
					temporaryOrder.setTotal(orderSplit.getTotal());
					temporaryOrder.setTaxAmount(orderSplit.getTaxAmount());
					temporaryOrder.setOrderNo(order.getOrderNo());
					BigDecimal remainTotal = BH.getBD(orderSplit.getTotal());
					RoundAmount roundAmount = ObjectFactory.getInstance().getRoundAmount(order, orderBill, remainTotal, App.instance.getLocalRestaurantConfig().getRoundType());
					App.instance.remoteBillPrint(printer, title, temporaryOrder,
							orderItems, orderModifiers, taxMap, null, roundAmount,null);
				}
//				OrderBill orderBill = OrderBillSQL
//						.getOrderBillByOrder(order);
//				RoundAmount orderRoundAmount = ObjectFactory.getInstance()
//						.getRoundAmount(order, orderBill, App.instance.getLocalRestaurantConfig().getRoundType());
//				orderRoundAmount.setRoundBalancePrice(Double.valueOf(orderRoundBalancePrice.toString()));
//				orderRoundAmount.setRoundBeforePrice(order.getTotal());
//				orderRoundAmount.setRoundAlfterPrice(BH.sub(BH.getBD(orderRoundAmount.getRoundBeforePrice()), BH.getBD(orderRoundAmount.getRoundBalancePrice()), true).toString());
//				RoundAmountSQL.update(orderRoundAmount);
//				OrderHelper.setOrderTotalAlfterRound(order, orderRoundAmount);
				order.setOrderStatus(ParamConst.ORDER_STATUS_UNPAY);
				OrderSQL.update(order);
				handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
				dismiss();
			}
			
		}
			break;
		case R.id.tv_print: {
//			OrderBill orderBill = OrderBillSQL
//					.getOrderBillByOrder(order);
//			RoundAmount roundAmount = ObjectFactory.getInstance()
//					.getRoundAmount(order, orderBill, App.instance.getLocalRestaurantConfig().getRoundType());
//			RoundAmountSQL.update(roundAmount);
//			OrderHelper.setOrderTotalAlfterRound(order, roundAmount);
			PrinterDevice printer = App.instance.getCahierPrinter();
			List<Map<String, String>> taxMap = OrderDetailTaxSQL
					.getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), order);

			ArrayList<PrintOrderItem> orderItems = ObjectFactory.getInstance()
					.getItemList(OrderDetailSQL.getOrderDetails(order.getId()));
			PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
					context);
			printerLoadingDialog.setTitle(context.getResources().getString(R.string.bill_printing));
			printerLoadingDialog.showByTime(3000);
			PrinterTitle title = ObjectFactory.getInstance().getPrinterTitle(
					App.instance.getRevenueCenter(),
					order,
					App.instance.getUser().getFirstName()
							+ App.instance.getUser().getLastName(),
					TableInfoSQL.getTableById(order.getTableId()).getName(), 1,App.instance.getSystemSettings().getTrainType());
			order.setOrderStatus(ParamConst.ORDER_STATUS_UNPAY);
			OrderSQL.update(order);
			ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
					.getInstance().getItemModifierList(order, OrderDetailSQL.getOrderDetails(order.getId()));
			App.instance.remoteBillPrint(printer, title, order, orderItems,
					orderModifiers, taxMap, null, null,null);
			handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
			dismiss();
		}
			break;
		case R.id.iv_back:
			dismiss();
			break;
		default:
			break;
		}
	}

}
