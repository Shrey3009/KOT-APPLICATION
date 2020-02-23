package com.alfredposclient.http;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import android.util.Base64;

import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.BohHoldSettlement;
import com.alfredbase.javabean.ConsumingRecords;
import com.alfredbase.javabean.HappyHour;
import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.ItemPromotion;
import com.alfredbase.javabean.LoginQrPayment;
import com.alfredbase.javabean.LoginResult;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.MonthlyPLUReport;
import com.alfredbase.javabean.MonthlySalesReport;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.Promotion;
import com.alfredbase.javabean.PromotionOrder;
import com.alfredbase.javabean.PromotionWeek;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.SettingData;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserRestaurant;
import com.alfredbase.javabean.UserTimeSheet;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.javabean.temporaryforapp.AppOrderDetail;
import com.alfredbase.javabean.temporaryforapp.AppOrderDetailTax;
import com.alfredbase.javabean.temporaryforapp.AppOrderModifier;
import com.alfredbase.javabean.temporaryforapp.TempModifierDetail;
import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.javabean.temporaryforapp.TempOrderDetail;
import com.alfredbase.store.Store;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.ConsumingRecordsSQL;
import com.alfredbase.store.sql.GeneralSQL;
import com.alfredbase.store.sql.HappyHourSQL;
import com.alfredbase.store.sql.HappyHourWeekSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.ItemHappyHourSQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.ItemModifierSQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.PrinterGroupSQL;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.store.sql.PromotionItemSQL;
import com.alfredbase.store.sql.PromotionOrderSQL;
import com.alfredbase.store.sql.PromotionSQL;
import com.alfredbase.store.sql.PromotionWeekSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.store.sql.ReportDaySalesSQL;
import com.alfredbase.store.sql.ReportDayTaxSQL;
import com.alfredbase.store.sql.ReportHourlySQL;
import com.alfredbase.store.sql.ReportPluDayComboModifierSQL;
import com.alfredbase.store.sql.ReportPluDayItemSQL;
import com.alfredbase.store.sql.RestaurantConfigSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.RevenueCenterSQL;
import com.alfredbase.store.sql.SettingDataSQL;
import com.alfredbase.store.sql.SettlementRestaurantSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.TaxCategorySQL;
import com.alfredbase.store.sql.TaxSQL;
import com.alfredbase.store.sql.UserRestaurantSQL;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderDetailTaxSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderModifierSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.store.sql.temporaryforapp.TempModifierDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderDetailSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderSQL;
import com.alfredbase.utils.BitmapUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.global.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HttpAnalysis {
    public static final String TAG = HttpAnalysis.class.getSimpleName();

    public static LoginResult login(int statusCode, Header[] headers,
                                    byte[] responseBody) {
        LoginResult result = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            result = gson.fromJson(object.toString(), LoginResult.class);
            Store.saveObject(App.instance, Store.LOGIN_RESULT, result);
            CoreData.getInstance().setLoginResult(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<User> getUsers(int statusCode, Header[] headers,
                                      byte[] responseBody) {
        List<User> result = null;
        List<UserRestaurant> urlist = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            result = gson.fromJson(object.getString("userList"),
                    new TypeToken<ArrayList<User>>() {
                    }.getType());
            CoreData.getInstance().setUsers(result);
            UserSQL.deleteAllUser();
            UserSQL.addUsers(result);

            urlist = gson.fromJson(object.getString("userRestaurantList"),
                    new TypeToken<ArrayList<UserRestaurant>>() {
                    }.getType());
            CoreData.getInstance().setUserRestaurant(urlist);
            UserRestaurantSQL.deleteAllUserRestaurant();
            UserRestaurantSQL.addUsers(urlist);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void getRestaurantInfo(int statusCode, Header[] headers,
                                         byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<RevenueCenter> revenueCenters = gson.fromJson(
                    object.getString("revenueList"),
                    new TypeToken<ArrayList<RevenueCenter>>() {
                    }.getType());

            CoreData.getInstance().setRevenueCenters(revenueCenters);
            RevenueCenter currentRevenueCenter = Store.getObject(App.instance, Store.CURRENT_REVENUE_CENTER,
                    RevenueCenter.class);
            if (currentRevenueCenter != null) {
                for (RevenueCenter revenueCenter : revenueCenters) {
                    if (revenueCenter.getId().intValue() == currentRevenueCenter.getId().intValue()) {
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
                }
            }

            RevenueCenterSQL.deleteAllRevenueCenter();
            RevenueCenterSQL.addRevenueCenters(revenueCenters);

            List<Printer> printers = gson.fromJson(
                    object.getString("printerList"),
                    new TypeToken<ArrayList<Printer>>() {
                    }.getType());

            //region set dummy data
//            for (Printer printer : printers) {
//                if ("Kitchen 1".equalsIgnoreCase(printer.getPrinterName()) ||
//                        "Kitchen 2".equalsIgnoreCase(printer.getPrinterName()) ||
//                        "Bar".equalsIgnoreCase(printer.getPrinterName())) {
//                    printer.setPrinterUsageType(Printer.KDS_SUB);
//                } else if ("EX Kitchen".equalsIgnoreCase(printer.getPrinterName())) {
//                    printer.setPrinterUsageType(Printer.KDS_EXPEDITER);
//                } else if ("Summary Printer".equalsIgnoreCase(printer.getPrinterName())) {
//                    printer.setPrinterUsageType(Printer.KDS_SUMMARY);
//                } else {
//                    printer.setPrinterUsageType(Printer.KDS_NORMAL);
//                }
//            }

//            Printer printer = new Printer();
//            printer.setId(123);
//            printer.setPrinterUsageType(Printer.KDS_BALANCER);
//            printer.setPrinterName("Balancer");
//            printer.setType(0);
//            printer.setCompanyId(0);
//            printer.setIsCashdrawer(0);
//            printer.setIsLablePrinter(0);
//            printer.setRestaurantId(0);
//            printers.add(printer);
            //endregion

            CoreData.getInstance().setPrinters(printers);
            PrinterSQL.deleteAllPrinter();
            PrinterSQL.addPrinters(printers);

            List<PrinterGroup> printerGroups = gson.fromJson(
                    object.getString("printerGroupList"),
                    new TypeToken<ArrayList<PrinterGroup>>() {
                    }.getType());

            CoreData.getInstance().setPrinterGroups(printerGroups);
            PrinterGroupSQL.deletePrinter();
            PrinterGroupSQL.addPrinterGroups(printerGroups);

            final Restaurant restaurant = gson.fromJson(
                    object.getString("restaurant"), Restaurant.class);
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
                            rzbmp.compress(CompressFormat.JPEG, 100, baos);
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

            if (restaurant != null) {
                Store.putInt(App.instance, Store.REPORT_ORDER_TIMELY, restaurant.getReportOrderTimely());
            }
            CoreData.getInstance().setRestaurant(restaurant);
            RestaurantSQL.deleteAllRestaurant();
            RestaurantSQL.addRestaurant(restaurant);

//			RoundRule roundRule = gson.fromJson(object.getString("roundRule"),
//					RoundRule.class);
//			CoreData.getInstance().setRoundRule(roundRule);
//			RoundRuleSQL.deleteAllRoundRule();
//			RoundRuleSQL.update(roundRule);

            List<RestaurantConfig> restaurantConfigs = gson.fromJson(object.getString("configList"),
                    new TypeToken<ArrayList<RestaurantConfig>>() {
                    }.getType());
            CoreData.getInstance().setRestaurantConfigs(restaurantConfigs);
            RestaurantConfigSQL.deleteAllRestaurantConfig();
            RestaurantConfigSQL.addRestaurantConfigs(restaurantConfigs);
            App.instance.setLocalRestaurantConfig(restaurantConfigs);
//			App.instance.setSessionConfigType(CoreData.getInstance().getRestaurantConfigs());
//			App.instance.setRoundType(restaurantConfigs);
//			App.instance.setCurrencySymbol(restaurantConfigs);
            LogUtil.i(TAG, restaurantConfigs + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//	public static void getPlaceInfo(int statusCode, Header[] headers,
//			byte[] responseBody) {
//		try {
//			JSONObject object = new JSONObject(new String(responseBody));
//			Gson gson = new Gson();
//
//			List<Places> places = gson.fromJson(object.getString("placeList"),
//					new TypeToken<ArrayList<Places>>() {
//					}.getType());
//			CoreData.getInstance().setPlaceList(places);
//			PlacesSQL.deleteAllPlaces();
//			PlacesSQL.addPlacesList(places);
//
//			List<Tables> tables = gson.fromJson(object.getString("tableList"),
//					new TypeToken<ArrayList<Tables>>() {
//					}.getType());
//			CoreData.getInstance().setTableList(tables);
//			TablesSQL.deleteAllTables();
//			TablesSQL.addTablesList(tables);
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

    public static void getItemCategory(int statusCode, Header[] headers,
                                       byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<ItemCategory> itemCategoryList = gson.fromJson(
                    object.getString("subCategoryList"),
                    new TypeToken<ArrayList<ItemCategory>>() {
                    }.getType());
            CoreData.getInstance().setItemCategories(itemCategoryList);
            ItemCategorySQL.deleteAllItemCategory();
            ItemCategorySQL.addItemCategoryList(itemCategoryList);

            List<ItemMainCategory> itemMainCategories = gson.fromJson(
                    object.getString("mainCategoryList"),
                    new TypeToken<ArrayList<ItemMainCategory>>() {
                    }.getType());
            ItemMainCategorySQL.deleteAllItemMainCategory();
            ItemMainCategorySQL.addItemMainCategory(itemMainCategories);
            //CoreData.getInstance().setItemMainCategories(ItemMainCategorySQL.getAllAvaiableItemMainCategoryInRevenueCenter());
            CoreData.getInstance().setItemMainCategories(ItemMainCategorySQL.getAllItemMainCategory());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getItemDetail(int statusCode, Header[] headers,
                                     byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<ItemDetail> itemDetailList = gson.fromJson(
                    object.getString("itemList"),
                    new TypeToken<ArrayList<ItemDetail>>() {
                    }.getType());
            ItemDetailSQL.deleteAllItemDetail();
            ItemDetailSQL.addItemDetailList(itemDetailList);
            CoreData.getInstance().setItemDetails(
                    ItemDetailSQL.getAllItemDetail());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getAllModifier(int statusCode, Header[] headers,
                                      byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<ItemModifier> itemModifierlList = gson.fromJson(
                    object.getString("itemModifierList"),
                    new TypeToken<ArrayList<ItemModifier>>() {
                    }.getType());


            ItemModifierSQL.deleteAllItemModifier();
            ItemModifierSQL.addItemModifierList(itemModifierlList);
            List<ItemModifier> itemModifiers = ItemModifierSQL.getAllItemModifier();

            CoreData.getInstance().setItemModifiers(ItemModifierSQL.getAllItemModifier());
            List<Modifier> modifierList = gson.fromJson(
                    object.getString("modifierList"),
                    new TypeToken<ArrayList<Modifier>>() {
                    }.getType());

            ModifierSQL.deleteAllModifier();
            ModifierSQL.addModifierList(modifierList);
            CoreData.getInstance().setModifierList(ModifierSQL.getAllModifier());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getTax(int statusCode, Header[] headers,
                              byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<TaxCategory> taxCategories = gson.fromJson(
                    object.getString("taxCategoryList"),
                    new TypeToken<ArrayList<TaxCategory>>() {
                    }.getType());
            CoreData.getInstance().setTaxCategories(taxCategories);
            TaxCategorySQL.deleteAllTaxCategory();
            TaxCategorySQL.addTaxCategorys(taxCategories);

            List<Tax> taxs = gson.fromJson(object.getString("taxList"),
                    new TypeToken<ArrayList<Tax>>() {
                    }.getType());
            CoreData.getInstance().setTaxs(taxs);
            TaxSQL.deleteAllTax();
            TaxSQL.addTaxs(taxs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getOther(int statusCode, Header[] headers,
                                byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();

            List<PaymentMethod> pamentMethodList = gson.fromJson(
                    object.getString("pamentMethodList"),
                    new TypeToken<ArrayList<PaymentMethod>>() {
                    }.getType());

            PaymentMethodSQL.deleteAllPaymentMethodCustom();
            PaymentMethodSQL.addPaymentMethod(pamentMethodList);
            CoreData.getInstance().setPamentMethodList(PaymentMethodSQL.getAllPaymentMethod());

            List<SettlementRestaurant> settlementRestaurant = gson.fromJson(object.getString("settlementRestaurants"),
                    new TypeToken<ArrayList<SettlementRestaurant>>() {
                    }.getType());
            SettlementRestaurantSQL.deleteAllSettlementRestaurantCustom();
            SettlementRestaurantSQL.addSettlementRestaurant(settlementRestaurant);
            CoreData.getInstance().setSettlementRestaurant(SettlementRestaurantSQL.getAllSettlementRestaurant());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getPaymentMethod(int statusCode, Header[] headers,
                                        byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();

            List<SettlementRestaurant> settlementRestaurant = gson.fromJson(object.getString("settlementRestaurants"),
                    new TypeToken<ArrayList<SettlementRestaurant>>() {
                    }.getType());
            SettlementRestaurantSQL.deleteAllSettlementRestaurantIpay88HalalPayment();
            String ipay88PaymentId = null;


            ArrayList<SettlementRestaurant> settlementRestaurantNonIpay = new ArrayList<>();
            ArrayList<PaymentMethod> ipay88PaymentMethods = new ArrayList<>();
            ArrayList<SettlementRestaurant> settlementRestaurantCustom = new ArrayList<>();
            for (SettlementRestaurant settle : settlementRestaurant) {
                if (!(settle.getMediaId() == ParamConst.SETTLEMENT_TYPE_DELIVEROO ||
                        settle.getMediaId() == ParamConst.SETTLEMENT_TYPE_UBEREATS ||
                        settle.getMediaId() == ParamConst.SETTLEMENT_TYPE_FOODPANDA ||
                        settle.getMediaId() == ParamConst.SETTLEMENT_TYPE_STORED_CARD ||
                        settle.getMediaId() == ParamConst.SETTLEMENT_TYPE_STORED_CARD_SALES ||
                        settle.getMediaId() == ParamConst.SETTLEMENT_TYPE_EZLINK ||
                        settle.getMediaId() == ParamConst.SETTLEMENT_TYPE_GROUPON)) {
                    if (settle.getMediaId() > ParamConst.SETTLEMENT_TYPE_IPAY88 && settle.getMediaId() < ParamConst.SETTLEMENT_TYPE_PAYHALAL) {
                        if (TextUtils.isEmpty(ipay88PaymentId)) {
                            ipay88PaymentId = "" + settle.getId();
                        } else {
                            ipay88PaymentId += "|" + settle.getId();
                        }

                        PaymentMethod ipay88Method = new PaymentMethod();
                        ipay88Method.setId(settle.getId());
                        ipay88Method.setRestaurantId(settle.getRestaurantId());
                        ipay88Method.setPaymentTypeId(Long.valueOf(settle.getMediaId()));
                        ipay88PaymentMethods.add(ipay88Method);
                    } else {
                        if (TextUtils.isEmpty(settle.getOtherPaymentId())) {
                            settlementRestaurantNonIpay.add(settle);
                        } else {
                            settlementRestaurantCustom.add(settle);
                        }
                    }
                }
            }
            if (settlementRestaurantNonIpay.size() > 0) {
                SettlementRestaurantSQL.addSettlementRestaurant(settlementRestaurantNonIpay);
            }

            PaymentMethodSQL.deleteAllPaymentMethodIpay88Halal();
            if (ipay88PaymentMethods.size() > 0) {
                PaymentMethodSQL.addPaymentMethod(ipay88PaymentMethods);
                CoreData.getInstance().setPamentMethodList(PaymentMethodSQL.getAllPaymentMethod());

                ArrayList<SettlementRestaurant> settlementRestaurants = new ArrayList<>();
                SettlementRestaurant settlementPay88 = new SettlementRestaurant();
                settlementPay88.setId(ParamConst.SETTLEMENT_TYPE_IPAY88);
                settlementPay88.setRestaurantId(ipay88PaymentMethods.get(0).getRestaurantId());
                settlementPay88.setMediaId(ParamConst.SETTLEMENT_TYPE_IPAY88);
                settlementPay88.setOtherPaymentId(ipay88PaymentId);
                settlementRestaurants.add(settlementPay88);
                SettlementRestaurantSQL.addSettlementRestaurant(settlementRestaurants);
            }
            if (settlementRestaurantCustom.size() > 0) {
                SettlementRestaurantSQL.addSettlementRestaurant(settlementRestaurantCustom);
            }
            CoreData.getInstance().setSettlementRestaurant(SettlementRestaurantSQL.getAllSettlementRestaurant());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void getHappyHour(int statusCode, Header[] headers,
                                    byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<ItemHappyHour> itemHappyHours = gson.fromJson(
                    object.getString("itemHappyHourList"),
                    new TypeToken<ArrayList<ItemHappyHour>>() {
                    }.getType());

            CoreData.getInstance().setItemHappyHours(itemHappyHours);
            ItemHappyHourSQL.deleteAllItemHappyHour();
            ItemHappyHourSQL.addItemHappyHourList(itemHappyHours);

            List<HappyHourWeek> happyHourWeeks = gson.fromJson(
                    object.getString("happyHourWeekList"),
                    new TypeToken<ArrayList<HappyHourWeek>>() {
                    }.getType());
            CoreData.getInstance().setHappyHourWeeks(happyHourWeeks);
            HappyHourWeekSQL.deleteAllHappyHourWeek();
            HappyHourWeekSQL.addHappyHourWeekList(happyHourWeeks);

            List<HappyHour> happyHours = gson.fromJson(
                    object.getString("happyHourList"),
                    new TypeToken<ArrayList<HappyHour>>() {
                    }.getType());
            CoreData.getInstance().setHappyHours(happyHours);
            HappyHourSQL.deleteAllHappyHour();
            HappyHourSQL.addHappyHourList(happyHours);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getPromotionInfo(int statusCode, Header[] headers,
                                        byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<Promotion> promotionList = gson.fromJson(
                    object.getString("promotionInfoList"),
                    new TypeToken<ArrayList<Promotion>>() {
                    }.getType());
            List<PromotionWeek> promotionWeekTimeList = gson.fromJson(
                    object.getString("promotionWeekTimeList"),
                    new TypeToken<ArrayList<PromotionWeek>>() {
                    }.getType());
            PromotionSQL.deleteAllPromotion();
            PromotionSQL.addPromotion(promotionList);
            PromotionWeekSQL.deleteAllPromotionWeek();
            PromotionWeekSQL.addPromotionWeek(promotionWeekTimeList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getPromotionData(int statusCode, Header[] headers,
                                        byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<PromotionWeek> promotionWeekList = gson.fromJson(
                    object.getString("promotionWeekList"),
                    new TypeToken<ArrayList<PromotionWeek>>() {
                    }.getType());
            List<ItemPromotion> itemPromotionList = gson.fromJson(
                    object.getString("itemPromotionList"),
                    new TypeToken<ArrayList<ItemPromotion>>() {
                    }.getType());
            List<PromotionOrder> orderPromotionList = gson.fromJson(
                    object.getString("orderPromotionList"),
                    new TypeToken<ArrayList<PromotionOrder>>() {
                    }.getType());

            PromotionWeekSQL.deleteAllPromotionWeek();
            PromotionWeekSQL.addPromotionWeek(promotionWeekList);
            PromotionItemSQL.deleteAllPromotionItem();
            PromotionOrderSQL.deleteAllpromotionOrder();
            PromotionOrderSQL.addPromotionOrder(orderPromotionList);
            PromotionItemSQL.addPromotionItem(itemPromotionList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//	public static void getPromotionData(int statusCode, Header[] headers,
//											byte[] responseBody) {
//		try {
//			JSONObject object = new JSONObject(new String(responseBody));
//			Gson gson = new Gson();
//			PromotionAndWeekVo promotionAndWeekVo = gson.fromJson(
//					object.getString("PromotionAndOrderVo"),
//					new TypeToken<PromotionAndWeekVo>() {
//					}.getType());
//
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}


    public static List<BohHoldSettlement> getBOHSettlement(int statusCode, Header[] headers,
                                                           byte[] responseBody) {
        String str = null;
        List<BohHoldSettlement> bohHoldSettlementList = new ArrayList<BohHoldSettlement>();
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            bohHoldSettlementList = gson.fromJson(object.getString("bohUnpaidList"),
                    new TypeToken<List<BohHoldSettlement>>() {
                    }.getType());
            str = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bohHoldSettlementList;
    }

    /**
     * KDS为服务器的时候的操作
     */

    // public static void submitKot1(int statusCode, Header[] headers,
    // byte[] responseBody) {
    // Gson gson = new Gson();
    // try {
    // JSONObject object = new JSONObject(new String(responseBody));
    // String method = object.optString("method");
    // if (!TextUtils.isEmpty(method)) {
    // KotSummary kotSummary = gson.fromJson(
    // object.optString("kotSummary"), KotSummary.class);
    // if (method.equals(ParamConst.JOB_NEW_KOT)) {
    // int orderId = kotSummary.getOrderId();
    // ArrayList<OrderDetail> orderDetails = OrderDetailSQL
    // .getOrderDetails(orderId);
    // ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
    // for (int i = 0; i < orderDetails.size(); i++) {
    // OrderDetail orderDetail = orderDetails.get(i);
    // orderDetail
    // .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD);
    // KotItemDetail kotItemDetail = KotItemDetailSQL
    // .getKotItemDetailByOrderDetailId(orderDetail
    // .getId());
    // kotItemDetail
    // .setKotStatus(ParamConst.KOT_STATUS_UNDONE);
    // kotItemDetails.add(kotItemDetail);
    // }
    // OrderDetailSQL.addOrderDetailList(orderDetails);
    // KotItemDetailSQL.addKotItemDetailList(kotItemDetails);
    // } else if (method.equals(ParamConst.JOB_UPDATE_KOT)) {
    // ArrayList<Integer> orderDetailIds = new ArrayList<Integer>();
    // orderDetailIds = gson.fromJson(
    // object.optString("orderDetailIds"),
    // new TypeToken<ArrayList<Integer>>() {
    // }.getType());
    // if (orderDetailIds.isEmpty()) {
    // return;
    // }
    // ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
    // ArrayList<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
    // for (int i = 0; i < orderDetailIds.size(); i++) {
    // OrderDetail orderDetail = OrderDetailSQL
    // .getOrderDetail(orderDetailIds.get(i));
    // orderDetail
    // .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD);
    // KotItemDetail kotItemDetail = KotItemDetailSQL
    // .getKotItemDetailByOrderDetailId(orderDetailIds
    // .get(i));
    // kotItemDetail
    // .setKotStatus(ParamConst.KOT_STATUS_UNDONE);
    // orderDetails.add(orderDetail);
    // kotItemDetails.add(kotItemDetail);
    // }
    // OrderDetailSQL.addOrderDetailList(orderDetails);
    // KotItemDetailSQL.addKotItemDetailList(kotItemDetails);
    // }
    // Order order = OrderSQL.getOrder(kotSummary.getOrderId());
    // order.setOrderStatus(ParamConst.ORDER_STATUS_UNPAY);
    // OrderSQL.update(order);
    // // App.instance.getTopActivity().httpRequestAction(
    // // MainPage.VIEW_EVENT_SET_DATA, kotSummary.getOrderId());
    // }
    //
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // }
    public static String getReportDayFromCloud(int statusCode, Header[] headers,
                                               byte[] responseBody) {
        String nettSales = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();

            //Parse remote DaySales Report
            ReportDaySales daySales = gson.fromJson(
                    object.getString("reportDaySales"), ReportDaySales.class);
            if (daySales != null) {
                GeneralSQL.deleteReportDataByBusinessDate(daySales.getBusinessDate().longValue());
                ReportDaySalesSQL.addReportDaySalesFromCloud(daySales);
                nettSales = daySales.getNettSales();
            }
            //Parse remote PLU Day Report
            List<ReportPluDayItem> reportPluDayItems = gson.fromJson(
                    object.getString("reportPluDayItems"), new TypeToken<ArrayList<ReportPluDayItem>>() {
                    }.getType());
            if (reportPluDayItems != null && reportPluDayItems.size() > 0) {
                ReportPluDayItemSQL.addReportPluDayItemsFromCloud(reportPluDayItems);
            }
            //Parse remote PLU Day combModifidier Report
            List<ReportPluDayComboModifier> reportPluDayComboModifiers = gson.fromJson(
                    object.getString("reportPluDayComboModifiers"), new TypeToken<ArrayList<ReportPluDayComboModifier>>() {
                    }.getType());
            if (reportPluDayComboModifiers != null && reportPluDayComboModifiers.size() > 0) {
                ReportPluDayComboModifierSQL.addReportPluDayModifierFromCloud(reportPluDayComboModifiers);
            }
            //Parse remote Day Tax report
            List<ReportDayTax> reportDayTax = gson.fromJson(
                    object.getString("reportDayTax"), new TypeToken<ArrayList<ReportDayTax>>() {
                    }.getType());
            if (reportDayTax != null && reportDayTax.size() > 0) {
                ReportDayTaxSQL.saveReportDayTaxListFromCloud(reportDayTax);
            }

            //Parse remote Hourly Sales
            List<ReportHourly> reportHourlys = gson.fromJson(
                    object.getString("reportHourlys"), new TypeToken<ArrayList<ReportHourly>>() {
                    }.getType());
            if (reportHourlys != null && reportHourlys.size() > 0) {
                ReportHourlySQL.addReportHourlyFromCloud(reportHourlys);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nettSales;
    }

    //monthly sale report
    public static List<MonthlySalesReport> getReportMonthlySaleFromCloud(int statusCode, Header[] headers,
                                                                         byte[] responseBody) {
        List<MonthlySalesReport> data = new ArrayList<MonthlySalesReport>();
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            MonthlySalesReport monthlySalesReport = new MonthlySalesReport();
            Gson gson = new Gson();
            data = gson.fromJson(object.getString("reportDaySalesList"), new TypeToken<ArrayList<MonthlySalesReport>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    //monthly PLU report
    public static List<MonthlyPLUReport> getReportMonthlyPLUFromCloud(int statusCode, Header[] headers,
                                                                      byte[] responseBody) {
        List<MonthlyPLUReport> data = new ArrayList<MonthlyPLUReport>();
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            MonthlyPLUReport monthlyPLUReport = new MonthlyPLUReport();
            Gson gson = new Gson();
            data = gson.fromJson(object.getString("reportPluDayItemList"), new TypeToken<ArrayList<MonthlyPLUReport>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void getOrderFromApp(int statusCode, Header[] headers,
                                       byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            TempOrder tempOrder = new TempOrder();
            tempOrder.setCustId(object.getInt("custId"));
            tempOrder.setTotal(object.getString("total"));
            tempOrder.setAppOrderId(object.getInt("appOrderId"));
            tempOrder.setPlaceOrderTime(object.getLong("placeOrderTime"));
            tempOrder.setStatus(ParamConst.TEMP_ORDER_NORMAL);
            List<TempOrderDetail> tempOrderDetails = gson.fromJson(object.getString("orderDetailList"), new TypeToken<ArrayList<TempOrderDetail>>() {
            }.getType());
            for (TempOrderDetail tempOrderDetail : tempOrderDetails) {
                tempOrderDetail.setAppOrderId(tempOrder.getAppOrderId());
            }
            List<TempModifierDetail> tempModifierDetails = gson.fromJson(object.getString("modifierList"), new TypeToken<ArrayList<TempModifierDetail>>() {
            }.getType());
            TempOrderSQL.addTempOrder(tempOrder);
            TempOrderDetailSQL.addTempOrderDetailList(tempOrder, tempOrderDetails);
            TempModifierDetailSQL.addTempModifierDetailList(tempModifierDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getAppOrderById(int statusCode, Header[] headers,
                                       byte[] responseBody, boolean canCheck) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            AppOrder appOrder = gson.fromJson(object.getString("appOrder"), AppOrder.class);
            if (appOrder.getPayStatus() == 0) {
                return;
            }
            List<AppOrderDetail> appOrderDetailList = gson.fromJson(object.getString("appOrderDetailList"), new TypeToken<ArrayList<AppOrderDetail>>() {
            }.getType());
            List<AppOrderDetailTax> appOrderDetailTaxList = gson.fromJson(object.getString("appOrderDetailTaxList"), new TypeToken<ArrayList<AppOrderDetailTax>>() {
            }.getType());
//					gson.fromJson(object.getString("appOrderDetailTaxList"), new TypeToken<ArrayList<AppOrderDetailTax>>(){}.getType());
            List<AppOrderModifier> appOrderModifierList = gson.fromJson(object.getString("appOrderModifierList"), new TypeToken<ArrayList<AppOrderModifier>>() {
            }.getType());
            int tableId = 0;
            if (!TextUtils.isEmpty(appOrder.getTableNo())) {
                try {
                    tableId = Integer.parseInt(appOrder.getTableNo());
                } catch (NumberFormatException e) {

                }
            }
            appOrder.setTableId(tableId);
            AppOrderSQL.addAppOrder(appOrder);
            AppOrderDetailSQL.addAppOrderDetailList(appOrderDetailList);
            AppOrderDetailTaxSQL.addAppOrderDetailTaxList(appOrderDetailTaxList);
            AppOrderModifierSQL.addAppOrderModifierList(appOrderModifierList);
            if (canCheck)
                if (!App.instance.isRevenueKiosk()) {
                    if (App.instance.getSystemSettings().isAutoRecevingOnlineOrder())
                        App.instance.appOrderShowDialog(true, appOrder, appOrderDetailList, appOrderModifierList, appOrderDetailTaxList);
                } else {
                    if (App.instance.getSystemSettings().isAutoRecevingOnlineOrder())
                        App.instance.appOrderTransforOrder(appOrder, appOrderDetailList, appOrderModifierList, appOrderDetailTaxList);
                }
            App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllAppOrder(int statusCode, Header[] headers,
                                      byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<AppOrder> appOrderList = gson.fromJson(object.getString("appOrderList"), new TypeToken<ArrayList<AppOrder>>() {
            }.getType());
            List<AppOrderDetail> appOrderDetailList = gson.fromJson(object.getString("appOrderDetailList"), new TypeToken<ArrayList<AppOrderDetail>>() {
            }.getType());
            List<AppOrderDetailTax> appOrderDetailTaxList = gson.fromJson(object.getString("appOrderDetailTaxList"), new TypeToken<ArrayList<AppOrderDetailTax>>() {
            }.getType());
//					gson.fromJson(object.getString("appOrderDetailTaxList"), new TypeToken<ArrayList<AppOrderDetailTax>>(){}.getType());
            List<AppOrderModifier> appOrderModifierList = gson.fromJson(object.getString("appOrderModifierList"), new TypeToken<ArrayList<AppOrderModifier>>() {
            }.getType());
//			AppOrderDetailSQL.addAppOrderDetailList(appOrderDetailList);
//			AppOrderDetailTaxSQL.addAppOrderDetailTaxList(appOrderDetailTaxList);
//			AppOrderModifierSQL.addAppOrderModifierlList(appOrderModifierList);
            for (AppOrder appOrder : appOrderList) {
                if (AppOrderSQL
                        .getAppOrderById(appOrder
                                .getId().intValue()) != null) {
                    continue;
                }
                if (appOrder.getPayStatus() == 0) {
                    continue;
                }
                int tableId = 0;
                if (!TextUtils.isEmpty(appOrder.getTableNo())) {
                    try {
                        tableId = Integer.parseInt(appOrder.getTableNo());
                    } catch (NumberFormatException e) {

                    }
                }
                appOrder.setTableId(tableId);
                AppOrderSQL.addAppOrder(appOrder);
                List<AppOrderDetail> appOrderDetails = new ArrayList<AppOrderDetail>();
                List<AppOrderModifier> appOrderModifiers = new ArrayList<AppOrderModifier>();
                List<AppOrderDetailTax> appOrderDetailTaxes = new ArrayList<AppOrderDetailTax>();
                for (AppOrderDetail appOrderDetail : appOrderDetailList) {
                    if (appOrderDetail.getOrderId().intValue() == appOrder
                            .getId().intValue()) {
                        AppOrderDetailSQL
                                .addAppOrderDetai(appOrderDetail);
                        appOrderDetails.add(appOrderDetail);
                        for (AppOrderModifier appOrderModifier : appOrderModifierList) {
                            if (appOrderModifier.getOrderDetailId()
                                    .intValue() == appOrderDetail
                                    .getId().intValue()) {
                                appOrderModifiers.add(appOrderModifier);
                                AppOrderModifierSQL
                                        .addAppOrderModifier(appOrderModifier);
                            }
                        }
                        for (AppOrderDetailTax appOrderDetailTax : appOrderDetailTaxList) {
                            if (appOrderDetailTax.getOrderDetailId().intValue() == appOrderDetail.getId().intValue()) {
                                appOrderDetailTaxes.add(appOrderDetailTax);
                                AppOrderDetailTaxSQL.addAppOrderDetailTax(appOrderDetailTax);
                            }
                        }
                    }
                }
//				App.instance.appOrderShowDialog(false, appOrder, appOrderDetails, appOrderModifiers, appOrderDetailTaxes);
            }
            App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void resetItemDetailStockNum(byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<RemainingStock> remainingStockList = gson.fromJson(object.getString("remainingStockList"), new TypeToken<ArrayList<RemainingStock>>() {
            }.getType());
            RemainingStockSQL.deleteAllRemainingStock();
            RemainingStockSQL.addRemainingStock(remainingStockList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRemainingStock(byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<RemainingStock> remainingStockList = gson.fromJson(object.getString("remainingStockList"), new TypeToken<ArrayList<RemainingStock>>() {
            }.getType());
            RemainingStockSQL.deleteAllRemainingStock();
            RemainingStockSQL.addRemainingStock(remainingStockList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPlaceTable(byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<PlaceInfo> placeInfoList = gson.fromJson(object.getString("placeList"), new TypeToken<ArrayList<PlaceInfo>>() {
            }.getType());
            List<TableInfo> tableInfoList = gson.fromJson(object.getString("tableList"), new TypeToken<ArrayList<TableInfo>>() {
            }.getType());
            PlaceInfoSQL.deleteAllPlaceInfo();
            TableInfoSQL.deleteAllTableInfo();
            PlaceInfoSQL.addPlaceInfoList(placeInfoList);
            TableInfoSQL.addTablesList(tableInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStoredCardValue(byte[] responseBody, int payTypeId) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            String balance = object.getString("balance");
            String consuming = object.getString("records");
            ConsumingRecords consumingRecords = gson.fromJson(consuming, ConsumingRecords.class);
            consumingRecords.setBusinessDate(App.instance.getBusinessDate());
            consumingRecords.setPayTypeId(payTypeId);
            ConsumingRecordsSQL.addConsumingRecords(consumingRecords);
            App.instance.remoteStoredCard(App.instance.getCahierPrinter(), consumingRecords, balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<UserTimeSheet> getClockInUserTimeSheet(byte[] responseBody) {
        List<UserTimeSheet> userTimeSheetList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            userTimeSheetList = gson.fromJson(object.getString("userTimeSheetList"), new TypeToken<List<UserTimeSheet>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userTimeSheetList;
    }

    //start pay88
    public static void saveLoginQRPayment(byte[] responseBody) {
        LoginQrPayment result = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            result = gson.fromJson(object.toString(), LoginQrPayment.class);
//            CoreData.getInstance().setLoginQrPayment(result);
            Store.saveObject(App.instance, Store.LOGIN_QRPAYMENT, result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //end pay88


}
