package com.alfredkds.view;

/** This is just a simple class for holding data that is used to render our custom view */
public class KOTData {
    private String mKotid;
    private String mOrderId;
    private String mTableId;


    public KOTData(String kotId, String orderId, String tableId) {
        this.mKotid = kotId;
        this.mOrderId = orderId;
        this.mTableId = tableId;
    }

    /**
     * @return the backgroundColor
     */
    public String getKotId() {
        return this.mKotid;
    }

    /**
     * @return the text
     */
    public String getOrderId() {
        return this.mOrderId;
    }
}
