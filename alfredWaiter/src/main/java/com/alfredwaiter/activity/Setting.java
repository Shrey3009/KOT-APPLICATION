package com.alfredwaiter.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.LanguageManager;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;

import com.alfredbase.view.SlipButton;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.SyncCentre;
import com.alfredwaiter.global.UIHelp;
import com.alfredwaiter.popupwindow.OpenItemWindow;
import com.alfredwaiter.view.MyToggleButton;
import com.alfredwaiter.view.WaiterReloginDialog;

public class Setting extends BaseActivity implements MyToggleButton.OnToggleStateChangeListeren, LanguageManager.LanguageDialogListener {
    public static final int HANDLER_LOGOUT_SUCCESS = 1;
    public static final int HANDLER_LOGOUT_FAILED = 2;
    public static final int TEMPORARY_DISH_ADD_POS_SUCCESS = 3;
    public static final int TEMPORARY_DISH_ADD_POS_FAILED = 4;
    public static final int VIEW_EVENT_DISMISS_OPEN_ITEM_WINDOW = 5;
    public static final int VIEW_EVENT_ADD_ORDER_DETAIL = 6;
    private LinearLayout ll_setting_content;
    private SlipButton sb_kot_notification;
    private SlipButton sb_zone_notification;
    private MyToggleButton sb_top_screen_lock;
    private MyToggleButton mtCrashReporting;
    private TextView tv_version, tv_open_item;
    private WaiterReloginDialog waiterReloginDialog;
    OpenItemWindow openItemWindow;
    Order currentOrder;

    private TextView tv_language;
    private ImageView iv_language;
    private LinearLayout ll_language_setting;
    private AlertDialog alertLanguage;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LOGOUT_SUCCESS:
                    User user = App.instance.getUser();
                    if (user != null) {
                        Store.remove(context, Store.WAITER_USER);
                    }
                    App.instance.popAllActivityExceptOne(EmployeeID.class);
                    UIHelp.startEmployeeID(context);
                    break;
                case TEMPORARY_DISH_ADD_POS_SUCCESS:
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getString(R.string.success)));
                    break;

                case TEMPORARY_DISH_ADD_POS_FAILED:
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getString(R.string.failed)));
                    break;

                case VIEW_EVENT_DISMISS_OPEN_ITEM_WINDOW:
                    dismissOpenItemWindow();
                    break;
                case VIEW_EVENT_ADD_ORDER_DETAIL:
//                    addOrderDetail((OrderDetail) msg.obj);
                    updateOrderDetail((ItemDetail) msg.obj, 1);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        super.initView();
        ScreenSizeUtil.initScreenScale(context);
        setContentView(R.layout.activity_setting);
        openItemWindow = new OpenItemWindow();
        getIntentOrder();
        init();
        setData();
    }

    private HashMap<String, Object> getIntentData() {
        Intent intent = getIntent();
        HashMap<String, Object> map = (HashMap<String, Object>) intent.getSerializableExtra("attrMap");
        return map;
    }


    private void init() {
        initTextTypeFace();
        ll_setting_content = (LinearLayout) findViewById(R.id.ll_setting_content);
//		sb_kot_notification = (SlipButton) findViewById(R.id.sb_kot_notification);
//		sb_zone_notification = (SlipButton) findViewById(R.id.sb_zone_notification);
        sb_top_screen_lock = (MyToggleButton) findViewById(R.id.sb_top_screen_lock);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_open_item = (TextView) findViewById(R.id.tv_open_item);
        mtCrashReporting = (MyToggleButton) findViewById(R.id.mtCrashReporting);
        boolean flag = Store.getBoolean(this, Store.WAITER_SET_LOCK, true);
        if (flag) {
            sb_top_screen_lock.setChecked(true);
        } else {
            sb_top_screen_lock.setChecked(false);
        }

        mtCrashReporting.setChecked(Store.getBoolean(this, Store.BUGSEE_STATUS, true));

        findViewById(R.id.tv_order_history).setOnClickListener(this);
        findViewById(R.id.tv_bump_mob_app).setOnClickListener(this);
        findViewById(R.id.tv_connect_pos).setOnClickListener(this);
        findViewById(R.id.tv_data_sync).setOnClickListener(this);
        findViewById(R.id.tv_reset).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);
        findViewById(R.id.tv_clock).setOnClickListener(this);
        mtCrashReporting.setOnClickListener(this);
        tv_open_item.setOnClickListener(this);

        tv_language = (TextView) findViewById(R.id.tv_language);
        tv_language.setOnClickListener(this);
        tv_language.setText(LanguageManager.getLanguageName(this));

        iv_language = (ImageView) findViewById(R.id.iv_language);
        iv_language.setImageDrawable(LanguageManager.getLanguageFlag(this));

        ll_language_setting = (LinearLayout) findViewById(R.id.ll_language_setting);
        ll_language_setting.setOnClickListener(this);

        //		sb_kot_notification.SetOnChangedListener(new SlipButtonChangeListener() {
