package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentSettlementSQL {
	public static void addPaymentSettlement(PaymentSettlement paymentSettlement) {
		if (paymentSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.PaymentSettlement
					+ "(id, billNo, paymentId, paymentTypeId, paidAmount, totalAmount, restaurantId, revenueId, userId, createTime, updateTime, cashChange, isActive, partChange)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { paymentSettlement.getId(),
							paymentSettlement.getBillNo(),
							paymentSettlement.getPaymentId(),
							paymentSettlement.getPaymentTypeId(),
							paymentSettlement.getPaidAmount(),
							paymentSettlement.getTotalAmount(),
							paymentSettlement.getRestaurantId(),
							paymentSettlement.getRevenueId(),
							paymentSettlement.getUserId(),
							paymentSettlement.getCreateTime(),
							paymentSettlement.getUpdateTime(),
							paymentSettlement.getCashChange(),
							paymentSettlement.getIsActive(),
							paymentSettlement.getPartChange()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void addPaymentSettlement(SQLiteDatabase db ,PaymentSettlement paymentSettlement) {
		if (paymentSettlement == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.PaymentSettlement
					+ "(id, billNo, paymentId, paymentTypeId, paidAmount, totalAmount, restaurantId, revenueId, userId, createTime, updateTime, cashChange, isActive, partChange)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(
					sql,
					new Object[] { paymentSettlement.getId(),
							paymentSettlement.getBillNo(),
							paymentSettlement.getPaymentId(),
							paymentSettlement.getPaymentTypeId(),
							paymentSettlement.getPaidAmount(),
							paymentSettlement.getTotalAmount(),
							paymentSettlement.getRestaurantId(),
							paymentSettlement.getRevenueId(),
							paymentSettlement.getUserId(),
							paymentSettlement.getCreateTime(),
							paymentSettlement.getUpdateTime(),
							paymentSettlement.getCashChange(),
							paymentSettlement.getIsActive(),
							paymentSettlement.getPartChange()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PaymentSettlement getPaymentSettlement(
			Integer paymentSettlementID) {
		PaymentSettlement paymentSettlement = null;
		String sql = "select * from " + TableNames.PaymentSettlement
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { paymentSettlementID + "" });
			if (cursor.moveToFirst()) {
				paymentSettlement = new PaymentSettlement();
				paymentSettlement.setId(cursor.getInt(0));
				paymentSettlement.setBillNo(cursor.getInt(1));
				paymentSettlement.setPaymentId(cursor.getInt(2));
				paymentSettlement.setPaymentTypeId(cursor.getInt(3));
				paymentSettlement.setPaidAmount(cursor.getString(4));
				paymentSettlement.setTotalAmount(cursor.getString(5));
				paymentSettlement.setRestaurantId(cursor.getInt(6));
				paymentSettlement.setRevenueId(cursor.getInt(7));
				paymentSettlement.setUserId(cursor.getInt(8));
				paymentSettlement.setCreateTime(cursor.getLong(9));
				paymentSettlement.setUpdateTime(cursor.getLong(10));
				paymentSettlement.setCashChange(cursor.getString(11));
				paymentSettlement.setIsActive(cursor.getInt(12));
				paymentSettlement.setPartChange(cursor.getString(13));
				return paymentSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return paymentSettlement;
	}

	public static ArrayList<PaymentSettlement> getPaymentSettlementsBypaymentId(
			Integer paymentID) {
		ArrayList<PaymentSettlement> paymentSettlements = new ArrayList<PaymentSettlement>();
		String sql = "select * from " + TableNames.PaymentSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentID) });
			int count = cursor.getCount();
			if (count < 1) {
				return paymentSettlements;
			}
			PaymentSettlement paymentSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				paymentSettlement = new PaymentSettlement();
				paymentSettlement.setId(cursor.getInt(0));
				paymentSettlement.setBillNo(cursor.getInt(1));
				paymentSettlement.setPaymentId(cursor.getInt(2));
				paymentSettlement.setPaymentTypeId(cursor.getInt(3));
				paymentSettlement.setPaidAmount(cursor.getString(4));
				paymentSettlement.setTotalAmount(cursor.getString(5));
				paymentSettlement.setRestaurantId(cursor.getInt(6));
				paymentSettlement.setRevenueId(cursor.getInt(7));
				paymentSettlement.setUserId(cursor.getInt(8));
				paymentSettlement.setCreateTime(cursor.getLong(9));
				paymentSettlement.setUpdateTime(cursor.getLong(10));
				paymentSettlement.setCashChange(cursor.getString(11));
				paymentSettlement.setIsActive(cursor.getInt(12));
				paymentSettlement.setPartChange(cursor.getString(13));
				paymentSettlements.add(paymentSettlement);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return paymentSettlements;
	}
	
	
	public static PaymentSettlement getPaymentSettlementsByOrderId(
			int orderId) {
		String sql = "select s.* from " 
				+ TableNames.PaymentSettlement
				+ " s,"
				+ TableNames.Payment
				+" p"
				+ " where s.paymentId = p.id and p.orderId = ? and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE;
		PaymentSettlement paymentSettlement = null;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(orderId) });
			int count = cursor.getCount();
			if (count < 1) {
				return paymentSettlement;
			}
			
			if (cursor.moveToFirst()) {
				paymentSettlement = new PaymentSettlement();
				paymentSettlement.setId(cursor.getInt(0));
				paymentSettlement.setBillNo(cursor.getInt(1));
				paymentSettlement.setPaymentId(cursor.getInt(2));
				paymentSettlement.setPaymentTypeId(cursor.getInt(3));
				paymentSettlement.setPaidAmount(cursor.getString(4));
				paymentSettlement.setTotalAmount(cursor.getString(5));
				paymentSettlement.setRestaurantId(cursor.getInt(6));
				paymentSettlement.setRevenueId(cursor.getInt(7));
				paymentSettlement.setUserId(cursor.getInt(8));
				paymentSettlement.setCreateTime(cursor.getLong(9));
				paymentSettlement.setUpdateTime(cursor.getLong(10));
				paymentSettlement.setCashChange(cursor.getString(11));
				paymentSettlement.setIsActive(cursor.getInt(12));
				paymentSettlement.setPartChange(cursor.getString(13));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return paymentSettlement;
	}

	public static String getPaymentSettlementsSumBypaymentId(Integer paymentID) {
		String sumPaidamount = null;
		String sql = "select sum(paidAmount) from "
				+ TableNames.PaymentSettlement + " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentID) });
			if (cursor.moveToFirst()) {
				sumPaidamount = cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return sumPaidamount;
	}

	public static PaymentSettlement getPaymentSettlementByPaymentIdAndTypeId(
			Payment payment, int typeid) {
		PaymentSettlement paymentSettlement = null;
		String sql = "select * from " + TableNames.PaymentSettlement
				+ " where paymentId = ? and paymentTypeId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(
					sql,
					new String[] { String.valueOf(payment.getId()),
							String.valueOf(typeid) });
			if (cursor.moveToFirst()) {
				paymentSettlement = new PaymentSettlement();
				paymentSettlement.setId(cursor.getInt(0));
				paymentSettlement.setBillNo(cursor.getInt(1));
				paymentSettlement.setPaymentId(cursor.getInt(2));
				paymentSettlement.setPaymentTypeId(cursor.getInt(3));
				paymentSettlement.setPaidAmount(cursor.getString(4));
				paymentSettlement.setTotalAmount(cursor.getString(5));
				paymentSettlement.setRestaurantId(cursor.getInt(6));
				paymentSettlement.setRevenueId(cursor.getInt(7));
				paymentSettlement.setUserId(cursor.getInt(8));
				paymentSettlement.setCreateTime(cursor.getLong(9));
				paymentSettlement.setUpdateTime(cursor.getLong(10));
				paymentSettlement.setCashChange(cursor.getString(11));
				paymentSettlement.setIsActive(cursor.getInt(12));
				paymentSettlement.setPartChange(cursor.getString(13));
				return paymentSettlement;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return paymentSettlement;
	}

	public static ArrayList<PaymentSettlement> getAllPaymentSettlement() {
		ArrayList<PaymentSettlement> result = new ArrayList<PaymentSettlement>();
		String sql = "select * from " + TableNames.PaymentSettlement + " where isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			PaymentSettlement paymentSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				paymentSettlement = new PaymentSettlement();
				paymentSettlement.setId(cursor.getInt(0));
				paymentSettlement.setBillNo(cursor.getInt(1));
				paymentSettlement.setPaymentId(cursor.getInt(2));
				paymentSettlement.setPaymentTypeId(cursor.getInt(3));
				paymentSettlement.setPaidAmount(cursor.getString(4));
				paymentSettlement.setTotalAmount(cursor.getString(5));
				paymentSettlement.setRestaurantId(cursor.getInt(6));
				paymentSettlement.setRevenueId(cursor.getInt(7));
				paymentSettlement.setUserId(cursor.getInt(8));
				paymentSettlement.setCreateTime(cursor.getLong(9));
				paymentSettlement.setUpdateTime(cursor.getLong(10));
				paymentSettlement.setCashChange(cursor.getString(11));
				paymentSettlement.setIsActive(cursor.getInt(12));
				paymentSettlement.setPartChange(cursor.getString(13));
				result.add(paymentSettlement);
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

	public static ArrayList<PaymentSettlement> getAllPaymentSettlementByPayment(
			Payment payment) {
		return getAllPaymentSettlementByPaymentId(payment.getId());
	}

	public static List<PaymentSettlement> getAllPaymentSettlementByOrderId(int orderId){
		ArrayList<PaymentSettlement> result = new ArrayList<PaymentSettlement>();
		String sql = "select ps.* from "
				+ TableNames.PaymentSettlement
				+ " ps, "
				+ TableNames.Payment
				+ " p where ps.paymentId = p.id and p.orderId = ? and ps.isActive = "
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE + " group by ps.id";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { orderId + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			PaymentSettlement paymentSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				paymentSettlement = new PaymentSettlement();
				paymentSettlement.setId(cursor.getInt(0));
				paymentSettlement.setBillNo(cursor.getInt(1));
				paymentSettlement.setPaymentId(cursor.getInt(2));
				paymentSettlement.setPaymentTypeId(cursor.getInt(3));
				paymentSettlement.setPaidAmount(cursor.getString(4));
				paymentSettlement.setTotalAmount(cursor.getString(5));
				paymentSettlement.setRestaurantId(cursor.getInt(6));
				paymentSettlement.setRevenueId(cursor.getInt(7));
				paymentSettlement.setUserId(cursor.getInt(8));
				paymentSettlement.setCreateTime(cursor.getLong(9));
				paymentSettlement.setUpdateTime(cursor.getLong(10));
				paymentSettlement.setCashChange(cursor.getString(11));
				paymentSettlement.setIsActive(cursor.getInt(12));
				paymentSettlement.setPartChange(cursor.getString(13));
				result.add(paymentSettlement);
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

	public static ArrayList<PaymentSettlement> getAllPaymentSettlementByPaymentId(
			int paymentId) {
		ArrayList<PaymentSettlement> result = new ArrayList<PaymentSettlement>();
		String sql = "select * from " + TableNames.PaymentSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE + " order by paymentTypeId asc";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { paymentId + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			PaymentSettlement paymentSettlement = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				paymentSettlement = new PaymentSettlement();
				paymentSettlement.setId(cursor.getInt(0));
				paymentSettlement.setBillNo(cursor.getInt(1));
				paymentSettlement.setPaymentId(cursor.getInt(2));
				paymentSettlement.setPaymentTypeId(cursor.getInt(3));
				paymentSettlement.setPaidAmount(cursor.getString(4));
				paymentSettlement.setTotalAmount(cursor.getString(5));
				paymentSettlement.setRestaurantId(cursor.getInt(6));
				paymentSettlement.setRevenueId(cursor.getInt(7));
				paymentSettlement.setUserId(cursor.getInt(8));
				paymentSettlement.setCreateTime(cursor.getLong(9));
				paymentSettlement.setUpdateTime(cursor.getLong(10));
				paymentSettlement.setCashChange(cursor.getString(11));
				paymentSettlement.setIsActive(cursor.getInt(12));
				paymentSettlement.setPartChange(cursor.getString(13));
				result.add(paymentSettlement);
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

	public static Map<String, String> getPaymentSettlementSumPaidAndCount(
			int paymentTypeId, long businessDate) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select SUM(paidAmount), COUNT (*) from "
				+ TableNames.PaymentSettlement
				+ " where paymentTypeId = ? and isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " and paymentId  in ( select id from "
				+ TableNames.Payment + " where businessDate = ?)";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] { String.valueOf(paymentTypeId),
							String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				map.put("sumAmount", cursor.getString(0));
				map.put("count", String.valueOf(cursor.getInt(1)));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		if (map.get("sumAmount") == null || map.get("sumAmount").equals("0")) {
			map.put("sumAmount", "0.00");
		}
		if (map.get("count") == null) {
			map.put("count", "0");
		}
		return map;
	}

	public static Map<String, String> getPaymentSettlementSumPaidAndCount(
			int paymentTypeId, long businessDate, SessionStatus sessionStatus) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select SUM(paidAmount), COUNT (*) from "
				+ TableNames.PaymentSettlement
				+ " where paymentTypeId = ? and isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " and paymentId  in ( select id from "
				+ TableNames.Payment
				+ " where orderId in ( select id from "
				+ TableNames.Order
				+ " where businessDate = ? and sessionStatus = ? and createTime > ?) )";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] { String.valueOf(paymentTypeId),
							String.valueOf(businessDate),
							String.valueOf(sessionStatus.getSession_status()),
							String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				map.put("sumAmount", cursor.getString(0));
				map.put("count", String.valueOf(cursor.getInt(1)));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		if (map.get("sumAmount") == null || map.get("sumAmount").equals("0")) {
			map.put("sumAmount", "0.00");
		}
		if (map.get("count") == null) {
			map.put("count", "0");
		}
		return map;
	}


	public static Map<String, String> getCustomPaymentSettlementSumPaidAndCount(
			int paymentTypeId, long businessDate, SessionStatus sessionStatus) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select SUM(paidAmount), COUNT (*), SUM(partChange) from "
				+ TableNames.PaymentSettlement
				+ " where paymentTypeId = ? and isActive = "
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE
				+ " and paymentId  in ( select id from "
				+ TableNames.Payment
				+ " where orderId in ( select id from "
				+ TableNames.Order
				+ " where businessDate = ? and sessionStatus = ? and createTime > ?) )";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] { String.valueOf(paymentTypeId),
							String.valueOf(businessDate),
							String.valueOf(sessionStatus.getSession_status()),
							String.valueOf(sessionStatus.getTime()) });
			if(cursor.getCount() < 1){
				return  map;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int count = cursor.getInt(1);
				if(count < 1){
					continue;
				}
				map.put("sumAmount", cursor.getString(0));
				map.put("count", String.valueOf(cursor.getInt(1)));
				map.put("partChange", cursor.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return map;
	}
	public static Map<String, String> getCustomPaymentSettlementSumPaidAndCount(
			int paymentTypeId, long businessDate) {
		Map<String, String> map = new HashMap<>();
		String sql = "select SUM(paidAmount), COUNT (*), SUM(partChange) from "
				+ TableNames.PaymentSettlement
				+ " where paymentTypeId = ? and isActive = "
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE
				+ " and paymentId  in ( select id from "
				+ TableNames.Payment
				+ " where orderId in ( select id from "
				+ TableNames.Order
				+ " where businessDate = ?) )";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(
					sql,
					new String[] { String.valueOf(paymentTypeId),
							String.valueOf(businessDate)});
			if(cursor.getCount() < 1){
				return  map;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int count = cursor.getInt(1);
				if(count < 1){
					continue;
				}
				map.put("sumAmount", cursor.getString(0));
				map.put("count", String.valueOf(cursor.getInt(1)));
				map.put("partChange", cursor.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return map;
	}

	public static String getSumPaidAmountByPaymentTypeId(int paymentTypeId,
			long businessDate) {
		String sql = "select SUM(paidAmount) from "
				+ TableNames.PaymentSettlement
				+ " where paymentTypeId = ? and isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " and paymentId  in ( select id from "
				+ TableNames.Payment + " where businessDate = ?)";
		Cursor cursor = null;
		String result = "0.00";
		try {
			cursor = SQLExe.getDB().rawQuery(
					sql,
					new String[] { String.valueOf(paymentTypeId),
							String.valueOf(businessDate) });
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
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

	public static String getSumPaidAmount(long businessDate) {
		String sql = "select SUM(paidAmount) from "
				+ TableNames.PaymentSettlement
				+ " where paymentId and isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " in ( select id from " + TableNames.Payment
				+ " where businessDate = ?)";
		Cursor cursor = null;
		String result = "0.00";
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { String.valueOf(businessDate) });
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
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

	public static void deletePaymentSettlement(
			PaymentSettlement paymentSettlement) {
		String sql = "delete from " + TableNames.PaymentSettlement
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { paymentSettlement.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<Integer, Map<String, String>> getItemsByBusinessDateAndPaymentType(int paymentTypeId, long businessDate) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = ? AND p.orderId = t.orderId and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND p.orderId = o.id AND p.businessDate = ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL 
				+ " and p.type= "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentTypeId), String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getSplitItemsByBusinessDateAndPaymentType(int paymentTypeId, long businessDate) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.OrderSplit
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = ? AND p.orderId = t.orderId and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " and t.groupId = o.groupId AND p.orderSplitId = o.id AND p.businessDate = ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL 
				+ " and p.type= "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentTypeId), String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsByBusinessDateAndPaymentTypeAndSession(int paymentTypeId, long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = ? AND p.orderId = t.orderId and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND p.orderId = o.id AND p.businessDate = ? and o.sessionStatus = ? and o.createTime > ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL 
				+" and p.type = "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentTypeId), String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsByBusinessDateAndPaymentTypeAndSessionAfterSplit(int paymentTypeId, long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.OrderSplit
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = ? AND p.orderId = t.orderId and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " and t.groupId = o.groupId AND p.orderSplitId = o.id AND p.businessDate = ? and o.sessionStatus = ? and o.createTime > ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL 
				+ " and p.type = "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(paymentTypeId), String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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


	public static Map<Integer, Map<String, String>> getItemsInVoidBillByBusinessDate(
			long businessDate) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE p.id = s.paymentId AND (s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_VOID
				+ " or s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_REFUND
				+ ") and p.type = "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " AND p.orderId = t.orderId AND p.orderId = o.id AND p.businessDate = ? and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL
				+ " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	
	public static Map<Integer, Map<String, String>> getModifiersInBillByBusinessDate(int itemId, int paymentTypeId,
			long businessDate) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT om.modifierId, sum(om.modifierNum), sum(om.modifierPrice) FROM "
				+ TableNames.OrderModifier
				+ " om, "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE om.orderDetailId = t.id AND t.itemId = ? AND p.id = s.paymentId AND s.paymentTypeId = ?"
				+ " AND p.orderId = t.orderId AND p.orderId = o.id AND p.businessDate = ? and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND om.status = "
				+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
				+ " AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL
				+ " group by om.modifierId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(itemId), String.valueOf(paymentTypeId), String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer modifierId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumModifierNum", sumItemNum + "");
				map.put("sumModifierPrice", sumRealPrice);
				result.put(modifierId, map);
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
	
	
	public static Map<Integer, Map<String, String>> getModifiersInBillBySessionStatus(int itemId, int paymentTypeId,
			long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT om.modifierId, sum(om.modifierNum), sum(om.modifierPrice) FROM "
				+ TableNames.OrderModifier
				+ " om, "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE om.orderDetailId = t.id AND t.itemId = ? AND p.id = s.paymentId AND s.paymentTypeId = ?"
				+ " AND p.orderId = t.orderId AND o.sessionStatus = ? and o.createTime > ? and o.orderStatus ="
				+ ParamConst.ORDER_STATUS_FINISHED 
				+ " and p.orderId = o.id AND p.businessDate = ? and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND om.status = "
				+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
				+ " AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL
				+ " group by om.modifierId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(itemId), String.valueOf(paymentTypeId), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()), String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer modifierId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumModifierNum", sumItemNum + "");
				map.put("sumModifierPrice", sumRealPrice);
				result.put(modifierId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsInVoidSplitBillByBusinessDate(
			long businessDate) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.OrderSplit
				+ " o "
				+ "WHERE p.id = s.paymentId AND (s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_VOID
				+ " or s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_REFUND
				+ ") and p.type = "
				+ ParamConst.BILL_TYPE_SPLIT
				+ " AND p.orderId = t.orderId and t.groupId = o.groupId and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND p.orderSplitId = o.id AND p.businessDate = ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL
				+ " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsInVoidBillByBusinessDateAndSession(
			long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE p.id = s.paymentId AND (s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_VOID
				+ " or s.paymentTypeId = " +
				+ ParamConst.SETTLEMENT_TYPE_REFUND
				+ " ) and p.type = "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " AND p.orderId = t.orderId AND p.orderId = o.id AND p.businessDate = ? and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " and o.sessionStatus = ? and o.createTime > ?  AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL + " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsInVoidSplitBillByBusinessDateAndSession(
			long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.OrderSplit
				+ " o "
				+ "WHERE p.id = s.paymentId AND (s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_VOID
				+ " or s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_REFUND
				+ " ) and p.type = "
				+ ParamConst.BILL_TYPE_SPLIT
				+ " AND p.orderId = t.orderId  and t.groupId = o.groupId AND p.orderSplitId = o.id and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND p.businessDate = ? and o.sessionStatus = ? and o.createTime > ?  AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL + " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	public static Map<Integer, Map<String, String>> getModifiersInVoidSplitBillByBusinessDateAndSession(
			long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT om.modifierId, sum(om.modifierNum), sum(om.modifierPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.OrderModifier
				+ " om, "
				+ TableNames.OrderSplit
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_VOID
				+" and p.type = "
				+ ParamConst.BILL_TYPE_SPLIT
				+ " AND p.orderId = t.orderId  and t.groupId = o.groupId AND p.orderSplitId = o.id and s.isActive = "
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE
				+ " AND p.businessDate = ? and o.sessionStatus = ? and o.createTime > ? AND om.orderDetailId = t.id AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL + " group by om.modifierId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer modifierId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(modifierId, map);
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

	public static Map<Integer, Map<String, String>> getItemsInFocBillByBusinessDate(
			long businessDate) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
				+ " and p.type = "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " AND p.orderId = t.orderId AND p.orderId = o.id and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND p.businessDate = ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL + " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsInFocSplitBillByBusinessDate(
			long businessDate) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.OrderSplit
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
				+ " and p.type = "
				+ ParamConst.BILL_TYPE_SPLIT
				+ " AND p.orderId = t.orderId and t.groupId = o.groupId and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND p.orderSplitId = o.id AND p.businessDate = ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL + " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsInFocBillByBusinessDateAndSession(
			long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.Order
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
				+" and p.type = "
				+ ParamConst.BILL_TYPE_UN_SPLIT
				+ " AND p.orderId = t.orderId AND p.orderId = o.id AND p.businessDate = ? and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " and o.sessionStatus = ? and o.createTime > ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL + " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static Map<Integer, Map<String, String>> getItemsInFocSplitBillByBusinessDateAndSession(
			long businessDate, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		String sql = "SELECT t.itemId, sum(t.itemNum), sum(t.realPrice) FROM "
				+ TableNames.Payment
				+ " p, "
				+ TableNames.PaymentSettlement
				+ " s, "
				+ TableNames.OrderDetail
				+ " t, "
				+ TableNames.OrderSplit
				+ " o "
				+ "WHERE p.id = s.paymentId AND s.paymentTypeId = "
				+ ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
				+" and p.type = "
				+ ParamConst.BILL_TYPE_SPLIT
				+ " AND p.orderId = t.orderId and t.groupId = o.groupId AND p.orderSplitId = o.id and s.isActive = " 
				+ ParamConst.PAYMENT_SETT_IS_ACTIVE 
				+ " AND p.businessDate = ? and o.sessionStatus = ? and o.createTime > ? AND t.orderDetailType = "
				+ ParamConst.ORDERDETAIL_TYPE_GENERAL + " group by t.itemId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();

		try {
			cursor = db.rawQuery(sql,
					new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Integer itemId = cursor.getInt(0);
				Integer sumItemNum = cursor.getInt(1);
				String sumRealPrice = cursor.getString(2) == null ? "0.00"
						: cursor.getString(2);
				map.put("sumItemNum", sumItemNum + "");
				map.put("sumRealPrice", sumRealPrice);
				result.put(itemId, map);
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
	
	public static void deleteAllNoActiveSettlement(Payment payment) {

		String sql = "delete from " + TableNames.PaymentSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_NO_ACTIVE;
		String sql1 = "delete from " + TableNames.CardsSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_NO_ACTIVE;
		String sql2 = "delete from " + TableNames.BohHoldSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_NO_ACTIVE;
		String sql3 = "delete from " + TableNames.VoidSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_NO_ACTIVE;
		String sql4 = "delete from " + TableNames.NonChargableSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_NO_ACTIVE;
		String sql5 = "delete from " + TableNames.NetsSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_NO_ACTIVE;
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql1,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql2,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql3,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql4,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql5,
					new Object[] { payment.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}


	public static void deleteAllSettlement(Payment payment) {

		String sql = "delete from " + TableNames.PaymentSettlement
				+ " where paymentId = ? ";
		String sql1 = "delete from " + TableNames.CardsSettlement
				+ " where paymentId = ? ";
		String sql2 = "delete from " + TableNames.BohHoldSettlement
				+ " where paymentId = ? ";
		String sql4 = "delete from " + TableNames.NonChargableSettlement
				+ " where paymentId = ? ";
		String sql5 = "delete from " + TableNames.NetsSettlement
				+ " where paymentId = ? ";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql1,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql2,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql4,
					new Object[] { payment.getId() });
			SQLExe.getDB().execSQL(sql5,
					new Object[] { payment.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void deleteAllSettlement() {

		String sql = "delete from " + TableNames.PaymentSettlement;
		String sql1 = "delete from " + TableNames.CardsSettlement;
		String sql2 = "delete from " + TableNames.BohHoldSettlement;
		String sql4 = "delete from " + TableNames.NonChargableSettlement;
		String sql5 = "delete from " + TableNames.NetsSettlement;
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] {});
			SQLExe.getDB().execSQL(sql1,
					new Object[] { });
			SQLExe.getDB().execSQL(sql2,
					new Object[] {});
			SQLExe.getDB().execSQL(sql4,
					new Object[] {});
			SQLExe.getDB().execSQL(sql5,
					new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void regainPaymentSettlementByPayment(Payment payment) {
		String sql = "delete from " + TableNames.PaymentSettlement
				+ " where paymentId = ? and isActive = " + ParamConst.PAYMENT_SETT_IS_ACTIVE;
		String sql1 = "update " + TableNames.PaymentSettlement
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

	public static void updatePaymentAmount( String paymentAmount, String cashChange,  int paymentId){

		String sql = "update " + TableNames.PaymentSettlement + " set paidAmount = ?, totalAmount = ?, cashChange = ? where paymentId = ? " ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {paymentAmount, paymentAmount, cashChange, paymentId});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateSplitOrderPaymentAmount( String paymentAmount, String cashChange, int paymentId){

		String sql = "update " + TableNames.PaymentSettlement + " set paidAmount = ?, totalAmount = ?, cashChange = ? where paymentId = ? " ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {paymentAmount, paymentAmount, cashChange, paymentId});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
