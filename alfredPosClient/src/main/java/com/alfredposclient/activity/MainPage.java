package com.alfredposclient.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.VerifyDialog;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderPromotion;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.javabean.temporaryforapp.TempModifierDetail;
import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.javabean.temporaryforapp.TempOrderDetail;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.PromotionDataSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.SyncMsgSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.store.sql.temporaryforapp.TempModifierDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.MachineUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.StockCallBack;
import com.alfredbase.utils.TimeUtil;
import com.alfredbase.utils.ToastUtils;
import com.alfredposclient.Fragment.TableLayoutFragment;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.javabean.MultiRVCPlacesDao;
import com.alfredposclient.javabean.TablesStatusInfo;
import com.alfredposclient.jobs.CloudSyncJobManager;
import com.alfredposclient.popupwindow.CloseOrderSplitWindow;
import com.alfredposclient.popupwindow.CloseOrderWindow;
import com.alfredposclient.popupwindow.DiscountWindow;
import com.alfredposclient.popupwindow.DiscountWindow.ResultCall;
import com.alfredposclient.popupwindow.ModifyQuantityWindow;
import com.alfredposclient.popupwindow.ModifyQuantityWindow.DismissCall;
import com.alfredposclient.popupwindow.OpenItemWindow;
import com.alfredposclient.popupwindow.OrderDetailFireWindow;
import com.alfredposclient.popupwindow.OrderSplitPrintWindow;
import com.alfredposclient.popupwindow.SetPAXWindow;
import com.alfredposclient.popupwindow.SetWeightWindow;
import com.alfredposclient.popupwindow.SpecialInstractionsWindow;
import com.alfredposclient.utils.AlertToDeviceSetting;
import com.alfredposclient.view.MainPageMenuView;
import com.alfredposclient.view.MainPageOperatePanel;
import com.alfredposclient.view.MainPageOrderView;
import com.alfredposclient.view.MainPageSearchView;
import com.alfredposclient.view.SelectOrderSplitDialog;
import com.alfredposclient.view.SettingView;
import com.alfredposclient.view.TopMenuView;
import com.alfredposclient.xmpp.XMPP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moonearly.utils.service.TcpUdpFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

//import com.alfredposclient.popupwindow.OrderDetailFireWindow;


public class MainPage extends BaseActivity {

    public static final int VIEW_EVENT_CLOSE_PAY_WINDOW = 99;
    public static final int CHECK_REQUEST_CODE = 98;
    public static final int REFRESH_UNSEAT_TABLE_VIEW = 97;
    public static final int REFRESH_STOCK_NUM = 101;
    public static final int REFRESH_TABLES_STATUS = 102;
    public static final int VIEW_EVENT_DISMISS_TABLES = 103;
    public static final int VIEW_EVENT_SET_TABLE_PACKS = 104;
    public static final int VIEW_EVENT_SHOW_CLOSE_ORDER_WINDOW = 105;
    public static final int VIEW_EVENT_SHOW_TABLES = 106;
    public static final int VIEW_EVENT_SHOW_OPEN_ITEM_WINDOW = 107;
    public static final int VIEW_EVENT_SHOW_DISCOUNT_WINDOW = 108;
    public static final int VIEW_EVENT_ADD_ORDER_DETAIL = 109;
    public static final int VIEW_EVENT_SHOW_SEARCH = 110;
    public static final int VIEW_EVENT_DISMISS_OPEN_ITEM_WINDOW = 111;
    public static final int VIEW_EVENT_OPEN_MODIFIERS = 112;
    public static final int VIEW_EVENT_SET_DATA = 113;
    public static final int VIEW_EVENT_SHOW_MODIFY_QUANTITY_WINDOW = 114;
    public static final int VIEW_EVENT_DISMISS_SEARCH = 115;
    public static final int DISMISS_SOFT_INPUT = 116;
    public static final int VIEW_EVENT_SEARCH = 117;
    public static final int VIEW_EVENT_OPERATEPANEL = 118;
    public static final int VIEW_EVENT_CLOSE_BILL = 119;
    public static final int KOT_DONE_SET_DATA = 120;
    public static final int VIEW_EVNT_GET_BILL_PRINT = 121;
    public static final int VIEW_EVENT_SELECT_BILL = 122;
    public static final int DIALOG_DISMISS = 1000;
    public static final int VIEW_EVENT_SHOW_BILL_ON_HOLD = 123;
    public static final int VIEW_EVENT_SHOW_VOID = 124;
    public static final int VIEW_EVENT_SHOW_ENTERTAINMENT = 125;
    public static final int VIEW_EVENT_TANSFER_TABLE = 126;
    public static final int VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER = 127;
    public static final int VIEW_EVENT_PLACE_ORDER = 128;
    public static final int ACTION_SWITCH_USER = 129;
    public static final int ACTION_TRANSFER_TABLE = 132;
    public static final int WAITER_SEND_KDS = 133;
    public static final int VIEW_EVENT_KICK_CASHDRAWER = 134;
    public static final int VIEW_EVENT_SHOW_VOIDORFREE_WINDOW = 135;
    public static final int VIEW_EVENT_TANSFER_PAX = 136;
    public static final int VIEW_EVENT_VOID_OR_FREE = 137;
    public static final int VIEW_EVENT_SHOW_SPECIAL_INSTRACTIONS_WINDOW = 138;
    public static final int VIEW_EVENT_CLOSE_MODIFIER_VIEW = 139;
    public static final int VIEW_SUB_MENU = 140;
    public static final int VIEW_SUB_MENU_ALL = 141;
    public static final int VIEW_EVENT_SHOW_CLOSE_SPLIT_BILL = 142;
    public static final int VIEW_EVENT_CLOSE_SPLIT_BILL = 143;
    public static final int VIEW_EVENT_SHOW_TABLES_AFTER_CLOSE_BILL = 144;
    public static final int VIEW_EVENT_OPEN_WAITING_LIST_DATA = 300;
    // KOT PRINT
    public static final int KOT_PRINT_FAILED = 200;
    public static final int KOT_PRINT_SUCCEED = 201;
    public static final int KOT_PRINT_NULL = 202;
    public static final int KOT_ITEM_PRINT_NULL = 203;
    //
    public static final int ORDER_TIME_OUT = 145;
    public static final int VIEW_EVENT_TAKE_AWAY = 146;
    public static final int VIEW_EVENT_SET_WEIGHT = 147;
    public static final int VIEW_EVENT_SET_APPORDER_TABLE_PACKS = 148;
    public static final int VIEW_EVENT_UNSEAT_ORDER = 149;
    public static final int VIEW_EVENT_FIRE = 150;
    public static final int VIEW_EVENT_TANSFER_ITEM = 151;
    public static final int ACTION_TRANSFER_ITEM = 152;
    public static final int VIEW_EVENT_SPLIT_BY_PAX = 154;
    public static final int ACTION_PAX_SPLIT_BY_PAX = 156;
    public static final int ACTION_PAX_SPLIT_BY_PAX_WINDOW = 157;
    public static final int ACTION_PRINT_PAX_SPLIT_BY_PAX = 158;
    public static final int VIEW_EVENT_SHOW_CLOSE_SPLIT_BY_PAX_BILL = 159;
    public static final int ACTION_TRANSFER_SPLIT_BY_NUM = 160;
    public static final int ACTION_REMOVE_ORDER_DETAIL = 161;
    public static final int ACTION_CANCEL_ORDER_DETAIL = 162;
    public static final int VIEW_EVENT_SET_DATA_AND_CLOSE_MODIFIER = 163;
    public static final int ACTION_KOT_NEXT_KDS = 164;
    public static final int SERVER_TRANSFER_TABLE_FROM_OTHER_RVC = 400;
    public static final int ACTION_MERGE_TABLE = 401;
    public static final String REFRESH_TABLES_BROADCAST = "REFRESH_TABLES_BROADCAST";
    public static final String REFRESH_COMMIT_ORDER = "REFRESH_COMMIT_ORDER";
    public static final String TRANSFER_TABLE = "TRANSFER_TABLE";
    public static final String TRANSFER_ITEM = "TRANSFER_ITEM";
    public static final String HANDLER_MSG_OBJECT_BILL_ON_HOLD = "BILL_ON_HOLD";
    public static final String HANDLER_MSG_OBJECT_VOID = "VOID";
    public static final String HANDLER_MSG_OBJECT_ENTERTAINMENT = "ENTERTAINMENT";
    public static final String HANDLER_MSG_OBJECT_OPEN_DRAWER = "OPEN_DRAWER";
    public static final String HANDLER_MSG_OBJECT_CANCEL_ITEM = "CANCEL_ITEM";
    private static final int GET_TABLESTATUSINFO_DATA = 100;
    // public static final int ACTION_SWITCH_USER_ERROR = 130;
    private static final int VIEW_EVENT_DISMISS_TABLES_AFTER_MERGER = 131;
    private static final String SHOW_TABLES = "SHOW_TABLES";
    private static final String HANDLER_MSG_OBJECT_DISCOUNT = "DISCOUNT";
    private static final String HANDLER_MSG_OBJECT_TRANSFER_TABLE = "TRANSFERTABLE";
    private static final String HANDLER_MSG_OBJECT_TRANSFER_ITEM = "TRANSFERITEM";
    private static final String HANDLER_MSG_OBJECT_ITEM_QTY = "ITEM_QTY";
    private static final String HANDLER_MSG_OBJECT_VOID_OR_FREE = "VOID_OR_FREE";
    private static final String PAMENT_METHOD = "PAMENTMETHOD";
    private static final String HANDLER_MSG_OBJECT_STORED_CARD_REFUND = "STORED_CARD_REFUND";
    private static final String HANDLER_MSG_OBJECT_STORED_CARD_LOSS = "STORED_CARD_LOSS";
    private static final String HANDLER_MSG_OBJECT_STORED_CARD_REPLACEMENT = "STORED_CARD_REPLACEMENT";
    public MainPageOrderView orderView;
    public String tableShowAction = SHOW_TABLES;
    public Order currentOrder;
    public PrinterLoadingDialog printerLoadingDialog;
    public SelectOrderSplitDialog selectOrderSplitDialog;
    private String TAG = MainPage.class.getSimpleName();
    private TopMenuView topMenuView;
    private MainPageSearchView mainPageSearchView;
    private MainPageOperatePanel operatePanel;
    private MainPageMenuView mainPageMenuView;
    private CloseOrderWindow closeOrderWindow; // settlement window
    private CloseOrderSplitWindow closeOrderSplitWindow;
    private SetPAXWindow setPAXWindow;
    private DiscountWindow discountWindow;
    private ModifyQuantityWindow modifyQuantityWindow;
    private OpenItemWindow openItemWindow;
    private SpecialInstractionsWindow specialInstractionsWindow;
    private SetWeightWindow setWeightWindow;
    //	private WebView web_tables;
//    private StoredCardActivity f_stored_card;
    private JavaConnectJS javaConnectJS;
    private Gson gson = new Gson();
    private boolean isShowTables = false;
    private boolean isTableFirstShow = true;
    private List<PlaceInfo> places = new ArrayList<PlaceInfo>();
    private List<TableInfo> tables = new ArrayList<TableInfo>();
    private List<TablesStatusInfo> tableStatusInfo = new ArrayList<TablesStatusInfo>();
    private TableInfo currentTable;
    private TableInfo oldTable;
    private Order oldOrder;


    //	public LoadingDialog loadingDialog;
    private List<OrderDetail> orderDetails;
    private VerifyDialog verifyDialog;
    private OrderSplitPrintWindow orderSplitPrintWindow;

    private DrawerLayout mDrawerLayout; // activity滑动布局
    private SettingView settingView; // 右滑视图

    private IntentFilter filter;
    private int count;

    private int activityRequestCode = 0;

    private int appOrderId;

    private View animatorView;
    private View view_top_line;
    private OrderDetail transfItemOrderDetail;
    private OrderDetail oldTransItemOrderDetail;

