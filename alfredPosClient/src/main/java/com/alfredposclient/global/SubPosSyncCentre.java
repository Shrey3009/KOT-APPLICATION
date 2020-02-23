package com.alfredposclient.global;

import android.content.Context;
import android.os.Handler;

import com.alfredbase.APPConfig;
import com.alfredbase.http.APIName;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.utils.CallBack;
import com.alfredposclient.http.subpos.SubPosHttpAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SubPosSyncCentre {

	private Timer timer;

	private TimerTask timerTask;

	private static AsyncHttpClient httpClient;

	private static SubPosSyncCentre instance;
	private static SyncHttpClient syncHttpClient;
	/**
	 * 大数据用
	 */
	private static SyncHttpClient bigSyncHttpClient;
	public static final int MODE_FIRST_SYNC = 1;
	public static final int MODE_PUSH_SYNC = 2;

	private SubPosSyncCentre() {

	}

	public static SubPosSyncCentre getInstance() {
		init();
		return instance;
	}

	private static void init() {
		if (instance == null) {
			instance = new SubPosSyncCentre();

			httpClient = new AsyncHttpClient();
			httpClient.addHeader("Connection", "close");
			httpClient.setMaxRetriesAndTimeout(0, 5 * 1000);
			httpClient.setTimeout(20 * 1000);
			syncHttpClient = new SyncHttpClient();
			syncHttpClient.addHeader("Connection", "close");
			syncHttpClient.setTimeout(20 * 1000);
			syncHttpClient.setMaxRetriesAndTimeout(0, 5 * 1000);
			bigSyncHttpClient = new SyncHttpClient();
			bigSyncHttpClient.addHeader("Connection", "close");
			bigSyncHttpClient.setTimeout(100 * 1000);
			bigSyncHttpClient.setMaxRetriesAndTimeout(0, 1 * 1000);
		}
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return "http://" + App.instance.getPairingIp() + ":" + APPConfig.HTTP_SERVER_PORT + "/"
				+ relativeUrl;
	}
	public void chooseRevenue(Context context, Handler handler){
		SubPosHttpAPI.chooseRevenue(context, getAbsoluteUrl(APIName.SUBPOS_CHOOSEREVENUE),httpClient, handler);
	}
	public void login(Context context, Map<String, Object> parameters, final Handler handler) {
		SubPosHttpAPI.login(context, parameters, getAbsoluteUrl(APIName.SUBPOS_LOGIN), httpClient, handler);
	}
	public void updateAllData(Context context, final Handler handler) {
		SubPosHttpAPI.updateAllData(context, getAbsoluteUrl(APIName.SUBPOS_UPDATE_DATA), httpClient, handler);
	}
	public void getOrder(Context context, Map<String, Object> parameters, final Handler handler) {
		SubPosHttpAPI.getOrder(context, parameters, getAbsoluteUrl(APIName.SUBPOS_UPDATE_DATA), httpClient, handler);
	}
	public void cloudSyncUploadOrderInfo(Context context, SyncMsg syncMsg){
		SubPosHttpAPI.cloudSyncUploadOrderInfo(context, syncMsg, getAbsoluteUrl(APIName.SUBPOS_COMMIT_ORDER), bigSyncHttpClient);

	}
	public void cloudSyncUploadOrderInfoLog(Context context, SyncMsg syncMsg){
		SubPosHttpAPI.cloudSyncUploadOrderInfoLog(context, syncMsg, getAbsoluteUrl(APIName.SUBPOS_COMMIT_ORDERLOG), bigSyncHttpClient);

	}
	public void cloudSyncUploadReportInfo(Context context, SyncMsg syncMsg, SubPosBean subPosBean, CallBack callBack){
		SubPosHttpAPI.cloudSyncUploadReportInfo(context, syncMsg, subPosBean, getAbsoluteUrl(APIName.SUBPOS_COMMIT_REPORT), bigSyncHttpClient, callBack);
	}
	public void closeSession(Context context, SubPosBean subPosBean, CallBack callBack){
		SubPosHttpAPI.closeSession(context, subPosBean, getAbsoluteUrl(APIName.SUBPOS_CLOSE_SESSION), bigSyncHttpClient, callBack);
	}


}
