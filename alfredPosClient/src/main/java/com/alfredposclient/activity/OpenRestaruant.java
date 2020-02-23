package com.alfredposclient.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.VerifyDialog;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.LoginResult;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.MultiReportRelation;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDayPromotion;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserOpenDrawerRecord;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.ReportSessionSales;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.GeneralSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotNotificationSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.MultiReportRelationSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.ReportDayPaymentSQL;
import com.alfredbase.store.sql.ReportDaySalesSQL;
import com.alfredbase.store.sql.ReportDayTaxSQL;
import com.alfredbase.store.sql.ReportHourlySQL;
import com.alfredbase.store.sql.ReportPluDayComboModifierSQL;
import com.alfredbase.store.sql.ReportPluDayItemSQL;
import com.alfredbase.store.sql.ReportPluDayModifierSQL;
import com.alfredbase.store.sql.ReportSessionSalesSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.RevenueCenterSQL;
import com.alfredbase.store.sql.SubPosBeanSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.UserOpenDrawerRecordSQL;
import com.alfredbase.store.sql.cpsql.CPOrderSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.PreciseTimeUtil;
import com.alfredposclient.global.ReportObjectFactory;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.jobs.CloudSyncJobManager;
import com.alfredposclient.utils.AlertToDeviceSetting;
import com.alfredposclient.utils.SessionImageUtils;
import com.alfredposclient.view.SettingView;
import com.floatwindow.float_lib.FloatActionController;
import com.google.gson.Gson;
import com.moonearly.model.UdpMsg;
import com.moonearly.utils.service.UdpServiceCallBack;
import com.tencent.bugly.crashreport.BuglyLog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class OpenRestaruant extends BaseActivity implements OnTouchListener {
    private RelativeLayout rl_lunch_bg;
    private ImageView iv_lunch_icon;
    private RelativeLayout rl_breakfast_bg;
    private ImageView iv_breakfast_icon;
    private RelativeLayout rl_dinner_bg;
    private ImageView iv_dinner_icon;

    private RelativeLayout rl_center_bg;
    private RelativeLayout rl_lunch_session_bg;
    private ImageView iv_lunch_session_icon;
    private RelativeLayout rl_breakfast_session_bg;
    private ImageView iv_breakfast_session_icon;
    private RelativeLayout rl_dinner_session_bg;
    private ImageView iv_dinner_session_icon;
    private RelativeLayout rl_supper_session_bg;
    private ImageView iv_supper_session_icon;

    private RelativeLayout rl_slideUnlockView;
    private float rl_slideUnlockViewX = -1;
    private float rl_slideUnlockViewY = -1;
    private float iv_lunch_iconX = -1;
    private float iv_lunch_iconY = -1;
    private float iv_breakfast_iconX = -1;
    private float iv_breakfast_iconY = -1;
    private float iv_dinner_iconX = -1;
    private float iv_dinner_iconY = -1;
    private float iv_lunch_session_iconX = -1;
    private float iv_lunch_session_iconY = -1;
    private float iv_breakfast_session_iconX = -1;
    private float iv_breakfast_session_iconY = -1;
    private float iv_dinner_session_iconX = -1;
    private float iv_dinner_session_iconY = -1;
    private float iv_supper_session_iconX = -1;
    private float iv_supper_session_iconY = -1;


    private RelativeLayout rl_closerestbg;
    private RelativeLayout rl_openbg;
    private IntentFilter filter;
    private ImageView iv_restaurant;
    private float iv_restaurantX;
    private float iv_restaurantY;
    private TextView tv_msgnum;
    private DrawerLayout mDrawerLayout;
    private SettingView mSettingView;
    private static final int CAN_CLOSE = 100;
    private static final int CAN_NOT_CLOSE_SUB = 99;
    private static final int CAN_NOT_CLOSE = 101;
    private static final int OPEN_RESTAURANT = 102;
    private static final int PRINTER_UNLINK = 103;
    private static final int PROGRESS_PRINT_Z_START = 104;
    private static final int PROGRESS_PRINT_Z_END = 105;
    private PrinterLoadingDialog zPrinterLoadingDialog;
    private boolean doubleBackToExitPressedOnce = false;
    private int size;
    private Observable<Integer> observable;
    private Observable<Object> observable1;
    private VerifyDialog verifyDialog;
    private static final int OVERLAY_PERMISSION_REQ_CODE = 0x001;
    int train;
    private Boolean isTrain = true;
//	private RelativeLayout rl_view_bg1;
//	private ImageView iv_view_icon1;
//	private RelativeLayout rl_view_bg2;
//	private ImageView iv_view_icon2;
//	private RelativeLayout rl_view_bg3;
//	private ImageView iv_view_icon3;
//	private RelativeLayout rl_view_bg4;
//	private ImageView iv_view_icon4;


    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);// 关闭阴影
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // 关闭手势滑动
        mSettingView = (SettingView) findViewById(R.id.settingView);
        mSettingView.setParams(this, mDrawerLayout);
