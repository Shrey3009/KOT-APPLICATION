package com.alfredbase.javabean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Order implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3504035521760172879L;

    private Integer id;

    /**
     * 订单来源(1pos、2服务员、3桌子自助、4手机app, 5SubPos)
     */
    private Integer orderOriginId;

    /**
     * 当前操作员工id
     */
    private Integer userId;

    /**
     * 就餐人数
     */
    private Integer persons;
    /**
     * 订单状态(1打开 从订单生成到打印凭条、10waiter发送到pos机、11待支付 、12已支付、13 挂单、20拆单、21拆单结算中、30已完成)
     */
    private Integer orderStatus;

    /**
     * 订单总金额
     */
    private String subTotal;
    /**
     * 税收总金额
     */
    private String taxAmount;
    /**
     * 打折总金额
     */
    private String discountAmount;

    /**
     * 主订单直接减免的金额
     */
    private String discountPrice;

    /**
     * 主订单按比例打折的比例
     */
    private String discountRate;
    /**
     * (0不打折、10主订单按照比率打折、11主订单直接减、12子订单打折, 21按分类百分比打折, 22 按分类直接减)
     */
    private Integer discountType;
    /**
     * 实收金额=订单总金额+税收总金额-打折总金额
     */
    private String total;
    /**
     * 早中午餐(0全天、1早上、2午餐、3晚餐)
     */
    private Integer sessionStatus;
    /**
     * 餐厅id
     */
    private Integer restId;
    /**
     * 收银中心id
     */
    private Integer revenueId;
    /**
     * 区域id
     */
    private Integer placeId;
    /**
     * 桌子id
     */
    private Integer tableId;

    private Long createTime;

    private Long updateTime;

    private Long businessDate;
    /**
     * 订单号
     */
    private Integer orderNo;

    private String inclusiveTaxName;

    private String inclusiveTaxPrice;

    private String inclusiveTaxPercentage;

    private Integer appOrderId;

    //默认不是takeaway
    /**
     * 0 dine in
     * 1 Delivery
     * 2 take away
     */
    private Integer isTakeAway = 0; //0 堂吃, 1 打包, 2外卖

    private String tableName;

    /**
     * 订单备注
     */
    private String orderRemark;

    private String discountCategoryId;

    // 在已经结账的订单上面做修改的时候 用到 临时用 不存数据库
    @Expose(serialize = false)
    private String oldTotal;
    // 给SubPos使用的tag，A,B,C等字母
    private String numTag = "";

    private int subPosBeanId = 0;// IF it is main Pos， subPosBeanId is  zero
    private String waiterInformation;
    private int isWaiterPrint; //0 未打印， 1 已打印
    @Expose(serialize = false)
    private int isSplitByPax;
    private String promotion;

    private String grandTotal; // only used "split by pax"
    private String  orderRound;
    public Order() {
    }

    public Order(Integer id, Integer orderOriginId, Integer userId,
                 Integer persons, Integer orderStatus, String subTotal,
                 String taxAmount, String discountAmount, String discountPrice,
                 String discountRate, Integer discountType, String total,
                 Integer sessionStatus, Integer restId, Integer revenueId,
                 Integer placeId, Integer tableId, Long createTime, Long updateTime,
                 Long businessDate, Integer orderNo, String inclusiveTaxName,
                 String inclusiveTaxPrice, String inclusiveTaxPercentage,
                 Integer appOrderId, Integer isTakeAway) {
        super();
        this.id = id;
        this.orderOriginId = orderOriginId;
        this.userId = userId;
        this.persons = persons;
        this.orderStatus = orderStatus;
        this.subTotal = subTotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
        this.discountType = discountType;
        this.total = total;
        this.sessionStatus = sessionStatus;
        this.restId = restId;
        this.revenueId = revenueId;
        this.placeId = placeId;
        this.tableId = tableId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.businessDate = businessDate;
        this.orderNo = orderNo;
        this.inclusiveTaxName = inclusiveTaxName;
        this.inclusiveTaxPrice = inclusiveTaxPrice;
        this.inclusiveTaxPercentage = inclusiveTaxPercentage;
        this.appOrderId = appOrderId;
        this.isTakeAway = isTakeAway;
    }

    public Order(Integer id, Integer orderOriginId, Integer userId,
                 Integer persons, Integer orderStatus, String subTotal,
                 String taxAmount, String discountAmount, String discountRate,
                 Integer discountType, String total, Integer sessionStatus,
                 Integer restId, Integer revenueId, Integer tableId,
                 Long createTime, Long updateTime, Long businessDate,
                 Integer orderNo) {
        super();
        this.id = id;
        this.orderOriginId = orderOriginId;
        this.userId = userId;
        this.persons = persons;
        this.orderStatus = orderStatus;
        this.subTotal = subTotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
        this.discountType = discountType;
        this.total = total;
        this.sessionStatus = sessionStatus;
        this.restId = restId;
        this.revenueId = revenueId;
        this.tableId = tableId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.businessDate = businessDate;
        this.orderNo = orderNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPersons() {
        return persons;
    }

    public void setPersons(Integer persons) {
        this.persons = persons;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(Integer sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Integer getRestId() {
        return restId;
    }

    public void setRestId(Integer restId) {
        this.restId = restId;
    }

    public Integer getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(Integer revenueId) {
        this.revenueId = revenueId;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
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

    public Long getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Long businessDate) {
        this.businessDate = businessDate;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }


    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getInclusiveTaxName() {
        return inclusiveTaxName;
    }

    public void setInclusiveTaxName(String inclusiveTaxName) {
        this.inclusiveTaxName = inclusiveTaxName;
    }

    public String getInclusiveTaxPrice() {
        return inclusiveTaxPrice;
    }

    public void setInclusiveTaxPrice(String inclusiveTaxPrice) {
        this.inclusiveTaxPrice = inclusiveTaxPrice;
    }

    public String getInclusiveTaxPercentage() {
        return inclusiveTaxPercentage;
    }

    public void setInclusiveTaxPercentage(String inclusiveTaxPercentage) {
        this.inclusiveTaxPercentage = inclusiveTaxPercentage;
    }

    public Integer getAppOrderId() {
        return appOrderId;
    }

    public void setAppOrderId(Integer appOrderId) {
        this.appOrderId = appOrderId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public Integer getIsTakeAway() {
        return isTakeAway;
    }

    public void setIsTakeAway(Integer isTakeAway) {
        this.isTakeAway = isTakeAway;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getDiscountCategoryId() {
        return discountCategoryId;
    }

    public void setDiscountCategoryId(String discountCategoryId) {
        this.discountCategoryId = discountCategoryId;
    }

    public String getOldTotal() {
        return oldTotal;
    }

    public void setOldTotal(String oldTotal) {
        this.oldTotal = oldTotal;
    }

    public String getNumTag() {
        return numTag;
    }

    public void setNumTag(String numTag) {
        this.numTag = numTag;
    }

    public int getSubPosBeanId() {
        return subPosBeanId;
    }

    public void setSubPosBeanId(int subPosBeanId) {
        this.subPosBeanId = subPosBeanId;
    }

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public String getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getOrderRound() {
		return orderRound;
	}

	public void setOrderRound(String orderRound) {
		this.orderRound = orderRound;
	}

    public String getWaiterInformation() {
        return waiterInformation;
    }

    public void setWaiterInformation(String waiterInformation) {
        this.waiterInformation = waiterInformation;
    }

	public int getIsWaiterPrint() {
		return isWaiterPrint;
	}

	public void setIsWaiterPrint(int isWaiterPrint) {
		this.isWaiterPrint = isWaiterPrint;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", orderOriginId=" + orderOriginId +
				", userId=" + userId +
				", persons=" + persons +
				", orderStatus=" + orderStatus +
				", subTotal='" + subTotal + '\'' +
				", taxAmount='" + taxAmount + '\'' +
				", discountAmount='" + discountAmount + '\'' +
				", discountPrice='" + discountPrice + '\'' +
				", discountRate='" + discountRate + '\'' +
				", discountType=" + discountType +
				", total='" + total + '\'' +
				", sessionStatus=" + sessionStatus +
				", restId=" + restId +
				", revenueId=" + revenueId +
				", placeId=" + placeId +
				", tableId=" + tableId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", businessDate=" + businessDate +
				", orderNo=" + orderNo +
				", inclusiveTaxName='" + inclusiveTaxName + '\'' +
				", inclusiveTaxPrice='" + inclusiveTaxPrice + '\'' +
				", inclusiveTaxPercentage='" + inclusiveTaxPercentage + '\'' +
				", appOrderId=" + appOrderId +
				", isTakeAway=" + isTakeAway +
				", tableName='" + tableName + '\'' +
				", orderRemark='" + orderRemark + '\'' +
				", discountCategoryId='" + discountCategoryId + '\'' +
				", oldTotal='" + oldTotal + '\'' +
				", numTag='" + numTag + '\'' +
				", subPosBeanId=" + subPosBeanId +
				", isSplitByPax=" + isSplitByPax +
				", promotion='" + promotion + '\'' +
				", orderRound='" + orderRound + '\'' +
				", waiterInformation='" + waiterInformation + '\'' +
				", isWaiterPrint=" + isWaiterPrint +
				", isSplitByPax=" + isSplitByPax +
				'}';

}
}