package com.alfredbase.javabean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 菜的详细信息
 *
 * @author Alex
 *
 */
public class ItemDetail implements Serializable ,Parcelable{

  /**
   *
   */
  private static final long serialVersionUID = 6722615760625234244L;

  private Integer id;

  private Integer restaurantId;

  private Integer itemTemplateId = 0;

  private Integer revenueId;

  private String itemName = "";
  // 说明
  private String itemDesc = "";

  private String itemCode = "";

  private String imgUrl = "";

  private String price;

  /**
   * 菜品类型(0菜单模板，1子菜单、2临时菜、3套餐)
   */
  private Integer itemType;

  /**
   * 设备组
   */
  private Integer printerId;

  private Integer isModifier;

  private Integer itemMainCategoryId;

  private Integer itemCategoryId;

  /**
   * 是否可用(-1删除，0禁用，1正常)
   */
  private Integer isActive;

  private Integer taxCategoryId;

  /**
   * 是否可以打包(0不可以，1可以) takeaways 打包带走 pack 打包
   */
  private Integer isPack;

  /**
   * 是否可外卖(0不可以，1可以) delivery 外卖 Takeout 外卖
   */
  private Integer isTakeout;

  private Integer happyHoursId;

  private Integer userId;

  private Long createTime;

  private Long updateTime;

  private Integer indexId;

  // 0不可以打折， 1可以打折
  private int isDiscount = 1;
  //
  private String barcode;

  private String itemCategoryName;
  private String tag;
  private int viewType;
  public ItemDetail() {
  }

  public ItemDetail(Integer id, Integer restaurantId, Integer itemTemplateId,
                    Integer revenueId, String itemName, String itemDesc,
                    String itemCode, String imgUrl, String price, Integer itemType,
                    Integer printerId, Integer isModifier, Integer itemMainCategoryId,
                    Integer itemCategoryId, Integer isActive, Integer taxCategoryId,
                    Integer isPack, Integer isTakeout, Integer happyHoursId,
                    Integer userId, Long createTime, Long updateTime,Integer indexId, int isDiscount,String itemCategoryName,String tag,int viewType) {
    super();
    this.id = id;
    this.restaurantId = restaurantId;
    this.itemTemplateId = itemTemplateId;
    this.revenueId = revenueId;
    this.itemName = itemName;
    this.itemDesc = itemDesc;
    this.itemCode = itemCode;
    this.imgUrl = imgUrl;
    this.price = price;
    this.itemType = itemType;
    this.printerId = printerId;
    this.isModifier = isModifier;
    this.itemMainCategoryId = itemMainCategoryId;
    this.itemCategoryId = itemCategoryId;
    this.isActive = isActive;
    this.taxCategoryId = taxCategoryId;
    this.isPack = isPack;
    this.isTakeout = isTakeout;
    this.happyHoursId = happyHoursId;
    this.userId = userId;
    this.createTime = createTime;
    this.updateTime = updateTime;
    this.indexId = indexId;
    this.isDiscount = isDiscount;
    this.itemCategoryName=itemCategoryName;
    this.tag=tag;
    this.viewType=viewType;
  }

  protected ItemDetail(Parcel in) {
    if (in.readByte() == 0) {
      id = null;
    } else {
      id = in.readInt();
    }
    if (in.readByte() == 0) {
      restaurantId = null;
    } else {
      restaurantId = in.readInt();
    }
    if (in.readByte() == 0) {
      itemTemplateId = null;
    } else {
      itemTemplateId = in.readInt();
    }
    if (in.readByte() == 0) {
      revenueId = null;
    } else {
      revenueId = in.readInt();
    }
    itemName = in.readString();
    itemDesc = in.readString();
    itemCode = in.readString();
    imgUrl = in.readString();
    price = in.readString();
    if (in.readByte() == 0) {
      itemType = null;
    } else {
      itemType = in.readInt();
    }
    if (in.readByte() == 0) {
      printerId = null;
    } else {
      printerId = in.readInt();
    }
    if (in.readByte() == 0) {
      isModifier = null;
    } else {
      isModifier = in.readInt();
    }
    if (in.readByte() == 0) {
      itemMainCategoryId = null;
    } else {
      itemMainCategoryId = in.readInt();
    }
    if (in.readByte() == 0) {
      itemCategoryId = null;
    } else {
      itemCategoryId = in.readInt();
    }
    if (in.readByte() == 0) {
      isActive = null;
    } else {
      isActive = in.readInt();
    }
    if (in.readByte() == 0) {
      taxCategoryId = null;
    } else {
      taxCategoryId = in.readInt();
    }
    if (in.readByte() == 0) {
      isPack = null;
    } else {
      isPack = in.readInt();
    }
    if (in.readByte() == 0) {
      isTakeout = null;
    } else {
      isTakeout = in.readInt();
    }
    if (in.readByte() == 0) {
      happyHoursId = null;
    } else {
      happyHoursId = in.readInt();
    }
    if (in.readByte() == 0) {
      userId = null;
    } else {
      userId = in.readInt();
    }
    if (in.readByte() == 0) {
      createTime = null;
    } else {
      createTime = in.readLong();
    }
    if (in.readByte() == 0) {
      updateTime = null;
    } else {
      updateTime = in.readLong();
    }
    if (in.readByte() == 0) {
      indexId = null;
    } else {
      indexId = in.readInt();
    }
    isDiscount = in.readInt();
    barcode = in.readString();
    itemCategoryName = in.readString();
  }

