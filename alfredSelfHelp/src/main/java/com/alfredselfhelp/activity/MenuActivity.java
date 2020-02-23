package com.alfredselfhelp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredselfhelp.R;
import com.alfredselfhelp.adapter.CartDetailAdapter;
import com.alfredselfhelp.adapter.ClassAdapter;
import com.alfredselfhelp.adapter.MainCategoryAdapter;
import com.alfredselfhelp.adapter.MenuDetailAdapter;
import com.alfredselfhelp.adapter.RvListener;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.global.CCCentre;
import com.alfredselfhelp.global.KpmDialogFactory;
import com.alfredselfhelp.global.RfidApiCentre;
import com.alfredselfhelp.global.SyncCentre;
import com.alfredselfhelp.javabean.ItemDetailDto;
import com.alfredselfhelp.javabean.NurTagDto;
import com.alfredselfhelp.popuwindow.SetItemCountWindow;
import com.alfredselfhelp.utils.CheckListener;
import com.alfredselfhelp.utils.ItemHeaderDecoration;
import com.alfredselfhelp.utils.KpmTextTypeFace;
import com.alfredselfhelp.utils.OrderDetailRFIDHelp;
import com.alfredselfhelp.utils.SelfOrderHelper;
import com.alfredselfhelp.utils.UIHelp;
import com.alfredselfhelp.view.CountView;
import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurRespInventory;
import com.nordicid.nurapi.NurTag;
import com.nordicid.nurapi.NurTagStorage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MenuActivity extends BaseActivity implements CheckListener {
    public static final int CC_TYPE_CC = 1;
    public static final int CC_TYPE_EZ = 2;
    public static final int VIEW_EVENT_MODIFY_ITEM_COUNT = 1;
    public static final int VIEW_EVENT_MODIFIER_COUNT = 8;
    public static final int MODIFY_ITEM_COUNT = 2;
    public static final int VIEW_ORDER_DETAIL_MODIFY_ITEM_COUNT = 3;
    public static final int VIEW_COMMIT_ORDER_SUCCEED = 1111;
    public static final int VIEW_COMMIT_ORDER_FAILED = -1111;

    public static final int VIEW_CC_CONNECT_SUCCEED = 1112;
    public static final int VIEW_CC_CONNECT_FAILED = -1112;

    public static final int VIEW_CC_PAYMENT_HAS_CARDNUM_SUCCEED = 1113;
    public static final int VIEW_CC_PAYMENT_NO_CARDNUM_SUCCEED = 1114;
    public static final int VIEW_PAYMENT_STORED_CARDNUM_SUCCEED = 1115;
    public static final int VIEW_PAYMENT_STORED_CARDNUM_FAILED = -1115;
    public static final int VIEW_CHECK_SOTCK_NUM_SUCCEED = 1116;
    public static final int VIEW_CHECK_SOTCK_NUM_FAILED = -1116;
    public static final int VIEW_CC_PAYMENT_FAILED = -1113;
    public static final int SETTLEMENT_TYPE_STORED_CARD = 1004;// 储值卡
    private static final String CUSTOM_PAYMENT_VISA = "Visa Kiosk";
    private static final String CUSTOM_PAYMENT_MC = "MC Kiosk";
    private static final String CUSTOM_PAYMENT_EZ = "EZlink Kiosk";
    private static final String CUSTOM_PAYMENT_OTHER = "Kiosk";

    private RecyclerView re_main_category;
    private List<ItemMainCategory> itemMainCategories;
    private LinearLayoutManager mLinearLayoutManager;
    MainCategoryAdapter mainCategoryAdapter;
    private LinearLayout ll_grab, ll_menu_details, ll_video, ll_view_cart, ll_view_cart_list, ll_view_pay, ll_menu_title, ll_view_cart_bg;
    private RecyclerView re_menu_classify, re_menu_details, re_view_cart;
    private ClassAdapter mClassAdapter;
    private MenuDetailAdapter mDetailAdapter;
    ItemHeaderDecoration mDecoration;
    private TextView total, tv_cart_num, tv_total_price, tv_time, tv_view_cart, tv_dialog_ok;

    private ImageView img_view_cart;
    private RelativeLayout rl_cart_num;

    private RelativeLayout rl_cart_total;
    private LinearLayout li_menu, ll_view_order_card, ll_view_order_ez, ll_view_order_qc, ll_order_dialog;
    List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();

    List<ItemDetail> itemDetailNur = new ArrayList<ItemDetail>();

    List<ItemCategory> itemCategorys = new ArrayList<ItemCategory>();
    private CheckListener checkListener;

    private Boolean isMoved = false;
    private int targetPosition;
    private int mIndex;
    private GridLayoutManager mManager;
    private Boolean move = false;
    private Order nurOrder;
    SetItemCountWindow setItemCountWindow;
    CartDetailAdapter cartAdater;
    //    private OrderSelfDialog selfDialog;
    List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
    private Timer timer = new Timer();
    private boolean isUpdating = false;
    private KpmTextTypeFace textTypeFace;
    private VideoView mVideoView;
    Dialog yesDialog;
    private Dialog paymentDialog, stockDialog;
    private int CCPaymentType = CC_TYPE_CC;
    private OrderSelfDialog dialog;

    //    Dialog fdialog;
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_menu);
        init();
        startTimer(3000);
        App.instance.startADKpm();

    }

    private void initVideo() {
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/aaa"));
        mVideoView.start();
        //监听视频播放完的代码
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
    }

    private void startTimer(long delay) {
        try {
            if (timer == null) {
                timer = new Timer();
            }
            timer.schedule(new MyTimerTask(), delay);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }

    private void cancelTimer() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void httpRequestAction(int action, Object obj) {

        handler.sendMessage(handler.obtainMessage(action, obj));
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeSlot();
        initVideo();

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
//                case VIEW_EVENT_MODIFY_ITEM_COUNT: {
//                    Log.d("444444444444--->", "4444444444444");
//                    Map<String, Object> map = (Map<String, Object>) msg.obj;
//                    updateOrderDetail((ItemDetail) map.get("itemDetail"),
//                            (Integer) map.get("count"));
//                    refreshTotal();
//                    refreshList();
//
//                    boolean isadd = (boolean) map.get("isAdd");
//
//                }
//                break;

                case MODIFY_ITEM_COUNT: {

                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
                    RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());

                    if (remainingStock != null) {
                        int qty = remainingStock.getQty() - remainingStock.getMinQty();
                        OrderDetail orderDetail = getItemOrderDetail(itemDetail);
                        if ((orderDetail != null && qty > orderDetail.getItemNum())
                                || (orderDetail == null && qty > 0)) {
                            updateitemOrderDetail(itemDetail,
                                    1);
                        }
                    } else {
                        updateitemOrderDetail(itemDetail,
                                1);
                    }
                    refreshTotal();
                    refreshList();

                }
                break;
                case App.HANDLER_REFRESH_TIME:
                    Intent intent = new Intent();
                    intent.setClass(MenuActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                    break;


                case VIEW_ORDER_DETAIL_MODIFY_ITEM_COUNT: {

                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    int count = (int) map.get("count");
                    OrderDetail orderDetail = (OrderDetail) map.get("orderDetail");
//                    if (count == 0) {
//
//                    } else {
                    ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(orderDetail.getItemId(),orderDetail.getItemName());
                    RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());

                    if (remainingStock != null) {
                        int qty = remainingStock.getQty() - remainingStock.getMinQty();
                        if (qty < count) {
                            UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.out_of_stock));
                            cartAdater.notifyDataSetChanged();
                            return;
                        }
                    }
                    updateCartOrderDetail(orderDetail,
                            count);
