package com.alfredposclient.http.server;

import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.HappyHour;
import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.NanoHTTPD;
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
import com.alfredbase.javabean.RemainingStockVo;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserRestaurant;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.sql.HappyHourSQL;
import com.alfredbase.store.sql.HappyHourWeekSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.ItemHappyHourSQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.ItemModifierSQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.PaymentSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.PrinterGroupSQL;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.store.sql.RestaurantConfigSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.SettlementRestaurantSQL;
import com.alfredbase.store.sql.SubPosCommitSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.TaxCategorySQL;
import com.alfredbase.store.sql.TaxSQL;
import com.alfredbase.store.sql.UserRestaurantSQL;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredbase.utils.StockCallBack;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KpmgResponseUtil {
    private MainPosHttpServer mainPosHttpServer;
    private Gson gson;
    public static KpmgResponseUtil instance;

    private KpmgResponseUtil() {

    }

    public static KpmgResponseUtil getInstance() {
        if (instance == null) {
            instance = new KpmgResponseUtil();
        }
        return instance;
    }

    public void init(MainPosHttpServer httpServer) {
        mainPosHttpServer = httpServer;
        gson = new Gson();
    }

    public NanoHTTPD.Response kpmgLogin(String params) {
        NanoHTTPD.Response resp = null;
        try {
            JSONObject jsonObject = new JSONObject(params);
            String employeeId = jsonObject.optString("employee_ID");
            int empId = Integer.parseInt(employeeId);
            long sessionStatusTime = 0l;
            if (jsonObject.has("sessionStatusTime")) {
                sessionStatusTime = jsonObject.optLong("sessionStatusTime");
            }
            User user = CoreData.getInstance().getUserByEmpId(empId);
            Map<String, Object> result = new HashMap<>();
            if (user != null) {
                if (user.getType() != ParamConst.USER_TYPE_MANAGER && user.getType() != ParamConst.USER_TYPE_POS
                        && user.getType() != ParamConst.USER_TYPE_WAITER) {
                    result.put(MainPosHttpServer.RESULT_CODE, ResultCode.USER_NO_PERMIT);
                    return mainPosHttpServer.getJsonResponse(new Gson().toJson(result));
                }
                result.put("user", user);
                result.put("businessDate", App.instance.getBusinessDate());
                if (sessionStatusTime != 0l && sessionStatusTime != App.instance.getSessionStatus().getTime()) {
                    result.put("resultCode", ResultCode.SESSION_HAS_CHANGE);
                } else {
                    result.put("resultCode", ResultCode.SUCCESS);
                }
                resp = mainPosHttpServer.getJsonResponse(gson.toJson(result));
            } else {
                result.put("resultCode", ResultCode.USER_NO_PERMIT);
                result.put("lineCode", Thread.currentThread().getStackTrace()[2].getLineNumber()+" "+this.getClass().getName());
                resp = mainPosHttpServer.getJsonResponse(gson.toJson(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = mainPosHttpServer.getInternalErrorResponse(App.getTopActivity().getResources().getString(R.string.login_failed));
        }
        return resp;
    }


    public NanoHTTPD.Response kpmgReaminingStock() {
        NanoHTTPD.Response resp = null;
        try {

            List<RemainingStock> remainingStocks = RemainingStockSQL.getAllRemainingStock();
            Map<String, Object> map = new HashMap<>();
            map.put("businessDate", App.instance.getBusinessDate());
            map.put("remainingStockList", remainingStocks);
            map.put("resultCode", ResultCode.SUCCESS);
            resp = mainPosHttpServer.getJsonResponse(gson.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
            resp = mainPosHttpServer.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }
    public NanoHTTPD.Response kpmgCheckSotckNum(String params) {
        NanoHTTPD.Response resp = null;
        try {
            JSONObject jsonObject = new JSONObject(params);
            List<RemainingStockVo> remainingStockVos = gson.fromJson(jsonObject.getString("remainingStockList"),
                    new TypeToken<List<RemainingStockVo>>() {
                    }.getType());
            List<RemainingStock> remainingStocks = RemainingStockSQL.getAllRemainingStock();
            List<RemainingStockVo> remainingStockList=new  ArrayList<RemainingStockVo>();
            for (int i = 0; i <remainingStockVos.size() ; i++) {
                ItemDetail itemDetail = ItemDetailSQL.getItemDetailById(remainingStockVos.get(i).getItemId());
                RemainingStock remainingStock=RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                if(remainingStockVos.get(i).getNum()>remainingStock.getQty()){
                    RemainingStockVo remainingStockVo=new RemainingStockVo();
                    remainingStockVo.setItemId(remainingStockVos.get(i).getItemId());
                    remainingStockVo.setNum(remainingStock.getQty());
                    remainingStockList.add(remainingStockVo);
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("remainingStockList", remainingStockList);
            map.put("resultCode", ResultCode.SUCCESS);
            resp = mainPosHttpServer.getJsonResponse(gson.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
            resp = mainPosHttpServer.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }

    public NanoHTTPD.Response updateAllData() {
        NanoHTTPD.Response resp = null;
        try {
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
            List<PaymentMethod> paymentMethods = PaymentMethodSQL.getAllPaymentMethod();
            List<SettlementRestaurant> settlementRestaurants = SettlementRestaurantSQL.getAllSettlementRestaurant();
            Map<String, Object> map = new HashMap<>();
            SessionStatus sessionStatus = App.instance.getSessionStatus();
            map.put("sessionStatus", sessionStatus);
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
            map.put("paymentMethods", paymentMethods);
            map.put("settlementRestaurants", settlementRestaurants);
            map.put("resultCode", ResultCode.SUCCESS);
            resp = mainPosHttpServer.getJsonResponse(gson.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
            resp = mainPosHttpServer.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }

    public NanoHTTPD.Response commitOrder(String params) {
        NanoHTTPD.Response resp = null;
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(params);
            Order order = gson.fromJson(jsonObject.getString("order"), Order.class);
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

            if(orderDetails!=null){
                for (int i = 0; i <orderDetails.size() ; i++) {
                    OrderDetail orderDetail=orderDetails.get(i);
                    final int itemTempId = CoreData.getInstance().getItemDetailById(orderDetail.getItemId(), orderDetail.getItemName()).getItemTemplateId();
                    RemainingStock remainingStock=RemainingStockSQL.getRemainingStockByitemId(itemTempId);
                    if(remainingStock!=null){
                        int num=orderDetail.getItemNum();
                        RemainingStockHelper.updateRemainingStockNum(remainingStock, num, false, new StockCallBack() {
                            @Override
                            public void onSuccess( Boolean isStock) {
                                if(isStock){
                                    App.instance.getSyncJob().updateRemainingStockNum(itemTempId);
                                }

                            }

                        });

                    }



                }

            }
            String cardNum ="";
            if(jsonObject.has("cardNum")) {
                jsonObject.getString("cardNum");
            }
            final int orderId = SubPosCommitSQL.commitOrderForKPMG(order, orderSplits, orderBills, payments, orderDetails,
                    orderModifiers, orderDetailTaxs, paymentSettlements, roundAmounts, cardNum, App.instance.getBusinessDate(),
                    App.instance.getSessionStatus().getSession_status(), TableInfoSQL.getKioskTable().getPosId());
            map.put("resultCode", ResultCode.SUCCESS);
            if (orderId != 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Order placeOrder = OrderSQL.getOrder(orderId);
                        Payment payment = PaymentSQL.getPaymentByOrderId(placeOrder.getId());
                        if (payment == null) {
                            placeOrder.setOrderStatus(ParamConst.ORDER_STATUS_KIOSK);
                            OrderSQL.updateOrder(placeOrder);
                        } else {
                            KotSummary kotSummary = ObjectFactory.getInstance()
                                    .getKotSummary(
                                            "", placeOrder,
                                            App.instance.getRevenueCenter(),
                                            App.instance.getBusinessDate());
                            if (kotSummary != null) {
                                ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                                ArrayList<KotItemDetail> kotItemDetails = new ArrayList<>();
                                List<OrderDetail> placedOrderDetails = OrderDetailSQL.getOrderDetails(placeOrder.getId());
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
                                    KotItemDetailSQL.update(kotItemDetail);
                                    kotItemDetails.add(kotItemDetail);
                                    orderDetailIds.add(orderDetail.getId());
                                    ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
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

                                Map<String, Object> orderMap = new HashMap<String, Object>();

                                orderMap.put("orderId", placeOrder.getId());
                                orderMap.put("orderDetailIds", orderDetailIds);
                                orderMap.put("orderPosType", ParamConst.POS_TYPE_MAIN);
                                App.instance.getKdsJobManager().tearDownKot(
                                        kotSummary, kotItemDetails,
                                        kotItemModifiers, ParamConst.JOB_NEW_KOT,
                                        orderMap);
                            }
                        }
                    }
                }).start();
            }


            Order placeOrder = OrderSQL.getOrder(orderId);

            ArrayList<PrintOrderItem> orderItems = ObjectFactory
                    .getInstance().getItemList(
                            OrderDetailSQL.getOrderDetails(placeOrder
                                    .getId()));
            List<Map<String, String>> taxMaps = OrderDetailTaxSQL
                    .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), placeOrder);

            ArrayList<PrintOrderModifier> orderModifierList = ObjectFactory
                    .getInstance().getItemModifierList(placeOrder, OrderDetailSQL.getOrderDetails(placeOrder
                            .getId()));

            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                    placeOrder, App.instance.getRevenueCenter());
            RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderAndBill(placeOrder, orderBill);
            List<PaymentSettlement> paymentSettlementList = PaymentSettlementSQL.getAllPaymentSettlementByOrderId(placeOrder.getId());
            PrinterTitle title = ObjectFactory.getInstance()
                    .getPrinterTitle(
                            App.instance.getRevenueCenter(),
                            placeOrder,
                            App.instance.getUser().getFirstName()
                                    + App.instance.getUser().getLastName(),
                            "", 1,App.instance.getSystemSettings().getTrainType());

            map.put("order", placeOrder);
            map.put("title", title);
            map.put("orderItems", orderItems);
            map.put("orderModifiers", orderModifierList);
            map.put("taxMaps", taxMaps);
            map.put("paymentSettlements", paymentSettlementList);
            map.put("roundAmount", roundAmount);
            resp = mainPosHttpServer.getJsonResponse(gson.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
            resp = mainPosHttpServer.getInternalErrorResponse(App.instance.getResources().getString(R.string.sync_data_failed));
        }
        return resp;
    }
}
