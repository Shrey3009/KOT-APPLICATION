package com.alfredwaiter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredwaiter.R;

public class TitleBar extends LinearLayout implements OnClickListener {
	private BaseActivity parent;
	private TextView tv_title_name;
	
	public TitleBar(Context context) {
		super(context);
		init(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.title_bar_view, this);
		findViewById(R.id.ll_back).setOnClickListener(this);
		tv_title_name = (TextView) findViewById(R.id.tv_title_name);
		findViewById(R.id.iv_kot_notification).setOnClickListener(this);
	}
	
	public void setParams(BaseActivity parent,String titleName) {
		this.parent = parent;
		tv_title_name.setText(titleName);
	}

	@Override
	public void onClick(View v) {
		if (ButtonClickTimer.canClick(v)) {
			switch (v.getId()) {
			case R.id.ll_back:
				parent.finish();
				break;
			default:
				break;
			}
		}

	}
}
