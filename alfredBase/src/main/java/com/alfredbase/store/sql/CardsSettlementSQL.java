package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.CardsSettlement;
import com.alfredbase.javabean.Payment;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;

public class CardsSettlementSQL {

	public static void addCardsSettlement(CardsSettlement cardsSettlement) {
		if (cardsSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.CardsSettlement
					+ "(id, paymentId, paymentSettId, billNo, cardNo, cardType, cvvNo, cardExpiryDate, createTime, updateTime, isActive)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { cardsSettlement.getId(),
							cardsSettlement.getPaymentId(),
							cardsSettlement.getPaymentSettId(),
							cardsSettlement.getBillNo(),
							cardsSettlement.getCardNo(),
							cardsSettlement.getCardType(),
							cardsSettlement.getCvvNo(),
							cardsSettlement.getCardExpiryDate(),
							cardsSettlement.getCreateTime(),
							cardsSettlement.getUpdateTime(),
							cardsSettlement.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static CardsSettlement getCardsSettlement(Integer cardsSettlementID) {
		CardsSettlement cardsSettlement = null;
		String sql = "select * from " + TableNames.CardsSettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { cardsSettlementID + "" });
			if (cursor.moveToFirst()) {
				cardsSettlement = new CardsSettlement();
				cardsSettlement.setId(cursor.getInt(0));
				cardsSettlement.setPaymentId(cursor.getInt(1));
				cardsSettlement.setPaymentSettId(cursor.getInt(2));
				cardsSettlement.setBillNo(cursor.getInt(3));
				cardsSettlement.setCardNo(cursor.getString(4));
				cardsSettlement.setCardType(cursor.getInt(5));
				cardsSettlement.setCvvNo(cursor.getInt(6));
				cardsSettlement.setCardExpiryDate(cursor.getString(7));
				cardsSettlement.setCreateTime(cursor.getLong(8));
				cardsSettlement.setUpdateTime(cursor.getLong(9));
				cardsSettlement.setIsActive(cursor.getInt(10));
				return cardsSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return cardsSettlement;
	}

	public static CardsSettlement getCardsSettlementByPament(Integer paymentId, Integer paymentSettId) {
		CardsSettlement cardsSettlement = null;
		String sql = "select * from " + TableNames.CardsSettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(paymentId), String.valueOf(paymentSettId) });
			if (cursor.moveToFirst()) {
				cardsSettlement = new CardsSettlement();
				cardsSettlement.setId(cursor.getInt(0));
				cardsSettlement.setPaymentId(cursor.getInt(1));
				cardsSettlement.setPaymentSettId(cursor.getInt(2));
				cardsSettlement.setBillNo(cursor.getInt(3));
				cardsSettlement.setCardNo(cursor.getString(4));
				cardsSettlement.setCardType(cursor.getInt(5));
				cardsSettlement.setCvvNo(cursor.getInt(6));
				cardsSettlement.setCardExpiryDate(cursor.getString(7));
				cardsSettlement.setCreateTime(cursor.getLong(8));
				cardsSettlement.setUpdateTime(cursor.getLong(9));
				cardsSettlement.setIsActive(cursor.getInt(10));
				return cardsSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return cardsSettlement;
	}

	public static ArrayList<CardsSettlement> getCardsSettlementsByPamentId(
			Integer paymentId) {
		ArrayList<CardsSettlement> cardsSettlements = new ArrayList<CardsSettlement>();
		String sql = "select * from " + TableNames.CardsSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentId) });
			if (cursor.moveToFirst()) {
				CardsSettlement cardsSettlement = new CardsSettlement();
				cardsSettlement.setId(cursor.getInt(0));
				cardsSettlement.setPaymentId(cursor.getInt(1));
				cardsSettlement.setPaymentSettId(cursor.getInt(2));
				cardsSettlement.setBillNo(cursor.getInt(3));
				cardsSettlement.setCardNo(cursor.getString(4));
				cardsSettlement.setCardType(cursor.getInt(5));
				cardsSettlement.setCvvNo(cursor.getInt(6));
				cardsSettlement.setCardExpiryDate(cursor.getString(7));
				cardsSettlement.setCreateTime(cursor.getLong(8));
				cardsSettlement.setUpdateTime(cursor.getLong(9));
				cardsSettlement.setIsActive(cursor.getInt(10));
				cardsSettlements.add(cardsSettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return cardsSettlements;
	}

	public static ArrayList<CardsSettlement> getAllCardsSettlement() {
		ArrayList<CardsSettlement> result = new ArrayList<CardsSettlement>();
		String sql = "select * from " + TableNames.CardsSettlement + " where isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			CardsSettlement cardsSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				cardsSettlement = new CardsSettlement();
				cardsSettlement.setId(cursor.getInt(0));
				cardsSettlement.setPaymentId(cursor.getInt(1));
				cardsSettlement.setPaymentSettId(cursor.getInt(2));
				cardsSettlement.setBillNo(cursor.getInt(3));
				cardsSettlement.setCardNo(cursor.getString(4));
				cardsSettlement.setCardType(cursor.getInt(5));
				cardsSettlement.setCvvNo(cursor.getInt(6));
				cardsSettlement.setCardExpiryDate(cursor.getString(7));
				cardsSettlement.setCreateTime(cursor.getLong(8));
				cardsSettlement.setUpdateTime(cursor.getLong(9));
				cardsSettlement.setIsActive(cursor.getInt(10));
				result.add(cardsSettlement);
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

	public static ArrayList<CardsSettlement> getCardsSettlementsByNowTime(long time, long DayZero) {
		ArrayList<CardsSettlement> result = new ArrayList<CardsSettlement>();
		String sql = "select * from "
				+ TableNames.CardsSettlement
				+ " where createTime < ? and paidDate > ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			CardsSettlement cardsSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				cardsSettlement = new CardsSettlement();
				cardsSettlement.setId(cursor.getInt(0));
				cardsSettlement.setPaymentId(cursor.getInt(1));
				cardsSettlement.setPaymentSettId(cursor.getInt(2));
				cardsSettlement.setBillNo(cursor.getInt(3));
				cardsSettlement.setCardNo(cursor.getString(4));
				cardsSettlement.setCardType(cursor.getInt(5));
				cardsSettlement.setCvvNo(cursor.getInt(6));
				cardsSettlement.setCardExpiryDate(cursor.getString(7));
				cardsSettlement.setCreateTime(cursor.getLong(8));
				cardsSettlement.setUpdateTime(cursor.getLong(9));
				cardsSettlement.setIsActive(cursor.getInt(10));
				result.add(cardsSettlement);
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
	
	public static String getCardNoByPaymentIdAndPaymentSettlementId(int paymentId, int paymentSettlementId){
		String cardNo = "";
		String sql = "select cardNo from "
				+ TableNames.CardsSettlement
				+ " where paymentId = ? and paymentSettId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(paymentId), String.valueOf(paymentSettlementId)});
			int count = cursor.getCount();
			if (count < 1) {
				return cardNo;
			}
			if (cursor.moveToFirst()) {
				cardNo = cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return cardNo;
	
	}
	
	public static void regainCardsSettlementByPayment(Payment payment) {
		String sql = "delete from " + TableNames.CardsSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		String sql1 = "update " + TableNames.CardsSettlement
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

	public static void deleteCardsSettlement(CardsSettlement cardsSettlement) {
		String sql = "delete from " + TableNames.CardsSettlement
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { cardsSettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