//                    }
                    tv_total_price.setText("S" + App.instance.getCurrencySymbol() + BH.formatMoney(nurOrder.getTotal()));
                    tv_total_price.setTextColor(context.getResources().getColor(R.color.green));
                }
                break;

                case VIEW_COMMIT_ORDER_SUCCEED: {
                    PaymentSettlement paymentSettlement = (PaymentSettlement) msg.obj;
                    try {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    rl_cart_num.setVisibility(View.GONE);
                    if (paymentSettlement != null) {
                        UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.succeed));
                    } else {
                        UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.please_go_to_counter));
                    }
                    MenuActivity.this.finish();
                }
                break;
                case VIEW_COMMIT_ORDER_FAILED:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
//                    UIHelp.showToast(context, "Please try again !");
                    break;
                case VIEW_CC_CONNECT_SUCCEED:
                    if (CCPaymentType == CC_TYPE_CC) {
                        CCCentre.getInstance().startPay(new DecimalFormat("0", new DecimalFormatSymbols(Locale.US)).format(BH.mul(BH.getBD(nurOrder.getTotal()), BH.getBD("100"), false)), 90 * 1000);
                    } else {
                        CCCentre.getInstance().startEZLinkPay(new DecimalFormat("0", new DecimalFormatSymbols(Locale.US)).format(BH.mul(BH.getBD(nurOrder.getTotal()), BH.getBD("100"), false)), 20 * 1000);
                    }
                    break;
                case VIEW_CC_CONNECT_FAILED:
                    if (paymentDialog != null && paymentDialog.isShowing()) {
                        paymentDialog.dismiss();
                    }
                    paymentDialog = KpmDialogFactory.kpmTipsDialog(context, getString(R.string.connection_down),
                            getString(R.string.cash_payment_at_counter),
                            R.drawable.icon_tip_cq, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }, false);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (paymentDialog != null && paymentDialog.isShowing()) {
                                paymentDialog.dismiss();
                            }
                            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(nurOrder, App.instance.getRevenueCenter());
                            commitOrder(orderBill, null, null, null);
                        }
                    }, 3000);
                    break;
                case VIEW_CC_PAYMENT_FAILED:
                    if (paymentDialog != null && paymentDialog.isShowing()) {
                        paymentDialog.dismiss();
                    }
                    String title = context.getString(R.string.credit_card_invalid);
                    String content = context.getString(R.string.cash_payment_at_counter);
                    if (CCPaymentType != CC_TYPE_CC) {
                        title = "EZ-Link Invalid";
                        content = context.getString(R.string.cash_payment_at_counter);
                    }
                    paymentDialog = KpmDialogFactory.kpmTipsDialog(context, title, content
                            ,
                            R.drawable.icon_tip_card, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }, false);
                    CCCentre.getInstance().disconnect();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (paymentDialog != null && paymentDialog.isShowing()) {
                                paymentDialog.dismiss();
                            }
                            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(nurOrder, App.instance.getRevenueCenter());
                            commitOrder(orderBill, null, null, null);

                        }
                    }, 5000);

//                    dismissLoadingDialog();
                    break;
                case VIEW_CC_PAYMENT_HAS_CARDNUM_SUCCEED: {
                    Map<String, Object> cardInfoMap = (Map<String, Object>) msg.obj;
                    final int paymentType = (int) cardInfoMap.get("paymentType");
                    final String cardNum = (String) cardInfoMap.get("cardNum");
                    if (paymentDialog != null && paymentDialog.isShowing()) {
                        paymentDialog.dismiss();
                    }
                    paymentDialog = KpmDialogFactory.kpmCompleteDialog(context, context.getString(R.string.thank_you),
                            context.getString(R.string.take_your_receipt), R.drawable.icon_paid, false);
                    CCCentre.getInstance().disconnect();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (paymentDialog != null && paymentDialog.isShowing()) {
                                paymentDialog.dismiss();
                            }

                            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(nurOrder, App.instance.getRevenueCenter());
                            Payment payment = ObjectFactory.getInstance().getPayment(nurOrder, orderBill);
                            PaymentSettlement paymentSettlement;
                            int paymentTypeId = paymentType;
                            if (paymentTypeId == ParamConst.SETTLEMENT_TYPE_VISA) {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_VISA);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            } else if (paymentTypeId == ParamConst.SETTLEMENT_TYPE_MASTERCARD) {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_MC);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            } else if (paymentTypeId == ParamConst.SETTLEMENT_TYPE_EZLINK) {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_EZ);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            } else {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_OTHER);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            }
                            paymentSettlement = ObjectFactory.getInstance().getPaymentSettlement(payment, paymentTypeId, nurOrder.getTotal());
                            commitOrder(orderBill, payment, paymentSettlement, cardNum);
                        }
                    }, 5000);
                }
                break;
                case VIEW_CC_PAYMENT_NO_CARDNUM_SUCCEED: {
                    final int paymentType = (int) msg.obj;
                    if (paymentDialog != null && paymentDialog.isShowing()) {
                        paymentDialog.dismiss();
                    }
                    paymentDialog = KpmDialogFactory.kpmCompleteDialog(context, context.getString(R.string.thank_you),
                            context.getString(R.string.take_your_receipt), R.drawable.icon_paid, false);
                    CCCentre.getInstance().disconnect();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (paymentDialog != null && paymentDialog.isShowing()) {
                                paymentDialog.dismiss();
                            }
                            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(nurOrder, App.instance.getRevenueCenter());
                            Payment payment = ObjectFactory.getInstance().getPayment(nurOrder, orderBill);
                            PaymentSettlement paymentSettlement;
                            int paymentTypeId = paymentType;
                            if (paymentTypeId == ParamConst.SETTLEMENT_TYPE_VISA) {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_VISA);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            } else if (paymentTypeId == ParamConst.SETTLEMENT_TYPE_MASTERCARD) {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_MC);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            } else if (paymentTypeId == ParamConst.SETTLEMENT_TYPE_EZLINK) {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_EZ);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            } else {
                                PaymentMethod paymentMethod = PaymentMethodSQL.getPaymentMethodByNameOt(CUSTOM_PAYMENT_OTHER);
                                if (paymentMethod != null) {
                                    paymentTypeId = paymentMethod.getPaymentTypeId().intValue();
                                }
                            }
                            paymentSettlement = ObjectFactory.getInstance().getPaymentSettlement(payment, paymentTypeId, nurOrder.getTotal());
                            commitOrder(orderBill, payment, paymentSettlement, "");
                        }
                    }, 5000);
                }
                break;

                case VIEW_PAYMENT_STORED_CARDNUM_SUCCEED: {

                    if (paymentDialog != null && paymentDialog.isShowing()) {
                        paymentDialog.dismiss();
                    }
                    paymentDialog = KpmDialogFactory.kpmCompleteDialog(context, context.getString(R.string.thank_you),
                            context.getString(R.string.take_your_receipt), R.drawable.icon_paid, false);
                    CCCentre.getInstance().disconnect();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (paymentDialog != null && paymentDialog.isShowing()) {
                                paymentDialog.dismiss();
                            }
                            OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(nurOrder, App.instance.getRevenueCenter());
                            Payment payment = ObjectFactory.getInstance().getPayment(nurOrder, orderBill);
                            PaymentSettlement paymentSettlement = ObjectFactory.getInstance().getPaymentSettlement(payment, SETTLEMENT_TYPE_STORED_CARD, nurOrder.getTotal());
                            commitOrder(orderBill, payment, paymentSettlement, "");
                        }
                    }, 5000);
                }
                break;
                case VIEW_PAYMENT_STORED_CARDNUM_FAILED:

                    break;
                case VIEW_CHECK_SOTCK_NUM_SUCCEED:

                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
