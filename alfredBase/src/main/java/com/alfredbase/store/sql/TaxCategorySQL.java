package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class TaxCategorySQL {

	public static void addTaxCategory(TaxCategory taxCategory) {
		if (taxCategory == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.TaxCategory
					+ "(id,companyId,restaurantId,taxCategoryId,taxCategoryName,taxId,taxOn,taxOnId,indexId,status)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { taxCategory.getId(),
							taxCategory.getCompanyId(),
							taxCategory.getRestaurantId(),
							taxCategory.getTaxCategoryId(),
							taxCategory.getTaxCategoryName(),
							taxCategory.getTaxId(), taxCategory.getTaxOn(),
							taxCategory.getTaxOnId(), taxCategory.getIndex(),
							taxCategory.getStatus() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addTaxCategorys(List<TaxCategory> taxCategories) {
		if (taxCategories == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.TaxCategory
					+ "(id,companyId,restaurantId,taxCategoryId,taxCategoryName,taxId,taxOn,taxOnId,indexId,status)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (TaxCategory taxCategory : taxCategories) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							taxCategory.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							taxCategory.getCompanyId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							taxCategory.getRestaurantId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							taxCategory.getTaxCategoryId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							taxCategory.getTaxCategoryName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							taxCategory.getTaxId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
							taxCategory.getTaxOn());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							taxCategory.getTaxOnId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							taxCategory.getIndex());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
							taxCategory.getStatus());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<TaxCategory> getAllTaxCategory() {
		ArrayList<TaxCategory> result = new ArrayList<TaxCategory>();
		String sql = "select * from " + TableNames.TaxCategory;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			TaxCategory taxCategory = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				taxCategory = new TaxCategory();
				taxCategory.setId(cursor.getInt(0));
				taxCategory.setCompanyId(cursor.getInt(1));
				taxCategory.setRestaurantId(cursor.getInt(2));
				taxCategory.setTaxCategoryId(cursor.getInt(3));
				taxCategory.setTaxCategoryName(cursor.getString(4));
				taxCategory.setTaxId(cursor.getInt(5));
				taxCategory.setTaxOn(cursor.getInt(6));
				taxCategory.setTaxOnId(cursor.getInt(7));
				taxCategory.setIndex(cursor.getInt(8));
				taxCategory.setStatus(cursor.getInt(9));
				result.add(taxCategory);
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

	public static void deleteTaxCategory(TaxCategory taxCategory) {
		String sql = "delete from " + TableNames.TaxCategory + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { taxCategory.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllTaxCategory() {
		String sql = "delete from " + TableNames.TaxCategory;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
