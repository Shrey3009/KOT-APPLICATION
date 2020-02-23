package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class KotItemDetailSQL {

    public static void update(KotItemDetail kotItemDetail) {
        if (kotItemDetail == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.KotItemDetail
                    + "(id, restaurantId, revenueId, orderId, orderDetailId, printerGroupId, kotSummaryId, "
                    + "itemName,itemNum,finishQty,sessionStatus,kotStatus,specialInstractions ,version,createTime,"
                    + "updateTime, unFinishQty, categoryId,isTakeAway, fireStatus,callType,expectedTime,startTime,endTime,itemType,itemId,uniqueId,kotSummaryUniqueId)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{kotItemDetail.getId(),
                            kotItemDetail.getRestaurantId(),
                            kotItemDetail.getRevenueId(),
                            kotItemDetail.getOrderId(),
                            kotItemDetail.getOrderDetailId(),
                            kotItemDetail.getPrinterGroupId(),
                            kotItemDetail.getKotSummaryId(),
                            kotItemDetail.getItemName(),
                            kotItemDetail.getItemNum(),
                            kotItemDetail.getFinishQty(),
                            kotItemDetail.getSessionStatus(),
                            kotItemDetail.getKotStatus(),
                            kotItemDetail.getSpecialInstractions(),
                            kotItemDetail.getVersion(),
                            kotItemDetail.getCreateTime(),
                            kotItemDetail.getUpdateTime(),
                            kotItemDetail.getUnFinishQty(),
                            kotItemDetail.getCategoryId(),
                            kotItemDetail.getIsTakeAway(),
                            kotItemDetail.getFireStatus(),
                            kotItemDetail.getCallType(),
                            kotItemDetail.getExpectedTime(),
                            kotItemDetail.getStartTime(),
                            kotItemDetail.getEndTime(),
                            kotItemDetail.getItemType(),
                            kotItemDetail.getItemId(),
                            kotItemDetail.getUniqueId(),
                            kotItemDetail.getKotSummaryUniqueId()
                    });
            kotItemDetail.getCallType();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateCallById(String uniqueId) {
        String sql = "update " + TableNames.KotItemDetail + " set callType = ? where uniqueId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{1, uniqueId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addKotItemDetailList(List<KotItemDetail> kotItemDetails) {
        if (kotItemDetails == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.KotItemDetail
                    + "(id, restaurantId, revenueId, orderId, orderDetailId,  printerGroupId, kotSummaryId, "
                    + "itemName,itemNum,finishQty,sessionStatus,"
                    + "kotStatus,specialInstractions ,version," +
                    "createTime,updateTime, unFinishQty, categoryId,isTakeAway, fireStatus,callType,expectedTime,startTime,endTime,itemType,itemId,uniqueId,kotSummaryUniqueId)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sql);
            for (KotItemDetail kotItemDetail : kotItemDetails) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        kotItemDetail.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        kotItemDetail.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        kotItemDetail.getRevenueId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        kotItemDetail.getOrderId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        kotItemDetail.getOrderDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        kotItemDetail.getPrinterGroupId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        kotItemDetail.getKotSummaryId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        kotItemDetail.getItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        kotItemDetail.getItemNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        kotItemDetail.getFinishQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        kotItemDetail.getSessionStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        kotItemDetail.getKotStatus());
                SQLiteStatementHelper.bindString(sqLiteStatement, 13,
                        kotItemDetail.getSpecialInstractions());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
                        kotItemDetail.getVersion());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
                        kotItemDetail.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        kotItemDetail.getUpdateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 17,
                        kotItemDetail.getUnFinishQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 18,
                        kotItemDetail.getCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 19,
                        kotItemDetail.getIsTakeAway());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
                        kotItemDetail.getFireStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 21,
                        kotItemDetail.getCallType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
                        kotItemDetail.getExpectedTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
                        kotItemDetail.getStartTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 24,
                        kotItemDetail.getEndTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 25,
                        kotItemDetail.getItemType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 26,
                        kotItemDetail.getItemId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 27,
                        kotItemDetail.getUniqueId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 28,
                        kotItemDetail.getKotSummaryUniqueId());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<KotItemDetail> getAllKotItemDetail() {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where categoryId = 0 order by id desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static ArrayList<KotItemDetail> getHistoryKotItemDetail() {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail +
                " where categoryId = 1 order by createTime desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static KotItemDetail getKotItemDetailById(int id) {
        KotItemDetail kotItemDetail = null;
        String sql = "select * from " + TableNames.KotItemDetail + " where id = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{id + ""});
            if (cursor.moveToFirst()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemDetail;
    }

    public static KotItemDetail getKotItemDetailByUniqueId(String uniqueId) {
        KotItemDetail kotItemDetail = null;
        String sql = "select * from " + TableNames.KotItemDetail + " where uniqueId = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{uniqueId + ""});
            if (cursor.moveToFirst()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemDetail;
    }


    public static KotItemDetail getSubKotItemDetailByMainKotItemDeail(KotItemDetail mainKotItemDetail) {
        KotItemDetail kotItemDetail = null;
        String sql = "select * from " + TableNames.KotItemDetail + " where kotSummaryId=? and orderDetailId = ? and unFinishQty = ? and categoryId = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{mainKotItemDetail.getKotSummaryId() + "", mainKotItemDetail.getOrderDetailId() + "", mainKotItemDetail.getUnFinishQty() + "", ParamConst.KOTITEMDETAIL_CATEGORYID_SUB + ""});
            if (cursor.moveToFirst()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemDetail;
    }

    public static KotItemDetail getLastKotItemDetailByOrderDetailId(int kotSummaryId, int orderDetailId) {
        KotItemDetail kotItemDetail = null;
        String sql = "select * from " + TableNames.KotItemDetail + " where kotSummaryId = ? and orderDetailId = ? and categoryId = ? order by id desc";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{kotSummaryId + "", String.valueOf(orderDetailId), ParamConst.KOTITEMDETAIL_CATEGORYID_SUB + ""});
            if (cursor.moveToFirst()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemDetail;
    }

    public static ArrayList<KotItemDetail> getKotItemDetailByOrderId(int orderId) {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where orderId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static ArrayList<KotItemDetail> getKotItemDetailByKotSummaryUniqueId(String kotSummaryUniqueId) {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryUniqueId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{kotSummaryUniqueId});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static ArrayList<KotItemDetail> getKotItemDetailByKotSummaryIdUndone(KotSummary kotSummary) {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and kotStatus <> " + ParamConst.KOT_STATUS_DONE;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{kotSummary.getId() + ""});
            int count = cursor.getCount();
            Log.i("KotItemDetailSQL", count + "");
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static ArrayList<KotItemDetail> getKotItemDetailByKotSummaryAndPrinterGroup(int kotSummaryId, int printerGroupId) {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and printerGroupId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{kotSummaryId + "", printerGroupId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static KotItemDetail getKotItemDetailByOrderDetailId(int kotSummaryId,
                                                                int orderDetailId) {
        KotItemDetail kotItemDetail = null;
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and orderDetailId = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{kotSummaryId + "", orderDetailId + ""});
            if (cursor.moveToFirst()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemDetail;
    }

    public static KotItemDetail getMainKotItemDetailByOrderDetailId(int kotSummaryId,
                                                                    int orderDetailId) {
        KotItemDetail kotItemDetail = null;
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and orderDetailId = ? and categoryId = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{kotSummaryId + "", orderDetailId + "", ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN + ""});
            if (cursor.moveToFirst()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemDetail;
    }


    public static List<KotItemDetail> getOtherSubKotItemDetailsByOrderDetailId(int kotSummaryId, KotItemDetail oldKotItemDetail) {
        List<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and orderDetailId = ? and id > ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{kotSummaryId + "", oldKotItemDetail.getOrderDetailId().intValue() + "", oldKotItemDetail.getId().intValue() + ""});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                KotItemDetail kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                kotItemDetails.add(kotItemDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return kotItemDetails;
    }

    public static ArrayList<KotItemDetail> getKotItemDetailBySummaryId(
            int kotSummaryId) {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and categoryId = 0 order by fireStatus desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{kotSummaryId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static ArrayList<KotItemDetail> getKotItemDetailBySummaryIdRvcId(
            int kotSummaryId, int rvcId) {
        ArrayList<KotItemDetail> result = new ArrayList<>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and revenueId = ? and categoryId = 0 order by fireStatus desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(kotSummaryId), String.valueOf(rvcId)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static int getKotItemDetailCountBySummaryId(
            int kotSummaryId) {
        return getKotItemDetailCountBySummaryId(kotSummaryId, 0);
    }

    public static int getKotItemDetailCountBySummaryId(
            int kotSummaryId, int rvcId) {
        int result = 0;
        String sql = "select id from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and unFinishQty > 0 and kotStatus < 3 and categoryId = 0";

        if (rvcId > 0) {
            sql = "select id from " + TableNames.KotItemDetail
                    + " where kotSummaryId = ? and revenueId = " + rvcId +
                    " and unFinishQty > 0 and kotStatus < 3 and categoryId = 0";
        }

        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{kotSummaryId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            result = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static int getKotItemDetailCountBySummaryUniqueId(String kotSummaryUniqueId) {
        int result = 0;
        String sql = "select id from " + TableNames.KotItemDetail
                + " where kotSummaryUniqueId = ? and unFinishQty > 0 and kotStatus < 3 and categoryId = 0";

        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{kotSummaryUniqueId});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            result = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static int getAllKotItemDetailCountBySummaryId(int kotSummaryId) {
        int result = 0;
        String sql = "select id from " + TableNames.KotItemDetail
                + " where kotSummaryId = ? and kotStatus < 4";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{kotSummaryId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            result = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static int getAllUnDoneKotItemDetailCount() {
        int result = 0;
        String sql = "select id from " + TableNames.KotItemDetail
                + " where unFinishQty > 0 and kotStatus < 3 and categoryId = 0";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            result = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static ArrayList<KotItemDetail> getKotItemDetailBySummaryIdandOrderId(int rvcId,
                                                                                 int kotSummaryId, int orderId) {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where revenueId = ? and kotSummaryId = ? and orderId=? and categoryId = 0 order by fireStatus desc, kotStatus asc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{rvcId + "", kotSummaryId + "", orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static ArrayList<KotItemDetail> getKotItemDetailBySummaryIdandOrderIdForMainPage(
            int rvcId, String koSummaryUniqueId, int orderId) {
        ArrayList<KotItemDetail> result = new ArrayList<>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where revenueId = ? and kotSummaryUniqueId = ? and orderId=? and categoryId = 0 and kotStatus < " + ParamConst.KOT_STATUS_DONE + " order by fireStatus desc, kotStatus asc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{rvcId + "", koSummaryUniqueId, orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
//                kotItemDetail.setUniqueId(cursor.getString(26));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static ArrayList<KotItemDetail> getKotItemDetailByDishName(
            String dishName) {
        ArrayList<KotItemDetail> result = new ArrayList<KotItemDetail>();
        String sql = "select * from " + TableNames.KotItemDetail
                + " where itemName = ? and categoryId = 0 and kotStatus < " + ParamConst.KOT_STATUS_DONE;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{dishName + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            KotItemDetail kotItemDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                kotItemDetail = new KotItemDetail();
//                kotItemDetail.setId(cursor.getInt(0));
//                kotItemDetail.setRestaurantId(cursor.getInt(1));
//                kotItemDetail.setRevenueId(cursor.getInt(2));
//                kotItemDetail.setOrderId(cursor.getInt(3));
//                kotItemDetail.setOrderDetailId(cursor.getInt(4));
//                kotItemDetail.setPrinterGroupId(cursor.getInt(5));
//                kotItemDetail.setKotSummaryId(cursor.getInt(6));
//                kotItemDetail.setItemName(cursor.getString(7));
//                kotItemDetail.setItemNum(cursor.getInt(8));
//                kotItemDetail.setFinishQty(cursor.getInt(9));
//                kotItemDetail.setSessionStatus(cursor.getInt(10));
//                kotItemDetail.setKotStatus(cursor.getInt(11));
//                kotItemDetail.setSpecialInstractions(cursor.getString(12));
//                kotItemDetail.setVersion(cursor.getInt(13));
//                kotItemDetail.setCreateTime(cursor.getLong(14));
//                kotItemDetail.setUpdateTime(cursor.getLong(15));
//                kotItemDetail.setUnFinishQty(cursor.getInt(16));
//                kotItemDetail.setCategoryId(cursor.getInt(17));
//                kotItemDetail.setIsTakeAway(cursor.getInt(18));
//                kotItemDetail.setFireStatus(cursor.getInt(19));
//                kotItemDetail.setCallType(cursor.getInt(20));
//                kotItemDetail.setExpectedTime(cursor.getLong(21));
//                kotItemDetail.setStartTime(cursor.getLong(22));
//                kotItemDetail.setEndTime(cursor.getLong(23));
//                kotItemDetail.setItemType(cursor.getInt(24));
//                kotItemDetail.setItemId(cursor.getInt(25));
                kotItemDetail.setId(cursor.getInt(0));
                kotItemDetail.setUniqueId(cursor.getString(1));
                kotItemDetail.setRestaurantId(cursor.getInt(2));
                kotItemDetail.setRevenueId(cursor.getInt(3));
                kotItemDetail.setOrderId(cursor.getInt(4));
                kotItemDetail.setOrderDetailId(cursor.getInt(5));
                kotItemDetail.setPrinterGroupId(cursor.getInt(6));
                kotItemDetail.setKotSummaryId(cursor.getInt(7));
                kotItemDetail.setItemName(cursor.getString(8));
                kotItemDetail.setItemNum(cursor.getInt(9));
                kotItemDetail.setFinishQty(cursor.getInt(10));
                kotItemDetail.setSessionStatus(cursor.getInt(11));
                kotItemDetail.setKotStatus(cursor.getInt(12));
                kotItemDetail.setSpecialInstractions(cursor.getString(13));
                kotItemDetail.setVersion(cursor.getInt(14));
                kotItemDetail.setCreateTime(cursor.getLong(15));
                kotItemDetail.setUpdateTime(cursor.getLong(16));
                kotItemDetail.setUnFinishQty(cursor.getInt(17));
                kotItemDetail.setCategoryId(cursor.getInt(18));
                kotItemDetail.setIsTakeAway(cursor.getInt(19));
                kotItemDetail.setFireStatus(cursor.getInt(20));
                kotItemDetail.setCallType(cursor.getInt(21));
                kotItemDetail.setExpectedTime(cursor.getLong(22));
                kotItemDetail.setStartTime(cursor.getLong(23));
                kotItemDetail.setEndTime(cursor.getLong(24));
                kotItemDetail.setItemType(cursor.getInt(25));
                kotItemDetail.setItemId(cursor.getInt(26));
                kotItemDetail.setKotSummaryUniqueId(cursor.getString(27));
                result.add(kotItemDetail);
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

    public static List<Integer> getPrinterGroupIds(KotSummary kotSummary) {
        List<Integer> result = new ArrayList<Integer>();
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().query(TableNames.KotItemDetail,
                    new String[]{"printerGroupId"},
                    " kotSummaryId = ?",
                    new String[]{kotSummary.getId() + ""}, "printerGroupId",
                    null, null);
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                result.add(cursor.getInt(0));
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

    public static void deleteKotItemDetail(KotItemDetail kotItemDetail) {
        String sql = "delete from " + TableNames.KotItemDetail
                + " where uniqueId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{kotItemDetail.getUniqueId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteKotItemDetail(int kotSummaryId, OrderDetail orderDetail) {
        String sql = "delete from " + TableNames.KotItemDetail
                + " where kotSummaryId= ? and orderDetailId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{kotSummaryId, orderDetail.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteKotItemDetail(List<KotItemDetail> kotItemDetails) {
        for (KotItemDetail kotItemDetail : kotItemDetails) {
            deleteKotItemDetail(kotItemDetail);
        }
    }

    public static void updateKotItemDetailId(int newOrderDetailId, int oldOrderDetailId, int kotSummaryId) {
        String sql = "update " + TableNames.KotItemDetail + " set orderDetailId = ? where orderDetailId = ? and kotSummaryId=?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{newOrderDetailId, oldOrderDetailId, kotSummaryId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateKotItemDetailField(String field, int value, int id) {
        String sql = "update " + TableNames.KotItemDetail + " set " + field + " = " + value + " where id = " + id;
        try {
            SQLExe.getDB().execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotItemDetail() {
        String sql = "delete from " + TableNames.KotItemDetail;
        String sssql = "select * from sqlite_sequence";
        String up = "update sqlite_sequence set seq=0 where name='" + TableNames.KotItemDetail + "'";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
            SQLExe.getDB().rawQuery(sssql, new String[]{});
            SQLExe.getDB().execSQL(up, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotItemDetail(Integer revenueId) {
        String deleteByRevenue = "";
        if (revenueId != null) {
            deleteByRevenue = " where revenueId = " + revenueId;
        }
        String sql = "delete from " + TableNames.KotItemDetail + deleteByRevenue;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotItemDetailByKotSummary(KotSummary kotSummary) {
        String sql = "delete from " + TableNames.KotItemDetail + " where kotSummaryId = ? and revenueId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{kotSummary.getId(), kotSummary.getRevenueCenterId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKotItemDetailByOrder(Order order) {
        String sql = "delete from " + TableNames.KotItemDetail
                + " where orderId = ? and numTag = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{order.getId(), order.getNumTag()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
