package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PlaceInfoSQL {

    public static void addPlaceInfo(PlaceInfo places) {
        if (places == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.PlaceInfo
                    + "(posId, placeName,placeDescription,restaurantId,revenueId,unionId,isActive,isKiosk)"
                    + " values (?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{places.getId(), places.getPlaceName(), places.getPlaceDescription(),
                            places.getRestaurantId(), places.getRevenueId(), places.getUnionId(),
                            places.getIsActive(), places.getIsKiosk()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPlaceInfoList(List<PlaceInfo> placeslList) {
        if (placeslList == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.PlaceInfo
                    + "(posId, placeName,placeDescription,restaurantId,revenueId,unionId, isActive, isKiosk)"
                    + " values (?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (PlaceInfo places : placeslList) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        places.getId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 2,
                        places.getPlaceName());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3,
                        places.getPlaceDescription());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        places.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        places.getRevenueId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6,
                        places.getUnionId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        places.getIsActive());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        places.getIsKiosk());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<PlaceInfo> getAllPlaceInfo() {
        ArrayList<PlaceInfo> result = new ArrayList<PlaceInfo>();
        String sql = "select * from " + TableNames.PlaceInfo + " where isKiosk <> 1";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            PlaceInfo places = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                places = new PlaceInfo();
                places.setId(cursor.getInt(0));
                places.setPlaceName(cursor.getString(1));
                places.setPlaceDescription(cursor.getString(2));
                places.setRestaurantId(cursor.getInt(3));
                places.setRevenueId(cursor.getInt(4));
                places.setUnionId(cursor.getString(5));
                places.setIsActive(cursor.getInt(6));
                places.setIsKiosk(cursor.getInt(7));
                result.add(places);
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


    public static ArrayList<PlaceInfo> getAllKioskPlaceInfo() {
        ArrayList<PlaceInfo> result = new ArrayList<PlaceInfo>();
        String sql = "select * from " + TableNames.PlaceInfo + " where isKiosk = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{"1"});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            PlaceInfo places = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                places = new PlaceInfo();
                places.setId(cursor.getInt(0));
                places.setPlaceName(cursor.getString(1));
                places.setPlaceDescription(cursor.getString(2));
                places.setRestaurantId(cursor.getInt(3));
                places.setRevenueId(cursor.getInt(4));
                places.setUnionId(cursor.getString(5));
                places.setIsActive(cursor.getInt(6));
                places.setIsKiosk(cursor.getInt(7));
                result.add(places);
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

    public static PlaceInfo getPlaceInfoById(int placeId) {
        PlaceInfo places = new PlaceInfo();
        String sql = "select * from " + TableNames.PlaceInfo + " where posId = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{String.valueOf(placeId)});
            if (cursor.moveToFirst()) {
                places.setId(cursor.getInt(0));
                places.setPlaceName(cursor.getString(1));
                places.setPlaceDescription(cursor.getString(2));
                places.setRestaurantId(cursor.getInt(3));
                places.setRevenueId(cursor.getInt(4));
                places.setUnionId(cursor.getString(5));
                places.setIsActive(cursor.getInt(6));
                places.setIsKiosk(cursor.getInt(7));
                return places;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return places;
    }

    public static PlaceInfo getKioskPlaceInfo() {
        PlaceInfo places = null;
        String sql = "select * from " + TableNames.PlaceInfo + " where isKiosk = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{String.valueOf(1)});
            if (cursor.moveToFirst()) {
                places = new PlaceInfo();
                places.setId(cursor.getInt(0));
                places.setPlaceName(cursor.getString(1));
                places.setPlaceDescription(cursor.getString(2));
                places.setRestaurantId(cursor.getInt(3));
                places.setRevenueId(cursor.getInt(4));
                places.setUnionId(cursor.getString(5));
                places.setIsActive(cursor.getInt(6));
                places.setIsKiosk(cursor.getInt(7));
                return places;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return places;
    }


    public static void updatePlaceInfo(PlaceInfo places) {
        String sql = "update " + TableNames.PlaceInfo + " set placeName = ? where posId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{places.getPlaceName(), places.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePlaceInfo(PlaceInfo places) {
        String sql = "delete from " + TableNames.PlaceInfo + " where posId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{places.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllPlaceInfo() {
        String sql = "delete from " + TableNames.PlaceInfo;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean checkPlaceInfoExistByName(String name) {
        PlaceInfo places = new PlaceInfo();
        String sql = "select * from " + TableNames.PlaceInfo + " where placeName = ?";
        Cursor cursor = null;
        cursor = SQLExe.getDB().rawQuery(sql, new String[]{name});
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        }
        return false;

    }
}
