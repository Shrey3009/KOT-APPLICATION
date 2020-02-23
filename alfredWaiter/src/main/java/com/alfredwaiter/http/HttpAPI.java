package com.alfredwaiter.http;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.AsyncHttpResponseHandlerEx;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.RxBus;
import com.alfredwaiter.R;
import com.alfredwaiter.activity.EmployeeID;
import com.alfredwaiter.activity.KOTNotification;
import com.alfredwaiter.activity.Login;
import com.alfredwaiter.activity.OrderDetailsTotal;
import com.alfredwaiter.activity.Setting;
import com.alfredwaiter.activity.TablesPage;
import com.alfredwaiter.activity.Welcome;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.SyncCentre;
import com.alfredwaiter.global.UIHelp;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpAPI {
    public static void employeeId(Context context,
                                  Map<String, Object> parameters, String url,
                                  AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters),
                            "UTF-8"), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.employeeId(statusCode, headers,
                                        responseBody, handler);
                            } else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else if (resultCode == ResultCode.USER_NO_PERMIT) {
                                handler.sendEmptyMessage(ResultCode.USER_NO_PERMIT);
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void login(Context context, Map<String, Object> parameters,
                             String url, AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.login(statusCode, headers,
                                        responseBody);
                                handler.sendMessage(handler
                                        .obtainMessage(Login.HANDLER_LOGIN));
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            // error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public static void getRestaurantInfo(Context context,
    // Map<String, Object> parameters, String url,
    // AsyncHttpClient httpClient, final Handler handler) {
    // // 除了登录接口，其他接口都要加这个
    // if (parameters != null) {
    // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
    // }
    // try {
    // httpClient.post(context, url,
    // new StringEntity(new Gson().toJson(parameters) + EOF,
    // "UTF-8"), HttpAssembling.CONTENT_TYPE,
    // new AsyncHttpResponseHandlerEx() {
    // @Override
    // public void onSuccess(int statusCode, Header[] headers,
    // byte[] responseBody) {
    // super.onSuccess(statusCode, headers, responseBody);
    // if (resultCode == ResultCode.SUCCESS) {
    // HttpAnalysis.getRestaurantInfo(statusCode,
    // headers, responseBody);
    // handler.sendMessage(handler
    // .obtainMessage(SelectRevenue.HANDLER_GET_RESTAURANT_INFO));
    // }
    // }
    //
    // @Override
    // public void onFailure(int statusCode, Header[] headers,
    // byte[] responseBody, Throwable error) {
    // super.onFailure(statusCode, headers, responseBody,
    // error);
    // // error.printStackTrace();
    // handler.sendMessage(handler
    // .obtainMessage(ResultCode.CONNECTION_FAILED));
    // }
    // });
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    public static void getUser(Context context, String url,
                               Map<String, Object> parameters, AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
//								new Thread(new Runnable() {
//									public void run() {
                                HttpAnalysis.getUsers(statusCode,
                                        headers, responseBody);
                                handler.sendEmptyMessage(EmployeeID.SYNC_DATA_TAG);
//									}
//								}).start();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRestaurantInfo(Context context, String url,
                                         Map<String, Object> parameters,
                                         AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getRestaurantInfo(statusCode,
                                        headers, responseBody);
                                handler.sendEmptyMessage(Login.HANDLER_RESTAURANT_INFO_SUCCESS);
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getItemCategory(Context context, String url,
                                       Map<String, Object> parameters, AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        // if (parameters != null) {
        // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
        // }
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
//								new Thread(new Runnable() {
//									public void run() {
                                HttpAnalysis.getItemCategory(
                                        statusCode, headers,
                                        responseBody);
                                handler.sendEmptyMessage(EmployeeID.SYNC_DATA_TAG);
//									}
//								}).start();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getItem(Context context, String url,
                               Map<String, Object> parameters, AsyncHttpClient httpClient) {
        // 除了登录接口，其他接口都要加这个
        // if (parameters != null) {
        // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
        // }
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
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
                                    }
                                }).start();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getStock(Context context, String url,
                                Map<String, Object> parameters, AsyncHttpClient httpClient) {
        // 除了登录接口，其他接口都要加这个
        // if (parameters != null) {
        // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
        // }
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.getStock(statusCode,
                                        headers, responseBody);
                                RxBus.getInstance().post(RxBus.RX_REFRESH_STOCK, null);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getModifier(Context context, String url,
                                   Map<String, Object> parameters, AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        // if (parameters != null) {
        // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
        // }
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
//								new Thread(new Runnable() {
//									public void run() {
                                HttpAnalysis.getAllModifier(statusCode,
                                        headers, responseBody);
                                handler.sendEmptyMessage(EmployeeID.SYNC_DATA_TAG);
//									}
//								}).start();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTax(Context context, String url,
                              Map<String, Object> parameters, AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        // if (parameters != null) {
        // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
        // }
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
//								new Thread(new Runnable() {
//									public void run() {
                                HttpAnalysis.getTax(statusCode,
                                        headers, responseBody);
                                handler.sendEmptyMessage(EmployeeID.SYNC_DATA_TAG);
//									}
//								}).start();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getHappyHour(Context context, String url,
                                    Map<String, Object> parameters, AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        // if (parameters != null) {
        // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
        // }
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
//								new Thread(new Runnable() {
//
//									@Override
//									public void run() {
                                HttpAnalysis.getHappyHour(statusCode,
                                        headers, responseBody);
                                handler.sendEmptyMessage(EmployeeID.SYNC_DATA_TAG);
//									}
//								}).start();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getPrinters(Context context, String url,
                                   Map<String, Object> parameters, AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        // if (parameters != null) {
        // parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
        // }
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
//								new Thread(new Runnable() {
//
//									@Override
//									public void run() {
                                HttpAnalysis.getPrinter(statusCode,
                                        headers, responseBody);
                                handler.sendEmptyMessage(EmployeeID.SYNC_DATA_TAG);
//									}
//								}).start();
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getPlaceInfo(final Context context,
                                    Map<String, Object> parameters, String url,
                                    AsyncHttpClient httpClient, final Handler mmHandler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        Handler mHandler = mmHandler;

                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        HttpAnalysis.getPlaceInfo(statusCode,
                                                headers, responseBody);
                                        mHandler.sendEmptyMessage(EmployeeID.SYNC_DATA_TAG);
                                    }
                                }).start();

                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, final Throwable error) {
                            mHandler.sendMessage(mHandler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
//							new Thread(new Runnable() {
//
//								@Override
//								public void run() {
//									if (error.getClass().equals(
//											SocketTimeoutException.class)) {
//										mHandler.sendMessage(mHandler.obtainMessage(
//												SelectRevenue.HANDLER_CONN_ERROR,
//												SelectRevenue.CONN_TIMEOUT));
//									} else if(error.getClass().equals(
//											HttpHostConnectException.class) && mHandler != null){
//										System.out.print(mHandler.toString());
//										boolean fal = mHandler.sendMessage(mHandler.obtainMessage(
//												SelectRevenue.HANDLER_CONN_REFUSED));
//									} else{
//										mHandler.sendMessage(mHandler.obtainMessage(
//												SelectRevenue.HANDLER_CONN_ERROR,
//												SelectRevenue.UNKNOW_ERROR));
//									}
//								}
//							}).start();

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void selectTables(Context context,
                                    Map<String, Object> parameters, String url,
                                    AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                Order order = HttpAnalysis.selectTables(
                                        statusCode, headers, responseBody);
                                handler.sendMessage(handler.obtainMessage(
                                        TablesPage.VIEW_EVENT_SELECT_TABLES,
                                        order));
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            // error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            super.onFailure(statusCode, headers, responseBody, error);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void commitOrderAndOrderDetails(final Context context,
                                                  Map<String, Object> parameters, String url,
                                                  AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                SyncCentre.getInstance().getStock(context);
                                HttpAnalysis.commitOrderAndOrderDetails(statusCode, headers, responseBody, handler);
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else if (resultCode == ResultCode.ORDER_FINISHED) {
                                handler.sendEmptyMessage(ResultCode.ORDER_FINISHED);
                            } else if (resultCode == ResultCode.NONEXISTENT_ORDER) {
                                handler.sendEmptyMessage(ResultCode.NONEXISTENT_ORDER);
                            } else if (resultCode == ResultCode.ORDER_HAS_CLOSING) {
                                handler.sendEmptyMessage(ResultCode.ORDER_HAS_CLOSING);
                            } else if (resultCode == ResultCode.ORDER_SPLIT_IS_SETTLED) {
                                int groupId = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                                    groupId = jsonObject.getInt("groupId");
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                handler.sendMessage(handler.obtainMessage(ResultCode.ORDER_SPLIT_IS_SETTLED, groupId));
                            } else if (resultCode == ResultCode.WAITER_OUT_OF_STOCK) {
                                String stockNum = null;
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                                    stockNum = jsonObject.getString("stockNum");
                                    SyncCentre.getInstance().getStock(context);
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                handler.sendMessage(handler.obtainMessage(ResultCode.WAITER_OUT_OF_STOCK, stockNum));
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

    /* Send Waiter device details to POS when pairing completes */
    public static void pairingComplete(final Context context,
                                       Map<String, Object> parameters, String url, AsyncHttpClient httpClient,
                                       final Handler handler) {
        if (parameters != null) {
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.pairingComplete(statusCode,
                                        headers, responseBody, handler);
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
//							Toast.makeText(context,"Pairing failed: Network errors", 1000).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout(final Context context,
                              Map<String, Object> parameters, String url, AsyncHttpClient httpClient,
                              final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters), HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendMessage(handler.obtainMessage(Setting.HANDLER_LOGOUT_SUCCESS));
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(Setting.HANDLER_LOGOUT_FAILED, error));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getKotNotification(final Context context,
                                          Map<String, Object> parameters, String url,
                                          AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters),
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
                                        HttpAnalysis.getKotNotification(
                                                statusCode, headers,
                                                responseBody, handler);
                                    }
                                }).start();

                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
                            UIHelp.showToast((BaseActivity) context, context.getResources().getString(R.string.pairing_failed));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handlerCollectKotItem(final Context context,
                                             Map<String, Object> parameters, String url,
                                             AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(KOTNotification.VIEW_EVENT_COLLECT_KOTITEM);
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendEmptyMessage(KOTNotification.VIEW_EVENT_COLLECT_KOTITEM);
                            UIHelp.showToast((BaseActivity) context, context.getResources().getString(R.string.pairing_failed));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handlerGetOrderDetails(final Context context,
                                              Map<String, Object> parameters, String url,
                                              AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getRevenueCenter().getId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.handlerGetOrderDetails(statusCode, headers, responseBody, handler);
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED, error));
//							Toast.makeText(context,"Get order details failed: Network errors", 1000).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getTemporaryDish(final Context context,
                                        Map<String, Object> parameters, String url,
                                        AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(Setting.TEMPORARY_DISH_ADD_POS_SUCCESS);
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                handler.sendEmptyMessage(Setting.TEMPORARY_DISH_ADD_POS_FAILED);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            //		handler.handleMessage(handler.obtainMessage(OrderDetailsTotal.VIEW_EVENT_GET_BILL_FAILED));
                            Toast.makeText(context, "Cannot get bill print at this moment: Network errors", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBillPrint(final Context context,
                                    Map<String, Object> parameters, String url,
                                    AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.saveOrderBill(statusCode, headers, responseBody);
                                handler.sendEmptyMessage(OrderDetailsTotal.VIEW_EVENT_GET_BILL);
                            }
                            else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            }
                            else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.handleMessage(handler.obtainMessage(OrderDetailsTotal.VIEW_EVENT_GET_BILL_FAILED));
//							Toast.makeText(context,"Cannot get bill print at this moment: Network errors", 1000).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printBill(final Context context,
                                 Map<String, Object> parameters, String url,
                                 AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    waiterBaseInfo(parameters),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.saveOrderBill(statusCode, headers, responseBody);
                                handler.sendEmptyMessage(OrderDetailsTotal.VIEW_EVENT_PRINT_BILL);
                            } else if(resultCode==ResultCode.SUCCESS_WAITER_ONCE){
                                handler.sendEmptyMessage(ResultCode.SUCCESS_WAITER_ONCE);
                            } else if(resultCode==ResultCode.USER_POS_TYPE){
                                diaLogTrain();
                            } else if (resultCode == ResultCode.ORDER_FINISHED) {
                                handler.sendEmptyMessage(ResultCode.ORDER_FINISHED);
                            } else if (resultCode == ResultCode.ORDER_PRINT) {
                                handler.sendEmptyMessage(ResultCode.ORDER_PRINT);
                            }else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.handleMessage(handler.obtainMessage(OrderDetailsTotal.VIEW_EVENT_PRINT_BILL_FAILED));
