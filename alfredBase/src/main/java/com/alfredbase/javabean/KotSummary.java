package com.alfredbase.javabean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class KotSummary implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3305618260951189578L;
    private Integer id;
    private Integer orderId; // linked to Order Table ID
    private Integer revenueCenterId;
    private Integer tableId;
    private String tableName;
    private String revenueCenterName;
    private int status;//KotSummary的状态为undone和done两种，分别为0和1
    private long createTime;
    private Long updateTime;
    private long businessDate;
    private Integer isTakeAway;

    private Integer orderNo; //流水号, 和订单的流水号一致。only for print


    //不存数据库 临时变量
    private String description;

    // 用于储存收银中心的indexId
    private Integer revenueCenterIndex = 1;

    private String orderRemark;// order 里面的备注

    private String empName; // 提交菜品的用户名

    private String numTag = "";
    private int eatType; // 0 堂吃, 1 打包, 2外卖
    private String address;//外卖地址
    private String contact;
    private String mobile;  //收货人电话
    private long deliveryTime;

    private int appOrderId;
    private String remarks;
    private int isSubPos;
    private String kotSummaryLog;
    private int kdsType;
    private int orderDetailCount;
    /**
     * put original kotSummary id here for temporary
     */
    private Integer originalId;

    private int isNext;

    private long completeTime;

    private long tagId;

    private String uniqueId;

    private String originalUniqueId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getEatType() {
        return eatType;
    }

    public int getAppOrderId() {
        return appOrderId;
    }

    public void setAppOrderId(int appOrderId) {
        this.appOrderId = appOrderId;
    }

    public void setEatType(int eatType) {
        this.eatType = eatType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public Integer getRevenueCenterId() {
        return revenueCenterId;
    }

    public void setRevenueCenterId(Integer revenueCenterId) {
        this.revenueCenterId = revenueCenterId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRevenueCenterName() {
        return revenueCenterName;
    }

    public void setRevenueCenterName(String revenueCenterName) {
        this.revenueCenterName = revenueCenterName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //: not use it for print
    public String getOrderIdString() {
        return String.valueOf(this.orderId);
    }

    public String getKotIdString() {
        return String.valueOf(this.id);
    }

    public String getCreateTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = formatter.format(this.createTime);
        return dateString;
    }

    public long getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(long businessDate) {
        this.businessDate = businessDate;
    }

    public Integer getIsTakeAway() {
        return isTakeAway;
    }

    public void setIsTakeAway(Integer isTakeAway) {
        this.isTakeAway = isTakeAway;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    //流水号
    public String getOrderNoString() {
        return String.valueOf(this.orderNo);
    }

    public Integer getRevenueCenterIndex() {
        return revenueCenterIndex;
    }

    public void setRevenueCenterIndex(Integer revenueCenterIndex) {
        this.revenueCenterIndex = revenueCenterIndex;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getNumTag() {
        return numTag;
    }

    public void setNumTag(String numTag) {
        this.numTag = numTag;
    }

    public int getIsSubPos() {
        return isSubPos;
    }

    public void setIsSubPos(int isSubPos) {
        this.isSubPos = isSubPos;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getKotSummaryLog() {
        return kotSummaryLog;
    }

    public void setKotSummaryLog(String kotSummaryLog) {
        this.kotSummaryLog = kotSummaryLog;
    }

    public int getKdsType() {
        return kdsType;
    }

    public void setKdsType(int kdsType) {
        this.kdsType = kdsType;
    }

    public int getOrderDetailCount() {
        return orderDetailCount;
    }

    public void setOrderDetailCount(int orderDetailCount) {
        this.orderDetailCount = orderDetailCount;
    }

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public int isNext() {
        return isNext;
    }

    public void setNext(int next) {
        isNext = next;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getOriginalUniqueId() {
        return originalUniqueId;
    }

    public void setOriginalUniqueId(String originalUniqueId) {
        this.originalUniqueId = originalUniqueId;
    }

    @Override
    public String toString() {
        return "KotSummary{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", revenueCenterId=" + revenueCenterId +
                ", tableId=" + tableId +
                ", tableName='" + tableName + '\'' +
                ", revenueCenterName='" + revenueCenterName + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", businessDate=" + businessDate +
                ", isTakeAway=" + isTakeAway +
                ", orderNo=" + orderNo +
                ", description='" + description + '\'' +
                ", revenueCenterIndex=" + revenueCenterIndex +
                ", orderRemark='" + orderRemark + '\'' +
                ", empName='" + empName + '\'' +
                ", numTag='" + numTag + '\'' +
                ", eatType=" + eatType +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", mobile='" + mobile + '\'' +
                ", deliveryTime=" + deliveryTime +
                ", appOrderId=" + appOrderId +
                ", remarks='" + remarks + '\'' +
                ", isSubPos=" + isSubPos +
                ", kotSummaryLog='" + kotSummaryLog + '\'' +
                ", kdsType=" + kdsType +
                ", orderDetailCount=" + orderDetailCount +
                ", originalId=" + originalId +
                ", isNext=" + isNext +
                ", completeTime=" + completeTime +
                ", tagId=" + tagId +
                ", uniqueId='" + uniqueId + '\'' +
                ", originalUniqueId='" + originalUniqueId + '\'' +
                '}';
    }
}
