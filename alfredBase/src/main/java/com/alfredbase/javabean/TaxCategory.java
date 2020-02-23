package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

public class TaxCategory {
	private Integer id;

	private Integer companyId;

	private Integer restaurantId;

	private Integer taxCategoryId;

	private String taxCategoryName;

	private Integer taxId;
	/**
	 * 0:onvalue,1ontax
	 */
	private Integer taxOn;

	private Integer taxOnId;

	private Integer index;
	/**
	 * -1删除，0禁用，1正常
	 */
	private Integer status;

	public TaxCategory() {
	}

	public TaxCategory(Integer id, Integer companyId, Integer restaurantId,
			Integer taxCategoryId, String taxCategoryName, Integer taxId,
			Integer taxOn, Integer taxOnId, Integer index, Integer status) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.restaurantId = restaurantId;
		this.taxCategoryId = taxCategoryId;
		this.taxCategoryName = taxCategoryName;
		this.taxId = taxId;
		this.taxOn = taxOn;
		this.taxOnId = taxOnId;
		this.index = index;
		this.status = status;
	}

	public Integer getId() {
		if (CommonUtil.isNull(id))
			return 0;
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		if (CommonUtil.isNull(companyId))
			return 0;
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getRestaurantId() {
		if (CommonUtil.isNull(restaurantId))
			return 0;
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getTaxCategoryId() {
		if (CommonUtil.isNull(taxCategoryId))
			return 0;
		return taxCategoryId;
	}

	public void setTaxCategoryId(Integer taxCategoryId) {
		this.taxCategoryId = taxCategoryId;
	}

	public String getTaxCategoryName() {
		if (CommonUtil.isNull(taxCategoryName))
			return "";
		return taxCategoryName;
	}

	public void setTaxCategoryName(String taxCategoryName) {
		this.taxCategoryName = taxCategoryName == null ? null : taxCategoryName
				.trim();
	}

	public Integer getTaxId() {
		if (CommonUtil.isNull(taxId))
			return 0;
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getTaxOn() {
		if (CommonUtil.isNull(taxOn))
			return 0;
		return taxOn;
	}

	public void setTaxOn(Integer taxOn) {
		this.taxOn = taxOn;
	}

	public Integer getTaxOnId() {
		if (CommonUtil.isNull(taxOnId))
			return 0;
		return taxOnId;
	}

	public void setTaxOnId(Integer taxOnId) {
		this.taxOnId = taxOnId;
	}

	public Integer getIndex() {
		if (CommonUtil.isNull(index))
			return 0;
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getStatus() {
		if (CommonUtil.isNull(status))
			return 0;
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TaxCategory [id=" + id + ", companyId=" + companyId
				+ ", restaurantId=" + restaurantId + ", taxCategoryId="
				+ taxCategoryId + ", taxCategoryName=" + taxCategoryName
				+ ", taxId=" + taxId + ", taxOn=" + taxOn + ", taxOnId="
				+ taxOnId + ", index=" + index + ", status=" + status + "]";
	}

}