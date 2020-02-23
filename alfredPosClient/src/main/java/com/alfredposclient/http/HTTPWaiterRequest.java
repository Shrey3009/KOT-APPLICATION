package com.alfredposclient.http;

import android.content.Context;

import com.alfredbase.http.AsyncHttpResponseHandlerEx;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.global.App;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.net.ConnectException;
import java.util.Map;

public class HTTPWaiterRequest {
	
	/* Notify waiter to get all notifications*/
	public static void sendKotNotification(Context context, Map<String, Object> parameters, String url,
			final WaiterDevice waiter, SyncHttpClient httpClient){
		    try {
		    	//: add main POS Info.
		    	//Waiter APP can filter out messages from unconnected main pos, because waiter app might change ip
		    	parameters.put("mainpos", App.instance.getMainPosInfo());
				httpClient.post(context,url, 
						 new StringEntity(new Gson().toJson(parameters), "UTF-8"),HttpAssembling.CONTENT_TYPE,
							new AsyncHttpResponseHandlerEx() {
								@Override
								public void onSuccess(int statusCode, Header[] headers,
										byte[] responseBody) {
									super.onSuccess(statusCode, headers, responseBody);
									if (resultCode == ResultCode.SUCCESS) {
										LogUtil.i("sendKotNotification", "SUCCESS");
									}else if(resultCode==ResultCode.INVALID_DEVICE) {
										//: if waiter device is invadate, POS need remove it.
										App.instance.removeWaiterDevice(waiter);
									}
								}
								@Override
								public void onFailure(int statusCode, Header[] headers,
										byte[] responseBody, Throwable error) {
									LogUtil.e("sendKotNotification", "FAILURE");
								}			
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/* Session close tp notify waiter */
	public static void sendSessionClose(Context context, Map<String, Object> parameters, String url, final WaiterDevice waiter,
			AsyncHttpClient httpClient){
		    try {
		    	//: add main POS Info.
		    	//Waiter APP can filter out messages from unconnected main pos, because waiter app might change ip
		    	parameters.put("mainpos", App.instance.getMainPosInfo());
		    	httpClient.post(context,url, 
						 new StringEntity(new Gson().toJson(parameters), "UTF-8"),HttpAssembling.CONTENT_TYPE,
							new AsyncHttpResponseHandlerEx() {
								@Override
								public void onSuccess(int statusCode, Header[] headers,
										byte[] responseBody) {
									super.onSuccess(statusCode, headers, responseBody);
									if (resultCode == ResultCode.SUCCESS) {
										LogUtil.i("sendSessionClose", "SUCCESS");
									}else if(resultCode==ResultCode.INVALID_DEVICE) {
										//: if waiter device is invadate, POS need remove it.
										App.instance.removeWaiterDevice(waiter);
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
	public static void transferTable(Context context, Map<String, Object> parameters, String url, WaiterDevice device,
			AsyncHttpClient httpClient){
	    try {
	    	//: add main POS Info.
	    	//Waiter APP can filter out messages from unconnected main pos, because waiter app might change ip
	    	parameters.put("mainpos", App.instance.getMainPosInfo());
	    	httpClient.post(context,url, 
					 new StringEntity(new Gson().toJson(parameters), "UTF-8"),HttpAssembling.CONTENT_TYPE,
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
