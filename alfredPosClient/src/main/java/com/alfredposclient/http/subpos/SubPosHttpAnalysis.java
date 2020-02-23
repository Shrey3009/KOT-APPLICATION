package com.alfredposclient.http.subpos;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Base64;

import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.HappyHour;
import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.SettingData;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserRestaurant;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.Store;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.GeneralSQL;
import com.alfredbase.store.sql.HappyHourSQL;
import com.alfredbase.store.sql.HappyHourWeekSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.ItemHappyHourSQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.ItemModifierSQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.PrinterGroupSQL;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.store.sql.RestaurantConfigSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.RevenueCenterSQL;
import com.alfredbase.store.sql.SettingDataSQL;
import com.alfredbase.store.sql.SettlementRestaurantSQL;
import com.alfredbase.store.sql.SubPosBeanSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.TaxCategorySQL;
import com.alfredbase.store.sql.TaxSQL;
import com.alfredbase.store.sql.UserRestaurantSQL;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.utils.BitmapUtil;
import com.alfredposclient.activity.kioskactivity.subpos.SubPosLogin;
import com.alfredposclient.global.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SubPosHttpAnalysis {
	public static final String TAG = SubPosHttpAnalysis.class.getSimpleName();
	public static void login(int resultCode, String responseBody, Handler handler) {
		Gson gson = new Gson();
		try {
			JSONObject object = new JSONObject(responseBody);
			User user = gson.fromJson(object.getString("user"), User.class);
			App.instance.setUser(user);
			Long businessDate = object.optLong("businessDate");
			Store.putLong(App.instance, Store.BUSINESS_DATE, businessDate);
			Store.putLong(App.instance, Store.LAST_BUSINESSDATE, businessDate);
			App.instance.setBusinessDate(businessDate);
			App.instance.setLastBusinessDate(businessDate);

			SubPosBean subPosBean = gson.fromJson(object.getString("subPosBean"), SubPosBean.class);
			if(subPosBean != null) {
				SubPosBeanSQL.updateSubPosBean(subPosBean);
			}
			if(resultCode == ResultCode.SESSION_HAS_CHANGE){
				GeneralSQL.deleteAllDataInSubPos();
			}
			App.instance.setSubPosBean(subPosBean);
			handler.sendEmptyMessage(resultCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void updateAllData(String responseBody, Handler handler) {
		Gson gson = new Gson();
		try {
			JSONObject object = new JSONObject(responseBody);
			List<User> users = gson.fromJson(object.getString("users"),
					new TypeToken<ArrayList<User>>() {
					}.getType());
			if(users != null && users.size() > 0){
				UserSQL.deleteAllUser();
				UserSQL.addUsers(users);
				CoreData.getInstance().setUsers(users);
			}
			final Restaurant restaurant = gson.fromJson(object.getString("restaurant"),
					Restaurant.class);
			if(restaurant != null){
				RestaurantSQL.deleteAllRestaurant();
				RestaurantSQL.addRestaurant(restaurant);
				if (restaurant.getLogoUrl() != null) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							ImageLoader imageLoader = ImageLoader.getInstance();
							// encode bitmap by base64
							Bitmap bmp = null;
							try {
								bmp = imageLoader.loadImageSync(restaurant
										.getLogoUrl());
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (bmp != null) {
								Bitmap rzbmp = BitmapUtil.getResizedBitmap(bmp,
										200, 200);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								rzbmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
								byte[] imgByte = baos.toByteArray();
								String imgString = Base64.encodeToString(imgByte,
										Base64.DEFAULT);
								SettingData settingData = new SettingData();
								settingData.setId(CommonSQL
										.getNextSeq(TableNames.SettingData));
								settingData.setLogoUrl(restaurant.getLogoUrl());
								settingData.setLogoString(imgString);
								SettingDataSQL.add(settingData);
							}
						}
					}).start();

				}
				CoreData.getInstance().setRestaurant(restaurant);
			}
			RevenueCenter revenueCenter = gson.fromJson(object.getString("revenueCenter"),
					RevenueCenter.class);
			if(revenueCenter != null){
				RevenueCenterSQL.deleteAllRevenueCenter();
				RevenueCenterSQL.addRevenueCenter(revenueCenter);
				List<RevenueCenter> revenueCenters = new ArrayList<>();
				revenueCenters.add(revenueCenter);
				CoreData.getInstance().setRevenueCenters(revenueCenters);
				App.instance.setRevenueCenter(revenueCenter);
				Store.saveObject(App.instance, Store.CURRENT_REVENUE_CENTER,
						revenueCenter);
				MainPosInfo mainPosInfo = App.instance.getMainPosInfo();
				if (mainPosInfo != null) {
					mainPosInfo.setIsKiosk(revenueCenter.getIsKiosk());
					App.instance.setMainPosInfo(mainPosInfo);
					Store.saveObject(App.instance, Store.MAINPOSINFO, mainPosInfo);
				}
			}

			List<ItemMainCategory> itemMainCategories = gson.fromJson(object.getString("itemMainCategories"),
					new TypeToken<ArrayList<ItemMainCategory>>() {
					}.getType());
			if(itemMainCategories != null && itemMainCategories.size() > 0){
				ItemMainCategorySQL.deleteAllItemMainCategory();
				ItemMainCategorySQL.addItemMainCategory(itemMainCategories);
				CoreData.getInstance().setItemMainCategories(itemMainCategories);
			}
			List<ItemCategory> itemCategories = gson.fromJson(object.getString("itemCategories"),
					new TypeToken<ArrayList<ItemCategory>>() {
					}.getType());
			if(itemCategories != null && itemCategories.size() > 0){
				ItemCategorySQL.deleteAllItemCategory();
				ItemCategorySQL.addItemCategoryList(itemCategories);
				CoreData.getInstance().setItemCategories(itemCategories);
			}
			List<ItemDetail> itemDetails = gson.fromJson(object.getString("itemDetails"),
					new TypeToken<ArrayList<ItemDetail>>() {
					}.getType());
			if(itemDetails != null && itemDetails.size() > 0){
				ItemDetailSQL.deleteAllItemDetail();
				ItemDetailSQL.addItemDetailList(itemDetails);
				CoreData.getInstance().setItemDetails(itemDetails);
			}
			List<Modifier> modifiers= gson.fromJson(object.getString("modifiers"),
					new TypeToken<ArrayList<Modifier>>() {
					}.getType());
			if(modifiers != null && modifiers.size() > 0){
				ModifierSQL.deleteAllModifier();
				ModifierSQL.addModifierList(modifiers);
				CoreData.getInstance().setModifierList(modifiers);
			}
			List<Printer> printers = gson.fromJson(object.getString("printers"),
					new TypeToken<ArrayList<Printer>>() {
					}.getType());
			if(printers != null && printers.size() > 0 ){
				PrinterSQL.deleteAllPrinter();
				PrinterSQL.addPrinters(printers);
				CoreData.getInstance().setPrinters(printers);
			}
			List<TableInfo> tableInfos = gson.fromJson(object.getString("tableInfos"),
					new TypeToken<ArrayList<TableInfo>>() {
					}.getType());
			if(tableInfos != null && tableInfos.size() > 0){
				TableInfoSQL.deleteAllTableInfo();
				TableInfoSQL.addTablesList(tableInfos);
			}
			List<PlaceInfo> placeInfos = gson.fromJson(object.getString("placeInfos"),
					new TypeToken<ArrayList<PlaceInfo>>() {
					}.getType());
			if(placeInfos != null && placeInfos.size() > 0){
				PlaceInfoSQL.deleteAllPlaceInfo();
				PlaceInfoSQL.addPlaceInfoList(placeInfos);
			}
			List<ItemModifier> itemModifiers = gson.fromJson(object.getString("itemModifiers"),
					new TypeToken<ArrayList<ItemModifier>>() {
					}.getType());
			if(itemModifiers != null && itemModifiers.size() > 0){
				ItemModifierSQL.deleteAllItemModifier();
				ItemModifierSQL.addItemModifierList(itemModifiers);
				CoreData.getInstance().setItemModifiers(itemModifiers);
			}
			List<TaxCategory> taxCategories = gson.fromJson(object.getString("taxCategories"),
					new TypeToken<ArrayList<TaxCategory>>() {
					}.getType());
			if(taxCategories != null && taxCategories.size() > 0){
				TaxCategorySQL.deleteAllTaxCategory();
				TaxCategorySQL.addTaxCategorys(taxCategories);
				CoreData.getInstance().setTaxCategories(taxCategories);
			}
			List<Tax> taxes = gson.fromJson(object.getString("taxes"),
					new TypeToken<ArrayList<Tax>>() {
					}.getType());
			if(taxes != null && taxes.size() > 0){
				TaxSQL.deleteAllTax();
				TaxSQL.addTaxs(taxes);
				CoreData.getInstance().setTaxs(taxes);
			}
			List<ItemHappyHour> itemHappyHours = gson.fromJson(object.getString("itemHappyHours"),
					new TypeToken<ArrayList<ItemHappyHour>>() {
					}.getType());
			if(itemHappyHours != null && itemHappyHours.size() > 0){
				ItemHappyHourSQL.deleteAllItemHappyHour();
				ItemHappyHourSQL.addItemHappyHourList(itemHappyHours);
				CoreData.getInstance().setItemHappyHours(itemHappyHours);
			}
			List<HappyHourWeek> happyHourWeeks = gson.fromJson(object.getString("happyHourWeeks"),
					new TypeToken<ArrayList<HappyHourWeek>>() {
					}.getType());
			if(happyHourWeeks != null && happyHourWeeks.size() > 0){
				HappyHourWeekSQL.deleteAllHappyHourWeek();
				HappyHourWeekSQL.addHappyHourWeekList(happyHourWeeks);
				CoreData.getInstance().setHappyHourWeeks(happyHourWeeks);
			}
			List<HappyHour> happyHours = gson.fromJson(object.getString("happyHours"),
					new TypeToken<ArrayList<HappyHour>>() {
					}.getType());
			if(happyHours != null && happyHours.size() > 0){
				HappyHourSQL.deleteAllHappyHour();
				HappyHourSQL.addHappyHourList(happyHours);
				CoreData.getInstance().setHappyHours(happyHours);
			}
			List<RestaurantConfig> restaurantConfigs = gson.fromJson(object.getString("restaurantConfigs"),
					new TypeToken<ArrayList<RestaurantConfig>>() {
					}.getType());
			if(restaurantConfigs != null && restaurantConfigs.size() > 0){
				RestaurantConfigSQL.deleteAllRestaurantConfig();
				RestaurantConfigSQL.addRestaurantConfigs(restaurantConfigs);
				CoreData.getInstance().setRestaurantConfigs(restaurantConfigs);
                App.instance.setLocalRestaurantConfig(restaurantConfigs);
			}
			List<PrinterGroup> printerGroups = gson.fromJson(object.getString("printerGroups"),
					new TypeToken<ArrayList<PrinterGroup>>() {
					}.getType());
			if(printerGroups != null && printerGroups.size() > 0){
				PrinterGroupSQL.deletePrinter();
				PrinterGroupSQL.addPrinterGroups(printerGroups);
				CoreData.getInstance().setPrinterGroups(printerGroups);
			}
			List<UserRestaurant> userRestaurants = gson.fromJson(object.getString("userRestaurants"),
					new TypeToken<ArrayList<UserRestaurant>>() {
					}.getType());
			if(userRestaurants != null && userRestaurants.size() > 0){
				UserRestaurantSQL.deleteAllUserRestaurant();
				UserRestaurantSQL.addUsers(userRestaurants);
				CoreData.getInstance().setUserRestaurant(userRestaurants);
			}
//			List<LocalDevice> localDevices = gson.fromJson(object.getString("localDevices"),
//					new TypeToken<ArrayList<LocalDevice>>() {
//					}.getType());
//			if(localDevices != null && localDevices.size() > 0){
//				LocalDeviceSQL.deleteAllLocalDevice();
//				LocalDeviceSQL.addLocalDeviceList(localDevices);
//			}
			List<PaymentMethod> paymentMethods = gson.fromJson(object.getString("paymentMethods"),
					new TypeToken<ArrayList<PaymentMethod>>() {
					}.getType());
			if(paymentMethods != null && paymentMethods.size() > 0){
				PaymentMethodSQL.deleteAllPaymentMethod();
				PaymentMethodSQL.addPaymentMethod(paymentMethods);
				CoreData.getInstance().setPamentMethodList(paymentMethods);
			}
			List<SettlementRestaurant> settlementRestaurants = gson.fromJson(object.getString("settlementRestaurants"),
					new TypeToken<ArrayList<SettlementRestaurant>>() {
					}.getType());
			if(settlementRestaurants != null && settlementRestaurants.size() > 0){
				SettlementRestaurantSQL.deleteAllSettlementRestaurant();
				SettlementRestaurantSQL.addSettlementRestaurant(settlementRestaurants);
				CoreData.getInstance().setSettlementRestaurant(settlementRestaurants);
			}
			SessionStatus sessionStatus = gson.fromJson(object.getString("sessionStatus"), SessionStatus.class);
			sessionStatus.setTime(System.currentTimeMillis());
			Store.saveObject(App.instance, Store.SESSION_STATUS, sessionStatus);
			App.instance.setSessionStatus(sessionStatus);
			handler.sendEmptyMessage(SubPosLogin.UPDATE_ALL_DATA_SUCCESS);
			return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		handler.sendEmptyMessage(SubPosLogin.UPDATE_ALL_DATA_FAILURE);
	}
	public static Order getOrder(String responseBody) {
		Gson gson = new Gson();
		try {
			JSONObject object = new JSONObject(responseBody);
			Order order = gson.fromJson(object.getString("order"), Order.class);
			OrderSQL.update(order);
			return order;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Order uploadOrder(String responseBody) {
		Gson gson = new Gson();
		try {
			JSONObject object = new JSONObject(responseBody);
			Order order = gson.fromJson(object.getString("order"), Order.class);
            List<OrderBill> orderBills = gson.fromJson(object.getString("orderBills"), new TypeToken<List<OrderBill>>(){}.getType());
            for(OrderBill o : orderBills){
                OrderBillSQL.add(o);
            }
			OrderSQL.update(order);
			return order;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}


}