//			
//			@Override
//			public void OnChanged(boolean CheckState) {
//				if (CheckState) {
//					//TODO
//				}else {
//					//TODO
//				}
//			}
//		});
//		sb_zone_notification.SetOnChangedListener(new SlipButtonChangeListener() {
//			
//			@Override
//			public void OnChanged(boolean CheckState) {
//				if (CheckState) {
//					//TODO
//				}else {
//					//TODO
//				}
//			}
//		});
    }

    private void setData() {
        ll_setting_content.setVisibility((Integer) getIntentData().get("visibilityMap"));
        tv_version.setText(App.instance.VERSION);
    }

    private void getIntentOrder() {
        Intent intent = getIntent();
        currentOrder = (Order) intent.getExtras().get("order");
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_open_item:
                showOpenItemWindow();
                break;
            case R.id.tv_order_history:
                //TODO
                break;
            case R.id.tv_bump_mob_app:
                //TODO
                break;
            case R.id.tv_connect_pos:
                //TODO
                break;
            case R.id.tv_data_sync:
                //TODO
                break;
            case R.id.tv_reset:
                DialogFactory.commonTwoBtnDialog(context,
                        context.getResources().getString(R.string.system_reset),
                        context.getResources().getString(R.string.clean_data),
                        context.getResources().getString(R.string.no),
                        context.getResources().getString(R.string.yes), null,
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                App.instance.finishAllActivityExceptOne(Setting.class);
                                App.instance.getTopActivity().finish();
                                User user = Store.getObject(context, Store.WAITER_USER, User.class);
                                if (user != null) {
                                    Store.remove(context, Store.WAITER_USER);
                                }
                                MainPosInfo mainPosInfo = App.instance.getMainPosInfo();
                                if (mainPosInfo != null) {
                                    Store.remove(context, Store.MAINPOSINFO);
                                }
                                UIHelp.startConnectPOS(context);
                            }
                        });
                break;
            case R.id.tv_logout:
                DialogFactory.commonTwoBtnDialog(context,
                        context.getResources().getString(R.string.logout_title),
                        context.getResources().getString(R.string.logout_content),
                        context.getResources().getString(R.string.no),
                        context.getResources().getString(R.string.yes), null,
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                WaiterDevice waiterDevice = App.instance.getWaiterdev();
                                if (waiterDevice == null) {
                                    return;
                                }
                                Map<String, Object> parameters = new HashMap<String, Object>();
                                parameters.put("device", waiterDevice);
                                parameters.put("deviceType", ParamConst.DEVICE_TYPE_WAITER);
                                SyncCentre.getInstance().logout(context, parameters, handler);
                            }
                        });
                break;
            case R.id.tv_clock:
                waiterReloginDialog = new WaiterReloginDialog(this, false);
                waiterReloginDialog.show();
                break;
            case R.id.ll_language_setting:
            case R.id.tv_language:
                alertLanguage = LanguageManager.alertLanguage(this, this);
                break;
            default:
                break;
        }
    }

    //改变字体
    private void initTextTypeFace() {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_setting));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_order_history));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_bump_mob_app));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_connect_pos));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_data_sync));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_kot_notification));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_zone_notification));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_reset));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_clock));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_version_name));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_version));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_logout));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_top_screen_lock));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_open_item));

    }

    @Override
    public void onToggleStateChangeListeren(MyToggleButton Mybutton, Boolean checkState) {
        switch (Mybutton.getId()) {

            case R.id.sb_top_screen_lock:
                if (checkState) {
                    sb_top_screen_lock.setChecked(true);
                    Store.putBoolean(Setting.this, Store.WAITER_SET_LOCK, true);
                    App.instance.startAD();
                } else {
                    sb_top_screen_lock.setChecked(false);
                    Store.putBoolean(Setting.this, Store.WAITER_SET_LOCK, false);
                    App.instance.stopAD();
                }
                break;
            case R.id.mtCrashReporting:
                String message;

                final boolean finalCheckedState = checkState;

                if (!finalCheckedState)
                    message = "Turn off crash reporting? \nsystem will be restart";
                else
                    message = "Turn on crash reporting? \nsystem will be restart";

                DialogFactory.commonTwoBtnDialog(context, "", message,
                        context.getResources().getString(R.string.cancel),
                        context.getResources().getString(R.string.ok),
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mtCrashReporting.setChecked(!finalCheckedState);
                            }
                        },
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mtCrashReporting.setChecked(finalCheckedState);
                                Store.putBoolean(Setting.this, Store.BUGSEE_STATUS, finalCheckedState);
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(App.instance, Welcome.class);
                                        @SuppressLint("WrongConstant")
                                        PendingIntent restartIntent = PendingIntent.getActivity(
                                                App.instance.getApplicationContext(), 0, intent,
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        AlarmManager mgr = (AlarmManager) App.instance
                                                .getSystemService(Context.ALARM_SERVICE);
                                        mgr.set(AlarmManager.RTC,
                                                System.currentTimeMillis() + 1000,
                                                restartIntent);
                                        ActivityManager am = (ActivityManager) App.instance
                                                .getSystemService(Context.ACTIVITY_SERVICE);
                                        if (am != null)
                                            am.killBackgroundProcesses(getPackageName());
                                        App.instance.finishAllActivity();
                                    }
                                });
                            }
                        });

                break;
        }
    }


    private void updateOrderDetail(ItemDetail itemDetail, int count) {
        OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
                currentOrder, itemDetail, 0,
                ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);
