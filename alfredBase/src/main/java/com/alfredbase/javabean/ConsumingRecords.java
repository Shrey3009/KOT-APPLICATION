package com.alfredbase.javabean;

import java.io.Serializable;

/**
 * Created by Alex on 16/10/20.
 */

public class ConsumingRecords implements Serializable{

    private Integer cardId;

    private Integer restId;

    private Integer staffId;

    private Integer consumingType;

    private Integer fromType;

    private String consumingAmount;

    private long consumingTime;

    private long businessDate;

    private int payTypeId;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getRestId() {
        return restId;
    }

    public void setRestId(Integer restId) {
        this.restId = restId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getConsumingType() {
        return consumingType;
    }

    public void setConsumingType(Integer consumingType) {
        this.consumingType = consumingType;
    }

    public Integer getFromType() {
        return fromType;
    }

    public void setFromType(Integer fromType) {
        this.fromType = fromType;
    }

    public String getConsumingAmount() {
        return consumingAmount;
    }

    public void setConsumingAmount(String consumingAmount) {
        this.consumingAmount = consumingAmount;
    }

    public long getConsumingTime() {
        return consumingTime;
    }

    public void setConsumingTime(long consumingTime) {
        this.consumingTime = consumingTime;
    }

    public long getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(long businessDate) {
        this.businessDate = businessDate;
    }

    public int getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(int payTypeId) {
        this.payTypeId = payTypeId;
    }

    @Override
    public String toString() {
        return "ConsumingRecords{" +
                "cardId=" + cardId +
                ", restId=" + restId +
                ", staffId=" + staffId +
                ", consumingType=" + consumingType +
                ", fromType=" + fromType +
                ", consumingAmount='" + consumingAmount + '\'' +
                ", consumingTime=" + consumingTime +
                ", businessDate=" + businessDate +
                ", payTypeId=" + payTypeId +
                '}';
    }
}
