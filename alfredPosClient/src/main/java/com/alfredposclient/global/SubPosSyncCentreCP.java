package com.alfredposclient.global;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.alfredbase.APPConfig;
import com.alfredbase.http.APIName;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.store.Store;
import com.alfredbase.utils.DialogFactory;
import com.alfredposclient.http.subpos.SubPosHttpAnalysis;
import com.alfredposclient.http.subpos.SubPosHttpCallBackEx;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SubPosSyncCentreCP {

	private Timer timer;

	private TimerTask timerTask;

//	private static AsyncHttpClient httpClient;
//
	private static SubPosSyncCentreCP instance;
//	private static SyncHttpClient syncHttpClient;
	/**
	 * 大数据用
	 */
//	private static SyncHttpClient bigSyncHttpClient;
	public static final int MODE_FIRST_SYNC = 1;
	public static final int MODE_PUSH_SYNC = 2;


	private static OkHttpClient httpClient;

	private static OkHttpClient bigHttpClient;
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
	private static MediaType mediaType;
	private static Gson gson;
	private SubPosSyncCentreCP() {

	}

	public static SubPosSyncCentreCP getInstance() {
		init();
		return instance;
	}

	private static void init() {
		if (instance == null) {
			instance = new SubPosSyncCentreCP();
			OkHttpClient.Builder builder = new OkHttpClient.Builder()
//					.addInterceptor(new Interceptor() {
//						@Override
//						public Response intercept(Chain chain) throws IOException {
//							Request request = chain.request()
//									.newBuilder()
//									.addHeader("Connection", "close")
////									.addHeader("accept", "application/json")
//									.build();
//							return chain.proceed(request);
//						}
//					})
					.retryOnConnectionFailure(false);
			builder.connectTimeout(10, TimeUnit.SECONDS)
					.readTimeout(10, TimeUnit.SECONDS)
					.writeTimeout(10, TimeUnit.SECONDS);
			httpClient = builder.build();

			bigHttpClient = httpClient.newBuilder()
					.connectTimeout(20, TimeUnit.SECONDS)
					.readTimeout(20, TimeUnit.SECONDS)
					.writeTimeout(20, TimeUnit.SECONDS).build();
			mediaType = MediaType.parse(CONTENT_TYPE);
			gson = new Gson();
//			httpClient = new AsyncHttpClient();
//			httpClient.addHeader("Connection", "close");
//			httpClient.setMaxRetriesAndTimeout(0, 5 * 1000);
//			httpClient.setTimeout(20 * 1000);
//			syncHttpClient = new SyncHttpClient();
//			syncHttpClient.addHeader("Connection", "close");
//			syncHttpClient.setTimeout(20 * 1000);
//			syncHttpClient.setMaxRetriesAndTimeout(0, 5 * 1000);
//			bigSyncHttpClient = new SyncHttpClient();
//			bigSyncHttpClient.addHeader("Connection", "close");
//			bigSyncHttpClient.setTimeout(100 * 1000);
//			bigSyncHttpClient.setMaxRetriesAndTimeout(0, 1 * 1000);
		}
	}

	public void chooseRevenue(final Handler handler){
		Request request = new Request.Builder()
				.url(getAbsoluteUrl(APIName.SUBPOS_CHOOSEREVENUE))
				.post(okhttp3.internal.Util.EMPTY_REQUEST)
				.addHeader("Connection", "close")
				.build();
		httpClient.newCall(request).enqueue(new SubPosHttpCallBackEx(){
			@Override
			public void onSuccess(String body) {
				if (resultCode == ResultCode.SUCCESS) {
					handler.sendEmptyMessage(ResultCode.SUCCESS);
				} else {
					elseResultCodeAction(resultCode, body);
				}
			}

			@Override
			public void onError(IOException e) {
				handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED,e));
			}
		});
	}
	public void login(Map<String, Object> parameters, final Handler handler) {
		String json = gson.toJson(parameters);
		Request request = new Request.Builder()
				.url(getAbsoluteUrl(APIName.SUBPOS_LOGIN))
				.post(RequestBody.create(mediaType, json))
				.build();
		httpClient.newCall(request).enqueue(new SubPosHttpCallBackEx(){
			@Override
			public void onSuccess(String body) {
				if (resultCode == ResultCode.SUCCESS) {
					SubPosHttpAnalysis.login(resultCode, body,handler);
				} else {
					elseResultCodeAction(resultCode, body);
				}
			}

			@Override
			public void onError(IOException e) {
				handler.sendMessage(handler.obtainMessage(ResultCode.CONNECTION_FAILED,e));
			}
		});
	}
	private String getAbsoluteUrl(String relativeUrl) {
		return "http://" + App.instance.getPairingIp() + ":" + APPConfig.HTTP_SERVER_PORT + "/"
				+ relativeUrl;
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

}