//		int oldCount = OrderDetailSQL.getUnFreeOrderDetailsNumInKOTOrPOS(
//				currentOrder, itemDetail, currentGroupId);
        if (count == 0) {// 删除
            OrderDetailSQL.deleteOrderDetail(orderDetail);
            OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
        } else {// 添加
//			count = count - oldCount;
            currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
            OrderSQL.update(currentOrder);
            if (orderDetail == null) {
                orderDetail = ObjectFactory.getInstance()
                        .createOrderDetailForWaiter(currentOrder, itemDetail,
                                0, App.instance.getUser());
                orderDetail.setItemNum(count);
                OrderDetailSQL.addOrderDetailETCForWaiterFirstAdd(orderDetail);
            } else {
                orderDetail.setItemNum(count);
                orderDetail.setUpdateTime(System.currentTimeMillis());
                OrderDetailSQL.updateOrderDetailAndOrderForWaiter(orderDetail);
            }
        }
    }

    private void addOrderDetail(OrderDetail orderDetail) {
        List<ItemModifier> itemModifiers = CoreData.getInstance()
                .getItemModifiers(
                        CoreData.getInstance().getItemDetailById(
                                orderDetail.getItemId(), orderDetail.getItemName()));
        OrderDetailSQL.addOrderDetailETC(orderDetail);
        //    setData();
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


        }
    }

    private void showOpenItemWindow() {
        openItemWindow.show(context, findViewById(R.id.rl_root), handler,
                currentOrder);
    }

    private void dismissOpenItemWindow() {
        openItemWindow.dismiss();
    }

    @Override
    public void setLanguage(final String language) {
        if (alertLanguage != null) {
            alertLanguage.dismiss();
        }
        App.getTopActivity().changeLanguage(language);
    }
}