//                    String ip = Store.getString(App.instance, Store.KPM_CC_IP);
//                    paymentAction(ip);

                    break;
                case VIEW_CHECK_SOTCK_NUM_FAILED:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }


                    break;

                case MainActivity.REMAINING_STOCK_SUCCESS:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    List<OrderDetail> removeOrderDetails = new ArrayList<>();
                    boolean isStock = false;
                    for (int i = 0; i < orderDetails.size(); i++) {
                        OrderDetail orderDetail = orderDetails.get(i);
                        ItemDetail itemDetail = ItemDetailSQL.getItemDetailById(orderDetail.getItemId(),orderDetail.getItemName());
                        RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                        if (remainingStock != null) {
                            int qty = remainingStock.getQty() - remainingStock.getMinQty();
                            int num = orderDetail.getItemNum();
                            if (qty <= 0) {
                                isStock = true;
                                removeOrderDetails.add(orderDetail);
                            } else if (num > qty) {
                                isStock = true;
                                updateCartOrderDetail(orderDetail,
                                        qty);
                            }
                        }
                    }
                    for (OrderDetail orderDetail : removeOrderDetails) {
                        updateCartOrderDetail(orderDetail,
                                0);
                    }
                    tv_total_price.setText("S" + App.instance.getCurrencySymbol() + BH.formatMoney(nurOrder.getTotal()));
                    tv_total_price.setTextColor(context.getResources().getColor(R.color.green));
                    if (isStock) {
                        stockDialog = KpmDialogFactory.kpmOutStockDialog(context, context.getString(R.string.out_of_stock),
                                context.getString(R.string.item_not_available_reorder), R.drawable.out_of_stock,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startTimer(2000);
                                    }
                                }, false);

                    } else {
                        String ip = Store.getString(App.instance, Store.KPM_CC_IP);
                        paymentAction(ip);
                    }
                    break;
                case MainActivity.REMAINING_STOCK_FAILURE:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;
            }

        }
    };


    private void commitOrder(OrderBill orderBill, Payment payment, PaymentSettlement paymentSettlement, String cardNum) {
        loadingDialog.setTitle(getString(R.string.order));
        loadingDialog.show();
        SyncCentre.getInstance().commitOrder(context, nurOrder, orderBill, orderDetails, payment, paymentSettlement, handler, cardNum);
    }

    private void init() {
        initTextTypeFace();
//        CCPaymentType = Store.getInt(context, Store.KPMG_PAYMENT_TYPE, 1);
        loadingDialog = new LoadingDialog(MenuActivity.this);
//        selfDialog = new OrderSelfDialog(MenuActivity.this);
        ll_grab = (LinearLayout) findViewById(R.id.ll_grab);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        ll_menu_details = (LinearLayout) findViewById(R.id.ll_menu_details);
        re_menu_classify = (RecyclerView) findViewById(R.id.re_menu_classify);
        re_menu_details = (RecyclerView) findViewById(R.id.re_menu_details);
        re_main_category = (RecyclerView) findViewById(R.id.re_main_category);
        ll_view_cart = (LinearLayout) findViewById(R.id.ll_view_cart);
        ll_view_cart_list = (LinearLayout) findViewById(R.id.ll_view_cart_list);
        re_view_cart = (RecyclerView) findViewById(R.id.re_view_cart);
        ll_view_pay = (LinearLayout) findViewById(R.id.ll_view_pay);
        tv_cart_num = (TextView) findViewById(R.id.tv_cart_num);
        rl_cart_num = (RelativeLayout) findViewById(R.id.rl_cart_num);
        tv_total_price = (TextView) findViewById(R.id.tv_cart_total);
        rl_cart_total = (RelativeLayout) findViewById(R.id.rl_cart_total);
        li_menu = (LinearLayout) findViewById(R.id.li_menu);
        ll_menu_title = (LinearLayout) findViewById(R.id.ll_menu_title);
        ll_view_cart_bg = (LinearLayout) findViewById(R.id.ll_view_cart_bg);
        tv_view_cart = (TextView) findViewById(R.id.tv_view_cart);
        img_view_cart = (ImageView) findViewById(R.id.img_view_cart);
        mVideoView = (VideoView) findViewById(R.id.video_menu);
        tv_time = (TextView) findViewById(R.id.tv_time);
        ll_view_order_card = (LinearLayout) findViewById(R.id.ll_view_order_card);
        ll_view_order_ez = (LinearLayout) findViewById(R.id.ll_view_order_ez);
        tv_dialog_ok = (TextView) findViewById(R.id.tv_dialog_ok);
        ll_order_dialog = (LinearLayout) findViewById(R.id.ll_order_dialog);
        ll_view_order_card.setVisibility(View.VISIBLE);
        ll_view_order_ez.setVisibility(View.VISIBLE);

        tv_dialog_ok.setOnClickListener(this);
        ll_view_order_card.setOnClickListener(this);
        ll_view_order_ez.setOnClickListener(this);
        ll_view_pay.setOnClickListener(this);
        total = (TextView) findViewById(R.id.tv_cart_total);
        ll_video.setVisibility(View.VISIBLE);
//        ll_menu_details.setVisibility(View.VISIBLE);
//        ll_video.setVisibility(View.GONE);
        ll_grab.setOnClickListener(this);
        ll_view_cart.setOnClickListener(this);
        itemMainCategories = CoreData.getInstance().getItemMainCategoriesForSelp();
        menuDetail();
        re_main_category = (RecyclerView) findViewById(R.id.re_main_category);
        mLinearLayoutManager = new LinearLayoutManager(context);
        re_main_category.setLayoutManager(mLinearLayoutManager);

        mLinearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //  re_main_category.setLayoutManager(mLinearLayoutManager);
//        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
//        re_main_category.addItemDecoration(decoration);
        mainCategoryAdapter = new MainCategoryAdapter(context, itemMainCategories, new RvListener() {

            @Override
            public void onItemClick(int id, int position) {
//                RfidApiCentre.getInstance().stopRFIDScan();
                ItemMainCategory itemMainCategory = itemMainCategories.get(position);
                mainCategoryAdapter.setCheckedPosition(position);
                li_menu.setBackgroundResource(R.drawable.bg_mod);
                ll_grab.setBackgroundResource(R.drawable.btn_main_g);
                ll_menu_details.setVisibility(View.VISIBLE);
                ll_video.setVisibility(View.GONE);
                ll_view_cart_list.setVisibility(View.GONE);
                ll_view_pay.setVisibility(View.GONE);
                ll_view_cart.setVisibility(View.VISIBLE);
                ll_menu_title.setVisibility(View.GONE);
                videoPause();
                getItemCategory(itemMainCategory.getId());

                //     UIHelp.showToastTransparentForKPMG(App.instance, "Item(s) have been added!");
                //   getItemDetail(itemMainCategory.getMainCategoryName(), itemMainCategory.getId().intValue());


            }
        });
        re_main_category.setAdapter(mainCategoryAdapter);

        setItemCountWindow = new SetItemCountWindow(this, findViewById(R.id.li_menu),
                handler);
//        nurOrder = OrderSQL.getAllOrder().get(0);
        nurOrder = SelfOrderHelper.getInstance().getOrder(
                ParamConst.ORDER_ORIGIN_POS, 0,
                App.instance.getRevenueCenter(), App.instance.getUser(),
                App.instance.getSessionStatus(),
                App.instance.getBusinessDate(),
                App.instance.getIndexOfRevenueCenter(),
                ParamConst.ORDER_STATUS_OPEN_IN_POS,
                App.instance.getLocalRestaurantConfig()
                        .getIncludedTax().getTax(), 0);

        int WIDTH = (int) (ScreenSizeUtil.width - ScreenSizeUtil.dip2px((Activity) context, 265));
        ViewGroup.LayoutParams lp;
        lp = ll_menu_details.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = WIDTH / 4 * 5 - 5;
        ll_menu_details.setLayoutParams(lp);
//
//
//        ViewGroup.LayoutParams lpDe;
//        lpDe = re_menu_details.getLayoutParams();
//        lpDe.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        lpDe.height = WIDTH / 4 * 5;
//        re_menu_details.setLayoutParams(lpDe);
        refreshTotal();
        CCCentre.getInstance().init(handler);
        dialog = new OrderSelfDialog(MenuActivity.this);
        dialog.setNoOnclickListener("", new OrderSelfDialog.onNoOnclickListener() {

            @Override
            public void onNoClick() {
                if (dialog != null && dialog.isShowing()) {
                    if (ButtonClickTimer.canClick()) {
                        startTimer(3000);
                    }
                    dialog.dismiss();
                }

            }
        });

        dialog.setYesOnclickListener("", new OrderSelfDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                String ip = Store.getString(App.instance, Store.KPM_CC_IP);
                if (TextUtils.isEmpty(ip)) {
                    UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.contact_staff_for_ip));
                } else {
                    //TODO
                    loadingDialog.setTitle(getString(R.string.checking_stock));
//                  List<RemainingStockVo> remainingStockList=new  ArrayList<RemainingStockVo>();
//                  Map<String,Object>  numMap=new HashMap<>();
//                    for (int i = 0; i <orderDetails.size() ; i++) {
//                        OrderDetail orderDetail= orderDetails.get(i);
//                        RemainingStockVo remainingStock=new RemainingStockVo();
//                     remainingStock.setItemId(orderDetail.getItemId());
//                     remainingStock.setNum(orderDetail.getItemNum());
//                     remainingStockList.add(remainingStock);
//                    }
//                    numMap.put("remainingStockList",remainingStockList);
                    SyncCentre.getInstance().getRemainingStock(MenuActivity.this, handler);
//                    paymentAction(ip);
                }
            }
        });
