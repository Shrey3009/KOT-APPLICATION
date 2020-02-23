package com.alfredposclient.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.http.AsyncHttpResponseHandlerEx;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.MonthlyPLUReport;
import com.alfredbase.javabean.MonthlySalesReport;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserTimeSheet;
import com.alfredbase.javabean.model.PushMessage;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.SyncMsgSQL;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.Fragment.TableLayoutFragment;
import com.alfredposclient.R;
import com.alfredposclient.activity.BOHSettlementHtml;
import com.alfredposclient.activity.ClockInOROut;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.MonthlyPLUReportHtml;
import com.alfredposclient.activity.MonthlySalesReportHtml;
import com.alfredposclient.activity.NetWorkOrderActivity;
import com.alfredposclient.activity.StoredCardActivity;
import com.alfredposclient.activity.SyncData;
import com.alfredposclient.activity.SystemSetting;
import com.alfredposclient.activity.XZReportHtml;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpAPI {

    // for KDS HTTP Server
    public static final String EOF = "\r\nEOF\r\n";

    /**
     * 订单数据
     */
    public static final int ORDER_DATA = 10;
    /**
     * 订单数据
     */
    public static final int SUBPOS_ORDER_DATA = 12;
    /**
     * 支付数据
     */
    public static final int REPORT_DATA = 20;

    /**
     * 同步订单记录数据
     */
    public static final int LOG_DATA = 11;

    /**
     * 同步开关餐厅开关session的记录数据
     */
    public static final int OPEN_CLOSE_SESSION_RESTAURANT = 90;


    /**
     * 用于更新网络订单状态
     */
    public static final int NETWORK_ORDER_STATUS_UPDATE = 1001;

    public static void login(Context context, Map<String, Object> parameters,
                             String url, AsyncHttpClient httpClient, final Handler handler) {
        try {
            StringEntity entity = HttpAssembling.getLoginParam(
                    (Integer) parameters.get("userID"),
                    (String) parameters.get("password"),
                    (String) parameters.get("bizID"),
                    (Integer) parameters.get("machineType"));
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                SharedPreferencesHelper.putInt(App.instance, SharedPreferencesHelper.TRAINING_MODE, 0);

                                HttpAnalysis.login(statusCode, headers,
                                        responseBody);
                                handler.sendMessage(handler
                                        .obtainMessage(SyncData.HANDLER_LOGIN));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(
                                                SyncData.HANDLER_ERROR_INFO,
                                                resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRestaurantInfo(Context context,
                                         Map<String, Object> parameters, String url,
                                         AsyncHttpClient httpClient, final Handler mHandler, int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getRestaurantParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        HttpAnalysis.getRestaurantInfo(
                                                statusCode, headers,
                                                responseBody);
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.PRINTER);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        if (mHandler != null) {
                                            mHandler.sendMessage(mHandler
                                                    .obtainMessage(SyncData.HANDLER_GET_RESTAURANT_INFO));
                                            mHandler.sendMessage(mHandler
                                                    .obtainMessage(ResultCode.SUCCESS));
                                        }

                                    }
                                }).start();

                            } else {
                                if (mHandler != null)
                                    mHandler.sendMessage(mHandler
                                            .obtainMessage(
                                                    SyncData.HANDLER_ERROR_INFO,
                                                    resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mHandler != null) {
                                mHandler.sendMessage(mHandler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBindDeviceIdInfo(final Context context,
                                           final Map<String, Object> parameters, String url,
                                           AsyncHttpClient httpClient, final Handler handler) {
        try {
            RevenueCenter revenueCenter = (RevenueCenter) parameters
                    .get("revenueCenter");
            StringEntity entity = HttpAssembling
                    .getBindDeviceIdInfo(revenueCenter);
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.HANDLER_GET_BINDDEVICEID_INFO,
                                        parameters));
                            } else if (resultCode == ResultCode.BINDING_TO_ALREADY) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.HANDLER_BIND_ALREADY,
                                        context.getResources().getString(
                                                R.string.binding_to_already)));
                            } else if (resultCode == ResultCode.BE_BOUND_ALREADY) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.HANDLER_BIND_ALREADY,
                                        context.getResources().getString(
                                                R.string.be_bound_already)));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(
                                                SyncData.HANDLER_ERROR_INFO,
                                                resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getUser(Context context, String url,
                               AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.USER);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        HttpAnalysis.getUsers(statusCode,
                                                headers, responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }

                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_PUSH_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            } else if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getUserRestaurant(Context context, String url,
                                         AsyncHttpClient httpClient) {
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        HttpAnalysis.getUsers(statusCode,
                                                headers, responseBody);
                                    }
                                }).start();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getItemCategory(Context context, String url,
                                       AsyncHttpClient httpClient, final Handler handler, final int mode) {
        Log.e("getItemCategory", "-----------start--------progress");
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Log.e("getItemCategory", "------------------progress");
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.ITEM);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        HttpAnalysis.getItemCategory(
                                                statusCode, headers,
                                                responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                            Log.e("getItemCategory", "-----------end--------progress");
                                        }
                                    }
                                }).start();

                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getItem(Context context, String url,
                               AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            RevenueCenter revenueCenter = (RevenueCenter) App.instance
                    .getRevenueCenter();
            StringEntity entity = HttpAssembling.getItemParam(revenueCenter);
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        HttpAnalysis.getItemDetail(statusCode,
                                                headers, responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getModifier(Context context, String url,
                                   AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.MODIFIER);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        HttpAnalysis.getAllModifier(statusCode,
                                                headers, responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTax(Context context, String url,
                              AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.TAX);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        HttpAnalysis.getTax(statusCode,
                                                headers, responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getHappyHour(Context context, String url,
                                    AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        HttpAnalysis.getHappyHour(statusCode,
                                                headers, responseBody);
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.HAPPY_HOURS);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPromotionInfo(Context context, String url,
                                        AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        HttpAnalysis.getPromotionInfo(statusCode,
                                                headers, responseBody);
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.PROMOTION);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getPromotionData(Context context, String url,
                                        AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        HttpAnalysis.getPromotionData(statusCode,
                                                headers, responseBody);
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.HAPPY_HOURS);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getItemPromotionInfos(Context context, String url,
                                             AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            httpClient.post(context, url, HttpAssembling.getParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
//                                        HttpAnalysis.getItemPromotionInfos(statusCode,
//                                                headers, responseBody);

                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.HAPPY_HOURS);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPlaceInfo(Context context,
                                    Map<String, Object> parameters, String url,
                                    AsyncHttpClient httpClient, final Handler handler, final int mode) {
        try {
            RevenueCenter revenueCenter = (RevenueCenter) parameters
                    .get("revenueCenter");
            StringEntity entity = HttpAssembling.getPlaceParam(revenueCenter);
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.PLACE_TABLE);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
//										HttpAnalysis.getPlaceInfo(statusCode,
//												headers, responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));
                                        } else {
                                            handler.sendMessage(handler
                                                    .obtainMessage(SyncData.HANDLER_GET_PLACE_INFO));
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }
                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                } else {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.HANDLER_ERROR_INFO,
                                            resultCode));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            } else
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout(Context context, String url,
                              AsyncHttpClient httpClient, final Handler handler) {
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            // if (resultCode == ResultCode.SUCCESS) {
                            // handler.sendMessage(handler.obtainMessage(MainPage.ACTION_SWITCH_USER));
                            // }
                            handler.sendMessage(handler
                                    .obtainMessage(MainPage.ACTION_SWITCH_USER));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uploadOrderInfo(Context context, final SyncMsg syncMsg,
                                       String url, SyncHttpClient httpClient) {
        try {
            StringEntity entity = HttpAssembling
                    .getUploadOrderInfoParam(syncMsg);
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                syncMsg.setStatus(ParamConst.SYNC_MSG_SUCCESS);
                                SyncMsgSQL.add(syncMsg);
                            } else {
                                syncMsg.setStatus(ParamConst.SYNC_MSG_MALDATA);
                                SyncMsgSQL.add(syncMsg);
                                if (resultCode == ResultCode.DEVICE_NO_PERMIT) {
                                    App.instance
                                            .getTopActivity()
                                            .httpRequestAction(
                                                    ResultCode.DEVICE_NO_PERMIT,
                                                    null);
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                            SyncMsgSQL.add(syncMsg);
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
     * Sync order and XZReport data from POS to Cloud
     */

    //  reportDaySales,reportDayTaxs,
    //	ReportDaySales reportDaySales;
//	List<ReportDayTax> reportDayTaxs;
    public static void sendEmailSync(Context context, final ReportDaySales reportDaySales, List<ReportDayTax> reportDayTaxs, List<ReportDayPayment> reportDayPayments,
                                     String url, SyncHttpClient httpClient) {
        // try {
        StringEntity entity = null;
        try {
            entity = HttpAssembling.getSendemailParam(reportDaySales, reportDayTaxs, reportDayPayments);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (entity != null) {
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            LogUtil.d("HttpAPI--", statusCode + "");
//                            if (resultCode == ResultCode.SUCCESS
//                                    || resultCode == ResultCode.RECEIVE_MSG_EXIST) {
//                                syncMsg.setStatus(ParamConst.SYNC_MSG_SUCCESS);
//                                SyncMsgSQL.add(syncMsg);
//                            } else {
//                                syncMsg.setStatus(ParamConst.SYNC_MSG_MALDATA);
//                                SyncMsgSQL.add(syncMsg);
//                                if (resultCode == ResultCode.DEVICE_NO_PERMIT) {
//                                    App.instance
//                                            .getTopActivity()
//                                            .httpRequestAction(
//                                                    ResultCode.DEVICE_NO_PERMIT,
//                                                    null);
//                                }
//                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {

                            LogUtil.d("HttpAPI--onFailure", statusCode + "");
                            // no need change status here. JOB will get the
                            // exception to rerun job
                            // syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                            // SyncMsgSQL.add(syncMsg);
//                            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
//                            SyncMsgSQL.add(syncMsg);
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            initBugseeModifier();
//                            throw new RuntimeException(error);
                        }
                    });
        }

    }


    public static void mediaSync(Context context, String url, AsyncHttpClient httpClient, final Handler handler, final int mode) {
        // try {
        Log.e("TAG", "-------------------strat");
        StringEntity entity = null;
        try {
            entity = HttpAssembling.getMediaParam();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (entity != null) {
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode, final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            LogUtil.d("mediaSync--", statusCode + "----" + resultCode);

                            if (resultCode == 1) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.PAYMENT_METHOD);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        Log.e("TAG", "-------------------progress");
                                        HttpAnalysis.getOther(statusCode, headers, responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));

                                            Log.e("TAG", "-------------------end");
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }

                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {

                            LogUtil.d("HttpAPI--onFailure", statusCode + "");
                            if (mode == SyncCentre.MODE_PUSH_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            } else if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            initBugseeModifier();
//                            throw new RuntimeException(error);
                        }
                    });
        }

    }

    private static void initBugseeModifier() {
        Restaurant restaurant = RestaurantSQL.getRestaurant();
        if (restaurant != null) {
            BugseeHelper.setEmail(restaurant.getEmail());
            BugseeHelper.setAttribute("restaurant_id", restaurant.getId());
            BugseeHelper.setAttribute("restaurant_company_id", restaurant.getCompanyId());
            BugseeHelper.setAttribute("restaurant_address", restaurant.getAddressPrint());
            BugseeHelper.setAttribute("restaurant_country", restaurant.getCountry());
            BugseeHelper.setAttribute("restaurant_city", restaurant.getCity());
        }

//        String employeeId = Store.getString(context, Store.EMPLOYEE_ID);
//        BugseeHelper.setAttribute("employee_id", employeeId);

//        throw new NullPointerException("Test Crash");
    }

    public static void paymentMethodSync(Context context, String url, AsyncHttpClient httpClient, final Handler handler, final int mode) {
        // try {
        Log.e("TAG", "-------------------strat");
        StringEntity entity = null;
        try {
            entity = HttpAssembling.getPaymentMethodParam();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (entity != null) {
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode, final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == 1) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.PAYMENT_METHOD);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        Log.e("TAG", "-------------------progress");
                                        HttpAnalysis.getPaymentMethod(statusCode, headers, responseBody);

                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));

                                            Log.e("TAG", "-------------------end");
                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }

                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            try {
                                if (mode == SyncCentre.MODE_PUSH_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            ResultCode.CONNECTION_FAILED, error));
                                } else if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                                super.onFailure(statusCode, headers, responseBody,
                                        error);
                                initBugseeModifier();
