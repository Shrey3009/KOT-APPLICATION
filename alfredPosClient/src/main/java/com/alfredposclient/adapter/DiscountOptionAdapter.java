package com.alfredposclient.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.view.DiscountMoneyKeyboard.KeyBoardClickListener;

public class DiscountOptionAdapter extends BaseAdapter {
	private Context context;
	private List<String> options = new ArrayList<String>();
	private KeyBoardClickListener keyBoardClickListener;

	public static final int ITEM_WIDTH_HEIGHT = 100;
	private TextTypeFace textTypeFace = TextTypeFace.getInstance();
	private LayoutInflater inflater;
	public DiscountOptionAdapter(Context context, String[] options) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.options.addAll(Arrays.asList(options));
		if(options.length % 4 != 0)
		for(int i = 0; i < 4 - options.length % 4; i++){
			this.options.add("");
		}
	}
	public void setKeyBoardClickListener(
			KeyBoardClickListener keyBoardClickListener) {
		this.keyBoardClickListener = keyBoardClickListener;
	}

	@Override
	public int getCount() {
		return options.size();
	}

	@Override
	public Object getItem(int arg0) {
		return options.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.discount_option_item_layout, null);
			holder = new ViewHolder();
			holder.btn = (Button) arg1.findViewById(R.id.btn_item_name);
			textTypeFace.setTrajanProRegular(holder.btn);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		if(!TextUtils.isEmpty(options.get(arg0))){
			holder.btn.setTag(options.get(arg0) + "%");
			holder.btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					BugseeHelper.buttonClicked((String)arg0.getTag());
					if (keyBoardClickListener != null)
						keyBoardClickListener.onKeyBoardClick((String)arg0.getTag());
				}
			});
			if (App.instance.countryCode == ParamConst.CHINA){
				holder.btn.setText((100 - Integer.parseInt(options.get(arg0))) + "折");
			}else{
				holder.btn.setText(options.get(arg0) + "%");
			}
			
			holder.btn.setBackgroundResource(R.drawable.box_key_number_selector);
		}else{
			holder.btn.setText("");
			holder.btn.setBackgroundResource(R.drawable.box_key_number);
		}
		return arg1;
	}

	class ViewHolder {
		public Button btn;
	}

}
