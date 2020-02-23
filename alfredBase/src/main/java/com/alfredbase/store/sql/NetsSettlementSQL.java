package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.NetsSettlement;
import com.alfredbase.javabean.Payment;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;

public class NetsSettlementSQL {

	public static void addNetsSettlement(NetsSettlement netsSettlement) {
		if (netsSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.NetsSettlement
					+ "(id, referenceNo, paymentId, paymentSettId, billNo, cashAmount, createTime, updateTime, isActive)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { netsSettlement.getId(),
							netsSettlement.getReferenceNo(),
							netsSettlement.getPaymentId(),
							netsSettlement.getPaymentSettId(),
							netsSettlement.getBillNo(),
							netsSettlement.getCashAmount(),
							netsSettlement.getCreateTime(),
							netsSettlement.getUpdateTime(),
							netsSettlement.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static NetsSettlement getNetsSettlement(Integer netsSettlementID) {
		NetsSettlement netsSettlement = null;
		String sql = "select * from " + TableNames.NetsSettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { netsSettlementID + "" });
			if (cursor.moveToFirst()) {
				netsSettlement = new NetsSettlement();
				netsSettlement.setId(cursor.getInt(0));
				netsSettlement.setReferenceNo(cursor.getInt(1));
				netsSettlement.setPaymentId(cursor.getInt(2));
				netsSettlement.setPaymentSettId(cursor.getInt(3));
				netsSettlement.setBillNo(cursor.getInt(4));
				netsSettlement.setCashAmount(cursor.getString(5));
				netsSettlement.setCreateTime(cursor.getLong(6));
				netsSettlement.setUpdateTime(cursor.getLong(7));
				netsSettlement.setIsActive(cursor.getInt(8));
				return netsSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return netsSettlement;
	}

	public static NetsSettlement getNetsSettlementByPament(Integer paymentId, Integer paymentSettId) {
		NetsSettlement netsSettlement = null;
		String sql = "select * from " + TableNames.NetsSettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId), String.valueOf(paymentSettId) });
			if (cursor.moveToFirst()) {
				netsSettlement = new NetsSettlement();
				netsSettlement.setId(cursor.getInt(0));
				netsSettlement.setReferenceNo(cursor.getInt(1));
				netsSettlement.setPaymentId(cursor.getInt(2));
				netsSettlement.setPaymentSettId(cursor.getInt(3));
				netsSettlement.setBillNo(cursor.getInt(4));
				netsSettlement.setCashAmount(cursor.getString(5));
				netsSettlement.setCreateTime(cursor.getLong(6));
				netsSettlement.setUpdateTime(cursor.getLong(7));
				netsSettlement.setIsActive(cursor.getInt(8));
				return netsSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return netsSettlement;
	}

	public static ArrayList<NetsSettlement> getNetsSettlementsByPamentId(
			Integer paymentId) {
		ArrayList<NetsSettlement> netsSettlements = new ArrayList<NetsSettlement>();

		String sql = "select * from " + TableNames.NetsSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentId) });
			if (cursor.moveToFirst()) {
				NetsSettlement netsSettlement = new NetsSettlement();
				netsSettlement.setId(cursor.getInt(0));
				netsSettlement.setReferenceNo(cursor.getInt(1));
				netsSettlement.setPaymentId(cursor.getInt(2));
				netsSettlement.setPaymentSettId(cursor.getInt(3));
				netsSettlement.setBillNo(cursor.getInt(4));
				netsSettlement.setCashAmount(cursor.getString(5));
				netsSettlement.setCreateTime(cursor.getLong(6));
				netsSettlement.setUpdateTime(cursor.getLong(7));
				netsSettlement.setIsActive(cursor.getInt(8));
				netsSettlements.add(netsSettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return netsSettlements;
	}

	public static ArrayList<NetsSettlement> getAllNetsSettlement() {
		ArrayList<NetsSettlement> result = new ArrayList<NetsSettlement>();
		String sql = "select * from " + TableNames.NetsSettlement;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			NetsSettlement netsSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				netsSettlement = new NetsSettlement();
				netsSettlement.setId(cursor.getInt(0));
				netsSettlement.setReferenceNo(cursor.getInt(1));
				netsSettlement.setPaymentId(cursor.getInt(2));
				netsSettlement.setPaymentSettId(cursor.getInt(3));
				netsSettlement.setBillNo(cursor.getInt(4));
				netsSettlement.setCashAmount(cursor.getString(5));
				netsSettlement.setCreateTime(cursor.getLong(6));
				netsSettlement.setUpdateTime(cursor.getLong(7));
				result.add(netsSettlement);
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

	public static ArrayList<NetsSettlement> getNetsSettlementsByNowTime() {
		ArrayList<NetsSettlement> result = new ArrayList<NetsSettlement>();
		String sql = "select * from "
				+ TableNames.NetsSettlement
				+ " where createTime < datetime('now','localtime') and paidDate > date('now','start of day') and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			NetsSettlement netsSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				netsSettlement = new NetsSettlement();
				netsSettlement.setId(cursor.getInt(0));
				netsSettlement.setReferenceNo(cursor.getInt(1));
				netsSettlement.setPaymentId(cursor.getInt(2));
				netsSettlement.setPaymentSettId(cursor.getInt(3));
				netsSettlement.setBillNo(cursor.getInt(4));
				netsSettlement.setCashAmount(cursor.getString(5));
				netsSettlement.setCreateTime(cursor.getLong(6));
				netsSettlement.setUpdateTime(cursor.getLong(7));
				netsSettlement.setIsActive(cursor.getInt(8));
				result.add(netsSettlement);
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
	
	public static void regainNetsSettlementByPayment(Payment payment) {
		String sql = "delete from " + TableNames.NetsSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		String sql1 = "update " + TableNames.NetsSettlement
				+ " set isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE
				+ " where paymentId = ? and isActive = "
				+ ParamConst.PAYMENT_SETT_IS_NO_ACTIVE;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql1, new Object[] { payment.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void deleteNetsSettlement(NetsSettlement netsSettlement) {
		String sql = "delete from " + TableNames.NetsSettlement
				+ " where id = ?";
		try {
			SQLExe.getDB()
					.execSQL(sql, new Object[] { netsSettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
