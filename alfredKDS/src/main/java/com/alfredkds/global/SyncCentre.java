package com.alfredkds.global;

import android.content.Context;
import android.os.Handler;

import com.alfredbase.APPConfig;
import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.APIName;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredkds.http.HttpAPI;
import com.loopj.android.http.AsyncHttpClient;

import java.util.Map;

/* Wrapper of all HTTP Requests (KDS -> Main POS) */
public class SyncCentre {
    private String ip;
    private boolean connected = false;

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
            httpClient.setMaxRetriesAndTimeout(1, 5 * 1000);
            httpClient.setTimeout(25 * 1000);
        }
    }

    public void setNetworkStatus(boolean connected) {
        this.connected = connected;
    }

    public void login(Context context, String posIp, Map<String, Object> parameters,
                      Handler handler) {
        HttpAPI.login(context, parameters,
                getAbsolutePOSUrlByIp(posIp, APIName.LOGIN_LOGINVERIFY), httpClient, handler);
    }

    public void syncSubmitKotToKDS(KDSDevice kdsDevice, Context context,
                                   Map<String, Object> parameters, Handler handler) throws Throwable {

        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.SUBMIT_NEW_KOT);
        HttpAPI.syncSubmitKot(context, parameters, url, httpClient,
                handler);

    }

    //get All printer during pairing
    public void getPrinters(Context context, String posIp, Map<String, Object> parameters,
                            Handler handler) {
        HttpAPI.getPrinters(context, parameters,
                getAbsolutePOSUrlByIp(posIp, APIName.GET_PRINTERS), httpClient, handler);
    }

    public void getConnectedKDS(Context context, String posIp, Map<String, Object> parameters,
                                Handler handler) {
        HttpAPI.getConnectedKDS(context, parameters,
                getAbsolutePOSUrlByIp(posIp, APIName.GET_CONNECTED_KDS), httpClient, handler);
    }

    public void updateKdsStatus(Context context, String posIp, Map<String, Object> parameters,
                                Handler handler) {
        HttpAPI.updateKdsStatus(context, parameters,
                getAbsolutePOSUrlByIp(posIp, APIName.UPDATE_KDS_STATUS), httpClient, handler);
    }

    public void pairingComplete(Context context, String posIp, Map<String, Object> parameters,
                                Handler handler) {
        HttpAPI.pairingComplete(context, parameters,
                getAbsolutePOSUrlByIp(posIp, APIName.PAIRING_COMPLETE), httpClient, handler);
    }

    /* Report KDS IP Change to Main POS*/
    public void kdsIpChange(Context context, Map<String, Object> parameters,
                            Handler handler) {
        //send IP change event to all connected POS
        Map<Integer, MainPosInfo> mainPOS = App.instance.getMainPosInfos();
        for (Map.Entry<Integer, MainPosInfo> entry : mainPOS.entrySet()) {
            MainPosInfo pos = entry.getValue();
            parameters.put("userKey", CoreData.getInstance().getUserKey(pos.getRevenueId()));
            HttpAPI.kdsIpChange(context, parameters,
                    getUrl(pos.getIP(), APIName.KDS_IP_CHANGE), httpClient, handler);

        }
    }

    public void updateRemainingStock(Context context, MainPosInfo mainPosInfo, Map<String, Object> parameters,
                                     Handler handler) {
        HttpAPI.updateRemainingStock(context, parameters,
                getAbsoluteUrl(mainPosInfo, APIName.KOT_OUT_OF_STOCK), httpClient, handler);
    }

    /* Send KOT Complete to main POS*/
    public void kotComplete(Context context, MainPosInfo mainPosInfo, Map<String, Object> parameters,
                            Handler handler, int id) {
        HttpAPI.KotComplete(context, parameters,
                getAbsoluteUrl(mainPosInfo, APIName.KOT_ITEM_COMPLETE), httpClient, handler, id);
    }

    public void kotNextKDS(Context context, MainPosInfo mainPosInfo, Map<String, Object> parameters,
                           Handler handler, int id) {
        HttpAPI.kotNextKds(context, parameters,
                getAbsoluteUrl(mainPosInfo, APIName.KOT_NEXT_KDS), httpClient, handler, id);
    }

    public void callSpecifyNum(Context context, MainPosInfo mainPosInfo, Map<String, Object> parameters,
                               Handler handler, String id) {
        if (parameters != null)
            parameters.put("userKey", CoreData.getInstance().getUserKey(mainPosInfo.getRevenueId()));
        HttpAPI.callSpecifyNum(context, parameters,
                getAbsoluteUrl(mainPosInfo, APIName.CALL_SPECIFY_THE_NUMBER), httpClient, handler, id);
    }

    /* cancel KOT Complete to main POS*/
    public void cancelComplete(Context context, MainPosInfo mainPosInfo, Map<String, Object> parameters,
                               Handler handler) {
        HttpAPI.CancelComplete(context, parameters,
                getAbsoluteUrl(mainPosInfo, APIName.CANCEL_COMPLETE), httpClient, handler);
    }

    public void Logout(Context context, MainPosInfo mainPosInfo, Map<String, Object> parameters,
                       Handler handler) {
        if (parameters != null)
            parameters.put("userKey", CoreData.getInstance().getUserKey(mainPosInfo.getRevenueId()));
        HttpAPI.Logout(context, parameters,
                getAbsoluteUrl(mainPosInfo, APIName.LOGIN_LOGOUT), httpClient, handler);
    }

    private String getUrl(String ip, String url) {
        return "http://" + ip + ":" + APPConfig.HTTP_SERVER_PORT + "/"
                + url;
    }

    private String getAbsoluteUrl(MainPosInfo mainPosInfo, String url) {
        return "http://" + getIp(mainPosInfo) + ":" + APPConfig.HTTP_SERVER_PORT + "/"
                + url;
    }

    private String getAbsolutePOSUrlByIp(String ip, String url) {
        return "http://" + ip + ":" + APPConfig.HTTP_SERVER_PORT + "/"
                + url;
    }

    private String getAbsoluteKDSUrlForJob(KDSDevice kdsDevice,
                                           String relativeUrl) {
        return "http://" + kdsDevice.getIP() + ":"
                + APPConfig.KDS_HTTP_SERVER_PORT + "/" + relativeUrl;
    }

    public String getIp(MainPosInfo mainPosInfo) {
//		if (ip == null){
        ip = mainPosInfo.getIP();
//		}
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
