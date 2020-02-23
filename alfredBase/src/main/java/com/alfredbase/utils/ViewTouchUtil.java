package com.alfredbase.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

public class ViewTouchUtil {
	/**
	 * 自定义扩大view的范围
	 * 
	 * @param view
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 */
	public static void expandViewTouchDelegate(final View view, final int top,
			final int bottom, final int left, final int right) {

		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);

				bounds.top -= top;
				bounds.bottom += bottom;
				bounds.left -= left;
				bounds.right += right;

				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}
	
	
	public static void expandViewTouchDelegate(final View view, final int bottom) {

		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);

				bounds.top -= 0;
				bounds.bottom = bounds.bottom * 5;
				bounds.left -= 0;
				bounds.right += 0;

				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

	/**
	 * 扩大view的点击范围 左右上下都为10；
	 * 
	 * @param view
	 */
	public static void expandViewTouchDelegate(final View view) {

		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);

				bounds.top -= 10;
				bounds.bottom += 10;
				bounds.left -= 10;
				bounds.right += 10;

				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

	/**
	 * 还原View的触摸和点击响应范围,最小不小于View自身范围
	 * 
	 * @param view
	 */
	public static void restoreViewTouchDelegate(final View view) {

		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				bounds.setEmpty();
				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

}
