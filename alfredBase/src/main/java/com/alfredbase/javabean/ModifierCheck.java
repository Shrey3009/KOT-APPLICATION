package com.alfredbase.javabean;

import java.io.Serializable;

public class ModifierCheck implements Serializable{

    private int id;
    private int orderDetailId;
    private int orderId;
    private int modifierCategoryId;
    private String itemName;
    private String  modifierCategoryName;
    private int num;
    private int minNum;

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getModifierCategoryId() {
        return modifierCategoryId;
    }

    public void setModifierCategoryId(int modifierCategoryId) {
        this.modifierCategoryId = modifierCategoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getModifierCategoryName() {
        return modifierCategoryName;
    }

    public void setModifierCategoryName(String modifierCategoryName) {
        this.modifierCategoryName = modifierCategoryName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ModifierCheck{" +
                "id=" + id +
                ", orderDetailId=" + orderDetailId +
                ", orderId=" + orderId +
                ", modifierCategoryId=" + modifierCategoryId +
                ", itemName='" + itemName + '\'' +
                ", modifierCategoryName='" + modifierCategoryName + '\'' +
                ", num=" + num +
                ", minNum=" + minNum +
                '}';
    }
}
