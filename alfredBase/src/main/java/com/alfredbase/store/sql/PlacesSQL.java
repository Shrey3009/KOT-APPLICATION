package com.alfredbase.store.sql;

public class PlacesSQL {
//
//	public static void addPlaces(Places places) {
//		if (places == null) {
//			return;
//		}
//		try {
//			String sql = "replace into "
//					+ TableNames.Places
//					+ "(id, placeName,placeDescription,restaurantId,revenueId,isActive)"
//					+ " values (?,?,?,?,?,?)";
//			SQLExe.getDB().execSQL(
//					sql,
//					new Object[] { places.getId(), places.getPlaceName(),
//							places.getPlaceDescription(),
//							places.getRestaurantId(), places.getRevenueId(),
//							places.getIsActive() });
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void addPlacesList(List<Places> placeslList) {
//		if (placeslList == null) {
//			return;
//		}
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			db.beginTransaction();
//			String sql = "replace into "
//					+ TableNames.Places
//					+ "(id, placeName,placeDescription,restaurantId,revenueId,isActive)"
//					+ " values (?,?,?,?,?,?)";
//			SQLiteStatement sqLiteStatement = db.compileStatement(
//					sql);
//				for (Places places : placeslList) {
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
//							places.getId());
//					SQLiteStatementHelper.bindString(sqLiteStatement, 2,
//							places.getPlaceName());
//					SQLiteStatementHelper.bindString(sqLiteStatement, 3,
//							places.getPlaceDescription());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
//							places.getRestaurantId());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
//							places.getRevenueId());
//					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
//							places.getIsActive());
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
//	public static ArrayList<Places> getAllPlaces() {
//		ArrayList<Places> result = new ArrayList<Places>();
//		String sql = "select * from " + TableNames.Places;
//		Cursor cursor = null;
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			db.beginTransaction();
//			cursor = db.rawQuery(sql, new String[] {});
//			int count = cursor.getCount();
//			if (count < 1) {
//				return result;
//			}
//			Places places = null;
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				places = new Places();
//				places.setId(cursor.getInt(0));
//				places.setPlaceName(cursor.getString(1));
//				places.setPlaceDescription(cursor.getString(2));
//				places.setRestaurantId(cursor.getInt(3));
//				places.setRevenueId(cursor.getInt(4));
//				places.setIsActive(cursor.getInt(5));
//				result.add(places);
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
//	public static Places getPlacesById(int placeId) {
//		Places places = new Places();
//		String sql = "select * from " + TableNames.Places + " where id = ?";
//		Cursor cursor = null;
//		try {
//			cursor = SQLExe.getDB().rawQuery(sql, new String[] {String.valueOf(placeId)});
//			if (cursor.moveToFirst()) {
//				places.setId(cursor.getInt(0));
//				places.setPlaceName(cursor.getString(1));
//				places.setPlaceDescription(cursor.getString(2));
//				places.setRestaurantId(cursor.getInt(3));
//				places.setRevenueId(cursor.getInt(4));
//				places.setIsActive(cursor.getInt(5));
//				return places;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return places;
//	}
//
//	public static void deletePlaces(Places places) {
//		String sql = "delete from " + TableNames.Places + " where id = ?";
//		try {
//			SQLExe.getDB().execSQL(sql, new Object[] { places.getId() });
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void deleteAllPlaces() {
//		String sql = "delete from " + TableNames.Places;
//		try {
//			SQLExe.getDB().execSQL(sql, new Object[] {});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
