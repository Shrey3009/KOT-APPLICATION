package com.alfredselfhelp.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredbase.javabean.ItemCategory;
import com.alfredselfhelp.R;
import com.alfredselfhelp.utils.KpmTextTypeFace;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ClassAdapter extends RvAdapter<ItemCategory> {

    private int checkedPosition;

    private KpmTextTypeFace textTypeFace = KpmTextTypeFace.getInstance();

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public ClassAdapter(Context context, List<ItemCategory> list, RvListener listener) {
        super(context, list, listener);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_class_list;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new ClassHolder(view, viewType, listener);
    }

    private class ClassHolder extends RvHolder<ItemCategory> {

        private TextView tvName;
        private View mView;

        private ImageView img_class;

        ClassHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            this.mView = itemView;

            tvName = (TextView) itemView.findViewById(R.id.tv_class_name);
            img_class = (ImageView) itemView.findViewById(R.id.img_class);

        }


        @Override
        public void bindHolder(ItemCategory itemCategory, int position) {
            tvName.setText(itemCategory.getItemCategoryName());
            textTypeFace.setUbuntuMedium(tvName);

            Glide.with(mContext)
                    .load(itemCategory.getImgUrl())
                    .placeholder(R.drawable.img_bg)
                    .error(R.drawable.img_bg)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(img_class);
            if (position == checkedPosition) {
                mView.setBackgroundResource(R.color.gray4);
                tvName.setTextColor(mContext.getResources().getColor(R.color.green1));

            } else {
                mView.setBackgroundResource(android.R.color.white);
                tvName.setTextColor(mContext.getResources().getColor(R.color.black2));

            }
//            if (position == checkedPosition) {
//                mView.setBackgroundColor(Color.parseColor("#f3f3f3"));
//                tvName.setTextColor(Color.parseColor("#0068cf"));
//            } else {
//                mView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                tvName.setTextColor(Color.parseColor("#1e1d1d"));
//            }
        }

        @Override
        public void bindHolderItem(ItemCategory itemCategory, int position) {

        }

    }
}
