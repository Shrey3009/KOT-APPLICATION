package com.alfredselfhelp.activity;

import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.AppVersion;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.VersionCheck;
import com.alfredbase.store.Store;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredselfhelp.R;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.utils.UIHelp;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;

public class Welcome extends BaseActivity {
    private View rootView;
    private DownloadManager downManager;
    private IntentFilter downFilter;

    @Override
    protected void initView() {
        super.initView();
        ScreenSizeUtil.initScreenScale(context);
        rootView = LayoutInflater.from(context).inflate(
                R.layout.activity_welcome, null);
        setContentView(rootView);
        ScreenSizeUtil.initScreenScale(context);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle(this.getResources().getString(R.string.downloading));
        downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(rootView, "alpha", 0f, 1f)
                .setDuration(1300);
        anim.start();
        TextTypeFace.getInstance().init(context);
        CoreData.getInstance().init(context);
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!checkVersion()) {
                    startNextActivity();
                }
            }
        }, 1300);

        downFilter = new IntentFilter();
        downFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downReceiver, downFilter);

        byte[] a ={0x52, 0x32, 0x30, 0x30, 0x30, 0x34, 0x31, 0x32, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                0x30, 0x30, 0x30, 0x31, 0x30, 0x30, 0x35, 0x37, 0x30, 0x36, 0x30, 0x30, 0x30, 0x30,
                0x37, 0x35, 0x36, 0x31, 0x32, 0x30, 0x30, 0x30, 0x31, 0x30, 0x39, 0x30, 0x34, 0x30,
                0x31, 0x31, 0x32, 0x31, 0x32, 0x31, 0x32, 0x30, 0x30, 0x30, 0x30, 0x31, 0x30, 0x32,
                0x31, 0x36, 0x35, 0x34, 0x31, 0x33, 0x33, 0x33, 0x30, 0x30, 0x38, 0x39, 0x30, 0x31,
                0x30, 0x35, 0x35, 0x38, 0x30, 0x37, 0x31, 0x30, 0x30, 0x34, 0x30, 0x31, 0x31, 0x32,
                0x31, 0x32, 0x31, 0x32, 0x31, 0x34, 0x30, 0x34, 0x31, 0x30, 0x31, 0x30, 0x33, 0x37,
                0x31, 0x32, 0x31, 0x32, 0x31, 0x32, 0x31, 0x32, 0x31, 0x32, 0x31, 0x32, 0x31, 0x32,
                0x33, 0x38, 0x30, 0x36, 0x30, 0x30, 0x30, 0x30, 0x31, 0x31, 0x33, 0x39, 0x30, 0x32,
                0x30, 0x30, 0x36, 0x32, 0x30, 0x36, 0x30, 0x30, 0x30, 0x30, 0x32, 0x32, 0x39, 0x38,
                0x30, 0x34, 0x41, 0x6C, 0x65, 0x78, 0x35, 0x35, 0x30, 0x31, 0x4D};