//        viewCart(true);
//        VtintApiCentre.getInstance().initUsb();
    }

    @Override
    protected void onDestroy() {
        try {
            cancelTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        videoStop();
        super.onDestroy();
    }

    private void initRfid(List<NurTagDto> barCodes) {
        Map<Integer, ItemDetailDto> itemDetailNumMap = new HashMap<>();
        if (barCodes != null && barCodes.size() > 0) {
            for (int j = 0; j < barCodes.size(); j++) {
                NurTagDto nurTagDto = barCodes.get(j);

                ItemDetail itemDetail = CoreData.getInstance().getItemDetailByBarCodeForKPMG(nurTagDto.getBarCode());
                if (itemDetail != null) {
                    int itemDetailId = itemDetail.getId();
                    if (itemDetailNumMap.containsKey(itemDetailId)) {
                        ItemDetailDto itemDetailDto = itemDetailNumMap.get(itemDetailId);
                        itemDetailDto.setItemNum(itemDetailDto.getItemNum() + nurTagDto.getNum());
                    } else {
                        ItemDetailDto itemDetailDto = new ItemDetailDto();
                        itemDetailDto.setItemId(itemDetailId);
                        itemDetailDto.setItemNum(nurTagDto.getNum());
                        itemDetailDto.setItemName(itemDetail.getItemName());
                        itemDetailDto.setItemPrice(itemDetail.getPrice());
                        itemDetailNumMap.put(itemDetailId, itemDetailDto);
                    }
                }
            }
        }
        if (itemDetailNumMap.size() > 0) {
            final List<ItemDetailDto> itemDetailDtos = new ArrayList<>(itemDetailNumMap.values());
            if (itemDetailDtos != null) {
//                loadingDialog.setTitle(context.getString(R.string.loading));
//                loadingDialog.show();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                boolean showViewCart = false;
                if (orderDetails.size() == 0) {
                    showViewCart = true;
                }
                boolean showToast = false;
                for (ItemDetailDto itemDetailDto : itemDetailDtos) {
                    ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(itemDetailDto.getItemId(),itemDetailDto.getItemName());
                    OrderDetail selectedOrderDetail = null;
                    if (orderDetails != null && orderDetails.size() > 0) {
                        for (OrderDetail orderDetail : orderDetails) {
                            if (orderDetail.getItemId().intValue() == itemDetail.getId().intValue()) {
                                selectedOrderDetail = orderDetail;
                                break;
                            }
                        }
                    }
                    if (selectedOrderDetail != null) {
                        selectedOrderDetail.setItemNum(selectedOrderDetail.getItemNum().intValue() + 1);
                        SelfOrderHelper.getInstance().calculateOrderDetail(nurOrder, selectedOrderDetail);
                        SelfOrderHelper.getInstance().calculate(nurOrder, orderDetails);
                    } else {
                        OrderDetail orderDetail = SelfOrderHelper.getInstance()
                                .createOrderDetailForWaiter(nurOrder, itemDetail,
                                        0, App.instance.getUser());
                        orderDetail.setItemNum(itemDetailDto.getItemNum());
                        SelfOrderHelper.getInstance().addOrderDetailETCForWaiterFirstAdd(nurOrder, orderDetail, orderDetails);
                    }
                    showToast = true;
                }

                if (showToast && mVideoView.getVisibility() == View.VISIBLE && ll_view_cart.getVisibility() == View.VISIBLE) {
                    UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.item_added));
                }

                if (showToast && ll_view_cart_list.getVisibility() == View.VISIBLE
                        && yesDialog != null && yesDialog.isShowing()) {
                    yesDialog.dismiss();
                    App.instance.startADKpm();

                    ll_order_dialog.setVisibility(View.GONE);
                }
                refreshTotal();
                if (ll_view_cart_list != null && ll_view_cart_list.getVisibility() == View.VISIBLE) {
                    refreshViewCart();
                } else if (showViewCart && ll_video.getVisibility() == View.VISIBLE) {
                    ll_view_cart.performClick();
                }
                isUpdating = false;
            }
        }
    }

    private void removeRfid(final Map<String, Integer> map) {
        if (map != null && map.size() > 0) {
            if (orderDetails != null && orderDetails.size() > 0) {
                for (int i = orderDetails.size() - 1; i >= 0; i--) {
                    OrderDetail orderDetail = orderDetails.get(i);
                    if (!TextUtils.isEmpty(orderDetail.getBarCode())) {
                        String barCode = IntegerUtils.format20(orderDetail.getBarCode());
                        if (map.containsKey(barCode)) {
                            Integer num = map.get(barCode);
                            if (orderDetail.getItemNum() < num.intValue()) {
                                orderDetails.remove(i);
                                map.put(barCode, num.intValue() - orderDetail.getItemNum());
                            } else {
                                if (orderDetail.getItemNum() > num.intValue()) {
                                    orderDetail.setItemNum(orderDetail.getItemNum() - num.intValue());
                                    SelfOrderHelper.getInstance().calculateOrderDetail(nurOrder, orderDetail);
                                } else {
                                    orderDetails.remove(i);
                                }
                                map.remove(barCode);
                            }
                            SelfOrderHelper.getInstance().calculate(nurOrder, orderDetails);
                        }
                    }
                }
            }
            refreshTotal();
            if (ll_view_cart_list != null && ll_view_cart_list.getVisibility() == View.VISIBLE) {
                refreshViewCart();
            }
            if (orderDetails.size() == 0 && ll_video.getVisibility() != View.VISIBLE) {
                ll_grab.performClick();
            }
            isUpdating = false;
        }
    }


    private void updateCartOrderDetail(OrderDetail orderDetail, int count) {


        if (count <= 0) {// 删除
            orderDetails.remove(orderDetail);
            SelfOrderHelper.getInstance().calculate(nurOrder, orderDetails);

        } else {// 添加
            nurOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
            orderDetail.setItemNum(count);
            orderDetail.setUpdateTime(System.currentTimeMillis());
            SelfOrderHelper.getInstance().updateOrderDetailAndOrderForWaiter(nurOrder, orderDetails, orderDetail);
        }
        cartAdater.notifyDataSetChanged();
        if (orderDetails.size() == 0) {
            View view = re_main_category.getChildAt(0);//获取到第一个Item的View
            if (view != null) {
                view.performClick();
            }
        }
    }


    private OrderDetail getItemOrderDetail(ItemDetail itemDetail) {
        OrderDetail orderDetail = SelfOrderHelper.getInstance().getOrderDetailFromList(itemDetail, orderDetails);

//        if (orderDetail == null) {
//            orderDetail = SelfOrderHelper.getInstance()
//                    .createOrderDetailForWaiter(nurOrder, itemDetail,
//                            0, App.instance.getUser());
//        }

        return orderDetail;
    }

    private void updateitemOrderDetail(ItemDetail itemDetail, int count) {
        OrderDetail orderDetail = SelfOrderHelper.getInstance().getOrderDetailFromList(itemDetail, orderDetails);
        if (orderDetail != null) {
            count = count + orderDetail.getItemNum();

        } else {
            count = 1;
        }
        if (count == 0) {// 删除
            if (orderDetail != null) {
                orderDetails.remove(orderDetail);
                SelfOrderHelper.getInstance().calculate(nurOrder, orderDetails);
            }
        } else {// 添加
//			count = count - oldCount;
            nurOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
            if (orderDetail == null) {
                orderDetail = SelfOrderHelper.getInstance()
                        .createOrderDetailForWaiter(nurOrder, itemDetail,
                                0, App.instance.getUser());
                orderDetail.setItemNum(count);
                SelfOrderHelper.getInstance().addOrderDetailETCForWaiterFirstAdd(nurOrder, orderDetail, orderDetails);
            } else {
                orderDetail.setItemNum(count);
                orderDetail.setItemUrl(itemDetail.getImgUrl());
                orderDetail.setUpdateTime(System.currentTimeMillis());
                SelfOrderHelper.getInstance().updateOrderDetailAndOrderForWaiter(nurOrder, orderDetails, orderDetail);
            }
        }
    }


    private void menuDetail() {

        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        re_menu_classify.setLayoutManager(mLinearLayoutManager);
        mClassAdapter = new ClassAdapter(context, itemCategorys, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {
                if (itemCategorys != null && itemCategorys.size() > 0) {
                    ItemCategory itemCategory = itemCategorys.get(position);
                    // isMoved = true;
                    //   App.isleftMoved = true;
                    //   targetPosition = position;
                    setChecked(position, true, 0);
                    getItemDetailmod(itemCategory);
                }
            }
        });

        re_menu_classify.setAdapter(mClassAdapter);


        mManager = new GridLayoutManager(context, 3);

        re_menu_details.setLayoutManager(mManager);

