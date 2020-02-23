package com.alfredbase;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.store.Store;
import com.alfredbase.store.sql.StoreValueSQL;
import com.alfredbase.utils.LanguageManager;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.RxBus;
import com.floatwindow.float_lib.FloatActionController;
import com.google.gson.Gson;
import com.moonearly.utils.service.TcpUdpFactory;
import com.moonearly.utils.service.UdpServiceCallBack;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.codec.language.bm.Lang;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseApplication extends Application {

    public static BaseApplication instance;
    public static List<BaseActivity> activitys;
    public static final int DATABASE_VERSION = 32;



    /**
     * 注意
     * 当 isDebug == false， isOpenLog == false 为正式服务器，地区服务器通过地区代码表示 SINGAPORE亚马逊 CHINA阿里
     * (For the official server, the regional server is represented by the area code SINGAPORE Amazon CHINA Ali)
     * 当 isDebug == false， isOpenLog == true 为阿里云服务器 (Alibaba Cloud Server)
     * 当 isDebug == true， isOpenLog == true 为本地的服务器 (Local server)
     */

    public static boolean isDebug = false;          //	Debug开关 release的时候设置为false
    public static boolean isOpenLog = false;        //	release 时设置为false
    public static boolean isZeeposDev = false;      //	zeepos local development
    public static boolean isCuscapiMYDev = false;    //  Cuscapi Malaysia Local Development

    protected String APPPATH = "sunmi";// sunmi or google or alibaba;


    public static int UDP_INDEX_POS = 1;
    public static int UDP_INDEX_WAITER = 2;
    public static int UDP_INDEX_KDS = 3;
    public static int UDP_INDEX_EMENU = 100;
    public static int UDP_INDEX_CALLNUM = 400;
    public static int UDP_INDEX_SUB_POS = 200;
    public static int UDP_INDEX_SELF_HELP = 300;
    private Handler reLoginHandler = new Handler();
    private Handler reKpmHandler = new Handler();
    public static Handler postHandler = new Handler();

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    public static final int REQUEST_ENABLE_BT = 1;  //请求的code

    /**
     * 国家电话代码
     * 用于区别不同国家的代码逻辑
     */
    public static int countryCode = ParamConst.SINGAPORE;

    public static boolean sub_thread_running = true;
    /**
     * 暂时做的假的version 给Singapore的人辨别使用的
     */
//	public static String fake_version = "0.1.5";
    //network
    public boolean network_connected = false;
    public String ipaddress_str = "";
    public int ipaddress_int = 0;

    public String wifi_mac = "";
    public String wifi_ssid = "";
    public int wifissi = 0;

    /**
     * ImageLoader
     * ImageLoader from https://github.com/nostra13/Android-Universal-Image-Loader
     *
     * @author Alex
     */
    private static final String IMAGE_ENGIN_PATH = "alfred/cache"; // 图片持久化的位置(本地sdcard 和机身ROM)
    public static int IMAGE_ENGINE_CORETHREAD = 5; // 可以同步加载图片的个数
    public static int IMAGE_ENGINE_COMPRESS_QUALITY = 100; // 对图片的压缩比例0~100数值约大越清晰，越小越模糊
    // Can slow
    // ImageLoader, use
    // it carefully
    // (Better don't use
    // it)
    public static int IMAGE_ENGINE_FREQ_LIMITED_MEMECACHE = 4 * 1024 * 1024; // 设置缓存内存的大小,在此设置2M的缓存
    public static int IMAGE_DISK_CACHE_SIZE = 80 * 1024 * 1024;    //	总共的存储的空间
    public static int IMAGE_ENGINE_SCALE_FOR_SDCARD = 1000; // 缓存图片的大小
    public static int IMAGE_ENGINE_SCALE_FOR_MEMECACHE = 1000;

    private int time = 5 * 60 * 1000;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LanguageManager.setLocale(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        activitys = new ArrayList<BaseActivity>();
        MobclickAgent.setDebugMode(isDebug);
        this.registerReceiver(this.myWifiReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        this.registerReceiver(this.myRssiChangeReceiver,
                new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

        initImageLoader(instance);
        if (isOpenLog)
            MobclickAgent.setCatchUncaughtExceptions(!isOpenLog);

        String device_model = Build.MODEL; // 设备型号 。
        String version_sdk = Build.VERSION.SDK; // 设备SDK版本  。
        String version_release = Build.VERSION.RELEASE; // 设备的系统版本 。
        String brand = Build.BRAND;
        String Manufacturer = Build.MANUFACTURER;
        String priduct = Build.PRODUCT;
        String fingerprint = Build.FINGERPRINT;
        String serial = Build.SERIAL;

        System.out.println("device_model*******" + device_model);
        System.out.println("version_sdk*******" + version_sdk);
        System.out.println("version_release*******" + version_release);
        System.out.println("brand*******" + brand);
        System.out.println("Manufacturer*******" + Manufacturer);
        System.out.println("priduct*******" + priduct);

        System.out.println("fingerprint*******" + fingerprint);
        System.out.println("serial*******" + serial);


//		if (Build.VERSION.SDK_INT >= 23) {
//			//校验是否已具有模糊定位权限
//			if (ContextCompat.checkSelfPermission(BaseApplication.this,
//					Manifest.permission.ACCESS_COARSE_LOCATION)
//					!= PackageManager.PERMISSION_GRANTED) {
//				ActivityCompat.requestPermissions(BaseApplication.this,
//						new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//						REQUEST_ENABLE_BT );
//			}else{//权限已打开
//				//  startScan();
//			}
//		}else{//小于23版本直接使用
//			// startScan();
//		}
    }


//	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//		if (requestCode == REQUEST_ENABLE_BT){
//			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//蓝牙权限开启成功
//				//startScan();
//
//				Toast.makeText(BaseApplication.this, "蓝牙权限已开启,请设置", Toast.LENGTH_SHORT).show();
//			}else{
//				Toast.makeText(BaseApplication.this, "蓝牙权限未开启,请设置", Toast.LENGTH_SHORT).show();
//			}
//		}
//		instance.onRequestPermissionsResult(requestCode, permissions, grantResults);
//	}

    public void startUDPService(int index, String serviceName, UdpServiceCallBack udpServiceCallBack) {
        TcpUdpFactory.startUdpServer(index, serviceName, udpServiceCallBack);
    }

    public void searchRevenueIp() {
        TcpUdpFactory.getServiceIp(1, null);
        postHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TcpUdpFactory.getServiceIp(1, null);
            }
        }, 1000);

    }

    protected void wifiPolicyNever() {
        ContentResolver resolver = instance.getContentResolver();
        int value = Settings.System.getInt(resolver, Settings.Global.WIFI_SLEEP_POLICY, Settings.Global.WIFI_SLEEP_POLICY_DEFAULT);
        if (Settings.Global.WIFI_SLEEP_POLICY_NEVER != value) {
            Settings.System.putInt(resolver, Settings.Global.WIFI_SLEEP_POLICY, Settings.Global.WIFI_SLEEP_POLICY_NEVER);

        }
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("BaseActivity", "show");
            RxBus.getInstance().post("showRelogin", null);
        }
    };

    public void startAD() {
        LogUtil.d("BaseActivity", "Remove");
        reLoginHandler.removeCallbacks(runnable);
        LogUtil.d("BaseActivity", "Start");
        reLoginHandler.postDelayed(runnable, time);
    }

    public void stopAD() {
        reLoginHandler.removeCallbacks(runnable);
    }


    private Runnable runnableKpm = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("BaseActivity", "show");
            RxBus.getInstance().post("kpmTime", null);
        }
    };

    public void startADKpm() {
        LogUtil.d("BaseActivity", "Remove");
        reKpmHandler.removeCallbacks(runnableKpm);
        LogUtil.d("BaseActivity", "Start");

        String time = Store.getString(instance, Store.KPM_TIME);
        int timekpm = 0;
        if (!TextUtils.isEmpty(time)) {
            float t = convertToFloat(time, 1.0f);
            timekpm = (int) (t * 60 * 1000);
            if (t > 0) {
                reKpmHandler.postDelayed(runnableKpm, timekpm);
            }
        }

    }


    public static float convertToFloat(String number, float defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void stopADKpm() {
        reKpmHandler.removeCallbacks(runnableKpm);
    }

    @Override
    public void onTerminate() {
        reLoginHandler.removeCallbacks(runnable);

        reKpmHandler.removeCallbacks(runnableKpm);
        super.onTerminate();
    }

    public String getAppVersionName() {
        PackageInfo info;
        String version = "";
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return version;
    }

    protected void update15to16() {
        String update = this.getClass().getSimpleName().toString();
        if (TextUtils.isEmpty(StoreValueSQL.getValue(update))) {
            SharedPreferences sharedPreferences = Store.getSharedPreferences(this);
            Map<String, ?> map = sharedPreferences.getAll();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                StoreValueSQL.updateStore(key, entry.getValue().toString());
            }
            StoreValueSQL.updateStore(update, update);
        }
    }

    public int getAppVersionCode() {
        PackageInfo info;
        int version = 1000;
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = info.versionCode;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return version;
    }

    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, IMAGE_ENGIN_PATH); // 图片持久化位置
        //	部分属性定制 其他都是默认
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                //	设置缓存图片的时候最大宽高
                .memoryCacheExtraOptions(IMAGE_ENGINE_SCALE_FOR_SDCARD, IMAGE_ENGINE_SCALE_FOR_SDCARD)
                .diskCacheExtraOptions(IMAGE_ENGINE_SCALE_FOR_SDCARD, IMAGE_ENGINE_SCALE_FOR_SDCARD, null)
                // 设置线程优先级
                .denyCacheImageMultipleSizesInMemory()
                //	自动缩放的大小
                .memoryCache(new UsingFreqLimitedMemoryCache(IMAGE_ENGINE_FREQ_LIMITED_MEMECACHE))
                .memoryCacheSize(IMAGE_ENGINE_FREQ_LIMITED_MEMECACHE)
                //	设置硬盘缓存大小  80 Mb
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheSize(IMAGE_DISK_CACHE_SIZE)
                // Remove for release app
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    private static Handler APPHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HandlerWhat.NO_NET: {
                    BaseActivity baseActivity = getTopActivity();
                    if (baseActivity != null) {

                    }

                    break;
                }
                case ParamConst.APP_QUIT: {

                }
                break;
                default:
                    break;
            }
        }

        ;
    };

    public static void sendMessage(int what, Object obj) {
        APPHandler.sendMessage(APPHandler.obtainMessage(what, obj));
    }

    public static BaseActivity getTopActivity() {
        if (activitys == null)
            return null;
        if (activitys.size() == 0)
            return null;
        return activitys.get(activitys.size() - 1);
    }

    protected void onAppQuit() {

    }

    /*Fire IP Change Event*/
    protected void onIPChanged() {


    }

    /*Connected or Discounted*/
    protected void onNetworkConnectionUpdate() {


    }

    public static BaseActivity getTopSecondActivity() {
        if (activitys == null)
            return null;
        if (activitys.size() == 0)
            return null;
        return activitys.get(activitys.size() - 2);
    }

    public static BaseActivity getLastActivity() {
        if (activitys == null)
            return null;
        if (activitys.size() == 0)
            return null;
        return activitys.get(0);
    }

    private BroadcastReceiver myRssiChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            wifissi = arg1.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);
        }
    };

    private BroadcastReceiver myWifiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            ConnectivityManager cm = (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                onIPChanged();
            }
