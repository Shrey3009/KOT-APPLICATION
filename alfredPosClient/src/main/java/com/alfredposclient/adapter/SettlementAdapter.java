package com.alfredposclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;

import java.util.List;

import static com.alfredbase.ParamConst.SETTLEMENT_TYPE_CASH;
import static com.alfredbase.ParamConst.SETTLEMENT_TYPE_IPAY88;


public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.ViewHolder> {

    private List<SettlementRestaurant> models;
    private ClickListener clickListener;
    private Context context;

    public SettlementAdapter(List<SettlementRestaurant> models, ClickListener clickListener) {
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
        final SettlementRestaurant model = models.get(position);

        holder.tvSettlement.setVisibility(View.GONE);
        holder.ivSettlement.setVisibility(View.GONE);

        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(holder.tvSettlement);

        if (!TextUtils.isEmpty(model.getOtherPaymentId())) {
            if (model.getMediaId() == SETTLEMENT_TYPE_CASH) {
                holder.tvSettlement.setVisibility(View.VISIBLE);
                holder.tvSettlement.setText("Custom");
            }
            if (model.getMediaId() == SETTLEMENT_TYPE_IPAY88) {
                holder.tvSettlement.setVisibility(View.VISIBLE);
                holder.tvSettlement.setText("IPAY88");
            }
        } else {
            int imgRes = getImageResource(model.getMediaId().longValue());
            if (imgRes == -1) {
                holder.tvSettlement.setVisibility(View.VISIBLE);
                holder.tvSettlement.setText("Other\n" + model.getMediaId());
            } else {
                holder.ivSettlement.setVisibility(View.VISIBLE);
                holder.ivSettlement.setImageResource(imgRes);
            }
        }


        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.settlementAdapteronClick(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    /* Interface for handling clicks */
    public interface ClickListener {
        public void settlementAdapteronClick(SettlementRestaurant settlementRestaurant);

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

    static int getImageResource(Long mediaId) {
        int drawable = -1;
        if (mediaId == ParamConst.SETTLEMENT_TYPE_MASTERCARD) {
            drawable = R.drawable.ic_mastercard;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_UNIPAY) {
            drawable = R.drawable.ic_pay88_unionpay;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_VISA) {
            drawable = R.drawable.ic_visa;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_AMEX) {
            drawable = R.drawable.ic_american_express;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_JCB) {
            drawable = R.drawable.ic_jcb;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_DINNER_INTERMATIONAL) {
            drawable = R.drawable.ic_diners_club;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_NETS) {
            drawable = R.drawable.ic_nets;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_ALIPAY) {
            drawable = R.drawable.ic_pay88_alipay;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_EZLINK) {
            drawable = R.drawable.ic_ezlink;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_PAYPAL) {
            drawable = R.drawable.ic_paypal;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_PAYHALAL || mediaId == ParamConst.SETTLEMENT_TYPE_HALAL) {
            drawable = R.drawable.ic_payhalal;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_WEPAY) {
            drawable = R.drawable.ic_wepay;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_ALIPAY) {
            drawable = R.drawable.ic_pay88_alipay;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_BOOST) {
            drawable = R.drawable.ic_pay88_boost;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_MCASH) {
            drawable = R.drawable.ic_pay88_mcash;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_TOUCHNGO) {
            drawable = R.drawable.ic_pay88_touchngo;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_UNIONPAY) {
            drawable = R.drawable.ic_pay88_unionpay;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_MBB) {
            drawable = R.drawable.ic_pay88_maybank;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_CIMB) {
            drawable = R.drawable.ic_pay88_cimb;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_GRABPAY) {
            drawable = R.drawable.ic_pay88_grabpay;
        } else if (mediaId == ParamConst.SETTLEMENT_TYPE_IPAY88_NETS) {
            drawable = R.drawable.ic_pay88_nets;
        }

        return drawable;

    }


}
