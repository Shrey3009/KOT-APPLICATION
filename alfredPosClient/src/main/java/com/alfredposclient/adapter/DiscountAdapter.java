package com.alfredposclient.adapter;

import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zun on 2017/2/15 0015.
 */

public class DiscountAdapter extends BaseAdapter {

    private BaseActivity activity;
    private List<ItemMainCategory> list = new ArrayList<ItemMainCategory>();
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    private Order order;

    public DiscountAdapter(BaseActivity activity, Order order) {
        sparseBooleanArray.clear();
        this.order = order;
        List<Integer> ids = new ArrayList<Integer>();
        List<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(order.getId());
        if (orderDetails != null && orderDetails.size() != 0) {
            list.clear();
            for (int i = 0; i < orderDetails.size(); i++) {
                OrderDetail detail = orderDetails.get(i);

                ItemDetail itemDetail = ItemDetailSQL.getItemDetailById(detail.getItemId(), detail.getItemName());
                if(itemDetail != null)
                {
                    if (!ids.contains(itemDetail.getItemMainCategoryId())){
                        ids.add(itemDetail.getItemMainCategoryId());
                        ItemMainCategory itemMainCategory = ItemMainCategorySQL.getItemMainCategoryById(itemDetail.getItemMainCategoryId());
                        list.add(itemMainCategory);
                        if(detail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB
                                || detail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE){
                            sparseBooleanArray.put(itemMainCategory.getId(), true);
                        }
                    }
                }
            }
        }
        this.activity = activity;
    }

    public String getSelectedItem(int discountType)
    {
        String discountCategoryId = "";
        if (sparseBooleanArray.size() > 0)
        {
            Boolean completedOrder = false;
            for(OrderDetail orderDetail : OrderDetailSQL.getOrderDetails(order.getId()))
            {
                for(OrderSplit finishedOrder : OrderSplitSQL.getFinishedOrderSplits(order.getId()))
                {
                    if(orderDetail.getOrderSplitId().equals(finishedOrder.getId()))
                    {
                        if(finishedOrder.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED)
                        {
                            completedOrder = true;
                        }
                    }
                }
            }
            if(!completedOrder)
            {
                OrderDetailSQL.updateDiscountTypeBeforeByMainCategoryId(order.getId());
                for (int i = 0; i < sparseBooleanArray.size(); i++)
                {
                    int key = sparseBooleanArray.keyAt(i);
                    if (sparseBooleanArray.get(key))
                    {
                        OrderDetailSQL.updateDiscountTypeByMainCategoryId(discountType, key, order.getId());
                        if (!TextUtils.isEmpty(discountCategoryId))
                        {
                            discountCategoryId = discountCategoryId + "," + key;
                        }
                        else
                        {
                            discountCategoryId = key + "";
                        }
                    }
                }
            }
            else
            {

            }
        }

        return discountCategoryId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.discount_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.discount_name = (TextView) convertView.findViewById(R.id.discount_name);
            viewHolder.discount_select = (CheckBox) convertView.findViewById(R.id.discount_select);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            if (list.size() != 0) {
                final ItemMainCategory itemMainCategory = list.get(position);
                TextTypeFace textTypeFace = TextTypeFace.getInstance();
                textTypeFace.setTrajanProRegular(viewHolder.discount_name);
                viewHolder.discount_name.setText(itemMainCategory.getMainCategoryName());
                viewHolder.discount_select.setTag(itemMainCategory.getId().intValue());
                viewHolder.discount_select.setChecked(sparseBooleanArray.get(itemMainCategory.getId().intValue()));
                viewHolder.discount_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox) v;
                        if (checkBox.isChecked()) {
                            sparseBooleanArray.put((Integer) v.getTag(), checkBox.isChecked());
                        } else {
                            sparseBooleanArray.delete((Integer) v.getTag());
                        }

                    }
                });
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView discount_name;
        CheckBox discount_select;
    }
}
