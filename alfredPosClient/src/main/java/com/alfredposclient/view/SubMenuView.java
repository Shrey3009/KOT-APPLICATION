package com.alfredposclient.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;

public class SubMenuView extends LinearLayout {
	private static final int WIDTH_HEIGHT = 100;
	private Handler mHandler;
	private BaseActivity parent;
	private LinearLayout ll_sub_menu_item;
	private int itemMainCategoryId;
	private TextTypeFace textTypeFace;

	public SubMenuView(Context context) {
		super(context);
		this.parent = (BaseActivity) context;
		init(context);
	}

	public SubMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	private void init(Context context) {
		View.inflate(context, R.layout.sub_menu_view, this);
		ll_sub_menu_item = (LinearLayout) findViewById(R.id.ll_sub_menu_item);
		initTextTypeFace();
	}

	public void setParams(int width, List<ItemCategory> itemCategories,
			Handler handler) {
		this.mHandler = handler;
		if (itemCategories.size() == 0) {
			return;
		}
		
		//此处有待修改
		if (!itemCategories.isEmpty() && 
				itemCategories.get(0) != null &&
				itemCategories.get(0).getItemMainCategoryId() != 0) {
			this.itemMainCategoryId = itemCategories.get(0).getItemMainCategoryId();
		}
		// 第一个不用于显示，添加null代替
		if (itemCategories.get(0) != null) {
			itemCategories.add(0, null);
		}
		int count = (int) Math.floor(width/(ScreenSizeUtil.dip2px(parent, 20)+ScreenSizeUtil.dip2px(parent, WIDTH_HEIGHT)));
		int childCount = itemCategories.size();
		int row_count = childCount / count;
		if (childCount % count != 0) {
			row_count++;
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
				ScreenSizeUtil.dip2px(parent, WIDTH_HEIGHT),
				ScreenSizeUtil.dip2px(parent, WIDTH_HEIGHT));
		int addCount = 0;
		for (int i = 0; i < row_count; i++) {
			LinearLayout layout = new LinearLayout(parent);
			layout.setLayoutParams(params);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < count; j++) {
				if (addCount >= childCount)
					break;
				View childView = View.inflate(parent, R.layout.sub_menu_item,
						null);
				childParams.setMargins(
						ScreenSizeUtil.dip2px(parent, 10),
						ScreenSizeUtil.dip2px(parent, 10), 
						ScreenSizeUtil.dip2px(parent, 10), 
						ScreenSizeUtil.dip2px(parent, 10));
				childView.setLayoutParams(childParams);
				ImageView iv_all_sub_menu = (ImageView) childView
						.findViewById(R.id.iv_all_sub_menu);
				TextView sub_menu_item = (TextView) childView
						.findViewById(R.id.tv_sub_menu_item);
				// 第一个为选择全部
				if (i == 0 && j == 0) {
					iv_all_sub_menu.setImageResource(R.drawable.all_sub_menu_selector);
					sub_menu_item.setVisibility(View.GONE);
					childView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!ButtonClickTimer.canClick(v)) {
								return;
							}
							mHandler.sendMessage(mHandler.obtainMessage(MainPage.VIEW_SUB_MENU_ALL,itemMainCategoryId));
						}
					});
					addCount++;
				} else {
					final ItemCategory itemCategory = itemCategories.get(i
							* count + j);
					textTypeFace.setTrajanProRegular(sub_menu_item);
					sub_menu_item.setText(itemCategory.getItemCategoryName());
					childView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!ButtonClickTimer.canClick(v)) {
								return;
							}
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("itemMainCategoryId", itemCategory.getItemMainCategoryId());
							params.put("itemCategoryId", itemCategory.getId());
							mHandler.sendMessage(mHandler.obtainMessage(MainPage.VIEW_SUB_MENU,params));
						}
					});
					addCount++;
				}
				layout.addView(childView);
			}
			if (i == 0) {
				ll_sub_menu_item.removeAllViews();
			}
			ll_sub_menu_item.addView(layout);
		}
	}
	private void initTextTypeFace(){
		textTypeFace = TextTypeFace.getInstance();
	}
}
