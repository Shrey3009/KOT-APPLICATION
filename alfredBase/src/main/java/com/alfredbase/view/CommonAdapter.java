package com.alfredbase.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

@SuppressWarnings("unused")
public abstract class CommonAdapter<T> extends BaseAdapter {

    public int layoutId;
    protected Context context;
    protected List<T> dataList;

    public CommonAdapter(final Context context, final List<T> dataList, final int layoutId) {
        this.context = context;
        this.dataList = dataList;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (dataList != null) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getInstance(context, convertView, parent, layoutId, position);
        viewHolder.setData(getItem(position));
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(final ViewHolder viewHolder, final T data, final int position);

}
