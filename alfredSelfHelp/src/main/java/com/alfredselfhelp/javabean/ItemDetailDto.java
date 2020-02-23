package com.alfredselfhelp.javabean;

public class ItemDetailDto {
    private int itemId;
    private int itemNum;
    private String itemName;
    private String barCode;
    private String itemPrice;
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "ItemDetailDto{" +
                "itemId=" + itemId +
                ", itemNum=" + itemNum +
                ", itemName='" + itemName + '\'' +
                ", barCode='" + barCode + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                '}';
    }
}
