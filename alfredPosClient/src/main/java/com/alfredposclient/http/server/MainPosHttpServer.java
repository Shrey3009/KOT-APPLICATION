package com.alfredposclient.http.server;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.APPConfig;
import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.http.APIName;
import com.alfredbase.http.AlfredHttpServer;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.HappyHour;
import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.KDSTracking;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.KotSummaryLog;
import com.alfredbase.javabean.LocalDevice;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.MultiOrderRelation;
import com.alfredbase.javabean.MultiReportRelation;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderDetailTax;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserOpenDrawerRecord;
import com.alfredbase.javabean.UserRestaurant;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.KotNotification;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.model.TableAndKotNotificationList;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.store.Store;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.HappyHourSQL;
import com.alfredbase.store.sql.HappyHourWeekSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.ItemHappyHourSQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.ItemModifierSQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotNotificationSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.MultiOrderRelationSQL;
import com.alfredbase.store.sql.MultiReportRelationSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.PrinterGroupSQL;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.store.sql.RestaurantConfigSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.RevenueCenterSQL;
import com.alfredbase.store.sql.SettlementRestaurantSQL;
import com.alfredbase.store.sql.SubPosBeanSQL;
import com.alfredbase.store.sql.SubPosCommitSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.TaxCategorySQL;
import com.alfredbase.store.sql.TaxSQL;
import com.alfredbase.store.sql.UserRestaurantSQL;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.store.sql.cpsql.CPOrderDetailSQL;
import com.alfredbase.store.sql.cpsql.CPOrderModifierSQL;
import com.alfredbase.store.sql.cpsql.CPOrderSQL;
import com.alfredbase.store.sql.cpsql.CPOrderSplitSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.KDSLogUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.StockCallBack;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.NetWorkOrderActivity;
import com.alfredposclient.activity.kioskactivity.KioskHoldActivity;
import com.alfredposclient.activity.kioskactivity.subpos.SubPosManagePage;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.javabean.MultiRVCPlacesDao;
import com.alfredposclient.jobs.CloudSyncJobManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moonearly.utils.service.TcpUdpFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainPosHttpServer extends AlfredHttpServer {
    public static final String MIME_JAVASCRIPT = "text/javascript";
    public static final String MIME_CSS = "text/css";
    public static final String MIME_JPEG = "image/jpeg";
    public static final String MIME_PNG = "image/png";
    public static final String MIME_SVG = "image/svg+xml";
    public static final String MIME_JSON = "application/json";
    public static final String RESULT_CODE = "resultCode";
    public static final Gson gson = new Gson();
    private String TAG = MainPosHttpServer.class.getSimpleName();
    private Object lockObject = new Object();

    public MainPosHttpServer() {
        super(APPConfig.HTTP_SERVER_PORT);
        KpmgResponseUtil.getInstance().init(this);
    }

    @Override
    public Response doGet(String uri, Method mothod, final Map<String, String> params, String body) {
        return super.doGet(uri, mothod, params, body);
    }

    @Override
    public Response doDesktopPost(String apiName, Method mothod, Map<String, String> params, String body) {
        LogUtil.d(TAG, "apiName : " + apiName + " body : " + body);
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return this.getForbiddenResponse("");
        }
        if (TextUtils.isEmpty(apiName)) {
            return this.getForbiddenResponse("");
        } else {
            if (apiName.equals(APIName.CALLNUM_ASSIGNREVENUE)) {
                try {
                    String ip = jsonObject.getString("ip");
                    App.instance.setCallAppIp(ip);
                    String header = Store.getString(App.instance, Store.CALL_NUM_HEADER);
                    String footer = Store.getString(App.instance, Store.CALL_NUM_FOOTER);
                    result.put(RESULT_CODE, ResultCode.SUCCESS);
                    result.put("calltype", App.instance.getSystemSettings().getCallStyle());
                    result.put("header", TextUtils.isEmpty(header) ? "" : header);
                    result.put("footer", TextUtils.isEmpty(footer) ? "" : footer);

                } catch (Exception e) {
                    result.put("resultCode", ResultCode.JSON_DATA_ERROR);
                }
                return this.getJsonResponse(new Gson().toJson(result));

            }

            int deviceType = jsonObject.optInt("deviceType");
            if (deviceType != 5) {
                result.put("resultCode", ResultCode.JSON_DATA_ERROR);
                return this.getJsonResponse(new Gson().toJson(result));
            }
            SessionStatus sessionStatus = App.instance.getSessionStatus();
            if (sessionStatus == null) {
                result.put("resultCode", ResultCode.SESSION_IS_CLOSED);
                return this.getJsonResponse(new Gson().toJson(result));
            }
            if (apiName.equals(APIName.DESKTOP_LOGIN)) {
                int employee_Id = 0;
                try {
                    String employeeId = jsonObject.optString("employeeId");
                    employee_Id = Integer.parseInt(employeeId);
                } catch (Exception e) {
                    result.put("resultCode", ResultCode.JSON_DATA_ERROR);
                }
                User user = CoreData.getInstance().getUserByEmpId(employee_Id);
                if (user != null) {
                    result.put("resultCode", ResultCode.SUCCESS);
                    result.put("revenue", App.instance.getRevenueCenter());
                    result.put("user", user);
                } else {
                    result.put("resultCode", ResultCode.USER_EMPTY);
                }
                return this.getJsonResponse(new Gson().toJson(result));
            } else {
                int revenueId = 0;
                int userId = 0;
                try {
                    revenueId = jsonObject.optInt("revenueId");
                    userId = jsonObject.optInt("userId");
                } catch (Exception e) {
                    result.put("resultCode", ResultCode.JSON_DATA_ERROR);
                    return this.getJsonResponse(new Gson().toJson(result));
                }
                User user = UserSQL.getUserById(userId);
                if (user == null) {
                    result.put("resultCode", ResultCode.USER_EMPTY);
                    return this.getJsonResponse(new Gson().toJson(result));
                }
                if (apiName.equals(APIName.DESKTOP_GETTABLE)) {
                    if (App.instance.isRevenueKiosk()) {
                        result.put("resultCode", ResultCode.REVENUE_IS_KIOSK);
                        return this.getJsonResponse(new Gson().toJson(result));
                    }
                    if (revenueId == App.instance.getRevenueCenter().getId().intValue()) {
                        List<PlaceInfo> placeList = PlaceInfoSQL.getAllPlaceInfo();
                        List<TableInfo> tableInfoList = TableInfoSQL.getAllTables();
                        result.put("resultCode", ResultCode.SUCCESS);
                        result.put("placeList", placeList);
                        result.put("tableList", tableInfoList);
                    } else {

                        result.put("resultCode", ResultCode.REVENUE_EMPLY);
                    }
                    return this.getJsonResponse(new Gson().toJson(result));
                } else if (apiName.equals(APIName.DESKTOP_PRINTBILL)) {
                    Response resp;
                    Gson gson = new Gson();
                    /*No waiter apps in kiosk mode */
                    if (App.instance.isRevenueKiosk()) {
                        result.put("resultCode", ResultCode.USER_NO_PERMIT);
                        result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                        resp = this.getJsonResponse(new Gson().toJson(result));
                        return resp;
                    }
                    try {
                        int orderId = jsonObject.optInt("orderId");
                        Order loadOrder = OrderSQL.getUnfinishedOrder(orderId);
                        if (loadOrder == null) {
                            result.put("resultCode", ResultCode.ORDER_FINISHED);
                            resp = this.getJsonResponse(new Gson().toJson(result));
                            return resp;
                        }
                        String tableName = jsonObject.getString("tableName");
                        if (orderId > 0) {
                            Order order = OrderSQL.getOrder(orderId);
                            if (order == null) {
                                result.put("resultCode", ResultCode.ORDER_NO_PLACE);
                                resp = this.getJsonResponse(new Gson().toJson(result));
                                return resp;
                            }
                            if (order.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                                result.put("resultCode", ResultCode.ORDER_FINISHED);
                                resp = this.getJsonResponse(new Gson().toJson(result));
                                return resp;
                            }
                            OrderBill orderBill = OrderBillSQL
                                    .getOrderBillByOrder(order);
                            if (orderBill == null) {
                                result.put("resultCode", ResultCode.ORDER_NO_PLACE);
                                resp = this.getJsonResponse(new Gson().toJson(result));
                                return resp;
                            }

                            PrinterTitle title = ObjectFactory.getInstance()
                                    .getPrinterTitle(
                                            App.instance.getRevenueCenter(),
                                            order,
                                            App.instance.getUser().getFirstName()
                                                    + App.instance.getUser()
                                                    .getLastName(),
                                            tableName, 1, App.instance.getSystemSettings().getTrainType());
                            ArrayList<PrintOrderItem> orderItems = ObjectFactory
                                    .getInstance().getItemList(
                                            OrderDetailSQL.getOrderDetails(order
                                                    .getId()));
                            ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                                    .getInstance().getItemModifierList(order, OrderDetailSQL.getOrderDetails(order
                                            .getId()));
                            List<Map<String, String>> taxMap = OrderDetailTaxSQL
                                    .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), order);
                            PrinterDevice printer = App.instance.getCahierPrinter();
                            App.instance.remoteBillPrint(printer, title, order,
                                    orderItems, orderModifiers, taxMap, null, null, null);
                            OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_UNPAY, orderId);
                        }
                        result.put("resultCode", ResultCode.SUCCESS);
                        resp = this.getJsonResponse(new Gson().toJson(result));

                    } catch (Exception e) {
                        e.printStackTrace();
                        resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
                    }
                    return resp;
                } else if (apiName.equals(APIName.DESKTOP_GETBILL)) {
                    Response resp;
                    try {
                        if (App.instance.isRevenueKiosk()) {
                            result.put("resultCode", ResultCode.REVENUE_IS_KIOSK);
                            return this.getJsonResponse(new Gson().toJson(result));
                        }
                        int tableId = jsonObject.optInt("tableId");
                        //Table status in waiter APP is not same that of table in POS
                        //need get latest status on app.
                        TableInfo tabInPOS = TableInfoSQL.getTableById(tableId);
                        if (tabInPOS == null) {
                            result.put("resultCode", ResultCode.JSON_DATA_ERROR);
                            resp = this.getJsonResponse(new Gson().toJson(result));
                            return resp;
                        }

                        App.getTopActivity().httpRequestAction(
                                MainPage.VIEW_EVNT_GET_BILL_PRINT, tabInPOS);

                        result.put("resultCode", ResultCode.SUCCESS);
                        resp = this.getJsonResponse(new Gson().toJson(result));
                    } catch (Exception e) {
                        e.printStackTrace();
                        resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
                    }
                    return resp;
                } else if (apiName.equals(APIName.DESKTOP_SELECTTABLE)) {
                    if (App.instance.isRevenueKiosk()) {
                        result.put("resultCode", ResultCode.REVENUE_IS_KIOSK);
                        return this.getJsonResponse(new Gson().toJson(result));
                    }
                    int tableId = 0;
                    int persons = 0;
                    int orderId = -1;
                    try {
                        tableId = jsonObject.optInt("tableId");
                        persons = jsonObject.optInt("persons");
                        if (!jsonObject.isNull("orderId")) {
                            orderId = jsonObject.optInt("orderId");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.put("resultCode", ResultCode.JSON_DATA_ERROR);
                    }
                    if (orderId != -1) {
                        Order order = OrderSQL.getOrder(orderId);
                        if (order != null && order.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                            result.put("resultCode", ResultCode.ORDER_FINISHED);
                            return this.getJsonResponse(new Gson().toJson(result));
                        }
                    }
                    if (tableId != 0 || persons != 0) {
                        TableInfo tableInfo = TableInfoSQL.getTableById(tableId);
                        if (tableInfo == null) {
                            result.put("resultCode", ResultCode.TABLE_EMPLY);
                            return this.getJsonResponse(new Gson().toJson(result));
                        }
                        tableInfo.setStatus(ParamConst.TABLE_STATUS_DINING);
                        TableInfoSQL.updateTables(tableInfo);
                        tableInfo.setPacks(persons);
//						Order order = ObjectFactory.getInstance().getOrder
                        Order order = ObjectFactory.getInstance().getOrder(
                                ParamConst.ORDER_ORIGIN_TABLE, App.instance.getSubPosBeanId(), tableInfo,
                                App.instance.getRevenueCenter(),
                                user,
                                App.instance.getSessionStatus(),
                                App.instance.getBusinessDate(),
                                App.instance.getIndexOfRevenueCenter(),
                                ParamConst.ORDER_STATUS_OPEN_IN_WAITER,
                                App.instance.getLocalRestaurantConfig()
                                        .getIncludedTax().getTax(), "");
                        List<OrderDetail> orderDetailListR = OrderDetailSQL.getAllOrderDetailsByOrder(order);
                        List<OrderModifier> orderModifierListR = OrderModifierSQL.getAllOrderModifier(order);
                        result.put("resultCode", ResultCode.SUCCESS);
                        result.put("order", order);
                        result.put("orderDetailList", orderDetailListR);
                        result.put("orderModifierList", orderModifierListR);
                        try {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("tableId", tableInfo.getPosId().intValue());
                            jsonObject1.put("status", ParamConst.TABLE_STATUS_DINING);
                            jsonObject1.put("RX", RxBus.RX_REFRESH_TABLE);
                            TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject1.toString(), null);
                            TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject1.toString(), null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        App.getTopActivity().httpRequestAction(
                                MainPage.REFRESH_TABLES_STATUS, tableInfo);
                    } else {
                        result.put("resultCode", ResultCode.JSON_DATA_ERROR);
                    }
                    return this.getJsonResponse(new Gson().toJson(result));
                } else if (apiName.equals(APIName.DESKTOP_GETITEM)) {
                    List<ItemMainCategory> itemMainCategoryList = ItemMainCategorySQL.getAllAvaiableItemMainCategoryInRevenueCenter();
                    List<ItemCategory> itemCategoryList = ItemCategorySQL.getAllItemCategory();
                    List<ItemDetail> itemDetailList = ItemDetailSQL.getAllItemDetail();
                    List<ItemModifier> itemModifierList = ItemModifierSQL.getAllItemModifier();
                    List<Modifier> modifierList = ModifierSQL.getAllModifier();

                    result.put("resultCode", ResultCode.SUCCESS);
                    result.put("itemMainCategoryList", itemMainCategoryList);
                    result.put("itemCategoryList", itemCategoryList);
                    result.put("itemDetailList", itemDetailList);
                    result.put("itemModifierList", itemModifierList);
                    result.put("modifierList", modifierList);
                    return this.getJsonResponse(new Gson().toJson(result));
                } else if (apiName.equals(APIName.DESKTOP_KIOSKORDER)) {
                    Response resp;
                    if (!App.instance.isRevenueKiosk()) {
                        result.put("resultCode", ResultCode.USER_NO_PERMIT);
                        result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                        return this.getJsonResponse(new Gson().toJson(result));
                    }
                    try {
                        Gson gson = new Gson();
                        Order order = ObjectFactory.getInstance().addOrderFromKioskDesktop(
                                ParamConst.ORDER_ORIGIN_TABLE, App.instance.getSubPosBeanId(),
                                TableInfoSQL.getKioskTable(),
                                App.instance.getRevenueCenter(),
                                user,
                                App.instance.getSessionStatus(),
                                App.instance.getBusinessDate(),
                                App.instance.getLocalRestaurantConfig().getIncludedTax().getTax());
                        ArrayList<OrderDetail> waiterOrderDetails = gson.fromJson(
                                jsonObject.optString("orderDetailList"),
                                new TypeToken<ArrayList<OrderDetail>>() {
                                }.getType());
                        ArrayList<OrderModifier> waiterOrderModifiers = gson.fromJson(
                                jsonObject.optString("orderModifierList"),
                                new TypeToken<ArrayList<OrderModifier>>() {
                                }.getType());
                        // waiter 过来的数据 存到 pos的DB中 不带Id存储
                        for (OrderDetail orderDetail : waiterOrderDetails) {
                            synchronized (lockObject) {
                                int oldOrderDetailId = orderDetail.getId();
                                ObjectFactory.getInstance().getOrderDetailFromKiosk(order, orderDetail);
                                OrderDetailSQL.addOrderDetailETCForWaiter(orderDetail);
                                if (waiterOrderModifiers != null
                                        && !waiterOrderModifiers.isEmpty()) {
                                    for (OrderModifier orderModifier : waiterOrderModifiers) {
                                        if (orderModifier.getOrderDetailId().intValue() == oldOrderDetailId) {
                                            Modifier modifier = ModifierSQL.getModifierById(orderModifier.getModifierId().intValue());
                                            OrderModifier localOrderModifier =
                                                    ObjectFactory.getInstance().getOrderModifier(order, orderDetail, modifier, 0);
                                            OrderModifierSQL
                                                    .updateOrderModifier(localOrderModifier);
                                        }
                                    }
                                }
                            }
                        }
                        order = OrderSQL.getOrder(order.getId().intValue());
                        result.put("resultCode", ResultCode.SUCCESS);
                        result.put("orderNo", order.getOrderNo());
                        resp = this.getJsonResponse(new Gson().toJson(result));
                        long nowTime = System.currentTimeMillis();
                        int count = OrderSQL.getKioskHoldCount(App.instance.getBusinessDate(), App.instance.getSessionStatus(), nowTime);
                        App.instance.setKioskHoldNum(count);
                        if (App.getTopActivity() != null) {
                            App.getTopActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone r = RingtoneManager.getRingtone(App.instance, notification);
                                    r.play();
                                }
                            });
                            if (App.getTopActivity() instanceof KioskHoldActivity) {
                                App.getTopActivity().httpRequestAction(
                                        MainPage.VIEW_EVENT_SET_DATA, App.getTopActivity());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.clear();
                        result.put("resultCode", ResultCode.ORDER_ERROR);
                        resp = this.getJsonResponse(new Gson().toJson(result));
                    }
                    return resp;
                } else if (apiName.equals(APIName.DESKTOP_COMMITORDER)) {
                    if (App.instance.isRevenueKiosk()) {
                        result.put("resultCode", ResultCode.REVENUE_IS_KIOSK);
                        return this.getJsonResponse(new Gson().toJson(result));
                    }
                    Response resp;
                    if (App.instance.isRevenueKiosk()) {
                        result.put("resultCode", ResultCode.USER_NO_PERMIT);
                        result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                        return this.getJsonResponse(new Gson().toJson(result));
                    }

                    try {
                        Gson gson = new Gson();
                        Order order = gson.fromJson(jsonObject.optJSONObject("order")
                                .toString(), Order.class);
                        Order loadOrder = OrderSQL.getUnfinishedOrder(order.getId());
                        if (loadOrder == null) {
                            result.put("resultCode", ResultCode.ORDER_FINISHED);
                            resp = this.getJsonResponse(new Gson().toJson(result));
                            return resp;
                        }
                        if ((App.instance.orderInPayment != null && App.instance.orderInPayment.getId().intValue() == order.getId().intValue())) {
                            result.put("resultCode", ResultCode.NONEXISTENT_ORDER);
                            resp = this.getJsonResponse(new Gson().toJson(result));
                            return resp;
                        }
                        if (App.instance.getClosingOrderId() == order.getId()) {
                            result.put("resultCode", ResultCode.ORDER_HAS_CLOSING);
                            resp = this.getJsonResponse(new Gson().toJson(result));
                            return resp;
                        }
                        ArrayList<OrderDetail> waiterOrderDetails = gson.fromJson(
                                jsonObject.optString("orderDetailList"),
                                new TypeToken<ArrayList<OrderDetail>>() {
                                }.getType());
                        ArrayList<OrderModifier> waiterOrderModifiers = gson.fromJson(
                                jsonObject.optString("orderModifierList"),
                                new TypeToken<ArrayList<OrderModifier>>() {
                                }.getType());
                        // 检测是否有 订单是已经完成的拆单中的
                        for (OrderDetail orderDetail : waiterOrderDetails) {
                            OrderSplit loadOrderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
                            if (loadOrderSplit != null && loadOrderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED) {
                                result.put("resultCode", ResultCode.ORDER_FINISHED);
                                resp = this.getJsonResponse(new Gson().toJson(result));
                                return resp;
                            }
                        }

                        // waiter 过来的数据 存到 pos的DB中 不带Id存储
                        for (OrderDetail orderDetail : waiterOrderDetails) {
                            synchronized (lockObject) {
                                OrderDetail orderDetailFromPOS = OrderDetailSQL
                                        .getOrderDetailByCreateTime(
                                                System.currentTimeMillis(),
                                                orderDetail.getOrderId());
                                if (orderDetailFromPOS != null) {
                                    continue;
                                } else {

                                    int orderDetailId = CommonSQL
                                            .getNextSeq(TableNames.OrderDetail);
                                    int oldOrderDetailId = orderDetail.getId();
                                    orderDetail
                                            .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
                                    orderDetail.setId(orderDetailId);
                                    OrderDetailSQL.addOrderDetailETC(orderDetail);
                                    if (orderDetail.getGroupId().intValue() > 0) {
                                        OrderSplit orderSplit = ObjectFactory.getInstance().getOrderSplit(order, orderDetail.getGroupId(), App.instance.getLocalRestaurantConfig()
                                                .getIncludedTax().getTax());
                                        OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
                                        orderDetail.setOrderSplitId(orderSplit.getId());
                                        OrderDetailSQL.updateOrderDetail(orderDetail);
                                        OrderDetailTaxSQL.updateOrderSplitIdbyOrderDetail(orderDetail);
                                    }
                                    if (waiterOrderModifiers != null
                                            && !waiterOrderModifiers.isEmpty()) {
                                        for (OrderModifier orderModifier : waiterOrderModifiers) {
                                            if (orderModifier.getOrderDetailId().intValue() == oldOrderDetailId) {
                                                Modifier modifier = ModifierSQL.getModifierById(orderModifier.getModifierId().intValue());
                                                OrderModifier localOrderModifier =
                                                        ObjectFactory.getInstance().getOrderModifier(order, orderDetail, modifier, 0);
                                                OrderModifierSQL
                                                        .updateOrderModifier(localOrderModifier);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        order = OrderSQL.getOrder(order.getId().intValue());

                        order.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS);

                        //	当前Order未完成时更新状态
                        OrderSQL.updateUnFinishedOrderFromWaiter(order);
                        //	这边重新从数据中获取OrderDetail 不依赖于waiter过来的数据
                        List<OrderDetail> orderDetails = OrderDetailSQL
                                .getOrderDetails(order.getId());
                        if (!orderDetails.isEmpty()) {
                            Order placedOrder = OrderSQL.getOrder(order.getId());
                            if (placedOrder.getOrderNo().intValue() == 0) {
                                order.setOrderNo(OrderHelper.calculateOrderNo(order.getBusinessDate()));
                                OrderSQL.updateOrderNo(order);
                            }
                            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                                    order, App.instance.getRevenueCenter());
                            OrderBillSQL.add(orderBill);
                        }
                        String kotCommitStatus;
                        KotSummary kotSummary = ObjectFactory.getInstance().getKotSummary(
                                TableInfoSQL.getTableById(order.getTableId()).getName(),
                                order, App.instance.getRevenueCenter(), App.instance.getBusinessDate());
                        List<Integer> orderDetailIds = new ArrayList<Integer>();
                        ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                        ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                        kotCommitStatus = ParamConst.JOB_NEW_KOT;
                        for (OrderDetail orderDetail : orderDetails) {
                            if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_PREPARED) {
                                continue;
                            }
                            if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                                kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
                            } else {
                                KotItemDetail kotItemDetail = ObjectFactory
                                        .getInstance()
                                        .getKotItemDetail(
                                                order,
                                                orderDetail,
                                                CoreData.getInstance().getItemDetailById(
                                                        orderDetail.getItemId(), orderDetail.getItemName()),
                                                kotSummary, App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                                kotItemDetail.setItemNum(orderDetail.getItemNum());
                                if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UNDONE) {
                                    kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
                                    kotItemDetail
                                            .setKotStatus(ParamConst.KOT_STATUS_UPDATE);
                                }
                                KotItemDetailSQL.update(kotItemDetail);
                                kotItemDetails.add(kotItemDetail);
                                orderDetailIds.add(orderDetail.getId());
                                ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                                        .getOrderModifiers(order, orderDetail);
                                for (OrderModifier orderModifier : orderModifiers) {
                                    KotItemModifier kotItemModifier = ObjectFactory
                                            .getInstance().getKotItemModifier(
                                                    kotItemDetail,
                                                    orderModifier,
                                                    CoreData.getInstance().getModifier(
                                                            orderModifier.getModifierId()));
                                    KotItemModifierSQL.update(kotItemModifier);
                                    kotItemModifiers.add(kotItemModifier);
                                }
                            }
                        }
                        KotSummarySQL.update(kotSummary);
                        List<OrderDetail> orderDetailListR = OrderDetailSQL.getAllOrderDetailsByOrder(order);
                        List<OrderModifier> orderModifierListR = OrderModifierSQL.getAllOrderModifier(order);
                        result.put("order", order);
                        result.put("orderDetailList", orderDetailListR);
                        result.put("orderModifierList", orderModifierListR);
                        result.put("resultCode", ResultCode.SUCCESS);
                        resp = this.getJsonResponse(new Gson().toJson(result));
                        // App.getTopActivity().httpRequestAction(MainPage.WAITER_SEND_KDS,
                        // kotMap);
                        Map<String, Object> orderMap = new HashMap<String, Object>();
                        orderMap.put("orderId", order.getId());
                        orderMap.put("orderDetailIds", orderDetailIds);
                        if (!App.instance.isRevenueKiosk() && App.instance.getSystemSettings().isOrderSummaryPrint()) {
                            PrinterDevice printer = App.instance.getCahierPrinter();
                            if (printer == null) {
                                UIHelp.showToast(
                                        App.getTopActivity(), App.getTopActivity().getResources().getString(R.string.setting_printer));
                            } else {
                                App.instance.remoteOrderSummaryPrint(printer, kotSummary, kotItemDetails, kotItemModifiers);
                            }
                        }

                        App.instance.getKdsJobManager()
                                .tearDownKot(kotSummary, kotItemDetails, kotItemModifiers,
                                        kotCommitStatus, orderMap);
                        App.getTopActivity().httpRequestAction(
                                MainPage.VIEW_EVENT_SET_DATA, order.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.clear();
                        result.put("resultCode", ResultCode.ORDER_ERROR);
                        resp = this.getJsonResponse(new Gson().toJson(result));
                    }
                    return resp;
                } else {
                    return this.getNotFoundResponse();
                }
            }
        }
    }


    @Override
    public Response doKPMGPost(String apiName, Method mothod, Map<String, String> params, String body) {
        LogUtil.d(TAG, "apiName : " + apiName + " body : " + body);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return this.getForbiddenResponse("");
        }
        String appVersion = jsonObject.optString("appVersion");
        if (apiName == null || TextUtils.isEmpty(appVersion)) {
            return this.getForbiddenResponse("");
        }
        Map<String, Object> result = new HashMap<String, Object>();

        if (!App.instance.isRevenueKiosk()) {
            result.put(RESULT_CODE, ResultCode.IS_NOT_KIOSK);
            return this.getJsonResponse(gson.toJson(result));
        }
        if (!appVersion.endsWith(App.instance.VERSION)) {
            result.put("resultCode", ResultCode.APP_VERSION_UNREAL);
            result.put("posVersion", App.instance.VERSION);
            String value = MobclickAgent.getConfigParams(App.getTopActivity(), "updateVersion");
            result.put("versionUpdate", value);
            return this.getJsonResponse(new Gson().toJson(result));
        }
        if (App.instance.getSessionStatus() == null) {
            result.put("resultCode", ResultCode.SESSION_IS_CLOSED);
            return this.getJsonResponse(new Gson().toJson(result));
        }

        if (apiName.equals(APIName.KPMG_LOGIN)) {
            return KpmgResponseUtil.getInstance().kpmgLogin(body);
        } else if (apiName.equals(APIName.KPMG_UPDATE_DATA)) {
            return KpmgResponseUtil.getInstance().updateAllData();
        } else if (apiName.equals(APIName.GET_REMAINING_STOCK_KPMG)) {
            return KpmgResponseUtil.getInstance().kpmgReaminingStock();
        } else if (apiName.equals(APIName.KPMG_CHECK_SOTCK_NUM)) {
            return KpmgResponseUtil.getInstance().kpmgCheckSotckNum(body);
        }
        int userId = jsonObject.optInt("userId");
        User user = CoreData.getInstance().getUserById(userId);
        if (user == null) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            return this.getJsonResponse(new Gson().toJson(result));
        }
        if (apiName.equals(APIName.KPMG_COMMIT_ORDER)) {
            return KpmgResponseUtil.getInstance().commitOrder(body);
        }
        return getForbiddenResponse("Not Support yet");
    }


    @Override
    public Response doSubPosPost(String apiName, Method mothod, Map<String, String> params, String body) {
        if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
            LogUtil.d(TAG, "apiName : " + apiName + " body : " + body);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(body);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return this.getForbiddenResponse("");
            }
            String appVersion = jsonObject.optString("appVersion");
            if (apiName == null || TextUtils.isEmpty(appVersion)) {
                return this.getForbiddenResponse("");
            }
            Map<String, Object> result = new HashMap<String, Object>();

            if (!App.instance.isRevenueKiosk()) {
                result.put(RESULT_CODE, ResultCode.IS_NOT_KIOSK);
                return this.getJsonResponse(gson.toJson(result));
            }
            if (!appVersion.endsWith(App.instance.VERSION)) {
                result.put("resultCode", ResultCode.APP_VERSION_UNREAL);
                result.put("posVersion", App.instance.VERSION);
                String value = MobclickAgent.getConfigParams(App.getTopActivity(), "updateVersion");
                result.put("versionUpdate", value);
                return this.getJsonResponse(new Gson().toJson(result));
            }
            if (App.instance.getSessionStatus() == null) {
                result.put("resultCode", ResultCode.SESSION_IS_CLOSED);
                return this.getJsonResponse(new Gson().toJson(result));
            }

            if (apiName.equals(APIName.SUBPOS_CHOOSEREVENUE)) {
                return subChooseRevenue();
            } else if (apiName.equals(APIName.SUBPOS_LOGIN)) {
                return subPosLogin(body);
            } else if (apiName.equals(APIName.SUBPOS_UPDATE_DATA)) {
                return updateAllData();
            }
            int userId = jsonObject.optInt("userId");
            User user = CoreData.getInstance().getUserById(userId);
            if (user == null) {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                return this.getJsonResponse(new Gson().toJson(result));
            }
            if (apiName.equals(APIName.SUBPOS_COMMIT_ORDER)) {
                return commitOrder(body);
            } else if (apiName.equals(APIName.SUBPOS_COMMIT_ORDERLOG)) {
                return commitOrderLog(body);
            } else if (apiName.equals(APIName.SUBPOS_COMMIT_REPORT)) {
                return commitReport(body);
            } else if (apiName.equals(APIName.SUBPOS_CLOSE_SESSION)) {
                return sunPosCloseSession(body);
            }


            return getForbiddenResponse("Not Support yet");
        } else {
            return getForbiddenResponse("Not Support yet");
        }
    }

    private Response subChooseRevenue() {
        Map<String, Object> result = new HashMap<>();
        result.put(RESULT_CODE, ResultCode.SUCCESS);
        return this.getJsonResponse(gson.toJson(result));
    }

    private Response subPosLogin(String params) {
        Response resp = null;
        try {
            JSONObject jsonObject = new JSONObject(params);
            String employeeId = jsonObject.optString("employeeId");
            String password = jsonObject.optString("password");
            String deviceId = jsonObject.optString("deviceId");
            long sessionStatusTime = 0l;
            if (jsonObject.has("sessionStatusTime")) {
                sessionStatusTime = jsonObject.optLong("sessionStatusTime");
            }
            User user = CoreData.getInstance().getUser(employeeId, password);
            Map<String, Object> result = new HashMap<>();
            if (user != null) {
                if (user.getType() != ParamConst.USER_TYPE_MANAGER && user.getType() != ParamConst.USER_TYPE_POS) {
                    result.put(RESULT_CODE, ResultCode.USER_NO_PERMIT);
                    result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                    return this.getJsonResponse(new Gson().toJson(result));
                }
                result.put("user", user);
                SubPosBean subPosBean = SubPosBeanSQL.getSubPosBeanByDeviceId(deviceId);
                if (subPosBean == null) {
                    subPosBean = new SubPosBean();
                    subPosBean.setId(CommonSQL.getNextSeq(TableNames.SubPosBean));
                    subPosBean.setDeviceId(deviceId);
                    subPosBean.setUserName(user.getFirstName() + user.getLastName());
                    subPosBean.setNumTag("" + (char) (subPosBean.getId() + 64));
                    subPosBean.setSubPosStatus(ParamConst.SUB_POS_STATUS_OPEN);
                    subPosBean.setSessionStatusTime(App.instance.getSessionStatus().getTime());
                    SubPosBeanSQL.updateSubPosBean(subPosBean);
                } else {
                    subPosBean.setSubPosStatus(ParamConst.SUB_POS_STATUS_OPEN);
                    subPosBean.setUserName(user.getFirstName() + user.getLastName());
                    subPosBean.setSessionStatusTime(App.instance.getSessionStatus().getTime());
                    SubPosBeanSQL.updateSubPosBean(subPosBean);
                }
                result.put("subPosBean", subPosBean);
                result.put("businessDate", App.instance.getBusinessDate());
                if (sessionStatusTime != 0l && sessionStatusTime <= App.instance.getSessionStatus().getTime()) {
                    result.put("resultCode", ResultCode.SESSION_HAS_CHANGE);
                } else {
                    result.put("resultCode", ResultCode.SUCCESS);
                }
                resp = this.getJsonResponse(gson.toJson(result));
            } else {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                resp = this.getJsonResponse(gson.toJson(result));
            }
            if (App.getTopActivity() instanceof SubPosManagePage) {
                App.getTopActivity().httpRequestAction(ResultCode.SUCCESS, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.login_failed));
        }
        return resp;
    }


    private Response updateAllData() {
        Response resp = null;
        try {
            List<User> users = UserSQL.getAllUser();
            Restaurant restaurant = RestaurantSQL.getRestaurant();
            RevenueCenter revenueCenter = App.instance.getRevenueCenter();
            List<ItemMainCategory> itemMainCategories = ItemMainCategorySQL.getAllItemMainCategory();
            List<ItemCategory> itemCategories = ItemCategorySQL.getAllItemCategory();
            List<ItemDetail> itemDetails = ItemDetailSQL.getAllItemDetail();
            List<Modifier> modifiers = ModifierSQL.getAllModifier();
            List<Printer> printers = PrinterSQL.getAllPrinter();
            List<TableInfo> tableInfos = TableInfoSQL.getAllTables();
            List<PlaceInfo> placeInfos = PlaceInfoSQL.getAllPlaceInfo();
            List<ItemModifier> itemModifiers = ItemModifierSQL.getAllItemModifier();
            List<TaxCategory> taxCategories = TaxCategorySQL.getAllTaxCategory();
            List<Tax> taxes = TaxSQL.getAllTax();
            List<ItemHappyHour> itemHappyHours = ItemHappyHourSQL.getAllItemHappyHour();
            List<HappyHourWeek> happyHourWeeks = HappyHourWeekSQL.getAllHappyHourWeek();
            List<HappyHour> happyHours = HappyHourSQL.getAllHappyHour();
            List<RestaurantConfig> restaurantConfigs = RestaurantConfigSQL.getAllRestaurantConfig();
            List<PrinterGroup> printerGroups = PrinterGroupSQL.getAllPrinterGroup();
            List<UserRestaurant> userRestaurants = UserRestaurantSQL.getAll();
//			List<LocalDevice> localDevices = LocalDeviceSQL.getAllLocalDevice();
            List<PaymentMethod> paymentMethods = PaymentMethodSQL.getAllPaymentMethod();
            List<SettlementRestaurant> settlementRestaurants = SettlementRestaurantSQL.getAllSettlementRestaurant();
            Map<String, Object> map = new HashMap<>();
            SessionStatus sessionStatus = App.instance.getSessionStatus();
            map.put("sessionStatus", sessionStatus);
            map.put("users", users);
            map.put("restaurant", restaurant);
            map.put("revenueCenter", revenueCenter);
            map.put("itemMainCategories", itemMainCategories);
            map.put("itemCategories", itemCategories);
            map.put("itemDetails", itemDetails);
            map.put("modifiers", modifiers);
            map.put("printers", printers);
            map.put("tableInfos", tableInfos);
            map.put("placeInfos", placeInfos);
            map.put("itemModifiers", itemModifiers);
            map.put("taxCategories", taxCategories);
            map.put("taxes", taxes);
            map.put("itemHappyHours", itemHappyHours);
            map.put("happyHourWeeks", happyHourWeeks);
            map.put("happyHours", happyHours);
            map.put("restaurantConfigs", restaurantConfigs);
            map.put("printerGroups", printerGroups);
            map.put("userRestaurants", userRestaurants);
//			map.put("localDevices", localDevices);
            map.put("paymentMethods", paymentMethods);
            map.put("settlementRestaurants", settlementRestaurants);
            map.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(gson.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }

    private Response commitOrder(String params) {
        Response resp = null;
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(params);
            Order order = gson.fromJson(jsonObject.getString("order"), Order.class);
            int subPosBeanId = jsonObject.optInt("subPosBeanId");
            MultiOrderRelation multiOrderRelation = MultiOrderRelationSQL.getMultiOrderRelationBySubOrderId(subPosBeanId, order.getId().intValue(), order.getCreateTime());
            if (multiOrderRelation != null) {
                map.put("resultCode", ResultCode.RECEIVE_MSG_EXIST);
                return this.getJsonResponse(gson.toJson(map));
            }

            List<OrderDetail> orderDetails = gson.fromJson(jsonObject.getString("orderDetails"),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
            List<OrderModifier> orderModifiers = gson.fromJson(jsonObject.getString("orderModifiers"),
                    new TypeToken<List<OrderModifier>>() {
                    }.getType());
            List<Payment> payments = gson.fromJson(jsonObject.getString("payments"),
                    new TypeToken<List<Payment>>() {
                    }.getType());
            List<PaymentSettlement> paymentSettlements = gson.fromJson(jsonObject.getString("paymentSettlements"),
                    new TypeToken<List<PaymentSettlement>>() {
                    }.getType());
            List<OrderDetailTax> orderDetailTaxs = gson.fromJson(jsonObject.getString("orderDetailTaxs"),
                    new TypeToken<List<OrderDetailTax>>() {
                    }.getType());
            List<OrderSplit> orderSplits = gson.fromJson(jsonObject.getString("orderSplits"),
                    new TypeToken<List<OrderSplit>>() {
                    }.getType());
            List<OrderBill> orderBills = gson.fromJson(jsonObject.getString("orderBills"),
                    new TypeToken<List<OrderBill>>() {
                    }.getType());
            List<RoundAmount> roundAmounts = gson.fromJson(jsonObject.getString("roundAmounts"),
                    new TypeToken<List<RoundAmount>>() {
                    }.getType());
            boolean isSuccessful = SubPosCommitSQL.commitOrder(subPosBeanId, order, orderSplits, orderBills, payments, orderDetails,
                    orderModifiers, orderDetailTaxs, paymentSettlements, roundAmounts);
            map.put("resultCode", ResultCode.SUCCESS);
            if (isSuccessful) {
                final int orderId = order.getId().intValue();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Order placeOrder = CPOrderSQL.getOrder(orderId);
                        List<OrderSplit> placeOrderSplit = CPOrderSplitSQL.getUnFinishedOrderSplits(orderId);
                        KotSummary kotSummary = ObjectFactory.getInstance()
                                .getKotSummary(
                                        TableInfoSQL.getTableById(
                                                placeOrder.getTableId()).getName(), placeOrder,
                                        App.instance.getRevenueCenter(),
                                        App.instance.getBusinessDate());
                        if (placeOrderSplit != null && placeOrderSplit.size() > 0) {
                            for (OrderSplit orderSplit : placeOrderSplit) {
                                if (kotSummary != null) {
                                    ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                                    List<OrderDetail> placedOrderDetails = CPOrderDetailSQL.getOrderDetailsByOrderAndOrderSplit(orderSplit);
                                    List<Integer> orderDetailIds = new ArrayList<Integer>();
                                    ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                                    for (OrderDetail orderDetail : placedOrderDetails) {
                                        orderDetailIds.add(orderDetail.getId());
                                        KotItemDetail kotItemDetail = ObjectFactory
                                                .getInstance()
                                                .getKotItemDetail(
                                                        placeOrder,
                                                        orderDetail,
                                                        CoreData.getInstance()
                                                                .getItemDetailById(
                                                                        orderDetail.getItemId(),
                                                                        orderDetail.getItemName()),
                                                        kotSummary,
                                                        App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                                        kotItemDetail.setItemNum(orderDetail
                                                .getItemNum());
//										if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UNDONE) {
//											kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
//											kotItemDetail
//													.setKotStatus(ParamConst.KOT_STATUS_UPDATE);
//										}
                                        KotItemDetailSQL.update(kotItemDetail);
                                        kotItemDetails.add(kotItemDetail);
                                        orderDetailIds.add(orderDetail.getId());
                                        ArrayList<OrderModifier> orderModifiers = CPOrderModifierSQL
                                                .getOrderModifiers(placeOrder, orderDetail);
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
//									for (KotItemDetail kot : kotItemDetails) {
//										ArrayList<KotItemModifier> kotItemModifierObj = KotItemModifierSQL
//												.getKotItemModifiersByKotItemDetail(kot.getId());
//										if (kotItemModifierObj != null)
//											kotItemModifiers.addAll(kotItemModifierObj);
//									}
                                    Map<String, Object> orderMap = new HashMap<String, Object>();
                                    orderMap.put("orderId", orderSplit.getOrderId());
                                    orderMap.put("orderDetailIds", orderDetailIds);
                                    orderMap.put("orderPosType", ParamConst.POS_TYPE_SUB);
                                    App.instance.getKdsJobManager().tearDownKotForSub(
                                            kotSummary, kotItemDetails,
                                            kotItemModifiers, ParamConst.JOB_NEW_KOT,
                                            orderMap);
                                }
                            }
                        } else {
                            if (kotSummary != null) {
                                ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                                ArrayList<KotItemDetail> kotItemDetails = new ArrayList<>();
                                List<OrderDetail> placedOrderDetails = CPOrderDetailSQL.getOrderDetails(placeOrder.getId());
                                List<Integer> orderDetailIds = new ArrayList<Integer>();
                                for (OrderDetail orderDetail : placedOrderDetails) {
                                    orderDetailIds.add(orderDetail.getId());
                                    KotItemDetail kotItemDetail = ObjectFactory
                                            .getInstance()
                                            .getKotItemDetail(
                                                    placeOrder,
                                                    orderDetail,
                                                    CoreData.getInstance()
                                                            .getItemDetailById(
                                                                    orderDetail.getItemId(),
                                                                    orderDetail.getItemName()),
                                                    kotSummary,
                                                    App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                                    kotItemDetail.setItemNum(orderDetail
                                            .getItemNum());
//										if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UNDONE) {
//											kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
//											kotItemDetail
//													.setKotStatus(ParamConst.KOT_STATUS_UPDATE);
//										}
                                    KotItemDetailSQL.update(kotItemDetail);
                                    kotItemDetails.add(kotItemDetail);
                                    orderDetailIds.add(orderDetail.getId());
                                    ArrayList<OrderModifier> orderModifiers = CPOrderModifierSQL
                                            .getOrderModifiers(placeOrder, orderDetail);
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
//								for (KotItemDetail kot : kotItemDetails) {
//									ArrayList<KotItemModifier> kotItemModifierObj = KotItemModifierSQL
//											.getKotItemModifiersByKotItemDetail(kot.getId());
//									if (kotItemModifierObj != null)
//										kotItemModifiers.addAll(kotItemModifierObj);
//								}
//								TableInfo tableInfo = TableInfoSQL.getTableById(placeOrder.getTableId().intValue());
//								PrinterTitle title = ObjectFactory.getInstance()
//										.getPrinterTitle(
//												App.instance.getRevenueCenter(),
//												placeOrder,
//												App.instance.getUser().getFirstName()
//														+ App.instance.getUser().getLastName(),
//												tableInfo.getName(), 1);

                                Map<String, Object> orderMap = new HashMap<String, Object>();

                                orderMap.put("orderId", placeOrder.getId());
                                orderMap.put("orderDetailIds", orderDetailIds);
                                orderMap.put("orderPosType", ParamConst.POS_TYPE_SUB);
//								orderMap.put("paidOrder", placeOrder);
//								orderMap.put("title", title);
//								orderMap.put("placedOrderDetails", placedOrderDetails);
                                App.instance.getKdsJobManager().tearDownKotForSub(
                                        kotSummary, kotItemDetails,
                                        kotItemModifiers, ParamConst.JOB_NEW_KOT,
                                        orderMap);
                            }
                        }
                    }

                }).start();
            }
            resp = this.getJsonResponse(gson.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }

    private Response commitOrderLog(String params) {
        Response resp = null;
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(params);
            Order order = gson.fromJson(jsonObject.getString("order"), Order.class);
            int subPosBeanId = jsonObject.optInt("subPosBeanId");
            MultiOrderRelation multiOrderRelation = MultiOrderRelationSQL.getMultiOrderRelationBySubOrderId(subPosBeanId, order.getId().intValue(), order.getCreateTime());
            if (multiOrderRelation == null) {
                map.put("resultCode", ResultCode.RECEIVE_MSG_NO_EXIST);
                return this.getJsonResponse(gson.toJson(map));
            }
            List<Payment> payments = gson.fromJson(jsonObject.getString("payments"),
                    new TypeToken<List<Payment>>() {
                    }.getType());
            List<PaymentSettlement> paymentSettlements = gson.fromJson(jsonObject.getString("paymentSettlements"),
                    new TypeToken<List<PaymentSettlement>>() {
                    }.getType());
            List<RoundAmount> roundAmounts = gson.fromJson(jsonObject.getString("roundAmounts"),
                    new TypeToken<List<RoundAmount>>() {
                    }.getType());
            List<OrderSplit> orderSplits = CPOrderSplitSQL.getOrderSplits(multiOrderRelation.getMainOrderId());
            SubPosCommitSQL.commitOrderLog(order, orderSplits, payments, paymentSettlements, roundAmounts);
            map.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(gson.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }


    private Response commitReport(String params) {
        Response resp = null;
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(params);
            ReportDaySales reportDaySales = gson.fromJson(jsonObject.getString("reportDaySales"), ReportDaySales.class);
            int subPosBeanId = jsonObject.optInt("subPosBeanId");
            MultiReportRelation multiReportRelation = MultiReportRelationSQL.getMultiReportRelationBySubReportId(subPosBeanId, reportDaySales.getId(), reportDaySales.getCreateTime());
            if (multiReportRelation != null) {
                map.put("resultCode", ResultCode.RECEIVE_MSG_EXIST);
                return this.getJsonResponse(gson.toJson(map));
            }
            List<ReportDayTax> reportDayTaxs = gson.fromJson(jsonObject.getString("reportDayTaxs"),
                    new TypeToken<List<ReportDayTax>>() {
                    }.getType());
            List<ReportDayPayment> reportDayPayments = gson.fromJson(jsonObject.getString("reportDayPayments"),
                    new TypeToken<List<ReportDayPayment>>() {
                    }.getType());
            List<ReportPluDayItem> reportPluDayItems = gson.fromJson(jsonObject.getString("reportPluDayItems"),
                    new TypeToken<List<ReportPluDayItem>>() {
                    }.getType());
            List<ReportPluDayModifier> reportPluDayModifiers = gson.fromJson(jsonObject.getString("reportPluDayModifiers"),
                    new TypeToken<List<ReportPluDayModifier>>() {
                    }.getType());
            List<ReportHourly> reportHourlys = gson.fromJson(jsonObject.getString("reportHourlys"),
                    new TypeToken<List<ReportHourly>>() {
                    }.getType());
            List<ReportPluDayComboModifier> reportPluDayComboModifiers = gson.fromJson(jsonObject.getString("reportPluDayComboModifiers"),
                    new TypeToken<List<ReportPluDayComboModifier>>() {
                    }.getType());
            List<UserOpenDrawerRecord> userOpenDrawerRecords = gson.fromJson(jsonObject.getString("userOpenDrawerRecords"),
                    new TypeToken<List<UserOpenDrawerRecord>>() {
                    }.getType());

            SubPosCommitSQL.commitReport(subPosBeanId, reportDaySales, reportDayTaxs, reportDayPayments, reportPluDayItems,
                    reportPluDayModifiers, reportHourlys, reportPluDayComboModifiers, userOpenDrawerRecords);
            SubPosBean subPosBean = SubPosBeanSQL.getSubPosBeanById(subPosBeanId);
            if (subPosBean != null) {
                subPosBean.setSubPosStatus(ParamConst.SUB_POS_STATUS_CLOSE);
                SubPosBeanSQL.updateSubPosBean(subPosBean);
            }
            map.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(gson.toJson(map));
            if (App.getTopActivity() instanceof SubPosManagePage) {
                App.getTopActivity().httpRequestAction(ResultCode.SUCCESS, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }

    private Response sunPosCloseSession(String params) {
        Response resp = null;
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(params);
            int subPosBeanId = jsonObject.optInt("subPosBeanId");
            SubPosBean subPosBean = SubPosBeanSQL.getSubPosBeanById(subPosBeanId);
            if (subPosBean != null) {
                subPosBean.setSubPosStatus(ParamConst.SUB_POS_STATUS_CLOSE);
                SubPosBeanSQL.updateSubPosBean(subPosBean);
            }
            map.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(gson.toJson(map));
            if (App.getTopActivity() instanceof SubPosManagePage) {
                App.getTopActivity().httpRequestAction(ResultCode.SUCCESS, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }


    @Override
    public Response doPost(String apiName, Method mothod,
                           Map<String, String> params, String body) {
        LogUtil.d(TAG, "apiName : " + apiName + " body : " + body);
        final JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return this.getForbiddenResponse("");
        }
        String appVersion = jsonObject.optString("appVersion");
        if (apiName == null || TextUtils.isEmpty(appVersion)) {
            return this.getForbiddenResponse("");
        }
        if (!appVersion.endsWith(App.instance.VERSION)) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("resultCode", ResultCode.APP_VERSION_UNREAL);
            result.put("posVersion", App.instance.VERSION);
            VersionUpdate versionUpdate = new VersionUpdate();
            String value = MobclickAgent.getConfigParams(App.getTopActivity(), "updateVersion");
            result.put("versionUpdate", value);
            return this.getJsonResponse(new Gson().toJson(result));
        } else if (apiName.equals(APIName.LOGIN_LOGINVERIFY)) {// 厨房登录
            return handlerLogin(body);
        } else if (apiName.equals(APIName.GET_PRINTERS)) {// 厨房请求是否有新的数据
            return handlerKitGetPrinters(body);
        } else if (apiName.equals(APIName.EMPLOYEE_ID)) { // waiter请求配对
            return hanlderWaiterGetRevenues(body);
        } else if (apiName.equals(APIName.KPM_EMPLOYEE_ID)) { // kpm请求配对
            return hanlderKpmGetRevenues(body);
        } else if (apiName.equals(APIName.HAPPYHOUR_GETHAPPYHOUR)) {// 欢乐时间
            return handlerHappyHourInfo();
        } else if (apiName.equals(APIName.GET_PRINTER)) {// waiter 获取printer
            return handlerGetPrinters();
        } else if (apiName.equals(APIName.ITEM_GETITEM)) {// 菜的信息
            return handlerItemInfo(body);
        } else if (apiName.equals(APIName.GET_REMAINING_STOCK)) {// 菜的信息
            return handlerStock(body);
        } else if (apiName.equals(APIName.ITEM_GETITEMCATEGORY)) {// 菜分类信息
            return handlerItemCategoryInfo(body);
        } else if (apiName.equals(APIName.ITEM_GETMODIFIER)) {// 配料信息
            return handlerModifierInfo(body);
        } else if (apiName.equals(APIName.RESTAURANT_GETPLACEINFO)) {// 位置信息
            return handlerPlaceInfo(body);
        } else if (apiName.equals(APIName.RESTAURANT_GETRESTAURANTINFO)) {// 餐厅信息
            return handlerRestaurantInfo(body);
        } else if (apiName.equals(APIName.TAX_GETTAX)) {// 税的信息
            return handlerTaxInfo();
        } else if (apiName.equals(APIName.USER_GETUSER)) {// 用户信息
            return handlerUserInfo(body);
        } else if (apiName.equals(APIName.PAIRING_COMPLETE)) {
            return handlerPairingComplete(body);
        } else if (apiName.equals(APIName.TEMPORARY_DISH)) {//waiter 端添加临时菜通知pos端
            return handlerTemporaryDish(body);
        } else if (apiName.equals(APIName.SET_LANGUAGE)) {// 注销
            return handlerLanguage(body);
        } else if (apiName.equals(APIName.GET_OTHER_RVC_PLACE_TABLE)) {

            MultiRVCPlacesDao multiRVCPlacesDao = new MultiRVCPlacesDao();
            multiRVCPlacesDao.setResultCode(ResultCode.SUCCESS);

            MultiRVCPlacesDao.Data rvcData = new MultiRVCPlacesDao.Data();

            List<MultiRVCPlacesDao.Places> places = new ArrayList<>();
            ArrayList<PlaceInfo> data = PlaceInfoSQL.getAllPlaceInfo();
            for (int i = 0; i < data.size(); i++) {
                List<TableInfo> tables = TableInfoSQL.getTableInfosByPlaces(data.get(i));
                MultiRVCPlacesDao.Places place = new MultiRVCPlacesDao.Places(App.instance.getMainPosInfo(), data.get(i), tables);
                places.add(place);
            }
            rvcData.setPlaces(places);
            multiRVCPlacesDao.setData(rvcData);

            return getJsonResponse(new Gson().toJson(multiRVCPlacesDao));
        } else if (apiName.equals(APIName.GET_OTHER_RVC_TABLE)) {

            int placeId = jsonObject.optInt("placeId");
            PlaceInfo data = PlaceInfoSQL.getPlaceInfoById(placeId);
            List<TableInfo> table = TableInfoSQL.getTableInfosByPlaces(data);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("resultCode", ResultCode.SUCCESS);
            result.put("data", new Gson().toJson(table));

            return getJsonResponse(new Gson().toJson(result));

        } else if (apiName.equals(APIName.TRANSFER_TABLE_TO_OTHER_RVC)) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("resultCode", ResultCode.SUCCESS);
            String orderData = jsonObject.optString("order");
            int tableId = jsonObject.optInt("tableId");
            TableInfo tableInfo = TableInfoSQL.getTableById(tableId);
            result.put("tableInfo", new Gson().toJson(tableInfo));
            String orderDetail = jsonObject.optString("orderDetail");
            String orderModifier = jsonObject.optString("orderModifier");
            result.put("orderDetail", orderDetail);
            result.put("orderModifier", orderModifier);

            try {
                Order order = new Gson().fromJson(orderData, Order.class);
                if (order != null && TableInfoSQL.getTableById(tableId) != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handlerTransferFromOtherRvc(jsonObject);
                        }
                    }).start();


                    Order currentOrder = OrderSQL.getLastOrderatTabel(tableId);
                    result.put("toOrder", new Gson().toJson(currentOrder));
                    result.put("toRevenue", new Gson().toJson(App.instance.getRevenueCenter()));
                } else {
                    result.put("resultCode", ResultCode.UNKNOW_ERROR);
                }
            } catch (Exception e) {
                result.put("resultCode", ResultCode.UNKNOW_ERROR);
                e.printStackTrace();
            }
            Log.wtf("Test_sendOrderToOtherRVC", "result_getto_" + new Gson().toJson(jsonObject));

            return getJsonResponse(new Gson().toJson(result));
        } else if (apiName.equals(APIName.TRANSFER_ITEM_TO_OTHER_RVC)) {
            return handlerTransferItemFromOtherRvc(jsonObject);
        } else if (apiName.equals(APIName.GET_CONNECTED_KDS)) {
            return handleConnectedKDSData(body);
        } else if (apiName.equals(APIName.UPDATE_KDS_STATUS)) {
            return handleUpdateKDSStatus(body);
        } else {
            String userKey = jsonObject.optString("userKey");
            if (TextUtils.isEmpty(userKey) || App.instance.getUserByKey(userKey) == null) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                App.getTopActivity().httpRequestAction(
                        MainPage.REFRESH_TABLES_STATUS, jsonObject);
                return this.getJsonResponse(new Gson().toJson(result));
            }

            String trainType = jsonObject.optString("trainType");

//            int  train = SharedPreferencesHelper.getInt(App.instance,SharedPreferencesHelper.TRAINING_MODE);
//            if (Integer.valueOf(trainType) != train) {
//                Map<String, Object> result = new HashMap<String, Object>();
//                result.put("resultCode", ResultCode.USER_POS_TYPE);
//                return this.getJsonResponse(new Gson().toJson(result));
//            }

            if (apiName.equals(APIName.LOGIN_LOGOUT)) {// 注销
                return handlerLogout(body);
            } else if (apiName.equals(APIName.SELECT_TABLES)) {// 选择桌子
                return handlerSelectTables(body);
            } else if (apiName.equals(APIName.GET_ORDERDETAILS)) {// 获取不是waiter点的
                // orderDetails
                return handlerGetOrderDetails(body);
            } else if (apiName.equals(APIName.COMMIT_ORDER)) {// Waiter提交订单信息
                return handlerCommitOrder(body);
            } else if (apiName.equals(APIName.KDS_IP_CHANGE)) {
                return this.handlerKDSIpChange(body);
            } else if (apiName.equals(APIName.KOT_ITEM_COMPLETE)) { // 厨房提交item做完数据
                return handlerKOTItemComplete(body);
            } else if (apiName.equals(APIName.KOT_OUT_OF_STOCK)) { //厨房out of stock
                return handlerKOTOutOfStock(body);
            } else if (apiName.equals(APIName.KOT_NEXT_KDS)) {
                return handlerNextKDSKOT(body);
            } else if (apiName.equals(APIName.CANCEL_COMPLETE)) {// 厨房取消做完的菜
                return cancelComplete(body);
            } else if (apiName.equals(APIName.COLLECT_KOT_ITEM)) { // waiter
                // 点击取菜
                return handlerCollectKotItem(body);
            } else if (apiName.equals(APIName.GET_KOT_NOTIFICATION)) {
                return handlerGetKotNotifications();
            } else if (apiName.equals(APIName.GET_BILL)) {
                return handlerGetBill(body);
            } else if (apiName.equals(APIName.PRINT_BILL)) {
                return handlerPrintBill(body);
            } else if (apiName.equals(APIName.RE_PRINT_KOT)) {
                return handlerRePrintKOT(body);
            } else if (apiName.equals(APIName.CALL_SPECIFY_THE_NUMBER)) {
                return handlerCallSpecifyNumber(body);
            } else if (apiName.equals(APIName.UNSEAT_TABLE)) {
                return handlerWaiterUnseatTable(body);
            } else if (apiName.equals(APIName.VOID_ITEM)) {
                return handlerWaiterVoidItem(body);
            } else if (apiName.equals(APIName.PRINT_KOT_DATA)) {
                return handlePrintKOTData(body);
            } else {
                return this.getNotFoundResponse();
            }
        }
    }

    private Response handleUpdateKDSStatus(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<>();

        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(params);
            KDSDevice kdsDevice = gson.fromJson(jsonObject.optString("kds"), KDSDevice.class);

            final KDSDevice kdsDevicesLocal = App.instance.getKDSDevice(kdsDevice.getDevice_id());

            if (kdsDevicesLocal != null) {
                kdsDevicesLocal.setKdsStatus(kdsDevice.getKdsStatus());
                App.instance.setKdsDevice(kdsDevicesLocal);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    App.instance.getKdsJobManager().updateKDSStatus(kdsDevicesLocal);
                }
            }).start();

            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (JSONException e) {
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }

    private Response handleConnectedKDSData(String params) {
        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();

        List<KDSDevice> kdsDeviceList = new ArrayList<>(App.instance.getKDSDevices().values());

        result.put("kdsList", gson.toJson(kdsDeviceList));
        result.put("resultCode", ResultCode.SUCCESS);
        return this.getJsonResponse(gson.toJson(result));

    }

    private Response handlePrintKOTData(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(params);
            Order order = gson.fromJson(jsonObject.optString("order"), Order.class);

            KotSummary kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());
            ArrayList<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByOrderId(order.getId());
            List<KotItemModifier> kotItemModifiers = new ArrayList<>();

            for (KotItemDetail kotItemDetail : kotItemDetails) {
                kotItemModifiers
                        .addAll(KotItemModifierSQL
                                .getKotItemModifiersByKotItemDetail(kotItemDetail
                                        .getId()));
            }

            result.put("kotSummary", kotSummary);
            result.put("kotItemDetails", kotItemDetails);
            result.put("kotItemModifiers", kotItemModifiers);
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (JSONException e) {
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }

    private Response handlerLogout(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(params);
            int deviceType = jsonObject.optInt("deviceType");
            String device = jsonObject.optString("device");
            if (!TextUtils.isEmpty(device)) {
                if (deviceType == ParamConst.DEVICE_TYPE_WAITER) {
                    final WaiterDevice waiterDevice = gson.fromJson(device,
                            WaiterDevice.class);
                    App.instance.removeWaiterDevice(waiterDevice);
                    CoreData.getInstance().removeLocalDeviceByDeviceIdAndIP(waiterDevice.getWaiterId(), waiterDevice.getIP());
                    // Notify Waiter pairing complete
                    App.getTopActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            App.getTopActivity().httpRequestAction(
                                    ParamConst.HTTP_REQ_REFRESH_WAITER_PAIRED,
                                    waiterDevice);
                        }
                    });
                } else if (deviceType == ParamConst.DEVICE_TYPE_KDS) {
                    final KDSDevice kdsDevice = gson.fromJson(device,
                            KDSDevice.class);
                    App.instance.removeKDSDevice(kdsDevice.getDevice_id());
                    CoreData.getInstance().removeLocalDeviceByDeviceIdAndIP(kdsDevice.getDevice_id(), kdsDevice.getIP());
                    // Notify KDS pairing complete
                    App.getTopActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            App.getTopActivity().httpRequestAction(
                                    ParamConst.HTTP_REQ_REFRESH_KDS_PAIRED, null);
                        }
                    });
                }
                result.put("mainPosInfo", App.instance.getMainPosInfo());
                result.put("resultCode", ResultCode.SUCCESS);
                resp = this.getJsonResponse(new Gson().toJson(result));
            } else {
                resp = this.getInternalErrorResponse("Device is invalid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse("");
        }
        return resp;
    }


    private Response hanlderWaiterGetRevenues(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(params);
            int employeeId = jsonObject.optInt("employee_ID");
            User user = CoreData.getInstance().getUserByEmpId(employeeId);
            if (App.instance.isRevenueKiosk()) {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                resp = this.getJsonResponse(new Gson().toJson(result));
                return resp;
            }
            if (user != null) {
                RevenueCenter revenueCenter = App.instance.getRevenueCenter();
                Boolean isPermitted = CoreData.getInstance()
                        .checkUserWaiterAccessInRevcenter(user.getId(),
                                revenueCenter.getRestaurantId(),
                                revenueCenter.getId());
                if (isPermitted) {
                    List<RevenueCenter> revenueCenters = CoreData.getInstance()
                            .getRevenueCenters();
                    List<Integer> revenueCenterIds = UserRestaurantSQL
                            .getRevenueIdByUserId(user.getId());
                    result.put("revenueCenters", revenueCenters);
                    result.put("revenueCenterIds", revenueCenterIds);
                    result.put("user", user);
                    result.put("revenue", revenueCenter);
                    result.put("resultCode", ResultCode.SUCCESS);
                } else {
                    result.put("resultCode", ResultCode.USER_NO_PERMIT);
                    result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                    result.put("printers", null);
                }
                resp = this.getJsonResponse(new Gson().toJson(result));
            } else {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                resp = this.getJsonResponse(new Gson().toJson(result));
            }
        } catch (Exception e) {
            // TODO: handle exception
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        return resp;
    }


    private Response hanlderKpmGetRevenues(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(params);
            int employeeId = jsonObject.optInt("employee_ID");
            User user = CoreData.getInstance().getUserByEmpId(employeeId);
//			if (App.instance.isRevenueKiosk()) {
//				result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
//				resp = this.getJsonResponse(new Gson().toJson(result));
//				return resp;
//			}
            if (user != null) {
                RevenueCenter revenueCenter = App.instance.getRevenueCenter();
                Boolean isPermitted = CoreData.getInstance()
                        .checkUserWaiterAccessInRevcenter(user.getId(),
                                revenueCenter.getRestaurantId(),
                                revenueCenter.getId());
                if (isPermitted) {
                    List<RevenueCenter> revenueCenters = CoreData.getInstance()
                            .getRevenueCenters();
                    List<Integer> revenueCenterIds = UserRestaurantSQL
                            .getRevenueIdByUserId(user.getId());
                    result.put("revenueCenters", revenueCenters);
                    result.put("revenueCenterIds", revenueCenterIds);
                    result.put("user", user);
                    result.put("revenue", revenueCenter);
                    result.put("resultCode", ResultCode.SUCCESS);
                } else {
                    result.put("resultCode", ResultCode.USER_NO_PERMIT);
                    result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                    result.put("printers", null);
                }
                resp = this.getJsonResponse(new Gson().toJson(result));
            } else {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                resp = this.getJsonResponse(new Gson().toJson(result));
            }
        } catch (Exception e) {
            // TODO: handle exception
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        return resp;
    }

    private Response handlerPairingComplete(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(params);
            int deviceType = jsonObject.optInt("deviceType");
            String device = jsonObject.optString("device");
            if (!TextUtils.isEmpty(device)) {
                if (deviceType == ParamConst.DEVICE_TYPE_WAITER &&
                        !App.instance.isRevenueKiosk()) {
                    final WaiterDevice waiterDevice = gson.fromJson(device,
                            WaiterDevice.class);
//					if (App.instance.isWaiterLoginAllowed(waiterDevice)) {
                    App.instance.addWaiterDevice(waiterDevice);
                    LocalDevice localDevice = ObjectFactory
                            .getInstance()
                            .getLocalDevice("", "waiter", ParamConst.DEVICE_TYPE_WAITER,
                                    waiterDevice.getWaiterId(),
                                    waiterDevice.getIP(), waiterDevice.getMac(), "", 0);
                    CoreData.getInstance().addLocalDevice(localDevice);
                    // Notify Waiter pairing complete
                    App.getTopActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            App.getTopActivity().httpRequestAction(
                                    ParamConst.HTTP_REQ_CALLBACK_WAITER_PAIRED,
                                    waiterDevice);
                        }
                    });
