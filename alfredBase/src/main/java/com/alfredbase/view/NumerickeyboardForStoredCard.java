package com.alfredbase.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseApplication;
import com.alfredbase.R;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.TextTypeFace;

public class NumerickeyboardForStoredCard extends LinearLayout {
	private LayoutInflater inflater;
	private GridView gv_keyboard;
	private KeyBoardClickListener keyBoardClickListener;


	private TextTypeFace textTypeFace;
	public NumerickeyboardForStoredCard(Context context) {
		super(context);
		init(context);
	}

	public NumerickeyboardForStoredCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	

	private void init(Context context) {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.init(context);
		final String[] NUMERIC = { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", ".",
			"0", BaseApplication.getTopActivity().getResources().getString(R.string.delete) };
		View.inflate(context, R.layout.numeric_keyboard, this);
		inflater = LayoutInflater.from(context);
		gv_keyboard = (GridView) findViewById(R.id.gv_keyboard);
		gv_keyboard.setAdapter(new Adapter(NUMERIC));
		gv_keyboard.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				BugseeHelper.buttonClicked(NUMERIC[arg2]);
				if (keyBoardClickListener != null) {
					keyBoardClickListener.onKeyBoardClick(NUMERIC[arg2]);
				}

			}
		});
	}

	public void setKeyBoardClickListener(KeyBoardClickListener keyBoardClickListener) {
		this.keyBoardClickListener = keyBoardClickListener;
	}

	private final class Adapter extends BaseAdapter {
		private String[] NUMERIC;
		public Adapter(String[] NUMERIC) {
			this.NUMERIC =  NUMERIC;
		}
		
		@Override
		public int getCount() {
			return NUMERIC.length;
		}

		@Override
		public Object getItem(int arg0) {
			return NUMERIC[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			arg1 = inflater.inflate(R.layout.item_numeric_keyboard, null);
			TextView tv_text = (TextView) arg1.findViewById(R.id.tv_text);
			tv_text.setText(NUMERIC[arg0]);
			tv_text.setTextSize(getResources().getDimension(R.dimen.text_size_m_smail));
			textTypeFace.setTrajanProRegular(tv_text);
			return arg1;
		}

	}

	public interface KeyBoardClickListener {
		void onKeyBoardClick(String key);
	}

}
