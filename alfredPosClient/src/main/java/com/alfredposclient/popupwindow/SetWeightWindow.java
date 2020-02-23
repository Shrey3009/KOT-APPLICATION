package com.alfredposclient.popupwindow;

import java.math.BigDecimal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
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
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.view.MoneyKeyboard;
import com.alfredposclient.view.MoneyKeyboard.KeyBoardClickListener;

public class SetWeightWindow implements OnClickListener, KeyBoardClickListener  {
	private static final int DURATION_1 = 300;
	private static final int DURATION_2 = 500;

	private BaseActivity parent;
	private View parentView;
	private Handler handler;
	private OrderDetail orderDetail;
	private View contentView;
	private PopupWindow popupWindow;
	private RelativeLayout rl_weight;
	private RelativeLayout rl_weight_panel;
	private MoneyKeyboard weightKeyboard;
	private TextView tv_weight;
	private StringBuffer keyBuffer;

	public SetWeightWindow(BaseActivity parent, View parentView, Handler handler) {
		this.parent = parent;
		this.parentView = parentView;
		this.handler = handler;
	}

	private void init() {
		contentView = LayoutInflater.from(parent).inflate(
				R.layout.popup_set_weight, null); 

		rl_weight = (RelativeLayout) contentView.findViewById(R.id.rl_weight);

		rl_weight_panel = (RelativeLayout) contentView
				.findViewById(R.id.rl_weight_panel);
		tv_weight = (TextView) contentView.findViewById(R.id.tv_weight);
		initTextTypeFace();
		if(orderDetail != null){
			tv_weight.setText(BH.getBDThirdFormat(orderDetail.getWeight()).toString());
		}
		weightKeyboard = (MoneyKeyboard) contentView
				.findViewById(R.id.weightKeyboard);
		weightKeyboard.setMoneyPanel(View.GONE);
		weightKeyboard.setKeyBoardClickListener(this);

		popupWindow = new PopupWindow(parentView,
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
	}

	public void show() {
		if(isShowing()){
			return;
		}
		keyBuffer = new StringBuffer();
		init();
		popupWindow
				.showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
		rl_weight_panel.post(new Runnable() {
			@Override
			public void run() {
				AnimatorSet set = new AnimatorSet();
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_weight_panel,
						"y", rl_weight_panel.getY() + rl_weight_panel.getHeight(),
						rl_weight_panel.getY()).setDuration(DURATION_2);

				ObjectAnimator animator2 = ObjectAnimator.ofFloat(
						rl_weight,
						"y",
						rl_weight.getY() + rl_weight.getHeight(),
						rl_weight.getY()
								+ ScreenSizeUtil.getStatusBarHeight(parent))
						.setDuration(DURATION_1);
				set.playTogether(animator1, animator2);
				set.setInterpolator(new DecelerateInterpolator());
				set.start();
			}
		});
	}
	
	public void show(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
		show();
	}
	

	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			AnimatorSet set = new AnimatorSet();
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_weight_panel,
					"y", rl_weight_panel.getY(),
					rl_weight_panel.getY() + rl_weight_panel.getHeight())
					.setDuration(DURATION_2);

			ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_weight, "y",
					rl_weight.getY() + ScreenSizeUtil.getStatusBarHeight(parent),
					rl_weight.getY() + rl_weight.getHeight()).setDuration(DURATION_1);
			set.playTogether(animator1, animator2);
			set.setInterpolator(new DecelerateInterpolator());
			set.addListener(new AnimatorListenerImpl() {
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					popupWindow.dismiss();
				};
			});
			set.start();
			this.orderDetail = null;
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
		} else if ("Enter".equals(key)) {
			String weight = tv_weight.getText().toString();
			if(BH.getBDNoFormat(weight).compareTo(BH.getBDNoFormat(orderDetail.getWeight())) != 0){
				orderDetail.setWeight(weight);
				OrderDetailSQL.updateOrderDetailAndOrder(orderDetail);
				handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
			}
			dismiss();
		} else if ("C".equals(key)) {
			keyBuffer = new StringBuffer();
			tv_weight.setText("0.000");
		} else {
			BigDecimal weight = BH.divThirdFormat(BH.getBD(keyBuffer.toString() + key),
					BH.getBD(1000), true);
			if(weight.compareTo(BH.getBD("100")) >= 0){
				return;
			}
			tv_weight.setText(weight.toString());
			keyBuffer.append(key);
		}
	}

	private void initTextTypeFace() {
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular( (TextView) contentView.findViewById(R.id.tv_weight_title));
		textTypeFace.setTrajanProRegular(tv_weight);
	}
}
