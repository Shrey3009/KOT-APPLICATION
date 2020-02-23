package com.alfredbase.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
	/**
	 *  跟 JS里面定义的获取js回调方法名
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static String getJSCallBackName(String json){
		JSONObject jsonObject;
		String name = "";
		try {
			jsonObject = new JSONObject(json);
			name = jsonObject.getString("js_callback") ;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public static String getJSONFromEncode(String str){
		try {
			str = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
