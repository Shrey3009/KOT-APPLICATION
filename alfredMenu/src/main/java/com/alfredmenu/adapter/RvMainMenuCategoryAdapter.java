package com.alfredmenu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.Order;
import com.alfredmenu.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RvMainMenuCategoryAdapter extends RecyclerView.Adapter<RvMainMenuCategoryHolders> {

    private List<ItemMainCategory> itemList;
    private Context context;
    private BaseActivity cont;
    private String id;
    private Order order;

    public RvMainMenuCategoryAdapter(Order order, Context context, List<ItemMainCategory> itemList, BaseActivity cont, RvListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.cont = cont;
        this.order = order;
    }

    public void setOrder(Order order){
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public RvMainMenuCategoryHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_menu_category_list, null);
        RvMainMenuCategoryHolders rcv = new RvMainMenuCategoryHolders(getOrder(), layoutView,cont,itemList);
        return rcv;
    }


    @Override
    public void onBindViewHolder(RvMainMenuCategoryHolders holder, int position) {
        holder.titleCategory.setText(itemList.get(position).getMainCategoryName());
//        holder.imgCategory.setImageResource(itemList.get(position).getPhoto());
        Drawable drawable = context.getResources().getDrawable(R.drawable.login_header);
//        Drawable drawable = LoadImageFromWebOperations(itemList.get(position).getImgUrl());
        holder.imgCategory.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    private Drawable LoadImageFromWebOperations(String url)
    {
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
    }
}
