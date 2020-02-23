package com.alfredposclient.activity;

import com.alfredbase.javabean.OrderDetail;

import java.util.List;

public class DiffData {

    private DifferentData data;

   public  DiffData(DifferentData data){
       this.data=data;
   }

    public void updateData(List<OrderDetail> orderDetails){
        data.update(orderDetails);//开始发送数据
    }

}
