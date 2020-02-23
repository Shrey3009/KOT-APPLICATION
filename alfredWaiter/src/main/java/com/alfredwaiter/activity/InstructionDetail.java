package com.alfredwaiter.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.google.gson.reflect.TypeToken;

public class InstructionDetail extends BaseActivity {

	private OrderDetail orderDetail;
	private String specialInstraction;
	private EditText et_instructions;
	private Button btn_inst_save;
	private ListView lv_item_inst;
	private InstAdapter instAdapter;
	private List<String> instHistory;
	private LayoutInflater inflater;
	private StringBuffer sBuffer;
	private TextTypeFace textTypeFace;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_instruction_detail);
		getIntentData();
		inflater = LayoutInflater.from(this);
		btn_inst_save = (Button) findViewById(R.id.btn_inst_save);
		btn_inst_save.setOnClickListener(this);
		findViewById(R.id.iv_back).setOnClickListener(this);
		if (orderDetail == null) {
			return;
		}
		et_instructions = (EditText) findViewById(R.id.et_instructions);
		et_instructions.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					//
				}
			}
		});
		et_instructions.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				TextChangedResult();
			}
		});
		initListView();
		initTextTypeFace();
		initTitle();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		orderDetail = (OrderDetail) intent.getSerializableExtra("orderDetail");
		sBuffer = new StringBuffer();
		if (orderDetail != null && orderDetail.getSpecialInstractions() != null) {
			sBuffer.append(orderDetail.getSpecialInstractions());
		}
	}

	public void initTitle(){
		findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_title_name)).setText(context.getResources().getString(R.string.instruction));
	}
	
	private void initListView() {
		lv_item_inst = (ListView) findViewById(R.id.lv_item_inst);
		instHistory = Store.getObject(context,
				Store.WAITER_INSTRUCTION_HISTORY,new TypeToken<List<String>>(){}.getType());
		if (instHistory == null) {// 开始没有内容
			instHistory = new ArrayList<String>();
		}
		instAdapter = new InstAdapter(removeDuplicateWithOrder(instHistory));
		instAdapter.setDate(instHistory);// 初始化时，是全部内容
		lv_item_inst.setAdapter(instAdapter);
		lv_item_inst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				et_instructions.setText(instHistory.get(position));
				et_instructions.setSelection(instHistory.get(position)
						.toString().length());
				instHistory = (removeDuplicateWithOrder(instHistory));
			}
		});
	}

	public void TextChangedResult() {
		specialInstraction = et_instructions.getText().toString().trim();
		List<String> instList = new ArrayList<String>();
		if (specialInstraction != null) {
			if (instHistory == null) {
				return;
			}
			specialInstraction.trim().replaceAll("\\s+", "");
			for (int i = 0; i < instHistory.size(); i++) {
				String name = CommonUtil.getInitial(instHistory.get(i));
				if (name.contains(specialInstraction)
						|| name.contains(specialInstraction.toUpperCase())) {
					instList.add(instHistory.get(i));
					continue;
				}
			}
			instAdapter.setDate(removeDuplicateWithOrder(instList));
		} else {
			instAdapter.setDate(removeDuplicateWithOrder(instHistory));
		}
	}

	//去掉list中重复元素，并且保持顺序
	public List<String> removeDuplicateWithOrder(List<String> list) {
		Set<String> set = new HashSet<String>();
		List<String> newList = new ArrayList<String>();
		for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add((String) element))
				newList.add((String) element);
		}
		list.clear();
		list.addAll(newList);
		return list;
	}

	@Override
	protected void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		switch (v.getId()) {
		case R.id.btn_inst_save:
			specialInstraction = et_instructions.getText().toString().trim();
			if (specialInstraction.isEmpty()) {
				this.finish();
				return;
			}
			instHistory.add(0, specialInstraction);
			Store.saveObject(context, Store.WAITER_INSTRUCTION_HISTORY,
					instHistory);
			// if (sBuffer.equals("") || sBuffer == null || sBuffer.length() ==
			// 0) {
			// sBuffer.append(specialInstraction);
			// } else {
			 sBuffer.insert(0, specialInstraction + "#");
//			sBuffer.append(specialInstraction + "#");
			// }
			orderDetail.setSpecialInstractions(sBuffer.toString());
			OrderDetailSQL.updateOrderDetail(orderDetail);
			setResult(OrderDetailPage.REFRESH_ORDER_DETAIL);
			this.finish();
			break;
		case R.id.iv_back:
			this.finish();
			break;
		default:
			break;
		}
	}

	public class InstAdapter extends BaseAdapter {

		private List<String> instListDate = new ArrayList<String>();

		public InstAdapter(List<String> instListDate) {
			super();
			if (instListDate == null) {
				return;
			}
			this.instListDate = instListDate;
		}

		private void setDate(List<String> instListDate) {
			if (instListDate == null) {
				return;
			}
			this.instListDate = instListDate;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return instListDate.size();
		}

		@Override
		public Object getItem(int position) {
			return instListDate.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.item_specialinstruction, null);
				holder = new ViewHolder();
				holder.tv_inst_name = (TextView) convertView
						.findViewById(R.id.tv_inst_name);
				holder.iv_inst_delete = (ImageView) convertView
						.findViewById(R.id.iv_inst_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_inst_name.setText(instListDate.get(position));
			textTypeFace.setTrajanProRegular(holder.tv_inst_name);
			holder.iv_inst_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					instHistory.remove(position);
					specialInstraction = et_instructions.getText().toString().trim();
					TextChangedResult();
//					setDate(instHistory);
					Store.saveObject(context, Store.WAITER_INSTRUCTION_HISTORY,
							instHistory);
				}
			});
			return convertView;
		}

	}

	public class ViewHolder {
		public TextView tv_inst_name;
		public ImageView iv_inst_delete;
	}

	private void initTextTypeFace() {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace
				.setTrajanProBlod((TextView) findViewById(R.id.tv_title_name));
		textTypeFace
				.setTrajanProRegular((EditText) findViewById(R.id.et_instructions));
		textTypeFace
				.setTrajanProBlod((Button) findViewById(R.id.btn_inst_save));
	}
}
