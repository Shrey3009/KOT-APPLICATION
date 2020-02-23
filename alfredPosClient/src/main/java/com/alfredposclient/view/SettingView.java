package com.alfredposclient.view;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.http.APIName;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.Store;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.MachineUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.Welcome;
import com.alfredposclient.activity.kioskactivity.MainPageKiosk;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.utils.AlertToDeviceSetting;

public class SettingView extends LinearLayout implements OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    final static String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    private BaseActivity context;
    private DrawerLayout mDrawerLayout;
    private long startTime;
    private float mTouchStartX;
    private float mTouchStartY;
    // 是否移动了
    private boolean isMoved;
    // 是否释放了
    private boolean isReleased;
    // 计数器，防止多次点击导致最后一次形成longpress的时间变短
    private int mCounter;
    // 长按的runnable
    private Runnable mLongPressRunnable;
    private long endTime;
    private boolean isclick = false;

    public SettingView(Context context) {
        super(context);
        init(context);
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setParams(BaseActivity parent, DrawerLayout drawerLayout) {
        this.context = parent;
        this.mDrawerLayout = drawerLayout;
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.setting_window, this);


        findViewById(R.id.ll_devices).setOnClickListener(this);
        findViewById(R.id.ll_reprint_bill).setOnClickListener(this);
        findViewById(R.id.ll_edit_settlement).setOnClickListener(
                this);
        findViewById(R.id.ll_boh_settlement).setOnClickListener(
                this);
        findViewById(R.id.ll_stored_card).setOnClickListener(this);
        findViewById(R.id.ll_dashboard).setOnClickListener(this);
        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.ll_close_subpos).setOnClickListener(this);
        findViewById(R.id.ll_xz_report).setOnClickListener(this);
        findViewById(R.id.ll_entplu).setOnClickListener(this);
        findViewById(R.id.ll_voidplu).setOnClickListener(this);
        findViewById(R.id.ll_clock_select).setOnClickListener(this);
        findViewById(R.id.ll_cash_inout).setOnClickListener(this);
        findViewById(R.id.ll_system).setOnClickListener(this);
        //findViewById(R.id.ll_system).setOnLongClickListener(this);
        findViewById(R.id.ll_system).setOnTouchListener(this);

        findViewById(R.id.ll_opendrawer).setOnClickListener(this);
        findViewById(R.id.iv_setting_close).setOnClickListener(this);
        findViewById(R.id.ll_setting_title).setOnClickListener(null);
        findViewById(R.id.ll_monthly_sale_report).setOnClickListener(this);
        findViewById(R.id.ll_monthly_plu_report).setOnClickListener(this);
        findViewById(R.id.ll_printer_qr_code).setOnClickListener(this);
        findViewById(R.id.ll_sub_pos).setOnClickListener(this);
        findViewById(R.id.ll_sunmi).setOnClickListener(this);
        findViewById(R.id.linear_sunmi).setOnClickListener(this);
        SUNMIVisible();


        if (BaseApplication.countryCode != ParamConst.CHINA) {
            findViewById(R.id.monthly_sale_report_container).setVisibility(View.GONE);
        }
        if (App.instance.isRevenueKiosk()) {
            findViewById(R.id.ll_printer_qr_code).setVisibility(View.VISIBLE);
            if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
                findViewById(R.id.ll_sub_pos).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_close_subpos).setVisibility(View.GONE);

            } else {
                findViewById(R.id.ll_sub_pos).setVisibility(View.GONE);
                findViewById(R.id.ll_close_subpos).setVisibility(View.VISIBLE);
            }
        } else {
            findViewById(R.id.ll_printer_qr_code).setVisibility(View.GONE);
            findViewById(R.id.ll_sub_pos).setVisibility(View.GONE);
            findViewById(R.id.ll_close_subpos).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        initTextTypeFace();

        mLongPressRunnable = new Runnable() {

            @Override
            public void run() {
                System.out.println("thread");
//                System.out.println("mCounter--->>>"+mCounter);
//                System.out.println("isReleased--->>>"+isReleased);
//                System.out.println("isMoved--->>>"+isMoved);
                mCounter--;
                // 计数器大于0，说明当前执行的Runnable不是最后一次down产生的。
                if (mCounter > 0 || isReleased || isMoved)
                    return;


                final int train = SharedPreferencesHelper.getInt(context, SharedPreferencesHelper.TRAINING_MODE);

                int display = Store.getInt(context, SharedPreferencesHelper.TRAIN_DISPLAY);
// 0  隐藏， 1 显示
                if (display == ParamConst.ENABLE_POS_TRAINING) {

                    DialogFactory.commonTwoBtnDialog((BaseActivity) context, "",
                            "Enable Training Mode Option？",
                            context.getResources().getString(R.string.cancel),
                            context.getResources().getString(R.string.ok),
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Store.putInt(context, SharedPreferencesHelper.TRAIN_DISPLAY, 1);
                                }
                            },
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {

                                    Store.putInt(context, SharedPreferencesHelper.TRAIN_DISPLAY, 0);
                                }


                            });
                } else {

                    DialogFactory.commonTwoBtnDialog((BaseActivity) context, "",
                            "Disable Training Mode Option？",
                            context.getResources().getString(R.string.cancel),
                            context.getResources().getString(R.string.ok),
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Store.putInt(context, SharedPreferencesHelper.TRAIN_DISPLAY, 0);

                                }
                            },
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {

                                    Store.putInt(context, SharedPreferencesHelper.TRAIN_DISPLAY, 1);

                                    if (train == 1) {
                                        SharedPreferencesHelper.putInt(context, SharedPreferencesHelper.TRAINING_MODE, 0);

//										context.runOnUiThread(new Runnable() {
//
//											@Override
//											public void run() {
                                        Intent intent = new Intent(App.instance, Welcome.class);
                                        @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
                                                App.instance
                                                        .getApplicationContext(),
                                                0, intent,
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        // 退出程序

//							File file = new File("/data/data/com.alfredposclient/databases/com.alfredposclient.train");
//							if(!file.exists()){
//								//LogUtil.e("ssss","sss");
//								SessionStatus sessionStatus = Store.getObject(
//										context, Store.SESSION_STATUS, SessionStatus.class);
//								GeneralSQL.deleteKioskHoldOrderInfoBySession(sessionStatus,App.instance.getBusinessDate());
//								Store.remove(context, Store.SESSION_STATUS);
//								App.instance.setSessionStatus(null);
//							}

                                        AlarmManager mgr = (AlarmManager) App.instance
                                                .getSystemService(Context.ALARM_SERVICE);
                                        mgr.set(AlarmManager.RTC,
                                                System.currentTimeMillis() + 1000,
                                                restartIntent); // 1秒钟后重启应用
                                        ActivityManager am = (ActivityManager) App.instance
                                                .getSystemService(Context.ACTIVITY_SERVICE);
                                        am.killBackgroundProcesses(context.getPackageName());
                                        App.instance.finishAllActivity();
//											}
//										});
                                    }
                                }


                            });
                }

                // performLongClick();// 回调长按事件
                //FloatActionController.getInstance().onClick();
            }
        };
    }

    public void initOptionsNoSessionOpen() {
        findViewById(R.id.ll_edit_settlement).setVisibility(
                View.GONE);
        findViewById(R.id.ll_close).setVisibility(View.GONE);
        if (App.instance.isSUNMIShow()) {
            findViewById(R.id.linear_sunmi).setVisibility(VISIBLE);
            SUNMIGone();
        } else {
            findViewById(R.id.linear_sunmi).setVisibility(GONE);
            SUNMIGone();
        }


    }

    public void initOptionsSessionOpen() {
        findViewById(R.id.ll_edit_settlement).setVisibility(
                View.GONE);
        findViewById(R.id.ll_close).setVisibility(View.GONE);
        if (MachineUtil.isSUNMIShow() || MachineUtil.isHisense()) {
            if (MachineUtil.isSunmiModel()) {
                findViewById(R.id.linear_sunmi).setVisibility(VISIBLE);
                SUNMIVisible();
            } else {
                findViewById(R.id.linear_sunmi).setVisibility(VISIBLE);
                SUNMIGone();
            }

        } else {
            findViewById(R.id.linear_sunmi).setVisibility(INVISIBLE);
            SUNMIGone();
        }
    }

    @Override
    public void onClick(View v) {
        if (ButtonClickTimer.canClick(v)) {
            switch (v.getId()) {
                case R.id.ll_devices: {
                    BugseeHelper.buttonClicked("Device");
                    UIHelp.startDevicesHtml(context);
                    break;
                }
                case R.id.ll_reprint_bill: {
                    BugseeHelper.buttonClicked("Reprint Bill");
//				UIHelp.startReprintBillHtml(context);
                    UIHelp.startEditSettlementHtml(context);
                    break;
                }
                case R.id.ll_edit_settlement: {
                    BugseeHelper.buttonClicked("Edit Settlement");
                    UIHelp.startEditSettlementHtml(context);
                    break;
                }
                case R.id.ll_boh_settlement: {
                    BugseeHelper.buttonClicked("Boh Settlement");
                    UIHelp.startBOHSettlementHtml(context);
                    break;
                }
                case R.id.ll_stored_card: {
                    BugseeHelper.buttonClicked("Store Card");
                    RxBus.getInstance().post(RxBus.RX_MSG_1, null);
//				mDrawerLayout.closeDrawer(Gravity.END);
                    break;
                }
                case R.id.ll_dashboard: {
                    BugseeHelper.buttonClicked("Dashboard");
                    UIHelp.startDashboardHtml(context);
                    break;
                }
                case R.id.ll_xz_report: {
                    BugseeHelper.buttonClicked("XZ Report");
                    UIHelp.startXZReportActivty(context);
                    break;
                }
                case R.id.ll_entplu: {
                    BugseeHelper.buttonClicked("Ent PLU");
                    UIHelp.startEntPluHtml(context);
                    break;
                }
                case R.id.ll_voidplu: {
                    BugseeHelper.buttonClicked("Void PLU");
                    UIHelp.startVoidPluHtml(context);
                    break;
                }
                case R.id.ll_close: {
                    BugseeHelper.buttonClicked("Close");
                    // UIHelp.startOpenRestaruant(context);
                    // context.overridePendingTransition(R.anim.slide_top_in,
                    // R.anim.anim_null);
                    context.finish();
                    break;
                }
                case R.id.ll_close_subpos: {
                    BugseeHelper.buttonClicked("Close Subpos");
                    if (context instanceof MainPageKiosk) {
                        ((MainPageKiosk) context).tryToCloseSession();
                    }
                    break;
                }
                case R.id.ll_clock_select:
                    BugseeHelper.buttonClicked("Clock Inout");
                    UIHelp.startClockInOROut(context);
                    break;
                case R.id.ll_cash_inout:
                    BugseeHelper.buttonClicked("Cash inout");
                    UIHelp.startCashInOut(context);
                    break;
                case R.id.ll_system:
                    BugseeHelper.buttonClicked("System Setting");
                    UIHelp.startSystemSetting(context);
                    break;
                case R.id.ll_opendrawer:
                    BugseeHelper.buttonClicked("Open Drawer");
                    PrinterDevice printer = App.instance.getCahierPrinter();
                    if (printer == null) {
                        AlertToDeviceSetting.noKDSorPrinter(context,
                                context.getResources().getString(R.string.no_cashier_printer));
                    } else {
//					App.instance.kickOutCashDrawer(printer);
                        RxBus.getInstance().post("open_drawer", null);
                    }
                    break;
                case R.id.iv_setting_close:
                    BugseeHelper.buttonClicked("Setting Close");
                    mDrawerLayout.closeDrawer(Gravity.END);
                    break;
                case R.id.ll_monthly_sale_report:
                    BugseeHelper.buttonClicked("Monthly sale report");
                    UIHelp.startMonthlySalesReport(context);
                    break;
                case R.id.ll_monthly_plu_report:
                    BugseeHelper.buttonClicked("Monthly plu report");
                    UIHelp.startMonthlyPLUReport(context);
                    break;
                case R.id.ll_printer_qr_code: {
                    BugseeHelper.buttonClicked("Printer qr Code");
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("companyId", CoreData.getInstance().getRestaurant().getCompanyId().intValue() + "");
//				map.put("restaurantId", CoreData.getInstance().getRestaurant().getId().intValue() + "");
//				map.put("tableId", "0");
//				map.put("tableName", "");
//				map.put("type", "2");
                    String restaurantId = getStringByInt(CoreData.getInstance().getRestaurant().getId().intValue() + "");
                    String tableId = "a";
                    StringBuffer sb = new StringBuffer();
                    sb.append(getAbsoluteUrl(APIName.QC_DOWNLOAD) + "&" + restaurantId + "&" + tableId);

//				mDrawerLayout.closeDrawer(Gravity.END);
//				final String content = new Gson().toJson(map);
                    final String content = sb.toString();
                    DialogFactory.showQrCodeDialog(context, content, "", false,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PrinterTitle title = ObjectFactory.getInstance()
                                            .getPrinterTitleForQRCode(
                                                    App.instance.getRevenueCenter(),
                                                    App.instance.getUser().getFirstName()
                                                            + App.instance.getUser().getLastName(),
                                                    "");
                                    App.instance.remitePrintTableQRCode(App.instance.getCahierPrinter(),
                                            title, 0 + "", content);
                                    LogUtil.d("SettingView", "打印二维码");
                                }
                            });
                }
                break;
                case R.id.ll_sunmi:
                    BugseeHelper.buttonClicked("Sunmi");
                    UIHelp.startSunmiActivity(context);
                    break;
                case R.id.linear_sunmi:
                    BugseeHelper.buttonClicked("Sunmi");
                    UIHelp.startSunmiActivity(context);
                    break;
                case R.id.ll_sub_pos:
                    BugseeHelper.buttonClicked("Sub POS");
                    UIHelp.startSubPosManagePage(context);
                    break;
                default:
                    break;
            }
        }
    }

    private String getStringByInt(String id) {
        final String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        StringBuffer sbf = new StringBuffer();
        char[] c = id.toCharArray();
        for (char ch : c) {
            int intNum = ch - '0';
            sbf.append(letters[intNum]);
        }
        return sbf.toString();
    }


    private String getAbsoluteUrl(String relativeUrl) {
        if (BaseApplication.isDebug) {
//			return "http://172.16.0.190:8087/alfred-api/" + relativeUrl;
            //  return "http://192.168.104.10:8083/alfred-api/" + relativeUrl;
//            return "http://192.168.20.100:8083/alfred-api/" + relativeUrl;
            return "http://18.140.71.198//alfred-api/" + relativeUrl;
        } else if (BaseApplication.isOpenLog) {
            return "http://139.224.17.126/" + relativeUrl;
        } else {
            if (BaseApplication.isZeeposDev) {
                return "http://18.140.71.198/" + relativeUrl;
            }
            else if (BaseApplication.isCuscapiMYDev)
            {
                return "http://52.221.95.33:180/" + relativeUrl;
            }
            else {
//			return "http://54.169.45.214/alfred-api/" + relativeUrl;52.77.208.125
                return "http://www.servedbyalfred.biz/" + relativeUrl;
            }
        }
    }

    public void openDrawer() {
        App.instance.kickOutCashDrawer(App.instance.getCahierPrinter());
    }

    private void initTextTypeFace() {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_setting));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_edit));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_report));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_system_title));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_reprint_bill));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_edit_settlement));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_boh_settlement));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_stored_card_setting));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_dashboard));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_xz_report));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_clock_select));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_cash_inout));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_devices));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_system));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_close));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_opendrawer));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_printer_qr_code));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_version));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_sunmi));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.text_sunmi));
    }

    public void SUNMIVisible() {
        findViewById(R.id.ll_sunmi).setVisibility(VISIBLE);
    }

    public void SUNMIGone() {
        findViewById(R.id.ll_sunmi).setVisibility(GONE);
    }

    @Override
    public boolean onLongClick(View v) {
        // 0  正常模式， 1 培训模式
        final int train = SharedPreferencesHelper.getInt(context, SharedPreferencesHelper.TRAINING_MODE);

        int display = Store.getInt(context, SharedPreferencesHelper.TRAIN_DISPLAY);


        //ToastUtils.showToast(context,"changanl");
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mCounter++;
                isReleased = false;
                isMoved = false;
                postDelayed(mLongPressRunnable, 5000);

                break;
            case MotionEvent.ACTION_MOVE:
                //图标移动的逻辑在这里
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    isMoved = true;
                    // 更新浮动窗口位置参数

                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                // 当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
//                if ((endTime - startTime) > 1 * 1000L) {
//                    isclick = false;
//                } else {
//                    isclick = true;
//                }
                isReleased = true;
                break;
        }

        return false;
    }
}
