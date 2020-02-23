package com.alfredwaiter.javabean;

import java.util.List;

import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;

public class ItemCategoryAndDetails {
	private ItemCategory itemCategory;
	private List<ItemDetail> itemDetails;

	public ItemCategoryAndDetails() {
	}

	public ItemCategoryAndDetails(ItemCategory itemCategory,
			List<ItemDetail> itemDetails) {
		super();
		this.itemCategory = itemCategory;
		this.itemDetails = itemDetails;
	}

	public ItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}

	public List<ItemDetail> getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(List<ItemDetail> itemDetails) {
		this.itemDetails = itemDetails;
	}

	@Override
	public String toString() {
		return "ItemCategoryAndDetails [itemCategory=" + itemCategory
				+ ", itemDetails=" + itemDetails + "]";
	}
}
