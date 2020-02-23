package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.MultiReportRelation;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.List;

public class MultiReportRelationSQL {
    /**
     *id, mainReportId, subReportId, subPosBeanId, subReportCreateTime
     */
    public static void updateMultiReportRelation(MultiReportRelation multiReportRelation) {
        if (multiReportRelation == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.MultiReportRelation
                    + "(mainReportId, subReportId, subPosBeanId, subReportCreateTime, syncStatus)"
                    + " values (?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { multiReportRelation.getMainReportId(), multiReportRelation.getSubReportId(),
                            multiReportRelation.getSubPosBeanId(), multiReportRelation.getSubReportCreateTime(),
                            multiReportRelation.getSyncStatus()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateMultiReportRelation(SQLiteDatabase db, MultiReportRelation multiReportRelation) {
        if (multiReportRelation == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.MultiReportRelation
                    + "(mainReportId, subReportId, subPosBeanId, subReportCreateTime, syncStatus)"
                    + " values (?,?,?,?,?)";
            db.execSQL(
                    sql,
                    new Object[] { multiReportRelation.getMainReportId(), multiReportRelation.getSubReportId(),
                            multiReportRelation.getSubPosBeanId(), multiReportRelation.getSubReportCreateTime(),
                            multiReportRelation.getSyncStatus()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<MultiReportRelation> getAllMultiReportRelation() {
        List<MultiReportRelation> result = new ArrayList<>();
        String sql = "select * from " + TableNames.MultiReportRelation;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            MultiReportRelation multiOrderRelation = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                multiOrderRelation = new MultiReportRelation();
                multiOrderRelation.setId(cursor.getInt(0));
                multiOrderRelation.setMainReportId(cursor.getInt(1));
                multiOrderRelation.setSubReportId(cursor.getInt(2));
                multiOrderRelation.setSubPosBeanId(cursor.getInt(3));
                multiOrderRelation.setSubReportCreateTime(cursor.getLong(4));
                multiOrderRelation.setSyncStatus(cursor.getInt(5));
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
    public static List<MultiReportRelation> getAllMultiReportRelationBySession(SessionStatus sessionStatus) {
        List<MultiReportRelation> result = new ArrayList<>();
        String sql = "select * from " + TableNames.MultiReportRelation + " where subReportCreateTime > ? and syncStatus = 0";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {sessionStatus.getTime() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            MultiReportRelation multiOrderRelation = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                multiOrderRelation = new MultiReportRelation();
                multiOrderRelation.setId(cursor.getInt(0));
                multiOrderRelation.setMainReportId(cursor.getInt(1));
                multiOrderRelation.setSubReportId(cursor.getInt(2));
                multiOrderRelation.setSubPosBeanId(cursor.getInt(3));
                multiOrderRelation.setSubReportCreateTime(cursor.getLong(4));
                multiOrderRelation.setSyncStatus(cursor.getInt(5));
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
    public static MultiReportRelation getMultiReportRelationBySubReportId(int subPosBeanId, int subReportId, long subReportCreateTime) {
        MultiReportRelation multiOrderRelation = null;
        String sql = "select * from " + TableNames.MultiReportRelation + " where subPosBeanId = ? and subReportId = ? and subReportCreateTime = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {subPosBeanId+"", subReportId+"", subReportCreateTime+""});
            int count = cursor.getCount();
            if (count < 1) {
                return null;
            }
            if (cursor.moveToFirst()) {
                multiOrderRelation = new MultiReportRelation();
                multiOrderRelation.setId(cursor.getInt(0));
                multiOrderRelation.setMainReportId(cursor.getInt(1));
                multiOrderRelation.setSubReportId(cursor.getInt(2));
                multiOrderRelation.setSubPosBeanId(cursor.getInt(3));
                multiOrderRelation.setSubReportCreateTime(cursor.getLong(4));
                multiOrderRelation.setSyncStatus(cursor.getInt(5));
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

}
