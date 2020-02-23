package com.alfredposclient.global;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.alfredbase.APPConfig;
import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.APIName;
import com.alfredbase.javabean.LoginResult;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.PushMessage;
import com.alfredbase.javabean.model.RVCDevice;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.store.Store;
import com.alfredbase.utils.CommonUtil;
import com.alfredposclient.R;
import com.alfredposclient.http.HTTPKDSRequest;
import com.alfredposclient.http.HTTPWaiterRequest;
import com.alfredposclient.http.HttpAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SyncCentre {

    private Timer timer;

    private TimerTask timerTask;

    private static AsyncHttpClient httpClient;

    private static SyncCentre instance;
    private static SyncHttpClient syncHttpClient;
    /**
     * 大数据用
     */
    private static SyncHttpClient bigSyncHttpClient;
    public static final int MODE_FIRST_SYNC = 1;
    public static final int MODE_PUSH_SYNC = 2;

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
            httpClient.setMaxRetriesAndTimeout(0, 5 * 1000);
            httpClient.addHeader("Keep-Alive", "30");
//            httpClient.setTimeout(20 * 1000);
            httpClient.setTimeout(75 * 1000);
            syncHttpClient = new SyncHttpClient();
            syncHttpClient.addHeader("Keep-Alive", "30");
            syncHttpClient.setTimeout(20 * 1000);
            syncHttpClient.setMaxRetriesAndTimeout(0, 5 * 1000);
            bigSyncHttpClient = new SyncHttpClient();
            bigSyncHttpClient.addHeader("Keep-Alive", "30");
            bigSyncHttpClient.setTimeout(100 * 1000);
            bigSyncHttpClient.setMaxRetriesAndTimeout(0, 1 * 1000);
        }
    }

    /*  We are using JOB. no need this function
        public void startTimerTask(final BaseActivity baseActivity,final Long bizDate) {
            if (timer == null) {
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        syncOrderInfo(baseActivity,bizDate);
                    }
                };
                // 30分钟跑一次定时任务
                timer.schedule(timerTask, 0, 30 * 60 * 1000);
            }
        }
    */
    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void login(Context context, Map<String, Object> parameters,
                      Handler handler) {
        HttpAPI.login(context, parameters,
                getAbsoluteUrl(APIName.LOGIN_LOGINVERIFY), httpClient, handler);
    }

    public void getRestaurantInfo(Context context,
                                  Map<String, Object> parameters, Handler handler) {
        HttpAPI.getRestaurantInfo(context, parameters,
                getAbsoluteUrl(APIName.RESTAURANT_GETRESTAURANTINFO),
                httpClient, handler, MODE_FIRST_SYNC);
    }

    //登录后获取并同步数据
    public void syncCommonData(Context context, Handler handler) {
        HttpAPI.getUser(context, getAbsoluteUrl(APIName.USER_GETUSER),
                httpClient, handler, MODE_FIRST_SYNC);
//
        HttpAPI.mediaSync(context,
                getAbsoluteUrl(APIName.SETTLEMENT_GETOTHERPAYMENT), httpClient, handler, MODE_FIRST_SYNC);
        HttpAPI.paymentMethodSync(context,
                getAbsoluteUrl(APIName.SETTLEMENT_GETPAYMENT_METHOD), httpClient, handler, MODE_FIRST_SYNC);
        HttpAPI.getItemCategory(context,
                getAbsoluteUrl(APIName.ITEM_GETITEMCATEGORY), httpClient, handler, MODE_FIRST_SYNC);
        HttpAPI.getModifier(context, getAbsoluteUrl(APIName.ITEM_GETMODIFIER),
                httpClient, handler, MODE_FIRST_SYNC);
        HttpAPI.getTax(context, getAbsoluteUrl(APIName.TAX_GETTAX), httpClient, handler, MODE_FIRST_SYNC);
        HttpAPI.getHappyHour(context,
                getAbsoluteUrl(APIName.HAPPYHOUR_GETHAPPYHOUR), httpClient, handler, MODE_FIRST_SYNC);
        HttpAPI.getPromotionInfo(context,
                getAbsoluteUrl(APIName.PROMOTIONINFO_GETPROMOTIONINFO), httpClient, handler, MODE_FIRST_SYNC);
//        HttpAPI.getPromotionData(context,
//                getAbsoluteUrl(APIName.PROMOTIONPOSSINFO_GETPROMOTIONDATA), httpClient, handler, MODE_FIRST_SYNC);

        getRemainingStock(context, handler, MODE_FIRST_SYNC);

    }

    //修改单个菜数量
    public void updateReaminingStockByItemId(Context context, Map<String, Object> parameters, Handler handler
    ) {

        HttpAPI.updateReaminingStockByItemId(context, getAbsoluteUrl(APIName.UPDATE_REAMINING_STOCK_ITEMID), httpClient, parameters, handler);
    }

    // 下单时修改数量
    public void updateReaminingStock(Context context, Map<String, Object> parameters, Handler handler
    ) {

        HttpAPI.updateReaminingStock(context, getAbsoluteUrl(APIName.UPDATE_REAMINING_STOCK), bigSyncHttpClient, parameters, handler);
    }

    // 获取菜的数量
    public void getRemainingStock(Context context,
                                  Handler handler, int mode) {

        HttpAPI.getRemainingStock(context, getAbsoluteUrl(APIName.GET_REMAINING_STOCK), httpClient, handler, mode);
    }

    public void resetItemDetailStockNum(Context context) {

        HttpAPI.resetItemDetailStockNum(context, getAbsoluteUrl(APIName.RESET_RESTAURANT_ITEM_NUM), httpClient);
    }

    public void getPlaceInfo(Context context, Map<String, Object> parameters,
                             Handler handler) {
        HttpAPI.getItem(context, getAbsoluteUrl(APIName.ITEM_GETITEM),
                httpClient, handler, MODE_FIRST_SYNC);
//		HttpAPI.getPlaceInfo(context, parameters,
//				getAbsoluteUrl(APIName.RESTAURANT_GETPLACEINFO), httpClient,
//				handler, MODE_FIRST_SYNC);
        HttpAPI.getPlaceTable(context, getAbsoluteUrl(APIName.RESTAURANT_GETPLACEINFONEW), httpClient, parameters, handler);
    }

    public void getBindDeviceIdInfo(Context context,
                                    Map<String, Object> parameters, Handler handler) {
        HttpAPI.getBindDeviceIdInfo(context, parameters,
                getAbsoluteUrl(APIName.RESTAURANT_BINDDEVICEID), httpClient,
                handler);
    }

    public void getBOHSettlement(Context context, Handler handler) {
        HttpAPI.getBOHSettlement(context,
                getAbsoluteUrl(APIName.BOH_GETBOHHOLDUNPAID), httpClient,
                handler);
    }

    public void upDateBOHHoldPaid(Context context,
                                  Map<String, Object> parameters, Handler handler) {
        HttpAPI.uploadBOHPaidInfo(context,
                getAbsoluteUrl(APIName.BOH_UPDATEBOHHOLDPAID), httpClient,
                parameters, handler);
    }

    public void logout(Context context, Handler handler) {
        HttpAPI.logout(context, getAbsoluteUrl(APIName.LOGIN_LOGOUT),
                httpClient, handler);
    }

    /*
     *  Sync Order Info to Cloud : for JOB
     * */
