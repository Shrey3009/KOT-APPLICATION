package com.alfredposclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class HorizontalScrollViewEx extends HorizontalScrollView {
	public int itemW;

	public HorizontalScrollViewEx(Context context) {

		super(context);
	}

	public HorizontalScrollViewEx(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public HorizontalScrollViewEx(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	/**
	 * 消除惯性
	 */
	@Override
	public void fling(int velocityX) {
		// super.fling(velocityX);
	}

}