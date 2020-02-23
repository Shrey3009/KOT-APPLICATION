package com.alfredmenu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemCategory;

import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.Order;

import com.alfredmenu.adapter.RvListener;
import com.alfredmenu.adapter.RvMenuCategoryAdapter;
import com.alfredmenu.R;
import com.alfredmenu.adapter.RvMenuListDetailAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuListPage extends BaseActivity {
    List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();
    private String id,itemMainCategoryId;
    private int idMenuCategory,cid, idcategory,currentcategory;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_menu_listdetail_page);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager mManager = new GridLayoutManager(context, 2);
        mManager .setOrientation(LinearLayoutManager.HORIZONTAL);
        rView.setLayoutManager(mManager);
        rView.scrollToPosition(1);
        getIntentData();
        idMenuCategory = Integer.parseInt(id);
        idcategory = Integer.parseInt(itemMainCategoryId);
        itemDetails.addAll(getItemDetail());
        RvMenuListDetailAdapter rcAdapter = new RvMenuListDetailAdapter(context, itemDetails,cid,itemMainCategoryId,this, new RvListener() {
            @Override
            public void onItemClick(View id, int position) {
                Toast.makeText(context, "position"+position, Toast.LENGTH_SHORT).show();
            }
        });
        rView.setAdapter(rcAdapter);

    }


    private List<ItemDetail> getItemDetail(
    ) {
        List<ItemDetail> itemDetaillist = new ArrayList<ItemDetail>();
        List<ItemDetail> itemDetailandCate = new ArrayList<ItemDetail>();

        List<ItemCategory> itemCategorylist = CoreData.getInstance().getItemCategoriesorDetail();
        List<ItemMainCategory> itemMainCategorielist = CoreData.getInstance().getItemMainCategories();

        for (int i = 0; i < itemMainCategorielist.size(); i++) {

            if(Integer.parseInt(itemMainCategoryId) == itemMainCategorielist.get(i).getId()){
                ItemDetail detail = new ItemDetail();
//                detail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                // detail.setId(list.get(j).getId());
                detail.setTag(String.valueOf(i));
                detail.setViewType(1);
//                itemDetailandCate.add(detail);

                for (int j = 0; j < itemCategorylist.size(); j++) {

                    idcategory = itemMainCategorielist.get(i).getId();
                    ItemCategory itemCategory = itemCategorylist.get(j);
                    currentcategory = itemCategory.getId();
                    cid = itemCategory.getItemMainCategoryId();
                    if (idMenuCategory == currentcategory) {

                        ItemDetail itemCateDetail = new ItemDetail();
                       // itemCateDetail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                        Log.d("main category name",""+itemMainCategorielist.get(i).getMainCategoryName());
                        // detail.setId(list.get(j).getId());
                      //  itemCateDetail.setItemName(itemCategory.getItemCategoryName());
                        Log.d("Makanan SUb", itemCategory.getItemCategoryName());

                        if(itemCategory.getImgUrl() != null ){
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

        return itemDetailandCate;
    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        id = extras.getString("itemId");
        itemMainCategoryId = extras.getString("itemMainCategoryId");
    }

}
