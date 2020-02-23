package com.alfredkds.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.utils.TextTypeFace;
import com.alfredkds.R;
import com.alfredkds.activity.CallNumActivity;
import com.alfredkds.activity.KitchenOrder;
import com.alfredkds.global.App;
import com.alfredkds.global.UIHelp;

public class TopBarView extends LinearLayout implements OnClickListener{
	public TextView kitchName;
	private ImageView iv_classify;
	private ImageView iv_refresh;
	public ImageView iv_setting;
	private ImageView btn_call_num;
	private KitchenOrder parent;
	 
	public TopBarView(Context context) {
		super(context);
		this.parent = (KitchenOrder) context;
		init(context);
	}

	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.parent = (KitchenOrder) context;
		init(context);
		
	}


	private void init(Context context) {
		View.inflate(context, R.layout.top_bar, this);
		kitchName = (TextView) findViewById(R.id.title_kitchen);
		iv_classify = (ImageView)findViewById(R.id.iv_classify);
		iv_refresh = (ImageView)findViewById(R.id.iv_refresh);
		iv_setting = (ImageView)findViewById(R.id.iv_setting);
		btn_call_num = (ImageView) findViewById(R.id.btn_call_num);
		kitchName.setText(App.instance.getPrinter().getPrinterName());
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod(kitchName);
		iv_classify.setOnClickListener(this);
		iv_refresh.setOnClickListener(this);
		iv_setting.setOnClickListener(this);
		btn_call_num.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_classify:
			UIHelp.startFilter(parent);
			break;
		case R.id.iv_refresh:
			if (App.instance.getSystemSettings().isKdsLan()) {
				parent.madapter.notifyDataSetChanged();
			} else {
				parent.adapter.notifyDataSetChanged();
			}

			break;
		case R.id.iv_setting:
			UIHelp.startSetting(parent);
			break;
		case R.id.btn_call_num:
			UIHelp.startCallNum(parent);
			break;
		default:
			break;
		}
	}
}
