package com.alfredposclient.view.viewkiosk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.SlideExpandable.AbstractSlideExpandableListAdapter;
import com.SlideExpandable.SlideExpandableListView;
import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ColorUtils;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredbase.utils.StockCallBack;
import com.alfredbase.utils.SystemUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.kioskactivity.MainPageKiosk;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.popupwindow.DiscountWindow.ResultCall;
import com.alfredposclient.popupwindow.ModifyQuantityWindow.DismissCall;
import com.alfredposclient.utils.NetworkUtils;
import com.alfredposclient.view.RingTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainPageOrderViewKiosk extends LinearLayout {
    private MainPageKiosk parent;
    private Context context;
    private SlideExpandableListView lv_order;
    private Order order;
    private List<OrderDetail> orderDetails = Collections.emptyList();
    private Handler handler;
    private LayoutInflater inflater;
    private OrderAdapter orderAdapter;
    private TextView tv_table_name_ontop;
    private TextView tv_pax;
    private TextView tv_item_count;
    private TextView tv_sub_total;
    private TextView tv_discount;
    private TextView tv_taxes;
    private TextView tv_grand_total;
    private Button btn_place_order;
    private String kotCommitStatus;
    private TextTypeFace textTypeFace;
    private TextView tv_page_order_mask;
//	final CloudSyncJobManager cloudSync = App.instance.getSyncJob();

    public MainPageOrderViewKiosk(Context context) {
        super(context);
        this.context = context;
        init(context);
        initTextTypeFace();
    }

    public MainPageOrderViewKiosk(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
        initTextTypeFace();
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.main_page_order_kiosk_view, this);
        inflater = LayoutInflater.from(context);
        lv_order = (SlideExpandableListView) findViewById(R.id.lv_order);
        orderAdapter = new OrderAdapter();
        lv_order.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv_order.setAdapter(orderAdapter);

        tv_table_name_ontop = (TextView) findViewById(R.id.tv_table_name_ontop);
        tv_pax = (TextView) findViewById(R.id.tv_pax);
        tv_item_count = (TextView) findViewById(R.id.tv_item_count);
        tv_sub_total = (TextView) findViewById(R.id.tv_sub_total);
        tv_discount = (TextView) findViewById(R.id.tv_discount);
        tv_taxes = (TextView) findViewById(R.id.tv_taxes);
        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        tv_grand_total = (TextView) findViewById(R.id.tv_grand_total);
        tv_page_order_mask = (TextView) findViewById(R.id.tv_page_order_mask);
        tv_page_order_mask.setVisibility(View.GONE);
        tv_page_order_mask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_page_order_mask.setVisibility(View.GONE);
                closeAnimateView();
            }
        });
        if (App.countryCode == ParamConst.CHINA && SystemUtil.isZh(context))
            btn_place_order.setBackgroundResource(R.drawable.kiosk_box_place_order_selector_zh);
        tv_pax.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_TANSFER_PAX, (String) tv_pax.getText().toString()));
            }
        });
        btn_place_order.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ButtonClickTimer.canClick(v)) {
                    return;
                }

                if (orderDetails.isEmpty()) {
                    UIHelp.showShortToast(parent, parent.getResources().getString(R.string.no_order_detail));
                    return;
                }

                if (!NetworkUtils.isNetworkAvailable(context)) {
                    UIHelp.showShortToast(parent, parent.getResources().getString(R.string.network_connected));

                    //return;

                }
                int timely = Store.getInt(App.instance, Store.REPORT_ORDER_TIMELY);

                List<ModifierCheck> allModifierCheck = ModifierCheckSql.getAllModifierCheck(order.getId());
                Map<Integer, String> categorMap = new HashMap<Integer, String>();
                Map<String, Map<Integer, String>> checkMap = new HashMap<String, Map<Integer, String>>();
                for (int i = 0; i < allModifierCheck.size(); i++) {
                    ModifierCheck modifierCheck;
                    modifierCheck = allModifierCheck.get(i);
                    boolean needCheck = false;
                    if (orderDetails != null && orderDetails.size() > 0) {
                        for (OrderDetail orderDetail : orderDetails) {
                            if (orderDetail.getId().intValue() == modifierCheck.getOrderDetailId()) {
                                needCheck = true;
                            }
                        }
                    }
                    if (modifierCheck.getNum() > 0 && needCheck) {
                        if (checkMap.containsKey(modifierCheck.getItemName())) {
                            categorMap.put(modifierCheck.getModifierCategoryId(), modifierCheck.getModifierCategoryName() + " " + context.getResources().getString(R.string.at_least) + " " + modifierCheck.getMinNum() + " " + context.getResources().getString(R.string.items));
                            checkMap.put(modifierCheck.getItemName(), categorMap);

                        } else {
                            categorMap = new HashMap<Integer, String>();
                            categorMap.put(modifierCheck.getModifierCategoryId(), modifierCheck.getModifierCategoryName() + " " + context.getResources().getString(R.string.at_least) + " " + modifierCheck.getMinNum() + " " + context.getResources().getString(R.string.items));
                            checkMap.put(modifierCheck.getItemName(), categorMap);
                        }
                    }
                }


                if (checkMap.size() > 0) {
                    StringBuffer checkbuf = new StringBuffer();
                    Iterator iter = checkMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        checkbuf.append(" " + key + ":");
                        Map<Integer, String> val = (Map<Integer, String>) entry.getValue();
                        Iterator iter2 = val.entrySet().iterator();
                        while (iter2.hasNext()) {
                            Map.Entry entry2 = (Map.Entry) iter2.next();
                            String val2 = (String) entry2.getValue();
                            checkbuf.append(val2 + " ");
                        }
                    }

                    UIHelp.showToast((BaseActivity) context, checkbuf.toString());
                } else {

//					List<OrderDetail> orderDetailList = OrderDetailSQL.getOrderDetails(order.getId());
//
//					for(OrderDetail orderDetail: orderDetailList) {
//						int itemTempId = CoreData.getInstance().getItemDetailById(orderDetail.getItemId()).getItemTemplateId();
//						RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);
//
//						if (remainingStock != null) {
//							RemainingStockSQL.updateRemainingById(orderDetail.getItemNum(), itemTempId);
//						}
//					}

//					cloudSync.updateRemainingStock(order.getId());
                    //DON'T use reference
                    Order placedOrder = OrderSQL.getOrder(order.getId());

                    handler.sendMessage(handler
                            .obtainMessage(MainPage.VIEW_EVENT_PLACE_ORDER));
                    OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                            placedOrder, App.instance.getRevenueCenter());
                    OrderBillSQL.add(orderBill);
