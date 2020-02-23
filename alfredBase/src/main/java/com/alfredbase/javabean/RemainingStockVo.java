package com.alfredbase.javabean;

import java.io.Serializable;

/**
 * Created by sin on 2018/11/22.
 */
public class RemainingStockVo implements Serializable {
    private int itemId;
    private int num;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
