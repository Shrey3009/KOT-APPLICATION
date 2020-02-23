package com.alfredposclient.http.subpos;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.alfredbase.ParamConst;
import com.alfredbase.http.AsyncHttpResponseHandlerEx;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.SubPosBeanSQL;
import com.alfredbase.store.sql.SyncMsgSQL;
import com.alfredbase.utils.CallBack;
import com.alfredbase.utils.DialogFactory;
import com.alfredposclient.activity.kioskactivity.subpos.SubPosLogin;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubPosHttpAPI {

    // for KDS HTTP Server
    public static final String EOF = "\r\nEOF\r\n";
    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    /**
     * 订单数据
     */
    public static final int ORDER_DATA = 10;
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



    public static void login(final Context context, Map<String, Object> parameters,
                             String url, AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters) + EOF,
                            "UTF-8"), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            String body = new String(responseBody);
                            if (resultCode == ResultCode.SUCCESS
                                    || resultCode == ResultCode.SESSION_HAS_CHANGE) {
                                SubPosHttpAnalysis.login(resultCode, body, handler);
                            } else {
                                elseResultCodeAction(resultCode, body);
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            // TODO Auto-generated method stub
                            errorAction(error);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void chooseRevenue(final Context context,
                             String url, AsyncHttpClient httpClient, final Handler handler) {
        Map<String, Object> parameters = new HashMap<>();
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters) + EOF,
                            "UTF-8"), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            String body = new String(responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(ResultCode.SUCCESS);
                            } else {
                                elseResultCodeAction(resultCode, body);
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            errorAction(error);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateAllData(final Context context,
                                     String url, AsyncHttpClient httpClient, final Handler handler) {
        Map<String, Object> parameters = new HashMap<>();
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters) + EOF,
                            "UTF-8"), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            String body = new String(responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                SubPosHttpAnalysis.updateAllData(body, handler);

                            } else {
                                elseResultCodeAction(resultCode, body);
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            errorAction(error);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void getOrder(final Context context, Map<String, Object> parameters,
                             String url, AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
            parameters.put("userId", App.instance.getUser().getId());
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters) + EOF,
                            "UTF-8"), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            String body = new String(responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                SubPosHttpAnalysis.getOrder(body);
                                handler.sendEmptyMessage(SubPosLogin.GET_ORDER_SUCCESS);
                            } else {
                                elseResultCodeAction(resultCode, body);
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            errorAction(error);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void uploadOrder(final Context context, Map<String, Object> parameters,
                             String url, AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
            parameters.put("userId", App.instance.getUser().getId());
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters) + EOF,
                            "UTF-8"), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            String body = new String(responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                SubPosHttpAnalysis.uploadOrder(body);
                                handler.sendEmptyMessage(SubPosLogin.GET_ORDER_SUCCESS);
                            } else {
                                elseResultCodeAction(resultCode, body);
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            errorAction(error);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void cloudSyncUploadOrderInfo(final Context context, final SyncMsg syncMsg,
                                                String url, AsyncHttpClient httpClient) {

        try {
            JSONObject jsonObject = new JSONObject(syncMsg.getData());
            jsonObject.put("appVersion", App.instance.VERSION);
            jsonObject.put("userId", App.instance.getUser().getId());
            httpClient.post(context, url,
                    new StringEntity(jsonObject + EOF,
                            "UTF-8"), CONTENT_TYPE,
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
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                            SyncMsgSQL.add(syncMsg);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void cloudSyncUploadOrderInfoLog(final Context context, final SyncMsg syncMsg,
                                                String url, AsyncHttpClient httpClient) {

        try {
            JSONObject jsonObject = new JSONObject(syncMsg.getData());
            jsonObject.put("appVersion", App.instance.VERSION);
            jsonObject.put("userId", App.instance.getUser().getId());
            httpClient.post(context, url,
                    new StringEntity(jsonObject + EOF,
                            "UTF-8"), CONTENT_TYPE,
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
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                            SyncMsgSQL.add(syncMsg);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void cloudSyncUploadReportInfo(final Context context, final SyncMsg syncMsg, final SubPosBean subPosBean,
                                                 String url, AsyncHttpClient httpClient, final CallBack callBack) {

        try {
            JSONObject jsonObject = new JSONObject(syncMsg.getData());
            jsonObject.put("appVersion", App.instance.VERSION);
            jsonObject.put("userId", App.instance.getUser().getId());
            jsonObject.put("subPosBeanId", subPosBean.getId());
            httpClient.post(context, url,
                    new StringEntity(jsonObject + EOF,
                            "UTF-8"), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS
                                    || resultCode == ResultCode.RECEIVE_MSG_EXIST) {
                                syncMsg.setStatus(ParamConst.SYNC_MSG_SUCCESS);
                                SyncMsgSQL.add(syncMsg);
                                subPosBean.setSubPosStatus(ParamConst.SUB_POS_STATUS_CLOSE);
                                SubPosBeanSQL.updateSubPosBean(subPosBean);
                                App.instance.setSubPosBean(subPosBean);
                                Store.remove(App.instance, Store.SESSION_STATUS);
                                App.instance.setSessionStatus(null);
                                callBack.onSuccess();
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                            SyncMsgSQL.add(syncMsg);
                            callBack.onError();
                            errorAction(error);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closeSession(final Context context,final SubPosBean subPosBean,
                                                 String url, AsyncHttpClient httpClient, final CallBack callBack) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appVersion", App.instance.VERSION);
            jsonObject.put("userId", App.instance.getUser().getId());
            jsonObject.put("subPosBeanId", subPosBean.getId());
            httpClient.post(context, url,
                    new StringEntity(jsonObject + EOF,
                            "UTF-8"), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS
                                    || resultCode == ResultCode.RECEIVE_MSG_EXIST) {
                                subPosBean.setSubPosStatus(ParamConst.SUB_POS_STATUS_CLOSE);
                                SubPosBeanSQL.updateSubPosBean(subPosBean);
                                App.instance.setSubPosBean(subPosBean);
                                Store.remove(App.instance, Store.SESSION_STATUS);
                                App.instance.setSessionStatus(null);
                                callBack.onSuccess();
                            }
                        }
                        @Override
                        public void onFailure(final int statusCode, final Header[] headers,
                                              final byte[] responseBody, final Throwable error) {
                            callBack.onError();
                            errorAction(error);
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 返回码不需要特殊处理的提醒
    private static  void elseResultCodeAction(final int resultCode, final String responseBody){
        App.getTopActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String information = null;
                try {
                    JSONObject object = new JSONObject(responseBody);
                    information = object.optString("posVersion");
                    if(object.has("versionUpdate")){
                        final VersionUpdate versionUpdate = new Gson().fromJson(object.getString("versionUpdate"), VersionUpdate.class);
                        if (versionUpdate != null && App.instance.getAppVersionCode() < versionUpdate.getVersionCode()) {
                            if (versionUpdate != null && App.instance.getAppVersionCode() < versionUpdate.getVersionCode()) {
                                DialogFactory.showUpdateVersionDialog(App.getTopActivity(), versionUpdate, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        long posUpdateId = DownloadFactory.downloadApk(App.getTopActivity(), (DownloadManager) App.getTopActivity().getSystemService(Context.DOWNLOAD_SERVICE), versionUpdate.getKdsDownload(), Store.getLong(App.getTopActivity(), "posUpdateId"));
                                        Store.putLong(App.getTopActivity(), "posUpdateId", posUpdateId);
                                    }
                                }, null);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                App.getTopActivity().dismissLoadingDialog();
                UIHelp.showToast(App.getTopActivity(), ResultCode.getErrorResultStrByCode(App.instance, resultCode, information));
            }
        });
    }
    // 返回错误不需要特殊处理的提醒
    private static  void errorAction(final Throwable error){
        App.getTopActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        App.getTopActivity().dismissLoadingDialog();
                        UIHelp.showToast(App.getTopActivity(), ResultCode.getErrorResultStr(App.instance, error,
                                "Revenue Center"));
                    }
                }
        );
    }
}
