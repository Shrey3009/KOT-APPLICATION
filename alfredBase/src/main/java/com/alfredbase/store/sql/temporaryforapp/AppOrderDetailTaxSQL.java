package com.alfredbase.store.sql.temporaryforapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.temporaryforapp.AppOrderDetailTax;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class AppOrderDetailTaxSQL {
	/*
	    private Integer id;     //'主键id',
    private Integer orderId;    //'主订单id',
    private Integer orderDetailId;  //'订单详情id',
    private Integer taxId;  //'收税id',
    private String taxName;     //'税收名称',
    private String taxPercentage;   //'税收比例',
    private String taxPrice;    //'税收金额',
    private Integer taxType;    //'税收类型(0消费税，1服务税)',
    private long createTime;    //'创建时间',
    private long updateTime;    //'更新时间',
	 */

	public static void addAppOrderDetailTax(AppOrderDetailTax appOrderDetailTax){

		if (appOrderDetailTax == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			String sql = "replace into "
					+ TableNames.AppOrderDetailTax
					+ " (id, orderId, orderDetailId, taxId, taxName, taxPercentage, taxPrice, taxType, createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(sql,
					new Object[]{appOrderDetailTax.getId(), appOrderDetailTax.getOrderId(),
							appOrderDetailTax.getOrderDetailId(), appOrderDetailTax.getTaxId(),
							appOrderDetailTax.getTaxName(), appOrderDetailTax.getTaxPercentage(),
							appOrderDetailTax.getTaxPrice(), appOrderDetailTax.getTaxType(),
							appOrderDetailTax.getCreateTime(), appOrderDetailTax.getUpdateTime()
								});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addAppOrderDetailTaxList(List<AppOrderDetailTax> appOrderDetailTaxList){

		if (appOrderDetailTaxList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.AppOrderDetailTax
					+ " (id, orderId, orderDetailId, taxId, taxName, taxPercentage, taxPrice, taxType, createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
			for (AppOrderDetailTax appOrderDetailTax : appOrderDetailTaxList) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
						appOrderDetailTax.getId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
						appOrderDetailTax.getOrderId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
						appOrderDetailTax.getOrderDetailId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
						appOrderDetailTax.getTaxId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 5,
						appOrderDetailTax.getTaxName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 6,
						appOrderDetailTax.getTaxPercentage());
				SQLiteStatementHelper.bindString(sqLiteStatement, 7,
						appOrderDetailTax.getTaxPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
						appOrderDetailTax.getTaxType());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
						appOrderDetailTax.getCreateTime());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
						appOrderDetailTax.getUpdateTime());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static String getAppOrderDetailTaxSumByAppOrderDetailId(int orderDetailId){
		String sql = "select sum(taxPrice) from " + TableNames.AppOrderDetailTax + " where orderDetailId = ?";
		Cursor cursor = null;
		String result = "0.00";
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {orderDetailId + ""});
			if (cursor.moveToFirst()) {
				result = BH.getBD(cursor.getString(0)).toString();
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

	
	public static List<AppOrderDetailTax> getAppOrderDetailTaxByAppOrderId(int appOrderId){
		String sql = "select * from " + TableNames.AppOrderDetailTax + " where orderId = ?";
		Cursor cursor = null;
		List<AppOrderDetailTax> result = new ArrayList<AppOrderDetailTax>();
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {appOrderId + ""});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				AppOrderDetailTax appOrderDetailTax = new AppOrderDetailTax();
				appOrderDetailTax.setId(cursor.getInt(0));
				appOrderDetailTax.setOrderId(cursor.getInt(1));
				appOrderDetailTax.setOrderDetailId(cursor.getInt(2));
				appOrderDetailTax.setTaxId(cursor.getInt(3));
				appOrderDetailTax.setTaxName(cursor.getString(4));
				appOrderDetailTax.setTaxPercentage(cursor.getString(5));
				appOrderDetailTax.setTaxPrice(cursor.getString(6));
				appOrderDetailTax.setTaxType(cursor.getInt(7));
				appOrderDetailTax.setCreateTime(cursor.getLong(8));
				appOrderDetailTax.setUpdateTime(cursor.getLong(9));
				result.add(appOrderDetailTax);
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
