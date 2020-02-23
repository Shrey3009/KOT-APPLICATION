package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.model.KotNotification;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class KotNotificationSQL {

    public static void update(KotNotification notification) {
        if (notification == null) {
            return;
        }
        try {

            String sql = "replace into "
                    + TableNames.KotNotification
                    + "(id, orderId, orderDetailId, revenueCenterId, tableName, revenueCenterName, itemName, qty,session, status, unFinishQty, kotItemDetailId,kotItemNum,uniqueId)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB()
                    .execSQL(
                            sql,
                            new Object[]{notification.getId(),
                                    notification.getOrderId(),
                                    notification.getOrderDetailId(),
                                    notification.getRevenueCenterId(),
                                    notification.getTableName(),
                                    notification.getRevenueCenterName(),
                                    notification.getItemName(),
                                    notification.getQty(),
                                    notification.getSession(),
                                    notification.getStatus(),
                                    notification.getUnFinishQty(),
                                    notification.getKotItemDetailId(),
                                    notification.getKotItemNum(),
                                    notification.getUniqueId()
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addKotNotificationList(
            List<KotNotification> notifications) {
        if (notifications == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.KotNotification
                    + "(id, orderId, orderDetailId, revenueCenterId, tableName, revenueCenterName, itemName, qty,session, status, unFinishQty,kotItemDetailId,kotItemNum,uniqueId)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sql);
            for (KotNotification notification : notifications) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        notification.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        notification.getOrderId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        notification.getOrderDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        notification.getRevenueCenterId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        notification.getTableName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6,
                        notification.getRevenueCenterName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        notification.getItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        notification.getQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        notification.getSession());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        notification.getStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        notification.getUnFinishQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        notification.getKotItemDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
                        notification.getKotItemNum());
                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
                        notification.getUniqueId());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<KotNotification> getAllKotNotification() {
        ArrayList<KotNotification> result = new ArrayList<KotNotification>();
        String sql = "select * from " + TableNames.KotNotification
                + " where status = " + ParamConst.KOTNOTIFICATION_STATUS_NORMAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotNotification notification = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                notification = new KotNotification();
                notification.setId(cursor.getInt(0));
                notification.setUniqueId(cursor.getString(1));
                notification.setOrderId(cursor.getInt(2));
                notification.setOrderDetailId(cursor.getInt(3));
                notification.setRevenueCenterId(cursor.getInt(4));
                notification.setTableName(cursor.getString(5));
                notification.setRevenueCenterName(cursor.getString(6));
                notification.setItemName(cursor.getString(7));
                notification.setQty(cursor.getInt(8));
                notification.setSession(cursor.getInt(9));
                notification.setStatus(cursor.getInt(10));
                notification.setUnFinishQty(cursor.getInt(11));
                notification.setKotItemDetailId(cursor.getInt(12));
                notification.setKotItemNum(cursor.getInt(13));
                result.add(notification);
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

    public static KotNotification getKotNotification(int orderDetailId, int kotItemDetailId) {
        KotNotification notification = null;
        String sql = "select * from " + TableNames.KotNotification
                + " where orderDetailId = ? and kotItemDetailId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderDetailId + "", kotItemDetailId + ""});
            if (cursor.moveToFirst()) {
                notification = new KotNotification();
//                result.setId(cursor.getInt(0));
//                result.setOrderId(cursor.getInt(1));
//                result.setOrderDetailId(cursor.getInt(2));
//                result.setRevenueCenterId(cursor.getInt(3));
//                result.setTableName(cursor.getString(4));
//                result.setRevenueCenterName(cursor.getString(5));
//                result.setItemName(cursor.getString(6));
//                result.setQty(cursor.getInt(7));
//                result.setSession(cursor.getInt(8));
//                result.setStatus(cursor.getInt(9));
//                result.setUnFinishQty(cursor.getInt(10));
//                result.setKotItemDetailId(cursor.getInt(11));
//                result.setKotItemNum(cursor.getInt(12));
//                result.setUniqueId(cursor.getString(13));
                notification.setId(cursor.getInt(0));
                notification.setUniqueId(cursor.getString(1));
                notification.setOrderId(cursor.getInt(2));
                notification.setOrderDetailId(cursor.getInt(3));
                notification.setRevenueCenterId(cursor.getInt(4));
                notification.setTableName(cursor.getString(5));
                notification.setRevenueCenterName(cursor.getString(6));
                notification.setItemName(cursor.getString(7));
                notification.setQty(cursor.getInt(8));
                notification.setSession(cursor.getInt(9));
                notification.setStatus(cursor.getInt(10));
                notification.setUnFinishQty(cursor.getInt(11));
                notification.setKotItemDetailId(cursor.getInt(12));
                notification.setKotItemNum(cursor.getInt(13));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return notification;
    }

    public static int getAllKotNotificationQty() {
        String sql = "select sum(qty) from " + TableNames.KotNotification
                + " where status = " + ParamConst.KOTNOTIFICATION_STATUS_NORMAL;
        Cursor cursor = null;
        int qty = 0;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{});
            if (cursor.moveToFirst()) {
                qty = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return qty;
    }

    public static void deleteKotNotification(KotNotification kotSummary) {
        String sql = "delete from " + TableNames.KotNotification
                + " where id = ? and revenueCenterId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{kotSummary.getRevenueCenterId(), kotSummary.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotNotifications(Integer revenueId) {
        String deleteByRevenue = "";
        if (revenueId != null) {
            deleteByRevenue = " where revenueCenterId = " + revenueId;
        }
        String sql = "delete from " + TableNames.KotNotification + deleteByRevenue;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotNotificationsByKotItemDetail(KotItemDetail kotItemDetail) {
        String sql = "delete from " + TableNames.KotNotification + " where revenueCenterId = ? and orderDetailId = ? and kotItemDetailId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{kotItemDetail.getRestaurantId(), kotItemDetail.getOrderDetailId(), kotItemDetail.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getTableNameList() {
        Cursor cursor = null;
        ArrayList<String> tableNames = new ArrayList<String>();
        try {
            cursor = SQLExe.getDB().query(
                    TableNames.KotNotification,
                    new String[]{"tableName"},
                    "status = "
                            + ParamConst.KOTNOTIFICATION_STATUS_NORMAL,
                    new String[]{}, "tableName", "", "");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                tableNames.add(cursor.getString(0));
            }
        } catch (Exception e) {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tableNames;
    }
}
