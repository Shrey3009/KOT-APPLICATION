package com.alfredbase.store.sql;

import android.database.Cursor;

import com.alfredbase.javabean.ReportDiscount;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

public class ReportDiscountSQL {
	public static void update(ReportDiscount reportDiscount){
		if (reportDiscount == null) {
			return;
		}
		try {
			String sql = "replace into " 
					+ TableNames.ReportDiscount
					+ " (id, restaurantId, revenueId, userId, orderId, revenueName, businessDate, billNumber, tableId, tableName, actuallAmount, discount, grandTotal)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(sql,new Object[]{
					reportDiscount.getId(),
					reportDiscount.getRestaurantId(),
					reportDiscount.getRevenueId(),
					reportDiscount.getUserId(),
					reportDiscount.getOrderId(),
					reportDiscount.getRevenueName(),
					reportDiscount.getBusinessDate(),
					reportDiscount.getBillNumber(),
					reportDiscount.getTableId(),
					reportDiscount.getTableName(),
					reportDiscount.getActuallAmount(),
					reportDiscount.getDiscount(),
					reportDiscount.getGrandTotal()
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ReportDiscount getReportDiscountByOrderId(int orderId){
		ReportDiscount reportDiscount = null;
		String sql = "select * from " + TableNames.ReportDiscount
				+ " where orderId = ? ";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[]{orderId + ""});
			if (cursor.moveToFirst()) {
				reportDiscount = new ReportDiscount();
				reportDiscount.setId(cursor.getInt(0));
				reportDiscount.setRestaurantId(cursor.getInt(1));
				reportDiscount.setRevenueId(cursor.getInt(2));
				reportDiscount.setUserId(cursor.getInt(3));
				reportDiscount.setOrderId(cursor.getInt(4));
				reportDiscount.setRevenueName(cursor.getString(5));
				reportDiscount.setBusinessDate(cursor.getLong(6));
				reportDiscount.setBillNumber(cursor.getInt(7));
				reportDiscount.setTableId(cursor.getInt(8));
				reportDiscount.setTableName(cursor.getString(9));
				reportDiscount.setActuallAmount(cursor.getString(10));
				reportDiscount.setDiscount(cursor.getString(11));
				reportDiscount.setGrandTotal(cursor.getString(12));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return reportDiscount;
	}
	
	public static void deleteReportDiscount(ReportDiscount reportDiscount){
		String sql = "delete from " + TableNames.ReportDiscount
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[]{reportDiscount.getId()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
