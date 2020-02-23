package com.alfredposclient.utils;

import android.view.View;
import android.view.View.OnClickListener;

import com.alfredbase.BaseActivity;
import com.alfredbase.utils.DialogFactory;
import com.alfredposclient.R;
import com.alfredposclient.global.UIHelp;

public class AlertToDeviceSetting {
	
	public static void noKDSorPrinter (final BaseActivity parent, final String msg) {
		parent.runOnUiThread(new Runnable() {
	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DialogFactory.commonTwoBtnDialog(parent, parent.getResources().getString(R.string.warning), 
						msg, 
						parent.getResources().getString(R.string.cancel), 
						parent.getResources().getString(R.string.setting), 
						null, 
						new OnClickListener() {
	
							@Override
							public void onClick(
									View v) {
								UIHelp.startDevicesHtml(parent);
							}
					});										
			}});
	}
}
