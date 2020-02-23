package com.alfredwaiter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alfredwaiter.listener.RvItemClickListener;


public abstract class RvHolder<T> extends RecyclerView.ViewHolder {
    protected RvItemClickListener mListener;

    public RvHolder(View itemView, int type, RvItemClickListener listener) {
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