//        re_menu_details.setLayoutManager(new GridLayoutManager(this,3){
//
//            @Override
//            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//                int count = state.getItemCount();
//                if (count > 0) {
//                    if(count>5){
//                        count =5;
//                    }
//                    int realHeight = 0;
//                    int realWidth = 0;
//                    for(int i = 0;i < count; i++){
//                        View view = recycler.getViewForPosition(0);
//                        if (view != null) {
//                            measureChild(view, widthSpec, heightSpec);
//                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//                            int measuredHeight = view.getMeasuredHeight();
//                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
//                            realHeight += measuredHeight;
//                        }
//                        setMeasuredDimension(realWidth, realHeight);
//                    }
//                } else {
//                    super.onMeasure(recycler, state, widthSpec, heightSpec);
//                }
//            }
//        });
        mDetailAdapter = new MenuDetailAdapter(context, itemDetails, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {
                if (!ButtonClickTimer.canClickShort()) {
                    return;
                }
                ItemDetail itemDetail = itemDetails.get(position);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("itemDetail", itemDetail);

                handler.sendMessage(handler.obtainMessage(
                        MODIFY_ITEM_COUNT, map));
            }
        });
        re_menu_details.setAdapter(mDetailAdapter);

    }


    private List<ItemCategory> getItemCategory(int id) {

        List<ItemCategory> itemCategorielist = new ArrayList<ItemCategory>();

        List<ItemCategory> itemCategorylist = ItemCategorySQL.getAllItemCategory();
        itemCategorys.clear();
        if (itemCategorylist != null || itemCategorielist.size() > 0) {


            for (int i = 0; i < itemCategorylist.size(); i++) {
                ItemCategory itemCategory = itemCategorylist.get(i);
                int cid;
                cid = itemCategorylist.get(i).getItemMainCategoryId();
                if (id == cid) {
                    itemCategorys.add(itemCategory);
                }
            }
        }
        mClassAdapter.setCheckedPosition(0);
        //  mClassAdapter.notifyDataSetChanged();
        if (itemCategorys != null && itemCategorys.size() > 0) {
            getItemDetailmod(itemCategorys.get(0));
        }


        return itemCategorielist;

    }

    private List<ItemDetail> getItemDetail(String mainCategoryName, int id
    ) {

        itemDetails.clear();

        mManager = new GridLayoutManager(context, 3);
        //通过isTitle的标志来判断是否是title
//        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return  1;
//            }
//        });1
        re_menu_details.setLayoutManager(mManager);
        mDetailAdapter = new MenuDetailAdapter(context, itemDetails, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {
                ItemDetail itemDetail = itemDetails.get(position);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("itemDetail", itemDetail);

                handler.sendMessage(handler.obtainMessage(
                        MODIFY_ITEM_COUNT, map));
            }
        });
        re_menu_details.setAdapter(mDetailAdapter);

        List<ItemDetail> itemDetaillist = new ArrayList<ItemDetail>();

        List<ItemDetail> itemDetailandCate = new ArrayList<ItemDetail>();
