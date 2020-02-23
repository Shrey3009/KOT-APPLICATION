package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.VoidSettlement;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoidSettlementSQL {

	public static void addVoidSettlement(VoidSettlement voidSettlement) {
		if (voidSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.VoidSettlement
					+ " (id, orderId, billNo, paymentId, paymentSettId, reason, authorizedUserId, amount, restaurantId, revenueId,"
					+ " userId, createTime, updateTime, isActive, type)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] {
							voidSettlement.getId(),
							voidSettlement.getOrderId(),
							voidSettlement.getBillNo(),
							voidSettlement.getPaymentId(),
							voidSettlement.getPaymentSettId(),
							voidSettlement.getReason(),
							voidSettlement.getAuthorizedUserId(),
							voidSettlement.getAmount(),
							voidSettlement.getRestaurantId(),
							voidSettlement.getRevenueId(),
							voidSettlement.getUserId(),
							voidSettlement.getCreateTime(),
							voidSettlement.getUpdateTime(),
							voidSettlement.getIsActive(),
							voidSettlement.getType()
							});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static VoidSettlement getVoidSettlement(Integer voidSettlementID) {
		VoidSettlement voidSettlement = null;
		String sql = "select * from " + TableNames.VoidSettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { voidSettlementID + "" });
			if (cursor.moveToFirst()) {
				voidSettlement = new VoidSettlement();
				voidSettlement.setId(cursor.getInt(0));
				voidSettlement.setOrderId(cursor.getInt(1));
				voidSettlement.setBillNo(2);
				voidSettlement.setPaymentId(cursor.getInt(3));
				voidSettlement.setPaymentSettId(cursor.getInt(4));
				voidSettlement.setReason(cursor.getString(5));
				voidSettlement.setAuthorizedUserId(cursor.getInt(6));
				voidSettlement.setAmount(cursor.getString(7));
				voidSettlement.setRestaurantId(cursor.getInt(8));
				voidSettlement.setRevenueId(cursor.getInt(9));
				voidSettlement.setUserId(cursor.getInt(10));
				voidSettlement.setCreateTime(cursor.getLong(11));
				voidSettlement.setUpdateTime(cursor.getLong(12));
				voidSettlement.setIsActive(cursor.getInt(13));
				voidSettlement.setType(cursor.getInt(14));
				return voidSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return voidSettlement;
	}

	public static VoidSettlement getVoidSettlementByPament(Integer paymentId, Integer paymentSettId) {
		VoidSettlement voidSettlement = null;
		String sql = "select * from " + TableNames.VoidSettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentId), String.valueOf(paymentSettId) });
			if (cursor.moveToFirst()) {
				voidSettlement = new VoidSettlement();
				voidSettlement.setId(cursor.getInt(0));
				voidSettlement.setOrderId(cursor.getInt(1));
				voidSettlement.setBillNo(2);
				voidSettlement.setPaymentId(cursor.getInt(3));
				voidSettlement.setPaymentSettId(cursor.getInt(4));
				voidSettlement.setReason(cursor.getString(5));
				voidSettlement.setAuthorizedUserId(cursor.getInt(6));
				voidSettlement.setAmount(cursor.getString(7));
				voidSettlement.setRestaurantId(cursor.getInt(8));
				voidSettlement.setRevenueId(cursor.getInt(9));
				voidSettlement.setUserId(cursor.getInt(10));
				voidSettlement.setCreateTime(cursor.getLong(11));
				voidSettlement.setUpdateTime(cursor.getLong(12));
				voidSettlement.setIsActive(cursor.getInt(13));
				voidSettlement.setType(cursor.getInt(14));
				return voidSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return voidSettlement;
	}

	public static ArrayList<VoidSettlement> getVoidSettlementsByPamentId(
			Integer paymentId) {
		ArrayList<VoidSettlement> voidSettlements = new ArrayList<VoidSettlement>();
		String sql = "select * from " + TableNames.VoidSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentId) });
			if (cursor.moveToFirst()) {
				VoidSettlement voidSettlement = new VoidSettlement();
				voidSettlement.setId(cursor.getInt(0));
				voidSettlement.setOrderId(cursor.getInt(1));
				voidSettlement.setBillNo(2);
				voidSettlement.setPaymentId(cursor.getInt(3));
				voidSettlement.setPaymentSettId(cursor.getInt(4));
				voidSettlement.setReason(cursor.getString(5));
				voidSettlement.setAuthorizedUserId(cursor.getInt(6));
				voidSettlement.setAmount(cursor.getString(7));
				voidSettlement.setRestaurantId(cursor.getInt(8));
				voidSettlement.setRevenueId(cursor.getInt(9));
				voidSettlement.setUserId(cursor.getInt(10));
				voidSettlement.setCreateTime(cursor.getLong(11));
				voidSettlement.setUpdateTime(cursor.getLong(12));
				voidSettlement.setIsActive(cursor.getInt(13));
				voidSettlement.setType(cursor.getInt(14));
				voidSettlements.add(voidSettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return voidSettlements;
	}

	public static ArrayList<VoidSettlement> getAllVoidSettlement() {
		ArrayList<VoidSettlement> result = new ArrayList<VoidSettlement>();
		String sql = "select * from " + TableNames.VoidSettlement;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			VoidSettlement voidSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				voidSettlement = new VoidSettlement();
				voidSettlement.setId(cursor.getInt(0));
				voidSettlement.setOrderId(cursor.getInt(1));
				voidSettlement.setBillNo(2);
				voidSettlement.setPaymentId(cursor.getInt(3));
				voidSettlement.setPaymentSettId(cursor.getInt(4));
				voidSettlement.setReason(cursor.getString(5));
				voidSettlement.setAuthorizedUserId(cursor.getInt(6));
				voidSettlement.setAmount(cursor.getString(7));
				voidSettlement.setRestaurantId(cursor.getInt(8));
				voidSettlement.setRevenueId(cursor.getInt(9));
				voidSettlement.setUserId(cursor.getInt(10));
				voidSettlement.setCreateTime(cursor.getLong(11));
				voidSettlement.setUpdateTime(cursor.getLong(12));
				voidSettlement.setType(cursor.getInt(14));
				result.add(voidSettlement);
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

	public static Map<String, Object> getVoidSettlementsByNowTime(long time,
			long todayZero) {
		Map<String, Object> result = new HashMap<String, Object>();
		String sql = "select sum(amount) count(*) from "
				+ TableNames.VoidSettlement
				+ " where createTime < ? and paidDate > ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] { String.valueOf(time),
							String.valueOf(todayZero) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {

				result.put("sumAmount", cursor.getString(0));
				result.put("count", cursor.getInt(1));
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
	
	public static void regainVoidSettlementByPayment(Payment payment) {
		String sql = "delete from " + TableNames.VoidSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		String sql1 = "update " + TableNames.VoidSettlement
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

	public static void deleteVoidSettlement(VoidSettlement voidSettlement) {
		String sql = "delete from " + TableNames.VoidSettlement
				+ " where id = ?";
		try {
			SQLExe.getDB()
					.execSQL(sql, new Object[] { voidSettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
