package com.alfredwaiter.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.store.Store;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.store.sql.temporaryforapp.TempOrderSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.VibrationUtil;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.SyncCentre;
import com.alfredwaiter.global.UIHelp;
import com.alfredwaiter.javabean.ModifierCPVariance;
import com.alfredwaiter.popupwindow.SelectGroupWindow;
import com.alfredwaiter.popupwindow.WaiterModifierCPWindow;
import com.alfredwaiter.utils.WaiterUtils;
import com.alfredwaiter.view.MoneyKeyboard;
import com.alfredwaiter.view.MoneyKeyboard.KeyBoardClickListener;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class OrderDetailsTotal extends BaseActivity implements KeyBoardClickListener {
    public static final int VIEW_EVENT_SELECT_GROUP = 0;
    public static final int PLACE_ORDER_RESULT_CODE = 1;
    public static final int VIEW_EVENT_GET_BILL = 2;
    public static final int VIEW_EVENT_GET_BILL_FAILED = 3;
    public static final int VIEW_EVENT_PRINT_BILL = 4;
    public static final int VIEW_EVENT_PRINT_BILL_FAILED = 5;
    public static final int VIEW_EVENT_GET_PRINT_LIST = 6;
    public static final int VIEW_EVENT_GET_PRINT_LIST_FAILED = 7;
    public static final int GET_PRINT_KOT_DATA_SUCCESS = 8;
    public static final int GET_PRINT_KOT_DATA_FAILED = 9;
    public static final int VIEW_EVENT_PRINT_KOT_SUCCESS = 10;
    public static final int VIEW_EVENT_PRINT_KOT_FAILED = 11;

    private static final int DURATION_1 = 300;
    private ListView lv_dishes;
    private Order currentOrder;
    private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
    private RelativeLayout rl_content;
    private RelativeLayout rl_countKeyboard;
    private TextView tv_group;
    private TextView tv_place_order;
    private Button btn_get_bill;
    private Button btn_print_bill;
    private Button btn_reprint_kot;
    private SelectGroupWindow selectGroupWindow;
    private int groupId = -1;
    private OrderDetailListAdapter adapter;
    private TextView tv_item_count;
    private TextView tv_sub_total;
    private TextView tv_discount;
    private TextView tv_taxes,tv_promotion;
    private TextView tv_grand_total;
    private ImageView iv_add;
    private List<OrderDetail> newOrderDetails = new ArrayList<OrderDetail>();
    private List<OrderDetail> oldOrderDetails = new ArrayList<OrderDetail>();
    private StringBuffer buffer = new StringBuffer();
    private DismissCall dismissCall;
    private LinearLayout ll_bill_action;
    private WaiterModifierCPWindow modifierWindow;
    private Observable<String> observable;
    private OrderDetail selectedOrderDetail;
    private PrinterDevice selectedPrinter;
    private boolean isOpenScreen;
    private boolean isRePrintKOT;
    private boolean isPrintLocal;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_order_detail_total);
        getIntentData();
        lv_dishes = (ListView) findViewById(R.id.lv_dishes);
        modifierWindow = new WaiterModifierCPWindow(context, handler, findViewById(R.id.rl_root));
        tv_place_order = (TextView) findViewById(R.id.tv_place_order);
        btn_get_bill = (Button) findViewById(R.id.btn_get_bill);
        btn_print_bill = (Button) findViewById(R.id.btn_print_bill);
        btn_reprint_kot = (Button) findViewById(R.id.btn_reprint_kot);
        loadingDialog = new LoadingDialog(context);
        adapter = new OrderDetailListAdapter(context);
        lv_dishes.setAdapter(adapter);
        lv_dishes.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        tv_item_count = (TextView) findViewById(R.id.tv_item_count);
        tv_sub_total = (TextView) findViewById(R.id.tv_sub_total);
        tv_discount = (TextView) findViewById(R.id.tv_discount);
        tv_taxes = (TextView) findViewById(R.id.tv_taxes);
        tv_promotion=(TextView) findViewById(R.id.tv_promotion) ;
        tv_grand_total = (TextView) findViewById(R.id.tv_grand_total);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);
        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_group.setOnClickListener(this);
        tv_place_order.setOnClickListener(this);
        btn_get_bill.setOnClickListener(this);
        btn_print_bill.setOnClickListener(this);
        btn_reprint_kot.setOnClickListener(this);
        ll_bill_action = (LinearLayout) findViewById(R.id.ll_bill_action);
        selectGroupWindow = new SelectGroupWindow(context, tv_group, handler);
        ((TextView) findViewById(R.id.tv_tables_name)).setText(TableInfoSQL.getTableById(currentOrder.getTableId())
                .getName());
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        rl_countKeyboard = (RelativeLayout) findViewById(R.id.rl_countKeyboard);
        rl_countKeyboard.setVisibility(View.GONE);
        MoneyKeyboard moneyKeyboard = (MoneyKeyboard) findViewById(R.id.countKeyboard);
        moneyKeyboard.setMoneyPanel(View.GONE);
        moneyKeyboard.setMoneyRoot(Color.BLACK);
        moneyKeyboard.setKeyBoardClickListener(this);
        refreshOrderTotal();
        refreshOrder();

        observable = RxBus.getInstance().register(RxBus.RX_REFRESH_ORDER);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String data) {
                if (currentOrder != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        int orderId = jsonObject.getInt("orderId");
                        if (orderId == currentOrder.getId().intValue()) {
                            loadOrder(TableInfoSQL.getTableById(currentOrder.getTableId().intValue()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GET_PRINT_KOT_DATA_SUCCESS:
                    printKOT(isRePrintKOT, isPrintLocal);
                    break;
                case GET_PRINT_KOT_DATA_FAILED:
                    //get data kot failed
                    break;
                case TablesPage.VIEW_EVENT_SELECT_TABLES:
                    dismissLoadingDialog();
                    currentOrder = (Order) msg.obj;
                    refreshList();
                    break;
                case VIEW_EVENT_SELECT_GROUP:
                    groupId = (Integer) msg.obj;
                    if (groupId < 0) {
                        tv_group.setText(getString(R.string.group_all));
                    } else if (groupId == 0) {
                        tv_group.setText(getString(R.string.group) + " ?");
                    } else {
                        tv_group.setText(getString(R.string.group) + " " + groupId);
                    }

                    refreshList();
                    break;
                case ResultCode.SUCCESS:
                    loadingDialog.dismiss();
                    if (currentOrder != null) {
                        currentOrder = OrderSQL.getOrder(currentOrder.getId());
                        currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS);
                        OrderSQL.update(currentOrder);
                    }
                    // if(orderDetails != null && !orderDetails.isEmpty()){
                    // for(int i = 0; i < orderDetails.size(); i++){
                    // orderDetails.get(i).setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD);
                    // }
                    // OrderDetailSQL.addOrderDetailList(orderDetails);
                    // }
                    orderDetails = OrderDetailSQL.getCreatedOrderDetails(currentOrder.getId());

                    getPrintKOTData();
                    refreshList();
                    UIHelp.showToast(context, context.getResources().getString(R.string.place_succ));

                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.place_failed));
                    break;
                case VIEW_EVENT_GET_BILL:
                    loadingDialog.dismiss();

                    if (!isOpenScreen)
                        printBill();
                    else
                        UIHelp.startOrderReceiptDetails(context, currentOrder);
                    break;
                case VIEW_EVENT_GET_BILL_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.network_errors));
                    break;
                case MainPage.TRANSFER_TABLE_NOTIFICATION:
                    WaiterUtils.showTransferTableDialog(context);
                    break;
                case ResultCode.ORDER_FINISHED:
                    loadingDialog.dismiss();
                    DialogFactory.showOneButtonCompelDialog(context,
                            context.getResources().getString(R.string.warn),
                            context.getResources().getString(R.string.order_closed), null);
                    break;

                case ResultCode.ORDER_PRINT:
                    loadingDialog.dismiss();
                    DialogFactory.showOneButtonCompelDialog(context,
                            context.getResources().getString(R.string.warn), "Bill Printed, please contact Cashier", null);
                    break;
                case ResultCode.NONEXISTENT_ORDER:
                    loadingDialog.dismiss();
                    DialogFactory.showOneButtonCompelDialog(context,
                            context.getResources().getString(R.string.warn),
                            context.getResources().getString(R.string.order_not_edited), null);
                    break;
                case ResultCode.ORDER_HAS_CLOSING:
                    DialogFactory.showOneButtonCompelDialog(context,
                            context.getResources().getString(R.string.warn),
                            context.getString(R.string.order_is_close_replace_new), null);
                    break;
                case ResultCode.ORDER_SPLIT_IS_SETTLED:
                    loadingDialog.dismiss();
                    int groupId = (Integer) msg.obj;
                    if (groupId == 0) {
                        DialogFactory.showOneButtonCompelDialog(context,
                                context.getResources().getString(R.string.warn),
                                context.getResources().getString(R.string.order_split_settled), null);
                    } else {
                        DialogFactory.showOneButtonCompelDialog(context,
                                context.getResources().getString(R.string.warn),
                                context.getResources().getString(R.string.order_split) + groupId +
                                        context.getResources().getString(R.string.settled), null);
                    }

                    break;

                case ResultCode.WAITER_OUT_OF_STOCK:
                    loadingDialog.dismiss();
                    String stockNum = (String) msg.obj;
                    UIHelp.showToast(OrderDetailsTotal.this, stockNum);

                    break;
                case VIEW_EVENT_PRINT_BILL:
                    UIHelp.showToast(context, context.getResources().getString(R.string.print_bill_succ));


                    break;
                case ResultCode.SUCCESS_WAITER_ONCE:
                    UIHelp.showToast(context, context.getResources().getString(R.string.print_bill_succ));
                    OrderSQL.updateWaiterPrint(1,currentOrder.getId());
                    break;
                case VIEW_EVENT_PRINT_BILL_FAILED:
                    UIHelp.showToast(context, context.getResources().getString(R.string.print_bill_failed));
                    break;
                case VIEW_EVENT_PRINT_KOT_SUCCESS:
                    UIHelp.showToast(context, context.getResources().getString(R.string.print_kot_succ));
                    break;
                case VIEW_EVENT_PRINT_KOT_FAILED:
                    UIHelp.showToast(context, context.getResources().getString(R.string.print_kot_failed));
                    break;
                case MainPage.VIEW_EVENT_ADD_ORDER_DETAIL_AND_MODIFIER:
                    if (selectedOrderDetail == null)
                        return;
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
//				ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
                    List<ModifierCPVariance> variances = (List<ModifierCPVariance>) map.get("variances");
                    String description = (String) map.get("description");
                    OrderDetail orderDetail = selectedOrderDetail;
                    addOrderDetailAndOrderModifier(orderDetail, orderDetail.getItemNum(), variances, description);