//							Toast.makeText(context,"Cannot get bill print at this moment: Network errors", 1000).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rePrintKOT(final Context context,
                                  Map<String, Object> parameters, String url,
                                  AsyncHttpClient httpClient, final Handler handler) {
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey());
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                handler.sendEmptyMessage(OrderDetailsTotal.VIEW_EVENT_PRINT_KOT_SUCCESS);
                            } else if (resultCode == ResultCode.ORDER_FINISHED) {
                                handler.sendEmptyMessage(ResultCode.ORDER_FINISHED);
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.handleMessage(handler.obtainMessage(OrderDetailsTotal.VIEW_EVENT_PRINT_KOT_FAILED));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPrintKOTData(final Context context,
                                       Map<String, Object> parameters, String url,
                                       AsyncHttpClient httpClient, final Handler handler) {
        // 除了登录接口，其他接口都要加这个
        if (parameters != null) {
            parameters.put("userKey", CoreData.getInstance().getUserKey(App.instance.getMainPosInfo().getRevenueId()));
            parameters.put("appVersion", App.instance.VERSION);
        }
        try {
            httpClient.post(context, url,
                    new StringEntity(new Gson().toJson(parameters), "UTF-8"),
                    HttpAssembling.CONTENT_TYPE,
                    new AsyncHttpResponseHandlerEx() {
                        @Override
                        public void onSuccess(final int statusCode,
                                              final Header[] headers,
                                              final byte[] responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            if (resultCode == ResultCode.SUCCESS) {
                                HttpAnalysis.saveKOTData(statusCode, headers, responseBody);

                                handler.sendEmptyMessage(OrderDetailsTotal.GET_PRINT_KOT_DATA_SUCCESS);
                            } else if (resultCode == ResultCode.ORDER_FINISHED) {
                                handler.sendEmptyMessage(ResultCode.ORDER_FINISHED);
                            } else {
                                elseResultCodeAction(resultCode, statusCode, headers, responseBody);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            handler.handleMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED));
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
                            DialogFactory.showUpdateVersionDialog(App.getTopActivity(), versionUpdate, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    long posUpdateId = DownloadFactory.downloadApk(App.getTopActivity(), (DownloadManager) App.getTopActivity().getSystemService(Context.DOWNLOAD_SERVICE), versionUpdate.getWaiterDownload(), Store.getLong(App.getTopActivity(), "posUpdateId"));
                                    Store.putLong(App.getTopActivity(), "posUpdateId", posUpdateId);
                                }
                            }, null);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                App.getTopActivity().dismissLoadingDialog();
                if (resultCode == ResultCode.USER_NO_PERMIT) {

                    DialogFactory.commonTwoBtnDialog(App.getTopActivity(),
                            App.instance.getResources().getString(R.string.warning),
                            App.instance.getResources().getString(com.alfredbase.R.string.user_no_permission) + "\nRelogin?",
                            App.instance.getResources().getString(com.alfredbase.R.string.ok).toUpperCase(), App.instance.getResources().getString(com.alfredbase.R.string.no).toUpperCase(), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OrderDetailSQL.deleteAllOrderDetail();
                                    Store.remove(App.instance, Store.WAITER_USER);
                                    App.instance.popAllActivityExceptOne(Welcome.class);
                                }
                            }, null);
                }
                UIHelp.showToast(App.getTopActivity(), ResultCode.getErrorResultStrByCode(App.instance, resultCode, information));


            }
        });
    }

    public static StringEntity waiterBaseInfo(Map<String, Object> map)
            throws UnsupportedEncodingException {
        Gson gson = new Gson();

        int type=Store.getInt(App.instance, Store.TRAIN_TYPE);
        map.put("trainType", type);

        StringEntity entity = new StringEntity(gson.toJson(map) ,
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
                                OrderSQL.deleteAllOrder();
                                OrderDetailSQL.deleteAllOrderDetail();
                                OrderModifierSQL.deleteAllOrderModifier();
                                OrderDetailTaxSQL.deleteAllOrderDetailTax();
                                UIHelp.startEmployeeID(App.getTopActivity());
                                App.instance.popAllActivityExceptOne(EmployeeID.class);
                            }
                        });

            }
        });

    }
}
