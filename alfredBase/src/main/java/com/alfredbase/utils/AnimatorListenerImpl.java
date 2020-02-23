package com.alfredbase.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

/**
 * AnimatorListener的一个简单实现，添加了isRunning变量，用于判断是否有动画正在运行，用于一些动画冲突的场景
 * 
 * @author
 * 
 */
public class AnimatorListenerImpl implements AnimatorListener {

	public static boolean isRunning = false;

	@Override
	public void onAnimationStart(Animator animation) {
		LogUtil.e("AnimatorListener---","onAnimationStart");
		isRunning = true;

	}

	@Override
	public void onAnimationEnd(Animator animation) {
		LogUtil.e("AnimatorListener---","onAnimationEnd");
		isRunning = false;
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		LogUtil.e("AnimatorListener---","onAnimationCancel");
	isRunning = false;
	}

	@Override
	public void onAnimationRepeat(Animator animation) {


		LogUtil.e("AnimatorListener---","onAnimationRepeat");
		isRunning = true;
	}

}
