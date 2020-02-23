package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.Modifier;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ModifierSQL {

	public static void addModifier(Modifier modifier) {
		if (modifier == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.Modifier
					+ "(id, restaurantId, type, categoryId, categoryName, price, modifierName, isActive, isDefault, itemId, isSet, qty, mustDefault, optionQty,minNumber,maxNumber)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { 
							modifier.getId(),
							modifier.getRestaurantId(), 
							modifier.getType(),
							modifier.getCategoryId(),
							modifier.getCategoryName(), 
							modifier.getPrice(),
							modifier.getModifierName(), 
							modifier.getIsActive(),
							modifier.getIsDefault(), 
							modifier.getItemId(),
							modifier.getIsSet(),
							modifier.getQty(),
							modifier.getMustDefault(),
							modifier.getOptionQty(),modifier.getMinNumber(),modifier.getMaxNumber()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addModifierList(List<Modifier> modifierList) {
		if (modifierList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.Modifier
					+ "(id, restaurantId, type, categoryId, categoryName, price, modifierName, isActive, isDefault, itemId, isSet, qty, mustDefault,optionQty,minNumber,maxNumber)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (Modifier modifier : modifierList) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							modifier.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							modifier.getRestaurantId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							modifier.getType());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							modifier.getCategoryId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							modifier.getCategoryName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 6,
							modifier.getPrice());
					SQLiteStatementHelper.bindString(sqLiteStatement, 7,
							modifier.getModifierName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							modifier.getIsActive());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							modifier.getIsDefault());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
							modifier.getItemId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
							modifier.getIsSet());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							modifier.getQty());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
							modifier.getMustDefault());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
							modifier.getOptionQty());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
							modifier.getMinNumber());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
							modifier.getMaxNumber());

					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<Modifier> getAllModifier() {
		ArrayList<Modifier> result = new ArrayList<Modifier>();
		String sql = "select * from " + TableNames.Modifier + " where isActive = 1";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Modifier modifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				modifier = new Modifier();
				modifier.setId(cursor.getInt(0));
				modifier.setRestaurantId(cursor.getInt(1));
				modifier.setType(cursor.getInt(2));
				modifier.setCategoryId(cursor.getInt(3));
				modifier.setCategoryName(cursor.getString(4));
				modifier.setPrice(cursor.getString(5));
				modifier.setModifierName(cursor.getString(6));
				modifier.setIsActive(cursor.getInt(7));
				modifier.setIsDefault(cursor.getInt(8));
				modifier.setItemId(cursor.getInt(9));
				modifier.setIsSet(cursor.getInt(10));
				modifier.setQty(cursor.getInt(11));
				modifier.setMustDefault(cursor.getInt(12));
				modifier.setOptionQty(cursor.getInt(13));
				modifier.setMinNumber(cursor.getInt(14));
				modifier.setMaxNumber(cursor.getInt(15));
				result.add(modifier);
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
	
	public static ArrayList<Modifier> getAllModifierForReport() {
		ArrayList<Modifier> result = new ArrayList<Modifier>();
		String sql = "select * from " + TableNames.Modifier;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Modifier modifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				modifier = new Modifier();
				modifier.setId(cursor.getInt(0));
				modifier.setRestaurantId(cursor.getInt(1));
				modifier.setType(cursor.getInt(2));
				modifier.setCategoryId(cursor.getInt(3));
				modifier.setCategoryName(cursor.getString(4));
				modifier.setPrice(cursor.getString(5));
				modifier.setModifierName(cursor.getString(6));
				modifier.setIsActive(cursor.getInt(7));
				modifier.setIsDefault(cursor.getInt(8));
				modifier.setItemId(cursor.getInt(9));
				modifier.setIsSet(cursor.getInt(10));
				modifier.setQty(cursor.getInt(11));
				modifier.setMustDefault(cursor.getInt(12));
				modifier.setOptionQty(cursor.getInt(13));
				modifier.setMinNumber(cursor.getInt(14));
				modifier.setMaxNumber(cursor.getInt(15));
				result.add(modifier);
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

	public static Modifier getModifierById(Integer id){
		Modifier modifier = null;
		String sql = "select * from " + TableNames.Modifier
				+ " where id = ? and isActive = 1";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[]{ id + ""});
			if (cursor.moveToFirst()) {
				modifier = new Modifier();
				modifier.setId(cursor.getInt(0));
				modifier.setRestaurantId(cursor.getInt(1));
				modifier.setType(cursor.getInt(2));
				modifier.setCategoryId(cursor.getInt(3));
				modifier.setCategoryName(cursor.getString(4));
				modifier.setPrice(cursor.getString(5));
				modifier.setModifierName(cursor.getString(6));
				modifier.setIsActive(cursor.getInt(7));
				modifier.setIsDefault(cursor.getInt(8));
				modifier.setItemId(cursor.getInt(9));
				modifier.setIsSet(cursor.getInt(10));
				modifier.setQty(cursor.getInt(11));
				modifier.setMustDefault(cursor.getInt(12));
				modifier.setOptionQty(cursor.getInt(13));
				modifier.setMinNumber(cursor.getInt(14));
				modifier.setMaxNumber(cursor.getInt(15));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return modifier;
	}
	
	public static List<Modifier> getModifierCategorys(int restaurantId){
		ArrayList<Modifier> result = new ArrayList<Modifier>();
		String sql = "select * from " + TableNames.Modifier
				+ " where restaurantId = ? and type = 0 and isActive = 1";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[]{ restaurantId + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Modifier modifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				modifier = new Modifier();
				modifier.setId(cursor.getInt(0));
				modifier.setRestaurantId(cursor.getInt(1));
				modifier.setType(cursor.getInt(2));
				modifier.setCategoryId(cursor.getInt(3));
				modifier.setCategoryName(cursor.getString(4));
				modifier.setPrice(cursor.getString(5));
				modifier.setModifierName(cursor.getString(6));
				modifier.setIsActive(cursor.getInt(7));
				modifier.setIsDefault(cursor.getInt(8));
				modifier.setItemId(cursor.getInt(9));
				modifier.setIsSet(cursor.getInt(10));
				modifier.setQty(cursor.getInt(11));
				modifier.setMustDefault(cursor.getInt(12));
				modifier.setOptionQty(cursor.getInt(13));
				modifier.setMinNumber(cursor.getInt(14));
				modifier.setMaxNumber(cursor.getInt(15));
				result.add(modifier);
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
	
	
	public static void deleteModifier(Modifier modifier) {
		String sql = "delete from " + TableNames.Modifier + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { modifier.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteAllModifier() {
		String sql = "delete from " + TableNames.Modifier;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
