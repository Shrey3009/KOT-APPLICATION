package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderSplitSQL {

	public static void add(OrderSplit orderSplit) {
		if (orderSplit == null) {
			return;
		}
		calculate(OrderSQL.getOrder(orderSplit.getOrderId()), orderSplit);
		try {
			String sql = "insert into "
					+ TableNames.OrderSplit
					+ "(id,orderId,orderOriginId,userId,persons,orderStatus,subTotal,"
					+ "taxAmount,discountAmount,total,sessionStatus,restId, "
					+ "revenueId,tableId,createTime,updateTime, sysCreateTime, sysUpdateTime, groupId,"
					+ "inclusiveTaxName, inclusiveTaxPrice, inclusiveTaxPercentage, splitByPax)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
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
							orderSplit.getSplitByPax()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateOrderSplitByOrder(Order order,OrderSplit orderSplit){
		try {
			calculate(order, orderSplit);
			update(orderSplit);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void update(OrderSplit orderSplit){
		if (orderSplit == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.OrderSplit
					+ "(id,orderId,orderOriginId,userId,persons,orderStatus,subTotal,"
					+ "taxAmount,discountAmount,total,sessionStatus,restId, "
					+ "revenueId,tableId,createTime,updateTime, sysCreateTime, sysUpdateTime, groupId,"
					+ "inclusiveTaxName, inclusiveTaxPrice, inclusiveTaxPercentage, splitByPax)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
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
							orderSplit.getSplitByPax()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void update(SQLiteDatabase db, OrderSplit orderSplit){
		if (orderSplit == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.OrderSplit
					+ "(id,orderId,orderOriginId,userId,persons,orderStatus,subTotal,"
					+ "taxAmount,discountAmount,total,sessionStatus,restId, "
					+ "revenueId,tableId,createTime,updateTime, sysCreateTime, sysUpdateTime, groupId,"
					+ "inclusiveTaxName, inclusiveTaxPrice, inclusiveTaxPercentage, splitByPax)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
							orderSplit.getSplitByPax()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static OrderSplit get(Integer id) {
		OrderSplit orderSplit = null;
		String sql = "select * from " + TableNames.OrderSplit + " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[] { id + "" });
			if (cursor.moveToFirst()) {
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
				return orderSplit;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderSplit;
	}

	public static void updateOrderSqlitList(List<OrderSplit> orderSplits) {
		if (orderSplits == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.OrderSplit
					+ "(id,orderId,orderOriginId,userId,persons,orderStatus,subTotal,"
					+ "taxAmount,discountAmount,total,sessionStatus,restId, "
					+ "revenueId,tableId,createTime,updateTime, sysCreateTime, sysUpdateTime, groupId,"
					+ "inclusiveTaxName, inclusiveTaxPrice, inclusiveTaxPercentage, splitByPax)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (OrderSplit orderSplit : orderSplits) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
						orderSplit.getId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
						orderSplit.getOrderId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
						orderSplit.getOrderOriginId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
						orderSplit.getUserId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
						orderSplit.getPersons());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
						orderSplit.getOrderStatus());
				SQLiteStatementHelper.bindString(sqLiteStatement, 7,
						orderSplit.getSubTotal());
				SQLiteStatementHelper.bindString(sqLiteStatement, 8,
						orderSplit.getTaxAmount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 9,
						orderSplit.getDiscountAmount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 10,
						orderSplit.getTotal());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
						orderSplit.getSessionStatus());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
						orderSplit.getRestId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
						orderSplit.getRevenueId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 14,
						orderSplit.getTableId());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
						orderSplit.getCreateTime());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
						orderSplit.getUpdateTime());
				SQLiteStatementHelper.bindString(sqLiteStatement, 17,
						orderSplit.getSysCreateTime());
				SQLiteStatementHelper.bindString(sqLiteStatement, 18,
						orderSplit.getSysUpdateTime());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 19,
						orderSplit.getGroupId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 20,
						orderSplit.getInclusiveTaxName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 21,
						orderSplit.getInclusiveTaxPrice());
				SQLiteStatementHelper.bindString(sqLiteStatement, 22,
						orderSplit.getInclusiveTaxPercentage());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
						orderSplit.getSplitByPax());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	public static List<OrderSplit> getOrderSplits(Order order) {
		ArrayList<OrderSplit> result = new ArrayList<OrderSplit>();
		String sql = "select * from " + TableNames.OrderSplit
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

	public static List<OrderSplit> getAllOrderSplits(Order order) {
		ArrayList<OrderSplit> result = new ArrayList<OrderSplit>();
		String sql = "select * from " + TableNames.OrderSplit
				+ " where orderId = ? ";
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

	public static List<OrderSplit> getFinishedOrderSplits(int orderId) {
		ArrayList<OrderSplit> result = new ArrayList<OrderSplit>();
		String sql = "select * from " + TableNames.OrderSplit
				+ " where orderId = ? and orderStatus = " + ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED
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
		String sql = "select * from " + TableNames.OrderSplit
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
	
	private static void calculate(Order order, OrderSplit orderSplit) {
		List<OrderDetail> orderDetails = OrderDetailSQL.getGeneralOrderDetails(order.getId());
		OrderHelper.setOrderSplitSubTotal(order, orderSplit, orderDetails);
		OrderHelper.setOrderSplitDiscount(order, orderSplit, orderDetails);
		OrderHelper.setOrderSplitTax(order, orderSplit, orderDetails);
		OrderHelper.setOrderSplitTotal(order, orderSplit, orderDetails);
		OrderHelper.setOrderSplitInclusiveTaxPrice(orderSplit);
	}
	
	public static OrderSplit getOrderSplitByOrderAndGroupId(Order order, int groupId){
		String sql = "select * from " + TableNames.OrderSplit
				+ " where orderId = ? and groupId = ?";
		Cursor cursor = null;
		OrderSplit orderSplit = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {order.getId() + "", groupId + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return orderSplit;
			}
			if (cursor.moveToFirst()) {
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
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderSplit;
	}

	public static int getOrderSplitsCountByOrder(Order order) {
		String sql = "select count(*) from " + TableNames.OrderSplit
				+ " where orderId = ?";
		Cursor cursor = null;
		int orderQty = 0;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {order.getId().toString()});
			int count = cursor.getCount();
			if (count < 1) {
				return 0;
			}
			if (cursor.moveToFirst()) {
				orderQty = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderQty;
	}
	
	public static String getSumOrderSplitInclusiveTaxPrice(Order order) {
//		String sql = "select sum(os.inclusiveTaxPrice) from "
//				+ TableNames.OrderSplit
//				+ " os, "
//				+ TableNames.Payment
//				+ " p,  where os.id = p.orderSplitId  and os.orderId = ? AND NOT EXISTS ( SELECT 0 FROM "
//				+  TableNames.PaymentSettlement
//				+ " ps where ps.paymentId = p.id and (ps.paymentTypeId = "
//				+ ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
//				+ " or ps.paymentTypeId = "
//				+ ParamConst.SETTLEMENT_TYPE_VOID
//				+ " or ps.paymentTypeId = "
//				+ ParamConst.SETTLEMENT_TYPE_REFUND
//				+ " ))";
		String sql = "select sum(os.inclusiveTaxPrice) from "
				+ TableNames.OrderSplit
				+ " os, "
				+ TableNames.Payment
				+ " p, "
				+  TableNames.PaymentSettlement
				+ " ps  where os.orderId = ? and os.id = p.orderSplitId and ps.paymentId = p.id and ps.paymentTypeId <> "
				+ ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
				+ " and ps.paymentTypeId <> "
				+ ParamConst.SETTLEMENT_TYPE_VOID
				+ " and ps.paymentTypeId <> "
				+ ParamConst.SETTLEMENT_TYPE_REFUND
				+ " group by os.id";
		Cursor cursor = null;
		String result = ParamConst.DOUBLE_ZERO;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {order.getId().toString()});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
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
	
	public static int getUnDoneOrderSplitsCountByOrder(int orderId, boolean splitPax) {
		String sql = "select count(s.id) from "
				+ TableNames.OrderSplit
				+ " s, " + TableNames.OrderDetail
				+ " d where s.orderId = ? and d.groupId = s.groupId and d.orderId = s.orderId and s.orderStatus <> "
				+ ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED + " group by s.groupId ";
		if(splitPax) {
			sql = "select count(id) from "
					+ TableNames.OrderSplit
					+ " where orderId = ? and orderStatus <> "
					+ ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED;
		}
		Cursor cursor = null;
		int orderQty = 0;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {orderId + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return 0;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderQty = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return orderQty;
	}


	public static void updateOrderSplitStatus( int orderStatus, int id){

		String sql = "update " + TableNames.OrderSplit + " set orderStatus = ? where id = ?" ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {orderStatus, id});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public static void delete(OrderSplit orderSplit) {
		String sql = "delete from " + TableNames.OrderSplit + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderSplit.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteBySpliteIdList(int orderId, String spliteId) {
		String sql = "delete from " + TableNames.OrderSplit + " where orderId = ? and id not in ("
				+ spliteId
				+ ")";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderId });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteOrderSplitByOrderAndGroupId(int orderId, int groupId){
		String sql = "delete from " + TableNames.OrderSplit + " where orderId = ? and groupId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {orderId + "",  groupId + ""});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteOrderSplitByOrderId(int orderId){
		String sql = "delete from " + TableNames.OrderSplit + " where orderId = ? ";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {orderId});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteOrderSplitPaxByOrderId(Order order){
		List<OrderSplit> orderSplits = getOrderSplits(order);
		for(OrderSplit orderSplit : orderSplits){
			if(orderSplit.getSplitByPax() <= 0){
				return;
			}
		}
		for(OrderSplit orderSplit : orderSplits){
			OrderBillSQL.getOrderBillByOrderSplit(orderSplit);
			Payment payment = PaymentSQL.getPaymentByOrderSplitId(orderSplit.getId());
			if(payment != null) {
				PaymentSettlementSQL.deleteAllSettlement(payment);
				PaymentSQL.deletePayment(payment);
			}
			String sql = "delete from " + TableNames.OrderSplit + " where id = ? and splitByPax > " + ParamConst.SPLIT_BY_PAX_FALSE;
			try {
				SQLExe.getDB().execSQL(sql, new Object[] {orderSplit.getId()});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