//		  ConnectivityManager myConnManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//	      NetworkInfo networkInfo = myConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		  if((networkInfo.getType() == ConnectivityManager.TYPE_WIFI) &&
//				  networkInfo.isConnected()){
//			  onIPChanged();
//		  }
        }
    };

    /**
     * 关闭 ActivityList用 慎用（必须确定目标activity在栈里） 且用，且珍惜！！
     * added by
     *
     * @param cls 关闭到当前activity为止(从栈顶)
     */
    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            BaseActivity currentActivity = getTopActivity();
            if (currentActivity == null) {
                break;
            }
            if (currentActivity.getClass().equals(cls)) {
                break;
            }
            currentActivity.finish();
            activitys.remove(currentActivity);
        }
    }

    /**
     * 关闭 ActivityList用 慎用（必须确定目标activity在栈里） 且用，且珍惜！！
     * added by
     *
     * @param cls 关闭到当前activity为止(从栈底)
     */
    public void finishAllActivityExceptOne(Class cls) {
        while (true) {
            BaseActivity oldActivity = activitys.get(0);
            if (oldActivity == null) {
                break;
            }
            if (oldActivity.getClass().equals(cls)) {
                break;
            }
            oldActivity.finish();
            activitys.remove(oldActivity);
        }
    }


    public void finishTheActivity(Class cls) {
        while (true) {
            BaseActivity oldActivity = activitys.get(0);
            if (oldActivity == null) {
                break;
            }
            if (oldActivity.getClass().equals(cls)) {
                oldActivity.finish();
                activitys.remove(oldActivity);
                return;
            }

        }
    }


    public void finishAllActivity() {
        while (true) {
            BaseActivity oldActivity = getTopActivity();
            if (oldActivity == null) {
                break;
            }
            oldActivity.setResult(Activity.RESULT_CANCELED);
            oldActivity.finish();
            activitys.remove(oldActivity);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        FloatActionController.getInstance().stopMonkServer(this);
    }

    /**
     * PS: return -1时 activity不在栈中
     *
     * @param cls
     * @return
     */
    public int getIndexOfActivity(Class cls) {
        for (BaseActivity baseActivity : activitys) {
            if (baseActivity.getClass().equals(cls)) {
                return activitys.indexOf(baseActivity);
            }
        }
        return -1;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        if (time >= 30 * 1000) {
            this.time = time;
        }
    }

    public static final int HANDLER_REFRESH_LANGUAGE = 772;
    public static final int HANDLER_GET_OTHER_RVC = 773;
    public static final int HANDLER_GET_OTHER_TABLE = 776;
    public static final int HANDLER_TRANSFER_TABLE_TO_OTHER_RVC = 774;
    public static final int HANDLER_TRANSFER_ITEM_TO_OTHER_RVC = 775;

}
