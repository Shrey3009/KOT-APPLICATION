package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.alfredbase.ParamHelper;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class RevenueCenterSQL {

	public static void addRevenueCenter(RevenueCenter revenueCenter) {
		if (revenueCenter == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.RevenueCenter
					+ "(id,restaurantId, printId,revName, isActive,createTime,updateTime,happy_hour_id,happy_start_time,happy_end_time, currentBillNo, indexId, currentValue, isKiosk, currentReportNo)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { revenueCenter.getId(),
							revenueCenter.getRestaurantId(),
							revenueCenter.getPrintId(),
							revenueCenter.getRevName(),
							revenueCenter.getIsActive(),
							revenueCenter.getCreateTime(),
							revenueCenter.getUpdateTime(),
							revenueCenter.getHappyHourId(),
							revenueCenter.getHappyStartTime(),
							revenueCenter.getHappyEndTime(),
							revenueCenter.getCurrentBillNo(),
							revenueCenter.getIndexId(),
							revenueCenter.getCurrentValue(),
							revenueCenter.getIsKiosk(),
							revenueCenter.getCurrentReportNo()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addRevenueCenters(List<RevenueCenter> revenueCenters) {
		if (revenueCenters == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.RevenueCenter
					+ " (id,restaurantId, printId,revName, isActive,createTime,updateTime,happy_hour_id,happy_start_time,happy_end_time, currentBillNo, indexId, currentValue, isKiosk, currentReportNo)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (RevenueCenter revenueCenter : revenueCenters) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							revenueCenter.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							revenueCenter.getRestaurantId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							revenueCenter.getPrintId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							revenueCenter.getRevName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							revenueCenter.getIsActive());
					SQLiteStatementHelper.bindString(sqLiteStatement, 6,
							revenueCenter.getCreateTime());
					SQLiteStatementHelper.bindString(sqLiteStatement, 7,
							revenueCenter.getUpdateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							revenueCenter.getHappyHourId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							revenueCenter.getHappyStartTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
							revenueCenter.getHappyEndTime());
					SQLiteStatementHelper.bindString(sqLiteStatement, 11,
							revenueCenter.getCurrentBillNo());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							revenueCenter.getIndexId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
							revenueCenter.getCurrentValue());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
							revenueCenter.getIsKiosk());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
							revenueCenter.getCurrentReportNo());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<RevenueCenter> getAllRevenueCenter() {
		ArrayList<RevenueCenter> result = new ArrayList<RevenueCenter>();
		String sql = "select * from " + TableNames.RevenueCenter;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			RevenueCenter revenueCenter = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				revenueCenter = new RevenueCenter();
				revenueCenter.setId(cursor.getInt(0));
				revenueCenter.setRestaurantId(cursor.getInt(1));
				revenueCenter.setPrintId(cursor.getInt(2));
				revenueCenter.setRevName(cursor.getString(3));
				revenueCenter.setIsActive(cursor.getInt(4));
				revenueCenter.setCreateTime(cursor.getString(5));
				revenueCenter.setUpdateTime(cursor.getString(6));
				revenueCenter.setHappyHourId(cursor.getInt(7));
				revenueCenter.setHappyStartTime(cursor.getLong(8));
				revenueCenter.setHappyEndTime(cursor.getLong(9));
				revenueCenter.setCurrentBillNo(cursor.getString(10) == null ? "0" : cursor.getString(10));
				revenueCenter.setIndexId(cursor.getInt(11));
				revenueCenter.setCurrentValue(cursor.getInt(12));
				revenueCenter.setIsKiosk(cursor.getInt(13));
				revenueCenter.setCurrentReportNo(cursor.getInt(14));
				result.add(revenueCenter);
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
	
	public static RevenueCenter getRevenueCenterById(int id) {
		RevenueCenter revenueCenter = null;
		String sql = "select * from " + TableNames.RevenueCenter + " where id = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {id + ""});
			if (cursor.moveToFirst()) {
				revenueCenter = new RevenueCenter();
				revenueCenter.setId(cursor.getInt(0));
				revenueCenter.setRestaurantId(cursor.getInt(1));
				revenueCenter.setPrintId(cursor.getInt(2));
				revenueCenter.setRevName(cursor.getString(3));
				revenueCenter.setIsActive(cursor.getInt(4));
				revenueCenter.setCreateTime(cursor.getString(5));
				revenueCenter.setUpdateTime(cursor.getString(6));
				revenueCenter.setHappyHourId(cursor.getInt(7));
				revenueCenter.setHappyStartTime(cursor.getLong(8));
				revenueCenter.setHappyEndTime(cursor.getLong(9));
				revenueCenter.setCurrentBillNo(cursor.getString(10) == null ? "0" : cursor.getString(10));
				revenueCenter.setIndexId(cursor.getInt(11));
				revenueCenter.setCurrentValue(cursor.getInt(12));
				revenueCenter.setIsKiosk(cursor.getInt(13));
				revenueCenter.setCurrentReportNo(cursor.getInt(14));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return revenueCenter;
	}
	
	public static void updateRevenueCenterCurrentValue(int currentValue, int id){
		String sql = "update " + TableNames.RevenueCenter + " set currentValue = ? where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { String.valueOf(currentValue), String.valueOf(id)});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getBillNoFromRevenueCenter(int id){
		int billNo = 0;
		int currentValue = 0;
		int indexId = 0;
		String sqlGet = "select currentValue, indexId from " + TableNames.RevenueCenter + " where id =?";
		String sqlUpdate = "update " + TableNames.RevenueCenter + " set currentValue = ? where id = ?";
		SQLiteDatabase db = SQLExe.getDB();
		Cursor cursor = null;
		try {
			db.beginTransaction();
			cursor = db.rawQuery(sqlGet, new String[] {String.valueOf(id)});
			int count = cursor.getCount();
			if (count < 1) {
				return billNo;
			}
			if (cursor.moveToNext()) {
				currentValue = cursor.getInt(0) + 1;
				indexId = cursor.getInt(1);
			}
			
			db.execSQL(sqlUpdate, new String[]{String.valueOf(currentValue), String.valueOf(id)});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			db.endTransaction();
		}
		String billNoStr = ParamHelper.getPrintOrderBillNo(indexId, currentValue);
		if(TextUtils.isEmpty(billNoStr)){
			return billNo;
		}
		billNo = Integer.valueOf(billNoStr);
		return billNo;
	}

	public static String getReportNoFromRevenueCenter(int id){
		String reportNo = "0";
		int currentReportNo = 0;
		int indexId = 0;
		String sqlGet = "select currentReportNo, indexId from " + TableNames.RevenueCenter + " where id =?";
		String sqlUpdate = "update " + TableNames.RevenueCenter + " set currentReportNo = ? where id = ?";
		SQLiteDatabase db = SQLExe.getDB();
		Cursor cursor = null;
		try {
			db.beginTransaction();
			cursor = db.rawQuery(sqlGet, new String[] {String.valueOf(id)});
			int count = cursor.getCount();
			if (count < 1) {
				return reportNo;
			}
			if (cursor.moveToNext()) {
				currentReportNo = cursor.getInt(0) + 1;
				indexId = cursor.getInt(1);
			}

			db.execSQL(sqlUpdate, new String[]{String.valueOf(currentReportNo), String.valueOf(id)});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			db.endTransaction();
		}
		String reportNoStr = ParamHelper.getPrintOrderBillNo(indexId, currentReportNo);
		if(TextUtils.isEmpty(reportNoStr)){
			return reportNo;
		}
		reportNo = reportNoStr;
		return reportNo;
	}

	public static void deleteRevenueCenter(RevenueCenter revenueCenter) {
		String sql = "delete from " + TableNames.RevenueCenter
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { revenueCenter.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllRevenueCenter() {
		String sql = "delete from " + TableNames.RevenueCenter;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
