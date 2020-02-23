package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailSQL {

    public static void updateItemDetail(ItemDetail itemDetail) {
        if (itemDetail == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.ItemDetail
                    + "(id, restaurantId, itemTemplateId, revenueId, itemName, itemDesc, itemCode, imgUrl, price, itemType, printerId, isModifier, itemMainCategoryId, "
                    + "itemCategoryId, isActive, taxCategoryId, isPack, isTakeout, happyHoursId, userId, createTime, updateTime, indexId, isDiscount, barcode)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{itemDetail.getId(),
                            itemDetail.getRestaurantId(),
                            itemDetail.getItemTemplateId(),
                            itemDetail.getRevenueId(),
                            itemDetail.getItemName(), itemDetail.getItemDesc(),
                            itemDetail.getItemCode(), itemDetail.getImgUrl(),
                            itemDetail.getPrice(), itemDetail.getItemType(),
                            itemDetail.getPrinterId(),
                            itemDetail.getIsModifier(),
                            itemDetail.getItemMainCategoryId(),
                            itemDetail.getItemCategoryId(),
                            itemDetail.getIsActive(),
                            itemDetail.getTaxCategoryId(),
                            itemDetail.getIsPack(), itemDetail.getIsTakeout(),
                            itemDetail.getHappyHoursId(),
                            itemDetail.getUserId(), itemDetail.getCreateTime(),
                            itemDetail.getUpdateTime(), itemDetail.getIndexId(),
                            itemDetail.getIsDiscount(), itemDetail.getBarcode()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addItemDetailByLocal(ItemDetail itemDetail) {
        if (itemDetail == null) {
            return;
        }
        try {
            String sql = "insert into "
                    + TableNames.ItemDetail
                    + "(restaurantId, itemTemplateId, revenueId, itemName, itemDesc, itemCode, imgUrl, price, itemType, printerId, isModifier, itemMainCategoryId, "
                    + "itemCategoryId, isActive, taxCategoryId, isPack, isTakeout, happyHoursId, userId, createTime, updateTime, indexId, isDiscount, barcode)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{itemDetail.getRestaurantId(),
                            itemDetail.getItemTemplateId(),
                            itemDetail.getRevenueId(),
                            itemDetail.getItemName(), itemDetail.getItemDesc(),
                            itemDetail.getItemCode(), itemDetail.getImgUrl(),
                            itemDetail.getPrice(), itemDetail.getItemType(),
                            itemDetail.getPrinterId(),
                            itemDetail.getIsModifier(),
                            itemDetail.getItemMainCategoryId(),
                            itemDetail.getItemCategoryId(),
                            itemDetail.getIsActive(),
                            itemDetail.getTaxCategoryId(),
                            itemDetail.getIsPack(), itemDetail.getIsTakeout(),
                            itemDetail.getHappyHoursId(),
                            itemDetail.getUserId(), itemDetail.getCreateTime(),
                            itemDetail.getUpdateTime(), itemDetail.getIndexId(),
                            itemDetail.getIsDiscount(), itemDetail.getBarcode()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addItemDetailList(List<ItemDetail> itemDetailList) {
        if (itemDetailList == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.ItemDetail
                    + "(id, restaurantId, itemTemplateId, revenueId, itemName, itemDesc, itemCode, imgUrl, price, itemType, printerId, isModifier, itemMainCategoryId, "
                    + "itemCategoryId, isActive, taxCategoryId, isPack, isTakeout, happyHoursId, userId, createTime, updateTime, indexId, isDiscount, barcode)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (ItemDetail itemDetail : itemDetailList) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        itemDetail.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        itemDetail.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        itemDetail.getItemTemplateId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        itemDetail.getRevenueId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        itemDetail.getItemName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6,
                        itemDetail.getItemDesc());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        itemDetail.getItemCode());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        itemDetail.getImgUrl());
                SQLiteStatementHelper.bindString(sqLiteStatement, 9,
                        itemDetail.getPrice());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        itemDetail.getItemType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        itemDetail.getPrinterId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        itemDetail.getIsModifier());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
                        itemDetail.getItemMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
                        itemDetail.getItemCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
                        itemDetail.getIsActive());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        itemDetail.getTaxCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 17,
                        itemDetail.getIsPack());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 18,
                        itemDetail.getIsTakeout());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 19,
                        itemDetail.getHappyHoursId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
                        itemDetail.getUserId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 21,
                        itemDetail.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
                        itemDetail.getUpdateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
                        itemDetail.getIndexId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 24,
                        itemDetail.getIsDiscount());
                SQLiteStatementHelper.bindString(sqLiteStatement, 25,
                        itemDetail.getBarcode());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<ItemDetail> getAllItemDetail() {
        ArrayList<ItemDetail> result = new ArrayList<ItemDetail>();
        String sql = "select * from " + TableNames.ItemDetail + " where isActive = 1 and itemType <> " + ParamConst.ITEMDETAIL_TEMPLATE + " and (itemTemplateId <> 0 or itemType == " + ParamConst.ITEMDETAIL_TEMP_ITEM + ") order by indexId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemDetail itemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemDetail = new ItemDetail();
                itemDetail.setId(cursor.getInt(0));
                itemDetail.setRestaurantId(cursor.getInt(1));
                itemDetail.setItemTemplateId(cursor.getInt(2));
                itemDetail.setRevenueId(cursor.getInt(3));
                itemDetail.setItemName(cursor.getString(4));
                itemDetail.setItemDesc(cursor.getString(5));
                itemDetail.setItemCode(cursor.getString(6));
                itemDetail.setImgUrl(cursor.getString(7));
                itemDetail.setPrice(cursor.getString(8));
                itemDetail.setItemType(cursor.getInt(9));
                itemDetail.setPrinterId(cursor.getInt(10));
                itemDetail.setIsModifier(cursor.getInt(11));
                itemDetail.setItemMainCategoryId(cursor.getInt(12));
                itemDetail.setItemCategoryId(cursor.getInt(13));
                itemDetail.setIsActive(cursor.getInt(14));
                itemDetail.setTaxCategoryId(cursor.getInt(15));
                itemDetail.setIsPack(cursor.getInt(16));
                itemDetail.setIsTakeout(cursor.getInt(17));
                itemDetail.setHappyHoursId(cursor.getInt(18));
                itemDetail.setUserId(cursor.getInt(19));
                itemDetail.setCreateTime(cursor.getLong(20));
                itemDetail.setUpdateTime(cursor.getLong(21));
                itemDetail.setIndexId(cursor.getInt(22));
                itemDetail.setIsDiscount(cursor.getInt(23));
                itemDetail.setBarcode(cursor.getString(24));
                result.add(itemDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }


    public static ArrayList<ItemDetail> getAllItemDetailForReport() {
        ArrayList<ItemDetail> result = new ArrayList<ItemDetail>();
        String sql = "select * from " + TableNames.ItemDetail + " where itemType <> " + ParamConst.ITEMDETAIL_TEMPLATE + " and (itemTemplateId <> 0 or itemType == " + ParamConst.ITEMDETAIL_TEMP_ITEM + ") order by indexId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemDetail itemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemDetail = new ItemDetail();
                itemDetail.setId(cursor.getInt(0));
                itemDetail.setRestaurantId(cursor.getInt(1));
                itemDetail.setItemTemplateId(cursor.getInt(2));
                itemDetail.setRevenueId(cursor.getInt(3));
                itemDetail.setItemName(cursor.getString(4));
                itemDetail.setItemDesc(cursor.getString(5));
                itemDetail.setItemCode(cursor.getString(6));
                itemDetail.setImgUrl(cursor.getString(7));
                itemDetail.setPrice(cursor.getString(8));
                itemDetail.setItemType(cursor.getInt(9));
                itemDetail.setPrinterId(cursor.getInt(10));
                itemDetail.setIsModifier(cursor.getInt(11));
                itemDetail.setItemMainCategoryId(cursor.getInt(12));
                itemDetail.setItemCategoryId(cursor.getInt(13));
                itemDetail.setIsActive(cursor.getInt(14));
                itemDetail.setTaxCategoryId(cursor.getInt(15));
                itemDetail.setIsPack(cursor.getInt(16));
                itemDetail.setIsTakeout(cursor.getInt(17));
                itemDetail.setHappyHoursId(cursor.getInt(18));
                itemDetail.setUserId(cursor.getInt(19));
                itemDetail.setCreateTime(cursor.getLong(20));
                itemDetail.setUpdateTime(cursor.getLong(21));
                itemDetail.setIndexId(cursor.getInt(22));
                itemDetail.setIsDiscount(cursor.getInt(23));
                itemDetail.setBarcode(cursor.getString(24));
                result.add(itemDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static ArrayList<ItemDetail> getAllTempItemDetail() {
        ArrayList<ItemDetail> result = new ArrayList<ItemDetail>();
        String sql = "select * from " + TableNames.ItemDetail
                + " where itemType = ? and isActive = 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{ParamConst.ITEMDETAIL_TEMP_ITEM + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemDetail itemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemDetail = new ItemDetail();
                itemDetail.setId(cursor.getInt(0));
                itemDetail.setRestaurantId(cursor.getInt(1));
                itemDetail.setItemTemplateId(cursor.getInt(2));
                itemDetail.setRevenueId(cursor.getInt(3));
                itemDetail.setItemName(cursor.getString(4));
                itemDetail.setItemDesc(cursor.getString(5));
                itemDetail.setItemCode(cursor.getString(6));
                itemDetail.setImgUrl(cursor.getString(7));
                itemDetail.setPrice(cursor.getString(8));
                itemDetail.setItemType(cursor.getInt(9));
                itemDetail.setPrinterId(cursor.getInt(10));
                itemDetail.setIsModifier(cursor.getInt(11));
                itemDetail.setItemMainCategoryId(cursor.getInt(12));
                itemDetail.setItemCategoryId(cursor.getInt(13));
                itemDetail.setIsActive(cursor.getInt(14));
                itemDetail.setTaxCategoryId(cursor.getInt(15));
                itemDetail.setIsPack(cursor.getInt(16));
                itemDetail.setIsTakeout(cursor.getInt(17));
                itemDetail.setHappyHoursId(cursor.getInt(18));
                itemDetail.setUserId(cursor.getInt(19));
                itemDetail.setCreateTime(cursor.getLong(20));
                itemDetail.setUpdateTime(cursor.getLong(21));
                itemDetail.setIndexId(cursor.getInt(22));
                itemDetail.setIsDiscount(cursor.getInt(23));
                itemDetail.setBarcode(cursor.getString(24));
                result.add(itemDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static ItemDetail getItemDetailById(int itemId) {
        return getItemDetailById(itemId, null);
    }

    public static ItemDetail getItemDetailById(int itemId, String name) {
        String query = " where id = ? and isActive = 1";
        if(!TextUtils.isEmpty(name)){
            query = " where (id = ? or itemName = '"+name+"') and isActive = 1";
        }
        String sql = "select * from " + TableNames.ItemDetail + query;
        Cursor cursor = null;
        ItemDetail itemDetail = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{String.valueOf(itemId)});

            if (cursor.moveToFirst()) {
                itemDetail = new ItemDetail();
                itemDetail.setId(cursor.getInt(0));
                itemDetail.setRestaurantId(cursor.getInt(1));
                itemDetail.setItemTemplateId(cursor.getInt(2));
                itemDetail.setRevenueId(cursor.getInt(3));
                itemDetail.setItemName(cursor.getString(4));
                itemDetail.setItemDesc(cursor.getString(5));
                itemDetail.setItemCode(cursor.getString(6));
                itemDetail.setImgUrl(cursor.getString(7));
                itemDetail.setPrice(cursor.getString(8));
                itemDetail.setItemType(cursor.getInt(9));
                itemDetail.setPrinterId(cursor.getInt(10));
                itemDetail.setIsModifier(cursor.getInt(11));
                itemDetail.setItemMainCategoryId(cursor.getInt(12));
                itemDetail.setItemCategoryId(cursor.getInt(13));
                itemDetail.setIsActive(cursor.getInt(14));
                itemDetail.setTaxCategoryId(cursor.getInt(15));
                itemDetail.setIsPack(cursor.getInt(16));
                itemDetail.setIsTakeout(cursor.getInt(17));
                itemDetail.setHappyHoursId(cursor.getInt(18));
                itemDetail.setUserId(cursor.getInt(19));
                itemDetail.setCreateTime(cursor.getLong(20));
                itemDetail.setUpdateTime(cursor.getLong(21));
                itemDetail.setIndexId(cursor.getInt(22));
                itemDetail.setIsDiscount(cursor.getInt(23));
                itemDetail.setBarcode(cursor.getString(24));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return itemDetail;
    }

    public static ItemDetail getItemDetailByName(Integer revId, String name) {

        String query = " where itemName = ? and isActive = 1 and revenueId = ?";
        String sql = "select * from " + TableNames.ItemDetail + query;
        Cursor cursor = null;
        ItemDetail itemDetail = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{name, String.valueOf(revId)});

            if (cursor.moveToFirst()) {
                itemDetail = new ItemDetail();
                itemDetail.setId(cursor.getInt(0));
                itemDetail.setRestaurantId(cursor.getInt(1));
                itemDetail.setItemTemplateId(cursor.getInt(2));
                itemDetail.setRevenueId(cursor.getInt(3));
                itemDetail.setItemName(cursor.getString(4));
                itemDetail.setItemDesc(cursor.getString(5));
                itemDetail.setItemCode(cursor.getString(6));
                itemDetail.setImgUrl(cursor.getString(7));
                itemDetail.setPrice(cursor.getString(8));
                itemDetail.setItemType(cursor.getInt(9));
                itemDetail.setPrinterId(cursor.getInt(10));
                itemDetail.setIsModifier(cursor.getInt(11));
                itemDetail.setItemMainCategoryId(cursor.getInt(12));
                itemDetail.setItemCategoryId(cursor.getInt(13));
                itemDetail.setIsActive(cursor.getInt(14));
                itemDetail.setTaxCategoryId(cursor.getInt(15));
                itemDetail.setIsPack(cursor.getInt(16));
                itemDetail.setIsTakeout(cursor.getInt(17));
                itemDetail.setHappyHoursId(cursor.getInt(18));
                itemDetail.setUserId(cursor.getInt(19));
                itemDetail.setCreateTime(cursor.getLong(20));
                itemDetail.setUpdateTime(cursor.getLong(21));
                itemDetail.setIndexId(cursor.getInt(22));
                itemDetail.setIsDiscount(cursor.getInt(23));
                itemDetail.setBarcode(cursor.getString(24));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return itemDetail;
    }


    public static int getAllTempItemDetailByTime(long time, long dayZero, int itemType) {
        int count = 0;
        String sql = "select count(*) from " + TableNames.ItemDetail
                + " where createTime < ? and createTime > ? and itemType = ? and isActive = 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(time), String.valueOf(dayZero), String.valueOf(itemType)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }


    public static void deleteItemDetail(ItemDetail itemDetail) {
        String sql = "delete from " + TableNames.ItemDetail + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{itemDetail.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllItemDetail() {
        String sql = "delete from " + TableNames.ItemDetail;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateItemNameForTest(int itemId, String itemName) {
        String sql = "update " + TableNames.ItemDetail + " set itemName = ? where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{itemName, itemId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
