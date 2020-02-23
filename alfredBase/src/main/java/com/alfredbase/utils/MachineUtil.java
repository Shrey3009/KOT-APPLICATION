package com.alfredbase.utils;

import android.os.Build;

public class MachineUtil {
    private final static String TAG = MachineUtil.class.getSimpleName();
    public static boolean isSUNMIShow() {
        String brand = Build.BRAND;
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        LogUtil.d(TAG, brand + "**************" + model);
        if ("SUNMI".equals(brand.toUpperCase())) {
            return true;
        }
        return false;
    }
// T1 or T2
    public static boolean isSunmiModel() {
        try {
            String sn = Build.SERIAL;
            String str = sn.substring(0,2);
            if (str.equals("T1") || str.equals("T2")) {
                return true;
            }  else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSunmiT2() {
        try {
            String sn = Build.SERIAL;
            String str = sn.substring(0,2);
            if ( str.equals("T2")) {
                return true;
            }  else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean isHisense() {
        String model = Build.MODEL;
        LogUtil.d(TAG, model + "**************" + model);
        if ("HK716".equals(model.toUpperCase())) {
            return true;
        }
        return false;
    }
}
