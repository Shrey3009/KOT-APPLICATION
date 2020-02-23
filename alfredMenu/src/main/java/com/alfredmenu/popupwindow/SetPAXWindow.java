package com.alfredmenu.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
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
import com.alfredmenu.R;
import com.alfredmenu.activity.TablesPage;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.view.MoneyKeyboard;
import com.alfredmenu.view.MoneyKeyboard.KeyBoardClickListener;

public class SetPAXWindow implements OnClickListener, KeyBoardClickListener {
	private static final int DURATION_1 = 300;
	private static final int DURATION_2 = 500;

	private BaseActivity parent;
	private View parentView;

	private View contentView;
	private PopupWindow popupWindow;
	private RelativeLayout rl_pax;
	private RelativeLayout rl_pax_panel;
	private MoneyKeyboard paxKeyboard;
	private TextView tv_pax;
	private Handler handler;
	private boolean flag = false;

	public SetPAXWindow(BaseActivity parent, View parentView, Handler handler) {
		this.parent = parent;
		this.parentView = parentView;
		this.handler = handler;
	}

	private void init() {
		contentView = LayoutInflater.from(parent).inflate(
				R.layout.popup_set_person_count, null);

		rl_pax = (RelativeLayout) contentView.findViewById(R.id.rl_pax);

		rl_pax_panel = (RelativeLayout) contentView
				.findViewById(R.id.rl_pax_panel);
		tv_pax = (TextView) contentView.findViewById(R.id.tv_pax);

		paxKeyboard = (MoneyKeyboard) contentView
				.findViewById(R.id.paxKeyboard);
		paxKeyboard.setMoneyPanel(View.GONE);
		paxKeyboard.setKeyBoardClickListener(this);

		popupWindow = new PopupWindow(parentView,
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
	}

	public void show() {
		init();
		flag = false;
		popupWindow
				.showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
		rl_pax_panel.post(new Runnable() {
			@Override
			public void run() {
				AnimatorSet set = new AnimatorSet();
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_pax_panel,
						"y", rl_pax_panel.getY() + rl_pax_panel.getHeight(),
						rl_pax_panel.getY()).setDuration(DURATION_2);

				ObjectAnimator animator2 = ObjectAnimator.ofFloat(
						rl_pax,
						"y",
						rl_pax.getY() + rl_pax.getHeight(),
						rl_pax.getY()
								+ ScreenSizeUtil.getStatusBarHeight(parent))
						.setDuration(DURATION_1);
				set.playTogether(animator1, animator2);
				set.setInterpolator(new DecelerateInterpolator());
				set.start();
			}
		});
	}

	public void dismiss(final boolean isEnter) {
		if (popupWindow != null && popupWindow.isShowing()) {
			AnimatorSet set = new AnimatorSet();
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_pax_panel,
					"y", rl_pax_panel.getY(),
					rl_pax_panel.getY() + rl_pax_panel.getHeight())
					.setDuration(DURATION_2);

			ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_pax, "y",
					rl_pax.getY() + ScreenSizeUtil.getStatusBarHeight(parent),
					rl_pax.getY() + rl_pax.getHeight()).setDuration(DURATION_1);
			set.playTogether(animator1, animator2);
			set.setInterpolator(new DecelerateInterpolator());
			set.addListener(new AnimatorListenerImpl() {
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					popupWindow.dismiss();
					if(isEnter){
						handler.sendMessage(handler.obtainMessage(
								TablesPage.VIEW_EVENT_SET_PAX, tv_pax.getText().toString()));
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
		if ("Cancel".equals(key)) {
			dismiss(false);
		} else if ("Enter".equals(key)) {
			if (TextUtils.isEmpty(tv_pax.getText().toString())) {
				UIHelp.showToast(parent, parent.getResources().getString(R.string.number_people));
			}else {
				dismiss(true);
			}
		} else if ("Clear".equals(key)) {
			tv_pax.setText("");
		} else if (flag) {
			tv_pax.setText(tv_pax.getText().toString() + key);
		} else {
			tv_pax.setText(key);
			flag = !flag;
		}
	}

}
