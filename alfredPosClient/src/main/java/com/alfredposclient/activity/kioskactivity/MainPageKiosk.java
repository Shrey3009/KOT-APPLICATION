package com.alfredposclient.activity.kioskactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.VerifyDialog;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
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
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserOpenDrawerRecord;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.temporaryforapp.TempModifierDetail;
import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.javabean.temporaryforapp.TempOrderDetail;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.GeneralSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
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
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.PromotionDataSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.UserOpenDrawerRecordSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.store.sql.temporaryforapp.TempModifierDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderSQL;
import com.alfredbase.utils.CallBack;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.MachineUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.NetWorkOrderActivity;
import com.alfredposclient.activity.StoredCardActivity;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.ReportObjectFactory;
import com.alfredposclient.global.SubPosSyncCentre;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.javabean.TablesStatusInfo;
import com.alfredposclient.jobs.CloudSyncJobManager;
import com.alfredposclient.jobs.SubPosCloudSyncJobManager;
import com.alfredposclient.popupwindow.CloseOrderSplitWindow;
import com.alfredposclient.popupwindow.CloseOrderWindow;
import com.alfredposclient.popupwindow.DiscountWindow;
import com.alfredposclient.popupwindow.DiscountWindow.ResultCall;
import com.alfredposclient.popupwindow.ModifyQuantityWindow;
import com.alfredposclient.popupwindow.ModifyQuantityWindow.DismissCall;
import com.alfredposclient.popupwindow.OpenItemWindow;
import com.alfredposclient.popupwindow.OrderSplitPrintWindow;
import com.alfredposclient.popupwindow.SetPAXWindow;
import com.alfredposclient.popupwindow.SetWeightWindow;
import com.alfredposclient.popupwindow.SpecialInstractionsWindow;
import com.alfredposclient.utils.AlertToDeviceSetting;
import com.alfredposclient.view.SelectOrderSplitDialog;
import com.alfredposclient.view.SettingView;
import com.alfredposclient.view.viewkiosk.MainPageMenuViewKiosk;
import com.alfredposclient.view.viewkiosk.MainPageOperatePanelKiosk;
import com.alfredposclient.view.viewkiosk.MainPageOrderViewKiosk;
import com.alfredposclient.view.viewkiosk.MainPageSearchViewKiosk;
import com.alfredposclient.view.viewkiosk.TopMenuViewKiosk;
import com.alfredposclient.xmpp.XMPP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainPageKiosk extends BaseActivity {
    private String TAG = MainPageKiosk.class.getSimpleName();
    private static final int GET_TABLESTATUSINFO_DATA = 100;

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
    // public static final int ACTION_SWITCH_USER_ERROR = 130;
    private static final int VIEW_EVENT_DISMISS_TABLES_AFTER_MERGER = 131;
    private static final int ACTION_TRANSFER_TABLE = 132;
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
    public static final int KIOSK_VIEW_EVENT_DELETE_ORDER = 1045;

    // KOT PRINT
    public static final int KOT_PRINT_FAILED = 204;
    public static final int KOT_PRINT_SUCCEED = 201;
    public static final int KOT_PRINT_NULL = 202;
    public static final int KOT_ITEM_PRINT_NULL = 203;

    public static final int VIEW_EVENT_TAKE_AWAY = 145;
    public static final int VIEW_EVENT_SET_WEIGHT = 147;
    public static final int CHECK_TO_CLOSE_CUSTOM_NOTE_VIEW = 148;
    public static final int CONTROL_PAGE_ORDER_VIEW_MASK = 149;
    public static final int VIEW_EVENT_START_KIOSK_BOLD = 150;


    public static final String REFRESH_TABLES_BROADCAST = "REFRESH_TABLES_BROADCAST";
    public static final String REFRESH_COMMIT_ORDER = "REFRESH_COMMIT_ORDER";
    private static final String SHOW_TABLES = "SHOW_TABLES";
    private static final String TRANSFER_TABLE = "TRANSFER_TABLE";
    private static final String HANDLER_MSG_OBJECT_DISCOUNT = "DISCOUNT";
    public static final String HANDLER_MSG_OBJECT_BILL_ON_HOLD = "BILL_ON_HOLD";
    public static final String HANDLER_MSG_OBJECT_VOID = "VOID";
    public static final String HANDLER_MSG_OBJECT_ENTERTAINMENT = "ENTERTAINMENT";
    private static final String HANDLER_MSG_OBJECT_TRANSFER_TABLE = "TRANSFERTABLE";
    private static final String HANDLER_MSG_OBJECT_ITEM_QTY = "ITEM_QTY";
    private static final String HANDLER_MSG_OBJECT_VOID_OR_FREE = "VOID_OR_FREE";

    private static final String PAMENT_METHOD = "PAMENTMETHOD";

    private static final String HANDLER_MSG_OBJECT_STORED_CARD_REFUND = "STORED_CARD_REFUND";
    private static final String HANDLER_MSG_OBJECT_STORED_CARD_LOSS = "STORED_CARD_LOSS";
    private static final String HANDLER_MSG_OBJECT_STORED_CARD_REPLACEMENT = "STORED_CARD_REPLACEMENT";
    private TopMenuViewKiosk topMenuView;
    private MainPageSearchViewKiosk mainPageSearchView;
    public MainPageOrderViewKiosk orderView;
    private MainPageOperatePanelKiosk operatePanel;
    private MainPageMenuViewKiosk mainPageMenuView;
    private CloseOrderWindow closeOrderWindow; // settlement window
    private CloseOrderSplitWindow closeOrderSplitWindow;
    private SetPAXWindow setPAXWindow;
    private DiscountWindow discountWindow;
    private ModifyQuantityWindow modifyQuantityWindow;
    private OpenItemWindow openItemWindow;
    private SpecialInstractionsWindow specialInstractionsWindow;
    private SetWeightWindow setWeightWindow;
    private JavaConnectJS javaConnectJS;
    private Gson gson = new Gson();
    private boolean isShowTables = false;
    private boolean isTableFirstShow = true;
    private String tableShowAction = SHOW_TABLES;
    private List<PlaceInfo> places = new ArrayList<PlaceInfo>();
    private List<TableInfo> tables = new ArrayList<TableInfo>();
    private List<TablesStatusInfo> tableStatusInfo = new ArrayList<TablesStatusInfo>();
    private TableInfo currentTable;
    private TableInfo oldTable;
    private Order currentOrder;
    private Order oldOrder;
    private List<OrderDetail> orderDetails;
    private VerifyDialog verifyDialog;
    private View view_top;
//	public LoadingDialog loadingDialog;

    public PrinterLoadingDialog printerLoadingDialog;

    public SelectOrderSplitDialog selectOrderSplitDialog;

    private OrderSplitPrintWindow orderSplitPrintWindow;

    private DrawerLayout mDrawerLayout; // activity滑动布局
    private SettingView settingView; // 右滑视图

    private int activityRequestCode = 0;
    private View view_top_line;
    private int appOrderId;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    //	private StoredCardActivity f_stored_card;
    private Observable<Integer> observable;
    private Observable<Object> observable1;


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
    protected void initView() {
        setContentView(R.layout.activity_kiosk_main_page);
        super.initView();
//		findViewById(R.id.rl_stored_card_fragment_father).setOnClickListener(null);
        fragmentManager = this.getSupportFragmentManager();
        view_top_line = findViewById(R.id.view_top_line);
//		f_stored_card = (StoredCardActivity) fragmentManager.findFragmentById(R.id.f_stored_card);
//		f_stored_card.setMainPageHandler(handler);
//		hideStoredCard();
        initDrawerLayout();
        ScreenSizeUtil.initScreenScale(context);
        verifyDialog = new VerifyDialog(context, handler);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getString(R.string.loading));
        printerLoadingDialog = new PrinterLoadingDialog(context);
        printerLoadingDialog.setTitle(context.getString(R.string.sending_to_kitchen));
        selectOrderSplitDialog = new SelectOrderSplitDialog(context, handler);

        topMenuView = (TopMenuViewKiosk) findViewById(R.id.topMenuView);
        topMenuView.setParams(this, handler, mDrawerLayout, settingView);
        topMenuView.setGetBillNum(App.instance.getGetTingBillNotifications()
                .size());
        mainPageSearchView = (MainPageSearchViewKiosk) findViewById(R.id.search);
        orderView = (MainPageOrderViewKiosk) findViewById(R.id.orderView);
        LinearLayout.LayoutParams ps1 = new LinearLayout.LayoutParams(
                (int) (750 * ScreenSizeUtil.width / ScreenSizeUtil.WIDTH_POS),
                LayoutParams.MATCH_PARENT);
        orderView.setLayoutParams(ps1);
        mainPageMenuView = (MainPageMenuViewKiosk) findViewById(R.id.mainPageMenuView);
        mainPageMenuView.setParent(context);
        operatePanel = (MainPageOperatePanelKiosk) findViewById(R.id.operatePanel);
        LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
                (int) (250 * ScreenSizeUtil.width / ScreenSizeUtil.WIDTH_POS),
                LayoutParams.MATCH_PARENT);
        operatePanel.setLayoutParams(ps);
        closeOrderWindow = new CloseOrderWindow(this,
                findViewById(R.id.rl_root), handler);
        closeOrderSplitWindow = new CloseOrderSplitWindow(this,
                findViewById(R.id.rl_root), handler);
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
        PlaceInfo placeInfo = PlaceInfoSQL.getKioskPlaceInfo();
        if (placeInfo == null) {
            placeInfo = ObjectFactory.getInstance().addNewPlace(App.instance.getRevenueCenter().getRestaurantId().intValue(),
                    App.instance.getRevenueCenter().getId().intValue(), "kiosk");
            placeInfo.setIsKiosk(1);
            PlaceInfoSQL.addPlaceInfo(placeInfo);
        }
        currentTable = TableInfoSQL.getKioskTable();
        if(currentTable == null){
            currentTable = ObjectFactory.getInstance().addNewTable("table_1_1", placeInfo.getRestaurantId().intValue(), placeInfo.getRevenueId().intValue(), placeInfo.getId().intValue(), 480,800);
            currentTable.setIsKiosk(1);
            TableInfoSQL.updateTables(currentTable);
        }
        if(currentTable == null){
//            currentOrder = ObjectFactory.getInstance().getTa
        }
        view_top = findViewById(R.id.view_top);
        setData();
        observable = RxBus.getInstance().register(RxBus.RX_MSG_1);
        observable1 = RxBus.getInstance().register("open_drawer");
        observable.observeOn(AndroidSchedulers.mainThread()).onBackpressureBuffer(10).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer object) {
//				showStoredCard();
                if (object != null) {
                    if (object.intValue() == 1)
                        topMenuView.refreshUserName();
                    else if (object.intValue() == 2) {
                        topMenuView.showAppOrderReciving();
                    } else if (object.intValue() == 3) {
                        topMenuView.showKioskHoldNum();
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
        long nowTime = System.currentTimeMillis();
        int count = OrderSQL.getKioskHoldCount(App.instance.getBusinessDate(), App.instance.getSessionStatus(), nowTime);
        App.instance.setKioskHoldNum(count);
        App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
        XMPP.getInstance().setCanCheckAppOrder(true);


    }

//	private void showStoredCard(){
//		findViewById(R.id.rl_stored_card_fragment_father).setVisibility(View.VISIBLE);
//		transaction = fragmentManager.beginTransaction();
////        transaction.setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
//		transaction.show(f_stored_card);
//		transaction.commitAllowingStateLoss();
//	}
//
//	private void hideStoredCard(){
//		findViewById(R.id.rl_stored_card_fragment_father).setVisibility(View.GONE);
//		transaction = fragmentManager.beginTransaction();
////        transaction.setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
//		transaction.hide(f_stored_card);
//		transaction.commitAllowingStateLoss();
//	}

    private void setTablePacks(String tablePacks) {
        if (currentTable != null) {
            currentTable.setPacks(Integer.parseInt(tablePacks));
            currentTable.setStatus(ParamConst.TABLE_STATUS_DINING);
            TableInfoSQL.updateTables(currentTable);
        }
    }

    private void getPlaces() {
        places = PlaceInfoSQL.getAllKioskPlaceInfo();
        getTables();
    }

    private void getTables() {
        tables = TableInfoSQL.getAllTables();
        handler.sendEmptyMessage(GET_TABLESTATUSINFO_DATA);
    }

    private View animatorView;

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
                        .getIncludedTax().getTax(),"");
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

    private void transferOrder(int tableId) {
        currentOrder.setTableId(tableId);
        OrderSQL.update(currentOrder);
//		handler.sendMessage(handler.obtainMessage(VIEW_EVENT_SET_TABLE_PACKS,
//				currentOrder.getPersons() + ""));
        setTablePacks(String.valueOf(currentOrder.getPersons()));
    }

    private void mergerOrder() {
        if (oldOrder == null) {
            return;
        }
        Order newOrder = OrderSQL.getUnfinishedOrderAtTable(currentTable.getPosId(), oldOrder.getBusinessDate(), App.instance.getSessionStatus());
        List<OrderDetail> orderDetails = OrderDetailSQL
                .getUnFreeOrderDetails(oldOrder);
        if (!orderDetails.isEmpty()) {
            for (OrderDetail orderDetail : orderDetails) {
                OrderDetail newOrderDetail = ObjectFactory.getInstance()
                        .getOrderDetailForTransferTable(newOrder, orderDetail);
                OrderDetailSQL.addOrderDetailETC(newOrderDetail);
                List<OrderModifier> orderModifiers = OrderModifierSQL
                        .getOrderModifiers(orderDetail);
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
        OrderDetailSQL.deleteOrderDetailByOrder(oldOrder);
        OrderModifierSQL.deleteOrderModifierByOrder(oldOrder);
        OrderSQL.deleteOrder(oldOrder);
        initOrder(currentTable);

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_STOCK_NUM:
                    if (mainPageMenuView != null) {
                        mainPageMenuView.setParam(currentOrder, handler);
                    }
                    break;
//			case REFRESH_TABLES_STATUS:
//				Tables table = (Tables) msg.obj;
//				web_tables
//						.loadUrl("javascript:JsConnectAndroid('RefreshTableStatus','"
//								+ getRefreshTableStatusStr(table) + "')");
//				System.out
//						.println("javascript:JsConnectAndroid('RefreshTableStatus','"
//								+ getRefreshTableStatusStr(table) + "')");
//				break;

//			case StoredCardActivity.VIEW_EVENT_STORED_CARD_PAY_RESULT: {
//				loadingDialog.show();
//				Map<String, Object> map = (Map<String, Object>) msg.obj;
//
//				SyncCentre.getInstance().updateStoredCardValue(context, map, handler);
//
//			}
//				break;
                case StoredCardActivity.VIEW_EVENT_STORED_CARD_PAY: {
                    String amount = (String) msg.obj;
                    Intent intent = new Intent(context, StoredCardActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("isPayAction", true);
//					UIHelp.startSoredCardActivity(context, MainPage.CHECK_REQUEST_CODE);
                    context.startActivityForResult(intent, MainPage.CHECK_REQUEST_CODE);
                }
                break;
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
                case GET_TABLESTATUSINFO_DATA:
                    getTableStatusInfo();
                    handler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_REFRESH);
                    break;
                case VIEW_EVENT_DISMISS_TABLES:
//				dismissTables();
                    if (loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    setData();
                    break;
                case VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER:
//				dismissTables();
                    setData();
                    break;
                case VIEW_EVENT_DISMISS_TABLES_AFTER_MERGER:
//				dismissTables();
                    mergerOrderSetData();
                    // App.instance.getKdsJobManager().transferTableDownKot(ParamConst.JOB_MERGER_KOT,
                    // toKotSummary, fromKotSummary);
                    break;
                case VIEW_EVENT_SET_TABLE_PACKS:
                    setData();
//				setTablePacks((String) msg.obj);
//				handler.sendEmptyMessage(VIEW_EVENT_DISMISS_TABLES);
                    break;
                // Open settlement window
                case VIEW_EVENT_SHOW_CLOSE_ORDER_WINDOW:

                    if (IntegerUtils.isEmptyOrZero(currentOrder.getAppOrderId())) {
                        currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_UNPAY);
                        OrderSQL.update(currentOrder);
                        showCloseBillWindow();
                    } else {
                        TempOrder tempOrder = TempOrderSQL.getTempOrderByAppOrderId(currentOrder.getAppOrderId());
                        if (tempOrder != null && tempOrder.getPaied() == ParamConst.TEMPORDER_PAIED) {
                            TempOrderISPaied();
                        } else {
                            currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_UNPAY);
                            OrderSQL.update(currentOrder);
                            showCloseBillWindow();
                        }
                    }


                    break;
                case VIEW_EVENT_SHOW_TABLES: {
                    tableShowAction = SHOW_TABLES;
                    if (currentOrder != null) {
                        if (orderDetails != null && orderDetails.size() <= 0) {
                            KotSummarySQL.deleteKotSummaryByOrder(currentOrder);
                        }
                    }
//				showTables();
                }
                break;
                case VIEW_EVENT_SHOW_TABLES_AFTER_CLOSE_BILL: {
                    selectOrderSplitDialog.dismiss();
                    tableShowAction = SHOW_TABLES;
                    currentOrder = null;
//				showTables();
                    setData();
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
                            orderSplitPrintWindow.show(MainPageKiosk.this, currentOrder, handler);
                        } else {
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
                                            currentTable.getName(), 1,App.instance.getSystemSettings().getTrainType());
                            currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_UNPAY);
                            OrderSQL.update(currentOrder);
                            ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                                    .getInstance().getItemModifierList(currentOrder, OrderDetailSQL.getOrderDetails(currentOrder
                                            .getId()));
                            List<OrderPromotion>  orderPromotions= PromotionDataSQL.getPromotionDataOrOrderid(currentOrder.getId());

                            App.instance.remoteBillPrint(printer, title, currentOrder,
                                    orderItems, orderModifiers, taxMap, null, null,orderPromotions);
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
                    HashMap<String, String> paymentMap = (HashMap<String, String>) msg.obj;
                    String changeNum;
                    final Order paidOrder = OrderSQL.getOrder(Integer.valueOf(paymentMap.get("orderId")));
                    List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL
                            .getAllPaymentSettlementByPaymentId(Integer.valueOf(paymentMap.get("paymentId")));
                    boolean isPrint = true;
                    changeNum = paymentMap.get("changeNum");
                    if (paymentMap.containsKey("isPrint") && !TextUtils.isEmpty(paymentMap.get("isPrint"))) {
                        isPrint = Boolean.valueOf(paymentMap.get("isPrint"));
                    }
//				KotSummary kotSummary = KotSummarySQL.getKotSummary(currentOrder.getId());
//				if(kotSummary != null){
//					kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
//					KotSummarySQL.update(kotSummary);
//				}

                    if (isPrint) {

                        PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                                context);
                        printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
                        printerLoadingDialog.showByTime(3000);

                        PrinterDevice printer = App.instance.getCahierPrinter();
                        PrinterTitle title = ObjectFactory.getInstance()
                                .getPrinterTitle(
                                        App.instance.getRevenueCenter(),
                                        paidOrder,
                                        App.instance.getUser().getFirstName()
                                                + App.instance.getUser().getLastName(),
                                        currentTable.getName(), 1,App.instance.getSystemSettings().getTrainType());


                        if (!TextUtils.isEmpty(changeNum)) {

                            if (!(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + "0.00").equals(changeNum))
                                DialogFactory.changeDialogOrder(context, changeNum, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        }

                        ArrayList<PrintOrderItem> orderItems = ObjectFactory
                                .getInstance().getItemList(
                                        OrderDetailSQL.getOrderDetails(paidOrder
                                                .getId()));
                        List<Map<String, String>> taxMap = OrderDetailTaxSQL
                                .getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), paidOrder);

                        ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
                                .getInstance().getItemModifierList(paidOrder, OrderDetailSQL.getOrderDetails(paidOrder
                                        .getId()));
                        List<PrinterDevice> printerList = App.instance.getPrinterLable();
                        ModifierCheckSql.deleteAllModifierCheck(paidOrder.getId());
                        if (printerList.size() > 0) {
                            for (int i = 0; i < printerList.size(); i++) {
                                PrinterDevice printers = printerList.get(i);
                                if (App.instance.isRevenueKiosk() && App.instance.getSystemSettings().isPrintLable() && printers.getIsLablePrinter() == 1) {
                                    List<OrderDetail> placedOrderDetails
                                            = OrderDetailSQL.getOrderDetailsForPrint(paidOrder.getId());
                                    App.instance.remoteTBillPrint(printers, title, paidOrder, (ArrayList<OrderDetail>) placedOrderDetails, orderModifiers);
//						}
                                }
                            }
                        }

                        if (orderItems.size() > 0 && printer != null) {
                            RoundAmount roundAmount = RoundAmountSQL.getRoundAmount(paidOrder);
//
                            List<OrderPromotion>  orderPromotions= PromotionDataSQL.getPromotionDataOrOrderid(currentOrder.getId());

//                            if (App.instance.isRevenueKiosk() && !App.instance.getSystemSettings().isPrintBill()) {
//
//                            } else {
                            if (!App.instance.isRevenueKiosk()) {
                                App.instance.remoteBillPrint(printer, title, paidOrder,
                                            orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount,orderPromotions);
                            } else {
                                if (printer.getIsLablePrinter() == 0) {
                                    App.instance.remoteBillPrint(printer, title, paidOrder,
                                                orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount,orderPromotions);
                                }
                            }
//                            }
                        }
//

                    } else {
                        PrinterDevice printer = App.instance.getCahierPrinter();
                        if (printer == null) {
                            AlertToDeviceSetting.noKDSorPrinter(context,
                                    context.getResources().getString(R.string.no_cashier_printer));
                        } else {
                            App.instance.kickOutCashDrawer(printer);
                        }
                    }
                    if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {

                        //Sent to Kitchen after close bill in kiosk mode
                        String kotCommitStatus = ParamConst.JOB_NEW_KOT;
                        List<OrderDetail> placedOrderDetails = OrderDetailSQL.getOrderDetails(paidOrder.getId());
                        List<Integer> orderDetailIds = new ArrayList<Integer>();
                        ArrayList<OrderModifier> kotorderModifiers = new ArrayList<OrderModifier>();
                        ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();

                        KotSummary kotSummary = KotSummarySQL.getKotSummary(paidOrder.getId(), paidOrder.getNumTag());
                        if (kotSummary != null) {
                            ArrayList<KotItemDetail> kotItemDetails = new ArrayList<>();
                            for (OrderDetail orderDetail : placedOrderDetails) {
                                if (orderDetail.getOrderDetailStatus() >= ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                                    continue;
                                }
                                orderDetailIds.add(orderDetail.getId());
                                KotItemDetail kotItemDetail = ObjectFactory
                                        .getInstance()
                                        .getKotItemDetail(
                                                paidOrder,
                                                orderDetail,
                                                CoreData.getInstance()
                                                        .getItemDetailById(
                                                                orderDetail.getItemId(),
                                                                orderDetail.getItemName()),
                                                kotSummary,
                                                App.instance.getSessionStatus(), ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN);
                                kotItemDetail.setItemNum(orderDetail
                                        .getItemNum());
                                kotItemDetails.add(kotItemDetail);
                                ArrayList<KotItemModifier> kotItemModifierObj = KotItemModifierSQL
                                        .getKotItemModifiersByKotItemDetail(kotItemDetail.getId());
                                if (kotItemModifierObj != null)
                                    kotItemModifiers.addAll(kotItemModifierObj);

                            }
//                            ArrayList<KotItemDetail> kotItemDetailList =
//                                    KotItemDetailSQL.getKotItemDetailBySummaryIdandOrderId(kotSummary.getId(), paidOrder.getId());
//                            kotorderModifiers = OrderModifierSQL.getAllOrderModifierByOrderAndNormal(paidOrder);
//
//                            for (KotItemDetail kot : kotItemDetailList) {
//                                if(kot.getKotStatus() > ParamConst.KOT_STATUS_UNSEND){
//                                    continue;
//                                }
//                                kotItemDetails.add(kot);
//                                ArrayList<KotItemModifier> kotItemModifierObj = KotItemModifierSQL
//                                        .getKotItemModifiersByKotItemDetail(kot.getId());
//                                if (kotItemModifierObj != null)
//                                    kotItemModifiers.addAll(kotItemModifierObj);
//                            }


//					List<OrderDetail> placedOrderDetails
//							= OrderDetailSQL.getOrderDetailsForPrint(paidOrder.getId());

                            PrinterTitle title = ObjectFactory.getInstance()
                                    .getPrinterTitle(
                                            App.instance.getRevenueCenter(),
                                            paidOrder,
                                            App.instance.getUser().getFirstName()
                                                    + App.instance.getUser().getLastName(),
                                            currentTable.getName(), 1,App.instance.getSystemSettings().getTrainType());

                            Map<String, Object> orderMap = new HashMap<String, Object>();

                            orderMap.put("orderId", paidOrder.getId());
                            orderMap.put("orderDetailIds", orderDetailIds);
                            orderMap.put("paidOrder", paidOrder);
                            orderMap.put("title", title);
                            orderMap.put("placedOrderDetails", placedOrderDetails);
                            App.instance.getKdsJobManager().tearDownKot(
                                    kotSummary, kotItemDetails,
                                    kotItemModifiers, kotCommitStatus,
                                    orderMap);
                        }
                        //end KOT print
                    }
                    // remove get bill notification
                    removeNotificationTables();
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
                    /**
                     * 给后台发送log 信息
                     */
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
                                CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                                if (cloudSync != null) {

                                    cloudSync.syncOrderInfoForLog(paidOrder.getId(),
                                            App.instance.getRevenueCenter().getId(),
                                            App.instance.getBusinessDate(), 1);
                                }
                            } else {
                                SubPosCloudSyncJobManager subPosCloudSyncJobManager = App.instance.getSubPosSyncJob();
                                if (subPosCloudSyncJobManager != null) {
                                    subPosCloudSyncJobManager.syncOrderInfo(paidOrder.getId(),
                                            App.instance.getRevenueCenter().getId(),
                                            App.instance.getBusinessDate());
                                }
                                handler.sendEmptyMessage(VIEW_EVENT_SET_DATA);
                            }
                        }
                    }).start();
                    break;
                }
                case VIEW_EVENT_CLOSE_SPLIT_BILL: {

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
                    PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                            context);
                    printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
                    printerLoadingDialog.showByTime(3000);
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
                    // ArrayList<OrderModifier> orderModifiers =
                    // OrderModifierSQL.getAllOrderModifierByOrderAndNormal(currentOrder);
                    Order temporaryOrder = new Order();
                    temporaryOrder.setPersons(paidOrderSplit.getPersons());
                    temporaryOrder.setSubTotal(paidOrderSplit.getSubTotal());
                    temporaryOrder.setDiscountAmount(paidOrderSplit.getDiscountAmount());
                    temporaryOrder.setTotal(paidOrderSplit.getTotal());
                    temporaryOrder.setTaxAmount(paidOrderSplit.getTaxAmount());
                    if (App.instance.getPosType() == ParamConst.POS_TYPE_SUB && App.instance.getSubPosBean() != null) {
                        temporaryOrder.setNumTag(App.instance.getSubPosBean().getNumTag());
                    }

                    ModifierCheckSql.deleteAllModifierCheck(paidOrderSplit.getOrderId());
                    List<PrinterDevice> printerList = App.instance.getPrinterLable();
                    if (printerList.size() > 0) {
                        for (int i = 0; i < printerList.size(); i++) {
                            PrinterDevice printers = printerList.get(i);
                            if (App.instance.getSystemSettings().isPrintLable() && printers.getIsLablePrinter() == 1) {
                                App.instance.remoteTBillPrint(printers, title, temporaryOrder, orderSplitDetails, orderModifiers);
//						}
                            }
                        }
                    }

                    if (orderItems.size() > 0 && printer != null) {
                        RoundAmount roundAmount = RoundAmountSQL.getRoundAmount(temporaryOrder);
//                        if (App.instance.getSystemSettings().isPrintBill()) {
                        App.instance.remoteBillPrint(printer, title, temporaryOrder,
                                    orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount,null);
//                        }
                    }
