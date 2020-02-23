package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.HappyHour;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class HappyHourSQL {
	public static void addHappyHourList(List<HappyHour> happyHours) {
		if (happyHours == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.HappyHour
					+ "(id, restaurant_id, happy_hour_name, is_active, create_time, update_time)"
					+ " values (?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (HappyHour happyHour : happyHours) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							happyHour.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							happyHour.getRestaurantId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 3,
							happyHour.getHappy_hour_name());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							happyHour.getIsActive());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							happyHour.getCreateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							happyHour.getUpdateTime());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	public static ArrayList<HappyHour> getAllHappyHour() {
		ArrayList<HappyHour> result = new ArrayList<HappyHour>();
		String sql = "select * from " + TableNames.HappyHour;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			HappyHour happyHour = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				happyHour = new HappyHour();
				happyHour.setId(cursor.getInt(0));
				happyHour.setRestaurantId(cursor.getInt(1));
				happyHour.setHappy_hour_name(cursor.getString(2));
				happyHour.setIsActive(cursor.getInt(3));
				happyHour.setCreateTime(cursor.getLong(4));
				happyHour.setUpdateTime(cursor.getLong(5));
				result.add(happyHour);
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
	
	public static void deleteAllHappyHour(){

		String sql = "delete from " + TableNames.HappyHour ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { });
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
