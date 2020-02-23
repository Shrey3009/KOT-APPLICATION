package com.alfredbase.store.sql;

import android.database.Cursor;
import android.text.TextUtils;

import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.Random;
import java.util.UUID;

public class CommonSQL {

    public static int getNextSeq(String table) {
        synchronized (table) {
            int seq = 0;
            String sql = "select seq from " + TableNames.SQLITE_SEQUENCE
                    + " where name = ?";
            Cursor cursor = null;
            try {
                cursor = SQLExe.getDB().rawQuery(sql, new String[]{table});
                if (cursor.moveToFirst()) {
                    seq = cursor.getInt(0);
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return seq + 1;
        }
    }

    public static int getKotNextSeq(String tableName) {
        synchronized (tableName) {
            int seq = 0;
            String sql = "select id from " + tableName
                    + " where id = (select max(id) from " + tableName + ")";
            Cursor cursor = null;
            try {
                cursor = SQLExe.getDB().rawQuery(sql, new String[]{});
                if (cursor.moveToFirst()) {
                    seq = cursor.getInt(0);
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return seq + 1;
        }
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static int getCurrentSeq(String table) {
        synchronized (table) {
            int seq = 0;
            String sql = "select seq from " + TableNames.SQLITE_SEQUENCE
                    + " where name = ?";
            Cursor cursor = null;
            try {
                cursor = SQLExe.getDB().rawQuery(sql, new String[]{table});
                if (cursor.moveToFirst()) {
                    seq = cursor.getInt(0);
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return seq;
        }
    }

    public static int getFakeId() {
        int fakeId = new Random().nextInt(100);
        return -fakeId;
    }

    public static boolean isFakeId(int id) {
        return id <= 0;
    }

    public static String getFakeUniqueId() {
        return "-" + getUniqueId();
    }

    public static boolean isFakeId(String id) {
        if (TextUtils.isEmpty(id)) return true;

        char c = id.charAt(0);
        return c == '-';
    }
}
