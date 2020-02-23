package com.alfredwaiter.activity;

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
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.UIHelp;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

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
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setTitle(this.getResources().getString(R.string.downloading));
		downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
		ObjectAnimator anim = ObjectAnimator.ofFloat(rootView, "alpha", 0f, 1f)
				.setDuration(1300);
		anim.start();
		TextTypeFace.getInstance().init(context);
		rootView.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!checkVersion()){
					startNextActivity();
				}
			}
		}, 1300);

		BugseeHelper.trace("APP", getString(R.string.app_name));
		
		downFilter = new IntentFilter();
		downFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downReceiver, downFilter);
		App.instance.finishAllActivityExceptOne(Welcome.class);
	}

	
	private BroadcastReceiver downReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
				long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				DownloadManager.Query query = new DownloadManager.Query();
				Cursor cursor= downManager.query(query);
				while(cursor.moveToNext()){
					long downId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
					int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
					if(status == DownloadManager.STATUS_SUCCESSFUL && downId == id){
						String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
						if(!TextUtils.isEmpty(address))
							handler.sendMessage(handler.obtainMessage(DownloadFactory.EVENT_DOWNLOAD_INSTALL, address));
					}
				}
				cursor.close();
			}
		}
	};
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadFactory.EVENT_DOWNLOAD_INSTALL:
				loadingDialog.dismiss();
				String address = (String)msg.obj;
				DownloadFactory.installApk(Welcome.this, address);
				break;

			default:
				break;
			}
		};
	};
	
	
	private void startNextActivity() {
		CoreData.getInstance().init(context);
		MainPosInfo mainPosInfo = App.instance.getMainPosInfo();
		User user = Store.getObject(context, Store.WAITER_USER, User.class);
		if (mainPosInfo == null) {
			UIHelp.startSelectRevenue(context);
//			UIHelp.startConnectPOS(context);
			finish();
		} else if (user == null) {
			App.instance.setPairingIp(mainPosInfo.getIP());
			UIHelp.startEmployeeID(context);
			finish();
		} else{
			App.instance.setPairingIp(mainPosInfo.getIP());
			UIHelp.startLogin(context);
			finish();
		}
	}
	
	private boolean checkVersion() {
		try {
			String value = MobclickAgent.getConfigParams(this, "version_check");
			if(App.countryCode == ParamConst.CHINA){
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
				if (!TextUtils.isEmpty(versionCheck.getAppVersion()) && !TextUtils.isEmpty(versionCheck.getPosUrl()) &&  !TextUtils.isEmpty(versionCheck.getPrinterUrl())) {
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
				}else{
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