//		if (App.instance.isSUNMIShow()){
//			mSettingView.SUNMIVisible();
//		}else {
//			mSettingView.SUNMIGone();
//		}
    }

    private DownloadManager downManager;
    private BroadcastReceiver downReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            long id1 = intent.getLongExtra(DownloadManager.INTENT_EXTRAS_SORT_BY_SIZE, -1);
            LogUtil.e("jidu", id1 + "");
            long downloadId = Store.getLong(context, "posUpdateId");
            if (id == downloadId) {
                DownloadManager.Query query = new DownloadManager.Query();
                Cursor cursor = downManager.query(query);
                while (cursor.moveToNext()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (!TextUtils.isEmpty(address)) {
                            if (context.getPackageName().equals(DownloadFactory.getApkInfo(context, Uri.parse(address).getPath()).packageName)) {
                                DownloadFactory.installApk(OpenRestaruant.this, address);
                                return;
                            }
                        }
                    }
                }
                cursor.close();
            }
        }
    };

    private void downloadPos(VersionUpdate versionUpdate) {
        long posUpdateId = DownloadFactory.downloadApk(context, downManager, versionUpdate.getPosDownload(), Store.getLong(context, "posUpdateId"));
        Store.putLong(context, "posUpdateId", posUpdateId);
    }

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_open_restaruant);
        train = SharedPreferencesHelper.getInt(context, SharedPreferencesHelper.TRAINING_MODE);
        BH.initFormart(App.instance.getLocalRestaurantConfig().getFormatType(), App.instance.getLocalRestaurantConfig().getCurrencySymbol());
        ButtonClickTimer.canClick();
        initDrawerLayout();
        verifyDialog = new VerifyDialog(context, handler);
        zPrinterLoadingDialog = new PrinterLoadingDialog(context);
        rl_slideUnlockView = (RelativeLayout) findViewById(R.id.rl_slideUnlockView);
        iv_restaurant = (ImageView) findViewById(R.id.iv_restaurant);
        rl_closerestbg = (RelativeLayout) findViewById(R.id.rl_closerestbg);
        rl_openbg = (RelativeLayout) findViewById(R.id.rl_openbg);
        tv_msgnum = (TextView) findViewById(R.id.tv_msgnum);
        TextTypeFace.getInstance().setTypeface(
                (TextView) findViewById(R.id.tv_now_hour));
        TextTypeFace.getInstance().setTypeface(
                (TextView) findViewById(R.id.tv_now_min));
        size = App.instance.getLocalRestaurantConfig().getSessionConfigType().size();
        intSessionView();
        setSessionIconStatus(size);
        ((TextView) findViewById(R.id.tv_userName)).setText(App.instance
                .getUser().getFirstName()
                + "."
                + App.instance.getUser().getLastName());
        findViewById(R.id.iv_setting).setOnClickListener(this);
        rl_closerestbg.setOnClickListener(this);
        rl_openbg.setOnClickListener(this);
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
//		App.instance.startPushServer("B." + App.instance.getRevenueCenter().getRestaurantId().intValue());
        setDateView();
        try {
            BuglyLog.d("OpenRestaruant", "RestaurantName" + CoreData.getInstance().getRestaurant().getRestaurantName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rl_slideUnlockView.post(new Runnable() {
            @Override
            public void run() {
                rl_slideUnlockViewX = rl_slideUnlockView.getX();
                rl_slideUnlockViewY = rl_slideUnlockView.getY();
                iv_restaurantX = iv_restaurant.getX();
                iv_restaurantY = iv_restaurant.getY();
                iv_lunch_iconX = iv_lunch_icon.getX();
                iv_lunch_iconY = iv_lunch_icon.getY();
                iv_breakfast_iconX = iv_breakfast_icon.getX();
                iv_breakfast_iconY = iv_breakfast_icon.getY();
                iv_dinner_iconX = iv_dinner_icon.getX();
                iv_dinner_iconY = iv_dinner_icon.getY();
                iv_lunch_session_iconX = iv_lunch_session_icon.getX();
                iv_lunch_session_iconY = iv_lunch_session_icon.getY();
                iv_breakfast_session_iconX = iv_breakfast_session_icon.getX();
                iv_breakfast_session_iconY = iv_breakfast_session_icon.getY();
                iv_dinner_session_iconX = iv_dinner_session_icon.getX();
                iv_dinner_session_iconY = iv_dinner_session_icon.getY();
                iv_supper_session_iconX = iv_supper_session_icon.getX();
                iv_supper_session_iconY = iv_supper_session_icon.getY();
                SessionStatus sessionStatus = Store.getObject(
                        context, Store.SESSION_STATUS, SessionStatus.class);
                if (sessionStatus != null) {
                    rl_openbg.setVisibility(View.GONE);
                    rl_closerestbg.setVisibility(View.GONE);
                    if (size == 4) {
                        iv_lunch_icon.setVisibility(View.GONE);
                        iv_breakfast_icon.setVisibility(View.GONE);
                        iv_dinner_icon.setVisibility(View.GONE);
                        iv_lunch_session_icon.setVisibility(View.VISIBLE);
                        iv_breakfast_session_icon.setVisibility(View.VISIBLE);
                        iv_dinner_session_icon.setVisibility(View.VISIBLE);
                        iv_supper_session_icon.setVisibility(View.VISIBLE);
                    } else {
                        iv_lunch_icon.setVisibility(iv_lunch_icon.getVisibility() != View.VISIBLE ? View.GONE : View.VISIBLE);
                        iv_breakfast_icon.setVisibility(iv_breakfast_icon.getVisibility() != View.VISIBLE ? View.GONE : View.VISIBLE);
                        iv_dinner_icon.setVisibility(iv_dinner_icon.getVisibility() != View.VISIBLE ? View.GONE : View.VISIBLE);
                        iv_lunch_session_icon.setVisibility(View.GONE);
                        iv_breakfast_session_icon.setVisibility(View.GONE);
                        iv_dinner_session_icon.setVisibility(View.GONE);
                        iv_supper_session_icon.setVisibility(View.GONE);
                    }


                    if (iv_lunch_icon.getTag() != null && sessionStatus.getSession_status() == ((Integer) iv_lunch_icon.getTag()).intValue()) {
                        iv_lunch_icon.post(new Runnable() {

                            @Override
                            public void run() {
                                open(iv_lunch_icon);
                            }
                        });

                    } else if (iv_breakfast_icon.getTag() != null && sessionStatus.getSession_status() == ((Integer) iv_breakfast_icon.getTag()).intValue()) {
                        iv_breakfast_icon.post(new Runnable() {

                            @Override
                            public void run() {
                                open(iv_breakfast_icon);
                            }
                        });

                    } else if (iv_dinner_icon.getTag() != null && sessionStatus.getSession_status() == ((Integer) iv_dinner_icon.getTag()).intValue()) {
                        iv_dinner_icon.post(new Runnable() {

                            @Override
                            public void run() {
                                open(iv_dinner_icon);
                            }
                        });

                    } else if (iv_lunch_session_icon.getTag() != null && sessionStatus.getSession_status() == ((Integer) iv_lunch_session_icon.getTag()).intValue()) {
                        iv_lunch_session_icon.post(new Runnable() {

                            @Override
                            public void run() {
                                open(iv_lunch_session_icon);
                            }
                        });

                    } else if (iv_breakfast_session_icon.getTag() != null && sessionStatus.getSession_status() == ((Integer) iv_breakfast_session_icon.getTag()).intValue()) {
                        iv_breakfast_session_icon.post(new Runnable() {

                            @Override
                            public void run() {
                                open(iv_breakfast_session_icon);
                            }
                        });

                    } else if (iv_dinner_session_icon.getTag() != null && sessionStatus.getSession_status() == ((Integer) iv_dinner_session_icon.getTag()).intValue()) {
                        iv_dinner_session_icon.post(new Runnable() {

                            @Override
                            public void run() {
                                open(iv_dinner_session_icon);
                            }
                        });

                    } else if (iv_supper_session_icon.getTag() != null && sessionStatus.getSession_status() == ((Integer) iv_supper_session_icon.getTag()).intValue()) {
                        iv_supper_session_icon.post(new Runnable() {

                            @Override
                            public void run() {
                                open(iv_supper_session_icon);
                            }
                        });

                    }
                    rl_slideUnlockView
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                }
                            });
                    mSettingView.initOptionsSessionOpen();
                } else {
//					if(Store.getLong(context, Store.BUSINESS_DATE) > 0L){
//						rl_openbg.setVisibility(View.GONE);
//						rl_closerestbg.setVisibility(View.VISIBLE);
//						if(size == 4){
//							iv_lunch_icon.setVisibility(View.GONE);
//							iv_breakfast_icon.setVisibility(View.GONE);
//							iv_dinner_icon.setVisibility(View.GONE);
//							iv_lunch_session_icon.setVisibility(View.VISIBLE);
//							iv_breakfast_session_icon.setVisibility(View.VISIBLE);
//							iv_dinner_session_icon.setVisibility(View.VISIBLE);
//							iv_supper_session_icon.setVisibility(View.VISIBLE);
//						} else {
//							iv_lunch_icon.setVisibility(iv_lunch_icon.getVisibility() != View.VISIBLE ? View.GONE : View.VISIBLE);
//							iv_breakfast_icon.setVisibility(iv_breakfast_icon.getVisibility() != View.VISIBLE ? View.GONE : View.VISIBLE);
//							iv_dinner_icon.setVisibility(iv_dinner_icon.getVisibility() != View.VISIBLE ? View.GONE : View.VISIBLE);
//							iv_lunch_session_icon.setVisibility(View.GONE);
//							iv_breakfast_session_icon.setVisibility(View.GONE);
//							iv_dinner_session_icon.setVisibility(View.GONE);
//							iv_supper_session_icon.setVisibility(View.GONE);
//						}
//					}else{
                    if (size == 4) {
                        iv_lunch_icon.setVisibility(View.GONE);
                        iv_breakfast_icon.setVisibility(View.GONE);
                        iv_dinner_icon.setVisibility(View.GONE);

                        rl_lunch_bg.setVisibility(View.GONE);
                        rl_breakfast_bg.setVisibility(View.GONE);
                        rl_dinner_bg.setVisibility(View.GONE);

                        iv_lunch_session_icon.setVisibility(View.INVISIBLE);
                        iv_breakfast_session_icon.setVisibility(View.INVISIBLE);
                        iv_dinner_session_icon.setVisibility(View.INVISIBLE);
                        iv_supper_session_icon.setVisibility(View.INVISIBLE);

                        rl_lunch_session_bg.setVisibility(View.INVISIBLE);
                        rl_breakfast_session_bg.setVisibility(View.INVISIBLE);
                        rl_dinner_session_bg.setVisibility(View.INVISIBLE);
                        rl_supper_session_bg.setVisibility(View.INVISIBLE);
                    } else {
                        iv_lunch_icon.setVisibility(View.INVISIBLE);
                        iv_breakfast_icon.setVisibility(View.INVISIBLE);
                        iv_dinner_icon.setVisibility(View.INVISIBLE);

                        rl_lunch_bg.setVisibility(View.INVISIBLE);
                        rl_breakfast_bg.setVisibility(View.INVISIBLE);
                        rl_dinner_bg.setVisibility(View.INVISIBLE);

                        iv_lunch_session_icon.setVisibility(View.GONE);
                        iv_breakfast_session_icon.setVisibility(View.GONE);
                        iv_dinner_session_icon.setVisibility(View.GONE);
                        iv_supper_session_icon.setVisibility(View.GONE);

                        rl_lunch_session_bg.setVisibility(View.GONE);
                        rl_breakfast_session_bg.setVisibility(View.GONE);
                        rl_dinner_session_bg.setVisibility(View.GONE);
                        rl_supper_session_bg.setVisibility(View.GONE);

                    }

                    rl_openbg.setVisibility(View.VISIBLE);
                    rl_closerestbg.setVisibility(View.GONE);
//					}
                    mSettingView.initOptionsNoSessionOpen();
                }
            }
        });
        // 系统初始化工作
        App.instance.startHttpServer();



        App.instance.startUDPService(App.UDP_INDEX_POS, App.instance.getRevenueCenter().getRevName(), new UdpServiceCallBack() {
            @Override
            public void callBack(final UdpMsg udpMsg) {
                RxBus.getInstance().post("RECEIVE_IP_ACTION", udpMsg);
            }
        });
        observable = RxBus.getInstance().register(RxBus.RX_MSG_1);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer object) {
                if (object != null) {
                    if (object.intValue() == 1) {
                        ((TextView) findViewById(R.id.tv_userName)).setText(App.instance
                                .getUser().getFirstName()
                                + "."
                                + App.instance.getUser().getLastName());
                    }
                } else if (App.getTopActivity() instanceof OpenRestaruant && Store.getObject(
                        context, Store.SESSION_STATUS, SessionStatus.class) != null) {
                    UIHelp.startSoredCardActivity(context);
                }
            }
        });

        observable1 = RxBus.getInstance().register("open_drawer");
        observable1.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object object) {
                if (App.instance.getSessionStatus() == null) {
                    UIHelp.showShortToast(context, "Please open the session!");
                    return;
                }
                if (App.getTopActivity() == OpenRestaruant.this)
                    verifyDialog.show(MainPage.HANDLER_MSG_OBJECT_OPEN_DRAWER, null);
            }
        });

        downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter downFilter = new IntentFilter();
        downFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downReceiver, downFilter);
