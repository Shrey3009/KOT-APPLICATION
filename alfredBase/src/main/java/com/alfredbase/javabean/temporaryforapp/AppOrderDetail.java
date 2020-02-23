package com.alfredbase.javabean.temporaryforapp;


public class AppOrderDetail {
    private Integer id;    //'主键id',
    private Integer orderId;    //'订单id',
    private Integer custId;    //'顾客id',
    private Integer itemId;    //'菜的id',
    private String itemName;    //'菜名称',
    private Integer itemNum;    //'菜的数量',
    private String itemPrice;    //'菜单金额',
    private String taxPrice;    //'税收金额',
    private String discountPrice;    //'打折金额',
    private String discountRate;    //'打折比例',
    private String realPrice;    //'实收金额=(菜单金额-HappyHour金额+配料金额)*数量',
    private Integer orderDetailStatus;    //'订单详情状态(0未确认，1已确认，2已下单，3已支付)',
    private Integer discountType;    //'打折类型(0不打折、1根据比例打折、2直接减)',
    private String modifierPrice;    //'配料价格',
    private String specialInstractions;    //'手动存入的指令，如饭前上餐前酒、饭后上水果等',
    private long createTime;    //'创建时间',
    private long updateTime;    //'更新时间',
    private String totalItemPrice;

    private String address;//外卖地址
    private String name;
    private String phone;  //收货人电话
    private long deliveryTime;

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public Integer getOrderDetailStatus() {
        return orderDetailStatus;
    }

    public void setOrderDetailStatus(Integer orderDetailStatus) {
        this.orderDetailStatus = orderDetailStatus;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public String getModifierPrice() {
        return modifierPrice;
    }

    public void setModifierPrice(String modifierPrice) {
        this.modifierPrice = modifierPrice;
    }

    public String getSpecialInstractions() {
        return specialInstractions;
    }

    public void setSpecialInstractions(String specialInstractions) {
        this.specialInstractions = specialInstractions;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(String totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    @Override
    public String toString() {
        return "AppOrderDetail{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", custId=" + custId +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemNum=" + itemNum +
                ", itemPrice='" + itemPrice + '\'' +
                ", taxPrice='" + taxPrice + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", discountRate='" + discountRate + '\'' +
                ", realPrice='" + realPrice + '\'' +
                ", orderDetailStatus=" + orderDetailStatus +
                ", discountType=" + discountType +
                ", modifierPrice='" + modifierPrice + '\'' +
                ", specialInstractions='" + specialInstractions + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", totalItemPrice='" + totalItemPrice + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