//        byte[] a = {0x43, 0x36, 0x31, 0x30, 0x30, 0x34, 0x31, 0x32, 0x30, 0x30, 0x30, 0x30, 0x30,
//                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x35, 0x37, 0x30, 0x36, 0x30, 0x30, 0x30, 0x31, 0x37, 0x33};
//        byte[] a = {0x52, 0x36, 0x31, 0x30, 0x30, 0x32, 0x31, 0x36, 0x31, 0x38, 0x34, 0x31, 0x36, 0x35, 0x31, 0x30, 0x34,
//                0x31, 0x30, 0x30, 0x30, 0x31, 0x30, 0x37, 0x31, 0x30, 0x30, 0x31, 0x34, 0x30, 0x34, 0x58, 0x58, 0x58, 0x58,
//                0x33, 0x31, 0x30, 0x30, 0x30, 0x31, 0x34, 0x33, 0x38, 0x30, 0x30, 0x32, 0x30, 0x30, 0x34, 0x31, 0x30, 0x38,
//                0x35, 0x31, 0x35, 0x35, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x34, 0x30, 0x35, 0x45, 0x5A, 0x4C, 0x2D, 0x50,
//                0x35, 0x30, 0x30, 0x31, 0x37, 0x33, 0x36, 0x33, 0x33, 0x36, 0x33, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
//                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x32, 0x32, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x38, 0x30, 0x30, 0x34,
//                0x35, 0x32, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x32, 0x31, 0x36, 0x30, 0x39, 0x35, 0x34, 0x33,
//                0x37, 0x37, 0x31, 0x32, 0x31, 0x31, 0x30, 0x32, 0x31, 0x36, 0x36, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x33,
//                0x39, 0x30, 0x30, 0x30, 0x30, 0x31, 0x36, 0x36, 0x34, 0x32, 0x31, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x37,
//                0x35 ,0x35, 0x30, 0x31, 0x45, 0x35, 0x37, 0x30, 0x36, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
//                0x33, 0x30, 0x30, 0x31, 0x31, 0x30, 0x30, 0x30, 0x30, 0x30 };
        Log.e("TAG", "new:" + new String(a));
        String sales = "C200";
        String amount = "0412%012d";
        String identifier = "5706%06d";
        String trace = "612000109040112121200001";
        String msg = "100";
        StringBuffer str = new StringBuffer(sales);
        str.append(String.format(Locale.US,amount, Integer.parseInt(msg)));
        str.append(String.format(Locale.US,identifier, 75));
        msg = str.append(trace).toString();
        Log.e("TAG", "msg:" + msg);

        BugseeHelper.trace("APP", getString(R.string.app_name));

        try {
            byte[] msgB = msg.getBytes();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0; i < msgB.length; i++){

                String hex = Integer.toHexString(msgB[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                stringBuffer.append(hex+" ");
            }
            Log.e("TAG",  stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        App.instance.finishAllActivityExceptOne(Welcome.class);
    }


    private BroadcastReceiver downReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                DownloadManager.Query query = new DownloadManager.Query();
                Cursor cursor = downManager.query(query);
                while (cursor.moveToNext()) {
                    long downId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL && downId == id) {
                        String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (!TextUtils.isEmpty(address))
                            handler.sendMessage(handler.obtainMessage(DownloadFactory.EVENT_DOWNLOAD_INSTALL, address));
                    }
                }
                cursor.close();
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DownloadFactory.EVENT_DOWNLOAD_INSTALL:
                    loadingDialog.dismiss();
                    String address = (String) msg.obj;
                    DownloadFactory.installApk(Welcome.this, address);
                    break;

                default:
                    break;
            }
        }

        ;
    };


    private void startNextActivity() {

        MainPosInfo mainPosInfo = App.instance.getMainPosInfo();
        User user = Store.getObject(context, Store.WAITER_USER, User.class);
        App.instance.connectRemotePrintService();
        App.instance.loadPrinters();
        if (TextUtils.isEmpty(App.instance.getPosIp())) {
            UIHelp.startSelectRevenue(context);
            finish();
        } else {
            UIHelp.startEmployeeID(context);
            finish();
        }
//		if (mainPosInfo == null) {
//			UIHelp.startSelectRevenue(context);
////			UIHelp.startConnectPOS(context);
//			finish();
//		} else if (user == null) {
//			App.instance.setPairingIp(mainPosInfo.getIP());
//			UIHelp.startEmployeeID(context);
//			finish();
//		} else{
//			App.instance.setPairingIp(mainPosInfo.getIP());
//			UIHelp.startLogin(context);
//			finish();
//		}
    }

    private boolean checkVersion() {
        try {
            String value = MobclickAgent.getConfigParams(this, "version_check");
            if (App.countryCode == ParamConst.CHINA) {
                value = MobclickAgent.getConfigParams(this, "version_checkCN");
            }
            Gson gson = new Gson();
            final VersionCheck versionCheck = gson.fromJson(value, VersionCheck.class);
            if (versionCheck.getStatus() == -1) {
                return false;
            }
            if (versionCheck.isForce()) {
                return false;
            } else {
                if (!TextUtils.isEmpty(versionCheck.getAppVersion()) && !TextUtils.isEmpty(versionCheck.getPosUrl()) && !TextUtils.isEmpty(versionCheck.getPrinterUrl())) {
                    AppVersion newAppVersion = new AppVersion(versionCheck.getAppVersion());
                    PackageInfo pi = context.getPackageManager().getPackageInfo(
                            context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
                    AppVersion localVersion = new AppVersion(pi.versionName);
                    if (localVersion.compare(newAppVersion) == 1) {
                        DialogFactory.commonOneTimeUpdateDialog(Welcome.this, Welcome.this.getResources().getString(R.string.app_update),
                                Welcome.this.getResources().getString(R.string.app_update_explain), Welcome.this.getResources().getString(R.string.no),
                                Welcome.this.getResources().getString(R.string.yes),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        startNextActivity();
                                    }
                                },
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        downManager.enqueue(DownloadFactory.downloadFile(Welcome.this, versionCheck.getPosUrl(), getResources().getString(R.string.app_name)));
                                        loadingDialog.show();
                                    }
                                });
                        return true;
                    }
                    return false;
                } else {
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(downReceiver);
        super.onDestroy();
    }
}
