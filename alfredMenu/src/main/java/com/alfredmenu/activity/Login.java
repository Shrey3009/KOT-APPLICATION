package com.alfredmenu.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;
import com.alfredbase.view.Numerickeyboard.KeyBoardClickListener;
import com.alfredmenu.global.App;
import com.alfredmenu.global.SyncCentre;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.R;

import java.util.HashMap;
import java.util.Map;

public class Login extends BaseActivity implements KeyBoardClickListener {
    public static final int HANDLER_LOGIN = 0;
    public static final int HANDLER_ERROR_INFO = 1;
    public static final int HANDLER_RESTAURANT_INFO_SUCCESS = 2;
    public static final int HANDLER_RESTAURANT_INFO_ERROR = 3;
    private TextView tv_login_tips;
    private TextView tv_id_1;
    private TextView tv_id_2;
    private TextView tv_id_3;
    private TextView tv_id_4;
    private TextView tv_id_5;
    private Numerickeyboard employee_id_keyboard;
    private String password;
    private String old_employee_ID;
    private User oldUser;
    private boolean doubleBackToExitPressedOnce = false;
    private TextTypeFace textTypeFace;

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
        old_employee_ID = Store.getString(context, Store.EMPLOYEE_ID);
        oldUser = Store.getObject(context, Store.WAITER_USER, User.class);
        tv_login_tips.setText(oldUser.getFirstName() + "." + oldUser.getLastName() + getString(R.string.waiter_login_tips2));
        ((TextView) findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        Button btn_re_connect = (Button) findViewById(R.id.btn_re_connect);
        btn_re_connect.setVisibility(View.VISIBLE);
        btn_re_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SyncCentre.getInstance().cancelAllRequests();
                Store.remove(Login.this, Store.WAITER_USER);
                Store.remove(Login.this, Store.MAINPOSINFO);
                App.instance.setMainPosInfo(null);
                Login.this.startActivity(new Intent(Login.this, Welcome.class));
                Login.this.finish();
            }
        });
        initTextTypeFace();
    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(tv_login_tips);
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_version));
        textTypeFace.setTrajanProRegular(tv_id_1);
        textTypeFace.setTrajanProRegular(tv_id_2);
        textTypeFace.setTrajanProRegular(tv_id_3);
        textTypeFace.setTrajanProRegular(tv_id_4);
        textTypeFace.setTrajanProRegular(tv_id_5);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_LOGIN: {
                    initBugseeModifier();
                    SyncCentre.getInstance().getRestaurantInfo(context, new HashMap<String, Object>(), handler);
                    break;
                }
//			case HANDLER_ERROR_INFO:
//				loadingDialog.dismiss();
//				UIHelp.showToast(Login.this, ResultCode.getErrorResultStrByCode(Login.this, (Integer) msg.obj));
//				break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                case HANDLER_RESTAURANT_INFO_SUCCESS:

                   /* boolean flag = Store.getBoolean(context, Store.WAITER_SET_LOCK, true);

                if(flag){
                    Store.putBoolean(context, Store.WAITER_SET_LOCK, false);
                    loadingDialog.dismiss();
                    finish();
                }else{*/
                    UIHelp.startTables(context);
//                    UIHelp.startMenuPage(context);
                    loadingDialog.dismiss();
                    finish();
//                    }
                    break;
                case HANDLER_RESTAURANT_INFO_ERROR:
                    loadingDialog.dismiss();
                    break;
                default:
                    break;
            }
        }

        ;
    };

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
            password = keyBuf.toString();
            login();
            keyBuf.delete(0, key_len);
            setPassword(keyBuf.length());
        }
    }

    private void login() {
        loadingDialog.setTitle(context.getResources().getString(R.string.logining));
        loadingDialog.show();
        Map<String, Object> parameters = new HashMap<String, Object>();
        WaiterDevice waiterDevice = App.instance.getWaiterdev();
        waiterDevice.setIP(CommonUtil.getLocalIpAddress());
        Store.putString(context, Store.WIFI_STR, waiterDevice.getIP());
        App.instance.setWaiterdev(waiterDevice);
        parameters.put("employee_ID", old_employee_ID);
        parameters.put("password", password);
        parameters.put("type", ParamConst.USER_TYPE_WAITER);
        parameters.put("device", App.instance.getWaiterdev());
        SyncCentre.getInstance().login(context, parameters, handler);
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
