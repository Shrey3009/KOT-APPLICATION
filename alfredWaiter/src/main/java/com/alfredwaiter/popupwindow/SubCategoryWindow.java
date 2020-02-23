package com.alfredwaiter.popupwindow;

import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredwaiter.R;
import com.alfredwaiter.activity.MainPage;
import com.alfredwaiter.adapter.SubCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryWindow {
    private BaseActivity context;
    private Handler handler;
    private View parentView;
    private View contentView;
    private PopupWindow popupWindow;
    private ListView lv_item_category;
    private List<ItemCategory> itemCategories = new ArrayList<>();
    private SubCategoryAdapter subCategoryAdapter;
    public SubCategoryWindow(BaseActivity context,Handler handler, View parentView) {
        this.context = context;
        this.handler = handler;
        this.parentView = parentView;
        init();
    }

    private void init(){
        contentView = View.inflate(context, R.layout.sub_category_popwindow, null);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.bottomInOutStyle);
        lv_item_category = (ListView) contentView.findViewById(R.id.lv_item_category);
        LinearLayout ll_item_category = (LinearLayout) contentView.findViewById(R.id.ll_item_category);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) ScreenSizeUtil.width/ 3 * 2, (int)ScreenSizeUtil.height/ 3 * 2);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        ll_item_category.setLayoutParams(lp);
        subCategoryAdapter = new SubCategoryAdapter(context, itemCategories);
        lv_item_category.setAdapter(subCategoryAdapter);
        lv_item_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemCategory itemCategory = itemCategories.get(position);
                handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_CLICK_SUB_CATEGORY, itemCategory));
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
    }

    public void show(List<ItemCategory> itemCategories){
        if(itemCategories == null || itemCategories.size() ==0){
            return;
        }
        this.itemCategories = itemCategories;
        popupWindow
                .showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
        subCategoryAdapter.setData(this.itemCategories);
        subCategoryAdapter.notifyDataSetChanged();
    }

    public void dismiss(){
        if(popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
}
