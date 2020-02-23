package com.alfredposclient.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
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
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.SystemSetting;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.view.MoneyKeyboard;
import com.alfredposclient.view.MoneyKeyboard.KeyBoardClickListener;

import java.util.List;

public class SetPAXWindow implements OnClickListener, KeyBoardClickListener {
    private static final int DURATION_1 = 300;
    private static final int DURATION_2 = 500;
    public static final int APP_ORDER = 1;
    public static final int GENERAL_ORDER = 0;
    public static final int MAX_ORDER_NO = 2;
    public static final int TRANSFER_ITEM = 3;
    public static final int SPLITE_BY_PAX = 4;
    public static final int TRANSFER_ITEM_SPLIT = 5;
    private BaseActivity parent;
    private View parentView;
    private Handler handler;
    private Order order;
    private List<OrderDetail> orderDetails;
    private View contentView;
    private PopupWindow popupWindow;
    private RelativeLayout rl_pax;
    private RelativeLayout rl_pax_panel;
    private MoneyKeyboard paxKeyboard;
    private TextView tv_pax_tips;
    private TableInfo transferItemTable;
    private TextView tv_pax;
    private boolean flag = false;
    private String str;
    private int maxNum;
    // 用于控制是否是APP订单 1为APP订单，0为正常流程
    private int type = 0;

    public SetPAXWindow(BaseActivity parent, View parentView, Handler handler) {
        this.parent = parent;
        this.parentView = parentView;
        this.handler = handler;
    }

    private void init() {
        contentView = LayoutInflater.from(parent).inflate(
                R.layout.popup_set_person_count, null);

        rl_pax = (RelativeLayout) contentView.findViewById(R.id.rl_pax);
        tv_pax_tips = (TextView) contentView.findViewById(R.id.tv_pax_tips);

        rl_pax_panel = (RelativeLayout) contentView
                .findViewById(R.id.rl_pax_panel);
        tv_pax = (TextView) contentView.findViewById(R.id.tv_pax);
        if (str != null) {
            tv_pax.setText(str + "");
        }
        initTextTypeFace();

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

    public void showForTransferItem(int type, String title, TableInfo tableInfo) {
        transferItemTable = tableInfo;
        show(type, title);
    }

    public void show(int type, String title) {
        if (isShowing()) {
            return;
        }
        init();
        flag = false;
        this.type = type;
        tv_pax_tips.setText(title);
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

    public void show(String str, Order order, String title) {
        this.str = str;
        this.order = order;
        show(0, title);
    }

    public void show(String str, Order order, String title, List<OrderDetail> orderDetails) {
        this.str = str;
        this.order = order;
        this.orderDetails = orderDetails;
        show(0, title);
    }

    public void show(int type, String str, String title) {
        this.str = str;
        show(type, title);
    }

    public void show(int type, String str, String title, int maxNum) {
        this.maxNum = maxNum;
        show(type, str, title);
    }


    public void dismiss() {
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
                }

                ;
            });
            set.start();

            this.str = null;
            this.order = null;
            this.maxNum = 0;
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
            if (TextUtils.isEmpty(tv_pax.getText().toString())) {
                UIHelp.showToast(parent, parent.getResources().getString(R.string.enter_people));
            } else {
                int num = 4;
                try {
                    String paxNum = tv_pax.getText().toString().trim().replace(":", "");
                    paxNum = paxNum.replace("Pax", "");
                    paxNum = paxNum.replace("pax", "");
                    num = Integer.parseInt(paxNum);
                } catch (Exception e) {
                    LogUtil.e("ERROR", e.getMessage());
                }
                if (num <= 0) {
                    return;
                }
                if (type == MAX_ORDER_NO) {
                    Message msg = handler.obtainMessage();
                    msg.what = SystemSetting.SET_MAX_ORDER_NO;
                    msg.obj = tv_pax.getText().toString();
                    handler.sendMessage(msg);
                    dismiss();
                } else if (type == APP_ORDER) {
                    Message msg = handler.obtainMessage();
                    msg.what = MainPage.VIEW_EVENT_SET_APPORDER_TABLE_PACKS;
                    msg.obj = tv_pax.getText().toString();
                    handler.sendMessage(msg);
                    dismiss();
                } else if (type == TRANSFER_ITEM) {
                    if (transferItemTable != null) {
                        transferItemTable.setPacks(Integer.parseInt(tv_pax.getText().toString()));
                        handler.sendMessage(handler
                                .obtainMessage(
                                        MainPage.ACTION_TRANSFER_ITEM,
                                        transferItemTable));
                    }
                    dismiss();
                } else if (type == SPLITE_BY_PAX) {
                    handler.sendMessage(handler.obtainMessage(MainPage.ACTION_PAX_SPLIT_BY_PAX, tv_pax.getText().toString()));
                    dismiss();
                } else if (type == TRANSFER_ITEM_SPLIT) {
                    handler.sendMessage(handler.obtainMessage(MainPage.ACTION_TRANSFER_SPLIT_BY_NUM, tv_pax.getText().toString()));
                    dismiss();
                } else {
                    if (order != null) {
                        order.setPersons(num);
                        OrderSQL.updateOrderPersions(num, order.getId());
                        OrderHelper.setPromotion(order, orderDetails);
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = MainPage.VIEW_EVENT_SET_TABLE_PACKS;
                    msg.obj = tv_pax.getText().toString();
                    handler.sendMessage(msg);

                    dismiss();
                }
            }
        } else if ("C".equals(key)) {
            tv_pax.setText("");
        } else {
            if (maxNum != 0) {
                if (flag) {
                    if (IntegerUtils.isInteger(key) && Integer.parseInt(tv_pax.getText().toString() + key) < maxNum) {
                        tv_pax.setText(tv_pax.getText().toString() + key);
                    }
                } else {
                    if (IntegerUtils.isInteger(key) && Integer.parseInt(key) < maxNum) {
                        tv_pax.setText(key);
                        flag = !flag;
                    }
                }
            } else {
                if (flag) {
                    tv_pax.setText(tv_pax.getText().toString() + key);
                } else {
                    tv_pax.setText(key);
                    flag = !flag;
                }
            }

        }
    }

    private void initTextTypeFace() {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(tv_pax_tips);
        textTypeFace.setTrajanProRegular(tv_pax);
    }
}
