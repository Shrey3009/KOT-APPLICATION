package com.alfredbase.http;

import com.alfredbase.utils.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class AsyncHttpJsonResponseHandlerEx extends JsonHttpResponseHandler {
	private static final String TAG = AsyncHttpJsonResponseHandlerEx.class
			.getSimpleName();
	protected int resultCode = 0;

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//		String result = new String(responseBody);
		LogUtil.d(TAG, response.toString());
		try {
//			if(!CommonUtil.isNull(result)){
//				JSONObject object = new JSONObject(result);
//				resultCode = object.getInt("resultCode");
//			}
			resultCode = response.getInt("resultCode");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
		error.printStackTrace();
	}

}
