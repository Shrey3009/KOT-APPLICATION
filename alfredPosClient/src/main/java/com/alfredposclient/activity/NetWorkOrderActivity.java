package com.alfredposclient.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.javabean.temporaryforapp.AppOrderDetail;
import com.alfredbase.javabean.temporaryforapp.AppOrderDetailTax;
import com.alfredbase.javabean.temporaryforapp.AppOrderModifier;
import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderDetailTaxSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderModifierSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderSQL;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.TimeUtil;
import com.alfredbase.utils.ToastUtils;
import com.alfredposclient.Fragment.TableLayoutFragment;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.jobs.CloudSyncJobManager;
import com.alfredposclient.view.dialog.DeliveryDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NetWorkOrderActivity extends BaseActivity implements DeliveryDialog.PaymentClickListener {

    public static final int REFRESH_APPORDER_SUCCESS = 101;
    public static final int RECEVING_APP_ORDER_SUCCESS = 102;
    public static final int REFRESH_APPORDER_FAILED = -101;
    public static final int HTTP_FAILED = -102;
    public static final int RESULT_FAILED = -103;
    public static final int CANCEL_APPORDER_SUCCESS = 103;
    public static final int READY_APP_ORDER_SUCCESS = 104;

    private List<AppOrder> appOrders = new ArrayList<AppOrder>();
    private List<AppOrder> selectAppOrders = new ArrayList<AppOrder>();

    private List<AppOrder> appOrderNet = new ArrayList<AppOrder>();

    private List<AppOrder> appOrderDelivery = new ArrayList<AppOrder>();
    private List<AppOrderDetail> appOrderDetails = new ArrayList<AppOrderDetail>();
    private ListView lv_order_list;
    private ListView lv_orderdetail_list;
    private TextTypeFace textTypeFace = TextTypeFace.getInstance();
    private LayoutInflater inflater;
    private int selectOrderItem = 0;

    private int isEat = 1; //1 堂吃  2 打包  3 外卖
    private Button btn_check;
    private Button btn_cancel, btn_delivery;
    public static final int CHECK_REQUEST_CODE = 110;
    private AppOderAdapter appOderAdapter;
    private AppOderDetailAdapter appOderDetailAdapter;
    private int appOrderId = 0;
    private int selectViewId;
    private TextView tv_new_order;
    private TextView tv_preparing_order;
    private TextView tv_completed_order, tv_net_order, tv_delivery_order, tv_app_dine_in;
    private TableLayoutFragment f_tables;
    private LinearLayout ll_orderdetail_layout, ll_address;

    private TextView tv_app_address, tv_app_address_name, tv_app_address_phone, tv_app_address_time;
    private DeliveryDialog deliveryDialog;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_network_order);
        ll_orderdetail_layout = (LinearLayout) findViewById(R.id.ll_orderdetail_layout);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle(context.getString(R.string.loading));
        inflater = LayoutInflater.from(this);
        lv_order_list = (ListView) findViewById(R.id.lv_order_list);
        lv_orderdetail_list = (ListView) findViewById(R.id.lv_orderdetail_list);
        selectViewId = R.id.tv_new_order;
        initTextTypeFace();

        btn_check = (Button) findViewById(R.id.btn_check);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_delivery = (Button) findViewById(R.id.btn_delivery);
        btn_delivery.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_refresh).setOnClickListener(this);
        tv_new_order = (TextView) findViewById(R.id.tv_new_order);
        tv_preparing_order = (TextView) findViewById(R.id.tv_preparing_order);
        tv_completed_order = (TextView) findViewById(R.id.tv_completed_order);
        tv_net_order = (TextView) findViewById(R.id.tv_net_order);
        tv_app_dine_in = (TextView) findViewById(R.id.tv_app_dine_in);
        tv_delivery_order = (TextView) findViewById(R.id.tv_delivery_order);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        tv_app_address = (TextView) findViewById(R.id.tv_app_address);
        tv_app_address_name = (TextView) findViewById(R.id.tv_app_address_name);
        tv_app_address_phone = (TextView) findViewById(R.id.tv_app_address_phone);
        tv_app_address_time = (TextView) findViewById(R.id.tv_app_address_time);
        tv_net_order.setOnClickListener(this);
        tv_delivery_order.setOnClickListener(this);
        tv_app_dine_in.setOnClickListener(this);
        tv_app_dine_in.setBackgroundColor(getResources().getColor(R.color.brownness));
        tv_app_dine_in.setTextColor(getResources().getColor(R.color.white));


        tv_new_order.setOnClickListener(this);
        tv_preparing_order.setOnClickListener(this);
        tv_completed_order.setOnClickListener(this);
        lv_order_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectOrderItem = arg2;
                refreshDataView();
            }
        });
        initData();
        appOderAdapter = new AppOderAdapter();
        appOderAdapter.setSelfAppOrders(appOrders);
        lv_order_list.setAdapter(appOderAdapter);
        appOderDetailAdapter = new AppOderDetailAdapter();
        appOderDetailAdapter.setSelfAppOrderDetails(appOrderDetails);
        lv_orderdetail_list.setAdapter(appOderDetailAdapter);
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("appOrderId"))) {
            appOrderId = Integer.parseInt(intent.getStringExtra("appOrderId"));
        }
        if (appOrderId != 0) {
            for (AppOrder appOrder : appOrders) {
                if (appOrder.getId().intValue() == appOrderId) {
                    selectOrderItem = appOrders
                            .indexOf(appOrder);
                    lv_order_list.setSelection(selectOrderItem);
                }
            }
            appOrderId = 0;
        }
        try {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            f_tables = (TableLayoutFragment) fragmentManager.findFragmentById(R.id.f_tables_net);
            closeTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TableInfo tableInfo;

    @Override
    public void selectTable(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
        AppOrder appOrder = (AppOrder) btn_check.getTag();
        if (tableInfo == null)
            return;
        appOrder.setTableId(tableInfo.getPosId());
        AppOrderSQL.updateAppOrder(appOrder);
        List<AppOrderDetail> appOrderDetailList = AppOrderDetailSQL.getAppOrderDetailByAppOrderId(appOrder.getId().intValue());
        List<AppOrderModifier> appOrderModifierList = AppOrderModifierSQL.getAppOrderModifierByAppOrderId(appOrder.getId().intValue());
        List<AppOrderDetailTax> appOrderDetailTaxList = AppOrderDetailTaxSQL.getAppOrderDetailTaxByAppOrderId(appOrder.getId().intValue());
        App.instance.appOrderTransforOrder(appOrder, appOrderDetailList, appOrderModifierList, appOrderDetailTaxList);

        dismissLoadingDialog();
        closeTables();
    }

    private void closeTables() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(f_tables);
        transaction.commitAllowingStateLoss();
    }

    private void showTables() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
        transaction.show(f_tables);
        transaction.commitAllowingStateLoss();
        App.instance.showWelcomeToSecondScreen();
    }

    private void initData() {
        tv_new_order.setBackgroundColor(getResources().getColor(R.color.white));
        tv_new_order.setTextColor(getResources().getColor(R.color.black));
        tv_preparing_order.setBackgroundColor(getResources().getColor(R.color.white));
        tv_preparing_order.setTextColor(getResources().getColor(R.color.black));
        tv_completed_order.setBackgroundColor(getResources().getColor(R.color.white));
        tv_completed_order.setTextColor(getResources().getColor(R.color.black));
        btn_check.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.GONE);
        switch (selectViewId) {
            default:
            case R.id.tv_new_order:
                tv_new_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_new_order.setTextColor(getResources().getColor(R.color.white));
                btn_delivery.setVisibility(View.GONE);
                if (isEat == 1 || isEat == 2) {
                    appOrders = AppOrderSQL.getNewAppOrder(App.instance.getBusinessDate(), isEat);
                } else if (isEat == 3) {
                    appOrders = AppOrderSQL.getNewAppOrderAddress(App.instance.getBusinessDate());
                }
                btn_check.setText(getResources().getText(R.string.receving_order));
                if (!App.instance.isRevenueKiosk()) {
                    btn_cancel.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_preparing_order:
                tv_preparing_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_preparing_order.setTextColor(getResources().getColor(R.color.white));
                if (isEat == 1 || isEat == 2) {
                    btn_delivery.setVisibility(View.GONE);
                    appOrders = AppOrderSQL.getPreparAppOrder(App.instance.getBusinessDate(), isEat);
                } else if (isEat == 3) {
                    btn_delivery.setVisibility(View.VISIBLE);
                    appOrders = AppOrderSQL.getPreparAppOrderDelivery(App.instance.getBusinessDate());
                }

                btn_check.setText(getResources().getText(R.string.completed_order));
//				if(!App.instance.isRevenueKiosk()){
//					btn_check.setVisibility(View.INVISIBLE);
//					btn_cancel.setVisibility(View.GONE);
//				}
                break;
            case R.id.tv_completed_order:
                tv_completed_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_completed_order.setTextColor(getResources().getColor(R.color.white));
                if (isEat == 1 || isEat == 2) {
                    btn_delivery.setVisibility(View.GONE);
                    appOrders = AppOrderSQL.getAppOrderByOrderStatus(ParamConst.APP_ORDER_STATUS_COMPLETED, App.instance.getBusinessDate(), isEat);
                } else if (isEat == 3) {
                    btn_delivery.setVisibility(View.VISIBLE);
                    appOrders = AppOrderSQL.getAppOrderByOrderStatusDelivery(ParamConst.APP_ORDER_STATUS_COMPLETED, App.instance.getBusinessDate());
                }


                btn_check.setText(getResources().getText(R.string.reprint_bill));
//				if(!App.instance.isRevenueKiosk()){
//					btn_check.setVisibility(View.INVISIBLE);
//					btn_cancel.setVisibility(View.GONE);
//				}
                break;
        }
        if (appOrders.size() > 0) {
            ll_orderdetail_layout.setVisibility(View.VISIBLE);
            if (selectOrderItem > appOrders.size() - 1) {
                selectOrderItem = 0;
            }
            AppOrder appOrder = appOrders.get(selectOrderItem);
            TextView tv_eat_type = (TextView) findViewById(R.id.tv_eat_type);
            TextView tv_app_remarks = (TextView) findViewById(R.id.tv_app_remarks);

            if (isEat == 1 || isEat == 2) {
                ll_address.setVisibility(View.GONE);
            } else {

                if (appOrder.getOrderStatus() == ParamConst.APP_ORDER_STATUS_ACCEPTED) {
                    btn_check.setText(this.getString(R.string.place_order));
                } else if (appOrder.getOrderStatus() == ParamConst.APP_ORDER_STATUS_PAID) {
                    btn_check.setText(getResources().getText(R.string.receving_order));
                }

                ll_address.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(appOrder.getAddress())) {
                    tv_app_address.setText("");
                } else {
                    //    String addr = appOrder.getAddress();
                    tv_app_address.setText(getResources().getString(R.string.address) + " : " + appOrder.getAddress());
                }

                if (TextUtils.isEmpty(appOrder.getContact())) {
                    tv_app_address_name.setText("");
                    tv_app_address_name.setVisibility(View.GONE);
                } else {
                    //    String addr = appOrder.getAddress();
                    tv_app_address_name.setVisibility(View.VISIBLE);
                    tv_app_address_name.setText(getString(R.string.name) + " : " + appOrder.getContact());
                }

                if (TextUtils.isEmpty(appOrder.getMobile())) {
                    tv_app_address_phone.setText("");
                    tv_app_address_phone.setVisibility(View.GONE);
                } else {
                    //    String addr = appOrder.getAddress();
                    tv_app_address_phone.setVisibility(View.VISIBLE);
                    tv_app_address_phone.setText(getString(R.string.phone) + " : " + appOrder.getMobile());
                }
                if (appOrder.getDeliveryTime() == 0) {
                    tv_app_address_time.setText("");
                } else {
                    //    String addr = appOrder.getAddress();
                    tv_app_address_time.setText(getString(R.string.delivery_time) + " : " + TimeUtil.getDeliveryDataTime(appOrder.getDeliveryTime()));
                }

//              ;
//                tv_app_address_phone.setText(appOrder.getPhone());
            }
            if (appOrder.getEatType() == ParamConst.APP_ORDER_TAKE_AWAY) {
                tv_eat_type.setText(getResources().getString(R.string.takeaway));
            } else if (appOrder.getEatType() == ParamConst.APP_ORDER_DELIVERY) {
                tv_eat_type.setText(getResources().getString(R.string.delivery));
            } else {
                tv_eat_type.setText(getResources().getString(R.string.app_dine_in));
            }
            if (!TextUtils.isEmpty(appOrder.getOrderRemark())) {
                tv_app_remarks.setText(appOrder.getOrderRemark());
            } else {
                tv_app_remarks.setText("");
            }
            appOrderDetails = AppOrderDetailSQL.getAppOrderDetailByAppOrderId(appOrder.getId().intValue());
        } else {
            ll_orderdetail_layout.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshDataView() {
        initData();
        appOderAdapter.notifyDataSetChanged(appOrders);
        appOderDetailAdapter.notifyDataSetChanged(appOrderDetails);
    }

    private void initTextTypeFace() {
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.btn_back));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.btn_refresh));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_order_no));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_order_type));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_order_status));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_order_time));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_item_name));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_item_qty));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.btn_check));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.btn_cancel));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_new_order));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_preparing_order));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_completed_order));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_eat_type));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_remarks_title));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_remarks));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_dine_in));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_net_order));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_delivery_order));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.btn_delivery));
//
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_address));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_address_name));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_address_phone));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_address_time));


    }

    @Override
    protected void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.btn_check: {
                final AppOrder appOrder = (AppOrder) v.getTag();
//			if(App.instance.isRevenueKiosk()){
                selectOrderItem = 0;
                if (appOrder == null) {
                    return;
                }
                if (selectViewId == R.id.tv_new_order) {
//					Map<String, Object> parameters = new HashMap<String, Object>();
//					parameters.put("appOrderId", appOrder.getId().intValue());
//					parameters.put("orderStatus", ParamConst.APP_ORDER_STATUS_ACCEPTED);
//					parameters.put("orderNum", appOrder.getOrderNo());

//					appOrder.setOrderStatus(ParamConst.APP_ORDER_STATUS_PREPARED);
                    if (App.instance.isRevenueKiosk()) {
                        if (isEat == 3 && appOrder.getOrderStatus() == ParamConst.APP_ORDER_STATUS_PAID) {
                            DialogFactory.commonTwoBtnTimeDialog(context, context.getResources().getString(R.string.warning), context.getResources().getString(R.string.delivery_time) + " : " + TimeUtil.getDeliveryDataTime(appOrder.getDeliveryTime()),
                                    context.getString(R.string.accept_order),
                                    context.getResources().getString(R.string.cancel),
                                    context.getResources().getString(R.string.ok),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            loadingDialog.setTitle(context.getString(R.string.loading));
                                            loadingDialog.show();
                                            SyncCentre.getInstance().readyAppOrderStatus(context, appOrder.getId(), handler);

                                        }
                                    },
                                    new View.OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            loadingDialog.setTitle(context.getString(R.string.loading));
                                            loadingDialog.show();
                                            SyncCentre.getInstance().recevingAppOrderStatus(context, appOrder.getId(), handler);

                                        }
                                    }, true
                            );
                        } else if (isEat == 3 && appOrder.getOrderStatus() == ParamConst.APP_ORDER_STATUS_ACCEPTED) {
                            loadingDialog.setTitle(context.getString(R.string.loading));
                            loadingDialog.show();
                            List<AppOrderDetail> appOrderDetailList = AppOrderDetailSQL.getAppOrderDetailByAppOrderId(appOrder.getId());
                            List<AppOrderModifier> appOrderModifierList = AppOrderModifierSQL.getAppOrderModifierByAppOrderId(appOrder.getId());
                            List<AppOrderDetailTax> appOrderDetailTaxList = AppOrderDetailTaxSQL.getAppOrderDetailTaxByAppOrderId(appOrder.getId());
                            App.instance.appOrderTransforOrder(appOrder, appOrderDetailList, appOrderModifierList, appOrderDetailTaxList);
                            dismissLoadingDialog();
                        } else {
                            loadingDialog.setTitle(context.getString(R.string.loading));
                            loadingDialog.show();
                            SyncCentre.getInstance().recevingAppOrderStatus(context, appOrder.getId(), handler);
                        }
                    } else {
                        showTables();
                    }
                } else {
                    boolean flag = false;
                    Order order = OrderSQL.getOrderByAppOrderId(appOrder.getId());
                    KotSummary ks = KotSummarySQL.getKotSummary(order.getId(),order.getNumTag());
                    if (ks == null)
                        flag = true;
                    if (ks.getStatus() == ParamConst.KOTS_STATUS_DONE)
                        flag = true;
                    if (flag) {
                        appOrder
                                .setOrderStatus(ParamConst.APP_ORDER_STATUS_COMPLETED);
                        AppOrderSQL.updateAppOrder(appOrder);
                        PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
                                context);
                        printerLoadingDialog.setTitle(context.getResources().getString(
                                R.string.receipt_printing));
                        printerLoadingDialog.showByTime(3000);
                        List<AppOrder> list = new ArrayList<>();
                        App.instance.printerAppOrder(appOrder, "", list);
                        CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                        if (cloudSync != null) {
                            cloudSync.checkAppOrderStatus(
                                    App.instance.getRevenueCenter().getId().intValue(),
                                    appOrder.getId().intValue(),
                                    appOrder.getOrderStatus().intValue(), "",
                                    App.instance.getBusinessDate().longValue(), appOrder.getOrderNo());
                        }
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                refreshView();
                            }
                        }, 3000);

                    } else {
                    ToastUtils.showToast(context,"Please complete order on KDS first");
                    }
                }