//	ReportDaySales reportDaySales;
//	List<ReportDayTax> reportDayTaxs;
    public void syncSendEmail(Context context,
                              ReportDaySales reportDaySales, List<ReportDayTax> reportDayTaxs, List<ReportDayPayment> reportDayPayments, Handler handler) {
        //orderDataMsg
        HttpAPI.sendEmailSync(context, reportDaySales, reportDayTaxs, reportDayPayments,
                getAbsoluteUrl(APIName.SEND_EMAIL), bigSyncHttpClient);
    }


    public void syncMedia(Context context,
                          Handler handler) {
        //orderDataMsg

        HttpAPI.mediaSync(context,
                getAbsoluteUrl(APIName.SETTLEMENT_GETOTHERPAYMENT), bigSyncHttpClient, handler, MODE_FIRST_SYNC);
        HttpAPI.paymentMethodSync(context,
                getAbsoluteUrl(APIName.SETTLEMENT_GETPAYMENT_METHOD), bigSyncHttpClient, handler, MODE_FIRST_SYNC);

    }

    public void syncKotItemDetail(BaseActivity context, SyncMsg syncMsg, Handler handler) {
        HttpAPI.syncKotItemDetail(context, syncMsg,
                getAbsoluteUrl(APIName.SYNC_KOT_ITEM_DETAIL), bigSyncHttpClient);
    }

    public void cloudSyncUploadOrderInfo(BaseActivity context,
                                         SyncMsg syncMsg, Handler handler) {
        //orderDataMsg
        HttpAPI.cloudSync(context, syncMsg,
                getAbsoluteUrl("receive/orderDataMsg"), bigSyncHttpClient);
    }

    public void cloudSyncUploadRealOrderInfo(BaseActivity context,
                                             SyncMsg syncMsg, Handler handler) {
        //orderDataMsg
        int timely = Store.getInt(App.instance, Store.REPORT_ORDER_TIMELY);
        if (timely == 0) {
            HttpAPI.cloudSync(context, syncMsg,
                    getAbsoluteUrl("receive/orderDataMsg"), bigSyncHttpClient);
        } else {

            HttpAPI.cloudSync(context, syncMsg,
                    getAbsoluteUrl("receive/orderRealDateDataMsg"), bigSyncHttpClient);
        }
    }

    /*
     *  Sync X/Z Report Info to Cloud : for JOB
     * */
    public void cloudSyncUploadReportInfo(BaseActivity context,
                                          SyncMsg syncMsg, Handler handler) {
        //reportDataMsg

        int timely = Store.getInt(App.instance, Store.REPORT_ORDER_TIMELY);
        if (timely == 0) {
            HttpAPI.cloudSync(context, syncMsg,
                    getAbsoluteUrl("receive/reportDataMsg"), bigSyncHttpClient);
        } else {

            HttpAPI.cloudSync(context, syncMsg,
                    getAbsoluteUrl("receive/reportRealDateDataMsg"), bigSyncHttpClient);
        }

    }

    /*load day sales report from cloud */
    public void loadCloudDaySalesReport(BaseActivity context,
                                        Map<String, Object> parameters, Handler handler) {
        HttpAPI.loadCloudXZReport(context,
                getAbsoluteUrl(APIName.RESTAURANT_DAYSALES_REPORT),
                httpClient, parameters, handler);
    }

    public void loadCloudMonthlySalesReport(BaseActivity context,
                                            Map<String, Object> parameters, Handler handler) {
        HttpAPI.loadCloudMonthlySalesReport(context,
                getAbsoluteUrl(APIName.RESTAURANT_MONTHLY_SALE_REPORT),
                httpClient, parameters, handler);
    }

    public void loadCloudMonthlyPLUReport(BaseActivity context,
                                          Map<String, Object> parameters, Handler handler) {
        HttpAPI.loadCloudMonthlyPLUReport(context,
                getAbsoluteUrl(APIName.RESTAURANT_MONTHLY_PLU_REPORT),
                httpClient, parameters, handler);
    }

    public void getOrderFromApp(BaseActivity context,
                                Map<String, Object> parameters) {
        HttpAPI.getOrderFromApp(context,
                getAbsoluteUrl(APIName.POSORDER_GETORDERBYQRCODE),
                httpClient, parameters);
    }

    //设置中同步后台数据
    public void pushCommonData(Context context, String type, Handler handler) {

        if (type.equals(PushMessage.STOCK)) {
            getRemainingStock(context, handler, MODE_PUSH_SYNC);
        }
        if (type.equals(PushMessage.PROMOTION)) {
            HttpAPI.getPromotionInfo(context,
                    getAbsoluteUrl(APIName.PROMOTIONINFO_GETPROMOTIONINFO), httpClient, handler, MODE_PUSH_SYNC);
        }

        if (type.equals(PushMessage.PAYMENT_METHOD)) {
            HttpAPI.mediaSync(context,
                    getAbsoluteUrl(APIName.SETTLEMENT_GETOTHERPAYMENT), httpClient, handler, MODE_PUSH_SYNC);
            HttpAPI.paymentMethodSync(context,
                    getAbsoluteUrl(APIName.SETTLEMENT_GETPAYMENT_METHOD), httpClient, handler, MODE_PUSH_SYNC);
        }
        if (type.equals(PushMessage.HAPPY_HOURS)) {
            HttpAPI.getHappyHour(context,
                    getAbsoluteUrl(APIName.HAPPYHOUR_GETHAPPYHOUR), httpClient, handler, MODE_PUSH_SYNC);
        } else if (type.equals(PushMessage.PRINTER)) {
            HttpAPI.getRestaurantInfo(context, null,
                    getAbsoluteUrl(APIName.RESTAURANT_GETRESTAURANTINFO),
                    httpClient, handler, MODE_PUSH_SYNC);
        } else if (type.equals(PushMessage.ITEM)) {
            HttpAPI.getItem(context, getAbsoluteUrl(APIName.ITEM_GETITEM),
                    httpClient, handler, MODE_PUSH_SYNC);
            HttpAPI.getItemCategory(context,
                    getAbsoluteUrl(APIName.ITEM_GETITEMCATEGORY), httpClient, null, MODE_PUSH_SYNC);
        } else if (type.equals(PushMessage.MODIFIER)) {
            HttpAPI.getModifier(context,
                    getAbsoluteUrl(APIName.ITEM_GETMODIFIER), httpClient, handler, MODE_PUSH_SYNC);
        } else if (type.equals(PushMessage.USER)) {
            HttpAPI.getUser(context, getAbsoluteUrl(APIName.USER_GETUSER),
                    httpClient, handler, MODE_PUSH_SYNC);
        } else if (type.equals(PushMessage.RESTAURANT) || type.equals(PushMessage.REST_CONFIG)) {
            HttpAPI.getRestaurantInfo(context, null,
                    getAbsoluteUrl(APIName.RESTAURANT_GETRESTAURANTINFO),
                    httpClient, handler, MODE_PUSH_SYNC);
//		} else if (type.equals(PushMessage.PLACE_TABLE)) {
//			RevenueCenter revenueCenter = App.instance.getRevenueCenter();
//			Map<String, Object> parameters = new HashMap<String, Object>();
//			parameters.put("revenueCenter", revenueCenter);
////			HttpAPI.getPlaceTable(context, parameters,
////					getAbsoluteUrl(APIName.RESTAURANT_GETPLACEINFO),
////					httpClient, handler);
        } else if (type.equals(PushMessage.TAX)) {
            HttpAPI.getTax(context, getAbsoluteUrl(APIName.TAX_GETTAX),
                    httpClient, handler, MODE_PUSH_SYNC);
        } else if (type.equals(PushMessage.PUSH_ORDER)) {

        }
    }

    public void updatePassword(Context context, Map<String, Object> map, Handler handler) {
        User user = (User) map.get("user");
        String newPassword = (String) map.get("newPassword");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("empId", user.getEmpId());
        parameters.put("oldPassword", user.getPassword());
        parameters.put("newPassword", newPassword);
        HttpAPI.updatePassword(context, getAbsoluteUrl(APIName.USER_UPDATEPASSWORD), httpClient, parameters, handler, user);
    }

    public void getAppOrderById(Context context, Map<String, Object> parameters, Handler handler, boolean canCheck) {
        HttpAPI.getAppOrderById(context, getAbsoluteUrl(APIName.POSORDER_GETPAIEDAPPORDERBYID), syncHttpClient, parameters, handler, canCheck);
    }

    public void appOrderRefund(Context context, int appOrderId, Handler handler) {
        HttpAPI.appOrderRefund(context, getAbsoluteUrl(APIName.APP_ORDER_REFUND), httpClient, appOrderId, handler);
    }

    public void updateTableStatusForApp(Context context, int tableId, Handler handler) {
        HttpAPI.updateTableStatusForApp(context, getAbsoluteUrl(APIName.TABLE_UPDATETABLESTATUS), httpClient, tableId, handler);
    }

    public void getAllAppOrder(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.getAllAppOrder(context, getAbsoluteUrl(APIName.POSORDER_GETALLPAIEDAPPORDER), httpClient, parameters, handler);
    }

    public void updateAppOrderStatus(Context context, SyncMsg syncMsg) {
        HttpAPI.updateAppOrderStatus(context, getAbsoluteUrl(APIName.POSORDER_UPDATEAPPORDERSTATUS), syncHttpClient, syncMsg);
    }

    public void recevingAppOrderStatus(Context context, int appOrderId, Handler handler) {
        HttpAPI.recevingAppOrder(context, getAbsoluteUrl(APIName.UPDATE_MANUALAPPORDERSTATUS), httpClient, appOrderId, handler);
    }

    public void readyAppOrderStatus(Context context, int appOrderId, Handler handler) {
        HttpAPI.readyAppOrder(context, getAbsoluteUrl(APIName.UPDATE_MANUALAPPORDERSTATUS), httpClient, appOrderId, handler);
    }

    public void updatePlaceTable(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.updatePlaceTable(context, getAbsoluteUrl(APIName.RESTAURANT_CHANGEPLACE), httpClient, parameters, handler);
    }

    public void getPlaceTable(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.getPlaceTable(context, getAbsoluteUrl(APIName.RESTAURANT_GETPLACEINFONEW), httpClient, parameters, handler);
    }

    public void registStoredCard(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.registStoredCard(context, getAbsoluteUrl(APIName.MEMBERSHIP_ACTIVATECARD), httpClient, parameters, handler);
    }

    public void updateStoredCardValue(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.updateStoredCardValue(context, getAbsoluteUrl(APIName.MEMBERSHIP_OPERATEBALANCE), httpClient, parameters, handler);
    }

    public void closeStoredCard(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.closeStoredCardValue(context, getAbsoluteUrl(APIName.MEMBERSHIP_REPORTCARD), httpClient, parameters, handler);
    }

    public void changeStoredCard(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.changeStoredCardValue(context, getAbsoluteUrl(APIName.MEMBERSHIP_REATTENDCARD), httpClient, parameters, handler);
    }

    public void queryStoredCardBalance(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.queryStoredCardBalance(context, getAbsoluteUrl(APIName.MEMBERSHIP_QUERYBALANCE), httpClient, parameters, handler);
    }

    public void getAppVersion(Context context, Map<String, Object> parameters, int applicationTypes) {
        HttpAPI.getAppVersion(context, getAbsoluteUrl(APIName.SOFTWARE_GETVERSION), httpClient, parameters, applicationTypes);
    }
