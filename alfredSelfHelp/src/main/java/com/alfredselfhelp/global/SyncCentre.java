package com.alfredselfhelp.global;

import android.content.Context;
import android.os.Handler;

import com.alfredbase.APPConfig;
import com.alfredbase.http.APIName;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderDetailTax;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredselfhelp.http.HttpAPI;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncCentre {
    private String ip;
    private static AsyncHttpClient httpClient;

    private static SyncCentre instance;

    private SyncCentre() {

    }

    public static SyncCentre getInstance() {
        init();
        return instance;
    }

    private static void init() {
        if (instance == null) {
            instance = new SyncCentre();

            httpClient = new AsyncHttpClient();
            httpClient.addHeader("Keep-Alive", "30");
            httpClient.setMaxRetriesAndTimeout(0, 30 * 1000);
            httpClient.setTimeout(30 * 1000);
        }
    }

    public void cancelAllRequests() {
        if (httpClient != null)
            httpClient.cancelAllRequests(true);
    }

    public void login(Context context, Map<String, Object> parameters, final Handler handler) {
        HttpAPI.login(context, parameters, getAbsoluteUrl(APIName.KPMG_LOGIN), httpClient, handler);
    }

    public void getRemainingStock(Context context, Handler handler) {
        HttpAPI.getRemainingStock(context, getAbsoluteUrl(APIName.GET_REMAINING_STOCK_KPMG), httpClient, handler);
    }

    public void getCheckSotckNum(Context context,Map<String, Object> parameters , final Handler handler) {
        HttpAPI.getCheckSotckNum(context,parameters, getAbsoluteUrl(APIName.KPMG_CHECK_SOTCK_NUM), httpClient, handler);
    }


    public void updateAllData(Context context, final Handler handler) {
        HttpAPI.updateAllData(context, getAbsoluteUrl(APIName.KPMG_UPDATE_DATA), httpClient, handler);
    }

    public void updateStoredCardValue(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.updateStoredCardValue(context, getAbsoluteUrl(APIName.MEMBERSHIP_OPERATEBALANCE), httpClient, parameters, handler);
    }

    public void commitOrder(Context context, Order order, OrderBill orderBill,  List<OrderDetail> orderDetails,
                            Payment payment,  PaymentSettlement paymentSettlement,  Handler handler, String cardNum){
        Map<String, Object> map = new HashMap<>();
        List<OrderModifier> orderModifiers = OrderModifierSQL.getAllOrderModifier(order);
        List<OrderDetailTax> orderDetailTaxs = OrderDetailTaxSQL.getAllOrderDetailTax(order);
        List<Payment> payments = new ArrayList<>();
        List<OrderBill> orderBills = new ArrayList<>();

        orderBills.add(orderBill);
        List<PaymentSettlement> paymentSettlements = new ArrayList<>();
        if(paymentSettlement != null) {
            payments.add(payment);
            paymentSettlements.add(paymentSettlement);
        }
        map.put("order", order);
        map.put("orderDetails" , orderDetails);
        map.put("orderModifiers" , orderModifiers);
        map.put("orderBills" , orderBills);
        map.put("payments" , payments);
        map.put("paymentSettlements" , paymentSettlements);
        map.put("cardNum", cardNum);
        map.put("orderDetailTaxs" , orderDetailTaxs);
        map.put("orderSplits" , new ArrayList<OrderSplit>());
        map.put("roundAmounts" , new ArrayList<RoundAmount>());
        /**
         * Order order = gson.fromJson(jsonObject.getString("order"), Order.class);
         List<OrderDetail> orderDetails = gson.fromJson(jsonObject.getString("orderDetails"),
         new TypeToken<List<OrderDetail>>(){}.getType());
         List<OrderModifier> orderModifiers = gson.fromJson(jsonObject.getString("orderModifiers"),
         new TypeToken<List<OrderModifier>>(){}.getType());
         List<Payment> payments = gson.fromJson(jsonObject.getString("payments"),
         new TypeToken<List<Payment>>(){}.getType());
         List<PaymentSettlement> paymentSettlements = gson.fromJson(jsonObject.getString("paymentSettlements"),
         new TypeToken<List<PaymentSettlement>>(){}.getType());
         List<OrderDetailTax> orderDetailTaxs = gson.fromJson(jsonObject.getString("orderDetailTaxs"),
         new TypeToken<List<OrderDetailTax>>(){}.getType());
         List<OrderSplit> orderSplits = gson.fromJson(jsonObject.getString("orderSplits"),
         new TypeToken<List<OrderSplit>>(){}.getType());
         List<OrderBill> orderBills = gson.fromJson(jsonObject.getString("orderBills"),
         new TypeToken<List<OrderBill>>(){}.getType());
         List<RoundAmount> roundAmounts = gson.fromJson(jsonObject.getString("roundAmounts"),
         new TypeToken<List<RoundAmount>>(){}.getType());
         */
        HttpAPI.commitOrder(context, map, getAbsoluteUrl(APIName.KPMG_COMMIT_ORDER), httpClient, handler, paymentSettlement, cardNum);

    }

    private String getAbsoluteUrl(String url) {
        return "http://" + getIp() + ":" + APPConfig.HTTP_SERVER_PORT + "/"
                + url;
    }

    private String getAbsolutePOSUrlByIp(String ip, String url) {
        return "http://" + ip + ":" + APPConfig.HTTP_SERVER_PORT + "/"
                + url;
    }

    public String getIp() {
        if (ip == null) {
            ip = App.instance.getPairingIp();
        }
        ;
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
