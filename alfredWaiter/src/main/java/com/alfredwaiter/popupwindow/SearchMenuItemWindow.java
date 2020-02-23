package com.alfredwaiter.popupwindow;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Order;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.activity.MainPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchMenuItemWindow implements OnClickListener {
	private BaseActivity context;
	private Handler handler;
	private EditText et_search;
	private Button btn_cancel;
	private View contentView;
	private PopupWindow popupWindow;
	private View parentView;
	private Order order;
	private int currentGroupId;
	private List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();
	private ItemDetailAdapter adapter;
	private ListView lv_item_detail;
	
	public SearchMenuItemWindow(BaseActivity context,Handler handler, View parentView) {
		this.context = context;
		this.handler = handler;
		this.parentView = parentView;
	}

	public SearchMenuItemWindow(Context context, AttributeSet attrs) {
		init();
	}

	public void setAdapterData(Order order,List<ItemDetail> itemDetails,int GroupId) {
		this.currentGroupId = GroupId;
		this.order = order;
		if (itemDetails != null) {
			adapter.setItemDetails(itemDetails);
		}
	}

	private void init() {
		contentView = View.inflate(context, R.layout.search_menu, null);
		
		et_search = (EditText) contentView.findViewById(R.id.et_search);
		et_search.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					btn_cancel.setVisibility(View.VISIBLE);
//					showSearch();
				}
			}
		});
		et_search.addTextChangedListener(new TextWatcher() {

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
				search();
			}
		});
		btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BaseApplication.postHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);    
						//得到InputMethodManager的实例  
						imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken()
								,InputMethodManager.HIDE_NOT_ALWAYS); 
					}
				}, 200);
				Message msg = handler.obtainMessage();
				msg.what = MainPage.VIEW_EVENT_DISMISS_SEARCH;
				handler.sendMessage(msg);
			}
		});
		initTextTypeFace();
		initListView();

		popupWindow = new PopupWindow(parentView,
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		
	}

	private void initListView() {
		lv_item_detail = (ListView) contentView.findViewById(R.id.lv_item_detail);
		adapter = new ItemDetailAdapter(context,itemDetails);
		lv_item_detail.setAdapter(adapter);
		lv_item_detail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View view, int position,
					long id) {
				CommonUtil.inputMethodSet(context);
				int count = 1;
				ItemDetail itemDetail = (ItemDetail) parentView
						.getItemAtPosition(position);
//				List<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(order, currentGroupId);
//				for(OrderDetail orderDetail : orderDetails){
//					if (orderDetail.getOrderDetailStatus() < ParamConst.ORDERDETAIL_STATUS_PREPARED
//						&& orderDetail.getItemName().toString().equals(itemDetail.getItemName().toString())) {
//						count += orderDetail.getItemNum();
//					}
//				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("itemDetail", itemDetail);
				map.put("count", count);
				map.put("isAdd", true);
				Message msg = null;

				msg = handler.obtainMessage();
				msg.what = MainPage.VIEW_EVENT_MODIFY_ITEM_COUNT;
				msg.obj = map;
				handler.sendMessage(msg);
				
				msg = handler.obtainMessage();
				msg.what = MainPage.VIEW_EVENT_DISMISS_SEARCH;
				handler.sendMessage(msg);
			}
		});

	}
	
	private void initTextTypeFace() {
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod((TextView) contentView.findViewById(R.id.tv_name));
	}
	
	private void showSearch() {
		Message msg = handler.obtainMessage();
		msg.what = MainPage.VIEW_EVENT_SHOW_SEARCH;
		handler.sendMessage(msg);
	}

	private void search() {
		String key = et_search.getText().toString();
		Message msg = handler.obtainMessage();
		msg.what = MainPage.VIEW_EVENT_SEARCH;
		msg.obj = key;
		handler.sendMessage(msg);
	}
	
	public void show(Order order, List<ItemDetail> itemDetails) {
		this.order = order;
		this.itemDetails.clear();
		this.itemDetails.addAll(itemDetails);
		init();
		popupWindow.showAtLocation(parentView, Gravity.TOP, 0, 0);
		this.popupOutputMethidWindow();
	}

	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}	
	
	public boolean isShowing() {
		if (popupWindow != null) {
			return popupWindow.isShowing();
		}
		return false;
	}
	
	private void refresh() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		if (ButtonClickTimer.canClick(v)) {
//			switch (v.getId()) {
//			case R.id.btn_cancel: {
//				v.postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);    
//						//得到InputMethodManager的实例  
//						imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken()
//								,InputMethodManager.HIDE_NOT_ALWAYS); 
//					}
//				}, 200);
//				Message msg = handler.obtainMessage();
//				msg.what = MainPage.VIEW_EVENT_DISMISS_SEARCH;
//				handler.sendMessage(msg);
//				break;
//			}
//			default:
//				break;
//			}

		}

	}
	private void popupOutputMethidWindow(){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);    
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void cancelSearch() {
		btn_cancel.setVisibility(View.INVISIBLE);
		et_search.setText("");
		et_search.clearFocus();
	}
	
	public class ItemDetailAdapter extends BaseAdapter{
		private Context context;
		private List<ItemDetail> itemDetails = Collections.emptyList();
		private LayoutInflater inflater;
		private ItemDetailAdapter(Context context,List<ItemDetail> adapteItemDetails){
			this.context = context;
			inflater = LayoutInflater.from(context);
			if (adapteItemDetails != null) {
				itemDetails = adapteItemDetails;
			}
		}
		
		public void setItemDetails(List<ItemDetail> itemDetails) {
			this.itemDetails = itemDetails;
			this.notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return itemDetails.size();
		}

		@Override
		public Object getItem(int position) {
			return itemDetails.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_item_name, null);
				holder = new ViewHolder();
				holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
				holder.tv_item_price = (TextView) convertView.findViewById(R.id.tv_item_price);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			ItemDetail itemDetail = itemDetails.get(position);
			holder.tv_item_name.setText(itemDetail.getItemName());
			holder.tv_item_price.setText(itemDetail.getPrice());
			return convertView;
		}
	}
	class ViewHolder{
		public TextView tv_item_name; 
		public TextView tv_item_price; 
	}
	
}