//					} else {
//						result.put("resultCode", ResultCode.USER_LOGIN_EXIST);
//						resp = this.getJsonResponse(new Gson().toJson(result));
//						return resp;
//					}
                } else if (deviceType == ParamConst.DEVICE_TYPE_KDS) {
                    KDSDevice kdsDevice = new KDSDevice();
                    kdsDevice = gson.fromJson(device,
                            KDSDevice.class);

                    App.instance.addKDSDevice(kdsDevice.getDevice_id(),
                            kdsDevice);

                    //region add connected kds to balancer
                    final KDSDevice finalKdsDevice1 = kdsDevice;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            addConnectedKDSToBalancer(finalKdsDevice1);
                        }
                    }).start();
                    //endregion

                    final LocalDevice localDevice = ObjectFactory.getInstance()
                            .getLocalDevice(kdsDevice.getName(), "kds",
                                    ParamConst.DEVICE_TYPE_KDS,
                                    kdsDevice.getDevice_id(),
                                    kdsDevice.getIP(), kdsDevice.getMac(), "", 0);
                    CoreData.getInstance().addLocalDevice(localDevice);
//                    final String kdsStr = kdsDevice == null ? "空的" : kdsDevice.toString();
//                    final String localStr = localDevice == null ? "空的" : localDevice.toString();

                    // Notify KDS pairing complete
                    final KDSDevice finalKdsDevice = kdsDevice;
                    App.getTopActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