//		if (!App.instance.getXmppThread().isAlive()) {
//			App.instance.getXmppThread().start();
//		}
        initBugseeModifier();
    }

    private void initBugseeModifier() {
        Restaurant restaurant = RestaurantSQL.getRestaurant();
        if (restaurant != null) {
            BugseeHelper.setEmail(restaurant.getEmail());
            BugseeHelper.setAttribute("restaurant_id", restaurant.getId());
            BugseeHelper.setAttribute("restaurant_company_id", restaurant.getCompanyId());
            BugseeHelper.setAttribute("restaurant_address", restaurant.getAddressPrint());
            BugseeHelper.setAttribute("restaurant_country", restaurant.getCountry());
            BugseeHelper.setAttribute("restaurant_city", restaurant.getCity());
        }

        String employeeId = Store.getString(context, Store.EMPLOYEE_ID);
        BugseeHelper.setAttribute("employee_id", employeeId);

//        throw new NullPointerException("Test Crash");
    }

    @Override
    protected void onStart() {
//		setDateView();
//		registerReceiver(receiver, filter);
        super.onStart();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private void intSessionView() {
        rl_lunch_bg = (RelativeLayout) findViewById(R.id.rl_lunch_bg);
        rl_breakfast_bg = (RelativeLayout) findViewById(R.id.rl_breakfast_bg);
        rl_dinner_bg = (RelativeLayout) findViewById(R.id.rl_dinner_bg);

        iv_lunch_icon = (ImageView) findViewById(R.id.iv_lunch_icon);
        iv_breakfast_icon = (ImageView) findViewById(R.id.iv_breakfast_icon);
        iv_dinner_icon = (ImageView) findViewById(R.id.iv_dinner_icon);
        rl_center_bg = (RelativeLayout) findViewById(R.id.rl_center_bg);

        rl_lunch_session_bg = (RelativeLayout) findViewById(R.id.rl_lunch_session_bg);
        rl_breakfast_session_bg = (RelativeLayout) findViewById(R.id.rl_breakfast_session_bg);
        rl_dinner_session_bg = (RelativeLayout) findViewById(R.id.rl_dinner_session_bg);
        rl_supper_session_bg = (RelativeLayout) findViewById(R.id.rl_supper_session_bg);
        iv_lunch_session_icon = (ImageView) findViewById(R.id.iv_lunch_session_icon);
        iv_breakfast_session_icon = (ImageView) findViewById(R.id.iv_breakfast_session_icon);
        iv_dinner_session_icon = (ImageView) findViewById(R.id.iv_dinner_session_icon);
        iv_supper_session_icon = (ImageView) findViewById(R.id.iv_supper_session_icon);

        if (App.instance.countryCode == ParamConst.CHINA) {
            rl_breakfast_bg.setBackgroundResource(R.drawable.breakfast_bg_zh);
            rl_lunch_bg.setBackgroundResource(R.drawable.lunch_bg_zh);
            rl_dinner_bg.setBackgroundResource(R.drawable.dinner_bg_zh);
            rl_breakfast_session_bg.setBackgroundResource(R.drawable.breakfast_bg_zh);
            rl_lunch_session_bg.setBackgroundResource(R.drawable.lunch_bg_zh);
            rl_dinner_session_bg.setBackgroundResource(R.drawable.dinner_bg_zh);
            rl_supper_session_bg.setBackgroundResource(R.drawable.supper_bg_zh);
        }

        if (size == 4) {
            rl_lunch_bg.setVisibility(View.GONE);
            rl_breakfast_bg.setVisibility(View.GONE);
            rl_dinner_bg.setVisibility(View.GONE);
            iv_lunch_icon.setVisibility(View.GONE);
            iv_breakfast_icon.setVisibility(View.GONE);
            iv_dinner_icon.setVisibility(View.GONE);
            rl_center_bg.setVisibility(View.INVISIBLE);
            rl_lunch_session_bg.setVisibility(View.VISIBLE);
            rl_breakfast_session_bg.setVisibility(View.VISIBLE);
            rl_dinner_session_bg.setVisibility(View.VISIBLE);
            rl_supper_session_bg.setVisibility(View.VISIBLE);
            iv_lunch_session_icon.setVisibility(View.VISIBLE);
            iv_breakfast_session_icon.setVisibility(View.VISIBLE);
            iv_dinner_session_icon.setVisibility(View.VISIBLE);
            iv_supper_session_icon.setVisibility(View.VISIBLE);
            iv_lunch_session_icon.setOnTouchListener(this);
            iv_breakfast_session_icon.setOnTouchListener(this);
            iv_dinner_session_icon.setOnTouchListener(this);
            iv_supper_session_icon.setOnTouchListener(this);

        } else {
            rl_lunch_bg.setVisibility(View.VISIBLE);
            rl_breakfast_bg.setVisibility(View.VISIBLE);
            rl_dinner_bg.setVisibility(View.VISIBLE);
            iv_lunch_icon.setVisibility(View.VISIBLE);
            iv_breakfast_icon.setVisibility(View.VISIBLE);
            iv_dinner_icon.setVisibility(View.VISIBLE);
            rl_center_bg.setVisibility(View.GONE);
            rl_lunch_session_bg.setVisibility(View.GONE);
            rl_breakfast_session_bg.setVisibility(View.GONE);
            rl_dinner_session_bg.setVisibility(View.GONE);
            rl_supper_session_bg.setVisibility(View.GONE);
            iv_lunch_session_icon.setVisibility(View.GONE);
            iv_breakfast_session_icon.setVisibility(View.GONE);
            iv_dinner_session_icon.setVisibility(View.GONE);
            iv_supper_session_icon.setVisibility(View.GONE);
            iv_lunch_icon.setOnTouchListener(this);
            iv_breakfast_icon.setOnTouchListener(this);
            iv_dinner_icon.setOnTouchListener(this);
        }
    }

    private void setDateView() {
        PreciseTimeUtil preciseTimeUtil = new PreciseTimeUtil(this);
        ((TextView) findViewById(R.id.tv_now_year)).setText(preciseTimeUtil
                .getYear());
        ((TextView) findViewById(R.id.tv_now_month)).setText(preciseTimeUtil
                .getMonth());
        ((TextView) findViewById(R.id.tv_now_day)).setText(preciseTimeUtil
                .getDay());
        ((TextView) findViewById(R.id.tv_now_week)).setText(preciseTimeUtil
                .getWeek());
        ((TextView) findViewById(R.id.tv_now_hour)).setText(preciseTimeUtil
                .getHour());

        ((TextView) findViewById(R.id.tv_now_min)).setText(preciseTimeUtil
                .getMin());
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.instance.showWelcomeToSecondScreen();
        if (mDrawerLayout.isDrawerOpen(mSettingView)) {
            mDrawerLayout.closeDrawer(Gravity.END);
        }
        setDateView();
        registerReceiver(receiver, filter);
        Map<String, Integer> map = App.instance.getPushMsgMap();
        if (!map.isEmpty()) {
            int num = 0;
            Set<String> key = map.keySet();
            for (Iterator it = key.iterator(); it.hasNext(); ) {
                String s = (String) it.next();
                num = num + map.get(s);
            }
            if (num != 0) {
                tv_msgnum.setVisibility(View.VISIBLE);
                tv_msgnum.setText(num + "");
            }

        } else {
            tv_msgnum.setVisibility(View.GONE);
        }
        SessionStatus sessionStatus = Store.getObject(
                context, Store.SESSION_STATUS, SessionStatus.class);

        if (sessionStatus == null) {
            mSettingView.initOptionsNoSessionOpen();
        } else {
            mSettingView.initOptionsSessionOpen();
        }
        doubleBackToExitPressedOnce = false;

        if (sessionStatus != null) {
            //check session data sync
            long now = (new Date()).getTime();
            int hh = TimeUtil.getHourDifference(now, sessionStatus.getTime());
            if (Math.abs(hh) >= 12) {
                DialogFactory.commonTwoBtnDialog(context, context.getResources().getString(R.string.warning),
                        context.getResources().getString(R.string.session_time_out),
                        context.getResources().getString(R.string.cancel),
                        context.getResources().getString(R.string.ok),
                        null,
                        new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                //handler.sendMessage(handler.obtainMessage(OPEN_RESTAURANT, null));
                                //Close Session

                            }
                        }
                );
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            setDateView();
        }
    };

    //  open session
    private boolean open(View view) {
        if (!ButtonClickTimer.canClick(view))
            return false;
        boolean result = true;
        view.setX(iv_restaurantX + rl_slideUnlockViewX);
        view.setY(iv_restaurantY + rl_slideUnlockViewY);
        SessionStatus sessionStatus = Store.getObject(context,
                Store.SESSION_STATUS, SessionStatus.class);
        int index = ((Integer) view.getTag()).intValue();
        if (sessionStatus != null && sessionStatus.getSession_status() == index) {
            result = false;
            App.instance.setSessionStatus(sessionStatus);
        } else {
            SessionStatus sessionStatus2 = new SessionStatus(
                    index,
                    System.currentTimeMillis());
            Store.saveObject(context, Store.SESSION_STATUS, sessionStatus2);
            App.instance.setSessionStatus(sessionStatus2);
        }
        switch (view.getId()) {
            case R.id.iv_lunch_icon: {
                BugseeHelper.event("Open Session lunch");
                rl_lunch_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_breakfast_icon: {
                BugseeHelper.event("Open Session breakfast");
                rl_breakfast_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_dinner_icon: {
                BugseeHelper.event("Open Session dinner");
                rl_dinner_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_lunch_session_icon: {

                rl_lunch_session_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_breakfast_session_icon: {

                rl_breakfast_session_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_dinner_session_icon: {

                rl_dinner_session_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_supper_session_icon: {

                rl_supper_session_bg.setOnClickListener(this);
                break;
            }
            default:
                break;
        }
        iv_breakfast_icon.setOnTouchListener(null);
        iv_lunch_icon.setOnTouchListener(null);
        iv_dinner_icon.setOnTouchListener(null);
        iv_breakfast_session_icon.setOnTouchListener(null);
        iv_lunch_session_icon.setOnTouchListener(null);
        iv_dinner_session_icon.setOnTouchListener(null);
        iv_supper_session_icon.setOnTouchListener(null);
        view.setOnTouchListener(this);
        return result;
    }

    private void openAction(final View view) {
        BugseeHelper.buttonClicked(view);
        view.setX(iv_restaurantX + rl_slideUnlockViewX);
        view.setY(iv_restaurantY + rl_slideUnlockViewY);
        switch (view.getId()) {
            case R.id.iv_lunch_icon: {
                BugseeHelper.buttonClicked("Open Action Lunch");
                rl_lunch_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_breakfast_icon: {
                BugseeHelper.buttonClicked("Open Action Breakfast");
                rl_breakfast_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_dinner_icon: {
                BugseeHelper.buttonClicked("Open Action Dinner");
                rl_dinner_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_lunch_session_icon: {
                rl_lunch_session_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_breakfast_session_icon: {
                rl_breakfast_session_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_dinner_session_icon: {
                rl_dinner_session_bg.setOnClickListener(this);
                break;
            }
            case R.id.iv_supper_session_icon: {
                rl_supper_session_bg.setOnClickListener(this);
                break;
            }
            default:
                break;
        }
        iv_breakfast_icon.setOnTouchListener(null);
        iv_lunch_icon.setOnTouchListener(null);
        iv_dinner_icon.setOnTouchListener(null);
        iv_breakfast_session_icon.setOnTouchListener(null);
        iv_lunch_session_icon.setOnTouchListener(null);
        iv_dinner_session_icon.setOnTouchListener(null);
        iv_supper_session_icon.setOnTouchListener(null);
        view.setOnTouchListener(this);
    }

    /* close session */
    private void close(final String actual) {

        int timely = Store.getInt(App.instance, Store.REPORT_ORDER_TIMELY);
//		if(!NetworkUtils.isNetworkAvailable(context)&&timely==1){
//
//			UIHelp.showShortToast(context, context.getResources().getString(R.string.network_connected));
//
//		}
        Map<String, Object> parameters = new HashMap<String, Object>();
        final SessionStatus sessionStatus = Store.getObject(
                context, Store.SESSION_STATUS, SessionStatus.class);
        final long bizDate = App.instance.getBusinessDate().longValue();
        final CloudSyncJobManager cloudSync = App.instance.getSyncJob();

        parameters.put("session",
                Store.getObject(context, Store.SESSION_STATUS, SessionStatus.class));
        SyncCentre.getInstance().sendSessionClose(context, parameters);
        if (size == 4) {
            iv_lunch_session_icon.setOnTouchListener(this);
            iv_breakfast_session_icon.setOnTouchListener(this);
            iv_dinner_session_icon.setOnTouchListener(this);
            iv_supper_session_icon.setOnTouchListener(this);
        } else {
            iv_lunch_icon.setOnTouchListener(this);
            iv_breakfast_icon.setOnTouchListener(this);
            iv_dinner_icon.setOnTouchListener(this);
        }
        // remove jobs on KOTJobManager
        App.instance.getKdsJobManager().clear();
        mSettingView.initOptionsNoSessionOpen();

        //when close session, we need save sales data
//		final PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(
//				context);
//		printerLoadingDialog.setTitle(context.getResources().getString(R.string.save_print_sales));
//		printerLoadingDialog.show();

        //generate XReport data

        final long closeTime = System.currentTimeMillis();
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (sessionStatus == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissPrinterLoadingDialog();
                        }
                    });
                    return;
                }
                SyncCentre.getInstance().posCloseSession(App.instance);
                //sync finished Order info in current session to cloud
                LogUtil.e("测试", "11");
                List<Order> orders = OrderSQL.getFinishedOrdersBySession(
                        sessionStatus, bizDate, closeTime);
                List<Order> subPosOrders = CPOrderSQL.getFinishedOrdersBySession(
                        sessionStatus, bizDate, closeTime);
                LogUtil.e("测试", "22");
                if (!orders.isEmpty()) {
                    for (Order order : orders) {
                        // sync order to cloud
                        if (cloudSync != null) {
                            cloudSync.syncOrderInfo(order.getId(),
                                    App.instance.getRevenueCenter().getId(),
                                    bizDate);

                        }
                    }
                    GeneralSQL.deleteKioskHoldOrderInfoBySession(sessionStatus, App.instance.getBusinessDate());
                    final Map<String, Object> xReportInfo
                            = ReportObjectFactory.getInstance().getXReportInfo(bizDate, sessionStatus, actual);
                    if (App.instance.getSystemSettings().isPrintWhenCloseSession())
                        sendXPrintData(xReportInfo, bizDate,
                                CommonUtil.getReportType(context, sessionStatus.getSession_status()),
                                sessionStatus);
                    //sync X-Report to cloud
                    if (cloudSync != null) {
                        int revenueId = App.instance.getRevenueCenter().getId();
                        RevenueCenter revenueCenter = RevenueCenterSQL.getRevenueCenterById(revenueId);
                        cloudSync.syncXReport(xReportInfo,
                                revenueId,
                                bizDate,
                                sessionStatus, revenueCenter.getCurrentReportNo());
                        cloudSync.syncOpenOrCloseSessionAndRestaurant(App.instance
                                        .getRevenueCenter().getId(), bizDate,
                                sessionStatus, CloudSyncJobManager.CLOSE_SESSION);
                        LogUtil.e("测试", "44");
                    }

                }

                if (subPosOrders != null && subPosOrders.size() > 0) {
                    for (Order order : subPosOrders) {
                        // sync order to cloud
                        if (cloudSync != null) {
                            cloudSync.syncOrderInfo(order.getId(),
                                    App.instance.getRevenueCenter().getId(),
                                    bizDate);

                        }
                    }
                    GeneralSQL.deleteKioskHoldOrderInfoBySession(sessionStatus, App.instance.getBusinessDate());
                    final Map<String, Object> xReportInfo
                            = ReportObjectFactory.getInstance().getXReportInfo(bizDate, sessionStatus, actual);
                    if (App.instance.getSystemSettings().isPrintWhenCloseSession())
                        sendXPrintData(xReportInfo, bizDate,
                                CommonUtil.getReportType(context, sessionStatus.getSession_status()),
                                sessionStatus);
                    //sync X-Report to cloud
                    if (cloudSync != null) {
                        int revenueId = App.instance.getRevenueCenter().getId();
                        RevenueCenter revenueCenter = RevenueCenterSQL.getRevenueCenterById(revenueId);
                        cloudSync.syncXReport(xReportInfo,
                                revenueId,
                                bizDate,
                                sessionStatus, revenueCenter.getCurrentReportNo());
                        cloudSync.syncOpenOrCloseSessionAndRestaurant(App.instance
                                        .getRevenueCenter().getId(), bizDate,
                                sessionStatus, CloudSyncJobManager.CLOSE_SESSION);
                        LogUtil.e("测试", "44");
                    }

                }

                if (subPosOrders != null && subPosOrders.size() > 0) {
                    for (Order order : subPosOrders) {
                        // sync order to cloud
                        if (cloudSync != null) {
                            cloudSync.syncSubPosOrderInfo(order.getId(),
                                    App.instance.getRevenueCenter().getId(),
                                    bizDate);

                        }
                    }
                }

                List<MultiReportRelation> multiReportRelations = MultiReportRelationSQL.getAllMultiReportRelationBySession(sessionStatus);
                if (multiReportRelations != null && multiReportRelations.size() > 0) {
                    for (MultiReportRelation multiReportRelation : multiReportRelations) {
                        int daySalesId = multiReportRelation.getMainReportId();
                        if (daySalesId <= 0) {
                            continue;
                        }
                        Map<String, Object> map = new HashMap<String, Object>();
                        // day sales report
                        ReportDaySales reportDaySales = ReportDaySalesSQL.getReportDaySales(daySalesId);
                        // open Drawer
                        List<UserOpenDrawerRecord> userOpenDrawerRecords = UserOpenDrawerRecordSQL.getAllUserOpenDrawerRecordByDaySalesId(daySalesId);
                        // tax report
                        ArrayList<ReportDayTax> reportDayTaxs = ReportDayTaxSQL.getAllReportDayTaxByDaySalesId(daySalesId);
                        List<ReportDayPayment> reportDayPayments = ReportDayPaymentSQL.getAllReportDayPaymentByDaySalesId(daySalesId);
                        // plu item reprot
                        ArrayList<ReportPluDayItem> reportPluDayItems = ReportPluDayItemSQL.getAllReportPluDayItemByDaySalesId(daySalesId);

                        // plu modifier report
                        ArrayList<ReportPluDayModifier> reportPluDayModifiers = ReportPluDayModifierSQL.getAllReportPluDayModifierByDaySalesId(daySalesId);
                        // plu combo report
                        ArrayList<ReportPluDayComboModifier> reportPluDayComboModifiers = ReportPluDayComboModifierSQL.getAllReportPluDayComboModifierByDaySalesId(daySalesId);
                        // plu hourly payment report
                        ArrayList<ReportHourly> reportHourlys = ReportHourlySQL.getAllReportHourlysByDaySalesId(daySalesId);
                        map.put("reportDaySales", reportDaySales);
                        map.put("reportDayTaxs", reportDayTaxs);
                        map.put("reportDayPayments", reportDayPayments);
                        map.put("reportPluDayItems", reportPluDayItems);
                        map.put("reportPluDayModifiers", reportPluDayModifiers);
                        map.put("reportHourlys", reportHourlys);
                        map.put("reportPluDayComboModifiers", reportPluDayComboModifiers);
                        map.put("sessionStatus", sessionStatus);
                        map.put("userOpenDrawerRecords", userOpenDrawerRecords);
                        int revenueId = App.instance.getRevenueCenter().getId();
                        RevenueCenter revenueCenter = RevenueCenterSQL.getRevenueCenterById(revenueId);
                        cloudSync.syncXReport(map,
                                revenueId,
                                bizDate,
                                sessionStatus, revenueCenter.getCurrentReportNo());
                        multiReportRelation.setSyncStatus(1);
                        MultiReportRelationSQL.updateMultiReportRelation(multiReportRelation);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissPrinterLoadingDialog();
                        Store.remove(context, Store.SESSION_STATUS);
                        App.instance.setSessionStatus(null);
                        BugseeHelper.trace("Close session", true);
                    }
                });
            }
        }).start();

    }

    String actual = "0.00";

    private void closeAction(final View v) {
        ObjectAnimator animator1 = null;
        ObjectAnimator animator2 = null;
        switch (v.getId()) {
            case R.id.iv_lunch_icon:
                BugseeHelper.event("Close lunch session");
                animator1 = ObjectAnimator
                        .ofFloat(v, "x", v.getX(), iv_lunch_iconX).setDuration(300);
                animator2 = ObjectAnimator
                        .ofFloat(v, "y", v.getY(), iv_lunch_iconY).setDuration(300);
                rl_lunch_bg.setOnClickListener(null);
                break;
            case R.id.iv_breakfast_icon:
                BugseeHelper.event("Close breakfast session");
                animator1 = ObjectAnimator.ofFloat(v, "x", v.getX(),
                        iv_breakfast_iconX).setDuration(200);
                animator2 = ObjectAnimator.ofFloat(v, "y", v.getY(),
                        iv_breakfast_iconY).setDuration(200);
                rl_breakfast_bg.setOnClickListener(null);
                break;
            case R.id.iv_dinner_icon:
                BugseeHelper.event("Close dinner session");
                animator1 = ObjectAnimator.ofFloat(v, "x", v.getX(),
                        iv_dinner_iconX).setDuration(200);
                animator2 = ObjectAnimator.ofFloat(v, "y", v.getY(),
                        iv_dinner_iconY).setDuration(200);
                rl_dinner_bg.setOnClickListener(null);
                break;
            case R.id.iv_lunch_session_icon:
                animator1 = ObjectAnimator
                        .ofFloat(v, "x", v.getX(), iv_lunch_session_iconX).setDuration(300);
                animator2 = ObjectAnimator
                        .ofFloat(v, "y", v.getY(), iv_lunch_session_iconY).setDuration(300);
                rl_lunch_session_bg.setOnClickListener(null);
                break;
            case R.id.iv_breakfast_session_icon:
                animator1 = ObjectAnimator.ofFloat(v, "x", v.getX(),
                        iv_breakfast_session_iconX).setDuration(200);
                animator2 = ObjectAnimator.ofFloat(v, "y", v.getY(),
                        iv_breakfast_session_iconY).setDuration(200);
                rl_breakfast_session_bg.setOnClickListener(null);
                break;
            case R.id.iv_dinner_session_icon:
                animator1 = ObjectAnimator.ofFloat(v, "x", v.getX(),
                        iv_dinner_session_iconX).setDuration(200);
                animator2 = ObjectAnimator.ofFloat(v, "y", v.getY(),
                        iv_dinner_session_iconY).setDuration(200);
                rl_dinner_session_bg.setOnClickListener(null);
                break;
            case R.id.iv_supper_session_icon:
                animator1 = ObjectAnimator.ofFloat(v, "x", v.getX(),
                        iv_supper_session_iconX).setDuration(200);
                animator2 = ObjectAnimator.ofFloat(v, "y", v.getY(),
                        iv_supper_session_iconY).setDuration(200);
                rl_supper_session_bg.setOnClickListener(null);
                break;
            default:
                break;
        }
        zPrinterLoadingDialog.setTitle(context.getResources().getString(R.string.save_print_sales));
        if (!zPrinterLoadingDialog.isShowing()) {
            zPrinterLoadingDialog.show();
        }
        AnimatorSet set = new AnimatorSet();
        animator1.addListener(new AnimatorListenerImpl() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rl_closerestbg.setVisibility(View.VISIBLE);
                if (!ButtonClickTimer.canClick(v))
                    return;

                if (!isShowingActualDialog) {
                    isShowingActualDialog = true;
                    DialogFactory.commonTwoBtnInputDialog(context, false, "Actual in Drawer", "Enter amount of cash in drawer", "CANCEL", "DONE",
                            new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    actual = "0.00";
                                    isShowingActualDialog = false;
                                    closeSessionThread(v);
                                }
                            },
                            new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    EditText editText = (EditText) view;
                                    actual = editText.getText().toString();
                                    isShowingActualDialog = false;
                                    closeSessionThread(v);
                                }
                            });
                }
            }
        });
        set.playTogether(animator1, animator2);
        set.start();
    }


    private void closeSessionThread(final View v) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                SessionStatus sessionStatus = Store.getObject(
                        context, Store.SESSION_STATUS, SessionStatus.class);
                if (sessionStatus == null) {
                    dismissPrinterLoadingDialog();
                    return;
                }
                int canClose = CAN_CLOSE;
                if (App.instance.getCahierPrinter() == null) {
                    handler.sendMessage(handler.obtainMessage(PRINTER_UNLINK, v));
                    return;
                }
                if (App.instance.isRevenueKiosk()) {
                    long nowTime = System.currentTimeMillis();
                    List<Order> orderList = OrderSQL.getUnpaidOrdersBySession(sessionStatus, App.instance.getBusinessDate(), nowTime);
                    if (!orderList.isEmpty()) {
                        for (final Order order : orderList) {
                            List<OrderDetail> orderDetailsUnIncludeVoid = OrderDetailSQL
                                    .getOrderDetails(order.getId());
                            if (!orderDetailsUnIncludeVoid.isEmpty()) {
                                canClose = CAN_NOT_CLOSE;
                                break;
                            } else {
//										OrderSQL.updateOrderStatus(ParamConst.ORDER_STATUS_FINISHED, order.getId().intValue());
//										OrderSQL.deleteOrder(order);
                                GeneralSQL.deleteOrderAndInforByOrderId(order.getId());
                            }
                        }
                    }
                    if (canClose == CAN_CLOSE) {
                        List<SubPosBean> subPosBeans = SubPosBeanSQL.getAllOpenSubPosBean();
                        if (subPosBeans != null && subPosBeans.size() > 0) {
                            canClose = CAN_NOT_CLOSE_SUB;
                        }
                    }
                } else {
                    List<TableInfo> tables = TableInfoSQL.getAllUsedTables();
                    for (TableInfo table : tables) {
                        Order order = OrderSQL
                                .getLastOrderatTabel(table.getPosId().intValue());
                        if (order != null
                                && order.getOrderStatus() != ParamConst.ORDER_STATUS_FINISHED) {
                            List<OrderDetail> orderDetailsIncludeVoid = OrderDetailSQL
                                    .getAllOrderDetailsByOrder(order);
                            if (orderDetailsIncludeVoid.isEmpty()) {
                                OrderSQL.deleteOrder(order);
                                OrderBillSQL
                                        .deleteOrderBillByOrder(order);
                                table.setStatus(ParamConst.TABLE_STATUS_IDLE);
//										TablesSQL.updateTables(table);
                                TableInfoSQL.updateTables(table);
                            } else {
                                List<OrderDetail> orderDetailsUnIncludeVoid = OrderDetailSQL
                                        .getOrderDetails(order.getId());
                                if (orderDetailsUnIncludeVoid.isEmpty()) {
                                    order.setOrderStatus(ParamConst.ORDER_STATUS_FINISHED);
                                    OrderSQL.update(order);
                                    table.setStatus(ParamConst.TABLE_STATUS_IDLE);
//											TablesSQL.updateTables(table);
                                    TableInfoSQL.updateTables(table);
                                } else {
                                    canClose = CAN_NOT_CLOSE;
                                }
                            }
                        } else {
                            table.setStatus(ParamConst.TABLE_STATUS_IDLE);
//									TablesSQL.updateTables(table);
                            TableInfoSQL.updateTables(table);
                        }
                    }
                }
                if (canClose == CAN_CLOSE) {
                    handler.sendMessage(handler.obtainMessage(canClose, actual));
                } else {
                    handler.sendMessage(handler.obtainMessage(canClose, v));
                }
            }
        }).start();
    }

    private void sendXPrintData(Map<String, Object> xReport, long businessDate,
                                String reportType,
                                SessionStatus sessionStatus) {
        ReportDaySales reportDaySales = (ReportDaySales) xReport.get("reportDaySales");

        if (reportDaySales == null) {
            return;
        }
        String bizDate = TimeUtil.getPrintingDate(businessDate);
        ArrayList<ReportDayTax> reportDayTaxs = (ArrayList<ReportDayTax>) xReport.get("reportDayTaxs");
        List<ReportDayPayment> reportDayPayments = (List<ReportDayPayment>) xReport.get("reportDayPayments");
        ArrayList<ReportPluDayItem> reportPluDayItems = (ArrayList<ReportPluDayItem>) xReport.get("reportPluDayItems");
        ArrayList<ReportDayPromotion> reportDayPromotions = (ArrayList<ReportDayPromotion>) xReport.get("reportDayPromotions");
        //	ArrayList<Promotion> promotions = PromotionSQL.getAllPromotion();
        //add to filter ENT and VOID item in PLU items
//		ArrayList<ReportPluDayItem> filteredPluDayItems = ReportObjectFactory
//			.getInstance().getPLUItemWithoutVoidEnt(reportPluDayItems);


        //	List<ReportDayTax> reportSvgDayTaxs = ReportObjectFactory.getInstance().loadReportSvgDayTax(businessDate,1);
//
        ArrayList<ReportPluDayModifier> reportPluDayModifiers = (ArrayList<ReportPluDayModifier>) xReport.get("reportPluDayModifiers");
        ArrayList<ReportHourly> reportHourlys = (ArrayList<ReportHourly>) xReport.get("reportHourlys");
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
                                + App.instance.getUser().getLastName(), null, bizDate, App.instance.getSystemSettings().getTrainType());

        PrinterDevice cashierPrinter = App.instance.getCahierPrinter();

        // Open Cash drawer
        App.instance.kickOutCashDrawer(cashierPrinter);

        // sales report
        App.instance.remotePrintDaySalesReport(reportType, cashierPrinter,
                title, reportDaySales, reportDayTaxs, reportDayPayments, ReportObjectFactory.getInstance().loadXReportUserOpenDrawerbySessionStatus(businessDate, sessionStatus), null);

        if (reportDayPromotions != null && reportDayPromotions.size() > 0) {
            App.instance.remotePrintPromotionReport(reportType, cashierPrinter, title,
                    reportDayPromotions);
        }

