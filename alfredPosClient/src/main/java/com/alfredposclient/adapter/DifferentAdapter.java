package com.alfredposclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

import java.util.ArrayList;
import java.util.List;

public class DifferentAdapter  extends RecyclerView.Adapter<DifferentAdapter.VH>{
    static TextTypeFace  textTypeFace;
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView name,price,tv_qty,subtotal,discount,total,modifier,specialInstract,tv_instruction;

        public VH(View v) {
            super(v);

            name = (TextView)v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
            tv_qty = (TextView) v.findViewById(R.id.tv_qty);
            subtotal = (TextView) v.findViewById(R.id.subtotal);
            discount = (TextView) v.findViewById(R.id.discount);
            total = (TextView) v.findViewById(R.id.total);
            modifier = (TextView) v
                    .findViewById(R.id.tv_modifier);
            specialInstract = (TextView) v
                    .findViewById(R.id.tv_special_instract);
            tv_instruction = (TextView) v
                    .findViewById(R.id.tv_instruction);
            
        }
    }

    private List<OrderDetail>  mDatas=new ArrayList<OrderDetail>();
    public DifferentAdapter(List<OrderDetail>  data) {
        this.mDatas = data;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(VH holder, int position) {
       OrderDetail  orderDetail  = mDatas.get(position);
        holder.name.setText(mDatas.get(position).getItemName());
        String modifiers = getItemModifiers(orderDetail);
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(holder.name);
        textTypeFace.setTrajanProRegular(holder.price);
        textTypeFace.setTrajanProRegular(holder.tv_qty);
        textTypeFace.setTrajanProRegular(holder.subtotal);
        textTypeFace.setTrajanProRegular(holder.discount);
        textTypeFace.setTrajanProRegular(holder.total);
        textTypeFace.setTrajanProRegular(holder.modifier);
        textTypeFace.setTrajanProRegular(holder.tv_instruction);
        holder.specialInstract
                .setText(orderDetail.getSpecialInstractions());
        holder.name.setText(orderDetail.getItemName());
        holder.price.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(orderDetail.getItemPrice()));
        holder.tv_qty.setText(orderDetail.getItemNum() + "");
     //   ,price,tv_qty,subtotal,discount,total,modifier,specialInstract,tv_instruction
        holder.subtotal.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(orderDetail.getRealPrice()));
        if(orderDetail.getOrderDetailType().intValue() == ParamConst.ORDERDETAIL_TYPE_FREE){
            holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
            holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(ParamConst.DOUBLE_ZERO).toString());
        }else{
            holder.discount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(orderDetail.getDiscountPrice()).toString());
            holder.total.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol()
                    + BH.sub(BH.getBD(orderDetail.getRealPrice()),
                    BH.getBD(orderDetail.getDiscountPrice()), true));
        }
        holder.modifier.setText(modifiers);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });
    }

    @Override
    public int getItemCount() {

        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_page_order, parent, false);
        return new VH(v);
    }


    public String getItemModifiers(OrderDetail orderDetail) {
        String result = "";
        ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                .getAllOrderModifierByOrderDetailAndNormal(orderDetail);
        for (OrderModifier orderModifier : orderModifiers) {
            Modifier modifier = ModifierSQL.getModifierById(orderModifier
                    .getModifierId());
            if (modifier != null) {
                String modifierName = modifier.getModifierName();
                if (modifier.getQty() > 1){
                    result += modifierName + "x" + modifier.getQty() + ";";
                } else {
                    result += modifierName + ";";
                }

            }
        }
        return result;
    }
}