//
        List<ItemCategory> itemCategorylist = ItemCategorySQL.getAllItemCategory();
        int tag = 0;
        if (itemCategorylist != null || itemCategorylist.size() > 0) {
            for (int j = 0; j < itemCategorylist.size(); j++) {
                ItemCategory itemCategory = itemCategorylist.get(j);
                int cid;
                cid = itemCategorylist.get(j).getItemMainCategoryId();
                if (id == cid) {
//
                    itemDetaillist.clear();
                    //  itemDetaillist = CoreData.getInstance().getItemDetails(itemCategory);
                    itemDetaillist = CoreData.getInstance().getItemDetails(itemCategorylist.get(j));
                    // itemDetaillist  = ItemDetailSQL.getAllItemDetail();
                    if (itemDetaillist != null || itemDetaillist.size() > 0) {
                        for (int d = 0; d < itemDetaillist.size(); d++) {
                            itemDetaillist.get(d).setItemCategoryName(mainCategoryName);
                            itemDetaillist.get(d).setTag(String.valueOf(tag));
                            itemDetaillist.get(d).setViewType(3);
                            itemDetails.add(itemDetaillist.get(d));
                            // }
                        }
                    }
                    tag++;
                }
            }

        }

        refreshTotal();
        refreshList();
        //  mDetailAdapter.notifyDataSetChanged();
        //  mDecoration.setData(itemDetails);

