package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodSQL {
    public static void addPaymentMethod(List<PaymentMethod> paymentMethods) {
        if (paymentMethods == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.PaymentMethod
                    + "(id,nameCh,nameEn,nameOt,logoMd,logoSm,payType,restaurantId,isTax,isDiscount,isAdmin,isMsg,isMsgRequire,isPart,partAcount,status,description,createTime,updateTime,paymentTypeId)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (PaymentMethod pamentMethod : paymentMethods) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        pamentMethod.getId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 2,
                        pamentMethod.getNameCh());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3,
                        pamentMethod.getNameEn());
                SQLiteStatementHelper.bindString(sqLiteStatement, 4,
                        pamentMethod.getNameOt());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        pamentMethod.getLogoMd());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6,
                        pamentMethod.getLogoSm());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        pamentMethod.getPayType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        pamentMethod.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        pamentMethod.getIsTax());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        pamentMethod.getIsDiscount());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        pamentMethod.getIsAdmin());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        pamentMethod.getIsMsg());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
                        pamentMethod.getIsMsgRequire());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
                        pamentMethod.getIsPart());

                SQLiteStatementHelper.bindDouble(sqLiteStatement, 15,
                        pamentMethod.getPartAcount());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        pamentMethod.getStatus());
                SQLiteStatementHelper.bindString(sqLiteStatement, 17,
                        pamentMethod.getDescription());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 18,
                        pamentMethod.getCreateTime());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 19,
                        pamentMethod.getUpdateTime());

                SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
                        pamentMethod.getPaymentTypeId());
