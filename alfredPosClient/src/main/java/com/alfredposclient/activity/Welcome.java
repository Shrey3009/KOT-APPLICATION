package com.alfredposclient.activity;

import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.http.DownloadFactory;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.LoginResult;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.model.AppVersion;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.PushMessage;
import com.alfredbase.javabean.model.VersionCheck;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.BohHoldSettlementSQL;
import com.alfredbase.store.sql.CardsSettlementSQL;
import com.alfredbase.store.sql.NetsSettlementSQL;
import com.alfredbase.store.sql.NonChargableSettlementSQL;
import com.alfredbase.store.sql.PaymentSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.VoidSettlementSQL;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.floatwindow.float_lib.FloatActionController;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Welcome extends BaseActivity {
	private View rootView;
	private IntentFilter downFilter;
	private IntentFilter filter;
	private DownloadManager downManager;
	private LoadingDialog loadingDialog;
	private int size = 0;
	private PackageInfo pi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initviewWelcom();
		ScreenSizeUtil.initScreenScale(context);
		rootView = LayoutInflater.from(context).inflate(
				R.layout.activity_welcome, null);
		setContentView(rootView);
		try {
			pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectAnimator anim = ObjectAnimator.ofFloat(rootView, "alpha", 0f, 1f)
				.setDuration(2000);
		anim.start();
		loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle(context.getString(R.string.downloading));
		TextTypeFace.getInstance().init(context);
//		check();
		BaseApplication.postHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(App.instance.getPosType() == 1){
					startSubPosNextActivity();
				}else {
					startNextActivity();
				}
			}
		}, 2000);
		downFilter = new IntentFilter();
		downFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downReceiver, downFilter);
		filter = new IntentFilter();
		filter.addDataScheme("package");
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		registerReceiver(receiver, filter);
		downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        BugseeHelper.trace("APP", getString(R.string.app_name));
         int trainType= SharedPreferencesHelper.getInt(context,SharedPreferencesHelper.TRAINING_MODE);
         if(trainType==1){
            App.instance.getSystemSettings().setTraining(trainType);
         }else {
             App.instance.getSystemSettings().setTraining(0);
         }

	}

	private boolean updateData(){
		Long businessDate = (Long) Store.getLong(this, Store.BUSINESS_DATE);
		if(pi != null
				&& Store.getBoolean(context, pi.versionName, false)
				&& businessDate == Store.DEFAULT_LONG_TYPE
				&& App.instance.getRevenueCenter() != null){

				AppVersion localVersion = new AppVersion(pi.versionName);
				boolean updateData = Store.getBoolean(this, localVersion.getVersion(), false);
				if(updateData){
                DialogFactory.compulsoryUpdateDialog(context, this.getString(R.string.warning), this.getString(R.string.version_requires_update), new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Map<String, Integer> map = new HashMap<String, Integer>();
							map.put(PushMessage.HAPPY_HOURS, 1);
							map.put(PushMessage.PRINTER, 1);
							map.put(PushMessage.ITEM, 1);
							map.put(PushMessage.MODIFIER, 1);
							map.put(PushMessage.USER, 1);
							map.put(PushMessage.PLACE_TABLE, 1);
							map.put(PushMessage.TAX, 1);
							syncDataAction(map);
						}
					});
				}
				return true;
		}else {
			return false;
		}
	}

	private void startNextActivity(){
		String str = Store.getString(
				context, Store.SYNC_DATA_TAG);
		App.instance.bindSyncService();
		App.instance.connectRemotePrintService();
		int time = Store.getInt(App.instance, Store.RELOGIN_TIME);
		App.instance.setTime(time);
		if (TextUtils.isEmpty(str)) {// 认为没有同步过服务器数据
			UIHelp.startSyncData(context);
			this.finish();
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {

					CoreData.getInstance().init(context);
					App.instance.setLocalRestaurantConfig(CoreData.getInstance().getRestaurantConfigs());
					App.instance.initKdsAndPrinters();
					MainPosInfo mps = Store.getObject(context, Store.MAINPOSINFO, MainPosInfo.class);
					checkAndUpdateMainPOS(mps);
					clearNoActivePaymentSettlement();
					context.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							UIHelp.startLogin(context);
							context.finish();
						}
					});
				}
			}).start();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

    private void startSubPosNextActivity() {
        App.instance.bindSyncService();
        SubPosBean subPosBean = App.instance.getSubPosBean();
        App.instance.connectRemotePrintService();
        int time = Store.getInt(App.instance, Store.RELOGIN_TIME);
        App.instance.setTime(time);
        if (subPosBean == null) {// 认为没有同步过服务器数据
            UIHelp.startSelectRevenu(Welcome.this);
            this.finish();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CoreData.getInstance().init(context);
                    App.instance.setLocalRestaurantConfig(CoreData.getInstance().getRestaurantConfigs());
                    App.instance.initKdsAndPrinters();
//					MainPosInfo mps = Store.getObject(context, Store.MAINPOSINFO, MainPosInfo.class);
//					checkAndUpdateMainPOS(mps);
                    clearNoActivePaymentSettlement();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIHelp.startSubPosLogin(context);
                            context.finish();
                        }
                    });
                }
            }).start();
        }

    }

    boolean isPosDownloaded = false;
    boolean isPrinterDownloaded = false;
    long posUpdateId = -1;
    private BroadcastReceiver downReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//			if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            posUpdateId = Store.getLong(context, "posUpdateId");
            long printerUpdateId = Store.getLong(context, "printerUpdateId");
            if (id == posUpdateId) {
                isPosDownloaded = true;
            }
            if (id == printerUpdateId) {
                isPrinterDownloaded = true;
            }
            if (isPosDownloaded && isPrinterDownloaded) {
                DownloadManager.Query query = new DownloadManager.Query();
                Cursor cursor = downManager.query(query);
                while (cursor.moveToNext()) {
                    long downId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL && downId == printerUpdateId) {
                        String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (!TextUtils.isEmpty(address))
                            handler.sendMessage(handler.obtainMessage(DownloadFactory.EVENT_DOWNLOAD_INSTALL, address));
                    }
                }
                cursor.close();
            }

