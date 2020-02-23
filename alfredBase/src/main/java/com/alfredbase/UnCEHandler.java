package com.alfredbase;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;

public class UnCEHandler implements UncaughtExceptionHandler {

	private Thread.UncaughtExceptionHandler mDefaultHandler;
	public static final String TAG = "CatchExcep";
	private BaseApplication application;
	private Class wellCommActivity;

	public UnCEHandler(BaseApplication application, Class wellCommActivity) {
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		this.application = application;
		this.wellCommActivity = wellCommActivity;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				CommonUtil.showToast(application.getBaseContext(),
						"System is geting an error. Restart soon.");
				Looper.loop();
			}
		}.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LogUtil.e(TAG, "error : " + e);
		}
		Intent intent = new Intent(application.getApplicationContext(),
				wellCommActivity);
		PendingIntent restartIntent = PendingIntent.getActivity(
				application.getApplicationContext(), 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		// 退出程序
		AlarmManager mgr = (AlarmManager) application
				.getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
				restartIntent); // 1秒钟后重启应用
		mDefaultHandler.uncaughtException(thread, ex);
		application.finishAllActivity();

	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
//	private boolean handleException(Throwable ex) {
//		// if(application.isDebug){
//		// return false;
//		// }
//		if (ex == null) {
//			return false;
//		}
//		// 使用Toast来显示异常信息
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				CommonUtil.showToast(application.getApplicationContext(),
//						"System is geting an error. Restart soon.");
//				Looper.loop();
//			}
//		}.start();
//		return true;
//	}
}
