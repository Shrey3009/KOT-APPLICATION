package com.alfredposclient.activity.kioskactivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.PaymentSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.RoundUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.TimeUtil;
import com.alfredbase.utils.ToastUtils;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alex on 2018/4/16.
 */

public class KioskHoldActivity extends BaseActivity implements View.OnLongClickListener {
    private boolean hasOrder;
    private LinearLayout ll_orderdetail_layout;
    private ListView lv_order_list;
    private ListView lv_orderdetail_list;
    private LayoutInflater inflater;
    private List<Order> orderList = new ArrayList<>();
    private List<Order> orderCache = new ArrayList<>();
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private int selectOrderItem = 0;
    private KioskHoldOderAdapter kioskHoldOderAdapter;
    private KioskHoldOderDetailAdapter kioskHoldOderDetailAdapter;
    private TextTypeFace textTypeFace = TextTypeFace.getInstance();
    private int selectViewId;
    private TextView tv_hold_order, tv_hold_kitchen_order, tv_kiosk_order;
    private TextView tv_remarks;
    private TextView tv_eat_type;
    private Button btn_get_order;
    private Button btn_close_w_cash;
    private Button btn_refresh;
    private Order currentOrder;
    private LinearLayout ll_remarks;
    private SearchView et_search;
    public static final int CHECK_REQUEST_CODE = 100;
    public static final int CHECK_RESULT_CODE = 100;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_kiosk_hold);
        Intent intent = getIntent();
        hasOrder = intent.getBooleanExtra("hasOrder", true);
        if (intent.hasExtra("currentOrder")) {
            currentOrder = (Order) intent.getExtras().get("currentOrder");
        }
//        orderList =
        ll_orderdetail_layout = (LinearLayout) findViewById(R.id.ll_orderdetail_layout);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle(context.getString(R.string.loading));
        inflater = LayoutInflater.from(this);
        selectOrderItem = 0;
        int status = Store.getInt(context, Store.DEFAULT_VIEW, -1);
        selectViewId = R.id.tv_hold_kitchen_order;
        if (status > 0) {
            if (status == ParamConst.ORDER_STATUS_HOLD) {
                selectViewId = R.id.tv_hold_order;
            } else if (status == ParamConst.ORDER_STATUS_KIOSK) {
                selectViewId = R.id.tv_kiosk_order;
            } else {
                selectViewId = R.id.tv_hold_kitchen_order;
            }
        }
        lv_order_list = (ListView) findViewById(R.id.lv_order_list);
        lv_orderdetail_list = (ListView) findViewById(R.id.lv_orderdetail_list);
        tv_hold_order = (TextView) findViewById(R.id.tv_hold_order);
        tv_hold_kitchen_order = (TextView) findViewById(R.id.tv_hold_kitchen_order);
        tv_kiosk_order = (TextView) findViewById(R.id.tv_kiosk_order);
        btn_close_w_cash = (Button) findViewById(R.id.btn_close_w_cash);
        btn_get_order = (Button) findViewById(R.id.btn_get_order);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        ll_remarks = (LinearLayout) findViewById(R.id.ll_remarks);
        tv_remarks = (TextView) findViewById(R.id.tv_remarks);
        tv_eat_type = (TextView) findViewById(R.id.tv_eat_type);
        et_search = (SearchView) findViewById(R.id.et_search);

//        et_search.setIconifiedByDefault(true);
        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 得到输入管理对象
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                    imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法                    }
                    et_search.clearFocus();
                }
                if (TextUtils.isEmpty(query)) {
                    refreshDataView();
                } else {
                    List<Order> orders = new ArrayList<Order>();
                    for (Order order : orderCache) {
                        if (query.equals(order.getOrderNo().intValue() + "")) {
                            orders.add(order);
                        }
                    }
                    orderList = orders;
                    kioskHoldOderAdapter.notifyDataSetChanged(orderList);
                    kioskHoldOderDetailAdapter.notifyDataSetChanged(orderDetails);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    refreshDataView();
                    return true;
                }
                return false;
            }
        });
