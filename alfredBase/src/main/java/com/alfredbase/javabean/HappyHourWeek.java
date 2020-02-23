package com.alfredbase.javabean;

public class HappyHourWeek {
	private Integer id;
	private Integer happyHourId;
	private String week;
	private Long startTime;
	private Long endTime;
	private Integer isActive;

	public HappyHourWeek() {
	}

	public HappyHourWeek(Integer id, Integer happyHourId, String week,
			Long startTime, Long endTime, Integer isActive) {
		super();
		this.id = id;
		this.happyHourId = happyHourId;
		this.week = week;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isActive = isActive;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHappyHourId() {
		return happyHourId;
	}

	public void setHappyHourId(Integer happyHourId) {
		this.happyHourId = happyHourId;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "HappyHourWeek [id=" + id + ", happyHourId=" + happyHourId
				+ ", week=" + week + ", startTime=" + startTime + ", endTime="
				+ endTime + ", isActive=" + isActive + "]";
	}
}