    //    private FragmentTransaction transaction;
//    private FragmentManager fragmentManager;
    private TableLayoutFragment f_tables;
    public Handler handler = new Handler() {
        public void handleMessage(final android.os.Message msg) {
            switch (msg.what) {
//			case VIEW_EVENT_CLOSE_PAY_WINDOW:
//				closeCloseOrderWindow();
//				break;
                case REFRESH_STOCK_NUM:
                    if (mainPageMenuView != null) {
                        mainPageMenuView.setParam(currentOrder, handler);
                    }
                    break;
                case StoredCardActivity.VIEW_EVENT_STORED_CARD_PAY: {
                    String amount = (String) msg.obj;
                    Intent intent = new Intent(context, StoredCardActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("isPayAction", true);
//				UIHelp.startSoredCardActivity(context, CHECK_REQUEST_CODE);
                    context.startActivityForResult(intent, CHECK_REQUEST_CODE);
                }
                break;
//			case StoredCardActivity.VIEW_EVENT_STORED_CARD_PAY_RESULT: {
//				hideStoredCard();
//
//				Map<String, Object> map = (Map<String, Object>) msg.obj;
//				SyncCentre.getInstance().updateStoredCardValue(context, map, handler);
//				loadingDialog.show();
//			}
//				break;
                case StoredCardActivity.PAID_STOREDCARD_SUCCEED: {
                    dismissLoadingDialog();
                    if (closeOrderWindow.isShowing()) {
                        closeOrderWindow.clickEnterAction();
                    }
                    if (closeOrderSplitWindow.isShowing()) {
                        closeOrderSplitWindow.clickEnterAction();
                    }
                }
                break;
                case StoredCardActivity.HTTP_FAILURE:
                    dismissLoadingDialog();
                    UIHelp.showShortToast(context, ResultCode.getErrorResultStrByCode(context,
                            (Integer) msg.obj, context.getResources().getString(R.string.server)));
                    break;
                case REFRESH_TABLES_STATUS:
                    if (!f_tables.isHidden())
                        f_tables.refresh();
                    break;
                case GET_TABLESTATUSINFO_DATA:
                    getTableStatusInfo();
                    handler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_REFRESH);
                    break;
                case VIEW_EVENT_DISMISS_TABLES: {
                    if (loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    final boolean fromThisRVC = checkIfTableFromThisRVC(currentTable);
                    if (!fromThisRVC) {
                        currentTable = oldTable;
                    }

                    closeTables();

                    if (currentTable.getPosId() < 0) {
                        setDataWaitingList();
                    } else {
                        setData();
                    }
                }
                break;
                case VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER: {

                    final boolean fromThisRVC = checkIfTableFromThisRVC(currentTable);
                    if (!fromThisRVC) {
                        currentTable = oldTable;
                        closeTables();
                    } else {

                        if (oldTable != null) {
                            if (oldTable.getPosId() < 0) {
                                TableInfoSQL.deleteTableInfo(oldTable.getPosId());
                            }
                        }

                        closeTables();
                        setData();
                    }
                }
                break;
                case VIEW_EVENT_DISMISS_TABLES_AFTER_MERGER:
                    closeTables();
                    mergerOrderSetData();
                    // App.instance.getKdsJobManager().transferTableDownKot(ParamConst.JOB_MERGER_KOT,
                    // toKotSummary, fromKotSummary);
                    break;
                case VIEW_EVENT_SET_TABLE_PACKS:
                    setTablePacks((String) msg.obj);
                    if (currentTable.getPosId() < 0) {
                        handler.sendMessage(handler.obtainMessage(
                                MainPage.VIEW_EVENT_OPEN_WAITING_LIST_DATA, currentTable));
                    } else {
                        handler.sendEmptyMessage(VIEW_EVENT_DISMISS_TABLES);
                    }
                    break;
                case VIEW_EVENT_SET_APPORDER_TABLE_PACKS:
                    setTablePacks((String) msg.obj);
                    loadingDialog.show();
                    initAppOrder(currentTable);
                    break;
                // Open settlement window
                case VIEW_EVENT_SHOW_CLOSE_ORDER_WINDOW:
                    mainPageMenuView.closeModifiers();
                    if (IntegerUtils.isEmptyOrZero(currentOrder.getAppOrderId())) {
                        showCloseBillWindow();
                    } else {
                        TempOrder tempOrder = TempOrderSQL.getTempOrderByAppOrderId(currentOrder.getAppOrderId());
                        if (tempOrder != null && tempOrder.getPaied() == ParamConst.TEMPORDER_PAIED) {
                            TempOrderISPaied();
                        } else {
                            showCloseBillWindow();
                        }
                    }

                    break;
                case VIEW_EVENT_SHOW_TABLES: {
                    activityRequestCode = 0;
                    tableShowAction = SHOW_TABLES;
                    if (currentOrder != null) {
                        if (orderDetails != null && orderDetails.size() <= 0) {
                            KotSummarySQL.deleteKotSummaryByOrder(currentOrder);
                        }
                    }
                    mainPageMenuView.closeModifiers();
                    showTables();
                }
                break;
                case VIEW_EVENT_SHOW_TABLES_AFTER_CLOSE_BILL: {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("tableId", currentTable.getPosId().intValue());
                        jsonObject.put("status", ParamConst.TABLE_STATUS_IDLE);
                        jsonObject.put("RX", RxBus.RX_REFRESH_TABLE);
                        TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                        TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    activityRequestCode = 0;
                    selectOrderSplitDialog.dismiss();
                    tableShowAction = SHOW_TABLES;
                    currentOrder = null;
                    showTables();
                }
                break;
                case VIEW_EVENT_SHOW_OPEN_ITEM_WINDOW:
                    showOpenItemWindow();
                    break;
                case VIEW_EVENT_DISMISS_OPEN_ITEM_WINDOW:
                    dismissOpenItemWindow();
                    break;
                case VIEW_EVENT_SHOW_SEARCH:
                    showSearchView();
                    break;
                case VIEW_EVENT_SEARCH:
                    search((String) msg.obj);
                    break;
                case VIEW_EVENT_DISMISS_SEARCH:
                    dismissSearchView();
                    mainPageSearchView.cancelSearch();
//				dismissSoftInput();
                    break;
                case VIEW_EVENT_OPERATEPANEL: {
                    mainPageMenuView.closeModifiers();

                    PrinterDevice printer = App.instance.getCahierPrinter();
                    List<Map<String, String>> taxMap = OrderDetailTaxSQL
                            .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig()
                                    .getIncludedTax().getTax(), currentOrder);

                    ArrayList<PrintOrderItem> orderItems = ObjectFactory
                            .getInstance().getItemList(
                                    OrderDetailSQL.getOrderDetails(currentOrder
                                            .getId()));
                    OrderBill orderBill = OrderBillSQL
                            .getOrderBillByOrder(currentOrder);
                    if (OrderDetailSQL
                            .getOrderDetailsCountUnPlaceOrder(currentOrder.getId()) > 0) {
                        UIHelp.showToast(context,
                                context.getResources().getString(R.string.place_before_print));
                    } else if (printer == null) {
                        UIHelp.showToast(
                                context, context.getResources().getString(R.string.setting_printer));
                    } else if (orderItems.size() > 0 && printer != null
                            && orderBill != null && orderBill.getBillNo() != null) {
                        int orderSplitCount = OrderSplitSQL.getOrderSplitsCountByOrder(currentOrder);
                        int orderDetailSplitCount = OrderDetailSQL.getOrderDetailSplitCountByOrder(currentOrder);
                        if (orderSplitCount != 0 && orderDetailSplitCount != 0) {
                            orderSplitPrintWindow.show(MainPage.this, currentOrder, handler);
                        } else {
//						RoundAmount roundAmount = ObjectFactory.getInstance()
//								.getRoundAmount(currentOrder, orderBill, App.instance.getLocalRestaurantConfig().getRoundType());
//						OrderHelper.setOrderTotalAlfterRound(currentOrder, roundAmount);

                            orderBill.setPrintTime(System.currentTimeMillis());
                            OrderBillSQL.updatePrintTime(orderBill);

                            PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                                    context);
                            printerLoadingDialog.setTitle(context.getResources().getString(R.string.bill_printing));
                            printerLoadingDialog.showByTime(3000);
                            PrinterTitle title = ObjectFactory.getInstance()
                                    .getPrinterTitle(
                                            App.instance.getRevenueCenter(),
                                            currentOrder,
                                            App.instance.getUser().getFirstName()
                                                    + App.instance.getUser()
                                                    .getLastName(),
                                            currentTable.getName(), 1, App.instance.getSystemSettings().getTrainType());

                            currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_UNPAY);
                            BigDecimal remainTotal = BH.getBD(currentOrder.getTotal());

                            OrderSQL.update(currentOrder);
                            ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                                    .getInstance().getItemModifierList(currentOrder, OrderDetailSQL.getOrderDetails(currentOrder
                                            .getId()));

                            List<OrderPromotion> orderPromotions = PromotionDataSQL.getPromotionDataOrOrderid(currentOrder.getId());

                            RoundAmount roundAmount = ObjectFactory.getInstance().getRoundAmount(currentOrder, orderBill, remainTotal, App.instance.getLocalRestaurantConfig().getRoundType());
                            App.instance.remoteBillPrint(printer, title, currentOrder,
                                    orderItems, orderModifiers, taxMap, null, roundAmount, orderPromotions);
//						handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
                        }
                    } else {
                        UIHelp.showToast(context, context.getResources().getString(R.string.no_items));
                    }
                }
                break;
                case VIEW_EVNT_GET_BILL_PRINT: {
                    TableInfo tables = (TableInfo) msg.obj;
                    if (tables.getStatus() != ParamConst.TABLE_STATUS_DINING)
                        return;
                    if (verifyTableRepeat(tables)) {
                        App.instance.addGettingBillNotification(tables);
                    }
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
                }
                break;
                case VIEW_EVENT_CLOSE_BILL: {
                    String changeNum;
                    HashMap<String, String> paymentMap = (HashMap<String, String>) msg.obj;
                    final Order paidOrder = OrderSQL.getOrder(Integer.valueOf(paymentMap.get("orderId")));
                    List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL
                            .getAllPaymentSettlementByPaymentId(Integer.valueOf(paymentMap.get("paymentId")));
                    KotSummary kotSummary = KotSummarySQL.getKotSummary(currentOrder.getId(), currentOrder.getNumTag());
                    if (kotSummary != null) {
                        kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
                        KotSummarySQL.update(kotSummary);
                    }

                    //  if (App.instance.getSystemSettings().isCashClosePrint()) {
                    PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                            context);
                    printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
                    printerLoadingDialog.showByTime(3000);
                    //       }

                    changeNum = paymentMap.get("changeNum");
                    if (!TextUtils.isEmpty(changeNum)) {
                        if (!(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + "0.00").equals(changeNum))
                            DialogFactory.changeDialogOrder(context, changeNum, new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    }
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    PrinterTitle title = ObjectFactory.getInstance()
                            .getPrinterTitle(
                                    App.instance.getRevenueCenter(),
                                    paidOrder,
                                    App.instance.getUser().getFirstName()
                                            + App.instance.getUser().getLastName(),
                                    currentTable.getName(), 1, App.instance.getSystemSettings().getTrainType());


                    ArrayList<PrintOrderItem> orderItems = ObjectFactory
                            .getInstance().getItemList(
                                    OrderDetailSQL.getOrderDetails(paidOrder
                                            .getId()));
                    List<Map<String, String>> taxMap = OrderDetailTaxSQL
                            .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), paidOrder);

                    ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                            .getInstance().getItemModifierList(paidOrder, OrderDetailSQL.getOrderDetails(paidOrder
                                    .getId()));

                    // ArrayList<OrderModifier> orderModifiers =
                    // OrderModifierSQL.getAllOrderModifierByOrderAndNormal(currentOrder);
                    OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
                            paidOrder, App.instance.getRevenueCenter());
                    RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderAndBill(currentOrder, orderBill);
                    if (orderItems.size() > 0 && printer != null) {
                        List<OrderPromotion> orderPromotions = PromotionDataSQL.getPromotionDataOrOrderid(currentOrder.getId());


                        //  if (App.instance.getSystemSettings().isCashClosePrint()) {

                        App.instance.remoteBillPrint(printer, title, paidOrder,
                                orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount, orderPromotions);
//                        } else {
//                            App.instance.kickOutCashDrawer(printer);
//                        }

                    }
                    // remove get bill notification
                    removeNotificationTables();
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
//				final Order sendOrder = currentOrder;
                    /**
                     通知桌面系统 菜已经做完
                     */
                    TcpUdpFactory.sendUdpMsg(100, "{\"id\":" + paidOrder.getId().intValue() + ",\"status\":" + paidOrder.getOrderStatus().intValue() + "}", null);
                    /**
                     * 给后台发送log 信息
                     */
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                            if (cloudSync != null) {

                                cloudSync.syncOrderInfoForLog(paidOrder.getId(),
                                        App.instance.getRevenueCenter().getId(),
                                        App.instance.getBusinessDate(), 1);
                                if (paidOrder.getAppOrderId() != null && paidOrder.getAppOrderId().intValue() != 0) {
                                    AppOrder appOrder = AppOrderSQL.getAppOrderById(paidOrder.getAppOrderId().intValue());
                                    appOrder
                                            .setOrderStatus(ParamConst.APP_ORDER_STATUS_COMPLETED);
                                    appOrder.setOrderNo(paidOrder.getOrderNo());
                                    AppOrderSQL.updateAppOrder(appOrder);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                                                    context);
                                            printerLoadingDialog.setTitle(context.getResources().getString(
                                                    R.string.receipt_printing));
                                            printerLoadingDialog.showByTime(3000);
                                        }
                                    });
//								App.instance.printerAppOrder(appOrder);
                                    cloudSync.checkAppOrderStatus(
                                            App.instance.getRevenueCenter().getId().intValue(),
                                            appOrder.getId().intValue(),
                                            appOrder.getOrderStatus().intValue(), "",
                                            App.instance.getBusinessDate().longValue(), appOrder.getOrderNo());
                                }
                            }
//						if(!TextUtils.isEmpty(App.instance.getCallAppIp()))
//							SyncCentre.getInstance().callAppNo(App.instance, paidOrder.getOrderNo().toString());
                        }
                    }).start();
                    break;
                }
                case VIEW_EVENT_CLOSE_SPLIT_BILL: {
                    PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                            context);
                    printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
                    printerLoadingDialog.showByTime(3000);
                    HashMap<String, String> paymentMap = (HashMap<String, String>) msg.obj;
                    OrderSplit paidOrderSplit = OrderSplitSQL.get(Integer.valueOf(paymentMap.get("orderSplitId")));
                    List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL
                            .getAllPaymentSettlementByPaymentId(Integer.valueOf(paymentMap.get("paymentId")));
