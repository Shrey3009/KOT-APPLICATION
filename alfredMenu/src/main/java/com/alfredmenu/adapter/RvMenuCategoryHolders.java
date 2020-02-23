package com.alfredmenu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.Order;
import com.alfredmenu.R;
import com.alfredmenu.activity.MenuOrderPage;
import com.alfredmenu.global.UIHelp;

import java.util.List;

public class RvMenuCategoryHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView titleCategory;
    public ImageView imgCategory;
    public BaseActivity context;
    public List<ItemCategory> itemList;
    public String id;
    public Order order;

    public RvMenuCategoryHolders(Order order,View itemView, BaseActivity context, List<ItemCategory>itemList) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.id = id;
        this.context = context;
        this.itemList = itemList;
        titleCategory = (TextView)itemView.findViewById(R.id.titleCategory);
        imgCategory = (ImageView)itemView.findViewById(R.id.imgCategory);
        this.order = order;
    }

    @Override
    public void onClick(View view) {
        int id = getPosition();
        UIHelp.startMainPage(context,order, ""+itemList.get(id).getId(),""+itemList.get(id).getItemMainCategoryId());
//            UIHelp.startMenuListPage(context, ""+itemList.get(id).getId(), ""+itemList.get(id).getItemMainCategoryId());
//            Toast.makeText(view.getContext(), "ID >>"+itemList.get(5).getId(), Toast.LENGTH_SHORT).show();
        }
    }
