package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.AlipaySettlement;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.List;

public class AlipaySettlementSQL {

	public static void addAlipaySettlement(AlipaySettlement alipaySettlement) {
		if (alipaySettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.AlipaySettlement
					+ "(id, paymentId, paymentSettId, billNo, tradeNo, buyerEmail, createTime, updateTime, isActive)"
					+ " values (?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { alipaySettlement.getId(),
							alipaySettlement.getPaymentId(),
							alipaySettlement.getPaymentSettId(),
							alipaySettlement.getBillNo(),
							alipaySettlement.getTradeNo(),
							alipaySettlement.getBuyerEmail(),
							alipaySettlement.getCreateTime(),
							alipaySettlement.getUpdateTime(),
							alipaySettlement.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AlipaySettlement getAlipaySettlement(Integer alipaySettlementID) {
		AlipaySettlement alipaySettlement = null;
		String sql = "select * from " + TableNames.AlipaySettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { alipaySettlementID + "" });
			if (cursor.moveToFirst()) {
				alipaySettlement = new AlipaySettlement();
				alipaySettlement.setId(cursor.getInt(0));
				alipaySettlement.setPaymentId(cursor.getInt(1));
				alipaySettlement.setPaymentSettId(cursor.getInt(2));
				alipaySettlement.setBillNo(cursor.getInt(3));
				alipaySettlement.setTradeNo(cursor.getString(4));
				alipaySettlement.setBuyerEmail(cursor.getString(5));
				alipaySettlement.setCreateTime(cursor.getLong(6));
				alipaySettlement.setUpdateTime(cursor.getLong(7));
				alipaySettlement.setIsActive(cursor.getInt(8));
				return alipaySettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return alipaySettlement;
	}
	
	public static List<AlipaySettlement> getAlipaySettlementByPamentId(Integer paymentId) {
		List<AlipaySettlement>  alipaySettlements =  new ArrayList<AlipaySettlement>();
		String sql = "select * from " + TableNames.AlipaySettlement
				+ " where paymentId = ?  and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId)});
			if (cursor.moveToFirst()) {
				AlipaySettlement alipaySettlement = new AlipaySettlement();
				alipaySettlement.setId(cursor.getInt(0));
				alipaySettlement.setPaymentId(cursor.getInt(1));
				alipaySettlement.setPaymentSettId(cursor.getInt(2));
				alipaySettlement.setBillNo(cursor.getInt(3));
				alipaySettlement.setTradeNo(cursor.getString(4));
				alipaySettlement.setBuyerEmail(cursor.getString(5));
				alipaySettlement.setCreateTime(cursor.getLong(6));
				alipaySettlement.setUpdateTime(cursor.getLong(7));
				alipaySettlement.setIsActive(cursor.getInt(8));
				alipaySettlements.add(alipaySettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return alipaySettlements;
	}

	public static AlipaySettlement getAlipaySettlementByPament(Integer paymentId, Integer paymentSettId) {
		AlipaySettlement alipaySettlement = null;
		String sql = "select * from " + TableNames.AlipaySettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId), String.valueOf(paymentSettId) });
			if (cursor.moveToFirst()) {
				alipaySettlement = new AlipaySettlement();
				alipaySettlement.setId(cursor.getInt(0));
				alipaySettlement.setPaymentId(cursor.getInt(1));
				alipaySettlement.setPaymentSettId(cursor.getInt(2));
				alipaySettlement.setBillNo(cursor.getInt(3));
				alipaySettlement.setTradeNo(cursor.getString(4));
				alipaySettlement.setBuyerEmail(cursor.getString(5));
				alipaySettlement.setCreateTime(cursor.getLong(6));
				alipaySettlement.setUpdateTime(cursor.getLong(7));
				alipaySettlement.setIsActive(cursor.getInt(8));
				return alipaySettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return alipaySettlement;
	}


	public static ArrayList<AlipaySettlement> getAllAlipaySettlement() {
		ArrayList<AlipaySettlement> result = new ArrayList<AlipaySettlement>();
		String sql = "select * from " + TableNames.AlipaySettlement + " where isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			AlipaySettlement alipaySettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				alipaySettlement = new AlipaySettlement();
				alipaySettlement.setId(cursor.getInt(0));
				alipaySettlement.setPaymentId(cursor.getInt(1));
				alipaySettlement.setPaymentSettId(cursor.getInt(2));
				alipaySettlement.setBillNo(cursor.getInt(3));
				alipaySettlement.setTradeNo(cursor.getString(4));
				alipaySettlement.setBuyerEmail(cursor.getString(5));
				alipaySettlement.setCreateTime(cursor.getLong(6));
				alipaySettlement.setUpdateTime(cursor.getLong(7));
				alipaySettlement.setIsActive(cursor.getInt(8));
				result.add(alipaySettlement);
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

	public static void deleteAlipaySettlement(AlipaySettlement alipaySettlement) {
		String sql = "delete from " + TableNames.AlipaySettlement
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { alipaySettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
