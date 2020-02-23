package com.alfredbase.javabean.model;

import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.store.Store;
import com.alfredbase.utils.BH;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocalRestaurantConfig {
	private static LocalRestaurantConfig instance;
	// 动态session类型
	private List<Integer> sessionConfigType;

	// 货币符号
	private String currencySymbol;

	private int currencySymbolType;

	private IncludedTax includedTax;

	private String roundType;

	private String[] discountOption = new String[] { "5", "10", "15", "20" };

	private List<String> sendFoodCardNumList;
	private  String formatType;

	public static LocalRestaurantConfig getInstance() {
		if (instance == null)
			instance = new LocalRestaurantConfig();
		return instance;
	}

	private LocalRestaurantConfig() {
		this.sessionConfigType = Arrays.asList(new Integer[] {
				ParamConst.SESSION_STATUS_BREAKFAST,
				ParamConst.SESSION_STATUS_LUNCH,
				ParamConst.SESSION_STATUS_DINNER });
		this.currencySymbol = "$";
		this.currencySymbolType = 100206;
		this.formatType="0.01";
		this.roundType = ParamConst.ROUND_NONE;
		this.includedTax = new IncludedTax();
		this.sendFoodCardNumList = new ArrayList<String>();
		for(int i = 1; i < 21; i++){
			this.sendFoodCardNumList.add(i+"");
		}
		List<String> stringList = Store.getObject(BaseApplication.getTopActivity(), Store.SEND_TABLE_NAME_LIST, new TypeToken<List<String>>(){}.getType());
		if(stringList != null)
			this.sendFoodCardNumList.addAll(stringList);
	}

	public List<Integer> getSessionConfigType() {
		if (sessionConfigType == null) {
			this.sessionConfigType = Arrays.asList(new Integer[] {
					ParamConst.SESSION_STATUS_BREAKFAST,
					ParamConst.SESSION_STATUS_LUNCH,
					ParamConst.SESSION_STATUS_DINNER });
		}
		return sessionConfigType;
	}

	public void setSessionConfigType(RestaurantConfig restaurantConfig) {

		if (TextUtils.isEmpty(restaurantConfig.getParaValue1())) {
			this.sessionConfigType = Arrays.asList(new Integer[] {
					ParamConst.SESSION_STATUS_BREAKFAST,
					ParamConst.SESSION_STATUS_LUNCH,
					ParamConst.SESSION_STATUS_DINNER });
		} else {
			String[] strArray = restaurantConfig.getParaValue1().split(
					ParamConst.PARA_VALUE_SPLIT);
			if (strArray.length == 0) {
				this.sessionConfigType = Arrays.asList(new Integer[] {
						ParamConst.SESSION_STATUS_BREAKFAST,
						ParamConst.SESSION_STATUS_LUNCH,
						ParamConst.SESSION_STATUS_DINNER });
			} else {
				Integer[] intArray = new Integer[strArray.length];
				for (int i = 0; i < strArray.length; i++) {
					intArray[i] = Integer.parseInt(strArray[i]);
				}
				Comparator comparator = new Comparator<Integer>() {

					@Override
					public int compare(Integer arg0, Integer arg1) {
						return arg0.compareTo(arg1);
					}
				};

				this.sessionConfigType = Arrays.asList(intArray);
				Collections.sort(this.sessionConfigType, comparator);
			}
		}

	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(RestaurantConfig restaurantConfig) {
		this.currencySymbol = restaurantConfig.getParaValue1();
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(RestaurantConfig restaurantConfig) {
		this.formatType = restaurantConfig.getParaValue3();
		BH.initFormart(formatType, getCurrencySymbol());
	}

	public int getCurrencySymbolType() {
		return currencySymbolType;
	}

	public void setCurrencySymbolType(RestaurantConfig restaurantConfig) {
		if(restaurantConfig != null && restaurantConfig.getParaId() != null) {
			this.currencySymbolType = restaurantConfig.getParaId();
		}else{
			this.currencySymbolType = 0;
		}
		//BH.initFormart(this.currencySymbolType >= 0);
	}

	public IncludedTax getIncludedTax() {
		return includedTax;
	}

	public void setIncludedTax(RestaurantConfig restaurantConfig) {
		if (!TextUtils.isEmpty(restaurantConfig.getParaValue1())) {
			this.includedTax.setIncludedTaxName(restaurantConfig
					.getParaValue2());
			this.includedTax.setId(Integer.valueOf(restaurantConfig
					.getParaValue1()));
			this.includedTax.setTypeName(restaurantConfig.getParaValue2());
			if(Integer.valueOf(restaurantConfig.getParaValue1()) > 0) {
				if(ParamConst.ITEM_PRICE_TYPE_VALUE2.equals(restaurantConfig.getParaValue2())){
					this.includedTax.setTax(null);
					this.includedTax.setTaxCategory(CoreData.getInstance().getTaxCategory(Integer.parseInt(restaurantConfig
							.getParaValue1())));
				}else {
					if (CoreData.getInstance().getTaxs() != null) {
						this.includedTax.setTax(
								CoreData.getInstance().getTax(
										Integer.parseInt(restaurantConfig
												.getParaValue1())));
						this.includedTax.setTaxCategory(null);
					}
				}
			}
		}

	}

	public String getRoundType() {
		return roundType;
	}

	public void setRoundType(RestaurantConfig restaurantConfig) {
		this.roundType = restaurantConfig.getParaValue1();
	}

	public String[] getDiscountOption() {
		return discountOption;
	}

	public void setDiscountOption(RestaurantConfig restaurantConfig) {
		if (!TextUtils.isEmpty(restaurantConfig.getParaValue1())) {
			String[] discountOption = restaurantConfig.getParaValue1().split(
					ParamConst.PARA_VALUE_SPLIT);
			if (discountOption.length > 0) {
				this.discountOption = discountOption;
			}
		}

	}

	public List<String> getSendFoodCardNumList() {
		return sendFoodCardNumList;
	}

	public void setSendFoodCardNumList(RestaurantConfig restaurantConfig) {
		sendFoodCardNumList.clear();
		try {
			if(!TextUtils.isEmpty(restaurantConfig.getParaValue1()) && !TextUtils.isEmpty(restaurantConfig.getParaValue2())){
				String[] ids = restaurantConfig.getParaValue2().split(ParamConst.PARA_VALUE_SPLIT);
				String[] names = restaurantConfig.getParaValue1().split(ParamConst.PARA_VALUE_SPLIT);
				for (String name: names) {
					for (String id: ids) {
						int idIndex = Integer.parseInt(id);
						for(int i = 1; i < idIndex + 1; i++)
							this.sendFoodCardNumList.add(name+i);
					}
				}
			}
			List<String> stringList = Store.getObject(BaseApplication.getTopActivity(), Store.SEND_TABLE_NAME_LIST, new TypeToken<List<String>>(){}.getType());
			if(stringList != null)
				this.sendFoodCardNumList.addAll(stringList);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
