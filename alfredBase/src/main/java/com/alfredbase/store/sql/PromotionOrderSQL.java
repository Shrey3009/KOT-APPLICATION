package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.PromotionOrder;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PromotionOrderSQL {
    
    public static void addPromotionOrder(List<PromotionOrder> promotionOrders) {
        if (promotionOrders == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();

            String sql = "insert into "
                    + TableNames.PromotionOrder
                    + "(id, promotionId, itemMainCategoryId, itemCategoryId, itemId,type," +
                    "discountPrice,discountPercentage,freeNum,freeItemId,itemMainCategoryName," +
                    "itemCategoryName,itemName,freeItemName,createTime,updateTime,basePrice)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (PromotionOrder promotionOrder : promotionOrders) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        promotionOrder.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        promotionOrder.getPromotionId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        promotionOrder.getItemMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        promotionOrder.getItemCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        promotionOrder.getItemId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        promotionOrder.getType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        promotionOrder.getDiscountPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        promotionOrder.getDiscountPercentage());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        promotionOrder.getFreeNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        promotionOrder.getFreeItemId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 11,
                        promotionOrder.getItemMainCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 12,
                        promotionOrder.getItemCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 13,
                        promotionOrder.getItemName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
                        promotionOrder.getFreeItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
                        promotionOrder.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        promotionOrder.getUpdateTime());
                SQLiteStatementHelper.bindString(sqLiteStatement, 17,
                        promotionOrder.getBasePrice());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<PromotionOrder> getAllpromotionOrder() {
        ArrayList<PromotionOrder> result = new ArrayList<PromotionOrder>();
        String sql = "select * from " + TableNames.PromotionOrder + " order by id desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            PromotionOrder promotionOrder = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {

                promotionOrder = new PromotionOrder();
                promotionOrder.setId(cursor.getInt(0));
                promotionOrder.setPromotionId(cursor.getInt(1));
                promotionOrder.setItemMainCategoryId(cursor.getInt(2));
                promotionOrder.setItemCategoryId(cursor.getInt(3));
                promotionOrder.setItemId(cursor.getInt(4));
                promotionOrder.setType(cursor.getInt(5));
                promotionOrder.setDiscountPrice(cursor.getString(6));
                promotionOrder.setDiscountPercentage(cursor.getString(7));
                promotionOrder.setFreeNum(cursor.getInt(8));
                promotionOrder.setFreeItemId(cursor.getInt(9));
                promotionOrder.setItemMainCategoryName(cursor.getString(10));
                promotionOrder.setItemCategoryName(cursor.getString(11));
                promotionOrder.setItemName(cursor.getString(12));
                promotionOrder.setFreeItemName(cursor.getString(13));
                promotionOrder.setCreateTime(cursor.getLong(14));
                promotionOrder.setUpdateTime(cursor.getLong(15));
                promotionOrder.setBasePrice(cursor.getString(16));
                result.add(promotionOrder);
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


    public static PromotionOrder getPromotionOrder(int promotionId)
    {
        PromotionOrder promotionOrder = null;
        String sql = "select * from " + TableNames.PromotionOrder
                + " where promotionId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(promotionId)});
            int count = cursor.getCount();
            if (count < 1) {
                return promotionOrder;
            }

            if (cursor.moveToFirst()) {
                promotionOrder = new PromotionOrder();
                promotionOrder.setId(cursor.getInt(0));
                promotionOrder.setPromotionId(cursor.getInt(1));
                promotionOrder.setItemMainCategoryId(cursor.getInt(2));
                promotionOrder.setItemCategoryId(cursor.getInt(3));
                promotionOrder.setItemId(cursor.getInt(4));
                promotionOrder.setType(cursor.getInt(5));
                promotionOrder.setDiscountPrice(cursor.getString(6));
                promotionOrder.setDiscountPercentage(cursor.getString(7));
                promotionOrder.setFreeNum(cursor.getInt(8));
                promotionOrder.setFreeItemId(cursor.getInt(9));
                promotionOrder.setItemMainCategoryName(cursor.getString(10));
                promotionOrder.setItemCategoryName(cursor.getString(11));
                promotionOrder.setItemName(cursor.getString(12));
                promotionOrder.setFreeItemName(cursor.getString(13));
                promotionOrder.setCreateTime(cursor.getLong(14));
                promotionOrder.setUpdateTime(cursor.getLong(15));
                promotionOrder.setBasePrice(cursor.getString(16));
                return promotionOrder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return promotionOrder;
    }



    public static PromotionOrder getPromotionOrderOrBasePrice(String basePrice)
    {
        PromotionOrder promotionOrder = null;
        String sql = "select * from " + TableNames.PromotionOrder
                + " where basePrice = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{basePrice});
            int count = cursor.getCount();
            if (count < 1) {
                return promotionOrder;
            }

            if (cursor.moveToFirst()) {
                promotionOrder = new PromotionOrder();
                promotionOrder.setId(cursor.getInt(0));
                promotionOrder.setPromotionId(cursor.getInt(1));
                promotionOrder.setItemMainCategoryId(cursor.getInt(2));
                promotionOrder.setItemCategoryId(cursor.getInt(3));
                promotionOrder.setItemId(cursor.getInt(4));
                promotionOrder.setType(cursor.getInt(5));
                promotionOrder.setDiscountPrice(cursor.getString(6));
                promotionOrder.setDiscountPercentage(cursor.getString(7));
                promotionOrder.setFreeNum(cursor.getInt(8));
                promotionOrder.setFreeItemId(cursor.getInt(9));
                promotionOrder.setItemMainCategoryName(cursor.getString(10));
                promotionOrder.setItemCategoryName(cursor.getString(11));
                promotionOrder.setItemName(cursor.getString(12));
                promotionOrder.setFreeItemName(cursor.getString(13));
                promotionOrder.setCreateTime(cursor.getLong(14));
                promotionOrder.setUpdateTime(cursor.getLong(15));
                promotionOrder.setBasePrice(cursor.getString(16));
                return promotionOrder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return promotionOrder;
    }

    public static void deletePromotion(PromotionOrder promotionOrder) {
        String sql = "delete from " + TableNames.PromotionOrder + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] { promotionOrder.getId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteAllpromotionOrder() {
        String sql = "delete from " + TableNames.PromotionOrder;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