//				KotSummary kotSummary = KotSummarySQL.getKotSummary(currentOrder.getId());
//				if(kotSummary != null){
//					kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
//					KotSummarySQL.update(kotSummary);
//				}

                    OrderBill orderBill = ObjectFactory.getInstance().getOrderBillByOrderSplit(paidOrderSplit, App.instance.getRevenueCenter());
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
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    PrinterTitle title = ObjectFactory.getInstance()
                            .getPrinterTitleByOrderSplit(
                                    App.instance.getRevenueCenter(),
                                    currentOrder,
                                    paidOrderSplit,
                                    App.instance.getUser().getFirstName()
                                            + App.instance.getUser().getLastName(),
                                    currentTable.getName(), orderBill, App.instance.getBusinessDate().toString(), 1);
                    ArrayList<OrderDetail> orderSplitDetails = (ArrayList<OrderDetail>) OrderDetailSQL.getOrderDetailsByOrderAndOrderSplit(paidOrderSplit);


                    ArrayList<PrintOrderItem> orderItems = ObjectFactory
                            .getInstance().getItemList(orderSplitDetails);
                    List<Map<String, String>> taxMap = OrderDetailTaxSQL
                            .getOrderSplitTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), paidOrderSplit);

                    ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                            .getInstance().getItemModifierList(currentOrder, orderSplitDetails);

                    // ArrayList<OrderModifier> orderModifiers =
                    // OrderModifierSQL.getAllOrderModifierByOrderAndNormal(currentOrder);
                    Order temporaryOrder = new Order();
                    temporaryOrder.setPersons(paidOrderSplit.getPersons());
                    temporaryOrder.setSubTotal(paidOrderSplit.getSubTotal());
                    temporaryOrder.setDiscountAmount(paidOrderSplit.getDiscountAmount());
                    temporaryOrder.setTotal(paidOrderSplit.getTotal());
                    temporaryOrder.setTaxAmount(paidOrderSplit.getTaxAmount());
                    temporaryOrder.setOrderNo(currentOrder.getOrderNo());
                    if (orderItems.size() > 0 && printer != null) {
                        RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(paidOrderSplit, orderBill);
                        //  List<OrderPromotion>  orderPromotions= PromotionDataSQL.getPromotionDataOrOrderid(currentOrder.getId());

                        App.instance.remoteBillPrint(printer, title, temporaryOrder,
                                orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount, null);
                    }
                    // remove get bill notification
                    removeNotificationTables();
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
                    setData();
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
                case VIEW_EVENT_SHOW_DISCOUNT_WINDOW: {
                    verifyDialog.show(HANDLER_MSG_OBJECT_DISCOUNT,
                            msg.obj);
                    break;
                }
                case VIEW_EVENT_SHOW_MODIFY_QUANTITY_WINDOW: {
                    Map<String, Object> qtyMap = (Map<String, Object>) msg.obj;
                    showModifyQuantityWindow(
                            Integer.parseInt(qtyMap.get("quantity").toString()),
                            (DismissCall) qtyMap.get("dismissCall"));
                    break;
                }
                case VIEW_EVENT_ADD_ORDER_DETAIL:
                    addOrderDetail((OrderDetail) msg.obj);
                    if (msg.arg1 > 0) { // When need refresh Menu List
                        mainPageMenuView.refreshAllMenu();
                    }
                    break;
                case VIEW_EVENT_SET_DATA:
                    if (currentTable.getPosId() < 0) {
                        setDataWaitingList();
                    } else {
                        setData();
                        if (msg.arg1 > 0) { // When need refresh Menu List
                            mainPageMenuView.refreshAllMenu();
                        }
                    }
                    break;
                case VIEW_EVENT_SET_DATA_AND_CLOSE_MODIFIER:
                    if (currentTable.getPosId() < 0) {
                        setDataWaitingList();
                    } else {
                        setData();
                    }
                    if (mainPageMenuView.isModifierOpen()) {
                        mainPageMenuView.closeModifiers();
                    }
                    break;
                case DISMISS_SOFT_INPUT:
                    dismissSoftInput();
                    break;
                case VIEW_EVENT_OPEN_MODIFIERS: {
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    openModifiers((OrderDetail) map.get("orderDetail"),
                            (List<ItemModifier>) map.get("itemModifiers"), (View) map.get("view"));
                    break;
                }

                case JavaConnectJS.ACTION_CLICK_TABLE: {
                    currentTable = (TableInfo) msg.obj;
                    boolean fromThisRVC = checkIfTableFromThisRVC(currentTable);
                    if (!fromThisRVC) {
                        return;
                    }
                    TableInfo tableInfo = TableInfoSQL.getTableById(currentTable.getPosId());

                    if (tableInfo.getIsLocked() == 1) {
                        ToastUtils.showToast(MainPage.this, "Transfer table on Going");
                        return;
                    }
                    if (currentTable != null) {
                        boolean isValid = false;
                        if (currentTable.getStatus() != null) {
                            if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                isValid = true;
                            }
                        } else {
                            if (currentTable.getPosId() < 0) {
                                isValid = true;
                            }
                        }
                        if (isValid) {
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    KotSummary kotSummary = KotSummarySQL.getKotSummaryByTable(currentTable.getPosId());
                                    if (kotSummary != null) {
                                        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByKotSummaryIdUndone(kotSummary);
                                        for (KotItemDetail kotItemDetail : kotItemDetails) {
                                            kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                                            KotItemDetailSQL.update(kotItemDetail);
                                        }
                                        kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
                                        KotSummarySQL.update(kotSummary);
                                    }
                                }
                            }).start();
                            if (App.instance.getSystemSettings().isOfPax()) {
                                setPAXWindow.show(SetPAXWindow.GENERAL_ORDER, context.getResources().getString(R.string.no_of_pax));
                            } else {
                                setTablePacks("4");
                                if (loadingDialog != null && loadingDialog.isShowing())
                                    loadingDialog.dismiss();
                                if (currentTable.getPosId() < 0) {
                                    closeTables();
                                    setDataWaitingList();
                                } else {
                                    closeTables();
                                    setData();
                                }
                            }
                        } else {
                            handler.sendMessage(handler
                                    .obtainMessage(MainPage.VIEW_EVENT_DISMISS_TABLES));
                        }
                    }
                }
                break;
                case JavaConnectJS.ACTION_CLICK_TABLE_TRANSFER: {
                    final TableInfo newTable = (TableInfo) msg.obj;
                    boolean showDialog = true;
                    if (newTable.getPosId().intValue() == oldTable.getPosId()
                            .intValue()) {
                        currentTable = newTable;
                        boolean fromThisRVC = checkIfTableFromThisRVC(newTable);
                        if (fromThisRVC) {
                            showDialog = false;
                            if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                setPAXWindow.show(SetPAXWindow.GENERAL_ORDER, context.getResources().getString(R.string.no_of_pax));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(MainPage.VIEW_EVENT_DISMISS_TABLES));
                            }
                        }
                    }
                    if (showDialog) {
                        DialogFactory.commonTwoBtnDialog(
                                context,
                                context.getResources().getString(R.string.table_transfer),
                                context.getResources().getString(R.string.ask_transfer_table, oldTable.getName(), newTable.getName()),
                                context.getResources().getString(R.string.no),
                                context.getResources().getString(R.string.yes), null, new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        loadingDialog.show();
                                        handler.sendMessage(handler
                                                .obtainMessage(
                                                        ACTION_TRANSFER_TABLE,
                                                        newTable));
                                    }
                                });
                    }
                }
                break;
                case JavaConnectJS.ACTION_CLICK_TABLE_ITEM: {
                    final TableInfo newTable = (TableInfo) msg.obj;
                    final boolean fromThisRVC = checkIfTableFromThisRVC(newTable);
                    boolean showDialog = true;
                    if (newTable.getPosId().intValue() == oldTable.getPosId().intValue() &&
                            newTable.getRevenueId().intValue() == oldTable.getRevenueId().intValue()
                    ) {
                        showDialog = false;
                        currentTable = newTable;
                        if (fromThisRVC) {
                            if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                setPAXWindow.show(SetPAXWindow.GENERAL_ORDER, context.getResources().getString(R.string.no_of_pax));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(MainPage.VIEW_EVENT_DISMISS_TABLES));
                            }
                        }

                    }
                    if (showDialog) {
                        DialogFactory.commonTwoBtnDialog(
                                context,
                                context.getResources().getString(R.string.table_transfer),
                                context.getResources().getString(R.string.ask_transfer_table, oldTable.getName() + ": " + transfItemOrderDetail.getItemName(), newTable.getName()),
                                context.getResources().getString(R.string.no),
                                context.getResources().getString(R.string.yes), null, new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        setPAXWindow.showForTransferItem(SetPAXWindow.TRANSFER_ITEM, context.getResources().getString(R.string.no_of_pax), newTable);
//									handler.sendMessage(handler
//											.obtainMessage(
//													ACTION_TRANSFER_ITEM,
//													newTable));
                                    }
                                });
                    }
                }
                break;
                case JavaConnectJS.ACTION_CLICK_TABLE_APPORDER: {
                    Gson gson = new Gson();
                    String json = (String) msg.obj;
                    JSONObject jsonject;
                    try {
                        jsonject = new JSONObject(json);
                        currentTable = gson.fromJson(jsonject.getString("table"),
                                new TypeToken<TableInfo>() {
                                }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (currentTable != null) {
                        boolean fromThisRVC = checkIfTableFromThisRVC(currentTable);
                        if (fromThisRVC) {
                            if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        KotSummary kotSummary = KotSummarySQL.getKotSummaryByTable(currentTable.getPosId().intValue());
                                        if (kotSummary != null) {
                                            List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByKotSummaryIdUndone(kotSummary);
                                            for (KotItemDetail kotItemDetail : kotItemDetails) {
                                                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                                                KotItemDetailSQL.update(kotItemDetail);
                                            }
                                            kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
                                            KotSummarySQL.update(kotSummary);
                                        }
                                    }
                                }).start();
                                setPAXWindow.show(SetPAXWindow.APP_ORDER, context.getResources().getString(R.string.no_of_pax));
                            } else {
                                DialogFactory.showOneButtonCompelDialog(context, getString(R.string.warning), getString(R.string.please_select_empty_table), null);
                            }
                        } else {
                            transferOrder(currentTable, ACTION_TRANSFER_TABLE);
                            handler.sendMessage(handler
                                    .obtainMessage(VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER));
                        }
                    }
                }
                break;

                case ACTION_TRANSFER_TABLE: {
                    currentTable = (TableInfo) msg.obj;
                    final boolean fromThisRVC = checkIfTableFromThisRVC(currentTable);
                    if (currentTable != null) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingDialog != null)
                                    loadingDialog.show();
                            }
                        });

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                    OrderBill orderBill = OrderBillSQL
                                            .getOrderBillByOrder(currentOrder);

                                    if (orderBill != null
                                            && orderBill.getBillNo() != null) {
                                        if (fromThisRVC) {
                                            currentOrder.setTableId(currentTable
                                                    .getPosId());
                                            OrderSQL.update(currentOrder);
                                            List<KotSummary> kotSummaryList = KotSummarySQL.getKotSummaryForTransfer(currentOrder.getId(), currentOrder.getNumTag());
                                            if (kotSummaryList != null) {

                                                for (KotSummary fromKotSummary : kotSummaryList) {

                                                    fromKotSummary.setTableName(currentTable
                                                            .getName());
                                                    Map<String, Object> parameters = new HashMap<String, Object>();
                                                    parameters.put("fromOrder", currentOrder);
                                                    parameters.put("fromTableName",
                                                            oldTable.getName());
                                                    parameters.put("toTableName",
                                                            currentTable.getName());
                                                    parameters.put("action",
                                                            ParamConst.JOB_TRANSFER_KOT);

                                                    App.instance
                                                            .getKdsJobManager()
                                                            .transferTableDownKot(
                                                                    ParamConst.JOB_TRANSFER_KOT,
                                                                    null, fromKotSummary,
                                                                    parameters);


                                                }
                                            }
                                        } else {
                                            transferOrder(currentTable, ACTION_TRANSFER_TABLE);
                                        }

                                    } else {
                                        transferOrder(currentTable, ACTION_TRANSFER_TABLE);
                                    }
                                    handler.sendMessage(handler
                                            .obtainMessage(VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER));
                                } else {
                                    oldOrder = currentOrder;
                                    OrderBill orderBill = OrderBillSQL
                                            .getOrderBillByOrder(oldOrder);
                                    if (orderBill != null
                                            && orderBill.getBillNo() != null) {
                                        if (fromThisRVC) {
                                            initOrder(currentTable);
                                            KotSummary toKotSummary = KotSummarySQL
                                                    .getKotSummary(currentOrder.getId(), currentOrder.getNumTag());
                                            if (toKotSummary == null) {
                                                toKotSummary = ObjectFactory
                                                        .getInstance()
                                                        .getKotSummary(
                                                                currentTable.getName(),
                                                                currentOrder,
                                                                App.instance
                                                                        .getRevenueCenter(),
                                                                App.instance
                                                                        .getBusinessDate());
                                                KotSummarySQL.update(toKotSummary);
                                            }
                                            KotSummary fromKotSummary = KotSummarySQL
                                                    .getKotSummary(oldOrder.getId(), oldOrder.getNumTag());
                                            Map<String, Object> parameters = new HashMap<String, Object>();
                                            parameters.put("action",
                                                    ParamConst.JOB_MERGER_KOT);
                                            parameters.put("fromOrder", oldOrder);
                                            parameters.put("fromTableName",
                                                    oldTable.getName());
                                            parameters.put("toTableName",
                                                    currentTable.getName());
                                            parameters.put("currentTableId",
                                                    currentTable.getPosId());
                                            mergerOrder();
                                            App.instance.getKdsJobManager()
                                                    .transferTableDownKot(
                                                            ParamConst.JOB_MERGER_KOT,
                                                            toKotSummary,
                                                            fromKotSummary, parameters);
                                        } else {
                                            mergerOrder();
                                            handler.sendMessage(handler
                                                    .obtainMessage(VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER));
                                        }
                                    } else {
                                        mergerOrder();
                                        if (!fromThisRVC)
                                            handler.sendMessage(handler
                                                    .obtainMessage(VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER));
                                        else
                                            handler.sendMessage(handler
                                                    .obtainMessage(VIEW_EVENT_DISMISS_TABLES_AFTER_MERGER));

                                    }

                                }
                                if (oldTable != null) {
                                    if (checkIfTableFromThisRVC(oldTable)) {
                                        oldTable.setStatus(ParamConst.TABLE_STATUS_IDLE);
                                        TableInfoSQL.updateTables(oldTable);
                                    }
//                                    else {
//                                        TableInfoSQL.updateTables(currentTable);
//                                    }
                                }

                            }
                        }).start();
                    }
                }
                break;
                case ACTION_TRANSFER_ITEM: {
                    currentTable = (TableInfo) msg.obj;
                    final boolean fromThisRVC = checkIfTableFromThisRVC(currentTable);
                    if (currentTable != null) {

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (fromThisRVC) {
                                    oldOrder = currentOrder;
                                    if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                        currentOrder = null;
                                    } else {
                                        //merge order
                                        currentOrder = OrderSQL.getUnfinishedOrderAtTable(currentTable.getPosId().intValue(), App.instance.getBusinessDate(), App.instance.getSessionStatus());

                                        if (currentOrder != null) {
                                            TableInfo oldTable = TableInfoSQL.getTableById(currentOrder.getTableId().intValue());
                                            currentTable.setPacks(currentTable.getPacks() + oldTable.getPacks());
                                        }
                                    }

                                    initOrder(currentTable);
                                    setTablePacks(currentTable.getPacks() + "");
                                    boolean idNull = false;
                                    transfItemOrderDetail.setOrderId(currentOrder.getId().intValue());
                                    transfItemOrderDetail.setGroupId(0);
                                    transfItemOrderDetail.setOrderSplitId(0);
                                    if (transfItemOrderDetail.getId() == null) {
                                        transfItemOrderDetail = ObjectFactory.getInstance().cpOrderDetail(transfItemOrderDetail);

                                        OrderDetail oldOrderDetail = OrderDetailSQL.getOrderDetail(transfItemOrderDetail.getTransferFromDetailId());
                                        if (oldOrderDetail != null) {
                                            oldOrderDetail.setItemNum(oldOrderDetail.getItemNum().intValue() - transfItemOrderDetail.getItemNum().intValue());
                                            OrderDetailSQL.updateOrderDetailAndOrder(oldOrderDetail);
                                        }
                                        idNull = true;
                                    } else {
                                        OrderDetailTaxSQL.deleteOrderDetailTax(transfItemOrderDetail);
                                        OrderDetailSQL.updateOrderDetail(transfItemOrderDetail);
                                        OrderSQL.updateOrder(oldOrder);
                                        OrderSQL.updateOrder(currentOrder);
                                        idNull = false;
                                    }

                                    if (transfItemOrderDetail.getOrderDetailStatus().intValue() > ParamConst.ORDERDETAIL_STATUS_ADDED) {
                                        //region after place order
                                        OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(currentOrder, App.instance.getRevenueCenter());
                                        KotSummary toKotSummary = KotSummarySQL
                                                .getKotSummary(currentOrder.getId(), currentOrder.getNumTag());
                                        if (toKotSummary == null) {
                                            toKotSummary = ObjectFactory
                                                    .getInstance()
                                                    .getKotSummary(
                                                            currentTable.getName(),
                                                            currentOrder,
                                                            App.instance
                                                                    .getRevenueCenter(),
                                                            App.instance
                                                                    .getBusinessDate());
                                            KotSummarySQL.update(toKotSummary);
                                        }
                                        KotSummary fromKotSummary = KotSummarySQL
                                                .getKotSummary(oldOrder.getId(), oldOrder.getNumTag());
                                        KotItemDetail kotItemDetail = KotItemDetailSQL.getMainKotItemDetailByOrderDetailId(fromKotSummary.getId(), transfItemOrderDetail.getTransferFromDetailId());
                                        if (idNull) {
                                            kotItemDetail = ObjectFactory.getInstance().cpKotItemDetail(kotItemDetail, transfItemOrderDetail);
                                        }
                                        Map<String, Object> parameters = new HashMap<String, Object>();
                                        parameters.put("action",
                                                ParamConst.JOB_MERGER_KOT);
                                        parameters.put("fromOrder", oldOrder);
                                        parameters.put("fromTableName",
                                                oldTable.getName());
                                        parameters.put("toTableName",
                                                currentTable.getName());
                                        parameters.put("currentTableId",
                                                currentTable.getPosId());

                                        kotItemDetail.setKotSummaryId(toKotSummary.getId().intValue());
                                        kotItemDetail.setOrderId(toKotSummary.getOrderId().intValue());
                                        kotItemDetail.setKotSummaryUniqueId(toKotSummary.getUniqueId());

                                        KotItemDetailSQL.update(kotItemDetail);
                                        int surplusCount = OrderDetailSQL.getNoVoidCountByOrderId(oldOrder.getId());
                                        if (surplusCount == 0) {
                                            OrderDetailSQL.deleteOrderDetailByOrder(oldOrder);
                                            KotSummarySQL.deleteKotSummaryByOrder(oldOrder);
                                            OrderBillSQL.deleteOrderBillByOrder(oldOrder);
                                            OrderSQL.deleteOrder(oldOrder);
                                            TableInfo tables = TableInfoSQL.getTableById(oldOrder.getTableId().intValue());
                                            tables.setStatus(ParamConst.TABLE_STATUS_IDLE);
                                            if (checkIfTableFromThisRVC(tables)) {
                                                TableInfoSQL.updateTables(tables);
                                            }
                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("tableId", tables.getPosId().intValue());
                                                jsonObject.put("status", ParamConst.TABLE_STATUS_IDLE);
                                                jsonObject.put("RX", RxBus.RX_REFRESH_TABLE);
                                                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                                                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        App.instance.getKdsJobManager()
                                                .transferItemDownKot(
                                                        toKotSummary,
                                                        fromKotSummary, parameters, kotItemDetail);

                                        //endregion
                                    }
                                } else {
                                    transferItemOtherRVC();
                                }
                                handler.sendEmptyMessage(VIEW_EVENT_DISMISS_TABLES);
                            }
                        }).start();
                    }
                }
                break;
