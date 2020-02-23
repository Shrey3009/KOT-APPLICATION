package com.alfredselfhelp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.BH;
import com.alfredselfhelp.R;
import com.alfredselfhelp.javabean.ItemDetailDto;
import com.alfredselfhelp.utils.KpmTextTypeFace;

import java.util.List;


public class NurDetailAdapter extends RvAdapter<OrderDetail> {


    private KpmTextTypeFace textTypeFace = KpmTextTypeFace.getInstance();

    public NurDetailAdapter(Context context, List<OrderDetail> list, RvListener listener) {
        super(context, list, listener);
    }


    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_nur_detail;
    }

    @Override
    public int getItemViewType(int position) {
        return 3;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new DetailHolder(view, viewType, listener);
    }

    public class DetailHolder extends RvHolder<OrderDetail> {
        TextView tvPrice;
        ImageView img, add;
        TextView tvName, num, code, tvNum;

        RelativeLayout re_modifier_num;

        public DetailHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            tvName = (TextView) itemView.findViewById(R.id.tv_nur_name);
            // img = (ImageView) itemView.findViewById(R.id.img_modifier);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_nur_price);
            textTypeFace.setUbuntuBold(tvPrice);
            textTypeFace.setUbuntuRegular(tvName);
            code = (TextView) itemView.findViewById(R.id.tv_nur_code);
            textTypeFace.setUbuntuRegular(code);
            tvNum = (TextView) itemView.findViewById(R.id.tv_nur_num);
            //  add = (ImageView) itemView.findViewById(R.id.img_modifier_add);
            //    re_modifier_num=(RelativeLayout)itemView.findViewById(R.id.re_modifier_num);
            //    num = (TextView) itemView.findViewById(R.id.tv_modifier_num);


        }


        @Override
        public void bindHolder(OrderDetail itemDetail, final int position) {
            int itemViewType = NurDetailAdapter.this.getItemViewType(position);

            tvName.setText(itemDetail.getItemName());
            tvPrice.setText("S$" + BH.formatMoney(itemDetail.getItemPrice()).toString());
            if (TextUtils.isEmpty(itemDetail.getBarCode())) {
                code.setVisibility(View.GONE);
            } else {
                code.setVisibility(View.VISIBLE);
            }
            tvNum.setText(itemDetail.getItemNum() + " X");
//            add.setImageResource(R.drawable.icon_add);
//            add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    notifyItemChanged(position, "aaaa");
//
//                }
//            });
//


        }

        @Override
        public void bindHolderItem(OrderDetail orderDetail, int position) {

        }


    }
}
