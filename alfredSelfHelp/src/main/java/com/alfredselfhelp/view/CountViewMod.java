package com.alfredselfhelp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.global.BugseeHelper;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredselfhelp.R;
import com.alfredselfhelp.popuwindow.SetItemCountWindow;


public class CountViewMod extends LinearLayout implements OnClickListener {
    private TextView tv_count;
    private OnCountChange onCountChange;
    private ItemDetail itemDetail;
    private SetItemCountWindow setItemCountWindow;

    private ImageView add;

    public CountViewMod(Context context) {
        super(context);
        init(context);
    }

    public CountViewMod(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.count_view_mod, this);
        tv_count = (TextView) findViewById(R.id.tv_num);
        add = (ImageView) findViewById(R.id.iv_add);
//        add.setOnClickListener(this);
//		findViewById(R.id.tv_add).setOnClickListener(this);
    }

    public void setOnCountChange(OnCountChange onCountChange) {
        this.onCountChange = onCountChange;
    }

    public void setIsCanClick(boolean isCanClick) {

        if (isCanClick) {
//            ll_minus.setOnClickListener(this);
//            ll_add.setOnClickListener(this);
//            ll_count.setOnClickListener(this);
        } else {
//			tv_minus.setTextColor(getResources().getColor(R.color.gray));
//            ll_add.setOnClickListener(this);
//            ll_count.setOnClickListener(this);
        }
    }

    public void setParam(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
//        this.setItemCountWindow = setItemCountWindow;
    }

    public void setInitCount(int count) {
        if (count == 0) {
            tv_count.setText(count + "");
            add.setImageResource(R.drawable.icon_add);
            tv_count.setVisibility(GONE);
        } else {
            tv_count.setText(count + "");
            add.setImageResource(R.drawable.mod_num);
            tv_count.setVisibility(VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (!ButtonClickTimer.canClick(v)) {
            return;
        }

        BugseeHelper.buttonClicked(v);
        switch (v.getId()) {
//            case R.id.ll_minus: {
//                int count = 0;
//                try {
//                    count = Integer.parseInt(tv_count.getText().toString());
//                    count--;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (count < 0)
//                    count = 0;
//                tv_count.setText(count + "");
//                if (onCountChange != null) {
//                    onCountChange.onChange(itemDetail, count, false);
//                }
//                break;
//            }
            case R.id.iv_add: {

                add.setImageResource(R.drawable.mod_num);
                tv_count.setVisibility(VISIBLE);
                int count = 1;
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
                    onCountChange.onChange(itemDetail, count, true);
                    //   Log.d("2222222222--->", "22222222222");
                }
                break;
            }
//            case R.id.ll_count:
//                setItemCountWindow.show(Integer.parseInt(tv_count.getText().toString()), itemDetail);
//                break;
            default:
                break;
        }

    }

    public interface OnCountChange {
        void onChange(ItemDetail selectedItemDetail, int count, boolean isAdd);
    }

}