//			}
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packName = intent.getDataString().substring(8);
            //packName为所安装的程序的包名
            String name = context.getResources().getString(R.string.printer_app_name);
            if (packName.equals("com.alfred.remote.printservice")) {
                DownloadManager.Query query = new DownloadManager.Query();
                Cursor cursor = downManager.query(query);
                while (cursor.moveToNext()) {
                    long downId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL && downId == posUpdateId) {
                        String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (!TextUtils.isEmpty(address))
                            handler.sendMessage(handler.obtainMessage(DownloadFactory.EVENT_DOWNLOAD_INSTALL, address));
                    }
                }
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DownloadFactory.EVENT_DOWNLOAD_INSTALL:
                    loadingDialog.dismiss();
                    String address = (String) msg.obj;
                    DownloadFactory.installApk(context, address);
                    break;
                case ResultCode.SUCCESS:
                    size = size - 1;
                    if (size == 0) {
                        loadingDialog.dismiss();
                        Store.remove(context, Store.PUSH_MESSAGE);
                        Store.remove(context, pi.versionName);
                        App.instance.setPushMsgMap(new HashMap<String, Integer>());
                        startNextActivity();
                    }
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    size = 0;
                    if (ButtonClickTimer.canClick())
                        UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                                context.getResources().getString(R.string.server)));
                    updateData();
                    break;

                default:
                    break;

            }
        }

        ;
    };


	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}

	@Override
	protected void onResume() {
		FloatActionController.getInstance().stopMonkServer(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(downReceiver);
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	private void checkAndUpdateMainPOS(MainPosInfo oldPosSetting) {
		String ip = CommonUtil.getLocalIpAddress();
		if (!ip.equals(oldPosSetting.getIP())) {
			oldPosSetting.setIP(ip);
			Store.saveObject(context, Store.MAINPOSINFO, oldPosSetting);
		}
	    App.instance.setMainPosInfo(oldPosSetting);
	}

	private void clearNoActivePaymentSettlement(){
		Long businessDate = (Long) Store.getLong(this, Store.BUSINESS_DATE);
		if(businessDate != null && businessDate != Store.DEFAULT_LONG_TYPE){
			List<Payment> payments = PaymentSQL.getPaymentFromNoActivePaymentSettlement(businessDate);
			if(!payments.isEmpty()){
				for(Payment payment : payments){
					PaymentSettlementSQL.regainPaymentSettlementByPayment(payment);
					CardsSettlementSQL.regainCardsSettlementByPayment(payment);
					BohHoldSettlementSQL.regainBohHoldSettlementByPayment(payment);
					VoidSettlementSQL.regainVoidSettlementByPayment(payment);
					NonChargableSettlementSQL.regainNonChargableSettlementByPayment(payment);
					NetsSettlementSQL.regainNetsSettlementByPayment(payment);
				}
			}
		}

	}

    private void check() {
        String value = MobclickAgent.getConfigParams(this, "version_check");
        if (App.countryCode == ParamConst.CHINA) {
            value = MobclickAgent.getConfigParams(this, "version_checkCN");
        }
        Gson gson = new Gson();
        final VersionCheck versionCheck = gson.fromJson(value, VersionCheck.class);
        int isCheck = 1 / (versionCheck.getDown() + 1);
    }

    private boolean checkVersion() {
        Long businessDate = (Long) Store.getLong(this, Store.BUSINESS_DATE);
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
//				return false;
            long posUpdateId = downManager.enqueue(DownloadFactory.downloadFile(context, versionCheck.getPosUrl(), getResources().getString(R.string.app_name)));
            Store.putLong(context, "posUpdateId", posUpdateId);
            long printerUpdateId = downManager.enqueue(DownloadFactory.downloadFile(context, versionCheck.getPrinterUrl(), getResources().getString(R.string.printer_app_name)));
            Store.putLong(context, "printerUpdateId", printerUpdateId);
            loadingDialog.show();
            return true;
        } else {
            if (businessDate != Store.DEFAULT_LONG_TYPE) {
                return false;
            }
            if (!TextUtils.isEmpty(versionCheck.getAppVersion()) && !TextUtils.isEmpty(versionCheck.getPosUrl()) && !TextUtils.isEmpty(versionCheck.getPrinterUrl())) {
                AppVersion newAppVersion = new AppVersion(versionCheck.getAppVersion());
                AppVersion localVersion = new AppVersion(pi.versionName);
                if (localVersion.compare(newAppVersion) == 1) {
                    DialogFactory.commonOneTimeUpdateDialog(context, context.getResources().getString(R.string.app_update),
                            context.getResources().getString(R.string.app_update_explain), context.getResources().getString(R.string.no),
                            context.getResources().getString(R.string.yes),
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    startNextActivity();
                                }
                            },
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    Store.putBoolean(context, versionCheck.getAppVersion(), versionCheck.isUpdateData());
                                    long posUpdateId = downManager.enqueue(DownloadFactory.downloadFile(context, versionCheck.getPosUrl(), getResources().getString(R.string.app_name)));
                                    Store.putLong(context, "posUpdateId", posUpdateId);
                                    long printerUpdateId = downManager.enqueue(DownloadFactory.downloadFile(context, versionCheck.getPrinterUrl(), getResources().getString(R.string.printer_app_name)));
                                    Store.putLong(context, "printerUpdateId", printerUpdateId);
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
    }

    private void syncDataAction(Map<String, Integer> syncMap) {
        loadingDialog.setTitle(context.getString(R.string.update));
        loadingDialog.show();
        CoreData.getInstance().setLoginResult(Store
                .getObject(context, Store.LOGIN_RESULT, LoginResult.class));
        for (String key : syncMap.keySet()) {
            size = size + 1;
            SyncCentre.getInstance().pushCommonData(context, key, handler);
        }
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("revenueId", App.instance.getRevenueCenter().getId().intValue());
//		SyncCentre.getInstance().getPlaceTable(context, map, handler);
    }

}
