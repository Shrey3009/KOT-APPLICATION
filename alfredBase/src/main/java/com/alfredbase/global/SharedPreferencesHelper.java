package com.alfredbase.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alfredbase.store.Store;
import com.alfredbase.store.sql.StoreValueSQL;

public class SharedPreferencesHelper {

    public static final String TRAINING_MODE = "train_key";
    public static final String TRAIN_DISPLAY = "train_display";
    private static final String TAG = Store.class.getSimpleName();
    private static final String PACK_NAME = SharedPreferencesHelper.class.getPackage().getName();


    private SharedPreferencesHelper() {
    };

    public static final long DEFAULT_LONG_TYPE = -1L;
    public static final float DEFAULT_FLOAT_TYPE = -123;
    public static final int DEFAULT_INT_TYPE = -1;  // 副屏只显示文字

    public static final int SUNMI_IMG = -122;  // 只显示图片
    public static final int SUNMI_TEXT = -123;  // 副屏只显示文字
    public static final int SUNMI_IMG_TEXT = -124;  // 显示图片和文字
    public static final int SUNMI_VIDEO = -121;
    public static final int SUNMI_VIDEO_TEXT = -125;

    private static final int TYPE_INT = 1;
    private static final int TYPE_STRING = 1;
    private static final int TYPE_DOUBLE = 1;
    private static final int TYPE_LONG = 1;



    public static final String DEFAULT_STRING_TYPE = "";
    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PACK_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void putString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putString(key, value).commit();
     //   StoreValueSQL.updateStore(key, value);
    }

    // 默认值为 "" 所以做判断的时候 注意用TextUtils
    public static String getString(Context context, String key) {
		return getSharedPreferences(context).getString(key, DEFAULT_STRING_TYPE);
     //   return StoreValueSQL.getValue(key);
    }

    public static void putLong(Context context, String key, long value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putLong(key, value).commit();
     //   StoreValueSQL.updateStore(key, value+"");

    }
    // 默认值为 -1L 注意做判断是时候多加个 为-1L的判断
    public static long getLong(Context context, String key) {
		return getSharedPreferences(context).getLong(key, DEFAULT_LONG_TYPE);
//        String value = StoreValueSQL.getValue(key);
//        if(TextUtils.isEmpty(value)){
//            return -1l;
//        }else{
//            return Long.parseLong(value);
//        }
    }

    public static void putInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putInt(key, value).commit();
      //  StoreValueSQL.updateStore(key,value+"");
    }
    // 默认值为 -123 注意做判断的时候 多加个 为-123的判断
    public static int getInt(Context context, String key) {
		return getSharedPreferences(context).getInt(key, DEFAULT_INT_TYPE);
//        String value = StoreValueSQL.getValue(key);
//        if(TextUtils.isEmpty(value)){
//            return -123;
//        }else{
//            return Integer.parseInt(value);
//        }
    }

    public static int getInt(Context context, String key, int defVal) {
		return getSharedPreferences(context).getInt(key, defVal);
//        String value = StoreValueSQL.getValue(key);
//        if(TextUtils.isEmpty(value)){
//            return defVal;
//        }else{
//            return Integer.parseInt(value);
//        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putBoolean(key, value).commit();
    //    StoreValueSQL.updateStore(key, value+"");
    }

    public static boolean getBoolean(Context context, String key, boolean defVal) {
		return getSharedPreferences(context).getBoolean(key, defVal);
//        String value = StoreValueSQL.getValue(key);
//        if(TextUtils.isEmpty(value)){
//            return defVal;
//        }else{
//            return Boolean.parseBoolean(value);
//        }
    }

}