//        if (itemDetails != null && itemDetails.size() > 0) {
//            mDecoration = new ItemHeaderDecoration(context, itemDetails);
//            re_menu_details.addItemDecoration(mDecoration);
//            mDecoration.setCheckListener(this);
//        }
        return itemDetailandCate;
    }

    private List<ItemDetail> getItemDetailmod(ItemCategory itemCategory) {
        itemDetails.clear();
        List<ItemDetail> itemDetaillist = new ArrayList<ItemDetail>();
        itemDetaillist = CoreData.getInstance().getItemDetails(itemCategory);
        // itemDetaillist  = ItemDetailSQL.getAllItemDetail();
        if (itemDetaillist != null || itemDetaillist.size() > 0) {
            for (int d = 0; d < itemDetaillist.size(); d++) {
                itemDetaillist.get(d).setItemCategoryName("");
                itemDetaillist.get(d).setTag(String.valueOf(0));
                itemDetaillist.get(d).setViewType(3);
                itemDetails.add(itemDetaillist.get(d));
                // }
            }
        }
        mDetailAdapter.notifyDataSetChanged();

        refreshTotal();
        refreshList();
        return null;
    }

    public long lastClickTime;

    public boolean canClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime > 500) {
            lastClickTime = currentTimeMillis;
            return true;
        } else {
            lastClickTime = currentTimeMillis;
            return false;
        }
    }


    @Override
    protected void handlerClickEvent(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ll_grab:
                mainCategoryAdapter.setCheckedPosition(-1);
                ll_grab.setBackgroundResource(R.drawable.main_btn_b);
                li_menu.setBackgroundResource(R.drawable.bg_grab);
                ll_menu_details.setVisibility(View.GONE);
                ll_video.setVisibility(View.VISIBLE);
                ll_view_cart_list.setVisibility(View.GONE);
                ll_view_pay.setVisibility(View.GONE);
                ll_view_cart.setVisibility(View.VISIBLE);
                ll_menu_title.setVisibility(View.VISIBLE);

                refreshTotal();
                videoStart();
//                RfidApiCentre.getInstance().startRFIDScan();
//            {// TODO
//                NurTagStorage nurTagStorage = new NurTagStorage();
//                String b = "989292920000000000000000";
//                NurTag tag = new NurTag(0,0,0,0,0,0,0, b.getBytes());
//                if(nurTagStorage.addTag(tag)) {
//                    HashMap<String, String> temp = new HashMap<>();
//                    temp.put("epc", tag.getEpcString());
//                    temp.put("rssi", "" + tag.getRssi());
//                    temp.put("timestamp", "" + tag.getTimestamp());
//                    temp.put("freq", "" + tag.getFreq() + " kHz (Ch: " + tag.getChannel() + ")");
//                    temp.put("found", "1");
//                    temp.put("foundpercent", "100");
//                    tag.setUserdata(temp);
//                }
//                RfidApiCentre.getInstance().setNurTagStorage(nurTagStorage);
//                initRfid();
//            }
                break;


            case R.id.ll_view_cart:
//                Intent intent = new Intent();
//                intent.setClass(MenuActivity.this, DialogActivity.class);
//                startActivity(intent);
                if (orderDetails != null && orderDetails.size() > 0) {
                    ll_menu_details.setVisibility(View.GONE);
                    ll_video.setVisibility(View.GONE);
                    ll_view_cart_list.setVisibility(View.VISIBLE);
                    ll_view_cart.setVisibility(View.GONE);
                    ll_view_pay.setVisibility(View.VISIBLE);
                    li_menu.setBackgroundResource(R.drawable.bg_grab);
                    ll_menu_title.setVisibility(View.GONE);
                    mainCategoryAdapter.setCheckedPosition(-1);
                    ll_order_dialog.setVisibility(View.GONE);
                    cartView();
                } else {
                    UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.select_menu_first));
                }

                break;
            case R.id.ll_view_order_card: {
                CCPaymentType = CC_TYPE_CC;
                paymentCheck();
            }
            break;
            case R.id.ll_view_order_ez: {
                CCPaymentType = CC_TYPE_EZ;
                paymentCheck();
            }
            break;


            case R.id.ll_view_order_qc: {

                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("qrCode", getIntent().getStringExtra("qrCode"));
//                map.put("revenueId", getIntent().getIntExtra("revenueId", 0));
//                map.put("consumeAmount", getIntent().getStringExtra("consumeAmount"));
//                map.put("operateType", getIntent().getIntExtra("operateType", -1));
                //  SyncCentre.getInstance().updateStoredCardValue(MenuActivity.this,map,handler);
            }
            break;
            case R.id.tv_dialog_ok:
                yesDialog = KpmDialogFactory.kpmVideoViewDialog(context, R.raw.grabplace, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, false);
                break;
        }
    }

    private void paymentCheck() {
        if (orderDetails == null || orderDetails.size() == 0) {
            UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.select_menu_first));
            return;
        }
        cancelTimer();
        dialog.setTotal(nurOrder.getTotal());
        dialog.setList(orderDetails);
        dialog.notifyAdapter();
        dialog.show();
    }

    private void paymentAction(String ip) {
        App.instance.stopADKpm();
        ll_view_cart.setVisibility(View.GONE);
        ll_view_pay.setVisibility(View.VISIBLE);
        if (paymentDialog != null && paymentDialog.isShowing()) {
            paymentDialog.dismiss();
        }

        String ccTitle = "Credit Card";
        if (CCPaymentType != CC_TYPE_CC) {
            ccTitle = "EZ-Link";
        }
        ccTitle += "\n"+getString(R.string.payment_in_progress);
        paymentDialog = KpmDialogFactory.qcDialog(context, ccTitle,
                "", R.drawable.credit_card, false,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CCCentre.getInstance().canCancel()) {
//                                CCCentre.getInstance().
                        } else {
                            UIHelp.showToastTransparentForKPMG(App.instance, getString(R.string.cannot_back));
                        }
                    }
                }, false);
        CCCentre.getInstance().connect(ip);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                VtintApiCentre.getInstance().startPay(new DecimalFormat("0").format(BH.mul(BH.getBD(nurOrder.getTotal()), BH.getBD("100"), false)));
//
//            }
//        }).start();
//
        mainCategoryAdapter.setCheckedPosition(-1);
    }

    private void cartView() {
        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        re_view_cart.setLayoutManager(mLinearLayoutManager);
        cartAdater = new CartDetailAdapter(context, orderDetails, setItemCountWindow, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {
//                isMoved = true;
//                App.isleftMoved = true;
//                targetPosition = position;
                setChecked(position, true, 0);

            }
        }, new CountView.OnCountChange() {
            @Override
            public void onChange(OrderDetail selectedorderDetail, int count, boolean isAdd) {

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderDetail", selectedorderDetail);
                map.put("count", count);
//                map.put("isAdd", isAdd);
                handler.sendMessage(handler.obtainMessage(
                        VIEW_ORDER_DETAIL_MODIFY_ITEM_COUNT, map));
            }
        });

        re_view_cart.setAdapter(cartAdater);
        tv_total_price.setText("S" + App.instance.getCurrencySymbol() + BH.formatMoney(nurOrder.getTotal()));
        tv_total_price.setTextColor(context.getResources().getColor(R.color.green));


    }


    private void setChecked(int position, boolean isLeft, int id) {
        Log.d("setChecked-------->", String.valueOf(position));
        if (isLeft) {
            mClassAdapter.setCheckedPosition(position);

        }
//        if (!App.isleftMoved) {
//          //  moveToCenter(position);
//        }

    }

