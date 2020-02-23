package com.alfredmenu.global;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.alfredbase.APPConfig;
import com.alfredbase.http.APIName;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;

import com.alfredmenu.http.HttpAPI;

import com.loopj.android.http.AsyncHttpClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
            httpClient.addHeader("Connection", "close");
            httpClient.setMaxRetriesAndTimeout(0, 30 * 1000);
            httpClient.setTimeout(30 * 1000);
        }
    }

    public void cancelAllRequests() {
        if (httpClient != null)
            httpClient.cancelAllRequests(true);
    }

    public void employeeId(Context context, String ip, Map<String, Object> parameters,
                           Handler handler) {
        HttpAPI.employeeId(context, parameters,
                getAbsolutePOSUrlByIp(ip, APIName.EMPLOYEE_ID), httpClient, handler);
    }

    public void login(Context context, Map<String, Object> parameters,
                      Handler handler) {
        HttpAPI.getItem(context, getAbsolutePOSUrlByIp(getIp(), APIName.ITEM_GETITEM),
                parameters, httpClient);
        HttpAPI.login(context, parameters,
                getAbsoluteUrl(APIName.LOGIN_LOGINVERIFY), httpClient, handler);
    }

    public void getRestaurantInfo(Context context,
                                  Map<String, Object> parameters, Handler handler) {
        HttpAPI.getRestaurantInfo(context,
                getAbsolutePOSUrlByIp(ip, APIName.RESTAURANT_GETRESTAURANTINFO), parameters,
                httpClient, handler);
    }

    public void getStock(Context context) {
        HttpAPI.getStock(context, getAbsolutePOSUrlByIp(getIp(), APIName.GET_REMAINING_STOCK), new HashMap<String, Object>(), httpClient);
    }

    public void syncCommonData(Context context, String ip, Map<String, Object> parameters, Handler handler) {

        HttpAPI.getUser(context, getAbsolutePOSUrlByIp(ip, APIName.USER_GETUSER),
                parameters, httpClient, handler);

        HttpAPI.getItemCategory(context,
                getAbsolutePOSUrlByIp(ip, APIName.ITEM_GETITEMCATEGORY), parameters,
                httpClient, handler);

        HttpAPI.getModifier(context, getAbsolutePOSUrlByIp(ip, APIName.ITEM_GETMODIFIER),
                parameters, httpClient, handler);

        HttpAPI.getTax(context, getAbsolutePOSUrlByIp(ip, APIName.TAX_GETTAX), parameters,
                httpClient, handler);

        HttpAPI.getHappyHour(context,
                getAbsolutePOSUrlByIp(ip, APIName.HAPPYHOUR_GETHAPPYHOUR), parameters,
                httpClient, handler);

        HttpAPI.getPrinters(context,
                getAbsolutePOSUrlByIp(ip, APIName.GET_PRINTER), parameters,
                httpClient, handler);
    }

    public void pairingComplete(Context context, String ip,
                                Map<String, Object> parameters, Handler handler) {
        HttpAPI.pairingComplete(context, parameters, getAbsolutePOSUrlByIp(ip, APIName.PAIRING_COMPLETE), httpClient, handler);
    }

    public void getPlaceInfo(Context context, String ip, Map<String, Object> parameters, Handler handler) {
        HttpAPI.getPlaceInfo(context, parameters,
                getAbsolutePOSUrlByIp(ip, APIName.RESTAURANT_GETPLACEINFO), httpClient, handler);
    }

    /*
     * select tables and get order ID*/
    public void selectTables(Context context, Map<String, Object> parameters,
                             Handler handler) {
        Map<String, String> waiterMap = new LinkedHashMap<String, String>();

        waiterMap.put(App.instance.getUser().getEmpId().toString(), App.instance.getUser().getFirstName() + "" + App.instance.getUser().getLastName());

        String waitterName = CommonUtil.getMapToString(waiterMap);
        parameters.put("waitterName", waitterName);
        HttpAPI.selectTables(context, parameters,
                getAbsoluteUrl(APIName.SELECT_TABLES), httpClient, handler);
    }

    /*
     * Get bill print */
    public void getBillPrint(Context context, Map<String, Object> parameters,
                             Handler handler) {
        HttpAPI.getBillPrint(context, parameters,
                getAbsoluteUrl(APIName.GET_BILL), httpClient, handler);
    }

    /*
    print Bill
     */
    public void printBill(Context context, Map<String, Object> parameters,
                          Handler handler) {
        HttpAPI.printBill(context, parameters,
                getAbsoluteUrl(APIName.PRINT_BILL), httpClient, handler);
    }

    public void rePrintKOT(Context context, Map<String, Object> parameters,
                           Handler handler) {
        HttpAPI.rePrintKOT(context, parameters,
                getAbsoluteUrl(APIName.RE_PRINT_KOT), httpClient, handler);
    }

    public void getPrintKOTData(Context context, Map<String, Object> parameters,
                                Handler handler) {
        HttpAPI.getPrintKOTData(context, parameters,
                getAbsoluteUrl(APIName.PRINT_KOT_DATA), httpClient, handler);
    }

    //临时菜
    public void getTemporaryDish(Context context, Map<String, Object> parameters,
                                 Handler handler) {
        HttpAPI.getTemporaryDish(context, parameters,
                getAbsoluteUrl(APIName.TEMPORARY_DISH), httpClient, handler);
    }

    // get OrderDetail Un Waiter Create
    public void handlerGetOrderDetails(Context context, Map<String, Object> parameters,
                                       Handler handler) {
        HttpAPI.handlerGetOrderDetails(context, parameters, getAbsoluteUrl(APIName.GET_ORDERDETAILS), httpClient, handler);
    }

    private String getAbsoluteUrl(String url) {
        return "http://" + getIp() + ":" + APPConfig.HTTP_SERVER_PORT + "/"
                + url;
    }

    private String getAbsolutePOSUrlByIp(String ip, String url) {
        return "http://" + ip + ":" + APPConfig.HTTP_SERVER_PORT + "/"
                + url;
    }

    public void commitOrderAndOrderDetails(Context context, Map<String, Object> parameters,
                                           Handler handler) {
        HttpAPI.commitOrderAndOrderDetails(context, parameters, getAbsoluteUrl(APIName.COMMIT_ORDER), httpClient, handler);
    }

    public void getKotNotifications(Context context, Map<String, Object> parameters,
                                    Handler handler) {
        HttpAPI.getKotNotification(context, parameters, getAbsoluteUrl(APIName.GET_KOT_NOTIFICATION), httpClient, handler);
    }

    public void handlerCollectKotItem(Context context, Map<String, Object> parameters,
                                      Handler handler) {
        HttpAPI.handlerCollectKotItem(context, parameters, getAbsoluteUrl(APIName.COLLECT_KOT_ITEM), httpClient, handler);
    }

    public void logout(Context context, Map<String, Object> parameters,
                       Handler handler) {
        HttpAPI.logout(context, parameters, getAbsoluteUrl(APIName.LOGIN_LOGOUT), httpClient, handler);
    }

    public String getIp() {
        if (ip == null) {
            ip = App.instance.getMainPosInfo().getIP();
        }
        ;
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


}
