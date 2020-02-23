package com.alfredposclient.view;

import android.app.Dialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.User;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;

/**
 * Created by Alex on 16/12/12.
 */

public class ReloginDialog implements View.OnClickListener, Numerickeyboard.KeyBoardClickListener{
    private static final int KEY_LENGTH = 5;
    private static final int STATE_IN_ENTER_ID = 0;
    private static final int STATE_IN_ENTER_PASSWORD = 1;
    private TextView tv_logout;
    private TextView tv_rest_name;
    private TextView tv_login_tips;
    private TextView tv_psw_1;
    private TextView tv_psw_2;
    private TextView tv_psw_3;
    private TextView tv_psw_4;
    private TextView tv_psw_5;
    private String employee_ID;
    private StringBuffer keyBuf = new StringBuffer();
    /**
     * 当前键盘输入对应的状态，0表示输入的员工ID，1表示输入的密码
     */
    public BaseActivity parent;
    public View contentView;
    private Dialog dialog;
    public TextTypeFace textTypeFace;

    public ReloginDialog(BaseActivity parent) {
        this.parent = parent;
        initView();
    }

    private void initView(){
        dialog = new Dialog(parent, com.alfredbase.R.style.base_dialog);
        contentView = LayoutInflater.from(parent).inflate(
                R.layout.re_login_view, null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(contentView);
        WindowManager windowManager = parent.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.5); //设置宽度
        dialog.getWindow().setAttributes(lp);
        Numerickeyboard numerickeyboard = (Numerickeyboard) contentView.findViewById(R.id.view_numerickeyboard);
        numerickeyboard.setParams(parent);
        numerickeyboard.setKeyBoardClickListener(this);
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.init(parent);
        tv_logout = (TextView) contentView.findViewById(R.id.tv_logout);
        tv_rest_name = (TextView) contentView.findViewById(R.id.tv_rest_name);
        tv_login_tips = (TextView) contentView.findViewById(R.id.tv_login_tips);
        textTypeFace.setTrajanProRegular(tv_logout);
        textTypeFace.setTrajanProRegular(tv_rest_name);
        textTypeFace.setTrajanProRegular(tv_login_tips);
        tv_psw_1 = (TextView) contentView.findViewById(R.id.tv_psw_1);
        tv_psw_2 = (TextView) contentView.findViewById(R.id.tv_psw_2);
        tv_psw_3 = (TextView) contentView.findViewById(R.id.tv_psw_3);
        tv_psw_4 = (TextView) contentView.findViewById(R.id.tv_psw_4);
        tv_psw_5 = (TextView) contentView.findViewById(R.id.tv_psw_5);
        tv_logout.setOnClickListener(this);
        tv_logout.setText(parent.getApplicationContext().getString(R.string.clock_in_out));
        Restaurant rest = CoreData.getInstance().getRestaurant();
        if(rest != null)
            tv_rest_name.setText(rest.getRestaurantName()+"");

    }

    public void show(){
        if (parent == null || parent.isFinishing())
            return;
        if(dialog.isShowing())
            return;
        dialog.show();
        tv_logout.setVisibility(View.VISIBLE);
    }

    public void dissmiss(){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
        App.instance.startAD();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_logout:
                UIHelp.startClockInOROut(parent);
                break;
        }
    }

    @Override
    public void onKeyBoardClick(String key) {
        BugseeHelper.buttonClicked(key);
        if (key.equals("X")) {
            if (keyBuf.length() > 0) {
                keyBuf.deleteCharAt(keyBuf.length() - 1);
            }
        } else {
            if (keyBuf.length() >= KEY_LENGTH)
                return;
            keyBuf.append(key);
            int key_len = keyBuf.length();
            if (key_len == KEY_LENGTH) {
                employee_ID = keyBuf.toString();
                setPassword(keyBuf.length());
                User user = CoreData.getInstance().getUserByEmpId(Integer.parseInt(employee_ID));
                if (user != null) {
                    RevenueCenter revenueCenter = App.instance
                            .getRevenueCenter();
                    boolean cashierAccess = CoreData.getInstance()
                            .checkUserCashierAccessInRevcenter(user,
                                    revenueCenter.getRestaurantId(),
                                    revenueCenter.getId());

                    if (cashierAccess) {
                        App.instance.setUser(user);
                        RxBus.getInstance().post(RxBus.RX_MSG_1, new Integer(1));
                        BaseApplication.postHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dissmiss();
                            }
                        }, 200);
                        return;
                    } else {
                        UIHelp.showToast(parent, parent.getResources().getString(R.string.login_required));
                        employee_ID = null;
                        keyBuf.delete(0,keyBuf.length());
                    }
                } else {
                    UIHelp.showToast(parent, parent.getResources().getString(R.string.invalid_employee));
                    employee_ID = null;
                    keyBuf.delete(0,keyBuf.length());
                }
            }

        }
        setPassword(keyBuf.length());
    }
    private void setPassword(int key_len) {
        switch (key_len) {
            case 0: {
                tv_psw_1.setText("");
                tv_psw_2.setText("");
                tv_psw_3.setText("");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 1: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("");
                tv_psw_3.setText("");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 2: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 3: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("*");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 4: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("*");
                tv_psw_4.setText("*");
                tv_psw_5.setText("");
                break;
            }
            case 5: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("*");
                tv_psw_4.setText("*");
                tv_psw_5.setText("*");
                break;
            }
            default:
                break;
        }
    }
}
