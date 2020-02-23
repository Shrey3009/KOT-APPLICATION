package com.alfredkds.http;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.alfredbase.ParamConst;
import com.alfredbase.http.AsyncHttpResponseHandlerEx;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.utils.DialogFactory;
import com.alfredkds.activity.KotHistory;
import com.alfredkds.activity.Login;
import com.alfredkds.activity.SelectKitchen;
import com.alfredkds.activity.Setting;
import com.alfredkds.global.App;
import com.alfredkds.global.UIHelp;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

/* KDS Requests main POS */
public class HttpAPI {
    /**
     * 用于pos server判断数据结束
     */
    private static final String EOF = "\r\nEOF\r\n";
    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    public static void login(final Context context, Map<String, Object> parameters,
                             String url, AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.login(statusCode, headers,
                                        responseBody, context);
                                handler.sendMessage(handler
                                        .obtainMessage(Login.HANDLER_LOGIN));
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            // TODO Auto-generated method stub
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncSubmitKot(Context context, Map<String, Object> parameters, String url,
                                     AsyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.SUCCESS) {
                        } else if (resultCode == ResultCode.INVALID_DEVICE) {
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        if (error.getCause() instanceof ConnectException) {
                            throw new RuntimeException(error);
                        }
                    }
                });
    }

    public static void getPrinters(final Context context,
                                   Map<String, Object> parameters, String url,
                                   AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler.obtainMessage(ResultCode.SUCCESS,
                                        HttpAnalysis.getPrinterList(statusCode, headers, responseBody)));
                            } else if (resultCode == ResultCode.USER_NO_PERMIT) {
                                handler.sendEmptyMessage(ResultCode.USER_NO_PERMIT);
                            } else if (resultCode == ResultCode.USER_POS_TYPE) {

                                diaLogTrain();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getConnectedKDS(final Context context,
                                       Map<String, Object> parameters, String url,
                                       AsyncHttpClient httpClient, final Handler handler) {
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
                            if (resultCode == ResultCode.SUCCESS) {
//                                handler.sendEmptyMessage(ResultCode.SUCCESS);
                                HttpAnalysis.saveConnectedKDS(statusCode, headers, responseBody);
                            } else if (resultCode == ResultCode.USER_NO_PERMIT) {
                                handler.sendEmptyMessage(ResultCode.USER_NO_PERMIT);
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateKdsStatus(final Context context,
                                       Map<String, Object> parameters, String url,
                                       AsyncHttpClient httpClient, final Handler handler) {
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
                            if (resultCode == ResultCode.SUCCESS) {
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Send KDS device details to POS when pairing completes */
    public static void pairingComplete(final Context context,
                                       Map<String, Object> parameters, String url, AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getMainPosInfo(statusCode, headers, responseBody, handler);
                            } else {
                                handler.sendMessage(handler.obtainMessage(SelectKitchen.HANDLER_ERROR, resultCode));
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Report KDS Dynamic IP change*/
    public static void kdsIpChange(final Context context,
                                   Map<String, Object> parameters, String url,
                                   AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
//            parameters.put("userKey", CoreData.getInstance().getUserKey());
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                            } else if (resultCode == ResultCode.USER_POS_TYPE) {

                                diaLogTrain();
                            } else {
//								elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
//							UIHelp.showToast((BaseApplication)context, context.getResources().getString(R.string.network_error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateRemainingStock(final Context context,
                                            final Map<String, Object> parameters, String url,
                                            AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
//            parameters.put("userKey", CoreData.getInstance().getUserKey());
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {

                            } else if (resultCode == ResultCode.USER_POS_TYPE) {

                                diaLogTrain();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();

                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void kotNextKds(final Context context,
                                  final Map<String, Object> parameters, String url,
                                  AsyncHttpClient httpClient, final Handler handler, final int id) {
        if (parameters != null) {
//            parameters.put("userKey", CoreData.getInstance().getUserKey());
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
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.deleteKotItemDetails(statusCode, headers, responseBody, handler);
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_KOT_NEXT_SUCCESS, null));
                            } else if (resultCode == ResultCode.KOTSUMMARY_IS_UNREAL) {
                                HttpAnalysis.deleteKot(statusCode, headers, responseBody, handler);
                            } else if (resultCode == ResultCode.USER_NO_PERMIT) {
                                handler.sendMessage(handler.obtainMessage(ResultCode.USER_NO_PERMIT, null));
                            } else {
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_KOT_NEXT_FAILED, null));
                            }
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            super.onFailure(statusCode, headers, responseBody, error);
                            handler.sendMessage(handler.obtainMessage(App.HANDLER_KOT_NEXT_FAILED, null));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendMessage(handler.obtainMessage(App.HANDLER_REFRESH_KOT, null));
        }
    }

    /*Complete items in KOT*/
    public static void KotComplete(final Context context,
                                   final Map<String, Object> parameters, String url,
                                   AsyncHttpClient httpClient, final Handler handler, final int id) {
        if (parameters != null) {
//            parameters.put("userKey", CoreData.getInstance().getUserKey());
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                List<KotItemDetail> kotItemDetails = (List<KotItemDetail>) parameters.get("kotItemDetails");
                                HttpAnalysis.getKotItemDetail(statusCode, headers, responseBody, handler);
                                if (id >= 0) {
                                    handler.sendMessage(handler.obtainMessage(App.HANDLER_KOT_ITEM_CALL, kotItemDetails));
                                } else {
                                    handler.sendMessage(handler.obtainMessage(App.HANDLER_REFRESH_KOT, kotItemDetails));
                                }
                            } else if (resultCode == ResultCode.USER_POS_TYPE) {

                                diaLogTrain();
                            } else if (resultCode == ResultCode.USER_NO_PERMIT) {
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_RECONNECT_POS));
                            } else if (resultCode == ResultCode.KOT_COMPLETE_USER_FAILED) {
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_KOT_COMPLETE_USER_FAILED));
                            } else if (resultCode == ResultCode.KOT_COMPLETE_FAILED) {
                                List<KotItemDetail> kotItemDetails = (List<KotItemDetail>) parameters.get("kotItemDetails");
                                for (int i = 0; i < kotItemDetails.size(); i++) {
                                    //将数据库的数据变为原来的数据
                                    KotItemDetail kotItemDetail = kotItemDetails.get(i);
                                    KotItemDetail sqlKotItemDetail = KotItemDetailSQL.getKotItemDetailByUniqueId(kotItemDetail.getUniqueId());
                                    sqlKotItemDetail.setUnFinishQty(sqlKotItemDetail.getUnFinishQty() + kotItemDetail.getFinishQty());
                                    sqlKotItemDetail.setFinishQty(sqlKotItemDetail.getFinishQty() - kotItemDetail.getFinishQty());
                                    sqlKotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNDONE);
                                    KotItemDetailSQL.update(sqlKotItemDetail);
                                }
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_RETURN_ERROR, resultCode));
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_RETURN_ERROR_SHOW, kotItemDetails));
                            } else if (resultCode == ResultCode.KOTSUMMARY_IS_UNREAL) {
                                KotSummary kotSummary = (KotSummary) parameters.get("kotSummary");
                                if (kotSummary != null) {
                                    KotSummarySQL.deleteKotSummary(kotSummary);
                                    KotItemDetailSQL.deleteAllKotItemDetailByKotSummary(kotSummary);
                                }
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_KOTSUMMARY_IS_UNREAL));
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            List<KotItemDetail> kotItemDetails = (List<KotItemDetail>) parameters.get("kotItemDetails");
                            for (int i = 0; i < kotItemDetails.size(); i++) {
                                KotItemDetail kotItemDetail = kotItemDetails.get(i);
                                KotItemDetail sqlKotItemDetail = KotItemDetailSQL.getKotItemDetailByUniqueId(kotItemDetail.getUniqueId());
                                sqlKotItemDetail.setUnFinishQty(sqlKotItemDetail.getUnFinishQty() + kotItemDetail.getFinishQty());
                                sqlKotItemDetail.setFinishQty(sqlKotItemDetail.getFinishQty() - kotItemDetail.getFinishQty());
                                sqlKotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNDONE);
                                KotItemDetailSQL.update(sqlKotItemDetail);
                            }
                            handler.sendMessage(handler.obtainMessage(App.HANDLER_SEND_FAILURE, kotItemDetails));
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*cancel complete items in KOT*/
    public static void CancelComplete(Context context,
                                      final Map<String, Object> parameters, String url,
                                      AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
//            parameters.put("userKey", CoreData.getInstance().getUserKey());
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getNewKotItemDetail(statusCode, headers, responseBody, handler);
                                KotItemDetail kotItemDetail = (KotItemDetail) parameters.get("kotItemDetail");
                                KotItemDetailSQL.deleteKotItemDetail(kotItemDetail);
                                handler.sendMessage(handler.obtainMessage(KotHistory.HANDLER_SUCCEED));
                            } else if (resultCode == ResultCode.USER_POS_TYPE) {

                                diaLogTrain();
                            } else if (resultCode == ResultCode.USER_NO_PERMIT) {
                                handler.sendMessage(handler.obtainMessage(KotHistory.HANDLER_RECONNECT_POS));
                            } else if (resultCode == ResultCode.KOT_COMPLETE_USER_FAILED) {
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_KOT_COMPLETE_USER_FAILED));
                            } else if (resultCode == ResultCode.KOTSUMMARY_IS_UNREAL) {
                                KotItemDetail kotItemDetail = (KotItemDetail) parameters.get("kotItemDetail");
                                KotItemDetailSQL.deleteKotItemDetail(kotItemDetail);
                                KotSummary kotSummary = (KotSummary) parameters.get("kotSummary");
                                KotSummarySQL.deleteKotSummary(kotSummary);
                                KotItemDetailSQL.deleteAllKotItemDetailByKotSummary(kotSummary);
                                handler.sendMessage(handler.obtainMessage(App.HANDLER_KOTSUMMARY_IS_UNREAL));
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
//							KotItemDetail kotItemDetail = (KotItemDetail) parameters.get("kotItemDetail");
//							kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
//							KotItemDetailSQL.update(kotItemDetail);	
                            handler.sendMessage(handler.obtainMessage(KotHistory.HANDLER_SEND_FAILURE));
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callSpecifyNum(Context context,
                                      final Map<String, Object> parameters, String url,
                                      AsyncHttpClient httpClient, final Handler handler, final String id) {
        if (parameters != null) {
//            parameters.put("userKey", CoreData.getInstance().getUserKey());
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                if (!TextUtils.isEmpty(id)) {
                                    KotItemDetailSQL.updateCallById(id);
                                    handler.sendMessage(handler.obtainMessage(App.HANDLER_KOT_ITEM_CALL, null));
                                }
                                handler.sendMessage(handler.obtainMessage(ResultCode.SUCCESS, null));

                            } else if (resultCode == ResultCode.USER_POS_TYPE) {

                                diaLogTrain();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Logout(Context context,
                              final Map<String, Object> parameters, String url,
                              AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
//            parameters.put("userKey", CoreData.getInstance().getUserKey());
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    kdsBaseInfo(parameters), CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler.obtainMessage(Setting.HANDLER_LOGOUT_SUCCESS));
                            } else if (resultCode == ResultCode.USER_POS_TYPE) {

                                diaLogTrain();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(Setting.HANDLER_LOGOUT_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 返回码不需要特殊处理的提醒
    private static void elseResultCodeAction(final int resultCode, int statusCode, Header[] headers, final byte[] responseBody) {
        App.getTopActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String information = null;
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    information = object.optString("posVersion");
                    if (object.has("versionUpdate")) {
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

    public static StringEntity kdsBaseInfo(Map<String, Object> map)
            throws UnsupportedEncodingException {
        Gson gson = new Gson();

        int type = Store.getInt(App.instance, Store.TRAIN_TYPE);
        map.put("trainType", type);

        StringEntity entity = new StringEntity(gson.toJson(map) + EOF,
                "UTF-8");
        return entity;
    }

    private static void diaLogTrain() {
        App.getTopActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                App.getTopActivity().showOneButtonCompelDialog("Mode Change",
                        "Please relogin",
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                KotSummarySQL.deleteAllKotSummary(null);
                                KotItemDetailSQL.deleteAllKotItemDetail(null);
                                KotItemModifierSQL.deleteAllKotItemModifier(null);
//							App.instance.popAllActivityExceptOne(EmployeeID.class);
                                UIHelp.startWelcome(App.getTopActivity());
                                App.instance.popAllActivityExceptOne(Login.class);
                            }
                        });

            }
        });

    }
}
