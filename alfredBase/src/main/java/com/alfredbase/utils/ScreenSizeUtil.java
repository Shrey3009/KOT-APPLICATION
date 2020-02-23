package com.alfredbase.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class ScreenSizeUtil {
	public static double width = 480;

	public static double height = 800;
	
	public static final double WIDTH_POS = 1600;
	
	public static final double HEIGHT_POS = 900;

	public static void initScreenScale(Activity mActivity) {
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		LogUtil.d("ScreenSizeUtil", width + "---" + height + "--------" + dm.density + "-------" +dm.ydpi +"-------" +dm.xdpi);
		
	}

	public static int getStatusBarHeight(Activity mActivity) {
		Rect frame = new Rect();
		mActivity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Activity mActivity, float dpValue) {
		final float scale = mActivity.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Activity mActivity, float pxValue) {
		final float scale = mActivity.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
