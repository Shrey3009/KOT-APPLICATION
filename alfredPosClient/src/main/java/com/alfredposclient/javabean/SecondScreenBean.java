package com.alfredposclient.javabean;

import com.alfredbase.store.Store;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

/**
 * Created by Alex on 16/11/14.
 */

public class SecondScreenBean {
    //  index
    private String param1;
    //  itemName
    private String param2;
    //  itemPrice
    private String param3;
    //  itemQty
    private String param4;
    //  itemTotal
    private String param5;

    public SecondScreenBean() {
        if (Store.getInt(App.getTopActivity(), Store.SUNMI_STYLE) == Store.SUNMI_TEXT) {
            param1 = App.instance.getResources().getString(R.string.index);
            param2 = App.instance.getResources().getString(R.string.name);
            param3 = App.instance.getResources().getString(R.string.price);
            param4 = App.instance.getResources().getString(R.string.qty);
            param5 = App.instance.getResources().getString(R.string.total);
        }else if (Store.getInt(App.getTopActivity(), Store.SUNMI_STYLE) == Store.SUNMI_IMG_TEXT){
            param1 = App.instance.getResources().getString(R.string.name);
            param2 = App.instance.getResources().getString(R.string.price);
            param3 = App.instance.getResources().getString(R.string.qty);
            param4 = App.instance.getResources().getString(R.string.total);
        }else if (Store.getInt(App.getTopActivity(), Store.SUNMI_STYLE) == Store.SUNMI_VIDEO_TEXT){
            param1 = App.instance.getResources().getString(R.string.name);
            param2 = App.instance.getResources().getString(R.string.price);
            param3 = App.instance.getResources().getString(R.string.qty);
            param4 = App.instance.getResources().getString(R.string.total);
        }
    }

    public SecondScreenBean(String index, String itemName, String itemPrice, String itemQty, String itemTotal) {
        this.param1 = index;
        this.param2 = itemName;
        this.param3 = itemPrice;
        this.param4 = itemQty;
        this.param5 = itemTotal;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    @Override
    public String toString() {
        return "SecondScreenData{" +
                "param1(index)='" + param1 + '\'' +
                ", param2(itemName)='" + param2 + '\'' +
                ", param3(itemPrice)='" + param3 + '\'' +
                ", param4(itemQty)='" + param4 + '\'' +
                ", param5(itemTotal)='" + param5 + '\'' +
                '}';
    }
}