//			case JavaConnectJS.ACTION_LOAD_TABLES:
//				getPlaces();
//				break;
//			case JavaConnectJS.ACTION_CLICK_BACK:
//
//				onBackPressed();
//				break;
                case JavaConnectJS.ACTION_CLICK_REFRESH:
//				web_tables
//						.loadUrl("javascript:JsConnectAndroid('RefreshTables','"
//								+ getJsonStr() + "')");
//				LogUtil.i(TAG, "javascript:JsConnectAndroid('RefreshTables','"
//						+ getJsonStr() + "')");
                    break;
                case VIEW_EVENT_PLACE_ORDER:
                    mainPageMenuView.closeModifiers();
                    break;
                case VIEW_EVENT_CLOSE_MODIFIER_VIEW:
                    mainPageMenuView.closeModifiers();
                    break;
                case VIEW_EVENT_SELECT_BILL:
                    currentTable = (TableInfo) msg.obj;
//				Tables tables = (Tables) msg.obj;
                    if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                        return;
                    }
                    handler.sendMessage(handler
                            .obtainMessage(MainPage.VIEW_EVENT_DISMISS_TABLES));
                    App.instance.removeGettingBillNotification(currentTable);
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
                    break;
                case VerifyDialog.DIALOG_DISMISS:

                    break;
                case VerifyDialog.DIALOG_RESPONSE: {
                    Map<String, Object> result = (Map<String, Object>) msg.obj;
                    User user = (User) result.get("User");
                    if (result.get("MsgObject").equals(HANDLER_MSG_OBJECT_DISCOUNT)) {
                        Map<String, Object> map = (Map<String, Object>) result
                                .get("Object");
                        ObjectFactory.getInstance().getReportDiscount(currentTable,
                                currentOrder, user,
                                App.instance.getRevenueCenter(),
                                App.instance.getBusinessDate());
                        showDiscountWindow((Order) map.get("order"),
                                (OrderDetail) map.get("orderDetail"),
                                (ResultCall) map.get("resultCall"));
                    } else if (result.get("MsgObject").equals(
                            PAMENT_METHOD)) {
                        if (closeOrderWindow.isShowing()) {
                            closeOrderWindow.setUser(user);
                            closeOrderWindow.openMoneyKeyboard(View.GONE,
                                    ParamConst.SETTLEMENT_CUSTOM_PAYMENT);
                        } else if (closeOrderSplitWindow.isShowing()) {
                            closeOrderSplitWindow.setUser(user);
                            closeOrderSplitWindow.openMoneyKeyboard(View.GONE,
                                    ParamConst.SETTLEMENT_CUSTOM_PAYMENT);
                        }
                    } else if (result.get("MsgObject").equals(
                            HANDLER_MSG_OBJECT_BILL_ON_HOLD)) {
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
                            HANDLER_MSG_OBJECT_VOID)) {
                        if (!verifyDialog.isShowing()) {
                            if (closeOrderWindow.isShowing()) {
                                closeOrderWindow.setUser(user);
                                closeOrderWindow.openMoneyKeyboardByVoidRefund();
                            } else if (closeOrderSplitWindow.isShowing()) {
                                closeOrderSplitWindow.setUser(user);
                                closeOrderSplitWindow.openMoneyKeyboardByVoidRefund();
                            }
                        }
                    } else if (result.get("MsgObject").equals(
                            HANDLER_MSG_OBJECT_ENTERTAINMENT)) {
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
                    } else if (result.get("MsgObject").equals(
                            HANDLER_MSG_OBJECT_TRANSFER_TABLE)) {
                        if (!verifyDialog.isShowing()) {
                            tableShowAction = TRANSFER_TABLE;
                            activityRequestCode = 0;
                            showTables();
                        }
                    } else if (result.get("MsgObject").equals(
                            HANDLER_MSG_OBJECT_TRANSFER_ITEM)) {
                        if (!verifyDialog.isShowing()) {
                            if (transfItemOrderDetail != null
                                    && transfItemOrderDetail.getItemNum() != null) {
                                if (transfItemOrderDetail.getItemNum().intValue() > 1) {
                                    DialogFactory.commonTwoBtnDialog(context, context.getString(R.string.warning), getString(R.string.transfer), getString(R.string.split_pay), getString(R.string.all_pay), new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (transfItemOrderDetail.getItemNum().intValue() == 2) {
                                                transfItemOrderDetail.setItemNum(1);
                                                transfItemOrderDetail.setId(null);
                                                tableShowAction = TRANSFER_ITEM;
                                                activityRequestCode = 0;
                                                showTables();
                                            } else {
                                                int maxNum = transfItemOrderDetail.getItemNum().intValue();
                                                setPAXWindow.show(SetPAXWindow.TRANSFER_ITEM_SPLIT, "1", getString(R.string.amount_less_than) + " " + maxNum, maxNum);
                                            }
                                        }
                                    }, new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            tableShowAction = TRANSFER_ITEM;
                                            activityRequestCode = 0;
                                            showTables();
                                        }
                                    });
                                } else {
                                    tableShowAction = TRANSFER_ITEM;
                                    activityRequestCode = 0;
                                    showTables();
                                }
                            }
                        }
                    } else if (result.get("MsgObject").equals(
                            HANDLER_MSG_OBJECT_VOID_OR_FREE)) {
                        Map<String, Object> voidOrFreeMap = (Map<String, Object>) result
                                .get("Object");
                        int type = (Integer) voidOrFreeMap.get("type");
                        final OrderDetail orderDetail = (OrderDetail) voidOrFreeMap
                                .get("orderDetail");
                        if (type == ParamConst.ORDERDETAIL_TYPE_VOID) {
                            // voidORFreeWindow.voidItem();
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    OrderDetailSQL.setOrderDetailToVoidOrFree(
                                            orderDetail,
                                            ParamConst.ORDERDETAIL_TYPE_VOID);
                                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
                                    String kotCommitStatus = ParamConst.JOB_VOID_KOT;
                                    KotSummary kotSummary = KotSummarySQL.getKotSummary(orderDetail
                                            .getOrderId(), "");
                                    KotItemDetail kotItemDetail = KotItemDetailSQL.getMainKotItemDetailByOrderDetailId(kotSummary.getId(), orderDetail.getId());
                                    kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_VOID);

                                    KotItemDetailSQL.update(kotItemDetail);
                                    ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                                    kotItemDetails.add(kotItemDetail);
                                    OrderDetail freeOrderDetail = OrderDetailSQL
                                            .getOrderDetail(
                                                    orderDetail.getOrderId(),
                                                    orderDetail);
                                    // OrderDetailSQL.deleteOrderDetail(freeOrderDetail);
                                    if (freeOrderDetail != null) {
                                        KotItemDetail freeKotItemDetail = KotItemDetailSQL
                                                .getMainKotItemDetailByOrderDetailId(kotSummary.getId(), freeOrderDetail
                                                        .getId());
                                        freeKotItemDetail.setKotStatus(ParamConst.KOT_STATUS_VOID);
                                        KotItemDetailSQL.update(freeKotItemDetail);
                                        kotItemDetails.add(freeKotItemDetail);
                                    }

                                    //: fix issue: kot print no modifier showup
                                    // look for kot modifiers
                                    Order placedOrder = OrderSQL.getOrder(orderDetail.getOrderId());
                                    ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
                                    ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                                            .getOrderModifiers(placedOrder, orderDetail);
                                    for (OrderModifier orderModifier : orderModifiers) {
                                        if (orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
                                            Modifier mod = CoreData.getInstance().getModifier(orderModifier.getModifierId());
                                            if (mod != null) {
                                                KotItemModifier kotItemModifier = KotItemModifierSQL
                                                        .getKotItemModifier(kotItemDetail.getId(), mod.getId());
                                                if (kotItemModifier != null)
                                                    kotItemModifiers.add(kotItemModifier);
                                            }
                                        }
                                    }
                                    //end fix

                                    Map<String, Object> orderMap = new HashMap<String, Object>();
                                    ArrayList<Integer> orderDetailIds = new ArrayList<Integer>();
                                    orderDetailIds.add(orderDetail.getId());
                                    orderMap.put("orderId", orderDetail.getOrderId());
                                    orderMap.put("orderDetailIds", orderDetailIds);
                                    App.instance.getKdsJobManager().tearDownKot(
                                            kotSummary, kotItemDetails,
                                            kotItemModifiers,
                                            kotCommitStatus, orderMap);
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("orderId", orderDetail.getOrderId().intValue());
                                        jsonObject.put("RX", RxBus.RX_REFRESH_ORDER);
                                        TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else if (type == ParamConst.ORDERDETAIL_TYPE_FREE) {
                            // voidORFreeWindow.freeItem();
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    OrderDetailSQL.setOrderDetailToVoidOrFree(
                                            orderDetail,
                                            ParamConst.ORDERDETAIL_TYPE_FREE);
                                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
                                }
                            }).start();
                        }
//				}else if(result.get("MsgObject")
//						.equals(HANDLER_MSG_OBJECT_STORED_CARD_REFUND)){
//					if(!f_stored_card.isHidden()){
//						f_stored_card.storedCardRefund();
//					}
//
//				}else if(result.get("MsgObject")
//						.equals(HANDLER_MSG_OBJECT_STORED_CARD_LOSS)){
//					if(!f_stored_card.isHidden()){
//						f_stored_card.storedCardLoss();
//					}
//				}else if(result.get("MsgObject")
//						.equals(HANDLER_MSG_OBJECT_STORED_CARD_REPLACEMENT)){
//					if(!f_stored_card.isHidden()){
//						f_stored_card.storedCardReplacement();
//					}
                    } else if (result.get("MsgObject").equals(MainPage.HANDLER_MSG_OBJECT_OPEN_DRAWER)) {
                        User openUser = user;
                        ObjectFactory.getInstance().getUserOpenDrawerRecord(App.instance.getRevenueCenter().getRestaurantId().intValue(),
                                App.instance.getRevenueCenter().getId().intValue(),
                                openUser,
                                App.instance.getUser().getId().intValue(),
                                App.instance.getSessionStatus().getSession_status());
//					settingView.openDrawer();
                        App.instance.kickOutCashDrawer(App.instance.getCahierPrinter());
                    } else if (result.get("MsgObject").equals(HANDLER_MSG_OBJECT_CANCEL_ITEM)) {
                        OrderDetail tag = (OrderDetail) result.get("Object");
                        removeItem(tag);
                    }
                }
                break;
                case VIEW_EVENT_SHOW_BILL_ON_HOLD:
                    verifyDialog.show(HANDLER_MSG_OBJECT_BILL_ON_HOLD, null);
                    break;
                case VIEW_EVENT_SHOW_VOID:
                    verifyDialog.show(HANDLER_MSG_OBJECT_VOID, null);
                    break;
                case VIEW_EVENT_SHOW_ENTERTAINMENT:
                    verifyDialog.show(HANDLER_MSG_OBJECT_ENTERTAINMENT, null);
                    break;
                case VIEW_EVENT_TANSFER_TABLE:
                    verifyDialog.show(HANDLER_MSG_OBJECT_TRANSFER_TABLE, null);
                    break;
                case VIEW_EVENT_TANSFER_ITEM: {
                    transfItemOrderDetail = (OrderDetail) msg.obj;
                    oldTransItemOrderDetail = (OrderDetail) msg.obj;
                    transfItemOrderDetail.setTransferFromDetailId(transfItemOrderDetail.getId().intValue());
                    transfItemOrderDetail.setTransferFromDetailNum(transfItemOrderDetail.getItemNum().intValue());
                    verifyDialog.show(HANDLER_MSG_OBJECT_TRANSFER_ITEM, null);
                }
                break;
                case VIEW_EVENT_KICK_CASHDRAWER: {
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    if (printer == null) {
                        AlertToDeviceSetting.noKDSorPrinter(context,
                                context.getResources().getString(R.string.no_set_kds_printer));
                    } else {
                        App.instance.kickOutCashDrawer(printer);
                    }
                    break;
                }
                case ACTION_SWITCH_USER:
                    UIHelp.startLogin(context);
                    MainPage.this.finish();
                    break;
                case ResultCode.CONNECTION_FAILED:
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context,
                            (Throwable) msg.obj, context.getResources().getString(R.string.server)));
                    break;
                case KOT_PRINT_NULL:
                    loadingDialog.dismiss();
                    if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                        printerLoadingDialog.dismiss();
                    }
                    UIHelp.showToast(context, context.getResources().getString(R.string.no_set_kds_printer));
                    break;
                case KOT_ITEM_PRINT_NULL: {
                    String itemName = (String) msg.obj;
                    loadingDialog.dismiss();
                    if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                        printerLoadingDialog.dismiss();
                    }


                    if (App.instance.isRevenueKiosk() && !App.instance.getSystemSettings().isPrintBill()) {
                        return;
                    } else {
                        UIHelp.showToast(context, String.format(Locale.US, context.getResources().getString(R.string.no_set_item_print), itemName));
                    }
                    //   UIHelp.showToast(context, String.format(context.getResources().getString(R.string.no_set_item_print), itemName));
                }
                break;
                // kot Print status
                case KOT_PRINT_FAILED: {
                    loadingDialog.dismiss();
                    if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                        printerLoadingDialog.dismiss();
                    }
                    if (msg.obj instanceof String && "KDS".equals(msg.obj)) {
                        UIHelp.showToast(context, "Please log in KDS");
                    } else {

                        if (App.instance.isRevenueKiosk() && !App.instance.getSystemSettings().isPrintBill()) {
                            return;
                            //	UIHelp.showToast(context, String.format(context.getResources().getString(R.string.no_set_item_print), itemName));

                        } else {
                            UIHelp.showToast(context, context.getResources().getString(R.string.place_order_failed));
                        }
                        //   UIHelp.showToast(context, context.getResources().getString(R.string.place_order_failed));
                    }
                }
                break;
                case KOT_PRINT_SUCCEED:
                    // if(!orderDetails.isEmpty()){
                    // List<OrderDetail> orderDetailUnDone =
                    // OrderDetailSQL.getPartOrderDetailByOrderIdAndOrderDetailStatus(currentOrder.getId());
                    // if(!orderDetailUnDone.isEmpty()){
                    // for(OrderDetail orderDetail : orderDetailUnDone){
                    // orderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD);
                    // OrderDetailSQL.updateOrderDetail(orderDetail);
                    // }
                    // setData();
                    // UIHelp.showToast(context,"Place Order Success");
                    // }
                    // }
                    if (currentOrder != null
                            && ((Integer) msg.obj).intValue() == currentOrder
                            .getId().intValue()) {
                        setData();
                        if (App.instance.getSystemSettings().isAutoToTable()) {
                            if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                                printerLoadingDialog.dismiss();
                            }
                            if (!isShowTables) {
//                                showTables();
                                handler.sendEmptyMessage(VIEW_EVENT_SHOW_TABLES);
                            }
                        }
                    }
                    break;
                case VIEW_EVENT_SHOW_SPECIAL_INSTRACTIONS_WINDOW: {
                    final OrderDetail orderDetail = (OrderDetail) msg.obj;
                    specialInstractionsWindow.show(view_top_line, orderDetail);
//				DialogFactory.commonTwoBtnInputDialog(context, "Instruction", "", "Cancle", "Save", null, new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						EditText editText = (EditText) v;
//						final String specialInstractions = editText
//								.getText().toString();
//						new Thread(new Runnable() {
//
//							@Override
//							public void run() {
//								if (specialInstractions != null) {
//									orderDetail
//											.setSpecialInstractions(specialInstractions);
//									OrderDetailSQL.updateOrderDetail(orderDetail);
//									handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
//								}
//							}
//						}).start();
//					}
//				});
                }
                break;
                case ParamConst.JOB_TYPE_POS_TRANSFER_TABLE:
                    int id = (Integer) msg.obj;
                    TableInfo table = TableInfoSQL.getTableById(id);
                    transferOrder(table);
                    break;
                case VIEW_EVENT_TANSFER_PAX:
                    String pax = (String) msg.obj;
                    setPAXWindow.show(pax, currentOrder, context.getResources().getString(R.string.no_of_pax), orderDetails);
                    break;
                case VIEW_EVENT_VOID_OR_FREE:
                    verifyDialog.show(HANDLER_MSG_OBJECT_VOID_OR_FREE,
                            msg.obj);
                    break;
