package com.alfredbase.javabean;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO :
 * Param :
 * Created by yangk on 2019/2/8.
 *
 * @ return :
 */
public class PromotionWeek implements Serializable {

    private static final long serialVersionUID = -5959201579346446680L;
    private Integer id;

    private  Integer promotionId;

    private Integer week;

    private String startTime;

    private String endTime;

    private Integer isActive;

    private long createTime;

    private long updateTime;
    private Integer  promotionDateInfoId;

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

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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

    public Integer getPromotionDateInfoId() {
        return promotionDateInfoId;
    }

    public void setPromotionDateInfoId(Integer promotionDateInfoId) {
        this.promotionDateInfoId = promotionDateInfoId;
    }

    @Override
    public String toString() {
        return "PromotionWeek{" +
                "id=" + id +
                ", promotionId=" + promotionId +
                ", week=" + week +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isActive=" + isActive +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", promotionDateInfoId=" + promotionDateInfoId +
                '}';
    }
}
