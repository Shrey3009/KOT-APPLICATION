package com.alfredbase;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class SureDialog extends Dialog {
	private static final int CHANGE_TITLE_WHAT = 1;
	private static final int CHANGE_TIME_OUT = 2;
	private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
	private static final int MAX_SUFFIX_NUMBER = 3;
	private static final char SUFFIX = '.';

	private ImageView iv_sure;

	private Handler handler = new Handler() {
		private int num = 0;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_TIME_OUT:
				if(SureDialog.this != null && isShowing()){
					SureDialog.this.dismiss();
				}
				break;
			default:
				break;
			}
		};
	};

	public SureDialog(Context context) {
		super(context, R.style.Dialog_sure);
		init();
	}

	private void init() {
		View contentView = View.inflate(getContext(),
				R.layout.sure_dialog_layout, null);
		setContentView(contentView);
		iv_sure = (ImageView) findViewById(R.id.iv_sure);
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}


	public void show(boolean isSure) {
		if(isSure){
			iv_sure.setImageResource(R.drawable.gou);
		}else{
			iv_sure.setImageResource(R.drawable.cha);
		}
		handler.sendEmptyMessageDelayed(CHANGE_TIME_OUT,CHNAGE_TITLE_DELAYMILLIS);
		super.show();
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void setTitle(int titleId) {
		setTitle(getContext().getString(titleId));
	}
}