//				RoundAmount roundAmount = ObjectFactory.getInstance()
//						.getRoundAmount(placedOrder, orderBill, App.instance.getLocalRestaurantConfig().getRoundType());
//				RoundAmountSQL.update(roundAmount);
//				OrderHelper.setOrderTotalAlfterRound(placedOrder, roundAmount);
                    OrderSQL.update(placedOrder);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Order placedOrder = OrderSQL.getOrder(order.getId());
                            List<OrderDetail> placedOrderDetails
                                    = OrderDetailSQL.getOrderDetails(placedOrder.getId());
                            KotSummary kotSummary = ObjectFactory.getInstance()
                                    .getKotSummary(
                                            TableInfoSQL.getTableById(
                                                    placedOrder.getTableId()).getName(), placedOrder,
                                            App.instance.getRevenueCenter(),
                                            App.instance.getBusinessDate());
                            ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                            List<Integer> orderDetailIds = new ArrayList<Integer>();
                            ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                            kotCommitStatus = ParamConst.JOB_NEW_KOT;
                            for (OrderDetail orderDetail : placedOrderDetails) {
                                if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_PREPARED) {
                                    continue;
                                }
                                if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                                    kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
                                } else {
                                    KotItemDetail kotItemDetail = ObjectFactory
                                            .getInstance()
                                            .getKotItemDetail(
                                                    placedOrder,
                                                    orderDetail,
                                                    CoreData.getInstance()
                                                            .getItemDetailById(
                                                                    orderDetail
                                                                            .getItemId()),
                                                    kotSummary,
                                                    App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                                    kotItemDetail.setItemNum(orderDetail.getItemNum());

                                    //region change status TMP when place order
//                                    if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_TMP) {
//                                        kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNSEND);
//                                    }
                                    //endregion

                                    if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UNDONE) {
                                        kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
                                        kotItemDetail
                                                .setKotStatus(ParamConst.KOT_STATUS_UPDATE);
                                    }
                                    KotItemDetailSQL.update(kotItemDetail);
                                    kotItemDetails.add(kotItemDetail);
                                    orderDetailIds.add(orderDetail.getId());
                                    ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                                            .getOrderModifiers(placedOrder, orderDetail);
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
                                        } else if (orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_DELETE) {

                                            KotItemModifier kotItemModifier = KotItemModifierSQL
                                                    .getKotItemModifier(kotItemDetail.getId(), CoreData.getInstance()
                                                            .getModifier(
                                                                    orderModifier
                                                                            .getModifierId()).getId());
                                            if (kotItemModifier != null) {
                                                KotItemModifierSQL.deleteKotItemModifier(kotItemModifier);
                                            }
                                        }
                                    }
                                }
                            }
                            KotSummarySQL.update(kotSummary);
                            handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_SHOW_CLOSE_ORDER_WINDOW);
						/*
						if (!kotItemDetails.isEmpty()) {
							// check system has KDS or printer devices
							if (App.instance.getKDSDevices().size() == 0
									&& App.instance.getPrinterDevices().size() == 0) {
								AlertToDeviceSetting
										.noKDSorPrinter(parent,
												parent.getResources().getString(R.string.no_set_kds_printer));
							} else {
								parent.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										parent.printerLoadingDialog
												.setTitle(parent.getResources().getString(R.string.send_kitchen));
										parent.printerLoadingDialog.showTime();
									}
								});
								Map<String, Object> orderMap = new HashMap<String, Object>();
								orderMap.put("orderId", order.getId());
								orderMap.put("orderDetailIds", orderDetailIds);
								App.instance.getKdsJobManager().tearDownKot(
										kotSummary, kotItemDetails,
										kotItemModifiers, kotCommitStatus,
										orderMap);
								// close bill
								handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_SHOW_CLOSE_ORDER_WINDOW);
							}
						} else {
							handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_SHOW_CLOSE_ORDER_WINDOW);
						}
						*/
                        }
                    }).start();

                }
            }
        });
    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod(tv_table_name_ontop);
        textTypeFace.setTrajanProBlod(tv_pax);
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_name_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_price_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_qry_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_sutotal_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_discount_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_total_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_grand_total));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_item_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_sub_total_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_discount_totle_title));
        textTypeFace
                .setTrajanProRegular((TextView) findViewById(R.id.tv_taxes_title));
        textTypeFace.setTrajanProRegular(tv_item_count);
        textTypeFace.setTrajanProRegular(tv_sub_total);
        textTypeFace.setTrajanProRegular(tv_discount);
        textTypeFace.setTrajanProRegular(tv_taxes);
    }

    public void setParam(MainPageKiosk parent, Order order,
                         List<OrderDetail> orderDetails, Handler handler) {
        this.parent = parent;
        this.order = order;
        this.orderDetails = orderDetails;
        this.handler = handler;
        refresh();
        App.instance.sendDataToSecondScreen(order, orderDetails);
    }

    private void refresh() {
        if (orderAdapter != null)
            orderAdapter.notifyDataSetChanged();
        int itemCount = 0;
        if (!orderDetails.isEmpty()) {
            for (OrderDetail orderDetail : orderDetails) {
                itemCount += orderDetail.getItemNum();
            }
        } else {
            //  add:
            // if there is no order detail, complete all KOT if the order has
            // KotSummary kotSummary =
            // KotSummarySQL.getKotSummary(order.getId());
            // if(kotSummary != null){
            // kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
            // KotSummarySQL.update(kotSummary);
            // }
        }
        String orderNoStr = "";
        if (!TextUtils.isEmpty(order.getTableName())) {
            orderNoStr = "TableName:" + order.getTableName();
        } else {
            if (App.instance.countryCode == ParamConst.CHINA) {
                orderNoStr = parent.getResources().getString(R.string.order_id) + App.instance.getPrintOrderNo(order.getId());
            } else {
                orderNoStr = parent.getResources().getString(R.string.order_id)
                        + order.getOrderNo();
            }
        }

        if (order.getIsTakeAway() == ParamConst.TAKE_AWAY) {
            orderNoStr = orderNoStr + "(" + parent.getResources().getString(R.string.takeaway) + ")";
        }
        tv_table_name_ontop.setText(orderNoStr);
        if (!IntegerUtils.isEmptyOrZero(order.getPersons())) {
            tv_pax.setText(parent.getString(R.string.pax) + " " + order.getPersons().intValue());
        } else {
            tv_pax.setText(parent.getString(R.string.pax) + " 4");
        }
        tv_item_count.setText("" + itemCount);
        tv_sub_total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(order.getSubTotal()));
        tv_discount.setText("-" + App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(order.getDiscountAmount()));
        tv_taxes.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(order.getTaxAmount()));
        tv_grand_total.setText(parent.getResources().getString(R.string.grand_total) + ": " + App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(order.getTotal()));
    }

    class OrderAdapter extends AbstractSlideExpandableListAdapter {

        private MoreViewOnClickListener moreViewOnClickListener;

        public OrderAdapter() {
            moreViewOnClickListener = new MoreViewOnClickListener();
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
                arg1 = inflater.inflate(R.layout.item_main_page_order, null);
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
                holder.tv_instruction = (TextView) arg1
                        .findViewById(R.id.tv_instruction);
                holder.tv_remove = (TextView) arg1.findViewById(R.id.tv_remove);
                holder.tv_void = (TextView) arg1.findViewById(R.id.tv_void);
                holder.tv_free = (TextView) arg1.findViewById(R.id.tv_free);
                holder.tv_takeaway = (TextView) arg1
                        .findViewById(R.id.tv_takeaway);
                holder.tv_transfer = (TextView) arg1
                        .findViewById(R.id.tv_transfer);
                holder.tv_weight = (TextView) arg1
                        .findViewById(R.id.tv_weight);
                holder.rv_split = (RingTextView) arg1.findViewById(R.id.rv_split);
                holder.ll_order_detail = (LinearLayout) arg1.findViewById(R.id.ll_order_detail);
                textTypeFace.setTrajanProRegular(holder.name);
                textTypeFace.setTrajanProRegular(holder.price);
                textTypeFace.setTrajanProRegular(holder.tv_qty);
                textTypeFace.setTrajanProRegular(holder.subtotal);
                textTypeFace.setTrajanProRegular(holder.discount);
                textTypeFace.setTrajanProRegular(holder.total);
                textTypeFace.setTrajanProRegular(holder.modifier);
                textTypeFace.setTrajanProRegular(holder.tv_instruction);
                textTypeFace.setTrajanProRegular(holder.tv_remove);
                textTypeFace.setTrajanProRegular(holder.tv_void);
                textTypeFace.setTrajanProRegular(holder.tv_free);
                textTypeFace.setTrajanProRegular(holder.tv_takeaway);
                textTypeFace.setTrajanProRegular(holder.tv_transfer);
                textTypeFace.setTrajanProRegular(holder.tv_weight);
                textTypeFace.setTrajanProRegular(holder.specialInstract);
                textTypeFace.setTrajanProRegular(holder.rv_split);
                holder.ll_more = (LinearLayout) arg1.findViewById(R.id.ll_more);
                holder.ll_instruction = (LinearLayout) arg1
                        .findViewById(R.id.ll_instruction);
                holder.ll_remove = (LinearLayout) arg1
                        .findViewById(R.id.ll_remove);
                holder.ll_void = (LinearLayout) arg1.findViewById(R.id.ll_void);
                holder.ll_free = (LinearLayout) arg1.findViewById(R.id.ll_free);
                holder.ll_takeaway = (LinearLayout) arg1
                        .findViewById(R.id.ll_takeaway);
                holder.ll_transfer = (LinearLayout) arg1
                        .findViewById(R.id.ll_transfer);
                holder.ll_weight = (LinearLayout) arg1
                        .findViewById(R.id.ll_weight);

                holder.ll_split = (LinearLayout) arg1.findViewById(R.id.ll_split);
                holder.gv_person_index = (GridView) arg1.findViewById(R.id.gv_person_index);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }

            final OrderDetail orderDetail = orderDetails.get(arg0);
            String modifiers = getItemModifiers(orderDetail);
            if (orderDetail.getOrderDetailStatus() < ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                holder.ll_order_detail.setBackgroundColor(parent.getResources().getColor(
                        R.color.white));
            } else {
                holder.ll_order_detail.setBackgroundColor(parent.getResources().getColor(
                        R.color.light_gray));
            }
            final Adapter adapter = new Adapter();
            holder.rv_split.setTag(adapter);
            enableFor(arg1, holder.rv_split, holder.ll_split, holder.ll_more, arg0, arg2);
            arg1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    collapseLastOpen();
                    if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
                        return;
                    }
                    // else if (orderDetail.getOrderDetailStatus() >=
                    // ParamConst.ORDERDETAIL_STATUS_PREPARED) {
                    // handler.sendMessage(handler.obtainMessage(
                    // MainPage.VIEW_EVENT_SHOW_VOIDORFREE_WINDOW,
                    // orderDetail));
                    // return;
                    // }
                    List<ItemModifier> itemModifiers = CoreData.getInstance()
                            .getItemModifiers(
                                    CoreData.getInstance().getItemDetailById(
                                            orderDetail.getItemId()));
                    if (itemModifiers.size() > 0) {
                        Message msg = handler.obtainMessage();
                        msg.what = MainPage.VIEW_EVENT_OPEN_MODIFIERS;
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("orderDetail", orderDetail);
                        map.put("itemModifiers", itemModifiers);
                        map.put("view", v);
                        msg.obj = map;
                        handler.sendMessage(msg);
                    }

                }
            });

            ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
                    orderDetail.getItemId());
            //  itemDetails will be null in case that wait app keep old
            // wrong menu
            // if (itemDetail == null) {
            // return arg1;
            // }
            if (modifiers != null) {
                holder.modifier.setText(modifiers);
            }
            holder.specialInstract
                    .setText(orderDetail.getSpecialInstractions());
            holder.name.setText(itemDetail.getItemName());
            holder.price.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(orderDetail.getItemPrice()));
            holder.tv_qty.setText(orderDetail.getItemNum() + "");
            holder.tv_qty.setBackgroundColor(context.getResources().getColor(
                    R.color.white));
            holder.tv_qty.setTag(orderDetail);
            holder.tv_qty.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    collapseLastOpen();
                    if (!ButtonClickTimer.canClick(arg0)) {
                        return;
                    }
                    final OrderDetail tag = (OrderDetail) arg0.getTag();
                    if (tag.getIsFree().intValue() == ParamConst.FREE) {
                        return;
                    } else if (tag.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                        return;
                    }
                    final TextView textView = (TextView) arg0;
                    textView.setBackgroundColor(context.getResources()
                            .getColor(R.color.gray));
                    Message msg = handler.obtainMessage();
                    msg.what = MainPage.VIEW_EVENT_SHOW_MODIFY_QUANTITY_WINDOW;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("quantity", tag.getItemNum());
                    map.put("dismissCall", new DismissCall() {

                        @Override
                        public void call(String key, int num) {
                            if (tag.getOrderDetailStatus() < ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                                if (num < 1) {

                                    final ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(tag.getItemId());
                                    RemainingStock remainingStock = null;
                                    if (itemDetail != null) {
                                        remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                                    }
                                    if (remainingStock != null) {
                                        RemainingStockHelper.updateRemainingStockNum(remainingStock, tag.getItemNum(), true, new StockCallBack() {
                                            @Override
                                            public void onSuccess(Boolean isStock) {
                                                if (isStock) {
                                                    App.instance.getSyncJob().updateRemainingStockNum(itemDetail.getItemTemplateId());
                                                }
                                            }
                                        });

                                    }
                                    OrderDetailSQL.deleteOrderDetail(tag);
                                    OrderModifierSQL.deleteOrderModifierByOrderDetail(tag);
                                    if (!IntegerUtils.isEmptyOrZero(tag.getOrderSplitId()) && !IntegerUtils.isEmptyOrZero(tag.getGroupId())) {
                                        int count = OrderDetailSQL.getOrderDetailCountByGroupId(tag.getGroupId().intValue(), order.getId().intValue());
                                        if (count == 0) {
                                            OrderSplitSQL.deleteOrderSplitByOrderAndGroupId(order.getId().intValue(), tag.getGroupId().intValue());
                                        }
                                    }
                                } else if (num > 999) {
                                    tag.setItemNum(999);
                                    OrderDetailSQL
                                            .updateOrderDetailAndOrder(tag);
//									OrderModifierSQL.updateOrderModifierNum(tag, 999);
                                    OrderHelper.setOrderModifierPirceAndNum(tag, 999);
                                } else {
                                    final ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(tag.getItemId());
                                    RemainingStock remainingStock = null;
                                    if (itemDetail != null) {
                                        remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                                    }
                                    if (remainingStock != null) {
//										int existedOrderDetailNum = OrderDetailSQL.getOrderDetailCountByOrderIdAndItemDetailId(order.getId(), itemDetail.getId());
//										existedOrderDetailNum += num - tag.getItemNum();
//										if(remainingStock.getQty() < existedOrderDetailNum){
//											UIHelp.showShortToast(parent, parent.getString(R.string.out_of_stock));
//											return;
//										}
                                        int newNum;
                                        final Boolean isChange;
                                        if (num >= tag.getItemNum()) {
                                            newNum = num - tag.getItemNum();
                                            RemainingStockHelper.updateRemainingStockNum(remainingStock, newNum, false, new StockCallBack() {
                                                @Override
                                                public void onSuccess(Boolean isStock) {
                                                    if (!isStock) {
                                                        UIHelp.showShortToast(parent, parent.getString(R.string.out_of_stock));
                                                        return;
                                                    } else {
                                                        App.instance.getSyncJob().updateRemainingStockNum(itemDetail.getItemTemplateId());
                                                    }
                                                }
                                            });
                                        } else {
                                            newNum = tag.getItemNum() - num;
                                            RemainingStockHelper.updateRemainingStockNum(remainingStock, newNum, true, new StockCallBack() {
                                                @Override
                                                public void onSuccess(Boolean isStock) {
                                                    if (!isStock) {
                                                        UIHelp.showShortToast(parent, parent.getString(R.string.out_of_stock));
                                                        return;
                                                    } else {
                                                        App.instance.getSyncJob().updateRemainingStockNum(itemDetail.getItemTemplateId());
                                                    }
                                                }
                                            });
                                        }

                                    }
                                    tag.setItemNum(num);
                                    OrderDetailSQL
                                            .updateOrderDetailAndOrder(tag);
//									OrderModifierSQL.updateOrderModifierNum(tag, num);
                                    OrderHelper.setOrderModifierPirceAndNum(tag, num);
                                }
                            } else {
                                if (num < 1) {
                                    // OrderDetail freeOrderDetail =
                                    // OrderDetailSQL
                                    // .getOrderDetail(order.getId(), tag);
                                    // OrderDetailSQL.deleteOrderDetail(tag);
                                    // kotCommitStatus =
                                    // ParamConst.JOB_DELETE_KOT;
                                    // KotItemDetail kotItemDetail =
                                    // KotItemDetailSQL
                                    // .getKotItemDetailByOrderDetailId(tag
                                    // .getId());
                                    // KotItemDetailSQL.deleteKotItemDetail(tag);
                                    // ArrayList<KotItemDetail> kotItemDetails =
                                    // new ArrayList<KotItemDetail>();
                                    // kotItemDetails.add(kotItemDetail);
                                    // if (freeOrderDetail != null) {
                                    // KotItemDetail freeKotItemDetail =
                                    // KotItemDetailSQL
                                    // .getKotItemDetailByOrderDetailId(freeOrderDetail
                                    // .getId());
                                    // KotItemDetailSQL
                                    // .deleteKotItemDetail(freeOrderDetail);
                                    // kotItemDetails.add(freeKotItemDetail);
                                    // }
                                    // App.instance
                                    // .getKdsJobManager()
                                    // .tearDownKot(
                                    // null,
                                    // kotItemDetails,
                                    // new ArrayList<KotItemModifier>(),
                                    // kotCommitStatus, null);

                                } else {
                                    final ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(tag.getItemId());
                                    RemainingStock remainingStock = null;
                                    if (itemDetail != null) {
                                        remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                                    }
                                    if (remainingStock != null) {
                                        int newNum;
                                        Boolean isChange;
                                        if (num >= tag.getItemNum()) {
                                            newNum = num - tag.getItemNum();
                                            RemainingStockHelper.updateRemainingStockNum(remainingStock, newNum, false, new StockCallBack() {
                                                @Override
                                                public void onSuccess(Boolean isStock) {
                                                    if (!isStock) {
                                                        UIHelp.showShortToast(parent, parent.getString(R.string.out_of_stock));
                                                        return;
                                                    } else {
                                                        App.instance.getSyncJob().updateRemainingStockNum(itemDetail.getItemTemplateId());
                                                    }
                                                }
                                            });
                                        } else {
                                            newNum = tag.getItemNum() - num;
                                            RemainingStockHelper.updateRemainingStockNum(remainingStock, newNum, true, new StockCallBack() {
                                                @Override
                                                public void onSuccess(Boolean isStock) {
                                                    if (!isStock) {
                                                        UIHelp.showShortToast(parent, parent.getString(R.string.out_of_stock));
                                                        return;
                                                    } else {
                                                        App.instance.getSyncJob().updateRemainingStockNum(itemDetail.getItemTemplateId());
                                                    }
                                                }
                                            });
                                        }

                                    }
                                    tag.setItemNum(num);
                                    tag.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
                                    OrderDetailSQL
                                            .updateOrderDetailAndOrder(tag);
//									OrderModifierSQL.updateOrderModifierNum(tag, num);
                                    OrderHelper.setOrderModifierPirceAndNum(tag, num);
                                }
                            }
                            Message msg = handler.obtainMessage();
                            msg.what = MainPage.VIEW_EVENT_SET_DATA_AND_CLOSE_MODIFIER;
                            handler.sendMessage(msg);
                        }
                    });
                    msg.obj = map;
                    handler.sendMessage(msg);
                }
            });

            holder.subtotal.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(orderDetail.getRealPrice()));

            if (orderDetail.getOrderDetailType().intValue() == ParamConst.ORDERDETAIL_TYPE_FREE) {
                holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(ParamConst.DOUBLE_ZERO).toString());
                holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(ParamConst.DOUBLE_ZERO).toString());
            } else {
                holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(orderDetail.getDiscountPrice()).toString());
                holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol()
                        + BH.formatMoney(BH.sub(BH.getBD(orderDetail.getRealPrice()),
                        BH.getBD(orderDetail.getDiscountPrice()), true).toString()));
            }
            holder.discount.setBackgroundColor(context.getResources().getColor(
                    R.color.white));
            holder.discount.setTag(orderDetail);
            holder.discount.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    collapseLastOpen();
                    if (!ButtonClickTimer.canClick(v)) {
                        return;
                    }
                    final OrderDetail tag = (OrderDetail) v.getTag();
                    if (tag.getIsFree().intValue() == ParamConst.FREE || tag.getIsItemDiscount() == ParamConst.ITEM_NO_DISCOUNT) {
                        UIHelp.showToast(parent, parent.getResources().getString(R.string.order_first));
                        return;
                    }
                    final TextView textView = (TextView) v;
                    textView.setBackgroundColor(context.getResources()
                            .getColor(R.color.gray));
                    Message msg = handler.obtainMessage();
                    msg.what = MainPage.VIEW_EVENT_SHOW_DISCOUNT_WINDOW;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("order", null);
                    map.put("orderDetail", tag);
                    map.put("resultCall", new ResultCall() {
                        @Override
                        public void call(final String discount_rate,
                                         final String discount_price, String discountByCategory) {
                            // parent.loadingDialog.show();
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    Message msg = handler.obtainMessage();
                                    msg.what = MainPage.VIEW_EVENT_SET_DATA_AND_CLOSE_MODIFIER;
                                    if (CommonUtil.isNull(discount_rate)
                                            && CommonUtil
                                            .isNull(discount_price)) {
                                        handler.sendMessage(msg);
                                        return;
                                    }
                                    if (!CommonUtil.isNull(discount_rate)) {
                                        if (BH.compare(BH.getBD(discount_rate), BH.getBD(ParamConst.DOUBLE_ZERO))) {
                                            tag.setDiscountRate(discount_rate);
                                            tag.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE);
                                            tag.setDiscountPrice(BH.mul(BH.getBD(tag.getRealPrice()), BH.getBDNoFormat(discount_rate), true).toString());
                                        } else {
                                            tag.setDiscountPrice(ParamConst.INT_ZERO);
                                            tag.setDiscountRate(ParamConst.INT_ZERO);
                                            tag.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
                                        }
                                    } else {
                                        if (BH.compare(BH.getBD(discount_price), BH.getBD(ParamConst.DOUBLE_ZERO))) {
                                            tag.setDiscountPrice(discount_price);
                                            tag.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB);
                                        } else {
                                            tag.setDiscountRate(ParamConst.INT_ZERO);
                                            tag.setDiscountPrice(ParamConst.INT_ZERO);
                                            tag.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
                                        }
                                    }
                                    OrderDetailSQL
                                            .updateOrderDetailAndOrderByDiscount(tag);
                                    handler.sendMessage(msg);
                                }
                            }).start();

                        }

                    });
                    msg.obj = map;
                    handler.sendMessage(msg);
                }
            });

            if (orderDetail.getGroupId().intValue() > 0) {
                holder.rv_split.setCircleColor(parent.getResources().getColor(ColorUtils.ColorGroup.getColor(orderDetail.getGroupId().intValue())), orderDetail.getGroupId().intValue());
            } else {
                holder.rv_split.restoreCircleColor();
            }
