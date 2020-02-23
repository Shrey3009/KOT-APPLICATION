package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemHappyHourSQL {
	public static void addItemHappyHourList(List<ItemHappyHour> itemHappyHours) {
		if (itemHappyHours == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.ItemHappyHour
					+ "(id , happy_hour_id, item_main_category_id,item_category_id,item_id, type,  discount_price, discount_rate,  free_num,item_main_category_name , item_category_name  , item_name,free_item_id,free_item_name)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = SQLExe.getDB().compileStatement(
					sql);
			try {
				for (ItemHappyHour itemHappyHour : itemHappyHours) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							itemHappyHour.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							itemHappyHour.getHappyHourId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							itemHappyHour.getItemMainCategoryId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							itemHappyHour.getItemCategoryId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							itemHappyHour.getItemId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							itemHappyHour.getType());
					SQLiteStatementHelper.bindString(sqLiteStatement, 7,
							itemHappyHour.getDiscountPrice());
					SQLiteStatementHelper.bindString(sqLiteStatement, 8,
							itemHappyHour.getDiscountRate());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							itemHappyHour.getFreeNum());
					SQLiteStatementHelper.bindString(sqLiteStatement, 10,
							itemHappyHour.getItemMainCategoryName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 11,
							itemHappyHour.getItemCategoryName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 12,
							itemHappyHour.getItemName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
							itemHappyHour.getFreeItemId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 14,
							itemHappyHour.getFreeItemName());
					sqLiteStatement.executeInsert();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<ItemHappyHour> getAllItemHappyHour() {
		ArrayList<ItemHappyHour> result = new ArrayList<ItemHappyHour>();
		String sql = "select * from " + TableNames.ItemHappyHour;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ItemHappyHour itemHappyHour = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				itemHappyHour = new ItemHappyHour();
				itemHappyHour.setId(cursor.getInt(0));
				itemHappyHour.setHappyHourId(cursor.getInt(1));
				itemHappyHour.setItemMainCategoryId(cursor.getInt(2));
				itemHappyHour.setItemCategoryId(cursor.getInt(3));
				itemHappyHour.setItemId(cursor.getInt(4));
				itemHappyHour.setType(cursor.getInt(5));
				itemHappyHour.setDiscountPrice(cursor.getString(6));
				itemHappyHour.setDiscountRate(cursor.getString(7));
				itemHappyHour.setFreeNum(cursor.getInt(8));
				itemHappyHour.setItemMainCategoryName(cursor.getString(9));
				itemHappyHour.setItemCategoryName(cursor.getString(10));
				itemHappyHour.setItemName(cursor.getString(11));
				itemHappyHour.setFreeItemId(cursor.getInt(12));
				itemHappyHour.setFreeItemName(cursor.getString(13));
				result.add(itemHappyHour);
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
	public static void deleteAllItemHappyHour(){

		String sql = "delete from " + TableNames.ItemHappyHour ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { });
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
