package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.UserRestaurant;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class UserRestaurantSQL {

	public static void addUsers(List<UserRestaurant> users) {
		if (users == null) {
			return;
		}
		
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.UserRestaurant
					+ "(id,userId, restaurantId,  revenueId, kitchenId)"
					+ " values (?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (UserRestaurant user : users) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							user.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							user.getUserId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							user.getRestaurantId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							user.getRevenueId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							user.getKitchenId());

					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
    public static List<UserRestaurant> getAll() {
		ArrayList<UserRestaurant> result = new ArrayList<UserRestaurant>();
		String sql = "select * from " + TableNames.UserRestaurant;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			UserRestaurant user = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				user = new UserRestaurant();
				user.setId(cursor.getInt(0));
				user.setUserId(cursor.getInt(1));
				user.setRestaurantId(cursor.getInt(2));
				user.setRevenueId(cursor.getInt(3));
				user.setKitchenId(cursor.getInt(4));

				result.add(user);
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
    
    public static List<Integer> getRevenueIdByUserId(int userId) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		String sql = "select revenueId from " + TableNames.UserRestaurant + " where userId = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {userId+""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.add(cursor.getInt(0));
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
    
    public static void deleteAllUserRestaurant() {
		String sql = "delete from " + TableNames.UserRestaurant;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
