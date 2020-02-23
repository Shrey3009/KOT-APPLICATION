package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ItemPromotion;
import com.alfredbase.javabean.PromotionWeek;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PromotionItemSQL {

    public static void addPromotionItem(List<ItemPromotion> itemPromotions) {
        if (itemPromotions == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();

            String sql = "insert into "
                    + TableNames.PromotionItem
                    + "(id, promotionId, itemMainCategoryId, itemCategoryId, itemId,type," +
                    "discountPrice,discountPercentage,freeNum,freeItemId,itemMainCategoryName," +
                    "itemCategoryName,itemName,freeItemName,createTime,updateTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (ItemPromotion itemPromotion : itemPromotions) {

                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        itemPromotion.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        itemPromotion.getPromotionId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        itemPromotion.getItemMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        itemPromotion.getItemCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        itemPromotion.getItemId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        itemPromotion.getType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        itemPromotion.getDiscountPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        itemPromotion.getDiscountPercentage());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        itemPromotion.getFreeNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        itemPromotion.getFreeItemId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 11,
                        itemPromotion.getItemMainCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 12,
                        itemPromotion.getItemCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 13,
                        itemPromotion.getItemName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
                        itemPromotion.getFreeItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
                        itemPromotion.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        itemPromotion.getUpdateTime());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<ItemPromotion> getAllPromotionItem() {
        ArrayList<ItemPromotion> result = new ArrayList<ItemPromotion>();
        String sql = "select * from " + TableNames.PromotionItem + " order by id desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ItemPromotion itemPromotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {

                itemPromotion = new ItemPromotion();
             itemPromotion.setId(cursor.getInt(0));
             itemPromotion.setPromotionId(cursor.getInt(1));
             itemPromotion.setItemMainCategoryId(cursor.getInt(2));
             itemPromotion.setItemCategoryId(cursor.getInt(3));
             itemPromotion.setItemId(cursor.getInt(4));
             itemPromotion.setType(cursor.getInt(5));
             itemPromotion.setDiscountPrice(cursor.getString(6));
             itemPromotion.setDiscountPercentage(cursor.getString(7));
                itemPromotion.setFreeNum(cursor.getInt(8));
                itemPromotion.setFreeItemId(cursor.getInt(9));
                itemPromotion.setItemMainCategoryName(cursor.getString(10));
                itemPromotion.setItemCategoryName(cursor.getString(11));
                itemPromotion.setItemName(cursor.getString(12));
                itemPromotion.setFreeItemName(cursor.getString(13));
                itemPromotion.setCreateTime(cursor.getLong(14));
                itemPromotion.setUpdateTime(cursor.getLong(15));
                result.add(itemPromotion);
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




    public static void deletePromotionItem(ItemPromotion itemPromotion) {
        String sql = "delete from " + TableNames.PromotionItem + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] { itemPromotion.getId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteAllPromotionItem() {
        String sql = "delete from " + TableNames.PromotionItem;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
