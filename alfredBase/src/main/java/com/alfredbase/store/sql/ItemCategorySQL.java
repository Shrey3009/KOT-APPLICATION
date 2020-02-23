package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemCategorySQL {

    public static void addItemCategory(ItemCategory itemCategory) {
        if (itemCategory == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.ItemCategory
                    + "(id, itemCategoryName, superCategoryId, color, itemMainCategoryId, restaurantId, isActive, indexId, printerGroupId, userId, createTime, updateTime,imgUrl)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{itemCategory.getId(),
                            itemCategory.getItemCategoryName(),
                            itemCategory.getSuperCategoryId(),
                            itemCategory.getColor(),
                            itemCategory.getItemMainCategoryId(),
                            itemCategory.getRestaurantId(),
                            itemCategory.getIsActive(),
                            itemCategory.getIndexId(),
                            itemCategory.getPrinterGroupId(),
                            itemCategory.getUserId(),
                            itemCategory.getCreateTime(),
                            itemCategory.getUpdateTime(),
                             itemCategory.getImgUrl()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addItemCategoryList(List<ItemCategory> itemCategoryList) {
        if (itemCategoryList == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.ItemCategory
                    + "(id, itemCategoryName, superCategoryId, color, itemMainCategoryId, restaurantId, isActive, indexId, printerGroupId, userId, createTime, updateTime,imgUrl)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (ItemCategory itemCategory : itemCategoryList) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        itemCategory.getId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 2,
                        itemCategory.getItemCategoryName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        itemCategory.getSuperCategoryId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 4,
                        itemCategory.getColor());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        itemCategory.getItemMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        itemCategory.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        itemCategory.getIsActive());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        itemCategory.getIndexId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        itemCategory.getPrinterGroupId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        itemCategory.getUserId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        itemCategory.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        itemCategory.getUpdateTime());
                SQLiteStatementHelper.bindString(sqLiteStatement, 13,
                        itemCategory.getImgUrl());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public static ArrayList<ItemCategory> getAllItemCategory() {
        ArrayList<ItemCategory> result = new ArrayList<ItemCategory>();
        String sql = "select * from " + TableNames.ItemCategory + " where isActive = 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemCategory itemCategory = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemCategory = new ItemCategory();
                itemCategory.setId(cursor.getInt(0));
                itemCategory.setItemCategoryName(cursor.getString(1));
                itemCategory.setSuperCategoryId(cursor.getInt(2));
                itemCategory.setColor(cursor.getString(3));
                itemCategory.setItemMainCategoryId(cursor.getInt(4));
                itemCategory.setRestaurantId(cursor.getInt(5));
                itemCategory.setIsActive(cursor.getInt(6));
                itemCategory.setIndexId(cursor.getInt(7));
                itemCategory.setPrinterGroupId(cursor.getInt(8));
                itemCategory.setUserId(cursor.getInt(9));
                itemCategory.setCreateTime(cursor.getLong(10));
                itemCategory.setUpdateTime(cursor.getLong(11));
                itemCategory.setImgUrl(cursor.getString(12));
                result.add(itemCategory);
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


    public static ArrayList<ItemCategory> getAllItemCategoryForReport() {
        ArrayList<ItemCategory> result = new ArrayList<ItemCategory>();
        String sql = "select * from " + TableNames.ItemCategory;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemCategory itemCategory = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemCategory = new ItemCategory();
                itemCategory.setId(cursor.getInt(0));
                itemCategory.setItemCategoryName(cursor.getString(1));
                itemCategory.setSuperCategoryId(cursor.getInt(2));
                itemCategory.setColor(cursor.getString(3));
                itemCategory.setItemMainCategoryId(cursor.getInt(4));
                itemCategory.setRestaurantId(cursor.getInt(5));
                itemCategory.setIsActive(cursor.getInt(6));
                itemCategory.setIndexId(cursor.getInt(7));
                itemCategory.setPrinterGroupId(cursor.getInt(8));
                itemCategory.setUserId(cursor.getInt(9));
                itemCategory.setCreateTime(cursor.getLong(10));
                itemCategory.setUpdateTime(cursor.getLong(11));
                itemCategory.setImgUrl(cursor.getString(12));
                result.add(itemCategory);
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

    public static ItemCategory getItemCategoryById(int itemCategoryId) {
        String sql = "select * from " + TableNames.ItemCategory + " where id = ? and isActive = 1";
        ItemCategory itemCategory = new ItemCategory();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(itemCategoryId)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemCategory.setId(cursor.getInt(0));
                itemCategory.setItemCategoryName(cursor.getString(1));
                itemCategory.setSuperCategoryId(cursor.getInt(2));
                itemCategory.setColor(cursor.getString(3));
                itemCategory.setItemMainCategoryId(cursor.getInt(4));
                itemCategory.setRestaurantId(cursor.getInt(5));
                itemCategory.setIsActive(cursor.getInt(6));
                itemCategory.setIndexId(cursor.getInt(7));
                itemCategory.setPrinterGroupId(cursor.getInt(8));
                itemCategory.setUserId(cursor.getInt(9));
                itemCategory.setCreateTime(cursor.getLong(10));
                itemCategory.setUpdateTime(cursor.getLong(11));
                itemCategory.setImgUrl(cursor.getString(12));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return itemCategory;
    }


    public static ItemCategory getItemCategoryByIdForReport(int itemCategoryId) {
        String sql = "select * from " + TableNames.ItemCategory + " where id = ?";
        ItemCategory itemCategory = new ItemCategory();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(itemCategoryId)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                itemCategory.setId(cursor.getInt(0));
                itemCategory.setItemCategoryName(cursor.getString(1));
                itemCategory.setSuperCategoryId(cursor.getInt(2));
                itemCategory.setColor(cursor.getString(3));
                itemCategory.setItemMainCategoryId(cursor.getInt(4));
                itemCategory.setRestaurantId(cursor.getInt(5));
                itemCategory.setIsActive(cursor.getInt(6));
                itemCategory.setIndexId(cursor.getInt(7));
                itemCategory.setPrinterGroupId(cursor.getInt(8));
                itemCategory.setUserId(cursor.getInt(9));
                itemCategory.setCreateTime(cursor.getLong(10));
                itemCategory.setUpdateTime(cursor.getLong(11));
                itemCategory.setImgUrl(cursor.getString(12));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return itemCategory;
    }

    public static void deleteItemCategory(ItemCategory itemCategory) {
        String sql = "delete from " + TableNames.ItemCategory + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{itemCategory.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllItemCategory() {
        String sql = "delete from " + TableNames.ItemCategory;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