//			case StoredCardActivity.VIEW_EVENT_STORED_CARD_REFUND:
//				verifyDialog.show(HANDLER_MSG_OBJECT_STORED_CARD_REFUND, null);
//				break;
//			case StoredCardActivity.VIEW_EVENT_STORED_CARD_LOSS:
//				verifyDialog.show(HANDLER_MSG_OBJECT_STORED_CARD_LOSS, null);
//			break;
//			case StoredCardActivity.VIEW_EVENT_STORED_CARD_REPLACEMEN:
//				verifyDialog.show(HANDLER_MSG_OBJECT_STORED_CARD_REPLACEMENT, null);
//				break;
                case VIEW_SUB_MENU:
                    Map<String, Object> result = (Map<String, Object>) msg.obj;
                    int itemMainCategoryId = (Integer) result.get("itemMainCategoryId");
                    int itemCategoryId = (Integer) result.get("itemCategoryId");
                    mainPageMenuView.closeSubMenu(itemMainCategoryId, itemCategoryId);
                    break;
                case VIEW_SUB_MENU_ALL:
                    int mainCategoryId = (Integer) msg.obj;
                    mainPageMenuView.closeSubMenu(mainCategoryId);
                    break;
                case VIEW_EVENT_SHOW_CLOSE_SPLIT_BILL:
                    closeOrderSplitWindow.show(view_top_line, currentOrder, (OrderSplit) msg.obj);
                    break;
                case ORDER_TIME_OUT:
                    TableInfo mTables = (TableInfo) msg.obj;
                    sendNotification(mTables);
                    break;
                case VIEW_EVENT_TAKE_AWAY: {
                    OrderDetail orderDetail = (OrderDetail) msg.obj;
                    if (orderDetail.getIsTakeAway() != ParamConst.TAKE_AWAY) {
                        orderDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
                        if (orderDetail != null && !TextUtils.isEmpty(orderDetail.getSpecialInstractions())) {
                            orderDetail.setSpecialInstractions(orderDetail.getSpecialInstractions() + " " + context.getResources().getString(R.string.takeaway));
                        } else {
                            orderDetail.setSpecialInstractions(context.getResources().getString(R.string.takeaway));
                        }
                    } else {
                        orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
                        if (orderDetail != null && !TextUtils.isEmpty(orderDetail.getSpecialInstractions())) {
                            orderDetail.setSpecialInstractions(orderDetail.getSpecialInstractions().replace(context.getResources().getString(R.string.takeaway), "").replace(context.getResources().getString(R.string.takeaway), ""));
                        }
                    }
                    OrderHelper.getOrderDetailTax(currentOrder, orderDetail);
                    OrderDetailSQL.updateOrderDetailAndOrder(orderDetail);
                    if (orderDetail != null && orderDetail.getOrderDetailStatus() < ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                        handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
                    } else {
                        String kotCommitStatus = ParamConst.JOB_UPDATE_KOT;
                        KotSummary kotSummary = KotSummarySQL.getKotSummary(orderDetail
                                .getOrderId(), "");
                        KotItemDetail kotItemDetail = KotItemDetailSQL
                                .getMainKotItemDetailByOrderDetailId(kotSummary.getId(), orderDetail
                                        .getId());
                        kotItemDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
                        kotItemDetail.setSpecialInstractions(orderDetail.getSpecialInstractions());

                        kotSummary.setIsTakeAway(currentOrder.getIsTakeAway());
                        KotSummarySQL.update(kotSummary);
                        KotItemDetailSQL.update(kotItemDetail);
                        ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                        kotItemDetails.add(kotItemDetail);
                        Map<String, Object> orderMap = new HashMap<String, Object>();
                        ArrayList<Integer> orderDetailIds = new ArrayList<Integer>();
                        orderDetailIds.add(orderDetail.getId());
                        orderMap.put("orderId", orderDetail.getOrderId());
                        orderMap.put("orderDetailIds", orderDetailIds);
                        App.instance.getKdsJobManager().tearDownKot(
                                kotSummary, kotItemDetails,
                                KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetail.getId()),
                                kotCommitStatus, orderMap);
                    }
                }
                break;
                case VIEW_EVENT_SET_WEIGHT: {
                    OrderDetail orderDetail = (OrderDetail) msg.obj;
                    setWeightWindow.show(orderDetail);
                }
                break;
                case VIEW_EVENT_UNSEAT_ORDER:
                    unseat();
                    break;
                case VIEW_EVENT_FIRE: {
                    OrderDetailFireWindow orderDetailFireWindow = new OrderDetailFireWindow(MainPage.this, findViewById(R.id.lv_order), handler);
                    orderDetailFireWindow.show(currentOrder, handler);
                }
                break;
                case VIEW_EVENT_SPLIT_BY_PAX: {
                    List<OrderSplit> orderSplitList = OrderSplitSQL.getOrderSplits(currentOrder);
                    if (orderSplitList.size() > 0 && orderSplitList.get(0).getSplitByPax() == 0) {
                        UIHelp.showShortToast(context, context.getResources().getString(R.string.cannot_split_by_pax));
                        return;
                    }
                    if (currentOrder.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_UNPAY) {
                        setPAXWindow.show(SetPAXWindow.SPLITE_BY_PAX, context.getResources().getString(R.string.no_of_pax));
                    } else {
                        OrderBill orderBill = OrderBillSQL.getOrderBillByOrder(currentOrder);
                        if (App.instance.getSystemSettings().isPrintBeforCloseBill() || orderBill == null || orderBill.getBillNo() == null) {
                            UIHelp.showToast(context, context.getResources().getString(R.string.print_bill_));
                            return;
                        } else {
                            if (OrderDetailSQL
                                    .getOrderDetailsCountUnPlaceOrder(currentOrder.getId()) > 0) {
                                UIHelp.showToast(context,
                                        context.getResources().getString(R.string.place_before_print));
                                return;
                            }
                        }
                    }
                }
                break;
                case ACTION_PAX_SPLIT_BY_PAX: {
                    int splitPax = Integer.parseInt(((String) msg.obj));
                    OrderSplitSQL.deleteOrderSplitByOrderId(currentOrder.getId().intValue());
                    List<OrderSplit> orderSplitList = ObjectFactory.getInstance().getOrderSplitListForPax(currentOrder, splitPax);
                    selectOrderSplitDialog.show(orderSplitList, currentOrder, true);
                }
                break;
                case ACTION_PAX_SPLIT_BY_PAX_WINDOW: {
                    List<OrderSplit> orderSplitList = OrderSplitSQL.getOrderSplits(currentOrder);
                    selectOrderSplitDialog.show(orderSplitList, currentOrder, true);
                }
                break;
                case ACTION_PRINT_PAX_SPLIT_BY_PAX: {
                    PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                            context);
                    printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
                    printerLoadingDialog.showByTime(3000);
                    HashMap<String, String> paymentMap = (HashMap<String, String>) msg.obj;
                    OrderSplit paidOrderSplit = OrderSplitSQL.get(Integer.valueOf(paymentMap.get("orderSplitId")));
                    List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL
                            .getAllPaymentSettlementByPaymentId(Integer.valueOf(paymentMap.get("paymentId")));
                    OrderBill orderBill = ObjectFactory.getInstance().getOrderBillByOrderSplit(paidOrderSplit, App.instance.getRevenueCenter());

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


//                    if(App.instance.isRevenueKiosk()&&!App.instance.getSystemSettings().isPrintBill())
//                    {
//
//                        //	UIHelp.showToast(context, String.format(context.getResources().getString(R.string.no_set_item_print), itemName));
//
//                    }else {
//
//                        PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
//                                context);
//                        printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
//                        printerLoadingDialog.showByTime(3000);
//                    }
//                    PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
//                            context);
//                    printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
//                    printerLoadingDialog.showByTime(3000);
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    PrinterTitle title = ObjectFactory.getInstance()
                            .getPrinterTitleByOrderSplit(
                                    App.instance.getRevenueCenter(),
                                    currentOrder,
                                    paidOrderSplit,
                                    App.instance.getUser().getFirstName()
                                            + App.instance.getUser().getLastName(),
                                    currentTable.getName(), orderBill, App.instance.getBusinessDate().toString(), 1);
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
                    if (orderItems.size() > 0 && printer != null) {
                        RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(paidOrderSplit, orderBill);
                        // List<OrderPromotion>  orderPromotions= PromotionDataSQL.getPromotionDataOrOrderid(currentOrder.getId());

                        App.instance.remoteBillPrint(printer, title, temporaryOrder,
                                orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount, null);
                    }
                    // remove get bill notification
                    removeNotificationTables();
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
                    setData();
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
                case VIEW_EVENT_SHOW_CLOSE_SPLIT_BY_PAX_BILL: {
                    closeOrderSplitWindow.show(view_top_line, currentOrder, (OrderSplit) msg.obj);
                }
                break;
                case ACTION_TRANSFER_SPLIT_BY_NUM: {
                    int splitPax = Integer.parseInt(((String) msg.obj));
                    transfItemOrderDetail.setItemNum(splitPax);
                    transfItemOrderDetail.setId(null);
                    tableShowAction = TRANSFER_ITEM;
                    activityRequestCode = 0;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showTables();
                        }
                    }, 300);

                }
                break;
                case ACTION_REMOVE_ORDER_DETAIL: {
                    OrderDetail tag = (OrderDetail) msg.obj;
                    removeItem(tag);
                }
                break;
                case ACTION_CANCEL_ORDER_DETAIL: {
                    OrderDetail tag = (OrderDetail) msg.obj;
                    verifyDialog.show(HANDLER_MSG_OBJECT_CANCEL_ITEM, tag);
                }
                break;
                case ACTION_KOT_NEXT_KDS:
                    int orderId = msg.arg1;
                    int kdsId = msg.arg2;
