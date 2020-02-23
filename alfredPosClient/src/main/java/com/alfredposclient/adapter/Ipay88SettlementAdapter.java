package com.alfredposclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredposclient.R;

import java.util.List;


public class Ipay88SettlementAdapter extends RecyclerView.Adapter<Ipay88SettlementAdapter.ViewHolder> {

    private List<PaymentMethod> models;
    private ClickListener clickListener;
    private Context context;

    public Ipay88SettlementAdapter(List<PaymentMethod> models, ClickListener clickListener) {
        this.models = models;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_settlement, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        context = parent.getContext();
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PaymentMethod model = models.get(position);

        holder.tvSettlement.setVisibility(View.GONE);
        holder.ivSettlement.setVisibility(View.VISIBLE);

        int imgRes = SettlementAdapter.getImageResource(model.getPaymentTypeId());

        holder.ivSettlement.setVisibility(View.VISIBLE);
        holder.ivSettlement.setImageDrawable(context.getResources().getDrawable(imgRes));

        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.ipay88SettlementAdapteronClick(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    /* Interface for handling clicks */
    public interface ClickListener {
        public void ipay88SettlementAdapteronClick(PaymentMethod paymentMethod);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivSettlement;
        public TextView tvSettlement;
        public LinearLayout llParent;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSettlement = (ImageView) itemView.findViewById(R.id.iv_settlement);
            tvSettlement = (TextView) itemView.findViewById(R.id.tv_settlement);
            llParent = (LinearLayout) itemView.findViewById(R.id.ll_parent);

        }

    }
}
