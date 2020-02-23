package com.alfredmenu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.utils.BH;
import com.alfredmenu.R;
import com.alfredmenu.activity.OrderDetailsTotal;
import com.alfredmenu.global.App;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RvMenuListDetailAdapter extends RecyclerView.Adapter<RvMenuListDetailHolders> {

    private List<ItemDetail> itemList;
    private Context context;
    private BaseActivity cont;
    private int id;
    private String itemMainCategoryId;


    public RvMenuListDetailAdapter(Context context, List<ItemDetail> itemList,int id,String iteMainCategoryId, BaseActivity cont, RvListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.cont = cont;
        this.id = id;
        this.itemMainCategoryId = iteMainCategoryId;
    }

    @Override
    public RvMenuListDetailHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_menu_listdetail, null);
        RvMenuListDetailHolders rcv = new RvMenuListDetailHolders(layoutView, cont, id, itemMainCategoryId);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RvMenuListDetailHolders holder, int position) {
        if(!itemList.get(position).getItemName().equals("")) {
            holder.titleCategory.setText(itemList.get(position).getItemName());
//            holder.no.setText(itemList.get(position).getItemDesc());
            holder.priceCategory.setText( App.instance.getCurrencySymbol() + BH.formatMoney(itemList.get(position).getPrice()));
//        holder.imgCategory.setImageResource(itemList.get(position).getPhoto());
//            Drawable drawable = LoadImageFromWebOperations(itemList.get(position).getImgUrl());
//        Drawable drawable = context.getResources().getDrawable(R.drawable.canada);
//            holder.imgCategory.setImageDrawable(drawable);
        }
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
