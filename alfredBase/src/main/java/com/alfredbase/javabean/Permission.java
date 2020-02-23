package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

public class Permission {
	private Integer id;

	private Integer permissId;

	private String permissName;

	private String permissDesc;

	private String permissRule;

	private String permModel;

	private Integer permisLevel;

	public Integer getId() {
		if (CommonUtil.isNull(id))
			return 0;
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPermissId() {
		if (CommonUtil.isNull(permissId))
			return 0;
		return permissId;
	}

	public void setPermissId(Integer permissId) {
		this.permissId = permissId;
	}

	public String getPermissName() {
		if (CommonUtil.isNull(permissName))
			return "";
		return permissName;
	}

	public void setPermissName(String permissName) {
		this.permissName = permissName == null ? null : permissName.trim();
	}

	public String getPermissDesc() {
		if (CommonUtil.isNull(permissDesc))
			return "";
		return permissDesc;
	}

	public void setPermissDesc(String permissDesc) {
		this.permissDesc = permissDesc == null ? null : permissDesc.trim();
	}

	public String getPermissRule() {
		if (CommonUtil.isNull(permissRule))
			return "";
		return permissRule;
	}

	public void setPermissRule(String permissRule) {
		this.permissRule = permissRule == null ? null : permissRule.trim();
	}

	public String getPermModel() {
		if (CommonUtil.isNull(permModel))
			return "";
		return permModel;
	}

	public void setPermModel(String permModel) {
		this.permModel = permModel == null ? null : permModel.trim();
	}

	public Integer getPermisLevel() {
		if (CommonUtil.isNull(permisLevel))
			return 0;
		return permisLevel;
	}

	public void setPermisLevel(Integer permisLevel) {
		this.permisLevel = permisLevel;
	}
}