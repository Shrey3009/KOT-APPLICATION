package com.alfredwaiter.view;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.User;
import com.alfredbase.store.Store;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.UIHelp;

/**
 * Created by Zun on 2017/2/8 0008.
 */

public class WaiterReloginDialog implements View.OnClickListener, Numerickeyboard.KeyBoardClickListener {

    private static final int KEY_LENGTH = 5;
    private TextView tv_logout;
    private TextView tv_rest_name;
    private TextView tv_login_tips;
    private TextView tv_psw_1;
    private TextView tv_psw_2;
    private TextView tv_psw_3;
    private TextView tv_psw_4;
    private TextView tv_psw_5;
    private String password;
    private StringBuffer keyBuf = new StringBuffer();
    public BaseActivity parent;
    public View contentView;
    private Dialog dialog;
    public TextTypeFace textTypeFace;
    private User user;
    private boolean boo;

    public WaiterReloginDialog(BaseActivity parent, boolean boo) {
        this.parent = parent;
        this.boo = boo;
        user = App.instance.getUser();
        initView();
    }

    private void initView(){
        dialog = new Dialog(parent, com.alfredbase.R.style.base_dialog);
        contentView = LayoutInflater.from(parent).inflate(
                R.layout.re_login_view, null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(contentView);
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
    }

    public void show(){
        if (parent == null || parent.isFinishing())
            return;
        if (!boo){
            App.instance.stopAD();
        }

        if(dialog.isShowing())
            return;
        dialog.show();
        tv_logout.setVisibility(View.GONE);
    }

    public void dissmiss(){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
        App.instance.startAD();
    }

    @Override
    public void onClick(View v) {

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
            keyBuf.delete(0, key_len);
            setPassword(keyBuf.length());
                if (user != null) {
                    tv_rest_name.setText(user.getFirstName()+"."+user.getLastName());
                    if (password.equals(user.getPassword())) {
                        if (!boo){
                            App.instance.startAD();
                        }
                        dissmiss();
                        return;
                    } else {
                        UIHelp.showToast(parent, parent.getResources().getString(R.string.access_denied));
                        password = null;
                    }
                }else{
                    UIHelp.showToast(parent, parent.getResources().getString(R.string.access_denied));
                    password = null;
                }
                keyBuf.delete(0, key_len);
                setPassword(keyBuf.length());
            }
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