//				refreshTotal();
                    refreshList();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void loadOrder(TableInfo tableInfo) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tables", tableInfo);
        loadingDialog.setTitle(context.getString(R.string.updating));
        loadingDialog.show();
        SyncCentre.getInstance().selectTables(context, parameters,
                handler);
    }

    private void addOrderDetailAndOrderModifier(OrderDetail orderDetail, int count, List<ModifierCPVariance> modifierIds, String description) {
        currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
        orderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
//		OrderDetailSQL.updateOrderDetailStatusById(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE, orderDetail.getItemId().intValue());
        OrderSQL.update(currentOrder);
//		OrderDetail orderDetail = ObjectFactory.getInstance()
//				.createOrderDetailForWaiter(currentOrder, itemDetail,
//						currentGroupId, App.instance.getUser());
        orderDetail.setItemNum(count);
        orderDetail.setSpecialInstractions(description);
        OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
        for (ModifierCPVariance modifierVariance : modifierIds) {
            Modifier modifier = CoreData.getInstance().getModifier(modifierVariance.getModifierId());
            OrderModifier orderModifier = new OrderModifier();
            orderModifier.setId(CommonSQL
                    .getNextSeq(TableNames.OrderModifier));
            orderModifier.setOrderId(currentOrder.getId());
            orderModifier.setOrderDetailId(orderDetail.getId());
            orderModifier
                    .setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
            orderModifier.setUserId(currentOrder.getUserId());
            orderModifier.setItemId(orderDetail.getItemId());
            orderModifier.setModifierId(modifier.getId());
            orderModifier.setModifierNum(1);
            orderModifier
                    .setStatus(ParamConst.ORDER_MODIFIER_STATUS_NORMAL);
            orderModifier.setModifierPrice(modifier.getPrice());
            Long time = System.currentTimeMillis();
            orderModifier.setCreateTime(time);
            orderModifier.setUpdateTime(time);
            OrderModifierSQL.addOrderModifierForWaiter(orderModifier);
        }
        OrderDetailSQL.addOrderDetailETCForWaiter(orderDetail);

    }

    private void refreshList() {
        orderDetails.clear();
        newOrderDetails.clear();
        oldOrderDetails.clear();
        if (groupId < 0) {
            newOrderDetails = OrderDetailSQL
                    .getOrderDetailByOrderIdAndOrderDetailStatus(
                            currentOrder.getId(),
                            ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
            oldOrderDetails = OrderDetailSQL
                    .getOrderDetailByOrderIdAndOrderDetailStatus(currentOrder
                            .getId());
        } else {
            newOrderDetails = OrderDetailSQL.getOrderDetails(currentOrder,
                    groupId, ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
            oldOrderDetails = OrderDetailSQL.getOrderDetailsUnZero(
                    currentOrder, groupId);
        }

        orderDetails.addAll(newOrderDetails);
        orderDetails.addAll(oldOrderDetails);
        adapter.notifyDataSetChanged();
        refreshOrderTotal();
        refreshOrder();
    }

    private void refreshOrderTotal() {
        List<OrderDetail> orderDetailList = OrderDetailSQL
                .getOrderDetails(currentOrder.getId());
        int itemCount = 0;
        if (!orderDetailList.isEmpty()) {
            for (OrderDetail orderDetail : orderDetailList) {
                itemCount += orderDetail.getItemNum();
            }
        }
        tv_item_count.setText(context.getResources().getString(R.string.item_count) + itemCount);
        tv_sub_total.setText(context.getResources().getString(R.string.subtotal) + " : " + App.instance.getCurrencySymbol()
                + BH.formatMoney(currentOrder.getSubTotal()));
        tv_discount.setText(context.getResources().getString(R.string.discount_) + App.instance.getCurrencySymbol()
                + BH.formatMoney(currentOrder.getDiscountAmount()));
        tv_taxes.setText(context.getResources().getString(R.string.taxes) +" : "+ App.instance.getCurrencySymbol() + BH.formatMoney(currentOrder.getTaxAmount()));
        tv_promotion.setText(context.getResources().getString(R.string.promotion) + App.instance.getCurrencySymbol() + BH.formatMoney(currentOrder.getPromotion()));

        BigDecimal  gTotal= BH.sub(BH.getBD(currentOrder.getTotal()),BH.getBD(currentOrder.getPromotion()),false);
        tv_grand_total.setText(context.getString(R.string.grand_total) + App.instance.getCurrencySymbol() + BH.formatMoney(gTotal.toString()));
    }

    private void getIntentData() {
        Intent intent = getIntent();
        currentOrder = (Order) intent.getExtras().get("order");
        currentOrder = OrderSQL.getOrder(currentOrder.getId());
        newOrderDetails = OrderDetailSQL
                .getOrderDetailByOrderIdAndOrderDetailStatus(
                        currentOrder.getId(),
                        ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
        oldOrderDetails = OrderDetailSQL
                .getOrderDetailByOrderIdAndOrderDetailStatus(currentOrder
                        .getId());
        // orderDetails = OrderDetailSQL.getOrderDetails(currentOrder);
        orderDetails.clear();
        orderDetails.addAll(newOrderDetails);
        orderDetails.addAll(oldOrderDetails);
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.tv_group: {
                int maxGroupId = OrderDetailSQL.getMaxGroupId(currentOrder);
                selectGroupWindow
                        .show(currentOrder.getPersons() > maxGroupId ? currentOrder
                                .getPersons() : maxGroupId);
                break;
            }
            case R.id.tv_place_order:
                // VibrationUtil.init(context);
                // VibrationUtil.playVibratorTwice();

                isRePrintKOT = false;

                String deviceName = Build.MODEL;
                if ("V1s-G".equalsIgnoreCase(deviceName)) {
                    isPrintLocal = true;
                } else {
                    isPrintLocal = false;
                }

                List<ModifierCheck> allModifierCheck = ModifierCheckSql.getAllModifierCheck(currentOrder.getId());

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
                        //  checkMap.put(modifierCheck.getItemName() + "," + modifierCheck.getModifierCategoryName(), modifierCheck.getNum() + "");
                        if (checkMap.containsKey(modifierCheck.getItemName())) {
//                                 if(checkMap.get(modifierCheck.getItemName()) !=null)
//                                 {
//                                    categorMap=checkMap.get(modifierCheck.getItemName());
//                                     categorMap.put(modifierCheck.getModifierCategoryId(),modifierCheck.getModifierCategoryName()+" 不能少于"+modifierCheck.getMinNum()+"种");
//                                     checkMap.put(modifierCheck.getItemName(),categorMap);
                            categorMap.put(modifierCheck.getModifierCategoryId(), modifierCheck.getModifierCategoryName() + " ");
                            checkMap.put(modifierCheck.getItemName(), categorMap);

                        } else {
                            categorMap = new HashMap<Integer, String>();
                            categorMap.put(modifierCheck.getModifierCategoryId(), modifierCheck.getModifierCategoryName() + " ");
                            checkMap.put(modifierCheck.getItemName(), categorMap);
                        }
                    }
                }
                if (checkMap.size() == 0) {
                    commitOrderToPOS();
                    //	UIHelp.showToast(context,"成功");
                } else {
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
//                      String val = (String) entry.getValue();
//                      checkbuf.append("不能少于"+val+"种 .");
                        }
                    }

                    UIHelp.showToast(context, checkbuf.toString());
                }


                //	commitOrderToPOS();
                break;
            case R.id.btn_get_bill: {
                isOpenScreen = true;
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters
                        .put("table",
                                TableInfoSQL.getTableById(
                                        currentOrder.getTableId()));
                parameters.put("order", currentOrder);
                SyncCentre.getInstance().getBillPrint(context, parameters, handler);
            }
            break;
            case R.id.btn_reprint_kot:
            case R.id.btn_print_bill: {
                final PrinterDevice printerDevice = Store.getObject(context, Store.WAITER_PRINTER_DEVICE, PrinterDevice.class);
                String str = getString(R.string.use_default_cashier_printer);
                if (printerDevice != null) {
                    str = getString(R.string.use_cashier_printer_ip, TextUtils.isEmpty(printerDevice.getPrinterName()) ? printerDevice.getName() : printerDevice.getPrinterName(), printerDevice.getIP());
                }

                isOpenScreen = false;
                isRePrintKOT = v.getId() == R.id.btn_reprint_kot;

                DialogFactory.commonTwoBtnDialog(context, getString(R.string.warning), str, getString(R.string.other), getString(R.string.ok).toUpperCase(),
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<Integer, PrinterDevice> printerDeviceMap = App.instance.getPrinterDevices();

                                DialogFactory.showSelectPrinterDialog(context, new DialogFactory.DialogCallBack() {
                                    @Override
                                    public void callBack(PrinterDevice printerDevice) {
                                        selectedPrinter = printerDevice;
                                        if ("V1s-G".equalsIgnoreCase(selectedPrinter.getName())) {//print local
                                            isPrintLocal = true;
                                            if (isRePrintKOT)
                                                getPrintKOTData();
                                            else
                                                getPrintBillData();
                                        } else {//print remote
                                            isPrintLocal = false;
                                            if (isRePrintKOT)
                                                printKOT(isRePrintKOT, isPrintLocal);
                                            else
                                                printBill();
                                        }

                                        Store.saveObject(context, Store.WAITER_PRINTER_DEVICE, printerDevice);
                                    }
                                }, printerDeviceMap);
                            }
                        },
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectedPrinter = printerDevice;
                                if (selectedPrinter != null &&
                                        "V1s-G".equalsIgnoreCase(selectedPrinter.getName())) {//print local
                                    isPrintLocal = true;
                                    if (isRePrintKOT)
                                        getPrintKOTData();
                                    else
                                        getPrintBillData();
                                } else {//print remote
                                    isPrintLocal = false;
                                    if (isRePrintKOT)
                                        printKOT(isRePrintKOT, isPrintLocal);
                                    else
                                        printBill();
                                }
                            }
                        }, true);
            }
            break;
            case R.id.iv_add:
                OpenMainPage();
                break;
            default:
                break;
        }
    }

    private void getPrintBillData() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("table",
                TableInfoSQL.getTableById(
                        currentOrder.getTableId()));
        parameters.put("order", currentOrder);
        SyncCentre.getInstance().getBillPrint(context, parameters, handler);
    }

    private void printBill() {
        if (currentOrder == null) return;
        if (selectedPrinter != null && "V1s-G".equalsIgnoreCase(selectedPrinter.getName())) {
            String tableName = TableInfoSQL.getTableById(
                    currentOrder.getTableId()).getName();

            RevenueCenter rvc = App.instance.getRevenueCenter();
            User user = App.instance.getUser();
            String firstName = user != null ? user.getFirstName() : "";
            String lastName = user != null ? user.getLastName() : "";

            PrinterTitle title = ObjectFactory.getInstance()
                    .getPrinterTitle(
                            rvc,
                            currentOrder,
                            firstName
                                    + lastName,
                            tableName, 1, 0);

            ArrayList<PrintOrderItem> orderItems = ObjectFactory
                    .getInstance().getItemList(
                            OrderDetailSQL.getOrderDetails(currentOrder
                                    .getId()));

            ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                    .getInstance().getItemModifierList(currentOrder, OrderDetailSQL.getOrderDetails(currentOrder
                            .getId()));

            List<Map<String, String>> taxMap = OrderDetailTaxSQL
                    .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), currentOrder);

            App.instance.printBill(selectedPrinter, title, currentOrder, orderItems, orderModifiers, taxMap, null, null, false);

        } else {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderId", currentOrder.getId().intValue());
            parameters.put("tableName", TableInfoSQL.getTableById(
                    currentOrder.getTableId()).getName());

            if (selectedPrinter != null && selectedPrinter.getDevice_id() > 0) {
                parameters.put("deviceId", selectedPrinter.getDevice_id());
            }
            SyncCentre.getInstance().printBill(context, parameters, handler);
        }

    }

    private void printKOT(boolean isRePrintKOT, boolean isPrintLocal) {
        if (currentOrder == null) return;

        if (isPrintLocal) {
            KotSummary kotsummary = KotSummarySQL.getKotSummary(currentOrder.getId(), currentOrder.getNumTag());

            if (kotsummary == null) return;

            PrinterDevice printerDevice = App.instance.getLocalPrinterDevice();
            ArrayList<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByOrderId(kotsummary.getOrderId());
            ArrayList<KotItemModifier> kotItemModifiers = KotItemModifierSQL.getAllKotItemModifier();
            ArrayList<KotItemDetail> kotItemDetailsNonVoid = new ArrayList<>();
            ArrayList<KotItemDetail> printKOTItemDetails = new ArrayList<>();

            for (KotItemDetail kotItemDetail : kotItemDetails) {
                if (kotItemDetail.getKotStatus() != ParamConst.KOT_STATUS_VOID) {
                    kotItemDetailsNonVoid.add(kotItemDetail);
                }
            }

            if (isRePrintKOT) {
                printKOTItemDetails.addAll(kotItemDetailsNonVoid);
            } else {
                if (App.instance.getNewOrderDetail() != null) {

                    for (OrderDetail orderDetail : App.instance.getNewOrderDetail()) {
                        for (KotItemDetail kotItemDetail : kotItemDetailsNonVoid) {
                            int orderDetailId = kotItemDetail.getOrderDetailId();

                            if (orderDetailId == orderDetail.getId()) {
                                printKOTItemDetails.add(kotItemDetail);
                                break;
                            }
                        }
                    }
                } else {
                    printKOTItemDetails.addAll(kotItemDetailsNonVoid);
                }
            }

            App.instance.getNewOrderDetail().clear();
            App.instance.printKOT(printerDevice, kotsummary, printKOTItemDetails, kotItemModifiers);
        } else {//print remote
            if (isRePrintKOT) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("orderId", currentOrder.getId().intValue());
                SyncCentre.getInstance().rePrintKOT(context, parameters, handler);
            }
        }
    }

    private void getPrintKOTData() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("order", currentOrder);
        SyncCentre.getInstance().getPrintKOTData(context, parameters, handler);
    }

    @Override
    public void onBackPressed() {
        //OpenMainPage();
        //this.
        super.onBackPressed();
    }

    private void OpenMainPage() {
//		UIHelp.startMainPage(context, currentOrder);
        if (BaseApplication.instance.getIndexOfActivity(MainPage.class) != -1) {
            this.finish();
        } else {
            UIHelp.startMainPage(context, currentOrder);
            this.finish();
        }

    }

    private void updateOrderDetail(OrderDetail orderDetail, int count) {
        if (count <= 0) {// 删除
            OrderDetailSQL.deleteOrderDetail(orderDetail);
            OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
        } else {// 添加
            orderDetail.setItemNum(count);
            OrderDetailSQL.updateOrderDetailAndOrderForWaiter(orderDetail);
        }
        currentOrder = OrderSQL.getOrder(orderDetail.getOrderId());
    }

    private void commitOrderToPOS() {
        if (newOrderDetails == null || newOrderDetails.isEmpty()) {
            return;
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        List<OrderModifier> orderModifiers = OrderModifierSQL
                .getAllOrderModifier(currentOrder);
        for (OrderDetail orderDetail : newOrderDetails) {
            if (orderDetail.getIsFree() != ParamConst.FREE) {
                orderDetails.add(orderDetail);
            }
        }
        parameters.put("order", currentOrder);
        parameters.put("orderDetails", orderDetails);
        parameters.put("orderModifiers", orderModifiers);
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        loadingDialog.show();
        SyncCentre.getInstance().commitOrderAndOrderDetails(context,
                parameters, handler);
    }

    private void refreshOrder() {
        boolean showPlace = false;
        if (orderDetails != null && orderDetails.size() > 0) {
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE) {
                    showPlace = true;
                }
            }
        }
        if (currentOrder.getOrderStatus() >= ParamConst.ORDER_STATUS_OPEN_IN_POS) {
            tv_place_order.setVisibility(View.GONE);
            ll_bill_action.setVisibility(View.VISIBLE);
        } else {
            if (showPlace) {
                tv_place_order.setVisibility(View.VISIBLE);
                ll_bill_action.setVisibility(View.GONE);
            } else {
                tv_place_order.setVisibility(View.GONE);
                ll_bill_action.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Order mOrder = OrderSQL.getOrder(currentOrder.getId());
        if (mOrder == null) {
            WaiterUtils.showTransferTableDialog(context);
        }
        // refreshList();
        // refreshOrder();
    }

    public void httpRequestAction(int action, Object obj) {

        if (MainPage.TRANSFER_TABLE_NOTIFICATION == action) {
            Order mOrder = (Order) obj;
            if (mOrder.getId().intValue() == currentOrder.getId().intValue()) {
                handler.sendEmptyMessage(MainPage.TRANSFER_TABLE_NOTIFICATION);
            }
        }
    }

    ;

    @Override
    protected void onDestroy() {
        VibrationUtil.cancel();
        if (observable != null)
            RxBus.getInstance().unregister(RxBus.RX_REFRESH_ORDER, observable);
        super.onDestroy();
    }

    class OrderDetailListAdapter extends BaseAdapter {
        private LayoutInflater inflater;


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
        public View getView(final int position, View arg1, ViewGroup arg2) {
            ViewHolder holder = null;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.item_order_detail, null);
                holder = new ViewHolder();
                holder.ll_title = (LinearLayout) arg1
                        .findViewById(R.id.ll_title);
                holder.name = (TextView) arg1.findViewById(R.id.name);
                holder.price = (TextView) arg1.findViewById(R.id.price);
                holder.tv_qty = (TextView) arg1.findViewById(R.id.tv_qty);
                holder.subtotal = (TextView) arg1.findViewById(R.id.subtotal);
                holder.tv_modifier = (TextView) arg1.findViewById(R.id.tv_modifier);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }

            final OrderDetail orderDetail = orderDetails.get(position);
            final ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
                    orderDetail.getItemId());

            final List<OrderModifier> modifiers = OrderModifierSQL.getAllOrderModifierByOrderDetailAndNormal(orderDetail);
            StringBuffer stringBuffer = new StringBuffer();
            if (modifiers.size() > 0) {
                for (OrderModifier orderModifier : modifiers) {
                    Modifier modifier = ModifierSQL.getModifierById(orderModifier.getModifierId());
                    stringBuffer.append(modifier.getModifierName() + "  ");
                }
            }
            stringBuffer.append(orderDetail.getSpecialInstractions());
            if (!TextUtils.isEmpty(stringBuffer.toString())) {
                holder.tv_modifier.setVisibility(View.VISIBLE);
                holder.tv_modifier.setText(stringBuffer.toString());
            } else {
                holder.tv_modifier.setVisibility(View.GONE);
            }

            if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE) {
                holder.ll_title.setBackgroundColor(context.getResources()
                        .getColor(R.color.white));
                holder.tv_qty.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        final OrderDetail tag = (OrderDetail) arg0.getTag();
                        if (tag.getIsFree().intValue() == ParamConst.FREE) {
                            return;
                        }
                        final TextView textView = (TextView) arg0;
                        textView.setBackgroundColor(context.getResources()
                                .getColor(R.color.gray));
                        startAnimation(position);
                        buffer.delete(0, buffer.length());
                        dismissCall = new DismissCall() {

                            @Override
                            public void call(String key, int num) {
                                if (num == orderDetail.getItemNum()) {
                                    return;
                                }
                                if (num < 0)
                                    num = 0;
                                if (num > 999)
                                    num = 999;
                                textView.setText(num + "");
                                if (key.equals("Enter")) {
                                    textView.setBackgroundColor(context
                                            .getResources().getColor(
                                                    R.color.white));
                                    final int itemTempId = CoreData.getInstance().getItemDetailById(tag.getItemId()).getItemTemplateId();
                                    final RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);
                                    int detailNum = OrderDetailSQL.getOrderNotSubmitDetailCountByOrderIdAndItemDetailId(currentOrder.getId(), tag.getItemId());
                                    if (remainingStock != null) {
                                        if (remainingStock.getQty() >= detailNum) {
                                            int newNum = remainingStock.getQty() - detailNum + tag.getItemNum();
                                            if (num <= newNum) {
                                                updateOrderDetail(tag, num);
                                            } else {
                                                UIHelp.showToast(OrderDetailsTotal.this, OrderDetailsTotal.this.getString(R.string.out_of_stock));
                                            }
                                        } else {
                                            textView.setText(tag.getItemName() + "");
                                        }
                                    } else {
                                        updateOrderDetail(tag, num);
                                    }

                                    if (num == 0) {
                                        updateOrderDetail(tag, num);
                                        final ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
                                                orderDetail.getItemId());
                                        ModifierCheckSql.deleteModifierCheck(orderDetail.getId(), currentOrder.getId());
                                    }
                                    refreshList();
                                }
                            }
                        };
                    }
                });
            } else {
                holder.tv_qty.setOnClickListener(null);
                holder.tv_qty.setBackgroundColor(context.getResources()
                        .getColor(R.color.gray));
                holder.ll_title.setBackgroundColor(context.getResources()
                        .getColor(R.color.gray));
            }
            holder.name.setText(orderDetail.getItemName());
            holder.price.setText(App.instance.getCurrencySymbol()
                    + BH.formatMoney(orderDetail.getItemPrice()).toString());
            holder.tv_qty.setText(orderDetail.getItemNum() + "");
            holder.tv_qty.setTag(orderDetail);
            holder.subtotal.setText(App.instance.getCurrencySymbol()
                    + BH.formatMoney(orderDetail.getRealPrice()).toString());
            arg1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderDetail.getOrderDetailStatus().intValue() == ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE) {
                        List<Integer> modifierIds = new ArrayList<Integer>();
                        for (OrderModifier orderModifier : modifiers) {
                            modifierIds.add(orderModifier.getModifierId().intValue());
                        }
                        selectedOrderDetail = orderDetail;
                        modifierWindow.show(itemDetail, modifierIds, currentOrder, orderDetail.getSpecialInstractions() == null ? "" : orderDetail.getSpecialInstractions(), orderDetail);
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
            public LinearLayout ll_title;
            public TextView tv_modifier;
        }
    }

    public void startAnimation(final int position) {
        rl_countKeyboard.setVisibility(View.VISIBLE);
        lv_dishes.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lv_dishes.setSelection(position);
            }
        });
