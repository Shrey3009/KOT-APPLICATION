package com.alfredbase.javabean;

public class PrinterGroup {

    public static final int KDS_NORMAL = 0;
    public static final int KDS_ASMBLY_LINE = 1;

    private Integer id;

    private Integer printerGroupId;

    private Integer printerId;//can be group id and regular printer id

    private String printerName; //not used in DB

    private Integer companyId;

    private Integer restaurantId;

    /**
     * printer group type
     * 0 = broadcast
     * 1 = assembly line
     */
    private Integer printerType;

    private int isChildGroup;

    private Integer sequenceNumber;

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrinterGroupId() {
        return printerGroupId;
    }

    public void setPrinterGroupId(Integer printerGroupId) {
        this.printerGroupId = printerGroupId;
    }

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getPrinterType() {
        return printerType;
    }

    public void setPrinterType(Integer printerType) {
        this.printerType = printerType;
    }

    public int getIsChildGroup() {
        return isChildGroup;
    }

    public void setIsChildGroup(int isChildGroup) {
        this.isChildGroup = isChildGroup;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}