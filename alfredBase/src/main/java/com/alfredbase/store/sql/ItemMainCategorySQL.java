package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemMainCategorySQL {

    public static void addItemMainCategory(ItemMainCategory itemMainCategory) {
        if (itemMainCategory == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.ItemMainCategory
                    + "(id,mainCategoryName, color,restaurantId,isActive,indexId,userId,printerGroupId,createTime,updateTime,isShowDiner)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{itemMainCategory.getId(),
                            itemMainCategory.getMainCategoryName(),
                            itemMainCategory.getColor(),
                            itemMainCategory.getRestaurantId(),
                            itemMainCategory.getIsActive(),
                            itemMainCategory.getIndexId(),
                            itemMainCategory.getUserId(),
                            itemMainCategory.getPrinterGroupId(),
                            itemMainCategory.getCreateTime(),
                            itemMainCategory.getUpdateTime(),
                            itemMainCategory.getIsShowDiner()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addItemMainCategory(
            List<ItemMainCategory> itemMainCategories) {
        if (itemMainCategories == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.ItemMainCategory
                    + "(id,mainCategoryName, color,restaurantId,isActive,indexId,userId,printerGroupId,createTime,updateTime,isShowDiner)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (ItemMainCategory itemMainCategory : itemMainCategories) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        itemMainCategory.getId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 2,
                        itemMainCategory.getMainCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3,
                        itemMainCategory.getColor());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        itemMainCategory.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        itemMainCategory.getIsActive());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        itemMainCategory.getIndexId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        itemMainCategory.getUserId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        itemMainCategory.getPrinterGroupId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        itemMainCategory.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        itemMainCategory.getUpdateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        itemMainCategory.getIsShowDiner());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<ItemMainCategory> getAllItemMainCategory() {
        ArrayList<ItemMainCategory> result = new ArrayList<ItemMainCategory>();
        String sql = "select * from " + TableNames.ItemMainCategory + " where isActive = 1 order by indexId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemMainCategory itemMainCategory = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemMainCategory = new ItemMainCategory();
                itemMainCategory.setId(cursor.getInt(0));
                itemMainCategory.setMainCategoryName(cursor.getString(1));
                itemMainCategory.setColor(cursor.getString(2));
                itemMainCategory.setRestaurantId(cursor.getInt(3));
                itemMainCategory.setIsActive(cursor.getInt(4));
                itemMainCategory.setIndexId(cursor.getInt(5));
                itemMainCategory.setUserId(cursor.getInt(6));
                itemMainCategory.setPrinterGroupId(cursor.getInt(7));
                itemMainCategory.setCreateTime(cursor.getLong(8));
                itemMainCategory.setUpdateTime(cursor.getLong(9));
                itemMainCategory.setIsShowDiner(cursor.getInt(10));
                result.add(itemMainCategory);
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

    public static ArrayList<ItemMainCategory> getAllItemMainCategoryForReport() {
        ArrayList<ItemMainCategory> result = new ArrayList<ItemMainCategory>();
        String sql = "select * from " + TableNames.ItemMainCategory + " order by indexId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemMainCategory itemMainCategory = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemMainCategory = new ItemMainCategory();
                itemMainCategory.setId(cursor.getInt(0));
                itemMainCategory.setMainCategoryName(cursor.getString(1));
                itemMainCategory.setColor(cursor.getString(2));
                itemMainCategory.setRestaurantId(cursor.getInt(3));
                itemMainCategory.setIsActive(cursor.getInt(4));
                itemMainCategory.setIndexId(cursor.getInt(5));
                itemMainCategory.setUserId(cursor.getInt(6));
                itemMainCategory.setPrinterGroupId(cursor.getInt(7));
                itemMainCategory.setCreateTime(cursor.getLong(8));
                itemMainCategory.setUpdateTime(cursor.getLong(9));
                itemMainCategory.setIsShowDiner(cursor.getInt(10));
                result.add(itemMainCategory);
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

    // Add: revenue center menu has own menu
    public static ArrayList<ItemMainCategory> getAllAvaiableItemMainCategoryInRevenueCenter() {
        ArrayList<ItemMainCategory> result = new ArrayList<ItemMainCategory>();
        String sql = "select * from " + TableNames.ItemMainCategory + " where isActive = 1 and id IN (select distinct itemMainCategoryId from " + TableNames.ItemDetail + " where isActive = 1) order by indexId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemMainCategory itemMainCategory = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemMainCategory = new ItemMainCategory();
                itemMainCategory.setId(cursor.getInt(0));
                itemMainCategory.setMainCategoryName(cursor.getString(1));
                itemMainCategory.setColor(cursor.getString(2));
                itemMainCategory.setRestaurantId(cursor.getInt(3));
                itemMainCategory.setIsActive(cursor.getInt(4));
                itemMainCategory.setIndexId(cursor.getInt(5));
                itemMainCategory.setUserId(cursor.getInt(6));
                itemMainCategory.setPrinterGroupId(cursor.getInt(7));
                itemMainCategory.setCreateTime(cursor.getLong(8));
                itemMainCategory.setUpdateTime(cursor.getLong(9));
                itemMainCategory.setIsShowDiner(cursor.getInt(10));
                result.add(itemMainCategory);
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

    public static ArrayList<ItemMainCategory> getAllAvaiableItemMainCategoryInRevenueCenterForSelfHelp() {
        ArrayList<ItemMainCategory> result = new ArrayList<ItemMainCategory>();
        String sql = "select * from " + TableNames.ItemMainCategory + " where isActive = 1 and isShowDiner = 1 and id IN (select distinct itemMainCategoryId from " + TableNames.ItemDetail + " where isActive = 1) order by indexId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemMainCategory itemMainCategory = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemMainCategory = new ItemMainCategory();
                itemMainCategory.setId(cursor.getInt(0));
                itemMainCategory.setMainCategoryName(cursor.getString(1));
                itemMainCategory.setColor(cursor.getString(2));
                itemMainCategory.setRestaurantId(cursor.getInt(3));
                itemMainCategory.setIsActive(cursor.getInt(4));
                itemMainCategory.setIndexId(cursor.getInt(5));
                itemMainCategory.setUserId(cursor.getInt(6));
                itemMainCategory.setPrinterGroupId(cursor.getInt(7));
                itemMainCategory.setCreateTime(cursor.getLong(8));
                itemMainCategory.setUpdateTime(cursor.getLong(9));
                itemMainCategory.setIsShowDiner(cursor.getInt(10));
                result.add(itemMainCategory);
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

    public static ItemMainCategory getItemMainCategoryById(int itemMainCategoryId) {
        String sql = "select * from " + TableNames.ItemMainCategory + " where id = ? and isActive = 1";
        Cursor cursor = null;
        ItemMainCategory itemMainCategory = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(itemMainCategoryId)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemMainCategory = new ItemMainCategory();
                itemMainCategory.setId(cursor.getInt(0));
                itemMainCategory.setMainCategoryName(cursor.getString(1));
                itemMainCategory.setColor(cursor.getString(2));
                itemMainCategory.setRestaurantId(cursor.getInt(3));
                itemMainCategory.setIsActive(cursor.getInt(4));
                itemMainCategory.setIndexId(cursor.getInt(5));
                itemMainCategory.setUserId(cursor.getInt(6));
                itemMainCategory.setPrinterGroupId(cursor.getInt(7));
                itemMainCategory.setCreateTime(cursor.getLong(8));
                itemMainCategory.setUpdateTime(cursor.getLong(9));
                itemMainCategory.setIsShowDiner(cursor.getInt(10));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return itemMainCategory;
    }

    public static ItemMainCategory getItemMainCategoryByIdForReport(int itemMainCategoryId) {
        String sql = "select * from " + TableNames.ItemMainCategory + " where id = ? ";
        Cursor cursor = null;
        ItemMainCategory itemMainCategory = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(itemMainCategoryId)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemMainCategory = new ItemMainCategory();
                itemMainCategory.setId(cursor.getInt(0));
                itemMainCategory.setMainCategoryName(cursor.getString(1));
                itemMainCategory.setColor(cursor.getString(2));
                itemMainCategory.setRestaurantId(cursor.getInt(3));
                itemMainCategory.setIsActive(cursor.getInt(4));
                itemMainCategory.setIndexId(cursor.getInt(5));
                itemMainCategory.setUserId(cursor.getInt(6));
                itemMainCategory.setPrinterGroupId(cursor.getInt(7));
                itemMainCategory.setCreateTime(cursor.getLong(8));
                itemMainCategory.setUpdateTime(cursor.getLong(9));
                itemMainCategory.setIsShowDiner(cursor.getInt(10));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return itemMainCategory;
    }

    public static void deleteItemMainCategory(ItemMainCategory itemMainCategory) {
        String sql = "delete from " + TableNames.ItemMainCategory
                + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql,
                    new Object[]{itemMainCategory.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllItemMainCategory() {
        String sql = "delete from " + TableNames.ItemMainCategory;
        try {
            SQLExe.getDB().execSQL(sql,
                    new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
