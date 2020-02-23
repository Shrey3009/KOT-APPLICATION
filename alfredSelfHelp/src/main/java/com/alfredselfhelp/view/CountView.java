package com.alfredselfhelp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.global.BugseeHelper;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredselfhelp.R;
import com.alfredselfhelp.popuwindow.SetItemCountWindow;


public class CountView extends LinearLayout implements OnClickListener {
    private TextView tv_count;
    private OnCountChange onCountChange;
    private OrderDetail orderDetail;
    private SetItemCountWindow setItemCountWindow;

    public CountView(Context context) {
        super(context);
        init(context);
    }

    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.count_view, this);
        tv_count = (TextView) findViewById(R.id.tv_count);
//		findViewById(R.id.tv_minus).setOnClickListener(this);
//		findViewById(R.id.tv_add).setOnClickListener(this);
    }

    public void setOnCountChange(OnCountChange onCountChange) {
        this.onCountChange = onCountChange;
    }

    public void setIsCanClick(boolean isCanClick) {
        LinearLayout ll_add = (LinearLayout) findViewById(R.id.ll_add);
        LinearLayout ll_minus = (LinearLayout) findViewById(R.id.ll_minus);
        ImageView iv_add = (ImageView) findViewById(R.id.iv_add);
        LinearLayout ll_count = (LinearLayout) findViewById(R.id.ll_count);
//        ll_count.setOnClickListener(this);
        RelativeLayout re_g = (RelativeLayout) findViewById(R.id.re_g);
        ll_minus.setOnClickListener(this);
        ll_add.setOnClickListener(this);
        if (isCanClick) {
            ll_add.setEnabled(true);
            ll_minus.setEnabled(true);
            ll_minus.setVisibility(View.VISIBLE);
            re_g.setVisibility(GONE);
            iv_add.setVisibility(VISIBLE);
            iv_add.setImageResource(R.drawable.icon_add);
        } else {
            ll_add.setEnabled(false);
            ll_minus.setEnabled(false);
            iv_add.setVisibility(GONE);
            re_g.setVisibility(VISIBLE);
            ll_minus.setVisibility(View.INVISIBLE);
            iv_add.setImageResource(R.drawable.icon_add);
        }
    }

    public void setParam(OrderDetail orderDetail, SetItemCountWindow setItemCountWindow) {
        this.orderDetail = orderDetail;
        this.setItemCountWindow = setItemCountWindow;
    }

    public void setInitCount(int count) {
        tv_count.setText(count + "");
    }

    @Override
    public void onClick(View v) {
        if(!ButtonClickTimer.canClickShort(v)){
            return;
        }

        BugseeHelper.buttonClicked(v);
        switch (v.getId()) {
            case R.id.ll_minus: {
                int count = 0;
                try {
                    count = Integer.parseInt(tv_count.getText().toString());
                    count--;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count < 0)
                    count = 0;
                tv_count.setText(count + "");
                if (onCountChange != null) {
                    onCountChange.onChange(orderDetail, count, false);
                }
                break;
            }
            case R.id.ll_add: {
                int count = 0;
                Log.d("111111111--->", "111111111");
                try {
                    count = Integer.parseInt(tv_count.getText().toString());
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count < 1)
                    count = 1;
                tv_count.setText(count + "");
                if (onCountChange != null) {
                    onCountChange.onChange(orderDetail, count, true);
                    Log.d("2222222222--->", "22222222222");
                }
                break;
            }
            case R.id.ll_count:
                setItemCountWindow.show(Integer.parseInt(tv_count.getText().toString()), orderDetail);
                break;
            default:
                break;
        }

    }

    public interface OnCountChange {
        void onChange(OrderDetail orderDetail, int count, boolean isAdd);
    }

}
