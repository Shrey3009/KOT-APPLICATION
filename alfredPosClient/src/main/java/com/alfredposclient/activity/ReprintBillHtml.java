package com.alfredposclient.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.model.PrintBill;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.SystemUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.WebViewConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReprintBillHtml extends BaseActivity {
	private WebView web;
	private JavaConnectJS javaConnectJS;
	private Gson gosn = new Gson();
	private ArrayList<Order> orders;
	@SuppressLint("JavascriptInterface")
	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_common_web);
		web = (WebView) findViewById(R.id.web);
		WebViewConfig.setDefaultConfig(web);
		javaConnectJS = new JavaConnectJS() {
			@Override
			@JavascriptInterface
			public void send(String action, String param) {

				if (!TextUtils.isEmpty(action)) {
					if (JavaConnectJS.LOAD_BILL_LIST.equals(action)) {
						mHandler.sendMessage(mHandler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_BILL_LIST, param));
					} else {
						if (!ButtonClickTimer.canClick(web)) {
							return;
						}
						if (action.endsWith(JavaConnectJS.CLICK_BACK)) {
							mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
						}
						if(JavaConnectJS.LOAD_BILL_DETAILS.equals(action)){
							mHandler.sendMessage(mHandler.obtainMessage(JavaConnectJS.ACTION_LOAD_BILL_DETAILS, param));
						}
						if(JavaConnectJS.CLICK_PRINT_BILL.equals(action)){
							mHandler.sendMessage(mHandler.obtainMessage(JavaConnectJS.ACTION_CLICK_PRINT_BILL, param));
						}
					}
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		if (SystemUtil.isZh(context)) {
			if (App.instance.isRevenueKiosk())
				web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "reprintBill_zh_kiosk.html");
			else
			    web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "reprintBill_zh.html");
		}else {
			if (App.instance.isRevenueKiosk())
				web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "reprintBill_kiosk.html");
			else
				web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "reprintBill.html");
		}
	}

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_CLICK_BACK:
				ReprintBillHtml.this.finish();
				break;
			case JavaConnectJS.ACTION_LOAD_BILL_LIST:{
				String param = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(param) + "','"
						+ getBillListStr() + "')");
				}
				break;
			case JavaConnectJS.ACTION_LOAD_BILL_DETAILS:{
				String param = (String) msg.obj;
				web.loadUrl("javascript:JsConnectAndroid('"
						+ JSONUtil.getJSCallBackName(param) + "','"
						+ getBillDetailStr(param) + "')");
				}
				break;
			case JavaConnectJS.ACTION_CLICK_PRINT_BILL:{
				String param = (String) msg.obj;
				printBillAction(param);
			}
				break;
			default:
				break;
			}
		};
	};
	
	private String getBillListStr(){
		orders = OrderSQL.getAllFinishedOrders();
		ArrayList<PrintBill> printBills = new ArrayList<PrintBill>();
		if (orders != null && !orders.isEmpty()) {
			for (Order order : orders) {
				//show Bill number instead of order ID
				OrderBill orderBill = OrderBillSQL.getOrderBillByOrder(order);
				if(orderBill == null){
					continue;
				}
				TableInfo tbl = TableInfoSQL.getTableById(order.getTableId());
				printBills.add(new PrintBill(orderBill.getBillNo(),
						PlaceInfoSQL.getPlaceInfoById(order.getPlaceId()).getPlaceName(),
								tbl.getName(),
								order.getId(), 
								order.getTotal(), 
								App.instance.getUser().getUserName()));
			}
			String str = gosn.toJson(printBills);
			return JSONUtil.getJSONFromEncode(str);
		}else{
			return "";
		}
	}
	
	private String getBillDetailStr(String json){
		org.json.JSONObject jsonObject;
		PrintBill printBill = null;
		try {
			jsonObject = new JSONObject(json);
			printBill = gosn.fromJson(jsonObject.getString("Bill"),
					new TypeToken<PrintBill>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(printBill == null){
			return "";
		}
		ArrayList<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(OrderSQL.getOrder(printBill.getOrderNo()).getId());
		ArrayList<PrintOrderItem> printOrderItems = ObjectFactory.getInstance().getItemList(orderDetails);
		String str = gosn.toJson(printOrderItems);
		return JSONUtil.getJSONFromEncode(str);
	}
	private void printBillAction(String json){
		org.json.JSONObject jsonObject;
		PrintBill printBill = null;
		try {
			jsonObject = new JSONObject(json);
			printBill = gosn.fromJson(jsonObject.getString("Bill"),
					new TypeToken<PrintBill>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(printBill == null){
			return;
		}
		//Order No data in bill is Order ID
		Order order = OrderSQL.getOrder(printBill.getOrderNo());
		if(order == null){
			return;
		}
		int orderDetailCount = OrderDetailSQL.getOrderDetailCountByGroupId(
				ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID, order.getId());
		
		PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
				context);
		printerLoadingDialog.setTitle(context.getResources().getString(R.string.bill_printing));
		printerLoadingDialog.showByTime(3000);
		PrinterDevice printer = App.instance.getCahierPrinter();
		if (orderDetailCount != 0) {
			ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
					.getInstance().getItemModifierList(order, OrderDetailSQL.getOrderDetails(order.getId()));
			OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
					order, App.instance.getRevenueCenter());
			RoundAmount roundAmount = RoundAmountSQL
					.getRoundAmountByOrderAndBill(order, orderBill);
			App.instance.remoteBillPrint(
					printer,
					ObjectFactory.getInstance().getPrinterTitle(
							App.instance.getRevenueCenter(),
							order,
							App.instance.getUser().getFirstName()
									+ App.instance.getUser().getLastName(),
							TableInfoSQL.getTableById(order.getTableId())
									.getName(), 1,App.instance.getSystemSettings().getTrainType()),
					order,
					ObjectFactory.getInstance().getItemList(
							OrderDetailSQL.getOrderDetails(order.getId())),
					orderModifiers, OrderDetailTaxSQL.getTaxPriceSUMForPrint(
							App.instance.getLocalRestaurantConfig()
									.getIncludedTax().getTax(), order), null,
					roundAmount,null);
		} else {
			List<OrderSplit> orderSplits = OrderSplitSQL.getOrderSplits(order);
			for (OrderSplit orderSplit : orderSplits) {
				OrderBill orderBill = ObjectFactory.getInstance()
						.getOrderBillByOrderSplit(orderSplit,
								App.instance.getRevenueCenter());
				ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) OrderDetailSQL
						.getOrderDetailsByOrderAndOrderSplit(orderSplit);
				if (orderDetails.isEmpty()) {
					continue;
				}
//				RoundAmount orderSplitRoundAmount = ObjectFactory.getInstance()
//						.getRoundAmountByOrderSplit(
//								orderSplit,
//								orderBill,
//								App.instance.getLocalRestaurantConfig()
//										.getRoundType(),
//								order.getBusinessDate());
//				OrderHelper.setOrderSplitTotalAlfterRound(orderSplit,
//						orderSplitRoundAmount);
				List<Map<String, String>> taxMap = OrderDetailTaxSQL
						.getOrderSplitTaxPriceSUMForPrint(App.instance
								.getLocalRestaurantConfig().getIncludedTax()
								.getTax(), orderSplit);
				ArrayList<PrintOrderItem> orderItems = ObjectFactory
						.getInstance().getItemList(orderDetails);

				PrinterTitle title = ObjectFactory.getInstance()
						.getPrinterTitleByOrderSplit(
								App.instance.getRevenueCenter(),
								order,
								orderSplit,
								App.instance.getUser().getFirstName()
										+ App.instance.getUser().getLastName(),
								TableInfoSQL.getTableById(orderSplit.getTableId())
										.getName(), orderBill, order.getBusinessDate().toString(), 1);
				orderSplit
						.setOrderStatus(ParamConst.ORDERSPLIT_ORDERSTATUS_UNPAY);
				OrderSplitSQL.update(orderSplit);
				ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
						.getInstance().getItemModifierListByOrderDetail(
								orderDetails);
				Order temporaryOrder = new Order();
				temporaryOrder.setPersons(orderSplit.getPersons());
				temporaryOrder.setSubTotal(orderSplit.getSubTotal());
				temporaryOrder
						.setDiscountAmount(orderSplit.getDiscountAmount());
				temporaryOrder.setTotal(orderSplit.getTotal());
				temporaryOrder.setTaxAmount(orderSplit.getTaxAmount());
				temporaryOrder.setOrderNo(order.getOrderNo());
				App.instance.remoteBillPrint(printer, title, temporaryOrder,
						orderItems, orderModifiers, taxMap, null,
						null,null);
			}
		}
	}
	
}
