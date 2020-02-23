package com.alfredbase.javabean;

public class HappyHour {
	private Integer id;
	private Integer restaurantId;
	private String happy_hour_name;
	private Integer isActive;
	private Long createTime;
	private Long updateTime;

	public HappyHour() {
	}

	public HappyHour(Integer id, Integer restaurantId, String happy_hour_name,
			Integer isActive, Long createTime, Long updateTime) {
		super();
		this.id = id;
		this.restaurantId = restaurantId;
		this.happy_hour_name = happy_hour_name;
		this.isActive = isActive;
		this.createTime = createTime;
		this.updateTime = updateTime;
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

	public String getHappy_hour_name() {
		return happy_hour_name;
	}

	public void setHappy_hour_name(String happy_hour_name) {
		this.happy_hour_name = happy_hour_name;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
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

	@Override
	public String toString() {
		return "HappyHour [id=" + id + ", restaurantId=" + restaurantId
				+ ", happy_hour_name=" + happy_hour_name + ", isActive="
				+ isActive + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
}
