package com.alfredbase.store.sql;

import android.database.Cursor;

import com.alfredbase.javabean.Restaurant;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

public class RestaurantSQL {

	public static void addRestaurant(Restaurant restaurant) {
		if (restaurant == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.Restaurant
					+ " (id,companyId,restaurantName,type,status,description,email,"
					+ "address1,address2,telNo,country,state,city,postalCode,createTime," 
					+ "updateTime,website, addressPrint, logoUrl, qrPayment, restaurantPrint,options,footerOptions)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB()
					.execSQL(
							sql,
							new Object[] { restaurant.getId(),
									restaurant.getCompanyId(),
									restaurant.getRestaurantName(),
									restaurant.getType(),
									restaurant.getStatus(),
									restaurant.getDescription(),
									restaurant.getEmail(),
									restaurant.getAddress1(),
									restaurant.getAddress2(),
									restaurant.getTelNo(),
									restaurant.getCountry(),
									restaurant.getState(),
									restaurant.getCity(),
									restaurant.getPostalCode(),
									restaurant.getCreateTime(),
									restaurant.getUpdateTime(),
									restaurant.getWebsite(),
									restaurant.getAddressPrint(),
									restaurant.getLogoUrl(),
									restaurant.getQrPayment(),
									restaurant.getRestaurantPrint(),
									restaurant.getOptions(),
									restaurant.getFooterOptions()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Restaurant getRestaurant() {
		Restaurant result = null;
		String sql = "select * from " + TableNames.Restaurant;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = new Restaurant();
				result.setId(cursor.getInt(0));
				result.setCompanyId(cursor.getInt(1));
				result.setRestaurantName(cursor.getString(2));
				result.setType(cursor.getInt(3));
				result.setStatus(cursor.getInt(4));
				result.setDescription(cursor.getString(5));
				result.setEmail(cursor.getString(6));
				result.setAddress1(cursor.getString(7));
				result.setAddress2(cursor.getString(8));
				result.setTelNo(cursor.getString(9));
				result.setCountry(cursor.getString(10));
				result.setState(cursor.getString(11));
				result.setCity(cursor.getString(12));
				result.setPostalCode(cursor.getString(13));
				result.setCreateTime(cursor.getLong(14));
				result.setUpdateTime(cursor.getLong(15));
				result.setWebsite(cursor.getString(16));
				result.setAddressPrint(cursor.getString(17));
				result.setLogoUrl(cursor.getString(18));
				result.setQrPayment(cursor.getInt(19));
				result.setRestaurantPrint(cursor.getString(20));
				result.setOptions(cursor.getString(21));
				result.setFooterOptions(cursor.getString(22));				
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

	public static void deleteRestaurant(Restaurant restaurant) {
		String sql = "delete from " + TableNames.Restaurant + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { restaurant.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllRestaurant() {
		String sql = "delete from " + TableNames.Restaurant;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
