package com.alfredposclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.alfredbase.utils.DialogFactory;
import com.alfredposclient.global.App;

public class AlipayWebView extends WebView {

	public AlipayWebView(Context context) {
		super(context);
	}
	public AlipayWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			DialogFactory.commonTwoBtnDialog(App.getTopActivity(), "警告", "正在使用支付宝支付，确定返回吗?", "取消", "确定", null, new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(getVisibility() == View.VISIBLE){
						setVisibility(View.GONE);
					}
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}
}
