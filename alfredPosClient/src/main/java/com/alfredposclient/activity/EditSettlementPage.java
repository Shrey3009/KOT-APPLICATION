package com.alfredposclient.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.VerifyDialog;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.AlipaySettlement;
import com.alfredbase.javabean.BohHoldSettlement;
import com.alfredbase.javabean.CardsSettlement;
import com.alfredbase.javabean.NetsSettlement;
import com.alfredbase.javabean.NonChargableSettlement;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderPromotion;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.VoidSettlement;
import com.alfredbase.javabean.javabeanforhtml.EditSettlementInfo;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.sql.AlipaySettlementSQL;
import com.alfredbase.store.sql.BohHoldSettlementSQL;
import com.alfredbase.store.sql.CardsSettlementSQL;
import com.alfredbase.store.sql.NetsSettlementSQL;
import com.alfredbase.store.sql.NonChargableSettlementSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PaymentSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.PromotionDataSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.SyncMsgSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.store.sql.VoidSettlementSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.R;
import com.alfredposclient.adapter.EditSettlementAdapter;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.jobs.CloudSyncJobManager;
import com.alfredposclient.jobs.SubPosCloudSyncJobManager;
import com.alfredposclient.popupwindow.CloseOrderSplitWindow;
import com.alfredposclient.popupwindow.CloseOrderWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSettlementPage extends BaseActivity {
    private String TAG = EditSettlementPage.class.getSimpleName();
    private CloseOrderWindow closeOrderWindow;
    private Order currentOrder;
    private CloseOrderSplitWindow closeOrderSplitWindow;
    private OrderSplit orderSplit;
//    private Gson gson = new Gson();
    public static final int EDIT_SETTLEMENT_CLOSE_BILL = -110;
    public static final String EDIT_ITEM_ACTION = "EDIT_ITEM_ACTION";
    private VerifyDialog verifyDialog;
    private View view_top_line;
    EditSettlementAdapter editSettlementAdapter;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.edit_settlement_page);
        closeOrderWindow = new CloseOrderWindow(this,
                findViewById(R.id.rl_root), mHandler);
        closeOrderSplitWindow = new CloseOrderSplitWindow(this,
                findViewById(R.id.rl_root), mHandler);
        verifyDialog = new VerifyDialog(context, mHandler);
        view_top_line = findViewById(R.id.view_top_line);
        editSettlementAdapter = new EditSettlementAdapter(context, getSettlementList(), verifyDialog);
        ListView lv_payment = (ListView) findViewById(R.id.lv_payment);
        findViewById(R.id.ll_print).setVisibility(View.GONE);
        lv_payment.setAdapter(editSettlementAdapter);
        lv_payment.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                EditSettlementInfo editSettlementInfo = getSettlementList().get(arg2);
                if (editSettlementInfo != null)
                    mHandler.sendMessage(mHandler.obtainMessage(JavaConnectJS.ACTION_CLICK_EDIT_SETTLEMENT, editSettlementInfo));
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mHandler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
            }
        });
        if (App.instance.isRevenueKiosk()) {
            findViewById(R.id.tv_place_name_title).setVisibility(View.GONE);
            findViewById(R.id.tv_table_name_title).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.tv_title_name)).setText(getResources().getString(R.string.edit_settlement));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e(TAG, "测试");
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case StoredCardActivity.VIEW_EVENT_STORED_CARD_PAY:{
                    String amount = (String) msg.obj;
                    Intent intent = new Intent(context,StoredCardActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("isPayAction", true);
//					UIHelp.startSoredCardActivity(context, MainPage.CHECK_REQUEST_CODE);
                    context.startActivityForResult(intent, MainPage.CHECK_REQUEST_CODE);
                }
                break;
                case StoredCardActivity.PAID_STOREDCARD_SUCCEED:
                {
                    if (closeOrderWindow.isShowing()) {
                        closeOrderWindow.clickEnterAction();
                    }
                    if (closeOrderSplitWindow.isShowing()) {
                        closeOrderSplitWindow.clickEnterAction();
                    }
                }
                break;
                case StoredCardActivity.HTTP_FAILURE:
                    UIHelp.showShortToast(context, ResultCode.getErrorResultStrByCode(context,
                            (Integer) msg.obj, context.getResources().getString(R.string.server)));
                    break;
                case JavaConnectJS.ACTION_CLICK_BACK:
                    EditSettlementPage.this.finish();
                    break;
                case JavaConnectJS.ACTION_CLICK_EDIT_SETTLEMENT: {
                    verifyDialog.show(EDIT_ITEM_ACTION, msg.obj);
                }
                break;

                case MainPage.VIEW_EVENT_CLOSE_BILL: {
//                    Intent intentCloseBill = new Intent();
                    String changeNum;
                    currentOrder = OrderSQL.getOrder(currentOrder.getId().intValue());
                    editSettlementAdapter.setEditSettlementInfos(getSettlementList());
                    editSettlementAdapter.notifyDataSetChanged();
                    HashMap<String, String> map = (HashMap<String, String>) msg.obj;

                    changeNum=map.get("changeNum");
                    if(!TextUtils.isEmpty(changeNum)){
                        if(!(App.instance.getLocalRestaurantConfig().getCurrencySymbol()+"0.00").equals(changeNum))
                        DialogFactory.changeDialogOrder(context, changeNum, new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    TableInfo table = TableInfoSQL.getTableById(
                            currentOrder.getTableId());
                    PrinterTitle title = ObjectFactory.getInstance()
                            .getPrinterTitle(
                                    App.instance.getRevenueCenter(),
                                    currentOrder,
                                    App.instance.getUser().getFirstName()
                                            + App.instance.getUser().getLastName(),
                                    table.getName(), 1,App.instance.getSystemSettings().getTrainType());
                    ArrayList<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(currentOrder
                            .getId());
                    ArrayList<PrintOrderItem> printOrderItems = ObjectFactory.getInstance().getItemList(orderDetails);
                    ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                            .getInstance().getItemModifierList(currentOrder, orderDetails);
                    OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                            currentOrder, App.instance.getRevenueCenter());
                    RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderAndBill(currentOrder, orderBill);


//                   if(App.instance.isRevenueKiosk()&&!App.instance.getSystemSettings().isPrintBill())
//                    {
//
//                    }else {

                       App.instance.remoteBillRePrint(
                               App.instance.getCahierPrinter(),
                               title,
                               currentOrder,
                               printOrderItems, orderModifiers,
                               OrderDetailTaxSQL.getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), currentOrder), PaymentSettlementSQL
                                       .getAllPaymentSettlementByPaymentId(Integer.valueOf(map.get("paymentId"))), roundAmount, App.instance.getSystemSettings().isCashClosePrint());
                  //  }

                    boolean isEdit = false;
                    if(!TextUtils.isEmpty(map.get("isEdit"))){
                        isEdit = Boolean.parseBoolean(map.get("isEdit"));
                    }
                    if(isEdit) {
                        /**
                         * 给后台发送log 信息
                         */
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
                                    CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                                    if (cloudSync != null) {
                                        int currCount = SyncMsgSQL.getSyncMsgCurrCountByOrderId(currentOrder.getId());
                                        cloudSync.syncOrderInfoForLog(currentOrder.getId(),
                                                App.instance.getRevenueCenter().getId(),
                                                App.instance.getBusinessDate(), currCount + 1);

                                    }
                                } else {
                                    SubPosCloudSyncJobManager subPosCloudSyncJobManager = App.instance.getSubPosSyncJob();
                                    if (subPosCloudSyncJobManager != null) {
                                        subPosCloudSyncJobManager.syncOrderInfoWhenEditPayment(currentOrder.getId(),
                                                App.instance.getRevenueCenter().getId(),
                                                App.instance.getBusinessDate());
                                    }
                                }
                            }
                        }).start();
                    }
                    break;
                }
                case MainPage.VIEW_EVENT_CLOSE_SPLIT_BILL: {

                    HashMap<String, String> paymentMap = (HashMap<String, String>) msg.obj;
                    List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL
                            .getAllPaymentSettlementByPaymentId(Integer.valueOf(paymentMap.get("paymentId")));
                    TableInfo table = TableInfoSQL.getTableById(
                            orderSplit.getTableId());


                    String changeNum;
                    changeNum = paymentMap.get("changeNum");
                    if (!TextUtils.isEmpty(changeNum)) {
                        if (!(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + "0.00").equals(changeNum))
                            DialogFactory.changeDialogOrder(context, changeNum, new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    }

                    OrderBill orderBill = ObjectFactory.getInstance().getOrderBillByOrderSplit(orderSplit, App.instance.getRevenueCenter());
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    PrinterTitle title = ObjectFactory.getInstance()
                            .getPrinterTitleByOrderSplit(
                                    App.instance.getRevenueCenter(),
                                    currentOrder,
                                    orderSplit,
                                    App.instance.getUser().getFirstName()
                                            + App.instance.getUser().getLastName(),
                                    table.getName(), orderBill, App.instance.getBusinessDate().toString(), 1);
                    ArrayList<OrderDetail> orderSplitDetails = (ArrayList<OrderDetail>) OrderDetailSQL.getOrderDetailsByOrderAndOrderSplit(orderSplit);

                    ArrayList<PrintOrderItem> orderItems = ObjectFactory
                            .getInstance().getItemList(orderSplitDetails);
                    List<Map<String, String>> taxMap = OrderDetailTaxSQL
                            .getOrderSplitTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), orderSplit);

                    ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                            .getInstance().getItemModifierList(OrderSQL.getOrder(orderSplit.getOrderId()), orderSplitDetails);
                    RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(orderSplit, orderBill);
                    Order temporaryOrder = new Order();
