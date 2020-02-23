package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

public class UserPermission {
	private Integer id;

	private Integer userId;

	private Integer permissionId;

	private String rule;

	public Integer getId() {
		if (CommonUtil.isNull(id))
			return 0;
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		if (CommonUtil.isNull(userId))
			return 0;
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPermissionId() {
		if (CommonUtil.isNull(permissionId))
			return 0;
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getRule() {
		if (CommonUtil.isNull(rule))
			return "";
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule == null ? null : rule.trim();
	}
}