package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.Printer;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PrinterSQL {

    public static void addPrinter(Printer printer) {
        if (printer == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.Printer
                    + " (id, printerGroupName," +
                    "printerName,printerLocation,printerType,  "
                    + " qPrint, isCashdrawer,companyId, " +
                    " restaurantId,type,createTime,updateTime,printerGroupType,printerUsageType)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{printer.getId(),
                            printer.getPrinterGroupName(),
                            printer.getPrinterName(),
                            printer.getPrinterLocation(),
                            printer.getPrinterType(),
                            printer.getqPrint(),
                            printer.getIsCashdrawer(),
                            printer.getCompanyId(),
                            printer.getRestaurantId(),
                            printer.getType(),
                            printer.getCreateTime(),
                            printer.getUpdateTime(),
                            printer.getPrinterGroupType(),
                            printer.getPrinterUsageType()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPrinters(List<Printer> printers) {
        if (printers == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.Printer
                    + " (id,printerGroupName, printerName," +
                    "printerLocation,printerType," +
                    " qPrint, isCashdrawer,companyId," +
                    "restaurantId,type,createTime,updateTime,isLablePrinter, " +
                    "printerGroupType, printerUsageType)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (Printer printer : printers) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        printer.getId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 2,
                        printer.getPrinterGroupName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3,
                        printer.getPrinterName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 4,
                        printer.getPrinterLocation());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        printer.getPrinterType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6,
                        printer.getqPrint());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        printer.getIsCashdrawer());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        printer.getCompanyId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        printer.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        printer.getType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        printer.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        printer.getUpdateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
                        printer.getIsLablePrinter());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
                        printer.getPrinterGroupType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
                        printer.getPrinterUsageType());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<Printer> getAllPrinter() {
        ArrayList<Printer> result = new ArrayList<Printer>();
        String sql = "select * from " + TableNames.Printer;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            Printer printer = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                printer = new Printer();
                printer.setId(cursor.getInt(0));
                printer.setPrinterGroupName(cursor.getString(1));
                printer.setPrinterName(cursor.getString(2));
                printer.setPrinterLocation(cursor.getString(3));
                printer.setPrinterType(cursor.getString(4));
                printer.setqPrint(cursor.getString(5));
                printer.setIsCashdrawer(cursor.getInt(6));
                printer.setCompanyId(cursor.getInt(7));
                printer.setRestaurantId(cursor.getInt(8));
                printer.setType(cursor.getInt(9));
                printer.setCreateTime(cursor.getLong(10));
                printer.setUpdateTime(cursor.getLong(11));
                printer.setIsLablePrinter(cursor.getInt(12));
                printer.setPrinterUsageType(cursor.getInt(13));
                printer.setPrinterGroupType(cursor.getInt(14));
                result.add(printer);
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

    public static ArrayList<Printer> getAllPrinterByType(int type) {
        ArrayList<Printer> result = new ArrayList<Printer>();
        String sql = "select * from " + TableNames.Printer + " where type = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{type + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            Printer printer = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                printer = new Printer();
                printer.setId(cursor.getInt(0));
                printer.setPrinterGroupName(cursor.getString(1));
                printer.setPrinterName(cursor.getString(2));
                printer.setPrinterLocation(cursor.getString(3));
                printer.setPrinterType(cursor.getString(4));
                printer.setqPrint(cursor.getString(5));
                printer.setIsCashdrawer(cursor.getInt(6));
                printer.setCompanyId(cursor.getInt(7));
                printer.setRestaurantId(cursor.getInt(8));
                printer.setType(cursor.getInt(9));
                printer.setCreateTime(cursor.getLong(10));
                printer.setUpdateTime(cursor.getLong(11));
                printer.setIsLablePrinter(cursor.getInt(12));
                printer.setPrinterUsageType(cursor.getInt(13));
                printer.setPrinterGroupType(cursor.getInt(14));
                result.add(printer);
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

    public static ArrayList<Printer> getCashierPrinter() {
        ArrayList<Printer> result = new ArrayList<>();
        String sql = "select * from " + TableNames.Printer + " where type = 1 and isCashdrawer = 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            Printer printer = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                printer = new Printer();
                printer.setId(cursor.getInt(0));
                printer.setPrinterGroupName(cursor.getString(1));
                printer.setPrinterName(cursor.getString(2));
                printer.setPrinterLocation(cursor.getString(3));
                printer.setPrinterType(cursor.getString(4));
                printer.setqPrint(cursor.getString(5));
                printer.setIsCashdrawer(cursor.getInt(6));
                printer.setCompanyId(cursor.getInt(7));
                printer.setRestaurantId(cursor.getInt(8));
                printer.setType(cursor.getInt(9));
                printer.setCreateTime(cursor.getLong(10));
                printer.setUpdateTime(cursor.getLong(11));
                printer.setIsLablePrinter(cursor.getInt(12));
                printer.setPrinterUsageType(cursor.getInt(13));
                printer.setPrinterGroupType(cursor.getInt(14));
                result.add(printer);
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

    public static void deletePrinter(Printer printer) {
        String sql = "delete from " + TableNames.Printer + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{printer.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllPrinter() {
        String sql = "delete from " + TableNames.Printer;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
