package com.alfredbase.javabean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * TODO :
 * Param :
 * Created by yangk on 2019/2/8.
 *
 * @ return :
 */
public class ItemPromotion implements Serializable {

    private static final long serialVersionUID = -6709227918145280458L;


    private Integer id;

    private Integer promotionId;

    private Integer itemMainCategoryId;

    private Integer itemCategoryId;

    private Integer itemId;

    private Integer type;

    private String discountPrice;

    private String discountPercentage;

    private Integer freeNum;
    
    private Integer freeItemId;
    
    private String itemMainCategoryName;
    
    private String itemCategoryName;
    
    private String itemName;
    
    private String freeItemName;

    private long createTime;

    private long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(Integer freeNum) {
        this.freeNum = freeNum;
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

		public String getItemMainCategoryName() {
			return itemMainCategoryName;
		}

		public void setItemMainCategoryName(String itemMainCategoryName) {
			this.itemMainCategoryName = itemMainCategoryName;
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

    @Override
    public String toString() {
        return "ItemPromotion{" +
                "id=" + id +
                ", promotionId=" + promotionId +
                ", itemMainCategoryId=" + itemMainCategoryId +
                ", itemCategoryId=" + itemCategoryId +
                ", itemId=" + itemId +
                ", type=" + type +
                ", discountPrice='" + discountPrice + '\'' +
                ", discountPercentage='" + discountPercentage + '\'' +
                ", freeNum=" + freeNum +
                ", freeItemId=" + freeItemId +
                ", itemMainCategoryName='" + itemMainCategoryName + '\'' +
                ", itemCategoryName='" + itemCategoryName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", freeItemName='" + freeItemName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}