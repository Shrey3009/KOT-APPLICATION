package com.alfredbase.javabean;



import java.io.Serializable;
import java.util.List;

/**
 * TODO :
 * Param :
 * Created by on 2019/2/8.
 *
 * @ return :
 */
public class Promotion implements Serializable {


    private Integer id;
    private Integer type;  //1
    private String promotionName;
    private Integer restaurantId;
    private Integer isActive;
    private  Integer promotionWeight;//权重  1 . Buy (qty)X Get Y at Z Price   2 .Buy (qty)X Get Y at Z  free  3.Buy Z total price get Y (free or price)
//    4. 分类下多个菜 免费最便宜的一个菜  5.满足用餐人数进行折扣

    private String discountPrice;

    private String discountPercentage;

    private Integer freeNum;

    private Integer freeItemId;

    private String freeItemName;

    private Integer itemMainCategoryId;

    private Integer itemCategoryId;

    private Integer itemId;
    private  Integer itemNum;
    private String itemMainCategoryName;

    private String itemCategoryName;

    private String itemName;

    private Integer secondItemMainCategoryId;

    private Integer secondItemCategoryId;

    private Integer secondItemId;
    private  Integer secondItemNum;
    private String secondItemMainCategoryName;

    private String secondItemCategoryName;

    private String secondItemName;

    private long createTime;

    private long updateTime;
    private String  basePrice;
    private Integer guestNum;// 用餐人数
    private  Integer promotionDateInfoId;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPromotionWeight() {
        return promotionWeight;
    }

    public void setPromotionWeight(Integer promotionWeight) {
        this.promotionWeight = promotionWeight;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(Integer freeNum) {
        this.freeNum = freeNum;
    }

    public Integer getFreeItemId() {
        return freeItemId;
    }

    public void setFreeItemId(Integer freeItemId) {
        this.freeItemId = freeItemId;
    }

    public String getFreeItemName() {
        return freeItemName;
    }

    public void setFreeItemName(String freeItemName) {
        this.freeItemName = freeItemName;
    }

    public Integer getItemMainCategoryId() {
        return itemMainCategoryId;
    }

    public void setItemMainCategoryId(Integer itemMainCategoryId) {
        this.itemMainCategoryId = itemMainCategoryId;
    }

    public Integer getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Integer itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemMainCategoryName() {
        return itemMainCategoryName;
    }

    public void setItemMainCategoryName(String itemMainCategoryName) {
        this.itemMainCategoryName = itemMainCategoryName;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSecondItemMainCategoryId() {
        return secondItemMainCategoryId;
    }

    public void setSecondItemMainCategoryId(Integer secondItemMainCategoryId) {
        this.secondItemMainCategoryId = secondItemMainCategoryId;
    }

    public Integer getSecondItemCategoryId() {
        return secondItemCategoryId;
    }

    public void setSecondItemCategoryId(Integer secondItemCategoryId) {
        this.secondItemCategoryId = secondItemCategoryId;
    }

    public Integer getSecondItemId() {
        return secondItemId;
    }

    public void setSecondItemId(Integer secondItemId) {
        this.secondItemId = secondItemId;
    }

    public String getSecondItemMainCategoryName() {
        return secondItemMainCategoryName;
    }

    public void setSecondItemMainCategoryName(String secondItemMainCategoryName) {
        this.secondItemMainCategoryName = secondItemMainCategoryName;
    }

    public String getSecondItemCategoryName() {
        return secondItemCategoryName;
    }

    public void setSecondItemCategoryName(String secondItemCategoryName) {
        this.secondItemCategoryName = secondItemCategoryName;
    }
    public String getSecondItemName() {
        return secondItemName;
    }

    public void setSecondItemName(String secondItemName) {
        this.secondItemName = secondItemName;
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

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public Integer getSecondItemNum() {
        return secondItemNum;
    }

    public void setSecondItemNum(Integer secondItemNum) {
        this.secondItemNum = secondItemNum;
    }

    public Integer getGuestNum() {
        return guestNum;
    }

    public void setGuestNum(Integer guestNum) {
        this.guestNum = guestNum;
    }

    public Integer getPromotionDateInfoId() {
        return promotionDateInfoId;
    }

    public void setPromotionDateInfoId(Integer promotionDateInfoId) {
        this.promotionDateInfoId = promotionDateInfoId;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", type=" + type +
                ", promotionName='" + promotionName + '\'' +
                ", restaurantId=" + restaurantId +
                ", isActive=" + isActive +
                ", promotionWeight=" + promotionWeight +
                ", discountPrice='" + discountPrice + '\'' +
                ", discountPercentage='" + discountPercentage + '\'' +
                ", freeNum=" + freeNum +
                ", freeItemId=" + freeItemId +
                ", freeItemName='" + freeItemName + '\'' +
                ", itemMainCategoryId=" + itemMainCategoryId +
                ", itemCategoryId=" + itemCategoryId +
                ", itemId=" + itemId +
                ", itemNum=" + itemNum +
                ", itemMainCategoryName='" + itemMainCategoryName + '\'' +
                ", itemCategoryName='" + itemCategoryName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", secondItemMainCategoryId=" + secondItemMainCategoryId +
                ", secondItemCategoryId=" + secondItemCategoryId +
                ", secondItemId=" + secondItemId +
                ", secondItemNum=" + secondItemNum +
                ", secondItemMainCategoryName='" + secondItemMainCategoryName + '\'' +
                ", secondItemCategoryName='" + secondItemCategoryName + '\'' +
                ", secondItemName='" + secondItemName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", basePrice='" + basePrice + '\'' +
                ", guestNum=" + guestNum +
                ", promotionDateInfoId=" + promotionDateInfoId +
                '}';
    }
}
