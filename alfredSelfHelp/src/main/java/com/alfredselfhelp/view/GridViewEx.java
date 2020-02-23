package com.alfredselfhelp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class GridViewEx extends GridView {

	public GridViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 为了全部展示，定义高度
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	// 屏蔽滑动事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
}
