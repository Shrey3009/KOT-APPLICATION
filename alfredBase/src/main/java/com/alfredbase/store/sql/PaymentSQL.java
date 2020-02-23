package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentSQL {
	public static void addPayment(Payment payment) {
		if (payment == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.Payment
					+ "(id, billNo, orderId, orderSplitId, businessDate, type, restaurantId, revenueId,  userId, paymentAmount, taxAmount, discountAmount, createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { payment.getId(), payment.getBillNo(),
							payment.getOrderId(), payment.getOrderSplitId(),
							payment.getBusinessDate(), payment.getType(),
							payment.getRestaurantId(), payment.getRevenueId(),
							payment.getUserId(), payment.getPaymentAmount(),
							payment.getTaxAmount(),
							payment.getDiscountAmount(),
							payment.getCreateTime(), payment.getUpdateTime() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void addPayment(SQLiteDatabase db, Payment payment) {
		if (payment == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.Payment
					+ "(id, billNo, orderId, orderSplitId, businessDate, type, restaurantId, revenueId,  userId, paymentAmount, taxAmount, discountAmount, createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(
					sql,
					new Object[] { payment.getId(), payment.getBillNo(),
							payment.getOrderId(), payment.getOrderSplitId(),
							payment.getBusinessDate(), payment.getType(),
							payment.getRestaurantId(), payment.getRevenueId(),
							payment.getUserId(), payment.getPaymentAmount(),
							payment.getTaxAmount(),
							payment.getDiscountAmount(),
							payment.getCreateTime(), payment.getUpdateTime() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Payment getPayment(Integer paymentID) {
		Payment payment = null;
		String sql = "select * from " + TableNames.Payment + " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { paymentID + "" });
			if (cursor.moveToFirst()) {
				payment = new Payment();
				payment.setId(cursor.getInt(0));
				payment.setBillNo(cursor.getInt(1));
				payment.setOrderId(cursor.getInt(2));
				payment.setOrderSplitId(cursor.getInt(3));
				payment.setBusinessDate(cursor.getLong(4));
				payment.setType(cursor.getInt(5));
				payment.setRestaurantId(cursor.getInt(6));
				payment.setRevenueId(cursor.getInt(7));
				payment.setUserId(cursor.getInt(8));
				payment.setPaymentAmount(cursor.getString(9));
				payment.setTaxAmount(cursor.getString(10));
				payment.setDiscountAmount(cursor.getString(11));
				payment.setCreateTime(cursor.getLong(12));
				payment.setUpdateTime(cursor.getLong(13));
				return payment;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return payment;
	}

	public static Payment getPaymentByOrderId(Integer orderId) {
		Payment payment = null;
		String sql = "select * from " + TableNames.Payment
				+ " where orderId = ? and orderSplitId = " + ParamConst.BILL_TYPE_UN_SPLIT;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB()
					.rawQuery(sql, new String[] { orderId + "" });
			if (cursor.moveToFirst()) {
				payment = new Payment();
				payment.setId(cursor.getInt(0));
				payment.setBillNo(cursor.getInt(1));
				payment.setOrderId(cursor.getInt(2));
				payment.setOrderSplitId(cursor.getInt(3));
				payment.setBusinessDate(cursor.getLong(4));
				payment.setType(cursor.getInt(5));
				payment.setRestaurantId(cursor.getInt(6));
				payment.setRevenueId(cursor.getInt(7));
				payment.setUserId(cursor.getInt(8));
				payment.setPaymentAmount(cursor.getString(9));
				payment.setTaxAmount(cursor.getString(10));
				payment.setDiscountAmount(cursor.getString(11));
				payment.setCreateTime(cursor.getLong(12));
				payment.setUpdateTime(cursor.getLong(13));
				return payment;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return payment;
	}
	
	public static void updatePaymentAmount( String paymentAmount, int orderId){

		String sql = "update " + TableNames.Payment + " set paymentAmount = ? where orderId = ? " ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {paymentAmount, orderId});
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public static void updateSplitOrderPaymentAmount( String paymentAmount, int orderSplitId){

		String sql = "update " + TableNames.Payment + " set paymentAmount = ? where orderSplitId = ? " ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {paymentAmount, orderSplitId});
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public static List<Payment> getPaymentByOrderIdForSyncData(Integer orderId) {
		List<Payment> payments = new ArrayList<Payment>();
		String sql = "select * from " + TableNames.Payment
				+ " where orderId = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB()
					.rawQuery(sql, new String[] { orderId + "" });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				Payment payment = new Payment();
				payment.setId(cursor.getInt(0));
				payment.setBillNo(cursor.getInt(1));
				payment.setOrderId(cursor.getInt(2));
				payment.setOrderSplitId(cursor.getInt(3));
				payment.setBusinessDate(cursor.getLong(4));
				payment.setType(cursor.getInt(5));
				payment.setRestaurantId(cursor.getInt(6));
				payment.setRevenueId(cursor.getInt(7));
				payment.setUserId(cursor.getInt(8));
				payment.setPaymentAmount(cursor.getString(9));
				payment.setTaxAmount(cursor.getString(10));
				payment.setDiscountAmount(cursor.getString(11));
				payment.setCreateTime(cursor.getLong(12));
				payment.setUpdateTime(cursor.getLong(13));
				payments.add(payment);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return payments;
	}
	
	public static Payment getPaymentByOrderSplitId(Integer orderSplitId) {
		Payment payment = null;
		String sql = "select * from " + TableNames.Payment
				+ " where orderSplitId = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB()
					.rawQuery(sql, new String[] { orderSplitId + "" });
			if (cursor.moveToFirst()) {
				payment = new Payment();
				payment.setId(cursor.getInt(0));
				payment.setBillNo(cursor.getInt(1));
				payment.setOrderId(cursor.getInt(2));
				payment.setOrderSplitId(cursor.getInt(3));
				payment.setBusinessDate(cursor.getLong(4));
				payment.setType(cursor.getInt(5));
				payment.setRestaurantId(cursor.getInt(6));
				payment.setRevenueId(cursor.getInt(7));
				payment.setUserId(cursor.getInt(8));
				payment.setPaymentAmount(cursor.getString(9));
				payment.setTaxAmount(cursor.getString(10));
				payment.setDiscountAmount(cursor.getString(11));
				payment.setCreateTime(cursor.getLong(12));
				payment.setUpdateTime(cursor.getLong(13));
				return payment;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return payment;
	}

	public static ArrayList<Payment> getAllPayment() {
		ArrayList<Payment> result = new ArrayList<Payment>();
		String sql = "select * from " + TableNames.Payment;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Payment payment = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				payment = new Payment();
				payment.setId(cursor.getInt(0));
				payment.setBillNo(cursor.getInt(1));
				payment.setOrderId(cursor.getInt(2));
				payment.setOrderSplitId(cursor.getInt(3));
				payment.setBusinessDate(cursor.getLong(4));
				payment.setType(cursor.getInt(5));
				payment.setRestaurantId(cursor.getInt(6));
				payment.setRevenueId(cursor.getInt(7));
				payment.setUserId(cursor.getInt(8));
				payment.setPaymentAmount(cursor.getString(9));
				payment.setTaxAmount(cursor.getString(10));
				payment.setDiscountAmount(cursor.getString(11));
				payment.setCreateTime(cursor.getLong(12));
				payment.setUpdateTime(cursor.getLong(13));
				result.add(payment);
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

	public static Map<String, String> getAllPaymentSumByTime(long businessDate, long time,
			long dayZero) {
		Map<String, String> map = new HashMap<String, String>();

//		String sql = " select sum(sumAmount), count(0) from (select sum(s.paidAmount) sumAmount, count(p.id) from "
//				+ TableNames.Payment
//				+ " p, "
//				+ TableNames.PaymentSettlement
//				+ " s "
//				+ " where p.businessDate = ? and s.paymentId = p.id and p.createTime > ? and p.createTime < ? and s.paymentTypeId <> "
//				+ ParamConst.SETTLEMENT_TYPE_VOID
//				+ " and"
//				+ " s.paymentTypeId <> "
//				+ ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
//				+ " and"
//				+ " s.paymentTypeId <> "
//				+ ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD
//				+ " group by p.id)";
		String sql = "select sum(paymentAmount), count(*) from "
				+ TableNames.Payment
				+ " where businessDate = ? and createTime > ? and createTime < ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { String.valueOf(businessDate), String.valueOf(time),
					String.valueOf(dayZero) });
			int count = cursor.getCount();
			if (count < 1) {
				return map;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				map.put("sum", cursor.getString(0));
				map.put("count", String.valueOf(cursor.getInt(1)));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		if (map.get("sum") == null) {
			return null;
		}
		return map;
	}

	public static ArrayList<Payment> getTodayAllPaymentBySession(
			SessionStatus sessionStatus, long businessDate) {
		ArrayList<Payment> result = new ArrayList<Payment>();
		String sql = "select * from "
				+ TableNames.Payment
				+ " where orderId in( select id from "
				+ TableNames.Order
				+ " where sessionStatus = ? and createTime > ? and businessDate = ? and orderStatus = "
				+ ParamConst.ORDER_STATUS_FINISHED + ") order by id desc";
//				+ " or orderStatus = "
//				+ ParamConst.ORDER_STATUS_BOH + ")";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] {
							String.valueOf(sessionStatus.getSession_status()),
							String.valueOf(sessionStatus.getTime()),
							String.valueOf(businessDate) });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Payment payment = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				payment = new Payment();
				payment.setId(cursor.getInt(0));
				payment.setBillNo(cursor.getInt(1));
				payment.setOrderId(cursor.getInt(2));
				payment.setOrderSplitId(cursor.getInt(3));
				payment.setBusinessDate(cursor.getLong(4));
				payment.setType(cursor.getInt(5));
				payment.setRestaurantId(cursor.getInt(6));
				payment.setRevenueId(cursor.getInt(7));
				payment.setUserId(cursor.getInt(8));
				payment.setPaymentAmount(cursor.getString(9));
				payment.setTaxAmount(cursor.getString(10));
				payment.setDiscountAmount(cursor.getString(11));
				payment.setCreateTime(cursor.getLong(12));
				payment.setUpdateTime(cursor.getLong(13));
				result.add(payment);
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
	
	public static List<Payment> getPaymentFromNoActivePaymentSettlement(long businessDate){
		List<Payment> result = new ArrayList<Payment>();
		String sql = "select p.* from " 
				+ TableNames.Payment 
				+ " p, " 
				+ TableNames.PaymentSettlement 
				+ " s where s.paymentId = p.id and p.businessDate = ? and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_NO_ACTIVE
				+ " group by p.id";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] {String.valueOf(businessDate) });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Payment payment = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				payment = new Payment();
				payment.setId(cursor.getInt(0));
				payment.setBillNo(cursor.getInt(1));
				payment.setOrderId(cursor.getInt(2));
				payment.setOrderSplitId(cursor.getInt(3));
				payment.setBusinessDate(cursor.getLong(4));
				payment.setType(cursor.getInt(5));
				payment.setRestaurantId(cursor.getInt(6));
				payment.setRevenueId(cursor.getInt(7));
				payment.setUserId(cursor.getInt(8));
				payment.setPaymentAmount(cursor.getString(9));
				payment.setTaxAmount(cursor.getString(10));
				payment.setDiscountAmount(cursor.getString(11));
				payment.setCreateTime(cursor.getLong(12));
				payment.setUpdateTime(cursor.getLong(13));
				result.add(payment);
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

	public static void deletePayment(Payment payment) {
		String sql = "delete from " + TableNames.Payment + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { payment.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteAllPayment() {
		String sql = "delete from " + TableNames.Payment;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
