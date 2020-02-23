package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class RestaurantConfigSQL {

	public static void addRestaurant(RestaurantConfig restaurantConfig) {
		if (restaurantConfig == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.RestaurantConfig
					+ " (id, restaurantId, paraId, paraType, paraName, paraValue1, paraValue2, paraValue3)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLExe.getDB()
					.execSQL(
							sql,
							new Object[] { restaurantConfig.getId(),
										   restaurantConfig.getRestaurantId(),
										   restaurantConfig.getParaId(),
										   restaurantConfig.getParaType(),
										   restaurantConfig.getParaName(),
										   restaurantConfig.getParaValue1(),
										   restaurantConfig.getParaValue2(),
							                restaurantConfig.getParaValue3()
							});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addRestaurantConfigs(List<RestaurantConfig> restaurantConfigs) {
		if (restaurantConfigs == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.RestaurantConfig
					+ " (id, restaurantId, paraId, paraType, paraName, paraValue1, paraValue2, paraValue3)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (RestaurantConfig restaurantConfig : restaurantConfigs) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							restaurantConfig.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							restaurantConfig.getRestaurantId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							restaurantConfig.getParaId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							restaurantConfig.getParaType());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							restaurantConfig.getParaName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 6,
							restaurantConfig.getParaValue1());
					SQLiteStatementHelper.bindString(sqLiteStatement, 7,
							restaurantConfig.getParaValue2());
					SQLiteStatementHelper.bindString(sqLiteStatement, 8,
							restaurantConfig.getParaValue3());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	public static List<RestaurantConfig> getAllRestaurantConfig() {
		ArrayList<RestaurantConfig> result = new ArrayList<RestaurantConfig>();
		String sql = "select * from " + TableNames.RestaurantConfig;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			RestaurantConfig restaurantConfig = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				restaurantConfig = new RestaurantConfig();
				restaurantConfig.setId(cursor.getInt(0));
				restaurantConfig.setRestaurantId(cursor.getInt(1));
				restaurantConfig.setParaId(cursor.getInt(2));
				restaurantConfig.setParaType(cursor.getInt(3));
				restaurantConfig.setParaName(cursor.getString(4));
				restaurantConfig.setParaValue1(cursor.getString(5));
				restaurantConfig.setParaValue2(cursor.getString(6));
				restaurantConfig.setParaValue3(cursor.getString(7));
				result.add(restaurantConfig);
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
	public static void deleteRestaurantConfig(RestaurantConfig restaurantConfig) {
		String sql = "delete from " + TableNames.RestaurantConfig + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { restaurantConfig.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllRestaurantConfig() {
		String sql = "delete from " + TableNames.RestaurantConfig;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
