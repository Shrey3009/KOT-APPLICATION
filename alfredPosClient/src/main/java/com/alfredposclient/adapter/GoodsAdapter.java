package com.alfredposclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alfredposclient.R;
import com.moonearly.model.GoodsModel;

import java.util.List;

/**
 * Created by Zun on 2016/12/16.
 */

public class GoodsAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsModel> list;
    private float size = 16;

    public GoodsAdapter(Context context, List<GoodsModel> list) {
        this.context = context;
        this.list = list;
    }

    public void ListSize(float f){
        this.size = f;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_page_item_listview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.listview_item_index = (TextView) convertView.findViewById(R.id.listview_item_index);
            viewHolder.listview_item_name = (TextView) convertView.findViewById(R.id.listview_item_name);
            viewHolder.listview_item_price = (TextView) convertView.findViewById(R.id.listview_item_price);
            viewHolder.listview_item_qty = (TextView) convertView.findViewById(R.id.listview_item_qty);
            viewHolder.listview_item_total = (TextView) convertView.findViewById(R.id.listview_item_total);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.listview_item_index.setTextSize(size);
        viewHolder.listview_item_name.setTextSize(size);
        viewHolder.listview_item_price.setTextSize(size);
        viewHolder.listview_item_qty.setTextSize(size);
        viewHolder.listview_item_total.setTextSize(size);

        viewHolder.listview_item_index.setText(list.get(position).getIndex());
        viewHolder.listview_item_name.setText(list.get(position).getName());
        String price = list.get(position).getPrice();
        viewHolder.listview_item_price.setText(price);
        viewHolder.listview_item_qty.setText(list.get(position).getQty());
        viewHolder.listview_item_total.setText(list.get(position).getTotal());
        return convertView;
    }

    static class ViewHolder {
        TextView listview_item_index, listview_item_name, listview_item_price, listview_item_qty, listview_item_total;
    }
}
