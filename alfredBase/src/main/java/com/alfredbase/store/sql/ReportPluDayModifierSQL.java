package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportPluDayModifierSQL {
	public static void addReportPluDayModifier(
			ReportPluDayModifier reportPluDayModifier) {
		if (reportPluDayModifier == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.ReportPluDayModifier
					+ "(id, reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, "
					+ "modifierCategoryName, modifierId, modifierName, modifierPrice, modifierCount, billVoidPrice, billVoidCount, " 
					+ "voidModifierPrice, voidModifierCount, bohModifierPrice, bohModifierCount, focModifierPrice, focModifierCount, "
					+ "billFocPrice, billFocCount, comboItemId, modifierItemPrice, realPrice, realCount,daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { reportPluDayModifier.getId(),
							reportPluDayModifier.getReportNo(),
							reportPluDayModifier.getRestaurantId(),
							reportPluDayModifier.getRestaurantName(),
							reportPluDayModifier.getRevenueId(),
							reportPluDayModifier.getRevenueName(),
							reportPluDayModifier.getBusinessDate(),
							reportPluDayModifier.getModifierCategoryId(),
							reportPluDayModifier.getModifierCategoryName(),
							reportPluDayModifier.getModifierId(),
							reportPluDayModifier.getModifierName(),
							reportPluDayModifier.getModifierPrice() == null ? "0.00" : reportPluDayModifier.getModifierPrice(),
							reportPluDayModifier.getModifierCount() == null ? 0 : reportPluDayModifier.getModifierCount(),
							reportPluDayModifier.getBillVoidPrice() == null ? "0.00" : reportPluDayModifier.getBillVoidPrice(),
							reportPluDayModifier.getBillVoidCount() == null ? 0 : reportPluDayModifier.getBillVoidCount(),
							reportPluDayModifier.getVoidModifierPrice() == null ? "0.00" : reportPluDayModifier.getVoidModifierPrice(),
							reportPluDayModifier.getVoidModifierCount() == null ? 0 : reportPluDayModifier.getVoidModifierCount(),
							reportPluDayModifier.getBohModifierPrice() == null ? "0.00" : reportPluDayModifier.getBohModifierPrice(),
							reportPluDayModifier.getBohModifierCount() == null ? 0 : reportPluDayModifier.getBohModifierCount(),
							reportPluDayModifier.getFocModifierPrice() == null ? "0.00" : reportPluDayModifier.getFocModifierPrice(),
							reportPluDayModifier.getFocModifierCount() == null ? 0 : reportPluDayModifier.getFocModifierCount(),
							reportPluDayModifier.getBillFocPrice() == null ? "0.00" : reportPluDayModifier.getBillFocPrice(),
							reportPluDayModifier.getBillFocCount() == null ? 0 : reportPluDayModifier.getBillFocCount(),
							reportPluDayModifier.getComboItemId(),
							reportPluDayModifier.getModifierItemPrice() == null ? "0.00" : reportPluDayModifier.getModifierItemPrice(),
							reportPluDayModifier.getRealPrice() == null ? "0.00" : reportPluDayModifier.getRealPrice(),
							reportPluDayModifier.getRealCount() == null ? 0 : reportPluDayModifier.getRealCount(),
							reportPluDayModifier.getDaySalesId()
							});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addReportPluDayModifierList(
			List<ReportPluDayModifier> reportPluDayModifiers) {
		if (reportPluDayModifiers != null && reportPluDayModifiers.size() > 0) {
			SQLiteDatabase db = SQLExe.getDB();
			try {
				db.beginTransaction();
				String sql = "replace into "
						+ TableNames.ReportPluDayModifier
						+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, "
						+ "modifierCategoryName, modifierId, modifierName, modifierPrice, modifierCount, billVoidPrice, billVoidCount, "
						+ "voidModifierPrice, voidModifierCount, bohModifierPrice, bohModifierCount, focModifierPrice, focModifierCount, "
						+ "billFocPrice, billFocCount, comboItemId, modifierItemPrice, realPrice, realCount, daySalesId)"
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				SQLiteStatement sqLiteStatement = db.compileStatement(
						sql);
				for (ReportPluDayModifier reportPluDayModifier : reportPluDayModifiers) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							reportPluDayModifier.getReportNo());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							reportPluDayModifier.getRestaurantId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 3,
							reportPluDayModifier.getRestaurantName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							reportPluDayModifier.getRevenueId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							reportPluDayModifier.getRevenueName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							reportPluDayModifier.getBusinessDate());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
							reportPluDayModifier.getModifierCategoryId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 8,
							reportPluDayModifier.getModifierCategoryName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							reportPluDayModifier.getModifierId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 10,
							reportPluDayModifier.getModifierName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 11,
							reportPluDayModifier.getModifierPrice() == null ? "0.00" : reportPluDayModifier.getModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							reportPluDayModifier.getModifierCount() == null ? 0 : reportPluDayModifier.getModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 13,
							reportPluDayModifier.getBillVoidPrice() == null ? "0.00" : reportPluDayModifier.getBillVoidPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
							reportPluDayModifier.getBillVoidCount() == null ? 0 : reportPluDayModifier.getBillVoidCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 15,
							reportPluDayModifier.getVoidModifierPrice() == null ? "0.00" : reportPluDayModifier.getVoidModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
							reportPluDayModifier.getVoidModifierCount() == null ? 0 : reportPluDayModifier.getVoidModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 17,
							reportPluDayModifier.getBohModifierPrice() == null ? "0.00" : reportPluDayModifier.getBohModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 18,
							reportPluDayModifier.getBohModifierCount() == null ? 0 : reportPluDayModifier.getBohModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 19,
							reportPluDayModifier.getFocModifierPrice() == null ? "0.00" : reportPluDayModifier.getFocModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
							reportPluDayModifier.getFocModifierCount() == null ? 0 : reportPluDayModifier.getFocModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 21,
							reportPluDayModifier.getBillFocPrice() == null ? "0.00" : reportPluDayModifier.getBillFocPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
							reportPluDayModifier.getBillFocCount() == null ? 0 : reportPluDayModifier.getBillFocCount());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
							reportPluDayModifier.getComboItemId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 24,
							reportPluDayModifier.getModifierItemPrice() == null ? "0.00" : reportPluDayModifier.getModifierItemPrice());
					SQLiteStatementHelper.bindString(sqLiteStatement, 25,
							reportPluDayModifier.getRealPrice() == null ? "0.00" : reportPluDayModifier.getRealPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 26,
							reportPluDayModifier.getRealCount() == null ? 0 : reportPluDayModifier.getRealCount());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 27,
							reportPluDayModifier.getDaySalesId());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
		}
	}
	public static void addReportPluDayModifierList(int daySalesId, SQLiteDatabase db,
			List<ReportPluDayModifier> reportPluDayModifiers) {
		if (reportPluDayModifiers != null && reportPluDayModifiers.size() > 0) {
			try {
				String sql = "replace into "
						+ TableNames.ReportPluDayModifier
						+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, "
						+ "modifierCategoryName, modifierId, modifierName, modifierPrice, modifierCount, billVoidPrice, billVoidCount, "
						+ "voidModifierPrice, voidModifierCount, bohModifierPrice, bohModifierCount, focModifierPrice, focModifierCount, "
						+ "billFocPrice, billFocCount, comboItemId, modifierItemPrice, realPrice, realCount, daySalesId)"
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				SQLiteStatement sqLiteStatement = db.compileStatement(
						sql);
				for (ReportPluDayModifier reportPluDayModifier : reportPluDayModifiers) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							reportPluDayModifier.getReportNo());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							reportPluDayModifier.getRestaurantId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 3,
							reportPluDayModifier.getRestaurantName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							reportPluDayModifier.getRevenueId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							reportPluDayModifier.getRevenueName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							reportPluDayModifier.getBusinessDate());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
							reportPluDayModifier.getModifierCategoryId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 8,
							reportPluDayModifier.getModifierCategoryName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							reportPluDayModifier.getModifierId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 10,
							reportPluDayModifier.getModifierName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 11,
							reportPluDayModifier.getModifierPrice() == null ? "0.00" : reportPluDayModifier.getModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							reportPluDayModifier.getModifierCount() == null ? 0 : reportPluDayModifier.getModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 13,
							reportPluDayModifier.getBillVoidPrice() == null ? "0.00" : reportPluDayModifier.getBillVoidPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
							reportPluDayModifier.getBillVoidCount() == null ? 0 : reportPluDayModifier.getBillVoidCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 15,
							reportPluDayModifier.getVoidModifierPrice() == null ? "0.00" : reportPluDayModifier.getVoidModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
							reportPluDayModifier.getVoidModifierCount() == null ? 0 : reportPluDayModifier.getVoidModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 17,
							reportPluDayModifier.getBohModifierPrice() == null ? "0.00" : reportPluDayModifier.getBohModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 18,
							reportPluDayModifier.getBohModifierCount() == null ? 0 : reportPluDayModifier.getBohModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 19,
							reportPluDayModifier.getFocModifierPrice() == null ? "0.00" : reportPluDayModifier.getFocModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
							reportPluDayModifier.getFocModifierCount() == null ? 0 : reportPluDayModifier.getFocModifierCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 21,
							reportPluDayModifier.getBillFocPrice() == null ? "0.00" : reportPluDayModifier.getBillFocPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
							reportPluDayModifier.getBillFocCount() == null ? 0 : reportPluDayModifier.getBillFocCount());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
							reportPluDayModifier.getComboItemId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 24,
							reportPluDayModifier.getModifierItemPrice() == null ? "0.00" : reportPluDayModifier.getModifierItemPrice());
					SQLiteStatementHelper.bindString(sqLiteStatement, 25,
							reportPluDayModifier.getRealPrice() == null ? "0.00" : reportPluDayModifier.getRealPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 26,
							reportPluDayModifier.getRealCount() == null ? 0 : reportPluDayModifier.getRealCount());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 27, daySalesId);
					sqLiteStatement.executeInsert();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static ReportPluDayModifier getReportPluDayModifier(
			Integer reportPluDayModifierID) {
		ReportPluDayModifier reportPluDayModifier = null;
		String sql = "select * from " + TableNames.ReportPluDayModifier
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { reportPluDayModifierID + "" });
			if (cursor.moveToFirst()) {
				reportPluDayModifier = new ReportPluDayModifier();
				reportPluDayModifier.setId(cursor.getInt(0));
				reportPluDayModifier.setReportNo(cursor.getInt(1));
				reportPluDayModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayModifier.setRestaurantName(cursor.getString(3));
				reportPluDayModifier.setRevenueId(cursor.getInt(4));
				reportPluDayModifier.setRevenueName(cursor.getString(5));
				reportPluDayModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayModifier.setModifierId(cursor.getInt(9));
				reportPluDayModifier.setModifierName(cursor.getString(10));
				reportPluDayModifier.setModifierPrice(cursor.getString(11));
				reportPluDayModifier.setModifierCount(cursor.getInt(12));
				reportPluDayModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayModifier.setBillVoidCount(cursor.getInt(14)); 
				reportPluDayModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayModifier.setComboItemId(cursor.getInt(23));
				reportPluDayModifier.setModifierItemPrice(cursor.getString(24));
				reportPluDayModifier.setRealPrice(cursor.getString(25));
				reportPluDayModifier.setRealCount(cursor.getInt(26));
				reportPluDayModifier.setDaySalesId(cursor.getInt(27));
				return reportPluDayModifier;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return reportPluDayModifier;
	}

	public static ArrayList<ReportPluDayModifier> getAllReportPluDayModifier() {
		ArrayList<ReportPluDayModifier> result = new ArrayList<ReportPluDayModifier>();
		String sql = "select * from " + TableNames.ReportPluDayModifier;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayModifier reportPluDayModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayModifier = new ReportPluDayModifier();
				reportPluDayModifier.setId(cursor.getInt(0));
				reportPluDayModifier.setReportNo(cursor.getInt(1));
				reportPluDayModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayModifier.setRestaurantName(cursor.getString(3));
				reportPluDayModifier.setRevenueId(cursor.getInt(4));
				reportPluDayModifier.setRevenueName(cursor.getString(5));
				reportPluDayModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayModifier.setModifierId(cursor.getInt(9));
				reportPluDayModifier.setModifierName(cursor.getString(10));
				reportPluDayModifier.setModifierPrice(cursor.getString(11));
				reportPluDayModifier.setModifierCount(cursor.getInt(12));
				reportPluDayModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayModifier.setBillVoidCount(cursor.getInt(14)); 
				reportPluDayModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayModifier.setComboItemId(cursor.getInt(23));
				reportPluDayModifier.setModifierItemPrice(cursor.getString(24));
				reportPluDayModifier.setRealPrice(cursor.getString(25));
				reportPluDayModifier.setRealCount(cursor.getInt(26));
				reportPluDayModifier.setDaySalesId(cursor.getInt(27));
				result.add(reportPluDayModifier);
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
	public static ArrayList<ReportPluDayModifier> getAllReportPluDayModifierByDaySalesId(int daySalesId) {
		ArrayList<ReportPluDayModifier> result = new ArrayList<>();
		String sql = "select * from " + TableNames.ReportPluDayModifier + " where daySalesId = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {daySalesId+""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayModifier reportPluDayModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayModifier = new ReportPluDayModifier();
				reportPluDayModifier.setId(cursor.getInt(0));
				reportPluDayModifier.setReportNo(cursor.getInt(1));
				reportPluDayModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayModifier.setRestaurantName(cursor.getString(3));
				reportPluDayModifier.setRevenueId(cursor.getInt(4));
				reportPluDayModifier.setRevenueName(cursor.getString(5));
				reportPluDayModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayModifier.setModifierId(cursor.getInt(9));
				reportPluDayModifier.setModifierName(cursor.getString(10));
				reportPluDayModifier.setModifierPrice(cursor.getString(11));
				reportPluDayModifier.setModifierCount(cursor.getInt(12));
				reportPluDayModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayModifier.setBillVoidCount(cursor.getInt(14));
				reportPluDayModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayModifier.setComboItemId(cursor.getInt(23));
				reportPluDayModifier.setModifierItemPrice(cursor.getString(24));
				reportPluDayModifier.setRealPrice(cursor.getString(25));
				reportPluDayModifier.setRealCount(cursor.getInt(26));
				reportPluDayModifier.setDaySalesId(cursor.getInt(27));
				result.add(reportPluDayModifier);
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

	public static ArrayList<ReportPluDayModifier> getReportPluDayModifiersByTime(long date) {
		ArrayList<ReportPluDayModifier> result = new ArrayList<ReportPluDayModifier>();
		String sql = "select * from "
				+ TableNames.ReportPluDayModifier
				+ " where businessDate = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(date)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayModifier reportPluDayModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayModifier = new ReportPluDayModifier();
				reportPluDayModifier.setId(cursor.getInt(0));
				reportPluDayModifier.setReportNo(cursor.getInt(1));
				reportPluDayModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayModifier.setRestaurantName(cursor.getString(3));
				reportPluDayModifier.setRevenueId(cursor.getInt(4));
				reportPluDayModifier.setRevenueName(cursor.getString(5));
				reportPluDayModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayModifier.setModifierId(cursor.getInt(9));
				reportPluDayModifier.setModifierName(cursor.getString(10));
				reportPluDayModifier.setModifierPrice(cursor.getString(11));
				reportPluDayModifier.setModifierCount(cursor.getInt(12));
				reportPluDayModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayModifier.setBillVoidCount(cursor.getInt(14)); 
				reportPluDayModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayModifier.setComboItemId(cursor.getInt(23));
				reportPluDayModifier.setModifierItemPrice(cursor.getString(24));
				reportPluDayModifier.setRealPrice(cursor.getString(25));
				reportPluDayModifier.setRealCount(cursor.getInt(26));
				reportPluDayModifier.setDaySalesId(cursor.getInt(27));
				result.add(reportPluDayModifier);
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

	public static ArrayList<ReportPluDayModifier> getReportPluDayModifiersForZReport(long date) {
		ArrayList<ReportPluDayModifier> result = new ArrayList<ReportPluDayModifier>();
		String sql = "select restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, modifierCategoryName, modifierId, modifierName, "
				+ " sum(modifierPrice), sum(modifierCount), sum(billVoidPrice), sum(billVoidCount), sum(voidModifierPrice), sum(voidModifierCount), "
				+ " sum(bohModifierPrice), sum(bohModifierCount), sum(focModifierPrice), sum(focModifierCount),"
				+ " sum(billFocPrice), sum(billFocCount), comboItemId, modifierItemPrice, sum(realPrice), sum(realCount) from "
				+ TableNames.ReportPluDayModifier
				+ " where businessDate = ? group by modifierId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(date)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayModifier reportPluDayModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayModifier = new ReportPluDayModifier();
				reportPluDayModifier.setRestaurantId(cursor.getInt(0));
				reportPluDayModifier.setRestaurantName(cursor.getString(1));
				reportPluDayModifier.setRevenueId(cursor.getInt(2));
				reportPluDayModifier.setRevenueName(cursor.getString(3));
				reportPluDayModifier.setBusinessDate(cursor.getLong(4));
				reportPluDayModifier.setModifierCategoryId(cursor.getInt(5));
				reportPluDayModifier.setModifierCategoryName(cursor
						.getString(6));
				reportPluDayModifier.setModifierId(cursor.getInt(7));
				reportPluDayModifier.setModifierName(cursor.getString(8));
				reportPluDayModifier.setModifierPrice(BH.getBD(cursor.getString(9)).toString());
				reportPluDayModifier.setModifierCount(cursor.getInt(10));
				reportPluDayModifier.setBillVoidPrice(BH.getBD(cursor.getString(11)).toString());
				reportPluDayModifier.setBillVoidCount(cursor.getInt(12));
				reportPluDayModifier.setVoidModifierPrice(BH.getBD(cursor.getString(13)).toString());
				reportPluDayModifier.setVoidModifierCount(cursor.getInt(14));
				reportPluDayModifier.setBohModifierPrice(BH.getBD(cursor.getString(15)).toString());
				reportPluDayModifier.setBohModifierCount(cursor.getInt(16));
				reportPluDayModifier.setFocModifierPrice(BH.getBD(cursor.getString(17)).toString());
				reportPluDayModifier.setFocModifierCount(cursor.getInt(18));
				reportPluDayModifier.setBillFocPrice(BH.getBD(cursor.getString(19)).toString());
				reportPluDayModifier.setBillFocCount(cursor.getInt(20));
				reportPluDayModifier.setComboItemId(cursor.getInt(21));
				reportPluDayModifier.setModifierItemPrice(BH.getBD(cursor.getString(22)).toString());
				reportPluDayModifier.setRealPrice(BH.getBD(cursor.getString(23)).toString());
				reportPluDayModifier.setRealCount(cursor.getInt(24));
				result.add(reportPluDayModifier);
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

	public static void deleteReportPluDayModifier(
			ReportPluDayModifier reportPluDayModifier) {
		String sql = "delete from " + TableNames.ReportPluDayModifier
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { reportPluDayModifier.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteReportPluDayModifierByBusinessDate(long businessDate) {
		String sql = "delete from " + TableNames.ReportPluDayModifier
				+ " where businessDate = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { businessDate + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