//
//				if (orderItems.size() > 0 && printer != null) {
//					RoundAmount roundAmount = RoundAmountSQL.getRoundAmount(temporaryOrder);
//
//					SystemSettings       settings = new SystemSettings(context);
//					if(App.instance.isRevenueKiosk()&&settings.isPrintLable()&&printer.getIsLablePrinter()==1&&printer.getIP().indexOf(":") != -1 )
//					{
//
//						PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
//								context);
//						printerLoadingDialog.setTitle(context.getResources().getString(R.string.receipt_printing));
//						printerLoadingDialog.showByTime(3000);
//						List<OrderDetail> placedOrderDetails = OrderDetailSQL.getOrderDetailsForPrint(paidOrderSplit.getOrderId());
//						App.instance.remoteTBillPrint(printer,title,temporaryOrder, (ArrayList<OrderDetail>) placedOrderDetails,orderModifiers);
//					}else {
//
//
//						if(App.instance.isRevenueKiosk()&&!App.instance.getSystemSettings().isPrintBill())
//						{
//
//						}else {
//							PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
//									context);
//
//							App.instance.remoteBillPrint(printer, title, temporaryOrder,
//									orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount);
//						}
//					}
////					App.instance.remoteBillPrint(printer, title, temporaryOrder,
////							orderItems, orderModifiers, taxMap, paymentSettlements, roundAmount);
//				}
                    if (App.instance.getPosType() == 0) {
                        //Sent to Kitchen after close bill in kiosk mode
                        String kotCommitStatus = ParamConst.JOB_NEW_KOT;
                        List<OrderDetail> placedOrderDetails = OrderDetailSQL.getOrderDetailsByOrderAndOrderSplit(paidOrderSplit);
                        List<Integer> orderDetailIds = new ArrayList<Integer>();
                        ArrayList<OrderModifier> kotorderModifiers = new ArrayList<OrderModifier>();
                        ArrayList<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();

                        KotSummary kotSummary = KotSummarySQL.getKotSummary(paidOrderSplit.getOrderId(), "");
                        if (kotSummary != null) {
                            ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
                            for (OrderDetail orderDetail : placedOrderDetails) {
                                orderDetailIds.add(orderDetail.getId());
                                KotItemDetail kotItemDetail = KotItemDetailSQL.getMainKotItemDetailByOrderDetailId(kotSummary.getId(), orderDetail.getId());
                                kotItemDetails.add(kotItemDetail);
                            }


//					kotorderModifiers = OrderModifierSQL.getAllOrderModifierByOrderAndNormal(paidOrder);
                            for (KotItemDetail kot : kotItemDetails) {
                                ArrayList<KotItemModifier> kotItemModifierObj = KotItemModifierSQL
                                        .getKotItemModifiersByKotItemDetail(kot.getId());
                                if (kotItemModifierObj != null)
                                    kotItemModifiers.addAll(kotItemModifierObj);
                            }
                            Map<String, Object> orderMap = new HashMap<String, Object>();
                            orderMap.put("orderId", paidOrderSplit.getOrderId());
                            orderMap.put("orderDetailIds", orderDetailIds);
                            App.instance.getKdsJobManager().tearDownKot(
                                    kotSummary, kotItemDetails,
                                    kotItemModifiers, kotCommitStatus,
                                    orderMap);
                        }
                    }
                    // remove get bill notification
                    removeNotificationTables();
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
                    final Order checkOrder = OrderSQL.getOrder(paidOrderSplit.getOrderId());
                    if (checkOrder.getOrderStatus().intValue() == ParamConst.ORDER_STATUS_FINISHED) {
                        /**
                         * 给后台发送log 信息
                         */
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
                                    CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                                    if (cloudSync != null) {

                                        cloudSync.syncOrderInfoForLog(checkOrder.getId(),
                                                App.instance.getRevenueCenter().getId(),
                                                App.instance.getBusinessDate(), 1);
                                    }
                                } else {
                                    SubPosCloudSyncJobManager subPosCloudSyncJobManager = App.instance.getSubPosSyncJob();
                                    if (subPosCloudSyncJobManager != null) {
                                        subPosCloudSyncJobManager.syncOrderInfo(checkOrder.getId(),
                                                App.instance.getRevenueCenter().getId(),
                                                App.instance.getBusinessDate());
                                    }
                                    handler.sendEmptyMessage(VIEW_EVENT_SET_DATA);
                                }
                            }
                        }).start();
                    } else {
                        handler.sendEmptyMessage(VIEW_EVENT_SET_DATA);
                    }
                }
                break;
                case VIEW_EVENT_SHOW_DISCOUNT_WINDOW: {
                    verifyDialog.show(HANDLER_MSG_OBJECT_DISCOUNT,
                            (Map<String, Object>) msg.obj);
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
                    if(msg.arg1 > 0){ // When need refresh Menu List
                        mainPageMenuView.refreshAllMenu();
                    }
                    break;
                case VIEW_EVENT_SET_DATA:
                    setData();
                    if(msg.arg1 > 0){ // When need refresh Menu List
                        mainPageMenuView.refreshAllMenu();
                    }
                    break;
                case MainPage.VIEW_EVENT_SET_DATA_AND_CLOSE_MODIFIER:
                    setData();
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
                            setPAXWindow.show(SetPAXWindow.GENERAL_ORDER, context.getResources().getString(R.string.no_of_pax));
                        } else {
                            handler.sendMessage(handler
                                    .obtainMessage(MainPageKiosk.VIEW_EVENT_DISMISS_TABLES));
                        }
                    }
                }
                break;
                case JavaConnectJS.ACTION_CLICK_TABLE_TRANSFER: {
                    Gson gson = new Gson();
                    String json = (String) msg.obj;
                    JSONObject jsonject;
                    try {
                        jsonject = new JSONObject(json);
                        final TableInfo newTable = gson.fromJson(
                                jsonject.getString("table"),
                                new TypeToken<TableInfo>() {
                                }.getType());
                        if (newTable.getPosId().intValue() == oldTable.getPosId()
                                .intValue()) {
                            currentTable = newTable;
                            if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                setPAXWindow.show(SetPAXWindow.GENERAL_ORDER, context.getResources().getString(R.string.no_of_pax));
                            } else {
                                handler.sendMessage(handler
                                        .obtainMessage(MainPageKiosk.VIEW_EVENT_DISMISS_TABLES));
                            }
                        } else {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case ACTION_TRANSFER_TABLE: {
                    currentTable = (TableInfo) msg.obj;
                    if (currentTable != null) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (currentTable.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                                    OrderBill orderBill = OrderBillSQL
                                            .getOrderBillByOrder(currentOrder);
                                    if (orderBill != null
                                            && orderBill.getBillNo() != null) {
                                        currentOrder.setTableId(currentTable
                                                .getPosId());
                                        OrderSQL.update(currentOrder);
                                        KotSummary fromKotSummary = KotSummarySQL
                                                .getKotSummary(currentOrder.getId(), currentOrder.getNumTag());
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
                                    } else {
                                        transferOrder(currentTable.getPosId());
                                    }
                                    handler.sendMessage(handler
                                            .obtainMessage(VIEW_EVENT_DISMISS_TABLES_AFTER_TRANSFER));
                                } else {
                                    oldOrder = currentOrder;
                                    OrderBill orderBill = OrderBillSQL
                                            .getOrderBillByOrder(oldOrder);
                                    if (orderBill != null
                                            && orderBill.getBillNo() != null) {
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
                                        App.instance.getKdsJobManager()
                                                .transferTableDownKot(
                                                        ParamConst.JOB_MERGER_KOT,
                                                        toKotSummary,
                                                        fromKotSummary, parameters);
                                    } else {
                                        mergerOrder();
                                        handler.sendMessage(handler
                                                .obtainMessage(VIEW_EVENT_DISMISS_TABLES_AFTER_MERGER));
                                    }

                                }
                                if (oldTable != null) {
                                    oldTable.setStatus(ParamConst.TABLE_STATUS_IDLE);
//								TablesSQL.updateTables(oldTable);
                                    TableInfoSQL.updateTables(oldTable);
                                }
                            }
                        }).start();
                    }
                }
                break;
                case JavaConnectJS.ACTION_LOAD_TABLES:
                    getPlaces();
                    break;
                case JavaConnectJS.ACTION_CLICK_BACK:
                    onBackPressed();
                    break;
