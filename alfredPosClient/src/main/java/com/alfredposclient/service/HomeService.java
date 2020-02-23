package com.alfredposclient.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.alfredposclient.activity.DifferentDislay;

public class HomeService extends Service {
    // 获取设备上的屏幕
    DisplayManager mDisplayManager;// 屏幕管理器
    Display[] displays;// 屏幕数组
    DifferentDislay mPresentation2;
    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        return null;
    }
    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
// TODO Auto-generated method stub
        super.onCreate();
//registerHomeandMenuActionReceiver();// 注册监听home键和菜单键的监听广播
// 双屏异显
        mDisplayManager = (DisplayManager) this
                .getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        showView();
    }

    @SuppressLint("NewApi")
    private void showView(){

        if (null == mPresentation2) {
            mPresentation2 = new DifferentDislay(getApplicationContext(),displays[1]);// displays[1]是副屏
            mPresentation2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mPresentation2.show();

        }

    }
}
