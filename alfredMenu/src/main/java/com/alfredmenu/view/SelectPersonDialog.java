package com.alfredmenu.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.alfredmenu.activity.MainPage;
import com.alfredmenu.R;

public class SelectPersonDialog extends Dialog {
	private LayoutInflater inflater;
	private GridView gv_person_index;
	private int persons;
	private Adapter adapter;
	private Handler handler;

	public SelectPersonDialog(Context context, Handler handler) {
		super(context, R.style.Dialog_bocop);
		inflater = LayoutInflater.from(context);
		this.handler = handler;
		init();
	}

	private void init() {
		View contentView = View.inflate(getContext(),
				R.layout.loading_select_person, null);
		setContentView(contentView);
		gv_person_index = (GridView) contentView
				.findViewById(R.id.gv_person_index);
		adapter = new Adapter();
		gv_person_index.setAdapter(adapter);
		gv_person_index.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == persons + 1) {
					persons++;
					adapter.notifyDataSetChanged();
				} else {
					dismiss();
					handler.sendMessage(handler.obtainMessage(
							MainPage.VIEW_EVENT_SET_PERSON_INDEX, arg2));
				}
			}
		});
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}

	public void show(int persons) {
		this.persons = persons;
		adapter.notifyDataSetChanged();
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	private final class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return persons + 2;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			arg1 = inflater.inflate(R.layout.item_person_index, null);
			TextView tv_text = (TextView) arg1.findViewById(R.id.tv_text);
			if (arg0 == 0) {
				tv_text.setText("?");
			} else if (arg0 == persons + 1) {
				tv_text.setText("+");
			} else {
				tv_text.setText(arg0 + "");
			}
			return arg1;
		}

	}
}
