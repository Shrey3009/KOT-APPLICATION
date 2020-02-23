package com.alfredposclient.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.ColorSelectedUtils;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.view.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ItemDetailAdapter extends BaseAdapter {
    private Context context;
    private List<ItemDetail> itemDetails;
    private DisplayImageOptions options;
    public static final int ITEM_WIDTH_HEIGHT = 100;
    private TextTypeFace textTypeFace = TextTypeFace.getInstance();
    private LayoutInflater inflater;

    public ItemDetailAdapter(Context context, List<ItemDetail> itemDetails) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (itemDetails == null)
            itemDetails = Collections.emptyList();
        this.itemDetails = itemDetails;
        //显示图片的配置  
        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.default_itemmenu)
                .showImageForEmptyUri(R.drawable.default_itemmenu)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void setItemDetails(List<ItemDetail> itemDetails) {
        this.itemDetails = itemDetails;
        i = 0;
        this.notifyDataSetInvalidated();

    }

    public void filter(int subCategoryId) {
        Iterator<ItemDetail> iter = itemDetails.iterator();

        while (iter.hasNext()) {
            ItemDetail item = iter.next();
            if (item.getItemCategoryId() != subCategoryId)
                iter.remove();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemDetails.size();
    }

    @Override
    public Object getItem(int arg0) {
        return itemDetails.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
//		TextView textView = new TextView(context);
//		ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ITEM_WIDTH_HEIGHT, ITEM_WIDTH_HEIGHT);
//		lp.setMargins(0, 1000, 0, 0);
//		AbsListView.LayoutParams params = new AbsListView.LayoutParams(lp);
////		params.
//		textView.setLayoutParams(params);
////		textView.setPadding(0, 10, 0, 0);
//		textView.setBackgroundResource(R.drawable.box_menu);
//		textView.setGravity(Gravity.CENTER);
//		textView.setTextColor(Color.parseColor("#000000"));
//		textView.setText(itemDetails.get(arg0).getItemName());
//		textTypeFace.setTrajanProRegular(textView);
//		return textView;
//	}
        ViewHolder holder = null;
        ImageViewHolder imageViewHolder = null;
        boolean isSet = Store.getBoolean(context, Store.SET_BACKGROUND, false);
        if (!isSet) {

            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.item_name_layout, null);
//				re = (TextView) itemView
//						.findViewById(R.id.tv_main_category);

                if (Store.getInt(App.instance, Store.TEXT_SIZE, 0) == 1) {
                    AbsListView.LayoutParams layoutParams;
                    layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenSizeUtil.dip2px((Activity) context, 60));
                    arg1.setLayoutParams(layoutParams);
//					layoutParams.setMargins(0, 10, 0, 3);
                }
                holder = new ViewHolder();
                holder.rl_item_num=(RelativeLayout)arg1.findViewById(R.id.rl_item_num);
                holder.tv_item_num=(TextView)arg1.findViewById(R.id.tv_item_num);
                holder.tv_text = (TextView) arg1.findViewById(R.id.tv_item_name);
                if (Store.getInt(App.instance, Store.TEXT_SIZE, 0) == 0) {
                    textTypeFace.setTrajanProBlod(holder.tv_text);
                }
                arg1.setTag(holder);
            } else {
                try
                {
                    holder = (ViewHolder) arg1.getTag();
                }
                catch(Exception e)
                {
                    Log.e("Cast Error", String.valueOf(e));
                }
            }
            if (((MyGridView) arg2).isOnMeasure) {
                //如果是onMeasure调用的就立即返回
                return arg1;
            }
            int color = Store.getInt(context, Store.COLOR_PICKER, 0);

            if (IntegerUtils.isEmptyOrZero(color)) {
                holder.tv_text.setTextColor(context.getResources().getColorStateList(R.color.text_color_selector));
                //	holder.tv_text.setBackgroundResource(R.drawable.box_menu_selector);
            } else {
                int[] rgb = CommonUtil.getRgb(color);
                int color1 = Color.rgb(Math.abs(rgb[0] - 255), Math.abs(rgb[1] - 255), Math.abs(rgb[2] - 255));
                holder.tv_text.setTextColor(ColorSelectedUtils.createColorStateList(color1, color));
                holder.tv_text.setBackgroundDrawable(ColorSelectedUtils.newSelector(color, color1));
            }
            RemainingStock remainingStock=RemainingStockSQL.getRemainingStockByitemId(itemDetails.get(arg0).getItemTemplateId());
            if(remainingStock!=null&&remainingStock.getDisplayQty()>=remainingStock.getQty()){
                int qty= remainingStock.getQty().intValue();
                holder.rl_item_num.setVisibility(View.VISIBLE);
                if(qty<=0){
                    holder.tv_item_num.setText(0+"");
                }else {
                    holder.tv_item_num.setText(remainingStock.getQty()+"");
                }

            }else {
                holder.rl_item_num.setVisibility(View.GONE);
            }

            holder.tv_text.setText(itemDetails.get(arg0).getItemName());
        } else {
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.item_name_image_layout, null);
                imageViewHolder = new ImageViewHolder();
                imageViewHolder.item_name_img = (ImageView) arg1.findViewById(R.id.item_name_img);
                imageViewHolder.item_name_tv = (TextView) arg1.findViewById(R.id.item_name_tv);
                imageViewHolder.tv_item_img_num=(TextView)arg1.findViewById(R.id.tv_item_img_num);
                imageViewHolder.rl_item_img_num=(RelativeLayout)arg1.findViewById(R.id.rl_item_img_num);
                textTypeFace.setTrajanProBlod(imageViewHolder.item_name_tv);
                arg1.setTag(imageViewHolder);
            } else {
                imageViewHolder = (ImageViewHolder) arg1.getTag();
            }
            imageViewHolder.item_name_tv.setText(itemDetails.get(arg0).getItemName());
            String url = itemDetails.get(arg0).getImgUrl();
            ImageLoader.getInstance().displayImage(url, imageViewHolder.item_name_img, options);
            RemainingStock remainingStock=RemainingStockSQL.getRemainingStockByitemId(itemDetails.get(arg0).getItemTemplateId());
            if(remainingStock!=null&&remainingStock.getDisplayQty()>=remainingStock.getQty()){
                int qty= remainingStock.getQty().intValue();
                imageViewHolder.rl_item_img_num.setVisibility(View.VISIBLE);
                if(qty<=0){
                    imageViewHolder.tv_item_img_num.setText(0+"");
                }else {
                    imageViewHolder.tv_item_img_num.setText(remainingStock.getQty()+"");
                }

            }else {
                imageViewHolder.rl_item_img_num.setVisibility(View.GONE);
            }
        }
        System.out.println("=====111111" + i++);

        return arg1;
    }


    int i = 0;

    class ViewHolder {
        public TextView tv_text,tv_item_num;

        public RelativeLayout re;
        public RelativeLayout rl_item_num;
    }

    class ImageViewHolder {
        public TextView item_name_tv,tv_item_img_num;
        public ImageView item_name_img;
        public RelativeLayout rl_item_img_num;

    }

}
