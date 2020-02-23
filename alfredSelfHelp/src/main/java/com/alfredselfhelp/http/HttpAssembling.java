package com.alfredselfhelp.http;

import android.os.Build;

import com.alfredbase.global.CoreData;
import com.alfredbase.utils.CommonUtil;
import com.alfredselfhelp.global.App;
import com.google.gson.Gson;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class HttpAssembling {
	public static final String CONTENT_TYPE = "text/plain;charset=UTF-8";




	public static StringEntity encapsulateBaseInfo(Map<String, Object> map)
			throws UnsupportedEncodingException {
		Gson gson = new Gson();
		map.put("userKey", CoreData.getInstance().getLoginResult().getUserKey());
		map.put("restaurantKey", CoreData.getInstance().getLoginResult()
				.getRestaurantKey());
		map.put("version", App.instance.VERSION );
		map.put("deviceId", CommonUtil.getLocalMacAddress(App.instance));
//		if(App.instance.isSUNMIShow()){
//			map.put("snCode", Build.SERIAL);
//		}
		StringEntity entity = new StringEntity(gson.toJson(map),"UTF-8");
		return entity;
	}
}
