package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PrinterGroupSQL {

    public static void addPrinterGroup(PrinterGroup printerGroup) {
        if (printerGroup == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.PrinterGroup
                    + "(id, printerGroupId,printerId,companyId,restaurantId,printerType,isChildGroup,sequenceNumber)"
                    + " values (?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{printerGroup.getId(),
                            printerGroup.getPrinterGroupId(),
                            printerGroup.getPrinterId(),
                            printerGroup.getCompanyId(),
                            printerGroup.getRestaurantId(),
                            printerGroup.getPrinterType(),
                            printerGroup.getIsChildGroup(),
                            printerGroup.getSequenceNumber()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPrinterGroups(List<PrinterGroup> printerGroups) {
        if (printerGroups == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.PrinterGroup
                    + "(id,printerGroupId,printerId,companyId,restaurantId,printerType,isChildGroup,sequenceNumber)"
                    + " values (?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (PrinterGroup printer : printerGroups) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        printer.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        printer.getPrinterGroupId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        printer.getPrinterId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        printer.getCompanyId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        printer.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        printer.getPrinterType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        printer.getIsChildGroup());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        printer.getSequenceNumber());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<PrinterGroup> getAllPrinterGroup() {
        ArrayList<PrinterGroup> result = new ArrayList<PrinterGroup>();
        String sql = "select * from " + TableNames.PrinterGroup + " group by  printerGroupId,printerId";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return null;
            }
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                PrinterGroup pg = new PrinterGroup();
                pg.setId(cursor.getInt(0));
                pg.setPrinterGroupId(cursor.getInt(1));
                pg.setPrinterId(cursor.getInt(2));
                pg.setCompanyId(cursor.getInt(3));
                pg.setRestaurantId(cursor.getInt(4));
                pg.setPrinterType(cursor.getInt(5));
                pg.setIsChildGroup(cursor.getInt(6));
                pg.setSequenceNumber(cursor.getInt(7));
                result.add(pg);
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

    public static void deletePrinter(PrinterGroup printerGroup) {
        String sql = "delete from " + TableNames.PrinterGroup + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{printerGroup.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePrinter() {
        String sql = "delete from " + TableNames.PrinterGroup;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
