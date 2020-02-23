package com.alfred.callnum.http.server;

import android.content.Context;
import android.util.Log;

import com.alfred.callnum.adapter.CallBean;
import com.alfred.callnum.global.App;
import com.alfredbase.APPConfig;
import com.alfredbase.BaseActivity;
import com.alfredbase.http.APIName;
import com.alfredbase.http.AlfredHttpServer;
import com.alfredbase.http.ResultCode;
import com.alfredbase.store.Store;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CallNumHttpServer extends AlfredHttpServer {
    private BaseActivity context;

    public CallNumHttpServer() {
        super(APPConfig.CALLNUM_HTTP_SERVER_PORT);
    }

    @Override
    public Response doPost(String apiName, Method mothod, Map<String, String> params, String body) {

        Response resp;

        if (apiName == null) {
            resp = getNotFoundResponse();
        } else {
            if (apiName.equals(APIName.CALL_POS_NUM)) {
                // TODO 在这边解析 pos 放过来的号码，开始叫号。
				/*
					叫号内容{"type" : 1, "callnumber":"A1235"}
				 */
                //	call(body);
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String callNumber = jsonObject.optString("callNumber");
                    int callType = jsonObject.optInt("callType");
                    int callTag = jsonObject.optInt("callTag");
                    String printerName = jsonObject.getString("printerName");
                    int printerGroupId = jsonObject.getInt("printerGroupId");

                    CallBean callBean = new CallBean(callNumber, callType, callTag, printerName, printerGroupId);

                    if (jsonObject.has("header")) {
                        Store.putString(App.instance, Store.CALL_NUM_HEADER, jsonObject.optString("header"));
                        callBean.setUpdate(true);
                    }
                    if (jsonObject.has("footer")) {
                        Store.putString(App.instance, Store.CALL_NUM_FOOTER, jsonObject.optString("footer"));
                        callBean.setUpdate(true);
                    }

                    App.getTopActivity().httpRequestAction(App.HANDLER_REFRESH_CALL, callBean);
                    /**
                     * 返回成功
                     */
                    map.put("resultCode", ResultCode.SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return getJsonResponse(new Gson().toJson(map));
            } else if (apiName.equals(APIName.POS_CLOSE_SESSION)) {
                App.getTopActivity().httpRequestAction(App.instance.HANDLER_CLEAN_CALL, null);
                Map<String, Object> map = new HashMap<>();
                map.put("resultCode", ResultCode.SUCCESS);
                return getJsonResponse(new Gson().toJson(map));
            } else if (apiName.equals(APIName.POS_LANGUAGE)) {
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String version = jsonObject.optString("version");
                    String language = jsonObject.optString("language");
                    App.getTopActivity().changeLanguage(language);
                    map.put("resultCode", ResultCode.SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return getJsonResponse(new Gson().toJson(map));
            } else {
                resp = getNotFoundResponse();
            }
        }

        return resp;
    }


    public static String call(int statusCode, Header[] headers,
                              byte[] responseBody, Context context) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));

            int callType = object.optInt("calltype");
            App.instance.setMainPageType(callType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
