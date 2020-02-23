package com.alfredkds.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.TextTypeFace;
import com.alfredkds.R;

public class CallNumboard extends LinearLayout implements OnClickListener{
	private KeyBoardClickListener keyBoardClickListener;
	private LinearLayout ll_money;
	private Button btn_1;
	private Button btn_2;
	private Button btn_3;
	private Button btn_4;
	private Button btn_5;
	private Button btn_6;
	private Button btn_7;
	private Button btn_8;
	private Button btn_9;
	private Button btn_0;
	private Button btn_00;
	private Button btn_Clear;
	private Button btn_Enter;
	private Button btn_Cancel;
	private TextTypeFace textTypeFace;

	public CallNumboard(Context context) {
		super(context);
		init(context);
	}

	public CallNumboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.money_keyboard, this);
		ll_money = (LinearLayout) findViewById(R.id.ll_money);
		ll_money.setVisibility(GONE);
		btn_1 = (Button) findViewById(R.id.btn_1);
		btn_2 = (Button) findViewById(R.id.btn_2);
		btn_3 = (Button) findViewById(R.id.btn_3);
		btn_4 = (Button) findViewById(R.id.btn_4);
		btn_5 = (Button) findViewById(R.id.btn_5);
		btn_6 = (Button) findViewById(R.id.btn_6);
		btn_7 = (Button) findViewById(R.id.btn_7);
		btn_8 = (Button) findViewById(R.id.btn_8);
		btn_9 = (Button) findViewById(R.id.btn_9);
		btn_0 = (Button) findViewById(R.id.btn_0);
		btn_00 = (Button) findViewById(R.id.btn_00);
		btn_Clear = (Button) findViewById(R.id.btn_Clear);
		btn_Enter = (Button) findViewById(R.id.btn_Enter);
		btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		btn_4.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_6.setOnClickListener(this);
		btn_7.setOnClickListener(this);
		btn_8.setOnClickListener(this);
		btn_9.setOnClickListener(this);
		btn_0.setOnClickListener(this);
		btn_00.setOnClickListener(this);
		btn_Clear.setOnClickListener(this);
		btn_Enter.setOnClickListener(this);
		btn_Cancel.setOnClickListener(this);
		initTextTypeFace();
	}

	private void initTextTypeFace() {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular(btn_0);
		textTypeFace.setTrajanProRegular(btn_00);
		textTypeFace.setTrajanProRegular(btn_1);
		textTypeFace.setTrajanProRegular(btn_2);
		textTypeFace.setTrajanProRegular(btn_3);
		textTypeFace.setTrajanProRegular(btn_4);
		textTypeFace.setTrajanProRegular(btn_5);
		textTypeFace.setTrajanProRegular(btn_6);
		textTypeFace.setTrajanProRegular(btn_7);
		textTypeFace.setTrajanProRegular(btn_8);
		textTypeFace.setTrajanProRegular(btn_9);
		textTypeFace.setTrajanProRegular(btn_Enter);
		textTypeFace.setTrajanProRegular(btn_Clear);
		textTypeFace.setTrajanProRegular(btn_Cancel);
	}
	
	public void setKeyBoardClickListener(
			KeyBoardClickListener keyBoardClickListener) {
		this.keyBoardClickListener = keyBoardClickListener;
	}

	public interface KeyBoardClickListener {
		void onKeyBoardClick(String key);
	}

	@Override
	public void onClick(View v) {
		Button button = (Button) v;
		BugseeHelper.buttonClicked((String) button.getText());
		switch (v.getId()) {
		default:
			if (keyBoardClickListener != null)
				keyBoardClickListener.onKeyBoardClick(button.getText()
						.toString());
			break;
		}

	}
}
