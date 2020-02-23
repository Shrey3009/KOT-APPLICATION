package com.alfredbase.store.sql.temporaryforapp;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

public class TempOrderSQL {
	public static void addTempOrder(TempOrder tempOrder){

		if (tempOrder == null) {
			return;
		}
		try {
			String sql = "insert into "
					+ TableNames.TempOrder
					+ " (appOrderId, total, custId, placeOrderTime, status, sourceType, paied)"
					+ " values (?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { tempOrder.getAppOrderId(),
							tempOrder.getTotal(),
							tempOrder.getCustId(),
							tempOrder.getPlaceOrderTime(),
							tempOrder.getStatus(),
							tempOrder.getSourceType(),
							tempOrder.getPaied()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateTempOrder(TempOrder tempOrder){

		if (tempOrder == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.TempOrder
					+ " (id, appOrderId, total, custId, placeOrderTime, status, sourceType, paied)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { tempOrder.getId(),
							tempOrder.getAppOrderId(),
							tempOrder.getTotal(),
							tempOrder.getCustId(),
							tempOrder.getPlaceOrderTime(),
							tempOrder.getStatus(),
							tempOrder.getSourceType(),
							tempOrder.getPaied()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static TempOrder getTempOrderByAppOrderId(int appOrderId){
		String sql = "select * from " + TableNames.TempOrder + " where appOrderId = ?";
		Cursor cursor = null;
		TempOrder result = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {appOrderId + ""});
			if (cursor.moveToFirst()) {
				result = new TempOrder();
				result.setId(cursor.getInt(0));
				result.setAppOrderId(cursor.getInt(1));
				result.setTotal(cursor.getString(2));
				result.setCustId(cursor.getInt(3));
				result.setPlaceOrderTime(cursor.getLong(4));
				result.setStatus(cursor.getInt(5));
				result.setSourceType(cursor.getInt(6));
				result.setPaied(cursor.getInt(7));
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
	
	
	public static List<TempOrder> getAllTempOrder(){
		String sql = "select * from " + TableNames.TempOrder +" order by status asc, placeOrderTime desc";
		Cursor cursor = null;
		List<TempOrder> result = new ArrayList<TempOrder>();
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			TempOrder tempOrder = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				tempOrder = new TempOrder();
				tempOrder.setId(cursor.getInt(0));
				tempOrder.setAppOrderId(cursor.getInt(1));
				tempOrder.setTotal(cursor.getString(2));
				tempOrder.setCustId(cursor.getInt(3));
				tempOrder.setPlaceOrderTime(cursor.getLong(4));
				tempOrder.setStatus(cursor.getInt(5));
				tempOrder.setSourceType(cursor.getInt(6));
				tempOrder.setPaied(cursor.getInt(7));
				result.add(tempOrder);
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
	
	public static void deleteTempOrder(){
		
	}
}
