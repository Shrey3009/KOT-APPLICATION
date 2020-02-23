package com.alfredselfhelp.utils;

import android.text.TextUtils;

import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.LogUtil;
import com.alfredselfhelp.R;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.javabean.NurTagDto;
import com.nordicid.nurapi.NurTag;
import com.nordicid.nurapi.NurTagStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailRFIDHelp {
    public static List<NurTagDto> getUnChooseItemBarCode(List<OrderDetail> orderDetails, NurTagStorage nurTagStorage) {
        Map<String, Integer> orderDetailNumMap = new HashMap<>();
        List<NurTagDto> barCodes = new ArrayList<>();
        if (orderDetails != null) {
            for (OrderDetail orderDetail : orderDetails) {
                if(TextUtils.isEmpty(orderDetail.getBarCode())){
                    continue;
                }
                String barCode = IntegerUtils.format20(orderDetail.getBarCode());
                if (orderDetailNumMap.containsKey(barCode)) {
                    int orderDetailNum = orderDetailNumMap.get(barCode);
                    orderDetailNumMap.put(barCode, orderDetailNum + orderDetail.getItemNum());
                } else {
                    orderDetailNumMap.put(barCode, orderDetail.getItemNum());
                }
            }
        }
        LogUtil.e("TAG", "orderDetails:" + orderDetails.size() +"  getUnChooseItemBarCode  map" + orderDetailNumMap.size());
        if (nurTagStorage != null && nurTagStorage.size() > 0) {
            for (int i = 0; i < nurTagStorage.size(); i++) {
                NurTag nurTag = nurTagStorage.get(i);
                if(nurTag.getEpcString().length() < 20){
                    UIHelp.showSoShortToast(App.getTopActivity(), App.getTopActivity().getString(R.string.invalid_product));
                    continue;
                }
                String barCode = nurTag.getEpcString().substring(0,20);
                if (!orderDetailNumMap.containsKey(barCode)) {
                    NurTagDto nurTagDto = new NurTagDto(barCode, nurTag.getUpdateCount());
                    barCodes.add(nurTagDto);
                } else {
                    int orderDetailNum = orderDetailNumMap.get(barCode);
                    if (orderDetailNum > nurTag.getUpdateCount()) {
                        orderDetailNumMap.put(barCode, orderDetailNum - nurTag.getUpdateCount());
                    } else {
                        orderDetailNumMap.remove(barCode);
                    }
                }

            }
        }
        return barCodes;
    }


    public static Map<String, Integer> getUnScannerItemBarCode(List<OrderDetail> orderDetails, NurTagStorage nurTagStorage) {
        Map<String, Integer> orderDetailNumMap = new HashMap<>();
        if (orderDetails != null) {
            for (OrderDetail orderDetail : orderDetails) {
                if(TextUtils.isEmpty(orderDetail.getBarCode())){
                    continue;
                }
                String barCode = IntegerUtils.format20(orderDetail.getBarCode());
                if (orderDetailNumMap.containsKey(barCode)) {
                    int orderDetailNum = orderDetailNumMap.get(barCode);
                    orderDetailNumMap.put(barCode, orderDetailNum + orderDetail.getItemNum());
                } else {
                    orderDetailNumMap.put(barCode, orderDetail.getItemNum());
                }
            }
        }

        LogUtil.e("TAG", "getUnScannerItemBarCode  orderDetails" + orderDetails.size() + " orderDetailNumMap : "+orderDetailNumMap.size());
        if (nurTagStorage != null && nurTagStorage.size() > 0) {
            for (int i = 0; i < nurTagStorage.size(); i++) {
                NurTag nurTag = nurTagStorage.get(i);
                if(nurTag.getEpcString().length() < 20){
                    UIHelp.showSoShortToast(App.getTopActivity(), App.getTopActivity().getString(R.string.invalid_product));
                    continue;
                }
                String barCode = nurTag.getEpcString().substring(0,20);
                if(orderDetailNumMap.containsKey(barCode)) {
                    int orderDetailNum = orderDetailNumMap.get(barCode);
                    if (orderDetailNum > nurTag.getUpdateCount()) {
                        orderDetailNumMap.put(barCode, orderDetailNum - nurTag.getUpdateCount());
                    } else {
                        orderDetailNumMap.remove(barCode);
                    }
                }
            }
        }
        return orderDetailNumMap;
    }

}
