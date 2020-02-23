package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alex on 16/9/24.
 */

public class TableInfoSQL {


    public static void addTables(TableInfo newTable) {
        if (newTable == null)
            return;
        try {
            String sql = "insert into "
                    + TableNames.TableInfo
                    + "(posId, name, imageName, restaurantId, revenueId, xAxis, yAxis, placesId, resolution, shape, type,"
                    + " status, isDecorate, unionId, isActive, packs, rotate, createTime, updateTime, orders, isKiosk, "
                    + " resolutionWidth, resolutionHeight, isLocked)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{newTable.getPosId(), newTable.getName(), newTable.getImageName(),
                            newTable.getRestaurantId(), newTable.getRevenueId(), newTable.getxAxis(),
                            newTable.getyAxis(), newTable.getPlacesId(), newTable.getResolution(),
                            newTable.getShape(), newTable.getType(), newTable.getStatus(),
                            newTable.getIsDecorate(), newTable.getUnionId(), newTable.getIsActive(),
                            newTable.getPacks(), newTable.getRotate(), newTable.getCreateTime(),
                            newTable.getUpdateTime(), newTable.getOrders(), newTable.getIsKiosk(),
                            newTable.getResolutionWidth(), newTable.getResolutionHeight(), newTable.getIsLocked()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 升级数据库tables数据以后，需要更新内存数据
     *
     * @param newTable
     */
    public static void updateTables(TableInfo newTable) {
        if (newTable == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.TableInfo
                    + "(posId, name, imageName, restaurantId, revenueId, xAxis, yAxis, placesId, resolution, shape, type,"
                    + " status, isDecorate, unionId, isActive, packs, rotate, createTime, updateTime, orders, isKiosk, "
                    + " resolutionWidth, resolutionHeight, isLocked)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{newTable.getPosId(), newTable.getName(), newTable.getImageName(),
                            newTable.getRestaurantId(), newTable.getRevenueId(), newTable.getxAxis(),
                            newTable.getyAxis(), newTable.getPlacesId(), newTable.getResolution(),
                            newTable.getShape(), newTable.getType(), newTable.getStatus(),
                            newTable.getIsDecorate(), newTable.getUnionId(), newTable.getIsActive(),
                            newTable.getPacks(), newTable.getRotate(), newTable.getCreateTime(),
                            newTable.getUpdateTime(), newTable.getOrders(), newTable.getIsKiosk(),
                            newTable.getResolutionWidth(), newTable.getResolutionHeight(), newTable.getIsLocked()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTablesList(List<TableInfo> newTablesList) {
        if (newTablesList == null || newTablesList.size() < 1) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.TableInfo
                    + "(posId, name, imageName, restaurantId, revenueId, xAxis, yAxis, placesId, resolution, shape, type,"
                    + " status, isDecorate, unionId, isActive, packs, rotate, createTime, updateTime, orders, isKiosk, "
                    + " resolutionWidth, resolutionHeight,isLocked)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (TableInfo newTable : newTablesList) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        newTable.getPosId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 2,
                        newTable.getName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3,
                        newTable.getImageName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        newTable.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        newTable.getRevenueId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6,
                        newTable.getxAxis());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        newTable.getyAxis());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        newTable.getPlacesId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        newTable.getResolution());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        newTable.getShape());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        newTable.getType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        newTable.getStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
                        newTable.getIsDecorate());
                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
                        newTable.getUnionId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
                        newTable.getIsActive());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        newTable.getPacks());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 17,
                        newTable.getRotate());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 18,
                        newTable.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 19,
                        newTable.getUpdateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
                        newTable.getOrders());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 21,
                        newTable.getIsKiosk());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
                        newTable.getResolutionWidth());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
                        newTable.getResolutionHeight());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 24,
                        newTable.getIsLocked());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static List<TableInfo> getAllTables() {
        return getAllTables(" and posId > 0");
    }

    public static List<TableInfo> getAllWaitingListTables() {
        return getAllTables(" and posId < 0");
    }

    public static List<TableInfo> getAllTables(String logicPosId) {
        List<TableInfo> result = new ArrayList<TableInfo>();
        String sql = "select * from " + TableNames.TableInfo + " where isKiosk <> 1 " + logicPosId;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            TableInfo newTable = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                newTable = new TableInfo();
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(cursor.getInt(15));
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
                result.add(newTable);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        if (result.size() > 0) {
            Collections.reverse(result);
        }
        return result;
    }

    public static List<TableInfo> getAllUsedTables() {
        List<TableInfo> result = new ArrayList<TableInfo>();
        String sql = "select * from " + TableNames.TableInfo + " where status <> " + ParamConst.TABLE_STATUS_IDLE + " and isKiosk <> 1  and posId > 0";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            TableInfo newTable = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                newTable = new TableInfo();
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(cursor.getInt(15));
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
                result.add(newTable);
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


    public static TableInfo getAllUsedOneTables() {
        String sql = "select * from " + TableNames.TableInfo + " where status = " + ParamConst.TABLE_STATUS_IDLE + " and isKiosk <> 1  and posId > 0";
        Cursor cursor = null;
        TableInfo newTable = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return newTable;
            }

            if (cursor.moveToFirst()) {
                newTable = new TableInfo();
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(cursor.getInt(15));
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newTable;
    }

    public static TableInfo getTableById(int id) {
        String sql = "select * from " + TableNames.TableInfo + " where posId = ?";
        TableInfo newTable = new TableInfo();
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(cursor.getInt(15));
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newTable;
    }

    public static TableInfo getTableByName(String name) {
        String sql = "select * from " + TableNames.TableInfo + " where name = ?";
        TableInfo newTable = null;
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{name});

            if (cursor.moveToFirst()) {
                newTable = new TableInfo();
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(cursor.getInt(15));
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newTable;
    }

    public static TableInfo getKioskTable() {
        String sql = "select * from " + TableNames.TableInfo + " where isKiosk = ?";
        TableInfo newTable = null;
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{String.valueOf(1)});

            if (cursor.moveToFirst()) {
                newTable = new TableInfo();
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(4);
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newTable;
    }


    public static void deleteTableInfo(int id) {
        String sql = "delete from " + TableNames.TableInfo + " where posId = ? ";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<TableInfo> getTableInfosByPlaces(PlaceInfo places) {
        return getTableInfosByPlaces(places, "and posId > 0");
    }

    public static List<TableInfo> getWaitingListInfosByPlaces(PlaceInfo places) {
        return getTableInfosByPlaces(places, "and posId < 0");
    }

    public static List<TableInfo> getTableInfosByPlaces(PlaceInfo places, String logic) {
        List<TableInfo> result = new ArrayList<TableInfo>();
        String sql = "select * from " + TableNames.TableInfo
                + " where placesId = ?  and isKiosk <> 1  " + logic;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{Integer.toString(places.getId())});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            TableInfo newTable = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                newTable = new TableInfo();
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(cursor.getInt(15));
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
                result.add(newTable);
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

    public static void setAllTableIdle() {
        String sql = "update " + TableNames.TableInfo + " set status = " + ParamConst.TABLE_STATUS_IDLE + " where status <> " + ParamConst.TABLE_STATUS_IDLE;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTableStatusById(int posId, int status) {
        String sql = "update " + TableNames.TableInfo + " set status = ? where  posId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{status, posId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllTableInfo() {
        String sql = "delete from " + TableNames.TableInfo;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTableInfoByPlaceId(int placeInfoId) {
        deleteTableInfoByPlaceId(placeInfoId, "and posId > 0");
    }

    public static void deleteWaitingListByPlaceId(int placeInfoId) {
        deleteTableInfoByPlaceId(placeInfoId, "and posId < 0");
    }

    public static void deleteTableInfoByPlaceId(int placeInfoId, String logicPosId) {
        String sql = "delete from " + TableNames.TableInfo + " where placesId = ?  " + logicPosId;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{placeInfoId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TableInfo getFirstTables() {
        TableInfo result = null;
        String sql = "select * from " + TableNames.TableInfo + " where isKiosk <> 1  ORDER BY posId ASC LIMIT 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            TableInfo newTable = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                newTable = new TableInfo();
                newTable.setPosId(cursor.getInt(0));
                newTable.setName(cursor.getString(1));
                newTable.setImageName(cursor.getString(2));
                newTable.setRestaurantId(cursor.getInt(3));
                newTable.setRevenueId(cursor.getInt(4));
                newTable.setxAxis(cursor.getString(5));
                newTable.setyAxis(cursor.getString(6));
                newTable.setPlacesId(cursor.getInt(7));
                newTable.setResolution(cursor.getInt(8));
                newTable.setShape(cursor.getInt(9));
                newTable.setType(cursor.getInt(10));
                newTable.setStatus(cursor.getInt(11));
                newTable.setIsDecorate(cursor.getInt(12));
                newTable.setUnionId(cursor.getString(13));
                newTable.setIsActive(cursor.getInt(14));
                newTable.setPacks(cursor.getInt(15));
                newTable.setRotate(cursor.getInt(16));
                newTable.setCreateTime(cursor.getLong(17));
                newTable.setUpdateTime(cursor.getLong(18));
                newTable.setOrders(cursor.getInt(19));
                newTable.setIsKiosk(cursor.getInt(20));
                newTable.setResolutionWidth(cursor.getInt(21));
                newTable.setResolutionHeight(cursor.getInt(22));
                newTable.setIsLocked(cursor.getInt(23));
                result = newTable;
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


}
