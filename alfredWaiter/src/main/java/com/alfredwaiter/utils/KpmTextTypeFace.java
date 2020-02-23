package com.alfredwaiter.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class KpmTextTypeFace {
	private Typeface ubuntuMedium  = null;
	private Typeface ubuntuBold = null;
	private Typeface srirachaRegular = null;
	private Typeface regular = null;

	private Typeface ubuntuRegular=null;

	private static KpmTextTypeFace instance;
	private KpmTextTypeFace() {
	}

	public static KpmTextTypeFace getInstance() {
		if (instance == null) {
			instance = new KpmTextTypeFace();
		}
		return instance;
	}
	public void init(Context mActivity){
		if (srirachaRegular == null) {
			srirachaRegular = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/Sriracha-Regular.ttf");
		}
		if (regular == null) {
			regular = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/regular.otf");
		}
		if (ubuntuBold == null) {
			ubuntuBold = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/Ubuntu-Bold.ttf");
		}
		if (ubuntuMedium == null) {
			ubuntuMedium = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/Ubuntu-Medium.ttf");
		}

		if (ubuntuRegular == null) {
			ubuntuRegular = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/Ubuntu-Regular.ttf");
		}

	}


	public void setUbuntuRegular(TextView textView) {
		if (textView != null && ubuntuRegular != null) {
			textView.setTypeface(ubuntuRegular);
		}
	}
	public void setUbuntuMedium(TextView textView) {
		if (textView != null && ubuntuMedium != null) {
			textView.setTypeface(ubuntuMedium);
		}
	}
	
	public void setUbuntuBold(TextView textView){
		if (textView != null && ubuntuBold != null) {
			textView.setTypeface(ubuntuBold);
		}
	}
	
	public void setSrirachaRegular(TextView textView){
		if (textView != null && srirachaRegular != null) {
			textView.setTypeface(srirachaRegular);
		}
	}
	public void setRegular(TextView textView){
		if (textView != null && regular != null) {
			textView.setTypeface(regular);
		}
	}
}
