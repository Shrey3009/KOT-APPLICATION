package com.alfredmenu.http.server;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.alfredbase.APPConfig;
import com.alfredbase.ParamConst;
import com.alfredbase.http.APIName;
import com.alfredbase.http.AlfredHttpServer;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredmenu.activity.EmployeeID;
import com.alfredmenu.activity.KOTNotification;
import com.alfredmenu.activity.MainPage;
import com.alfredmenu.global.App;
import com.alfredmenu.global.UIHelp;

import com.alfredmenu.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WaiterHttpServer extends AlfredHttpServer {

	public WaiterHttpServer() {
		super(APPConfig.WAITER_HTTP_SERVER_PORT);
	}

	@Override
	public Response doPost(String apiName, Method mothod,
			Map<String, String> params, String body) {

		Response resp;

		if (apiName == null) {
			resp = getNotFoundResponse();
		} else {
			if (validMessageFromConnectedPOS(body) != true)
				resp = invalidDeviceResponse();
			else {
				if (apiName.equals(APIName.KOT_NOTIFICATION)) {
					resp = kotNotification(body);
				} else if (apiName.equals(APIName.CLOSE_SESSION)) {
					resp = handlerSessionClose(body);
				} else if (apiName.equals(APIName.TRANSFER_TABLE)) {
					resp = handlerTransferTable(body);
				}  else if (apiName.equals(APIName.POS_LANGUAGE)) {
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
				}else {
					resp = getNotFoundResponse();
				}
			}
		}

		return resp;
	}
	
    private boolean validMessageFromConnectedPOS(String params) {
    	boolean ret = false;
    	try {
    		Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(params);
			MainPosInfo pos = gson.fromJson(
					jsonObject.optString("mainpos"), MainPosInfo.class);
			MainPosInfo connectedPos = App.instance.getMainPosInfo();
			
			//old POS version(<1.0.1) POS dont have mainpos object in request
			if (pos == null)
				return true;
			if (connectedPos != null &&
					pos.getRestId() ==  connectedPos.getRestId() 
					&& pos.getRevenueId() == connectedPos.getRevenueId()) {
				ret = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
	private Response kotNotification(String params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(params);
			int kotNotificationQty = jsonObject.optInt("total");
			App.instance.setKotNotificationQty(kotNotificationQty);
			if(!(App.getTopActivity() instanceof KOTNotification))
				App.instance.playVibration();
			App.getTopActivity().httpRequestAction(App.VIEW_EVENT_SET_QTY,
					null);// 在这边参数没有作用
		} catch (JSONException e) {
			e.printStackTrace();
		}
		result.put("resultCode", ResultCode.SUCCESS);
		Response resp = this.getJsonResponse(new Gson().toJson(result));
		return resp;
	}

	private Response handlerSessionClose(String params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(params);
			SessionStatus sessionStatus = gson.fromJson(
					jsonObject.optString("session"), SessionStatus.class);
			App.getTopActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					App.getTopActivity().showOneButtonCompelDialog(
							App.getTopActivity().getResources().getString(R.string.session_change), 
							App.getTopActivity().getResources().getString(R.string.relogin_pos),
							new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									OrderSQL.deleteAllOrder();
									OrderDetailSQL.deleteAllOrderDetail();
									OrderModifierSQL.deleteAllOrderModifier();
									OrderDetailTaxSQL.deleteAllOrderDetailTax();
									UIHelp.startEmployeeID(App.getTopActivity());
									App.instance.popAllActivityExceptOne(EmployeeID.class);
								}
							});
//					App.getTopActivity().showCompelDialog("Session Change",
//							"Select Yes To Clean Data Or/nClose APP", "No",
//							"Yes", new OnClickListener() {
//
//								@Override
//								public void onClick(View arg0) {
//									List<BaseActivity> activitys = App.instance.activitys;
//									if (!activitys.isEmpty()) {
//										for (int i = activitys.size() - 1; i >= 0; i--) {
//											activitys.get(i).finish();
//										}
//									}
//								}
//							}, new OnClickListener() {
//
//								@Override
//								public void onClick(View arg0) {
//									OrderSQL.deleteAllOrder();
//									OrderDetailSQL.deleteAllOrderDetail();
//									OrderModifierSQL.deleteAllOrderModifier();
//									OrderDetailTaxSQL.deleteAllOrderDetailTax();
//									App.instance.popAllActivityExceptOne(EmployeeID.class);
//								}
//							});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("resultCode", ResultCode.SUCCESS);
		Response resp = this.getJsonResponse(new Gson().toJson(result));
		return resp;
	}

	private Response handlerTransferTable(String params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(params);
			Order fromOrder = gson.fromJson(jsonObject.optString("fromOrder"),
					Order.class);
			String fromTableName = jsonObject.optString("fromTableName");
			String toTableName = jsonObject.optString("toTableName");
			String action = jsonObject.optString("action");
			Order order = OrderSQL.getOrder(fromOrder.getId());
			if (order != null) {
				if (ParamConst.JOB_TRANSFER_KOT.equals(action)) {
					OrderSQL.update(fromOrder);
					OrderSQL.deleteOrder(fromOrder);
				} else if (ParamConst.JOB_MERGER_KOT.equals(action)) {
					OrderDetailSQL.deleteOrderDetailByOrder(fromOrder);
					OrderModifierSQL.deleteOrderModifierByOrder(fromOrder);
					OrderSQL.deleteOrder(fromOrder);
				}
				App.instance.setFromTableName(fromTableName);
				App.instance.setToTableName(toTableName);
				App.instance.getTopActivity().httpRequestAction(MainPage.TRANSFER_TABLE_NOTIFICATION, order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("resultCode", ResultCode.SUCCESS);
		Response resp = this.getJsonResponse(new Gson().toJson(result));
		return resp;
	}


	
	private Response dummyResponse() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("resultCode", ResultCode.SUCCESS);
		Response resp = this.getJsonResponse(new Gson().toJson(result));
		return resp;
	}
	
	private Response invalidDeviceResponse() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("resultCode", ResultCode.INVALID_DEVICE);
		Response resp = this.getJsonResponse(new Gson().toJson(result));
		return resp;
	}
}
