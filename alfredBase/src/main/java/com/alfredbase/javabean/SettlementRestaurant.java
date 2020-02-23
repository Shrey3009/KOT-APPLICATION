package com.alfredbase.javabean;

import java.io.Serializable;

public class SettlementRestaurant implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -873313576272913202L;

		private Integer id;

    private Integer restaurantId;

    private Integer mediaId;

    private Integer adjustmentsId;

    private Integer onlineServiceId;

    private Integer type;

    private String remarks;

    private Integer discriptionId;

    private String  otherPaymentId;

    public String getOtherPaymentId() {
        return otherPaymentId;
    }

    public void setOtherPaymentId(String otherPaymentId) {
        this.otherPaymentId = otherPaymentId;
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

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getAdjustmentsId() {
        return adjustmentsId;
    }

    public void setAdjustmentsId(Integer adjustmentsId) {
        this.adjustmentsId = adjustmentsId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
		
    public Integer getOnlineServiceId() {
        return onlineServiceId;
    }

    public void setOnlineServiceId(Integer onlineServiceId) {
        this.onlineServiceId = onlineServiceId;
		}

    public Integer getDiscriptionId() {
        return discriptionId;
    }

    public void setDiscriptionId(Integer discriptionId) {
        this.discriptionId = discriptionId;
    }

    @Override
    public String toString() {
        return "SettlementRestaurant{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", mediaId=" + mediaId +
                ", adjustmentsId=" + adjustmentsId +
                ", onlineServiceId=" + onlineServiceId +
                ", type=" + type +
                ", remarks='" + remarks + '\'' +
                ", discriptionId=" + discriptionId +
                ", otherPaymentId=" + otherPaymentId +
                '}';
    }
}