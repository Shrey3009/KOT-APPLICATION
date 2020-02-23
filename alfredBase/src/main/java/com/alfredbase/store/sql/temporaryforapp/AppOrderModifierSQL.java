package com.alfredbase.store.sql.temporaryforapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.temporaryforapp.AppOrderModifier;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class AppOrderModifierSQL {
	/*
		private Integer id; 	//'主键id',
	private Integer orderId; 	//'主订单id',
	private Integer orderDetailId; 	//'订单详情id',
	private Integer custId; 	//'顾客id',
	private Integer itemId; 	//'菜单id',
	private Integer modifierId; 	//'配料的id',
	private String modifierName; 	//'配料名称',
	private Integer modifierNum; 	//'配料的数量',
	private Integer status; 	//'配料订单状态(-1删除，0正常)',
	private String modifierPrice; 	//'配料金额',
	private long createTime; 	//'创建时间',
	private long updateTime; 	//'更新时间',
	 */

	public static void addAppOrderModifier(AppOrderModifier appOrderModifier) {
		if (appOrderModifier == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.AppOrderModifier
					+ "(id, orderId, orderDetailId, custId, itemId, modifierId, modifierName, modifierNum, status, modifierPrice,"
					+ " createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(sql,
					new Object[]{appOrderModifier.getId(), appOrderModifier.getOrderId(),
							appOrderModifier.getOrderDetailId(), appOrderModifier.getCustId(),
							appOrderModifier.getItemId(), appOrderModifier.getModifierId(),
							appOrderModifier.getModifierName(), appOrderModifier.getModifierNum(),
							appOrderModifier.getStatus(), appOrderModifier.getModifierPrice(),
							appOrderModifier.getCreateTime(), appOrderModifier.getUpdateTime()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addAppOrderModifierList(List<AppOrderModifier> appOrderModifierList) {
		if (appOrderModifierList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.AppOrderModifier
					+ "(id, orderId, orderDetailId, custId, itemId, modifierId, modifierName, modifierNum, status, modifierPrice,"
					+ " createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (AppOrderModifier appOrderModifier : appOrderModifierList) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							appOrderModifier.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							appOrderModifier.getOrderId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							appOrderModifier.getOrderDetailId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							appOrderModifier.getCustId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							appOrderModifier.getItemId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							appOrderModifier.getModifierId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 7,
							appOrderModifier.getModifierName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							appOrderModifier.getModifierNum());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							appOrderModifier.getStatus());
					SQLiteStatementHelper.bindString(sqLiteStatement, 10,
							appOrderModifier.getModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
							appOrderModifier.getCreateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							appOrderModifier.getUpdateTime());

					sqLiteStatement.executeInsert();
				}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	public static List<AppOrderModifier> getAppOrderModifierByOrderDetailId(int orderDetailId){
		String sql = "select * from " + TableNames.AppOrderModifier + " where orderDetailId = ?";
		Cursor cursor = null;
		List<AppOrderModifier> result = new ArrayList<AppOrderModifier>();
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {orderDetailId + ""});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				AppOrderModifier appOrderModifier = new AppOrderModifier();
				appOrderModifier.setId(cursor.getInt(0));
				appOrderModifier.setOrderId(cursor.getInt(1));
				appOrderModifier.setOrderDetailId(cursor.getInt(2));
				appOrderModifier.setCustId(cursor.getInt(3));
				appOrderModifier.setItemId(cursor.getInt(4));
				appOrderModifier.setModifierId(cursor.getInt(5));
				appOrderModifier.setModifierName(cursor.getString(6));
				appOrderModifier.setModifierNum(cursor.getInt(7));
				appOrderModifier.setStatus(cursor.getInt(8));
				appOrderModifier.setModifierPrice(cursor.getString(9));
				appOrderModifier.setCreateTime(cursor.getLong(10));
				appOrderModifier.setUpdateTime(cursor.getLong(11));
				result.add(appOrderModifier);
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

	public static List<AppOrderModifier> getAppOrderModifierByAppOrderId(int appOrderId){
		String sql = "select * from " + TableNames.AppOrderModifier + " where orderId = ?";
		Cursor cursor = null;
		List<AppOrderModifier> result = new ArrayList<AppOrderModifier>();
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {appOrderId + ""});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				AppOrderModifier appOrderModifier = new AppOrderModifier();
				appOrderModifier.setId(cursor.getInt(0));
				appOrderModifier.setOrderId(cursor.getInt(1));
				appOrderModifier.setOrderDetailId(cursor.getInt(2));
				appOrderModifier.setCustId(cursor.getInt(3));
				appOrderModifier.setItemId(cursor.getInt(4));
				appOrderModifier.setModifierId(cursor.getInt(5));
				appOrderModifier.setModifierName(cursor.getString(6));
				appOrderModifier.setModifierNum(cursor.getInt(7));
				appOrderModifier.setStatus(cursor.getInt(8));
				appOrderModifier.setModifierPrice(cursor.getString(9));
				appOrderModifier.setCreateTime(cursor.getLong(10));
				appOrderModifier.setUpdateTime(cursor.getLong(11));
				result.add(appOrderModifier);
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
