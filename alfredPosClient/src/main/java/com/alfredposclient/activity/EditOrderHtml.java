package com.alfredposclient.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.javabeanforhtml.EditOrderInfo;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.OrderHelper;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.WebViewConfig;
import com.alfredposclient.popupwindow.ModifyQuantityWindow;
import com.alfredposclient.popupwindow.ModifyQuantityWindow.DismissCall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditOrderHtml extends BaseActivity {
	private String TAG = EditOrderHtml.class.getSimpleName();
	private WebView web;
	private JavaConnectJS javaConnectJS;
	private Gson gson = new Gson();
	private ModifyQuantityWindow modifyQuantityWindow;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_common_web);
		modifyQuantityWindow = new ModifyQuantityWindow(this,
				findViewById(R.id.rl_root));
		ButtonClickTimer.lastClickTime = 0;
		web = (WebView) findViewById(R.id.web);
		WebViewConfig.setDefaultConfig(web);
		javaConnectJS = new JavaConnectJS() {
			@Override
			@JavascriptInterface
			public void send(String action, String param) {
				LogUtil.i(action, param);
				if (!ButtonClickTimer.canClick(web)) {
					return;
				}
				if (!TextUtils.isEmpty(action)) {
					if (JavaConnectJS.CLICK_BACK.equals(action)) {
						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
					}
					if (JavaConnectJS.LOAD_ORDERS.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_ORDERS, param));
					}
					if (JavaConnectJS.CLICK_ALL.equals(action)) {
						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICL_ALL);
					}
					if (JavaConnectJS.CLICK_TODAY.equals(action)) {
						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_TODAY);
					}
					if (JavaConnectJS.LOAD_ORDER_DETAILS.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_ORDER_DETAILS, param));
					}
					if (JavaConnectJS.EDIT_ORDER_DETAILS.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_EDIT_ORDER_DETAILS, param));
					}
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "editOrder.html");
		getOrdersJsonStr();
	}

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_CLICK_BACK:
				EditOrderHtml.this.finish();
				break;
			case JavaConnectJS.ACTION_LOAD_ORDERS:
				String str = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(str) + "','"
						+ getOrdersJsonStr() + "')");
				LogUtil.i(TAG, "javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(str) + "','"
						+ getOrdersJsonStr() + "')");
				break;
			case JavaConnectJS.ACTION_LOAD_ORDER_DETAILS:
				String param = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(param) + "','"
						+ getOrderDetailsJson(param) + "')");
				break;
//			case JavaConnectJS.ACTION_CHANGE_QTY:
//				String json = (String) msg.obj;
//				ReplaceOrderDetailToSQL(json);
//				break;
			case JavaConnectJS.ACTION_EDIT_ORDER_DETAILS:
				String json1 = (String) msg.obj;

				break;
			default:
				break;
			}
		};
	};

	private String getOrdersJsonStr() {
		Map<String, Object> map = new HashMap<String, Object>();
		long nowTime = System.currentTimeMillis();
		List<Order> orders = OrderSQL.getUnpaidOrdersBySession(App.instance.getSessionStatus(),
				App.instance.getLastBusinessDate(), nowTime);
		List<EditOrderInfo> editOrders = new ArrayList<EditOrderInfo>();
		for (Order orderitem : orders) {
			PlaceInfo place = PlaceInfoSQL.getPlaceInfoById(orderitem.getPlaceId());
			User waiter = UserSQL.getUserById(orderitem.getUserId());
			TableInfo tab = TableInfoSQL.getTableById(orderitem.getTableId());
			EditOrderInfo mOrderInfo = new EditOrderInfo(orderitem.getOrderNo(), orderitem.getId(), 1,
					place.getPlaceName(), tab.getName(), waiter.getFirstName()+' '+waiter.getLastName());
		}
		String str = gson.toJson(editOrders);
		return JSONUtil.getJSONFromEncode(str);
	}

	private String getOrderDetailsJson(String json) {
		Order order = new Order();
		List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();
		List<ItemDetail> itemDetailsForOrderDetail = new ArrayList<ItemDetail>();
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			order = gson.fromJson(jsonObject.getString("order"),
					new TypeToken<Order>() {
					}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		orderDetails = OrderDetailSQL.getOrderDetails(order.getId());
		itemDetails = ItemDetailSQL.getAllItemDetail();
		for (OrderDetail mOrderDetail : orderDetails) {
			itemDetailsForOrderDetail.add(CoreData.getInstance()
					.getItemDetailById(mOrderDetail.getItemId(), mOrderDetail.getItemName()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OrderDetails", orderDetails);
		map.put("ItemDetail", itemDetailsForOrderDetail);
		String str = gson.toJson(map);
		System.err.println(str);
		return JSONUtil.getJSONFromEncode(str);
	}

	private void ReplaceOrderDetailToSQL(String json) {
		OrderDetail orderDetail = new OrderDetail();
		JSONObject jsonject;
		try {
			jsonject = new JSONObject(json);
			orderDetail = gson.fromJson(jsonject.getString("orderdetail"),
					new TypeToken<OrderDetail>() {
					}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// OrderDetailSQL.addOrderDetail(orderDetail);
		showModifyQuantityWindow(orderDetail);
	}

	private void showModifyQuantityWindow(final OrderDetail orderDetail) {
		modifyQuantityWindow.show(orderDetail.getItemNum(), new DismissCall() {

			@Override
			public void call(String key, int num) {
				if (num < 1) {
					OrderDetailSQL.deleteOrderDetail(orderDetail);
					OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
					if(!IntegerUtils.isEmptyOrZero(orderDetail.getOrderSplitId()) && ! IntegerUtils.isEmptyOrZero(orderDetail.getGroupId())){
						int count = OrderDetailSQL.getOrderDetailCountByGroupId(orderDetail.getGroupId().intValue(), orderDetail.getId().intValue());
						if(count == 0){
							OrderSplitSQL.deleteOrderSplitByOrderAndGroupId(orderDetail.getId().intValue(), orderDetail.getGroupId().intValue());
						}
					}
				} else if (num > 999) {
					orderDetail.setItemNum(999);
					OrderDetailSQL.updateOrderDetailAndOrder(orderDetail);
//					OrderModifierSQL.updateOrderModifierNum(orderDetail, 999);
					OrderHelper.setOrderModifierPirceAndNum(orderDetail, 999);
				} else {
					orderDetail.setItemNum(num);
					OrderDetailSQL.updateOrderDetailAndOrder(orderDetail);
//					OrderModifierSQL.updateOrderModifierNum(orderDetail, num);
					OrderHelper.setOrderModifierPirceAndNum(orderDetail, num);
				}
			}
		});
	}
}
