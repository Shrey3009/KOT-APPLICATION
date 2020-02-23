package com.alfred.callnum.activity;

import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alfred.callnum.R;
import com.alfred.callnum.global.App;
import com.alfred.callnum.http.SyncCentre;
import com.alfred.callnum.utils.UIHelp;
import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectMainPos extends BaseActivity implements Numerickeyboard.KeyBoardClickListener, View.OnFocusChangeListener {
    private Numerickeyboard ipkeyboard;

    private EditText et_ip1;
    private EditText et_ip2;
    private EditText et_ip3;
    private EditText et_ip4;
    private TextView tv_connect;
    private TextTypeFace textTypeFace;
    private TextView tv_app_version;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.ip_connect_view);
        tv_app_version = (TextView) findViewById(R.id.tv_app_version);
        ipkeyboard = (Numerickeyboard) findViewById(R.id.ipkeyboard);
        ipkeyboard.setKeyBoardClickListener(this);
        ipkeyboard.setParams(context);
        tv_connect = (TextView) findViewById(R.id.tv_connect);
        tv_connect.setOnClickListener(this);
        et_ip1 = (EditText) findViewById(R.id.et_ip1);
        et_ip2 = (EditText) findViewById(R.id.et_ip2);
        et_ip3 = (EditText) findViewById(R.id.et_ip3);
        et_ip4 = (EditText) findViewById(R.id.et_ip4);
        et_ip1.setOnFocusChangeListener(this);
        et_ip2.setOnFocusChangeListener(this);
        et_ip3.setOnFocusChangeListener(this);
        et_ip4.setOnFocusChangeListener(this);
        et_ip1.setInputType(InputType.TYPE_NULL);
        et_ip2.setInputType(InputType.TYPE_NULL);
        et_ip3.setInputType(InputType.TYPE_NULL);
        et_ip4.setInputType(InputType.TYPE_NULL);
        et_ip1.requestFocus();
        initTextTypeFace();
    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(tv_app_version);
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_ipaddr_tips));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_connect));
        textTypeFace.setTrajanProRegular(et_ip1);
        textTypeFace.setTrajanProRegular(et_ip2);
        textTypeFace.setTrajanProRegular(et_ip3);
        textTypeFace.setTrajanProRegular(et_ip4);
    }

    private EditText getFocusView() {
        if (et_ip1.hasFocus())
            return et_ip1;
        if (et_ip2.hasFocus())
            return et_ip2;
        if (et_ip3.hasFocus())
            return et_ip3;
        if (et_ip4.hasFocus())
            return et_ip4;
        return null;
    }

    private void setNextFocusView() {
        if (et_ip1.hasFocus()) {
            et_ip2.requestFocus();
        } else if (et_ip2.hasFocus()) {
            et_ip3.requestFocus();
        } else if (et_ip3.hasFocus()) {
            et_ip4.requestFocus();
        }
    }

    private void setBeforeFocusView() {
        if (et_ip2.hasFocus()) {
            et_ip1.requestFocus();
        } else if (et_ip3.hasFocus()) {
            et_ip2.requestFocus();
        } else if (et_ip4.hasFocus()) {
            et_ip3.requestFocus();
        }
    }

    @Override
    public void onKeyBoardClick(String key) {
        if (TextUtils.isEmpty(key)) {
//            App.instance.setPairingIp(getInputedIP());
//            UIHelp.startEmployeeID(context);
            finish();
            return;
        }

        BugseeHelper.buttonClicked(key);
        EditText tempEditText = getFocusView();
        if (tempEditText == null)
            return;
        if (key.equals("X")) {
            String content = tempEditText.getText().toString();
            if (content.length() > 0) {
                tempEditText
                        .setText(content.substring(0, content.length() - 1));
            } else {
                setBeforeFocusView();
            }
        } else {
            tempEditText.setText(tempEditText.getText().toString() + key);
            if (tempEditText.getText().toString().length() >= 3)
                setNextFocusView();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_ip1:
                if (hasFocus) {
                    et_ip1.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    et_ip1.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            case R.id.et_ip2:
                if (hasFocus) {
                    et_ip2.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    et_ip2.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            case R.id.et_ip3:
                if (hasFocus) {
                    et_ip3.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    et_ip3.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            case R.id.et_ip4:
                if (hasFocus) {
                    et_ip4.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    et_ip4.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            default:
                break;
        }

    }

    private String getInputedIP() {
        String ipAddress = et_ip1.getText().toString() + "."
                + et_ip2.getText().toString() + "."
                + et_ip3.getText().toString() + "."
                + et_ip4.getText().toString();
        return ipAddress;

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ResultCode.SUCCESS:
                    dismissLoadingDialog();
                    initBugseeModifier();
                    UIHelp.startMainActivity(context, App.instance.getMainPageType());
                    finish();
                    break;
                case ResultCode.CONNECTION_FAILED:
                    App.instance.setPosIp("");
                    dismissLoadingDialog();
                    break;
            }
        }
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
        if (!TextUtils.isEmpty(employeeId))
            BugseeHelper.setAttribute("employee_id", employeeId);

        BugseeHelper.setAttribute("pos_ip", App.instance.getPosIp());

//        throw new NullPointerException("Test crash");
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.tv_connect:
                String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
                Pattern pattern = Pattern.compile(ip);
                Matcher matcher = pattern.matcher(getInputedIP());
                if (matcher.matches()) {
                    App.instance.setPosIp(getInputedIP());
                    if (loadingDialog == null) {
                        loadingDialog = new LoadingDialog(context);
                    }
                    loadingDialog.setTitle(getString(R.string.loading));
                    loadingDialog.show();
                    SyncCentre.getInstance().assignRevenue(context, App.instance.getPosIp(), handler);
//                    App.instance.setPosIp(getInputedIP());
//                    UIHelp.startMainActivity(this, App.instance.getMainPageType());
//                    finish();
                } else {
                    UIHelp.showToast(context, context.getResources().getString(R.string.check_ip));
                }
                break;

            default:
                break;
        }
    }

}