//	public void downloadApk(String url){
//		HttpAPI.downloadApk(url,new AsyncHttpClient());
//	}

    public void getClockInUserTimeSheet(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.getClockInUserTimeSheet(context, getAbsoluteUrl(APIName.CLOCK_GETUSERTIMESHEET), httpClient, parameters, handler);
    }

    public void clockInOut(Context context, Map<String, Object> parameters, Handler handler) {
        HttpAPI.clockInOut(context, getAbsoluteUrl(APIName.CLOCK_CLOCKINOUT), httpClient, parameters, handler);
    }

    // Backend Server IP
    private String getAbsoluteUrl(String relativeUrl) {
        if (BaseApplication.isDebug) {
//			return "http://172.16.0.190:8087/alfred-api/" + relativeUrl;
            //  return "http://192.168.104.10:8083/alfred-api/" + relativeUrl;
//            return "http://172.16.3.168:8083/alfred-api/" + relativeUrl;
            return "http://18.140.71.198//alfred-api/" + relativeUrl;
        } else if (BaseApplication.isOpenLog) {
            return "http://139.224.17.126/alfred-api/" + relativeUrl;
        } else {
            if (BaseApplication.isZeeposDev) {
                return "http://18.140.71.198//alfred-api/" + relativeUrl;
            }
            else if (BaseApplication.isCuscapiMYDev)
            {
                return "http://52.221.95.33:180/alfred-api/" + relativeUrl;
            }
            else {
//			return "http://54.169.45.214/alfred-api/" + relativeUrl;52.77.208.125
                return "http://www.servedbyalfred.biz/alfred-api/" + relativeUrl;
            }
        }
    }

    // private String getAbsoluteKDSUrl(String relativeUrl) {
    // return "http://192.168.0.18"
    // +":"+APPConfig.KDS_HTTP_SERVER_PORT+"/"+relativeUrl;
    // }

    // WaiterBacken Server URLList
