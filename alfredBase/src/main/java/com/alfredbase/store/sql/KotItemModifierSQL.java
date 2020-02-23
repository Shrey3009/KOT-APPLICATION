package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class KotItemModifierSQL {

    public static void update(KotItemModifier kotItemModifier) {
        if (kotItemModifier == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.KotItemModifier
                    + "(id, kotItemDetailId, modifierId, modifierName, modifierNum, status, printerId, uniqueId, kotItemDetailUniqueId)"
                    + " values (?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{kotItemModifier.getId(),
                            kotItemModifier.getKotItemDetailId(),
                            kotItemModifier.getModifierId(),
                            kotItemModifier.getModifierName(),
                            kotItemModifier.getModifierNum(),
                            kotItemModifier.getStatus(),
                            kotItemModifier.getPrinterId(),
                            kotItemModifier.getUniqueId(),
                            kotItemModifier.getKotItemDetailUniqueId()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addKotItemModifierList(List<KotItemModifier> kotItemModifiers) {
        if (kotItemModifiers == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.KotItemModifier
                    + "(id, kotItemDetailId, modifierId, modifierName, modifierNum, status, printerId, uniqueId, kotItemDetailUniqueId)"
                    + " values (?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (KotItemModifier kotItemModifier : kotItemModifiers) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        kotItemModifier.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        kotItemModifier.getKotItemDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        kotItemModifier.getModifierId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 4,
                        kotItemModifier.getModifierName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        kotItemModifier.getModifierNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        kotItemModifier.getStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        kotItemModifier.getPrinterId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        kotItemModifier.getUniqueId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 9,
                        kotItemModifier.getKotItemDetailUniqueId());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<KotItemModifier> getAllKotItemModifier() {
        ArrayList<KotItemModifier> result = new ArrayList<KotItemModifier>();
        String sql = "select * from " + TableNames.KotItemModifier;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemModifier kotItemModifier = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemModifier = new KotItemModifier();
                kotItemModifier.setId(cursor.getInt(0));
                kotItemModifier.setUniqueId(cursor.getString(1));
                kotItemModifier.setKotItemDetailId(cursor.getInt(2));
                kotItemModifier.setModifierId(cursor.getInt(3));
                kotItemModifier.setModifierName(cursor.getString(4));
                kotItemModifier.setModifierNum(cursor.getInt(5));
                kotItemModifier.setStatus(cursor.getInt(6));
                kotItemModifier.setPrinterId(cursor.getInt(7));
                kotItemModifier.setKotItemDetailUniqueId(cursor.getString(8));
                result.add(kotItemModifier);
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

    public static KotItemModifier getKotItemModifier(int kotItemDetailId, int modifierId) {
        KotItemModifier kotItemModifier = null;
        String sql = "select * from " + TableNames.KotItemModifier + " where kotItemDetailId = ? and modifierId = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{kotItemDetailId + "", modifierId + ""});
            if (cursor.moveToFirst()) {
                kotItemModifier = new KotItemModifier();
//                kotItemModifier.setId(cursor.getInt(0));
//                kotItemModifier.setKotItemDetailId(cursor.getInt(1));
//                kotItemModifier.setModifierId(cursor.getInt(2));
//                kotItemModifier.setModifierName(cursor.getString(3));
//                kotItemModifier.setModifierNum(cursor.getInt(4));
//                kotItemModifier.setStatus(cursor.getInt(5));
//                kotItemModifier.setPrinterId(cursor.getInt(6));
//                kotItemModifier.setUniqueId(cursor.getString(7));
                kotItemModifier.setId(cursor.getInt(0));
                kotItemModifier.setUniqueId(cursor.getString(1));
                kotItemModifier.setKotItemDetailId(cursor.getInt(2));
                kotItemModifier.setModifierId(cursor.getInt(3));
                kotItemModifier.setModifierName(cursor.getString(4));
                kotItemModifier.setModifierNum(cursor.getInt(5));
                kotItemModifier.setStatus(cursor.getInt(6));
                kotItemModifier.setPrinterId(cursor.getInt(7));
                kotItemModifier.setKotItemDetailUniqueId(cursor.getString(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemModifier;
    }

    public static ArrayList<KotItemModifier> getKotItemModifiersByKotItemDetail(int kotItemDetailId) {
        ArrayList<KotItemModifier> result = new ArrayList<KotItemModifier>();
        String sql = "select * from " + TableNames.KotItemModifier + " where kotItemDetailId= ?";

        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(kotItemDetailId)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemModifier kotItemModifier = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemModifier = new KotItemModifier();
//                kotItemModifier.setId(cursor.getInt(0));
//                kotItemModifier.setKotItemDetailId(cursor.getInt(1));
//                kotItemModifier.setModifierId(cursor.getInt(2));
//                kotItemModifier.setModifierName(cursor.getString(3));
//                kotItemModifier.setModifierNum(cursor.getInt(4));
//                kotItemModifier.setStatus(cursor.getInt(5));
//                kotItemModifier.setPrinterId(cursor.getInt(6));
//                kotItemModifier.setUniqueId(cursor.getString(7));
                kotItemModifier.setId(cursor.getInt(0));
                kotItemModifier.setUniqueId(cursor.getString(1));
                kotItemModifier.setKotItemDetailId(cursor.getInt(2));
                kotItemModifier.setModifierId(cursor.getInt(3));
                kotItemModifier.setModifierName(cursor.getString(4));
                kotItemModifier.setModifierNum(cursor.getInt(5));
                kotItemModifier.setStatus(cursor.getInt(6));
                kotItemModifier.setPrinterId(cursor.getInt(7));
                kotItemModifier.setKotItemDetailUniqueId(cursor.getString(8));
                result.add(kotItemModifier);
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

    public static ArrayList<KotItemModifier> getKotItemModifiersByKotItemDetail(KotItemDetail kotItemDetail) {
        ArrayList<KotItemModifier> result = new ArrayList<KotItemModifier>();
        String sql = "select * from " + TableNames.KotItemModifier + " where kotItemDetailUniqueId = ?";

        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(kotItemDetail.getUniqueId())});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemModifier kotItemModifier = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemModifier = new KotItemModifier();
//                kotItemModifier.setId(cursor.getInt(0));
//                kotItemModifier.setKotItemDetailId(cursor.getInt(1));
//                kotItemModifier.setModifierId(cursor.getInt(2));
//                kotItemModifier.setModifierName(cursor.getString(3));
//                kotItemModifier.setModifierNum(cursor.getInt(4));
//                kotItemModifier.setStatus(cursor.getInt(5));
//                kotItemModifier.setPrinterId(cursor.getInt(6));
//                kotItemModifier.setUniqueId(cursor.getString(7));
                kotItemModifier.setId(cursor.getInt(0));
                kotItemModifier.setUniqueId(cursor.getString(1));
                kotItemModifier.setKotItemDetailId(cursor.getInt(2));
                kotItemModifier.setModifierId(cursor.getInt(3));
                kotItemModifier.setModifierName(cursor.getString(4));
                kotItemModifier.setModifierNum(cursor.getInt(5));
                kotItemModifier.setStatus(cursor.getInt(6));
                kotItemModifier.setPrinterId(cursor.getInt(7));
                kotItemModifier.setKotItemDetailUniqueId(cursor.getString(8));
                result.add(kotItemModifier);
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

    public static void deleteKotItemModifiers(List<KotItemModifier> kotItemModifiers) {
        for (KotItemModifier kotItemModifier : kotItemModifiers) {
            deleteKotItemModifier(kotItemModifier);
        }
    }

    public static void deleteKotItemModifier(KotItemModifier kotItemModifier) {
        String sql = "delete from " + TableNames.KotItemModifier
                + " where uniqueId = ?";
        try {
            SQLExe.getDB().execSQL(sql,
                    new Object[]{kotItemModifier.getUniqueId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotItemModifier() {
        String sql = "delete from " + TableNames.KotItemModifier;
        String sssql = "select * from sqlite_sequence";
        String up = "update sqlite_sequence set seq=0 where name='" + TableNames.KotItemModifier + "'";
        try {
            SQLExe.getDB().execSQL(sql,
                    new Object[]{});
            SQLExe.getDB().rawQuery(sssql, new String[]{});
            SQLExe.getDB().execSQL(up, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotItemModifier(Integer revenueId) {

        String sql = "delete from " + TableNames.KotItemModifier;
        if (revenueId != null) {
            sql = "delete from " + TableNames.KotItemModifier +
                    " where kotItemDetailId in (select kotItemDetailId from "
                    + TableNames.KotItemDetail + " where revenueId = " + revenueId + ")";
        }

        try {
            SQLExe.getDB().execSQL(sql,
                    new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
