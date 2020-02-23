package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ReportDayPromotion;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportPromotionSQL {
	public static void addReportPromotion(ReportDayPromotion reportDayPromotion){
		if(reportDayPromotion == null){
			return;
		}
		try {
//			private long createTime;
//			private long updateTime;
///	private long sysCreateTime;
//			private long sysUpdateTime;
			
			String sql = "replace into " 
					+ TableNames.ReportDayPromotion
					+ "(id, restaurantId, revenueId, revenueName, businessDate, promotionQty,promotionAmount, " +
					"promotionName,promotionId, daySalesId,createTime,updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(sql, new Object[]{
					reportDayPromotion.getId(),
					reportDayPromotion.getRestaurantId(),
					reportDayPromotion.getRevenueId(),
					reportDayPromotion.getRevenueName(),
					reportDayPromotion.getBusinessDate(),
					reportDayPromotion.getPromotionQty(),
					reportDayPromotion.getPromotionAmount(),
					reportDayPromotion.getPromotionName(),
					reportDayPromotion.getPromotionId(),
					reportDayPromotion.getDaySalesId(),
					reportDayPromotion.getCreateTime(),
					reportDayPromotion.getUpdateTime()

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void addReportPromotion(List<ReportDayPromotion> reportDayPromotions){
		if(reportDayPromotions == null){
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
            String sql = "replace into "
                    + TableNames.ReportDayPromotion
                    + "( restaurantId, revenueId, revenueName, businessDate, promotionQty,promotionAmount," +
					" promotionName,promotionId, daySalesId,createTime,updateTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportDayPromotion reportDayPromotion : reportDayPromotions) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportDayPromotion.getRestaurantId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportDayPromotion.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportDayPromotion.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportDayPromotion.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 5, reportDayPromotion.getPromotionQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 6, reportDayPromotion.getPromotionAmount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 7, reportDayPromotion.getPromotionName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8, reportDayPromotion.getPromotionId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportDayPromotion.getDaySalesId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 10, reportDayPromotion.getCreateTime());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 11, reportDayPromotion.getUpdateTime());

				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

    public static void addReportPromotion(int daySalesId, SQLiteDatabase db,List<ReportDayPromotion> reportDayPromotions){
        if(reportDayPromotions == null){
            return;
        }

        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.ReportDayPromotion
                    + "( restaurantId, revenueId, revenueName, businessDate, promotionQty," +
					"promotionAmount, promotionName,promotionId, daySalesId,createTime,updateTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sql);
            for (ReportDayPromotion reportDayPromotion : reportDayPromotions) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportDayPromotion.getRestaurantId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportDayPromotion.getRevenueId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportDayPromotion.getRevenueName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportDayPromotion.getBusinessDate());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5, reportDayPromotion.getPromotionQty());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6, reportDayPromotion.getPromotionAmount());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7, reportDayPromotion.getPromotionName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8, reportDayPromotion.getPromotionId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportDayPromotion.getDaySalesId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 10, reportDayPromotion.getCreateTime());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 11, reportDayPromotion.getUpdateTime());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

	public static ArrayList<ReportDayPromotion> getAllReportPromotion(){
		ArrayList<ReportDayPromotion> reportDayPromotions = new ArrayList<ReportDayPromotion>();
		String sql = "select * from " + TableNames.ReportDayPromotion;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[]{});
			int count = cursor.getCount();
			if(count < 1){
				return reportDayPromotions;
			}
            ReportDayPromotion reportDayPromotion = null;
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                reportDayPromotion = new ReportDayPromotion();
				reportDayPromotion.setId(cursor.getInt(0));
				reportDayPromotion.setRestaurantId(cursor.getInt(1));
				reportDayPromotion.setRevenueId(cursor.getInt(2));
				reportDayPromotion.setRevenueName(cursor.getString(3));
				reportDayPromotion.setBusinessDate(cursor.getLong(4));
				reportDayPromotion.setPromotionQty(cursor.getInt(5));
				reportDayPromotion.setPromotionAmount(cursor.getString(6));
				reportDayPromotion.setPromotionName(cursor.getString(7));
				reportDayPromotion.setPromotionId(cursor.getInt(8));
                reportDayPromotion.setDaySalesId(cursor.getInt(9));
                reportDayPromotion.setCreateTime(cursor.getLong(10));
                reportDayPromotion.setUpdateTime(cursor.getLong(11));

                reportDayPromotions.add(reportDayPromotion);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return reportDayPromotions;
	}


	public static ArrayList<ReportDayPromotion> getReportPromotionByTime(long businessDate){
		ArrayList<ReportDayPromotion> reportDayPromotions = new ArrayList<ReportDayPromotion>();

        String sql = "select id, restaurantId, revenueId, revenueName, businessDate,sum(promotionQty), sum(promotionAmount),promotionName,promotionId, daySalesId ,createTime,updateTime from "
                + TableNames.ReportDayPromotion
                + " where businessDate = ? ";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[]{String.valueOf(businessDate)});
			int count = cursor.getCount();
			if(count < 1){
				return reportDayPromotions;
			}
            ReportDayPromotion reportDayPromotion = null;
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                reportDayPromotion = new ReportDayPromotion();
                reportDayPromotion.setId(cursor.getInt(0));
                reportDayPromotion.setRestaurantId(cursor.getInt(1));
                reportDayPromotion.setRevenueId(cursor.getInt(2));
                reportDayPromotion.setRevenueName(cursor.getString(3));
                reportDayPromotion.setBusinessDate(cursor.getLong(4));
                reportDayPromotion.setPromotionQty(cursor.getInt(5));
                reportDayPromotion.setPromotionAmount(cursor.getString(6));
                reportDayPromotion.setPromotionName(cursor.getString(7));
                reportDayPromotion.setPromotionId(cursor.getInt(8));
                reportDayPromotion.setDaySalesId(cursor.getInt(9));
				reportDayPromotion.setCreateTime(cursor.getLong(10));
				reportDayPromotion.setUpdateTime(cursor.getLong(11));

                reportDayPromotions.add(reportDayPromotion);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return reportDayPromotions;
	}
	
//	public static void deleteReportHourlyByBusinessDate(long businessDate) {
//		String sql = "delete from " + TableNames.ReportDayPromotion
//				+ " where businessDate = ?";
//		try {
//			SQLExe.getDB().execSQL(sql,
//					new Object[] { businessDate + "" });
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