//        et_search.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                refreshDataView();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
//                    imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法                    }
//                    et_search.clearFocus();
//                }
//                return true;
//            }
//        });

        lv_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectOrderItem = arg2;
                refreshDataView();
            }
        });
        initData();
        kioskHoldOderAdapter = new KioskHoldOderAdapter();
        kioskHoldOderAdapter.setSelfOrderList(orderList);
        lv_order_list.setAdapter(kioskHoldOderAdapter);
        kioskHoldOderDetailAdapter = new KioskHoldOderDetailAdapter();
        kioskHoldOderDetailAdapter.setSelfOrderDetailList(orderDetails);
        lv_orderdetail_list.setAdapter(kioskHoldOderDetailAdapter);
        tv_hold_order.setOnClickListener(this);
        tv_hold_kitchen_order.setOnClickListener(this);
        tv_kiosk_order.setOnClickListener(this);
        tv_hold_order.setOnLongClickListener(this);
        tv_hold_kitchen_order.setOnLongClickListener(this);
        tv_kiosk_order.setOnLongClickListener(this);
        btn_close_w_cash.setOnClickListener(this);
        btn_get_order.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        findViewById(R.id.ll_search).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        hasOrder = true;
        currentOrder = null;
        super.onDestroy();
    }

    private void initData() {
        tv_hold_order.setBackgroundColor(getResources().getColor(R.color.white));
        tv_hold_order.setTextColor(getResources().getColor(R.color.black));
        tv_hold_kitchen_order.setBackgroundColor(getResources().getColor(R.color.white));
        tv_hold_kitchen_order.setTextColor(getResources().getColor(R.color.black));
        tv_kiosk_order.setBackgroundColor(getResources().getColor(R.color.white));
        tv_kiosk_order.setTextColor(getResources().getColor(R.color.black));
        //int count = OrderSQL.getKioskHoldCount(App.instance.getBusinessDate(), App.instance.getSessionStatus(), nowTime);
        int holdNum = OrderSQL.getKioskHoldCountByStatus(
                App.instance.getBusinessDate(),
                App.instance.getSessionStatus(),
                System.currentTimeMillis(),
                ParamConst.ORDER_STATUS_HOLD
        );
        int holdKitchenNum = OrderSQL.getKioskHoldCountByStatus(
                App.instance.getBusinessDate(),
                App.instance.getSessionStatus(),
                System.currentTimeMillis(),
                ParamConst.ORDER_STATUS_HOLD_KITCHEN
        );
        int kioskNum = OrderSQL.getKioskHoldCountByStatus(
                App.instance.getBusinessDate(),
                App.instance.getSessionStatus(),
                System.currentTimeMillis(),
                ParamConst.ORDER_STATUS_KIOSK
        );
        String holdOrderStr = context.getString(R.string.hold_order);
        String holdKitchenStr = context.getString(R.string.hold_kitchen);
        String kioskOrderStr = context.getString(R.string.kiosk_order);
        tv_hold_order.setText(holdNum > 0 ? String.format(Locale.US,holdOrderStr + " (*%d)", holdNum) : holdOrderStr);
        tv_hold_kitchen_order.setText(holdKitchenNum > 0 ? String.format(Locale.US,holdKitchenStr + " (*%d)", holdKitchenNum) : holdKitchenStr);
        tv_kiosk_order.setText(kioskNum > 0 ? String.format(Locale.US,kioskOrderStr + " (*%d)", kioskNum) : kioskOrderStr);
        int orderStatus = ParamConst.ORDER_STATUS_HOLD_KITCHEN;
        switch (selectViewId) {
            case R.id.tv_hold_order:
                orderStatus = ParamConst.ORDER_STATUS_HOLD;
                tv_hold_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_hold_order.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_hold_kitchen_order:
                orderStatus = ParamConst.ORDER_STATUS_HOLD_KITCHEN;
                tv_hold_kitchen_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_hold_kitchen_order.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_kiosk_order:
                orderStatus = ParamConst.ORDER_STATUS_KIOSK;
                tv_kiosk_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_kiosk_order.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        long nowTime = System.currentTimeMillis();
        orderList = OrderSQL.getOrderByStatus(orderStatus, App.instance.getSessionStatus(), nowTime);
        orderCache = orderList;
        if (orderList != null && orderList.size() > 0) {
            if (selectOrderItem >= orderList.size() || selectOrderItem < 0) {
                selectOrderItem = 0;
            }
            Order order = orderList.get(selectOrderItem);
            orderDetails = OrderDetailSQL.getOrderDetails(order.getId().intValue());
            if (orderDetails != null && orderDetails.size() > 0) {
                ll_orderdetail_layout.setVisibility(View.VISIBLE);
            } else {
                ll_orderdetail_layout.setVisibility(View.INVISIBLE);
            }
            if (TextUtils.isEmpty(order.getOrderRemark())) {
                ll_remarks.setVisibility(View.GONE);
            } else {
                ll_remarks.setVisibility(View.VISIBLE);
                tv_remarks.setText(order.getOrderRemark());
            }
            if (order.getIsTakeAway().intValue() == ParamConst.TAKE_AWAY) {
                tv_eat_type.setText(this.getString(R.string.takeaway));
            } else {
                tv_eat_type.setText(this.getString(R.string.app_dine_in));
            }
        } else {
            ll_orderdetail_layout.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.tv_hold_order:
            case R.id.tv_hold_kitchen_order:
            case R.id.tv_kiosk_order:
                selectViewId = v.getId();
                et_search.setQuery("", false);
                refreshDataView();
                break;
            case R.id.btn_close_w_cash: {
                Order order = orderList.get(selectOrderItem);
                if (order != null && order.getId() != null) {
                    cashPay(order);

                }
            }
            break;
            case R.id.btn_get_order: {
                if (hasOrder) {
                    ToastUtils.showToast(context, context.getString(R.string.close_the_last_order));
                } else {
                    Order order = orderList.get(selectOrderItem);
                    if (order != null && order.getId() != null) {
                        if (currentOrder != null) {
                            OrderSQL.deleteOrder(currentOrder);
                        }
                        OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS, order.getId().intValue());
                        long nowTime = System.currentTimeMillis();
                        int count = OrderSQL.getKioskHoldCount(App.instance.getBusinessDate(), App.instance.getSessionStatus(), nowTime);
                        App.instance.setKioskHoldNum(count);
                        this.finish();
                    }
                }
            }
            break;
            case R.id.btn_refresh:
                refreshDataView();
                break;
            case R.id.ll_search:
                et_search.requestFocus();
                et_search.onActionViewExpanded();
                break;
        }

    }

    private void refreshDataView() {
        initData();
        kioskHoldOderAdapter.notifyDataSetChanged(orderList);
        kioskHoldOderDetailAdapter.notifyDataSetChanged(orderDetails);
    }

    private void cashPay(final Order order) {
        loadingDialog.show();
        List<OrderDetail> orderDetailList = OrderDetailSQL.getAllOrderDetailsByOrder(order);
        if (orderDetailList.isEmpty()) {
            UIHelp.showShortToast(context, getResources().getString(R.string.no_order_detail));
            return;
        }
        OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                order, App.instance.getRevenueCenter());
        OrderBillSQL.add(orderBill);
        final Payment payment = ObjectFactory.getInstance().getPayment(order, orderBill);
        BigDecimal remainTotalAfterRound = RoundUtil.getPriceAfterRound(App.instance.getLocalRestaurantConfig().getRoundType(), BH.getBD(order.getTotal()));
        RoundAmount roundAmount = ObjectFactory.getInstance().getRoundAmount(order, orderBill, BH.getBD(order.getTotal()), App.instance.getLocalRestaurantConfig().getRoundType());
        order.setOrderStatus(ParamConst.ORDER_STATUS_FINISHED);
        OrderHelper.setOrderTotalAlfterRound(order, roundAmount);
        OrderSQL.update(order);
        PaymentSettlement paymentSettlement = ObjectFactory.getInstance()
                .getPaymentSettlement(payment, ParamConst.SETTLEMENT_TYPE_CASH, remainTotalAfterRound.toString());
        PaymentSQL.updatePaymentAmount(order.getTotal(), order.getId().intValue());
        PaymentSettlementSQL.addPaymentSettlement(paymentSettlement);
        final int paidOrderId = order.getId();
        new Thread(new Runnable() {

            @Override
            public void run() {
                Order placedOrder = OrderSQL.getOrder(order.getId());
                List<OrderDetail> placedOrderDetails
                        = OrderDetailSQL.getOrderDetailsForPrint(placedOrder.getId());
                KotSummary kotSummary = ObjectFactory.getInstance()
                        .getKotSummary(
                                TableInfoSQL.getTableById(
                                        placedOrder.getTableId()).getName(), placedOrder,
                                App.instance.getRevenueCenter(),
                                App.instance.getBusinessDate());
                ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                List<Integer> orderDetailIds = new ArrayList<Integer>();
                ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                String kotCommitStatus = ParamConst.JOB_NEW_KOT;
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
                                                        orderDetail.getItemId(),
                                                        orderDetail.getItemName()),
                                        kotSummary,
                                        App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                        kotItemDetail.setItemNum(orderDetail
                                .getItemNum());
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
                            }
                        }
                    }
                }
                KotSummarySQL.update(kotSummary);
                PaymentSettlementSQL.deleteAllNoActiveSettlement(payment);
                final HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId", String.valueOf(paidOrderId));
                map.put("paymentId", String.valueOf(payment.getId().intValue()));
                map.put("isPrint", App.instance.getSystemSettings().isCashClosePrint() + "");