//                    if(orderSplit.)
                    temporaryOrder.setId(orderSplit.getOrderId());
                    temporaryOrder.setPersons(orderSplit.getPersons());
                    temporaryOrder.setSubTotal(orderSplit.getSubTotal());
                    temporaryOrder.setDiscountAmount(orderSplit.getDiscountAmount());
                    temporaryOrder.setTotal(orderSplit.getTotal());
                    temporaryOrder.setTaxAmount(orderSplit.getTaxAmount());
                    temporaryOrder.setTableId(orderSplit.getTableId());
                    temporaryOrder.setOrderNo(currentOrder.getOrderNo());
                    if (orderItems.size() > 0 && printer != null)
                    {
                        try
                        {
                            App.instance.remoteBillRePrint(printer, title, temporaryOrder,
                                    orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount, App.instance.getSystemSettings().isCashClosePrint());
                        }
                        catch (Exception e)
                        {
                            Log.i("Error Check", String.valueOf(e));
                        }

                    }
                    boolean isEdit = false;
                    if(!TextUtils.isEmpty(paymentMap.get("isEdit"))){
                        isEdit = Boolean.parseBoolean(paymentMap.get("isEdit"));
                    }

                    if(isEdit) {
                        /**
                         * 给后台发送log 信息
                         */
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
                                    CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                                    if (cloudSync != null) {
                                        int currCount = SyncMsgSQL.getSyncMsgCurrCountByOrderId(orderSplit.getOrderId());
                                        cloudSync.syncOrderInfoForLog(orderSplit.getOrderId(),
                                                App.instance.getRevenueCenter().getId(),
                                                App.instance.getBusinessDate(), currCount + 1);
                                    }
                                } else {
                                    SubPosCloudSyncJobManager subPosCloudSyncJobManager = App.instance.getSubPosSyncJob();
                                    if (subPosCloudSyncJobManager != null) {
                                        subPosCloudSyncJobManager.syncOrderInfoWhenEditPayment(orderSplit.getOrderId(),
                                                App.instance.getRevenueCenter().getId(),
                                                App.instance.getBusinessDate());
                                    }
                                }
                            }
                        }).start();
                    }
                }

                break;
                case EDIT_SETTLEMENT_CLOSE_BILL: {
                    Map<String, List<Map<String, Object>>> map = (Map<String, List<Map<String, Object>>>) msg.obj;
                    List<Map<String, Object>> oldPaymentMapList = map.get("oldPaymentMapList");
                    List<Map<String, Object>> newPaymentMapList = map.get("newPaymentMapList");
                    // 旧的支付方式先储存到数据库中
                    for (Map<String, Object> paymentMap : oldPaymentMapList) {
                        PaymentSettlement paymentSettlement = (PaymentSettlement) paymentMap
                                .get("paymentSettlement");
                        switch (paymentSettlement.getPaymentTypeId()) {
                            case ParamConst.SETTLEMENT_TYPE_CASH:
                                OrderBill orderBill = OrderBillSQL.getOrderBillByBillNo(paymentSettlement.getBillNo().intValue());
                                if (orderBill.getType().intValue() == ParamConst.BILL_TYPE_UN_SPLIT) {
                                    Order order = OrderSQL.getOrder(orderBill.getOrderId().intValue());
                                    RoundAmount round = RoundAmountSQL.getRoundAmount(order);
                                    if (round != null) {
                                        round.setRoundBalancePrice(Double.parseDouble(BH.sub(BH.getBD(round.getRoundAlfterPrice()), BH.getBD(round.getRoundBeforePrice()), true).toString()));
                                        RoundAmountSQL.update(round);
                                        OrderHelper.setOrderTotalAlfterRound(order, round);
                                        OrderSQL.update(order);
                                    }
                                } else {
                                    OrderSplit orderSplit = OrderSplitSQL.get(orderBill.getOrderSplitId().intValue());
                                    RoundAmount round = RoundAmountSQL.getRoundAmount(orderSplit);
                                    if (round != null) {
                                        round.setRoundBalancePrice(Double.parseDouble(BH.sub(BH.getBD(round.getRoundAlfterPrice()), BH.getBD(round.getRoundBeforePrice()), true).toString()));
                                        RoundAmountSQL.update(round);
                                        OrderHelper.setOrderSplitTotalAlfterRound(orderSplit, round);
                                        OrderSplitSQL.update(orderSplit);
                                    }
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
                            case ParamConst.SETTLEMENT_TYPE_UNIPAY:
                            case ParamConst.SETTLEMENT_TYPE_VISA:
                            case ParamConst.SETTLEMENT_TYPE_AMEX:
                            case ParamConst.SETTLEMENT_TYPE_JCB:
                                CardsSettlement oldCardsSettlement = (CardsSettlement) paymentMap
                                        .get("subPaymentSettlement");
                                if (oldCardsSettlement != null) {
                                    oldCardsSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                                    CardsSettlementSQL
                                            .addCardsSettlement(oldCardsSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD:
                                BohHoldSettlement oldBohHoldSettlement = (BohHoldSettlement) paymentMap
                                        .get("subPaymentSettlement");
                                if (oldBohHoldSettlement != null) {
                                    oldBohHoldSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                                    BohHoldSettlementSQL
                                            .addBohHoldSettlement(oldBohHoldSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT:
                                NonChargableSettlement oldNonChargableSettlement = (NonChargableSettlement) paymentMap
                                        .get("subPaymentSettlement");
                                if (oldNonChargableSettlement != null) {
                                    oldNonChargableSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                                    NonChargableSettlementSQL
                                            .addNonChargableSettlement(oldNonChargableSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_VOID: {
                                VoidSettlement oldVoidSettlement = (VoidSettlement) paymentMap
                                        .get("subPaymentSettlement");
                                if (oldVoidSettlement != null) {
                                    oldVoidSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                                    VoidSettlementSQL
                                            .addVoidSettlement(oldVoidSettlement);
                                }
                            }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_REFUND: {
                                VoidSettlement oldVoidSettlement = (VoidSettlement) paymentMap
                                        .get("subPaymentSettlement");
                                if (oldVoidSettlement != null) {
                                    oldVoidSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                                    VoidSettlementSQL
                                            .addVoidSettlement(oldVoidSettlement);
                                }
                            }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_NETS:
                                NetsSettlement oldNetsSettlement = (NetsSettlement) paymentMap
                                        .get("subPaymentSettlement");
                                if (oldNetsSettlement != null) {
                                    oldNetsSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                                    NetsSettlementSQL
                                            .addNetsSettlement(oldNetsSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_ALIPAY:
                                AlipaySettlement oldAlipaySettlement = (AlipaySettlement) paymentMap
                                        .get("subPaymentSettlement");
                                if (oldAlipaySettlement != null) {
                                    oldAlipaySettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                                    AlipaySettlementSQL
                                            .addAlipaySettlement(oldAlipaySettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_EZLINK:
//                                WeixinSettlement oldWeixinSettlement = (WeixinSettlement) paymentMap
//                                        .get("subPaymentSettlement");
//                                if (oldWeixinSettlement != null) {
//                                    oldWeixinSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
//                                    WeixinSettlementSQL
//                                            .addWeixinSettlement(oldWeixinSettlement);
//                                }
                                break;
                            default:
                                break;
                        }
                        paymentSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                        PaymentSettlementSQL
                                .addPaymentSettlement(paymentSettlement);
                    }
                    // 新支付方式再删除
                    for (Map<String, Object> newPaymentMap : newPaymentMapList) {
                        PaymentSettlement paymentSettlement = (PaymentSettlement) newPaymentMap
                                .get("newPaymentSettlement");
                        switch (paymentSettlement.getPaymentTypeId()) {
                            case ParamConst.SETTLEMENT_TYPE_AMEX:
                            case ParamConst.SETTLEMENT_TYPE_JCB:
                            case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
                            case ParamConst.SETTLEMENT_TYPE_UNIPAY:
                            case ParamConst.SETTLEMENT_TYPE_VISA:
                                CardsSettlement newCardsSettlement = (CardsSettlement) newPaymentMap
                                        .get("newSubPaymentSettlement");
                                if (newCardsSettlement != null) {
                                    CardsSettlementSQL
                                            .deleteCardsSettlement(newCardsSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD:
                                BohHoldSettlement newBohHoldSettlement = (BohHoldSettlement) newPaymentMap
                                        .get("newSubPaymentSettlement");
                                if (newBohHoldSettlement != null) {
                                    BohHoldSettlementSQL
                                            .deleteBohHoldSettlement(newBohHoldSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT:
                                NonChargableSettlement newNonChargableSettlement = (NonChargableSettlement) newPaymentMap
                                        .get("newSubPaymentSettlement");
                                if (newNonChargableSettlement != null) {
                                    NonChargableSettlementSQL
                                            .deleteNonChargableSettlement(newNonChargableSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_VOID: {
                                VoidSettlement newVoidSettlement = (VoidSettlement) newPaymentMap
                                        .get("newSubPaymentSettlement");
                                if (newVoidSettlement != null) {
                                    VoidSettlementSQL
                                            .deleteVoidSettlement(newVoidSettlement);
                                }
                            }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_REFUND: {
                                VoidSettlement newVoidSettlement = (VoidSettlement) newPaymentMap
                                        .get("newSubPaymentSettlement");
                                if (newVoidSettlement != null) {
                                    VoidSettlementSQL
                                            .deleteVoidSettlement(newVoidSettlement);
                                }
                            }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_NETS:
                                NetsSettlement newNetsSettlement = (NetsSettlement) newPaymentMap
                                        .get("newSubPaymentSettlement");
                                if (newNetsSettlement != null) {
                                    NetsSettlementSQL
                                            .deleteNetsSettlement(newNetsSettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_ALIPAY:
                                AlipaySettlement newAlipaySettlement = (AlipaySettlement) newPaymentMap
                                        .get("newSubPaymentSettlement");
                                if (newAlipaySettlement != null) {
                                    AlipaySettlementSQL
                                            .deleteAlipaySettlement(newAlipaySettlement);
                                }
                                break;
                            case ParamConst.SETTLEMENT_TYPE_EZLINK:
//                                WeixinSettlement newWeixinSettlement = (WeixinSettlement) newPaymentMap
//                                        .get("newSubPaymentSettlement");
//                                if (newWeixinSettlement != null) {
//                                    WeixinSettlementSQL
//                                            .deleteWeixinSettlement(newWeixinSettlement);
//                                }
                                break;
                            default:
                                break;
                        }
                        PaymentSettlementSQL
                                .deletePaymentSettlement(paymentSettlement);
                    }
                }
                break;
                case MainPage.VIEW_EVENT_SHOW_BILL_ON_HOLD:
                    verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_BILL_ON_HOLD, null);
                    break;
                case MainPage.VIEW_EVENT_SHOW_VOID:
                    verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_VOID, null);
                    break;
                case MainPage.VIEW_EVENT_SHOW_ENTERTAINMENT:
                    verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_ENTERTAINMENT, null);
                    break;
                case VerifyDialog.DIALOG_DISMISS:

                    break;
                case VerifyDialog.DIALOG_RESPONSE: {
                    Map<String, Object> result = (Map<String, Object>) msg.obj;
                    User user = (User) result.get("User");
                    if (result.get("MsgObject").equals(
                            MainPage.HANDLER_MSG_OBJECT_BILL_ON_HOLD)) {
                        if (!verifyDialog.isShowing()) {
                            if (closeOrderWindow.isShowing()) {
                                closeOrderWindow.setUser(user);
                                closeOrderWindow.openMoneyKeyboard(View.GONE,
                                        ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD);
                            } else if (closeOrderSplitWindow.isShowing()) {
                                closeOrderSplitWindow.setUser(user);
                                closeOrderSplitWindow.openMoneyKeyboard(View.GONE,
                                        ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD);
                            }
                        }
                    } else if (result.get("MsgObject").equals(
                            MainPage.HANDLER_MSG_OBJECT_VOID)) {
                        if (!verifyDialog.isShowing()) {
                            if (closeOrderWindow.isShowing()) {
                                closeOrderWindow.setUser(user);
                                closeOrderWindow.openMoneyKeyboard(View.GONE,
                                        ParamConst.SETTLEMENT_TYPE_REFUND);
                            } else if (closeOrderSplitWindow.isShowing()) {
                                closeOrderSplitWindow.setUser(user);
                                closeOrderSplitWindow.openMoneyKeyboard(View.GONE,
                                        ParamConst.SETTLEMENT_TYPE_REFUND);
                            }
                        }
                    } else if (result.get("MsgObject").equals(
                            MainPage.HANDLER_MSG_OBJECT_ENTERTAINMENT)) {
                        if (!verifyDialog.isShowing()) {
                            if (closeOrderWindow.isShowing()) {
                                closeOrderWindow.setUser(user);
                                closeOrderWindow.openMoneyKeyboard(View.GONE,
                                        ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT);
                            } else if (closeOrderSplitWindow.isShowing()) {
                                closeOrderSplitWindow.setUser(user);
                                closeOrderSplitWindow.openMoneyKeyboard(View.GONE,
                                        ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT);
                            }
                        }
                    } else if (result.get("MsgObject").equals(EDIT_ITEM_ACTION)) {
                        if (!verifyDialog.isShowing()) {
                            closeOrderWindow.setUser(user);
                            closeOrderSplitWindow.setUser(user);
                            EditSettlementInfo editSettlementInfo = (EditSettlementInfo) result.get("Object");
                            showCloseBillWindow(editSettlementInfo);
                        }
                    }
                }
                break;
                case MainPage.ACTION_PRINT_PAX_SPLIT_BY_PAX: {
                    HashMap<String, String> paymentMap = (HashMap<String, String>) msg.obj;
                    OrderSplit paidOrderSplit = OrderSplitSQL.get(Integer.valueOf(paymentMap.get("orderSplitId")));
                    List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL
                            .getAllPaymentSettlementByPaymentId(Integer.valueOf(paymentMap.get("paymentId")));
                    OrderBill orderBill = ObjectFactory.getInstance().getOrderBillByOrderSplit(paidOrderSplit, App.instance.getRevenueCenter());
                    PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                            context);
                    printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
                    printerLoadingDialog.showByTime(3000);
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    TableInfo table = TableInfoSQL.getTableById(
                            orderSplit.getTableId());
                    PrinterTitle title = ObjectFactory.getInstance()
                            .getPrinterTitleByOrderSplit(
                                    App.instance.getRevenueCenter(),
                                    currentOrder,
                                    paidOrderSplit,
                                    App.instance.getUser().getFirstName()
                                            + App.instance.getUser().getLastName(),
                                    table.getName(), orderBill, App.instance.getBusinessDate().toString(), 1);
                    title.setSpliteByPax(paidOrderSplit.getSplitByPax());
                    ArrayList<OrderDetail> orderSplitDetails = OrderDetailSQL.getOrderDetails(currentOrder
                            .getId());
                    ArrayList<PrintOrderItem> orderItems = ObjectFactory
                            .getInstance().getItemList(orderSplitDetails);
                    List<Map<String, String>> taxMap = OrderDetailTaxSQL
                            .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), currentOrder);

                    ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                            .getInstance().getItemModifierList(currentOrder, orderSplitDetails);
                    Order temporaryOrder = new Order();
                    temporaryOrder.setPersons(paidOrderSplit.getPersons());
                    temporaryOrder.setSubTotal(paidOrderSplit.getSubTotal());
                    temporaryOrder.setDiscountAmount(currentOrder.getDiscountAmount());
                    temporaryOrder.setTotal(currentOrder.getTotal());
                    temporaryOrder.setTaxAmount(paidOrderSplit.getTaxAmount());
                    temporaryOrder.setOrderNo(currentOrder.getOrderNo());
                    temporaryOrder.setGrandTotal(paidOrderSplit.getTotal());
                    /*
                    ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                            .getInstance().getItemModifierList(currentOrder, orderSplitDetails);
                    Order temporaryOrder = new Order();
                    temporaryOrder.setPersons(paidOrderSplit.getPersons());
                    temporaryOrder.setSubTotal(paidOrderSplit.getSubTotal());
                    temporaryOrder.setDiscountAmount(currentOrder.getDiscountAmount());
                    temporaryOrder.setTotal(currentOrder.getTotal());
                    temporaryOrder.setTaxAmount(paidOrderSplit.getTaxAmount());
                    temporaryOrder.setOrderNo(currentOrder.getOrderNo());
                    temporaryOrder.setGrandTotal(paidOrderSplit.getTotal());
                     */
                    if (orderItems.size() > 0 && printer != null) {
                        RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(paidOrderSplit, orderBill);
                        List<OrderPromotion>  orderPromotions= PromotionDataSQL.getPromotionDataOrOrderid(currentOrder.getId());

                        App.instance.remoteBillPrint(printer, title, temporaryOrder,
                                orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount,null);
                    }
                    // remove get bill notification
                    /**
                     * 给后台发送log 信息
                     */
                    if (OrderSQL.getOrder(paidOrderSplit.getOrderId()).getOrderStatus() == ParamConst.ORDER_STATUS_FINISHED) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                                if (cloudSync != null) {
                                    int currCount = SyncMsgSQL.getSyncMsgCurrCountByOrderId(currentOrder.getId());
                                    cloudSync.syncOrderInfoForLog(currentOrder.getId(),
                                            App.instance.getRevenueCenter().getId(),
                                            App.instance.getBusinessDate(), currCount + 1);

                                }
                            }
                        }).start();
                    }

                }
                break;
                default:
                    break;
            }
        }
    };

    private List<EditSettlementInfo> getSettlementList() {
        List<EditSettlementInfo> editSettlementInfos = new ArrayList<EditSettlementInfo>();
        List<Payment> payments = PaymentSQL.getTodayAllPaymentBySession(
                App.instance.getSessionStatus(),
                App.instance.getLastBusinessDate());
        if (payments.isEmpty()) {
            return editSettlementInfos;
        }
        for (Payment payment : payments) {
            Order order = OrderSQL.getOrder(payment.getOrderId());
            PlaceInfo place = PlaceInfoSQL.getPlaceInfoById(order.getPlaceId());
            TableInfo tableInfo = TableInfoSQL.getTableById(order.getTableId());
            User user = UserSQL.getUserById(payment.getUserId());
            int  splitId = 0;
            int splitGroupId = 0;
            if(payment.getType().intValue() == ParamConst.BILL_TYPE_SPLIT){
                splitId = payment.getOrderSplitId() == null ? 0 : payment.getOrderSplitId();
                OrderSplit orderSplitInfo = OrderSplitSQL.get(splitId);
                if(orderSplitInfo != null){
                    splitGroupId = orderSplitInfo.getGroupId().intValue();
                }
            }
            EditSettlementInfo editSettlementInfo = new EditSettlementInfo(
                    payment.getId(), payment.getOrderId(), splitId,
                    payment.getBillNo(), payment.getPaymentAmount(), place.getPlaceName(), tableInfo.getName(),
                    order.getTableId(), TimeUtil.getTimeFormat(payment
                    .getCreateTime()), user.getFirstName()
                    + user.getLastName(), payment.getType(), splitGroupId);
            editSettlementInfos.add(editSettlementInfo);
        }
        return editSettlementInfos;
    }

    private void showCloseBillWindow(EditSettlementInfo editSettlementInfo) {
        if (editSettlementInfo == null) {
            return;
        }
        switch (editSettlementInfo.getType().intValue()) {
            case ParamConst.BILL_TYPE_UN_SPLIT:
                currentOrder = OrderSQL.getOrder(editSettlementInfo.getOrderId());
                if(currentOrder == null){
                    return;
                }
                currentOrder.setOldTotal(currentOrder.getTotal());
                OrderBill orderBill = OrderBillSQL.getOrderBillByOrder(currentOrder);
                List<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
                closeOrderWindow.show(view_top_line,currentOrder, 250.0f, orderBill, orderDetails);
                break;
            case ParamConst.BILL_TYPE_SPLIT:
                Payment payment = PaymentSQL.getPayment(editSettlementInfo.getPaymentId());
                if(payment.getOrderSplitId() != null && payment.getOrderSplitId().intValue() != 0){
                    orderSplit = OrderSplitSQL.get(payment.getOrderSplitId());
                    orderSplit.setOldTotal(orderSplit.getTotal());
                    currentOrder = OrderSQL.getOrder(editSettlementInfo.getOrderId());
                    closeOrderSplitWindow.show(view_top_line, OrderSQL.getOrder(payment.getOrderId()), orderSplit);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(closeOrderWindow != null && closeOrderWindow.isShowing()){
            closeOrderWindow.backLikeClose();
            return;
        }else if(closeOrderSplitWindow != null && closeOrderSplitWindow.isShowing()){
            closeOrderSplitWindow.backLikeClose();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainPage.CHECK_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("qrCode", getIntent().getStringExtra("qrCode"));
                map.put("revenueId", getIntent().getIntExtra("revenueId", 0));
                map.put("consumeAmount", getIntent().getStringExtra("consumeAmount"));
                map.put("operateType", getIntent().getIntExtra("operateType", -1));
                SyncCentre.getInstance().updateStoredCardValue(context, map, mHandler);
                loadingDialog.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
