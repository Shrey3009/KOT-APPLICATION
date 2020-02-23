package com.alfredbase.http;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AsyncHttpResponseHandlerEx extends AsyncHttpResponseHandler {
	private static final String TAG = AsyncHttpResponseHandlerEx.class
			.getSimpleName();
	protected int resultCode = 0;

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		String result = new String(responseBody);
		LogUtil.d(TAG, result);
		try {
			if(!CommonUtil.isNull(result)){
				JSONObject object = new JSONObject(result);
				resultCode = object.getInt("resultCode");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			byte[] responseBody, Throwable error) {
		error.printStackTrace();
	}

}
