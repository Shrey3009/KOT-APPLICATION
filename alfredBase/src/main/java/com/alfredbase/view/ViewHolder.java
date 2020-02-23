package com.alfredbase.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用Holder
 */
@SuppressWarnings("unused")
public class ViewHolder {

    private SparseArray<View> componentViews;
    private View convertView;
    private Object data;

    private ViewHolder(Context context, ViewGroup parent, int layout, int position) {
        this.componentViews = new SparseArray<View>();
        if (layout != 0) {
            this.convertView = LayoutInflater.from(context).inflate(layout, parent, false);
        }
        if (this.convertView != null) {
            this.convertView.setTag(this);
        }
    }

    private ViewHolder(View convertView) {
        this.componentViews = new SparseArray<View>();
        this.convertView = convertView;

        if (this.convertView != null) {
            this.convertView.setTag(this);
        }
    }

    public static ViewHolder getInstance(Context context, View convertView, ViewGroup parent, int layout, int position) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(context, parent, layout, position);
        } else if (convertView.getTag() == null) {
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return holder;
    }

    public static ViewHolder resetInstance(Context context, View convertView, ViewGroup parent, int layout, final int position) {
        return new ViewHolder(context, parent, layout, position);
    }

    public View getView(final int viewId) {
        View componentView = componentViews.get(viewId);
        if (componentView == null && convertView != null) {
            componentView = convertView.findViewById(viewId);
            componentViews.put(viewId, componentView);
        }
        return componentView;
    }

    public View getConvertView() {
        return convertView;
    }

    public ViewHolder setText(int textViewId, String text) {
        ((TextView) getView(textViewId)).setText(text);
        return this;
    }

    public ViewHolder setText(int textViewId, CharSequence text) {
        ((TextView) getView(textViewId)).setText(text);
        return this;
    }

    public ViewHolder setImage(int imageViewId, int imageResourceId) {
        ((ImageView) getView(imageViewId)).setImageResource(imageResourceId);
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
