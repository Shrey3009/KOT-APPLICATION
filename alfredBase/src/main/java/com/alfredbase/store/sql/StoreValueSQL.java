package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

/**
 * Created by Alex on 2017/7/31.
 */

public class StoreValueSQL {

    public static void updateStore(String id, String value ) {
        updateStore(id, 0, value);
    }
    public static void updateStore(String id, int type, String value ) {
        if (TextUtils.isEmpty(value))
            return;
        try {
            String sql = "replace into "
                    + TableNames.StoreValue
                    + " (id, type, storeValue)"
                    + " values (?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { id, type, value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String id) {
        String result = "";
        String sql = "select storeValue from " + TableNames.StoreValue + " where id = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {id});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
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

    public static void deleteStore(String id) {
        String sql = "delete from " + TableNames.StoreValue + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
