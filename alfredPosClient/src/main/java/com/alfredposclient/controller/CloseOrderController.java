package com.alfredposclient.controller;

import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderSplit;

/**
 * Created by Alex on 16/10/3.
 */

public class CloseOrderController {
    private Order order;
    private OrderSplit orderSplit;
    private OrderBill orderBill;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderSplit getOrderSplit() {
        return orderSplit;
    }

    public void setOrderSplit(OrderSplit orderSplit) {
        this.orderSplit = orderSplit;
    }

    public OrderBill getOrderBill() {
        return orderBill;
    }

    public void setOrderBill(OrderBill orderBill) {
        this.orderBill = orderBill;
    }

    @Override
    public String toString() {
        return "CloseOrderController{" +
                "order=" + order +
                ", orderSplit=" + orderSplit +
                ", orderBill=" + orderBill +
                '}';
    }
}
