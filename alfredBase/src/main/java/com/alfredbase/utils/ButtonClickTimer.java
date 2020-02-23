package com.alfredbase.utils;

import android.view.View;

import com.alfredbase.global.BugseeHelper;

/**
 * @author 按钮点击控制类，在500毫秒之类，任何按钮不能点击两次
 */
public class ButtonClickTimer {
    public static long lastClickTime;
    public static View lastView;

    public static boolean canClick(View view) {
        if (lastView != view) {
            lastView = view;
            BugseeHelper.buttonClicked(view);
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime > 500) {

            BugseeHelper.buttonClicked(view);

            //create fake crash to trigger bugsee reporting
//            if (currentTimeMillis - lastClickTime > 60 * 1000) { //interval 5 minutes
//                try {
//                    LogUtil.log(currentTimeMillis - lastClickTime + "");
//                    throw new Exception("Non-Fatal Exception");
//                } catch (Exception ex) {
//                }
//            }
            lastClickTime = currentTimeMillis;
            return true;
        } else {
            lastClickTime = currentTimeMillis;
            return false;
        }
    }

    public static boolean canClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime > 500) {
            lastClickTime = currentTimeMillis;
            return true;
        } else {
            lastClickTime = currentTimeMillis;
            return false;
        }
    }

    public static long lastLinkTime;
    private static Object lastOb;

    public static boolean canLink() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastLinkTime > 5000) {
            lastLinkTime = currentTimeMillis;
            return true;
        } else {
//			lastLinkTime = currentTimeMillis;
            return false;
        }
    }

    public static boolean canLink(Object ob) {
        if (ob != lastOb) {
            lastOb = ob;
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastLinkTime > 5000) {
            lastLinkTime = currentTimeMillis;
            return true;
        } else {
//			lastLinkTime = currentTimeMillis;
            return false;
        }
    }


    public static boolean canClickShort() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime > 300) {
            lastClickTime = currentTimeMillis;
            return true;
        } else {
            lastClickTime = currentTimeMillis;
            return false;
        }
    }

    public static boolean canClickShort(View view) {
        if (lastView != view) {
            lastView = view;
            BugseeHelper.buttonClicked(view);
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime > 300) {
            lastClickTime = currentTimeMillis;
            BugseeHelper.buttonClicked(view);
            return true;
        } else {
            lastClickTime = currentTimeMillis;
            return false;
        }
    }
}
