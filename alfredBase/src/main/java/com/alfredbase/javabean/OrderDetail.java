package com.alfredbase.javabean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class OrderDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8940119869956206734L;

    private Integer id;

    private Integer orderId;

    /**
     * 订单来源
     */
    private Integer orderOriginId;

    private Integer userId;

    private Integer itemId;

    /* general item or open item */
    private String itemName;

    private Integer itemNum;

    /**
     * 订单详情状态(0waiter用表示还没有概念上的保存,1added用于Pos点菜和waiter那边save之后的状态、2Kotprinterd、发送到厨房 3prepared、 厨房已经做好了 4served、 服务员已经送菜
     * 5removed、 还没有送到厨房之前退单 6cancelled 退单)
     */
    private Integer orderDetailStatus;

    /**
     * 订单详情类型 0--general、1--void、2--free
     */
    private Integer orderDetailType;

    private String reason;

    private Integer printStatus;

    private String itemPrice;

    private String taxPrice;

    private String discountPrice;

    private String discountRate;
    /**
     * 打折类型(0不打折、1根据自己比例打折、2根据自己直接减、3根据Order按照比例打折、4根据Order直接减, 5 根据分类按比例打折, 6根据分类直接减)
     */
    private Integer discountType = 0;

    private String modifierPrice;
    /**
     * 实收金额=(菜单金额+配料金额)*数量
     */
    private String realPrice;

    private Long createTime;

    private Long updateTime;

    /**
     * 哪个菜送的
     */
    private Integer fromOrderDetailId;

    /**
     * 是否是赠送(0非赠送、1赠送)
     */
    private Integer isFree;

    private Integer groupId;

    /* Open Item */
    private Integer isOpenItem; /* 0: No (default); 1: YES */

    /**
     * 手动存入的指令，如饭前上餐前酒、饭后上水果等
     */
    private String specialInstractions = "";

    private Integer orderSplitId;

    /**
     * 是否外带(0不外带、1外带)
     */
    private int isTakeAway;


    /**
     * 重量 暂时只前端用
     */
    private String weight;
    private String barCode;


    // 目前只用于前端，从itemDetail过来的数据，方便用做计算和判断（在计算的时候减少数据库的耗时操作）
    @Expose(serialize = false)
    private int isItemDiscount = 1;

    private int appOrderDetailId = 0;

    private int mainCategoryId;

    private int fireStatus;

    /**
     * 只用于本地计算，方便计算
     */
    private int isSet = 0;
    //菜品 图片
    private String itemUrl;

    // 说明
    private String itemDesc ;


    /**
     * 不存数据库 临时缓存用
     */
    @Expose(serialize = false)
    private int transferFromDetailId = 0;
    @Expose(serialize = false)
    private int transferFromDetailNum = 0;
    private String orderDetailRound;

    public OrderDetail() {
        // set openItem False
        this.isOpenItem = 0;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
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

    public Integer getOrderOriginId() {
        return orderOriginId;
    }

    public void setOrderOriginId(Integer orderOriginId) {
        this.orderOriginId = orderOriginId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public Integer getOrderDetailStatus() {
        return orderDetailStatus;
    }

    public void setOrderDetailStatus(Integer orderDetailStatus) {
        this.orderDetailStatus = orderDetailStatus;
    }

    public Integer getOrderDetailType() {
        return orderDetailType;
    }

    public void setOrderDetailType(Integer orderDetailType) {
        this.orderDetailType = orderDetailType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Integer printStatus) {
        this.printStatus = printStatus;
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

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFromOrderDetailId() {
        return fromOrderDetailId;
    }

    public void setFromOrderDetailId(Integer fromOrderDetailId) {
        this.fromOrderDetailId = fromOrderDetailId;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public Integer getGroupId() {
        if (groupId == null) {
            groupId = 0;
        }
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getSpecialInstractions() {
        return specialInstractions;
    }

    public void setSpecialInstractions(String specialInstractions) {
        this.specialInstractions = specialInstractions;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getIsOpenItem() {
        return isOpenItem;
    }

    public void setIsOpenItem(Integer isOpenItem) {
        this.isOpenItem = isOpenItem;
    }

    public Integer getOrderSplitId() {
        if(orderSplitId == null){
            orderSplitId = 0;
        }
        return orderSplitId;
    }

    public void setOrderSplitId(Integer orderSplitId) {
        this.orderSplitId = orderSplitId;
    }

    public int getIsTakeAway() {
        return isTakeAway;
    }

    public void setIsTakeAway(int isTakeAway) {
        this.isTakeAway = isTakeAway;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getIsItemDiscount() {
        return isItemDiscount;
    }

    public void setIsItemDiscount(int isItemDiscount) {
        this.isItemDiscount = isItemDiscount;
    }

    public int getIsSet() {
        return isSet;
    }

    public void setIsSet(int isSet) {
        this.isSet = isSet;
    }

    public int getAppOrderDetailId() {
        return appOrderDetailId;
    }

    public void setAppOrderDetailId(int appOrderDetailId) {
        this.appOrderDetailId = appOrderDetailId;
    }

    public int getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public int getFireStatus() {
        return fireStatus;
    }

    public void setFireStatus(int fireStatus) {
        this.fireStatus = fireStatus;
    }

    public int getTransferFromDetailId() {
        return transferFromDetailId;
    }

    public void setTransferFromDetailId(int transferFromDetailId) {
        this.transferFromDetailId = transferFromDetailId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOrderDetailRound() {
        return orderDetailRound;
    }

    public void setOrderDetailRound(String orderDetailRound) {
        this.orderDetailRound = orderDetailRound;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderOriginId=" + orderOriginId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemNum=" + itemNum +
                ", orderDetailStatus=" + orderDetailStatus +
                ", orderDetailType=" + orderDetailType +
                ", reason='" + reason + '\'' +
                ", printStatus=" + printStatus +
                ", itemPrice='" + itemPrice + '\'' +
                ", taxPrice='" + taxPrice + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", discountRate='" + discountRate + '\'' +
                ", discountType=" + discountType +
                ", modifierPrice='" + modifierPrice + '\'' +
                ", realPrice='" + realPrice + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", fromOrderDetailId=" + fromOrderDetailId +
                ", isFree=" + isFree +
                ", groupId=" + groupId +
                ", isOpenItem=" + isOpenItem +
                ", specialInstractions='" + specialInstractions + '\'' +
                ", orderSplitId=" + orderSplitId +
                ", isTakeAway=" + isTakeAway +
                ", weight='" + weight + '\'' +
                ", barCode='" + barCode + '\'' +
                ", isItemDiscount=" + isItemDiscount +
                ", appOrderDetailId=" + appOrderDetailId +
                ", mainCategoryId=" + mainCategoryId +
                ", fireStatus=" + fireStatus +
                ", isSet=" + isSet +
                ", itemUrl='" + itemUrl + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", transferFromDetailId=" + transferFromDetailId +
                ", transferFromDetailNum=" + transferFromDetailNum +
                ", orderDetailRound='" + orderDetailRound + '\'' +
                '}';
    }

    public int getTransferFromDetailNum() {
        return transferFromDetailNum;
    }

    public void setTransferFromDetailNum(int transferFromDetailNum) {
        this.transferFromDetailNum = transferFromDetailNum;
    }


}