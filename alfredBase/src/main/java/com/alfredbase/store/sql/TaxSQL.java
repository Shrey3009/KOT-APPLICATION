package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.Tax;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class TaxSQL {

	public static void addTax(Tax tax) {
		if (tax == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.Tax
					+ "(id,companyId,restaurantId,taxName,taxPercentage,taxType,status,createTime,updateTime,beforeDiscount)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { tax.getId(), tax.getCompanyId(),
							tax.getRestaurantId(), tax.getTaxName(),
							tax.getTaxPercentage(), tax.getTaxType(),
							tax.getStatus(), tax.getCreateTime(),
							tax.getUpdateTime(),tax.getBeforeDiscount() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addTaxs(List<Tax> taxs) {
		if (taxs == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.Tax
					+ "(id,companyId,restaurantId,taxName,taxPercentage,taxType,status,createTime,updateTime,beforeDiscount)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (Tax tax : taxs) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							tax.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							tax.getCompanyId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							tax.getRestaurantId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							tax.getTaxName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							tax.getTaxPercentage());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							tax.getTaxType());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
							tax.getStatus());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							tax.getCreateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							tax.getUpdateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
							tax.getBeforeDiscount());

					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<Tax> getAllTax() {
		ArrayList<Tax> result = new ArrayList<Tax>();
		String sql = "select * from " + TableNames.Tax;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Tax tax = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				tax = new Tax();
				tax.setId(cursor.getInt(0));
				tax.setCompanyId(cursor.getInt(1));
				tax.setRestaurantId(cursor.getInt(2));
				tax.setTaxName(cursor.getString(3));
				tax.setTaxPercentage(cursor.getString(4));
				tax.setTaxType(cursor.getInt(5));
				tax.setStatus(cursor.getInt(6));
				tax.setCreateTime(cursor.getLong(7));
				tax.setUpdateTime(cursor.getLong(8));
				tax.setBeforeDiscount(cursor.getInt(9));
				result.add(tax);
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

	public static Tax getTaxId(Integer taxId) {
		Tax tax = null;
		String sql = "select * from " + TableNames.Tax
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { taxId + "" });
			if (cursor.moveToFirst()) {
				tax = new Tax();
				tax.setId(cursor.getInt(0));
				tax.setCompanyId(cursor.getInt(1));
				tax.setRestaurantId(cursor.getInt(2));
				tax.setTaxName(cursor.getString(3));
				tax.setTaxPercentage(cursor.getString(4));
				tax.setTaxType(cursor.getInt(5));
				tax.setStatus(cursor.getInt(6));
				tax.setCreateTime(cursor.getLong(7));
				tax.setUpdateTime(cursor.getLong(8));
				tax.setBeforeDiscount(cursor.getInt(9));
				return tax;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return tax;
	}




	public static void deleteTax(Tax tax) {
		String sql = "delete from " + TableNames.Tax + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { tax.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllTax() {
		String sql = "delete from " + TableNames.Tax;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
