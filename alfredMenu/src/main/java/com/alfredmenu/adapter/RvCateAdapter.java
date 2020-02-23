package com.alfredmenu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class RvCateAdapter<T> extends RecyclerView.Adapter<RvCateHolder> {
    protected List<T> list;
    protected Context mContext;
    protected RvListener listener;
    protected LayoutInflater mInflater;


    public RvCateAdapter(Context context, List<T> list, RvListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.listener = listener;
    }
    public List<T> getData(){
        return list;
    }
    @Override
    public RvCateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(getLayoutId(viewType), parent, false);

        return getHolder(view, viewType);
    }

    protected abstract int getLayoutId(int viewType);


    public void onBindViewHolder(RvCateHolder holder, int position) {

        holder.bindHolder(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    protected abstract RvCateHolder getHolder(View view, int viewType);

}
