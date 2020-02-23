package com.alfredmenu.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredmenu.R;
import com.alfredmenu.activity.MenuOrderPage;
import com.alfredmenu.global.UIHelp;

import java.util.ArrayList;
import java.util.List;

public class RvMenuListDetailHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView titleCategory, priceCategory;
    private EditText notesCategory;
    public ImageView imgCategory;
    public BaseActivity context;
    public int id;
    public String itemMainCategoryId;
    private MenuOrderPage menuOrderPage;
    List<ItemDetail> itemDetaillist = new ArrayList<ItemDetail>();
    private CardView cardView;

    public RvMenuListDetailHolders(View itemView, BaseActivity context, int id , String itemMainCategoryId) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.id = id;
        this.context = context;
        this.itemMainCategoryId = itemMainCategoryId;
        titleCategory = (TextView)itemView.findViewById(R.id.titleCategory);
        priceCategory = (TextView) itemView.findViewById(R.id.priceCategory);
        notesCategory = (EditText) itemView.findViewById(R.id.txtNotes);
    }

    @Override
    public void onClick(View view) {

//        MenuOrderPage menuOrderPage = new MenuOrderPage(context,id,getPosition());
//        menuOrderPage.show();
        List<ItemDetail> itemDetaillist = new ArrayList<ItemDetail>();
        List<ItemDetail> itemDetailandCate = new ArrayList<ItemDetail>();

        List<ItemCategory> itemCategorylist = CoreData.getInstance().getItemCategoriesorDetail();
        List<ItemMainCategory> itemMainCategorielist = CoreData.getInstance().getItemMainCategories();

        for (int i = 0; i < itemMainCategorielist.size(); i++) {

            ItemDetail detail = new ItemDetail();
            detail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
            // detail.setId(list.get(j).getId());
            detail.setTag(String.valueOf(i));
            detail.setViewType(1);
            itemDetailandCate.add(detail);

            for (int j = 0; j < itemCategorylist.size(); j++) {

                if(Integer.parseInt(itemMainCategoryId) == itemMainCategorielist.get(i).getId()){
                    int idcategory = itemMainCategorielist.get(i).getId();
                ItemCategory itemCategory = itemCategorylist.get(j);
                int currentcategory = itemCategory.getId();
                int cid = itemCategory.getItemMainCategoryId();
                if (id == cid) {

                    ItemDetail itemCateDetail = new ItemDetail();
                    itemCateDetail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                    // detail.setId(list.get(j).getId());
                    itemCateDetail.setItemName(itemCategory.getItemCategoryName());
                    Log.d("Makanan SUb", itemCategory.getItemCategoryName());

                    if (itemCategory.getImgUrl() != null) {
                        itemCateDetail.setImgUrl(itemCategory.getImgUrl());
                        Log.d("image url", itemCategory.getImgUrl());
                    }

                    itemCateDetail.setItemCategoryId(itemCategory.getId());
                    itemCateDetail.setTag(String.valueOf(i));
                    itemCateDetail.setViewType(2);
                    itemDetailandCate.add(itemCateDetail);
                    itemDetaillist.clear();
                    itemDetaillist = CoreData.getInstance().getItemDetails(itemCategory);
                    for (int d = 0; d < itemDetaillist.size(); d++) {
                        Log.d("posistion",""+getPosition());
                         menuOrderPage = new MenuOrderPage(context,id, getPosition());
                        Log.d("Makanan Detail List", itemDetaillist.get(d).getItemName());
                        itemDetaillist.get(d).setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                        itemDetaillist.get(d).setTag(String.valueOf(i));
                        itemDetaillist.get(d).setViewType(3);
                        itemDetailandCate.add(itemDetaillist.get(d));

                    }

                }
                }

            }

        }

        menuOrderPage.show();
    }
}