package com.alfredposclient.http.subpos;

import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class SubPosHttpCallBackEx implements Callback {

    private static final String TAG = SubPosHttpCallBackEx.class.getSimpleName();
    protected int resultCode = 0;
    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();
        LogUtil.d(TAG, result);
        try {
            if(!CommonUtil.isNull(result)){
                JSONObject object = new JSONObject(result);
                resultCode = object.getInt("resultCode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onSuccess(response.body().string());
    }
    public abstract void onSuccess(String body);
    public abstract void onError(IOException e);
}