//							UIHelp.showToast(App.getTopActivity(), kdsStr);
//							UIHelp.showToast(App.getTopActivity(), localStr);
                            App.getTopActivity().httpRequestAction(
                                    ParamConst.HTTP_REQ_CALLBACK_KDS_PAIRED,
                                    finalKdsDevice);
                        }
                    });
                }
                result.put("mainPosInfo", App.instance.getMainPosInfo());
                result.put("resultCode", ResultCode.SUCCESS);
                resp = this.getJsonResponse(new Gson().toJson(result));
            } else {
                resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.device_invalid));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse("");
        }
        return resp;
    }

    private void addConnectedKDSToBalancer(KDSDevice kdsDevice) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("kds", kdsDevice);
            KDSDevice balancerKds = App.instance.getBalancerKDSDevice();
            if (balancerKds != null)
                SyncCentre.getInstance().syncSubmitConnectedKDS(balancerKds, App.instance, param, null);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private Response handlerTemporaryDish(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(params);
            int timeUpdate = jsonObject.optInt("timeUpdate");
            String temporaryDish = jsonObject.optString("temporaryDish");
            if (!TextUtils.isEmpty(temporaryDish)) {
                if (timeUpdate == 0 &&
                        !App.instance.isRevenueKiosk()) {
                    final ItemDetail temDetail = gson.fromJson(temporaryDish,
                            ItemDetail.class);

                    ItemDetailSQL.addItemDetailByLocal(temDetail);
                    CoreData.getInstance().setItemDetails(
                            ItemDetailSQL.getAllItemDetail());

//					if (App.instance.isWaiterLoginAllowed(waiterDevice)) {

//					} else {
//						result.put("resultCode", ResultCode.USER_LOGIN_EXIST);
//						resp = this.getJsonResponse(new Gson().toJson(result));
//						return resp;
//					}
                } else {
                    final ItemDetail temDetail = gson.fromJson(temporaryDish,
                            ItemDetail.class);

                    ItemDetailSQL.updateItemDetail(temDetail);
                    CoreData.getInstance().setItemDetails(
                            ItemDetailSQL.getAllItemDetail());
                }
                result.put("resultCode", ResultCode.SUCCESS);
                resp = this.getJsonResponse(new Gson().toJson(result));
            } else {
                resp = this.getInternalErrorResponse("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse("");
        }
        return resp;
    }

    private Response handlerLogin(String params) {
        Response resp = null;
        try {
            JSONObject jsonObject = new JSONObject(params);
            String employee_ID = jsonObject.optString("employee_ID");
            String password = jsonObject.optString("password");
            Integer type = jsonObject.optInt("type");
            int trainType = SharedPreferencesHelper.getInt(App.instance, SharedPreferencesHelper.TRAINING_MODE);
            if (trainType < 1) {
                trainType = 0;
            }
            User user = CoreData.getInstance().getUser(employee_ID, password);
            Map<String, Object> result = new HashMap<String, Object>();
            if (user != null) {
                SessionStatus sessionStatus = App.instance.getSessionStatus();
                if (sessionStatus == null) {
                    result.put("resultCode", ResultCode.SESSION_IS_CLOSED);
                    resp = this.getJsonResponse(new Gson().toJson(result));
                } else {
                    String userKey = UUID.randomUUID().toString();

                    MainPosInfo mainPosInfo = App.instance.getMainPosInfo();
                    App.instance.addActiveUser(userKey, user);

                    Gson gson = new Gson();
                    if (type == ParamConst.USER_TYPE_KOT) {
                        KDSDevice dev = gson.fromJson(jsonObject
                                        .optJSONObject("device").toString(),
                                KDSDevice.class);
                        LocalDevice localDevice = ObjectFactory.getInstance()
                                .getLocalDevice(dev.getName(), "kds",
                                        ParamConst.DEVICE_TYPE_KDS,
                                        dev.getDevice_id(),
                                        dev.getIP(), dev.getMac(), "", 0);

                        CoreData.getInstance().addLocalDevice(localDevice);
                        App.instance.addKDSDevice(dev.getDevice_id(), dev);
                        List<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                        List<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                        List<KotSummary> kotSummaryList = new ArrayList<KotSummary>();
                        if (App.instance.isRevenueKiosk()) {
                            kotSummaryList = KotSummarySQL.getUndoneKotSummaryByBusinessDateForKiosk(App.instance
                                    .getBusinessDate());
                            List<KotSummary> k = KotSummarySQL.getUndoneKotSummaryByBusinessDateForSubKiosk(App.instance
                                    .getBusinessDate());
                            if (k != null && k.size() > 0) {
                                kotSummaryList.addAll(k);
                            }
                        } else {
                            kotSummaryList = KotSummarySQL
                                    .getUndoneKotSummaryByBusinessDateAndOrderUnfinish(App.instance
                                            .getBusinessDate());
                        }

//                        List<PrinterGroup> printerGroupList = CoreData
//                                .getInstance().getPrinterGroupByPrinter(
//                                        dev.getDevice_id());

                        List<KotSummary> kotSummaries = new ArrayList<>();
                        for (KotSummary kotSummary : kotSummaryList) {
                            KotSummaryLog kotSummaryLogs = new Gson().fromJson(kotSummary.getKotSummaryLog(), KotSummaryLog.class);
                            for (KDSTracking kdsTracking : kotSummaryLogs.kdsTrackingList) {
                                if (kdsTracking.kdsDevice.getDevice_id() == dev.getDevice_id() &&
                                        kdsTracking.kdsDevice.getIP().equals(dev.getIP())) {
                                    for (KotItemDetail kid : kdsTracking.kotItemDetails) {
                                        KotItemDetail kotItemDetail = KotItemDetailSQL.getKotItemDetailById(kid.getId());
                                        if (kotItemDetail == null) continue;
                                        kotItemDetails.add(kotItemDetail);
                                    }
                                    kotSummaries.add(kotSummary);
                                }
                            }

//                            for (PrinterGroup printerGroup : printerGroupList) {
//                                kotItemDetails
//                                        .addAll(KotItemDetailSQL
//                                                .getKotItemDetailByKotSummaryAndPrinterGroup(
//                                                        kotSummary.getId(),
//                                                        printerGroup
//                                                                .getPrinterGroupId()));
//                            }
                        }
                        for (KotItemDetail kotItemDetail : kotItemDetails) {
                            kotItemModifiers
                                    .addAll(KotItemModifierSQL
                                            .getKotItemModifiersByKotItemDetail(kotItemDetail
                                                    .getId()));
                        }

                        result.put("kotSummaryList", kotSummaries);
                        result.put("kotItemDetails", kotItemDetails);
                        result.put("kotItemModifiers", kotItemModifiers);
                        result.put("user", user);
                        result.put("resultCode", ResultCode.SUCCESS);
                        result.put("userKey", userKey);
                        result.put("mainPosInfo", mainPosInfo);
                        result.put("session", sessionStatus);
                        result.put("businessDate", App.instance.getBusinessDate());
                        result.put("trainType", trainType);
                        result.put("formatType", App.instance.getLocalRestaurantConfig().getFormatType());
                        resp = this.getJsonResponse(new Gson().toJson(result));

                    } else if (type == ParamConst.USER_TYPE_WAITER &&
                            !App.instance.isRevenueKiosk()) {

                        //no need waiter app in kiosk mode
                        WaiterDevice dev = gson.fromJson(
                                jsonObject.optJSONObject("device").toString(),
                                WaiterDevice.class);
                        //waiter can login one device at one time
//							if (App.instance.isWaiterLoginAllowed(dev)) {
                        App.instance.addWaiterDevice(dev);
                        LocalDevice localDevice = ObjectFactory
                                .getInstance()
                                .getLocalDevice("", "waiter", ParamConst.DEVICE_TYPE_WAITER,
                                        dev.getWaiterId(),
                                        dev.getIP(), dev.getMac(), "", 0);
                        CoreData.getInstance().addLocalDevice(localDevice);
                        result.put("user", user);
                        result.put("resultCode", ResultCode.SUCCESS);
                        result.put("userKey", userKey);
                        result.put("mainPosInfo", mainPosInfo);
                        result.put("session", sessionStatus);
                        result.put("businessDate", App.instance.getBusinessDate());
                        result.put("currencySymbol", App.instance.getLocalRestaurantConfig().getCurrencySymbol());
                        result.put("isDouble", App.instance.getLocalRestaurantConfig().getCurrencySymbolType() >= 0);
                        result.put("formatType", App.instance.getLocalRestaurantConfig().getFormatType());


//							} else {
//								result.put("resultCode", ResultCode.USER_LOGIN_EXIST);
//							}

                        resp = this.getJsonResponse(new Gson().toJson(result));
                    }
                }
            } else {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                resp = this.getJsonResponse(new Gson().toJson(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.login_failed));
        }
        return resp;
    }

    private Response handlerRestaurantInfo(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        if (!isValidUser(params)) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
        } else {
            result.put("revenueList", CoreData.getInstance()
                    .getRevenueCenters());
            result.put("printerList", CoreData.getInstance().getPrinters());
            result.put("restaurant", CoreData.getInstance().getRestaurant());
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        }
        return resp;
    }

    private Response handlerUserInfo(String params) {
        // if (!isValidUser()) {
        // send404();
        // return;
        // }
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userList", CoreData.getInstance().getUsers());
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerItemCategoryInfo(String params) {
        // if (!isValidUser()) {
        // send404();
        // return;
        // }
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("subCategoryList", CoreData.getInstance()
                .getItemCategories());
        result.put("mainCategoryList", CoreData.getInstance()
                .getItemMainCategories());
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerItemInfo(String params) {
        Response resp;
        // if (!isValidUser()) {
        // send404();
        // return;
        // }
        List<RemainingStock> remainingStocks = RemainingStockSQL.getAllRemainingStock();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("remainingStockList", remainingStocks);
        result.put("itemList", CoreData.getInstance().getItemDetails());
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerStock(String params) {
        Response resp;
        List<RemainingStock> remainingStocks = RemainingStockSQL.getAllRemainingStock();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("remainingStockList", remainingStocks);
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerModifierInfo(String params) {
        Response resp;
        // if (!isValidUser()) {
        // send404();
        // return;
        // }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("itemModifierList", CoreData.getInstance()
                .getItemModifiers());
        result.put("modifierList", CoreData.getInstance().getModifierList());
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerTaxInfo() {
        Response resp;
        // if (!isValidUser()) {
        // send404();
        // return;
        // }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("taxList", CoreData.getInstance().getTaxs());
        result.put("taxCategoryList", CoreData.getInstance().getTaxCategories());
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerHappyHourInfo() {
        // if (!isValidUser()) {
        // send404();
        // return;
        // }
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("itemHappyHourList", CoreData.getInstance()
                .getItemHappyHours());
        result.put("happyHourWeekList", CoreData.getInstance()
                .getHappyHourWeeks());
        result.put("happyHourList", CoreData.getInstance().getHappyHours());
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerPlaceInfo(String params) {
        Response resp;
        // if (!isValidUser()) {
        // send404();
        // return;
        // }
        Integer revenueId = 0;
        Map<String, Object> result = new HashMap<String, Object>();

        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }
        try {
            revenueId = new JSONObject(params).optInt("revenueId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result.put("placeList", PlaceInfoSQL.getAllPlaceInfo());
        result.put("tableList", TableInfoSQL.getAllTables());
        result.put("printMap", App.instance.getPrinterDevices());
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    //Waiter Select Table from Waiter APP
    private Response handlerSelectTables(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();

        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        if (!isValidUser(params)) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        try {
            JSONObject jsonObject = new JSONObject(params);
            Gson gson = new Gson();
            TableInfo tables = gson.fromJson(jsonObject.optJSONObject("tables")
                    .toString(), TableInfo.class);
            String userKey = jsonObject.optString("userKey");
            String waitterName = jsonObject.optString("waitterName");

            result.put("tempItems", ItemDetailSQL.getAllTempItemDetail());
            if (TableInfoSQL.getTableById(tables.getPosId())
                    .getStatus() == ParamConst.TABLE_STATUS_IDLE) {
//				TablesSQL.updateTables(tables);
                TableInfoSQL.updateTables(tables);
                //clean all KOT summary and KotDetails if the table is EMPTY
                KotSummary kotSummary = KotSummarySQL.getKotSummaryByTable(tables.getPosId().intValue());
                if (kotSummary != null) {
                    List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByKotSummaryIdUndone(kotSummary);
                    if (kotItemDetails != null)
                        for (KotItemDetail kotItemDetail : kotItemDetails) {
                            kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                            KotItemDetailSQL.update(kotItemDetail);
                        }
                    kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
                    KotSummarySQL.update(kotSummary);
                }

//				CoreData.getInstance().setTableList(TablesSQL.getAllTables());
                Order order = ObjectFactory.getInstance().getOrder(
                        ParamConst.ORDER_ORIGIN_WAITER, App.instance.getSubPosBeanId(), tables,
                        App.instance.getRevenueCenter(),
                        App.instance.getUserByKey(userKey),
                        App.instance.getSessionStatus(),
                        App.instance.getBusinessDate(),
                        App.instance.getIndexOfRevenueCenter(),
                        ParamConst.ORDER_STATUS_OPEN_IN_WAITER,
                        App.instance.getLocalRestaurantConfig()
                                .getIncludedTax().getTax(), waitterName);
                // ArrayList<OrderDetail> orderDetails = OrderDetailSQL
                // .getOrderDetailByOrderIdAndOrderOriginId(order.getId(),
                // ParamConst.ORDER_ORIGIN_WAITER);
                result.put("order", order);
                result.put("resultCode", ResultCode.SUCCESS);
                resp = this.getJsonResponse(new Gson().toJson(result));
                try {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("tableId", tables.getPosId().intValue());
                    jsonObject1.put("status", ParamConst.TABLE_STATUS_DINING);
                    jsonObject1.put("RX", RxBus.RX_REFRESH_TABLE);
                    TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject1.toString(), null);
                    TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject1.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (App.getTopActivity() != null)
                    App.getTopActivity().httpRequestAction(
                            MainPage.REFRESH_TABLES_STATUS, tables);
            } else {// 错误处理
                // result.put("resultCode", ResultCode.UNKNOW_ERROR);
                // send200(new Gson().toJson(result));

                // 暂时也回复正常数据，便于测试
//				TablesSQL.updateTables(tables);
                TableInfoSQL.updateTables(tables);
                Order order = ObjectFactory.getInstance().getOrder(
                        ParamConst.ORDER_ORIGIN_WAITER, App.instance.getSubPosBeanId(), tables,
                        App.instance.getRevenueCenter(),
                        App.instance.getUserByKey(userKey),
                        App.instance.getSessionStatus(),
                        App.instance.getBusinessDate(),
                        App.instance.getIndexOfRevenueCenter(),
                        ParamConst.ORDER_STATUS_OPEN_IN_WAITER,
                        App.instance.getLocalRestaurantConfig()
                                .getIncludedTax().getTax(), waitterName);
                ArrayList<OrderDetail> orderDetails = OrderDetailSQL
                        .getAllOrderDetailsByOrder(order);
                result.put("order", order);
                result.put("resultCode", ResultCode.SUCCESS);
                result.put("orderDetails", orderDetails);
                resp = this.getJsonResponse(new Gson().toJson(result));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        return resp;
    }

    private Response handlerGetOrderDetails(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();

        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        if (!isValidUser(params)) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }
        try {
            JSONObject jsonObject = new JSONObject(params);
            Gson gson = new Gson();
            Order order = gson.fromJson(jsonObject.optString("order"),
                    Order.class);
            ArrayList<OrderDetail> orderDetails = OrderDetailSQL
                    .getOrderDetailByOrderIdAndOrderOriginId(order.getId(),
                            ParamConst.ORDER_ORIGIN_WAITER);
            result.put("otherOrderDetails", orderDetails);
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        return resp;
    }

    private Response handlerCommitOrder(String params) {
        System.out.println("1111111111111");


        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();

        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        if (!isValidUser(params)) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }
        try {
            JSONObject jsonObject = new JSONObject(params);
            Gson gson = new Gson();
            Order order = gson.fromJson(jsonObject.optJSONObject("order")
                    .toString(), Order.class);
            Order loadOrder = OrderSQL.getUnfinishedOrder(order.getId());
            if (loadOrder == null) {
                result.put("resultCode", ResultCode.ORDER_FINISHED);
                resp = this.getJsonResponse(new Gson().toJson(result));
                return resp;
            }
            if ((App.instance.orderInPayment != null && App.instance.orderInPayment.getId().intValue() == order.getId().intValue())) {
                result.put("resultCode", ResultCode.NONEXISTENT_ORDER);
                resp = this.getJsonResponse(new Gson().toJson(result));
                return resp;
            }
            if (App.instance.getClosingOrderId() == order.getId()) {
                result.put("resultCode", ResultCode.ORDER_HAS_CLOSING);
                resp = this.getJsonResponse(new Gson().toJson(result));
                return resp;
            }
            ArrayList<OrderDetail> waiterOrderDetails = gson.fromJson(
                    jsonObject.optString("orderDetails"),
                    new TypeToken<ArrayList<OrderDetail>>() {
                    }.getType());
            ArrayList<OrderModifier> waiterOrderModifiers = gson.fromJson(
                    jsonObject.optString("orderModifiers"),
                    new TypeToken<ArrayList<OrderModifier>>() {
                    }.getType());
            if (OrderDetailSQL.getOrderDetailCountByOrder(order) > 0) {
                // 检测是否有 订单是已经完成的拆单中的
                for (OrderDetail orderDetail : waiterOrderDetails) {
                    OrderSplit loadOrderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
                    if (loadOrderSplit != null && loadOrderSplit.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED) {
                        result.put("resultCode", ResultCode.ORDER_SPLIT_IS_SETTLED);
                        result.put("groupId", loadOrderSplit.getGroupId());
                        resp = this.getJsonResponse(new Gson().toJson(result));
                        return resp;
                    }
                }
            }
            LogUtil.i(TAG, "------11111");
            final StringBuffer stringBuffer = new StringBuffer();

            if (waiterOrderDetails != null) {

                Map<String, String> map = new HashMap<String, String>();
                Map<Integer, Object> mapNum = new HashMap<Integer, Object>();
                for (int i = 0; i < waiterOrderDetails.size(); i++) {
                    OrderDetail orderDetail = waiterOrderDetails.get(i);
                    int itemTempId = CoreData.getInstance().getItemDetailById(orderDetail.getItemId(), orderDetail.getItemName()).getItemTemplateId();
                    RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);
                    if (mapNum.containsKey(itemTempId)) {
                        //  int num=mapNum.get(orderDetail.getItemId()).intValue()+orderDetail.getItemNum();
                        OrderDetail orderDetail1 = (OrderDetail) mapNum.get(itemTempId);
                        OrderDetail orderDetail1New = new OrderDetail();
                        orderDetail1New.setItemName(orderDetail1.getItemName());
                        int num = orderDetail1.getItemNum().intValue() + orderDetail.getItemNum().intValue();
                        orderDetail1New.setItemNum(num);
                        mapNum.put(itemTempId, orderDetail1New);

                    } else {
                        mapNum.put(itemTempId, orderDetail);
                    }
//                        if(remainingStock!=null) {
//                            int num = orderDetail.getItemNum();
//                            if(num>remainingStock.getQty()){
//                                map.put(orderDetail.getItemName(),orderDetail.getItemName() + "：alone" + remainingStock.getQty() + " ");
////                                    stringBuffer.append(orderDetail.getItemName() + "：alone" + remainingStock.getQty() + " ");
//                            }
//                        }
                }


                Iterator<Map.Entry<Integer, Object>> entries = mapNum.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<Integer, Object> entry = entries.next();
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                    final RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(entry.getKey());

                    if (remainingStock != null) {
                        OrderDetail orderDetailStock = (OrderDetail) entry.getValue();
                        if (orderDetailStock.getItemNum() > remainingStock.getQty()) {
                            map.put(orderDetailStock.getItemName(), orderDetailStock.getItemName() + "：alone" + remainingStock.getQty() + " ");
//                                    stringBuffer.append(orderDetail.getItemName() + "：alone" + remainingStock.getQty() + " ");
                        }
                    }

                }
//                for (int value : mapNum.values()) {
//                    System.out.println("Value = " + value);
//                    stringBuffer.append(value);
//                }
                if (map != null && map.size() > 0) {
                    for (String value : map.values()) {
                        System.out.println("Value = " + value);
                        stringBuffer.append(value);
                    }
                    result.put("resultCode", ResultCode.WAITER_OUT_OF_STOCK);
                    result.put("stockNum", stringBuffer.toString());
                    resp = this.getJsonResponse(new Gson().toJson(result));
                    return resp;
                } else {
                    for (int i = 0; i < waiterOrderDetails.size(); i++) {
                        final OrderDetail orderDetail = waiterOrderDetails.get(i);
                        final int itemTempId = CoreData.getInstance().getItemDetailById(orderDetail.getItemId(), orderDetail.getItemName()).getItemTemplateId();
                        final RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);
                        if (remainingStock != null) {
                            int num = orderDetail.getItemNum();
                            RemainingStockHelper.updateRemainingStockNum(remainingStock, num, false, new StockCallBack() {
                                @Override
                                public void onSuccess(Boolean isStock) {
                                    if (isStock) {
                                        App.instance.getSyncJob().updateRemainingStockNum(itemTempId);
                                    }
                                }
                            });

                        }
                    }
                }
            }

            List<OrderDetail> newOrderDetails = new ArrayList<>();

            // waiter 过来的数据 存到 pos的DB中 不带Id存储
            for (OrderDetail orderDetail : waiterOrderDetails) {
                synchronized (lockObject) {
                    OrderDetail orderDetailFromPOS = OrderDetailSQL
                            .getOrderDetailByCreateTime(
                                    orderDetail.getCreateTime(),
                                    orderDetail.getOrderId());
                    if (orderDetailFromPOS != null) {
                        continue;
                    } else {

                        int orderDetailId = CommonSQL
                                .getNextSeq(TableNames.OrderDetail);
                        int oldOrderDetailId = orderDetail.getId();
                        orderDetail
                                .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
                        orderDetail.setId(orderDetailId);
                        OrderDetailSQL.addOrderDetailETCFromWaiter(orderDetail);
                        if (orderDetail.getGroupId().intValue() > 0) {
                            OrderSplit orderSplit = ObjectFactory.getInstance().getOrderSplit(order, orderDetail.getGroupId(), App.instance.getLocalRestaurantConfig()
                                    .getIncludedTax().getTax());
                            OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
                            orderDetail.setOrderSplitId(orderSplit.getId());
                            OrderDetailSQL.updateOrderDetail(orderDetail);
                            OrderDetailTaxSQL.updateOrderSplitIdbyOrderDetail(orderDetail);
                        }

                        newOrderDetails.add(orderDetail);

                        if (waiterOrderModifiers != null
                                && !waiterOrderModifiers.isEmpty()) {
                            for (OrderModifier orderModifier : waiterOrderModifiers) {
                                if (orderModifier.getOrderDetailId().intValue() == oldOrderDetailId) {
                                    orderModifier.setOrderDetailId(orderDetailId);
                                    orderModifier.setId(CommonSQL
                                            .getNextSeq(TableNames.OrderModifier));
                                    OrderModifierSQL
                                            .updateOrderModifier(orderModifier);
                                }
                            }
                        }
                    }
                }
            }
            LogUtil.i(TAG, "=====11111");
            order.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS);
            order.setIsWaiterPrint(0);
            OrderSQL.updateWaiterPrint(0, order.getId());

            //	当前Order未完成时更新状态
            OrderSQL.updateUnFinishedOrderFromWaiter(order);
            OrderSQL.updateFromWaiterName(order);
            //	这边重新从数据中获取OrderDetail 不依赖于waiter过来的数据
            List<OrderDetail> orderDetails = OrderDetailSQL
                    .getOrderDetails(order.getId());
            if (!orderDetails.isEmpty()) {
                Order placedOrder = OrderSQL.getOrder(order.getId());
                if (placedOrder.getOrderNo().intValue() == 0) {
                    order.setOrderNo(OrderHelper.calculateOrderNo(order.getBusinessDate()));
                    OrderSQL.updateOrderNo(order);
                }
                OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                        order, App.instance.getRevenueCenter());
                OrderBillSQL.add(orderBill);
            }
            LogUtil.i(TAG, "4444444444");
//			RoundAmount roundAmount = ObjectFactory.getInstance()
//					.getRoundAmount(order, orderBill, App.instance.getLocalRestaurantConfig().getRoundType());
//			RoundAmountSQL.update(roundAmount);
            String kotCommitStatus;
            KotSummary kotSummary = ObjectFactory.getInstance().getKotSummaryForPlace(
                    TableInfoSQL.getTableById(order.getTableId()).getName(),
                    order, App.instance.getRevenueCenter(), App.instance.getBusinessDate());
            User user = UserSQL.getUserById(order.getUserId());
            if (user != null) {
                if (!TextUtils.isEmpty(order.getWaiterInformation())) {
                    Map<String, String> waiterMap = new LinkedHashMap<String, String>();
                    waiterMap = CommonUtil.getStringToMap(order.getWaiterInformation());
                    String waiterName = "";
                    if (waiterMap != null && waiterMap.size() > 0) {

                        for (Iterator it = waiterMap.entrySet().iterator(); it.hasNext(); ) {
                            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                            if (!"".equals(entry.getValue())) {
                                waiterName = entry.getValue();
                            }
                        }
                        kotSummary.setEmpName(waiterName);
                        KotSummarySQL.updateKotSummaryEmpById(waiterName, kotSummary.getId().intValue());
                    }
                } else {
                    String empName = user.getFirstName() + user.getLastName();
                    kotSummary.setEmpName(empName);
                    KotSummarySQL.updateKotSummaryEmpById(empName, kotSummary.getId().intValue());
                }

            }
            List<Integer> orderDetailIds = new ArrayList<Integer>();
            ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
            ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
            kotCommitStatus = ParamConst.JOB_NEW_KOT;
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_PREPARED) {
                    continue;
                }
                if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                    kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
                } else {
                    KotItemDetail kotItemDetail = ObjectFactory
                            .getInstance()
                            .getKotItemDetail(
                                    order,
                                    orderDetail,
                                    CoreData.getInstance().getItemDetailById(
                                            orderDetail.getItemId(), orderDetail.getItemName()),
                                    kotSummary, App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                    kotItemDetail.setItemNum(orderDetail.getItemNum());
                    if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UNDONE) {
                        kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
                        kotItemDetail
                                .setKotStatus(ParamConst.KOT_STATUS_UPDATE);
                    }
                    KotItemDetailSQL.update(kotItemDetail);
                    kotItemDetails.add(kotItemDetail);
                    orderDetailIds.add(orderDetail.getId());
                    ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                            .getOrderModifiers(order, orderDetail);
                    for (OrderModifier orderModifier : orderModifiers) {
                        KotItemModifier kotItemModifier = ObjectFactory
                                .getInstance().getKotItemModifier(
                                        kotItemDetail,
                                        orderModifier,
                                        CoreData.getInstance().getModifier(
                                                orderModifier.getModifierId()));
                        KotItemModifierSQL.update(kotItemModifier);
                        kotItemModifiers.add(kotItemModifier);
                    }
                }
            }
            result.put("order", order);
            result.put("orderDetails", OrderDetailSQL.getOrderDetails(order.getId()));
            result.put("newOrderDetails", newOrderDetails);
            result.put("orderModifiers",
                    OrderModifierSQL.getAllOrderModifier(order));
            result.put("orderDetailTaxs",
                    OrderDetailTaxSQL.getAllOrderDetailTax(order));
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
            // App.getTopActivity().httpRequestAction(MainPage.WAITER_SEND_KDS,
            // kotMap);
            Map<String, Object> orderMap = new HashMap<String, Object>();
            orderMap.put("orderId", order.getId());
            orderMap.put("orderDetailIds", orderDetailIds);
            LogUtil.i(TAG, "3333333333333");
            if (!kotItemDetails.isEmpty()) {
                KotSummarySQL.update(kotSummary);
                if (!App.instance.isRevenueKiosk() && App.instance.getSystemSettings().isOrderSummaryPrint()) {
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    if (printer == null) {
                        UIHelp.showToast(
                                App.getTopActivity(), App.getTopActivity().getResources().getString(R.string.setting_printer));
                    } else {
                        App.instance.remoteOrderSummaryPrint(printer, kotSummary, kotItemDetails, kotItemModifiers);
                    }
                }
                App.instance.getKdsJobManager()
                        .tearDownKot(kotSummary, kotItemDetails, kotItemModifiers,
                                kotCommitStatus, orderMap);
                App.getTopActivity().httpRequestAction(
                        MainPage.VIEW_EVENT_SET_DATA, order.getId());
            } else {
                KotSummarySQL.deleteKotSummary(kotSummary);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        LogUtil.i(TAG, "22222222222222");
        return resp;
    }

    /*
     * Handle request from KDS
     */

    private Response handlerKDSIpChange(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        try {
            JSONObject jsonObject = new JSONObject(params);
			/*
			 * {"device_id":220,
			 *  "mac":"98:3b:16:18:5a:94",
			 * "userKey":"f1b02b44-0a92-4c6c-a473-0534844067bc",
				"appVersion":"1.0.9",
				"ip":"192.168.0.8",
				"name":"KDS 1 KITCHEN"}
			*/
            String ip_str = jsonObject.optString("ip");
            int devideid = jsonObject.optInt("device_id");
            String mac = jsonObject.optString("mac");
            String devicename = jsonObject.optString("name");


            KDSDevice device = new KDSDevice();
            device.setDevice_id(devideid);
            device.setIP(ip_str);
            device.setMac(mac);
            device.setName(devicename);

            LocalDevice localDevice = ObjectFactory.getInstance()
                    .getLocalDevice(device.getName(), "kds",
                            ParamConst.DEVICE_TYPE_KDS,
                            device.getDevice_id(),
                            device.getIP(), device.getMac(), "", 0);

            CoreData.getInstance().addLocalDevice(localDevice);

            App.instance.addKDSDevice(devideid, device);

            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        return resp;
    }

// Not used
//	private Response handlerKotComplete(String params) {
//		Response resp;
//		Map<String, Object> result = new HashMap<String, Object>();
//		try {
//			JSONObject jsonObject = new JSONObject(params);
//
//			Gson gson = new Gson();
//			List<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
//			kotItemDetails = gson.fromJson(
//					jsonObject.optJSONObject("kotItemDtails").toString(),
//					new TypeToken<List<KotItemDetail>>() {
//					}.getType());
//
//			List<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
//			kotItemModifiers = gson.fromJson(
//					jsonObject.optJSONObject("kotItemModifiers").toString(),
//					new TypeToken<List<KotItemDetail>>() {
//					}.getType());
//
//			KotSummary kotSummary = gson.fromJson(
//					jsonObject.optJSONObject("kotSummary").toString(),
//					KotSummary.class);
//
//			result.put("resultCode", ResultCode.SUCCESS);
//			resp = this.getJsonResponse(new Gson().toJson(result));
//		} catch (Exception e) {
//			e.printStackTrace();
//			resp = this.getInternalErrorResponse("Internal Error");
//		}
//		return resp;
//	}

    private Response handlerKitGetPrinters(String params) {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(params);
            int printerType = jsonObject.optInt("printerType");
            int employeeId = jsonObject.optInt("employeeId");
            // verify employee
            User usr = CoreData.getInstance().getUserByEmpId(employeeId);
            if (usr != null) {
                RevenueCenter rc = App.instance.getRevenueCenter();
                Boolean isPermitted = CoreData.getInstance()
                        .checkUserKDSAccessInRevcenter(usr.getId(),
                                rc.getRestaurantId(), rc.getId());
                if (isPermitted) {
                    List<Printer> printers = PrinterSQL.getAllPrinter();
//                    List<Printer> printers = PrinterSQL
//                            .getAllPrinterByType(printerType);

                    List<PrinterGroup> printerGroups = PrinterGroupSQL.getAllPrinterGroup();

                    LogUtil.log("http printers : " + printers.toString());
                    result.put("printers", printers);
                    result.put("printer_group", printerGroups);
                    result.put("user", usr);
                    result.put("resultCode", ResultCode.SUCCESS);
                } else {
                    result.put("resultCode", ResultCode.USER_NO_PERMIT);
                    result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                    result.put("printers", null);
                }
            } else {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
                result.put("printers", null);
            }
            resp = this.getJsonResponse(new Gson().toJson(result));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        return resp;
    }

    private Response handlerGetPrinters() {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        int id = CoreData.getInstance()
                .getRestaurant().getId().intValue();
        List<Printer> printers = CoreData.getInstance()
                .getNameOfPrintergroup();
        result.put("resultCode", ResultCode.SUCCESS);
        result.put("printers", printers);
        result.put("restaurantId", id);

        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    // private Response handlerKdsPairComplete(String params) {
    // Response resp;
    // Map<String, Object> result = new HashMap<String, Object>();
    // Gson gson = new Gson();
    // try {
    // JSONObject jsonObject = new JSONObject(params);
    // final KDSDevice kds = gson.fromJson(jsonObject.optJSONObject("kds")
    // .toString(), KDSDevice.class);
    // App.instance.addKDSDevice(kds.getDevice_id(), kds);
    // MainPosInfo mainPosInfo = App.instance.getMainPosInfo();
    // result.put("resultCode", ResultCode.SUCCESS);
    // result.put("mainpos", mainPosInfo);
    // resp = this.getJsonResponse(new Gson().toJson(result));
    //
    // //Notify KDS pairing complete
    // App.getTopActivity().runOnUiThread(new Runnable() {
    //
    // @Override
    // public void run() {
    // App.getTopActivity().httpRequestAction(ParamConst.HTTP_REQ_CALLBACK_KDS_PAIRED,
    // kds);
    // }
    // });
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // resp = this.getInternalErrorResponse("Internal Error");
    // }
    // return resp;
    // }

    private Response handlerKOTOutOfStock(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject(params);
            int orderDetailId = jsonObject.getInt("orderDetailId");
            OrderDetail orderDetail = OrderDetailSQL.getOrderDetail(orderDetailId);
            ItemDetail itemDetail = ItemDetailSQL.getItemDetailById(orderDetail.getItemId(), orderDetail.getItemName());
            RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
            if (remainingStock != null) {
                Map<String, Object> reMap = new HashMap<String, Object>();
                reMap.put("itemId", itemDetail.getItemTemplateId());
                reMap.put("num", 0);
                RemainingStockSQL.updateRemainingNum(0, itemDetail.getItemTemplateId());
                SyncCentre.getInstance().updateReaminingStockByItemId(App.instance, reMap, null);
            }

//				}
//			});
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse("");
        }
        return resp;
    }

    private Response handlerNextKDSKOT(String params) {
        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();

        try {
            JSONObject jsonObject = new JSONObject(params);
            String kotSummaryStr = jsonObject.getString("kotSummary");
            String kidStr = jsonObject.getString("kotItemDetails");
            String kimStr = jsonObject.getString("kotModifiers");
            int kdsId = jsonObject.getInt("kdsId");//current kds, flag for next kds

            final ArrayList<KotItemDetail> kotItemDetails = gson.fromJson(kidStr,
                    new TypeToken<ArrayList<KotItemDetail>>() {
                    }.getType());
            final ArrayList<KotItemModifier> kotItemModifiers = gson.fromJson(kimStr,
                    new TypeToken<ArrayList<KotItemModifier>>() {
                    }.getType());

            KotSummary kotSummary = gson.fromJson(kotSummaryStr, KotSummary.class);
            int kotSummaryId;

            if (CommonSQL.isFakeId(kotSummary.getId())) {
                kotSummaryId = kotSummary.getOriginalId();
            } else {
                kotSummaryId = kotSummary.getId();
            }

            KotSummary kotSummaryLocal = KotSummarySQL.getKotSummaryById(kotSummaryId);

            if (kotSummaryLocal == null) {
                result.put("kotSummary", kotSummary);
                result.put("resultCode", ResultCode.KOTSUMMARY_IS_UNREAL);
                return this.getJsonResponse(gson.toJson(result));
            }

            List<KotItemDetail> kotItemDetailsCopy = new ArrayList<>();
            for (KotItemDetail kotItemDetail : kotItemDetails) {
                KotItemDetail kidLocal = KotItemDetailSQL.getKotItemDetailById(kotItemDetail.getId());
                if (kidLocal == null) continue;

                kidLocal.setEndTime(System.currentTimeMillis());
                KotItemDetailSQL.update(kidLocal);
                kotItemDetailsCopy.add(kidLocal);
            }

            kotSummaryLocal.setKotSummaryLog(KDSLogUtil.putLog(kotSummaryLocal.getKotSummaryLog(), kotItemDetailsCopy, App.instance.getKDSDevice(kdsId)));
            kotSummaryLocal.setKotSummaryLog(KDSLogUtil.removeTrackerLog(kotSummaryLocal.getKotSummaryLog(), kotItemDetailsCopy, App.instance.getKDSDevice(kdsId)));
            KotSummarySQL.updateKotSummaryLog(kotSummaryLocal);

            sendToNextKDS(params);

            result.put("kotSummary", kotSummary);
            result.put("kotItemDetails", kotItemDetails);
            result.put("resultCode", ResultCode.SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this.getJsonResponse(new Gson().toJson(result));
    }

    private void sendToNextKDS(String params) {

        Gson gson = new Gson();

        try {
            JSONObject jsonObject = new JSONObject(params);

            int type = jsonObject.getInt("type");
            final int kdsId = jsonObject.getInt("kdsId");
            final KotSummary kotSummary = gson.fromJson(
                    jsonObject.getString("kotSummary"), KotSummary.class);
            final ArrayList<KotItemDetail> kotItemDetails = gson.fromJson(
                    jsonObject.optString("kotItemDetails"),
                    new TypeToken<ArrayList<KotItemDetail>>() {
                    }.getType());
            final ArrayList<KotItemModifier> kotItemModifiers = gson.fromJson(
                    jsonObject.optString("kotModifiers"),
                    new TypeToken<ArrayList<KotItemModifier>>() {
                    }.getType());

            final Order order = OrderSQL.getOrder(kotSummary.getOrderId());
            if (order == null) return;

            int kotSummaryId;

            if (CommonSQL.isFakeId(kotSummary.getId())) {
                kotSummaryId = kotSummary.getOriginalId();
            } else {
                kotSummaryId = kotSummary.getId();
            }

            KotSummary kotSummaryLocal = KotSummarySQL.getKotSummaryById(kotSummaryId);

            if (kotSummaryLocal == null) {
                return;
            }

            final List<Integer> orderDetailIds = new ArrayList<>();
            List<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(order.getId());

            for (OrderDetail orderDetail : orderDetails) {
                orderDetailIds.add(orderDetail.getId());
            }

//            final ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<>();
//
//            for (KotItemDetail kid : kotItemDetails) {
//                kotItemModifiers.addAll(KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kid.getId()));
//            }

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Map<String, Object> orderMap = new HashMap<>();
                    orderMap.put("orderId", order.getId());
                    orderMap.put("orderDetailIds", orderDetailIds);
                    App.instance.getKdsJobManager().sendKOTToNextKDS(
                            kotSummary, kotItemDetails,
                            kotItemModifiers, ParamConst.JOB_TMP_KOT,
                            orderMap, kdsId);
                }
            }).start();

            deleteKdsLogs(kotSummary, kotItemDetails, App.instance.getKDSDevice(kdsId));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteKotSummary(final KotSummary kotSummary, final List<KotItemDetail> kotItemDetails) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.instance.getKdsJobManager().deleteKotSummary(kotSummary, kotItemDetails);
            }
        }).start();
    }

    private void deleteKdsLogs(final KotSummary kotSummary, final List<KotItemDetail> kotItemDetails, final KDSDevice deleteKdsLog) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.instance.getKdsJobManager().deleteKotItemDetailLogOnBalancer(kotSummary, kotItemDetails, deleteKdsLog);
            }
        }).start();
    }

    private Response handlerKOTItemComplete(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(params);

            String kotSummaryStr = jsonObject.getString("kotSummary");
            int kdsId = jsonObject.getInt("kdsId");
            final KotSummary kotSummary = gson.fromJson(kotSummaryStr, KotSummary.class);
            List<KotNotification> kotNotifications = new ArrayList<KotNotification>();
            ArrayList<KotItemDetail> kotItemDetails = gson.fromJson(
                    jsonObject.optString("kotItemDetails"),
                    new TypeToken<ArrayList<KotItemDetail>>() {
                    }.getType());

            int kotSummaryId;

            if (CommonSQL.isFakeId(kotSummary.getId())) {
                kotSummaryId = kotSummary.getOriginalId();
            } else {
                kotSummaryId = kotSummary.getId();
            }

            final KotSummary localKotSummary = KotSummarySQL.getKotSummaryById(kotSummaryId);
            if (localKotSummary == null) {
                result.put("resultCode", ResultCode.KOTSUMMARY_IS_UNREAL);
                resp = this.getJsonResponse(new Gson().toJson(result));
                return resp;
            }

            List<KotItemDetail> kotItemDetailsCopy = new ArrayList<>();
            for (KotItemDetail kotItemDetail : kotItemDetails) {
//                KotItemDetail kidLocal = KotItemDetailSQL.getKotItemDetailById(kotItemDetail.getId());
                KotItemDetail kidLocal = KotItemDetailSQL.getKotItemDetailByUniqueId(kotItemDetail.getUniqueId());
                if (kidLocal == null) continue;

                kidLocal.setKotStatus(kotItemDetail.getKotStatus());
                kidLocal.setEndTime(System.currentTimeMillis());
                KotItemDetailSQL.update(kidLocal);
                kotItemDetailsCopy.add(kidLocal);
            }

            localKotSummary.setCompleteTime(System.currentTimeMillis());
            localKotSummary.setKotSummaryLog(KDSLogUtil.putLog(localKotSummary.getKotSummaryLog(), kotItemDetailsCopy, App.instance.getKDSDevice(kdsId)));
            localKotSummary.setKotSummaryLog(KDSLogUtil.removeTrackerLog(localKotSummary.getKotSummaryLog(), kotItemDetailsCopy, App.instance.getKDSDevice(kdsId)));
            KotSummarySQL.updateKotSummaryLog(localKotSummary);
            KotSummarySQL.updateKotCompleteTime(localKotSummary);

            // : fix bug: filter out old data that may be in KDS
            ArrayList<KotItemDetail> filteredKotItemDetails = new ArrayList<KotItemDetail>();
            for (int i = 0; i < kotItemDetailsCopy.size(); i++) {
                KotItemDetail kotItemDetail = kotItemDetailsCopy.get(i);
                if (kotItemDetail.getOrderId().intValue() == localKotSummary.getOrderId().intValue())
                    filteredKotItemDetails.add(kotItemDetail);
            }

            List<KotItemDetail> resultKotItemDetails = new ArrayList<KotItemDetail>();
            // end bug fix

            for (int i = 0; i < filteredKotItemDetails.size(); i++) {
                KotItemDetail kotItemDetail = filteredKotItemDetails.get(i);
                if (TextUtils.isEmpty(localKotSummary.getNumTag())) {
                    OrderDetailSQL.updateOrderDetailStatusById(
                            ParamConst.ORDERDETAIL_STATUS_PREPARED,
                            kotItemDetail.getOrderDetailId());
                } else {
                    CPOrderDetailSQL.updateOrderDetailStatusById(
                            ParamConst.ORDERDETAIL_STATUS_PREPARED,
                            kotItemDetail.getOrderDetailId());
                }

                KotItemDetail lastSubKotItemDetail = KotItemDetailSQL.getLastKotItemDetailByOrderDetailId(localKotSummary.getId(), kotItemDetail.getOrderDetailId());
                if (lastSubKotItemDetail != null && lastSubKotItemDetail.getUnFinishQty() != (kotItemDetail.getUnFinishQty() + kotItemDetail.getFinishQty())) {
                    result.put("resultCode", ResultCode.KOT_COMPLETE_USER_FAILED);
                    resp = this.getJsonResponse(new Gson().toJson(result));
                    return resp;
                } else if (kotItemDetail.getUnFinishQty() == 0) {
                    kotItemDetail.setFinishQty(kotItemDetail.getItemNum());
                    kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                    kotItemDetail.setFireStatus(ParamConst.FIRE_STATUS_DEFAULT);
                }
                KotItemDetail subKotItemDetail = ObjectFactory.getInstance()
                        .getSubKotItemDetail(kotItemDetail);
                resultKotItemDetails.add(subKotItemDetail);
                KotNotification kotNotification = ObjectFactory.getInstance()
                        .getKotNotification(App.instance.getSessionStatus(),
                                localKotSummary, subKotItemDetail);

                kotNotifications.add(kotNotification);
            }
            if (filteredKotItemDetails.size() > 0) {
                KotItemDetailSQL.addKotItemDetailList(filteredKotItemDetails);
                KotNotificationSQL.addKotNotificationList(kotNotifications);

                if (App.getTopActivity() != null)
                    App.getTopActivity().httpRequestAction(
                            MainPage.VIEW_EVENT_SET_DATA, localKotSummary.getOrderId());
                result.put("resultCode", ResultCode.SUCCESS);
                result.put("resultKotItemDetails", resultKotItemDetails);
                result.put("kotSummary", kotSummary);
                result.put("kotSummaryId", kotSummary.getId());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(App.instance.getCallAppIp())) {
                            String orderNo = localKotSummary.getNumTag() + IntegerUtils.fromat(App.instance.getRevenueCenter().getIndexId(), localKotSummary.getOrderNo());
                            SyncCentre.getInstance().callAppNo(App.instance, localKotSummary.getNumTag(), orderNo);

                        }
                        int count = KotItemDetailSQL.getKotItemDetailCountBySummaryId(localKotSummary.getId());
                        if (count == 0) {
                            KotSummarySQL.updateKotSummaryStatusById(ParamConst.KOTS_STATUS_DONE, localKotSummary.getId());
                        }

                        /* no waiter in kiosk mode*/
                        if (!App.instance.isRevenueKiosk())
//							App.getTopActivity().runOnUiThread(new Runnable() {
//
//								@Override
//								public void run() {
                            SyncCentre.getInstance()
                                    .notifyWaiterToGetNotifications(
                                            App.getTopActivity(),
                                            KotNotificationSQL
                                                    .getAllKotNotificationQty());
//								}
//							});
                        if (count == 0) {
                            Order order = OrderSQL.getOrder(localKotSummary.getOrderId().intValue());
                            if (order != null && !IntegerUtils.isEmptyOrZero(order.getAppOrderId())) {
                                AppOrder appOrder = AppOrderSQL.getAppOrderById(order.getAppOrderId().intValue());
                                appOrder.setOrderStatus(ParamConst.APP_ORDER_STATUS_PREPARED);
                                AppOrderSQL.updateAppOrder(appOrder);
                                CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                                if (cloudSync != null) {
                                    cloudSync.checkAppOrderStatus(
                                            App.instance.getRevenueCenter().getId().intValue(),
                                            appOrder.getId().intValue(),
                                            appOrder.getOrderStatus().intValue(), "",
                                            App.instance.getBusinessDate().longValue(), appOrder.getOrderNo());
                                }
                                if (App.getTopActivity() instanceof NetWorkOrderActivity) {
                                    App.getTopActivity().httpRequestAction(Activity.RESULT_OK, "");
                                }
                            }

                        }
                    }
                }).start();

                //delete kot on summary kds
                //use kotItemDetails from kds don't use local
                deleteKotSummary(localKotSummary, kotItemDetails);
                deleteKdsLogs(localKotSummary, kotItemDetails, App.instance.getKDSDevice(kdsId));
                resp = this.getJsonResponse(new Gson().toJson(result));


