package com.alfredkds.activity;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;
import com.alfredbase.view.Numerickeyboard.KeyBoardClickListener;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.global.UIHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends BaseActivity implements KeyBoardClickListener {
    private static final int STATE_IN_ENTER_ID = 0;
    public static final int HANDLER_ERROR_INFO = 1;
    public static final int HANDLER_LOGIN = 0;
    private TextView tv_login_tips;
    private TextView tv_id_1;
    private TextView tv_id_2;
    private TextView tv_id_3;
    private TextView tv_id_4;
    private TextView tv_id_5;
    private Numerickeyboard employee_id_keyboard;
    /**
     * 当前键盘输入对应的状态，0表示输入的员工ID，1表示输入的密码
     */
    private int state = STATE_IN_ENTER_ID;

    //	private String employee_ID;
    private String password;
    private String old_employee_ID;
    private boolean hasLogined = false;
    private KDSDevice kdsDevice;
    private boolean doubleBackToExitPressedOnce = false;
    private TextTypeFace textTypeFace;

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = new LoadingDialog(context);
        setContentView(R.layout.activity_login);
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
        tv_login_tips.setText(getString(R.string.cashier_login_tips2));
        User user = Store.getObject(context, Store.KDS_USER, User.class);
        TextView tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_name.setVisibility(View.VISIBLE);
        tv_user_name.setText(user.getFirstName() + " " + user.getLastName());

        ((TextView) findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        initTextTypeFace();
        textTypeFace.setTrajanProRegular(tv_user_name);
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

    int loginCount = 0;
    int loginSize = App.instance.getCurrentConnectedMainPosList().size();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_LOGIN: {
                    loginCount++;

                    if (loginCount >= loginSize) {
                        loginCount = 0;
                        loadingDialog.dismiss();
                        User user = CoreData.getInstance().getUser(old_employee_ID,
                                password);
                        App.instance.setUser(user);
                        Store.putString(context, Store.EMPLOYEE_ID, old_employee_ID);
                        Store.putString(context, Store.PASSWORD, password);

                        initBugseeModifier();
                        if (App.instance.isBalancer()) {
                            UIHelp.startLogScreen(context);
                        } else {
                            UIHelp.startKitchenOrder(context);
                        }
                        finish();
                    }
                    break;
                }
                case ResultCode.CONNECTION_FAILED: {
                    loginCount = 0;
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                }
                break;
//			case HANDLER_ERROR_INFO:
//				loadingDialog.dismiss();
//				UIHelp.showToast(context, ResultCode.getErrorResultStrByCode(context, (Integer)msg.obj));
//				break;
                default:
                    break;
            }
        }

        ;
    };

    private static final int KEY_LENGTH = 5;
    private StringBuffer keyBuf = new StringBuffer();

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

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("employee_ID", old_employee_ID);
        parameters.put("password", password);
        parameters.put("type", ParamConst.USER_TYPE_KOT);
        parameters.put("device", App.instance.getKdsDevice());
//        MainPosInfo currentOne = App.instance.getCurrentConnectedMainPos();
        List<MainPosInfo> connectedMainPos = App.instance.getCurrentConnectedMainPosList();
//        if (isBalancer()) {
        loadingDialog.setTitle(context.getResources().getString(R.string.logining));
        loadingDialog.show();
        if (connectedMainPos.size() > 0) {
            for (MainPosInfo mainPosInfo : connectedMainPos) {
                SyncCentre.getInstance().login(context, mainPosInfo.getIP(), parameters, handler);
            }
        } else {
            loadingDialog.dismiss();
            UIHelp.showToast(context, context.getResources().getString(R.string.paired_failed));
        }
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
