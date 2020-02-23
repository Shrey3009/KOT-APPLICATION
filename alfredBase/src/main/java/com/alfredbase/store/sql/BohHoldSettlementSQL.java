package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.BohHoldSettlement;
import com.alfredbase.javabean.Payment;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;

public class BohHoldSettlementSQL {

	public static void addBohHoldSettlement(BohHoldSettlement bohHoldSettlement) {
		if (bohHoldSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.BohHoldSettlement
					+ "(id, restaurantId, revenueId, orderId, paymentId, paymentSettId, billNo, nameOfPerson, phone, remarks, authorizedUserId, amount, status, paymentType, paidDate, daysDue, isActive)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { bohHoldSettlement.getId(),
							bohHoldSettlement.getRestaurantId(),
							bohHoldSettlement.getRevenueId(),
							bohHoldSettlement.getOrderId(),
							bohHoldSettlement.getPaymentId(),
							bohHoldSettlement.getPaymentSettId(),
							bohHoldSettlement.getBillNo(),
							bohHoldSettlement.getNameOfPerson(),
							bohHoldSettlement.getPhone(),
							bohHoldSettlement.getRemarks(),
							bohHoldSettlement.getAuthorizedUserId(),
							bohHoldSettlement.getAmount(),
							bohHoldSettlement.getStatus(),
							bohHoldSettlement.getPaymentType(),
							bohHoldSettlement.getPaidDate(),
							bohHoldSettlement.getDaysDue(),
							bohHoldSettlement.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BohHoldSettlement getBohHoldSettlement(
			Integer bohHoldSettlementID) {
		BohHoldSettlement bohHoldSettlement = null;
		String sql = "select * from " + TableNames.BohHoldSettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { bohHoldSettlementID + "" });
			if (cursor.moveToFirst()) {
				bohHoldSettlement = new BohHoldSettlement();
				bohHoldSettlement.setId(cursor.getInt(0));
				bohHoldSettlement.setRestaurantId(cursor.getInt(1));
				bohHoldSettlement.setRevenueId(cursor.getInt(2));
				bohHoldSettlement.setOrderId(cursor.getInt(3));
				bohHoldSettlement.setPaymentId(cursor.getInt(4));
				bohHoldSettlement.setPaymentSettId(cursor.getInt(5));
				bohHoldSettlement.setBillNo(cursor.getInt(6));
				bohHoldSettlement.setNameOfPerson(cursor.getString(7));
				bohHoldSettlement.setPhone(cursor.getString(8));
				bohHoldSettlement.setRemarks(cursor.getString(9));
				bohHoldSettlement.setAuthorizedUserId(cursor.getInt(10));
				bohHoldSettlement.setAmount(cursor.getString(11));
				bohHoldSettlement.setStatus(cursor.getInt(12));
				bohHoldSettlement.setPaymentType(cursor.getInt(13));
				bohHoldSettlement.setPaidDate(cursor.getLong(14));
				bohHoldSettlement.setDaysDue(cursor.getLong(15));
				bohHoldSettlement.setIsActive(cursor.getInt(16));
				return bohHoldSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return bohHoldSettlement;
	}

	public static BohHoldSettlement getBohHoldSettlementByPament(
			Integer paymentId, Integer paymentSettId) {
		BohHoldSettlement bohHoldSettlement = null;
		String sql = "select * from " + TableNames.BohHoldSettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId), String.valueOf(paymentSettId) });
			if (cursor.moveToFirst()) {
				bohHoldSettlement = new BohHoldSettlement();
				bohHoldSettlement.setId(cursor.getInt(0));
				bohHoldSettlement.setRestaurantId(cursor.getInt(1));
				bohHoldSettlement.setRevenueId(cursor.getInt(2));
				bohHoldSettlement.setOrderId(cursor.getInt(3));
				bohHoldSettlement.setPaymentId(cursor.getInt(4));
				bohHoldSettlement.setPaymentSettId(cursor.getInt(5));
				bohHoldSettlement.setBillNo(cursor.getInt(6));
				bohHoldSettlement.setNameOfPerson(cursor.getString(7));
				bohHoldSettlement.setPhone(cursor.getString(8));
				bohHoldSettlement.setRemarks(cursor.getString(9));
				bohHoldSettlement.setAuthorizedUserId(cursor.getInt(10));
				bohHoldSettlement.setAmount(cursor.getString(11));
				bohHoldSettlement.setStatus(cursor.getInt(12));
				bohHoldSettlement.setPaymentType(cursor.getInt(13));
				bohHoldSettlement.setPaidDate(cursor.getLong(14));
				bohHoldSettlement.setDaysDue(cursor.getLong(15));
				bohHoldSettlement.setIsActive(cursor.getInt(16));
				return bohHoldSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return bohHoldSettlement;
	}

