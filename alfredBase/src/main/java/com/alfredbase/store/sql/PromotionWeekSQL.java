package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.Promotion;
import com.alfredbase.javabean.PromotionWeek;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class PromotionWeekSQL {
    
    public static void addPromotionWeek(List<PromotionWeek> promotionWeeks) {
        if (promotionWeeks == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();

            String sql = "insert into "
                    + TableNames.PromotionWeek
                    + "(id, promotionId, week, startTime, endTime,isActive,createTime,updateTime,promotionDateInfoId)"
                    + " values (?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (PromotionWeek promotionWeek : promotionWeeks) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        promotionWeek.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        promotionWeek.getPromotionId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        promotionWeek.getWeek());
                SQLiteStatementHelper.bindString(sqLiteStatement, 4,
                        promotionWeek.getStartTime());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        promotionWeek.getEndTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        promotionWeek.getIsActive());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        promotionWeek.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        promotionWeek.getUpdateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        promotionWeek.getPromotionDateInfoId());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<PromotionWeek> getAllPromotionWeek() {
        ArrayList<PromotionWeek> result = new ArrayList<PromotionWeek>();
        String sql = "select * from " + TableNames.PromotionWeek + " order by id desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            PromotionWeek promotionWeek = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {

                promotionWeek = new PromotionWeek();
                promotionWeek.setId(cursor.getInt(0));
                promotionWeek.setPromotionId(cursor.getInt(1));
                promotionWeek.setWeek(cursor.getInt(2));
                promotionWeek.setStartTime(cursor.getString(3));
                promotionWeek.setEndTime(cursor.getString(4));
                promotionWeek.setIsActive(cursor.getInt(5));
                promotionWeek.setCreateTime(cursor.getLong(6));
                promotionWeek.setUpdateTime(cursor.getLong(7));
                promotionWeek.setPromotionDateInfoId(cursor.getInt(8));
                result.add(promotionWeek);
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




    public static void deletePromotionWeek(PromotionWeek promotionWeek) {
        String sql = "delete from " + TableNames.PromotionWeek + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] { promotionWeek.getId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteAllPromotionWeek() {
        String sql = "delete from " + TableNames.PromotionWeek;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
