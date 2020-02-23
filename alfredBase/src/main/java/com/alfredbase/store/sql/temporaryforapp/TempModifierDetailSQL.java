package com.alfredbase.store.sql.temporaryforapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.temporaryforapp.TempModifierDetail;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class TempModifierDetailSQL {
	public static void addTempModifierDetailList(List<TempModifierDetail> tempModifierDetailList) {
		if (tempModifierDetailList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "insert into "
					+ TableNames.TempModifierDetail
					+ "(orderDetailId, itemId, modifierName, modifierPrice, modifierId)"
					+ " values (?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (TempModifierDetail tempModifierDetail : tempModifierDetailList) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							tempModifierDetail.getOrderDetailId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							tempModifierDetail.getItemId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 3,
							tempModifierDetail.getModifierName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							tempModifierDetail.getModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							tempModifierDetail.getModifierId());
					
					sqLiteStatement.executeInsert();
				}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	public static List<TempModifierDetail> getTempOrderDetailByOrderDetailId(int orderDetailId){
		String sql = "select * from " + TableNames.TempModifierDetail + " where orderDetailId = ?";
		Cursor cursor = null;
		List<TempModifierDetail> result = new ArrayList<TempModifierDetail>();
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {orderDetailId + ""});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				TempModifierDetail tempModifierDetail = new TempModifierDetail();
				tempModifierDetail.setId(cursor.getInt(0));
				tempModifierDetail.setOrderDetailId(cursor.getInt(1));
				tempModifierDetail.setItemId(cursor.getInt(2));
				tempModifierDetail.setModifierName(cursor.getString(3));
				tempModifierDetail.setModifierPrice(cursor.getString(4));
				tempModifierDetail.setModifierId(cursor.getInt(5));
				result.add(tempModifierDetail);
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
}
