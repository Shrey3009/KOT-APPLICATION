package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.ConsumingRecords;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 16/12/3.
 */

public class ConsumingRecordsSQL {

    public static void addConsumingRecords(ConsumingRecords consumingRecords) {
        if (consumingRecords == null) {
            return;
        }
        try {
            String sql = "insert into "
                    + TableNames.ConsumingRecords
                    + "(cardId, restId, staffId, consumingType, fromType, consumingAmount, consumingTime, businessDate, payTypeId)"
                    + " values (?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] {
                            consumingRecords.getCardId(),
                            consumingRecords.getRestId(),
                            consumingRecords.getStaffId(),
                            consumingRecords.getConsumingType(),
                            consumingRecords.getFromType(),
                            consumingRecords.getConsumingAmount(),
                            consumingRecords.getConsumingTime(),
                            consumingRecords.getBusinessDate(),
                            consumingRecords.getPayTypeId()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getSumTopUPAndRefoundByBusinessDate(long businessDate){
        Map<String, String> result = null;
        String sql = "select SUM(consumingAmount), COUNT(*) from " + TableNames.ConsumingRecords + " where businessDate = ? and consumingType = 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {businessDate + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = new HashMap<String, String>();
                result.put("sumAmount", cursor.getString(0));
                result.put("count", String.valueOf(cursor.getInt(1)));
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

    public static Map<String, String> getSumTopUPAndRefoundBySession(long businessDate, SessionStatus sessionStatus){
        Map<String, String> result = new HashMap<String, String>();;
        String sql = "select SUM(consumingAmount), COUNT(*) from " + TableNames.ConsumingRecords + " where businessDate = ? and consumingTime > ? and consumingType = 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {businessDate + "", sessionStatus.getTime() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result.put("sumAmount", cursor.getString(0));
                result.put("count", String.valueOf(cursor.getInt(1)));
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
    public static Map<String, String> getSumCashTopUPByBusinessDate(long businessDate){
        Map<String, String> result = null;
        String sql = "select SUM(consumingAmount) from " + TableNames.ConsumingRecords + " where businessDate = ? and consumingType = 1 and payTypeId = 0";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {businessDate + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = new HashMap<String, String>();
                result.put("sumCashAmount", cursor.getString(0));
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

    public static Map<String, String> getSumCashTopUPBySession(long businessDate, SessionStatus sessionStatus){
        Map<String, String> result = null;
        String sql = "select SUM(consumingAmount) from " + TableNames.ConsumingRecords + " where businessDate = ? and consumingTime > ? and consumingType = 1 and payTypeId = 0";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {businessDate + "", sessionStatus.getTime() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = new HashMap<String, String>();
                result.put("sumCashAmount", cursor.getString(0));
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

}
