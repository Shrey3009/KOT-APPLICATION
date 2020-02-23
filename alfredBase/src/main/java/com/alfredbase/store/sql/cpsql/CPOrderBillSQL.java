package com.alfredbase.store.sql.cpsql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.List;

public class CPOrderBillSQL {

	public static void add(OrderBill orderBill) {
		if (orderBill == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.CPOrderBill
					+ "(id, billNo, orderId, orderSplitId, type, restaurantId, revenueId, userId, createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderBill.getId(), orderBill.getBillNo(),
							orderBill.getOrderId(),
							orderBill.getOrderSplitId(), orderBill.getType(),
							orderBill.getRestaurantId(),
							orderBill.getRevenueId(), orderBill.getUserId(),
							orderBill.getCreateTime(),
							orderBill.getUpdateTime() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void add(SQLiteDatabase db, OrderBill orderBill) {
		if (orderBill == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.CPOrderBill
					+ "(id, billNo, orderId, orderSplitId, type, restaurantId, revenueId, userId, createTime, updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(
					sql,
					new Object[] { orderBill.getId(), orderBill.getBillNo(),
							orderBill.getOrderId(),
							orderBill.getOrderSplitId(), orderBill.getType(),
							orderBill.getRestaurantId(),
							orderBill.getRevenueId(), orderBill.getUserId(),
							orderBill.getCreateTime(),
							orderBill.getUpdateTime() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static OrderBill getOrderBill(Integer orderBillID) {
		OrderBill orderBill = null;
		String sql = "select * from " + TableNames.CPOrderBill + " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { orderBillID + "" });
			if (cursor.moveToFirst()) {
				orderBill = new OrderBill();
				orderBill.setId(cursor.getInt(0));
				orderBill.setBillNo(cursor.getInt(1));
				orderBill.setOrderId(cursor.getInt(2));
				orderBill.setOrderSplitId(cursor.getInt(3));
				orderBill.setType(cursor.getInt(4));
				orderBill.setRestaurantId(cursor.getInt(5));
				orderBill.setRevenueId(cursor.getInt(6));
				orderBill.setUserId(cursor.getInt(7));
				orderBill.setCreateTime(cursor.getLong(8));
				orderBill.setUpdateTime(cursor.getLong(9));
				return orderBill;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderBill;
	}

	public static OrderBill getOrderBillByOrder(Order order) {
		OrderBill orderBill = null;
		String sql = "select * from " + TableNames.CPOrderBill
				+ " where orderId = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { order.getId() + "" });
			if (cursor.moveToFirst()) {
				orderBill = new OrderBill();
				orderBill.setId(cursor.getInt(0));
				orderBill.setBillNo(cursor.getInt(1));
				orderBill.setOrderId(cursor.getInt(2));
				orderBill.setOrderSplitId(cursor.getInt(3));
				orderBill.setType(cursor.getInt(4));
				orderBill.setRestaurantId(cursor.getInt(5));
				orderBill.setRevenueId(cursor.getInt(6));
				orderBill.setUserId(cursor.getInt(7));
				orderBill.setCreateTime(cursor.getLong(8));
				orderBill.setUpdateTime(cursor.getLong(9));
				return orderBill;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderBill;
	}

	public static OrderBill getOrderBillByOnlyOrder(int orderId) {
		OrderBill orderBill = null;
		String sql = "select * from " + TableNames.CPOrderBill
				+ " where orderId = ? and type = " + ParamConst.BILL_TYPE_UN_SPLIT;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { orderId + "" });
			if (cursor.moveToFirst()) {
				orderBill = new OrderBill();
				orderBill.setId(cursor.getInt(0));
				orderBill.setBillNo(cursor.getInt(1));
				orderBill.setOrderId(cursor.getInt(2));
				orderBill.setOrderSplitId(cursor.getInt(3));
				orderBill.setType(cursor.getInt(4));
				orderBill.setRestaurantId(cursor.getInt(5));
				orderBill.setRevenueId(cursor.getInt(6));
				orderBill.setUserId(cursor.getInt(7));
				orderBill.setCreateTime(cursor.getLong(8));
				orderBill.setUpdateTime(cursor.getLong(9));
				return orderBill;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderBill;
	}

	public static List<OrderBill> getAllOrderBillByOrder(Order order) {
		List<OrderBill> orderBills = new ArrayList<OrderBill>();
		OrderBill orderBill = null;
		String sql = "select * from " + TableNames.CPOrderBill
				+ " where orderId = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { order.getId() + "" });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				orderBill = new OrderBill();
				orderBill.setId(cursor.getInt(0));
				orderBill.setBillNo(cursor.getInt(1));
				orderBill.setOrderId(cursor.getInt(2));
				orderBill.setOrderSplitId(cursor.getInt(3));
				orderBill.setType(cursor.getInt(4));
				orderBill.setRestaurantId(cursor.getInt(5));
				orderBill.setRevenueId(cursor.getInt(6));
				orderBill.setUserId(cursor.getInt(7));
				orderBill.setCreateTime(cursor.getLong(8));
				orderBill.setUpdateTime(cursor.getLong(9));
				orderBills.add(orderBill);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderBills;
	}
	
	public static OrderBill getOrderBillByOrderSplit(OrderSplit orderSplit) {
		OrderBill orderBill = null;
		String sql = "select * from " + TableNames.CPOrderBill
				+ " where orderSplitId = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { orderSplit.getId() + "" });
			if (cursor.moveToFirst()) {
				orderBill = new OrderBill();
				orderBill.setId(cursor.getInt(0));
				orderBill.setBillNo(cursor.getInt(1));
				orderBill.setOrderId(cursor.getInt(2));
				orderBill.setOrderSplitId(cursor.getInt(3));
				orderBill.setType(cursor.getInt(4));
				orderBill.setRestaurantId(cursor.getInt(5));
				orderBill.setRevenueId(cursor.getInt(6));
				orderBill.setUserId(cursor.getInt(7));
				orderBill.setCreateTime(cursor.getLong(8));
				orderBill.setUpdateTime(cursor.getLong(9));
				return orderBill;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderBill;
	}
	
	public static OrderBill getOrderBillByBillNo(int billNo) {
		OrderBill orderBill = null;
		String sql = "select * from " + TableNames.CPOrderBill
				+ " where billNo = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { billNo + "" });
			if (cursor.moveToFirst()) {
				orderBill = new OrderBill();
				orderBill.setId(cursor.getInt(0));
				orderBill.setBillNo(cursor.getInt(1));
				orderBill.setOrderId(cursor.getInt(2));
				orderBill.setOrderSplitId(cursor.getInt(3));
				orderBill.setType(cursor.getInt(4));
				orderBill.setRestaurantId(cursor.getInt(5));
				orderBill.setRevenueId(cursor.getInt(6));
				orderBill.setUserId(cursor.getInt(7));
				orderBill.setCreateTime(cursor.getLong(8));
				orderBill.setUpdateTime(cursor.getLong(9));
				return orderBill;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderBill;
	}
	
//	public static int getOrderBillCountByTime(long businessDate) {
//		int count = 0 ;
//		String sql = "select count(*) from " + TableNames.CPOrderBill
//				+ " where orderId in ( select id from " + TableNames.Order + " where businessDate = ?)";
//		Cursor cursor = null;
//		try {
//			cursor = SQLExe.getDB().rawQuery(sql,
//					new String[] { String.valueOf(businessDate)});
//			if (cursor.moveToFirst()) {
//				count = cursor.getInt(0);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return count;
//	}
	
//	public static int getOrderBillCountByTime(long businessDate, SessionStatus sessionStatus) {
//		int count = 0 ;
//		String sql = "select count(*) from " + TableNames.CPOrderBill
//				+ " where orderId in ( select id from " + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ?)";
//		Cursor cursor = null;
//		try {
//			cursor = SQLExe.getDB().rawQuery(sql,
//					new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())});
//			if (cursor.moveToFirst()) {
//				count = cursor.getInt(0);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return count;
//	}

	public static void deleteOrderBill(OrderBill orderBill) {
		String sql = "delete from " + TableNames.CPOrderBill + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderBill.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteOrderBillByOrder(Order order) {
		String sql = "delete from " + TableNames.CPOrderBill + " where orderId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { order.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