//                    sendKOTTmpToKDS(orderId, kdsId);
                case VIEW_EVENT_OPEN_WAITING_LIST_DATA:
                    currentTable = (TableInfo) msg.obj;
                    closeTables();
                    setDataWaitingList();
                    break;
                case BaseApplication.HANDLER_TRANSFER_TABLE_TO_OTHER_RVC:
                    if (loadingDialog != null)
                        loadingDialog.dismiss();

                    if (oldTable != null)
                        currentTable = oldTable;
                    currentOrder = OrderSQL.getLastOrderatTabel(currentTable.getPosId());

                    final String data = (String) msg.obj;
                    final Order currentOrderFinal = currentOrder;
                    final String currentTableName = currentTable.getName();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Gson gson = new Gson();
                                JSONObject jsonObject = new JSONObject(data);

                                String toRevenueCenterStr = jsonObject.optString("toRevenue");
                                String toOrderStr = jsonObject.optString("toOrder");
                                String tableInfoStr = jsonObject.optString("tableInfo");
                                String orderDetailStr = jsonObject.optString("orderDetail");
                                String orderModifierStr = jsonObject.optString("orderModifier");

                                RevenueCenter toRevenueCenter = gson.fromJson(toRevenueCenterStr, RevenueCenter.class);
                                Order toOrder = gson.fromJson(toOrderStr, Order.class);
                                TableInfo tableInfo = gson.fromJson(tableInfoStr, TableInfo.class);

                                List<OrderDetail> orderDetails = gson.fromJson(orderDetailStr,
                                        new TypeToken<List<OrderDetail>>() {
                                        }.getType());
                                List<OrderModifier> orderModifiers = gson.fromJson(orderModifierStr,
                                        new TypeToken<List<OrderModifier>>() {
                                        }.getType());

                                String tableName = tableInfo != null ? tableInfo.getName() : "";
                                RevenueCenter fromRevenueCenter = App.instance.getRevenueCenter();


                                App.instance.printTransferOrder(
                                        App.instance.getCahierPrinter(), fromRevenueCenter, toRevenueCenter,
                                        currentTableName, tableName,
                                        toOrder, currentOrderFinal, orderDetails, orderModifiers);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                    unseat(currentOrder);
                    break;
                case BaseApplication.HANDLER_TRANSFER_ITEM_TO_OTHER_RVC:
                    if (loadingDialog != null)
                        loadingDialog.dismiss();

                    String data2 = (String) msg.obj;

                    try {
                        JSONObject jsonObject = new JSONObject(data2);

                        String toRevenueCenterStr = jsonObject.optString("toRevenue");
                        String toOrderStr = jsonObject.optString("toOrder");
                        String tableInfoStr = jsonObject.optString("tableInfo");
                        String orderDetailStr = jsonObject.optString("orderDetail");
                        String orderModifierStr = jsonObject.optString("orderModifier");

                        RevenueCenter toRevenueCenter = gson.fromJson(toRevenueCenterStr, RevenueCenter.class);
                        Order toOrder = new Gson().fromJson(toOrderStr, Order.class);
                        TableInfo tableInfo = new Gson().fromJson(tableInfoStr, TableInfo.class);

                        List<OrderModifier> orderModifiers = gson.fromJson(orderModifierStr,
                                new TypeToken<List<OrderModifier>>() {
                                }.getType());

                        List<OrderDetail> orderDetailsResult = new ArrayList<>();
                        orderDetailsResult.add(transfItemOrderDetail);
                        String tableName = tableInfo != null ? tableInfo.getName() : "";
                        RevenueCenter fromRevenueCenter = App.instance.getRevenueCenter();

                        App.instance.printTransferOrder(App.instance.getCahierPrinter(),
                                fromRevenueCenter, toRevenueCenter,
                                oldTable.getName(), tableName,
                                toOrder, currentOrder, orderDetailsResult, orderModifiers);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (transfItemOrderDetail.getId() == null) {
                        transfItemOrderDetail = ObjectFactory.getInstance().cpOrderDetail(transfItemOrderDetail);

                        OrderDetail oldOrderDetail = OrderDetailSQL.getOrderDetail(transfItemOrderDetail.getTransferFromDetailId());
                        if (oldOrderDetail != null) {
                            oldOrderDetail.setItemNum(oldOrderDetail.getItemNum().intValue() - transfItemOrderDetail.getItemNum().intValue());
                            OrderDetailSQL.updateOrderDetailAndOrder(oldOrderDetail);
                        }
                    } else {
                        OrderDetailTaxSQL.deleteOrderDetailTax(transfItemOrderDetail);
                        OrderDetailSQL.deleteOrderDetail(transfItemOrderDetail);
                        OrderSQL.updateOrder(oldOrder);
                    }

                    if (transfItemOrderDetail.getOrderDetailStatus().intValue() > ParamConst.ORDERDETAIL_STATUS_ADDED) {
                        //region after place order
                        int surplusCount = OrderDetailSQL.getNoVoidCountByOrderId(oldOrder.getId());

                        if (surplusCount == 0) {
                            OrderDetailSQL.deleteOrderDetailByOrder(oldOrder);
                            KotSummarySQL.deleteKotSummaryByOrder(oldOrder);
                            OrderBillSQL.deleteOrderBillByOrder(oldOrder);
                            OrderSQL.deleteOrder(oldOrder);
                            TableInfo tables = TableInfoSQL.getTableById(oldOrder.getTableId().intValue());
                            tables.setStatus(ParamConst.TABLE_STATUS_IDLE);
                            if (checkIfTableFromThisRVC(tables)) {
                                TableInfoSQL.updateTables(tables);
                            }
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("tableId", tables.getPosId().intValue());
                                jsonObject.put("status", ParamConst.TABLE_STATUS_IDLE);
                                jsonObject.put("RX", RxBus.RX_REFRESH_TABLE);
                                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //endregion
                    }

                    handler.sendEmptyMessage(VIEW_EVENT_DISMISS_TABLES);//refresh


//                    final OrderDetail orderDetail = transfItemOrderDetail;

//                    if (orderDetail.getIsFree().intValue() == ParamConst.FREE) {
//
//                    } else if (!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())) {
//
//                    } else if (orderDetail.getOrderSplitId() != null && orderDetail.getOrderSplitId().intValue() != 0) {
//                        OrderSplit orderSplit = OrderSplitSQL.get(orderDetail.getOrderSplitId().intValue());
//                        if (orderSplit.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
//
//                        }
//                    }

//                    if (orderDetail.getOrderDetailStatus() < ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
//                        removeItemLogic(orderDetail);
//                    } else if (App.instance.getSystemSettings().isRemoveToVoid()) {
//                        handler.sendMessage(handler.obtainMessage(MainPage.ACTION_CANCEL_ORDER_DETAIL, orderDetail));
//                    } else if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("orderDetail", orderDetail);
//                        map.put("type", new Integer(ParamConst.ORDERDETAIL_TYPE_VOID));
//                        handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_VOID_OR_FREE, map));
//                        handler.sendEmptyMessage(MainPage.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
//                        List<OrderDetail> ordersDetail = OrderDetailSQL.getOrderDetails(oldOrder.getId());
//                        if (ordersDetail.size() > 0) {
//                            removeItemLogic(orderDetail);
//                            showTables();
//                            onBackPressed();
//                            ordersDetail = OrderDetailSQL.getOrderDetails(oldOrder.getId());
//                            if (ordersDetail.size() <= 0) {
//                                unseat(oldOrder);
//                            } else {
//
//                                removeItemLogic(orderDetail);
//
//                                List<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(oldOrder.getId());
//                                if (orderDetails.size() <= 0) {
//                                    unseat(oldOrder);
//                                }
//                                onBackPressed();
//                            }
//                        onBackPressed();
//                        }
//                    }
                    break;
                default:
                    break;


            }
        }
    };

    private Observable<Integer> observable;
    private Observable<Object> observable1;
    private String kotCommitStatus;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (count == 30) {
                List<TableInfo> tablesList = OrderSQL.getAllTimeOutOrderByTime(App.instance.getBusinessDate(),
                        App.instance.getSessionStatus(), TimeUtil.getTimeBeforeTwoHour());
                mNotificationManager.cancelAll();
                if (!tablesList.isEmpty()) {
                    for (TableInfo tables : tablesList) {
                        handler.sendMessage(handler.obtainMessage(ORDER_TIME_OUT, tables));
                    }
                }
                count = 0;
            } else {
                count++;
            }


        }
    };

    public static void setTablePacks(TableInfo tableInfo, String tablePacks) {
        if (tableInfo != null) {
            tableInfo.setPacks(Integer.parseInt(tablePacks));
            tableInfo.setStatus(ParamConst.TABLE_STATUS_DINING);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tableId", tableInfo.getPosId().intValue());
                jsonObject.put("status", ParamConst.TABLE_STATUS_DINING);
                jsonObject.put("RX", RxBus.RX_REFRESH_TABLE);
                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tableInfo.getRevenueId().equals(App.instance.getMainPosInfo().getRevenueId())) {
                TableInfoSQL.updateTables(tableInfo);
            }
//			TablesSQL.updateTables(tableInfo);
        }
    }

    private void unseat() {
        OrderDetailSQL.deleteOrderDetailByOrder(currentOrder);
        KotSummarySQL.deleteKotSummaryByOrder(currentOrder);
        OrderModifierSQL.deleteOrderModifierByOrder(currentOrder);
        List<OrderSplit> splits = OrderSplitSQL.getAllOrderSplits(currentOrder);
        if (splits.size() > 0) {
            for (OrderSplit split : splits) {
                OrderSplitSQL.delete(split);
            }
        }
        OrderBillSQL.deleteOrderBillByOrder(currentOrder);
        OrderSQL.deleteOrder(currentOrder);

        TableInfo tables = TableInfoSQL.getTableById(currentOrder.getTableId().intValue());
        tables.setStatus(ParamConst.TABLE_STATUS_IDLE);
        if (checkIfTableFromThisRVC(tables)) {
            TableInfoSQL.updateTables(tables);
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tableId", currentTable.getPosId().intValue());
            jsonObject.put("status", ParamConst.TABLE_STATUS_IDLE);
            jsonObject.put("RX", RxBus.RX_REFRESH_TABLE);
            TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
            TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        activityRequestCode = 0;
        selectOrderSplitDialog.dismiss();
        tableShowAction = SHOW_TABLES;
        currentOrder = null;
        showTables();
    }

    private void unseat(Order order) {
        OrderDetailSQL.deleteOrderDetailByOrder(order);
        KotSummarySQL.deleteKotSummaryByOrder(order);
        OrderModifierSQL.deleteOrderModifierByOrder(order);
        List<OrderSplit> splits = OrderSplitSQL.getAllOrderSplits(order);
        if (splits.size() > 0) {
            for (OrderSplit split : splits) {
                OrderSplitSQL.delete(split);
            }
        }
        OrderBillSQL.deleteOrderBillByOrder(order);
        OrderSQL.deleteOrder(order);

        TableInfo tables = TableInfoSQL.getTableById(order.getTableId().intValue());
        tables.setStatus(ParamConst.TABLE_STATUS_IDLE);
        if (checkIfTableFromThisRVC(tables)) {
            TableInfoSQL.updateTables(tables);
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tableId", currentTable.getPosId().intValue());
            jsonObject.put("status", ParamConst.TABLE_STATUS_IDLE);
            jsonObject.put("RX", RxBus.RX_REFRESH_TABLE);
            TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
            TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        activityRequestCode = 0;
        selectOrderSplitDialog.dismiss();
        tableShowAction = SHOW_TABLES;
        order = null;
        currentOrder = null;
        showTables();
    }

    private boolean checkIfTableFromThisRVC(TableInfo tableInfo) {
        return tableInfo.getRevenueId().equals(App.instance.getMainPosInfo().getRevenueId());
    }

    @Override
    public void onBackPressed() {
//		if(closeOrderWindow != null && closeOrderWindow.isShowing()){
//			closeOrderWindow.onBack();
//			return;
//		}
//		if(closeOrderSplitWindow != null && closeOrderSplitWindow.isShowing()){
//			closeOrderSplitWindow.onBack();
//			return;
//		}
        ButtonClickTimer.canClick();
        if (selectOrderSplitDialog != null && selectOrderSplitDialog.isShowing()) {
            selectOrderSplitDialog.dismiss();
        }
        if (closeOrderSplitWindow != null && closeOrderSplitWindow.isShowing()) {
            closeOrderSplitWindow.backLikeClose();
            return;
        }
//		if (isShowTables
//				&& !isTableFirstShow
//				&& currentOrder != null
//				&& OrderSQL.getOrder(currentOrder.getId()).getOrderStatus() != ParamConst.ORDER_STATUS_FINISHED) {
//			closeTables();
//			isShowTables = false;
//			return;
//		}
        if (mainPageSearchView.getVisibility() == View.VISIBLE) {
            Message msg = handler.obtainMessage();
            msg.what = MainPage.VIEW_EVENT_DISMISS_SEARCH;
            handler.sendMessage(msg);
            return;
        }
        if (!isShowTables) {
            activityRequestCode = 0;
            tableShowAction = SHOW_TABLES;
            if (currentOrder != null) {
                if (orderDetails != null && orderDetails.size() <= 0) {
                    KotSummarySQL.deleteKotSummaryByOrder(currentOrder);
                }
            }
            mainPageMenuView.closeModifiers();
            showTables();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        SessionStatus sessionStatus = Store.getObject(context,
                Store.SESSION_STATUS, SessionStatus.class);
//		List<Order> unPlayOrders =
//		Order order = OrderSQL.getOrderByUnPlay(sessionStatus);

//		if (order != null) {
//			if (!OrderDetailSQL.getOrderDetails(order.getId()).isEmpty()) {
//				UIHelp.showToast(context, "Orders are not closed");
//			} else {
//				Tables tables = TablesSQL.getTableById(order.getTableId());
//				tables.setTableStatus(ParamConst.TABLE_STATUS_IDLE);
//				TablesSQL.updateTables(tables);
//				OrderSQL.deleteOrder(order);
//				OrderBillSQL.deleteOrderBillByOrder(order);
//				super.finish();
//			}
//		} else {
        super.finish();
//		}
    }

    private String getJsonStr() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableShowAction", tableShowAction);
        map.put("places", places);
        map.put("tables", tables);
        map.put("tableStatusInfo", tableStatusInfo);
        String str = gson.toJson(map);
        return JSONUtil.getJSONFromEncode(str);
    }

    private String getRefreshTableStatusStr(TableInfo table) {
        int id = table.getPlacesId();
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<TableInfo> tables = new ArrayList<TableInfo>();
        tables.add(table);
        map.put("id", id);
        map.put("tables", tables);
        String str = gson.toJson(map);
        return JSONUtil.getJSONFromEncode(str);
    }

    private void showDiscountWindow(Order order, OrderDetail orderDetail,
                                    ResultCall resultCall) {
        discountWindow.show(order, orderDetail, resultCall);
    }

    private void showCloseBillWindow() {
        OrderBill orderBill = OrderBillSQL.getOrderBillByOrder(currentOrder);
        if (currentOrder.getOrderStatus().intValue() != ParamConst.ORDER_STATUS_UNPAY && App.instance.getSystemSettings().isPrintBeforCloseBill()) {
            UIHelp.showToast(context, context.getResources().getString(R.string.print_bill_));
            return;
        }
        if (OrderDetailSQL
                .getOrderDetailsCountUnPlaceOrder(currentOrder.getId()) > 0) {
            UIHelp.showToast(context,
                    context.getResources().getString(R.string.place_before_print));
            return;
        }
        if (orderBill != null && orderBill.getBillNo() != null) {
//
//			List<OrderDetail>
            int id = OrderDetailSQL.getMaxGroupId(currentOrder);
            if (id == 0) {
                closeOrderWindow.show(view_top_line, currentOrder, operatePanel.getWidth(), orderBill, orderDetails);
            } else {
                int count = OrderDetailSQL.getOrderDetailCountByGroupId(ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID, currentOrder.getId());
                if (count == 0) {
                    List<OrderSplit> orderSplits = OrderSplitSQL.getOrderSplits(currentOrder);
                    selectOrderSplitDialog.show(orderSplits, currentOrder);
                } else {
                    UIHelp.showToast(context, context.getResources().getString(R.string.assign_items_to_group));
                }
            }
        } else {
            UIHelp.showToast(context,
                    context.getResources().getString(R.string.place_before_print));
        }
    }

    private void showOpenItemWindow() {
        if (openItemWindow != null) {
            openItemWindow.show(context, findViewById(R.id.rl_root), handler,
                    currentOrder);
        }
        if (isShowTables) {
            dismissOpenItemWindow();
        }
    }

    private void dismissOpenItemWindow() {
        if (openItemWindow != null) {
            openItemWindow.dismiss();
        }
    }

    private void showSearchView() {
        mainPageSearchView.setVisibility(View.VISIBLE);
        mainPageSearchView.setParam(context, currentOrder, CoreData
                .getInstance().getItemDetails(), handler, true);
    }

    private void search(String key) {
        if (mainPageSearchView.getVisibility() == View.VISIBLE) {
            List<ItemDetail> itemDetails = CoreData.getInstance()
                    .getItemDetails();
            List<ItemDetail> itemDetailList = new ArrayList<ItemDetail>();
            if (key != null) {
                key = key.trim().replaceAll("\\s+", "");
                for (ItemDetail itemDtail : itemDetails) {
                    String name = CommonUtil.getInitial(itemDtail.getItemName());
                    if (name.contains(key) || name.contains(key.toUpperCase())) {
                        itemDetailList.add(itemDtail);
                        continue;
                    }
                }
            }
            mainPageSearchView.setParam(context, currentOrder, itemDetailList,
                    handler, false);
        }
    }

    private void dismissSoftInput() {
        // 得到InputMethodManager的实例
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 如果开启
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
        }
    }

    private void dismissSearchView() {
        mainPageSearchView.setVisibility(View.GONE);
//		mainPageSearchView.setParam(context, currentOrder, CoreData
//				.getInstance().getItemDetails(""), handler);
    }

    private void showTables() {
        dismissOpenItemWindow();
//		getTables();
//        ObjectAnimator animator = ObjectAnimator.ofFloat(web_tables, "y",
//                web_tables.getY(), 0).setDuration(300);
//        animator.start();
//		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        isShowTables = true;
        isTableFirstShow = false;
//        transaction = fragmentManager.beginTransaction();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
        LogUtil.e(TAG, "before table show");
        transaction.show(f_tables);
        transaction.commitAllowingStateLoss();
        App.instance.showWelcomeToSecondScreen();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissOpenItemWindow();
            }
        }, 300);

    }

    private void transferItemOtherRVC() {
        oldOrder = currentOrder;
        for (final MultiRVCPlacesDao.Places otherPlace : App.instance.getOtherRVCPlaces()) {
            if (currentTable.getRevenueId().equals(otherPlace.getRevenueId())) {
                SyncCentre.getInstance().transferItemToOtherRVC(context, otherPlace.getIp(), oldOrder, transfItemOrderDetail, currentTable, handler);
                break;
            }
        }
    }

