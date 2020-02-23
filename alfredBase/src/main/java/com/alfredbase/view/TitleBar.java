package com.alfredbase.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.R;
import com.alfredbase.utils.ButtonClickTimer;

public class TitleBar extends LinearLayout implements OnClickListener {
	private BaseActivity parent;
	private Handler handler;
	private TextView tv_table;
	
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
		tv_table = (TextView) findViewById(R.id.tv_table);
	}
	
	public void setParams(BaseActivity parent, Handler handler,String titleName) {
		this.parent = parent;
		this.handler = handler;
		tv_table.setText(titleName);
	}

	@Override
	public void onClick(View v) {
		if (ButtonClickTimer.canClick(v)) {
			if (v.getId() == R.id.ll_back) {
				parent.finish();
			}

		}

	}
}
