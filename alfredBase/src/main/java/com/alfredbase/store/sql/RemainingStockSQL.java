package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class RemainingStockSQL {


    public static void addRemainingStock(RemainingStock remainingStock) {
        if (remainingStock == null) {
            return;
        }
        try {
            String sql = "insert into "
                    + TableNames.RemainingStock
                    + "(id, restaurantId, itemId, qty, defultQty, minQty, isActive, "
                    + "displayQty, createTime, updateTime, resetTime )"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { remainingStock.getId(), remainingStock.getRestaurantId(),
                            remainingStock.getItemId(), remainingStock.getQty(),
                            remainingStock.getDefultQty(), remainingStock.getQty(),
                            remainingStock.getIsActive(), remainingStock.getDisplayQty(),
                            remainingStock.getCreateTime(), remainingStock.getUpdateTime(),
                            remainingStock.getResetTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addRemainingStock(List<RemainingStock> remainingStocks) {
        if (remainingStocks == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "insert into "
                    + TableNames.RemainingStock
                    + "(id, restaurantId, itemId, qty, defultQty, minQty, isActive, "
                    + "displayQty, createTime, updateTime, resetTime )"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (RemainingStock remainingStock : remainingStocks) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        remainingStock.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        remainingStock.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        remainingStock.getItemId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        remainingStock.getQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        remainingStock.getDefultQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        remainingStock.getMinQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        remainingStock.getIsActive());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        remainingStock.getDisplayQty());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        remainingStock.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        remainingStock.getUpdateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        remainingStock.getResetTime());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<RemainingStock> getAllRemainingStock() {
        ArrayList<RemainingStock> result = new ArrayList<RemainingStock>();
        String sql = "select * from " + TableNames.RemainingStock + " order by id desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            RemainingStock remainingStock = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {

                remainingStock = new RemainingStock();
                remainingStock.setId(cursor.getInt(0));
                remainingStock.setRestaurantId(cursor.getInt(1));
                remainingStock.setItemId(cursor.getInt(2));
                remainingStock.setQty(cursor.getInt(3));
                remainingStock.setDefultQty(cursor.getInt(4));
                remainingStock.setMinQty(cursor.getInt(5));
                remainingStock.setIsActive(cursor.getInt(6));
                remainingStock.setDisplayQty(cursor.getInt(7));
                remainingStock.setCreateTime(cursor.getLong(8));
                remainingStock.setUpdateTime(cursor.getLong(9));
                remainingStock.setResetTime(cursor.getLong(10));

                result.add(remainingStock);
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


    public static void updateRemainingById(int num,
                                           int itemId) {
        try {
            String sql = "update " + TableNames.RemainingStock
                    + " set qty=(qty-?) where itemId = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{num,itemId});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addRemainingById(int num,
                                           int itemId) {
        try {
            String sql = "update " + TableNames.RemainingStock
                    + " set qty=(qty+?) where itemId = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{num,itemId});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateRemainingNum(int num,
                                           int itemId) {
        try {
            String sql = "update " + TableNames.RemainingStock
                    + " set qty=? where itemId = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{num,itemId});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static RemainingStock getRemainingStockByitemId(int itemId) {
        RemainingStock remainingStock = null;
        String sql = "select * from " + TableNames.RemainingStock
                + " where itemId = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[] { itemId + "" });
            int count = cursor.getCount();
            if (count < 1) {
                return remainingStock;
            }
            if (cursor.moveToFirst()) {
                remainingStock = new RemainingStock();
                remainingStock = new RemainingStock();
                remainingStock.setId(cursor.getInt(0));
                remainingStock.setRestaurantId(cursor.getInt(1));
                remainingStock.setItemId(cursor.getInt(2));
                remainingStock.setQty(cursor.getInt(3));
                remainingStock.setDefultQty(cursor.getInt(4));
                remainingStock.setMinQty(cursor.getInt(5));
                remainingStock.setIsActive(cursor.getInt(6));
                remainingStock.setDisplayQty(cursor.getInt(7));
                remainingStock.setCreateTime(cursor.getLong(8));
                remainingStock.setUpdateTime(cursor.getLong(9));
                remainingStock.setResetTime(cursor.getLong(10));
                return remainingStock;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return remainingStock;
    }
    public static void update(RemainingStock remainingStock) {
        if (remainingStock == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.RemainingStock
                    + "(id, restaurantId, itemId, qty, defultQty, minQty, isActive, "
                    + "displayQty, createTime, updateTime, resetTime )"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { remainingStock.getId(), remainingStock.getRestaurantId(),
                            remainingStock.getItemId(), remainingStock.getQty(),
                            remainingStock.getDefultQty(), remainingStock.getQty(),
                            remainingStock.getIsActive(), remainingStock.getDisplayQty(),
                            remainingStock.getCreateTime(), remainingStock.getUpdateTime(),
                            remainingStock.getResetTime()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteRemainingStock(RemainingStock remainingStock) {
        String sql = "delete from " + TableNames.RemainingStock + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] { remainingStock.getId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteAllRemainingStock() {
        String sql = "delete from " + TableNames.RemainingStock;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
