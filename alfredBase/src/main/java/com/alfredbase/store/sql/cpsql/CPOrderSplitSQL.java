package com.alfredbase.store.sql.cpsql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.List;

public class CPOrderSplitSQL {



	public static void update(SQLiteDatabase db, OrderSplit orderSplit, int oldOrderSplitId){
		if (orderSplit == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.CPOrderSplit
					+ "(id,orderId,orderOriginId,userId,persons,orderStatus,subTotal,"
					+ "taxAmount,discountAmount,total,sessionStatus,restId, "
					+ "revenueId,tableId,createTime,updateTime, sysCreateTime, sysUpdateTime, groupId,"
					+ "inclusiveTaxName, inclusiveTaxPrice, inclusiveTaxPercentage, splitByPax, oldOrderSplitId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(
					sql,
					new Object[] { orderSplit.getId(), orderSplit.getOrderId(),
							orderSplit.getOrderOriginId(),
							orderSplit.getUserId(), orderSplit.getPersons(),
							orderSplit.getOrderStatus(),
							orderSplit.getSubTotal(),
							orderSplit.getTaxAmount(),
							orderSplit.getDiscountAmount(),
							orderSplit.getTotal(),
							orderSplit.getSessionStatus(),
							orderSplit.getRestId(), orderSplit.getRevenueId(),
							orderSplit.getTableId(),
							orderSplit.getCreateTime(),
							orderSplit.getUpdateTime(),
							orderSplit.getSysCreateTime(),
							orderSplit.getSysUpdateTime(),
							orderSplit.getGroupId(),
							orderSplit.getInclusiveTaxName(),
							orderSplit.getInclusiveTaxPrice(),
							orderSplit.getInclusiveTaxPercentage(),
							orderSplit.getSplitByPax(),
							oldOrderSplitId});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static List<OrderSplit> getOrderSplits(Order order) {
		ArrayList<OrderSplit> result = new ArrayList<OrderSplit>();
		String sql = "select * from " + TableNames.CPOrderSplit
				+ " where orderId = ? and groupId <> " + ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {order.getId() + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderSplit orderSplit = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderSplit = new OrderSplit();
				orderSplit.setId(cursor.getInt(0));
				orderSplit.setOrderId(cursor.getInt(1));
				orderSplit.setOrderOriginId(cursor.getInt(2));
				orderSplit.setUserId(cursor.getInt(3));
				orderSplit.setPersons(cursor.getInt(4));
				orderSplit.setOrderStatus(cursor.getInt(5));
				orderSplit.setSubTotal(cursor.getString(6));
				orderSplit.setTaxAmount(cursor.getString(7));
				orderSplit.setDiscountAmount(cursor.getString(8));
				orderSplit.setTotal(cursor.getString(9));
				orderSplit.setSessionStatus(cursor.getInt(10));
				orderSplit.setRestId(cursor.getInt(11));
				orderSplit.setRevenueId(cursor.getInt(12));
				orderSplit.setTableId(cursor.getInt(13));
				orderSplit.setCreateTime(cursor.getLong(14));
				orderSplit.setUpdateTime(cursor.getLong(15));
				orderSplit.setSysCreateTime(cursor.getString(16));
				orderSplit.setSysUpdateTime(cursor.getString(17));
				orderSplit.setGroupId(cursor.getInt(18));
				orderSplit.setInclusiveTaxName(cursor.getString(19));
				orderSplit.setInclusiveTaxPrice(cursor.getString(20));
				orderSplit.setInclusiveTaxPercentage(cursor.getString(21));
				orderSplit.setSplitByPax(cursor.getInt(22));
				orderSplit.setOldOrderSplitId(cursor.getInt(23));
				result.add(orderSplit);
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
	public static List<OrderSplit> getOrderSplits(int orderId) {
		ArrayList<OrderSplit> result = new ArrayList<OrderSplit>();
		String sql = "select * from " + TableNames.CPOrderSplit
				+ " where orderId = ? and groupId <> " + ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {orderId + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderSplit orderSplit = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderSplit = new OrderSplit();
				orderSplit.setId(cursor.getInt(0));
				orderSplit.setOrderId(cursor.getInt(1));
				orderSplit.setOrderOriginId(cursor.getInt(2));
				orderSplit.setUserId(cursor.getInt(3));
				orderSplit.setPersons(cursor.getInt(4));
				orderSplit.setOrderStatus(cursor.getInt(5));
				orderSplit.setSubTotal(cursor.getString(6));
				orderSplit.setTaxAmount(cursor.getString(7));
				orderSplit.setDiscountAmount(cursor.getString(8));
				orderSplit.setTotal(cursor.getString(9));
				orderSplit.setSessionStatus(cursor.getInt(10));
				orderSplit.setRestId(cursor.getInt(11));
				orderSplit.setRevenueId(cursor.getInt(12));
				orderSplit.setTableId(cursor.getInt(13));
				orderSplit.setCreateTime(cursor.getLong(14));
				orderSplit.setUpdateTime(cursor.getLong(15));
				orderSplit.setSysCreateTime(cursor.getString(16));
				orderSplit.setSysUpdateTime(cursor.getString(17));
				orderSplit.setGroupId(cursor.getInt(18));
				orderSplit.setInclusiveTaxName(cursor.getString(19));
				orderSplit.setInclusiveTaxPrice(cursor.getString(20));
				orderSplit.setInclusiveTaxPercentage(cursor.getString(21));
				orderSplit.setSplitByPax(cursor.getInt(22));
				orderSplit.setOldOrderSplitId(cursor.getInt(23));
				result.add(orderSplit);
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



	public static List<OrderSplit> getUnFinishedOrderSplits(int orderId) {
		ArrayList<OrderSplit> result = new ArrayList<OrderSplit>();
		String sql = "select * from " + TableNames.CPOrderSplit
				+ " where orderId = ? and orderStatus <> " + ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED
				+ " and groupId <> " + ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {orderId + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderSplit orderSplit = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderSplit = new OrderSplit();
				orderSplit.setId(cursor.getInt(0));
				orderSplit.setOrderId(cursor.getInt(1));
				orderSplit.setOrderOriginId(cursor.getInt(2));
				orderSplit.setUserId(cursor.getInt(3));
				orderSplit.setPersons(cursor.getInt(4));
				orderSplit.setOrderStatus(cursor.getInt(5));
				orderSplit.setSubTotal(cursor.getString(6));
				orderSplit.setTaxAmount(cursor.getString(7));
				orderSplit.setDiscountAmount(cursor.getString(8));
				orderSplit.setTotal(cursor.getString(9));
				orderSplit.setSessionStatus(cursor.getInt(10));
				orderSplit.setRestId(cursor.getInt(11));
				orderSplit.setRevenueId(cursor.getInt(12));
				orderSplit.setTableId(cursor.getInt(13));
				orderSplit.setCreateTime(cursor.getLong(14));
				orderSplit.setUpdateTime(cursor.getLong(15));
				orderSplit.setSysCreateTime(cursor.getString(16));
				orderSplit.setSysUpdateTime(cursor.getString(17));
				orderSplit.setGroupId(cursor.getInt(18));
				orderSplit.setInclusiveTaxName(cursor.getString(19));
				orderSplit.setInclusiveTaxPrice(cursor.getString(20));
				orderSplit.setInclusiveTaxPercentage(cursor.getString(21));
				orderSplit.setSplitByPax(cursor.getInt(22));
				orderSplit.setOldOrderSplitId(cursor.getInt(23));
				result.add(orderSplit);
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
	
	public static void delete(OrderSplit orderSplit) {
		String sql = "delete from " + TableNames.CPOrderSplit + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderSplit.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteOrderSplitByOrderAndGroupId(int orderId, int groupId){
		String sql = "delete from " + TableNames.CPOrderSplit + " where orderId = ? and groupId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {orderId + "",  groupId + ""});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteOrderSplitByOrderId(int orderId){
		String sql = "delete from " + TableNames.CPOrderSplit + " where orderId = ? ";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {orderId});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