//                handler.sendMessage(handler.obtainMessage(
//                        MainPage.VIEW_EVENT_CLOSE_BILL, map));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        Intent intent = new Intent();
                        intent.putExtra("map", map);
                        setResult(CHECK_RESULT_CODE, intent);
                        long nowTime = System.currentTimeMillis();
                        int count = OrderSQL.getKioskHoldCount(App.instance.getBusinessDate(), App.instance.getSessionStatus(), nowTime);
                        App.instance.setKioskHoldNum(count);
                        KioskHoldActivity.this.finish();
                    }
                });
            }
        }).start();
    }

    @Override
    public void httpRequestAction(int action, Object obj) {
        if (obj == this) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (selectViewId == R.id.tv_kiosk_order) {
                        refreshDataView();
                    }
                }
            });
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hold_order:
                DialogFactory.commonTwoBtnDialog(context, getString(R.string.warning), context.getString(R.string.default_view),
                        getString(R.string.cancel), getString(R.string.ok), null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Store.putInt(context, Store.DEFAULT_VIEW, ParamConst.ORDER_STATUS_HOLD);
                            }
                        });

                break;
            case R.id.tv_hold_kitchen_order:
                DialogFactory.commonTwoBtnDialog(context, getString(R.string.warning), context.getString(R.string.default_view),
                        getString(R.string.cancel), getString(R.string.ok), null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Store.putInt(context, Store.DEFAULT_VIEW, ParamConst.ORDER_STATUS_HOLD_KITCHEN);
                            }
                        });
                break;
            case R.id.tv_kiosk_order:
                DialogFactory.commonTwoBtnDialog(context, getString(R.string.warning), context.getString(R.string.default_view),
                        getString(R.string.cancel), getString(R.string.ok), null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Store.putInt(context, Store.DEFAULT_VIEW, ParamConst.ORDER_STATUS_KIOSK);
                            }
                        });
                break;
        }
        return false;
    }

    class KioskHoldOderAdapter extends BaseAdapter {
        private List<Order> selfOrderList = new ArrayList<>();

        public void setSelfOrderList(List<Order> orderList) {
            selfOrderList.clear();
            selfOrderList.addAll(orderList);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return selfOrderList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return selfOrderList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public void notifyDataSetChanged(List<Order> orderList) {
            selfOrderList.clear();
            selfOrderList.addAll(orderList);
            super.notifyDataSetChanged();

        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            HolderView holder = null;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.temp_order_item, null);
                holder = new HolderView();
                holder.tv_order_id = (TextView) arg1.findViewById(R.id.tv_order_id);
                holder.tv_order_status = (TextView) arg1.findViewById(R.id.tv_order_status);
                holder.tv_order_type = (TextView) arg1.findViewById(R.id.tv_order_type);
                holder.tv_place_time = (TextView) arg1.findViewById(R.id.tv_place_time);
                textTypeFace.setTrajanProRegular(holder.tv_order_id);
                textTypeFace.setTrajanProRegular(holder.tv_order_type);
                textTypeFace.setTrajanProRegular(holder.tv_place_time);
                textTypeFace.setTrajanProRegular(holder.tv_order_status);
                arg1.setTag(holder);
            } else {
                holder = (HolderView) arg1.getTag();
            }

            Order order = selfOrderList.get(arg0);
            if (arg0 == selectOrderItem) {
                arg1.setBackgroundColor(getResources().getColor(R.color.brownness));
//                btn_check.setTag(order);
//                btn_cancel.setTag(order);
//				if(appOrder.getTableType().intValue() == ParamConst.APP_ORDER_TABLE_STATUS_USED){
//					btn_check.setVisibility(View.VISIBLE);
//				}else{
//					btn_check.setVisibility(View.INVISIBLE);
//				}
                holder.tv_order_id.setTextColor(getResources().getColor(R.color.white));
                holder.tv_order_status.setTextColor(getResources().getColor(R.color.white));
                holder.tv_order_type.setTextColor(getResources().getColor(R.color.white));
                holder.tv_place_time.setTextColor(getResources().getColor(R.color.white));
            } else {
                arg1.setBackgroundColor(getResources().getColor(R.color.white));
                holder.tv_order_id.setTextColor(getResources().getColor(R.color.black));
                holder.tv_order_status.setTextColor(getResources().getColor(R.color.black));
                holder.tv_order_type.setTextColor(getResources().getColor(R.color.black));
                holder.tv_place_time.setTextColor(getResources().getColor(R.color.black));
            }
            holder.tv_order_id.setText(order.getOrderNo() + "");
            String statusStr = "";
            switch (order.getOrderStatus().intValue()) {
                case ParamConst.ORDER_STATUS_HOLD:
                    statusStr = context.getString(R.string.hold_order);
                    break;
                case ParamConst.ORDER_STATUS_KIOSK:
                    statusStr = context.getString(R.string.kiosk_order);
                    break;
                default:
                    break;
            }
            holder.tv_order_type.setText(statusStr);
            holder.tv_order_status.setText(TimeUtil.getCloseBillDataTime(order.getCreateTime()));
            holder.tv_place_time.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(order.getTotal()));
            return arg1;
        }

        class HolderView {
            public TextView tv_order_id;
            public TextView tv_order_status;
            public TextView tv_order_type;
            public TextView tv_place_time;
        }
    }

    class KioskHoldOderDetailAdapter extends BaseAdapter {
        private List<OrderDetail> selfOrderDetailList = new ArrayList<>();

        public void setSelfOrderDetailList(List<OrderDetail> orderDetailList) {
            selfOrderDetailList.clear();
            selfOrderDetailList.addAll(orderDetailList);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return selfOrderDetailList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return selfOrderDetailList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }


        public void notifyDataSetChanged(List<OrderDetail> orderDetailList) {
            selfOrderDetailList.clear();
            selfOrderDetailList.addAll(orderDetailList);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            HolderView holder = null;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.temp_orderdetail_item, null);
                holder = new HolderView();
                holder.tv_orderdetail_name = (TextView) arg1.findViewById(R.id.tv_orderdetail_name);
                holder.tv_orderdetail_qty = (TextView) arg1.findViewById(R.id.tv_orderdetail_qty);
                holder.tv_temp_modifier = (TextView) arg1.findViewById(R.id.tv_temp_modifier);
                textTypeFace.setTrajanProRegular(holder.tv_orderdetail_name);
                textTypeFace.setTrajanProRegular(holder.tv_orderdetail_qty);
                textTypeFace.setTrajanProRegular(holder.tv_temp_modifier);
                arg1.setTag(holder);
            } else {
                holder = (HolderView) arg1.getTag();
            }
            OrderDetail orderDetail = selfOrderDetailList.get(arg0);
            holder.tv_orderdetail_name.setText(orderDetail.getItemName());
            holder.tv_orderdetail_qty.setText(orderDetail.getItemNum() + "");
            List<OrderModifier> orderModifiers = OrderModifierSQL.getOrderModifiersByOrderDetailId(orderDetail.getId().intValue());
            StringBuffer modifierNames = new StringBuffer();
            if (!TextUtils.isEmpty(orderDetail.getReason())) {
                modifierNames.append(orderDetail.getReason());
            }
            for (OrderModifier orderModifier : orderModifiers) {
                if (modifierNames.length() != 0) {
                    modifierNames.append(",");
                }
                String modifierName = CoreData.getInstance().getModifier(orderModifier.getModifierId().intValue()).getModifierName();
                modifierNames.append(modifierName);
            }
            holder.tv_temp_modifier.setText(modifierNames.toString());
            return arg1;
        }

        class HolderView {
            public TextView tv_orderdetail_name;
            public TextView tv_orderdetail_qty;
            public TextView tv_temp_modifier;
        }
    }

}