//			}else{
//				showTables();
//			}

			/*
			final AppOrder appOrder = (AppOrder)v.getTag();
			Tables tables = CoreData.getInstance().getTables(appOrder.getTableId().intValue());
			DialogFactory.commonTwoBtnDialog(this, "Warning", "Please confirm '" + tables.getTableName() + "' has been cleared?",
					"No", "Yes", null, new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(context);
							printerLoadingDialog.setTitle(context.getString(R.string.loading));
							printerLoadingDialog.showByTime(3000);
							new Thread(new Runnable() {
								@Override
								public void run() {
									App.instance.appOrderTransforOrder(appOrder,
											AppOrderDetailSQL.getAppOrderDetailByAppOrderId(appOrder.getId().intValue()),
											AppOrderModifierSQL.getAppOrderModifierByAppOrderId(appOrder.getId().intValue()),
											AppOrderDetailTaxSQL.getAppOrderDetailTaxByAppOrderId(appOrder.getId().intValue()));
								}
							}).start();
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									appOderAdapter.notifyDataSetChanged();
									appOderDetailAdapter.notifyDataSetChanged();
								}
							}, 3001);
						}
					});


*/
            }
            break;
            case R.id.btn_cancel: {
                AppOrder appOrder = (AppOrder) v.getTag();
                loadingDialog.show();
                SyncCentre.getInstance().appOrderRefund(this, appOrder.getId().intValue(), handler);
            }
            break;
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_refresh:
                refreshView();
                break;
            case R.id.tv_new_order:
            case R.id.tv_preparing_order:
            case R.id.tv_completed_order:
                if (selectViewId != v.getId()) {
                    selectViewId = v.getId();
                    selectOrderItem = 0;
                    refreshDataView();
                }
                break;

            case R.id.tv_net_order:
                isEat = 2;
                tv_net_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_net_order.setTextColor(getResources().getColor(R.color.white));
                tv_delivery_order.setBackgroundColor(getResources().getColor(R.color.white));
                tv_delivery_order.setTextColor(getResources().getColor(R.color.black));
                tv_app_dine_in.setBackgroundColor(getResources().getColor(R.color.white));
                tv_app_dine_in.setTextColor(getResources().getColor(R.color.black));
                selectOrderItem = 0;
                refreshDataView();
                break;
            case R.id.tv_delivery_order:
                tv_delivery_order.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_delivery_order.setTextColor(getResources().getColor(R.color.white));
                tv_net_order.setBackgroundColor(getResources().getColor(R.color.white));
                tv_net_order.setTextColor(getResources().getColor(R.color.black));
                tv_app_dine_in.setBackgroundColor(getResources().getColor(R.color.white));
                tv_app_dine_in.setTextColor(getResources().getColor(R.color.black));
                isEat = 3;
                selectOrderItem = 0;
                refreshDataView();
                break;

            case R.id.tv_app_dine_in:
                tv_app_dine_in.setBackgroundColor(getResources().getColor(R.color.brownness));
                tv_app_dine_in.setTextColor(getResources().getColor(R.color.white));
                tv_net_order.setBackgroundColor(getResources().getColor(R.color.white));
                tv_net_order.setTextColor(getResources().getColor(R.color.black));
                tv_delivery_order.setBackgroundColor(getResources().getColor(R.color.white));
                tv_delivery_order.setTextColor(getResources().getColor(R.color.black));
                isEat = 1;
                selectOrderItem = 0;
                refreshDataView();
                break;

            case R.id.btn_delivery:
                appOrders = AppOrderSQL.getAppOrderList(App.instance.getBusinessDate());
                if (isEat == 3 && selectViewId != R.id.tv_new_order) {
                    deliveryDialog = new DeliveryDialog(NetWorkOrderActivity.this, appOrders);
                    deliveryDialog.setPaymentClickListener(this);
                }


