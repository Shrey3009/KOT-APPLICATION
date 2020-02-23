package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportDayPaymentSQL {



    public static void addReportDayPaymentList(List<ReportDayPayment> reportDayPayments) {
        if (reportDayPayments == null) {
            return;
        }


        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();

            String sql = "replace into "
                    + TableNames.ReportDayPayment
                    + "(daySalesId,restaurantId,restaurantName,revenueId,revenueName,businessDate,paymentTypeId,paymentName,paymentQty,paymentAmount, overPaymentAmount, createTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (ReportDayPayment reportDayPayment : reportDayPayments) {

                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        reportDayPayment.getDaySalesId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        reportDayPayment.getRestaurantId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3,
                        reportDayPayment.getRestaurantName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        reportDayPayment.getRevenueId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        reportDayPayment.getRevenueName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        reportDayPayment.getBusinessDate());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        reportDayPayment.getPaymentTypeId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        reportDayPayment.getPaymentName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        reportDayPayment.getPaymentQty());
                SQLiteStatementHelper.bindString(sqLiteStatement, 10,
                        reportDayPayment.getPaymentAmount());
                SQLiteStatementHelper.bindString(sqLiteStatement, 11,
                        reportDayPayment.getOverPaymentAmount());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        reportDayPayment.getCreateTime());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    public static void addReportDayPayment(ReportDayPayment reportDayPayment) {
        if (reportDayPayment == null) {
            return;
        }
        try {

            String sql = "replace into "
                    + TableNames.ReportDayPayment
                    + "(id,daySalesId,restaurantId,restaurantName,revenueId,revenueName,businessDate,paymentTypeId,paymentName,paymentQty,paymentAmount, overPaymentAmount, createTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,new Object[]{
                            reportDayPayment.getId(),
                            reportDayPayment.getDaySalesId(),
                            reportDayPayment.getRestaurantId(),
                            reportDayPayment.getRestaurantName(),
                            reportDayPayment.getRevenueId(),
                            reportDayPayment.getRevenueName(),
                            reportDayPayment.getBusinessDate(),
                            reportDayPayment.getPaymentTypeId(),
                            reportDayPayment.getPaymentName(),
                            reportDayPayment.getPaymentQty(),
                            reportDayPayment.getPaymentAmount(),
                            reportDayPayment.getOverPaymentAmount(),
                            reportDayPayment.getCreateTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addReportDayPayment(SQLiteDatabase db, ReportDayPayment reportDayPayment) {
        if (reportDayPayment == null) {
            return;
        }
        try {

            String sql = "replace into "
                    + TableNames.ReportDayPayment
                    + "(id,daySalesId,restaurantId,restaurantName,revenueId,revenueName,businessDate,paymentTypeId,paymentName,paymentQty,paymentAmount, overPaymentAmount, createTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            db.execSQL(
                    sql,new Object[]{
                            reportDayPayment.getId(),
                            reportDayPayment.getDaySalesId(),
                            reportDayPayment.getRestaurantId(),
                            reportDayPayment.getRestaurantName(),
                            reportDayPayment.getRevenueId(),
                            reportDayPayment.getRevenueName(),
                            reportDayPayment.getBusinessDate(),
                            reportDayPayment.getPaymentTypeId(),
                            reportDayPayment.getPaymentName(),
                            reportDayPayment.getPaymentQty(),
                            reportDayPayment.getPaymentAmount(),
                            reportDayPayment.getOverPaymentAmount(),
                            reportDayPayment.getCreateTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<ReportDayPayment> getAllReportDayPayment() {
        ArrayList<ReportDayPayment> result = new ArrayList<ReportDayPayment>();
        String sql = "select * from " + TableNames.ReportDayPayment;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ReportDayPayment reportDayPayment = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                reportDayPayment = new ReportDayPayment();
                reportDayPayment.setId(cursor.getInt(0));

                reportDayPayment.setDaySalesId(cursor.getInt(1));
                reportDayPayment.setRestaurantId(cursor.getInt(2));
                reportDayPayment.setRestaurantName(cursor.getString(3));
                reportDayPayment.setRevenueId(cursor.getInt(4));
                reportDayPayment.setRevenueName(cursor.getString(5));

                reportDayPayment.setBusinessDate(cursor.getLong(6));
                reportDayPayment.setPaymentTypeId(cursor.getLong(7));
                reportDayPayment.setPaymentName(cursor.getString(8));
                reportDayPayment.setPaymentQty(cursor.getInt(9));
                reportDayPayment.setPaymentAmount(cursor.getString(10));
                reportDayPayment.setOverPaymentAmount(cursor.getString(11));
                reportDayPayment.setCreateTime(cursor.getLong(13));
                result.add(reportDayPayment);
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
    public static ArrayList<ReportDayPayment> getAllReportDayPaymentByDaySalesId(int daySalesId) {
        ArrayList<ReportDayPayment> result = new ArrayList<>();
        String sql = "select * from " + TableNames.ReportDayPayment + " where daySalesId = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {daySalesId+""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ReportDayPayment reportDayPayment = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                reportDayPayment = new ReportDayPayment();
                reportDayPayment.setId(cursor.getInt(0));

                reportDayPayment.setDaySalesId(cursor.getInt(1));
                reportDayPayment.setRestaurantId(cursor.getInt(2));
                reportDayPayment.setRestaurantName(cursor.getString(3));
                reportDayPayment.setRevenueId(cursor.getInt(4));
                reportDayPayment.setRevenueName(cursor.getString(5));

                reportDayPayment.setBusinessDate(cursor.getLong(6));
                reportDayPayment.setPaymentTypeId(cursor.getLong(7));
                reportDayPayment.setPaymentName(cursor.getString(8));
                reportDayPayment.setPaymentQty(cursor.getInt(9));
                reportDayPayment.setPaymentAmount(cursor.getString(10));
                reportDayPayment.setOverPaymentAmount(cursor.getString(11));
                reportDayPayment.setCreateTime(cursor.getLong(13));
                result.add(reportDayPayment);
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


    public static List<ReportDayPayment> getReportDayPaymentsForZReport(long businessDate){
        List<ReportDayPayment> result = new ArrayList<>();
        String sql = "select restaurantId,restaurantName,revenueId,revenueName,businessDate,"
                + " paymentTypeId,paymentName,sum(paymentQty),sum(paymentAmount), sum(overPaymentAmount) from "
                + TableNames.ReportDayPayment
                + " where businessDate = ? group by paymentTypeId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] { String.valueOf(businessDate) });
            int count = cursor.getCount();
            if(count < 1){
                return result;
            }
            ReportDayPayment reportDayPayment = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                reportDayPayment = new ReportDayPayment();
                reportDayPayment.setRestaurantId(cursor.getInt(0));
                reportDayPayment.setRestaurantName(cursor.getString(1));
                reportDayPayment.setRevenueId(cursor.getInt(2));
                reportDayPayment.setRevenueName(cursor.getString(3));
                reportDayPayment.setBusinessDate(cursor.getLong(4));
                reportDayPayment.setPaymentTypeId(cursor.getLong(5));
                reportDayPayment.setPaymentName(cursor.getString(6));
                reportDayPayment.setPaymentQty(cursor.getInt(7));
                reportDayPayment.setPaymentAmount(cursor.getString(8));
                reportDayPayment.setOverPaymentAmount(cursor.getString(9));
                result.add(reportDayPayment);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }



    public static void deleteReportDayPayment(ReportDayPayment reportDayPayment) {
        String sql = "delete from " + TableNames.ReportDayPayment + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] { reportDayPayment.getId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllReportDayPayment() {
        String sql = "delete from " + TableNames.ReportDayPayment;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
