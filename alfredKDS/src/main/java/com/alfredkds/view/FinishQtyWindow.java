package com.alfredkds.view;

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
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.global.UIHelp;
import com.alfredkds.view.MoneyKeyboard.KeyBoardClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinishQtyWindow implements OnClickListener, KeyBoardClickListener {
    private static final int DURATION_1 = 300;
    private static final int DURATION_2 = 500;
    private static final int FROM_FRONT = 1;

    private BaseActivity parent;
    private View parentView;
    private Handler handler;
    private View contentView;
    private KotSummary kotSummary;
    private KotItemDetail kotItemDetail;
    private PopupWindow popupWindow;
    private RelativeLayout rl_itemnum;
    private RelativeLayout rl_itemnum_panel;
    private MoneyKeyboard paxKeyboard;
    private TextView tv_itemnum_tips;
    private TextView tv_itemnum;
    private boolean flag = false;
    private String str;
    private LoadingDialog loadingDialog;
    private int from;

    public FinishQtyWindow(BaseActivity parent, View parentView, Handler handler) {
        this(parent, parentView, handler, 0);
    }

    public FinishQtyWindow(BaseActivity parent, View parentView, Handler handler, int from) {
        this.parent = parent;
        this.parentView = parentView;
        this.handler = handler;
        this.from = from;
    }

    private void init() {
        contentView = LayoutInflater.from(parent).inflate(R.layout.popup_finish_qty, null);

        rl_itemnum = (RelativeLayout) contentView.findViewById(R.id.rl_itemnum);
        tv_itemnum_tips = (TextView) contentView.findViewById(R.id.tv_itemnum_tips);

        rl_itemnum_panel = (RelativeLayout) contentView.findViewById(R.id.rl_itemnum_panel);
        tv_itemnum = (TextView) contentView.findViewById(R.id.tv_itemnum);
        if (str != null) {
            tv_itemnum.setText(str + "");
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

    public void show() {
        if (isShowing()) {
            return;
        }
        init();
        flag = false;
        popupWindow
                .showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
        rl_itemnum_panel.post(new Runnable() {
            @Override
            public void run() {
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_itemnum_panel,
                        "y", rl_itemnum_panel.getY() + rl_itemnum_panel.getHeight(),
                        rl_itemnum_panel.getY()).setDuration(DURATION_2);

                ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                        rl_itemnum,
                        "y",
                        rl_itemnum.getY() + rl_itemnum.getHeight(),
                        rl_itemnum.getY()
                                + ScreenSizeUtil.getStatusBarHeight(parent))
                        .setDuration(DURATION_1);
                set.playTogether(animator1, animator2);
                set.setInterpolator(new DecelerateInterpolator());
                set.start();
            }
        });
    }

    public void show(String str, KotSummary kotSummary,
                     KotItemDetail kotItemDetail, LoadingDialog loadingDialog) {
        this.str = str;
        this.kotSummary = kotSummary;
        this.kotItemDetail = kotItemDetail;
        this.loadingDialog = loadingDialog;
        this.loadingDialog.setTitle(parent.getResources().getString(R.string.sending));
        show();
    }

    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_itemnum_panel,
                    "y", rl_itemnum_panel.getY(),
                    rl_itemnum_panel.getY() + rl_itemnum_panel.getHeight())
                    .setDuration(DURATION_2);

            ObjectAnimator animator2 = ObjectAnimator.ofFloat(rl_itemnum, "y",
                    rl_itemnum.getY() + ScreenSizeUtil.getStatusBarHeight(parent),
                    rl_itemnum.getY() + rl_itemnum.getHeight()).setDuration(DURATION_1);
            set.playTogether(animator1, animator2);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerImpl() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    popupWindow.dismiss();
                };
            });
            set.start();

            this.str = null;
            this.kotSummary = null;
            this.kotItemDetail = null;
        }
    }

    public boolean isShowing() {
        if (popupWindow != null) {
            return popupWindow.isShowing();
        }
        return false;
    }

    @Override
    public void onKeyBoardClick(String key) {
        String itemnum = tv_itemnum.getText().toString();
        BugseeHelper.log(itemnum);
        BugseeHelper.buttonClicked(key);
        if ("X".equals(key)) {
            dismiss();
        } else if ("Enter".equals(key)) {
            if (TextUtils.isEmpty(itemnum)) {
                UIHelp.showToast(parent, parent.getResources().getString(R.string.finished_items_number));
            } else {
                if (Integer.parseInt(itemnum) == 0) {
                    return;
                }

                if (from == FROM_FRONT) {

                } else {

				/*
				kotItemDetail.setFinishQty(kotItemDetail.getUnFinishQty());
				kotItemDetail.setUnFinishQty(0);
				kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
				KotItemDetailSQL.update(kotItemDetail);
				 */
                    if (kotItemDetail != null && kotItemDetail.getKotStatus() < ParamConst.KOT_STATUS_DONE) {
                        loadingDialog.show();
                        if (itemnum.equals(str)) {
                            kotItemDetail.setUnFinishQty(0);
//						kotItemDetail.setFinishQty(kotItemDetail.getFinishQty()+Integer.parseInt(str));
                            kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                            KotItemDetailSQL.update(kotItemDetail);
                            kotItemDetail.setFinishQty(Integer.parseInt(str));
//						kotItemDetail.setUnFinishQty(kotItemDetail.getItemNum()-Integer.parseInt(str));
                            List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
                            itemDetails.add(kotItemDetail);
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            parameters.put("kotSummary", kotSummary);
                            parameters.put("kotItemDetails", itemDetails);
                            parameters.put("userKey", CoreData.getInstance().getUserKey(kotSummary.getRevenueCenterId()));

                            SyncCentre.getInstance().kotComplete(parent,
                                    App.instance.getCurrentConnectedMainPos(kotSummary.getRevenueCenterId()), parameters, handler, -1);
                        } else {
                            kotItemDetail.setUnFinishQty(Integer.parseInt(str) - Integer.parseInt(itemnum));
//						kotItemDetail.setFinishQty(kotItemDetail.getFinishQty()+Integer.parseInt(itemnum));
                            KotItemDetailSQL.update(kotItemDetail);
//						kotItemDetail.setUnFinishQty(kotItemDetail.getItemNum()-Integer.parseInt(itemnum));
                            kotItemDetail.setFinishQty(Integer.parseInt(itemnum));
//						kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                            List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
                            itemDetails.add(kotItemDetail);
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            parameters.put("kotSummary", kotSummary);
                            parameters.put("kotItemDetails", itemDetails);
                            parameters.put("userKey", CoreData.getInstance().getUserKey(kotSummary.getRevenueCenterId()));

                            SyncCentre.getInstance().kotComplete(parent,
                                    App.instance.getCurrentConnectedMainPos(kotSummary.getRevenueCenterId()), parameters, handler, -1);
                        }
                    }
                }

                dismiss();
            }
        } else if ("C".equals(key)) {
            tv_itemnum.setText("");
        } else if (flag) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (Integer.parseInt(itemnum + key) > Integer.parseInt(str)) {
                tv_itemnum.setText(str);
                UIHelp.showToast(parent, parent.getResources().getString(R.string.max_number) + str);
                return;
            }
            tv_itemnum.setText(tv_itemnum.getText().toString() + key);
        } else {
            if (Integer.parseInt(key) >= Integer.parseInt(str)) {
                tv_itemnum.setText(str);
                UIHelp.showToast(parent, parent.getResources().getString(R.string.max_number) + str);
                return;
            }
            tv_itemnum.setText(key);
            flag = !flag;
        }

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

    private void initTextTypeFace() {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(tv_itemnum_tips);
        textTypeFace.setTrajanProRegular(tv_itemnum);
    }

}
