package com.alfredmenu.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.LogUtil;
import com.alfredmenu.R;
import com.alfredmenu.global.App;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.javabean.ModifierCPVariance;
import com.alfredmenu.javabean.ModifierVariance;
import com.alfredmenu.popupwindow.SetItemCountWindow;
import com.alfredmenu.popupwindow.SubCategoryWindow;
import com.alfredmenu.popupwindow.WaiterModifierCPWindow;
import com.alfredmenu.utils.WaiterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuOrderPage extends Dialog implements View.OnClickListener {

    public int id, itemDetailPosition;
    private TextView titleCategory;
    private ImageView imgCategory;
    private EditText descCategory;
    private TextView priceCategory;
    private Activity activity;
    private Button btnAdd,btnCancel;
    private SetItemCountWindow setItemCountWindow;
    private String itemId,itemMainCategoryId;
    private SubCategoryWindow subCategoryWindow;
//    private BaseActivity baseActivity

    public MenuOrderPage(Activity activity, int id, int itemDetailPosition){
        super(activity);
        this.activity = activity;
        this.id = id;
//        this.baseActivity = baseActivity;
        this.itemDetailPosition = itemDetailPosition;
//        setItemCountWindow = new SetItemCountWindowDialog(activity,handler, findViewById(R.id.content));

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
//                case VIEW_EVENT_MODIFY_ITEM_COUNT: {
//                    LogUtil.d("444444444444--->", "4444444444444");
//
//                    Map<String, Object> map = (Map<String, Object>) msg.obj;
//                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
//
//                    int num = (int) map.get("count");
//                    RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
//                    if (remainingStock != null) {
//
//                        int existedOrderDetailNum = OrderDetailSQL.getOrderAddDetailCountByOrderIdAndItemDetailId(currentOrder.getId(), itemDetail.getId());
//                        int reNum = remainingStock.getQty() - existedOrderDetailNum - num;
//                        if (reNum >= 0) {
//                            updateOrderDetail(itemDetail,
//                                    num);
//                            refreshTotal();
//                            refreshList();
//                            boolean isadd = (boolean) map.get("isAdd");
//                            if (isadd) {
//                                isShow((ItemDetail) map.get("itemDetail"));
//                            }
//                        } else {
//                            UIHelp.showToast(MainPage.this,MainPage.this.getString(R.string.out_of_stock));
//                            //  return;
//                        }
//
//                    } else {
//                        updateOrderDetail(itemDetail,
//                                num);
//                        refreshTotal();
//                        refreshList();
//                        boolean isadd = (boolean) map.get("isAdd");
//                        if (isadd) {
//                            isShow((ItemDetail) map.get("itemDetail"));
//                        }
//                    }
//
//
//                    break;
//                }
//                case VIEW_EVENT_MODIFIER_COUNT: {
//                    Map<String, Object> map = (Map<String, Object>) msg.obj;
//                    int count = (Integer) map.get("count");
//                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
//                    ModifierVariance modifierVariance = (ModifierVariance) map.get("modifierVariance");
////				modifierWindow.setList(modifierVariance);
//
//                    break;
//                }

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_popupwindow_page);

        titleCategory = (TextView) findViewById(R.id.titleCategory);
        priceCategory = (TextView) findViewById(R.id.priceCategory);
        descCategory = (EditText) findViewById(R.id.txtNotes);
        btnAdd = (Button) findViewById(R.id.btn_Add);
        btnCancel = (Button) findViewById(R.id.btn_Cancel);
        getIntentData();
         getItemDetail();
//        titleCategory.setText(rowListItem.get(5).getItemName());
    }

    public int getId() {
        return id;
    }

    @Override
    public void onClick(View view) {
//            Log.d()view.getTag();
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
            detail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
            // detail.setId(list.get(j).getId());
            detail.setTag(String.valueOf(i));
            detail.setViewType(1);
            itemDetailandCate.add(detail);

            for (int j = 0; j < itemCategorylist.size(); j++) {

                int idcategory = itemMainCategorielist.get(i).getId();
                ItemCategory itemCategory = itemCategorylist.get(j);
                int currentcategory = itemCategory.getId();
                int cid= itemCategory.getItemMainCategoryId();
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

                    itemCateDetail.setItemCategoryId(Integer.parseInt(itemId));
                    itemCateDetail.setTag(String.valueOf(i));
                    itemCateDetail.setViewType(2);
                    itemDetailandCate.add(itemCateDetail);
                    itemDetaillist.clear();
                    itemDetaillist = CoreData.getInstance().getItemDetails(itemCategory);
                    for (int d = 0; d < itemDetaillist.size(); d++) {

                        if(Integer.parseInt(itemId) == itemDetaillist.get(d).getItemCategoryId()) {
                            Log.d("Makanan Detail List", itemDetaillist.get(d).getItemName());
                            Log.d("ID Makanan Detail List", "" + itemDetaillist.get(d).getId());
//
                            titleCategory.setText(itemDetaillist.get(itemDetailPosition - 2).getItemName());
//                            descCategory.setText(itemDetaillist.get(itemDetailPosition - 2).getItemDesc());
                            priceCategory.setText(itemDetaillist.get(itemDetailPosition - 2).getPrice());

                            itemDetaillist.get(d).setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                            itemDetaillist.get(d).setTag(String.valueOf(i));
                            itemDetaillist.get(d).setViewType(3);
                            itemDetailandCate.add(itemDetaillist.get(d));
                        }

                    }

                }
                }

            }

        }

        return itemDetailandCate;
    }

    private void getIntentData() {
        Bundle extras = activity.getIntent().getExtras();
        itemId = extras.getString("itemId");
        itemMainCategoryId = extras.getString("itemMainCategoryId");
    }

}


