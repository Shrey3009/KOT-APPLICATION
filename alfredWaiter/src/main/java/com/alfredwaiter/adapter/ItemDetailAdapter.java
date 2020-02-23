package com.alfredwaiter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;

import com.alfredwaiter.popupwindow.SetItemCountWindow;
import com.alfredwaiter.view.CountView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class ItemDetailAdapter extends RvCateAdapter<ItemDetail> {
    private DisplayImageOptions options;
    private TextTypeFace textTypeFace = TextTypeFace.getInstance();
    private CountView.OnCountChange onCountChange;
    private SetItemCountWindow setItemCountWindow;
    private List<OrderDetail> orderDetails;
    private int currentGroupId;
    private Order currentOrder;

    public void setParams(Order currentOrder, List<OrderDetail> orderDetails, int currentGroupId) {
        this.currentOrder = currentOrder;
        this.orderDetails = orderDetails;
        this.currentGroupId = currentGroupId;
    }

    public ItemDetailAdapter(Context context, List<ItemDetail> list,SetItemCountWindow setItemCountWindow, RvListener listener,CountView.OnCountChange onCountChange) {

        super(context, list, listener);

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.default_itemmenu)
                .showImageForEmptyUri(R.drawable.default_itemmenu)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        this.setItemCountWindow = setItemCountWindow;
        this.onCountChange = onCountChange;
    }


    @Override
    protected int getLayoutId(int viewType) {
        if (viewType == 1) {
            return R.layout.kot_mian_item_listview;
        } else if (viewType == 2) {
            return R.layout.kot_notification_listview;
        } else {
            return R.layout.item_item_detail;

        }
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
        // return TextUtils.isEmpty(list.get(position).getItemName())==true ? 0 : 1;
    }

    @Override
    protected RvCateHolder getHolder(View view, int viewType) {
        return new ItemHolder(view, viewType, listener);
    }

    public class ItemHolder extends RvCateHolder<ItemDetail> {
        TextView tvCity;
        ImageView avatar;
        TextView tvTitle;
        TextView title;
        TextView tv_name,tv_remain_num,tv_out_of;

        TextView tv_price;
        ImageView img_icon;
        CountView count_view;

        LinearLayout add, minus;
        TextView tv_count;
        RelativeLayout rl_remain_num;

        public ItemHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            Log.d("  布局--->", "");
            switch (type) {
                case 1:
                    title = (TextView) itemView.findViewById(R.id.tv_main_title);
                    textTypeFace.setTrajanProBlod(title);
                    break;
                case 2:
                    tvTitle = (TextView) itemView.findViewById(R.id.title);
                    textTypeFace.setTrajanProBlod(tvTitle);
                    break;
                case 3:
                    tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                    tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                    img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
                    add = (LinearLayout) itemView.findViewById(R.id.ll_add);
                    tv_remain_num=(TextView)itemView.findViewById(R.id.tv_remain_num);
                    rl_remain_num=(RelativeLayout) itemView.findViewById(R.id.rl_remain_num);
                    tv_out_of=(TextView)itemView.findViewById(R.id.tv_out_of);
                    minus = (LinearLayout) itemView.findViewById(R.id.ll_minus);
                    tv_count = (TextView) itemView.findViewById(R.id.tv_count);
                    count_view = (CountView) itemView
                            .findViewById(R.id.count_view);
                    break;
            }

        }

        @Override
        public void bindHolder(final ItemDetail itemDetail, final int position) {
            int itemViewType = ItemDetailAdapter.this.getItemViewType(position);
            switch (itemViewType) {
                case 1:
                    title.setText(itemDetail.getItemCategoryName());
                    break;
                case 2:
                    tvTitle.setText(itemDetail.getItemName() + "");
                    //  title.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 3:
                    tv_name.setText(itemDetail.getItemName());
                    textTypeFace.setTrajanProRegular(tv_name);
                    tv_price.setText(App.instance.getCurrencySymbol() + BH.formatMoney(itemDetail.getPrice()).toString());
                    textTypeFace.setTrajanProRegular(tv_price);
                    RemainingStock remainingStock=RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                    if(remainingStock!=null){
                        rl_remain_num.setVisibility(View.VISIBLE);
                        int existedOrderDetailNum = OrderDetailSQL.getOrderAddDetailCountByOrderIdAndItemDetailId(currentOrder.getId(), itemDetail.getId());
//
                        int reNum=remainingStock.getQty()-existedOrderDetailNum-getItemNum(itemDetail);
                        if(reNum>0){
                            tv_out_of.setVisibility(View.GONE);
                            tv_remain_num.setText(reNum+"");
                            count_view.setVisibility(View.VISIBLE);
                        }else {
                            tv_remain_num.setText(0+"");
                            tv_out_of.setVisibility(View.VISIBLE);
                          count_view.setVisibility(View.INVISIBLE
                            );
                            // tv_remin_num.setText(0+"");
                        }
                    }else {
                        tv_out_of.setVisibility(View.GONE);
                        rl_remain_num.setVisibility(View.GONE);
                        count_view.setVisibility(View.VISIBLE);
                    }

                    String url = itemDetail.getImgUrl();
               //     System.out.println("数量--->");
                    Log.d( "  数量--->", ""+getItemNum(itemDetail));

                    ImageLoader.getInstance().displayImage(url, img_icon, options);
//
//                    tv_count.setText("0");
//                    add.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });

                    count_view.setIsCanClick(getOrderDetailStatus(itemDetail));
                    count_view.setInitCount(getItemNum(itemDetail));
                    count_view.setTag(itemDetail);
                    count_view.setParam(itemDetail,setItemCountWindow);
                    count_view.setOnCountChange(onCountChange);
                    break;
            }

        }
    }




    private int getItemNum(ItemDetail itemDetail) {
        int itemNum = 0;
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getItemId().intValue() == itemDetail.getId()
                    .intValue()
                 ) {
                itemNum += orderDetail.getItemNum();
            }
        }
        return itemNum;
    }

    private boolean getOrderDetailStatus(ItemDetail itemDetail) {
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getItemId().intValue() == itemDetail.getId()
                    .intValue()
                    ) {
                if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }
}
