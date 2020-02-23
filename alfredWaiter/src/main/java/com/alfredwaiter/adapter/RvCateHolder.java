package com.alfredwaiter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RvCateHolder<T> extends RecyclerView.ViewHolder {
    protected RvListener mListener;

    public RvCateHolder(View itemView, int type, RvListener listener) {
        super(itemView);
        this.mListener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, getAdapterPosition());
            }
        });
    }


    public abstract void bindHolder(T t, int position);

}
