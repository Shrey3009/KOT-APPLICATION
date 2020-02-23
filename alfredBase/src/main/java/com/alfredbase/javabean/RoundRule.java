package com.alfredbase.javabean;

public class RoundRule {
	private Integer id;

	private String country;

	private Integer status;

	/**
	 * 四舍五入规则(ROUND_5CENTS精确小数点后两位四舍五入、ROUND_10CENTS精确小数点后一位四舍五入、
	 * ROUND_1DOLLAR精确到小数点前一位四舍五入、ROUND_NONE不做处理)
	 */

	private String ruleType;

	public RoundRule() {
	}

	public RoundRule(Integer id, String country, Integer status, String ruleType) {
		super();
		this.id = id;
		this.country = country;
		this.status = status;
		this.ruleType = ruleType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	@Override
	public String toString() {
		return "RoundRule [id=" + id + ", country=" + country + ", status="
				+ status + ", ruleType=" + ruleType + "]";
	}

}