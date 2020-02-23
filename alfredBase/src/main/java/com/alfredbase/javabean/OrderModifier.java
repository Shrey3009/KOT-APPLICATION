package com.alfredbase.javabean;


public class OrderModifier {
    private Integer id;

    private Integer orderId;

    private Integer orderDetailId;

    private Integer orderOriginId;

    private Integer userId;

    private Integer itemId;

    private Integer modifierId;

    private Integer modifierNum;

    /**
     * 配料订单状态(-1删除，0正常)
     */
    private Integer status;
    /**
     * Modifier总金额
     */
    private String modifierPrice;

    private Long createTime;

    private Long updateTime;

    private Integer printerId;//Printer Group

    private String modifierItemPrice;
    /**
     * 只用于前端,判断是否满足minNumber
     * 0 否  1 是
     */
    private int isMin;

    /**
     * not save to db
     * just for print and local used
     */
    public String modifierName;


    public OrderModifier() {
    }

    public int getIsMin() {
        return isMin;
    }

    public void setIsMin(int isMin) {
        this.isMin = isMin;
    }

    public OrderModifier(Integer id, Integer orderId, Integer orderDetailId,
                         Integer orderOriginId, Integer userId, Integer itemId,
                         Integer modifierId, Integer modifierNum, Integer status,
                         String modifierPrice, Long createTime, Long updateTime, String modifierItemPrice) {
        super();
        this.id = id;
        this.orderId = orderId;
        this.orderDetailId = orderDetailId;
        this.orderOriginId = orderOriginId;
        this.userId = userId;
        this.itemId = itemId;
        this.modifierId = modifierId;
        this.modifierNum = modifierNum;
        this.status = status;
        this.modifierPrice = modifierPrice;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.modifierItemPrice = modifierItemPrice;
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

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
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

    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    public Integer getModifierNum() {
        return modifierNum;
    }

    public void setModifierNum(Integer modifierNum) {
        this.modifierNum = modifierNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getModifierPrice() {
        return modifierPrice;
    }

    public void setModifierPrice(String modifierPrice) {
        this.modifierPrice = modifierPrice;
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

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public String getModifierItemPrice() {
        return modifierItemPrice;
    }

    public void setModifierItemPrice(String modifierItemPrice) {
        this.modifierItemPrice = modifierItemPrice;
    }

    @Override
    public String toString() {
        return "OrderModifier{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderDetailId=" + orderDetailId +
                ", orderOriginId=" + orderOriginId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", modifierId=" + modifierId +
                ", modifierNum=" + modifierNum +
                ", status=" + status +
                ", modifierPrice='" + modifierPrice + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", printerId=" + printerId +
                ", modifierItemPrice='" + modifierItemPrice + '\'' +
                '}';
    }

}