//                ArrayList<Integer> printerGrougIds = new ArrayList<Integer>();
//                for (KotItemDetail items : kotItemDetails) {
//                    Integer pgid = items.getPrinterGroupId();
//                    if (!printerGrougIds.contains(pgid))
//                        printerGrougIds.add(pgid);
//                }
//
//                if (printerGrougIds.size() > 0) {
//                    App.instance.getKdsJobManager().refreshAllKDS(kotItemDetails, ParamConst.JOB_REFRESH_KOT);
//                }


            } else {
                result.put("resultCode", ResultCode.KOT_COMPLETE_FAILED);
                resp = this.getJsonResponse(new Gson().toJson(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }


//not used
//	private Response handlerKOTComplete(String params) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		Response resp;
//		Gson gson = new Gson();
//		try {
//			JSONObject jsonObject = new JSONObject(params);
//			KotSummary kotSummary = gson.fromJson(
//					jsonObject.getString("kotSummary"), KotSummary.class);
//			KotSummarySQL.update(kotSummary);
//			result.put("resultCode", ResultCode.SUCCESS);
//			resp = this.getJsonResponse(new Gson().toJson(result));
//		} catch (JSONException e) {
//			e.printStackTrace();
//			resp = this.getInternalErrorResponse("Internal Error");
//		}
//		return resp;
//	}

    private boolean isValidUser(String params) {
        String userKey = null;
        try {
            userKey = new JSONObject(params).optString("userKey");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return !CommonUtil.isNull(userKey)
                && App.instance.getUserByKey(userKey) != null;
    }

    private Response handlerGetKotNotifications() {
        Response resp;
        Map<String, Object> result = new HashMap<String, Object>();
        List<KotNotification> kotNotifications = KotNotificationSQL
                .getAllKotNotification();
        List<String> tableNames = KotNotificationSQL.getTableNameList();
        List<TableAndKotNotificationList> notificationLists = new ArrayList<TableAndKotNotificationList>();

        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        for (String tableName : tableNames) {
            TableAndKotNotificationList tableAndKotNotificationList = new TableAndKotNotificationList();
            tableAndKotNotificationList.setTableName(tableName);
            for (KotNotification kotNotification : kotNotifications) {
                if (kotNotification.getTableName().equals(tableName)) {
                    tableAndKotNotificationList.getKotNotifications().add(
                            kotNotification);
                }
            }
            notificationLists.add(tableAndKotNotificationList);
        }

        result.put("notificationLists", notificationLists);
        result.put("resultCode", ResultCode.SUCCESS);
        resp = this.getJsonResponse(new Gson().toJson(result));
        return resp;
    }

    private Response handlerCollectKotItem(String params) {
        Response resp;
        JSONObject jsonObject;
        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        try {
            jsonObject = new JSONObject(params);
            KotNotification kotNotification = gson.fromJson(jsonObject
                            .optJSONObject("kotNotification").toString(),
                    KotNotification.class);
            kotNotification.setStatus(ParamConst.KOTNOTIFICATION_STATUS_DELETE);
            KotNotificationSQL.update(kotNotification);
//			App.getTopActivity().runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
            SyncCentre.getInstance().notifyWaiterToGetNotifications(
                    App.getTopActivity(),
                    KotNotificationSQL.getAllKotNotificationQty());
//				}
//			});
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (JSONException e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse("");
        }
        return resp;
    }

    private Response cancelComplete(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        Gson gson = new Gson();
        LogUtil.e(TAG, "cancelComplete1");
        try {
            JSONObject jsonObject = new JSONObject(params);
            KotSummary kotSummary = gson.fromJson(
                    jsonObject.getString("kotSummary"), KotSummary.class);
            KotItemDetail kotItemDetail = gson.fromJson(
                    jsonObject.optString("kotItemDetail"), KotItemDetail.class);
            List<KotItemDetail> newKotItemDetails = new ArrayList<KotItemDetail>();

            if (kotItemDetail.getCategoryId().intValue() != ParamConst.KOTITEMDETAIL_CATEGORYID_SUB) {
                resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
                return resp;
            }
            LogUtil.e(TAG, "cancelComplete2");
            KotItemDetail kItemDetail = KotItemDetailSQL.getKotItemDetailById(kotItemDetail.getId());
            if (kItemDetail == null) {
//				resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
                result.put("resultCode", ResultCode.KOT_COMPLETE_USER_FAILED);
                return resp = this.getJsonResponse(new Gson().toJson(result));
            }

            KotItemDetail localMainKotItemDetail = KotItemDetailSQL
                    .getMainKotItemDetailByOrderDetailId(kotItemDetail.getKotSummaryId(), kotItemDetail
                            .getOrderDetailId());
            localMainKotItemDetail.setUnFinishQty(localMainKotItemDetail
                    .getUnFinishQty().intValue()
                    + kotItemDetail.getFinishQty().intValue());
            localMainKotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNDONE);
            newKotItemDetails.add(localMainKotItemDetail);

            LogUtil.e(TAG, "cancelComplete3");
            List<KotItemDetail> localSubKotItemDetails = KotItemDetailSQL
                    .getOtherSubKotItemDetailsByOrderDetailId(localMainKotItemDetail.getKotSummaryId(), kotItemDetail);
            for (KotItemDetail localSubKotItemDetail : localSubKotItemDetails) {
                localSubKotItemDetail.setUnFinishQty(localSubKotItemDetail
                        .getUnFinishQty().intValue()
                        + kotItemDetail.getFinishQty().intValue());
                newKotItemDetails.add(localSubKotItemDetail);
            }
            if (localMainKotItemDetail.getUnFinishQty().intValue() == localMainKotItemDetail.getItemNum().intValue()) {
                OrderDetailSQL.updateOrderDetailStatusById(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD, kotItemDetail.getOrderDetailId());
            }
            LogUtil.e(TAG, "cancelComplete4");
            KotItemDetailSQL.deleteKotItemDetail(kotItemDetail);
            KotItemDetailSQL.addKotItemDetailList(newKotItemDetails);
            KotNotificationSQL.deleteAllKotNotificationsByKotItemDetail(kotItemDetail);
            App.getTopActivity().httpRequestAction(
                    MainPage.VIEW_EVENT_SET_DATA, kotSummary.getOrderId());
            result.put("resultCode", ResultCode.SUCCESS);
            result.put("newKotItemDetails", newKotItemDetails);
            LogUtil.e(TAG, "cancelComplete5");
            resp = this.getJsonResponse(new Gson().toJson(result));
//			App.getTopActivity().runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
            SyncCentre.getInstance().notifyWaiterToGetNotifications(
                    App.getTopActivity(),
                    KotNotificationSQL.getAllKotNotificationQty());
//				}
//			});
            LogUtil.e(TAG, "cancelComplete6");
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }
        LogUtil.e(TAG, "cancelComplete7");
        return resp;
    }

    private Response handlerGetBill(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        Gson gson = new Gson();

        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        try {
            JSONObject jsonObject = new JSONObject(params);
            TableInfo tables = gson.fromJson(jsonObject.getString("table"),
                    TableInfo.class);
            Order order = gson.fromJson(jsonObject.getString("order"), Order.class);
            //Table status in waiter APP is not same that of table in POS
            //need get latest status on app.
            TableInfo tabInPOS = TableInfoSQL.getTableById(tables.getPosId());
            App.getTopActivity().httpRequestAction(
                    MainPage.VIEW_EVNT_GET_BILL_PRINT, tabInPOS);

            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                    order, App.instance.getRevenueCenter());

            result.put("resultCode", ResultCode.SUCCESS);
            result.put("orderBill", orderBill);
            resp = this.getJsonResponse(new Gson().toJson(result));

        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }

    public Response handlerRePrintKOT(String params) {
        Map<String, Object> result = new HashMap<>();

        int orderId = 0;

        try {
            JSONObject jsonObject = new JSONObject(params);
            orderId = jsonObject.getInt("orderId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> printerGroupIds = new ArrayList<>();
        Map<Integer, ArrayList<KotItemDetail>> kots = new HashMap<>();
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<>();
        BaseActivity context = App.getTopActivity();

        Order order = OrderSQL.getOrder(orderId);
        KotSummary kotSummary = KotSummarySQL.getKotSummary(orderId, order.getNumTag());

        if (kotSummary == null) {
            result.put("resultCode", ResultCode.CONNECTION_FAILED);
            return this.getJsonResponse(new Gson().toJson(result));
        }

        ArrayList<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByOrderId(kotSummary.getOrderId());
        ArrayList<KotItemModifier> kotItemModifiers = KotItemModifierSQL.getAllKotItemModifier();

        for (KotItemDetail items : kotItemDetails) {
            Integer pgid = items.getPrinterGroupId();
            if (pgid.intValue() == 0) {
                result.put("resultCode", ResultCode.CONNECTION_FAILED);
                return this.getJsonResponse(new Gson().toJson(result));
            }

            if (items.getKotStatus() == ParamConst.KOT_STATUS_VOID) continue;

            int kotItemDetailId = items.getId().intValue();

            // Get all Group ids that KOT blongs to
            if (!printerGroupIds.contains(pgid))
                printerGroupIds.add(pgid);

            // kot
            if (kots.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = kots.get(pgid);
                tmp.add(items);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<>();
                tmp.add(items);
                kots.put(pgid, tmp);
            }

            // modifier
            if (mods.containsKey(pgid)) {
                ArrayList<KotItemModifier> tmp = mods.get(pgid);
                for (KotItemModifier mof : kotItemModifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
            } else {
                ArrayList<KotItemModifier> tmp = new ArrayList<>();
                for (KotItemModifier mof : kotItemModifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
                mods.put(items.getPrinterGroupId(), tmp);
            }
        }

        // add job to send it to KDS
        for (Integer prgid : printerGroupIds) {
            ArrayList<Printer> printers = CoreData.getInstance()
                    .getPrintersInGroup(prgid.intValue());

            for (Printer printer : printers) {
                // physical printer
                PrinterDevice printerDevice = App.instance.getPrinterDeviceById(printer
                        .getId());

                if (printerDevice != null) {
                    printerDevice.setGroupId(prgid.intValue());

                    boolean printed = false;

                    if ((!printerDevice.getIP().contains(":") && !printerDevice.getIP().contains(",")) || printerDevice.getIsLablePrinter() != 1) {
                        printed = App.instance.remoteKotPrint(printerDevice,
                                kotSummary, kots.get(prgid), mods.get(prgid), false);

                        if (printed) {
                            ArrayList<OrderDetail> orderDetails = new ArrayList<>();
                            synchronized (orderDetails) {
                                for (int i = 0; i < kotItemDetails.size(); i++) {
                                    OrderDetail orderDetail = OrderDetailSQL.getOrderDetail(kotItemDetails.get(i).getOrderDetailId());
                                    if (orderDetail == null) continue;
                                    orderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD);
                                    orderDetails.add(orderDetail);

                                }
                            }

                            OrderDetailSQL.addOrderDetailList(orderDetails);
                            result.put("resultCode", ResultCode.SUCCESS);
                        } else {
                            result.put("resultCode", ResultCode.CONNECTION_FAILED);
                        }
                    }
                }
            }

        }

        return this.getJsonResponse(new Gson().toJson(result));
    }

    private Response handlerPrintBill(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        Gson gson = new Gson();

        /*No waiter apps in kiosk mode */
        if (App.instance.isRevenueKiosk()) {
            result.put("resultCode", ResultCode.USER_NO_PERMIT);
            result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber() + " " + this.getClass().getName());
            resp = this.getJsonResponse(new Gson().toJson(result));
            return resp;
        }

        try {
            JSONObject jsonObject = new JSONObject(params);
            int orderId = jsonObject.getInt("orderId");
//            int type = jsonObject.getInt("type");
            Order loadOrder = OrderSQL.getUnfinishedOrder(orderId);
            if (loadOrder == null) {
                result.put("resultCode", ResultCode.ORDER_FINISHED);
                resp = this.getJsonResponse(new Gson().toJson(result));
                return resp;
            }
//            if(loadOrder.getIsWaiterPrint()==1){
//                result.put("resultCode", ResultCode.ORDER_PRINT);
//                resp = this.getJsonResponse(new Gson().toJson(result));
//                return resp;
//            }
            String tableName = jsonObject.getString("tableName");
            int deviceId = 0;
            if (jsonObject.has("deviceId")) {
                deviceId = jsonObject.getInt("deviceId");
            }
            if (orderId > 0) {
                Order order = OrderSQL.getOrder(orderId);
                if (order == null) {
                    result.put("resultCode", ResultCode.ORDER_NO_PLACE);
                    resp = this.getJsonResponse(new Gson().toJson(result));
                    return resp;
                }
                if (App.instance.getSystemSettings().isPrintWaiterOnce() && order.getIsWaiterPrint() == 1) {
                    result.put("resultCode", ResultCode.ORDER_PRINT);
                    resp = this.getJsonResponse(new Gson().toJson(result));
                    return resp;
                }
                if (order.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                    result.put("resultCode", ResultCode.ORDER_FINISHED);
                    resp = this.getJsonResponse(new Gson().toJson(result));
                    return resp;
                }
                List<OrderSplit> orderSplits = OrderSplitSQL.getUnFinishedOrderSplits(order.getId().intValue());
                if (orderSplits != null && orderSplits.size() > 0) {
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    if (deviceId != 0) {
                        printer = App.instance.getPrinterDeviceById(deviceId);
                    }
                    for (OrderSplit orderSplit : orderSplits) {
                        if (orderSplit.getOrderStatus().intValue() >= ParamConst.ORDERSPLIT_ORDERSTATUS_PAYED) {
                            continue;
                        }
                        OrderBill orderBill = ObjectFactory.getInstance()
                                .getOrderBillByOrderSplit(orderSplit,
                                        App.instance.getRevenueCenter());
                        if (orderBill == null) {
                            result.put("resultCode", ResultCode.ORDER_NO_PLACE);
                            resp = this.getJsonResponse(new Gson().toJson(result));
                            return resp;
                        }
                        ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) OrderDetailSQL
                                .getOrderDetailsByOrderAndOrderSplit(orderSplit);
                        if (orderDetails.isEmpty()) {
                            continue;
                        }
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

                        OrderSplitSQL.updateOrderSplitStatus(ParamConst.ORDERSPLIT_ORDERSTATUS_UNPAY, orderSplit.getId().intValue());
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

//                        if (type == 1) {//return data to local
//                            result.put("orderDetailTaxs", orderDetailTaxs);
//                            result.put("orderBill", orderBill);
//                        } else {
                        App.instance.remoteBillPrint(printer, title, temporaryOrder,
                                orderItems, orderModifiers, taxMap, null, null, null);
                    }
                } else {
                    OrderBill orderBill = OrderBillSQL
                            .getOrderBillByOrder(order);
                    if (orderBill == null) {
                        result.put("resultCode", ResultCode.ORDER_NO_PLACE);
                        resp = this.getJsonResponse(new Gson().toJson(result));
                        return resp;
                    }
                    PrinterTitle title = ObjectFactory.getInstance()
                            .getPrinterTitle(
                                    App.instance.getRevenueCenter(),
                                    order,
                                    App.instance.getUser().getFirstName()
                                            + App.instance.getUser()
                                            .getLastName(),
                                    tableName, 1, App.instance.getSystemSettings().getTrainType());
                    ArrayList<PrintOrderItem> orderItems = ObjectFactory
                            .getInstance().getItemList(
                                    OrderDetailSQL.getOrderDetails(order
                                            .getId()));
                    ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                            .getInstance().getItemModifierList(order, OrderDetailSQL.getOrderDetails(order
                                    .getId()));
                    List<Map<String, String>> taxMap = OrderDetailTaxSQL
                            .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), order);
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    if (deviceId != 0) {
                        printer = App.instance.getPrinterDeviceById(deviceId);
                    }
                    App.instance.remoteBillPrint(printer, title, order,
                            orderItems, orderModifiers, taxMap, null, null, null);
                    OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_UNPAY, orderId);
//                }
//                result.put("resultCode", ResultCode.SUCCESS);
//            } else {
//                result.put("resultCode", ResultCode.ORDER_NO_PLACE);
//            }
//
//            resp = this.getJsonResponse(new Gson().toJson(result));
                }
            }
            if (App.instance.getSystemSettings().isPrintWaiterOnce()) {

                OrderSQL.updateWaiterPrint(1, orderId);
                result.put("resultCode", ResultCode.SUCCESS_WAITER_ONCE);
            } else {

                OrderSQL.updateWaiterPrint(1, orderId);

                result.put("resultCode", ResultCode.SUCCESS);
            }
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }

    private Response handlerCallSpecifyNumber(final String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
//        Gson gson = new Gson();
        try {
//            JSONObject jsonObject = new JSONObject(params);
//            final String str = jsonObject.getString("callNumber");
//            final String tag = jsonObject.getString("numTag");

//            String ip = App.instance.getCallAppIp();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(App.instance.getCallAppIp())) {
//                        SyncCentre.getInstance().callAppNo(App.getTopActivity(), tag, str);
                        SyncCentre.getInstance().callAppNo(App.getTopActivity(), params);
                    }
                }
            }).start();
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }

    private Response handlerWaiterVoidItem(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        try {
            JSONObject jsonObject = new JSONObject(params);
            int orderDetailId = jsonObject.getInt("orderDetailId");
            OrderDetail orderDetail = OrderDetailSQL.getOrderDetail(orderDetailId);
            if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
                result.put("resultCode", ResultCode.VOID_ITEM_FAIL);
                return this.getJsonResponse(new Gson().toJson(result));
            } else if (!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())) {
                result.put("resultCode", ResultCode.VOID_ITEM_FAIL);
                return this.getJsonResponse(new Gson().toJson(result));
            } else if (orderDetail.getOrderSplitId() != null && orderDetail.getOrderSplitId().intValue() != 0) {
                OrderSplit orderSplit = OrderSplitSQL.get(orderDetail.getOrderSplitId().intValue());
                if (orderSplit.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                    result.put("resultCode", ResultCode.SPLIT_ORDER_FINISHED);
                    return this.getJsonResponse(new Gson().toJson(result));
                }
            }
            if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                OrderDetailSQL.setOrderDetailToVoidOrFree(
                        orderDetail,
                        ParamConst.ORDERDETAIL_TYPE_VOID);
                String kotCommitStatus = ParamConst.JOB_VOID_KOT;
                KotSummary kotSummary = KotSummarySQL.getKotSummary(orderDetail
                        .getOrderId(), "");
                KotItemDetail kotItemDetail = KotItemDetailSQL
                        .getMainKotItemDetailByOrderDetailId(kotSummary.getId(), orderDetail
                                .getId());
                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_VOID);

                KotItemDetailSQL.update(kotItemDetail);
                ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                kotItemDetails.add(kotItemDetail);
                OrderDetail freeOrderDetail = OrderDetailSQL
                        .getOrderDetail(
                                orderDetail.getOrderId(),
                                orderDetail);
                if (freeOrderDetail != null) {
                    KotItemDetail freeKotItemDetail = KotItemDetailSQL
                            .getMainKotItemDetailByOrderDetailId(kotSummary.getId(), freeOrderDetail
                                    .getId());
                    freeKotItemDetail.setKotStatus(ParamConst.KOT_STATUS_VOID);
                    KotItemDetailSQL.update(freeKotItemDetail);
                    kotItemDetails.add(freeKotItemDetail);
                }

                //: fix issue: kot print no modifier showup
                // look for kot modifiers
                Order placedOrder = OrderSQL.getOrder(orderDetail.getOrderId());
                ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                        .getOrderModifiers(placedOrder, orderDetail);
                for (OrderModifier orderModifier : orderModifiers) {
                    if (orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
                        Modifier mod = CoreData.getInstance().getModifier(orderModifier.getModifierId());
                        if (mod != null) {
                            KotItemModifier kotItemModifier = KotItemModifierSQL
                                    .getKotItemModifier(kotItemDetail.getId(), mod.getId());
                            if (kotItemModifier != null)
                                kotItemModifiers.add(kotItemModifier);
                        }
                    }
                }
                //end fix

                Map<String, Object> orderMap = new HashMap<String, Object>();
                ArrayList<Integer> orderDetailIds = new ArrayList<Integer>();
                orderDetailIds.add(orderDetail.getId());
                orderMap.put("orderId", orderDetail.getOrderId());
                orderMap.put("orderDetailIds", orderDetailIds);
                App.instance.getKdsJobManager().tearDownKot(
                        kotSummary, kotItemDetails,
                        kotItemModifiers,
                        kotCommitStatus, orderMap);
                try {
                    JSONObject jsonObjectMsg = new JSONObject();
                    jsonObjectMsg.put("orderId", orderDetail.getOrderId().intValue());
                    jsonObjectMsg.put("RX", RxBus.RX_REFRESH_ORDER);
                    TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObjectMsg.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BaseActivity activity = App.getTopActivity();
                if (activity instanceof MainPage) {
                    activity.httpRequestAction(MainPage.VIEW_EVENT_SET_DATA, orderDetail.getOrderId().intValue());
                }
            } else {
                result.put("resultCode", ResultCode.VOID_ITEM_FAIL);
                return this.getJsonResponse(new Gson().toJson(result));
            }
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }


    private Response handlerWaiterUnseatTable(String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        try {
            JSONObject jsonObject = new JSONObject(params);
            int tableId = jsonObject.getInt("tableId");
            TableInfo tableInfo = TableInfoSQL.getTableById(tableId);
            if (tableInfo != null && tableInfo.getStatus().intValue() != ParamConst.TABLE_STATUS_IDLE) {
                Order order = OrderSQL.getUnfinishedOrderAtTable(tableInfo.getPosId(), App.instance.getBusinessDate(), App.instance.getSessionStatus());
                int placeOrderCount = OrderDetailSQL.getOrderDetailPlaceOrderCountByOrder(order);
                if (placeOrderCount > 0) {
                    result.put("resultCode", ResultCode.CONNOT_UNSEAT_TABLE);
                    return this.getJsonResponse(new Gson().toJson(result));
                } else {
                    OrderDetailSQL.deleteOrderDetailByOrder(order);
                    KotSummarySQL.deleteKotSummaryByOrder(order);
                    OrderBillSQL.deleteOrderBillByOrder(order);
                    OrderSQL.deleteOrder(order);
                    tableInfo.setStatus(ParamConst.TABLE_STATUS_IDLE);
                    TableInfoSQL.updateTables(tableInfo);
                    BaseActivity activity = App.getTopActivity();
                    if (activity instanceof MainPage) {
                        activity.httpRequestAction(MainPage.REFRESH_UNSEAT_TABLE_VIEW, order.getId().intValue());
                    }
                    try {
                        JSONObject jsonObjectMsg = new JSONObject();
                        jsonObjectMsg.put("tableId", tableInfo.getPosId().intValue());
                        jsonObjectMsg.put("status", ParamConst.TABLE_STATUS_IDLE);
                        jsonObjectMsg.put("RX", RxBus.RX_REFRESH_TABLE);
                        TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObjectMsg.toString(), null);
                        TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObjectMsg.toString(), null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                result.put("resultCode", ResultCode.CONNOT_UNSEAT_TABLE);
                return this.getJsonResponse(new Gson().toJson(result));
            }
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }

    private Response handlerLanguage(final String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Response resp;
        try {
            JSONObject jsonObject = new JSONObject(params);
            final String language = jsonObject.getString("language");
            final String version = jsonObject.getString("appVersion");
            SyncCentre.getInstance().setClientLanguage(App.getTopActivity(), version, language);
            result.put("resultCode", ResultCode.SUCCESS);
            resp = this.getJsonResponse(new Gson().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            resp = this.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.internal_error));
        }

        return resp;
    }

    private void handlerTransferFromOtherRvc(JSONObject jsonObject) {
        String orderData = jsonObject.optString("order");
        String orderDetail = jsonObject.optString("orderDetail");
        String kotSummary = jsonObject.optString("kotSummary");
        String kotItemDetail = jsonObject.optString("kotItemDetail");
        final String kotItemModifier = jsonObject.optString("kotItemModifier");

        String orderbill = jsonObject.optString("orderBill");
        int targetTableId = jsonObject.optInt("tableId");
        int transferType = jsonObject.optInt("transferType");

        String orderModifier = jsonObject.optString("orderModifier");
        String orderSplit = jsonObject.optString("orderSplit");

        long time = System.currentTimeMillis();
        Order order = new Gson().fromJson(orderData, Order.class);
        final Order oldOrder = new Gson().fromJson(orderData, Order.class);
        TableInfo tableInfo = TableInfoSQL.getTableById(targetTableId);
        Order orderTarget = OrderSQL.getUnfinishedOrderAtTable(targetTableId, App.instance.getBusinessDate(), App.instance.getSessionStatus());

        int persons = order.getPersons();

//        if (transferType == MainPage.ACTION_TRANSFER_TABLE) {
        //region transfer to new table
        if (orderTarget == null) {
            order.setRevenueId(App.instance.getMainPosInfo().getRevenueId());
            order.setBusinessDate(App.instance.getBusinessDate());
            order.setRestId(CoreData.getInstance().getRestaurant().getId());
            order.setPlaceId(tableInfo.getPlacesId());
            order.setTableName(tableInfo.getName());
            order.setTableId(tableInfo.getPosId());
            order.setCreateTime(time);
            order.setUpdateTime(time);
            order.setOrderNo(OrderHelper.calculateOrderNo(App.instance.getBusinessDate()));//流水号
            order.setUserId(App.instance.getUser().getId());
            OrderSQL.addOrder(order);
            order = OrderSQL.getUnfinishedOrderAtTable(targetTableId, order.getBusinessDate(), App.instance.getSessionStatus());
        } else {
            order = orderTarget;
            persons += order.getPersons();
        }

        tableInfo.setPacks(persons);
        tableInfo.setIsLocked(1);
        TableInfoSQL.updateTables(tableInfo);
        MainPage.setTablePacks(tableInfo, tableInfo.getPacks() + "");

        if (App.getTopActivity() != null)
            App.getTopActivity().httpRequestAction(
                    MainPage.SERVER_TRANSFER_TABLE_FROM_OTHER_RVC, tableInfo);

        addOrderDetailFromOtherRVC(order, orderDetail, kotSummary, orderbill, orderModifier,
                orderSplit, kotItemDetail, kotItemModifier, transferType, true);

        tableInfo.setIsLocked(0);
        TableInfoSQL.updateTables(tableInfo);

        if (App.getTopActivity() != null)
            App.getTopActivity().httpRequestAction(MainPage.SERVER_TRANSFER_TABLE_FROM_OTHER_RVC, tableInfo);

        if (!TextUtils.isEmpty(kotSummary)) {
            final KotSummary fromKotSummary = gson.fromJson(kotSummary, KotSummary.class);

            if (fromKotSummary != null) {
                KotSummary kotSummaryTarget = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());

                if (kotSummaryTarget == null) {
                    kotSummaryTarget = KotSummarySQL.getKotSummaryByUniqueId(fromKotSummary.getUniqueId());
                }

                if (kotSummaryTarget != null) {
                    kotSummaryTarget.setTableName(tableInfo.getName());

                    if (transferType == MainPage.ACTION_TRANSFER_TABLE) {

                        final Map<String, Object> parameters = new HashMap<>();

                        parameters.put("fromOrder", order);
                        parameters.put("fromTableName", oldOrder.getTableName());
                        parameters.put("toTableName", order.getTableName());
                        parameters.put("action", ParamConst.JOB_TRANSFER_KOT);

                        final KotSummary kotSummaryFinal = kotSummaryTarget;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                App.instance
                                        .getKdsJobManager()
                                        .transferTableDownKot(
                                                ParamConst.JOB_TRANSFER_KOT,
                                                null, kotSummaryFinal,
                                                parameters, true);
                            }
                        }).start();

                    } else if (transferType == MainPage.ACTION_MERGE_TABLE) {

                        //create tmp transfer kot
                        //used for transfer table to kds
                        addOrderDetailFromOtherRVC(oldOrder, orderDetail, kotSummary, orderbill, orderModifier,
                                orderSplit, kotItemDetail, kotItemModifier, transferType, false);

                        final Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("fromOrder", oldOrder);
                        parameters.put("fromTableName", oldOrder.getTableName());
                        parameters.put("toTableName", order.getTableName());
                        parameters.put("currentTableId", tableInfo.getPosId());
                        parameters.put("action", ParamConst.JOB_MERGER_KOT);

                        final KotSummary kotSummaryFinal = kotSummaryTarget;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                App.instance.getKdsJobManager()
                                        .transferTableDownKot(
                                                ParamConst.JOB_MERGER_KOT,
                                                kotSummaryFinal,
                                                fromKotSummary, parameters, true);

                                //delete tmp data
                                oldOrder.setId(-1);
                                OrderSplitSQL.deleteOrderSplitPaxByOrderId(oldOrder);
                                OrderDetailSQL.deleteOrderDetailByOrder(oldOrder);
                                OrderModifierSQL.deleteOrderModifierByOrder(oldOrder);
                                OrderBillSQL.deleteOrderBillByOrder(oldOrder);
                                KotSummarySQL.deleteKotSummaryByOrder(oldOrder);
                                KotItemDetailSQL.deleteAllKotItemDetailByOrder(oldOrder);

                                if (!TextUtils.isEmpty(kotItemModifier)) {
                                    List<KotItemModifier> kotItemModifiers = gson.fromJson(kotItemModifier,
                                            new TypeToken<List<KotItemModifier>>() {
                                            }.getType());

                                    KotItemModifierSQL.deleteKotItemModifiers(kotItemModifiers);
                                }

                            }
                        }).start();
                    }
                }
            }
        }
        //endregion

