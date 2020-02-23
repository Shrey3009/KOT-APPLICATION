package com.alfredposclient.popupwindow;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.utils.AlertToDeviceSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailFireWindow implements OnClickListener {
	private PopupWindow popupWindow;
	private LayoutInflater inflater;
	private MainPage context;
	private View parentView;
	private View contentView;
	private Handler handler;
	private Order order;
	private List<OrderDetail> orderDetails;
	private List<OrderDetail> selectedOrderDetails;
	private TextTypeFace textTypeFace;
	private RecyclerView lv_order;
	private OrderDetailAdapter orderAdapter;
	private ImageView iv_back;
	private TextView tv_ok;

	public OrderDetailFireWindow(MainPage context, View parentView,
								 Handler handler) {
		this.context = context;
		this.parentView = parentView;
		this.handler = handler;
		init();
	}

	private void init() {
		selectedOrderDetails = new ArrayList<OrderDetail>();
		inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.order_detail_fire_window,
				null);
		popupWindow = new PopupWindow(parentView,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		popupWindow.setAnimationStyle(R.style.allBottomInOutStyle);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		lv_order = (RecyclerView) contentView.findViewById(R.id.lv_order);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		lv_order.setLayoutManager(linearLayoutManager);
		initTextTypeFace();
		iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_ok = (TextView) contentView.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(this);
	}

	private void initTextTypeFace() {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular((TextView) contentView
				.findViewById(R.id.tv_title));

	}

	public void show(Order order, Handler handler) {
		if (isShowing()) {
			return;
		}
		this.order = order;
		this.orderDetails = OrderDetailSQL.getOrderDetailsForFire(order.getId());
		this.selectedOrderDetails.clear();
		this.handler = handler;
		if (orderAdapter != null) {
			orderAdapter.notifyDataSetChanged();
		} else {
			orderAdapter = new OrderDetailAdapter();
			lv_order.setAdapter(orderAdapter);
		}
		popupWindow.showAtLocation(parentView, Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL, 0, 0);
		App.instance.orderInPayment = order;
	}

//	private void refresh() {
//		refreshPrintButton();
//		orderDetails = OrderDetailSQL.getOrderDetails(order.getId());
//		orderAdapter.notifyDataSetChanged();
//	}
	
	@SuppressWarnings("deprecation")
//	private void refreshPrintButton(){
//		int orderDetailCount = OrderDetailSQL.getOrderDetailCountByGroupId(
//				ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID, order.getId());
//		if(orderDetailCount < orderDetails.size()){
//			tv_print.setOnClickListener(null);
//			tv_print.setBackgroundResource(R.drawable.print_btn);
//		}else{
//			tv_print.setOnClickListener(this);
//			tv_print.setBackgroundResource(R.drawable.print_btn_selector);
//		}
//	}

	public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
		class OrderDetailViewHolder extends RecyclerView.ViewHolder{
			TextView tv_name;
			TextView tv_qty;
			CheckBox cb_fire;
			public OrderDetailViewHolder(View itemView) {
				super(itemView);
			}
		}
		@Override
		public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View contextView = LayoutInflater.from(context).inflate(R.layout.item_order_detali_fire,
					parent, false);
			OrderDetailViewHolder viewHolder = new OrderDetailViewHolder(contextView);
			viewHolder.tv_name = (TextView) contextView.findViewById(R.id.tv_name);
			viewHolder.tv_qty = (TextView) contextView.findViewById(R.id.tv_qty);
			viewHolder.cb_fire = (CheckBox) contextView.findViewById(R.id.cb_fire);
			return viewHolder;
		}

		@Override
		public void onBindViewHolder(final OrderDetailViewHolder holder, int position) {
			final OrderDetail orderDetail = orderDetails.get(position);
			holder.tv_name.setText(orderDetail.getItemName());
			holder.tv_qty.setText(orderDetail.getItemNum().toString());
			if(orderDetail.getFireStatus() == 0){
				holder.cb_fire.setVisibility(View.VISIBLE);
			}else{
				holder.cb_fire.setVisibility(View.INVISIBLE);
			}
			if(selectedOrderDetails.contains(orderDetail)){
				holder.cb_fire.setChecked(true);
			}else{
				holder.cb_fire.setChecked(false);
			}
			holder.cb_fire.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox)v;
					if(cb.isChecked()){
						selectedOrderDetails.add(orderDetail);
					}else{
						selectedOrderDetails.remove(orderDetail);
					}
				}
			});
		}


		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public int getItemCount() {
			return orderDetails.size();
		}

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
		case R.id.tv_ok:{
			if(!selectedOrderDetails.isEmpty()){
				new Thread(new Runnable() {
					@Override
					public void run() {
						String kotCommitStatus;
						KotSummary kotSummary = ObjectFactory.getInstance()
								.getKotSummaryForPlace(
										TableInfoSQL.getTableById(
												order.getTableId()).getName(), order,
										App.instance.getRevenueCenter(),
										App.instance.getBusinessDate());
						User user = App.instance.getUser();
						if(user != null){
							String empName = user.getFirstName() + user.getLastName();
							kotSummary.setEmpName(empName);
							KotSummarySQL.updateKotSummaryEmpById(empName, kotSummary.getId().intValue());
						}
						ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
						List<Integer> orderDetailIds = new ArrayList<Integer>();
						ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
						kotCommitStatus = ParamConst.JOB_NEW_KOT;
						for (OrderDetail orderDetail : selectedOrderDetails) {
							if (orderDetail.getOrderDetailStatus() > ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD
									|| orderDetail.getFireStatus() == 1) {
								continue;
							}
							if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
								kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
								KotItemDetail kotItemDetail = ObjectFactory
										.getInstance()
										.getKotItemDetail(
												order,
												orderDetail,
												CoreData.getInstance()
														.getItemDetailById(
																orderDetail.getItemId(), orderDetail.getItemName()),
												kotSummary,
												App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
								kotItemDetail.setFireStatus(1);
								KotItemDetailSQL.update(kotItemDetail);
								kotItemDetails.add(kotItemDetail);
								orderDetailIds.add(orderDetail.getId());
								ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
										.getOrderModifiers(order, orderDetail);
								for (OrderModifier orderModifier : orderModifiers) {
									if (orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
										KotItemModifier kotItemModifier = ObjectFactory
												.getInstance()
												.getKotItemModifier(
														kotItemDetail,
														orderModifier,
														CoreData.getInstance()
																.getModifier(
																		orderModifier
																				.getModifierId()));
										KotItemModifierSQL.update(kotItemModifier);
										kotItemModifiers.add(kotItemModifier);
									}
								}
							}
							OrderDetailSQL.updateOrderDetailFireStatus(1, orderDetail.getId().intValue());
						}
						if (!kotItemDetails.isEmpty()) {
							KotSummarySQL.update(kotSummary);
							if(!App.instance.isRevenueKiosk() && App.instance.getSystemSettings().isOrderSummaryPrint()){
								PrinterDevice printer = App.instance.getCahierPrinter();
								if (printer == null) {
									UIHelp.showToast(
											context,context.getResources().getString(R.string.setting_printer));
								} else {
									App.instance.remoteOrderSummaryPrint(printer, kotSummary, kotItemDetails, kotItemModifiers);
								}
							}
							// check system has KDS or printer devices
							if (App.instance.getKDSDevices().size() == 0
									&& App.instance.getPrinterDevices().size() == 0) {
								AlertToDeviceSetting
										.noKDSorPrinter(context,
												context.getResources().getString(R.string.no_set_kds_printer));
							} else {
								context.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										context.printerLoadingDialog
												.setTitle(context.getResources().getString(R.string.sending_to_kitchen));
										context.printerLoadingDialog.showTime();
									}
								});
								Map<String, Object> orderMap = new HashMap<String, Object>();
								orderMap.put("orderId", order.getId());
								orderMap.put("orderDetailIds", orderDetailIds);
								App.instance.getKdsJobManager().tearDownKotFire(
										kotSummary, kotItemDetails,
										kotItemModifiers, kotCommitStatus,
										orderMap);
							}
						} else {
							KotSummarySQL.deleteKotSummary(kotSummary);
							context.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									UIHelp.showToast(context,
											context.getResources().getString(R.string.sent_to_kitchen));
								}
							});
						}
					}
				}).start();
			}
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
