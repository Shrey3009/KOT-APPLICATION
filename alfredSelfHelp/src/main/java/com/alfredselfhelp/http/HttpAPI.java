package com.alfredselfhelp.http;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.alfredbase.global.BugseeHelper;
import com.alfredbase.http.AsyncHttpResponseHandlerEx;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.store.Store;
import com.alfredbase.utils.DialogFactory;
import com.alfredselfhelp.activity.MenuActivity;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.utils.UIHelp;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpAPI {
	public static final String EOF = "\r\nEOF\r\n";
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
	public static void getRemainingStock(final Context context,
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
								HttpAnalysis.remainingStock(responseBody,handler);

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



	public static void getCheckSotckNum(final Context context,Map<String, Object> parameters,
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
							if (resultCode == ResultCode.SUCCESS) {
								HttpAnalysis.getCheckStock(responseBody,handler);

							} else {
								//handler.sendEmptyMessage(MenuActivity.VIEW_CHECK_SOTCK_NUM_FAILED);
								//elseResultCodeAction(resultCode, body);
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
								HttpAnalysis.updateAllData(body, handler);

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
								HttpAnalysis.login(resultCode, body, handler);
							} else {

							//	handler.sendEmptyMessage(resultCode);
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
								handler.sendEmptyMessage(MenuActivity.VIEW_PAYMENT_STORED_CARDNUM_SUCCEED);
							} else {
								handler.sendMessage(handler.obtainMessage(MenuActivity.VIEW_PAYMENT_STORED_CARDNUM_SUCCEED, resultCode));
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




	public static void commitOrder(Context context, Map<String, Object> map, String url,
								   AsyncHttpClient httpClient, final Handler handler,
								   final PaymentSettlement paymentSettlement, final String cardNum) {

		try {
			String j = new Gson().toJson(map);
			JSONObject jsonObject = new JSONObject(j);
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
							if (resultCode == ResultCode.SUCCESS) {
								HttpAnalysis.commitOrderAndOrderDetails(responseBody, cardNum);
								handler.sendMessage(handler.obtainMessage(MenuActivity.VIEW_COMMIT_ORDER_SUCCEED, paymentSettlement));
							} else {
								handler.sendEmptyMessage(MenuActivity.VIEW_COMMIT_ORDER_FAILED);
							}
						}
						@Override
						public void onFailure(final int statusCode, final Header[] headers,
											  final byte[] responseBody, final Throwable error) {
							super.onFailure(statusCode, headers, responseBody, error);
							handler.sendEmptyMessage(MenuActivity.VIEW_COMMIT_ORDER_FAILED);
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
										BugseeHelper.buttonClicked(v);
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
