package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.Promotion;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PromotionSQL {

    public static void addPromotion(List<Promotion> Promotions) {
        if (Promotions == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
//
//            "id=" + id +
//                    ", type=" + type +
//                    ", promotionName='" + promotionName + '\'' +
//                    ", restaurantId=" + restaurantId +
//                    ", isActive=" + isActive +
//                    ", promotionWeight=" + promotionWeight +
//                    ", discountPrice='" + discountPrice + '\'' +
//                    ", discountPercentage='" + discountPercentage + '\'' +
//                    ", freeNum=" + freeNum +
//                    ", freeItemId=" + freeItemId +
//                    ", freeItemName='" + freeItemName + '\'' +
//                    ", itemMainCategoryId=" + itemMainCategoryId +
//                    ", itemCategoryId=" + itemCategoryId +
//                    ", itemId=" + itemId +
//                    ", itemNum=" + itemNum +
//                    ", itemMainCategoryName='" + itemMainCategoryName + '\'' +
//                    ", itemCategoryName='" + itemCategoryName + '\'' +
//                    ", itemName='" + itemName + '\'' +
//                    ", secondItemMainCategoryId=" + secondItemMainCategoryId +
//                    ", secondItemCategoryId=" + secondItemCategoryId +
//                    ", secondItemId=" + secondItemId +
//                    ", secondItemNum=" + secondItemNum +
//                    ", secondItemMainCategoryName='" + secondItemMainCategoryName + '\'' +
//                    ", secondItemCategoryName='" + secondItemCategoryName + '\'' +
//                    ", secondItemName='" + secondItemName + '\'' +
//                    ", createTime=" + createTime +
//                    ", updateTime=" + updateTime +
//                    ", basePrice='" + basePrice + '\'' +
//                    ", guestNum=" + guestNum +
//                    ", weekId=" + weekId +
            String sql = "insert into "
                    + TableNames.Promotion
                    + "(id, type, promotionName, restaurantId, isActive,promotionWeight,discountPrice,discountPercentage,freeNum,"
                    + "freeItemId,freeItemName,itemMainCategoryId,itemCategoryId,itemId,itemNum,itemMainCategoryName,itemCategoryName,itemName," +
                    "secondItemMainCategoryId,secondItemCategoryId,secondItemId,secondItemNum,secondItemMainCategoryName,secondItemCategoryName,secondItemName," +
                    "createTime,updateTime,basePrice,guestNum,promotionDateInfoId)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (Promotion promotion : Promotions) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        promotion.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        promotion.getType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3,
                        promotion.getPromotionName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        promotion.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        promotion.getIsActive());


                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        promotion.getPromotionWeight());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        promotion.getDiscountPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        promotion.getDiscountPercentage());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        promotion.getFreeNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        promotion.getFreeItemId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 11,
                        promotion.getFreeItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        promotion.getItemMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
                        promotion.getItemCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
                        promotion.getItemId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
                        promotion.getItemNum());
                SQLiteStatementHelper.bindString(sqLiteStatement, 16,
                        promotion.getItemMainCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 17,
                        promotion.getItemCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 18,
                        promotion.getItemName());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 19,
                        promotion.getSecondItemMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
                        promotion.getSecondItemCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 21,
                        promotion.getSecondItemId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
                        promotion.getSecondItemNum());
                SQLiteStatementHelper.bindString(sqLiteStatement, 23,
                        promotion.getSecondItemMainCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 24,
                        promotion.getSecondItemCategoryName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 25,
                        promotion.getSecondItemName());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 26,
                        promotion.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 27,
                        promotion.getUpdateTime());
                SQLiteStatementHelper.bindString(sqLiteStatement, 28,
                        promotion.getBasePrice());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 29,
                        promotion.getGuestNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 30,
                        promotion.getPromotionDateInfoId());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<Promotion> getAllPromotion() {
        ArrayList<Promotion> result = new ArrayList<Promotion>();
        String sql = "select * from " + TableNames.Promotion + " order by promotionWeight desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            Promotion promotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {

                promotion = new Promotion();
                promotion.setId(cursor.getInt(0));
                promotion.setType(cursor.getInt(1));
                promotion.setPromotionName(cursor.getString(2));
                promotion.setRestaurantId(cursor.getInt(3));
                promotion.setIsActive(cursor.getInt(4));
                promotion.setPromotionWeight(cursor.getInt(5));
                promotion.setDiscountPrice(cursor.getString(6));
                promotion.setDiscountPercentage(cursor.getString(7));
                promotion.setFreeNum(cursor.getInt(8));
                promotion.setFreeItemId(cursor.getInt(9));
                promotion.setFreeItemName(cursor.getString(10));
                promotion.setItemMainCategoryId(cursor.getInt(11));
                promotion.setItemCategoryId(cursor.getInt(12));
                promotion.setItemId(cursor.getInt(13));
                promotion.setItemNum(cursor.getInt(14));
                promotion.setItemMainCategoryName(cursor.getString(15));
                promotion.setItemCategoryName(cursor.getString(16));
                promotion.setItemName(cursor.getString(17));

                promotion.setSecondItemMainCategoryId(cursor.getInt(18));
                promotion.setSecondItemCategoryId(cursor.getInt(19));
                promotion.setSecondItemId(cursor.getInt(20));
                promotion.setSecondItemNum(cursor.getInt(21));
                promotion.setSecondItemMainCategoryName(cursor.getString(22));
                promotion.setSecondItemCategoryName(cursor.getString(23));
                promotion.setSecondItemName(cursor.getString(24));
                promotion.setCreateTime(cursor.getLong(25));
                promotion.setUpdateTime(cursor.getLong(26));
                promotion.setBasePrice(cursor.getString(27));
                promotion.setGuestNum(cursor.getInt(28));
                promotion.setPromotionDateInfoId(cursor.getInt(29));


                result.add(promotion);
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

    public static Promotion getPromotion(int proId) {
       Promotion promotion = null;
        String sql = "select * from " + TableNames.Promotion +  " where id = ? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {String.valueOf(proId)});
            int count = cursor.getCount();
            if (count < 1) {
                return promotion;
            }

            if (cursor.moveToFirst()) {

                promotion = new Promotion();
                promotion.setId(cursor.getInt(0));
                promotion.setType(cursor.getInt(1));
                promotion.setPromotionName(cursor.getString(2));
                promotion.setRestaurantId(cursor.getInt(3));
                promotion.setIsActive(cursor.getInt(4));
               return promotion;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return promotion;
    }




    public static void deletePromotion(Promotion promotion) {
        String sql = "delete from " + TableNames.Promotion + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] { promotion.getId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteAllPromotion() {
        String sql = "delete from " + TableNames.Promotion;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
