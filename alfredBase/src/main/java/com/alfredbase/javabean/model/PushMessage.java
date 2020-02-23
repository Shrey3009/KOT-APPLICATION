package com.alfredbase.javabean.model;

import com.google.gson.Gson;

public class PushMessage {
	public final static String HAPPY_HOURS = "happyhour";

	public final static String PRINTER = "printer";

	public final static String ITEM = "item";

	public final static String MODIFIER = "modifier";

	public final static String USER = "user";

	public final static String RESTAURANT = "restaurant";

	public final static String PLACE_TABLE = "place_table";

	public final static String TAX = "tax";

	public final static String PAYMENT_METHOD = "payment_method";
	
	public final static String REST_CONFIG = "rest_config";
	
	public final static String PUSH_ORDER = "push_order";
	
	public final static String ALIPAY_RESULT = "alipay_result";
	
	public final static String THIRDPARTYPAY_RESULT = "thirdpartyPay_result";

	public final static String RE_SYNC_DATA_BY_BUSINESS_DATE = "RE_SYNC_DATA_BY_BUSINESS_DATE";

	public final static String REAL_TIME_REPORT = "real_time_report";
	public final static String STOCK = "stock";
	public final static  String PROMOTION="rest_promotion";
	
	public final static int MESSAGE_TYPE_HEART_BEAT = -2;
	public final static int MESSAGE_TYPE_REGISTER = 0;
	public final static int MESSAGE_TYPE_UPDATE=1;
	
	public final static String CONTENT_DELIMITER = ",";
	
	private String push; // 事件类型
	private Integer restId;
	private Integer revenueId;
	private String content; // json 数据
	private String businessStr;

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	private Long  sendTime;


	public PushMessage() {
	}

	public PushMessage(int type, String push) {
		this.push = push;
	}

	public String getMsg() {
		return push;
	}

	public void setMsg(String push) {
		this.push = push;
	}

	
	public Integer getRestId() {
		return restId;
	}

	
	public void setRestId(Integer restId) {
		this.restId = restId;
	}

	
	public Integer getRevenueId() {
		return revenueId;
	}

	
	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}



	public static PushMessage deserializeList(String json){
			return new Gson().fromJson(json, PushMessage.class);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBusinessStr() {
		return businessStr;
	}

	public void setBusinessStr(String businessStr) {
		this.businessStr = businessStr;
	}

	@Override
	public String toString() {
		return "PushMessage{" +
				"push='" + push + '\'' +
				", restId=" + restId +
				", revenueId=" + revenueId +
				", content='" + content + '\'' +
				", businessStr='" + businessStr + '\'' +
				", sendTime=" + sendTime +
				'}';
	}
}