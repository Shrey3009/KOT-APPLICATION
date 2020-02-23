package com.alfredbase;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredbase.utils.ToastUtils;

public class PrinterLoadingDialog extends Dialog {
	private static final int CHANGE_TITLE_WHAT = 1;
	private static final int CHANGE_TIME_OUT = 2;
	private static final int CHANGE_LONG_TIME_OUT = 4;
	private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
	private static final int MAX_SUFFIX_NUMBER = 3;
	private static final char SUFFIX = '.';
	private BaseActivity context;
	private ImageView iv_route;
	private TextView tv;
	private TextView tv_point;
	private RotateAnimation mAnim;

	private Handler handler = new Handler() {
		private int num = 0;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_TITLE_WHAT: {

				StringBuilder builder = new StringBuilder();
				if (num >= MAX_SUFFIX_NUMBER) {
					num = 0;
				}
				num++;
				for (int i = 0; i < num; i++) {
					builder.append(SUFFIX);
				}
				tv_point.setText(builder.toString());
				if (isShowing()) {
					handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT,
							CHNAGE_TITLE_DELAYMILLIS);
				} else {
					num = 0;
				}

			}
				break;
			case CHANGE_TIME_OUT:
				if(PrinterLoadingDialog.this != null && isShowing()){
					PrinterLoadingDialog.this.dismiss();
				}
				break;
			case CHANGE_LONG_TIME_OUT:
				if(PrinterLoadingDialog.this != null && isShowing()){
					PrinterLoadingDialog.this.dismiss();
					ToastUtils.showToast(context, context.getResources().getString(R.string.no_network));
				}
				break;
			default:
				break;
			}
		};
	};

	public PrinterLoadingDialog(BaseActivity context) {
		super(context, R.style.Dialog_bocop);
		this.context = context;
		init();
	}

	private void init() {
		View contentView = View.inflate(getContext(),
				R.layout.loading_dialog_layout, null);
		setContentView(contentView);
		iv_route = (ImageView) findViewById(R.id.iv_route);
		tv = (TextView) findViewById(R.id.tv);
		tv_point = (TextView) findViewById(R.id.tv_point);
		initAnim();
		getWindow().setWindowAnimations(R.anim.alpha_in);
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}

	private void initAnim() {
		mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f,
				Animation.RESTART, 0.5f);
		mAnim.setDuration(2000);
		mAnim.setRepeatCount(Animation.INFINITE);
		mAnim.setRepeatMode(Animation.RESTART);
		mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
	}

	@Override
	public void show() {
		if (context == null || context.isFinishing())
			return;		
		super.show();
		iv_route.startAnimation(mAnim);
		handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
	}
	
	public void showByTime(long time){
		show();
		handler.sendEmptyMessageDelayed(CHANGE_TIME_OUT,time);
	}
	
	public void showTime(){
		show();
		handler.sendEmptyMessageDelayed(CHANGE_LONG_TIME_OUT,15000);
	}

	@Override
	public void dismiss() {
		try{
			mAnim.cancel();			
			super.dismiss();
		}catch (Exception e) {
			
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		tv.setText(title);
	}

	@Override
	public void setTitle(int titleId) {
		setTitle(getContext().getString(titleId));
	}
}
