package com.alfredbase.store.sql;

import android.database.Cursor;

import com.alfredbase.javabean.SettingData;
import com.alfredbase.javabean.User;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

public class SettingDataSQL {
	public static void add(SettingData settingData) {
		if (settingData == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.SettingData
					+ "(id, logoUrl, logoString)"
					+ " values (?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { settingData.getId(), settingData.getLogoUrl(), settingData.getLogoString() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SettingData getSettingData(int settingDataId){
		SettingData settingData = new SettingData();
		String sql = "select * from " + TableNames.SettingData + " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[] {String.valueOf(settingDataId)});
			if (cursor.moveToFirst()) {
				settingData.setId(cursor.getInt(0));
				settingData.setLogoUrl(cursor.getString(1));
				settingData.setLogoString(cursor.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return settingData;
	}
	
	public static SettingData getSettingDataByUrl(String logoUrl){
		SettingData settingData = new SettingData();
		String sql = "select * from " + TableNames.SettingData + " where logoUrl = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[] {String.valueOf(logoUrl)});
			if (cursor.moveToFirst()) {
				settingData.setId(cursor.getInt(0));
				settingData.setLogoUrl(cursor.getString(1));
				settingData.setLogoString(cursor.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return settingData;
	}
	
	public static void deleteUser(User user) {
		String sql = "delete from " + TableNames.SettingData + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { user.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