//	private void dismissTables() {
//		ObjectAnimator animator = ObjectAnimator.ofFloat(web_tables, "y",
//				web_tables.getY(), web_tables.getY() + web_tables.getHeight())
//				.setDuration(300);
//		animator.start();
//		isShowTables = false;
//	}

    private void closeTables() {
//		ObjectAnimator animator = ObjectAnimator.ofFloat(web_tables, "y",
//				web_tables.getY(), web_tables.getY() + web_tables.getHeight())
//				.setDuration(300);
//		animator.start();
//        transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
//		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(f_tables);
        transaction.commitAllowingStateLoss();
        isShowTables = false;

    }

    private void setData() {
        initOrder(currentTable);
        if (currentOrder == null) {
            showTables();
            return;
        }
        orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
//        List<OrderDetail> myorderDetails = OrderDetailSQL.getGeneralOrderDetails(currentOrder.getId());
//
//
//        List<OrderSplit> orderSplits  = OrderSplitSQL.getAllOrderSplits(currentOrder);
//        //update tabels orders
        currentTable.setOrders(orderDetails.size());
        if (checkIfTableFromThisRVC(currentTable)) {
            TableInfoSQL.updateTables(currentTable);
        }
        mainPageMenuView.setParam(currentOrder, handler);
        orderView.setParam(this, currentOrder, orderDetails, handler);

//        DiffData data = new DiffData(this);//实例化data类
//        data.updateData(orderDetails);//启动发送
        //  DifferentDislay.setParam(orderDetails,currentOrder);
        operatePanel.setParams(this, currentOrder, orderDetails, handler);
        loadingDialog.dismiss();
        if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
            printerLoadingDialog.dismiss();
        }
    }

    private void setDataWaitingList() {
        initOrderWaitingList(currentTable);
        orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
        //update tabels orders
        currentTable.setOrders(orderDetails.size());
        if (checkIfTableFromThisRVC(currentTable)) {
            TableInfoSQL.updateTables(currentTable);
        }
        mainPageMenuView.setParam(currentOrder, handler);
        orderView.setParam(this, currentOrder, orderDetails, handler);

//        DiffData data = new DiffData(this);//实例化data类
//        data.updateData(orderDetails);//启动发送
        //  DifferentDislay.setParam(orderDetails,currentOrder);
        operatePanel.setParams(this, currentOrder, orderDetails,
                handler);
        loadingDialog.dismiss();
        if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
            printerLoadingDialog.dismiss();
        }
    }

    private void mergerOrderSetData() {
        // initOrder(currentTable);
        orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
        mainPageMenuView.setParam(currentOrder, handler);
        orderView.setParam(this, currentOrder, orderDetails, handler);
        operatePanel.setParams(this, currentOrder, orderDetails,
                handler);
        loadingDialog.dismiss();
        if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
            printerLoadingDialog.dismiss();
        }
    }

    private void showModifyQuantityWindow(int quantity, DismissCall dismissCall) {
        modifyQuantityWindow.show(quantity, dismissCall);
    }

    private void addOrderDetail(OrderDetail orderDetail) {
        List<ItemModifier> itemModifiers = CoreData.getInstance()
                .getItemModifiers(
                        CoreData.getInstance().getItemDetailById(
                                orderDetail.getItemId(), orderDetail.getItemName()));
        OrderDetailSQL.addOrderDetailETC(orderDetail);
        if (currentTable.getPosId() < 0) {
            setDataWaitingList();
        } else {
            setData();
            //        sendKOTTmpToKDS(orderDetail);
        }
        if (itemModifiers.size() > 0) {
            for (ItemModifier itemModifier : itemModifiers) {

                final Modifier modifier_type = CoreData.getInstance().getModifier(
                        itemModifier);
                if (modifier_type.getMinNumber() > 0) {
                    ModifierCheck modifierCheck = null;
                    modifierCheck = ObjectFactory.getInstance().getModifierCheck(currentOrder, orderDetail, modifier_type, itemModifier);
                    ModifierCheckSql.addModifierCheck(modifierCheck);
                }
            }

            mainPageMenuView.openModifiers(currentOrder, orderDetail,
                    itemModifiers);
        }
    }

    private void sendKOTTmpToKDS(final OrderDetail orderDetail) {
//        sendKOTTmpToKDS(orderDetail, null, ParamConst.JOB_TMP_KOT);
    }

    private void sendKOTTmpToKDS(final OrderDetail orderDetail,
                                 final KotItemDetail mKotItemDetail, final String method) {

        if (currentOrder.getOrderNo().equals(0)) {
            currentOrder.setOrderNo(OrderHelper.calculateOrderNo(currentOrder.getBusinessDate()));
            OrderSQL.updateOrderNo(currentOrder);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Order order = OrderSQL.getOrder(currentOrder.getId());
                KotSummary kotSummary = ObjectFactory.getInstance()
                        .getKotSummaryForPlace(
                                TableInfoSQL.getTableById(
                                        order.getTableId()).getName(), order,
                                App.instance.getRevenueCenter(),
                                App.instance.getBusinessDate());

                User user = App.instance.getUser();
                if (user != null) {
                    String empName = user.getFirstName() + user.getLastName();
                    kotSummary.setEmpName(empName);
                    KotSummarySQL.updateKotSummaryEmpById(empName, kotSummary.getId());
                }

                ArrayList<KotItemDetail> kotItemDetails = new ArrayList<>();
                List<Integer> orderDetailIds = new ArrayList<>();
                ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<>();

                KotItemDetail kotItemDetail;

                if (method.equals(ParamConst.JOB_DELETE_TMP_ITEM_KOT) && mKotItemDetail != null) {
                    kotItemDetail = mKotItemDetail;
                } else {
                    kotItemDetail = ObjectFactory
                            .getInstance()
                            .getKotItemDetail(
                                    order,
                                    orderDetail,
                                    CoreData.getInstance()
                                            .getItemDetailById(
                                                    orderDetail
                                                            .getItemId()),
                                    kotSummary,
                                    App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                }

                if (!method.equals(ParamConst.JOB_DELETE_TMP_ITEM_KOT)) {
                    kotItemDetail.setItemNum(orderDetail.getItemNum());
                    kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_TMP);

                    KotItemDetailSQL.update(kotItemDetail);
                }

                kotItemDetails.add(kotItemDetail);
                orderDetailIds.add(orderDetail.getId());
                ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                        .getOrderModifiers(order, orderDetail);

                if (method.equals(ParamConst.JOB_DELETE_TMP_ITEM_KOT)) {
                    orderModifiers = new ArrayList<>();
                }

                for (OrderModifier orderModifier : orderModifiers) {
                    if (orderModifier.getStatus().equals(ParamConst.ORDER_MODIFIER_STATUS_NORMAL)) {
                        KotItemModifier kotItemModifier = ObjectFactory
                                .getInstance()
                                .getKotItemModifier(
                                        kotItemDetail,
                                        orderModifier,
                                        CoreData.getInstance()
                                                .getModifier(
                                                        orderModifier
                                                                .getModifierId()));
                        KotItemModifierSQL.update(kotItemModifier);
                        kotItemModifiers.add(kotItemModifier);
                    }
                }

                if (!method.equals(ParamConst.JOB_DELETE_TMP_ITEM_KOT)) {
                    KotSummarySQL.update(kotSummary);
                }

                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("orderId", currentOrder.getId());
                orderMap.put("orderDetailIds", orderDetailIds);
                App.instance.getKdsJobManager().sendKOTTmpToKDS(
                        kotSummary, kotItemDetails,
                        kotItemModifiers, method,
                        orderMap);
            }
        }).start();
    }

    private boolean verifyTableRepeat(TableInfo newTables) {
        List<TableInfo> notificationTables = App.instance
                .getGetTingBillNotifications();
        if (notificationTables == null) {
            return true;
        } else {
            for (int i = 0; i < notificationTables.size(); i++) {
                if (notificationTables.get(i).getPosId().intValue() == newTables
                        .getPosId().intValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNotificationTables() {
        List<TableInfo> notificationTables = App.instance
                .getGetTingBillNotifications();
        if (notificationTables == null) {
            return;
        }
        for (int i = 0; i < notificationTables.size(); i++) {
            if (notificationTables.get(i).getPosId().intValue() == currentTable
                    .getPosId().intValue()) {
                App.instance.removeGettingBillNotification(notificationTables
                        .get(i));
            }
        }
    }

    private void sendNotification(TableInfo tables) {
        if (tables != null && tables.getName() != null) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle(context.getResources().getString(R.string.alfred_pos))
                    .setContentText(context.getResources().getString(R.string.the_table) + tables.getName() +
                            context.getResources().getString(R.string.occupied_time))
                    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                    .setTicker(context.getResources().getString(R.string.pos_notification))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.logo_icon);
            mNotificationManager.notify(tables.getPosId(), mBuilder.build());
        }
    }

    private void initAppOrder(final TableInfo tables) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Order order = ObjectFactory.getInstance().getOrder(
                        ParamConst.ORDER_ORIGIN_POS, App.instance.getSubPosBeanId(), tables,
                        App.instance.getRevenueCenter(), App.instance.getUser(),
                        App.instance.getSessionStatus(),
                        App.instance.getBusinessDate(),
                        App.instance.getIndexOfRevenueCenter(),
                        ParamConst.ORDER_STATUS_OPEN_IN_POS,
                        App.instance.getLocalRestaurantConfig()
                                .getIncludedTax().getTax(), appOrderId, "");
                List<TempOrderDetail> tempOrderDetails = TempOrderDetailSQL.getTempOrderDetailByAppOrderId(appOrderId);
                for (TempOrderDetail tempOrderDetail : tempOrderDetails) {
                    ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(tempOrderDetail.getItemId(), tempOrderDetail.getItemName());
                    if (itemDetail == null) {
                        continue;
                    }
                    OrderDetail orderDetail = ObjectFactory.getInstance().getOrderDetail(order, itemDetail, 0);
                    List<TempModifierDetail> tempModifierDetails = TempModifierDetailSQL.getTempOrderDetailByOrderDetailId(tempOrderDetail.getOrderDetailId());
                    for (TempModifierDetail tempModifierDetail : tempModifierDetails) {
                        Modifier modifier = ModifierSQL.getModifierById(tempModifierDetail.getModifierId());
                        if (modifier == null) {
                            continue;
                        }
                        OrderModifier orderModifier = ObjectFactory.getInstance().getOrderModifier(order, orderDetail, modifier, 0);
                        OrderModifierSQL.addOrderModifier(orderModifier);
                    }
                    OrderDetailSQL.addOrderDetailETC(orderDetail);
                }
                handler.sendEmptyMessage(VIEW_EVENT_DISMISS_TABLES);
            }
        }).start();
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

//	public Handler mmHandler = new Handler() {
//		public void handleMessage(Message msg) {
//
//		};
//	};

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
//		App.instance.unBindPushWebSocketService();
        XMPP.getInstance().setCanCheckAppOrder(false);
        if (observable != null) {
            RxBus.getInstance().unregister(RxBus.RX_MSG_1, observable);
        }
        if (observable1 != null) {
            RxBus.getInstance().unregister("open_drawer", observable1);
        }
        super.onDestroy();
    }

    public void httpRequestAction(int action, Object obj) {
        switch (action) {
            case VIEW_EVENT_SET_DATA:
                int orderId = (Integer) obj;
                if (currentOrder != null && orderId == currentOrder.getId()) {
                    handler.sendMessage(handler.obtainMessage(action));
                }
                break;
            case REFRESH_TABLES_STATUS:
                handler.sendEmptyMessage(REFRESH_TABLES_STATUS);
                break;
            case VIEW_EVNT_GET_BILL_PRINT: {
                TableInfo tables = (TableInfo) obj;
                handler.sendMessage(handler.obtainMessage(VIEW_EVNT_GET_BILL_PRINT,
                        tables));
            }
            break;
            case REFRESH_STOCK_NUM:
                handler.sendEmptyMessage(action);
                break;
            case SERVER_TRANSFER_TABLE_FROM_OTHER_RVC:
                currentTable = (TableInfo) obj;
                handler.sendEmptyMessage(REFRESH_TABLES_STATUS);
                break;
            default:
                break;
        }
    }

    public void httpRequestActions(int action, Object... objs) {
        switch (action) {
            case ACTION_KOT_NEXT_KDS:
                int orderId = (int) objs[0];
                int kdsId = (int) objs[1];
                handler.sendMessage(handler.obtainMessage(ACTION_KOT_NEXT_KDS, orderId, kdsId));
                break;
            default:
                break;
        }
    }

    private void removeItem(final OrderDetail tag) {
        {
            DialogFactory.commonTwoBtnDialog(context, context.getResources().getString(R.string.warning),
                    context.getResources().getString(R.string.remove_item),
                    context.getResources().getString(R.string.no),
                    context.getResources().getString(R.string.yes), null,
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            removeItemLogic(tag);
                        }
                    });
        }
    }

    private void removeItemLogic(OrderDetail tag) {
        final int itemTempId = CoreData.getInstance().getItemDetailById(tag.getItemId(), tag.getItemName()).getItemTemplateId();
        RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);
        if (remainingStock != null) {
            int num = tag.getItemNum();
            RemainingStockHelper.updateRemainingStockNum(remainingStock, num, true, new StockCallBack() {
                @Override
                public void onSuccess(Boolean isStock) {
                    if (isStock) {
                        App.instance.getSyncJob().updateRemainingStockNum(itemTempId);
                    }

                }
            });

        }

        KotSummary kotSummary = KotSummarySQL.getKotSummary(currentOrder.getId(), currentOrder.getNumTag());

        if (kotSummary != null) {
            KotItemDetail kotItemDetail = KotItemDetailSQL.getKotItemDetailByOrderDetailId(kotSummary.getId(), tag.getId());

//                            sendKOTTmpToKDS(tag, kotItemDetail, ParamConst.JOB_DELETE_TMP_ITEM_KOT);
        }

