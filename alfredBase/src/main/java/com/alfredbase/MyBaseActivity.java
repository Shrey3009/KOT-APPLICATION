package com.alfredbase;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alfredbase.javabean.TableInfo;
import com.alfredbase.store.Store;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.RxBus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

public class MyBaseActivity extends FragmentActivity implements OnClickListener {
	protected MyBaseActivity context;
	protected Dialog compelDialog;
	protected Dialog oneButtonCompelDialog;
	public LoadingDialog loadingDialog;
	protected NotificationManager mNotificationManager;
	protected static DisplayImageOptions display = new DisplayImageOptions.Builder() // 圆角边处理的头像
	.cacheInMemory(true) // 缓存到内存，设置true则缓存到内存
	.cacheOnDisk(true) // 缓存到本地磁盘,设置true则缓存到磁盘
//	.showImageForEmptyUri(R.drawable.image_default) // 设置尚未加载，或者无图片的默认图片
//	.showImageOnFail(R.drawable.image_default) // 设置加载图片失败时的默认图片
	.displayer(new RoundedBitmapDisplayer(0)) // 可以继承BitmapDisplayer接口来实现bitmap的其他特效，再此非0是实现圆角边特效
	.build();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		Window window = getWindow();
//		WindowManager.LayoutParams params = window.getAttributes();
//		params.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//				| View.SYSTEM_UI_FLAG_FULLSCREEN
////				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//		window.setAttributes(params);
//		//状态栏 @ 顶部
//		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//A
//		//导航栏 @ 底部
//		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//B
		super.onCreate(savedInstanceState);
		context = this;

		initView();

	}


	protected void initView() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}


	/**
	 * 1.对原始点击事件做了一层封装，在500毫秒内，不应该处理两次或者两次以上的点击
	 *
	 * 2.不应该复写这个方法，而是复写handlerClickEvent
	 */
	@Override
	public void onClick(View v) {
		if (ButtonClickTimer.canClick(v)) {
			handlerClickEvent(v);
		}
	}

	protected void handlerClickEvent(View v) {

	}


}
