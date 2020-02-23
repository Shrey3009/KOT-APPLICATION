package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.NonChargableSettlement;
import com.alfredbase.javabean.Payment;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NonChargableSettlementSQL {

	public static void addNonChargableSettlement(
			NonChargableSettlement nonChargableSettlement) {
		if (nonChargableSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.NonChargableSettlement
					+ "(id, orderId, billNo, paymentId, paymentSettId, nameOfPerson, remarks, authorizedUserId, amount, restaurantId, revenueId, userId,  createTime, updateTime, isActive)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { nonChargableSettlement.getId(),
							nonChargableSettlement.getOrderId(),
							nonChargableSettlement.getBillNo(),
							nonChargableSettlement.getPaymentId(),
							nonChargableSettlement.getPaymentSettId(),
							nonChargableSettlement.getNameOfPerson(),
							nonChargableSettlement.getRemarks(),
							nonChargableSettlement.getAuthorizedUserId(),
							nonChargableSettlement.getAmount(),
							nonChargableSettlement.getRestaurantId(),
							nonChargableSettlement.getRevenueId(),
							nonChargableSettlement.getUserId(),
							nonChargableSettlement.getCreateTime(),
							nonChargableSettlement.getUpdateTime(),
							nonChargableSettlement.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static NonChargableSettlement getNonChargableSettlement(
			Integer nonChargableSettlementID) {
		NonChargableSettlement nonChargableSettlement = null;
		String sql = "select * from " + TableNames.NonChargableSettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { nonChargableSettlementID + "" });
			if (cursor.moveToFirst()) {
				nonChargableSettlement = new NonChargableSettlement();
				nonChargableSettlement.setId(cursor.getInt(0));
				nonChargableSettlement.setOrderId(cursor.getInt(1));
				nonChargableSettlement.setBillNo(cursor.getInt(2));
				nonChargableSettlement.setPaymentId(cursor.getInt(3));
				nonChargableSettlement.setPaymentSettId(cursor.getInt(4));
				nonChargableSettlement.setNameOfPerson(cursor.getString(5));
				nonChargableSettlement.setRemarks(cursor.getString(6));
				nonChargableSettlement.setAuthorizedUserId(cursor.getInt(7));
				nonChargableSettlement.setAmount(cursor.getString(8));
				nonChargableSettlement.setRestaurantId(cursor.getInt(9));
				nonChargableSettlement.setRevenueId(cursor.getInt(10));
				nonChargableSettlement.setUserId(cursor.getInt(11));
				nonChargableSettlement.setCreateTime(cursor.getLong(12));
				nonChargableSettlement.setUpdateTime(cursor.getLong(13));
				nonChargableSettlement.setIsActive(cursor.getInt(14));
				return nonChargableSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return nonChargableSettlement;
	}

	public static NonChargableSettlement getNonChargableSettlementByPaymentId(
			Integer paymentId, int paymentSettId) {
		NonChargableSettlement nonChargableSettlement = null;
		String sql = "select * from " + TableNames.NonChargableSettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId),String.valueOf(paymentSettId) });
			if (cursor.moveToFirst()) {
				nonChargableSettlement = new NonChargableSettlement();
				nonChargableSettlement.setId(cursor.getInt(0));
				nonChargableSettlement.setOrderId(cursor.getInt(1));
				nonChargableSettlement.setBillNo(cursor.getInt(2));
				nonChargableSettlement.setPaymentId(cursor.getInt(3));
				nonChargableSettlement.setPaymentSettId(cursor.getInt(4));
				nonChargableSettlement.setNameOfPerson(cursor.getString(5));
				nonChargableSettlement.setRemarks(cursor.getString(6));
				nonChargableSettlement.setAuthorizedUserId(cursor.getInt(7));
				nonChargableSettlement.setAmount(cursor.getString(8));
				nonChargableSettlement.setRestaurantId(cursor.getInt(9));
				nonChargableSettlement.setRevenueId(cursor.getInt(10));
				nonChargableSettlement.setUserId(cursor.getInt(11));
				nonChargableSettlement.setCreateTime(cursor.getLong(12));
				nonChargableSettlement.setUpdateTime(cursor.getLong(13));
				nonChargableSettlement.setIsActive(cursor.getInt(14));
				return nonChargableSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return nonChargableSettlement;
	}

	public static ArrayList<NonChargableSettlement> getNonChargableSettlementsByPaymentId(
			Integer paymentId) {
		ArrayList<NonChargableSettlement> nonChargableSettlements = new ArrayList<NonChargableSettlement>();
		String sql = "select * from " + TableNames.NonChargableSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentId) });
			if (cursor.moveToFirst()) {
				NonChargableSettlement nonChargableSettlement = new NonChargableSettlement();
				nonChargableSettlement.setId(cursor.getInt(0));
				nonChargableSettlement.setOrderId(cursor.getInt(1));
				nonChargableSettlement.setBillNo(cursor.getInt(2));
				nonChargableSettlement.setPaymentId(cursor.getInt(3));
				nonChargableSettlement.setPaymentSettId(cursor.getInt(4));
				nonChargableSettlement.setNameOfPerson(cursor.getString(5));
				nonChargableSettlement.setRemarks(cursor.getString(6));
				nonChargableSettlement.setAuthorizedUserId(cursor.getInt(7));
				nonChargableSettlement.setAmount(cursor.getString(8));
				nonChargableSettlement.setRestaurantId(cursor.getInt(9));
				nonChargableSettlement.setRevenueId(cursor.getInt(10));
				nonChargableSettlement.setUserId(cursor.getInt(11));
				nonChargableSettlement.setCreateTime(cursor.getLong(12));
				nonChargableSettlement.setUpdateTime(cursor.getLong(13));
				nonChargableSettlement.setIsActive(cursor.getInt(14));
				nonChargableSettlements.add(nonChargableSettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return nonChargableSettlements;
	}

	public static ArrayList<NonChargableSettlement> getAllNonChargableSettlement() {
		ArrayList<NonChargableSettlement> result = new ArrayList<NonChargableSettlement>();
		String sql = "select * from " + TableNames.NonChargableSettlement;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			NonChargableSettlement nonChargableSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				nonChargableSettlement = new NonChargableSettlement();
				nonChargableSettlement.setId(cursor.getInt(0));
				nonChargableSettlement.setOrderId(cursor.getInt(1));
				nonChargableSettlement.setBillNo(cursor.getInt(2));
				nonChargableSettlement.setPaymentId(cursor.getInt(3));
				nonChargableSettlement.setPaymentSettId(cursor.getInt(4));
				nonChargableSettlement.setNameOfPerson(cursor.getString(5));
				nonChargableSettlement.setRemarks(cursor.getString(6));
				nonChargableSettlement.setAuthorizedUserId(cursor.getInt(7));
				nonChargableSettlement.setAmount(cursor.getString(8));
				nonChargableSettlement.setRestaurantId(cursor.getInt(9));
				nonChargableSettlement.setRevenueId(cursor.getInt(10));
				nonChargableSettlement.setUserId(cursor.getInt(11));
				nonChargableSettlement.setCreateTime(cursor.getLong(12));
				nonChargableSettlement.setUpdateTime(cursor.getLong(13));
				nonChargableSettlement.setIsActive(cursor.getInt(14));
				result.add(nonChargableSettlement);
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

	public static void deleteNonChargableSettlement(
			NonChargableSettlement nonChargableSettlement) {
		String sql = "delete from " + TableNames.NonChargableSettlement
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { nonChargableSettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> getNonChargableSettlementsByTime(
			long time, long todayZero) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum ( amount ) count ( * ) from "
				+ TableNames.NonChargableSettlement
				+ "where createTime < ? and createTime > ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db =SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] { String.valueOf(time),
							String.valueOf(todayZero) });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumAmount", cursor.getString(0));
				result.put("count", String.valueOf(cursor.getInt(1)));
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
	
	
	public static void regainNonChargableSettlementByPayment(Payment payment) {
		String sql = "delete from " + TableNames.NonChargableSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		String sql1 = "update " + TableNames.NonChargableSettlement
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

}
