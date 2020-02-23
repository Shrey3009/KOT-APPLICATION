package com.alfredposclient.global;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewConfig {
	public static final String ROOT_DIRECTORY = "file:///android_asset/";

	public static void setDefaultConfig(WebView webView) {
		// 允许webkit执行js代码；
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setPluginState(PluginState.ON);
		// 允许webkit执行alert
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new LocalWebViewClient());
//		// 允许点击输入框上滑
//		webView.requestFocus(View.FOCUS_DOWN);
	}

	private static final class LocalWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}

}
