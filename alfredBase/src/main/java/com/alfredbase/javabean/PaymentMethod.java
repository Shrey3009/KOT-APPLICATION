package com.alfredbase.javabean;

import java.io.Serializable;

/**
 * Created by yangk on 2018-05-23.
 */
public class PaymentMethod implements Serializable {
    private static final long serialVersionUID = -6968457809578569954L;

    private Integer id;

    private String nameCh;

    private String nameEn;

    private String nameOt;

    private String logoMd;

    private String logoSm;

    private int payType;

    private Integer restaurantId;

    private int isTax;

    private int isDiscount;

    private int isAdmin;//是否验证

    private int isMsg;

    private int isMsgRequire;

    private int isPart;//0 不计税  、1 计税

    private double partAcount;

    private int status;

    private String description;

    private Long createTime;

    private Long updateTime;

    private  Long paymentTypeId;

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

    public Long getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }





   public int getIsverify() {
        return isverify;
    }

    public void setIsverify(int isverify) {
        this.isverify = isverify;
    }

    private int isverify;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameOt() {
        return nameOt;
    }

    public void setNameOt(String nameOt) {
        this.nameOt = nameOt;
    }

    public String getLogoMd() {
        return logoMd;
    }

    public void setLogoMd(String logoMd) {
        this.logoMd = logoMd;
    }

    public String getLogoSm() {
        return logoSm;
    }

    public void setLogoSm(String logoSm) {
        this.logoSm = logoSm;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getIsTax() {
        return isTax;
    }

    public void setIsTax(int isTax) {
        this.isTax = isTax;
    }

    public int getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(int isDiscount) {
        this.isDiscount = isDiscount;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getIsMsg() {
        return isMsg;
    }

    public void setIsMsg(int isMsg) {
        this.isMsg = isMsg;
    }

    public int getIsMsgRequire() {
        return isMsgRequire;
    }

    public void setIsMsgRequire(int isMsgRequire) {
        this.isMsgRequire = isMsgRequire;
    }

    public int getIsPart() {
        return isPart;
    }

    public void setIsPart(int isPart) {
        this.isPart = isPart;
    }

    public double getPartAcount() {
        return partAcount;
    }

    public void setPartAcount(double partAcount) {
        this.partAcount = partAcount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
