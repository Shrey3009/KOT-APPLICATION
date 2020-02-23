package com.alfredbase.store.sql;

import java.util.ArrayList;

import android.database.Cursor;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.UserTimeSheet;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

public class UserTimeSheetSQL {
	public static void addUser(UserTimeSheet userTimeSheet) {
		if (userTimeSheet == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.UserTimeSheet
					+ "(id, businessDate, restaurantId, revenueId, userId, empId, empName, loginTime, logoutTime, totalHours, status)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { userTimeSheet.getId(),
							userTimeSheet.getBusinessDate(),
							userTimeSheet.getRestaurantId(),
							userTimeSheet.getRevenueId(),
							userTimeSheet.getUserId(),
							userTimeSheet.getEmpId(),
							userTimeSheet.getEmpName(),
							userTimeSheet.getLoginTime(),
							userTimeSheet.getLogoutTime(),
							userTimeSheet.getTotalHours(),
							userTimeSheet.getStatus()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static UserTimeSheet getUserTimeSheetByEmpId(int empId, long businessDate){

		UserTimeSheet userTimeSheet = null;
		String sql = "select * from " + TableNames.UserTimeSheet + " where empId = ? and businessDate = ? and status = " + ParamConst.USERTIMESHEET_STATUS_LOGIN;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[] {String.valueOf(empId), String.valueOf(businessDate)});
			int count = cursor.getCount();
			if (count < 1) {
				return userTimeSheet;
			}
			if (cursor.moveToFirst()) {
				userTimeSheet = new UserTimeSheet();
				userTimeSheet.setId(cursor.getInt(0));
				userTimeSheet.setBusinessDate(cursor.getLong(1));
				userTimeSheet.setRestaurantId(cursor.getInt(2));
				userTimeSheet.setRevenueId(cursor.getInt(3));
				userTimeSheet.setUserId(cursor.getInt(4));
				userTimeSheet.setEmpId(cursor.getInt(5));
				userTimeSheet.setEmpName(cursor.getString(6));
				userTimeSheet.setLoginTime(cursor.getLong(7));
				userTimeSheet.setLogoutTime(cursor.getLong(8));
				userTimeSheet.setTotalHours(cursor.getDouble(9));
				userTimeSheet.setStatus(cursor.getInt(10));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return userTimeSheet;
	
	}
	
	public static ArrayList<UserTimeSheet> getUserTimeSheetsByEmpId(int empId, long businessDate){
		ArrayList<UserTimeSheet> result = new ArrayList<UserTimeSheet>();
		UserTimeSheet userTimeSheet = null;
		String sql = "select * from " + TableNames.UserTimeSheet + " where empId = ? and businessDate = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[] {String.valueOf(empId), String.valueOf(businessDate)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				userTimeSheet = new UserTimeSheet();
				userTimeSheet.setId(cursor.getInt(0));
				userTimeSheet.setBusinessDate(cursor.getLong(1));
				userTimeSheet.setRestaurantId(cursor.getInt(2));
				userTimeSheet.setRevenueId(cursor.getInt(3));
				userTimeSheet.setUserId(cursor.getInt(4));
				userTimeSheet.setEmpId(cursor.getInt(5));
				userTimeSheet.setEmpName(cursor.getString(6));
				userTimeSheet.setLoginTime(cursor.getLong(7));
				userTimeSheet.setLogoutTime(cursor.getLong(8));
				userTimeSheet.setTotalHours(cursor.getDouble(9));
				userTimeSheet.setStatus(cursor.getInt(10));
				result.add(userTimeSheet);
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
	
	public static ArrayList<UserTimeSheet> getUserTimeSheetsByBusinessDate(long businessDate){
		ArrayList<UserTimeSheet> result = new ArrayList<UserTimeSheet>();
		UserTimeSheet userTimeSheet = null;
		String sql = "select * from " + TableNames.UserTimeSheet + " where businessDate = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[]{String.valueOf(businessDate)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				userTimeSheet = new UserTimeSheet();
				userTimeSheet.setId(cursor.getInt(0));
				userTimeSheet.setBusinessDate(cursor.getLong(1));
				userTimeSheet.setRestaurantId(cursor.getInt(2));
				userTimeSheet.setRevenueId(cursor.getInt(3));
				userTimeSheet.setUserId(cursor.getInt(4));
				userTimeSheet.setEmpId(cursor.getInt(5));
				userTimeSheet.setEmpName(cursor.getString(6));
				userTimeSheet.setLoginTime(cursor.getLong(7));
				userTimeSheet.setLogoutTime(cursor.getLong(8));
				userTimeSheet.setTotalHours(cursor.getDouble(9));
				userTimeSheet.setStatus(cursor.getInt(10));
				result.add(userTimeSheet);
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
