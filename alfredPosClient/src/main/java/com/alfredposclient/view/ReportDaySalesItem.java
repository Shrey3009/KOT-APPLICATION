package com.alfredposclient.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredposclient.R;

public class ReportDaySalesItem extends LinearLayout {

    private TextView tv_name;
    private TextView tv_num;
    private TextView tv_amount;
    private TextView tv_title;
    private View view_line;
    private LinearLayout ll_item_name;

    public ReportDaySalesItem(Context context) {
        super(context);
        init(context);
    }

    public ReportDaySalesItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.report_day_sales_item, this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_title = (TextView) findViewById(R.id.tv_title);
        view_line = findViewById(R.id.view_line);
        ll_item_name = (LinearLayout) findViewById(R.id.ll_item);
    }

    public void setData(String name, String num, String amount, boolean showLine) {
        if (TextUtils.isEmpty(name)) {
            ll_item_name.setVisibility(GONE);
        } else {
            ll_item_name.setVisibility(VISIBLE);
        }
        tv_name.setText(name);
        tv_num.setText(num);
        tv_amount.setText(amount);
        if (showLine) {
            view_line.setVisibility(View.VISIBLE);
        } else {
            view_line.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        findViewById(R.id.ll_title_day).setVisibility(View.VISIBLE);
        tv_title.setText(title);
    }
}
