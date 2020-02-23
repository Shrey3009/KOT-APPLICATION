package com.alfredkds.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.PrinterSQL;
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

public class EmployeeID extends BaseActivity implements KeyBoardClickListener {
    //	public static final int GET_PRINTER_OK = 1;
//	public static final int GET_PRINTER_FAILED = 2;
    private TextView tv_login_tips;
    private TextView tv_id_1;
    private TextView tv_id_2;
    private TextView tv_id_3;
    private TextView tv_id_4;
    private TextView tv_id_5;
    private Numerickeyboard employee_id_keyboard;

    private String employee_ID;
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
        tv_login_tips.setText(getString(R.string.cashier_login_tips1));
        ((TextView) findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
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

    int pairingCount = 0;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultCode.SUCCESS: {
                    pairingCount++;

//                    ArrayList<Printer> printers = (ArrayList<Printer>) msg.obj;

                    List<Printer> printers = PrinterSQL
                            .getAllPrinterByType(1);//get printer device

                    ArrayList<Printer> printerList = new ArrayList<>();
                    for (Printer printer : printers) {//为cashier时不显示
                        if (!(printer.getIsCashdrawer().intValue() == 1)) {
                            printerList.add(printer);
                        }
                    }

                    if (pairingCount >= App.instance.getAllPairingIp().size()) {
                        pairingCount = 0;
                        loadingDialog.dismiss();

                        UIHelp.startSelectKitchen(context, printerList);
                        finish();
                    }

                    break;
                }

                case ResultCode.USER_NO_PERMIT:
                    loadingDialog.dismiss();
                    pairingCount = 0;
                    UIHelp.showToast(context, context.getResources().getString(R.string.pairing_fails));
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    pairingCount = 0;
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                default:
                    break;
            }
        }

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
            Store.putString(context, Store.EMPLOYEE_ID, employee_ID);
            loadingDialog.show();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("printerType", ParamConst.PRINTER_TYPE_UNGROUP);
            map.put("employeeId", employee_ID);
            //Verify employee and load all printers/KDS from POS

//            List<String> ips = new ArrayList<>();
//            if (App.instance.isBalancer()) {
//                ips.addAll(App.instance.getAllPairingIp());
//            } else {
//                ips.add(App.instance.getPairingIp());
//            }

            for (String ip : App.instance.getAllPairingIp()) {
                if (TextUtils.isEmpty(ip)) continue;
                SyncCentre.getInstance().getPrinters(context, ip, map, handler);

                if (App.instance.isBalancer()) {
                    SyncCentre.getInstance().getConnectedKDS(context, ip, map, handler);
                }
            }
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
