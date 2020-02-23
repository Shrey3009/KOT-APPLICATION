package com.alfredposclient.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.view.MoneyKeyboard;
import com.alfredposclient.view.MoneyKeyboard.KeyBoardClickListener;

public class ModifyQuantityWindow implements OnClickListener,
		KeyBoardClickListener {
	private static final int DURATION_1 = 300;
	private static final int DURATION_2 = 500;

	private BaseActivity parent;
	private View parentView;

	private View contentView;
	private PopupWindow popupWindow;
	private RelativeLayout rl_quantity;
	private RelativeLayout rl_quantity_panel;
	private MoneyKeyboard quantityKeyboard;
	private TextView tv_quantity;
	private DismissCall dismissCall;
	private int originCount;
	private boolean flag = false;
	private boolean canEnter = false;

	public ModifyQuantityWindow(BaseActivity parent, View parentView) {
		this.parent = parent;
		this.parentView = parentView;
	}

	private void init() {
		contentView = LayoutInflater.from(parent).inflate(
				R.layout.popup_modify_quantity, null);

		rl_quantity = (RelativeLayout) contentView
				.findViewById(R.id.rl_quantity);
		tv_quantity = (TextView) contentView.findViewById(R.id.tv_quantity);
		tv_quantity.setText(originCount + "");

		rl_quantity_panel = (RelativeLayout) contentView
				.findViewById(R.id.rl_quantity_panel);
		
		initTextTypeFace(contentView);

		quantityKeyboard = (MoneyKeyboard) contentView
				.findViewById(R.id.quantityKeyboard);
		quantityKeyboard.setMoneyPanel(View.GONE);
		quantityKeyboard.setKeyBoardClickListener(this);

		popupWindow = new PopupWindow(parentView,
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
	}

	private void initTextTypeFace(View view){
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod((TextView)view.findViewById(R.id.tv_quantity_tips));
		textTypeFace.setTrajanProRegular(tv_quantity);
	}
	
	public void show(int originCount, DismissCall dismissCall) {
		if(isShowing()){
			return;
		}
		canEnter = true;
		flag = false;
		this.originCount = originCount;
		this.dismissCall = dismissCall;
		init();
		popupWindow
				.showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
		rl_quantity_panel.post(new Runnable() {
			@Override
			public void run() {
				AnimatorSet set = new AnimatorSet();
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(
						rl_quantity_panel,
						"y",
						rl_quantity_panel.getY()
								+ rl_quantity_panel.getHeight(),
						rl_quantity_panel.getY()).setDuration(DURATION_2);

				ObjectAnimator animator2 = ObjectAnimator.ofFloat(
						rl_quantity,
						"y",
						rl_quantity.getY() + rl_quantity.getHeight(),
						rl_quantity.getY()
								+ ScreenSizeUtil.getStatusBarHeight(parent))
						.setDuration(DURATION_1);
				set.playTogether(animator1, animator2);
				set.setInterpolator(new DecelerateInterpolator());
				set.start();
			}
		});
	}

	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			AnimatorSet set = new AnimatorSet();
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(
					rl_quantity_panel, "y", rl_quantity_panel.getY(),
					rl_quantity_panel.getY() + rl_quantity_panel.getHeight())
					.setDuration(DURATION_2);

			ObjectAnimator animator2 = ObjectAnimator.ofFloat(
					rl_quantity,
					"y",
					rl_quantity.getY()
							+ ScreenSizeUtil.getStatusBarHeight(parent),
					rl_quantity.getY() + rl_quantity.getHeight()).setDuration(
					DURATION_1);
			set.playTogether(animator1, animator2);
			set.setInterpolator(new DecelerateInterpolator());
			set.addListener(new AnimatorListenerImpl() {
				public void onAnimationStart(Animator animation) {
					ButtonClickTimer.canClick();
					super.onAnimationStart(animation);
				}
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					popupWindow.dismiss();
				};
			});
			set.start();
		}
	}
	public void dismissWithDataCallback(final String key, final int originCount, final String data) {
		if (popupWindow != null && popupWindow.isShowing()) {
			AnimatorSet set = new AnimatorSet();
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(
					rl_quantity_panel, "y", rl_quantity_panel.getY(),
					rl_quantity_panel.getY() + rl_quantity_panel.getHeight())
					.setDuration(DURATION_2);

			ObjectAnimator animator2 = ObjectAnimator.ofFloat(
					rl_quantity,
					"y",
					rl_quantity.getY()
							+ ScreenSizeUtil.getStatusBarHeight(parent),
					rl_quantity.getY() + rl_quantity.getHeight()).setDuration(
					DURATION_1);
			set.playTogether(animator1, animator2);
			set.setInterpolator(new DecelerateInterpolator());
			set.addListener(new AnimatorListenerImpl() {
				public void onAnimationStart(Animator animation) {
					ButtonClickTimer.canClick();
					super.onAnimationStart(animation);
				}
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					popupWindow.dismiss();
					if (TextUtils.isEmpty(data)) {
						dismissCall.call(key, originCount);
					} else {
						dismissCall.call(key, Integer.parseInt(data));
					}
					
				};
			});
			set.start();
		}
	}

	public boolean isShowing() {
		if (popupWindow != null) {
			return popupWindow.isShowing();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (ButtonClickTimer.canClick(v)) {
			switch (v.getId()) {
			default:
				break;
			}
		}
	}

	@Override
	public void onKeyBoardClick(String key) {
		BugseeHelper.buttonClicked(key);
		if ("X".equals(key)) {
			dismiss();
			if (dismissCall != null) {
				// dismissCall.call(key, originCount);
			}
		} else if ("Enter".equals(key)) {
			if(!ButtonClickTimer.canClick()){
				return;
			}
			if(!canEnter){
				return;
			}
			canEnter = false;
			//dismiss();

			String str = tv_quantity.getText().toString();
			dismissWithDataCallback(key, originCount, str);

		} else if ("C".equals(key)) {
			tv_quantity.setText("");
		} else if (flag) {
			tv_quantity.setText(tv_quantity.getText().toString() + key);
		} else {
			tv_quantity.setText(key);
			flag = !flag;
		}
	}

	public interface DismissCall {
		public void call(String key, int num);
	}

}
