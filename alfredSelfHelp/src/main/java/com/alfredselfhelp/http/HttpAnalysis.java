package com.alfredselfhelp.http;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Base64;

import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ConsumingRecords;
import com.alfredbase.javabean.HappyHour;
import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.javabean.RemainingStockVo;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.SettingData;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserRestaurant;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.model.TableAndKotNotificationList;
import com.alfredbase.store.Store;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.ConsumingRecordsSQL;
import com.alfredbase.store.sql.HappyHourSQL;
import com.alfredbase.store.sql.HappyHourWeekSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.ItemHappyHourSQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.ItemModifierSQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.PaymentSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.PrinterGroupSQL;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
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
import com.alfredbase.utils.BitmapUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredselfhelp.activity.EmployeeID;
import com.alfredselfhelp.activity.MainActivity;
import com.alfredselfhelp.activity.MenuActivity;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.global.SyncCentre;
import com.alfredselfhelp.utils.UIHelp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpAnalysis {
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
            handler.sendEmptyMessage(resultCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void updateAllData(String responseBody, Handler handler) {
        Gson gson = new Gson();
        try {
            JSONObject object = new JSONObject(responseBody);
//			List<User> users = gson.fromJson(object.getString("users"),
//					new TypeToken<ArrayList<User>>() {
//					}.getType());
            OrderSQL.deleteAllOrder();
            OrderDetailSQL.deleteAllOrderDetail();
            OrderBillSQL.deleteAllOrderBill();
            OrderDetailTaxSQL.deleteAllOrderDetailTax();
            OrderModifierSQL.deleteAllOrderModifier();
            PaymentSQL.deleteAllPayment();
            PaymentSettlementSQL.deleteAllSettlement();
//			if(users != null && users.size() > 0){
//				UserSQL.deleteAllUser();
//				UserSQL.addUsers(users);
//				CoreData.getInstance().setUsers(users);
//			}
            final Restaurant restaurant = gson.fromJson(object.getString("restaurant"),
                    Restaurant.class);
            if (restaurant != null) {
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
            if (revenueCenter != null) {
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
            if (itemMainCategories != null && itemMainCategories.size() > 0) {
                ItemMainCategorySQL.deleteAllItemMainCategory();
                ItemMainCategorySQL.addItemMainCategory(itemMainCategories);
                CoreData.getInstance().setItemMainCategories(itemMainCategories);
            }
            List<ItemCategory> itemCategories = gson.fromJson(object.getString("itemCategories"),
                    new TypeToken<ArrayList<ItemCategory>>() {
                    }.getType());
            if (itemCategories != null && itemCategories.size() > 0) {
                ItemCategorySQL.deleteAllItemCategory();
                ItemCategorySQL.addItemCategoryList(itemCategories);
                CoreData.getInstance().setItemCategories(itemCategories);
            }
            List<ItemDetail> itemDetails = gson.fromJson(object.getString("itemDetails"),
                    new TypeToken<ArrayList<ItemDetail>>() {
                    }.getType());
            if (itemDetails != null && itemDetails.size() > 0) {
                ItemDetailSQL.deleteAllItemDetail();
                ItemDetailSQL.addItemDetailList(itemDetails);
                CoreData.getInstance().setItemDetails(itemDetails);
            }
            List<Modifier> modifiers = gson.fromJson(object.getString("modifiers"),
                    new TypeToken<ArrayList<Modifier>>() {
                    }.getType());
            if (modifiers != null && modifiers.size() > 0) {
                ModifierSQL.deleteAllModifier();
                ModifierSQL.addModifierList(modifiers);
                CoreData.getInstance().setModifierList(modifiers);
            }
            List<Printer> printers = gson.fromJson(object.getString("printers"),
                    new TypeToken<ArrayList<Printer>>() {
                    }.getType());
            if (printers != null && printers.size() > 0) {
                PrinterSQL.deleteAllPrinter();
                PrinterSQL.addPrinters(printers);
                CoreData.getInstance().setPrinters(printers);
            }
            List<TableInfo> tableInfos = gson.fromJson(object.getString("tableInfos"),
                    new TypeToken<ArrayList<TableInfo>>() {
                    }.getType());
            if (tableInfos != null && tableInfos.size() > 0) {
                TableInfoSQL.deleteAllTableInfo();
                TableInfoSQL.addTablesList(tableInfos);
            }

            TableInfo tableInfo = TableInfoSQL.getKioskTable();
            PlaceInfo placeInfo = PlaceInfoSQL.getKioskPlaceInfo();
            if (placeInfo == null) {
                placeInfo = ObjectFactory.getInstance().addNewPlace(App.instance.getRevenueCenter().getRestaurantId().intValue(),
                        App.instance.getRevenueCenter().getId().intValue(), "kiosk");
                placeInfo.setIsKiosk(1);
                PlaceInfoSQL.addPlaceInfo(placeInfo);
            }
            if (tableInfo == null) {
                tableInfo = ObjectFactory.getInstance().addNewTable("table_1_1", placeInfo.getRestaurantId().intValue(), placeInfo.getRevenueId().intValue(), placeInfo.getId().intValue(), 480, 800);
                tableInfo.setIsKiosk(1);
                tableInfo.setPosId(0);
                TableInfoSQL.addTables(tableInfo);
            }
            List<PlaceInfo> placeInfos = gson.fromJson(object.getString("placeInfos"),
                    new TypeToken<ArrayList<PlaceInfo>>() {
                    }.getType());
            if (placeInfos != null && placeInfos.size() > 0) {
                PlaceInfoSQL.deleteAllPlaceInfo();
                PlaceInfoSQL.addPlaceInfoList(placeInfos);
            }
            List<ItemModifier> itemModifiers = gson.fromJson(object.getString("itemModifiers"),
                    new TypeToken<ArrayList<ItemModifier>>() {
                    }.getType());
            if (itemModifiers != null && itemModifiers.size() > 0) {
                ItemModifierSQL.deleteAllItemModifier();
                ItemModifierSQL.addItemModifierList(itemModifiers);
                CoreData.getInstance().setItemModifiers(itemModifiers);
            }
            List<TaxCategory> taxCategories = gson.fromJson(object.getString("taxCategories"),
                    new TypeToken<ArrayList<TaxCategory>>() {
                    }.getType());
            if (taxCategories != null && taxCategories.size() > 0) {
                TaxCategorySQL.deleteAllTaxCategory();
                TaxCategorySQL.addTaxCategorys(taxCategories);
                CoreData.getInstance().setTaxCategories(taxCategories);
            }
            List<Tax> taxes = gson.fromJson(object.getString("taxes"),
                    new TypeToken<ArrayList<Tax>>() {
                    }.getType());
            if (taxes != null && taxes.size() > 0) {
                TaxSQL.deleteAllTax();
                TaxSQL.addTaxs(taxes);
                CoreData.getInstance().setTaxs(taxes);
            }
            List<ItemHappyHour> itemHappyHours = gson.fromJson(object.getString("itemHappyHours"),
                    new TypeToken<ArrayList<ItemHappyHour>>() {
                    }.getType());
            if (itemHappyHours != null && itemHappyHours.size() > 0) {
                ItemHappyHourSQL.deleteAllItemHappyHour();
                ItemHappyHourSQL.addItemHappyHourList(itemHappyHours);
                CoreData.getInstance().setItemHappyHours(itemHappyHours);
            }
            List<HappyHourWeek> happyHourWeeks = gson.fromJson(object.getString("happyHourWeeks"),
                    new TypeToken<ArrayList<HappyHourWeek>>() {
                    }.getType());
            if (happyHourWeeks != null && happyHourWeeks.size() > 0) {
                HappyHourWeekSQL.deleteAllHappyHourWeek();
                HappyHourWeekSQL.addHappyHourWeekList(happyHourWeeks);
                CoreData.getInstance().setHappyHourWeeks(happyHourWeeks);
            }
            List<HappyHour> happyHours = gson.fromJson(object.getString("happyHours"),
                    new TypeToken<ArrayList<HappyHour>>() {
                    }.getType());
            if (happyHours != null && happyHours.size() > 0) {
                HappyHourSQL.deleteAllHappyHour();
                HappyHourSQL.addHappyHourList(happyHours);
                CoreData.getInstance().setHappyHours(happyHours);
            }
            List<RestaurantConfig> restaurantConfigs = gson.fromJson(object.getString("restaurantConfigs"),
                    new TypeToken<ArrayList<RestaurantConfig>>() {
                    }.getType());
            if (restaurantConfigs != null && restaurantConfigs.size() > 0) {
                RestaurantConfigSQL.deleteAllRestaurantConfig();
                RestaurantConfigSQL.addRestaurantConfigs(restaurantConfigs);
                CoreData.getInstance().setRestaurantConfigs(restaurantConfigs);
                App.instance.setLocalRestaurantConfig(restaurantConfigs);
            }
            List<PrinterGroup> printerGroups = gson.fromJson(object.getString("printerGroups"),
                    new TypeToken<ArrayList<PrinterGroup>>() {
                    }.getType());
            if (printerGroups != null && printerGroups.size() > 0) {
                PrinterGroupSQL.deletePrinter();
                PrinterGroupSQL.addPrinterGroups(printerGroups);
                CoreData.getInstance().setPrinterGroups(printerGroups);
            }
            List<UserRestaurant> userRestaurants = gson.fromJson(object.getString("userRestaurants"),
                    new TypeToken<ArrayList<UserRestaurant>>() {
                    }.getType());
            if (userRestaurants != null && userRestaurants.size() > 0) {
                UserRestaurantSQL.deleteAllUserRestaurant();
                UserRestaurantSQL.addUsers(userRestaurants);
                CoreData.getInstance().setUserRestaurant(userRestaurants);
            }
            List<PaymentMethod> paymentMethods = gson.fromJson(object.getString("paymentMethods"),
                    new TypeToken<ArrayList<PaymentMethod>>() {
                    }.getType());
            if (paymentMethods != null && paymentMethods.size() > 0) {
                PaymentMethodSQL.deleteAllPaymentMethod();
                PaymentMethodSQL.addPaymentMethod(paymentMethods);
                CoreData.getInstance().setPamentMethodList(paymentMethods);
            }
            List<SettlementRestaurant> settlementRestaurants = gson.fromJson(object.getString("settlementRestaurants"),
                    new TypeToken<ArrayList<SettlementRestaurant>>() {
                    }.getType());
            if (settlementRestaurants != null && settlementRestaurants.size() > 0) {
                SettlementRestaurantSQL.deleteAllSettlementRestaurant();
                SettlementRestaurantSQL.addSettlementRestaurant(settlementRestaurants);
                CoreData.getInstance().setSettlementRestaurant(settlementRestaurants);
            }
            SessionStatus sessionStatus = gson.fromJson(object.getString("sessionStatus"), SessionStatus.class);
            sessionStatus.setTime(System.currentTimeMillis());
            Store.saveObject(App.instance, Store.SESSION_STATUS, sessionStatus);
            App.instance.setSessionStatus(sessionStatus);
            handler.sendEmptyMessage(EmployeeID.UPDATE_ALL_DATA_SUCCESS);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(EmployeeID.UPDATE_ALL_DATA_FAILURE);
    }


    public static void remainingStock(
                                      byte[] responseBody, Handler handler) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            List<RemainingStock> remainingStocks = gson.fromJson(object.getString("remainingStockList"),
                    new TypeToken<ArrayList<RemainingStock>>() {
                    }.getType());
            RemainingStockSQL.deleteAllRemainingStock();
            RemainingStockSQL.addRemainingStock(remainingStocks);
            if(object.has("businessDate")) {
                Long businessDate = object.optLong("businessDate");
                Store.putLong(App.instance, Store.BUSINESS_DATE, businessDate);
                Store.putLong(App.instance, Store.LAST_BUSINESSDATE, businessDate);
                App.instance.setBusinessDate(businessDate);
                App.instance.setLastBusinessDate(businessDate);
            }
            handler.sendEmptyMessage(MainActivity.REMAINING_STOCK_SUCCESS);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(MainActivity.REMAINING_STOCK_FAILURE);
    }


    public static void getCheckStock(
            byte[] responseBody, Handler handler) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            List<RemainingStockVo> remainingStockList = gson.fromJson(object.getString("remainingStockList"),
                    new TypeToken<ArrayList<RemainingStockVo>>() {
                    }.getType());
//            RemainingStockSQL.deleteAllRemainingStock();
//            RemainingStockSQL.addRemainingStock(remainingStocks);
            if(remainingStockList!=null&&remainingStockList.size()>0)
            {
                handler.sendEmptyMessage(MenuActivity.VIEW_CHECK_SOTCK_NUM_FAILED);
            }else {
                handler.sendEmptyMessage(MenuActivity.VIEW_CHECK_SOTCK_NUM_SUCCEED);
            }

            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(MenuActivity.VIEW_CHECK_SOTCK_NUM_FAILED);
    }


    public static String login(int statusCode, Header[] headers,
                               byte[] responseBody) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            String userKey = object.optString("userKey");
            String currencySymbol = object.optString("currencySymbol");
            boolean isDouble = object.optBoolean("isDouble");
            SessionStatus sessionStatus = gson.fromJson(
                    object.optString("session"), SessionStatus.class);
            MainPosInfo mainPosInfo = gson.fromJson(object.optString("mainPosInfo"), MainPosInfo.class);

//			SessionStatus localSessionStatus = App.instance.getSessionStatus();
//			if (localSessionStatus == null
//					|| localSessionStatus.getSession_status() != sessionStatus
//							.getSession_status()) {
//				OrderSQL.deleteAllOrder();
//				OrderDetailSQL.deleteAllOrderDetail();
//				OrderModifierSQL.deleteAllOrderModifier();
//				OrderDetailTaxSQL.deleteAllOrderDetailTax();
//			}
            //	Store.saveObject(App.instance, Store.SESSION_STATUS, sessionStatus);
            App.instance.setMainPosInfo(mainPosInfo);
            App.instance.setSessionStatus(sessionStatus);
            App.instance.setCurrencySymbol(currencySymbol, isDouble);
            CoreData.getInstance().setUserKey(mainPosInfo.getRevenueId(),userKey);
            return userKey;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
            RevenueCenterSQL.deleteAllRevenueCenter();
            RevenueCenterSQL.addRevenueCenters(revenueCenters);

            List<Printer> printers = gson.fromJson(
                    object.getString("printerList"),
                    new TypeToken<ArrayList<Printer>>() {
                    }.getType());

            CoreData.getInstance().setPrinters(printers);
            PrinterSQL.deleteAllPrinter();
            PrinterSQL.addPrinters(printers);

            Restaurant restaurant = gson.fromJson(
                    object.getString("restaurant"), Restaurant.class);
            CoreData.getInstance().setRestaurant(restaurant);
            RestaurantSQL.deleteAllRestaurant();
            RestaurantSQL.addRestaurant(restaurant);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUsers(int statusCode, Header[] headers,
                                      byte[] responseBody) {
        List<User> result = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            result = gson.fromJson(object.getString("userList"),
                    new TypeToken<ArrayList<User>>() {
                    }.getType());
            CoreData.getInstance().setUsers(result);
            UserSQL.deleteAllUser();
            UserSQL.addUsers(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void getPlaceInfo(int statusCode, Header[] headers,
                                    byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();

            List<PlaceInfo> places = gson.fromJson(object.getString("placeList"),
                    new TypeToken<ArrayList<PlaceInfo>>() {
                    }.getType());
//			CoreData.getInstance().setPlaceList(places);
//			PlacesSQL.deleteAllPlaces();
//			PlacesSQL.addPlacesList(places);
            PlaceInfoSQL.deleteAllPlaceInfo();
            PlaceInfoSQL.addPlaceInfoList(places);

            List<TableInfo> tables = gson.fromJson(object.getString("tableList"),
                    new TypeToken<ArrayList<TableInfo>>() {
                    }.getType());
//			CoreData.getInstance().setTableList(tables);
//			TablesSQL.deleteAllTables();
//			TablesSQL.addTablesList(tables);
            TableInfoSQL.deleteAllTableInfo();
            TableInfoSQL.addTablesList(tables);

            if (object.has("printMap")) {
                Map<Integer, PrinterDevice> printerDevices = gson.fromJson(object.getString("printMap"),
                        new TypeToken<Map<Integer, PrinterDevice>>() {
                        }.getType());
                //App.instance.setPrinterDevices(printerDevices);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Order selectTables(int statusCode, Header[] headers,
                                     byte[] responseBody) {
        Order order = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            order = gson.fromJson(object.getJSONObject("order").toString(),
                    Order.class);
            List<ItemDetail> itemDetails = gson.fromJson(object.getString("tempItems"),
                    new TypeToken<ArrayList<ItemDetail>>() {
                    }.getType());
            if (!itemDetails.isEmpty()) {
                ItemDetailSQL.addItemDetailList(itemDetails);
                CoreData.getInstance().setItemDetails(ItemDetailSQL.getAllItemDetail());
            }
            if (object.has("orderDetails")) {
                List<OrderDetail> orderDetails = gson.fromJson(object.getString("orderDetails"),
                        new TypeToken<ArrayList<OrderDetail>>() {
                        }.getType());
                if (!orderDetails.isEmpty()) {
                    OrderDetailSQL.addOrderDetailList(orderDetails);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return order;
    }

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
            CoreData.getInstance().setItemMainCategories(itemMainCategories);
            ItemMainCategorySQL.deleteAllItemMainCategory();
            ItemMainCategorySQL.addItemMainCategory(itemMainCategories);

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
            CoreData.getInstance().setItemModifiers(itemModifierlList);

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

    public static void pairingComplete(int statusCode, Header[] headers,
                                       byte[] responseBody, Handler handler) {
        Gson gson = new Gson();
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            MainPosInfo mainPosInfo = gson.fromJson(
                    object.optString("mainPosInfo"), MainPosInfo.class);
            App.instance.setMainPosInfo(mainPosInfo);
            SyncCentre.getInstance().setIp(mainPosInfo.getIP());
            handler.sendEmptyMessage(EmployeeID.HANDLER_PAIRING_COMPLETE);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void commitOrderAndOrderDetails(byte[] responseBody, String cardNum) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            PrinterDevice printer = App.instance.getCahierPrinter();
            if (printer != null) {

                Order order = gson.fromJson(jsonObject.optString("order"),
                        Order.class);
                PrinterTitle title = gson.fromJson(jsonObject.optString("title"),
                        PrinterTitle.class);
//				map.put("title", title);
//				PrinterTitle title = ObjectFactory.getInstance()
//						.getPrinterTitle(
//								App.instance.getRevenueCenter(),
//								order,
//								App.instance.getUser().getFirstName()
//										+ App.instance.getUser().getLastName(),
//								"", 1);
                List<PrintOrderItem> orderItems = gson.fromJson(jsonObject.getString("orderItems"),
                        new TypeToken<List<PrintOrderItem>>() {
                        }.getType());
                List<PrintOrderModifier> orderModifiers = gson.fromJson(jsonObject.getString("orderModifiers"),
                        new TypeToken<List<PrintOrderModifier>>() {
                        }.getType());
                List<Map<String, String>> taxMaps = gson.fromJson(jsonObject.getString("taxMaps"),
                        new TypeToken<List<Map<String, String>>>() {
                        }.getType());
                List<PaymentSettlement> paymentSettlements = gson.fromJson(jsonObject.getString("paymentSettlements"),
                        new TypeToken<List<PaymentSettlement>>() {
                        }.getType());
                //	RoundAmount roundAmount = gson.fromJson(jsonObject.getString("roundAmount"), RoundAmount.class);
                if (paymentSettlements == null || paymentSettlements.size() == 0) {
                    title.setOrderNo(title.getOrderNo() + "\n(Payment Failed)");
                }
                RoundAmount roundAmount = new RoundAmount();
                App.instance.remoteBillPrint(printer, title, order,
                        orderItems, orderModifiers, taxMaps, paymentSettlements, roundAmount, cardNum);
            }
        } catch (final Exception e) {
            App.getTopActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    UIHelp.showToast(App.instance, "error:" + e.getMessage());
                }
            });
            e.printStackTrace();
        }
    }

    public static void getKotNotification(int statusCode, Header[] headers,
                                          byte[] responseBody, Handler handler) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            List<TableAndKotNotificationList> notifications = new ArrayList<TableAndKotNotificationList>();
            notifications = gson.fromJson(
                    jsonObject.optString("notificationLists"),
                    new TypeToken<List<TableAndKotNotificationList>>() {
                    }.getType());
//			handler.sendMessage(handler.obtainMessage(
//					KOTNotification.VIEW_EVENT_GET_DATA, notifications));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void handlerGetOrderDetails(int statusCode, Header[] headers,
                                              byte[] responseBody, Handler handler) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            List<OrderDetail> orderDetails = gson.fromJson(
                    jsonObject.optString("otherOrderDetails"),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
            OrderDetailSQL.addOrderDetailList(orderDetails);
//			handler.sendEmptyMessage(MainPage.VIEW_ENVENT_GET_ORDERDETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
