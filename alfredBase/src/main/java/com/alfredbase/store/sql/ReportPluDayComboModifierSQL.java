package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportPluDayComboModifierSQL {
	public static void addReportPluDayModifier(
			ReportPluDayComboModifier reportPluDayComboModifier) {
		if (reportPluDayComboModifier == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.ReportPluDayComboModifier
					+ "(id, reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, "
					+ "modifierCategoryName, modifierId, modifierName, modifierPrice, modifierCount, billVoidPrice, billVoidCount, " 
					+ "voidModifierPrice, voidModifierCount, bohModifierPrice, bohModifierCount, focModifierPrice, focModifierCount, "
					+ "billFocPrice, billFocCount, comboItemId, itemId, itemName, modifierItemPrice, realPrice, realCount, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { reportPluDayComboModifier.getId(),
							reportPluDayComboModifier.getReportNo(),
							reportPluDayComboModifier.getRestaurantId(),
							reportPluDayComboModifier.getRestaurantName(),
							reportPluDayComboModifier.getRevenueId(),
							reportPluDayComboModifier.getRevenueName(),
							reportPluDayComboModifier.getBusinessDate(),
							reportPluDayComboModifier.getModifierCategoryId(),
							reportPluDayComboModifier.getModifierCategoryName(),
							reportPluDayComboModifier.getModifierId(),
							reportPluDayComboModifier.getModifierName(),
							reportPluDayComboModifier.getModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getModifierPrice(),
							reportPluDayComboModifier.getModifierCount() == null ? 0 : reportPluDayComboModifier.getModifierCount(),
							reportPluDayComboModifier.getBillVoidPrice() == null ? "0.00" : reportPluDayComboModifier.getBillVoidPrice(),
							reportPluDayComboModifier.getBillVoidCount() == null ? 0 : reportPluDayComboModifier.getBillVoidCount(),
							reportPluDayComboModifier.getVoidModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getVoidModifierPrice(),
							reportPluDayComboModifier.getVoidModifierCount() == null ? 0 : reportPluDayComboModifier.getVoidModifierCount(),
							reportPluDayComboModifier.getBohModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getBohModifierPrice(),
							reportPluDayComboModifier.getBohModifierCount() == null ? 0 : reportPluDayComboModifier.getBohModifierCount(),
							reportPluDayComboModifier.getFocModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getFocModifierPrice(),
							reportPluDayComboModifier.getFocModifierCount() == null ? 0 : reportPluDayComboModifier.getFocModifierCount(),
							reportPluDayComboModifier.getBillFocPrice() == null ? "0.00" : reportPluDayComboModifier.getBillFocPrice(),
							reportPluDayComboModifier.getBillFocCount() == null ? 0 : reportPluDayComboModifier.getBillFocCount(),
							reportPluDayComboModifier.getComboItemId(),
							reportPluDayComboModifier.getItemId(),
							reportPluDayComboModifier.getItemName(),
							reportPluDayComboModifier.getModifierItemPrice() == null ? "0.00" : reportPluDayComboModifier.getModifierItemPrice(),
							reportPluDayComboModifier.getRealPrice() == null ? "0.00" : reportPluDayComboModifier.getRealPrice(),
							reportPluDayComboModifier.getRealCount() == null ? 0 : reportPluDayComboModifier.getRealCount(),
							reportPluDayComboModifier.getDaySalesId()
							});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addReportPluDayModifierList(List<ReportPluDayComboModifier> reportPluDayComboModifiers) {
		if (reportPluDayComboModifiers == null)
			return;
		SQLiteDatabase db = SQLExe.getDB();
		try {

			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.ReportPluDayComboModifier
					+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, "
					+ "modifierCategoryName, modifierId, modifierName, modifierPrice, modifierCount, billVoidPrice, billVoidCount, "
					+ "voidModifierPrice, voidModifierCount, bohModifierPrice, bohModifierCount, focModifierPrice, focModifierCount, "
					+ "billFocPrice, billFocCount, comboItemId, itemId, itemName, modifierItemPrice, realPrice, realCount, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportPluDayComboModifier reportPluDayComboModifier : reportPluDayComboModifiers) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportPluDayComboModifier.getReportNo());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportPluDayComboModifier.getRestaurantId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportPluDayComboModifier.getRestaurantName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportPluDayComboModifier.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 5, reportPluDayComboModifier.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6, reportPluDayComboModifier.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 7, reportPluDayComboModifier.getModifierCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 8, reportPluDayComboModifier.getModifierCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportPluDayComboModifier.getModifierId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 10, reportPluDayComboModifier.getModifierName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 11, reportPluDayComboModifier.getModifierPrice() == null ? "0.00":reportPluDayComboModifier.getModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 12, reportPluDayComboModifier.getModifierCount() == null ? 0 : reportPluDayComboModifier.getModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 13, reportPluDayComboModifier.getBillVoidPrice() == null ? "0.00" : reportPluDayComboModifier.getBillVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 14, reportPluDayComboModifier.getBillVoidCount() == null ? 0 : reportPluDayComboModifier.getBillVoidCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 15, reportPluDayComboModifier.getVoidModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getVoidModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 16, reportPluDayComboModifier.getVoidModifierCount() == null ? 0 : reportPluDayComboModifier.getVoidModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 17, reportPluDayComboModifier.getBohModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getBohModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 18, reportPluDayComboModifier.getBohModifierCount() == null ? 0 : reportPluDayComboModifier.getBohModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 19, reportPluDayComboModifier.getFocModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getFocModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 20, reportPluDayComboModifier.getFocModifierCount() == null ? 0 : reportPluDayComboModifier.getFocModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 21, reportPluDayComboModifier.getBillFocPrice() == null ? "0.00" : reportPluDayComboModifier.getBillFocPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 22, reportPluDayComboModifier.getBillFocCount() == null ? 0 : reportPluDayComboModifier.getBillFocCount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 23, reportPluDayComboModifier.getComboItemId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 24, reportPluDayComboModifier.getItemId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 25, reportPluDayComboModifier.getItemName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 26, reportPluDayComboModifier.getModifierItemPrice() == null ? "0.00" : reportPluDayComboModifier.getModifierItemPrice());
				SQLiteStatementHelper.bindString(sqLiteStatement, 27, reportPluDayComboModifier.getRealPrice() == null ? "0.00" : reportPluDayComboModifier.getRealPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 28, reportPluDayComboModifier.getRealCount() == null ? 0 : reportPluDayComboModifier.getRealCount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 29, reportPluDayComboModifier.getDaySalesId());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	public static void addReportPluDayModifierList(int daySalesId, SQLiteDatabase db,
												   List<ReportPluDayComboModifier> reportPluDayComboModifiers) {
		if (reportPluDayComboModifiers == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.ReportPluDayComboModifier
					+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, "
					+ "modifierCategoryName, modifierId, modifierName, modifierPrice, modifierCount, billVoidPrice, billVoidCount, "
					+ "voidModifierPrice, voidModifierCount, bohModifierPrice, bohModifierCount, focModifierPrice, focModifierCount, "
					+ "billFocPrice, billFocCount, comboItemId, itemId, itemName, modifierItemPrice, realPrice, realCount, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportPluDayComboModifier reportPluDayComboModifier : reportPluDayComboModifiers) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportPluDayComboModifier.getReportNo());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportPluDayComboModifier.getRestaurantId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportPluDayComboModifier.getRestaurantName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportPluDayComboModifier.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 5, reportPluDayComboModifier.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6, reportPluDayComboModifier.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 7, reportPluDayComboModifier.getModifierCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 8, reportPluDayComboModifier.getModifierCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportPluDayComboModifier.getModifierId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 10, reportPluDayComboModifier.getModifierName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 11, reportPluDayComboModifier.getModifierPrice() == null ? "0.00":reportPluDayComboModifier.getModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 12, reportPluDayComboModifier.getModifierCount() == null ? 0 : reportPluDayComboModifier.getModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 13, reportPluDayComboModifier.getBillVoidPrice() == null ? "0.00" : reportPluDayComboModifier.getBillVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 14, reportPluDayComboModifier.getBillVoidCount() == null ? 0 : reportPluDayComboModifier.getBillVoidCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 15, reportPluDayComboModifier.getVoidModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getVoidModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 16, reportPluDayComboModifier.getVoidModifierCount() == null ? 0 : reportPluDayComboModifier.getVoidModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 17, reportPluDayComboModifier.getBohModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getBohModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 18, reportPluDayComboModifier.getBohModifierCount() == null ? 0 : reportPluDayComboModifier.getBohModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 19, reportPluDayComboModifier.getFocModifierPrice() == null ? "0.00" : reportPluDayComboModifier.getFocModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 20, reportPluDayComboModifier.getFocModifierCount() == null ? 0 : reportPluDayComboModifier.getFocModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 21, reportPluDayComboModifier.getBillFocPrice() == null ? "0.00" : reportPluDayComboModifier.getBillFocPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 22, reportPluDayComboModifier.getBillFocCount() == null ? 0 : reportPluDayComboModifier.getBillFocCount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 23, reportPluDayComboModifier.getComboItemId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 24, reportPluDayComboModifier.getItemId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 25, reportPluDayComboModifier.getItemName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 26, reportPluDayComboModifier.getModifierItemPrice() == null ? "0.00" : reportPluDayComboModifier.getModifierItemPrice());
				SQLiteStatementHelper.bindString(sqLiteStatement, 27, reportPluDayComboModifier.getRealPrice() == null ? "0.00" : reportPluDayComboModifier.getRealPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 28, reportPluDayComboModifier.getRealCount() == null ? 0 : reportPluDayComboModifier.getRealCount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 29, daySalesId);
				sqLiteStatement.executeInsert();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		//ONLY USE to save PLU DAY ITEMs download from cloud
	public static void addReportPluDayModifierFromCloud(List<ReportPluDayComboModifier> reportPluDayComboModifiers) {
		if (reportPluDayComboModifiers == null)
			return;
		SQLiteDatabase db = SQLExe.getDB();
		try {

			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.ReportPluDayComboModifier
					+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId, "
					+ "modifierCategoryName, modifierId, modifierName, modifierPrice, modifierCount, billVoidPrice, billVoidCount, " 
					+ "voidModifierPrice, voidModifierCount, bohModifierPrice, bohModifierCount, focModifierPrice, focModifierCount, "
					+ "billFocPrice, billFocCount, comboItemId, itemId, itemName, modifierItemPrice, realPrice, realCount, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportPluDayComboModifier reportPluDayComboModifier : reportPluDayComboModifiers) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportPluDayComboModifier.getReportNo());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportPluDayComboModifier.getRestaurantId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportPluDayComboModifier.getRestaurantName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportPluDayComboModifier.getRevenueId());				
				SQLiteStatementHelper.bindString(sqLiteStatement, 5, reportPluDayComboModifier.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6, reportPluDayComboModifier.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 7, reportPluDayComboModifier.getModifierCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 8, reportPluDayComboModifier.getModifierCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportPluDayComboModifier.getModifierId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 10, reportPluDayComboModifier.getModifierName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 11, reportPluDayComboModifier.getModifierPrice());				
				SQLiteStatementHelper.bindLong(sqLiteStatement, 12, reportPluDayComboModifier.getModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 13, reportPluDayComboModifier.getBillVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 14, reportPluDayComboModifier.getBillVoidCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 15, reportPluDayComboModifier.getVoidModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 16, reportPluDayComboModifier.getVoidModifierCount());				
				SQLiteStatementHelper.bindString(sqLiteStatement, 17, reportPluDayComboModifier.getBohModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 18, reportPluDayComboModifier.getBohModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 19, reportPluDayComboModifier.getFocModifierPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 20, reportPluDayComboModifier.getFocModifierCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 21, reportPluDayComboModifier.getBillFocPrice());				
				SQLiteStatementHelper.bindLong(sqLiteStatement, 22, reportPluDayComboModifier.getBillFocCount());				
				SQLiteStatementHelper.bindLong(sqLiteStatement, 23, reportPluDayComboModifier.getComboItemId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 24, reportPluDayComboModifier.getItemId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 25, reportPluDayComboModifier.getItemName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 26, reportPluDayComboModifier.getModifierItemPrice());
				SQLiteStatementHelper.bindString(sqLiteStatement, 27, reportPluDayComboModifier.getRealPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 28, reportPluDayComboModifier.getRealCount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 29, reportPluDayComboModifier.getDaySalesId());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	public static ReportPluDayComboModifier getReportPluDayComboModifier(
			Integer reportPluDayComboModifierID) {
		ReportPluDayComboModifier reportPluDayComboModifier = null;
		String sql = "select * from " + TableNames.ReportPluDayComboModifier
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { reportPluDayComboModifierID + "" });
			if (cursor.moveToFirst()) {
				reportPluDayComboModifier = new ReportPluDayComboModifier();
				reportPluDayComboModifier.setId(cursor.getInt(0));
				reportPluDayComboModifier.setReportNo(cursor.getInt(1));
				reportPluDayComboModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayComboModifier.setRestaurantName(cursor.getString(3));
				reportPluDayComboModifier.setRevenueId(cursor.getInt(4));
				reportPluDayComboModifier.setRevenueName(cursor.getString(5));
				reportPluDayComboModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayComboModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayComboModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayComboModifier.setModifierId(cursor.getInt(9));
				reportPluDayComboModifier.setModifierName(cursor.getString(10));
				reportPluDayComboModifier.setModifierPrice(cursor.getString(11));
				reportPluDayComboModifier.setModifierCount(cursor.getInt(12));
				reportPluDayComboModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayComboModifier.setBillVoidCount(cursor.getInt(14)); 
				reportPluDayComboModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayComboModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayComboModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayComboModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayComboModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayComboModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayComboModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayComboModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayComboModifier.setComboItemId(cursor.getInt(23));
				reportPluDayComboModifier.setItemId(cursor.getInt(24));
				reportPluDayComboModifier.setItemName(cursor.getString(25));
				reportPluDayComboModifier.setModifierItemPrice(cursor.getString(26));
				reportPluDayComboModifier.setRealPrice(cursor.getString(27));
				reportPluDayComboModifier.setRealCount(cursor.getInt(28));
				reportPluDayComboModifier.setDaySalesId(cursor.getInt(29));
				return reportPluDayComboModifier;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return reportPluDayComboModifier;
	}

	public static ArrayList<ReportPluDayComboModifier> getAllReportPluDayComboModifier() {
		ArrayList<ReportPluDayComboModifier> result = new ArrayList<ReportPluDayComboModifier>();
		String sql = "select * from " + TableNames.ReportPluDayComboModifier;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayComboModifier reportPluDayComboModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayComboModifier = new ReportPluDayComboModifier();
				reportPluDayComboModifier.setId(cursor.getInt(0));
				reportPluDayComboModifier.setReportNo(cursor.getInt(1));
				reportPluDayComboModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayComboModifier.setRestaurantName(cursor.getString(3));
				reportPluDayComboModifier.setRevenueId(cursor.getInt(4));
				reportPluDayComboModifier.setRevenueName(cursor.getString(5));
				reportPluDayComboModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayComboModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayComboModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayComboModifier.setModifierId(cursor.getInt(9));
				reportPluDayComboModifier.setModifierName(cursor.getString(10));
				reportPluDayComboModifier.setModifierPrice(cursor.getString(11));
				reportPluDayComboModifier.setModifierCount(cursor.getInt(12));
				reportPluDayComboModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayComboModifier.setBillVoidCount(cursor.getInt(14)); 
				reportPluDayComboModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayComboModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayComboModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayComboModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayComboModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayComboModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayComboModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayComboModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayComboModifier.setComboItemId(cursor.getInt(23));
				reportPluDayComboModifier.setItemId(cursor.getInt(24));
				reportPluDayComboModifier.setItemName(cursor.getString(25));
				reportPluDayComboModifier.setModifierItemPrice(cursor.getString(26));
				reportPluDayComboModifier.setRealPrice(cursor.getString(27));
				reportPluDayComboModifier.setRealCount(cursor.getInt(28));
				reportPluDayComboModifier.setDaySalesId(cursor.getInt(29));
				result.add(reportPluDayComboModifier);
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
	public static ArrayList<ReportPluDayComboModifier> getAllReportPluDayComboModifierByDaySalesId(int daySalesId) {
		ArrayList<ReportPluDayComboModifier> result = new ArrayList<>();
		String sql = "select * from " + TableNames.ReportPluDayComboModifier + " where daySalesId = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {daySalesId+""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayComboModifier reportPluDayComboModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayComboModifier = new ReportPluDayComboModifier();
				reportPluDayComboModifier.setId(cursor.getInt(0));
				reportPluDayComboModifier.setReportNo(cursor.getInt(1));
				reportPluDayComboModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayComboModifier.setRestaurantName(cursor.getString(3));
				reportPluDayComboModifier.setRevenueId(cursor.getInt(4));
				reportPluDayComboModifier.setRevenueName(cursor.getString(5));
				reportPluDayComboModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayComboModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayComboModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayComboModifier.setModifierId(cursor.getInt(9));
				reportPluDayComboModifier.setModifierName(cursor.getString(10));
				reportPluDayComboModifier.setModifierPrice(cursor.getString(11));
				reportPluDayComboModifier.setModifierCount(cursor.getInt(12));
				reportPluDayComboModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayComboModifier.setBillVoidCount(cursor.getInt(14));
				reportPluDayComboModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayComboModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayComboModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayComboModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayComboModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayComboModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayComboModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayComboModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayComboModifier.setComboItemId(cursor.getInt(23));
				reportPluDayComboModifier.setItemId(cursor.getInt(24));
				reportPluDayComboModifier.setItemName(cursor.getString(25));
				reportPluDayComboModifier.setModifierItemPrice(cursor.getString(26));
				reportPluDayComboModifier.setRealPrice(cursor.getString(27));
				reportPluDayComboModifier.setRealCount(cursor.getInt(28));
				reportPluDayComboModifier.setDaySalesId(cursor.getInt(29));
				result.add(reportPluDayComboModifier);
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

	public static ArrayList<ReportPluDayComboModifier> getReportPluDayComboModifiersByTime(long date) {
		ArrayList<ReportPluDayComboModifier> result = new ArrayList<ReportPluDayComboModifier>();
		String sql = "select * from "
				+ TableNames.ReportPluDayComboModifier
				+ " where businessDate = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(date)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayComboModifier reportPluDayComboModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayComboModifier = new ReportPluDayComboModifier();
				reportPluDayComboModifier.setId(cursor.getInt(0));
				reportPluDayComboModifier.setReportNo(cursor.getInt(1));
				reportPluDayComboModifier.setRestaurantId(cursor.getInt(2));
				reportPluDayComboModifier.setRestaurantName(cursor.getString(3));
				reportPluDayComboModifier.setRevenueId(cursor.getInt(4));
				reportPluDayComboModifier.setRevenueName(cursor.getString(5));
				reportPluDayComboModifier.setBusinessDate(cursor.getLong(6));
				reportPluDayComboModifier.setModifierCategoryId(cursor.getInt(7));
				reportPluDayComboModifier.setModifierCategoryName(cursor
						.getString(8));
				reportPluDayComboModifier.setModifierId(cursor.getInt(9));
				reportPluDayComboModifier.setModifierName(cursor.getString(10));
				reportPluDayComboModifier.setModifierPrice(cursor.getString(11));
				reportPluDayComboModifier.setModifierCount(cursor.getInt(12));
				reportPluDayComboModifier.setBillVoidPrice(cursor.getString(13));
				reportPluDayComboModifier.setBillVoidCount(cursor.getInt(14)); 
				reportPluDayComboModifier.setVoidModifierPrice(cursor.getString(15));
				reportPluDayComboModifier.setVoidModifierCount(cursor.getInt(16));
				reportPluDayComboModifier.setBohModifierPrice(cursor.getString(17));
				reportPluDayComboModifier.setBohModifierCount(cursor.getInt(18));
				reportPluDayComboModifier.setFocModifierPrice(cursor.getString(19));
				reportPluDayComboModifier.setFocModifierCount(cursor.getInt(20));
				reportPluDayComboModifier.setBillFocPrice(cursor.getString(21));
				reportPluDayComboModifier.setBillFocCount(cursor.getInt(22));
				reportPluDayComboModifier.setComboItemId(cursor.getInt(23));
				reportPluDayComboModifier.setItemId(cursor.getInt(24));
				reportPluDayComboModifier.setItemName(cursor.getString(25));
				reportPluDayComboModifier.setModifierItemPrice(cursor.getString(26));
				reportPluDayComboModifier.setRealPrice(cursor.getString(27));
				reportPluDayComboModifier.setRealCount(cursor.getInt(28));
				reportPluDayComboModifier.setDaySalesId(cursor.getInt(29));
				result.add(reportPluDayComboModifier);
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

	public static ArrayList<ReportPluDayComboModifier> getReportPluDayComboModifiersForZReport(long date) {
		ArrayList<ReportPluDayComboModifier> result = new ArrayList<ReportPluDayComboModifier>();
		String sql = "select restaurantId, restaurantName, revenueId, revenueName, businessDate, modifierCategoryId,"
				+ " modifierCategoryName, modifierId, modifierName, modifierPrice, sum(modifierCount), sum(billVoidPrice), sum(billVoidCount),"
				+ " sum(voidModifierPrice), sum(voidModifierCount), sum(bohModifierPrice), sum(bohModifierCount), sum(focModifierPrice), sum(focModifierCount), "
				+ " sum(billFocPrice), sum(billFocCount), comboItemId, itemId, itemName, modifierItemPrice, sum(realPrice), sum(realCount) from "
				+ TableNames.ReportPluDayComboModifier
				+ " where businessDate = ? group by modifierId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(date)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayComboModifier reportPluDayComboModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayComboModifier = new ReportPluDayComboModifier();
				reportPluDayComboModifier.setRestaurantId(cursor.getInt(0));
				reportPluDayComboModifier.setRestaurantName(cursor.getString(1));
				reportPluDayComboModifier.setRevenueId(cursor.getInt(2));
				reportPluDayComboModifier.setRevenueName(cursor.getString(3));
				reportPluDayComboModifier.setBusinessDate(cursor.getLong(4));
				reportPluDayComboModifier.setModifierCategoryId(cursor.getInt(5));
				reportPluDayComboModifier.setModifierCategoryName(cursor
						.getString(6));
				reportPluDayComboModifier.setModifierId(cursor.getInt(7));
				reportPluDayComboModifier.setModifierName(cursor.getString(8));
				reportPluDayComboModifier.setModifierPrice(BH.getBD(cursor.getString(9)).toString());
				reportPluDayComboModifier.setModifierCount(cursor.getInt(10));
				reportPluDayComboModifier.setBillVoidPrice(BH.getBD(cursor.getString(11)).toString());
				reportPluDayComboModifier.setBillVoidCount(cursor.getInt(12));
				reportPluDayComboModifier.setVoidModifierPrice(BH.getBD(cursor.getString(13)).toString());
				reportPluDayComboModifier.setVoidModifierCount(cursor.getInt(14));
				reportPluDayComboModifier.setBohModifierPrice(BH.getBD(cursor.getString(15)).toString());
				reportPluDayComboModifier.setBohModifierCount(cursor.getInt(16));
				reportPluDayComboModifier.setFocModifierPrice(BH.getBD(cursor.getString(17)).toString());
				reportPluDayComboModifier.setFocModifierCount(cursor.getInt(18));
				reportPluDayComboModifier.setBillFocPrice(BH.getBD(cursor.getString(19)).toString());
				reportPluDayComboModifier.setBillFocCount(cursor.getInt(20));
				reportPluDayComboModifier.setComboItemId(cursor.getInt(21));
				reportPluDayComboModifier.setItemId(cursor.getInt(22));
				reportPluDayComboModifier.setItemName(cursor.getString(23));
				reportPluDayComboModifier.setModifierItemPrice(BH.getBD(cursor.getString(24)).toString());
				reportPluDayComboModifier.setRealPrice(BH.getBD(cursor.getString(25)).toString());
				reportPluDayComboModifier.setRealCount(cursor.getInt(26));
				result.add(reportPluDayComboModifier);
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

	public static void deleteReportPluDayComboModifier(
			ReportPluDayComboModifier reportPluDayComboModifier) {
		String sql = "delete from " + TableNames.ReportPluDayComboModifier
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { reportPluDayComboModifier.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteReportPluDayComboModifierByBusinessDate(long businessDate) {
		String sql = "delete from " + TableNames.ReportPluDayComboModifier
				+ " where businessDate = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { businessDate + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