//		try {
//			Thread.sleep(5000);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
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
        if (App.instance.getSystemSettings().isPrintPluCategory())
            App.instance.remotePrintSummaryAnalysisReport(reportType,
                    cashierPrinter, title, reportPluDayItems,
                    reportPluDayModifiers, itemMainCategorys, itemCategorys);
        if (App.instance.getSystemSettings().isPrintHourlyPayment())
            // hourly sales
            App.instance.remotePrintHourlyReport(reportType, cashierPrinter, title,
                    reportHourlys);
//		if(App.countryCode == ParamConst.CHINA) {
//		if(reportPluDayModifiers != null && reportPluDayModifiers.size() > 0)
//		// modifier detail analysis
//		App.instance.remotePrintModifierDetailAnalysisReport(reportType,
//				cashierPrinter, title,
//				reportPluDayModifiers, ModifierSQL.getModifierCategorys(App.instance.getRevenueCenter().getRestaurantId().intValue()));
//		}
//		ArrayList<ReportPluDayModifier> reportPluDayComboModifiers = new ArrayList<ReportPluDayModifier>();
//		for(ReportPluDayModifier reportPluDayModifier : reportPluDayModifiers){
//			if(reportPluDayModifier.getComboItemId() != 0){
//				reportPluDayComboModifiers.add(reportPluDayModifier);
//			}
//		}
//		App.instance.remotePrintComboDetailAnalysisReport(reportType, cashierPrinter, title,
//				reportPluDayComboModifiers);
    }

    private void sendPrintData(long businessDate, String reportType) {
        ReportDaySales reportDaySales = ReportObjectFactory.getInstance()
                .loadReportDaySales(businessDate);
        if (reportDaySales == null) {
            handler.sendMessage(handler.obtainMessage(PROGRESS_PRINT_Z_END, null));
            return;
        }
        String bizDate = TimeUtil.getPrintingDate(businessDate);
        List<ReportDayTax> reportSvgDayTaxs = ReportObjectFactory
                .getInstance().loadReportDayTax(businessDate);
        LoginResult loginResult = CoreData.getInstance().getLoginResult();
        List<ReportDayTax> reportDayTaxs = ReportObjectFactory
                .getInstance().loadReportDayTax(businessDate);
        List<ReportDayPayment> reportDayPayments = ReportObjectFactory.getInstance().loadReportDayPayment(businessDate);
        ArrayList<ReportPluDayItem> reportPluDayItems = ReportObjectFactory
                .getInstance().loadReportPluDayItem(businessDate);
        Map<String, Object> map = ReportObjectFactory.getInstance().loadReportPluDayModifierInfo(businessDate);
        ArrayList<ReportPluDayModifier> reportPluDayModifiers = (ArrayList<ReportPluDayModifier>) map.get("reportPluDayModifiers");

        ArrayList<ReportPluDayComboModifier> reportPluDayComboModifiers = (ArrayList<ReportPluDayComboModifier>) map.get("reportPluDayComboModifiers");
        ArrayList<ReportDayPromotion> reportDayPromotions = ReportObjectFactory.getInstance()
                .loadReportPromotions(businessDate);

        //   ArrayList<Promotion> promotions = PromotionSQL.getAllPromotion();
        ArrayList<ReportHourly> reportHourlys = ReportObjectFactory
                .getInstance().loadReportHourlys(businessDate);


//		List<ReportVoidItem> reportVoidItems = ReportObjectFactory
//				.getInstance().loadReportVoidItem(businessDate);
//		List<ReportEntItem> reportEntItems = ReportObjectFactory
//				.getInstance().loadReportEntItem(businessDate);


        ArrayList<ItemCategory> itemCategorys = ItemCategorySQL
                .getAllItemCategory();
        ArrayList<ItemMainCategory> itemMainCategorys = ItemMainCategorySQL
                .getAllItemMainCategory();
        List<ReportSessionSales> reportSessionSalesList = ReportSessionSalesSQL.getAllReportSessionSales(businessDate);
        PrinterTitle title = ObjectFactory.getInstance()
                .getPrinterTitleForReport(
                        App.instance.getRevenueCenter().getId(),
                        "Z" + reportDaySales.getReportNoStr(),
                        App.instance.getUser().getFirstName()
                                + App.instance.getUser().getLastName(), null, bizDate, App.instance.getSystemSettings().getTrainType());

        PrinterDevice cashierPrinter = App.instance.getCahierPrinter();

        // sales report
        App.instance.remotePrintDaySalesReport(reportType, cashierPrinter,
                title, reportDaySales, reportDayTaxs, reportDayPayments,
                ReportObjectFactory.getInstance().loadReportUserOpenDrawerbyBusinessDate(businessDate),
                reportSessionSalesList);
        if (reportDayPromotions != null && reportDayPromotions.size() > 0) {
            App.instance.remotePrintPromotionReport(reportType, cashierPrinter, title,
                    reportDayPromotions);
        }

//		try {
//			Thread.sleep(2000);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
        if (App.instance.getSystemSettings().isPrintPluItem())
            // detail analysis
            App.instance.remotePrintDetailAnalysisReport(reportType,
                    cashierPrinter, title, reportDaySales, reportPluDayItems,
                    reportPluDayModifiers, reportPluDayComboModifiers, itemMainCategorys, itemCategorys);
        if (App.instance.getSystemSettings().isPrintPluCategory())
            App.instance.remotePrintSummaryAnalysisReport(reportType,
                    cashierPrinter, title, reportPluDayItems,
                    reportPluDayModifiers, itemMainCategorys, itemCategorys);
        if (App.instance.getSystemSettings().isPrintHourlyPayment())
            // hourly sales
            App.instance.remotePrintHourlyReport(reportType, cashierPrinter, title,
                    reportHourlys);
        if (reportPluDayModifiers != null && reportPluDayModifiers.size() > 0) {
            if (App.instance.getSystemSettings().isPrintPluModifier())
                App.instance.remotePrintModifierDetailAnalysisReport(reportType,
                        cashierPrinter, title, reportPluDayModifiers,
                        ModifierSQL.getModifierCategorys(App.instance.getRevenueCenter().getRestaurantId().intValue()));
        }
//		if (reportVoidItems != null && reportVoidItems.size() > 0)
//			// Void PLU
//			App.instance.remotePrintVoidItemReport(reportType, cashierPrinter,
//					title, reportVoidItems);
//
//		if (reportEntItems != null && reportEntItems.size() > 0)
//			// Ent PLU
//			App.instance.remotePrintEntItemReport(reportType, cashierPrinter,
//					title, reportEntItems);
        if (App.countryCode == ParamConst.CHINA) {
//		if(reportPluDayModifiers != null && reportPluDayModifiers.size() > 0)
//		// modifier detail analysis
//		App.instance.remotePrintModifierDetailAnalysisReport(reportType,
//				cashierPrinter, title,
//				reportPluDayModifiers, ModifierSQL.getModifierCategorys(App.instance.getRevenueCenter().getRestaurantId().intValue()));
//		}

//		ArrayList<ReportPluDayModifier> reportPluDayComboModifiers = new ArrayList<ReportPluDayModifier>();
//		for(ReportPluDayModifier reportPluDayModifier : reportPluDayModifiers){
//			if(reportPluDayModifier.getComboItemId() != 0){
//				reportPluDayComboModifiers.add(reportPluDayModifier);
//			}
//		if(reportPluDayComboModifiers != null && reportPluDayComboModifiers.size() > 0)
//			App.instance.remotePrintComboDetailAnalysisReport(reportType, cashierPrinter, title,
//					filteredPluDayItems, reportPluDayComboModifiers);
        }
        handler.sendMessage(handler.obtainMessage(PROGRESS_PRINT_Z_END, null));
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);

        switch (v.getId()) {
            //close restaurant
            case R.id.rl_closerestbg: {
                final View vv = v;
                final long business_date = App.instance.getBusinessDate();
                final CloudSyncJobManager cloudSync = App.instance.getSyncJob();

                BugseeHelper.buttonClicked("Close restaurant");

//			final PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(context);
                DialogFactory.commonTwoBtnDialog(this, "",
                        context.getResources().getString(R.string.whether_close_restaurant),
                        context.getResources().getString(R.string.no),
                        context.getResources().getString(R.string.yes), null,
                        new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                //show loading dialog
                                handler.sendMessage(handler.obtainMessage(PROGRESS_PRINT_Z_START, null));

//							// 未Clock Out员工，在close时系统将自动Clock out
//							ArrayList<UserTimeSheet> userTimeSheets = UserTimeSheetSQL
//									.getUserTimeSheetsByBusinessDate(business_date);
//							LogUtil.i("userTimeSheets", userTimeSheets.toString());
//							if (!userTimeSheets.isEmpty()) {
//								for (UserTimeSheet userTimeSheet : userTimeSheets) {
//									if (userTimeSheet.getLogoutTime().longValue() == 0
//											|| userTimeSheet.getStatus() == ParamConst.USERTIMESHEET_STATUS_LOGIN) {
//										userTimeSheet.setLogoutTime(System
//												.currentTimeMillis());
//										userTimeSheet
//												.setStatus(ParamConst.USERTIMESHEET_STATUS_LOGOUT);
//										UserTimeSheetSQL.addUser(userTimeSheet);
//									}
//								}
//							}

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //print ZReport
                                        sendPrintData(business_date, CommonUtil.getReportType(context, 999));

                                    }
                                }).start();
                                vv.setVisibility(View.GONE);
                                rl_openbg.setVisibility(View.VISIBLE);
                                if (size == 4) {
                                    ObjectAnimator anim1X = ObjectAnimator.ofFloat(iv_lunch_session_icon,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim1Y = ObjectAnimator.ofFloat(iv_lunch_session_icon,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim2X = ObjectAnimator.ofFloat(iv_breakfast_session_icon,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim2Y = ObjectAnimator.ofFloat(iv_breakfast_session_icon,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim3X = ObjectAnimator.ofFloat(iv_dinner_session_icon,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim3Y = ObjectAnimator.ofFloat(iv_dinner_session_icon,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim4X = ObjectAnimator.ofFloat(iv_supper_session_icon,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim4Y = ObjectAnimator.ofFloat(iv_supper_session_icon,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator animX1 = ObjectAnimator.ofFloat(rl_lunch_session_bg,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator animY1 = ObjectAnimator.ofFloat(rl_lunch_session_bg,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator animX2 = ObjectAnimator.ofFloat(rl_breakfast_session_bg,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator animY2 = ObjectAnimator.ofFloat(rl_breakfast_session_bg,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator animX3 = ObjectAnimator.ofFloat(rl_dinner_session_bg,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator animY3 = ObjectAnimator.ofFloat(rl_dinner_session_bg,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator animX4 = ObjectAnimator.ofFloat(rl_supper_session_bg,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator animY4 = ObjectAnimator.ofFloat(rl_supper_session_bg,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    AnimatorSet set = new AnimatorSet();
                                    set.playTogether(anim1X, anim1Y, anim2X, anim2Y, anim3X, anim3Y, anim4X, anim4Y,
                                            animX1, animY1, animX2, animY2, animX3, animY3, animX4, animY4);

                                    set.addListener(new AnimatorListenerImpl() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            iv_lunch_icon.post(new Runnable() {

                                                @Override
                                                public void run() {
                                                    iv_lunch_icon.setVisibility(View.INVISIBLE);
                                                    iv_breakfast_icon.setVisibility(View.INVISIBLE);
                                                    iv_dinner_icon.setVisibility(View.INVISIBLE);

                                                    rl_lunch_bg.setVisibility(View.INVISIBLE);
                                                    rl_breakfast_bg.setVisibility(View.INVISIBLE);
                                                    rl_dinner_bg.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    });
                                    set.start();
                                } else {
                                    ObjectAnimator anim1X = ObjectAnimator.ofFloat(iv_lunch_icon,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim1Y = ObjectAnimator.ofFloat(iv_lunch_icon,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim2X = ObjectAnimator.ofFloat(iv_breakfast_icon,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim2Y = ObjectAnimator.ofFloat(iv_breakfast_icon,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim3X = ObjectAnimator.ofFloat(iv_dinner_icon,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator anim3Y = ObjectAnimator.ofFloat(iv_dinner_icon,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator animX1 = ObjectAnimator.ofFloat(rl_lunch_bg,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator animY1 = ObjectAnimator.ofFloat(rl_lunch_bg,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator animX2 = ObjectAnimator.ofFloat(rl_breakfast_bg,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator animY2 = ObjectAnimator.ofFloat(rl_breakfast_bg,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    ObjectAnimator animX3 = ObjectAnimator.ofFloat(rl_dinner_bg,
                                            "scaleX", 1f, 0f).setDuration(300);
                                    ObjectAnimator animY3 = ObjectAnimator.ofFloat(rl_dinner_bg,
                                            "scaleY", 1f, 0f).setDuration(300);
                                    AnimatorSet set = new AnimatorSet();
                                    switch (size) {
                                        case 1:
                                            set.playTogether(anim2X, anim2Y, animX2, animY2);
                                            break;
                                        case 2:
                                            set.playTogether(anim1X, anim1Y, anim3X, anim3Y,
                                                    animX1, animY1, animX3, animY3);
                                            break;
                                        case 3:
                                        default:
                                            set.playTogether(anim1X, anim1Y, anim2X, anim2Y, anim3X, anim3Y,
                                                    animX1, animY1, animX2, animY2, animX3, animY3);
                                            break;
                                    }

                                    set.addListener(new AnimatorListenerImpl() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            iv_lunch_icon.post(new Runnable() {

                                                @Override
                                                public void run() {
                                                    iv_lunch_icon.setVisibility(View.INVISIBLE);
                                                    iv_breakfast_icon.setVisibility(View.INVISIBLE);
                                                    iv_dinner_icon.setVisibility(View.INVISIBLE);

                                                    rl_lunch_bg.setVisibility(View.INVISIBLE);
                                                    rl_breakfast_bg.setVisibility(View.INVISIBLE);
                                                    rl_dinner_bg.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    });
                                    set.start();
                                }
                                //SyncCentre.getInstance().uploadReportInfo(context,business_date);
                                cloudSync.syncZReport(App.instance.getRevenueCenter().getId(), business_date);
                                Store.remove(context, Store.BUSINESS_DATE);
//							App.instance.setBusinessDate(null);
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                                        if (cloudSync != null) {
                                            cloudSync.syncOpenOrCloseSessionAndRestaurant(
                                                    App.instance.getRevenueCenter().getId(),
                                                    business_date,
                                                    null,
                                                    CloudSyncJobManager.CLOSE_RESTAURANT);
                                        }
                                        SyncCentre.getInstance().resetItemDetailStockNum(context);
                                    }
                                }).start();
                            }
                        });


            }
            break;
            //开店
            case R.id.rl_openbg: {
                BugseeHelper.buttonClicked("Open restaurant");
                String value = MobclickAgent.getConfigParams(context, "updateVersion");
                if (App.isOpenLog) {
                    value = MobclickAgent.getConfigParams(context, "updateVersionTest");
                }
                Gson gson = new Gson();
                final VersionUpdate updateInfo = gson.fromJson(value, VersionUpdate.class);
                if (updateInfo != null && App.instance.getAppVersionCode() < updateInfo.getVersionCode()) {
                    if (updateInfo.getForceUpdate() == 1) {
                        DialogFactory.showUpdateVersionDialog(context, updateInfo, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//								SyncCentre.getInstance().downloadApk(updateInfo.getPosDownload());
                                downloadPos(updateInfo);
                            }
                        }, null);
                        return;
                    }
                }
                final View vv = v;
                final Long businessDate = TimeUtil.getNewBusinessDate();
                App.instance.deleteOldPrinterMsg(businessDate);
                String bizYmd = TimeUtil.getYMD(businessDate);
                DialogFactory.commonTwoBtnDialog(context, context.getResources().getString(R.string.open_restaurant),
                        context.getResources().getString(R.string.operation_on) + bizYmd +
                                context.getResources().getString(R.string.is_going_to_start),
                        context.getResources().getString(R.string.cancel),
                        context.getResources().getString(R.string.ok),
                        null,
                        new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                vv.setVisibility(View.GONE);
                                handler.sendMessage(handler.obtainMessage(OPEN_RESTAURANT, null));

                            }
                        });
            }

            break;
            case R.id.iv_setting:
                BugseeHelper.buttonClicked("Setting");
                // SettingWindow settingWindow = new SettingWindow(this,
                // findViewById(R.id.iv_setting));
                // settingWindow.openedInOpenRestaurant();
                if (mDrawerLayout.isDrawerOpen(mSettingView)) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                } else {
                    mDrawerLayout.openDrawer(Gravity.END);
                }
                break;
            case R.id.rl_lunch_bg:
            case R.id.rl_breakfast_bg:
            case R.id.rl_dinner_bg:
            case R.id.rl_lunch_session_bg:
            case R.id.rl_breakfast_session_bg:
            case R.id.rl_dinner_session_bg:
            case R.id.rl_supper_session_bg:
                BugseeHelper.buttonClicked("Open Session");
                System.out.println("====-------000oooooooooo");
                mSettingView.initOptionsSessionOpen();
                App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
                if (App.instance.isRevenueKiosk()) {
                    UIHelp.startMainPageKiosk(context);
                } else {
                    UIHelp.startMainPage(context);
                }

                break;
            default:
                break;
        }
    }

    float mEventDownX, mEventDownY;
    private float initX = -1;
    private float initY = -1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (initX == -1) {
            initX = v.getX();
        }
        if (initY == -1) {
            initY = v.getY();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下
                mEventDownX = event.getRawX();
                mEventDownY = event.getRawY();
                rl_closerestbg.setVisibility(View.GONE);
                switch (v.getId()) {
                    case R.id.iv_lunch_icon:
                        rl_lunch_bg.setOnClickListener(null);
                        break;
                    case R.id.iv_breakfast_icon:
                        rl_breakfast_bg.setOnClickListener(null);
                        break;
                    case R.id.iv_dinner_icon:
                        rl_dinner_bg.setOnClickListener(null);
                        break;
                    case R.id.iv_lunch_session_icon:
                        rl_lunch_bg.setOnClickListener(null);
                        break;
                    case R.id.iv_breakfast_session_icon:
                        rl_breakfast_bg.setOnClickListener(null);
                        break;
                    case R.id.iv_dinner_session_icon:
                        rl_dinner_bg.setOnClickListener(null);
                        break;
                    case R.id.iv_supper_session_icon:
                        rl_dinner_bg.setOnClickListener(null);
                        break;
                    default:
                        break;
                }
                switch (((Integer) v.getTag()).intValue()) {
                    case ParamConst.SESSION_STATUS_BREAKFAST:
                        v.setBackgroundResource(R.drawable.breakfast_drag);
                        break;
                    case ParamConst.SESSION_STATUS_DINNER:
                        v.setBackgroundResource(R.drawable.dinner_drag);
                        break;
                    case ParamConst.SESSION_STATUS_LUNCH:
                        v.setBackgroundResource(R.drawable.lunch_drag);
                        break;
                    case ParamConst.SESSION_STATUS_SUPPER:
                        v.setBackgroundResource(R.drawable.supper_drag);
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE: // 移动
                float dx = event.getRawX() - mEventDownX;
                float dy = event.getRawY() - mEventDownY;
                v.setX(initX + dx);
                v.setY(initY + dy);
                v.invalidate();
                break;
            case MotionEvent.ACTION_UP: // 脱离
                Rect r = new Rect();
                v.getLocalVisibleRect(r);
                switch (((Integer) v.getTag()).intValue()) {
                    case ParamConst.SESSION_STATUS_BREAKFAST:
                        v.setBackgroundResource(R.drawable.breakfast_icon);
                        break;
                    case ParamConst.SESSION_STATUS_DINNER:
                        v.setBackgroundResource(R.drawable.dinner_icon);
                        break;
                    case ParamConst.SESSION_STATUS_LUNCH:
                        v.setBackgroundResource(R.drawable.lunch_icon);
                        break;
                    case ParamConst.SESSION_STATUS_SUPPER:
                        v.setBackgroundResource(R.drawable.supper_icon);
                        break;
                    default:
                        break;
                }
                float x_ = v.getX() + v.getWidth() / 2.0f
                        - rl_slideUnlockView.getX();
                float y_ = v.getY() + v.getHeight() / 2.0f
                        - rl_slideUnlockView.getY();
                if (x_ > 0 && x_ < rl_slideUnlockView.getWidth() && y_ > 0
                        && y_ < rl_slideUnlockView.getHeight()) {
                    if (open(v)) {
                        DialogFactory.commonTwoBtnInputDialog(context, true, context.getString(R.string.start_drawer), context.getString(R.string.enter_amount_of_cash_in_drawer), context.getString(R.string.cancel), context.getString(R.string.done).toUpperCase(),
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openSession();
                                    }
                                },
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EditText editText = (EditText) v;
                                        String start = editText.getText().toString();
                                        ObjectFactory.getInstance().getCashInOut(App.instance.getRevenueCenter(), App.instance.getBusinessDate(), App.instance.getUser(), 2, BH.getBD(start).toString(), "");
                                        openSession();
                                    }
                                });
                    }
                } else {
                    // close(v);
                    closeAction(v);
                }
                initX = -1;
                initY = -1;
                break;
        }
        return true;
    }

    private void openSession() {
        System.out.println("====-------000oooooooooo");
        UIHelp.showShortToast(context, context.getString(R.string.open));
        new Thread(new Runnable() {

            @Override
            public void run() {
                CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                if (cloudSync != null) {
                    cloudSync.syncOpenOrCloseSessionAndRestaurant(
                            App.instance.getRevenueCenter().getId(),
                            App.instance.getBusinessDate(),
                            App.instance.getSessionStatus(),
                            CloudSyncJobManager.OPEN_SESSION);
                }

            }
        }).start();
        App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
        if (App.instance.isRevenueKiosk()) {
            UIHelp.startMainPageKiosk(context);
        } else {
            UIHelp.startMainPage(context);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (observable != null) {
            RxBus.getInstance().unregister(RxBus.RX_MSG_1, observable);
        }
        if (observable1 != null) {
            RxBus.getInstance().unregister("open_drawer", observable1);
        }
        unregisterReceiver(downReceiver);
    }

    @Override
    public void httpRequestAction(int action, Object obj) {
        if (action == ResultCode.DEVICE_NO_PERMIT) {
            if (ButtonClickTimer.canLink(App.instance)) {
                this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        DialogFactory.showOneButtonCompelDialog(context, context.getResources().getString(R.string.warning),
                                ResultCode.getErrorResultStrByCode(context,
                                        ResultCode.DEVICE_NO_PERMIT, null),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        Store.remove(context, Store.SYNC_DATA_TAG);
                                        App.instance
                                                .popAllActivityExceptOne(Welcome.class);
                                    }
                                });
                    }
                });
            }
        }
    }

    private boolean isShowingActualDialog = false;
    private Handler handler = new Handler() {
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case CAN_CLOSE: {
                    String actual = (String) msg.obj;
                    close(actual);
//				if(!isShowingActualDialog) {
//					isShowingActualDialog = true;
//					final View msgView = (View) msg.obj;
//					DialogFactory.commonTwoBtnInputDialog(context, false, "Actual in Drawer", "Enter amount of cash in drawer", "CANCEL", "DONE",
//							new OnClickListener() {
//								@Override
//								public void onClick(View view) {
//									close(msgView, "0.00");
//									isShowingActualDialog = false;
//								}
//							},
//							new OnClickListener() {
//								@Override
//								public void onClick(View view) {
//									EditText editText = (EditText) view;
//									String actual = editText.getText().toString();
//									close(msgView, actual);
//									isShowingActualDialog = false;
//								}
//							});
//				}
                }
                break;
                case CAN_NOT_CLOSE: {
                    final View view = (View) msg.obj;
                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            dismissPrinterLoadingDialog();
                            openAction(view);
                        }
                    });

                    DialogFactory.showOneButtonCompelDialog(context, context.getResources().getString(R.string.warning),
                            context.getResources().getString(R.string.bill_not_closed), new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
                                    if (App.instance.isRevenueKiosk()) {
                                        UIHelp.startMainPageKiosk(context);
                                    } else {
                                        UIHelp.startMainPage(context);
                                    }
                                }
                            });
                    rl_closerestbg.setVisibility(View.GONE);
                }
                break;
                case CAN_NOT_CLOSE_SUB: {
                    final View view = (View) msg.obj;
                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            dismissPrinterLoadingDialog();
                            openAction(view);
                        }
                    });

                    DialogFactory.commonTwoBtnDialog(context, context.getResources().getString(R.string.warning),
                            getString(R.string.subpos_not_closed), getString(R.string.ok), context.getString(R.string.close), null,
                            new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    if (App.instance.isRevenueKiosk()) {
                                        UIHelp.startSubPosManagePage(context);
                                    }
                                }
                            });
                    rl_closerestbg.setVisibility(View.GONE);
                }
                break;
                case PRINTER_UNLINK: {
                    final View view = (View) msg.obj;
                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            openAction(view);
                        }
                    });
                    rl_closerestbg.setVisibility(View.GONE);
                    AlertToDeviceSetting
                            .noKDSorPrinter(context,
                                    context.getResources().getString(R.string.cashier_printer));
                    dismissPrinterLoadingDialog();

                }
                break;
                case PROGRESS_PRINT_Z_START:
                    zPrinterLoadingDialog.setTitle(context.getResources().getString(R.string.printing_close_report));
                    zPrinterLoadingDialog.show();
                    break;

                case PROGRESS_PRINT_Z_END:
