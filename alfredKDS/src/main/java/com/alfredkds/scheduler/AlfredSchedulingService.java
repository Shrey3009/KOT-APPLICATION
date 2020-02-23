package com.alfredkds.scheduler;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AlfredSchedulingService extends IntentService {
    public static final String ACTION = "com.alfredbase.scheduler.AlfredSchedulingService";
    public static final String URL = "http://www.google.com";
    public static final String RESPONSE_STRING = "POLLING_KDS";
    public static final String RESPONSE_MESSAGE = "POLLING_KDS_DATA";

    private AsyncHttpClient client;
    
	public AlfredSchedulingService() {
        super("SchedulingService");
        this.client =  new AsyncHttpClient();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String urlString = URL;
        String result ="";
        
        if (bundle != null) {
            //result = loadFromNetwork(urlString);
			result = "server is triggered";
			Bundle data = new Bundle();
			data.putString("pulleddata", result);
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("com.alfredkds.activity.intent.action.PROCESS_RESPONSE");
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra(RESPONSE_STRING, result);
			broadcastIntent.putExtra(RESPONSE_MESSAGE, result);
			sendBroadcast(broadcastIntent);
        }
        // Release the wake lock provided by the BroadcastReceiver.
        AlfredAlarmReceiver.completeWakefulIntent(intent);
    }
    
    private void pollData(String url){
    
        // get method
        client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
                try {
                    JSONObject result = new JSONObject(new String(
                            responseBody, "utf-8"));
                    int state = result.getInt("state");
                } catch (Exception e) {
                    e.printStackTrace();
                }				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
        });
    }
}