//		rl_content.post(new Runnable() {
//			@Override
//			public void run() {
//				if (AnimatorListenerImpl.isRunning) {
//					return;
//				}
//				AnimatorSet set = new AnimatorSet();
//				ObjectAnimator animator = ObjectAnimator.ofFloat(rl_content,
//						"y",rl_content.getY() + rl_countKeyboard.getHeight(),rl_content.getY())
//						.setDuration(2000);
//				set.play(animator); 
//				set.setInterpolator(new DecelerateInterpolator());
//				set.addListener(new AnimatorListenerImpl(){
//					@Override
//					public void onAnimationStart(Animator animation) {
//						// TODO Auto-generated method stub
//						super.onAnimationStart(animation);
//						
//					}
//				});
//				set.start();
//			}
//		});
    }

    public void endAnimation() {
        rl_countKeyboard.setVisibility(View.GONE);
//		rl_countKeyboard.post(new Runnable() {
//			@Override
//			public void run() {
//				if (AnimatorListenerImpl.isRunning) {
//					return;
//				}
//				AnimatorSet set = new AnimatorSet();
//				ObjectAnimator animator = ObjectAnimator.ofFloat(rl_content,
//						"y",rl_countKeyboard.getY(),rl_countKeyboard.getY() + rl_countKeyboard.getHeight())
//						.setDuration(2000);
//				set.play(animator);
//				set.setInterpolator(new DecelerateInterpolator());
//				set.addListener(new AnimatorListenerImpl(){
//					@Override
//					public void onAnimationEnd(Animator animation) {
//						// TODO Auto-generated method stub
//						super.onAnimationEnd(animation);
//						
//					}
//				});
//				set.start();
//			}
//		});
    }

    @Override
    public void onKeyBoardClick(String key) {
        BugseeHelper.buttonClicked(key);
        if ("Cancel".equals(key)) {
            endAnimation();
        } else if ("Enter".equals(key)) {
            endAnimation();
            if (buffer.length() > 0) {
                dismissCall.call(key, Integer.parseInt(buffer.toString()));
            } else {
                return;
            }
        } else if ("Clear".equals(key)) {
            buffer.delete(0, buffer.length());
            dismissCall.call(key, 0);
        } else {
            if (buffer.length() < 4) {
                buffer.append(key);
            }
            dismissCall.call(key, Integer.parseInt(buffer.toString()));
        }
    }

    public interface DismissCall {
        public void call(String key, int num);
    }
}