//
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();

        }
    }

    public static ArrayList<PaymentMethod> getAllPaymentMethod() {
        ArrayList<PaymentMethod> result = new ArrayList<PaymentMethod>();
        String sql = "select * from " + TableNames.PaymentMethod;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            PaymentMethod pamentMethod = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                pamentMethod = new PaymentMethod();
                pamentMethod.setId(cursor.getInt(0));
                pamentMethod.setNameCh(cursor.getString(1));
                pamentMethod.setNameEn(cursor.getString(2));
                pamentMethod.setNameOt(cursor.getString(3));
                pamentMethod.setLogoMd(cursor.getString(4));
                pamentMethod.setLogoSm(cursor.getString(5));
                pamentMethod.setPayType(cursor.getInt(6));
                pamentMethod.setRestaurantId(cursor.getInt(7));
                pamentMethod.setIsTax(cursor.getInt(8));
                pamentMethod.setIsDiscount(cursor.getInt(9));
                pamentMethod.setIsAdmin(cursor.getInt(10));
                pamentMethod.setIsMsgRequire(cursor.getInt(11));
                pamentMethod.setIsMsgRequire(cursor.getInt(12));
                pamentMethod.setIsPart(cursor.getInt(13));
                pamentMethod.setPartAcount(cursor.getDouble(14));
                pamentMethod.setStatus(cursor.getInt(15));
                pamentMethod.setDescription(cursor.getString(16));
                pamentMethod.setCreateTime(cursor.getLong(17));
                pamentMethod.setUpdateTime(cursor.getLong(18));
                pamentMethod.setPaymentTypeId(cursor.getLong(19));
                result.add(pamentMethod);
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


    public static PaymentMethod getPaymentMethod(int paid) {
        PaymentMethod result = null;
        String sql = "select * from " + TableNames.PaymentMethod + " where id = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{paid + ""});
//            int count = cursor.getCount();
//            if (count < 1) {
//                return result;
//            }
            if (cursor.moveToFirst()) {
                result = new PaymentMethod();
                result.setId(cursor.getInt(0));
                result.setNameCh(cursor.getString(1));
                result.setNameEn(cursor.getString(2));
                result.setNameOt(cursor.getString(3));
                result.setLogoMd(cursor.getString(4));
                result.setLogoSm(cursor.getString(5));
                result.setPayType(cursor.getInt(6));
                result.setRestaurantId(cursor.getInt(7));
                result.setIsTax(cursor.getInt(8));
                result.setIsDiscount(cursor.getInt(9));
                result.setIsAdmin(cursor.getInt(10));
                result.setIsMsgRequire(cursor.getInt(11));
                result.setIsMsgRequire(cursor.getInt(12));
                result.setIsPart(cursor.getInt(13));
                result.setPartAcount(cursor.getDouble(14));
                result.setStatus(cursor.getInt(15));
                result.setDescription(cursor.getString(16));
                result.setCreateTime(cursor.getLong(17));
                result.setUpdateTime(cursor.getLong(18));
                result.setPaymentTypeId(cursor.getLong(19));
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

    public static PaymentMethod getPaymentMethodByPaymentTypeId(int paymentTypeId) {
        PaymentMethod result = null;
        String sql = "select * from " + TableNames.PaymentMethod + " where paymentTypeId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{paymentTypeId + ""});
            if (cursor.moveToFirst()) {
                result = new PaymentMethod();
                result.setId(cursor.getInt(0));
                result.setNameCh(cursor.getString(1));
                result.setNameEn(cursor.getString(2));
                result.setNameOt(cursor.getString(3));
                result.setLogoMd(cursor.getString(4));
                result.setLogoSm(cursor.getString(5));
                result.setPayType(cursor.getInt(6));
                result.setRestaurantId(cursor.getInt(7));
                result.setIsTax(cursor.getInt(8));
                result.setIsDiscount(cursor.getInt(9));
                result.setIsAdmin(cursor.getInt(10));
                result.setIsMsgRequire(cursor.getInt(11));
                result.setIsMsgRequire(cursor.getInt(12));
                result.setIsPart(cursor.getInt(13));
                result.setPartAcount(cursor.getDouble(14));
                result.setStatus(cursor.getInt(15));
                result.setDescription(cursor.getString(16));
                result.setCreateTime(cursor.getLong(17));
                result.setUpdateTime(cursor.getLong(18));
                result.setPaymentTypeId(cursor.getLong(19));
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
    public static PaymentMethod getPaymentMethodByNameOt(String nameOt) {
        PaymentMethod result = null;
        String sql = "select * from " + TableNames.PaymentMethod+ " where nameOt = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,new String[] {nameOt});
            if (cursor.moveToFirst()) {
                result = new PaymentMethod();
                result.setId(cursor.getInt(0));
                result.setNameCh(cursor.getString(1));
                result.setNameEn(cursor.getString(2));
                result.setNameOt(cursor.getString(3));
                result.setLogoMd(cursor.getString(4));
                result.setLogoSm(cursor.getString(5));
                result.setPayType(cursor.getInt(6));
                result.setRestaurantId(cursor.getInt(7));
                result.setIsTax(cursor.getInt(8));
                result.setIsDiscount(cursor.getInt(9));
                result.setIsAdmin(cursor.getInt(10));
                result.setIsMsgRequire(cursor.getInt(11));
                result.setIsMsgRequire(cursor.getInt(12));
                result.setIsPart(cursor.getInt(13));
                result.setPartAcount(cursor.getDouble(14));
                result.setStatus(cursor.getInt(15));
                result.setDescription(cursor.getString(16));
                result.setCreateTime(cursor.getLong(17));
                result.setUpdateTime(cursor.getLong(18));
                result.setPaymentTypeId(cursor.getLong(19));
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


    public static void deletePaymentMethod(PaymentMethod pamentMethod) {
        String sql = "delete from " + TableNames.PaymentMethod + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{pamentMethod.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllPaymentMethod() {
        String sql = "delete from " + TableNames.PaymentMethod;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllPaymentMethodCustom() {
        String sql = "delete from " + TableNames.PaymentMethod+" where paymentTypeId < ? AND paymentTypeId > ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{ParamConst.SETTLEMENT_TYPE_IPAY88, ParamConst.SETTLEMENT_TYPE_PAYHALAL});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllPaymentMethodIpay88Halal() {
        String sql = "delete from " + TableNames.PaymentMethod+" where paymentTypeId >= ? AND paymentTypeId < ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{ParamConst.SETTLEMENT_TYPE_IPAY88, ParamConst.SETTLEMENT_TYPE_PAYHALAL});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void deleteAllSettlementRestaurantCustom() {
//        String sql = "delete from " + TableNames.SettlementRestaurant + " where otherPaymentId IS NOT NULL and id != ?";
//        try {
//            SQLExe.getDB().execSQL(sql, new Object[]{ParamConst.SETTLEMENT_TYPE_IPAY88});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void deleteAllSettlementRestaurantIpay88HalalPayment() {
//        String sql = "delete from " + TableNames.SettlementRestaurant + " where otherPaymentId IS NULL  or id = ?";
//        try {
//            SQLExe.getDB().execSQL(sql, new Object[]{ParamConst.SETTLEMENT_TYPE_IPAY88});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