//    private void moveToCenter(int position) {
//        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
//        View childAt = re_menu_classify.getChildAt(position - mLinearLayoutManager.findFirstVisibleItemPosition());
//        if (childAt != null) {
//            int y = (childAt.getTop() - re_main_category.getHeight() / 2);
//            re_menu_classify.smoothScrollBy(0, y);
//        }
//
//    }

    @Override
    public void check(int position, boolean isScroll) {

        setChecked(position, isScroll, 0);
    }

    private void refreshTotal() {
        //int itemCount = OrderDetailSQL.getCreatedOrderDetailCountForKpm(nurOrder.getId().intValue());
        int itemCount = 0;
        for (int i = 0; i < orderDetails.size(); i++) {
            itemCount = itemCount + orderDetails.get(i).getItemNum().intValue();

        }
        if (itemCount > 0) {
            if (ll_view_cart_list.getVisibility() == View.VISIBLE) {
                ll_view_cart.setVisibility(View.GONE);
            } else {
                ll_view_cart.setVisibility(View.VISIBLE);
            }

            rl_cart_num.setVisibility(View.VISIBLE);
            tv_cart_num.setText(itemCount + "");
            ll_view_cart.setEnabled(true);
            viewCart(false);
        } else {
            if (ll_video.getVisibility() == View.VISIBLE) {
                ll_view_cart.setVisibility(View.GONE);
                ll_view_cart.setEnabled(true);
            } else {
                ll_view_cart.setVisibility(View.VISIBLE);
                ll_view_cart.setEnabled(false);
                rl_cart_num.setVisibility(View.GONE);
                viewCart(true);
            }
        }
    }

    private void refreshList() {
//        adapter.setParams(currentOrder, orderDetails, currentGroupId);
////        adapter.notifyDataSetChanged();
        if (mDetailAdapter != null) {
            mDetailAdapter.setParams(nurOrder, orderDetails, 0);
            //cartAdater.notifyDataSetChanged();
            mDetailAdapter.notifyDataSetChanged();
        }

    }

    private void refreshViewCart() {
        if (cartAdater != null) {
            cartAdater.notifyDataSetChanged();
            tv_total_price.setText("S" + App.instance.getCurrencySymbol() + BH.formatMoney(nurOrder.getTotal()));
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            try {

                // Clear tag storage
                NurApi api = RfidApiCentre.getInstance().getNurApi();
                if (api != null && api.isConnected()) {
                    api.clearIdBuffer();
                    api.clearTagStorage();
                    LogUtil.e("TAG", "clear Api : " + api.getStorage().size());
                    NurTagStorage nurTagStorage = RfidApiCentre.getInstance().getNurTagStorage();
                    nurTagStorage.clear();
                    LogUtil.e("TAG", "clear nurTagStorage  : " + nurTagStorage.size());
                    NurRespInventory resp = api.inventory();
                    System.out.println("inventory numTagsFound: " + resp.numTagsFound);
                    LogUtil.e("TAG", "inventory numTagsFound: " + resp.numTagsFound);
                    LogUtil.e("TAG", "api.getStorage(): " + api.getStorage());
                    if (resp.numTagsFound > 0) {
                        // Fetch and print tags
                        api.fetchTags();
                        for (int n = 0; n < resp.numTagsFound; n++) {
                            NurTag tag = api.getStorage().get(n);
                            nurTagStorage.addTag(tag);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (nurOrder != null && nurOrder.getId() > 0) {
                                NurTagStorage nurTagStorage = RfidApiCentre.getInstance().getNurTagStorage();
//                            UIHelp.showShortToast(App.instance, "nurTagStorage size" + nurTagStorage.size());
                                List<NurTagDto> barCodes = OrderDetailRFIDHelp.getUnChooseItemBarCode(orderDetails, nurTagStorage);
                                if (!isUpdating) {
                                    LogUtil.e("TAG", "Storage size: =======" + nurTagStorage.size());
                                    if (barCodes.size() > 0) {

                                        LogUtil.e("TAG", "Add: ======= barCodes size" + barCodes.size());
                                        isUpdating = true;
                                        initRfid(barCodes);
                                    } else {
                                        Map<String, Integer> map = OrderDetailRFIDHelp.getUnScannerItemBarCode(orderDetails, nurTagStorage);
                                        if (map.size() > 0) {
                                            LogUtil.e("TAG", "Remove: ======= map size" + map.size());
                                            isUpdating = true;
                                            removeRfid(map);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                startTimer(2000);
            }
        }
    }

    private void initTextTypeFace() {
        textTypeFace = KpmTextTypeFace.getInstance();
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_grab));

        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_view_cart));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_card));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_ez));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_you_order));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_total));
        textTypeFace.setUbuntuBold((TextView) findViewById(R.id.tv_cart_total));
        textTypeFace.setUbuntuRegular((TextView) findViewById(R.id.tv_grab_content));
        textTypeFace.setRegular((TextView) findViewById(R.id.tv_menu_title));
        textTypeFace.setRegular((TextView) findViewById(R.id.tv_time));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_dia_title));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_dialog_ok));
    }

    private void timeSlot() {
        Calendar cal = Calendar.getInstance(Locale.US);// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        if (hour >= 0 && hour < 12) {
            tv_time.setText(getString(R.string.morning));
        } else if (hour >= 12 && hour < 15) {
//            System.out.println("在外围外");
            tv_time.setText(getString(R.string.midday));
        } else if (hour >= 15 && hour < 17) {
//            System.out.println("在外围外");
            tv_time.setText(getString(R.string.afternoon));
        } else if (hour >= 17 && hour < 20) {
            tv_time.setText(getString(R.string.evening));
        } else if (hour >= 20 && hour < 24) {
            tv_time.setText(getString(R.string.night));
        }

    }


    private void videoPause() {

        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
        }
    }

    private void videoStart() {

        if (mVideoView != null) {
            mVideoView.start();
        }
    }

    private void videoStop() {

        if (mVideoView != null) {
            if (mVideoView != null) {
                mVideoView.stopPlayback();

            }
        }
    }

    private void viewCart(Boolean b) {
        if (b) {
            ll_view_cart_bg.setBackgroundResource(R.drawable.btn_view_cart_g);
            tv_view_cart.setTextColor(getResources().getColor(R.color.gray6));
            img_view_cart.setBackgroundResource(R.drawable.view_cart_g);
        } else {
            ll_view_cart_bg.setBackgroundResource(R.drawable.btn_view_cart);
            tv_view_cart.setTextColor(getResources().getColor(R.color.white));
            img_view_cart.setBackgroundResource(R.drawable.view_cart);
        }


    }


}

