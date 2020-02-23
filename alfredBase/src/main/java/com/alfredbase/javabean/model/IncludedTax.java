package com.alfredbase.javabean.model;

import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;

public class IncludedTax {
	//	当id为0时表示不存在包含在itemPrice内部的税
	private int id;
	private String includedTaxName;
	private Tax tax;
	private TaxCategory taxCategory;
	private String typeName;
	protected IncludedTax(){
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIncludedTaxName() {
		return includedTaxName;
	}

	public void setIncludedTaxName(String includedTaxName) {
		this.includedTaxName = includedTaxName;
	}
	
	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public TaxCategory getTaxCategory() {
		return taxCategory;
	}

	public void setTaxCategory(TaxCategory taxCategory) {
		this.taxCategory = taxCategory;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "IncludedTax{" +
				"id=" + id +
				", includedTaxName='" + includedTaxName + '\'' +
				", tax=" + tax +
				", taxCategory=" + taxCategory +
				", typeName='" + typeName + '\'' +
				'}';
	}

}
