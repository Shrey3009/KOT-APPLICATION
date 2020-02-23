package com.alfredposclient.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredposclient.R;
import com.alfredposclient.global.UIHelp;

public class SlideUnlockView extends RelativeLayout {
	private TextView tv_swipe;
	private float initX = -1;
	private float mEventDownX;
	private int initW = -1;
	private UnLock unLock;
	private boolean isUnLock = false;
	private Context context;
	float downX;
	float downY;

	public SlideUnlockView(Context context) {
		super(context);
		init(context);
	}

	public SlideUnlockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		View.inflate(context, R.layout.slide_unlock, this);
		tv_swipe = (TextView) findViewById(R.id.tv_swipe);
	}

	public void setUnLock(UnLock unLock) {
		this.unLock = unLock;
	}

	public void setUnLock(boolean isUnLock) {
		this.isUnLock = isUnLock;
		if (isUnLock) {
			tv_swipe.setVisibility(View.GONE);
		} else {
			tv_swipe.setVisibility(View.VISIBLE);
		}
	}

	public void setViewShow(boolean isShow) {
		if (isShow) {
			tv_swipe.setVisibility(View.VISIBLE);
			tv_swipe.setX(tv_swipe.getWidth());
		} else {
			tv_swipe.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isUnLock) {
			if (isUnLock) {
				return super.onTouchEvent(event);
			}
			final int action = event.getActionMasked();
			if (initX == -1) {
				initX = tv_swipe.getX();
			}
			if (initW == -1) {
				initW = tv_swipe.getWidth();
			}

			switch (action) {
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_DOWN:
				mEventDownX = event.getX();
				break;

			case MotionEvent.ACTION_MOVE:
				handleMove(event);
				break;

			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_UP:
				handleUp(event);
				break;

			case MotionEvent.ACTION_CANCEL:
				break;

			}
			invalidate();
			return true;
		} else if (isUnLock && tv_swipe.getVisibility() == View.VISIBLE) {
			if (!isUnLock) {
				return super.onTouchEvent(event);
			}
			final int action = event.getActionMasked();
			if (initX == -1) {
				initX = tv_swipe.getX();
			}
			if (initW == -1) {
				initW = tv_swipe.getWidth();
			}
			switch (action) {
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_DOWN:
				mEventDownX = event.getX();
				break;

			case MotionEvent.ACTION_MOVE:
				handleMoveClose(event);
				break;

			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_UP:
				handleClose(event);
				break;

			case MotionEvent.ACTION_CANCEL:
				break;

			}
			invalidate();
			return true;
		} else {
			int action = event.getActionMasked();

			switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				break;

			case MotionEvent.ACTION_UP:
				if (downX - event.getX() > -5 && downX - event.getX() < 5
						&& downY - event.getY() > -5
						&& downY - event.getY() < 5) {
					UIHelp.startMainPage(context);
				}
				break;

			}
			return true;
		}

	}

	

	private void handleUp(MotionEvent event) {
		if (event.getX() < mEventDownX)// 只能向右滑动
			return;
		if (event.getX() - mEventDownX >= initW / 2) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(tv_swipe, "x",
					tv_swipe.getX(), initX + initW).setDuration(300);
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					isUnLock = true;
					initX = -1;
					initW = -1;
					if (unLock != null)
						unLock.unLock();
				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});
			animator.start();
		} else {
			ObjectAnimator.ofFloat(tv_swipe, "x", tv_swipe.getX(), initX)
					.setDuration(300).start();
		}
	}

	private void handleMove(MotionEvent event) {
		if (event.getX() > mEventDownX)// 只能向右滑动
			tv_swipe.setX(event.getX() - mEventDownX);
	}

	private void handleMoveClose(MotionEvent event) {
		if (event.getX() < mEventDownX && tv_swipe.getX() > 0)// 只能向左滑动
			tv_swipe.setX(initX - (mEventDownX - event.getX()));
	}

	private void handleClose(MotionEvent event) {

		if (event.getX() > mEventDownX)// 只能向左滑动
			return;
		if (mEventDownX - event.getX() >= initW / 2) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(tv_swipe, "x",
					tv_swipe.getX(), 0).setDuration(300);
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					isUnLock = false;
					initX = -1;
					initW = -1;
					if (unLock != null)
						unLock.lock();
				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});
			animator.start();
		} else {
			ObjectAnimator.ofFloat(tv_swipe, "x", tv_swipe.getX(), initX)
					.setDuration(300).start();
		}

	}

	public interface UnLock {
		void unLock();

		void lock();
	}

}