  public static final Creator<ItemDetail> CREATOR = new Creator<ItemDetail>() {
    @Override
    public ItemDetail createFromParcel(Parcel in) {
      return new ItemDetail(in);
    }

    @Override
    public ItemDetail[] newArray(int size) {
      return new ItemDetail[size];
    }
  };

  public String getItemCategoryName() {
    return itemCategoryName;
  }

  public void setItemCategoryName(String itemCategoryName) {
    this.itemCategoryName = itemCategoryName;
  }

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

  public Integer getRevenueId() {
    return revenueId;
  }

  public void setRevenueId(Integer revenueId) {
    this.revenueId = revenueId;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName == null ? null : itemName.trim();
  }

  public String getItemDesc() {
    return itemDesc;
  }

  public void setItemDesc(String itemDesc) {
    this.itemDesc = itemDesc == null ? null : itemDesc.trim();
  }

  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode == null ? null : itemCode.trim();
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public Integer getItemType() {
    return itemType;
  }

  public void setItemType(Integer itemType) {
    this.itemType = itemType;
  }

  public Integer getPrinterId() {
    return printerId;
  }

  public void setPrinterId(Integer printerId) {
    this.printerId = printerId;
  }

  public Integer getIsModifier() {
    return isModifier;
  }

  public void setIsModifier(Integer isModifier) {
    this.isModifier = isModifier;
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

  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
  }

  public Integer getTaxCategoryId() {
    return taxCategoryId;
  }

  public void setTaxCategoryId(Integer taxCategoryId) {
    this.taxCategoryId = taxCategoryId;
  }

  public Integer getIsPack() {
    return isPack;
  }

  public void setIsPack(Integer isPack) {
    this.isPack = isPack;
  }

  public Integer getIsTakeout() {
    return isTakeout;
  }

  public void setIsTakeout(Integer isTakeout) {
    this.isTakeout = isTakeout;
  }

  public Integer getHappyHoursId() {
    return happyHoursId;
  }

  public void setHappyHoursId(Integer happyHoursId) {
    this.happyHoursId = happyHoursId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
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

  public Integer getItemTemplateId() {
    return itemTemplateId;
  }

  public void setItemTemplateId(Integer itemTemplateId) {
    this.itemTemplateId = itemTemplateId;
  }

  public Integer getIndexId() {
    return indexId;
  }

  public void setIndexId(Integer indexId) {
    this.indexId = indexId;
  }

  public int getIsDiscount() {
    return isDiscount;
  }

  public void setIsDiscount(int isDiscount) {
    this.isDiscount = isDiscount;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public int getViewType() {
    return viewType;
  }

  public void setViewType(int viewType) {
    this.viewType = viewType;
  }

  @Override
  public String toString() {
    return "ItemDetail{" +
            "id=" + id +
            ", restaurantId=" + restaurantId +
            ", itemTemplateId=" + itemTemplateId +
            ", revenueId=" + revenueId +
            ", itemName='" + itemName + '\'' +
            ", itemDesc='" + itemDesc + '\'' +
            ", itemCode='" + itemCode + '\'' +
            ", imgUrl='" + imgUrl + '\'' +
            ", price='" + price + '\'' +
            ", itemType=" + itemType +
            ", printerId=" + printerId +
            ", isModifier=" + isModifier +
            ", itemMainCategoryId=" + itemMainCategoryId +
            ", itemCategoryId=" + itemCategoryId +
            ", isActive=" + isActive +
            ", taxCategoryId=" + taxCategoryId +
            ", isPack=" + isPack +
            ", isTakeout=" + isTakeout +
            ", happyHoursId=" + happyHoursId +
            ", userId=" + userId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", indexId=" + indexId +
            ", isDiscount=" + isDiscount +
            ", barcode='" + barcode + '\'' +
            ", itemCategoryName='" + itemCategoryName + '\'' +
            ", tag='" + tag + '\'' +
            ", viewType=" + viewType +
            '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (id == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(id);
    }
    if (restaurantId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(restaurantId);
    }
    if (itemTemplateId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(itemTemplateId);
    }
    if (revenueId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(revenueId);
    }
    dest.writeString(itemName);
    dest.writeString(itemDesc);
    dest.writeString(itemCode);
    dest.writeString(imgUrl);
    dest.writeString(price);
    if (itemType == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(itemType);
    }
    if (printerId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(printerId);
    }
    if (isModifier == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(isModifier);
    }
    if (itemMainCategoryId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(itemMainCategoryId);
    }
    if (itemCategoryId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(itemCategoryId);
    }
    if (isActive == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(isActive);
    }
    if (taxCategoryId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(taxCategoryId);
    }
    if (isPack == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(isPack);
    }
    if (isTakeout == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(isTakeout);
    }
    if (happyHoursId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(happyHoursId);
    }
    if (userId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(userId);
    }
    if (createTime == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(createTime);
    }
    if (updateTime == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(updateTime);
    }
    if (indexId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(indexId);
    }
    dest.writeInt(isDiscount);
    dest.writeString(barcode);
    dest.writeString(itemCategoryName);
  }
}