package com.alfred.callnum.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alfred.callnum.R;
import com.alfred.callnum.fragment.OneFragment;
import com.alfred.callnum.global.App;
import com.alfred.callnum.utils.UIHelp;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.LogUtil;

public class WelcomeActivity extends FragmentActivity implements View.OnClickListener {


    OneFragment oneFragment;

    private Button one, two, three, four;

    int viewId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        BugseeHelper.trace("APP", getString(R.string.app_name));

        one = (Button) findViewById(R.id.btn_one);
        two = (Button) findViewById(R.id.btn_two);
        three = (Button) findViewById(R.id.btn_three);
        four = (Button) findViewById(R.id.btn_four);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        // createFragment();\
//String s="A";
//char[] ch=s.toCharArray();
//byte num= (byte) s.charAt(0);
//LogUtil.e("wwww",ch[0]+"--"+num);


        if (!TextUtils.isEmpty(App.instance.getPosIp())) {
            UIHelp.startMainActivity(WelcomeActivity.this, App.instance.getMainPageType());
            finish();
        } else {
            UIHelp.startSelectRevenue(this);
            finish();
        }

//系统音量

    }


    @Override
    public void onClick(View v) {
        BugseeHelper.buttonClicked(v);
        switch (v.getId()) {

            case R.id.btn_one:
                viewId = 1;
                break;
            case R.id.btn_two:
                viewId = 2;
                break;
//            case R.id.btn_three:
//                viewId = 3;
//                break;
            case R.id.btn_four:
                viewId = 4;
                break;


        }
        //  App.instance.setMainPageType(viewId);
        UIHelp.startSelectRevenue(this);

        // UIHelp.startMainActivity(WelcomeActivity.this, App.instance.getMainPageType());
    }
}
