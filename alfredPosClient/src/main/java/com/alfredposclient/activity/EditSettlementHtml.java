package com.alfredposclient.activity;

import com.alfredbase.BaseActivity;

public class EditSettlementHtml extends BaseActivity {
//	private String TAG = EditSettlementHtml.class.getSimpleName();
//	private WebView web;
//	private JavaConnectJS javaConnectJS;
//	private CloseOrderWindow closeOrderWindow;
//	private Order currentOrder;
//	private CloseOrderSplitWindow closeOrderSplitWindow;
//	private OrderSplit orderSplit;
//	private Gson gson = new Gson();
//	public static final int EDIT_SETTLEMENT_CLOSE_BILL = -110;
//	public static final String EDIT_ITEM_ACTION = "EDIT_ITEM_ACTION";
//	private VerifyDialog verifyDialog;
//
//
//	@Override
//	protected void initView() {
//		super.initView();
//		setContentView(R.layout.activity_common_web);
//		web = (WebView) findViewById(R.id.web);
//		WebViewConfig.setDefaultConfig(web);
//		javaConnectJS = new JavaConnectJS() {
//			@Override
//			@JavascriptInterface
//			public void send(String action, String param) {
//
//				if (!TextUtils.isEmpty(action)) {
//					if (JavaConnectJS.LOAD_SETTLEMENT_LIST.endsWith(action)) {
//						mHandler.sendMessage(mHandler.obtainMessage(
//								JavaConnectJS.ACTION_LOAD_SETTLEMENT_LIST,
//								param));
//					}
//					if (!ButtonClickTimer.canClick(web)) {
//						return;
//					}
//					if (JavaConnectJS.CLICK_BACK.equals(action)) {
//						mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
//					}
//					if (JavaConnectJS.CLICK_EDIT_SETTLEMENT.equals(action)) {
//						mHandler.sendMessage(mHandler.obtainMessage(
//								JavaConnectJS.ACTION_CLICK_EDIT_SETTLEMENT,
//								param));
//					}
//				}
//			}
//		};
//		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
//		if (SystemUtil.isZh(context))
//			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "editSettlement_zh.html");
//		else
//			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "editSettlement.html");
//
//		closeOrderWindow = new CloseOrderWindow(this,
//				findViewById(R.id.rl_root), mHandler);
//		closeOrderSplitWindow = new CloseOrderSplitWindow(this,
//				findViewById(R.id.rl_root), mHandler);
//		verifyDialog = new VerifyDialog(context, mHandler);
//	}
//
//	@Override
//	public void handlerClickEvent(View v) {
//		super.handlerClickEvent(v);
//	}
//
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case JavaConnectJS.ACTION_CLICK_BACK:
//				EditSettlementHtml.this.finish();
//				break;
//			case JavaConnectJS.ACTION_LOAD_SETTLEMENT_LIST: {
//				String param = (String) msg.obj;
//				web.loadUrl("javascript:JsConnectAndroid('"
//						+ JSONUtil.getJSCallBackName(param) + "','"
//						+ getSettlementListStr() + "')");
//				LogUtil.i(
//						TAG,
//						"javascript:JsConnectAndroid('"
//								+ JSONUtil.getJSCallBackName(param) + "','"
//								+ getSettlementListStr() + "')");
//			}
//				break;
//			case JavaConnectJS.ACTION_CLICK_EDIT_SETTLEMENT: {
//				verifyDialog.show(EDIT_ITEM_ACTION, msg.obj);
//			}
//				break;
//
//			case MainPage.VIEW_EVENT_CLOSE_BILL: {
//				Intent intentCloseBill = new Intent();
//				HashMap<String, String> map = (HashMap<String, String>) msg.obj;
//				// BigDecimal cash_num = map.get("cash_num");
//				// BigDecimal change_num = map.get("change_num");
//				// BigDecimal payment_amount = map.get("payment_amount");
//				// PrinterDetail printerDetailCloseBill = null;
//				// printerDetailCloseBill =
//				// ObjectFactory.getInstance().getPrinterDetailForCloseBill(cash_num,
//				// change_num, payment_amount,
//				// currentOrder,App.instance.getUser().getUserName());
//				// intentCloseBill.addCategory(Intent.CATEGORY_LAUNCHER);
//				// ComponentName cnCloseBill = new ComponentName(
//				// "com.alfredprint",
//				// "com.alfredprint.com.alfredselfhelp.activity.PrintMainActivity");
//
//				// intentCloseBill.putExtra("PrinterTitle",
//				// ObjectFactory.getInstance().getPrinterTitle(App.instance.getRevenueCenter().getId(),
//				// currentOrder.getOrderNo(),
//				// App.instance.getUser().getUserName(),
//				// table.getTableName()));
//				// intentCloseBill.putExtra("order", currentOrder);
//				// intentCloseBill.putExtra("printOrderItemList",
//				// ObjectFactory.getInstance().getItemList(OrderDetailSQL.getOrderDetails(currentOrder)));
//				// intentCloseBill.putExtra("taxMap",
//				// OrderDetailTaxSQL.getTaxPriceSUM(currentOrder));
//				// intentCloseBill.putExtra("paymentMap", map);
//				// intentCloseBill.setComponent(cnCloseBill);
//				// startActivity(intentCloseBill);
//
//				Tables table = CoreData.getInstance().getTables(
//						currentOrder.getTableId());
//				PrinterTitle title = ObjectFactory.getInstance()
//						.getPrinterTitle(
//								App.instance.getRevenueCenter(),
//								currentOrder,
//								App.instance.getUser().getFirstName()
//										+ App.instance.getUser().getLastName(),
//								table.getTableName());
//				// ArrayList<OrderModifier> orderModifiers =
//				// OrderModifierSQL.getAllOrderModifierByOrderAndNormal(currentOrder);
//				ArrayList<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(currentOrder
//						.getId());
//				ArrayList<PrintOrderItem> printOrderItems = ObjectFactory.getInstance().getItemList(orderDetails);
//				ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
//						.getInstance().getItemModifierList(currentOrder, orderDetails);
////
////				App.instance.remoteBillPrint(
////						App.instance.getCahierPrinter(),
////						title,
////						currentOrder,
////						ObjectFactory.getInstance().getItemList(
////								OrderDetailSQL.getOrderDetails(currentOrder
////										.getId())), orderModifiers,
////						OrderDetailTaxSQL.getTaxPriceSUM(currentOrder), map);
//				OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
//						currentOrder, App.instance.getRevenueCenter());
//				RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderAndBill(currentOrder, orderBill);
//				App.instance.remoteBillPrint(
//						App.instance.getCahierPrinter(),
//						title,
//						currentOrder,
//						printOrderItems, orderModifiers,
//										OrderDetailTaxSQL.getTaxPriceSUM(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), currentOrder), PaymentSettlementSQL
//						.getAllPaymentSettlementByPaymentId(Integer.valueOf(map.get("paymentId"))), roundAmount);
//				/**
//				 * 给后台发送log 信息
//				 */
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						CloudSyncJobManager cloudSync = App.instance.getSyncJob();
//						if (cloudSync!=null) {
//							int currCount = SyncMsgSQL.getSyncMsgCurrCountByOrderId(currentOrder.getId());
//							cloudSync.syncOrderInfoForLog(currentOrder.getId(),
//										App.instance.getRevenueCenter().getId(),
//										App.instance.getBusinessDate(), currCount + 1);
//
//						}
//					}
//				}).start();
//				break;
//			}
//			case MainPage.VIEW_EVENT_CLOSE_SPLIT_BILL:{
//
//
//				HashMap<String, String> paymentMap = (HashMap<String, String>) msg.obj;
//				List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL
//						.getAllPaymentSettlementByPaymentId(Integer.valueOf(paymentMap.get("paymentId")));
////				KotSummary kotSummary = KotSummarySQL.getKotSummary(currentOrder.getId());
////				if(kotSummary != null){
////					kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
////					KotSummarySQL.update(kotSummary);
////				}
//				Tables table = CoreData.getInstance().getTables(
//						orderSplit.getTableId());
//
//				OrderBill orderBill = ObjectFactory.getInstance().getOrderBillByOrderSplit(orderSplit, App.instance.getRevenueCenter());
////				PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
////						context);
////				printerLoadingDialog.setTitle("Receipt is Printing");
////				printerLoadingDialog.showByTime(3000);
//				PrinterDevice printer = App.instance.getCahierPrinter();
//				PrinterTitle title = ObjectFactory.getInstance()
//						.getPrinterTitleByOrderSplit(
//								App.instance.getRevenueCenter(),
//								currentOrder,
//								orderSplit,
//								App.instance.getUser().getFirstName()
//										+ App.instance.getUser().getLastName(),
//										table.getTableName(), orderBill,App.instance.getBusinessDate().toString());
//				ArrayList<OrderDetail> orderSplitDetails = (ArrayList<OrderDetail>) OrderDetailSQL.getOrderDetailsByOrderAndOrderSplit(orderSplit);
//
//
//				ArrayList<PrintOrderItem> orderItems = ObjectFactory
//						.getInstance().getItemList(orderSplitDetails);
//				List<Map<String, String>> taxMap = OrderDetailTaxSQL
//						.getOrderSplitTaxPriceSUM(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), orderSplit);
//
//				ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
//						.getInstance().getItemModifierList(OrderSQL.getOrder(orderSplit.getOrderId()), orderSplitDetails);
//				RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(orderSplit, orderBill);
//				// ArrayList<OrderModifier> orderModifiers =
//				// OrderModifierSQL.getAllOrderModifierByOrderAndNormal(currentOrder);
//				Order temporaryOrder = new Order();
//				temporaryOrder.setPersons(orderSplit.getPersons());
//				temporaryOrder.setSubTotal(orderSplit.getSubTotal());
//				temporaryOrder.setDiscountAmount(orderSplit.getDiscountAmount());
//				temporaryOrder.setTotal(orderSplit.getTotal());
//				temporaryOrder.setTaxAmount(orderSplit.getTaxAmount());
//				temporaryOrder.setOrderNo(currentOrder.getOrderNo());
//				if (orderItems.size() > 0 && printer != null) {
//					App.instance.remoteBillPrint(printer, title, temporaryOrder,
//							orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount);
//				}
//				// remove get bill notification
//				/**
//				 * 给后台发送log 信息
//				 */
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						CloudSyncJobManager cloudSync = App.instance.getSyncJob();
//						if (cloudSync!=null) {
//							int currCount = SyncMsgSQL.getSyncMsgCurrCountByOrderId(orderSplit.getOrderId());
//							cloudSync.syncOrderInfoForLog(orderSplit.getOrderId(),
//										App.instance.getRevenueCenter().getId(),
//										App.instance.getBusinessDate(), currCount + 1);
//
//						}
//					}
//				}).start();
//
//			}
//
//				break;
//			case EDIT_SETTLEMENT_CLOSE_BILL: {
//				Map<String,List<Map<String, Object>>> map = (Map<String, List<Map<String, Object>>>) msg.obj;
//				List<Map<String, Object>> oldPaymentMapList = map.get("oldPaymentMapList");
//				List<Map<String, Object>> newPaymentMapList = map.get("newPaymentMapList");
//				// 旧的支付方式先储存到数据库中
//				for (Map<String, Object> paymentMap : oldPaymentMapList) {
//					PaymentSettlement paymentSettlement = (PaymentSettlement) paymentMap
//							.get("paymentSettlement");
//					switch (paymentSettlement.getPaymentTypeId()) {
//					case ParamConst.SETTLEMENT_TYPE_CASH:
//						OrderBill orderBill = OrderBillSQL.getOrderBillByBillNo(paymentSettlement.getBillNo().intValue());
//						if(orderBill.getType().intValue() == ParamConst.BILL_TYPE_UN_SPLIT){
//							Order order = OrderSQL.getOrder(orderBill.getOrderId().intValue());
//							RoundAmount round = RoundAmountSQL.getRoundAmount(order) ;
//							if(round != null){
//								round.setRoundBalancePrice(Double.parseDouble(BH.sub(BH.getBD(round.getRoundAlfterPrice()),BH.getBD(round.getRoundBeforePrice()), true).toString()));
//								RoundAmountSQL.update(round);
//								OrderHelper.setOrderTotalAlfterRound(order, round);
//								OrderSQL.update(order);
//							}
//						}else{
//							OrderSplit orderSplit = OrderSplitSQL.get(orderBill.getOrderSplitId().intValue());
//							RoundAmount round = RoundAmountSQL.getRoundAmount(orderSplit);
//							if(round != null){
//								round.setRoundBalancePrice(Double.parseDouble(BH.sub(BH.getBD(round.getRoundAlfterPrice()),BH.getBD(round.getRoundBeforePrice()), true).toString()));
//								RoundAmountSQL.update(round);
//								OrderHelper.setOrderSplitTotalAlfterRound(orderSplit, round);
//								OrderSplitSQL.update(orderSplit);
//							}
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
//					case ParamConst.SETTLEMENT_TYPE_UNIPAY:
//					case ParamConst.SETTLEMENT_TYPE_VISA:
//					case ParamConst.SETTLEMENT_TYPE_AMEX:
//					case ParamConst.SETTLEMENT_TYPE_JCB:
//						CardsSettlement oldCardsSettlement = (CardsSettlement) paymentMap
//								.get("subPaymentSettlement");
//						if (oldCardsSettlement != null) {
//							oldCardsSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//							CardsSettlementSQL
//									.addCardsSettlement(oldCardsSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD:
//						BohHoldSettlement oldBohHoldSettlement = (BohHoldSettlement) paymentMap
//								.get("subPaymentSettlement");
//						if (oldBohHoldSettlement != null) {
//							oldBohHoldSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//							BohHoldSettlementSQL
//									.addBohHoldSettlement(oldBohHoldSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT:
//						NonChargableSettlement oldNonChargableSettlement = (NonChargableSettlement) paymentMap
//								.get("subPaymentSettlement");
//						if (oldNonChargableSettlement != null) {
//							oldNonChargableSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//							NonChargableSettlementSQL
//									.addNonChargableSettlement(oldNonChargableSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_VOID:
//						VoidSettlement oldVoidSettlement = (VoidSettlement) paymentMap
//								.get("subPaymentSettlement");
//						if (oldVoidSettlement != null) {
//							oldVoidSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//							VoidSettlementSQL
//									.addVoidSettlement(oldVoidSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_NETS:
//						NetsSettlement oldNetsSettlement = (NetsSettlement) paymentMap
//								.get("subPaymentSettlement");
//						if (oldNetsSettlement != null) {
//							oldNetsSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//							NetsSettlementSQL
//									.addNetsSettlement(oldNetsSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_ALIPAY:
//						AlipaySettlement oldAlipaySettlement = (AlipaySettlement) paymentMap
//								.get("subPaymentSettlement");
//						if (oldAlipaySettlement != null) {
//							oldAlipaySettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//							AlipaySettlementSQL
//									.addAlipaySettlement(oldAlipaySettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_EZLINK:
//						WeixinSettlement oldWeixinSettlement = (WeixinSettlement) paymentMap
//								.get("subPaymentSettlement");
//						if (oldWeixinSettlement != null) {
//							oldWeixinSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//							WeixinSettlementSQL
//									.addWeixinSettlement(oldWeixinSettlement);
//						}
//						break;
//					default:
//							break;
//					}
//					paymentSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//					PaymentSettlementSQL
//							.addPaymentSettlement(paymentSettlement);
//				}
//				// 新支付方式再删除
//				for (Map<String, Object> newPaymentMap : newPaymentMapList) {
//					PaymentSettlement paymentSettlement = (PaymentSettlement) newPaymentMap
//							.get("newPaymentSettlement");
//					switch (paymentSettlement.getPaymentTypeId()) {
//					case ParamConst.SETTLEMENT_TYPE_AMEX:
//					case ParamConst.SETTLEMENT_TYPE_JCB:
//					case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
//					case ParamConst.SETTLEMENT_TYPE_UNIPAY:
//					case ParamConst.SETTLEMENT_TYPE_VISA:
//						CardsSettlement newCardsSettlement = (CardsSettlement) newPaymentMap
//								.get("newSubPaymentSettlement");
//						if (newCardsSettlement != null) {
//							CardsSettlementSQL
//									.deleteCardsSettlement(newCardsSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD:
//						BohHoldSettlement newBohHoldSettlement = (BohHoldSettlement) newPaymentMap
//								.get("newSubPaymentSettlement");
//						if (newBohHoldSettlement != null) {
//							BohHoldSettlementSQL
//									.deleteBohHoldSettlement(newBohHoldSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT:
//						NonChargableSettlement newNonChargableSettlement = (NonChargableSettlement) newPaymentMap
//								.get("newSubPaymentSettlement");
//						if (newNonChargableSettlement != null) {
//							NonChargableSettlementSQL
//									.deleteNonChargableSettlement(newNonChargableSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_VOID:
//						VoidSettlement newVoidSettlement = (VoidSettlement) newPaymentMap
//								.get("newSubPaymentSettlement");
//						if (newVoidSettlement != null) {
//							VoidSettlementSQL
//									.deleteVoidSettlement(newVoidSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_NETS:
//						NetsSettlement newNetsSettlement = (NetsSettlement) newPaymentMap
//								.get("newSubPaymentSettlement");
//						if (newNetsSettlement != null) {
//							NetsSettlementSQL
//									.deleteNetsSettlement(newNetsSettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_ALIPAY:
//						AlipaySettlement newAlipaySettlement = (AlipaySettlement) newPaymentMap
//								.get("newSubPaymentSettlement");
//						if (newAlipaySettlement != null) {
//							AlipaySettlementSQL
//									.deleteAlipaySettlement(newAlipaySettlement);
//						}
//						break;
//					case ParamConst.SETTLEMENT_TYPE_EZLINK:
//						WeixinSettlement newWeixinSettlement = (WeixinSettlement) newPaymentMap
//								.get("newSubPaymentSettlement");
//						if (newWeixinSettlement != null) {
//							WeixinSettlementSQL
//									.deleteWeixinSettlement(newWeixinSettlement);
//						}
//						break;
//					default:
//						break;
//					}
//					PaymentSettlementSQL
//							.deletePaymentSettlement(paymentSettlement);
//
//				}
//
//
//
//
////				PaymentSettlement oldCashPaymentSettlement = (PaymentSettlement) map
////						.get("cashPaymentSettlement");
////				if (oldCashPaymentSettlement != null) {
////					PaymentSettlement newCashPaymentSettlement = (PaymentSettlement) map
////							.get("newCashPaymentSettlement");
////					PaymentSettlementSQL
////							.addPaymentSettlement(oldCashPaymentSettlement);
////					if (newCashPaymentSettlement != null) {
////						PaymentSettlementSQL
////								.deletePaymentSettlement(newCashPaymentSettlement);
////					}
////				}
////				PaymentSettlement oldOtherPaymentSettlement = (PaymentSettlement) map
////						.get("otherPaymentSettlement");
////				if (oldOtherPaymentSettlement != null) {
////
////					switch (oldOtherPaymentSettlement.getPaymentTypeId()) {
////					case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
////					case ParamConst.SETTLEMENT_TYPE_UNIPAY:
////					case ParamConst.SETTLEMENT_TYPE_VISA:
////					case ParamConst.SETTLEMENT_TYPE_AMEX:
////					case ParamConst.SETTLEMENT_TYPE_JCB:
////						CardsSettlement oldCardsSettlement = (CardsSettlement) map
////								.get("subOtherPaymentSettlement");
////						if (oldCardsSettlement != null) {
////							CardsSettlementSQL
////									.addCardsSettlement(oldCardsSettlement);
////						}
////						break;
////					case ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD:
////						BohHoldSettlement oldBohHoldSettlement = (BohHoldSettlement) map
////								.get("subOtherPaymentSettlement");
////						if (oldBohHoldSettlement != null) {
////							BohHoldSettlementSQL
////									.addBohHoldSettlement(oldBohHoldSettlement);
////						}
////						break;
////					case ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT:
////						NonChargableSettlement oldNonChargableSettlement = (NonChargableSettlement) map
////								.get("subOtherPaymentSettlement");
////						if (oldNonChargableSettlement != null) {
////							NonChargableSettlementSQL
////									.addNonChargableSettlement(oldNonChargableSettlement);
////						}
////						break;
////					case ParamConst.SETTLEMENT_TYPE_VOID:
////						VoidSettlement oldVoidSettlement = (VoidSettlement) map
////								.get("subOtherPaymentSettlement");
////						if (oldVoidSettlement != null) {
////							VoidSettlementSQL
////									.addVoidSettlement(oldVoidSettlement);
////						}
////						break;
////					case ParamConst.SETTLEMENT_TYPE_NETS:
////						NetsSettlement oldNetsSettlement = (NetsSettlement) map
////								.get("subOtherPaymentSettlement");
////						if (oldNetsSettlement != null) {
////							NetsSettlementSQL
////									.addNetsSettlement(oldNetsSettlement);
////						}
////						break;
////					default:
////						break;
////					}
////					PaymentSettlementSQL
////							.addPaymentSettlement(oldOtherPaymentSettlement);
////
////					PaymentSettlement newOtherPaymentSettlement = (PaymentSettlement) map
////							.get("newOtherPaymentSettlement");
////					if (newOtherPaymentSettlement != null) {
////						switch (newOtherPaymentSettlement.getPaymentTypeId()) {
////						case ParamConst.SETTLEMENT_TYPE_AMEX:
////						case ParamConst.SETTLEMENT_TYPE_JCB:
////						case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
////						case ParamConst.SETTLEMENT_TYPE_UNIPAY:
////						case ParamConst.SETTLEMENT_TYPE_VISA:
////							CardsSettlement newCardsSettlement = (CardsSettlement) map
////									.get("subNewOtherPaymentSettlement");
////							if (newCardsSettlement != null) {
////								CardsSettlementSQL
////										.deleteCardsSettlement(newCardsSettlement);
////							}
////							break;
////						case ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD:
////							BohHoldSettlement newBohHoldSettlement = (BohHoldSettlement) map
////									.get("subNewOtherPaymentSettlement");
////							if (newBohHoldSettlement != null) {
////								BohHoldSettlementSQL
////										.deleteBohHoldSettlement(newBohHoldSettlement);
////							}
////							break;
////						case ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT:
////							NonChargableSettlement newNonChargableSettlement = (NonChargableSettlement) map
////									.get("subNewOtherPaymentSettlement");
////							if (newNonChargableSettlement != null) {
////								NonChargableSettlementSQL
////										.deleteNonChargableSettlement(newNonChargableSettlement);
////							}
////							break;
////						case ParamConst.SETTLEMENT_TYPE_VOID:
////							VoidSettlement newVoidSettlement = (VoidSettlement) map
////									.get("subNewOtherPaymentSettlement");
////							if (newVoidSettlement != null) {
////								VoidSettlementSQL
////										.deleteVoidSettlement(newVoidSettlement);
////							}
////							break;
////						case ParamConst.SETTLEMENT_TYPE_NETS:
////							NetsSettlement newNetsSettlement = (NetsSettlement) map
////									.get("subNewOtherPaymentSettlement");
////							if (newNetsSettlement != null) {
////								NetsSettlementSQL
////										.deleteNetsSettlement(newNetsSettlement);
////							}
////							break;
////						default:
////							break;
////						}
////						PaymentSettlementSQL
////								.deletePaymentSettlement(newOtherPaymentSettlement);
////					}
//
////				}
//
//			}
//				break;
//			case MainPage.VIEW_EVENT_SHOW_BILL_ON_HOLD:
//				verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_BILL_ON_HOLD, null);
//				break;
//			case MainPage.VIEW_EVENT_SHOW_VOID:
//				verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_VOID, null);
//				break;
//			case MainPage.VIEW_EVENT_SHOW_ENTERTAINMENT:
//				verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_ENTERTAINMENT, null);
//				break;
//			case VerifyDialog.DIALOG_RESPONSE:{
//				Map<String, Object> result = (Map<String, Object>) msg.obj;
//				User user = (User) result.get("User");
//				if (result.get("MsgObject").equals(
//						MainPage.HANDLER_MSG_OBJECT_BILL_ON_HOLD)) {
//					if (!verifyDialog.isShowing()) {
//						if(closeOrderWindow.isShowing()){
//							closeOrderWindow.setUser(user);
//							closeOrderWindow.openMoneyKeyboard(View.GONE,
//									ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD);
//						}else if (closeOrderSplitWindow.isShowing()){
//							closeOrderSplitWindow.setUser(user);
//							closeOrderSplitWindow.openMoneyKeyboard(View.GONE,
//									ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD);
//						}
//					}
//				} else if (result.get("MsgObject").equals(
//						MainPage.HANDLER_MSG_OBJECT_VOID)) {
//					if (!verifyDialog.isShowing()) {
//						if(closeOrderWindow.isShowing()){
//							closeOrderWindow.setUser(user);
//							closeOrderWindow.openMoneyKeyboard(View.GONE,
//									ParamConst.SETTLEMENT_TYPE_VOID);
//						}else if(closeOrderSplitWindow.isShowing()){
//							closeOrderSplitWindow.setUser(user);
//							closeOrderSplitWindow.openMoneyKeyboard(View.GONE,
//									ParamConst.SETTLEMENT_TYPE_VOID);
//						}
//					}
//				} else if (result.get("MsgObject").equals(
//						MainPage.HANDLER_MSG_OBJECT_ENTERTAINMENT)) {
//					if (!verifyDialog.isShowing()) {
//						if(closeOrderWindow.isShowing()){
//							closeOrderWindow.setUser(user);
//							closeOrderWindow.openMoneyKeyboard(View.GONE,
//									ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT);
//						}else if(closeOrderSplitWindow.isShowing()){
//							closeOrderSplitWindow.setUser(user);
//							closeOrderSplitWindow.openMoneyKeyboard(View.GONE,
//									ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT);
//						}
//					}
//				}else if(result.get("MsgObject").equals(EDIT_ITEM_ACTION)){
//					if (!verifyDialog.isShowing()) {
//						closeOrderWindow.setUser(user);
//						closeOrderSplitWindow.setUser(user);
//						String param = (String) result.get("Object");
//						showCloseBillWindow(param);
//					}
//				}
//			}
//				break;
//			default:
//				break;
//			}
//		};
//	};
//
//	private String getSettlementListStr() {
//		List<Payment> payments = PaymentSQL.getTodayAllPaymentBySession(
//				App.instance.getSessionStatus(),
//				App.instance.getLastBusinessDate());
//		if (payments.isEmpty()) {
//			return "";
//		}
//		List<EditSettlementInfo> editSettlementInfos = new ArrayList<EditSettlementInfo>();
//		for (Payment payment : payments) {
//			Order order = OrderSQL.getOrder(payment.getOrderId());
//			Places place = PlacesSQL.getPlacesById(order.getPlaceId());
//			User user = UserSQL.getUserById(payment.getUserId());
//			EditSettlementInfo editSettlementInfo = new EditSettlementInfo(
//					payment.getId(), payment.getOrderId(), payment.getBillNo(),
//					payment.getPaymentAmount(), place.getPlaceName(),
//					order.getTableId(), TimeUtil.getTimeFormat(payment
//							.getCreateTime()), user.getFirstName()
//							+ user.getLastName(), payment.getType());
//			editSettlementInfos.add(editSettlementInfo);
//		}
//		String str = gson.toJson(editSettlementInfos);
//		LogUtil.i(TAG, str);
//		return JSONUtil.getJSONFromEncode(str);
//	}
//
//	private void showCloseBillWindow(String param) {
//		EditSettlementInfo editSettlementInfo = null;
//		JSONObject jsonObject;
//		try {
//			jsonObject = new JSONObject(param);
//			editSettlementInfo = gson.fromJson(
//					jsonObject.getString("settlement"),
//					new TypeToken<EditSettlementInfo>() {
//					}.getType());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		if (editSettlementInfo == null) {
//			return;
//		}
//		switch (editSettlementInfo.getType().intValue()) {
//		case ParamConst.BILL_TYPE_UN_SPLIT:
//			currentOrder = OrderSQL.getOrder(editSettlementInfo.getOrderId());
//			OrderBill orderBill = OrderBillSQL.getOrderBillByOrder(currentOrder);
//			closeOrderWindow.show(currentOrder,250.0f, orderBill);
//			break;
//		case ParamConst.BILL_TYPE_SPLIT:
//			Payment payment = PaymentSQL.getPayment(editSettlementInfo.getPaymentId());
//			if(payment.getOrderSplitId() != null && payment.getOrderSplitId().intValue() != 0){
//				orderSplit = OrderSplitSQL.get(payment.getOrderSplitId());
//				closeOrderSplitWindow.show(OrderSQL.getOrder(payment.getOrderId()), orderSplit);
//			}
//			break;
//		default:
//			break;
//		}
//
//	}
	
}
