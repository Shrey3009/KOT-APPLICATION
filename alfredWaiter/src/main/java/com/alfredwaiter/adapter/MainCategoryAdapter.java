package com.alfredwaiter.adapter;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.listener.RvItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainCategoryAdapter extends RvAdapter<ItemMainCategory> {

    private int checkedPosition;
    private TextTypeFace textTypeFace = TextTypeFace.getInstance();
    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public MainCategoryAdapter(Context context,List<ItemMainCategory> itemMainCategories, RvItemClickListener listener) {
        super(context, itemMainCategories, listener);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_main_category;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new CateHolder(view, viewType, listener);
    }

    private class CateHolder extends RvHolder<ItemMainCategory> {

        private TextView tv_text;
        private View mView;

       CateHolder(View itemView, int type, RvItemClickListener listener) {
            super(itemView, type, listener);
            this.mView = itemView;
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);

        }

        @Override
        public void bindHolder(ItemMainCategory item, int position) {
            tv_text.setText(item.getMainCategoryName());
            textTypeFace.setTrajanProRegular(tv_text);

            if (position == checkedPosition) {
                mView.setBackgroundResource(R.color.bg_item_category);

            } else {
                mView.setBackgroundResource(android.R.color.transparent);

            }
        }

    }
}
