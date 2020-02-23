package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.MultiOrderRelation;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.List;

public class MultiOrderRelationSQL {
    /**
     * private int id;
     private int mainOrderId;
     private int subOrderId;
     private int subPosBeanId;
     */
    public static void updateMultiOrderRelation(MultiOrderRelation multiOrderRelation) {
        if (multiOrderRelation == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.MultiOrderRelation
                    + "(mainOrderId, subOrderId, subPosBeanId, subOrderCreateTime)"
                    + " values (?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { multiOrderRelation.getMainOrderId(), multiOrderRelation.getSubOrderId(),
                            multiOrderRelation.getSubPosBeanId(), multiOrderRelation.getSubOrderCreateTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateMultiOrderRelation(SQLiteDatabase db, MultiOrderRelation multiOrderRelation) {
        if (multiOrderRelation == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.MultiOrderRelation
                    + "(mainOrderId, subOrderId, subPosBeanId, subOrderCreateTime)"
                    + " values (?,?,?,?)";
            db.execSQL(
                    sql,
                    new Object[] { multiOrderRelation.getMainOrderId(), multiOrderRelation.getSubOrderId(),
                            multiOrderRelation.getSubPosBeanId(), multiOrderRelation.getSubOrderCreateTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<MultiOrderRelation> getAllMultiOrderRelation() {
        List<MultiOrderRelation> result = new ArrayList<>();
        String sql = "select * from " + TableNames.MultiOrderRelation;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            MultiOrderRelation multiOrderRelation = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                multiOrderRelation = new MultiOrderRelation();
                multiOrderRelation.setId(cursor.getInt(0));
                multiOrderRelation.setMainOrderId(cursor.getInt(1));
                multiOrderRelation.setSubOrderId(cursor.getInt(2));
                multiOrderRelation.setSubPosBeanId(cursor.getInt(3));
                multiOrderRelation.setSubOrderCreateTime(cursor.getLong(4));
                result.add(multiOrderRelation);
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
    public static MultiOrderRelation getMultiOrderRelationBySubOrderId(int subPosBeanId, int subOrderId, long subOrderCreateTime) {
        MultiOrderRelation multiOrderRelation = null;
        String sql = "select * from " + TableNames.MultiOrderRelation + " where subPosBeanId = ? and subOrderId = ? and subOrderCreateTime = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {subPosBeanId+"", subOrderId+"", subOrderCreateTime+""});
            int count = cursor.getCount();
            if (count < 1) {
                return null;
            }
            if (cursor.moveToFirst()) {
                multiOrderRelation = new MultiOrderRelation();
                multiOrderRelation.setId(cursor.getInt(0));
                multiOrderRelation.setMainOrderId(cursor.getInt(1));
                multiOrderRelation.setSubOrderId(cursor.getInt(2));
                multiOrderRelation.setSubPosBeanId(cursor.getInt(3));
                multiOrderRelation.setSubOrderCreateTime(cursor.getLong(4));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return multiOrderRelation;
    }
    public static void deleteAllSubPosBean() {
        String sql = "delete from " + TableNames.MultiOrderRelation;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSubPosBeanById(int id) {
        String sql = "delete from " + TableNames.MultiOrderRelation + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
