package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class HappyHourWeekSQL {
	public static void addHappyHourWeekList(List<HappyHourWeek> happyHourWeeks) {
		if (happyHourWeeks == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.HappyHourWeek
					+ "(id, happy_hour_id, week, start_time, end_time , is_active)"
					+ " values (?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (HappyHourWeek happyHourWeek : happyHourWeeks) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							happyHourWeek.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							happyHourWeek.getHappyHourId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 3,
							happyHourWeek.getWeek());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							happyHourWeek.getStartTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							happyHourWeek.getEndTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							happyHourWeek.getIsActive());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	public static ArrayList<HappyHourWeek> getAllHappyHourWeek() {
		ArrayList<HappyHourWeek> result = new ArrayList<HappyHourWeek>();
		String sql = "select * from " + TableNames.HappyHourWeek;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			HappyHourWeek happyHourWeek = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				happyHourWeek = new HappyHourWeek();
				happyHourWeek.setId(cursor.getInt(0));
				happyHourWeek.setHappyHourId(cursor.getInt(1));
				happyHourWeek.setWeek(cursor.getString(2));
				happyHourWeek.setStartTime(cursor.getLong(3));
				happyHourWeek.setEndTime(cursor.getLong(4));
				happyHourWeek.setIsActive(cursor.getInt(5));
				result.add(happyHourWeek);
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
	
	public static void deleteAllHappyHourWeek(){

		String sql = "delete from " + TableNames.HappyHourWeek ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { });
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