//        } else if (transferType == MainPage.ACTION_MERGE_TABLE) {
//            order.setTableId(-1);
//            OrderSQL.addOrder(order);
//            Order orderFrom = OrderSQL.getLastOrderatTabel(-1);
//
//            tableInfo.setIsLocked(1);
//            TableInfoSQL.updateTables(tableInfo);
//
//            if (App.getTopActivity() != null)
//                App.getTopActivity().httpRequestAction(
//                        MainPage.SERVER_TRANSFER_TABLE_FROM_OTHER_RVC, tableInfo);
//
//            //add fake order
//            addOrderDetailFromOtherRVC(orderFrom, orderDetail, kotSummary, orderbill, orderModifier, orderSplit, kotItemDetail, kotItemModifier);
//
//            tableInfo.setIsLocked(0);
//            TableInfoSQL.updateTables(tableInfo);

//            List<OrderDetail> orderDetails = new ArrayList<>();
//
//            List<OrderSplit> orderSplits = OrderSplitSQL.getFinishedOrderSplits(last.getId().intValue());
//            StringBuffer orderSplitIds = new StringBuffer();
//            if (orderSplits != null && orderSplits.size() > 0) {
//                for (int i = 0; i < orderSplits.size(); i++) {
//                    orderSplitIds.append(orderSplits.get(i).getId());
//                    if (i < orderSplits.size() - 1) {
//                        orderSplitIds.append(',');
//                    }
//                }
//            }
//            if (orderSplitIds.length() > 0) {
//                orderDetails.addAll(OrderDetailSQL.getUnFreeOrderDetailsWithOutSplit(last, orderSplitIds.toString()));
//            } else {
//                orderDetails.addAll(OrderDetailSQL.getUnFreeOrderDetails(last));
//            }
//
////            if (!orderDetails.isEmpty()) {
////                for (OrderDetail orderDetailNewOrder : orderDetails) {
////                    OrderDetail newOrderDetail = ObjectFactory.getInstance()
////                            .getOrderDetailForTransferTable(newOrder, orderDetailNewOrder);
////                    OrderDetailSQL.addOrderDetailETC(newOrderDetail);
////                    List<OrderModifier> orderModifiers = OrderModifierSQL
////                            .getOrderModifiers(orderDetailNewOrder);
////                    if (orderModifiers.isEmpty()) {
////                        continue;
////                    }
////                    for (OrderModifier orderModif : orderModifiers) {
////                        OrderModifier newOrderModifier = ObjectFactory
////                                .getInstance().getOrderModifier(
////                                        newOrder,
////                                        newOrderDetail,
////                                        CoreData.getInstance().getModifier(
////                                                orderModif.getModifierId()),
////                                        orderModif.getPrinterId().intValue());
////                        OrderModifierSQL.addOrderModifier(newOrderModifier);
////                    }
////                }
////            }
//
//
//            int pax = tableInfo.getPacks() + last.getPersons();
//            MainPage.setTablePacks(tableInfo, String.valueOf(pax));
//
////            if (App.getTopActivity() != null)
////                App.getTopActivity().httpRequestAction(MainPage.SERVER_TRANSFER_TABLE_FROM_OTHER_RVC, tableInfo);
////
////            if (!TextUtils.isEmpty(kotSummary)) {
////                KotSummary fromKotSummary = gson.fromJson(kotSummary, KotSummary.class);
////
////                if (fromKotSummary != null) {
////                    final KotSummary kotSummaryLocal = KotSummarySQL.getKotSummaryByUniqueId(fromKotSummary.getUniqueId());
////
////                    if (kotSummaryLocal != null) {
////                        kotSummaryLocal.setTableName(tableInfo.getName());
////
////                        final Map<String, Object> parameters = new HashMap<>();
////
////                        parameters.put("fromOrder", newOrder);
////                        parameters.put("fromTableName", oldTableName);
////                        parameters.put("toTableName", newOrder.getTableName());
////                        parameters.put("action", ParamConst.JOB_TRANSFER_KOT);
////
////                        new Thread(new Runnable() {
////                            @Override
////                            public void run() {
////
////                                App.instance
////                                        .getKdsJobManager()
////                                        .transferTableDownKot(
////                                                ParamConst.JOB_TRANSFER_KOT,
////                                                null, kotSummaryLocal,
////                                                parameters, true);
////                            }
////                        }).start();
////                    }
////                }
////            }
//
//        }

    }

    /**
     * Transfer item from other rvc
     *
     * @param jsonObject
     * @return
     */
    private Response handlerTransferItemFromOtherRvc(JSONObject jsonObject) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ResultCode.SUCCESS);

        String kotSummaryStr = "";
        String kotItemDetailStr = "";
        String kotItemModifierStr = "";
        String orderbillStr = jsonObject.optString("orderBill");
        String oldOrderDetailDataStr = jsonObject.optString("orderDetail");
        String orderModifierStr = jsonObject.optString("orderModifier");
        int packs = jsonObject.optInt("pack");
        int targetTableId = jsonObject.optInt("tableId");

        OrderDetail transfItemOrderDetail = new Gson().fromJson(oldOrderDetailDataStr, OrderDetail.class);
        OrderBill orderBill = new Gson().fromJson(orderbillStr, OrderBill.class);

        KotSummary fromKotSummary = null;
        KotSummary toKotSummary = null;
        KotItemDetail kotItemDetails = null;

        List<KotItemModifier> kotModfiers = new ArrayList<>();
        List<OrderModifier> orderModifiers = gson.fromJson(orderModifierStr, new TypeToken<List<OrderModifier>>() {
        }.getType());

        if (jsonObject.has("kotSummary")) {
            kotSummaryStr = jsonObject.optString("kotSummary");
            fromKotSummary = new Gson().fromJson(kotSummaryStr, KotSummary.class);
            toKotSummary = new Gson().fromJson(kotSummaryStr, KotSummary.class);
        }

        if (jsonObject.has("kotItemDetail")) {
            kotItemDetailStr = jsonObject.optString("kotItemDetail");
            kotItemDetails = new Gson().fromJson(kotItemDetailStr, KotItemDetail.class);
        }

        if (jsonObject.has("kotModifier")) {
            kotItemModifierStr = jsonObject.optString("kotModifier");
            kotModfiers = gson.fromJson(kotItemModifierStr, new TypeToken<List<KotItemModifier>>() {
            }.getType());
        }


        Order orderTarget = OrderSQL.getUnfinishedOrderAtTable(targetTableId, App.instance.getBusinessDate(), App.instance.getSessionStatus());

        TableInfo tableInfo = TableInfoSQL.getTableById(targetTableId);

        tableInfo.setPacks(tableInfo.getPacks() + packs);
        tableInfo.setStatus(ParamConst.TABLE_STATUS_DINING);
        TableInfoSQL.updateTables(tableInfo);
        MainPage.setTablePacks(tableInfo, packs + "");

        RevenueCenter rvc = App.instance.getRevenueCenter();
        Restaurant restaurant = CoreData.getInstance().getRestaurant();
        long time = System.currentTimeMillis();

        try {
            if (orderTarget == null) {
                orderTarget = ObjectFactory.getInstance().getOrder(
                        ParamConst.ORDER_ORIGIN_POS, App.instance.getSubPosBeanId(), tableInfo,
                        App.instance.getRevenueCenter(), App.instance.getUser(),
                        App.instance.getSessionStatus(),
                        App.instance.getBusinessDate(),
                        App.instance.getIndexOfRevenueCenter(),
                        ParamConst.ORDER_STATUS_OPEN_IN_POS,
                        App.instance.getLocalRestaurantConfig()
                                .getIncludedTax().getTax(), "");

                OrderSQL.addOrder(orderTarget);
                orderTarget = OrderSQL.getUnfinishedOrderAtTable(targetTableId, App.instance.getBusinessDate(), App.instance.getSessionStatus());
            }

            int oldOrderDetailId = transfItemOrderDetail.getId();
            int newIdOrderDetail = CommonSQL.getNextSeq(TableNames.OrderDetail);

            transfItemOrderDetail.setGroupId(0);
            transfItemOrderDetail.setOrderSplitId(0);

            ItemDetail currentRevItemDetail = ItemDetailSQL.getItemDetailByName(rvc.getId(), transfItemOrderDetail.getItemName());

            transfItemOrderDetail.setId(newIdOrderDetail);
            transfItemOrderDetail.setOrderId(orderTarget.getId().intValue());

            if (currentRevItemDetail != null) {
                transfItemOrderDetail.setItemId(currentRevItemDetail.getId());
            }

            transfItemOrderDetail.setTransferFromDetailId(oldOrderDetailId);
            transfItemOrderDetail.setOrderId(orderTarget.getId());
            transfItemOrderDetail.setOrderOriginId(orderTarget.getId());
            transfItemOrderDetail.setCreateTime(time);
            transfItemOrderDetail.setUpdateTime(time);
            transfItemOrderDetail.setUserId(orderTarget.getUserId());
            OrderDetailSQL.addOrderDetailETC(transfItemOrderDetail);

            if (orderModifiers != null) {
                for (OrderModifier data : orderModifiers) {
                    data.setId(CommonSQL.getNextSeq(TableNames.OrderModifier));
                    data.setOrderDetailId(newIdOrderDetail);
                    data.setOrderId(orderTarget.getId());
                    data.setCreateTime(time);
                    data.setUpdateTime(time);
                    data.setUserId(orderTarget.getUserId());
                    data.setItemId(transfItemOrderDetail.getItemId());

                    ItemDetail item = ItemDetailSQL.getItemDetailById(data.getItemId());
                    if (item != null) {
                        data.setPrinterId(item.getPrinterId());
                        data.setItemId(item.getId());
                    }
                    OrderModifierSQL.addOrderModifier(data);
                }
            }

            if (orderBill != null) {
                try {
                    orderBill.setId(CommonSQL.getNextSeq(TableNames.OrderBill));
                    orderBill.setOrderSplitId(transfItemOrderDetail.getOrderSplitId());
                    orderBill.setRestaurantId(restaurant.getId());
                    orderBill.setOrderId(orderTarget.getId());
                    orderBill.setRevenueId(App.instance.getMainPosInfo().getRevenueId());
                    orderBill.setBillNo(RevenueCenterSQL.getBillNoFromRevenueCenter(rvc.getId()));
                    orderBill.setOrderId(orderTarget.getId());
                    orderBill.setRevenueId(rvc.getId());
                    orderBill.setUserId(orderTarget.getUserId());
                    orderBill.setCreateTime(time);
                    orderBill.setUpdateTime(time);
                    OrderBillSQL.add(orderBill);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (App.getTopActivity() != null)
                App.getTopActivity().httpRequestAction(
                        MainPage.SERVER_TRANSFER_TABLE_FROM_OTHER_RVC, tableInfo);

            if (fromKotSummary != null) {
                KotSummary kotSummaryTarget = KotSummarySQL.getKotSummary(orderTarget.getId(), orderTarget.getNumTag());
                if (kotSummaryTarget == null) {
                    int newId = CommonSQL.getKotNextSeq(TableNames.KotSummary);
                    String uniqueId = CommonSQL.getUniqueId();

                    kotSummaryTarget = toKotSummary;//duplicate from original kot
                    kotSummaryTarget.setId(newId);
                    kotSummaryTarget.setUniqueId(uniqueId);
                    kotSummaryTarget.setOriginalUniqueId(uniqueId);
                    kotSummaryTarget.setRevenueCenterId(rvc.getId());
                    kotSummaryTarget.setRevenueCenterIndex(rvc.getIndexId());
                    kotSummaryTarget.setRevenueCenterName(rvc.getRevName());
                    kotSummaryTarget.setTableId(tableInfo.getPosId());
                    kotSummaryTarget.setOrderId(orderTarget.getId());
                    kotSummaryTarget.setOrderNo(orderTarget.getOrderNo());
                    kotSummaryTarget.setBusinessDate(App.instance.getBusinessDate());
                    kotSummaryTarget.setNumTag(orderTarget.getNumTag());
                    kotSummaryTarget.setCreateTime(time);
                    kotSummaryTarget.setUpdateTime(time);
                    kotSummaryTarget.setIsTakeAway(orderTarget.getIsTakeAway());

                    if (rvc.getIsKiosk() == ParamConst.REVENUECENTER_IS_KIOSK) {
                        kotSummaryTarget.setTableName(orderTarget.getTableName());
                    } else {
                        kotSummaryTarget.setTableName(tableInfo.getName());
                    }

                    KotSummarySQL.update(kotSummaryTarget);

                }

                if (kotItemDetails != null) {
                    kotItemDetails.setId(CommonSQL.getKotNextSeq(TableNames.KotItemDetail));
                    kotItemDetails.setOrderId(orderTarget.getId());
                    kotItemDetails.setRevenueId(App.instance.getMainPosInfo().getRevenueId());
                    kotItemDetails.setCreateTime(time);
                    kotItemDetails.setUpdateTime(time);
                    kotItemDetails.setOrderDetailId(transfItemOrderDetail.getId());
                    kotItemDetails.setKotSummaryId(kotSummaryTarget.getId());
                    kotItemDetails.setKotSummaryUniqueId(kotSummaryTarget.getUniqueId());
                    kotItemDetails.setOrderId(kotSummaryTarget.getOrderId());
                    KotItemDetailSQL.update(kotItemDetails);

                    for (KotItemModifier data : kotModfiers) {
                        data.setId(CommonSQL.getKotNextSeq(TableNames.KotItemModifier));
                        data.setKotItemDetailId(kotItemDetails.getId());
                        KotItemModifierSQL.update(data);
                    }
                }

                String fromTableName = fromKotSummary != null ? fromKotSummary.getTableName() : "";
                final Map<String, Object> parameters = new HashMap<>();
                parameters.put("action", ParamConst.JOB_MERGER_KOT);
                parameters.put("fromOrder", orderTarget);
                parameters.put("fromTableName", fromTableName);
                parameters.put("toTableName", tableInfo.getName());
                parameters.put("currentTableId", tableInfo.getPosId());

                final KotSummary toKotSummaryFinal = kotSummaryTarget;
                final KotSummary fromKotSummaryFinal = fromKotSummary;
                final KotItemDetail kotItemDetailFinal = kotItemDetails;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        App.instance.getKdsJobManager()
                                .transferItemDownKot(
                                        toKotSummaryFinal,
                                        fromKotSummaryFinal, parameters, kotItemDetailFinal, true);

                    }
                }).start();
            }

            String orderDetail = jsonObject.optString("orderDetail");

            result.put("toRevenue", new Gson().toJson(App.instance.getRevenueCenter()));
            result.put("toOrder", new Gson().toJson(orderTarget));
            result.put("tableInfo", new Gson().toJson(tableInfo));
            result.put("orderDetail", orderDetail);
            result.put("orderModifier", orderModifierStr);
            result.put("orderTarget", new Gson().toJson(orderTarget));
            result.put("tableTarget", new Gson().toJson(tableInfo));

        } catch (Exception e) {
            result.put("resultCode", ResultCode.UNKNOW_ERROR);
            e.printStackTrace();
        }


        return getJsonResponse(new Gson().toJson(result));


    }

    /**
     * @param order
     * @param orderDetail
     * @param kotSummary
     * @param orderbill
     * @param orderModifier
     * @param orderSplit
     * @param kotItemDetail
     * @param kotItemModifier
     * @param isCreateNew     true = create new id for all, base on current rvc
     */
    private void addOrderDetailFromOtherRVC(Order order, String orderDetail, String kotSummary,
                                            String orderbill, String orderModifier, String orderSplit,
                                            String kotItemDetail, String kotItemModifier, int transferType, boolean isCreateNew) {

        RevenueCenter rvc = App.instance.getRevenueCenter();
        Restaurant resto = CoreData.getInstance().getRestaurant();
        Map<Integer, Integer> mapSplit = new HashMap<>();
        long time = System.currentTimeMillis();

        try {
            List<OrderSplit> orderSplits = gson.fromJson(orderSplit,
                    new TypeToken<List<OrderSplit>>() {
                    }.getType());
            for (OrderSplit data : orderSplits) {
                if (isCreateNew) {
                    int newId = CommonSQL.getNextSeq(TableNames.OrderSplit);
                    mapSplit.put(data.getId(), newId);

                    data.setId(newId);
                    data.setRevenueId(App.instance.getMainPosInfo().getRevenueId());
                    data.setTableId(order.getTableId());
                    data.setOrderId(order.getId());
                    data.setRevenueId(App.instance.getMainPosInfo().getRevenueId());
                } else {
                    //fake id to delete temporary data
                    //on merge table
                    data.setOrderId(-1);
                }

                OrderSplitSQL.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Map<Integer, Integer> mapOrderDetail = new HashMap<>();
        Map<Integer, Integer> mapItemId = new HashMap<>();
        try {
            List<OrderDetail> orderDetails = gson.fromJson(orderDetail,
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());

            for (OrderDetail orderDetail1 : orderDetails) {
                int oldId = orderDetail1.getId();
                int newId = CommonSQL.getNextSeq(TableNames.OrderDetail);

                if (isCreateNew) {
                    mapOrderDetail.put(oldId, newId);
                    orderDetail1.setId(newId);
                    if (mapSplit.size() > 0) {
                        Integer value = mapSplit.get(orderDetail1.getOrderSplitId());
                        if (value != null) {
                            orderDetail1.setOrderSplitId(value);
                        }
                    }

                    ItemDetail currentRevItemDetail = ItemDetailSQL.getItemDetailByName(rvc.getId(), orderDetail1.getItemName());

                    if (currentRevItemDetail != null) {
                        mapItemId.put(orderDetail1.getItemId(), currentRevItemDetail.getId());
                        orderDetail1.setItemId(currentRevItemDetail.getId());
                    }

                    orderDetail1.setOrderOriginId(orderDetail1.getOrderId());
                    orderDetail1.setOrderId(order.getId());
                    orderDetail1.setCreateTime(time);
                    orderDetail1.setUpdateTime(time);
                    orderDetail1.setUserId(order.getUserId());
                } else {
                    //fake id to delete temporary data
                    //on merge table
                    orderDetail1.setOrderId(-1);
                }

                OrderDetailSQL.addOrderDetailETC(orderDetail1);
            }

            List<OrderModifier> orderModifiers = gson.fromJson(orderModifier,
                    new TypeToken<List<OrderModifier>>() {
                    }.getType());

            if (orderModifiers != null) {
                for (OrderModifier data : orderModifiers) {
                    if (isCreateNew) {
                        data.setId(CommonSQL.getNextSeq(TableNames.OrderModifier));
                        data.setOrderId(order.getId());
                        data.setOrderDetailId(mapOrderDetail.get(data.getOrderDetailId()));
                        data.setCreateTime(time);
                        data.setUpdateTime(time);
                        data.setUserId(order.getUserId());
                        data.setItemId(mapItemId.get(data.getItemId()));

                        ItemDetail item = ItemDetailSQL.getItemDetailById(data.getItemId());
                        if (item != null) {
                            data.setPrinterId(item.getPrinterId());
                            data.setItemId(item.getId());
                        }
                    } else {
                        //fake id to delete temporary data
                        //on merge table
                        data.setOrderId(-1);
                    }
                    OrderModifierSQL.addOrderModifier(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<OrderBill> orderbills = gson.fromJson(orderbill,
                    new TypeToken<List<OrderBill>>() {
                    }.getType());
            for (OrderBill orderbill1 : orderbills) {
                if (isCreateNew) {
                    orderbill1.setId(CommonSQL.getNextSeq(TableNames.OrderBill));
                    if (mapSplit.size() > 0) {
                        Integer value = mapSplit.get(orderbill1.getOrderSplitId());
                        if (value != null) {
                            orderbill1.setOrderSplitId(value);
                        }
                    }
                    orderbill1.setRestaurantId(resto.getId());
                    orderbill1.setOrderId(order.getId());
                    orderbill1.setRevenueId(App.instance.getMainPosInfo().getRevenueId());
                    orderbill1.setBillNo(RevenueCenterSQL.getBillNoFromRevenueCenter(rvc.getId()));
                    orderbill1.setOrderId(order.getId());
                    orderbill1.setRestaurantId(CoreData.getInstance().getRestaurant()
                            .getId());
                    orderbill1.setRevenueId(rvc.getId());
                    orderbill1.setUserId(order.getUserId());
                    orderbill1.setCreateTime(time);
                    orderbill1.setUpdateTime(time);
                } else {
                    //fake id to delete temporary data
                    //on merge table
                    orderbill1.setOrderId(-1);
                }

                OrderBillSQL.add(orderbill1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<Integer, Integer> mapKotSummary = new HashMap<>();
        Map<String, String> mapKotSummaryUniqueId = new HashMap<>();
        if (!TextUtils.isEmpty(kotSummary)) {

            KotSummary kot = new Gson().fromJson(kotSummary, KotSummary.class);
            if (kot != null) {
                if (isCreateNew) {
                    int newId = CommonSQL.getKotNextSeq(TableNames.KotSummary);
                    String uniqueId = CommonSQL.getUniqueId();
                    mapKotSummary.put(kot.getId(), newId);
                    mapKotSummaryUniqueId.put(kot.getUniqueId(), uniqueId);
                    kot.setId(newId);

                    if (transferType == MainPage.ACTION_MERGE_TABLE) {
                        kot.setUniqueId(uniqueId);
                        kot.setOriginalUniqueId(uniqueId);
                    }

                    kot.setRevenueCenterId(rvc.getId());
                    kot.setRevenueCenterIndex(rvc.getIndexId());
                    kot.setRevenueCenterName(rvc.getRevName());
                    kot.setTableId(order.getTableId());
                    kot.setTableName(order.getTableName());
                    kot.setOrderId(order.getId());
                    kot.setOrderNo(order.getOrderNo());
                    kot.setBusinessDate(App.instance.getBusinessDate());
                    kot.setNumTag(order.getNumTag());
                    kot.setCreateTime(time);
                    kot.setUpdateTime(time);
                } else {
                    //fake id to delete temporary data
                    //on merge table
                    kot.setOrderId(-1);
                }

                List<KotSummary> kots = new ArrayList<>();
                kots.add(kot);
                KotSummarySQL.addKotSummaryList(kots);
            }
        }

        //String kotItemDetail
        Map<Integer, Integer> mapKotItemDetail = new HashMap<>();
        try {
            if (!TextUtils.isEmpty(kotItemDetail)) {
                List<KotItemDetail> kotItemDetails = gson.fromJson(kotItemDetail,
                        new TypeToken<List<KotItemDetail>>() {
                        }.getType());
                for (KotItemDetail data : kotItemDetails) {
                    if (isCreateNew) {
                        int newId = CommonSQL.getKotNextSeq(TableNames.KotItemDetail);
                        mapKotItemDetail.put(data.getId(), newId);
                        data.setId(newId);

                        if (transferType == MainPage.ACTION_MERGE_TABLE) {
                            data.setKotSummaryUniqueId(mapKotSummaryUniqueId.get(data.getKotSummaryUniqueId()));
                        }

                        data.setOrderId(order.getId());
                        data.setRevenueId(App.instance.getMainPosInfo().getRevenueId());
                        data.setKotSummaryId(mapKotSummary.get(data.getKotSummaryId()));
                        data.setOrderDetailId(mapOrderDetail.get(data.getOrderDetailId()));
                        data.setRestaurantId(order.getRestId());
                        data.setCreateTime(time);
                        data.setUpdateTime(time);
                    } else {
                        //fake id to delete temporary data
                        //on merge table
                        data.setOrderId(-1);
                    }

                    KotItemDetailSQL.update(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!TextUtils.isEmpty(kotItemModifier)) {
                List<KotItemModifier> kotItemModifiers = gson.fromJson(kotItemModifier,
                        new TypeToken<List<KotItemModifier>>() {
                        }.getType());
                for (KotItemModifier data : kotItemModifiers) {
                    if (isCreateNew) {
                        data.setId(CommonSQL.getKotNextSeq(TableNames.KotItemModifier));
                        data.setKotItemDetailId(mapKotItemDetail.get(data.getKotItemDetailId()));
                    } else {
                        //fake id to delete temporary data
                        //on merge table
                        //data.setOrderId(-1);
                    }
                    KotItemModifierSQL.update(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