//				zPrinterLoadingDialog.dismiss();
                    dismissPrinterLoadingDialog();
                    String value = MobclickAgent.getConfigParams(context, "updateVersion");
                    if (App.isOpenLog) {
                        value = MobclickAgent.getConfigParams(context, "updateVersionTest");
                    }
                    Gson gson = new Gson();
                    final VersionUpdate updateInfo = gson.fromJson(value, VersionUpdate.class);
                    if (updateInfo != null && App.instance.getAppVersionCode() < updateInfo.getVersionCode()) {
                        DialogFactory.showUpdateVersionDialog(context, updateInfo, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//								SyncCentre.getInstance().downloadApk(updateInfo.getPosDownload());
                                downloadPos(updateInfo);
                            }
                        }, null);
                    }
                    break;
                case OPEN_RESTAURANT: {

                    final Long businessDate = TimeUtil.getNewBusinessDate();
                    Store.putLong(context, Store.BUSINESS_DATE, businessDate);
                    Store.putLong(context, Store.LAST_BUSINESSDATE, businessDate);
                    App.instance.setBusinessDate(businessDate);
                    App.instance.setLastBusinessDate(businessDate);

                    rl_closerestbg.setVisibility(View.VISIBLE);
                    if (size == 4) {
                        ObjectAnimator oanim1X = ObjectAnimator.ofFloat(
                                iv_lunch_session_icon, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim1Y = ObjectAnimator.ofFloat(
                                iv_lunch_session_icon, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim2X = ObjectAnimator.ofFloat(
                                iv_breakfast_session_icon, "scaleX", 0f, 1f).setDuration(
                                300);
                        ObjectAnimator oanim2Y = ObjectAnimator.ofFloat(
                                iv_breakfast_session_icon, "scaleY", 0f, 1f).setDuration(
                                300);
                        ObjectAnimator oanim3X = ObjectAnimator.ofFloat(
                                iv_dinner_session_icon, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim3Y = ObjectAnimator.ofFloat(
                                iv_dinner_session_icon, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim4X = ObjectAnimator.ofFloat(
                                iv_supper_session_icon, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim4Y = ObjectAnimator.ofFloat(
                                iv_supper_session_icon, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimX1 = ObjectAnimator.ofFloat(
                                rl_lunch_session_bg, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimY1 = ObjectAnimator.ofFloat(
                                rl_lunch_session_bg, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimX2 = ObjectAnimator.ofFloat(
                                rl_breakfast_session_bg, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimY2 = ObjectAnimator.ofFloat(
                                rl_breakfast_session_bg, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimX3 = ObjectAnimator.ofFloat(
                                rl_dinner_session_bg, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimY3 = ObjectAnimator.ofFloat(
                                rl_dinner_session_bg, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimX4 = ObjectAnimator.ofFloat(
                                rl_supper_session_bg, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimY4 = ObjectAnimator.ofFloat(
                                rl_supper_session_bg, "scaleY", 0f, 1f).setDuration(300);
                        AnimatorSet aset = new AnimatorSet();
                        aset.playTogether(oanim1X, oanim1Y, oanim2X, oanim2Y,
                                oanim3X, oanim3Y, oanim4X, oanim4Y, oanimX1, oanimY1, oanimX2,
                                oanimY2, oanimX3, oanimY3, oanimX4, oanimY4);

                        aset.addListener(new AnimatorListenerImpl() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                setSessionIconStatus(size);
                            }

                        });
                        aset.start();
                    } else {

                        ObjectAnimator oanim1X = ObjectAnimator.ofFloat(
                                iv_lunch_icon, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim1Y = ObjectAnimator.ofFloat(
                                iv_lunch_icon, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim2X = ObjectAnimator.ofFloat(
                                iv_breakfast_icon, "scaleX", 0f, 1f).setDuration(
                                300);
                        ObjectAnimator oanim2Y = ObjectAnimator.ofFloat(
                                iv_breakfast_icon, "scaleY", 0f, 1f).setDuration(
                                300);
                        ObjectAnimator oanim3X = ObjectAnimator.ofFloat(
                                iv_dinner_icon, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanim3Y = ObjectAnimator.ofFloat(
                                iv_dinner_icon, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimX1 = ObjectAnimator.ofFloat(
                                rl_lunch_bg, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimY1 = ObjectAnimator.ofFloat(
                                rl_lunch_bg, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimX2 = ObjectAnimator.ofFloat(
                                rl_breakfast_bg, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimY2 = ObjectAnimator.ofFloat(
                                rl_breakfast_bg, "scaleY", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimX3 = ObjectAnimator.ofFloat(
                                rl_dinner_bg, "scaleX", 0f, 1f).setDuration(300);
                        ObjectAnimator oanimY3 = ObjectAnimator.ofFloat(
                                rl_dinner_bg, "scaleY", 0f, 1f).setDuration(300);
                        AnimatorSet aset = new AnimatorSet();
                        switch (size) {
                            case 1:
                                aset.playTogether(oanim2X, oanim2Y, oanimX2, oanimY2);
                                break;
                            case 2:
                                aset.playTogether(oanim1X, oanim1Y, oanim3X, oanim3Y,
                                        oanimX1, oanimY1, oanimX3, oanimY3);
                                break;
                            case 3:
                                aset.playTogether(oanim1X, oanim1Y, oanim2X, oanim2Y,
                                        oanim3X, oanim3Y, oanimX1, oanimY1, oanimX2,
                                        oanimY2, oanimX3, oanimY3);
                                break;
                            default:
                                aset.playTogether(oanim1X, oanim1Y, oanim2X, oanim2Y,
                                        oanim3X, oanim3Y, oanimX1, oanimY1, oanimX2,
                                        oanimY2, oanimX3, oanimY3);
                                break;
                        }

                        aset.addListener(new AnimatorListenerImpl() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                setSessionIconStatus(size);
                            }

                        });
                        aset.start();


                    }
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            //bob: delete all KOT summary and KOT details

                            KotSummarySQL.deleteAllKotSummary(App.instance.getRevenueCenter().getId());
                            KotItemDetailSQL.deleteAllKotItemDetail(App.instance.getRevenueCenter().getId());
                            KotNotificationSQL.deleteAllKotNotifications(App.instance.getRevenueCenter().getId());
//							TablesSQL.setAllTableIdle();
                            TableInfoSQL.setAllTableIdle();

                            //Alfred POS save data within 3 days
                            GeneralSQL.deleteDataInPosBeforeYesterday(TimeUtil
                                    .getBeforeYesterday(businessDate));
                        }
                    }).start();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            CloudSyncJobManager cloudSync = App.instance.getSyncJob();
                            if (cloudSync != null) {
                                cloudSync.syncOpenOrCloseSessionAndRestaurant(
                                        App.instance.getRevenueCenter().getId(),
                                        App.instance.getBusinessDate(),
                                        null,
                                        CloudSyncJobManager.OPEN_RESTAURANT);
                            }

                        }
                    }).start();
                }

                break;
                case VerifyDialog.DIALOG_DISMISS:

                    break;
                case VerifyDialog.DIALOG_RESPONSE:
                    Map<String, Object> result = (Map<String, Object>) msg.obj;
                    User user = (User) result.get("User");
                    if (result.get("MsgObject").equals(MainPage.HANDLER_MSG_OBJECT_OPEN_DRAWER)) {
                        User openUser = user;
                        ObjectFactory.getInstance().getUserOpenDrawerRecord(App.instance.getRevenueCenter().getRestaurantId().intValue(),
                                App.instance.getRevenueCenter().getId().intValue(),
                                openUser,
                                App.instance.getUser().getId().intValue(),
                                App.instance.getSessionStatus() == null ? -1 : App.instance.getSessionStatus().getSession_status());
//					settingView.openDrawer();
                        App.instance.kickOutCashDrawer(App.instance.getCahierPrinter());
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void setSessionIconStatus(int size) {
        List<Integer> session = App.instance.getLocalRestaurantConfig().getSessionConfigType();
        switch (size) {
            case 1:
                rl_lunch_bg.setVisibility(View.VISIBLE);
                iv_lunch_icon.setVisibility(View.VISIBLE);
                iv_lunch_icon.setTag(session.get(0));
                rl_breakfast_bg.setVisibility(View.INVISIBLE);
                iv_breakfast_icon.setVisibility(View.INVISIBLE);
                rl_dinner_bg.setVisibility(View.INVISIBLE);
                iv_dinner_icon.setVisibility(View.INVISIBLE);
                iv_lunch_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(0)));
                rl_lunch_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(0)));

                break;
            case 2:
                iv_breakfast_icon.setTag(session.get(0));
                rl_breakfast_bg.setVisibility(View.VISIBLE);
                iv_breakfast_icon.setVisibility(View.VISIBLE);
                iv_dinner_icon.setTag(session.get(1));
                rl_dinner_bg.setVisibility(View.VISIBLE);
                iv_dinner_icon.setVisibility(View.VISIBLE);
                rl_lunch_bg.setVisibility(View.INVISIBLE);
                iv_lunch_icon.setVisibility(View.INVISIBLE);
                iv_breakfast_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(0)));
                rl_breakfast_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(0)));
                iv_dinner_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(1)));
                rl_dinner_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(1)));
                break;
            case 3:
                iv_breakfast_icon.setTag(session.get(0));
                rl_breakfast_bg.setVisibility(View.VISIBLE);
                iv_breakfast_icon.setVisibility(View.VISIBLE);
                iv_lunch_icon.setTag(session.get(1));
                rl_lunch_bg.setVisibility(View.VISIBLE);
                iv_lunch_icon.setVisibility(View.VISIBLE);
                iv_dinner_icon.setTag(session.get(2));
                rl_dinner_bg.setVisibility(View.VISIBLE);
                iv_dinner_icon.setVisibility(View.VISIBLE);
                iv_breakfast_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(0)));
                rl_breakfast_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(0)));
                iv_lunch_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(1)));
                rl_lunch_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(1)));
                iv_dinner_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(2)));
                rl_dinner_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(2)));
                break;
            case 4:
                rl_breakfast_session_bg.setVisibility(View.VISIBLE);
                iv_breakfast_session_icon.setVisibility(View.VISIBLE);
                iv_breakfast_session_icon.setTag(session.get(0));
                rl_lunch_session_bg.setVisibility(View.VISIBLE);
                iv_lunch_session_icon.setVisibility(View.VISIBLE);
                iv_lunch_session_icon.setTag(session.get(1));
                rl_dinner_session_bg.setVisibility(View.VISIBLE);
                iv_dinner_session_icon.setVisibility(View.VISIBLE);
                iv_dinner_session_icon.setTag(session.get(2));
                rl_supper_session_bg.setVisibility(View.VISIBLE);
                iv_supper_session_icon.setVisibility(View.VISIBLE);
                iv_supper_session_icon.setTag(session.get(3));

                iv_breakfast_session_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(0)));
                rl_breakfast_session_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(0)));
                iv_lunch_session_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(1)));
                rl_lunch_session_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(1)));
                iv_dinner_session_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(2)));
                rl_dinner_session_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(2)));
                iv_supper_session_icon.setBackgroundResource(SessionImageUtils.SessionImageIconGroup.getImageIconRes(session.get(3)));
                rl_supper_session_bg.setBackgroundResource(SessionImageUtils.SessionImageBgGroup.getImageBgRes(session.get(3)));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OVERLAY_PERMISSION_REQ_CODE:
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//						if (!Settings.canDrawOverlays(this)) {
//							Toast.makeText(OpenRestaruant.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
//						} else {
//							// TODO: 18/1/7 已经授权
//						}
//					}
                    break;
            }
        }
        BaseApplication.postHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mDrawerLayout.isDrawerOpen(mSettingView)) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                    if (resultCode == RESULT_OK) {
                        if (data.getBooleanExtra("refreshSession", false))
                            initView();
                    }
                }
            }
        }, 500);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            FloatActionController.getInstance().stopMonkServer(context);
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));
        BugseeHelper.buttonClicked("Back Exit");

        BaseApplication.postHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void dismissPrinterLoadingDialog() {
        if (zPrinterLoadingDialog != null && zPrinterLoadingDialog.isShowing()) {
            zPrinterLoadingDialog.dismiss();
        }
    }
}
