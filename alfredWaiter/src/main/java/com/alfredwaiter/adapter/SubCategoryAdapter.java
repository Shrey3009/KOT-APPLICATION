package com.alfredwaiter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alfredbase.javabean.ItemCategory;
import com.alfredwaiter.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends BaseAdapter {
    private List<ItemCategory> subCategorys = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    public SubCategoryAdapter(Context context, List<ItemCategory> subCategorys) {
        this.context = context;
        this.subCategorys = subCategorys;
        inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<ItemCategory> subCategorys){
        this.subCategorys = subCategorys;
    }
    @Override
    public int getCount() {
        return subCategorys.size();
    }

    @Override
    public Object getItem(int position) {
        return subCategorys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_main_category, null);
            viewHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ItemCategory itemCategory = subCategorys.get(position);
        viewHolder.tv_text.setText(itemCategory.getItemCategoryName());
        return convertView;
    }

    class ViewHolder{
        private TextView tv_text;
    }
}
