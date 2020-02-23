package com.alfredmenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredmenu.javabean.ModifierVariance;
import com.alfredmenu.popupwindow.ModifierSetItemCountWindow;
import com.alfredmenu.R;

public class ModifierCountView extends LinearLayout implements OnClickListener {
	private TextView modifier_tv_count;
	private OnCountChange onCountChange;
	private ModifierVariance modifierVariance;
	private ItemDetail itemDetail;
	private ModifierSetItemCountWindow setItemCountWindow;
	public ModifierCountView(Context context) {
		super(context);
		init(context);
	}

	public ModifierCountView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.modifier_count_view, this);
		modifier_tv_count = (TextView) findViewById(R.id.modifier_tv_count);
	}

	public void setOnCountChange(OnCountChange onCountChange) {
		this.onCountChange = onCountChange;
	}
	public void setIsCanClick(boolean isCanClick){
		LinearLayout modifier_ll_add = (LinearLayout) findViewById(R.id.modifier_ll_add);
		LinearLayout modifier_ll_minus = (LinearLayout) findViewById(R.id.modifier_ll_minus);
		LinearLayout modifier_ll_count = (LinearLayout) findViewById(R.id.modifier_ll_count);
		if(isCanClick){
			modifier_ll_minus.setOnClickListener(this);
			modifier_ll_add.setOnClickListener(this);
			modifier_ll_count.setOnClickListener(this);
		}else{
			modifier_ll_add.setOnClickListener(this);
			modifier_ll_count.setOnClickListener(this);
		}
	}
	public void setParam(ItemDetail itemDetail, ModifierVariance modifierVariance, ModifierSetItemCountWindow setItemCountWindow){
		this.modifierVariance = modifierVariance;
		this.setItemCountWindow = setItemCountWindow;
		this.itemDetail = itemDetail;
	}
	public void setInitCount(int count) {
		modifier_tv_count.setText(count + "");
	}

	@Override
	public void onClick(View v) {
		if(!ButtonClickTimer.canClick(v)){
			return;
		}
		switch (v.getId()) {
		case R.id.modifier_ll_minus: {
			int count = 0;
			try {
				count = Integer.parseInt(modifier_tv_count.getText().toString());
				count--;
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (count < 0)
				count = 0;
			modifier_tv_count.setText(count + "");
			if (onCountChange != null) {
				onCountChange.onChange(modifierVariance, count, false);
			}
			break;
		}
		case R.id.modifier_ll_add: {
			int count = 0;
			try {
				count = Integer.parseInt(modifier_tv_count.getText().toString());
				count++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (count < 1)
				count = 1;
			modifier_tv_count.setText(count + "");
			if (onCountChange != null) {
				onCountChange.onChange(modifierVariance, count, true);
			}
			break;
		}
		case R.id.modifier_ll_count:
//			setItemCountWindow.show(Integer.parseInt(modifier_tv_count.getText().toString()),itemDetail, modifierVariance);
			break;
		default:
			break;
		}

	}

	public interface OnCountChange {
		void onChange(ModifierVariance modifierVariance, int count, boolean isAdd);
	}

}
