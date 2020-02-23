package com.alfredwaiter.activity;

import android.os.Handler;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;
import com.alfredbase.view.Numerickeyboard.KeyBoardClickListener;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.SyncCentre;
import com.alfredwaiter.global.UIHelp;

import java.util.HashMap;
import java.util.Map;

public class EmployeeID extends BaseActivity implements KeyBoardClickListener {
    //	public static final int CONNECT_FAILED = 0x10;
//	public static final int CONNECT_TIMEOUT = 0x11;
    private TextView tv_login_tips;
    private TextView tv_id_1;
    private TextView tv_id_2;
    private TextView tv_id_3;
    private TextView tv_id_4;
    private TextView tv_id_5;
    private Numerickeyboard employee_id_keyboard;
    private TextTypeFace textTypeFace;
    public static final int SYNC_DATA_TAG = 2015;
    public static final int HANDLER_PAIRING_COMPLETE = 1001;
    private int syncDataCount = 0;
    /**
     * 当前键盘输入对应的状态，0表示输入的员工ID，1表示输入的密码
     */

    private String employee_ID;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = new LoadingDialog(context);
        setContentView(R.layout.activity_employee_id);
        employee_id_keyboard = (Numerickeyboard) findViewById(R.id.employee_id_keyboard);
        employee_id_keyboard.setKeyBoardClickListener(this);
        employee_id_keyboard.setParams(context);
        tv_id_1 = (TextView) findViewById(R.id.tv_id_1);
        tv_id_2 = (TextView) findViewById(R.id.tv_id_2);
        tv_id_3 = (TextView) findViewById(R.id.tv_id_3);
        tv_id_4 = (TextView) findViewById(R.id.tv_id_4);
        tv_id_5 = (TextView) findViewById(R.id.tv_id_5);
        tv_login_tips = (TextView) findViewById(R.id.tv_login_tips);
        tv_login_tips.setText(getString(R.string.waiter_login_tips1));
        ((TextView) findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        initTextTypeFace();
    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_version));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_login_tips));
        textTypeFace.setTrajanProRegular(tv_id_1);
        textTypeFace.setTrajanProRegular(tv_id_2);
        textTypeFace.setTrajanProRegular(tv_id_3);
        textTypeFace.setTrajanProRegular(tv_id_4);
        textTypeFace.setTrajanProRegular(tv_id_5);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultCode.SUCCESS: {
                    loadingDialog.dismiss();
                    HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
//				UIHelp.startSelectRevenue(context, map);
                    RevenueCenter revenueCenter = (RevenueCenter) map.get("revenue");
                    App.instance.setRevenueCenter(revenueCenter);
                    Store.saveObject(context, Store.CURRENT_REVENUE_CENTER,
                            revenueCenter);
                    loadingDialog.setTitle(context.getResources().getString(R.string.loading));
                    loadingDialog.show();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            OrderSQL.deleteAllOrder();
                            OrderDetailSQL.deleteAllOrderDetail();
                            OrderModifierSQL.deleteAllOrderModifier();
                            OrderDetailTaxSQL.deleteAllOrderDetailTax();
                        }
                    }).start();
                    syncDataCount = 0;
                    SyncData();
                    getPlaces();
//				finish();
                }
                break;
                case ResultCode.USER_NO_PERMIT:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.access_denied));
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                case HANDLER_PAIRING_COMPLETE: {
                    loadingDialog.dismiss();
                    UIHelp.startLogin(context);
                    finish();
                    break;
                }
                case TablesPage.HANDLER_GET_PLACE_INFO: {
                    // 预留2秒让数据存下数据库
                    BaseApplication.postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            WaiterDevice waiterDevice = new WaiterDevice();
                            waiterDevice
                                    .setWaiterId(App.instance.getUser().getId());
                            waiterDevice.setIP(CommonUtil.getLocalIpAddress());
                            waiterDevice.setMac(CommonUtil
                                    .getLocalMacAddress(context));
                            Store.saveObject(context, Store.WAITER_DEVICE,
                                    waiterDevice);
                            parameters.put("device", waiterDevice);
                            parameters.put("deviceType",
                                    ParamConst.DEVICE_TYPE_WAITER);
                            SyncCentre.getInstance().pairingComplete(context,
                                    App.instance.getPairingIp(), parameters,
                                    handler);
                        }
                    }, 2 * 1000);
                }
                break;
                case SYNC_DATA_TAG:
                    if (syncDataCount == 6) {
                        handler.sendEmptyMessage(TablesPage.HANDLER_GET_PLACE_INFO);
                    } else {
                        syncDataCount++;
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private static final int KEY_LENGTH = 5;
    private StringBuffer keyBuf = new StringBuffer();

    @Override
    public void onKeyBoardClick(String key) {
        if (keyBuf.length() >= KEY_LENGTH)
            return;

        BugseeHelper.buttonClicked(key);
        if (key.equals("X")) {
            if (keyBuf.length() > 0) {
                keyBuf.deleteCharAt(keyBuf.length() - 1);
            }
        } else {
            keyBuf.append(key);
        }
        int key_len = keyBuf.length();
        setPassword(key_len);
        if (key_len == KEY_LENGTH) {
            employee_ID = keyBuf.toString();
            keyBuf.delete(0, key_len);
            setPassword(keyBuf.length());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("employee_ID", employee_ID);
            loadingDialog.show();
            Store.putString(context, Store.EMPLOYEE_ID, employee_ID);
            SyncCentre.getInstance().employeeId(context, App.instance.getPairingIp(), map, handler);
        }
    }

    private void SyncData() {
        Map<String, Object> parameters = new HashMap<>();
        SyncCentre.getInstance().syncCommonData(context,
                App.instance.getPairingIp(), parameters, handler);
    }

    private void getPlaces() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("revenueId", App.instance.getRevenueCenter().getId());
        SyncCentre.getInstance().getPlaceInfo(context,
                App.instance.getPairingIp(), parameters, handler);

    }

    private void setPassword(int key_len) {
        switch (key_len) {
            case 0: {
                tv_id_1.setText("");
                tv_id_2.setText("");
                tv_id_3.setText("");
                tv_id_4.setText("");
                tv_id_5.setText("");
                break;
            }
            case 1: {
                tv_id_1.setText("*");
                tv_id_2.setText("");
                tv_id_3.setText("");
                tv_id_4.setText("");
                tv_id_5.setText("");
                break;
            }
            case 2: {
                tv_id_1.setText("*");
                tv_id_2.setText("*");
                tv_id_3.setText("");
                tv_id_4.setText("");
                tv_id_5.setText("");
                break;
            }
            case 3: {
                tv_id_1.setText("*");
                tv_id_2.setText("*");
                tv_id_3.setText("*");
                tv_id_4.setText("");
                tv_id_5.setText("");
                break;
            }
            case 4: {
                tv_id_1.setText("*");
                tv_id_2.setText("*");
                tv_id_3.setText("*");
                tv_id_4.setText("*");
                tv_id_5.setText("");
                break;
            }
            case 5: {
                tv_id_1.setText("*");
                tv_id_2.setText("*");
                tv_id_3.setText("*");
                tv_id_4.setText("*");
                tv_id_5.setText("*");
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));

        BaseApplication.postHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
