package com.alfredbase.store.sql;

public class TablesSQL {
//
//	public static void addTables(Tables tables) {
//		if (tables == null)
//			return;
//		try {
//			String sql = "insert into "
//					+ TableNames.Tables
//					+ "(id, restaurantId,  revenueId,placesId,  tableName, tablePacks, isActive, tableStatus, orders)"
//					+ " values (?,?,?,?,?,?,?,?,?)";
//			SQLExe.getDB().execSQL(
//					sql,
//					new Object[] { tables.getId(), tables.getRestaurantId(),
//							tables.getRevenueId(), tables.getPlacesId(),
//							tables.getTableName(), tables.getTablePacks(),
//							tables.getIsActive(), tables.getTableStatus(),
//							tables.getOrders()});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 升级数据库tables数据以后，需要更新内存数据
//	 *
//	 * @param tables
//	 */
//	public static void updateTables(Tables tables) {
//		if (tables == null)
//			return;
//		try {
//			String sql = "replace into "
//					+ TableNames.Tables
//					+ "(id, restaurantId,  revenueId,placesId,  tableName, tablePacks, isActive, tableStatus, orders)"
//					+ " values (?,?,?,?,?,?,?,?,?)";
//			SQLExe.getDB().execSQL(
//					sql,
//					new Object[] { tables.getId(), tables.getRestaurantId(),
//							tables.getRevenueId(), tables.getPlacesId(),
//							tables.getTableName(), tables.getTablePacks(),
//							tables.getIsActive(), tables.getTableStatus(), tables.getOrders() });
//			CoreData.getInstance().setTableList(getAllTables());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void addTablesList(List<Tables> tablesList) {
//		if (tablesList == null) {
//			return;
//		}
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			db.beginTransaction();
//			String sql = "replace into "
//					+ TableNames.Tables
//					+ "(id, restaurantId,  revenueId,placesId,  tableName, tablePacks, isActive, tableStatus, orders)"
//					+ " values (?,?,?,?,?,?,?,?,?)";
//			SQLiteStatement sqLiteStatement = db.compileStatement(
//					sql);
//				for (Tables tables : tablesList) {
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
//							tables.getId());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
//							tables.getRestaurantId());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
//							tables.getRevenueId());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
//							tables.getPlacesId());
//					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
//							tables.getTableName());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
//							tables.getTablePacks());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
//							tables.getIsActive());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
//							tables.getTableStatus());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
//							tables.getOrders());
//					sqLiteStatement.executeInsert();
//				}
//				db.setTransactionSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			db.endTransaction();
//		}
//	}
//
//	public static ArrayList<Tables> getAllTables() {
//		ArrayList<Tables> result = new ArrayList<Tables>();
//		String sql = "select * from " + TableNames.Tables;
//		Cursor cursor = null;
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			db.beginTransaction();
//			cursor = db.rawQuery(sql, new String[] {});
//			int count = cursor.getCount();
//			if (count < 1) {
//				return result;
//			}
//			Tables tables = null;
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				tables = new Tables();
//				tables.setId(cursor.getInt(0));
//				tables.setRestaurantId(cursor.getInt(1));
//				tables.setRevenueId(cursor.getInt(2));
//				tables.setPlacesId(cursor.getInt(3));
//				tables.setTableName(cursor.getString(4));
//				tables.setTablePacks(cursor.getInt(5));
//				tables.setIsActive(cursor.getInt(6));
//				tables.setTableStatus(cursor.getInt(7));
//				tables.setOrders(cursor.getInt(8));
//				result.add(tables);
//			}
//			db.setTransactionSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//			db.endTransaction();
//		}
//		return result;
//	}
//
//	public static ArrayList<Tables> getAllUsedTables() {
//		ArrayList<Tables> result = new ArrayList<Tables>();
//		String sql = "select * from " + TableNames.Tables + " where tableStatus <> " + ParamConst.TABLE_STATUS_IDLE;
//		Cursor cursor = null;
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			db.beginTransaction();
//			cursor = db.rawQuery(sql, new String[] {});
//			int count = cursor.getCount();
//			if (count < 1) {
//				return result;
//			}
//			Tables tables = null;
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				tables = new Tables();
//				tables.setId(cursor.getInt(0));
//				tables.setRestaurantId(cursor.getInt(1));
//				tables.setRevenueId(cursor.getInt(2));
//				tables.setPlacesId(cursor.getInt(3));
//				tables.setTableName(cursor.getString(4));
//				tables.setTablePacks(cursor.getInt(5));
//				tables.setIsActive(cursor.getInt(6));
//				tables.setTableStatus(cursor.getInt(7));
//				tables.setOrders(cursor.getInt(8));
//				result.add(tables);
//			}
//			db.setTransactionSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//			db.endTransaction();
//		}
//		return result;
//	}
//
//
//	public static Tables getAllUsedOneTables() {
//		String sql = "select * from " + TableNames.Tables + " where tableStatus = " + ParamConst.TABLE_STATUS_IDLE;
//		Cursor cursor = null;
//		Tables tables = null;
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			db.beginTransaction();
//			cursor = db.rawQuery(sql, new String[] {});
//			int count = cursor.getCount();
//			if (count < 1) {
//				return tables;
//			}
//
//			if (cursor.moveToFirst()) {
//				tables = new Tables();
//				tables.setId(cursor.getInt(0));
//				tables.setRestaurantId(cursor.getInt(1));
//				tables.setRevenueId(cursor.getInt(2));
//				tables.setPlacesId(cursor.getInt(3));
//				tables.setTableName(cursor.getString(4));
//				tables.setTablePacks(cursor.getInt(5));
//				tables.setIsActive(cursor.getInt(6));
//				tables.setTableStatus(cursor.getInt(7));
//				tables.setOrders(cursor.getInt(8));
//			}
//			db.setTransactionSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//			db.endTransaction();
//		}
//		return tables;
//	}
//
//	public static Tables getTableById(int id) {
//		String sql = "select * from " + TableNames.Tables + " where id = ?";
//		Tables tables = new Tables();
//		Cursor cursor = null;
//		try {
//			cursor = SQLExe.getDB().rawQuery(sql, new String[] {String.valueOf(id)});
//
//			if (cursor.moveToFirst()) {
//				tables.setId(cursor.getInt(0));
//				tables.setRestaurantId(cursor.getInt(1));
//				tables.setRevenueId(cursor.getInt(2));
//				tables.setPlacesId(cursor.getInt(3));
//				tables.setTableName(cursor.getString(4));
//				tables.setTablePacks(cursor.getInt(5));
//				tables.setIsActive(cursor.getInt(6));
//				tables.setTableStatus(cursor.getInt(7));
//				tables.setOrders(cursor.getInt(8));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return tables;
//	}
//
//	public static Tables getKioskTable() {
//		String sql = "select * from " + TableNames.Tables;
//		Tables tables = new Tables();
//		Cursor cursor = null;
//		try {
//			cursor = SQLExe.getDB().rawQuery(sql, new String[] {});
//
//			if (cursor.moveToFirst()) {
//				tables.setId(cursor.getInt(0));
//				tables.setRestaurantId(cursor.getInt(1));
//				tables.setRevenueId(cursor.getInt(2));
//				tables.setPlacesId(cursor.getInt(3));
//				tables.setTableName(cursor.getString(4));
//				tables.setTablePacks(cursor.getInt(5));
//				tables.setIsActive(cursor.getInt(6));
//				tables.setTableStatus(cursor.getInt(7));
//				tables.setOrders(cursor.getInt(8));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return tables;
//	}
//
//	public static void deleteTables(Tables tables) {
//		String sql = "delete from " + TableNames.Tables + " where id = ?";
//		try {
//			SQLExe.getDB().execSQL(sql, new Object[] { tables.getId() });
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static ArrayList<Tables> getTablesBuyPlaces(Places places) {
//		ArrayList<Tables> result = new ArrayList<Tables>();
//		String sql = "select * from " + TableNames.Tables
//				+ " where placesId = ?";
//		Cursor cursor = null;
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			db.beginTransaction();
//			cursor = db.rawQuery(sql,
//					new String[] { Integer.toString(places.getId()) });
//			int count = cursor.getCount();
//			if (count < 1) {
//				return result;
//			}
//			Tables tables = null;
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				tables = new Tables();
//				tables.setId(cursor.getInt(0));
//				tables.setRestaurantId(cursor.getInt(1));
//				tables.setRevenueId(cursor.getInt(2));
//				tables.setPlacesId(cursor.getInt(3));
//				tables.setTableName(cursor.getString(4));
//				tables.setTablePacks(cursor.getInt(5));
//				tables.setIsActive(cursor.getInt(6));
//				tables.setTableStatus(cursor.getInt(7));
//				tables.setOrders(cursor.getInt(8));
//				result.add(tables);
//			}
//			db.setTransactionSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//			db.endTransaction();
//		}
//		return result;
//	}
//
//	public static void setAllTableIdle(){
//		String sql = "update " + TableNames.Tables + " set tableStatus = " + ParamConst.TABLE_STATUS_IDLE + " where tableStatus <> " + ParamConst.TABLE_STATUS_IDLE;
//		try {
//			SQLExe.getDB().execSQL(sql, new Object[] {});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void deleteAllTables() {
//		String sql = "delete from " + TableNames.Tables;
//		try {
//			SQLExe.getDB().execSQL(sql, new Object[] {});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


}