//			holder.rv_split.setTag(orderDetail);
//			holder.rv_split.setOnClickListener(moreViewOnClickListener);

            holder.gv_person_index.setAdapter(adapter);
            holder.ll_instruction.setTag(orderDetail);
            holder.ll_remove.setTag(orderDetail);
            holder.ll_void.setTag(orderDetail);
            holder.ll_free.setTag(orderDetail);
            holder.ll_takeaway.setTag(orderDetail);
            holder.ll_transfer.setTag(orderDetail);
            holder.ll_weight.setTag(orderDetail);
            holder.ll_instruction.setOnClickListener(moreViewOnClickListener);
            holder.ll_remove.setOnClickListener(moreViewOnClickListener);
            holder.ll_void.setOnClickListener(moreViewOnClickListener);
            holder.ll_free.setOnClickListener(moreViewOnClickListener);
            holder.ll_takeaway.setOnClickListener(moreViewOnClickListener);
            holder.ll_transfer.setOnClickListener(moreViewOnClickListener);
            holder.ll_weight.setOnClickListener(moreViewOnClickListener);
            holder.gv_person_index.setTag(holder.rv_split);
            holder.gv_person_index.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    if (!IntegerUtils.isEmptyOrZero(order.getAppOrderId())) {
                        return;
                    }
                    if (arg2 == 0) {
                        if (adapter.getPageIndex() == 0) {
                            if (orderDetail.getGroupId().intValue() > 0) {
                                OrderSplit orderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
                                if (orderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED) {
                                    UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) + orderSplit.getGroupId() +
                                            parent.getResources().getString(R.string._settled));
                                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
                                    return;
                                }
                                int count = OrderDetailSQL
                                        .getOrderDetailCountByGroupId(
                                                orderDetail.getGroupId(), order.getId());
                                ((RingTextView) arg0.getTag()).restoreCircleColor();
                                orderDetail.setGroupId(0);
                                orderDetail.setOrderSplitId(0);
                                OrderDetailSQL.updateOrderDetail(orderDetail);
                                OrderDetailTaxSQL.updateOrderSplitIdbyOrderDetail(orderDetail);
                                if (count > 1) {
                                    OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
                                } else {
                                    OrderSplitSQL.delete(orderSplit);
                                    RoundAmountSQL.deleteRoundAmount(orderSplit);
                                }
                                handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
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
                        int groupId = orderDetail.getGroupId().intValue();
                        int index = (arg2 + adapter.getPageIndex() * 6);
                        if (groupId == index) {
                            collapseLastOpen();
                            return;
                        }
//						if(orderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED){
//							UIHelp.showToast(parent   , parent.getResources().getString(R.string.order_split_) +
//									orderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
//							handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
//							return;
//						}
                        OrderSplit oldOrderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, groupId);
                        if (oldOrderSplit != null && oldOrderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED) {
                            UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) +
                                    oldOrderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
                            collapseLastOpen();
                            return;
                        }
                        OrderSplit orderSplit = ObjectFactory.getInstance().getOrderSplit(order, index, App.instance.getLocalRestaurantConfig()
                                .getIncludedTax().getTax());
                        if (orderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED) {
                            UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) +
                                    orderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
                            collapseLastOpen();
                            return;
                        }
                        orderDetail.setGroupId(index);
                        if (groupId > 0) {
                            int count = OrderDetailSQL.getOrderDetailCountByGroupId(groupId, order.getId());
                            if (count == 1 && groupId != index) {
                                OrderSplitSQL.deleteOrderSplitByOrderAndGroupId(order.getId(), groupId);
                                RoundAmountSQL.deleteRoundAmount(oldOrderSplit);
                            } else {
                                orderDetail.setOrderSplitId(orderSplit.getId());
                                OrderDetailSQL.updateOrderDetail(orderDetail);
                                OrderSplitSQL.updateOrderSplitByOrder(order, oldOrderSplit);
                            }
                        }

                        ((RingTextView) arg0.getTag()).setCircleColor(parent.getResources().getColor(ColorUtils.ColorGroup.getColor(index)), index);
                        orderDetail.setOrderSplitId(orderSplit.getId());
                        OrderDetailSQL.updateOrderDetail(orderDetail);
                        OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
                        OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS, order.getId());
                        OrderDetailTaxSQL.updateOrderSplitIdbyOrderDetail(orderDetail);
                        collapseLastOpen();
                        handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
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
            public LinearLayout ll_more;
            public LinearLayout ll_instruction;
            public LinearLayout ll_remove;
            public LinearLayout ll_void;
            public LinearLayout ll_free;
            public LinearLayout ll_takeaway;
            public LinearLayout ll_transfer;
            public LinearLayout ll_weight;
            public TextView tv_instruction;
            public TextView tv_remove;
            public TextView tv_void;
            public TextView tv_free;
            public TextView tv_takeaway;
            public TextView tv_transfer;
            public TextView tv_weight;
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
            }

            ;

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

        class MoreViewOnClickListener implements OnClickListener {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ll_instruction: {
                        final OrderDetail orderDetail = (OrderDetail) view.getTag();
                        if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
                        } else if (orderDetail.getOrderDetailStatus() <= ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                            handler.sendMessage(handler
                                    .obtainMessage(
                                            MainPage.VIEW_EVENT_SHOW_SPECIAL_INSTRACTIONS_WINDOW,
                                            orderDetail));
                            handler.sendEmptyMessage(MainPage.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
                        }
                    }
                    break;
                    case R.id.ll_remove:
                        final OrderDetail tag = (OrderDetail) view.getTag();
                        if (tag.getIsFree().intValue() == ParamConst.FREE) {
                            return;
                        } else if (tag.getOrderSplitId() != null && tag.getOrderSplitId().intValue() != 0) {
                            OrderSplit orderSplit = OrderSplitSQL.get(tag.getOrderSplitId().intValue());
                            if (orderSplit.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                                UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) +
                                        orderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
                                return;
                            }
                        }
                        if (tag.getOrderDetailStatus() < ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                            DialogFactory.commonTwoBtnDialog(parent, parent.getResources().getString(R.string.warning),
                                    parent.getResources().getString(R.string.remove_item),
                                    parent.getResources().getString(R.string.no),
                                    parent.getResources().getString(R.string.yes), null,
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            final int itemTempId = CoreData.getInstance().getItemDetailById(tag.getItemId(), tag.getItemName()).getItemTemplateId();
                                            RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);
                                            if (remainingStock != null) {
                                                int num = tag.getItemNum();
                                                RemainingStockHelper.updateRemainingStockNum(remainingStock, num, true, new StockCallBack() {
                                                    @Override
                                                    public void onSuccess(Boolean isStock) {
                                                        if (isStock) {
                                                            App.instance.getSyncJob().updateRemainingStockNum(itemTempId);
                                                        }
                                                    }
                                                });

                                            }

                                            OrderDetailSQL.deleteOrderDetail(tag);
                                            KotSummary kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());
                                            if (kotSummary != null) {
                                                KotItemDetailSQL.deleteKotItemDetail(kotSummary.getId(), tag);
                                            }
                                            OrderModifierSQL.deleteOrderModifierByOrderDetail(tag);
                                            ModifierCheckSql.deleteModifierCheck(tag.getId(), tag.getOrderId());
                                            if (!IntegerUtils.isEmptyOrZero(tag.getOrderSplitId()) && !IntegerUtils.isEmptyOrZero(tag.getGroupId())) {
                                                int count = OrderDetailSQL.getOrderDetailCountByGroupId(tag.getGroupId().intValue(), order.getId().intValue());
                                                if (count == 0) {
                                                    OrderSplitSQL.deleteOrderSplitByOrderAndGroupId(order.getId().intValue(), tag.getGroupId().intValue());
                                                }
                                            }
                                            handler.sendEmptyMessage(MainPage.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
                                            handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);

                                        }
                                    });
                        }
                        break;
                    case R.id.ll_void: {
                        OrderDetail orderDetail = (OrderDetail) view.getTag();
                        if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
                            return;
                        } else if (orderDetail.getOrderSplitId() != null && orderDetail.getOrderSplitId().intValue() != 0) {
                            OrderSplit orderSplit = OrderSplitSQL.get(orderDetail.getOrderSplitId().intValue());
                            if (orderSplit.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                                UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) +
                                        orderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
                                return;
                            }
                        }
                        if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("orderDetail", orderDetail);
                            map.put("type", new Integer(
                                    ParamConst.ORDERDETAIL_TYPE_VOID));
                            handler.sendMessage(handler.obtainMessage(
                                    MainPage.VIEW_EVENT_VOID_OR_FREE, map));
                            handler.sendEmptyMessage(MainPage.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
                            // handler.sendMessage(handler.obtainMessage(
                            // MainPage.VIEW_EVENT_SHOW_VOIDORFREE_WINDOW,
                            // orderDetail));
                            return;
                        }
                    }
                    break;
                    case R.id.ll_free: {
                        OrderDetail orderDetail = (OrderDetail) view.getTag();
                        if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
                            return;
                        } else if (orderDetail.getOrderSplitId() != null && orderDetail.getOrderSplitId().intValue() != 0) {
                            OrderSplit orderSplit = OrderSplitSQL.get(orderDetail.getOrderSplitId().intValue());
                            if (orderSplit.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                                UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) +
                                        orderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
                                return;
                            }
                        }
                        if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_ADDED) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("orderDetail", orderDetail);
                            map.put("type", new Integer(
                                    ParamConst.ORDERDETAIL_TYPE_FREE));
                            handler.sendMessage(handler.obtainMessage(
                                    MainPage.VIEW_EVENT_VOID_OR_FREE, map));
                            handler.sendEmptyMessage(MainPage.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
                            // handler.sendMessage(handler.obtainMessage(
                            // MainPage.VIEW_EVENT_SHOW_VOIDORFREE_WINDOW,
                            // orderDetail));
                            return;
                        }
                    }
                    break;
                    case R.id.ll_takeaway: {
                        final OrderDetail orderDetail = (OrderDetail) view.getTag();
                        if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
                            return;
                        }
                        if (orderDetail.getOrderDetailStatus() > ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                            UIHelp.showToast(parent, parent.getResources().getString(R.string.item_complete));
                            return;
                        }
                        if (orderDetail.getOrderSplitId() != null && orderDetail.getOrderSplitId().intValue() != 0) {
                            OrderSplit orderSplit = OrderSplitSQL.get(orderDetail.getOrderSplitId().intValue());
                            if (orderSplit.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                                UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) +
                                        orderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
                                return;
                            }
                        }
                        if (orderDetail.getIsTakeAway() == ParamConst.TAKE_AWAY) {
                            handler.sendMessage(handler.obtainMessage(MainPageKiosk.VIEW_EVENT_TAKE_AWAY, orderDetail));
                            handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
                        } else {
                            DialogFactory.commonTwoBtnDialog(parent, parent.getResources().getString(R.string.warning),
                                    parent.getResources().getString(R.string.sure_take_away),
                                    parent.getResources().getString(R.string.no),
                                    parent.getResources().getString(R.string.yes), null,
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            handler.sendMessage(handler.obtainMessage(MainPageKiosk.VIEW_EVENT_TAKE_AWAY, orderDetail));
                                            handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
                                        }
                                    });
                        }
                    }
                    break;
                    case R.id.ll_transfer:

                        break;
                    case R.id.ll_weight: {
                        OrderDetail orderDetail = (OrderDetail) view.getTag();
                        if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
                            return;
                        } else if (orderDetail.getOrderSplitId() != null && orderDetail.getOrderSplitId().intValue() != 0) {
                            OrderSplit orderSplit = OrderSplitSQL.get(orderDetail.getOrderSplitId().intValue());
                            if (orderSplit.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                                UIHelp.showToast(parent, parent.getResources().getString(R.string.order_split_) +
                                        orderSplit.getGroupId() + parent.getResources().getString(R.string._settled));
                                return;
                            }
                        }
                        if (orderDetail.getOrderDetailStatus() < ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                            handler.sendMessage(handler
                                    .obtainMessage(
                                            MainPageKiosk.VIEW_EVENT_SET_WEIGHT,
                                            orderDetail));
                            handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
                        }
                    }
                    break;
//				case R.id.rv_split:
//					OrderDetail sqlitOrderDetail = (OrderDetail) view.getTag();
//					handler.sendMessage(handler.obtainMessage(MainPage.VIEW_SHOW_SQLITE_DIALOG, view.getTag()));
//					break;
                    default:
                        break;
                }
            }

        }
    }

    private void closeAnimateView() {
        handler.sendMessage(handler.obtainMessage(MainPageKiosk.CHECK_TO_CLOSE_CUSTOM_NOTE_VIEW, ""));
    }

    public void showOrCloseMask(boolean show) {
        if (show) {
            tv_page_order_mask.setVisibility(View.VISIBLE);
        } else {
            tv_page_order_mask.setVisibility(View.GONE);
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
                if (modifier.getQty() > 1) {
                    result += modifierName + "x" + modifier.getQty() + ";";
                } else {
                    result += modifierName + ";";
                }
            }
        }
        return result;
    }
}
