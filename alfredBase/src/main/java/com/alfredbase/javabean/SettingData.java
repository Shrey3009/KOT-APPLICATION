package com.alfredbase.javabean;

public class SettingData {
	private Integer id;
	private String logoUrl;
	private String logoString;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getLogoString() {
		return logoString;
	}
	public void setLogoString(String logoString) {
		this.logoString = logoString;
	}
	@Override
	public String toString() {
		return "SettingData [id=" + id + ", logoUrl=" + logoUrl
				+ ", logoString=" + logoString + "]";
	}
}