//			case JavaConnectJS.ACTION_CLICK_REFRESH:
//				web_tables
//						.loadUrl("javascript:JsConnectAndroid('RefreshTables','"
//								+ getJsonStr() + "')");
//				LogUtil.i(TAG, "javascript:JsConnectAndroid('RefreshTables','"
//						+ getJsonStr() + "')");
//				break;
                case VIEW_EVENT_PLACE_ORDER:
                    mainPageMenuView.closeModifiers();
                    break;
                case VIEW_EVENT_CLOSE_MODIFIER_VIEW:
                    mainPageMenuView.closeModifiers();
                    break;
                case VIEW_EVENT_SELECT_BILL:
                    currentTable = (TableInfo) msg.obj;
                    TableInfo tables = (TableInfo) msg.obj;
                    if (tables.getStatus() == ParamConst.TABLE_STATUS_IDLE) {
                        return;
                    }
                    handler.sendMessage(handler
                            .obtainMessage(MainPageKiosk.VIEW_EVENT_DISMISS_TABLES));
                    App.instance.removeGettingBillNotification(currentTable);
                    topMenuView.setGetBillNum(App.instance
                            .getGetTingBillNotifications().size());
                    break;
                case VerifyDialog.DIALOG_DISMISS:

                    break;
                case VerifyDialog.DIALOG_RESPONSE: {
                    Map<String, Object> result = (Map<String, Object>) msg.obj;
                    User user = (User) result.get("User");


                    //		Toast.makeText(context,result.get("MsgObject")+"--111111- ",Toast.LENGTH_LONG).show();

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
//						showTables();
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
                                    handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_SET_DATA);
                                    String kotCommitStatus = ParamConst.JOB_VOID_KOT;
                                    KotSummary kotSummary = KotSummarySQL.getKotSummary(orderDetail
                                            .getOrderId(), "");
                                    KotItemDetail kotItemDetail = KotItemDetailSQL
                                            .getMainKotItemDetailByOrderDetailId(kotSummary.getId(), orderDetail
                                                    .getId());
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

                                    // fix issue: kot print no modifier showup
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
                                    handler.sendEmptyMessage(MainPageKiosk.VIEW_EVENT_SET_DATA);
                                }
                            }).start();
                        }
                    } else if (result.get("MsgObject").equals(MainPage.HANDLER_MSG_OBJECT_OPEN_DRAWER)) {
                        User openUser = user;
                        ObjectFactory.getInstance().getUserOpenDrawerRecord(App.instance.getRevenueCenter().getRestaurantId().intValue(),
                                App.instance.getRevenueCenter().getId().intValue(),
                                openUser,
                                App.instance.getUser().getId().intValue(),
                                App.instance.getSessionStatus().getSession_status());
//					settingView.openDrawer();
                        App.instance.kickOutCashDrawer(App.instance.getCahierPrinter());
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
                    MainPageKiosk.this.finish();
                    break;
                case ResultCode.CONNECTION_FAILED:
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context,
                            (Throwable) msg.obj, context.getResources().getString(R.string.server)));
                    break;
                case KOT_PRINT_NULL:
                    loadingDialog.dismiss();
                    printerLoadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.no_set_kds_printer));
                    break;
                case KOT_ITEM_PRINT_NULL: {
                    String itemName = (String) msg.obj;
                    loadingDialog.dismiss();
                    printerLoadingDialog.dismiss();


                    if (App.instance.isRevenueKiosk() && !App.instance.getSystemSettings().isPrintBill()) {
                        return;
                        //	UIHelp.showToast(context, String.format(context.getResources().getString(R.string.no_set_item_print), itemName));

                    } else {
                        UIHelp.showToast(context, String.format(Locale.US,context.getResources().getString(R.string.no_set_item_print), itemName));
                    }

                }
                // kot Print status
                case KOT_PRINT_FAILED:
                    loadingDialog.dismiss();
                    printerLoadingDialog.dismiss();


                    if (App.instance.isRevenueKiosk() && !App.instance.getSystemSettings().isPrintBill()) {
                        return;
                        //	UIHelp.showToast(context, String.format(context.getResources().getString(R.string.no_set_item_print), itemName));

                    } else {
                        UIHelp.showToast(context, context.getResources().getString(R.string.place_order_failed));
                    }

                    //	UIHelp.showToast(context, context.getResources().getString(R.string.place_order_failed));
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
                    }
                    break;
                case VIEW_EVENT_SHOW_SPECIAL_INSTRACTIONS_WINDOW: {
                    OrderDetail orderDetail = (OrderDetail) msg.obj;
                    specialInstractionsWindow.show(view_top_line, orderDetail);
                }
                break;
                case ParamConst.JOB_TYPE_POS_TRANSFER_TABLE:
                    int id = (Integer) msg.obj;
                    transferOrder(id);
                    break;
                case VIEW_EVENT_TANSFER_PAX:
                    String pax = (String) msg.obj;
                    setPAXWindow.show(pax, currentOrder, context.getResources().getString(R.string.no_of_pax));
                    break;
                case VIEW_EVENT_VOID_OR_FREE:
                    verifyDialog.show(HANDLER_MSG_OBJECT_VOID_OR_FREE,
                            (Map<String, Object>) msg.obj);
                    break;
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
                case KIOSK_VIEW_EVENT_DELETE_ORDER:
                    OrderDetailSQL.deleteOrderDetailByOrder(currentOrder);
                    KotSummarySQL.deleteKotSummaryByOrder(currentOrder);
                    OrderBillSQL.deleteOrderBillByOrder(currentOrder);
                    OrderSQL.deleteOrder(currentOrder);

                    if (currentOrder != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                KotSummary kotSummary = KotSummarySQL.getKotSummary(currentOrder.getId(),
                                        currentOrder.getNumTag());
                                if (kotSummary != null)
                                    App.instance.getKdsJobManager().deleteKotSummaryAllKds(kotSummary,
                                            KotItemDetailSQL.getKotItemDetailBySummaryId(kotSummary.getId()));
                            }
                        }).start();
                    }

                    setData();
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
                            orderDetail.setSpecialInstractions(orderDetail.getSpecialInstractions().toString().replace(context.getResources().getString(R.string.takeaway), "").replace(context.getResources().getString(R.string.takeaway), ""));
                        }
                    }
                    OrderHelper.getOrderDetailTax(currentOrder, orderDetail);
                    //  OrderDetailSQL.updateOrderDetail(orderDetail);
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
                                new ArrayList<KotItemModifier>(),
                                kotCommitStatus, orderMap);
                    }
                }
                break;
                case VIEW_EVENT_SET_WEIGHT: {
                    OrderDetail orderDetail = (OrderDetail) msg.obj;
                    setWeightWindow.show(orderDetail);
                }
                break;
                case CONTROL_PAGE_ORDER_VIEW_MASK:
                    orderView.showOrCloseMask((Boolean) msg.obj);
                    break;
                case CHECK_TO_CLOSE_CUSTOM_NOTE_VIEW:
                    mainPageMenuView.checkToCloseCustomNoteView();
                    break;
                case VIEW_EVENT_START_KIOSK_BOLD:
                    UIHelp.startKioskHoldActivity(context, orderDetails != null && orderDetails.size() > 0, currentOrder);
                    break;
                default:
                    break;
            }
        }

        ;
    };

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
        if (selectOrderSplitDialog != null && selectOrderSplitDialog.isShowing()) {
            selectOrderSplitDialog.dismiss();
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
            msg.what = MainPageKiosk.VIEW_EVENT_DISMISS_SEARCH;
            handler.sendMessage(msg);
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
        if (currentOrder.getOrderStatus().intValue() != ParamConst.ORDER_STATUS_UNPAY) {
            UIHelp.showToast(context, context.getResources().getString(R.string.print_bill_));
            return;
        }
        if (orderBill != null && orderBill.getBillNo() != null) {

            List<OrderSplit> orderSplits = OrderSplitSQL.getOrderSplits(currentOrder);
            if (orderSplits.isEmpty()) {
                closeOrderWindow.show(view_top_line, currentOrder, operatePanel.getWidth(), orderBill, orderDetails);
            } else {
                int count = OrderDetailSQL.getOrderDetailCountByGroupId(0, currentOrder.getId());
                if (count == 0) {
                    selectOrderSplitDialog.show(orderSplits, currentOrder);
                } else {
                    UIHelp.showToast(context, context.getResources().getString(R.string.assign_items_to_group));
                }
            }
        } else {
            UIHelp.showToast(context, context.getResources().getString(R.string.not_complete_order));
        }
    }

    private void showOpenItemWindow() {
        openItemWindow.show(context, findViewById(R.id.rl_root), handler,
                currentOrder);
    }

    private void dismissOpenItemWindow() {
        openItemWindow.dismiss();
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

//	private void showTables() {
//		getTables();
//		ObjectAnimator animator = ObjectAnimator.ofFloat(web_tables, "y",
//				web_tables.getY(), 0).setDuration(300);
//		animator.start();
//		isShowTables = true;
//		isTableFirstShow = false;
//	}

//	private void closeTables() {
//		ObjectAnimator animator = ObjectAnimator.ofFloat(web_tables, "y",
//				web_tables.getY(), web_tables.getY() + web_tables.getHeight())
//				.setDuration(300);
//		animator.start();
//		isShowTables = false;
//	}

//	private void dismissTables() {
//		ObjectAnimator animator = ObjectAnimator.ofFloat(web_tables, "y",
//				web_tables.getY(), web_tables.getY() + web_tables.getHeight())
//				.setDuration(300);
//		animator.start();
//		isShowTables = false;
//	}

    private void setData() {
        initOrder(currentTable);
        orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
        mainPageMenuView.setParam(currentOrder, handler);
        if (App.instance.getSystemSettings().isTopMaskingIsUser()) {
            view_top.setVisibility(View.VISIBLE);
        } else {
            view_top.setVisibility(View.INVISIBLE);
        }
        view_top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return App.instance.getSystemSettings().isTopMaskingIsUser();
            }
        });
