package com.alfredposclient.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.alfredbase.ParamConst;
import com.alfredbase.http.AsyncHttpResponseHandlerEx;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.utils.KDSLogUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.global.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPKDSRequest {

    public static void syncSubmitTmpKot(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                        SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        parameters.put("mainpos", App.instance.getMainPosInfo());

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.INVALID_DEVICE) {
                            // if kds device is invadate, POS need remove it.
//									App.instance.removeKDSDevice(kds.getDevice_id());
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

    public static void syncSubmitKotToNextKDS(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                              SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        parameters.put("mainpos", App.instance.getMainPosInfo());
        final KotSummary kotSummary = (KotSummary) parameters.get("kotSummary");
        final List<KotItemDetail> kotItemDetails = (List<KotItemDetail>) parameters.get("kotItemDetails");

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.INVALID_DEVICE) {
                            // if kds device is invadate, POS need remove it.
//									App.instance.removeKDSDevice(kds.getDevice_id());
                        } else if (resultCode == ResultCode.SUCCESS) {
                            if (kotSummary != null) {
                                int kotSummaryId = kotSummary.getId() <= 0 ? kotSummary.getOriginalId() : kotSummary.getId();

                                KotSummary kotSummaryLocal = KotSummarySQL.getKotSummaryById(kotSummaryId);

                                if (kotSummaryLocal == null) return;

                                List<KotItemDetail> kotItemDetailsCopy = new ArrayList<>();
                                if (kotItemDetails != null) {

                                    for (KotItemDetail kotItemDetail : kotItemDetails) {
                                        KotItemDetail kidLocal = KotItemDetailSQL.getKotItemDetailById(kotItemDetail.getId());
                                        if (kidLocal == null) continue;

                                        kidLocal.setStartTime(System.currentTimeMillis());

                                        if (kds.getKdsType() == Printer.KDS_EXPEDITER ||
                                                kds.getKdsType() == Printer.KDS_SUMMARY) {
                                            kidLocal.setEndTime(System.currentTimeMillis());
                                        }
                                        KotItemDetailSQL.update(kidLocal);
                                        kotItemDetailsCopy.add(kidLocal);
                                    }
                                }

                                kotSummaryLocal.setKotSummaryLog(KDSLogUtil.putLog(kotSummaryLocal.getKotSummaryLog(), kotItemDetailsCopy, kds));
                                KotSummarySQL.updateKotSummaryLog(kotSummaryLocal);

                                sendKOTToSummaryKDS(kotSummaryLocal);
                            }
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

    public static void syncSubmitConnectedKDS(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                              SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        parameters.put("mainpos", App.instance.getMainPosInfo());

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.INVALID_DEVICE) {
                        } else if (resultCode == ResultCode.SUCCESS) {

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

    public static void syncSubmitKotToSummaryKDS(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                                 SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        parameters.put("mainpos", App.instance.getMainPosInfo());

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.INVALID_DEVICE) {
                        } else if (resultCode == ResultCode.SUCCESS) {
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

    public static void updateOrderCount(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                        SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        parameters.put("mainpos", App.instance.getMainPosInfo());

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.INVALID_DEVICE) {
                        } else if (resultCode == ResultCode.SUCCESS) {
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

    public static void deleteKdsLogOnBalancer(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                              SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        if (parameters != null) {
            parameters.put("mainpos", App.instance.getMainPosInfo());
        }

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.INVALID_DEVICE) {
                        } else if (resultCode == ResultCode.SUCCESS) {
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

    public static void updateKdsStatus(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                       SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        parameters.put("mainpos", App.instance.getMainPosInfo());

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.SUCCESS) {
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

    public static void deleteKotSummary(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                        SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        parameters.put("mainpos", App.instance.getMainPosInfo());

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.INVALID_DEVICE) {
                            // if kds device is invadate, POS need remove it.
//									App.instance.removeKDSDevice(kds.getDevice_id());
                        } else if (resultCode == ResultCode.SUCCESS) {
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

    public static void syncSubmitKot(Context context, final Map<String, Object> parameters, String url, final KDSDevice kds,
                                     SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        if (parameters != null) {
            if (!parameters.containsKey("mainpos"))
                parameters.put("mainpos", App.instance.getMainPosInfo());
            if (!parameters.containsKey("kds"))
                parameters.put("kds", kds);
        }

        final KotSummary kotSummary = (KotSummary) parameters.get("kotSummary");
        final List<KotItemDetail> kotItemDetails = (List<KotItemDetail>) parameters.get("kotItemDetails");

        LogUtil.log("param : " + new Gson().toJson(parameters) + HttpAPI.EOF);

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.SUCCESS) {
                            boolean isFire = false;
                            if (parameters.containsKey("isFire"))
                                isFire = (boolean) parameters.get("isFire");

                            if (isFire) return;

                            if (kotSummary != null) {

                                List<KotItemDetail> kotItemDetailsCopy = new ArrayList<>();

                                if (kotItemDetails != null) {
                                    for (KotItemDetail kotItemDetail : kotItemDetails) {
                                        kotItemDetail.setStartTime(System.currentTimeMillis());
                                        KotItemDetailSQL.update(kotItemDetail);
                                        kotItemDetailsCopy.add(kotItemDetail);
                                        KotItemDetail kidLocal = KotItemDetailSQL.getKotItemDetailById(kotItemDetail.getId());
                                        LogUtil.log("start time : " + kidLocal.getStartTime());
                                    }
                                }

                                int kotSummaryId = CommonSQL.isFakeId(kotSummary.getId()) ? kotSummary.getOriginalId() : kotSummary.getId();

                                KotSummary kotSummaryLocal = KotSummarySQL.getKotSummaryById(kotSummaryId);
                                if (!TextUtils.isEmpty(kotSummaryLocal.getKotSummaryLog())) {
                                    kotSummary.setKotSummaryLog(kotSummaryLocal.getKotSummaryLog());
                                }

                                kotSummary.setKotSummaryLog(KDSLogUtil.putLog(kotSummary.getKotSummaryLog(), kotItemDetailsCopy, kds));
                                KotSummarySQL.updateKotSummaryLog(kotSummary);

                                sendKOTToSummaryKDS(kotSummary);

                            }
                        } else if (resultCode == ResultCode.INVALID_DEVICE) {
                            // if kds device is invadate, POS need remove it.
//									App.instance.removeKDSDevice(kds.getDevice_id());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        if (error.getCause() instanceof ConnectException) {
                            LogUtil.log("Kds Submit failed : " + error.getCause().getMessage());
                            throw new RuntimeException(error);
                        }
                    }
                });
    }

    public static void checkKdsBalance(Context context, final Map<String, Object> parameters, String url, final KDSDevice kds,
                                       SyncHttpClient syncHttpClient, final Handler handler) throws Exception {

        if (parameters != null) {
            parameters.put("mainpos", App.instance.getMainPosInfo());
            parameters.put("kds", kds);
        }

        syncHttpClient.post(context, url,
                new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
                        "UTF-8"), HttpAssembling.CONTENT_TYPE,
                new AsyncHttpResponseHandlerEx() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers,
                                          final byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (resultCode == ResultCode.SUCCESS) {

                            try {
                                JSONObject object = new JSONObject(new String(responseBody));
                                Gson gson = new Gson();
                                String params = object.getString("params");
                                KDSDevice selectedKds = gson.fromJson(object.optString("kds"), KDSDevice.class);
                                object = new JSONObject(params);

                                KotSummary kotSummary = gson.fromJson(object.getString("kotSummary"), KotSummary.class);
                                ArrayList<KotItemDetail> kotItemDetails = gson.fromJson(object.getString("kotItemDetails"),
                                        new TypeToken<ArrayList<KotItemDetail>>() {
                                        }.getType());
                                ArrayList<KotItemModifier> kotItemModifiers = gson.fromJson(object.getString("kotItemModifiers"),
                                        new TypeToken<ArrayList<KotItemModifier>>() {
                                        }.getType());
                                String method = object.getString("method");

                                App.instance.getKdsJobManager().sendToSelectedKDS(
                                        kotSummary, kotItemDetails,
                                        kotItemModifiers, method, null, selectedKds);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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

    private static void sendKOTToSummaryKDS(KotSummary kotSummary) {
        ArrayList<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryId(kotSummary.getId());
        ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<>();

        for (KotItemDetail kotItemDetail : kotItemDetails) {
            kotItemModifiers.addAll(KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetail.getId()));
        }

        App.instance.getKdsJobManager().sendKOTToKDSSummary(
                kotSummary, kotItemDetails,
                kotItemModifiers, ParamConst.JOB_KOT_SUMMARY);
    }

//	public static void asyncSubmitKot(Context context, Map<String, Object> parameters, String url,
//			AsyncHttpClient httpClient, final Handler handler) throws Exception {
//
//		httpClient.post(context,url,
//					 new StringEntity(new Gson().toJson(parameters) + HttpAPI.EOF,
//							 "UTF-8"),HttpAssembling.CONTENT_TYPE,
//						new AsyncHttpResponseHandlerEx() {
//							@Override
//							public void onSuccess(int statusCode, Header[] headers,
//									byte[] responseBody) {
//								super.onSuccess(statusCode, headers, responseBody);
//								if (resultCode == ResultCode.SUCCESS) {
//
//								}
//							}
//							@Override
//							public void onFailure(int statusCode, Header[] headers,
//									byte[] responseBody, Throwable error) {
//								if (error.getCause() instanceof ConnectException) {
//								  throw new RuntimeException(error);
//								}
//							}
//			});
//	}

    public static void sendSessionClose(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                        AsyncHttpClient httpClient) {
        parameters.put("mainpos", App.instance.getMainPosInfo());
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                LogUtil.i("sendSessionClose", "SUCCESS");
                            } else if (resultCode == ResultCode.INVALID_DEVICE) {
                                //: if waiter device is invadate, POS need remove it.
                                App.instance.removeKDSDevice(kds.getDevice_id());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            LogUtil.e("sendSessionClose", "FAILURE");
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void transferTable(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                     AsyncHttpClient httpClient) {
        parameters.put("mainpos", App.instance.getMainPosInfo());
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (error.getCause() instanceof ConnectException) {
                                throw new RuntimeException(error);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshSameGroupKDS(Context context, Map<String, Object> parameters, String url, final KDSDevice kds,
                                           AsyncHttpClient httpClient) {
        parameters.put("mainpos", App.instance.getMainPosInfo());
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            if (error.getCause() instanceof ConnectException) {
                                throw new RuntimeException(error);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
