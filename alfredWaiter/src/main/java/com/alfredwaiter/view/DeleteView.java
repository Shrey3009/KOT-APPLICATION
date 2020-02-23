package com.alfredwaiter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;

public class DeleteView extends LinearLayout implements OnClickListener {
	private TextView tv_text;
	private DeleteListener listener;

	public DeleteView(Context context) {
		super(context);
		init(context);
	}

	public DeleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.delete_view, this);
		tv_text = (TextView) findViewById(R.id.tv_text);
		findViewById(R.id.iv_delete).setOnClickListener(this);
	}

	public void setDeleteListener(DeleteListener listener) {
		this.listener = listener;
	}

	public void setText(String text,TextTypeFace textTypeFace) {
		tv_text.setText(text);
		textTypeFace.setTrajanProRegular(tv_text);
	}

	public interface DeleteListener {
		void deleteClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_delete: {
			if (listener != null) {
				listener.deleteClick();
			}
			break;
		}
		default:
			break;
		}

	}
}
