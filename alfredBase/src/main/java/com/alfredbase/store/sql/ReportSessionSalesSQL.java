package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.model.ReportSessionSales;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import java.util.ArrayList;
import java.util.List;

public class ReportSessionSalesSQL {
	public static void addReportSessionSales(ReportSessionSales reportSessionSales) {
		if (reportSessionSales == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.ReportSessionSales
					+ "(id, sessionName, startDrawer, cash, cashTopup, expectedAmount, actualAmount, difference, businessDate)"
					+ " values (?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] {
							reportSessionSales.getId(),
							reportSessionSales.getSessionName(),
							reportSessionSales.getStartDrawer() == null ? "0.00" : reportSessionSales.getStartDrawer(),
							reportSessionSales.getCash() == null ? "0.00" : reportSessionSales.getCash(),
							reportSessionSales.getCashTopup() == null ? "0.00" : reportSessionSales.getCashTopup(),
							reportSessionSales.getExpectedAmount() == null ? "0.00" : reportSessionSales.getExpectedAmount(),
							reportSessionSales.getActualAmount() == null ? "0.00" : reportSessionSales.getActualAmount(),
							reportSessionSales.getDifference() == null ? "0.00" : reportSessionSales.getDifference(),
							reportSessionSales.getBusinessDate()
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static List<ReportSessionSales> getAllReportSessionSales(long businessDate) {
		List<ReportSessionSales> result = new ArrayList<ReportSessionSales>();
		String sql = "select * from " + TableNames.ReportSessionSales + " where businessDate = ? group by id";
		Cursor cursor = null;
		SQLiteDatabase db =SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {businessDate + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ReportSessionSales reportSessionSales = new ReportSessionSales();
				reportSessionSales.setId(cursor.getInt(0));
				reportSessionSales.setSessionName(cursor.getString(1));
				reportSessionSales.setStartDrawer(cursor.getString(2));
				reportSessionSales.setCash(cursor.getString(3));
				reportSessionSales.setCashTopup(cursor.getString(4));
				reportSessionSales.setExpectedAmount(cursor.getString(5));
				reportSessionSales.setActualAmount(cursor.getString(6));
				reportSessionSales.setDifference(cursor.getString(7));
				reportSessionSales.setBusinessDate(cursor.getLong(8));
				result.add(reportSessionSales);
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


	public static String getSumStartDrawer(long businessDate){
		String sumStartDrawer = "0.00";
		String sql = "select SUM(startDrawer) from "
				+ TableNames.ReportSessionSales
				+ " where businessDate = ?";
		Cursor cursor = null;
		try{
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[]{businessDate + ""});
			if(cursor.moveToFirst()){
				sumStartDrawer = cursor.getString(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return sumStartDrawer;
	}
	public static String getSumExpected(long businessDate){
		String expectedAmount = "0.00";
		String sql = "select SUM(expectedAmount) from "
				+ TableNames.ReportSessionSales
				+ " where businessDate = ? ";
		Cursor cursor = null;
		try{
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[]{businessDate + ""});
			if(cursor.moveToFirst()){
				expectedAmount = cursor.getString(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return expectedAmount;
	}
	public static String getSumActual(long businessDate){
		String waiterAmount = "0.00";
		String sql = "select SUM(actualAmount) from "
				+ TableNames.ReportSessionSales
				+ " where businessDate = ?";
		Cursor cursor = null;
		try{
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[]{businessDate + ""});
			if(cursor.moveToFirst()){
				waiterAmount = cursor.getString(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return waiterAmount;
	}
	public static String getSumDifference(long businessDate){
		String difference = "0.00";
		String sql = "select SUM(difference) from "
				+ TableNames.ReportSessionSales
				+ " where businessDate = ?";
		Cursor cursor = null;
		try{
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[]{businessDate + ""});
			if(cursor.moveToFirst()){
				difference = cursor.getString(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return difference;
	}


	public static void deleteReportSessionSales(ReportSessionSales reportSessionSales) {
		String sql = "delete from " + TableNames.ReportSessionSales
				+ " where id = ?";
		try {
			SQLExe.getDB()
					.execSQL(sql, new Object[] { reportSessionSales.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