	public static ArrayList<BohHoldSettlement> getBohHoldSettlementsByPamentId(
			Integer paymentId) {
		ArrayList<BohHoldSettlement> bohHoldSettlements = new ArrayList<BohHoldSettlement>();
		String sql = "select * from " + TableNames.BohHoldSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentId) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext())  {
				BohHoldSettlement bohHoldSettlement = new BohHoldSettlement();
				bohHoldSettlement.setId(cursor.getInt(0));
				bohHoldSettlement.setRestaurantId(cursor.getInt(1));
				bohHoldSettlement.setRevenueId(cursor.getInt(2));
				bohHoldSettlement.setOrderId(cursor.getInt(3));
				bohHoldSettlement.setPaymentId(cursor.getInt(4));
				bohHoldSettlement.setPaymentSettId(cursor.getInt(5));
				bohHoldSettlement.setBillNo(cursor.getInt(6));
				bohHoldSettlement.setNameOfPerson(cursor.getString(7));
				bohHoldSettlement.setPhone(cursor.getString(8));
				bohHoldSettlement.setRemarks(cursor.getString(9));
				bohHoldSettlement.setAuthorizedUserId(cursor.getInt(10));
				bohHoldSettlement.setAmount(cursor.getString(11));
				bohHoldSettlement.setStatus(cursor.getInt(12));
				bohHoldSettlement.setPaymentType(cursor.getInt(13));
				bohHoldSettlement.setPaidDate(cursor.getLong(14) == 0 ? null: cursor.getLong(14));
				bohHoldSettlement.setDaysDue(cursor.getLong(15));
				bohHoldSettlement.setIsActive(cursor.getInt(16));
				bohHoldSettlements.add(bohHoldSettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return bohHoldSettlements;
	}

	public static ArrayList<BohHoldSettlement> getAllBohHoldSettlement() {
		ArrayList<BohHoldSettlement> result = new ArrayList<BohHoldSettlement>();
		String sql = "select * from " + TableNames.BohHoldSettlement;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			BohHoldSettlement bohHoldSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				bohHoldSettlement = new BohHoldSettlement();
				bohHoldSettlement.setId(cursor.getInt(0));
				bohHoldSettlement.setRestaurantId(cursor.getInt(1));
				bohHoldSettlement.setRevenueId(cursor.getInt(2));
				bohHoldSettlement.setOrderId(cursor.getInt(3));
				bohHoldSettlement.setPaymentId(cursor.getInt(4));
				bohHoldSettlement.setPaymentSettId(cursor.getInt(5));
				bohHoldSettlement.setBillNo(cursor.getInt(6));
				bohHoldSettlement.setNameOfPerson(cursor.getString(7));
				bohHoldSettlement.setPhone(cursor.getString(8));
				bohHoldSettlement.setRemarks(cursor.getString(9));
				bohHoldSettlement.setAuthorizedUserId(cursor.getInt(10));
				bohHoldSettlement.setAmount(cursor.getString(11));
				bohHoldSettlement.setStatus(cursor.getInt(12));
				bohHoldSettlement.setPaymentType(cursor.getInt(13));
				bohHoldSettlement.setPaidDate(cursor.getLong(14));
				bohHoldSettlement.setDaysDue(cursor.getLong(15));
				bohHoldSettlement.setIsActive(cursor.getInt(16));
				result.add(bohHoldSettlement);
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

	public static ArrayList<BohHoldSettlement> getBohHoldSettlementsByStatus(int status) {
		ArrayList<BohHoldSettlement> result = new ArrayList<BohHoldSettlement>();
		String sql = "select * from "
				+ TableNames.BohHoldSettlement
				+ " where status = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE + " order by id desc";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(status)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			BohHoldSettlement bohHoldSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				bohHoldSettlement = new BohHoldSettlement();
				bohHoldSettlement.setId(cursor.getInt(0));
				bohHoldSettlement.setRestaurantId(cursor.getInt(1));
				bohHoldSettlement.setRevenueId(cursor.getInt(2));
				bohHoldSettlement.setOrderId(cursor.getInt(3));
				bohHoldSettlement.setPaymentId(cursor.getInt(4));
				bohHoldSettlement.setPaymentSettId(cursor.getInt(5));
				bohHoldSettlement.setBillNo(cursor.getInt(6));
				bohHoldSettlement.setNameOfPerson(cursor.getString(7));
				bohHoldSettlement.setPhone(cursor.getString(8));
				bohHoldSettlement.setRemarks(cursor.getString(9));
				bohHoldSettlement.setAuthorizedUserId(cursor.getInt(10));
				bohHoldSettlement.setAmount(cursor.getString(11));
				bohHoldSettlement.setStatus(cursor.getInt(12));
				bohHoldSettlement.setPaymentType(cursor.getInt(13));
				bohHoldSettlement.setPaidDate(cursor.getLong(14));
				bohHoldSettlement.setDaysDue(cursor.getLong(15));
				bohHoldSettlement.setIsActive(cursor.getInt(16));
				result.add(bohHoldSettlement);
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

	public static void regainBohHoldSettlementByPayment(Payment payment) {
		String sql = "delete from " + TableNames.BohHoldSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		String sql1 = "update " + TableNames.BohHoldSettlement
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
	
	public static void deleteBohHoldSettlement(
			BohHoldSettlement bohHoldSettlement) {
		String sql = "delete from " + TableNames.BohHoldSettlement
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { bohHoldSettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
