package com.alfredkds.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alfredbase.javabean.KotItemDetail;
import com.alfredkds.R;
import com.alfredkds.javabean.KotItem;

import java.util.List;

/**
 * Created by Arif S. on 8/22/19
 */
public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.ViewHolder> {

    private Context context;
    private List<KotItem> data;

    public PendingListAdapter(Context context, List<KotItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.item_detail_item, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KotItem item = data.get(position);

        holder.tvItemName.setText(item.getItemDetailName());
        holder.tvItemCount.setText(item.getQty() + "");

        if (position >= data.size() - 1)
            holder.black_line.setVisibility(View.GONE);
        else
            holder.black_line.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<KotItem> kotItemList) {
        this.data = kotItemList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemName;
        TextView tvItemCount;
        View black_line;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvItemCount);
            black_line = (View) itemView.findViewById(R.id.black_line);
        }
    }
}
