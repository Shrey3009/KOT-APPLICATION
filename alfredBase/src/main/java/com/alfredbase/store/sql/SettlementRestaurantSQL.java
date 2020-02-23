package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class SettlementRestaurantSQL {

    public static void addSettlementRestaurant(List<SettlementRestaurant> settlementRestaurants) {
        if (settlementRestaurants == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();

            String sql = "replace into "
                    + TableNames.SettlementRestaurant
                    + "(id,restaurantId,mediaId,adjustmentsId,onlineServiceId,type,remarks,discriptionId,otherPaymentId)"
                    + " values (?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (SettlementRestaurant settlementRestaurant : settlementRestaurants) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        settlementRestaurant.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        settlementRestaurant.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        settlementRestaurant.getMediaId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        settlementRestaurant.getAdjustmentsId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        settlementRestaurant.getOnlineServiceId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        settlementRestaurant.getType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        settlementRestaurant.getRemarks());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        settlementRestaurant.getDiscriptionId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 9,
                        settlementRestaurant.getOtherPaymentId());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    public static ArrayList<SettlementRestaurant> getAllSettlementRestaurant() {
        ArrayList<SettlementRestaurant> result = new ArrayList<SettlementRestaurant>();
        String sql = "select * from " + TableNames.SettlementRestaurant;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            SettlementRestaurant settlementRestaurant = null;

            if (cursor.moveToFirst() == false) {

                Log.e("SettlementRestaurant", "您的CITY表中无数据: ");

            }
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                settlementRestaurant = new SettlementRestaurant();
                settlementRestaurant.setId(cursor.getInt(0));
                settlementRestaurant.setRestaurantId(cursor.getInt(1));
                settlementRestaurant.setMediaId(cursor.getInt(2));
                settlementRestaurant.setAdjustmentsId(cursor.getInt(3));
                settlementRestaurant.setOnlineServiceId(cursor.getInt(4));
                settlementRestaurant.setType(cursor.getInt(5));
                settlementRestaurant.setRemarks(cursor.getString(6));
                settlementRestaurant.setDiscriptionId(cursor.getInt(7));
                settlementRestaurant.setOtherPaymentId(cursor.getString(8));
                result.add(settlementRestaurant);
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


    public static SettlementRestaurant getSettlementRestaurant(int paid) {
        SettlementRestaurant result = null;
        String sql = "select * from " + TableNames.SettlementRestaurant + " where id = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{paid + ""});
//            int count = cursor.getCount();
//            if (count < 1) {
//                return result;
//            }
            if (cursor.moveToFirst()) {
                result = new SettlementRestaurant();
                result.setId(cursor.getInt(0));
                result.setRestaurantId(cursor.getInt(1));
                result.setMediaId(cursor.getInt(2));
                result.setAdjustmentsId(cursor.getInt(3));
                result.setOnlineServiceId(cursor.getInt(4));
                result.setType(cursor.getInt(5));
                result.setRemarks(cursor.getString(6));
                result.setDiscriptionId(cursor.getInt(7));
                result.setOtherPaymentId(cursor.getString(8));

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

    public static void deleteSettlementRestaurant(SettlementRestaurant settlementRestaurant) {
        String sql = "delete from " + TableNames.SettlementRestaurant + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{settlementRestaurant.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllSettlementRestaurant() {
        String sql = "delete from " + TableNames.SettlementRestaurant;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllSettlementRestaurantCustom() {
        String sql = "delete from " + TableNames.SettlementRestaurant + " where otherPaymentId IS NOT NULL and id != ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{ParamConst.SETTLEMENT_TYPE_IPAY88});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllSettlementRestaurantIpay88HalalPayment() {
        String sql = "delete from " + TableNames.SettlementRestaurant + " where otherPaymentId IS NULL  or id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{ParamConst.SETTLEMENT_TYPE_IPAY88});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
