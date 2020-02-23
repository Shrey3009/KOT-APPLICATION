package com.alfredbase.global;

import android.content.Context;
import android.text.TextUtils;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.HappyHour;
import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.ItemPromotion;
import com.alfredbase.javabean.LocalDevice;
import com.alfredbase.javabean.LoginResult;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.Promotion;
import com.alfredbase.javabean.PromotionOrder;
import com.alfredbase.javabean.PromotionWeek;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundRule;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserRestaurant;
import com.alfredbase.javabean.model.KotNotification;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.GeneralSQL;
import com.alfredbase.store.sql.HappyHourSQL;
import com.alfredbase.store.sql.HappyHourWeekSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.ItemHappyHourSQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.ItemModifierSQL;
import com.alfredbase.store.sql.KotNotificationSQL;
import com.alfredbase.store.sql.LocalDeviceSQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.PrinterGroupSQL;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.store.sql.PromotionItemSQL;
import com.alfredbase.store.sql.PromotionOrderSQL;
import com.alfredbase.store.sql.PromotionSQL;
import com.alfredbase.store.sql.PromotionWeekSQL;
import com.alfredbase.store.sql.RestaurantConfigSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.RevenueCenterSQL;
import com.alfredbase.store.sql.SettlementRestaurantSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.TaxCategorySQL;
import com.alfredbase.store.sql.TaxSQL;
import com.alfredbase.store.sql.UserRestaurantSQL;
import com.alfredbase.store.sql.UserSQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.LogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CoreData {
    private static CoreData instance;

    private Map<Integer, String> userKey = new HashMap<>();

    private List<User> users;
    private List<RevenueCenter> revenueCenters;
    private Restaurant restaurant;
    //	private List<Tables> tableList;
    private List<TableInfo> newTableList;
    //	private List<Places> placeList;
    private List<ItemModifier> itemModifiers;
    private List<ItemDetail> itemDetails;
    private List<ItemCategory> itemCategories;
    private List<ItemMainCategory> itemMainCategories;
    private List<ItemMainCategory> itemMainCategoriesForSelfHelp;
    private LoginResult loginResult;
    private List<TaxCategory> taxCategories;
    private List<Modifier> modifierList;
    private List<Tax> taxs;
    private List<ItemHappyHour> itemHappyHours;
    private List<HappyHourWeek> happyHourWeeks;
    private List<HappyHour> happyHours;
    private List<ItemPromotion> itemPromotions;
    private List<PromotionOrder> promotionOrders;
    private List<Promotion> promotions;
    private List<PromotionWeek> promotionWeeks;
    private RoundRule roundRule;
    private List<Printer> printers;
    private List<RestaurantConfig> restaurantConfigs;

    private List<PrinterGroup> printerGroups;
    private List<UserRestaurant> userRestaurant;
    private List<KotNotification> kotNotifications;
    private List<LocalDevice> localDevices;
    private Map<Integer, List<PrinterGroup>> printerGroupMap = new HashMap<>();

    private SubPosBean subPosBean;


    private List<PaymentMethod> pamentMethodList;
    private List<SettlementRestaurant> settlementRestaurant;

    private PrinterDevice device;
    private int trainType;

    public RoundRule getRoundRule() {
        return roundRule;
    }

    public void setRoundRule(RoundRule roundRule) {
        this.roundRule = roundRule;
    }

    private CoreData() {
    }

    public static CoreData getInstance() {
        if (instance == null) {
            instance = new CoreData();
        }
        return instance;
    }

    public void init(Context context) {
        loginResult = Store
                .getObject(context, Store.LOGIN_RESULT, LoginResult.class);
        users = UserSQL.getAllUser();
        revenueCenters = RevenueCenterSQL.getAllRevenueCenter();
        printers = PrinterSQL.getAllPrinter();
        printerGroups = PrinterGroupSQL.getAllPrinterGroup();
        restaurant = RestaurantSQL.getRestaurant();
//		tableList = TablesSQL.getAllTables();
        newTableList = TableInfoSQL.getAllTables();
//		placeList = PlacesSQL.getAllPlaces();
        itemModifiers = ItemModifierSQL.getAllItemModifier();
        itemDetails = ItemDetailSQL.getAllItemDetail();
        itemCategories = ItemCategorySQL.getAllItemCategory();
        itemMainCategories = ItemMainCategorySQL.getAllAvaiableItemMainCategoryInRevenueCenter();//getAllItemMainCategory();
        taxCategories = TaxCategorySQL.getAllTaxCategory();
        modifierList = ModifierSQL.getAllModifier();
        taxs = TaxSQL.getAllTax();
        itemHappyHours = ItemHappyHourSQL.getAllItemHappyHour();
        happyHourWeeks = HappyHourWeekSQL.getAllHappyHourWeek();
        happyHours = HappyHourSQL.getAllHappyHour();
        promotions = PromotionSQL.getAllPromotion();
        promotionOrders = PromotionOrderSQL.getAllpromotionOrder();
        itemPromotions = PromotionItemSQL.getAllPromotionItem();
        promotionWeeks = PromotionWeekSQL.getAllPromotionWeek();
        userRestaurant = UserRestaurantSQL.getAll();
        kotNotifications = KotNotificationSQL.getAllKotNotification();
        localDevices = LocalDeviceSQL.getAllLocalDevice();
        restaurantConfigs = RestaurantConfigSQL.getAllRestaurantConfig();
        settlementRestaurant = SettlementRestaurantSQL.getAllSettlementRestaurant();
        pamentMethodList = PaymentMethodSQL.getAllPaymentMethod();

        trainType = SharedPreferencesHelper.getInt(context, SharedPreferencesHelper.TRAINING_MODE);
        SessionStatus sessionStatus = Store.getObject(
                context, Store.SESSION_STATUS, SessionStatus.class);


//
        //train= SharedPreferencesHelper.pu(this,SharedPreferencesHelper.TRAINING_MODE);
        if (trainType == 1) {


            int first = Store.getInt(context, Store.TRAIN_FIRST, 0);
            if (first == 0) {

                GeneralSQL.deleteAllDataInSubPos();
                Store.remove(context, Store.SESSION_STATUS);
                Store.putInt(context, Store.TRAIN_FIRST, 1);
            }

        } else {

        }

    }

    public PrinterDevice getDevice() {
        return device;
    }

    public void setDevice(PrinterDevice device) {
        this.device = device;
    }

    public List<ItemModifier> getItemModifiers(ItemDetail itemDetail) {
        List<ItemModifier> result = new ArrayList<ItemModifier>();
        if (itemDetail == null || itemDetail.getItemTemplateId() == null) {
            return result;
        }
        for (ItemModifier itemModifier : getItemModifiers()) {
            if (itemModifier.getType() != null && itemModifier.getItemCategoryId() != null
                    && itemModifier.getItemCategoryId().intValue() == itemDetail.getItemCategoryId().intValue()
                    && itemModifier.getType().intValue() == 1) {
                result.add(itemModifier);
            } else if (itemModifier.getItemId() != null
                    && itemDetail.getItemTemplateId() != null
                    && itemModifier.getItemId().intValue() == itemDetail.getItemTemplateId().intValue()) {
                result.add(itemModifier);
            }
        }
        return result;
    }


    public OrderModifier getOrderModifier(List<OrderModifier> orderModifiers,
                                          Modifier modifier) {
        for (OrderModifier orderModifier : orderModifiers) {
            if (modifier.getId().intValue() == orderModifier.getModifierId()
                    .intValue()) {
                return orderModifier;
            }
        }
        return null;
    }

    private Printer getPrinterById(int printerid) {
        for (Printer printer : getPrinters()) {
            if (printerid == printer.getId().intValue()) {
                return printer;
            }
        }
        return null;
    }

    public int isCashierPrinter(int printid) {
        for (Printer printer : getPrinters()) {
            if (printid == printer.getId().intValue()
                    && 1 == printer.getIsCashdrawer()) {
                return 1;
            }
        }
        return 0;
    }

    public Printer getPrinter(ItemDetail itemDetail) {
        if (itemDetail == null || itemDetail.getPrinterId() == null) {
            return null;
        }
        for (Printer printer : getPrinters()) {
            if (itemDetail.getPrinterId().intValue() == printer.getId()
                    .intValue()) {
                return printer;
            }
        }
        return null;
    }

    public Printer getPrinterByName(String name) {
        for (Printer printer : getPrinters()) {
            if (name.equals(printer.getPrinterName())) {
                return printer;
            }
        }
        return null;
    }

    public List<Printer> getKDSPhysicalPrinters() {
        List<Printer> printers = new ArrayList<Printer>();
        for (Printer printer : getPrinters()) {
            if (printer.getType() == ParamConst.PRINTER_TYPE_UNGROUP
                    && printer.getIsCashdrawer() == 0) {
                printers.add(printer);
            }
        }
        return printers;
    }

    public List<Printer> getPhysicalPrinters() {
        List<Printer> printers = new ArrayList<Printer>();
        for (Printer printer : getPrinters()) {
            if (printer.getType() == ParamConst.PRINTER_TYPE_UNGROUP) {
                printers.add(printer);
            }
        }
        return printers;
    }

    public ArrayList<Printer> getPrintersInGroup(int groupid) {
        ArrayList<Printer> result = new ArrayList<Printer>();
        List<Printer> summaryPrinter = new ArrayList<>();
        List<Printer> expediterPrinter = new ArrayList<>();

        for (PrinterGroup pg : getPrinterGroupsById(groupid)) {
            if (pg.getPrinterGroupId().intValue() == groupid) {
                Printer pt = this.getPrinterById(pg.getPrinterId().intValue());
                if (pt == null) continue;
                if (pt.getPrinterUsageType() == Printer.KDS_SUMMARY)
                    summaryPrinter.add(pt);
                else if (pt.getPrinterUsageType() == Printer.KDS_EXPEDITER)
                    expediterPrinter.add(pt);
                else
                    result.add(pt);
            }
        }
        result.addAll(expediterPrinter);//add summary printer before summary
        result.addAll(summaryPrinter);//add summary printer at last
        return result;
    }

    public ArrayList<PrinterGroup> getPrinterGroupInGroup(int groupid) {
        ArrayList<PrinterGroup> result = new ArrayList<>();
        for (PrinterGroup pg : getPrinterGroupsById(groupid)) {
            if (pg.getPrinterGroupId().intValue() == groupid && pg.getIsChildGroup() == 1) {
                result.add(pg);
            }
        }
        return result;
    }

    public Printer getPrinterByGroupId(int printerGroupId) {
        for (Printer printer : getPrinters()) {
            if (printer.getId() == printerGroupId) {
                return printer;
            }
        }
        return null;
    }

    public List<PrinterGroup> getPrinterGroupByPrinter(int printerId) {
        List<PrinterGroup> printerGroupList = new ArrayList<PrinterGroup>();
        for (PrinterGroup pg : this.printerGroups) {
            if (pg.getPrinterId().intValue() == printerId) {
                printerGroupList.add(pg);
            }
        }
        return printerGroupList;
    }

    public PrinterGroup getPrinterGroup(int printerGroupId) {
        for (PrinterGroup pg : getPrinterGroupsById(printerGroupId)) {
            if (pg.getPrinterGroupId().equals(printerGroupId)) {
                return pg;
            }
        }
        return null;
    }

    public List<PrinterGroup> getAllPrinterGroup(int printerGroupId) {
        List<PrinterGroup> printerGroups = new ArrayList<>();
        for (PrinterGroup pg : this.printerGroups) {
            if (pg.getPrinterGroupId().equals(printerGroupId)) {
                printerGroups.add(pg);
            }
        }
        return printerGroups;
    }

    public Modifier getModifier(ItemModifier itemModifier) {
        if (itemModifier == null
                || itemModifier.getModifierCategoryId() == null) {
            return null;
        }
        for (Modifier modifier : getModifierList()) {
            if (modifier.getId().intValue() == itemModifier
                    .getModifierCategoryId().intValue()) {
                return modifier;
            }
        }
        return null;
    }

    public Modifier getModifier(int modifierId) {
        for (Modifier modifier : getModifierList()) {
            if (modifier.getId().intValue() == modifierId) {
                return modifier;
            }
        }
        return null;
    }

    public List<Modifier> getModifiers(Modifier modifier) {
        List<Modifier> result = new ArrayList<Modifier>();
        if (modifier == null || modifier.getId() == null)
            return result;
        for (Modifier temp : getModifierList()) {
            if (modifier.getId().intValue() == temp.getCategoryId().intValue()
                    && temp.getType().intValue() == 1) {
                result.add(temp);
            }
        }
        return result;
    }


    public ItemDetail getItemDetailById(Integer id) {
        if (id == null)
            return null;
        for (ItemDetail itemDetail : getItemDetails()) {
            if (itemDetail.getId().intValue() == id.intValue()) {
                return itemDetail;
            }
        }
        return null;
    }

    public ItemDetail getItemDetailById(Integer id, String name) {
        if (id == null) {
            return null;
        }

        for (ItemDetail itemDetail : getItemDetails()) {
            if (itemDetail.getId().intValue() == id.intValue()) {
                if (!TextUtils.isEmpty(name)) {
                    if (itemDetail.getItemName().equals(name)) {
                        return itemDetail;
                    }
                } else {
                    return itemDetail;
                }

            }

            if (!TextUtils.isEmpty(name)) {
                if (itemDetail.getItemName().equals(name)) {
                    return itemDetail;
                }
            }


        }
        return null;
    }

    public ItemDetail getItemDetailByBarCode(String barcode) {
        if (TextUtils.isEmpty(barcode))
            return null;
        for (ItemDetail itemDetail : getItemDetails()) {
            if (barcode.equals(itemDetail.getBarcode())) {
                return itemDetail;
            }
        }
        return null;
    }

    public ItemDetail getItemDetailByBarCodeForKPMG(String barcode) {
        if (TextUtils.isEmpty(barcode))
            return null;
        for (ItemDetail itemDetail : getItemDetails()) {
            if (barcode.equals(IntegerUtils.format20(itemDetail.getBarcode()))) {
                return itemDetail;
            }
        }
        return null;
    }

    /*: This function CANNNOT be used for Open Item coz all open items have no template ID*/
    public ItemDetail getItemDetailByTemplateId(Integer id) {
        if (id == null || id.intValue() == 0)
            return null;
        for (ItemDetail itemDetail : getItemDetails()) {
            if (itemDetail.getItemTemplateId().intValue() == id.intValue()) {
                return itemDetail;
            }
        }
        return null;
    }

    public ItemHappyHour getItemHappyHour(RevenueCenter revenueCenter,
                                          ItemDetail itemDetail) {
        if (itemDetail == null)
            return null;
        List<ItemHappyHour> itemHappyHours = getItemHappyHours();
        for (ItemHappyHour itemHappyHour1 : itemHappyHours) {
            if (itemHappyHour1.getHappyHourId().intValue() == revenueCenter
                    .getHappyHourId().intValue()) {
                // 先按照菜来找
                if (itemHappyHour1.getItemId().intValue() == itemDetail
                        .getItemTemplateId().intValue()) {
                    return itemHappyHour1;
                }
                // 然后按照分类来找
                if (itemHappyHour1.getItemId().intValue() <= 0) {
                    if (itemHappyHour1.getItemCategoryId().intValue() == itemDetail
                            .getItemCategoryId().intValue()) {
                        return itemHappyHour1;
                    }
                }
                // 最后按照主分类找
                if (itemHappyHour1.getItemId().intValue() <= 0 && itemHappyHour1.getItemCategoryId() <= 0) {
                    if (itemHappyHour1.getItemMainCategoryId().intValue() == itemDetail
                            .getItemMainCategoryId().intValue()) {
                        return itemHappyHour1;
                    }
                }
            }
        }
        return null;
    }

    public ItemPromotion getItemPromotion(RevenueCenter revenueCenter,
                                          ItemDetail itemDetail) {
        if (itemDetail == null)
            return null;
        List<ItemPromotion> itemPromotions = getItemPromotions();
        for (ItemPromotion itemPromotion : itemPromotions) {
//			if (itemPromotion.getPromotionId().intValue() == revenueCenter
//					.getHappyHourId().intValue()) {
            // 先按照菜来找
            if (itemPromotion.getItemId().intValue() == itemDetail
                    .getItemTemplateId().intValue()) {
                return itemPromotion;
            }
            // 然后按照分类来找
            if (itemPromotion.getItemId().intValue() <= 0) {
                if (itemPromotion.getItemCategoryId().intValue() == itemDetail
                        .getItemCategoryId().intValue()) {
                    return itemPromotion;
                }
            }
            // 最后按照主分类找
            if (itemPromotion.getItemId().intValue() <= 0 && itemPromotion.getItemCategoryId() <= 0) {
                if (itemPromotion.getItemMainCategoryId().intValue() == itemDetail
                        .getItemMainCategoryId().intValue()) {
                    return itemPromotion;
                }
            }
//			}
        }
        return null;
    }

    public List<TaxCategory> getTaxCategorys(Integer taxCategoryId) {
        List<TaxCategory> result = new ArrayList<TaxCategory>();
        if (taxCategoryId == null)
            return result;
        TaxCategory currentTaxCategory = null;
        for (TaxCategory taxCategory : getTaxCategories()) {
            if (taxCategory.getId().intValue() == taxCategoryId.intValue()) {
                currentTaxCategory = taxCategory;
                break;
            }
        }
        if (currentTaxCategory != null) {
            for (TaxCategory taxCategory : getTaxCategories()) {
                if (currentTaxCategory.getId().intValue() == taxCategory
                        .getTaxCategoryId().intValue()) {
                    result.add(taxCategory);
                }
            }
        }
        return result;
    }

    public Tax getTax(Integer taxId) {
        if (taxId == null)
            return null;
        for (Tax tax : getTaxs()) {
            if (tax.getId().intValue() == taxId.intValue()) {
                return tax;
            }
        }
        return null;
    }

    public TaxCategory getTaxCategory(Integer taxOnId) {
        if (taxOnId == null)
            return null;
        for (TaxCategory taxCategory : getTaxCategories()) {
            if (taxCategory.getId().intValue() == taxOnId.intValue()) {
                return taxCategory;
            }
        }
        return null;
    }

    public TaxCategory getTaxCategoryByTaxId(Integer taxId) {
        if (taxId == null)
            return null;
        for (TaxCategory taxCategory : getTaxCategories()) {
            if (taxCategory.getTaxId().intValue() == taxId.intValue()) {
                return taxCategory;
            }
        }
        return null;
    }

    public ArrayList<ItemCategory> getItemCategory(
            ItemMainCategory itemMainCategory) {
        if (itemCategories == null)
            return null;
        ArrayList<ItemCategory> result = new ArrayList<ItemCategory>();
        if (itemMainCategory == null || itemMainCategory.getId() == null) {
            return result;
        }
        for (int i = 0; i < itemCategories.size(); i++) {
            ItemCategory itemCategory = itemCategories.get(i);
            if (itemCategory.getItemMainCategoryId().intValue() == itemMainCategory
                    .getId().intValue())
                result.add(itemCategory);
        }
        return result;
    }

    public List<ItemDetail> getItemDetails(String key) {
        if (CommonUtil.isNull(key))
            return null;
        List<ItemDetail> result = new ArrayList<ItemDetail>();
        for (ItemDetail itemDetail : getItemDetails()) {
            if (itemDetail.getItemName().contains(key))
                result.add(itemDetail);
        }
        return result;
    }

    public List<ItemDetail> getItemDetails(Integer itemCategoryId) {
        List<ItemDetail> result = new ArrayList<ItemDetail>();
        for (ItemDetail itemDetail : getItemDetails()) {
            if (itemDetail.getItemCategoryId().intValue() == itemCategoryId.intValue())
                result.add(itemDetail);
        }
        return result;
    }


    public List<ItemDetail> getItemDetails(ItemCategory itemCategory) {
        List<ItemDetail> result = new ArrayList<ItemDetail>();
        for (ItemDetail itemDetail : getItemDetails()) {
            if (itemDetail.getItemCategoryId().intValue() == itemCategory
                    .getId().intValue())
                result.add(itemDetail);
        }
        return result;
    }

    public RevenueCenter getRevenueCenter(Order order) {
        for (RevenueCenter revenueCenter : getRevenueCenters()) {
            if (revenueCenter.getId().intValue() == order.getRevenueId()
                    .intValue()) {
                return revenueCenter;
            }
        }
        return null;
    }

//	public List<Places> getPlaceList(Integer revenueId) {
//		if (placeList == null)
//			return Collections.emptyList();
//		List<Places> result = new ArrayList<Places>();
//		for (Places places : placeList) {
//			if (places.getRevenueId().intValue() == revenueId.intValue()) {
//				result.add(places);
//			}
//		}
//		return result;
//	}

//	public List<Tables> getTableList(Integer revenueId) {
//		if (tableList == null)
//			return Collections.emptyList();
//		List<Tables> result = new ArrayList<Tables>();
//		for (Tables tables : tableList) {
//			if (tables.getRevenueId().intValue() == revenueId.intValue()) {
//				result.add(tables);
//			}
//		}
//		return result;
//	}

//	public List<Tables> getTableList(Integer revenueId, Integer placesId) {
//		if (tableList == null)
//			return Collections.emptyList();
//		List<Tables> result = new ArrayList<Tables>();
//		for (Tables tables : tableList) {
//			if (tables.getRevenueId().intValue() == revenueId.intValue()
//					&& tables.getPlacesId().intValue() == placesId.intValue()) {
//				result.add(tables);
//			}
//		}
//		return result;
//	}

//	public Tables getTables(Integer tablesId) {
//		for (Tables tables : tableList) {
//			if (tables.getId().intValue() == tablesId.intValue()) {
//				return tables;
//			}
//		}
//		return null;
//	}

    public User getUser(String employee_ID, String password) {
        if (CommonUtil.isNull(employee_ID) || CommonUtil.isNull(password))
            return null;
        if (users == null || users.size() == 0) {
            users = UserSQL.getAllUser();
        }
        if (users == null) {
            return null;
        }
        for (User user : users) {
            if (Integer.parseInt(employee_ID) == user.getEmpId().intValue()
                    && password.equals(user.getPassword() + ""))
                return user;

        }
        return null;
    }

    public User getUserByEmpId(int empId) {
        if (users == null || users.size() == 0)
            users = UserSQL.getAllUser();
        if (users == null) {
            return null;
        }
        for (User user : users) {
            if (user.getEmpId().intValue() == empId) {
                return user;
            }
        }
        return null;
    }

    public User getUserByPassword(int pwd) {
        if (users == null || users.size() == 0)
            users = UserSQL.getAllUser();
        if (users == null) {
            return null;
        }
        for (User user : users) {
            if (user.getPassword().equals(String.valueOf(pwd))) {
                return user;
            }
        }
        return null;
    }

    public User getUserById(int uId) {
        if (uId < 1)
            return null;
        if (users == null || users.size() == 0)
            users = UserSQL.getAllUser();
        if (users == null) {
            return null;
        }
        for (User user : users) {
            if (user.getId().intValue() == uId) {
                return user;
            }
        }
        return null;
    }

    public Boolean checkUserInRevcenter(int userId, int restaurantid,
                                        int revenueid) {

        for (UserRestaurant user : userRestaurant) {
            if (user.getUserId().intValue() == userId
                    && user.getRestaurantId().intValue() == restaurantid
                    && user.getRevenueId().intValue() == revenueid) {
                return true;
            }
        }
        return false;
    }

    /*
     * Only Cashier, Manager staff can access Cashier device
     */
    public Boolean checkUserCashierAccessInRevcenter(User mUser,
                                                     int restaurantid, int revenueid) {

        for (UserRestaurant user : userRestaurant) {
            if (user.getUserId().intValue() == mUser.getId().intValue()
                    && user.getRestaurantId().intValue() == restaurantid
                    && user.getRevenueId().intValue() == revenueid) {
                if (mUser.getType() == ParamConst.USER_TYPE_POS
                        || mUser.getType() == ParamConst.USER_TYPE_MANAGER) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Only Cashier, Manager, Waiter staff can access KDS device
     */
    public Boolean checkUserWaiterAccessInRevcenter(int userId,
                                                    int restaurantid, int revenueid) {

        for (UserRestaurant user : userRestaurant) {
            if (user.getUserId().intValue() == userId
                    && user.getRestaurantId().intValue() == restaurantid
                    && user.getRevenueId().intValue() == revenueid) {
                User kdsuser = getUserById(userId);
                if (kdsuser != null
                        && (kdsuser.getType() == ParamConst.USER_TYPE_POS
                        || kdsuser.getType() == ParamConst.USER_TYPE_MANAGER || kdsuser
                        .getType() == ParamConst.USER_TYPE_WAITER)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Only Cashier, Manager, Kitchen staff can access KDS device
     */
    public Boolean checkUserKDSAccessInRevcenter(int userId, int restaurantid,
                                                 int revenueid) {

        for (UserRestaurant user : userRestaurant) {
            if (user.getUserId().intValue() == userId
                    && user.getRestaurantId().intValue() == restaurantid
                    && user.getRevenueId().intValue() == revenueid) {
                User kdsuser = getUserById(userId);
                if (kdsuser != null
                        && (kdsuser.getType() == ParamConst.USER_TYPE_POS
                        || kdsuser.getType() == ParamConst.USER_TYPE_MANAGER || kdsuser
                        .getType() == ParamConst.USER_TYPE_KOT)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean checkUserInRestaurant(int userId, int restaurantid) {

        for (UserRestaurant user : userRestaurant) {
            if (user.getUserId().intValue() == userId
                    && user.getRestaurantId().intValue() == restaurantid) {
                return true;
            }
        }
        return false;
    }

    public List<ItemCategory> getItemCategories(
            ItemMainCategory itemMainCategory) {
        if (itemCategories == null)
            return Collections.emptyList();
        List<ItemCategory> result = new ArrayList<ItemCategory>();
        for (ItemCategory itemCategory : itemCategories) {
            if (itemCategory.getItemMainCategoryId().intValue() == itemMainCategory
                    .getId().intValue())
                result.add(itemCategory);
        }

        return result;
    }

    public List<User> getUsers() {
        if (users == null || users.size() == 0)
            users = UserSQL.getAllUser();
        if (users == null) {
            return Collections.emptyList();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<RevenueCenter> getRevenueCenters() {
        if (revenueCenters == null)
            return Collections.emptyList();
        return revenueCenters;
    }

    public void setRevenueCenters(List<RevenueCenter> revenueCenters) {
        this.revenueCenters = revenueCenters;
    }

    public List<Printer> getPrinters() {
        if (printers == null)
            return Collections.emptyList();
        return printers;
    }

    public List<Printer> getNameOfPrintergroup() {
        List<Printer> groupNames = new ArrayList<Printer>();
        for (Printer printer : this.printers) {
            if (printer.getType().intValue() == 0)
                groupNames.add(printer);
        }
        return groupNames;
    }

    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    public List<PrinterGroup> getPrinterGroups() {
        if (printerGroups == null)
            printerGroups = PrinterGroupSQL.getAllPrinterGroup();
        return printerGroups;
    }

    public List<PrinterGroup> getPrinterGroupsById(int printerGroupId) {
//        if (!printerGroupMap.containsKey(printerGroupId)) {
        SortedMap<Integer, PrinterGroup> sortedMap = new TreeMap<>();

        int seqNumber = 0;
        for (PrinterGroup pg : getPrinterGroups()) {

            if (pg.getPrinterGroupId().equals(printerGroupId)) {
                seqNumber++;
                if (pg.getSequenceNumber() == null || pg.getSequenceNumber() <= 0)
                    pg.setSequenceNumber(seqNumber);

                sortedMap.put(pg.getSequenceNumber(), pg);
            }
        }

        List<PrinterGroup> printerGroupList = new ArrayList<>(sortedMap.values());
        printerGroupMap.put(printerGroupId, printerGroupList);
//        }

        return printerGroupMap.get(printerGroupId);
    }

    public void setPrinterGroups(List<PrinterGroup> printerGroups) {
        this.printerGroups = printerGroups;
    }

    public Restaurant getRestaurant() {
        if (restaurant == null) {
            restaurant = RestaurantSQL.getRestaurant();
        }
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

//	public List<Tables> getTableList() {
//		if (tableList == null)
//			return Collections.emptyList();
//		return tableList;
//	}

//	public void setTableList(List<Tables> tableList) {
//		this.tableList = tableList;
//	}

//	public List<Places> getPlaceList() {
//		if (placeList == null)
//			return Collections.emptyList();
//		return placeList;
//	}

//	public void setPlaceList(List<Places> placeList) {
//		this.placeList = placeList;
//	}

    public List<ItemModifier> getItemModifiers() {
        if (itemModifiers == null)
            return Collections.emptyList();
        return itemModifiers;
    }

    public void setItemModifiers(List<ItemModifier> itemModifiers) {
        this.itemModifiers = itemModifiers;
    }

    public List<ItemDetail> getItemDetails() {
        if (itemDetails == null)
            return Collections.emptyList();
        return itemDetails;
    }

    public void setItemDetails(List<ItemDetail> itemDetails) {
        List<ItemDetail> mItemDetails = new ArrayList<ItemDetail>();
        for (ItemDetail itemDetail : itemDetails) {
            if (itemDetail.getItemType() != ParamConst.ITEMDETAIL_TEMPLATE) {
                mItemDetails.add(itemDetail);
            }
        }
        this.itemDetails = mItemDetails;
    }

    public List<ItemCategory> getItemCategories() {
        if (itemCategories == null)
            return Collections.emptyList();
        return itemCategories;
    }


    public List<ItemCategory> getItemCategoriesorDetail() {
        if (itemCategories == null) {
            return Collections.emptyList();
        }
        List<ItemCategory> mItemCategory = new ArrayList<ItemCategory>();
        List<ItemDetail> itemDetailandCate = new ArrayList<ItemDetail>();
        for (ItemCategory itemCategory : itemCategories) {
            itemDetailandCate.clear();
            for (ItemDetail itemDetail : CoreData.getInstance().getItemDetails()) {
                if (itemDetail.getItemCategoryId().intValue() == itemCategory
                        .getId().intValue()) {
                    itemDetailandCate.add(itemDetail);
                }
            }

            if (itemDetailandCate.size() > 0) {
                mItemCategory.add(itemCategory);
            }

        }
        return mItemCategory;
    }

    public void setItemCategories(List<ItemCategory> itemCategories) {
        this.itemCategories = itemCategories;
    }

    public List<ItemMainCategory> getItemMainCategories() {
        if (itemMainCategories == null)
            return Collections.emptyList();
        itemMainCategories = ItemMainCategorySQL.getAllAvaiableItemMainCategoryInRevenueCenter();
        return itemMainCategories;
    }

    public List<ItemMainCategory> getItemMainCategoriesForSelp() {
        if (itemMainCategoriesForSelfHelp == null)
            itemMainCategoriesForSelfHelp = ItemMainCategorySQL.getAllAvaiableItemMainCategoryInRevenueCenterForSelfHelp();
        return itemMainCategoriesForSelfHelp;
    }

    public void setItemMainCategories(List<ItemMainCategory> itemMainCategories) {
        this.itemMainCategories = itemMainCategories;
    }

    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    public List<PaymentMethod> getPamentMethodList() {
        return pamentMethodList;
    }

    public void setPamentMethodList(List<PaymentMethod> pamentMethodList) {
        this.pamentMethodList = pamentMethodList;
    }

    public PaymentMethod getPaymentMethod(int id) {
        if (pamentMethodList != null) {
            for (PaymentMethod paymentMethod : pamentMethodList) {
                if (paymentMethod.getId().intValue() == id) {
                    return paymentMethod;
                }
            }
        }
        return null;
    }


    public PaymentMethod getPamentMethod(Integer pamentMethodId) {
        if (pamentMethodId == null)
            return null;
        for (PaymentMethod pamentMethod : getPamentMethodList()) {
            if (pamentMethod.getId().intValue() == pamentMethodId.intValue()) {
                return pamentMethod;
            }
        }
        return null;
    }

    public PaymentMethod getPamentMethodByPaymentTypeId(Integer paymentTypeId) {
        if (paymentTypeId == null)
            return null;
        if (pamentMethodList != null && pamentMethodList.size() > 0) {
            for (PaymentMethod pamentMethod : pamentMethodList) {
                if (pamentMethod.getPaymentTypeId().intValue() == paymentTypeId.intValue()) {
                    return pamentMethod;
                }
            }
        }
        return null;
    }

    public List<SettlementRestaurant> getSettlementRestaurant() {
        return settlementRestaurant;
    }

    public void setSettlementRestaurant(List<SettlementRestaurant> settlementRestaurant) {
        this.settlementRestaurant = settlementRestaurant;
    }

    public List<TaxCategory> getTaxCategories() {
        if (taxCategories == null) {
            return new ArrayList<TaxCategory>();
        }
        return taxCategories;
    }

    public void setTaxCategories(List<TaxCategory> taxCategories) {
        this.taxCategories = taxCategories;
    }

    public List<Modifier> getModifierList() {
        return modifierList;
    }

    public void setModifierList(List<Modifier> modifierList) {
        this.modifierList = modifierList;
    }

    public List<Tax> getTaxs() {
        return taxs;
    }

    public void setTaxs(List<Tax> taxs) {
        this.taxs = taxs;
    }

    public List<ItemPromotion> getItemPromotions() {
        return itemPromotions;
    }

    public void setItemPromotions(List<ItemPromotion> itemPromotions) {
        this.itemPromotions = itemPromotions;
    }

    public List<PromotionWeek> getPromotionWeeks() {
        return promotionWeeks;
    }

    public void setPromotionWeeks(List<PromotionWeek> promotionWeeks) {
        this.promotionWeeks = promotionWeeks;
    }

    public List<PromotionOrder> getPromotionOrders() {
        return promotionOrders;
    }

    public void setPromotionOrders(List<PromotionOrder> promotionOrders) {
        this.promotionOrders = promotionOrders;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<ItemHappyHour> getItemHappyHours() {
        return itemHappyHours;
    }

    public void setItemHappyHours(List<ItemHappyHour> itemHappyHours) {
        this.itemHappyHours = itemHappyHours;
    }

    public List<HappyHourWeek> getHappyHourWeeks() {
        if (happyHourWeeks == null)
            return Collections.emptyList();
        return happyHourWeeks;
    }

    public void setHappyHourWeeks(List<HappyHourWeek> happyHourWeeks) {
        this.happyHourWeeks = happyHourWeeks;
    }

    public List<HappyHour> getHappyHours() {
        return happyHours;
    }

    public void setHappyHours(List<HappyHour> happyHours) {
        this.happyHours = happyHours;
    }

    public String getUserKey() {
        for (Map.Entry<Integer, String> entry : userKey.entrySet()) {
            return entry.getValue();
        }
        return "";
    }

    public String getUserKey(int revId) {
        return userKey.get(revId) != null ? userKey.get(revId) : "";
    }

    public void setUserKey(int revId, String userKey) {
        this.userKey.put(revId, userKey);
    }

    public List<UserRestaurant> getUserRestaurant() {
        return userRestaurant;
    }

    public void setUserRestaurant(List<UserRestaurant> userRestaurant) {
        this.userRestaurant = userRestaurant;
    }

    public List<KotNotification> getKotNotifications() {
        return kotNotifications;
    }

    public void setKotNotifications(List<KotNotification> notifications) {
        this.kotNotifications = notifications;
    }

    public List<LocalDevice> getLocalDevices() {
        return localDevices;
    }

    public LocalDevice getLocalDeviceById(int id) {
        LocalDevice ret = null;
        for (LocalDevice item : this.localDevices) {
            if (item.getId() == id) {
                ret = item;
                break;
            }
        }
        return ret;
    }

    public LocalDevice getLocalDeviceByDeviceIdAndIP(int deviceId, String ip) {
        LocalDevice ret = null;
        for (LocalDevice item : this.localDevices) {
            if (item.getDeviceId().intValue() == deviceId
                    && item.getIp().equals(ip)) {
                ret = item;
                break;
            }
        }
        return ret;
    }

    public void setLocalDevices(List<LocalDevice> localDevices) {
        this.localDevices = localDevices;
    }

    public void addLocalDevice(LocalDevice localDevice) {
        LocalDeviceSQL.addLocalDevice(localDevice);

//		if (localDevices == null)
        localDevices = LocalDeviceSQL.getAllLocalDevice();

//		boolean found = false;
//		for (LocalDevice item : localDevices) {
//			if (item.getDeviceType().intValue() == localDevice.getDeviceType().intValue()
//					&& item.getIp().equals(localDevice.getIp())) {
//				found = true;
//			}
//		}
//		if (!found)
//			this.localDevices.add(localDevice);
    }

    public void removeLocalDeviceByDeviceIdAndIP(int deviceId, String ip) {
        LocalDeviceSQL.deleteLocalDeviceByPrinterIdAndIP(deviceId, ip);
        for (Iterator<LocalDevice> it = localDevices.listIterator(); it
                .hasNext(); ) {
            LocalDevice item = it.next();
            if (item.getDeviceId().intValue() == deviceId
                    && item.getIp().equals(ip)) {
                it.remove();
            }
        }
    }

    public List<RestaurantConfig> getRestaurantConfigs() {
        if (restaurantConfigs == null) {
            return Collections.emptyList();
        }
        return restaurantConfigs;
    }

    public void setRestaurantConfigs(List<RestaurantConfig> restaurantConfigs) {
        this.restaurantConfigs = restaurantConfigs;
    }

    public List<TaxCategory> getTaxCategoryGroup() {
        List<TaxCategory> taxCategoryList = new ArrayList<TaxCategory>();
        for (TaxCategory taxCategory : taxCategories) {
            if (IntegerUtils.isEmptyOrZero(taxCategory.getTaxCategoryId()) && IntegerUtils.isEmptyOrZero(taxCategory.getTaxId())) {
                taxCategoryList.add(taxCategory);
            }
        }
        taxCategoryList.add(new TaxCategory());
        return taxCategoryList;
    }

    public List<TableInfo> getNewTableListByPlace(int placeId) {
        List<TableInfo> newTables = new ArrayList<TableInfo>();
        for (TableInfo newTable : newTableList) {
            if (newTable.getPlacesId().intValue() == placeId) {
                newTables.add(newTable);
            }
        }
        return newTables;
    }

}