//                            RemainingStockSQL.updateRemainingNum(num,itemTempId);
        OrderDetailSQL.deleteOrderDetail(tag);
        OrderModifierSQL.deleteOrderModifierByOrderDetail(tag);
        ModifierCheckSql.deleteModifierCheck(tag.getId(), tag.getOrderId());

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", tag.getOrderId().intValue());
            jsonObject.put("RX", RxBus.RX_REFRESH_ORDER);
            TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!IntegerUtils.isEmptyOrZero(tag.getOrderSplitId()) && !IntegerUtils.isEmptyOrZero(tag.getGroupId())) {
            int count = OrderDetailSQL.getOrderDetailCountByGroupId(tag.getGroupId().intValue(), currentOrder.getId().intValue());
            if (count == 0) {
                OrderSplitSQL.deleteOrderSplitByOrderAndGroupId(currentOrder.getId().intValue(), tag.getGroupId().intValue());
            }
        }
        handler.sendEmptyMessage(MainPage.VIEW_EVENT_CLOSE_MODIFIER_VIEW);
        handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);

    }


    public void kotPrintStatus(int action, Object obj) {
        switch (action) {
            case KOT_PRINT_FAILED:
                handler.sendMessage(handler.obtainMessage(action, obj));
                break;
            case KOT_PRINT_SUCCEED:
                handler.sendMessage(handler.obtainMessage(action, obj));
                break;
            case ParamConst.JOB_TYPE_POS_TRANSFER_TABLE:
                Order order = (Order) obj;
                setTablePacks(String.valueOf(order.getPersons()));
                handler.sendEmptyMessage(VIEW_EVENT_SET_DATA);
                break;
            case ParamConst.JOB_TYPE_POS_MERGER_TABLE:
                handler.sendEmptyMessage(VIEW_EVENT_DISMISS_TABLES);
                break;
            case KOT_PRINT_NULL:
                handler.sendEmptyMessage(action);
                break;
            case KOT_ITEM_PRINT_NULL:
                handler.sendMessage(handler.obtainMessage(action, obj));
                break;
            default:
                break;
        }
    }

    private void TempOrderISPaied() {
        Payment payment = ObjectFactory.getInstance().getPayment(currentOrder, ObjectFactory.getInstance().getOrderBill(currentOrder, App.instance.getRevenueCenter()));
        ObjectFactory.getInstance().getPaymentSettlement(payment, ParamConst.SETTLEMENT_TYPE_THIRDPARTY, currentOrder.getTotal());
        currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_FINISHED);
        OrderSQL.update(currentOrder);
        if (!App.instance.isRevenueKiosk()) {
//			Tables tables = TablesSQL.getTableById(currentTable.getPosId().intValue());
//			tables.setTableStatus(ParamConst.TABLE_STATUS_IDLE);
//			TablesSQL.updateTables(tables);
            TableInfo tableInfo = TableInfoSQL.getTableById(currentTable.getPosId().intValue());
            tableInfo.setStatus(ParamConst.TABLE_STATUS_IDLE);
            if (checkIfTableFromThisRVC(tableInfo)) {
                TableInfoSQL.updateTables(tableInfo);
            }
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tableId", currentTable.getPosId().intValue());
                jsonObject.put("status", ParamConst.TABLE_STATUS_IDLE);
                jsonObject.put("RX", RxBus.RX_REFRESH_TABLE);
                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_WAITER, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
                TcpUdpFactory.sendUdpMsg(BaseApplication.UDP_INDEX_EMENU, TcpUdpFactory.UDP_REQUEST_MSG + jsonObject.toString(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("orderId", String.valueOf(currentOrder.getId()));
        map.put("paymentId", String.valueOf(payment.getId().intValue()));
        handler.sendMessage(handler.obtainMessage(VIEW_EVENT_CLOSE_BILL, map));
        handler.sendEmptyMessage(MainPage.VIEW_EVENT_SHOW_TABLES_AFTER_CLOSE_BILL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(settingView)) {
            mDrawerLayout.closeDrawer(Gravity.END);
        }

        mainPageMenuView.setParent(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        setData();

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NetWorkOrderActivity.CHECK_REQUEST_CODE && resultCode == NetWorkOrderActivity.CHECK_REQUEST_CODE) {
            activityRequestCode = requestCode;
            appOrderId = data.getIntExtra("appOrderId", 0);
            if (appOrderId == 0)
                return;
            showTables();
        } else if (requestCode == CHECK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("qrCode", data.getStringExtra("qrCode"));
                map.put("revenueId", data.getIntExtra("revenueId", 0));
                map.put("consumeAmount", data.getStringExtra("consumeAmount"));
                map.put("operateType", data.getIntExtra("operateType", -1));
                SyncCentre.getInstance().updateStoredCardValue(context, map, handler);
                loadingDialog.show();
            }
        } else {
            activityRequestCode = 0;
//			mDrawerLayout.postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					if (mDrawerLayout.isDrawerOpen(settingView)) {
//						mDrawerLayout.closeDrawer(Gravity.END);
//					}
//				}
//			}, 500);
        }
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);//关闭阴影
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);    //关闭手势滑动
        settingView = (SettingView) findViewById(R.id.settingView);
        settingView.setParams(this, mDrawerLayout);
        if (MachineUtil.isHisense()) {
            if (MachineUtil.isSunmiModel()) {
                settingView.SUNMIVisible();
            } else {
                settingView.SUNMIGone();
            }

        } else if (MachineUtil.isHisense()) {
            settingView.SUNMIVisible();
        } else {
            settingView.SUNMIGone();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        System.out.println("========MMMMMM");
        setContentView(R.layout.activity_main_page);
        super.initView();
//		findViewById(R.id.rl_stored_card_fragment_father).setOnClickListener(null);
        initDrawerLayout();
        ScreenSizeUtil.initScreenScale(context);
        verifyDialog = new VerifyDialog(context, handler);

//        mediaDialog = new MediaDialog(context, handler);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        printerLoadingDialog = new PrinterLoadingDialog(context);
        printerLoadingDialog.setTitle(context.getResources().getString(R.string.sending_to_kitchen));
        view_top_line = findViewById(R.id.view_top_line);
        selectOrderSplitDialog = new SelectOrderSplitDialog(context, handler);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        f_tables = (TableLayoutFragment) fragmentManager.findFragmentById(R.id.f_tables);

        topMenuView = (TopMenuView) findViewById(R.id.topMenuView);
        topMenuView.setParams(this, handler, mDrawerLayout, settingView);
        topMenuView.setGetBillNum(App.instance.getGetTingBillNotifications()
                .size());
        mainPageSearchView = (MainPageSearchView) findViewById(R.id.search);
        orderView = (MainPageOrderView) findViewById(R.id.orderView);
        LinearLayout.LayoutParams ps1 = new LinearLayout.LayoutParams(
                (int) (700 * ScreenSizeUtil.width / ScreenSizeUtil.WIDTH_POS),
                LayoutParams.MATCH_PARENT);
        orderView.setLayoutParams(ps1);
        mainPageMenuView = (MainPageMenuView) findViewById(R.id.mainPageMenuView);
        mainPageMenuView.setParent(context);
        operatePanel = (MainPageOperatePanel) findViewById(R.id.operatePanel);
        LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
                (int) (300 * ScreenSizeUtil.width / ScreenSizeUtil.WIDTH_POS),
                LayoutParams.MATCH_PARENT);
        operatePanel.setLayoutParams(ps);
        closeOrderWindow = new CloseOrderWindow(this,
                findViewById(R.id.rl_root), handler);
        closeOrderSplitWindow = new CloseOrderSplitWindow(this,
                findViewById(R.id.rl_root), handler);
        System.out.println("--window start");
        setPAXWindow = new SetPAXWindow(context, findViewById(R.id.rl_root),
                handler);
        discountWindow = new DiscountWindow(this, findViewById(R.id.rl_root));
        openItemWindow = new OpenItemWindow();
        modifyQuantityWindow = new ModifyQuantityWindow(this,
                findViewById(R.id.rl_root));
        specialInstractionsWindow = new SpecialInstractionsWindow(context,
                findViewById(R.id.lv_order), handler);

        orderSplitPrintWindow = new OrderSplitPrintWindow(context,
                findViewById(R.id.lv_order), handler);
        setWeightWindow = new SetWeightWindow(context, findViewById(R.id.rl_root),
                handler);
        System.out.println("--window end");
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
        observable = RxBus.getInstance().register(RxBus.RX_MSG_1);
        observable1 = RxBus.getInstance().register("open_drawer");

        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer object) {
//                showStoredCard();
                if (object != null) {
                    if (object.intValue() == 1)
                        topMenuView.refreshUserName();
                    else if (object.intValue() == 2) {
                        topMenuView.showAppOrderReciving();
                    } else if (object.intValue() == 3) {
                        topMenuView.showAppOrderReciving();
                    }
                } else {
                    UIHelp.startSoredCardActivity(context);
                }
            }
        });
        observable1.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object object) {
                verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_OPEN_DRAWER, null);
            }
        });
//		App.instance.bindPushWebSocketService(App.instance.getRevenueCenter().getRestaurantId());
        XMPP.getInstance().setCanCheckAppOrder(true);
        if (savedInstanceState != null) {
            currentOrder = (Order) savedInstanceState.getSerializable("currentOrder");
            currentTable = (TableInfo) savedInstanceState.getSerializable("currentTable");
            setData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("currentOrder", currentOrder);
        outState.putSerializable("currentTable", currentTable);
    }

    public void tableAction(TableInfo tableInfo) {
        if (tableInfo != null) {
            try {
                if (currentTable != null) {
                    if (currentTable.getPosId() < 0 && tableInfo.getStatus() > ParamConst.TABLE_STATUS_IDLE && tableShowAction.equals(TRANSFER_TABLE)) {
                        UIHelp.showShortToast(context, "Can not assign because table is not empty");
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (TRANSFER_ITEM.equals(tableShowAction)) {
                oldTable = currentTable;
                handler.sendMessage(handler
                        .obtainMessage(
                                JavaConnectJS.ACTION_CLICK_TABLE_ITEM,
                                tableInfo));
            } else if (TRANSFER_TABLE.equals(tableShowAction)) {
                oldTable = currentTable;
                handler.sendMessage(handler
                        .obtainMessage(
                                JavaConnectJS.ACTION_CLICK_TABLE_TRANSFER,
                                tableInfo));
            } else {
                oldTable = null;
                handler.sendMessage(handler
                        .obtainMessage(
                                JavaConnectJS.ACTION_CLICK_TABLE,
                                tableInfo));
            }
        }
//		} else if (JavaConnectJS.CLICK_REFRESH.equals(action)) {
//			isShowTables = true;
//			handler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_REFRESH);
//		}
    }

//	private void getPlaces() {
////		places = PlacesSQL.getAllPlaces();
//		places = PlaceInfoSQL.getAllPlaceInfo();
//		getTables();
//	}
//
//	private void getTables() {
//		tables = TableInfoSQL.getAllTables();
//		handler.sendEmptyMessage(GET_TABLESTATUSINFO_DATA);
//	}

    public void setTablePacks(String tablePacks) {
        setTablePacks(currentTable, tablePacks);
    }

    private void openModifiers(OrderDetail orderDetail,
                               List<ItemModifier> itemModifiers, View view) {
        if (mainPageMenuView.isModifierOpen()) {
            if (animatorView == view) {
                return;
            } else {
                animatorView = view;
            }
        }
        mainPageMenuView
                .openModifiers(currentOrder, orderDetail, itemModifiers);
    }

    private void initOrder(TableInfo tables) {
        currentOrder = ObjectFactory.getInstance().getOrder(
                ParamConst.ORDER_ORIGIN_POS, App.instance.getSubPosBeanId(), tables,
                App.instance.getRevenueCenter(), App.instance.getUser(),
                App.instance.getSessionStatus(),
                App.instance.getBusinessDate(),
                App.instance.getIndexOfRevenueCenter(),
                ParamConst.ORDER_STATUS_OPEN_IN_POS,
                App.instance.getLocalRestaurantConfig()
                        .getIncludedTax().getTax(), "");
    }

    private void initOrderWaitingList(TableInfo tables) {
        currentOrder = ObjectFactory.getInstance().getOrderWaitingList(
                ParamConst.ORDER_ORIGIN_POS, App.instance.getSubPosBeanId(), tables,
                App.instance.getRevenueCenter(), App.instance.getUser(),
                App.instance.getSessionStatus(),
                App.instance.getBusinessDate(),
                App.instance.getIndexOfRevenueCenter(),
                ParamConst.ORDER_STATUS_OPEN_IN_POS,
                App.instance.getLocalRestaurantConfig()
                        .getIncludedTax().getTax());
    }

    private void getTableStatusInfo() {
        for (int i = 0; i < places.size(); i++) {
            int id = places.get(i).getId();
            int idleNum = 0;
            int diningNum = 0;
            int inCheckoutNum = 0;
            for (int j = 0; j < tables.size(); j++) {
                if (tables.get(j).getPlacesId() != id) {
                    continue;
                }
                int table_status = tables.get(j).getStatus();
                switch (table_status) {
                    case ParamConst.TABLE_STATUS_IDLE:
                        idleNum++;
                        break;
                    case ParamConst.TABLE_STATUS_DINING:
                        diningNum++;
                        break;
                    case ParamConst.TABLE_STATUS_INCHECKOUT:
                        inCheckoutNum++;
                        break;
                    default:
                        break;
                }
            }
            tableStatusInfo.add(new TablesStatusInfo(id, idleNum, diningNum,
                    inCheckoutNum));
        }
    }

    private void transferOrder(TableInfo table) {
        transferOrder(table, -1);
    }

    private void transferOrder(TableInfo table, int transferType) {
        //msg.what
        boolean fromThisRVC = checkIfTableFromThisRVC(table);
        if (fromThisRVC) {
            transferOrder(currentOrder, table.getPosId());
        } else {
            for (final MultiRVCPlacesDao.Places otherPlace : App.instance.getOtherRVCPlaces()) {
                if (table.getRevenueId().equals(otherPlace.getRevenueId())) {
                    SyncCentre.getInstance().sendOrderToOtherRVC(context, otherPlace.getIp(), transferType, currentOrder, table.getPosId(), handler);
                    break;
                }
            }
        }

    }

    private void transferOrder(Order order, int tableId) {
        order.setTableId(tableId);
        OrderSQL.update(order);
        setTablePacks(String.valueOf(order.getPersons()));
    }

    private void mergerOrder() {
        if (oldOrder == null) {
            return;
        }

        boolean fromThisRVC = checkIfTableFromThisRVC(currentTable);
        if (fromThisRVC) {
            List<OrderSplit> orderSplits = OrderSplitSQL.getFinishedOrderSplits(oldOrder.getId().intValue());
            StringBuffer orderSplitIds = new StringBuffer();
            if (orderSplits != null && orderSplits.size() > 0) {
                for (int i = 0; i < orderSplits.size(); i++) {
                    orderSplitIds.append(orderSplits.get(i).getId());
                    if (i < orderSplits.size() - 1) {
                        orderSplitIds.append(',');
                    }
                }
            }
            Order newOrder = OrderSQL.getUnfinishedOrderAtTable(currentTable.getPosId().intValue(), oldOrder.getBusinessDate(), App.instance.getSessionStatus());
            List<OrderDetail> orderDetails = new ArrayList<>();
            if (orderSplitIds.length() > 0) {
                orderDetails.addAll(OrderDetailSQL.getUnFreeOrderDetailsWithOutSplit(oldOrder, orderSplitIds.toString()));
            } else {
                orderDetails.addAll(OrderDetailSQL
                        .getUnFreeOrderDetails(oldOrder));
            }


            KotSummary kotSummary = KotSummarySQL.getKotSummary(oldOrder.getId(), oldOrder.getNumTag());
            if (!orderDetails.isEmpty()) {
                for (OrderDetail orderDetail : orderDetails) {
                    OrderDetail newOrderDetail = ObjectFactory.getInstance()
                            .getOrderDetailForTransferTable(newOrder, orderDetail);
                    OrderDetailSQL.addOrderDetailETC(newOrderDetail);
                    List<OrderModifier> orderModifiers = OrderModifierSQL
                            .getOrderModifiers(orderDetail);
                    if (kotSummary != null) {
                        KotItemDetailSQL.updateKotItemDetailId(newOrderDetail.getId().intValue(), orderDetail.getId().intValue(), kotSummary.getId().intValue());
                    }
                    if (orderModifiers.isEmpty()) {
                        continue;
                    }
                    for (OrderModifier orderModifier : orderModifiers) {
                        OrderModifier newOrderModifier = ObjectFactory
                                .getInstance().getOrderModifier(
                                        newOrder,
                                        newOrderDetail,
                                        CoreData.getInstance().getModifier(
                                                orderModifier.getModifierId()),
                                        orderModifier.getPrinterId().intValue());
                        OrderModifierSQL.addOrderModifier(newOrderModifier);
                    }
                }
            }

            if (orderSplitIds.length() > 0) {
                OrderDetailSQL.deleteOrderDetailByOrderOutsideOrderSplit(oldOrder.getId(), orderSplitIds.toString());
                OrderModifierSQL.deleteOrderModifierByOrderOutsideOrderDetail(oldOrder);
                OrderSplitSQL.deleteBySpliteIdList(oldOrder.getId(), orderSplitIds.toString());
                OrderSQL.updateOrder(oldOrder);
                OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_FINISHED, oldOrder.getId());
            } else {
                OrderDetailSQL.deleteOrderDetailByOrder(oldOrder);
                OrderModifierSQL.deleteOrderModifierByOrder(oldOrder);
                OrderSQL.deleteOrder(oldOrder);
            }

            TableInfo oldTable = TableInfoSQL.getTableById(oldOrder.getTableId().intValue());
            currentTable.setPacks(currentTable.getPacks() + oldTable.getPacks());

            initOrder(currentTable);
            setTablePacks(currentTable.getPacks() + "");
        } else {
            for (final MultiRVCPlacesDao.Places otherPlace : App.instance.getOtherRVCPlaces()) {
                if (currentTable.getRevenueId().equals(otherPlace.getRevenueId())) {
                    SyncCentre.getInstance().sendOrderToOtherRVC(context, otherPlace.getIp(), ACTION_MERGE_TABLE, oldOrder, currentTable.getPosId(), handler);
                    break;
                }
            }
        }

    }
}
