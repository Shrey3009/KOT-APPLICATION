package com.alfredposclient.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.adapter.ItemDetailAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPageSearchView extends LinearLayout implements OnClickListener{
	private static final String TAG = MainPageSearchView.class.getSimpleName();
	private BaseActivity parent;
	private Context context;
	private SearchView et_search;
	private ImageView iv_cancel;
	private Order order;
	private Handler handler;
	private MyGridView gv_items;
	private ItemDetailAdapter itemDetailAdapter;
	private InputMethodManager imm;
	private List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();
	private List<ItemDetail> itemDetailList = new ArrayList<ItemDetail>();
	private int selectNum = 0;
	private TextView tv_select_num;
	public MainPageSearchView(Context context) {
		super(context);
		this.context = context;
		itemDetailList.clear();
		init(context);
	}

	public MainPageSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		itemDetailList.clear();
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.main_page_search_view, this);
		LinearLayout ll_blank_left = (LinearLayout) findViewById(R.id.ll_blank_left);
		LinearLayout ll_blank_right = (LinearLayout) findViewById(R.id.ll_blank_right);
		ll_blank_left.setOnClickListener(null);
		ll_blank_right.setOnClickListener(null);
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		gv_items = (MyGridView) findViewById(R.id.gv_items);
		et_search = (SearchView) findViewById(R.id.et_search);
		tv_select_num = (TextView) findViewById(R.id.tv_select_num);
		textTypeFace.setTrajanProBlod(tv_select_num);
//		et_search.findViewById(R.id.submit_area).setBackgroundColor(parent.getResources().getColor(R.color.white));
//		et_search.performClick();
		Class<?> argClass = et_search.getClass();
		try {
			Field ownField = argClass.getDeclaredField("mSearchPlate");
			ownField.setAccessible(true);
			View mSubmitArea = (View) ownField.get(et_search);
			mSubmitArea.setBackgroundColor(getResources().getColor(R.color.white));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Field mQueryTextViewField = argClass.getDeclaredField("mSearchSrcTextView");
			mQueryTextViewField.setAccessible(true);
			View mQueryTextView = (View) mQueryTextViewField.get(et_search);

			textTypeFace.setTrajanProBlod((EditText)mQueryTextView);
		} catch (Exception e) {
			try {
				Field mQueryTextViewField = argClass.getDeclaredField("mQueryTextView");
				mQueryTextViewField.setAccessible(true);
				View mQueryTextView = (View) mQueryTextViewField.get(et_search);
				textTypeFace.setTrajanProBlod((EditText)mQueryTextView);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

//		textTypeFace.setTrajanProBlod((EditText) et_search.findViewById(R.id.search_src_text));
//		et_search.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				search();
//			}
//		});
		et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			// 当点击搜索按钮时触发该方法
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			// 当搜索内容改变时触发该方法
			@Override
			public boolean onQueryTextChange(String newText) {
				if (!TextUtils.isEmpty(newText)){
					search(newText);
				}else{
//					mListView.clearTextFilter();
					search("");
				}
				return false;
			}
		});
		findViewById(R.id.ll_search).setOnClickListener(null);
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
		iv_cancel.setOnClickListener(this);
		itemDetailAdapter = new ItemDetailAdapter(context, itemDetailList);
		gv_items.setAdapter(itemDetailAdapter);
		gv_items.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//CommonUtil.inputMethodSet(parent);
				CommonUtil.hideSoftkeyBoard(parent);
				
				ItemDetail itemDetail = (ItemDetail) arg0
						.getItemAtPosition(arg2);
				OrderDetail orderDetail = ObjectFactory.getInstance()
						.getOrderDetail(order, itemDetail, 0);
				Message msg = null;

				msg = handler.obtainMessage();
				msg.what = MainPage.VIEW_EVENT_ADD_ORDER_DETAIL;
				msg.obj = orderDetail;
				handler.sendMessage(msg);
				selectNum++;
				tv_select_num.setText(selectNum+"");
			}
		});
		gv_items.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if(arg1 == OnScrollListener.SCROLL_STATE_IDLE){
					if (imm != null)
						imm.hideSoftInputFromWindow(MainPageSearchView.this.parent.getCurrentFocus().getWindowToken()
								,InputMethodManager.HIDE_NOT_ALWAYS);
				}
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				
			}
		});
	}

	public void setParam(BaseActivity parent, Order order,
			List<ItemDetail> itemDetails, Handler handler, boolean showKey) {
		System.out.println("111111");
		if (itemDetails == null)
			itemDetails = Collections.emptyList();
		this.parent = parent;
		this.order = order;
		this.itemDetailList.clear();
		this.itemDetailList.addAll(itemDetails);
		this.itemDetails = itemDetails;
		this.handler = handler;
		this.selectNum = 0;
//		et_search.setFocusable(true);
//		et_search.setFocusableInTouchMode(true);
//		et_search.setQuery("", false);
		et_search.requestFocus();
		et_search.onActionViewExpanded();
		tv_select_num.setText("");
		if(showKey) {
			imm = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null)
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
		gv_items.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						 int numColumns =
						 (int)Math.floor((gv_items.getWidth() - gv_items.getPaddingLeft() - gv_items.getPaddingRight())/(gv_items.getVerticalSpacing()
						 + ScreenSizeUtil.dip2px(MainPageSearchView.this.parent,
						 ItemDetailAdapter.ITEM_WIDTH_HEIGHT)));
						gv_items.setNumColumns(numColumns);
					}
				});
		refresh();
	}



	private void search(String key) {
//		String key = et_search.getText().toString();
		itemDetailList.clear();
		if (key != null) {
			key = key.trim().replaceAll("\\s+","");
			for (ItemDetail itemDtail : itemDetails) {
				String name = itemDtail.getItemName();
//				String name = CommonUtil.getInitial(itemDtail.getItemName());
//				if (name.contains(key) || name.contains(key.toUpperCase())) {
				if(name.toLowerCase().contains(key.toLowerCase())){
					itemDetailList.add(itemDtail);
					continue;
				}
			}
		}
		gv_items.postDelayed(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		}, 100);
//		if(handler == null || key == null)
//			return;
//		Message msg = handler.obtainMessage();
//		msg.what = MainPage.VIEW_EVENT_SEARCH;
//		msg.obj = key;
//		handler.sendMessage(msg);
	}
	
	public void cancelSearch() {
//		et_search.setText("");
//		et_search.clearFocus();
	}
	
	private void refresh() {
		if (itemDetailAdapter != null)
			itemDetailAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_cancel: {
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (imm != null)
						imm.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken()
							,InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}, 200);
			Message msg = handler.obtainMessage();
			msg.what = MainPage.VIEW_EVENT_DISMISS_SEARCH;
			handler.sendMessage(msg);
			break;
		}
		default:
			break;
		}
	}

}
