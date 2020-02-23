package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.WeixinSettlement;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.List;

public class WeixinSettlementSQL {

	public static void addWeixinSettlement(WeixinSettlement weixinSettlement) {
		if (weixinSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.WeixinSettlement
					+ "(id, paymentId, paymentSettId, billNo, tradeNo, buyerEmail, createTime, updateTime, isActive)"
					+ " values (?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { weixinSettlement.getId(),
							weixinSettlement.getPaymentId(),
							weixinSettlement.getPaymentSettId(),
							weixinSettlement.getBillNo(),
							weixinSettlement.getTradeNo(),
							weixinSettlement.getBuyerEmail(),
							weixinSettlement.getCreateTime(),
							weixinSettlement.getUpdateTime(),
							weixinSettlement.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WeixinSettlement getWeixinSettlement(Integer weixinSettlementID) {
		WeixinSettlement weixinSettlement = null;
		String sql = "select * from " + TableNames.WeixinSettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { weixinSettlementID + "" });
			if (cursor.moveToFirst()) {
				weixinSettlement = new WeixinSettlement();
				weixinSettlement.setId(cursor.getInt(0));
				weixinSettlement.setPaymentId(cursor.getInt(1));
				weixinSettlement.setPaymentSettId(cursor.getInt(2));
				weixinSettlement.setBillNo(cursor.getInt(3));
				weixinSettlement.setTradeNo(cursor.getString(4));
				weixinSettlement.setBuyerEmail(cursor.getString(5));
				weixinSettlement.setCreateTime(cursor.getLong(6));
				weixinSettlement.setUpdateTime(cursor.getLong(7));
				weixinSettlement.setIsActive(cursor.getInt(8));
				return weixinSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return weixinSettlement;
	}
	
	public static List<WeixinSettlement> getWeixinSettlementByPamentId(Integer paymentId) {
		List<WeixinSettlement>  weixinSettlements =  new ArrayList<WeixinSettlement>();
		String sql = "select * from " + TableNames.WeixinSettlement
				+ " where paymentId = ?  and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId)});
			if (cursor.moveToFirst()) {
				WeixinSettlement weixinSettlement = new WeixinSettlement();
				weixinSettlement.setId(cursor.getInt(0));
				weixinSettlement.setPaymentId(cursor.getInt(1));
				weixinSettlement.setPaymentSettId(cursor.getInt(2));
				weixinSettlement.setBillNo(cursor.getInt(3));
				weixinSettlement.setTradeNo(cursor.getString(4));
				weixinSettlement.setBuyerEmail(cursor.getString(5));
				weixinSettlement.setCreateTime(cursor.getLong(6));
				weixinSettlement.setUpdateTime(cursor.getLong(7));
				weixinSettlement.setIsActive(cursor.getInt(8));
				weixinSettlements.add(weixinSettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return weixinSettlements;
	}

	public static WeixinSettlement getWeixinSettlementByPament(Integer paymentId, Integer paymentSettId) {
		WeixinSettlement weixinSettlement = null;
		String sql = "select * from " + TableNames.WeixinSettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId), String.valueOf(paymentSettId) });
			if (cursor.moveToFirst()) {
				weixinSettlement = new WeixinSettlement();
				weixinSettlement.setId(cursor.getInt(0));
				weixinSettlement.setPaymentId(cursor.getInt(1));
				weixinSettlement.setPaymentSettId(cursor.getInt(2));
				weixinSettlement.setBillNo(cursor.getInt(3));
				weixinSettlement.setTradeNo(cursor.getString(4));
				weixinSettlement.setBuyerEmail(cursor.getString(5));
				weixinSettlement.setCreateTime(cursor.getLong(6));
				weixinSettlement.setUpdateTime(cursor.getLong(7));
				weixinSettlement.setIsActive(cursor.getInt(8));
				return weixinSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return weixinSettlement;
	}


	public static ArrayList<WeixinSettlement> getAllWeixinSettlement() {
		ArrayList<WeixinSettlement> result = new ArrayList<WeixinSettlement>();
		String sql = "select * from " + TableNames.WeixinSettlement + " where isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			WeixinSettlement weixinSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				weixinSettlement = new WeixinSettlement();
				weixinSettlement.setId(cursor.getInt(0));
				weixinSettlement.setPaymentId(cursor.getInt(1));
				weixinSettlement.setPaymentSettId(cursor.getInt(2));
				weixinSettlement.setBillNo(cursor.getInt(3));
				weixinSettlement.setTradeNo(cursor.getString(4));
				weixinSettlement.setBuyerEmail(cursor.getString(5));
				weixinSettlement.setCreateTime(cursor.getLong(6));
				weixinSettlement.setUpdateTime(cursor.getLong(7));
				weixinSettlement.setIsActive(cursor.getInt(8));
				result.add(weixinSettlement);
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

	public static void deleteWeixinSettlement(WeixinSettlement weixinSettlement) {
		String sql = "delete from " + TableNames.WeixinSettlement
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { weixinSettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
