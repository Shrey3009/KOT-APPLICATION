package com.alfredposclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alfredposclient.R;
import com.alfredposclient.adapter.GoodsAdapter;
import com.moonearly.model.OrderModel;

/**
 * Created by Zun on 2016/12/16.
 */

public class HomePageData extends LinearLayout {

    private TextView home_page_resturantname_tv, home_page_index_tv, home_page_name_tv, home_page_price_tv, home_page_qty_tv
            , home_page_total_tv, home_page_subtotal_tv, home_page_discount_tv, home_page_taxes_tv, home_page_grandtotal_tv;
    private TextView subtotal_tv, discount_tv, taxes_tv, grandtotal_tv;
    private ListView home_page_listview;
    private RelativeLayout home_page_rl;
    private GoodsAdapter goodsAdapter;
    private OrderModel orderModel;
    private int percent = 50;

    public HomePageData(Context context, int percent) {
        super(context);
        this.percent = percent;
        initView(context);
    }

    public HomePageData(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void refreshUI(Context context, OrderModel orderModel){
        this.orderModel = orderModel;
        initView(context);
    }

    public void dataSize(int i){
        this.percent = i;
        setSize();
    }

    private void initView(Context context){
        View view = inflate(context, R.layout.home_page_data, this);
        home_page_resturantname_tv = (TextView) view.findViewById(R.id.home_page_resturantname_tv);
        home_page_rl = (RelativeLayout) view.findViewById(R.id.home_page_rl);
        home_page_index_tv = (TextView) view.findViewById(R.id.home_page_index_tv);
        home_page_name_tv = (TextView) view.findViewById(R.id.home_page_name_tv);
        home_page_price_tv = (TextView) view.findViewById(R.id.home_page_price_tv);
        home_page_qty_tv = (TextView) view.findViewById(R.id.home_page_qty_tv);
        home_page_total_tv = (TextView) view.findViewById(R.id.home_page_total_tv);
        home_page_subtotal_tv = (TextView) view.findViewById(R.id.home_page_subtotal_tv);
        home_page_discount_tv = (TextView) view.findViewById(R.id.home_page_discount_tv);
        home_page_taxes_tv = (TextView) view.findViewById(R.id.home_page_taxes_tv);
        home_page_grandtotal_tv = (TextView) view.findViewById(R.id.home_page_grandtotal_tv);
        home_page_listview = (ListView) view.findViewById(R.id.home_page_listview);

        subtotal_tv = (TextView) view.findViewById(R.id.subtotal_tv);
        discount_tv = (TextView) view.findViewById(R.id.discount_tv);
        taxes_tv = (TextView) view.findViewById(R.id.taxes_tv);
        grandtotal_tv = (TextView) view.findViewById(R.id.grandtotal_tv);

        if(orderModel != null) {
            goodsAdapter = new GoodsAdapter(context, orderModel.getGoodsModelList());
            home_page_listview.setAdapter(goodsAdapter);

            home_page_resturantname_tv.setText(orderModel.getRestaurantName());
            home_page_index_tv.setText(orderModel.getGoodsModel().getIndex());
            home_page_name_tv.setText(orderModel.getGoodsModel().getName());
            home_page_price_tv.setText(orderModel.getGoodsModel().getPrice());
            home_page_qty_tv.setText(orderModel.getGoodsModel().getQty());
            home_page_total_tv.setText(orderModel.getGoodsModel().getTotal());
            String str1 = orderModel.getSubTotal();
            String[] s1 = str1.split(":");
            String str2 = orderModel.getDiscount();
            String[] s2 = str2.split(":");
            String str3 = orderModel.getTaxes();
            String[] s3 = str3.split(":");
            String str4 = orderModel.getGrandTotal();
            String[] s4 = str4.split(":");

            home_page_subtotal_tv.setText(s1[1]);
            home_page_discount_tv.setText(s2[1]);
            home_page_taxes_tv.setText(s3[1]);
            home_page_grandtotal_tv.setText(s4[1]);

            subtotal_tv.setText(s1[0] + ":");
            discount_tv.setText(s2[0] + ":");
            taxes_tv.setText(s3[0] + ":");
            grandtotal_tv.setText(s4[0] + ":");

        }
        setSize();
    }

    private void setSize(){
        float wel = (float) (7 + 30 * (percent/100.0));
        float text = (float) (6 + 24 * (percent/100.0));
        home_page_resturantname_tv.setTextSize(wel);
        home_page_index_tv.setTextSize(text);
        home_page_name_tv.setTextSize(text);
        home_page_price_tv.setTextSize(text);
        home_page_qty_tv.setTextSize(text);
        home_page_total_tv.setTextSize(text);
        home_page_subtotal_tv.setTextSize(text);
        home_page_discount_tv.setTextSize(text);
        home_page_taxes_tv.setTextSize(text);
        home_page_grandtotal_tv.setTextSize(text);

        subtotal_tv.setTextSize(text);
        discount_tv.setTextSize(text);
        taxes_tv.setTextSize(text);
        grandtotal_tv.setTextSize(text);

        if (goodsAdapter != null) {
            goodsAdapter.ListSize(text);
        }
    }
}