//	private List<String> getAbsoluteWaiterUrlList1(String relativeUrl) {
//		List<String> urls = new ArrayList<String>();
//		Map<Integer, WaiterDevice> waiterDeviceMap = App.instance
//				.getWaiterDevices();
//		Set<Integer> key = waiterDeviceMap.keySet();
//		for (Integer index : key) {
//			WaiterDevice waiterDevice = waiterDeviceMap.get(index);
//			urls.add("http://" + waiterDevice.getIP() + ":"
//					+ APPConfig.WAITER_HTTP_SERVER_PORT + "/" + relativeUrl);
//		}
//		return urls;
//	}

    // KDSBacken Server URLList
//	private List<String> getAbsoluteKDSUrlList1(String relativeUrl) {
//		List<String> urls = new ArrayList<String>();
//		Map<Integer, KDSDevice> kdsDeviceMap = App.instance.getKDSDevices();
//		Set<Integer> key = kdsDeviceMap.keySet();
//		for (Integer index : key) {
//			KDSDevice kdsDevice = kdsDeviceMap.get(index);
//			urls.add("http://" + kdsDevice.getIP() + ":"
//					+ APPConfig.KDS_HTTP_SERVER_PORT + "/" + relativeUrl);
//		}
//		return urls;
//	}

    private String getAbsoluteKDSUrlForJob(KDSDevice kdsDevice,
                                           String relativeUrl) {
        return "http://" + kdsDevice.getIP() + ":"
                + APPConfig.KDS_HTTP_SERVER_PORT + "/" + relativeUrl;
    }

    /**
     * forWaiter
     */
    /*
     * notify waiter to get notification list parameters: {"total":##}
     */
    public void notifyWaiterToGetNotifications(final BaseActivity context,
                                               int KotNotificationqty) {

        Map<Integer, WaiterDevice> waiterDeviceMap = App.instance.getWaiterDevices();
        Set<Integer> key = waiterDeviceMap.keySet();
        for (Integer index : key) {
            WaiterDevice waiterDevice = waiterDeviceMap.get(index);
            if (waiterDevice == null || TextUtils.isEmpty(waiterDevice.getIP())) {
                continue;
            }
            String url = "http://" + waiterDevice.getIP() + ":" + APPConfig.WAITER_HTTP_SERVER_PORT + "/" + APIName.KOT_NOTIFICATION;
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("total", KotNotificationqty);
            HTTPWaiterRequest.sendKotNotification(context, parameters, url, waiterDevice.clone(), syncHttpClient);
        }

    }

    public void sendSessionClose(Context context, Map<String, Object> parameters) {

        Map<Integer, WaiterDevice> waiterDeviceMap = App.instance.getWaiterDevices();
        Set<Integer> key = waiterDeviceMap.keySet();
        for (Integer index : key) {
            WaiterDevice waiterDevice = waiterDeviceMap.get(index);
            String url = "http://" + waiterDevice.getIP() + ":" + APPConfig.WAITER_HTTP_SERVER_PORT + "/" + APIName.CLOSE_SESSION;
            HTTPWaiterRequest.sendSessionClose(context, parameters, url, waiterDevice.clone(), httpClient);
        }

        Map<Integer, KDSDevice> kdsDeviceMap = App.instance.getKDSDevices();
        key = kdsDeviceMap.keySet();
        for (Integer index : key) {
            KDSDevice kdsDevice = kdsDeviceMap.get(index);
            String url = "http://" + kdsDevice.getIP() + ":"
                    + APPConfig.KDS_HTTP_SERVER_PORT + "/" + APIName.CLOSE_SESSION;
            HTTPKDSRequest.sendSessionClose(context, parameters, url, kdsDevice.clone(), httpClient);
        }
    }

    public void transferTable(Context context, Map<String, Object> parameters) {

        Map<Integer, WaiterDevice> waiterDeviceMap = App.instance.getWaiterDevices();
        Set<Integer> key = waiterDeviceMap.keySet();
        for (Integer index : key) {
            WaiterDevice waiterDevice = waiterDeviceMap.get(index);
            String url = "http://" + waiterDevice.getIP() + ":" + APPConfig.WAITER_HTTP_SERVER_PORT + "/" + APIName.TRANSFER_TABLE;
            HTTPWaiterRequest.transferTable(context, parameters, url, waiterDevice.clone(), httpClient);
        }

    }

    /**
     * for KDS
     */

    /* sync KOT data to KDS */
    public void checkKdsBalance(KDSDevice kdsDevice, BaseActivity context,
                                Map<String, Object> parameters, Handler handler) throws Throwable {

        KDSDevice kdsBalancer = App.instance.getBalancerKDSDevice();
        if (kdsBalancer == null) return;
        String url = getAbsoluteKDSUrlForJob(kdsBalancer, APIName.CHECK_KDS_BALANCE);
        HTTPKDSRequest.checkKdsBalance(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void syncSubmitKotToKDS(KDSDevice kdsDevice, BaseActivity context,
                                   Map<String, Object> parameters, Handler handler) throws Throwable {

        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.SUBMIT_NEW_KOT);
        HTTPKDSRequest.syncSubmitKot(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void refreshSameGroupKDS(KDSDevice kdsDevice, BaseActivity context,
                                    Map<String, Object> parameters) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.REFRESH_KOT);
        HTTPKDSRequest.refreshSameGroupKDS(context, parameters, url, kdsDevice.clone(), syncHttpClient);

    }

    public void syncSubmitTmpKotToKDS(KDSDevice kdsDevice, BaseActivity context,
                                      Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.SUBMIT_TMP_KOT);
        HTTPKDSRequest.syncSubmitTmpKot(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void syncSubmitKotToNextKDS(KDSDevice kdsDevice, BaseActivity context,
                                       Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.SUBMIT_NEXT_KOT);
        HTTPKDSRequest.syncSubmitKotToNextKDS(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void syncSubmitConnectedKDS(KDSDevice kdsDevice, Context context,
                                       Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.UPDATE_CONNECTED_KDS);
        HTTPKDSRequest.syncSubmitConnectedKDS(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void syncSubmitKotToSummaryKDS(KDSDevice kdsDevice, BaseActivity context,
                                          Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.SUBMIT_SUMMARY_KDS);
        HTTPKDSRequest.syncSubmitKotToSummaryKDS(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void updateOrderCount(KDSDevice kdsDevice, BaseActivity context,
                                 Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.UPDATE_ORDER_COUNT);
        HTTPKDSRequest.updateOrderCount(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void updateKdsStatus(KDSDevice kdsDevice, BaseActivity context,
                                Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.UPDATE_KDS_STATUS);
        HTTPKDSRequest.updateKdsStatus(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void deleteKdsLogOnBalancer(KDSDevice kdsDevice, BaseActivity context,
                                       Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.DELETE_KDS_LOG_BALANCER);
        HTTPKDSRequest.deleteKdsLogOnBalancer(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void deleteKotSummary(KDSDevice kdsDevice, BaseActivity context,
                                 Map<String, Object> parameters, Handler handler) throws Throwable {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.DELETE_KOT_KDS);
        HTTPKDSRequest.deleteKotSummary(context, parameters, url, kdsDevice.clone(), syncHttpClient,
                handler);

    }

    public void syncTransferTable(KDSDevice kdsDevice, BaseActivity context,
                                  final Map<String, Object> parameters, Handler handler) {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.TRANSFER_KOT);
        HTTPKDSRequest.transferTable(context, parameters, url, kdsDevice.clone(), syncHttpClient);
    }

    public void syncTransferItem(KDSDevice kdsDevice, BaseActivity context,
                                 final Map<String, Object> parameters, Handler handler) {
        String url = getAbsoluteKDSUrlForJob(kdsDevice, APIName.TRANSFER_ITEM_KOT);
        HTTPKDSRequest.transferTable(context, parameters, url, kdsDevice.clone(), syncHttpClient);
    }

    /**
     * for callNum App
     *
     * @param context
     * @param num
     */
    public void callAppNo(final Context context, String tag, String num) {
        String url = "http://" + App.instance.getCallAppIp() + ":" + APPConfig.CALLNUM_HTTP_SERVER_PORT + "/" + APIName.CALL_POS_NUM;
        HttpAPI.callAppNo(context, url, syncHttpClient, tag, num);
    }

    public void callAppNo(final Context context, String params) {
        String url = "http://" + App.instance.getCallAppIp() + ":" + APPConfig.CALLNUM_HTTP_SERVER_PORT + "/" + APIName.CALL_POS_NUM;
        HttpAPI.callAppNo(context, url, syncHttpClient, params);
    }

    //start language

    public void setClientLanguage(final Context context, String version, String tag) {
        ArrayList<String> urls = new ArrayList<String>();

        Map<Integer, WaiterDevice> waiterDeviceMap = App.instance.getWaiterDevices();
        if (waiterDeviceMap != null && waiterDeviceMap.size() > 0) {
            Set<Integer> key = waiterDeviceMap.keySet();
            for (Integer index : key) {
                WaiterDevice waiterDevice = waiterDeviceMap.get(index);
                String url = "http://" + waiterDevice.getIP() + ":" + APPConfig.WAITER_HTTP_SERVER_PORT + "/" + APIName.POS_LANGUAGE;
                urls.add(url);
            }
        }

        Map<Integer, KDSDevice> kdsDeviceMap = App.instance.getKDSDevices();
        if (kdsDeviceMap != null && kdsDeviceMap.size() > 0) {
            Set<Integer> key = kdsDeviceMap.keySet();
            for (Integer index : key) {
                KDSDevice kdsDevice = kdsDeviceMap.get(index);
                String url = "http://" + kdsDevice.getIP() + ":" + APPConfig.KDS_HTTP_SERVER_PORT + "/" + APIName.POS_LANGUAGE;
                urls.add(url);
            }
        }

        if (!TextUtils.isEmpty(App.instance.getCallAppIp())) {
            String callNumApi = "http://" + App.instance.getCallAppIp() + ":" + APPConfig.CALLNUM_HTTP_SERVER_PORT + "/" + APIName.POS_LANGUAGE;
            urls.add(callNumApi);
        }

        for (int i = 0; i < urls.size(); i++) {
            HttpAPI.setClientLanguage(context, urls.get(i), syncHttpClient, version, tag);
        }
    }


    public void callServerLanguage(Context context, MainPosInfo mainPosInfo, Map<String, Object> parameters,
                                   Handler handler) {
        HttpAPI.setServerLanguage(context, parameters,
                getAbsoluteUrl(mainPosInfo, APIName.SET_LANGUAGE), httpClient, handler);
    }

    private String getAbsoluteUrl(MainPosInfo mainPosInfo, String url) {
        return "http://" + getIp(mainPosInfo) + ":" + APPConfig.HTTP_SERVER_PORT + "/" + url;
    }

    public String getIp(MainPosInfo mainPosInfo) {
//		if (ip == null){
        String ip = mainPosInfo.getIP();
//		}
        return ip;
    }


    //end language

    public void posCloseSession(final Context context) {
        if (!TextUtils.isEmpty(App.instance.getCallAppIp())) {
            String url = "http://" + App.instance.getCallAppIp() + ":" + APPConfig.CALLNUM_HTTP_SERVER_PORT + "/" + APIName.POS_CLOSE_SESSION;
            HttpAPI.posCloseSession(context, url, syncHttpClient);
        }

    }

    //3rd party services
    public String requestAlipayUrl(final Context context, final Map<String, Object> parameters) {
        //{restaurantKey:"h6s235",userKey:"19x6ljc",revenueId:27,orderId:123243,billNo:12312,amount:100}
        String url = null;
        StringBuffer param = new StringBuffer("userKey=" + CoreData.getInstance().getLoginResult().getUserKey() + "&");

        if (BaseApplication.isDebug) {
            url = "http://218.244.136.120:8080/alfred-api/" + APIName.REQUEST_ALIPAY;
            param.append("amount=0.01&");
        } else if (BaseApplication.isOpenLog) {
            url = "http://218.244.136.120:8080/alfred-api/" + APIName.REQUEST_ALIPAY;
            param.append("amount=0.01&");
        } else {
            if (App.instance.countryCode == ParamConst.CHINA) {
                url = "http://121.40.168.178/alfred-api/" + APIName.REQUEST_ALIPAY;
            } else {
                if (BaseApplication.isZeeposDev) {
                    url = "http://18.140.71.198//alfred-api/" + APIName.REQUEST_ALIPAY;
                }
                else if (BaseApplication.isCuscapiMYDev)
                {
                    return "http://52.221.95.33:180/alfred-api/" + APIName.REQUEST_ALIPAY;
                }
                else {
                    url = "http://www.servedbyalfred.biz/alfred-api/" + APIName.REQUEST_ALIPAY;
                }
            }
            String amount = context.getString(R.string.amount);
            param.append(amount + " =" + parameters.get("amount") + "&");
        }

        param.append("restaurantKey=" + CoreData.getInstance().getLoginResult()
                .getRestaurantKey() + "&");
        param.append("version=" + App.instance.VERSION + "&");
        param.append("deviceId=" + CommonUtil.getLocalMacAddress(App.instance) + "&");
        param.append("revenueId=" + App.instance.getRevenueCenter().getId() + "&");
        param.append("orderId=" + parameters.get("orderId") + "&");
        param.append("billNo=" + parameters.get("billNo") + "&");
        param.append("orderCreateTime=" + parameters.get("orderCreateTime") + "&");
        param.append("appOrderId=" + (parameters.get("appOrderId") == null ? "0" : parameters.get("appOrderId")));

        return (url + '?' + param);
        //HttpAPI.submitAlipay(context, url, httpClient, parameters);
    }

    public String getAlipayVerifyReturnUrl() {
        return getAbsoluteUrl("alipay/verifyReturnUrl");
    }

    public String getAlipayVerifyErrorNotifyUrl() {
        return getAbsoluteUrl("alipay/verifyErrorNotifyUrl");
    }

    public String getURLStart() {
        return getAbsoluteUrl("");
    }


    public void loginQRPayment(Context context,
                               Handler handler) {
        User user = App.instance.getUser();
        LoginResult login = CoreData.getInstance().getLoginResult();
        String url = getAbsoluteUrl(APIName.REQUEST_IPAY88_LOGIN);
        HttpAPI.loginQRPayment(context, url, user.getEmpId(), user.getPassword(), login.getRestaurantKey(), httpClient, handler);
    }


    //start pay88
    public void qrcodePay88(Context context,
                            Integer paymentTypeId, //alipay, grabpay
                            BigDecimal amount,
                            String currency,
                            String productDesc,
                            long billNo,
                            String custContact,
                            String custEmail,
                            String custName,
                            Handler handler) {

        LoginResult login = CoreData.getInstance().getLoginResult();
        String userkey = login.getUserKey();
        String url = getAbsoluteUrl(APIName.REQUEST_IPAY88_QRCODE);

        HttpAPI.qrcodePay88(context, url, login.getRestaurantKey(), userkey, paymentTypeId, amount, currency, productDesc, billNo, custContact, custEmail, custName, httpClient, handler);
    }


    public void checkStatusPay88(Context context,
                                 long billNo,
                                 BigDecimal amount,
                                 Handler handler) {

        LoginResult login = CoreData.getInstance().getLoginResult();
        String userkey = login.getUserKey();
        String url = getAbsoluteUrl(APIName.REQUEST_IPAY88_CHECK_STATUS);
        HttpAPI.checkStatusPay88(context, url, login.getRestaurantKey(), userkey, billNo, amount, httpClient, handler);
    }


    //end pay88

    //Start payhalal
    public void qrcodePayHalal(Context context,
                               Integer paymentTypeId,
                               BigDecimal amount,
                               String currency,
                               String productDesc,
                               long billNo,
                               String custContact,
                               String custEmail,
                               String custName,
                               Handler handler) {
        LoginResult login = CoreData.getInstance().getLoginResult();
        String userkey = login.getUserKey();
        String url = getAbsoluteUrl(APIName.REQUEST_PAYHALAL_QRCODE);

        HttpAPI.qrcodePayhalal(context, url, login.getRestaurantKey(), userkey, paymentTypeId, amount, currency, productDesc, billNo, custContact, custEmail, custName, httpClient, handler);
    }


    public void checkStatusPayHalal(Context context,
                                    String currency,
                                    long billNo,
                                    String transactionId,
                                    BigDecimal amount,
                                    Handler handler) {
        LoginResult login = CoreData.getInstance().getLoginResult();
        String userkey = login.getUserKey();
        String url = getAbsoluteUrl(APIName.REQUEST_PAYHALAL_CHECK_STATUS);
        HttpAPI.checkStatusPayhalal(context, url, login.getRestaurantKey(), userkey, amount, currency, billNo, transactionId, httpClient, handler);
    }
    //end payhalal

    //start connection multi RVC
    public void getOtherRVCPlaceTable(Context context, Handler handler) {
        ArrayList<String> urls = new ArrayList<String>();
        for (Map.Entry<String, RVCDevice> entry : App.instance.getRVCDevices().entrySet()) {
            RVCDevice posInfo = entry.getValue();
            urls.add(getAbsoluteUrl(posInfo.getIp(), APIName.GET_OTHER_RVC_PLACE_TABLE));
        }
        for (int i = 0; i < urls.size(); i++) {
            HttpAPI.getOtherRVCPlaceTable(context,
                    urls.get(i), httpClient, handler);
        }
    }

    public void getOtherRVCTable(Context context, String url, int placeId, Handler handler) {
        HttpAPI.getOtherRVCTable(context,
                getAbsoluteUrl(url, APIName.GET_OTHER_RVC_TABLE), placeId, httpClient, handler);

    }


    public void sendOrderToOtherRVC(Context context, String url, int transferType, Order currentOrder, int tableId, Handler handler) {
        HttpAPI.sendOrderToOtherRVC(context,
                getAbsoluteUrl(url, APIName.TRANSFER_TABLE_TO_OTHER_RVC), transferType, currentOrder, tableId, httpClient, handler);

    }

    public void transferItemToOtherRVC(Context context, String url, Order oldOrder, OrderDetail transfItemOrderDetail, TableInfo targetTable, Handler handler) {
        HttpAPI.transferItemToOtherRVC(context,
                getAbsoluteUrl(url, APIName.TRANSFER_ITEM_TO_OTHER_RVC), oldOrder, transfItemOrderDetail, targetTable.getPosId(), targetTable.getPacks(), httpClient, handler);
    }


    private String getAbsoluteUrl(String url, String subUrl) {
        return "http://" + url + ":" + APPConfig.HTTP_SERVER_PORT + "/" + subUrl;
    }


    //end conenction multi RVC
}