//		mainPageMenuView.closeModifiers();
        orderView.setParam(this, currentOrder, orderDetails, handler);
        operatePanel.setParams(this, currentOrder, orderDetails,
                handler);
        loadingDialog.dismiss();
        printerLoadingDialog.dismiss();
    }


    private void mergerOrderSetData() {
        // initOrder(currentTable);
        orderDetails = OrderDetailSQL.getOrderDetails(currentOrder.getId());
        mainPageMenuView.setParam(currentOrder, handler);
        orderView.setParam(this, currentOrder, orderDetails, handler);
        operatePanel.setParams(this, currentOrder, orderDetails,
                handler);
        loadingDialog.dismiss();
        printerLoadingDialog.dismiss();
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
        setData();
//        sendKOTTmpToKDS(orderDetail);
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

                KotItemDetail kotItemDetail = ObjectFactory
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

                kotItemDetail.setItemNum(orderDetail.getItemNum());
                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_TMP);

                KotItemDetailSQL.update(kotItemDetail);
                kotItemDetails.add(kotItemDetail);
                orderDetailIds.add(orderDetail.getId());
                ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                        .getOrderModifiers(order, orderDetail);

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

                KotSummarySQL.update(kotSummary);

                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("orderId", currentOrder.getId());
                orderMap.put("orderDetailIds", orderDetailIds);
                App.instance.getKdsJobManager().sendKOTTmpToKDS(
                        kotSummary, kotItemDetails,
                        kotItemModifiers, ParamConst.JOB_TMP_KOT,
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

    @Override
    protected void onDestroy() {
        if (observable != null) {
            RxBus.getInstance().unregister(RxBus.RX_MSG_1, observable);
        }
        if (observable1 != null) {
            RxBus.getInstance().unregister("open_drawer", observable1);
        }
//		App.instance.unBindPushWebSocketService();
        XMPP.getInstance().setCanCheckAppOrder(false);
        super.onDestroy();
    }

    public Handler mmHandler = new Handler() {
        public void handleMessage(Message msg) {

        }

        ;
    };

    public void httpRequestAction(int action, Object obj) {
        switch (action) {
            case VIEW_EVENT_SET_DATA:
                int orderId = (Integer) obj;
                if (currentOrder != null && orderId == currentOrder.getId()) {
                    handler.sendMessage(handler.obtainMessage(action));
                }
                break;
            case REFRESH_TABLES_STATUS:
                if (isShowTables) {
                    TableInfo tables = (TableInfo) obj;
                    handler.sendMessage(handler.obtainMessage(
                            REFRESH_TABLES_STATUS, tables));
                }
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
            default:
                break;
        }
    }

    ;

    public void kotPrintStatus(int action, Object obj) {
        switch (action) {
            case KOT_PRINT_FAILED:
                handler.sendMessage(handler.obtainMessage(action));
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

    ;

    private void initAppOrder(final TableInfo tables) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                OrderSQL.deleteOrder(currentOrder);
                Order order = ObjectFactory.getInstance().getOrder(
                        ParamConst.ORDER_ORIGIN_POS, App.instance.getSubPosBeanId(), tables,
                        App.instance.getRevenueCenter(), App.instance.getUser(),
                        App.instance.getSessionStatus(),
                        App.instance.getBusinessDate(),
                        App.instance.getIndexOfRevenueCenter(),
                        ParamConst.ORDER_STATUS_OPEN_IN_POS,
                        App.instance.getLocalRestaurantConfig()
                                .getIncludedTax().getTax(),"");
                List<TempOrderDetail> tempOrderDetails = TempOrderDetailSQL.getTempOrderDetailByAppOrderId(appOrderId);
                for (TempOrderDetail tempOrderDetail : tempOrderDetails) {
                    ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(tempOrderDetail.getItemId(),tempOrderDetail.getItemName());
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

    private void TempOrderISPaied() {
        Payment payment = ObjectFactory.getInstance().getPayment(currentOrder, ObjectFactory.getInstance().getOrderBill(currentOrder, App.instance.getRevenueCenter()));
        ObjectFactory.getInstance().getPaymentSettlement(payment, ParamConst.SETTLEMENT_TYPE_THIRDPARTY, currentOrder.getTotal());
        currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_FINISHED);
        OrderSQL.update(currentOrder);

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("orderId", String.valueOf(currentOrder.getId()));
        map.put("paymentId", String.valueOf(payment.getId().intValue()));
        handler.sendMessage(handler.obtainMessage(VIEW_EVENT_CLOSE_BILL, map));
    }

    public void openCustomNoteView() {
        mainPageMenuView.openCustomNoteView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(settingView)) {
            mDrawerLayout.closeDrawer(Gravity.END);
        }
        mainPageMenuView.setParent(context);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setData();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NetWorkOrderActivity.CHECK_REQUEST_CODE && resultCode == NetWorkOrderActivity.CHECK_REQUEST_CODE) {
            activityRequestCode = requestCode;
            appOrderId = data.getIntExtra("appOrderId", 0);
            if (appOrderId == 0)
                return;
            if (orderDetails != null && orderDetails.size() != 0) {
                DialogFactory.showOneButtonCompelDialog(context, getString(R.string.warning), getString(R.string.close_the_last_order), null);
            } else {
                loadingDialog.show();
                initAppOrder(currentTable);
            }
        } else if (requestCode == MainPage.CHECK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("qrCode", data.getStringExtra("qrCode"));
                map.put("revenueId", data.getIntExtra("revenueId", 0));
                map.put("consumeAmount", data.getStringExtra("consumeAmount"));
                map.put("operateType", data.getIntExtra("operateType", -1));
                SyncCentre.getInstance().updateStoredCardValue(context, map, handler);
                loadingDialog.show();
            }
        } else if (requestCode == KioskHoldActivity.CHECK_REQUEST_CODE && resultCode == KioskHoldActivity.CHECK_RESULT_CODE) {
            Map<String, Object> map = (Map<String, Object>) data.getExtras().get("map");
            handler.sendMessage(handler.obtainMessage(
                    MainPage.VIEW_EVENT_CLOSE_BILL, map));
        } else if (requestCode == 0) {
            if (resultCode == 10000) {
                Log.v(TAG, "system返回:------- ");
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

    public void tryToCloseSession() {
        boolean canClose;
        List<OrderDetail> orderDetailsUnIncludeVoid = OrderDetailSQL
                .getOrderDetails(currentOrder.getId());
        if (!orderDetailsUnIncludeVoid.isEmpty()) {
            canClose = false;
        } else {
            canClose = true;
        }
        if (canClose) {
            DialogFactory.commonTwoBtnInputDialog(context, false, context.getString(R.string.start_drawer), context.getString(R.string.enter_amount_of_cash_in_drawer), context.getString(R.string.cancel), context.getString(R.string.done).toUpperCase(),
                    new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendXReportToMainPos("0.00");
                        }
                    },
                    new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText editText = (EditText) view;
                            String actual = editText.getText().toString();
                            sendXReportToMainPos(actual);
                        }
                    });
        } else {
            DialogFactory.showOneButtonCompelDialog(context, context.getResources().getString(R.string.warning),
                    context.getResources().getString(R.string.bill_not_closed), null);
        }
    }


    // 只在副Pos中调用
    private void sendXReportToMainPos(final String actualAmount) {
        printerLoadingDialog.setTitle(getString(R.string.printing_x_report));
        printerLoadingDialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                PrinterDevice cashierPrinter = App.instance.getCahierPrinter();
                if (cashierPrinter == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                                printerLoadingDialog.dismiss();
                            }
                            UIHelp.showToast(
                                    context, context.getResources().getString(R.string.setting_printer));
                        }
                    });
                    return;
                }
                SessionStatus sessionStatus = App.instance.getSessionStatus();
                long businessDate = App.instance.getBusinessDate();
                GeneralSQL.deleteKioskHoldOrderInfoBySession(sessionStatus, App.instance.getBusinessDate());
                Map<String, Object> map = new HashMap<String, Object>();
                // day sales report
                ReportDaySales reportDaySales = ReportObjectFactory.getInstance().loadXReportDaySales(businessDate, sessionStatus, actualAmount);
                if (reportDaySales == null) {
                    SubPosBean subPosBean = App.instance.getSubPosBean();
                    SubPosSyncCentre.getInstance().closeSession(context, subPosBean, new CallBack() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                                        printerLoadingDialog.dismiss();
                                    }
                                    dismissLoadingDialog();
                                    Store.remove(context, Store.SESSION_STATUS);
                                    App.instance.setSessionStatus(null);
                                    if (orderDetails.isEmpty()) {
                                        OrderSQL.deleteOrder(currentOrder);
                                    }
                                    MainPageKiosk.this.finish();
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                                printerLoadingDialog.dismiss();
                            }
                        }
                    });
                    return;
                }
                String reportType = CommonUtil.getReportType(context, sessionStatus.getSession_status());
                String bizDate = TimeUtil.getPrintingDate(businessDate);
                ArrayList<ItemCategory> itemCategorys = ItemCategorySQL
                        .getAllItemCategory();
                ArrayList<ItemMainCategory> itemMainCategorys = ItemMainCategorySQL
                        .getAllItemMainCategory();
                List<Modifier> modifiers = ModifierSQL.getModifierCategorys(App.instance.getRevenueCenter().getRestaurantId().intValue());
                PrinterTitle title = ObjectFactory.getInstance()
                        .getPrinterTitleForReport(
                                App.instance.getRevenueCenter().getId(),
                                "X" + reportDaySales.getReportNoStr(),
                                App.instance.getUser().getFirstName()
                                        + App.instance.getUser().getLastName(), null, bizDate,App.instance.getSystemSettings().getTrainType());

                // Open Cash drawer
                App.instance.kickOutCashDrawer(cashierPrinter);
                // tax report
                ArrayList<ReportDayTax> reportDayTaxs = ReportObjectFactory.getInstance().loadXReportDayTax(reportDaySales, businessDate,
                        sessionStatus);
                //paymentReport
                List<ReportDayPayment> reportDayPayments = ReportObjectFactory.getInstance().loadXReportDayPayment(reportDaySales, businessDate, sessionStatus);
                // sales report
                App.instance.remotePrintDaySalesReport(reportType, cashierPrinter,
                        title, reportDaySales, reportDayTaxs, reportDayPayments, ReportObjectFactory.getInstance().loadXReportUserOpenDrawerbySessionStatus(businessDate, sessionStatus), null);
                // plu item reprot
                ArrayList<ReportPluDayItem> reportPluDayItems = ReportObjectFactory.getInstance().loadXReportPluDayItem(businessDate, sessionStatus);
                Map<String, Object> modifierInfoMap = ReportObjectFactory.getInstance().loadXReportPluDayModifierInfo(businessDate, sessionStatus);
                ArrayList<ReportPluDayModifier> reportPluDayModifiers = (ArrayList<ReportPluDayModifier>) modifierInfoMap.get("reportPluDayModifiers");
                if (App.instance.getSystemSettings().isPrintPluItem())
                    // detail analysis
                    App.instance.remotePrintDetailAnalysisReport(reportType,
                            cashierPrinter, title, null, reportPluDayItems,
                            reportPluDayModifiers, null, itemMainCategorys, itemCategorys);
                if (reportPluDayModifiers != null && reportPluDayModifiers.size() > 0) {
                    if (App.instance.getSystemSettings().isPrintPluModifier())
                        //modifier report
                        App.instance.remotePrintModifierDetailAnalysisReport(reportType,
                                cashierPrinter, title, reportPluDayModifiers, modifiers);
                }
                ArrayList<ReportHourly> reportHourlys = ReportObjectFactory.getInstance().loadXReportHourlys(businessDate, sessionStatus);
                if (App.instance.getSystemSettings().isPrintPluCategory())
                    App.instance.remotePrintSummaryAnalysisReport(reportType,
                            cashierPrinter, title, reportPluDayItems,
                            reportPluDayModifiers, itemMainCategorys, itemCategorys);
                if (App.instance.getSystemSettings().isPrintHourlyPayment())
                    // hourly sales
                    App.instance.remotePrintHourlyReport(reportType, cashierPrinter, title,
                            reportHourlys);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                            printerLoadingDialog.dismiss();
                        }
                        loadingDialog.setTitle(getString(R.string.send_to_pos));
                        loadingDialog.show();
                    }
                });
                ArrayList<ReportPluDayComboModifier> reportPluDayComboModifiers = (ArrayList<ReportPluDayComboModifier>) modifierInfoMap.get("reportPluDayComboModifiers");
                List<UserOpenDrawerRecord> userOpenDrawerRecords = UserOpenDrawerRecordSQL.getAllUserOpenDrawerRecord(sessionStatus.getSession_status(), businessDate);
                map.put("reportDaySales", reportDaySales);
                map.put("reportDayTaxs", reportDayTaxs);
                map.put("reportDayPayments", reportDayPayments);
                map.put("reportPluDayItems", reportPluDayItems);
                map.put("reportPluDayModifiers", reportPluDayModifiers);
                map.put("reportHourlys", reportHourlys);
                map.put("reportPluDayComboModifiers", reportPluDayComboModifiers);
                map.put("sessionStatus", sessionStatus);
                map.put("userOpenDrawerRecords", userOpenDrawerRecords);
                //sync X-Report to cloud
                SubPosCloudSyncJobManager subPosCloudSyncJobManager = App.instance.getSubPosSyncJob();
                if (subPosCloudSyncJobManager != null) {
                    SyncMsg syncMsg = subPosCloudSyncJobManager.getSyncXReport(map,
                            App.instance.getRevenueCenter().getId(),
                            businessDate);
                    SubPosBean subPosBean = App.instance.getSubPosBean();
                    SubPosSyncCentre.getInstance().cloudSyncUploadReportInfo(context, syncMsg, subPosBean, new CallBack() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissLoadingDialog();
                                    Store.remove(context, Store.SESSION_STATUS);
                                    App.instance.setSessionStatus(null);
                                    if (orderDetails.isEmpty()) {
                                        OrderSQL.deleteOrder(currentOrder);
                                    }
                                    MainPageKiosk.this.finish();
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            if (printerLoadingDialog != null && printerLoadingDialog.isShowing()) {
                                printerLoadingDialog.dismiss();
                            }
                        }

                    });

                }


            }
        }).start();
    }

    /**
     * 到新加坡做测试

     private StringBuffer inPutBarCode = new StringBuffer("");

     @Override public boolean dispatchKeyEvent(KeyEvent event) {
     boolean hasInputDevice = false;
     int[] devicesIds = InputDevice.getDeviceIds();
     if(devicesIds != null && devicesIds.length > 0){
     for(int id : devicesIds){
     String name = InputDevice.getDevice(id).getName().trim();
     if(name.toUpperCase().contains("BARCODE")){
     hasInputDevice = true;
     break;
     }
     }
     }
     if(hasInputDevice) {
     int keyCode = event.getKeyCode();
     if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
     String barCode = inPutBarCode.toString();
     if (TextUtils.isEmpty(barCode)) {
     UIHelp.showToast(this, "Barcode cannot be empty");
     return false;
     }
     inPutBarCode = new StringBuffer("");
     ItemDetail itemDetail = CoreData.getInstance().getItemDetailByBarCode(barCode);
     OrderDetail orderDetail = null;
     SureDialog sureDialog = new SureDialog(this);
     if (itemDetail != null) {
     orderDetail = ObjectFactory.getInstance()
     .getOrderDetail(currentOrder, itemDetail, 0);
     }
     if (orderDetail == null) {
     sureDialog.show(false);
     return false;
     }
     Message msg = handler.obtainMessage();
     msg.what = MainPage.VIEW_EVENT_ADD_ORDER_DETAIL;
     msg.obj = orderDetail;
     handler.sendMessage(msg);
     sureDialog.show(true);
     return true;
     }else{
     if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
     inPutBarCode.append(event.getKeyCode() - KeyEvent.KEYCODE_0);
     return true;
     }
     return false;
     }
     }else{
     return super.dispatchKeyEvent(event);
     }

     }
     */
}