//                if (appOrders.size() > 0) {
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("apporder",
//                            (Serializable) appOrders);// 将数据打包存入intent
//                    intent.setClass(NetWorkOrderActivity.this, DeliveryDialogActivity.class);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, 100);
//                }
                break;
            default:
                break;
        }
    }

    private void checkOrder(TempOrder tempOrder) {
        tempOrder.setStatus(ParamConst.TEMPORDER_STATUS_CHECKED);
        TempOrderSQL.updateTempOrder(tempOrder);
        Intent intent = this.getIntent();
        intent.putExtra("appOrderId", tempOrder.getAppOrderId());
        setResult(CHECK_REQUEST_CODE, intent);
        this.finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_APPORDER_SUCCESS:
                    loadingDialog.dismiss();
                    initData();
                    appOderAdapter.notifyDataSetChanged(appOrders);
                    appOderDetailAdapter.notifyDataSetChanged(appOrderDetails);
                    break;
                case REFRESH_APPORDER_FAILED:
                    dismissLoadingDialog();
                    initData();
                    appOderAdapter.notifyDataSetChanged(appOrders);
                    appOderDetailAdapter.notifyDataSetChanged(appOrderDetails);
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context,
                            (Throwable) msg.obj, context.getResources().getString(R.string.server)));
                    break;
                case RESULT_OK:
                    initData();
                    appOderAdapter.notifyDataSetChanged(appOrders);
                    appOderDetailAdapter.notifyDataSetChanged(appOrderDetails);
                    dismissLoadingDialog();
                    break;
                case RECEVING_APP_ORDER_SUCCESS: {
                    dismissLoadingDialog();
                    int id = (Integer) msg.obj;
                    AppOrder appOrder = AppOrderSQL.getAppOrderById(id);
                    appOrder.setOrderStatus(ParamConst.APP_ORDER_STATUS_ACCEPTED);
                    List<AppOrderDetail> appOrderDetailList = AppOrderDetailSQL.getAppOrderDetailByAppOrderId(id);
                    List<AppOrderModifier> appOrderModifierList = AppOrderModifierSQL.getAppOrderModifierByAppOrderId(id);
                    List<AppOrderDetailTax> appOrderDetailTaxList = AppOrderDetailTaxSQL.getAppOrderDetailTaxByAppOrderId(id);
                    App.instance.appOrderTransforOrder(appOrder, appOrderDetailList, appOrderModifierList, appOrderDetailTaxList);
                }
                break;

                case READY_APP_ORDER_SUCCESS: {
                    dismissLoadingDialog();
                    int id = (Integer) msg.obj;
                    AppOrder appOrder = AppOrderSQL.getAppOrderById(id);
                    appOrder.setOrderStatus(ParamConst.APP_ORDER_STATUS_ACCEPTED);
                    AppOrderSQL.addAppOrder(appOrder);

                    dismissLoadingDialog();
                    refreshDataView();
//                    List<AppOrderDetail> appOrderDetailList = AppOrderDetailSQL.getAppOrderDetailByAppOrderId(id);
//                    List<AppOrderModifier> appOrderModifierList = AppOrderModifierSQL.getAppOrderModifierByAppOrderId(id);
//                    List<AppOrderDetailTax> appOrderDetailTaxList = AppOrderDetailTaxSQL.getAppOrderDetailTaxByAppOrderId(id);
//                    App.instance.appOrderTransforOrder(appOrder, appOrderDetailList, appOrderModifierList, appOrderDetailTaxList);
                }
                break;
                case HTTP_FAILED:
                    dismissLoadingDialog();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context,
                            (Throwable) msg.obj, context.getResources().getString(R.string.server)));
                    break;
                case RESULT_FAILED:
                    dismissLoadingDialog();
                    UIHelp.showShortToast(context, ResultCode.getErrorResultStrByCode(context,
                            (Integer) msg.obj, context.getResources().getString(R.string.server)));
                    refreshDataView();
                    break;
                case MainPage.VIEW_EVENT_SET_APPORDER_TABLE_PACKS: {
                    AppOrder appOrder = (AppOrder) btn_check.getTag();
                    if (tableInfo == null)
                        return;
                    appOrder.setTableId(tableInfo.getPosId());
                    AppOrderSQL.updateAppOrder(appOrder);
                    List<AppOrderDetail> appOrderDetailList = AppOrderDetailSQL.getAppOrderDetailByAppOrderId(appOrder.getId().intValue());
                    List<AppOrderModifier> appOrderModifierList = AppOrderModifierSQL.getAppOrderModifierByAppOrderId(appOrder.getId().intValue());
                    List<AppOrderDetailTax> appOrderDetailTaxList = AppOrderDetailTaxSQL.getAppOrderDetailTaxByAppOrderId(appOrder.getId().intValue());
                    App.instance.appOrderTransforOrder(appOrder, appOrderDetailList, appOrderModifierList, appOrderDetailTaxList);
                    dismissLoadingDialog();
                    closeTables();
                }
                break;
                case CANCEL_APPORDER_SUCCESS:
                    dismissLoadingDialog();
                    refreshDataView();
                    break;
                case ResultCode.APP_REFUND_FAILD:
                    dismissLoadingDialog();
                    UIHelp.showToast(context, context.getString(R.string.refund_error) + " " + msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void refreshView() {
        loadingDialog.show();
        int type=SharedPreferencesHelper.getInt(this,SharedPreferencesHelper.TRAINING_MODE);
        if(type!=1){
            SyncCentre.getInstance().getAllAppOrder(this, new HashMap<String, Object>(), handler);

        }
    }


    @Override
    public void httpRequestAction(int action, Object obj) {
        if (action == RESULT_OK) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.show();
                }
            });
            handler.sendEmptyMessage(RESULT_OK);
        }
        if (action == ResultCode.APP_ORDER_REFUND) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshView();
                }
            });
        }
        super.httpRequestAction(action, obj);

    }

    @Override
    public void onPaymentClick(List<AppOrder> order) {

        StringBuffer str = new StringBuffer();

//        for (int i = 0; i < order.size(); i++) {
//
////            Order paidOrder = OrderSQL.getOrderByAppOrderId(order.get(i)
////                    .getId().intValue());
//
//
////            if (paidOrder != null) {
////                order.get(i).setOrderNo(Integer.valueOf(IntegerUtils.fromat(App.instance.getRevenueCenter().getIndexId(), paidOrder.getOrderNo().toString())));
////            }
////            str.append(getResources().getString(R.string.order_no_) + " " + IntegerUtils.fromat(App.instance.getRevenueCenter().getIndexId(), paidOrder.getOrderNo().toString()) + "\n");
////            AppOrder appOrder = order.get(i);
////            if (!TextUtils.isEmpty(appOrder.getContact())) {
////                if (!TextUtils.isEmpty(appOrder.getMobile())) {
////                    str.append(appOrder.getContact() + "    " + appOrder.getMobile() + "\n");
////                } else {
////                    str.append(appOrder.getContact() + "   " + "\n");
////                }
////
////            } else {
////                if (!TextUtils.isEmpty(appOrder.getMobile())) {
////                    str.append(appOrder.getMobile() + "\n");
////                } else {
////                    //   str.append(appOrder.getContact()+"   "+"\n");
////                }
////            }
////            if (!TextUtils.isEmpty(appOrder.getAddress())) {
////                str.append(appOrder.getAddress() + "\n");
////            }
////
////
//        }


        App.instance.printerAppOrder(appOrders.get(0), "", order);
//        Toast.makeText(NetWorkOrderActivity.this, "print !", Toast.LENGTH_SHORT)
//                .show();

    }

    class AppOderAdapter extends BaseAdapter {
        private List<AppOrder> selfAppOrders = new ArrayList<AppOrder>();

        public void setSelfAppOrders(List<AppOrder> selfAppOrders) {
            this.selfAppOrders.clear();
            this.selfAppOrders.addAll(selfAppOrders);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return selfAppOrders.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return selfAppOrders.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public void notifyDataSetChanged(List<AppOrder> selfAppOrders) {
            this.selfAppOrders.clear();
            this.selfAppOrders.addAll(selfAppOrders);
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            HolderView holder = null;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.temp_order_item, null);
                holder = new HolderView();
                holder.tv_order_id = (TextView) arg1.findViewById(R.id.tv_order_id);
                holder.tv_order_status = (TextView) arg1.findViewById(R.id.tv_order_status);
                holder.tv_order_type = (TextView) arg1.findViewById(R.id.tv_order_type);
                holder.tv_place_time = (TextView) arg1.findViewById(R.id.tv_place_time);
                textTypeFace.setTrajanProRegular(holder.tv_order_id);
                textTypeFace.setTrajanProRegular(holder.tv_order_type);
                textTypeFace.setTrajanProRegular(holder.tv_place_time);
                textTypeFace.setTrajanProRegular(holder.tv_order_status);
                arg1.setTag(holder);
            } else {
                holder = (HolderView) arg1.getTag();
            }

            AppOrder appOrder = selfAppOrders.get(arg0);


            if (arg0 == selectOrderItem) {
                arg1.setBackgroundColor(NetWorkOrderActivity.this.getResources().getColor(R.color.brownness));
                btn_check.setTag(appOrder);
                btn_cancel.setTag(appOrder);
//				if(appOrder.getTableType().intValue() == ParamConst.APP_ORDER_TABLE_STATUS_USED){
//					btn_check.setVisibility(View.VISIBLE);
//				}else{
//					btn_check.setVisibility(View.INVISIBLE);
//				}
                holder.tv_order_id.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
                holder.tv_order_status.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
                holder.tv_order_type.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
                holder.tv_place_time.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
            } else {
                arg1.setBackgroundColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
                holder.tv_order_id.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
                holder.tv_order_status.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
                holder.tv_order_type.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
                holder.tv_place_time.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
            }
            holder.tv_order_id.setText(appOrder.getId() + "");
            String statusStr = "";
            switch (appOrder.getOrderStatus().intValue()) {
                case ParamConst.APP_ORDER_STATUS_PAID:
                    statusStr = getResources().getString(R.string.paid);
                    break;
                case ParamConst.APP_ORDER_STATUS_ACCEPTED:
                    statusStr = getResources().getString(R.string.confirmed);
                    break;
                case ParamConst.APP_ORDER_STATUS_PREPARING:
                    statusStr = getResources().getString(R.string.preparing);
                    break;
                case ParamConst.APP_ORDER_STATUS_PREPARED:
                    statusStr = getResources().getString(R.string.prepared);
                    break;
                case ParamConst.APP_ORDER_STATUS_COMPLETED:
                    statusStr = getResources().getString(R.string.finish);
                    break;
                default:
                    break;
            }
            holder.tv_order_status.setText(statusStr);
            holder.tv_order_type.setText(getString(R.string.online));
            holder.tv_place_time.setText(TimeUtil.getCloseBillDataTime(appOrder.getCreateTime()));
//            } else {
//
//                if (arg0 == selectOrderItem) {
//                    arg1.setBackgroundColor(NetWorkOrderActivity.this.getResources().getColor(R.color.brownness));
//                    btn_check.setTag(appOrder);
//                    btn_cancel.setTag(appOrder);
////				if(appOrder.getTableType().intValue() == ParamConst.APP_ORDER_TABLE_STATUS_USED){
////					btn_check.setVisibility(View.VISIBLE);
////				}else{
////					btn_check.setVisibility(View.INVISIBLE);
////				}
//                    holder.tv_order_id.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
//                    holder.tv_order_status.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
//                    holder.tv_order_type.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
//                    holder.tv_place_time.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
//                } else {
//                    arg1.setBackgroundColor(NetWorkOrderActivity.this.getResources().getColor(R.color.white));
//                    holder.tv_order_id.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
//                    holder.tv_order_status.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
//                    holder.tv_order_type.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
//                    holder.tv_place_time.setTextColor(NetWorkOrderActivity.this.getResources().getColor(R.color.black));
//                }
//                holder.tv_order_id.setText(appOrder.getId() + "");
//                String statusStr = "";
//                switch (appOrder.getOrderStatus().intValue()) {
//                    case ParamConst.APP_ORDER_STATUS_PAID:
//                        statusStr = getResources().getString(R.string.paid);
//                        break;
//                    case ParamConst.APP_ORDER_STATUS_ACCEPTED:
//                        statusStr = getResources().getString(R.string.making);
//                        break;
//                    case ParamConst.APP_ORDER_STATUS_PREPARING:
//                        statusStr = getResources().getString(R.string.preparing);
//                        break;
//                    case ParamConst.APP_ORDER_STATUS_PREPARED:
//                        statusStr = getResources().getString(R.string.prepared);
//                        break;
//                    case ParamConst.APP_ORDER_STATUS_COMPLETED:
//                        statusStr = getResources().getString(R.string.finish);
//                        break;
//                    default:
//                        break;
//                }
//                holder.tv_order_status.setText(statusStr);
//                holder.tv_order_type.setText(getString(R.string.online));
//                holder.tv_place_time.setText(TimeUtil.getCloseBillDataTime(appOrder.getCreateTime()));
//
//            }
            return arg1;
        }

        class HolderView {
            public TextView tv_order_id;
            public TextView tv_order_status;
            public TextView tv_order_type;
            public TextView tv_place_time;

        }
    }


    class AppOderDetailAdapter extends BaseAdapter {
        private List<AppOrderDetail> selfAppOrderDetails = new ArrayList<AppOrderDetail>();

        public void setSelfAppOrderDetails(List<AppOrderDetail> selfAppOrderDetails) {
            this.selfAppOrderDetails.clear();
            this.selfAppOrderDetails.addAll(selfAppOrderDetails);
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return selfAppOrderDetails.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return selfAppOrderDetails.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public void notifyDataSetChanged(List<AppOrderDetail> selfAppOrderDetails) {
            this.selfAppOrderDetails.clear();
            this.selfAppOrderDetails.addAll(selfAppOrderDetails);
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            HolderView holder = null;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.temp_orderdetail_item, null);
                holder = new HolderView();
                holder.tv_orderdetail_name = (TextView) arg1.findViewById(R.id.tv_orderdetail_name);
                holder.tv_orderdetail_qty = (TextView) arg1.findViewById(R.id.tv_orderdetail_qty);
                holder.tv_temp_modifier = (TextView) arg1.findViewById(R.id.tv_temp_modifier);
                holder.ll_address = (LinearLayout) arg1.findViewById(R.id.ll_address);
                holder.tv_app_address = (TextView) arg1.findViewById(R.id.tv_app_address);
                holder.tv_app_address_phone = (TextView) arg1.findViewById(R.id.tv_app_address);
                holder.tv_app_address_name = (TextView) arg1.findViewById(R.id.tv_app_address);
                holder.tv_app_address_time = (TextView) arg1.findViewById(R.id.tv_app_address_time);
                textTypeFace.setTrajanProRegular(holder.tv_orderdetail_name);
                textTypeFace.setTrajanProRegular(holder.tv_orderdetail_qty);
                textTypeFace.setTrajanProRegular(holder.tv_temp_modifier);
                arg1.setTag(holder);
            } else {
                holder = (HolderView) arg1.getTag();
            }
            AppOrderDetail appOrderDetail = selfAppOrderDetails.get(arg0);
            holder.tv_orderdetail_name.setText(appOrderDetail.getItemName());
            holder.tv_orderdetail_qty.setText(appOrderDetail.getItemNum() + "");

            List<AppOrderModifier> appOrderModifiers = AppOrderModifierSQL.getAppOrderModifierByOrderDetailId(appOrderDetail.getId().intValue());
            StringBuffer modifierNames = new StringBuffer();
            for (AppOrderModifier appOrderModifier : appOrderModifiers) {
                if (modifierNames.length() != 0) {
                    modifierNames.append(",");
                }
                modifierNames.append(appOrderModifier.getModifierName());
            }
            holder.tv_temp_modifier.setText(modifierNames.toString());
            return arg1;
        }

        class HolderView {
            public TextView tv_orderdetail_name;
            public TextView tv_orderdetail_qty;
            public TextView tv_temp_modifier;

            public LinearLayout ll_address;
            public TextView tv_app_address_phone, tv_app_address_name, tv_app_address, tv_app_address_time;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {// 请求码为MainToDialog
            if (resultCode == 200) {// 取消
                selectAppOrders = (List<AppOrder>) getIntent().getSerializableExtra("selectedApporder");
            } else if (resultCode == 10000) {
//                selectedStudent = data
//                        .getParcelableArrayListExtra("selectedStudents");getParcelableArrayListExtra
            }
        }
        handleSelectedData();
    }

    private void handleSelectedData() {

        if (selectAppOrders == null) {// 取消
            Toast.makeText(NetWorkOrderActivity.this, this.getString(R.string.didnt_choose_data), Toast.LENGTH_SHORT)
                    .show();
        } else {// 确定
            if (selectAppOrders.size() >= 1) {// 有选择
                StringBuilder sb = new StringBuilder();
                Toast.makeText(NetWorkOrderActivity.this, this.getString(R.string.have_a_choice),
                        Toast.LENGTH_SHORT).show();
//                App student = null;
//                for (int i = 0; i < selectedStudent.size(); i++) {
//                    student = selectedStudent.get(i);
//                    sb.append(student.getName() + ",");
//                }
//                sb.deleteCharAt(sb.length() - 1);
//                tvShow.setText(sb.toString());
            } else {// 无选择
                Toast.makeText(NetWorkOrderActivity.this, this.getString(R.string.didnt_choose_data),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}



