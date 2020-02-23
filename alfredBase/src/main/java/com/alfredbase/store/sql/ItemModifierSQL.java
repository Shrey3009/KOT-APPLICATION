package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemModifierSQL {

	public static void addItemModifier(ItemModifier itemModifier) {
		if (itemModifier == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.ItemModifier
					+ "(id, restaurantId, itemId, modifierId, modifierCategoryId, itemCategoryId, type,indexNo)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { itemModifier.getId(),
							itemModifier.getRestaurantId(),
							itemModifier.getItemId(),
							itemModifier.getModifierId(),
							itemModifier.getModifierCategoryId(),
							itemModifier.getItemCategoryId(),
							itemModifier.getType(),
							itemModifier.getIndexNo()
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addItemModifierList(List<ItemModifier> itemModifierList) {
		if (itemModifierList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.ItemModifier
					+ "(id, restaurantId, itemId, modifierId, modifierCategoryId, itemCategoryId, type,indexNo)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (ItemModifier itemModifier : itemModifierList) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							itemModifier.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							itemModifier.getRestaurantId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							itemModifier.getItemId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							itemModifier.getModifierId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							itemModifier.getModifierCategoryId());
					SQLiteStatementHelper.bindLong(sqLiteStatement,6,
							itemModifier.getItemCategoryId());
					SQLiteStatementHelper.bindLong(sqLiteStatement,7,
							itemModifier.getType());
					SQLiteStatementHelper.bindLong(sqLiteStatement,8,
							itemModifier.getIndexNo());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<ItemModifier> getAllItemModifier() {
		ArrayList<ItemModifier> result = new ArrayList<ItemModifier>();
	//	+" order by indexNo asc"
		String sql = "select * from " + TableNames.ItemModifier+" order by indexNo asc";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ItemModifier itemModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				itemModifier = new ItemModifier();
				itemModifier.setId(cursor.getInt(0));
				itemModifier.setRestaurantId(cursor.getInt(1));
				itemModifier.setItemId(cursor.getInt(2));
				itemModifier.setModifierId(cursor.getInt(3));
				itemModifier.setModifierCategoryId(cursor.getInt(4));
				itemModifier.setItemCategoryId(cursor.getInt(5));
				itemModifier.setType(cursor.getInt(6));
				itemModifier.setIndexNo(cursor.getInt(7));
				result.add(itemModifier);
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

	public static void deleteItemModifier(ItemModifier itemModifier) {
		String sql = "delete from " + TableNames.ItemModifier + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { itemModifier.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllItemModifier() {
		String sql = "delete from " + TableNames.ItemModifier;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
