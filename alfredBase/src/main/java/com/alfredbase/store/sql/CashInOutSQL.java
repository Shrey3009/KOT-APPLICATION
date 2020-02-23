package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.CashInOut;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;

public class CashInOutSQL {

	public static void update(CashInOut cashInOut) {
		if (cashInOut == null) {
			return;
		}
		String sql = "replace into "
				+ TableNames.CashInOut
				+ " (id, restaurantId,revenueId,userId,empId,empName,businessDate,type,comment,cash,createTime)"
				+ "values (?,?,?,?,?,?,?,?,?,?,?)";
		try {
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { cashInOut.getId(),
							cashInOut.getRestaurantId(),
							cashInOut.getRevenueId(), cashInOut.getUserId(),
							cashInOut.getEmpId(), cashInOut.getEmpName(),
							cashInOut.getBusinessDate(), cashInOut.getType(),
							cashInOut.getComment(), cashInOut.getCash(),
							cashInOut.getCreateTime() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCashInSUM(long businessDate) {
		String result = "0.00";
		String sql = "select SUM(cash) from " + TableNames.CashInOut
				+ " where businessDate = ? and type = "
				+ ParamConst.CASHINOUT_TYPE_IN;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { businessDate + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
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


	public static String getCashInSUMBySession(long businessDate, SessionStatus sessionStatus) {
		String result = "0.00";
		String sql = "select SUM(cash) from " + TableNames.CashInOut
				+ " where businessDate = ? and createTime > ? and type = "
				+ ParamConst.CASHINOUT_TYPE_IN;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { businessDate + "", sessionStatus.getTime() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
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
	public static String getStartDrawerSUMBySession(long businessDate, SessionStatus sessionStatus) {
		String result = "0.00";
		String sql = "select SUM(cash) from " + TableNames.CashInOut
				+ " where businessDate = ? and createTime > ? and type = "
				+ ParamConst.CASHINOUT_TYPE_START;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { businessDate + "", sessionStatus.getTime() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
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

	public static String getCashOutSUM(long businessDate) {
		String result = "0.00";
		String sql = "select SUM(cash) from " + TableNames.CashInOut
				+ " where businessDate = ? and type = "
				+ ParamConst.CASHINOUT_TYPE_OUT;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { businessDate + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
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


	public static String getCashOutSUMBySession(long businessDate, SessionStatus sessionStatus) {
		String result = "0.00";
		String sql = "select SUM(cash) from " + TableNames.CashInOut
				+ " where businessDate = ? and createTime > ? and type = "
				+ ParamConst.CASHINOUT_TYPE_OUT;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { businessDate + "", sessionStatus.getTime() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
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

	public static ArrayList<CashInOut> getAllCashInOut(long businessDate) {
		ArrayList<CashInOut> result = new ArrayList<CashInOut>();
		String sql = "select * from " + TableNames.CashInOut
				+ " where businessDate = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { businessDate + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			CashInOut cashInOut = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				cashInOut = new CashInOut();
				cashInOut.setId(cursor.getInt(0));
				cashInOut.setRestaurantId(cursor.getInt(1));
				cashInOut.setRevenueId(cursor.getInt(2));
				cashInOut.setUserId(cursor.getInt(3));
				cashInOut.setEmpId(cursor.getInt(4));
				cashInOut.setEmpName(cursor.getString(5));
				cashInOut.setBusinessDate(cursor.getLong(6));
				cashInOut.setType(cursor.getInt(7));
				cashInOut.setComment(cursor.getString(8));
				cashInOut.setCash(cursor.getString(9));
				cashInOut.setCreateTime(cursor.getString(10));
				result.add(cashInOut);
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