//                                throw new RuntimeException(error);

                            } catch (Exception e) {
                                Log.e("Server Refuse", String.valueOf(e));
                            }
                        }
                    });
        }

    }

    public static void syncKotItemDetail(Context context, SyncMsg syncMsg,
                                         String url, SyncHttpClient httpClient) {
        StringEntity entity = null;
        try {
            entity = HttpAssembling.getUploadOrderInfoParam(syncMsg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (entity != null) {
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                            } else {
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            initBugseeModifier();
//                            throw new RuntimeException(error);
                        }
                    });
        }
    }

    /*
     * Sync order and XZReport data from POS to Cloud
     */
    public static void cloudSync(Context context, final SyncMsg syncMsg,
                                 String url, SyncHttpClient httpClient) {
        // try {
        StringEntity entity = null;
        try {
            entity = HttpAssembling.getUploadOrderInfoParam(syncMsg);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (entity != null) {
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS
                                    || resultCode == ResultCode.RECEIVE_MSG_EXIST) {
                                syncMsg.setStatus(ParamConst.SYNC_MSG_SUCCESS);
                                SyncMsgSQL.add(syncMsg);
                            } else {
                                syncMsg.setStatus(ParamConst.SYNC_MSG_MALDATA);
                                SyncMsgSQL.add(syncMsg);
                                if (resultCode == ResultCode.DEVICE_NO_PERMIT) {
                                    App.instance
                                            .getTopActivity()
                                            .httpRequestAction(
                                                    ResultCode.DEVICE_NO_PERMIT,
                                                    null);
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            // no need change status here. JOB will get the
                            // exception to rerun job
                            // syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                            // SyncMsgSQL.add(syncMsg);
                            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                            SyncMsgSQL.add(syncMsg);
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            initBugseeModifier();
//                            throw new RuntimeException(error);
                        }
                    });
        }

        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public static void getBOHSettlement(Context context, String url,
                                        AsyncHttpClient httpClient, final Handler handler) {
        try {
            httpClient.post(context, url, HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        handler.sendMessage(handler
                                                .obtainMessage(
                                                        BOHSettlementHtml.BOH_GETBOHHOLDUNPAID_INFO,
                                                        HttpAnalysis
                                                                .getBOHSettlement(
                                                                        statusCode,
                                                                        headers,
                                                                        responseBody)));
                                    }
                                }).start();
                            } else if (resultCode == ResultCode.DEVICE_NO_PERMIT) {
                                App.getTopActivity().httpRequestAction(
                                        ResultCode.DEVICE_NO_PERMIT, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uploadBOHPaidInfo(Context context, String url,
                                         AsyncHttpClient httpClient, Map<String, Object> parameters,
                                         final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.getuploadBOHPaidInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        handler.sendEmptyMessage(BOHSettlementHtml.UPLOAD_BOH_PAID);
                                    }
                                }).start();
                            } else if (resultCode == ResultCode.DEVICE_NO_PERMIT) {
                                App.getTopActivity().httpRequestAction(
                                        resultCode, null);

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCloudXZReport(Context context, String url,
                                         AsyncHttpClient httpClient, Map<String, Object> parameters,
                                         final Handler handler) {
        final Map<String, Object> param = parameters;
        try {
            httpClient.post(context, url,
                    HttpAssembling.getloadReportInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            Map<String, Object> zObj = new HashMap<String, Object>();
                            if (resultCode == ResultCode.SUCCESS) {
                                // write Sales data in DB
                                String nettsale = HttpAnalysis
                                        .getReportDayFromCloud(statusCode,
                                                headers, responseBody);
                                zObj.put("sales", nettsale);
                                zObj.put("bizDate", param.get("bizDate"));
                                // Notifity UI to show data
                                handler.sendMessage(handler
                                        .obtainMessage(
                                                XZReportHtml.LOAD_CLOUD_REPORT_COMPLETE,
                                                zObj));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load and print monthly sales report from cloud
    public static void loadCloudMonthlySalesReport(Context context, String url,
                                                   AsyncHttpClient httpClient, Map<String, Object> parameters,
                                                   final Handler handler) {
        final Map<String, Object> param = parameters;
        try {
            httpClient.post(context, url,
                    HttpAssembling.getloadMonthlySalesReportInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            Map<String, Object> zObj = new HashMap<String, Object>();
                            if (resultCode == ResultCode.SUCCESS) {
                                // write Sales data in DB
                                List<MonthlySalesReport> sales = HttpAnalysis.getReportMonthlySaleFromCloud(statusCode,
                                        headers, responseBody);
                                zObj.put("date", param.get("month"));
                                zObj.put("salesData", sales);
                                // Notifity UI to show data
                                handler.sendMessage(handler
                                        .obtainMessage(
                                                MonthlySalesReportHtml.LOAD_MONTYLY_SALES_REPORT_COMPLETE,
                                                zObj));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load and print monthly PLU report from cloud
    public static void loadCloudMonthlyPLUReport(Context context, String url,
                                                 AsyncHttpClient httpClient, Map<String, Object> parameters,
                                                 final Handler handler) {
        final Map<String, Object> param = parameters;
        try {
            httpClient.post(context, url,
                    HttpAssembling.getloadMonthlyPLUReportInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            Map<String, Object> zObj = new HashMap<String, Object>();
                            if (resultCode == ResultCode.SUCCESS) {
                                // write Sales data in DB
                                List<MonthlyPLUReport> pluData = HttpAnalysis.getReportMonthlyPLUFromCloud(statusCode,
                                        headers, responseBody);
                                zObj.put("date", param.get("month"));
                                zObj.put("plu", pluData);
                                // Notifity UI to show data
                                handler.sendMessage(handler
                                        .obtainMessage(
                                                MonthlyPLUReportHtml.LOAD_MONTYLY_PLU_REPORT_COMPLETE,
                                                zObj));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getOrderFromApp(Context context, String url,
                                       AsyncHttpClient httpClient, Map<String, Object> parameters) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getOrderFromApp(statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePassword(Context context, String url,
                                      AsyncHttpClient httpClient, final Map<String, Object> parameters, final Handler handler, final User user) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                UserSQL.updateUserPassword(String.valueOf(parameters.get("newPassword")), user);
                                CoreData.getInstance().setUsers(UserSQL.getAllUser());
                            }
                            handler.sendMessage(handler.obtainMessage(SystemSetting.UPDATE_PASSWORD_TAG, resultCode));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAppOrderById(Context context, String url,
                                       AsyncHttpClient httpClient, final Map<String, Object> parameters, final Handler handler, final boolean canCheck) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getAppOrderById(statusCode, headers, responseBody, canCheck);
                            }
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(SystemSetting.UPDATE_PASSWORD_TAG, resultCode));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appOrderRefund(Context context, String url,
                                      AsyncHttpClient httpClient, final int appOrderId, final Handler handler) {
        try {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("appOrderId", appOrderId);
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {

                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                AppOrderSQL.updateAppOrderStatusById(appOrderId, ParamConst.APP_ORDER_STATUS_REFUND);
                                App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
                                handler.sendMessage(handler.obtainMessage(NetWorkOrderActivity.CANCEL_APPORDER_SUCCESS, resultCode));
                                return;
                            } else if (resultCode == ResultCode.APP_REFUND_FAILD) {
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                                    String content = jsonObject.getString(resultCode + "");
                                    handler.sendMessage(handler.obtainMessage(ResultCode.APP_REFUND_FAILD, content));
                                    return;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            handler.sendMessage(handler.obtainMessage(
                                    NetWorkOrderActivity.RESULT_FAILED, resultCode));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllAppOrder(Context context, String url,
                                      AsyncHttpClient httpClient, final Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getAllAppOrder(statusCode, headers, responseBody);
                            }
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(NetWorkOrderActivity.REFRESH_APPORDER_SUCCESS, resultCode));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTableStatusForApp(Context context, String url,
                                               AsyncHttpClient httpClient, int tableId, final Handler handler) {
        try {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("tableId", tableId);
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getAllAppOrder(statusCode, headers, responseBody);
                            }
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(NetWorkOrderActivity.REFRESH_APPORDER_SUCCESS, resultCode));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateAppOrderStatus(Context context, String url,
                                            AsyncHttpClient httpClient, final SyncMsg syncMsg) {
        try {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("appOrderId", syncMsg.getAppOrderId());
            parameters.put("orderStatus", syncMsg.getOrderStatus());
            parameters.put("orderNum", syncMsg.getOrderNum());
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                syncMsg.setStatus(ParamConst.SYNC_MSG_SUCCESS);
                                SyncMsgSQL.add(syncMsg);
                            } else {
                                syncMsg.setStatus(ParamConst.SYNC_MSG_MALDATA);
                                SyncMsgSQL.add(syncMsg);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recevingAppOrder(Context context, String url,
                                        AsyncHttpClient httpClient, final int appOrderId, final Handler handler) {
        try {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("appOrderId", appOrderId);
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler.obtainMessage(NetWorkOrderActivity.RECEVING_APP_ORDER_SUCCESS, appOrderId));
                            } else {
                                if (resultCode == ResultCode.APP_ORDER_REFUND) {
                                    AppOrderSQL.updateAppOrderStatusById(appOrderId, ParamConst.APP_ORDER_STATUS_REFUND);
                                }
                                handler.sendMessage(handler.obtainMessage(
                                        NetWorkOrderActivity.RESULT_FAILED, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        NetWorkOrderActivity.HTTP_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void readyAppOrder(Context context, String url,
                                     AsyncHttpClient httpClient, final int appOrderId, final Handler handler) {
        try {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("appOrderId", appOrderId);
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler.obtainMessage(NetWorkOrderActivity.READY_APP_ORDER_SUCCESS, appOrderId));
                            } else {
                                if (resultCode == ResultCode.APP_ORDER_REFUND) {
                                    AppOrderSQL.updateAppOrderStatusById(appOrderId, ParamConst.APP_ORDER_STATUS_REFUND);
                                }
                                handler.sendMessage(handler.obtainMessage(
                                        NetWorkOrderActivity.RESULT_FAILED, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        NetWorkOrderActivity.HTTP_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePlaceTable(Context context, String url, AsyncHttpClient httpClient,
                                        Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(TableLayoutFragment.UPDATE_PLACE_TABLE_SUCCEED);
                            } else {
                                handler.sendMessage(handler.obtainMessage(TableLayoutFragment.UPDATE_PLACE_TABLE_FAILURE, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateReaminingStockByItemId(Context context, String url, AsyncHttpClient httpClient,
                                                    Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);

                            if (resultCode == ResultCode.SUCCESS) {

                            } else {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {

                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateReaminingStock(Context context, String url, AsyncHttpClient httpClient,
                                            Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);

                            if (resultCode == ResultCode.SUCCESS) {

                            } else {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {

                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetItemDetailStockNum(Context context, String url, AsyncHttpClient httpClient) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);

                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        HttpAnalysis.resetItemDetailStockNum(responseBody);
                                    }
                                }).start();
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
//
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getRemainingStock(Context context, String url, AsyncHttpClient httpClient,
                                         final Handler handler, final int mode) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.getTokenParam(),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);

                            if (resultCode == 1) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.STOCK);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        Log.e("TAG", "-------------------progress");
                                        HttpAnalysis.getRemainingStock(responseBody);
                                        if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                            handler.sendMessage(handler
                                                    .obtainMessage(
                                                            SyncData.SYNC_DATA_TAG,
                                                            SyncData.SYNC_SUCCEED));

                                        } else {
                                            handler.sendEmptyMessage(ResultCode.SUCCESS);
                                        }

                                    }
                                }).start();
                            } else {
                                if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                    handler.sendMessage(handler.obtainMessage(
                                            SyncData.SYNC_DATA_TAG,
                                            SyncData.SYNC_FAILURE));
                                }
                            }

//                            if (resultCode == ResultCode.SUCCESS) {
//
//                                        HttpAnalysis.getRemainingStock(responseBody);
//                                    }
//
//                             else {
//
//                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {

                            if (mode == SyncCentre.MODE_PUSH_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            } else if (mode == SyncCentre.MODE_FIRST_SYNC) {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            }
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPlaceTable(Context context, String url, AsyncHttpClient httpClient,
                                     Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);

                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        HttpAnalysis.getPlaceTable(responseBody);
                                        Map<String, Integer> map = App.instance
                                                .getPushMsgMap();
                                        if (!map.isEmpty()) {
                                            map.remove(PushMessage.PLACE_TABLE);
                                            Store.saveObject(App.instance,
                                                    Store.PUSH_MESSAGE, map);
                                            App.instance.setPushMsgMap(map);
                                        }
                                        handler.sendMessage(handler
                                                .obtainMessage(
                                                        SyncData.SYNC_DATA_TAG,
                                                        SyncData.SYNC_SUCCEED));
                                    }
                                }).start();
                            } else {
                                handler.sendMessage(handler.obtainMessage(
                                        SyncData.SYNC_DATA_TAG,
                                        SyncData.SYNC_FAILURE));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    SyncData.SYNC_DATA_TAG,
                                    SyncData.SYNC_FAILURE));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registStoredCard(Context context, String url, AsyncHttpClient httpClient,
                                        Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(StoredCardActivity.REGIST_STOREDCARD_SUCCEED);
                            } else {
                                handler.sendMessage(handler.obtainMessage(StoredCardActivity.HTTP_FAILURE, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStoredCardValue(Context context, String url, AsyncHttpClient httpClient,
                                             final Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                int payTypeId = -1;
                                if (parameters.containsKey("payTypeId")) {
                                    payTypeId = (Integer) parameters.get("payTypeId");
                                }
                                HttpAnalysis.updateStoredCardValue(responseBody, payTypeId);
                                handler.sendEmptyMessage(StoredCardActivity.PAID_STOREDCARD_SUCCEED);
                            } else {
                                handler.sendMessage(handler.obtainMessage(StoredCardActivity.HTTP_FAILURE, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeStoredCardValue(Context context, String url, AsyncHttpClient httpClient,
                                            Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(StoredCardActivity.REPLACEMENT_STOREDCARD_SUCCEED);
                            } else {
                                handler.sendMessage(handler.obtainMessage(StoredCardActivity.HTTP_FAILURE, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeStoredCardValue(Context context, String url, AsyncHttpClient httpClient,
                                             Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(StoredCardActivity.CHANGE_STOREDCARD_SUCCEED);
                            } else {
                                handler.sendMessage(handler.obtainMessage(StoredCardActivity.HTTP_FAILURE, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryStoredCardBalance(Context context, String url, AsyncHttpClient httpClient,
                                              Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url,
                    HttpAssembling.encapsulateBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                String balance = BH.getBD(ParamConst.DOUBLE_ZERO).toString();
                                try {
                                    JSONObject object = new JSONObject(new String(responseBody));
                                    balance = BH.getBD(object.getDouble("balance")).toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                handler.sendMessage(handler.obtainMessage(StoredCardActivity.QUERYBALANCE_STOREDCARD_SUCCEED, balance));
                            } else {
                                handler.sendMessage(handler.obtainMessage(StoredCardActivity.HTTP_FAILURE, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAppVersion(final Context context, String url, AsyncHttpClient httpClient,
                                     Map<String, Object> parameters, final int applicationTypes) {
        try {
            parameters.put("applicationTypes", applicationTypes);
            httpClient.post(context, url,
                    HttpAssembling.getAppVersion(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            try {
                                if (resultCode == ResultCode.SUCCESS) {
                                    Gson gson = new Gson();
                                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                                    VersionUpdate versionUpdate = gson.fromJson(jsonObject.toString(), VersionUpdate.class);
                                    switch (applicationTypes) {
                                        case 0:
                                            Store.saveObject(context, Store.POS_SYSTEM_UPDATE_INFO, versionUpdate);
                                            break;
                                        case 5:
                                            Store.saveObject(context, Store.WAITER_SYSTEM_UPDATE_INFO, versionUpdate);
                                            break;
                                        case 6:
                                            Store.saveObject(context, Store.KDS_SYSTEM_UPDATE_INFO, versionUpdate);
                                            break;
                                    }
                                    if (applicationTypes == 0) {

                                    }

                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getClockInUserTimeSheet(final Context context, String url,
                                               AsyncHttpClient httpClient, final Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url, HttpAssembling.getPlaceParam(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                List<UserTimeSheet> userTimeSheetList = HttpAnalysis.getClockInUserTimeSheet(responseBody);
                                handler.sendMessage(handler.obtainMessage(ClockInOROut.GET_LIST_SUCCESS, userTimeSheetList));
                            } else {
                                handler.sendMessage(handler.obtainMessage(ClockInOROut.HTTP_FAIL, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody, error);
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clockInOut(final Context context, String url,
                                  AsyncHttpClient httpClient, final Map<String, Object> parameters, final Handler handler) {
        try {
            httpClient.post(context, url, HttpAssembling.getPlaceParam(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(ClockInOROut.CLOCK_SUCCESS);
                            } else if (resultCode == ResultCode.USER_LOGIN_EXIST) {
                                handler.sendMessage(handler.obtainMessage(resultCode, parameters.get("type")));
                            } else {
                                handler.sendMessage(handler.obtainMessage(ClockInOROut.HTTP_FAIL, resultCode));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody, error);
                            if (handler != null)
                                handler.sendMessage(handler.obtainMessage(
                                        ResultCode.CONNECTION_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void callAppNo(final Context context, String url,
                                 AsyncHttpClient httpClient, String tag, String num) {
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("callNumber", num);
            requestParams.put("callType", App.instance.getSystemSettings().getCallStyle());
            if (Store.getBoolean(App.instance, Store.CALL_NUM_UPDATE, false)) {
                requestParams.put("header", Store.getString(App.instance, Store.CALL_NUM_HEADER));
                requestParams.put("footer", Store.getString(App.instance, Store.CALL_NUM_FOOTER));
            }
            if (TextUtils.isEmpty(tag)) {
                //     byte t = (byte) tag.charAt(0);
                requestParams.put("callTag", 0);
            } else {
                byte t = (byte) tag.charAt(0);
                requestParams.put("callTag", t % 64);
            }
            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.print("192.168.20.102========onSuccess");
                            Store.putBoolean(App.instance, Store.CALL_NUM_UPDATE, false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            System.out.print("192.168.20.102========onFailure:" + error.getMessage());
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callAppNo(final Context context, String url,
                                 AsyncHttpClient httpClient, String params) {
        try {

            JSONObject jsonObject = new JSONObject(params);
            String num = jsonObject.getString("callNumber");
            String tag = jsonObject.getString("numTag");
            String printerName = jsonObject.getString("printerName");
            int printerGroupId = jsonObject.getInt("printerGroupId");

            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("callNumber", num);
            requestParams.put("printerGroupId", printerGroupId);
            requestParams.put("printerName", printerName);
            requestParams.put("callType", App.instance.getSystemSettings().getCallStyle());

            if (Store.getBoolean(App.instance, Store.CALL_NUM_UPDATE, false)) {
                requestParams.put("header", Store.getString(App.instance, Store.CALL_NUM_HEADER));
                requestParams.put("footer", Store.getString(App.instance, Store.CALL_NUM_FOOTER));
            }

            if (TextUtils.isEmpty(tag)) {
                requestParams.put("callTag", 0);
            } else {
                byte t = (byte) tag.charAt(0);
                requestParams.put("callTag", t % 64);
            }

            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Store.putBoolean(App.instance, Store.CALL_NUM_UPDATE, false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void posCloseSession(final Context context, String url,
                                       AsyncHttpClient httpClient) {
        try {
            Map<String, Object> requestParams = new HashMap<>();
            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadApk(String url,
                                   AsyncHttpClient httpClient) {
        httpClient.get(url, new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                LogUtil.e("下载进度", "" + (int) ((bytesWritten * 1.0 / totalSize) * 100));
            }
        });
    }

//    public static void mediaSync(Context context, String absoluteUrl, SyncHttpClient syncHttpClient, Handler handler) {
//    }

    public static void setServerLanguage(Context context,
                                         final Map<String, Object> parameters, final String url,
                                         AsyncHttpClient httpClient, final Handler handler) {

        parameters.put("appVersion", App.instance.VERSION);
        final String language = (String) parameters.get("language");

        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                String body = new String(responseBody);
                                Message message = new Message();
                                message.obj = language;
                                message.arg2 = -1;
                                message.what = App.HANDLER_REFRESH_LANGUAGE;
                                handler.sendMessage(message);
                                handler.sendMessage(handler.obtainMessage(ResultCode.SUCCESS, null));
                            } else {
                                String body = new String(responseBody);
                                handler.sendMessage(handler.obtainMessage(ResultCode.UNKNOW_ERROR, body));
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            String body = new String(responseBody);
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setClientLanguage(final Context context, String url,
                                         AsyncHttpClient httpClient, String version, String language) {

        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("version", version);
            requestParams.put("language", language);
            requestParams.put("callType", App.instance.getSystemSettings().getCallStyle());

            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(requestParams), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            String body = new String(responseBody);
                            if (resultCode == ResultCode.SUCCESS) {

                            } else {

                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            String body = new String(responseBody);
                            super.onFailure(statusCode, headers, responseBody, error);

                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loginQRPayment(final Context context, String url, Integer empId, String password, String restaurantKey,
                                      AsyncHttpClient httpClient, final Handler handler) {
        try {

            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("empId", empId);
            requestParams.put("password", password);
            requestParams.put("restaurantKey", restaurantKey);

            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE_JSON,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.saveLoginQRPayment(responseBody);
                                handler.sendMessage(handler
                                        .obtainMessage(SyncData.HANDLER_LOGIN_QRPAYMENT, new String(responseBody)));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(
                                                SyncData.HANDLER_LOGIN_QRPAYMENT,
                                                null));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(
                                    SyncData.HANDLER_LOGIN_QRPAYMENT,
                                    null));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //start pay88
    public static void qrcodePay88(final Context context,
                                   String url,
                                   String restaurantKey,
                                   String userKey,
                                   Integer paymentTypeId, //alipay, grabpay
                                   BigDecimal amount,
                                   String currency,
                                   String productDesc,
                                   long billNo,
                                   String custContact,
                                   String custEmail,
                                   String custName,
                                   AsyncHttpClient httpClient, final Handler handler) {
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("restaurantKey", restaurantKey);
            requestParams.put("userKey", userKey);
            requestParams.put("amount", amount);

            requestParams.put("paymentId", paymentTypeId);
            requestParams.put("currency", currency);
            requestParams.put("productDesc", productDesc);
            requestParams.put("billNo", billNo);
            requestParams.put("userContact", custContact);
            requestParams.put("userEmail", custEmail);
            requestParams.put("username", custName);

            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            Log.wtf("Checkerentity1", String.valueOf(requestParams));
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE_JSON,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            handler.sendMessage(handler.obtainMessage(SyncData.HANDLER_QRCODE_PAY88, new String(responseBody)));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(SyncData.HANDLER_QRCODE_PAY88, null));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkStatusPay88(final Context context,
                                        String url,
                                        String restaurantKey,
                                        String userKey,
                                        long billNo,
                                        BigDecimal amount,
                                        AsyncHttpClient httpClient, final Handler handler) {
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("restaurantKey", restaurantKey);
            requestParams.put("userKey", userKey);
            requestParams.put("amount", amount);

            requestParams.put("billNo", billNo);

            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE_JSON,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler
                                        .obtainMessage(SyncData.HANDLER_CHECK_STATUS_PAY88, new String(responseBody)));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(SyncData.HANDLER_CHECK_STATUS_PAY88, null));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler
                                    .obtainMessage(SyncData.HANDLER_CHECK_STATUS_PAY88, null));
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //end pay88

    //start payhalal
    public static void qrcodePayhalal(final Context context,
                                      String url,
                                      String restaurantKey,
                                      String userKey,
                                      Integer paymentTypeId,
                                      BigDecimal amount,
                                      String currency,
                                      String productDesc,
                                      long billNo,
                                      String custContact,
                                      String custEmail,
                                      String custName,
                                      AsyncHttpClient httpClient, final Handler handler) {

        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("restaurantKey", restaurantKey);
            requestParams.put("userKey", userKey);
            requestParams.put("amount", amount);
            requestParams.put("paymentId", paymentTypeId);
            requestParams.put("currency", currency);
            requestParams.put("productDesc", productDesc);
            requestParams.put("billNo", billNo);
            requestParams.put("userContact", custContact);
            requestParams.put("userEmail", custEmail);
            requestParams.put("username", custName);

            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE_JSON,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            handler.sendMessage(handler.obtainMessage(SyncData.HANDLER_QRCODE_PAYHALAL, new String(responseBody)));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(SyncData.HANDLER_QRCODE_PAYHALAL, null));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void checkStatusPayhalal(final Context context,
                                           String url,
                                           String restaurantKey,
                                           String userKey,
                                           BigDecimal amount,
                                           String currency,
                                           long billNo,
                                           String transactionId,
                                           AsyncHttpClient httpClient, final Handler handler) {
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("restaurantKey", restaurantKey);
            requestParams.put("userKey", userKey);
            requestParams.put("amount", amount);
            requestParams.put("currency", currency);
            requestParams.put("billNo", billNo);
            requestParams.put("transactionId", transactionId);

            StringEntity entity = new StringEntity(new Gson().toJson(requestParams), "UTF-8");
            httpClient.post(context, url, entity, HttpAssembling.CONTENT_TYPE_JSON,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler
                                        .obtainMessage(SyncData.HANDLER_CHECK_STATUS_PAYHALAL, new String(responseBody)));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(SyncData.HANDLER_CHECK_STATUS_PAYHALAL, null));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler
                                    .obtainMessage(SyncData.HANDLER_CHECK_STATUS_PAYHALAL, null));
                            handler.sendMessage(handler.obtainMessage(
                                    ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody,
                                    error);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //end payhalal

    //start connection multi rvc


    public static void getOtherRVCPlaceTable(Context context,
                                             final String url,
                                             AsyncHttpClient httpClient, final Handler handler) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("appVersion", App.instance.VERSION);

        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                String body = new String(responseBody);
                                Message message = new Message();
                                message.obj = body;
                                message.arg2 = -1;
                                message.what = App.HANDLER_GET_OTHER_RVC;
                                handler.sendMessage(message);
                                handler.sendMessage(handler.obtainMessage(ResultCode.SUCCESS, null));
                            } else {
                                Message message = new Message();
                                message.obj = null;
                                message.arg2 = -1;
                                message.what = App.HANDLER_GET_OTHER_RVC;
                                handler.sendMessage(message);
                                handler.sendMessage(handler.obtainMessage(ResultCode.UNKNOW_ERROR, null));
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            Message message = new Message();
                            message.obj = null;
                            message.arg2 = -1;
                            message.what = App.HANDLER_GET_OTHER_RVC;
                            handler.sendMessage(message);
                            handler.sendMessage(handler.obtainMessage(ResultCode.UNKNOW_ERROR, null));
                            super.onFailure(statusCode, headers, responseBody, error);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getOtherRVCTable(Context context,
                                        final String url,
                                        int placeId,
                                        AsyncHttpClient httpClient, final Handler handler) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("appVersion", App.instance.VERSION);
        parameters.put("placeId", placeId);


        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);

                            Message message = new Message();
                            try {
                                JSONObject object = new JSONObject(new String(responseBody));
                                if (resultCode == ResultCode.SUCCESS) {
                                    message.obj = object.get("data");
                                } else {
                                    message.obj = null;
                                }
                            } catch (JSONException e) {
                                message.obj = null;
                                e.printStackTrace();
                            }
                            message.arg2 = -1;
                            message.what = App.HANDLER_GET_OTHER_TABLE;

                            handler.sendMessage(message);
                            handler.sendMessage(handler.obtainMessage(ResultCode.SUCCESS, null));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            Message message = new Message();
                            message.obj = null;
                            message.arg2 = -1;
                            message.what = App.HANDLER_GET_OTHER_TABLE;
                            handler.sendMessage(message);
                            handler.sendMessage(handler.obtainMessage(ResultCode.UNKNOW_ERROR, null));
                            super.onFailure(statusCode, headers, responseBody, error);

                        }


                        @Override
                        public boolean getUseSynchronousMode() {
                            return false;
                        }

                    });
        } catch (Exception e) {
            Message message = new Message();
            message.obj = null;
            message.arg2 = -1;
            message.what = App.HANDLER_GET_OTHER_TABLE;
            handler.sendMessage(message);
            handler.sendMessage(handler.obtainMessage(ResultCode.UNKNOW_ERROR, null));
            e.printStackTrace();
        }
    }

    public static void sendOrderToOtherRVC(Context context,
                                           final String url, int transferType, Order currentOrder, int tableId,
                                           AsyncHttpClient httpClient, final Handler handler) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("appVersion", App.instance.VERSION);
        parameters.put("order", new Gson().toJson(currentOrder));
        parameters.put("transferType", transferType);

        List<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
        parameters.put("orderDetail", new Gson().toJson(orderDetails));


        KotSummary kot = KotSummarySQL.getKotSummary(currentOrder.getId(), currentOrder.getNumTag());
        parameters.put("kotSummary", new Gson().toJson(kot));

        ArrayList<KotItemDetail> kotItemDetail = KotItemDetailSQL.getKotItemDetailByOrderId(currentOrder.getId());
        parameters.put("kotItemDetail", new Gson().toJson(kotItemDetail));

        ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<>();
        for (KotItemDetail itemDetail : kotItemDetail) {
            ArrayList<KotItemModifier> kotItemModifier = KotItemModifierSQL.getKotItemModifiersByKotItemDetail(itemDetail.getId());
            kotItemModifiers.addAll(kotItemModifier);

        }
        parameters.put("kotItemModifier", new Gson().toJson(kotItemModifiers));

        List<OrderBill> orderbill = OrderBillSQL.getAllOrderBillByOrder(currentOrder);
        parameters.put("orderBill", new Gson().toJson(orderbill));

        ArrayList<OrderModifier> orderModifier = OrderModifierSQL.getAllOrderModifier(currentOrder);
        parameters.put("orderModifier", new Gson().toJson(orderModifier));

        List<OrderSplit> orderSplit = OrderSplitSQL.getAllOrderSplits(currentOrder);
        parameters.put("orderSplit", new Gson().toJson(orderSplit));

        parameters.put("tableId", tableId); //selected tableId
        parameters.put("tableName", currentOrder.getTableName()); //selected tableId

        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
//                            Log.wtf("Test_sendOrderToOtherRVC", "result_success_" + resultCode);
                            if (resultCode == ResultCode.SUCCESS) {
                                String body = new String(responseBody);
                                Message message = new Message();
                                message.obj = body;
                                message.arg2 = -1;
                                message.what = App.HANDLER_TRANSFER_TABLE_TO_OTHER_RVC;
                                handler.sendMessage(message);
//                                handler.sendMessage(handler.obtainMessage(ResultCode.SUCCESS, null));
                            } else {
                                String body = new String(responseBody);
                                handler.sendMessage(handler.obtainMessage(ResultCode.UNKNOW_ERROR, body));
                            }

                        }


                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
//                            Log.wtf("Test_sendOrderToOtherRVC", "result_failed_" + new String(responseBody));
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);

                        }

                        @Override
                        public boolean getUseSynchronousMode() {
                            return false;
                        }
                    });
        } catch (Exception e) {
//            Log.wtf("Test_sendOrderExcep",""+e.getMessage());
            e.printStackTrace();
        }
    }


    public static void transferItemToOtherRVC(Context context, String url, Order oldOrder, OrderDetail transfItemOrderDetail, int tableId, int pack, AsyncHttpClient httpClient, final Handler handler) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("appVersion", App.instance.VERSION);
        parameters.put("order", new Gson().toJson(oldOrder));
        parameters.put("orderDetail", new Gson().toJson(transfItemOrderDetail));
        parameters.put("tableId", tableId); //selected tableId
        parameters.put("pack", pack); //selected tableId

        ArrayList<KotItemModifier> kotModifiers = new ArrayList<>();
        ArrayList<OrderModifier> orderModifiers = OrderModifierSQL.getOrderModifiersByOrderDetailId(transfItemOrderDetail.getId());
        parameters.put("orderModifier", new Gson().toJson(orderModifiers));

        OrderBill orderbill = OrderBillSQL.getOrderBillByOnlyOrder(oldOrder.getId());
        parameters.put("orderBill", new Gson().toJson(orderbill));

        KotSummary kot = KotSummarySQL.getKotSummary(oldOrder.getId(), oldOrder.getNumTag());

        if (kot != null) {
            parameters.put("kotSummary", new Gson().toJson(kot));

            KotItemDetail kotItemDetail = KotItemDetailSQL.getKotItemDetailByOrderDetailId(kot.getId(), transfItemOrderDetail.getId());
            if (kotItemDetail != null)
                parameters.put("kotItemDetail", new Gson().toJson(kotItemDetail));

            if (kotItemDetail != null) {
                kotModifiers = KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetail.getId());
            }
        }

        parameters.put("kotModifier", new Gson().toJson(kotModifiers));


        Log.wtf("Test_transfer_item_params", "" + new Gson().toJson(parameters));
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            Log.wtf("Test_transfer_item_to_other", "result_success_" + resultCode);
                            if (resultCode == ResultCode.SUCCESS) {
                                String body = new String(responseBody);
                                Message message = new Message();
                                message.obj = body;
                                message.arg2 = -1;
                                message.what = App.HANDLER_TRANSFER_ITEM_TO_OTHER_RVC;
                                handler.sendMessage(message);
//                                handler.sendMessage(handler.obtainMessage(ResultCode.SUCCESS, null));
                            } else {
                                String body = new String(responseBody);
                                handler.sendMessage(handler.obtainMessage(ResultCode.UNKNOW_ERROR, body));
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            Log.wtf("Test_transfer_item_to_other", "result_failed_" + new String(responseBody));
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);

                        }

                        @Override
                        public boolean getUseSynchronousMode() {
                            return false;
                        }
                    });
        } catch (Exception e) {
            Log.wtf("Test_sendOrderExcep", "" + e.getMessage());

            e.printStackTrace();
        }
    }


    //end connection multi rvc
}