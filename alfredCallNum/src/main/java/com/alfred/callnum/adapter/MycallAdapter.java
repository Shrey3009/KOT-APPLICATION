package com.alfred.callnum.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.alfred.callnum.R;
import com.alfred.callnum.activity.MainActivity;
import com.alfredbase.utils.EditTextUtil;

import java.util.ArrayList;
import java.util.List;


public class MycallAdapter extends RvAdapter<CallBean> {
    public List<CallBean> callList;
    ScaleAnimation scaleAnimation;
    private Context mContext;
    private int printerGroupId;

    public MycallAdapter(Context context, List<CallBean> list, RvListener listener) {


        super(context, list, listener);
        this.callList = list;
        this.mContext = context;
    }

    //    public void addData(int position) {
//        CallBean callBean=new CallBean();
//        callBean.setId(0);
//        callBean.setName("Insert One");
//        callList.add(position, callBean);
//        //  notifyItemInserted(position);
//        notifyItemRangeChanged(position,callList.size()-position);
//    }
    @Override
    protected int getLayoutId(int viewType) {
        return viewType == 0 ? R.layout.item_mycall : R.layout.item_mycall_small;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new MyCallHolder(view, viewType, listener);
    }

    public int getPrinterGroupId() {
        return printerGroupId;
    }

    public void setPrinterGroupId(int printerGroupId) {
        this.printerGroupId = printerGroupId;
    }

    public void setAnimation() {
        scaleAnimation.cancel();
    }

    public class MyCallHolder extends RvHolder<CallBean> {
        TextView tvName;

        TextView tvSmallName;

        public MyCallHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            switch (type) {
                case 0:
                    tvName = (TextView) itemView.findViewById(R.id.tv_name);
                    break;
                case 1:
                    tvSmallName = (TextView) itemView.findViewById(R.id.tv_small_name);

                    break;
            }

        }

        @Override
        public void bindHolder(CallBean callBean, int position) {
            int itemViewType = MycallAdapter.this.getItemViewType(position);


            switch (itemViewType) {
                case 0:
                    tvName.setText(EditTextUtil.formatLocale(callBean.getCallNumber()));
                    ((MainActivity) mContext).adAnimation(tvName);

                    break;
                case 1:
                    //   tvCity.setText(sortBean.getName());
                    tvSmallName.setText(EditTextUtil.formatLocale(callBean.getCallNumber()));
                    break;
            }
        }


    }


//    public void textAnimation(TextView textScore) {
//        textScore.setVisibility(View.VISIBLE);
//        TranslateAnimation tAnimation = new TranslateAnimation(0f, 0f, 0f, 0f); //位移动画效果
//        AlphaAnimation aAnimation = new AlphaAnimation(1, 1); //透明度动画效果
//
//        ScaleAnimation animation = new ScaleAnimation(
//                0.5f, 2.0f, 0.5f, 2.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
//        );
//        animation.setRepeatCount(3);
//        //   ScaleAnimation sAnimation = new ScaleAnimation(0f, 1.0f, 0f, 1.0f, 0f, 0f);  //缩放动画效果
//
//        textAnimationSet = new AnimationSet(true);
//        //   textAnimationSet.addAnimation(tAnimation);
//        textAnimationSet.addAnimation(aAnimation);
//        textAnimationSet.addAnimation(animation);
//        textAnimationSet.setFillBefore(false);
//        textAnimationSet.setFillAfter(false);
//        textAnimationSet.setFillEnabled(true);
//        textAnimationSet.setDuration(2500);
//
//        textScore.setAnimation(textAnimationSet);
//    }
}
