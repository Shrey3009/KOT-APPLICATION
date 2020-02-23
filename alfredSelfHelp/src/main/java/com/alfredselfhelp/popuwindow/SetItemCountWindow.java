package com.alfredselfhelp.popuwindow;

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
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredselfhelp.R;
import com.alfredselfhelp.activity.MenuActivity;
import com.alfredselfhelp.utils.MoneyKeyboard;
import com.alfredselfhelp.utils.UIHelp;


import java.util.HashMap;
import java.util.Map;

public class SetItemCountWindow implements OnClickListener, MoneyKeyboard.KeyBoardClickListener {
    private static final int DURATION_1 = 300;
    private static final int DURATION_2 = 500;

    private BaseActivity parent;
    private View parentView;

    private View contentView;
    private PopupWindow popupWindow;
    private RelativeLayout rl_num;
    private RelativeLayout rl_num_panel;
    private MoneyKeyboard numKeyboard;
    private TextView tv_num;
    private Handler handler;
    private boolean flag = false;
    private OrderDetail orderDetail;
    private int oldCount;

    public SetItemCountWindow(BaseActivity parent, View parentView, Handler handler) {
        this.parent = parent;
        this.parentView = parentView;
        this.handler = handler;
    }

    private void init() {
        contentView = LayoutInflater.from(parent).inflate(
                R.layout.popup_set_num_count, null);

        rl_num = (RelativeLayout) contentView.findViewById(R.id.rl_num);

        rl_num_panel = (RelativeLayout) contentView
                .findViewById(R.id.rl_num_panel);
        tv_num = (TextView) contentView.findViewById(R.id.tv_num);

        numKeyboard = (MoneyKeyboard) contentView
                .findViewById(R.id.numKeyboard);
        numKeyboard.setMoneyPanel(View.GONE);
        numKeyboard.setKeyBoardClickListener(this);

        popupWindow = new PopupWindow(parentView,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
    }

    public void show(int count, OrderDetail orderDetail) {
        init();
        this.oldCount = count;
        this.orderDetail = orderDetail;
        tv_num.setText(count + "");
        flag = false;
        popupWindow.showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
        rl_num_panel.post(new Runnable() {
            @Override
            public void run() {
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_num_panel,
                        "y", rl_num_panel.getY() + rl_num_panel.getHeight(),
                        rl_num_panel.getY()).setDuration(DURATION_2);

                ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                        rl_num,
                        "y",
                        rl_num.getY() + rl_num.getHeight(),
                        rl_num.getY()
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
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_num_panel,
                    "y", rl_num_panel.getY(),
                    rl_num_panel.getY() + rl_num_panel.getHeight())
                    .setDuration(DURATION_2);

            ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_num, "y",
                    rl_num.getY() + ScreenSizeUtil.getStatusBarHeight(parent),
                    rl_num.getY() + rl_num.getHeight()).setDuration(DURATION_1);
            set.playTogether(animator1, animator2);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerImpl() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    popupWindow.dismiss();
                    if (isEnter) {
                        Map<String, Object> result = new HashMap<String, Object>();
                        result.put("orderDetail", orderDetail);
                        result.put("count", Integer.parseInt(tv_num.getText().toString()));
                        //int newCount=Integer.parseInt(tv_num.getText().toString());
//						if(newCount>oldCount) {
//							result.put("isAdd", true);
//						}else {
//							result.put("isAdd", false);
//						}
                        handler.sendMessage(handler.obtainMessage(
                                MenuActivity.VIEW_ORDER_DETAIL_MODIFY_ITEM_COUNT, result));
                    }
                }

                ;
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
            if (TextUtils.isEmpty(tv_num.getText().toString())) {
                UIHelp.showToast(parent, parent.getResources().getString(R.string.empty_invalid));
            } else {
                dismiss(true);
            }
        } else if ("Clear".equals(key)) {
            tv_num.setText("");
        } else if (flag) {
            tv_num.setText(tv_num.getText().toString() + key);
        } else {
            tv_num.setText(key);
            flag = !flag;
        }
    }

}
