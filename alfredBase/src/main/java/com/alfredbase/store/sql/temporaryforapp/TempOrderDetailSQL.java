package com.alfredbase.store.sql.temporaryforapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.javabean.temporaryforapp.TempOrderDetail;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class TempOrderDetailSQL {
	public static void addTempOrderDetailList(TempOrder tempOrder, List<TempOrderDetail> tempOrderDetailList) {
		if (tempOrderDetailList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "insert into "
					+ TableNames.TempOrderDetail
					+ "(orderDetailId, appOrderId, itemId, specialInstractions, itemCount, itemPrice, itemName)"
					+ " values (?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (TempOrderDetail tempOrderDetail : tempOrderDetailList) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							tempOrderDetail.getOrderDetailId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							tempOrder.getAppOrderId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							tempOrderDetail.getItemId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							tempOrderDetail.getSpecialInstractions());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							tempOrderDetail.getItemCount());
					SQLiteStatementHelper.bindString(sqLiteStatement, 6,
							tempOrderDetail.getItemPrice());
					SQLiteStatementHelper.bindString(sqLiteStatement, 7,
							tempOrderDetail.getItemName());
					
					sqLiteStatement.executeInsert();
				}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	
	public static List<TempOrderDetail> getTempOrderDetailByAppOrderId(int appOrderId){
		String sql = "select * from " + TableNames.TempOrderDetail + " where appOrderId = ?";
		Cursor cursor = null;
		List<TempOrderDetail> result = new ArrayList<TempOrderDetail>();
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {appOrderId + ""});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				TempOrderDetail tempOrderDetail = new TempOrderDetail();
				tempOrderDetail.setId(cursor.getInt(0));
				tempOrderDetail.setOrderDetailId(cursor.getInt(1));
				tempOrderDetail.setAppOrderId(cursor.getInt(2));
				tempOrderDetail.setItemId(cursor.getInt(3));
				tempOrderDetail.setSpecialInstractions(cursor.getString(4));
				tempOrderDetail.setItemCount(cursor.getInt(5));
				tempOrderDetail.setItemPrice(cursor.getString(6));
				tempOrderDetail.setItemName(cursor.getString(7));
				result.add(tempOrderDetail);
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