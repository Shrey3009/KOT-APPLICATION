package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportHourlySQL {
	public static void addReportHourly(ReportHourly reportHourly){
		if(reportHourly == null){
			return;
		}
		try {
			
			String sql = "replace into " 
					+ TableNames.ReportHourly 
					+ "(id, restaurantId, revenueId, revenueName, businessDate, hour,amountQty, amountPrice, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(sql, new Object[]{
					reportHourly.getId(),
					reportHourly.getRestaurantId(),
					reportHourly.getRevenueId(),
					reportHourly.getRevenueName(),
					reportHourly.getBusinessDate(),
					reportHourly.getHour(),
					reportHourly.getAmountQty(),
					reportHourly.getAmountPrice(),
					reportHourly.getDaySalesId()
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//save Hourly Report from Cloud
	public static void addReportHourlyFromCloud(List<ReportHourly> reportHourlys){
		if(reportHourlys == null){
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into " 
					+ TableNames.ReportHourly 
					+ "(restaurantId, revenueId, revenueName, businessDate, hour,amountQty, amountPrice, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportHourly reportHourly: reportHourlys) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1,reportHourly.getRestaurantId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2,reportHourly.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3,reportHourly.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4,reportHourly.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 5,reportHourly.getHour());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6,reportHourly.getAmountQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 7,reportHourly.getAmountPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 8, reportHourly.getDaySalesId());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static void addReportHourly(List<ReportHourly> reportHourlys){
		if(reportHourlys == null){
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.ReportHourly
					+ "(restaurantId, revenueId, revenueName, businessDate, hour,amountQty, amountPrice, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportHourly reportHourly: reportHourlys) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1,reportHourly.getRestaurantId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2,reportHourly.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3,reportHourly.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4,reportHourly.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 5,reportHourly.getHour());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6,reportHourly.getAmountQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 7,reportHourly.getAmountPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 8, reportHourly.getDaySalesId());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	public static void addReportHourly(int daySalesId, SQLiteDatabase db, List<ReportHourly> reportHourlys){
		if(reportHourlys == null){
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.ReportHourly
					+ "(restaurantId, revenueId, revenueName, businessDate, hour,amountQty, amountPrice, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportHourly reportHourly: reportHourlys) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1,reportHourly.getRestaurantId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2,reportHourly.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3,reportHourly.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4,reportHourly.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 5,reportHourly.getHour());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6,reportHourly.getAmountQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 7,reportHourly.getAmountPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 8, daySalesId);
				sqLiteStatement.executeInsert();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<ReportHourly> getAllReportHourlys(){
		ArrayList<ReportHourly> reportHourlys = new ArrayList<ReportHourly>();
		String sql = "select * from " + TableNames.ReportHourly;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[]{});
			int count = cursor.getCount();
			if(count < 1){
				return reportHourlys;
			}
			ReportHourly reportHourly = null;
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
				reportHourly = new ReportHourly();
				reportHourly.setId(cursor.getInt(0));
				reportHourly.setRestaurantId(cursor.getInt(1));
				reportHourly.setRevenueId(cursor.getInt(2));
				reportHourly.setRevenueName(cursor.getString(3));
				reportHourly.setBusinessDate(cursor.getLong(4));
				reportHourly.setHour(cursor.getInt(5));
				reportHourly.setAmountQty(cursor.getInt(6));
				reportHourly.setAmountPrice(cursor.getString(7));
				reportHourly.setDaySalesId(cursor.getInt(8));
				reportHourlys.add(reportHourly);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return reportHourlys;
	}
	public static ArrayList<ReportHourly> getAllReportHourlysByDaySalesId(int daySalesId){
		ArrayList<ReportHourly> reportHourlys = new ArrayList<>();
		String sql = "select * from " + TableNames.ReportHourly + " where daySalesId= ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[]{daySalesId+""});
			int count = cursor.getCount();
			if(count < 1){
				return reportHourlys;
			}
			ReportHourly reportHourly = null;
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
				reportHourly = new ReportHourly();
				reportHourly.setId(cursor.getInt(0));
				reportHourly.setRestaurantId(cursor.getInt(1));
				reportHourly.setRevenueId(cursor.getInt(2));
				reportHourly.setRevenueName(cursor.getString(3));
				reportHourly.setBusinessDate(cursor.getLong(4));
				reportHourly.setHour(cursor.getInt(5));
				reportHourly.setAmountQty(cursor.getInt(6));
				reportHourly.setAmountPrice(cursor.getString(7));
				reportHourly.setDaySalesId(cursor.getInt(8));
				reportHourlys.add(reportHourly);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return reportHourlys;
	}

	public static ArrayList<ReportHourly> getReportHourlysByTime(long businessDate){
		ArrayList<ReportHourly> reportHourlys = new ArrayList<ReportHourly>();
		String sql = "select * from " + TableNames.ReportHourly + " where businessDate = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[]{String.valueOf(businessDate)});
			int count = cursor.getCount();
			if(count < 1){
				return reportHourlys;
			}
			ReportHourly reportHourly = null;
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
				reportHourly = new ReportHourly();
				reportHourly.setId(cursor.getInt(0));
				reportHourly.setRestaurantId(cursor.getInt(1));
				reportHourly.setRevenueId(cursor.getInt(2));
				reportHourly.setRevenueName(cursor.getString(3));
				reportHourly.setBusinessDate(cursor.getLong(4));
				reportHourly.setHour(cursor.getInt(5));
				reportHourly.setAmountQty(cursor.getInt(6));
				reportHourly.setAmountPrice(BH.getBD(cursor.getString(7)).toString());
				reportHourly.setDaySalesId(cursor.getInt(8));
				reportHourlys.add(reportHourly);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return reportHourlys;
	}
	
	public static void deleteReportHourlyByBusinessDate(long businessDate) {
		String sql = "delete from " + TableNames.ReportHourly
				+ " where businessDate = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { businessDate + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
