package com.alfredbase.javabean;

public class RemainingStock {
    private Integer id;

    private Integer restaurantId;

    private Integer itemId;

    private Integer qty;

    private Integer defultQty;

    private Integer minQty;

    private Integer isActive;

    private Integer displayQty;

    private long createTime;

    private long updateTime;

    private long resetTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getDefultQty() {
        return defultQty;
    }

    public void setDefultQty(Integer defultQty) {
        this.defultQty = defultQty;
    }

    public Integer getMinQty() {
        return minQty;
    }

    public void setMinQty(Integer minQty) {
        this.minQty = minQty;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(Integer displayQty) {
        this.displayQty = displayQty;
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

    public long getResetTime() {
        return resetTime;
    }

    public void setResetTime(long resetTime) {
        this.resetTime = resetTime;
    }

    @Override
    public String toString() {
        return "RemainingStock{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", itemId=" + itemId +
                ", qty=" + qty +
                ", defultQty=" + defultQty +
                ", minQty=" + minQty +
                ", isActive=" + isActive +
                ", displayQty=" + displayQty +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", resetTime=" + resetTime +
                '}';
    }
}
