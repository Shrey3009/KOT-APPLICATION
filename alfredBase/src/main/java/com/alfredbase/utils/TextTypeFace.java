package com.alfredbase.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class TextTypeFace {
	private Typeface myFont = null;
	private Typeface trajanProBlod = null;
	private Typeface trajanProRegular = null;
	private static TextTypeFace instance;
	private TextTypeFace() {
	}

	public static TextTypeFace getInstance() {
		if (instance == null) {
			instance = new TextTypeFace();
		}
		return instance;
	}
	public void init(Context mActivity){
		if (trajanProBlod == null) {
			trajanProBlod = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/TrajanProBold.otf");
		}
		if (trajanProRegular == null) {
			trajanProRegular = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/TrajanProRegular.otf");
		}
		if (myFont == null) {
			myFont = Typeface.createFromAsset(mActivity.getAssets(),
					"fonts/SansitaOne.ttf");
		}
	}
	public void setTypeface(TextView textView) {
		if (textView != null && myFont != null) {
			textView.setTypeface(myFont);
		}
	}
	
	public void setTrajanProBlod(TextView textView){
		if (textView != null && trajanProBlod != null) {
			textView.setTypeface(trajanProBlod);
		}
	}
	
	public void setTrajanProRegular(TextView textView){
		if (textView != null && trajanProRegular != null) {
			textView.setTypeface(trajanProRegular);
		}
	}
}
