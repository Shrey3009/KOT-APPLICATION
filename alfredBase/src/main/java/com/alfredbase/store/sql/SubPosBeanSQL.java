package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.List;

public class SubPosBeanSQL {
/*
private int id;
    private String userName;
    private String deviceId;
    private int indexNum;
    private String numTag;
 */

    public static void updateSubPosBean(SubPosBean subPosBean) {
        if (subPosBean == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.SubPosBean
                    + "(id, userName, deviceId, numTag, subPosStatus, sessionStatusTime)"
                    + " values (?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { subPosBean.getId(),
                            subPosBean.getUserName(), subPosBean.getDeviceId(),
                            subPosBean.getNumTag(), subPosBean.getSubPosStatus(),
                            subPosBean.getSessionStatusTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<SubPosBean> getAllSubPosBean() {
        List<SubPosBean> result = new ArrayList<>();
        String sql = "select * from " + TableNames.SubPosBean;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            SubPosBean subPosBean = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                subPosBean = new SubPosBean();
                subPosBean.setId(cursor.getInt(0));
                subPosBean.setUserName(cursor.getString(1));
                subPosBean.setDeviceId(cursor.getString(2));
                subPosBean.setNumTag(cursor.getString(3));
                subPosBean.setSubPosStatus(cursor.getInt(4));
                subPosBean.setSessionStatusTime(cursor.getLong(5));
                result.add(subPosBean);
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
    public static List<SubPosBean> getAllOpenSubPosBean() {
        List<SubPosBean> result = new ArrayList<>();
        String sql = "select * from " + TableNames.SubPosBean + " where subPosStatus = " + ParamConst.SUB_POS_STATUS_OPEN;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            SubPosBean subPosBean = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                subPosBean = new SubPosBean();
                subPosBean.setId(cursor.getInt(0));
                subPosBean.setUserName(cursor.getString(1));
                subPosBean.setDeviceId(cursor.getString(2));
                subPosBean.setNumTag(cursor.getString(3));
                subPosBean.setSubPosStatus(cursor.getInt(4));
                subPosBean.setSessionStatusTime(cursor.getLong(5));
                result.add(subPosBean);
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
    public static SubPosBean getSubPosBeanByDeviceId(String deviceId) {
        SubPosBean subPosBean = null;
        String sql = "select * from " + TableNames.SubPosBean + " where deviceId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {deviceId});
            int count = cursor.getCount();
            if (count < 1) {
                return null;
            }
            if (cursor.moveToFirst()) {
                subPosBean = new SubPosBean();
                subPosBean.setId(cursor.getInt(0));
                subPosBean.setUserName(cursor.getString(1));
                subPosBean.setDeviceId(cursor.getString(2));
                subPosBean.setNumTag(cursor.getString(3));
                subPosBean.setSubPosStatus(cursor.getInt(4));
                subPosBean.setSessionStatusTime(cursor.getLong(5));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return subPosBean;
    }
    public static SubPosBean getSubPosBeanById(int id) {
        SubPosBean subPosBean = null;
        String sql = "select * from " + TableNames.SubPosBean + " where id = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {id+""});
            int count = cursor.getCount();
            if (count < 1) {
                return null;
            }
            if (cursor.moveToFirst()) {
                subPosBean = new SubPosBean();
                subPosBean.setId(cursor.getInt(0));
                subPosBean.setUserName(cursor.getString(1));
                subPosBean.setDeviceId(cursor.getString(2));
                subPosBean.setNumTag(cursor.getString(3));
                subPosBean.setSubPosStatus(cursor.getInt(4));
                subPosBean.setSessionStatusTime(cursor.getLong(5));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return subPosBean;
    }
    // only used in sub pos
    public static SubPosBean getSelfSubPosBean() {
        SubPosBean subPosBean = null;
        String sql = "select * from " + TableNames.SubPosBean;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return null;
            }
            if (cursor.moveToFirst()) {
                subPosBean = new SubPosBean();
                subPosBean.setId(cursor.getInt(0));
                subPosBean.setUserName(cursor.getString(1));
                subPosBean.setDeviceId(cursor.getString(2));
                subPosBean.setNumTag(cursor.getString(3));
                subPosBean.setSubPosStatus(cursor.getInt(4));
                subPosBean.setSessionStatusTime(cursor.getLong(5));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return subPosBean;
    }
    public static void deleteAllSubPosBean() {
        String sql = "delete from " + TableNames.SubPosBean;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSubPosBeanById(int id) {
        String sql = "delete from " + TableNames.SubPosBean + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
