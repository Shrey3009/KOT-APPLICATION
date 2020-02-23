package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderModifierSQL {

	public static void addOrderModifier(OrderModifier orderModifier) {
		if (orderModifier == null) {
			return;
		}
		try {
			String sql = "insert into "
					+ TableNames.OrderModifier
					+ "(orderId, orderDetailId, orderOriginId, userId, itemId, modifierId, modifierNum, status, modifierPrice, createTime, updateTime, printerId, modifierItemPrice)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderModifier.getOrderId(),
							orderModifier.getOrderDetailId(),
							orderModifier.getOrderOriginId(),
							orderModifier.getUserId(),
							orderModifier.getItemId(),
							orderModifier.getModifierId(),
							orderModifier.getModifierNum(),
							orderModifier.getStatus(),
							orderModifier.getModifierPrice(),
							orderModifier.getCreateTime(),
							orderModifier.getUpdateTime(),
							orderModifier.getPrinterId(),
							orderModifier.getModifierItemPrice()});
			updateOrderDetail(orderModifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void addOrderModifierForDiner(OrderModifier orderModifier) {
		if (orderModifier == null) {
			return;
		}
		try {
			String sql = "insert into "
					+ TableNames.OrderModifier
					+ "(orderId, orderDetailId, orderOriginId, userId, itemId, modifierId, modifierNum, status, modifierPrice, createTime, updateTime, printerId,modifierItemPrice)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderModifier.getOrderId(),
							orderModifier.getOrderDetailId(),
							orderModifier.getOrderOriginId(),
							orderModifier.getUserId(),
							orderModifier.getItemId(),
							orderModifier.getModifierId(),
							orderModifier.getModifierNum(),
							orderModifier.getStatus(),
							orderModifier.getModifierPrice(),
							orderModifier.getCreateTime(),
							orderModifier.getUpdateTime(),
							orderModifier.getPrinterId(),
							orderModifier.getModifierItemPrice()});
//			updateOrderDetailForWaiter(orderModifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addOrderModifierForWaiter(OrderModifier orderModifier) {
		if (orderModifier == null) {
			return;
		}
		try {
			String sql = "insert into "
					+ TableNames.OrderModifier
					+ "(orderId, orderDetailId, orderOriginId, userId, itemId, modifierId, modifierNum, status, modifierPrice, createTime, updateTime, printerId,modifierItemPrice)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderModifier.getOrderId(),
							orderModifier.getOrderDetailId(),
							orderModifier.getOrderOriginId(),
							orderModifier.getUserId(),
							orderModifier.getItemId(),
							orderModifier.getModifierId(),
							orderModifier.getModifierNum(),
							orderModifier.getStatus(),
							orderModifier.getModifierPrice(),
							orderModifier.getCreateTime(),
							orderModifier.getUpdateTime(),
							orderModifier.getPrinterId(),
							orderModifier.getModifierItemPrice()});
//			updateOrderDetailForWaiter(orderModifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateOrderModifier(OrderModifier orderModifier) {
		if (orderModifier == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.OrderModifier
					+ "(id,orderId, orderDetailId, orderOriginId, userId, itemId, modifierId, modifierNum, status, modifierPrice, createTime, updateTime, printerId, modifierItemPrice)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderModifier.getId(),
							orderModifier.getOrderId(),
							orderModifier.getOrderDetailId(),
							orderModifier.getOrderOriginId(),
							orderModifier.getUserId(),
							orderModifier.getItemId(),
							orderModifier.getModifierId(),
							orderModifier.getModifierNum(),
							orderModifier.getStatus(),
							orderModifier.getModifierPrice(),
							orderModifier.getCreateTime(),
							orderModifier.getUpdateTime(),
							orderModifier.getPrinterId(),
							orderModifier.getModifierItemPrice()});
			updateOrderDetail(orderModifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateOrderModifierForWaiter(OrderModifier orderModifier) {
		if (orderModifier == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.OrderModifier
					+ "(id,orderId, orderDetailId, orderOriginId, userId, itemId, modifierId, modifierNum, status, modifierPrice, createTime, updateTime, printerId, modifierItemPrice)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderModifier.getId(),
							orderModifier.getOrderId(),
							orderModifier.getOrderDetailId(),
							orderModifier.getOrderOriginId(),
							orderModifier.getUserId(),
							orderModifier.getItemId(),
							orderModifier.getModifierId(),
							orderModifier.getModifierNum(),
							orderModifier.getStatus(),
							orderModifier.getModifierPrice(),
							orderModifier.getCreateTime(),
							orderModifier.getUpdateTime(),
							orderModifier.getPrinterId(),
							orderModifier.getModifierItemPrice()});
			updateOrderDetailForWaiter(orderModifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void updateOrderDetail(OrderModifier orderModifier) {
		OrderDetail orderDetail = OrderDetailSQL.getOrderDetail(orderModifier
				.getOrderDetailId());
		OrderDetailSQL.updateOrderDetailAndOrder(orderDetail);
	}
	
	private static void updateOrderDetailForWaiter(OrderModifier orderModifier) {
		OrderDetail orderDetail = OrderDetailSQL.getOrderDetail(orderModifier
				.getOrderDetailId());
		OrderDetailSQL.updateOrderDetailAndOrderForWaiter(orderDetail);
	}

	public static void addOrderModifierList(
			List<OrderModifier> orderModifierList) {
		if (orderModifierList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.OrderModifier
					+ "( orderId, orderDetailId, orderOriginId, userId, itemId, modifierId, modifierNum, status, modifierPrice, createTime, updateTime, printerId, modifierItemPrice)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = SQLExe.getDB().compileStatement(
					sql);
				for (OrderModifier orderModifier : orderModifierList) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							orderModifier.getOrderId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							orderModifier.getOrderDetailId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							orderModifier.getOrderOriginId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							orderModifier.getUserId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							orderModifier.getItemId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							orderModifier.getModifierId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
							orderModifier.getModifierNum());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							orderModifier.getStatus());
					SQLiteStatementHelper.bindString(sqLiteStatement, 9,
							orderModifier.getModifierPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
							orderModifier.getCreateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
							orderModifier.getUpdateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							orderModifier.getPrinterId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 13, 
							orderModifier.getModifierItemPrice());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<OrderModifier> getAllOrderModifier(Order order) {
		ArrayList<OrderModifier> result = new ArrayList<OrderModifier>();
		String sql = "select * from " + TableNames.OrderModifier
				+ " where orderId = ? and status = " + ParamConst.ORDER_MODIFIER_STATUS_NORMAL;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { order.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderModifier orderModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderModifier = new OrderModifier();
				orderModifier.setId(cursor.getInt(0));
				orderModifier.setOrderId(cursor.getInt(1));
				orderModifier.setOrderDetailId(cursor.getInt(2));
				orderModifier.setOrderOriginId(cursor.getInt(3));
				orderModifier.setUserId(cursor.getInt(4));
				orderModifier.setItemId(cursor.getInt(5));
				orderModifier.setModifierId(cursor.getInt(6));
				orderModifier.setModifierNum(cursor.getInt(7));
				orderModifier.setStatus(cursor.getInt(8));
				orderModifier.setModifierPrice(cursor.getString(9));
				orderModifier.setCreateTime(cursor.getLong(10));
				orderModifier.setUpdateTime(cursor.getLong(11));
				orderModifier.setPrinterId(cursor.getInt(12));
				orderModifier.setModifierItemPrice(cursor.getString(13));
				result.add(orderModifier);
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
	
	
    /*Get modifiers unremoved */
	public static ArrayList<OrderModifier> getAllOrderModifierByOrderDetailAndNormal(OrderDetail orderDetail){
			ArrayList<OrderModifier> result = new ArrayList<OrderModifier>();
			String sql = "select * from " + TableNames.OrderModifier
					+ " o,"
					+ TableNames.Modifier
					+ " m"
					+ " where o.orderDetailId = ? and o.status = " 
					+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
					+ " and o.modifierId = m.id and m.type = 1 order by m.categoryId ";
			Cursor cursor = null;
			SQLiteDatabase db = SQLExe.getDB();
			try {
				cursor = db.rawQuery(sql,
						new String[] { orderDetail.getId() + "" });
				int count = cursor.getCount();
				if (count < 1) {
					return result;
				}
				OrderModifier orderModifier = null;
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					orderModifier = new OrderModifier();
					orderModifier.setId(cursor.getInt(0));
					orderModifier.setOrderId(cursor.getInt(1));
					orderModifier.setOrderDetailId(cursor.getInt(2));
					orderModifier.setOrderOriginId(cursor.getInt(3));
					orderModifier.setUserId(cursor.getInt(4));
					orderModifier.setItemId(cursor.getInt(5));
					orderModifier.setModifierId(cursor.getInt(6));
					orderModifier.setModifierNum(cursor.getInt(7));
					orderModifier.setStatus(cursor.getInt(8));
					orderModifier.setModifierPrice(cursor.getString(9));
					orderModifier.setCreateTime(cursor.getLong(10));
					orderModifier.setUpdateTime(cursor.getLong(11));
					orderModifier.setPrinterId(cursor.getInt(12));
					orderModifier.setModifierItemPrice(cursor.getString(13));
					result.add(orderModifier);
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

	public static int getOrderModifierCountByOrderDetailAndNormal(OrderDetail orderDetail){
		int result = 0;
		String sql = "select count(0) from " + TableNames.OrderModifier
				+ " o,"
				+ TableNames.Modifier
				+ " m"
				+ " where o.orderDetailId = ? and o.status = "
				+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL
				+ " and o.modifierId = m.id and m.type = 1 order by m.categoryId ";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { orderDetail.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			if(cursor.moveToFirst()){
				result = cursor.getInt(0);
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
	
	public static ArrayList<OrderModifier> getAllOrderModifierByOrderAndNormal(Order order){
		ArrayList<OrderModifier> result = new ArrayList<OrderModifier>();
		String sql = "select * from " 
				+ TableNames.OrderModifier
				+ " o,"
				+ TableNames.Modifier
				+ " m"
				+ " where o.orderId = ? and o.status = " 
				+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL
				+ " and o.modifierId = m.id and m.type = 1 order by m.categoryId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { order.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderModifier orderModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderModifier = new OrderModifier();
				orderModifier.setId(cursor.getInt(0));
				orderModifier.setOrderId(cursor.getInt(1));
				orderModifier.setOrderDetailId(cursor.getInt(2));
				orderModifier.setOrderOriginId(cursor.getInt(3));
				orderModifier.setUserId(cursor.getInt(4));
				orderModifier.setItemId(cursor.getInt(5));
				orderModifier.setModifierId(cursor.getInt(6));
				orderModifier.setModifierNum(cursor.getInt(7));
				orderModifier.setStatus(cursor.getInt(8));
				orderModifier.setModifierPrice(cursor.getString(9));
				orderModifier.setCreateTime(cursor.getLong(10));
				orderModifier.setUpdateTime(cursor.getLong(11));
				orderModifier.setPrinterId(cursor.getInt(12));
				orderModifier.setModifierItemPrice(cursor.getString(13));
				result.add(orderModifier);
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
	
	public static Map<String, String> getAllOrderModifierByOrderDetailType(int modifierId, long businessDate, int orderDetailStatus) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum(modifierPrice), sum(modifierNum) from " + TableNames.OrderModifier
				+ " where modifierId = ? and orderId in ( select id from " + TableNames.Order + " where businessDate = ? and orderStatus = " + ParamConst.ORDER_STATUS_FINISHED + " ) and orderDetailId in (select id from "+TableNames.OrderDetail+" where orderDetailStatus = ? )";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(modifierId), String.valueOf(businessDate), String.valueOf(orderDetailStatus)});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumModifierPrice", cursor.getString(0));
				result.put("sumModifierNum", String.valueOf(cursor.getInt(1)));
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
	
	public static Map<String, String> getAllOrderModifierByItemAndModifierAndOrderDetailType(int itemId, int modifierId, long businessDate, int orderDetailStatus) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum(modifierPrice), sum(modifierNum) from " + TableNames.OrderModifier
				+ " where itemId = ? and modifierId = ? and orderId in ( select id from " + TableNames.Order + " where businessDate = ? and orderStatus = " + ParamConst.ORDER_STATUS_FINISHED + " ) and orderDetailId in (select id from "+TableNames.OrderDetail+" where orderDetailStatus = ? )";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(itemId), String.valueOf(modifierId), String.valueOf(businessDate), String.valueOf(orderDetailStatus)});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumModifierPrice", cursor.getString(0));
				result.put("sumModifierNum", String.valueOf(cursor.getInt(1)));
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
	
	public static Map<String, String> getAllOrderModifierByOrderDetailType(int modifierId, long businessDate, SessionStatus sessionStatus, int orderDetailStatus) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum(modifierPrice), sum(modifierNum) from " + TableNames.OrderModifier
				+ " where modifierId = ? and orderId in ( select id from " + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ? and orderStatus = " + ParamConst.ORDER_STATUS_FINISHED + " ) and orderDetailId in (select id from "+TableNames.OrderDetail+" where orderDetailStatus = ? )";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(modifierId), String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()), String.valueOf(orderDetailStatus)});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumModifierPrice", cursor.getString(0));
				result.put("sumModifierNum", String.valueOf(cursor.getInt(1)));
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

	public static Map<String, String> getOrderModifierByModifierId(int modifierId, long businessDate) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum(modifierPrice), sum(modifierNum) from " + TableNames.OrderModifier
				+ " where modifierId = ? and orderId in (select id from " + TableNames.Order + " where businessDate = ?)";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(modifierId), String.valueOf(businessDate)});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumModifierPrice", cursor.getString(0));
				result.put("sumModifierNum", String.valueOf(cursor.getInt(1)));
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
	
	public static Map<String, String> getOrderModifierByItemAndModifier(int itemId, int modifierId, long businessDate) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum(modifierPrice), sum(modifierNum), modifierItemPrice from " 
				+ TableNames.OrderModifier
				+ " where itemId = ? and modifierId = ?  and status = " 
				+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
				+ " and orderId in (select id from " 
				+ TableNames.Order + " where businessDate = ?)";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(itemId), String.valueOf(modifierId), String.valueOf(businessDate)});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumModifierPrice", cursor.getString(0));
				result.put("sumModifierNum", String.valueOf(cursor.getInt(1)));
				result.put("modifierItemPrice", cursor.getString(2));
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
	
	public static Map<String, String> getOrderModifierByItemAndModifierAndSession(int itemId, int modifierId, long businessDate, SessionStatus sessionStatus) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum(modifierPrice), sum(modifierNum), modifierItemPrice from " 
				+ TableNames.OrderModifier
				+ " where itemId = ? and modifierId = ? and status = " 
				+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
				+ " and orderId in (select id from " 
				+ TableNames.Order 
				+ " where businessDate = ? and sessionStatus = ? and createTime > ?)";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(itemId), String.valueOf(modifierId), String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumModifierPrice", cursor.getString(0));
				result.put("sumModifierNum", String.valueOf(cursor.getInt(1)));
				result.put("modifierItemPrice", cursor.getString(2));
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
	
	public static Map<String, String> getOrderModifierByModifierId(int modifierId, long businessDate, SessionStatus sessionStatus) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select sum(modifierPrice), sum(modifierNum) from " + TableNames.OrderModifier
				+ " where modifierId = ? and orderId in (select id from " + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ?)";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] {String.valueOf(modifierId), String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.put("sumModifierPrice", cursor.getString(0));
				result.put("sumModifierNum", String.valueOf(cursor.getInt(1)));
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

	public static ArrayList<OrderModifier> getOrderModifiers(Order order,
			OrderDetail orderDetail) {
		ArrayList<OrderModifier> result = new ArrayList<OrderModifier>();
		String sql = "select * from "
		+ TableNames.OrderModifier
				+ " o,"
				+ TableNames.Modifier
				+ " m"
				+ " where o.orderId = ? and o.orderDetailId = ? and o.modifierId = m.id and m.type = 1 order by m.categoryId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db
					.rawQuery(
							sql,
							new String[] { order.getId() + "",
									orderDetail.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderModifier orderModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderModifier = new OrderModifier();
				orderModifier.setId(cursor.getInt(0));
				orderModifier.setOrderId(cursor.getInt(1));
				orderModifier.setOrderDetailId(cursor.getInt(2));
				orderModifier.setOrderOriginId(cursor.getInt(3));
				orderModifier.setUserId(cursor.getInt(4));
				orderModifier.setItemId(cursor.getInt(5));
				orderModifier.setModifierId(cursor.getInt(6));
				orderModifier.setModifierNum(cursor.getInt(7));
				orderModifier.setStatus(cursor.getInt(8));
				orderModifier.setModifierPrice(cursor.getString(9));
				orderModifier.setCreateTime(cursor.getLong(10));
				orderModifier.setUpdateTime(cursor.getLong(11));
				orderModifier.setPrinterId(cursor.getInt(12));
				orderModifier.setModifierItemPrice(cursor.getString(13));
				result.add(orderModifier);
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

	public static ArrayList<OrderModifier> getOrderModifiersByOrderDetailId(int orderDetailId) {
		ArrayList<OrderModifier> result = new ArrayList<OrderModifier>();
		String sql = "select * from " + TableNames.OrderModifier
				+ " where orderDetailId = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db
					.rawQuery(
							sql,
							new String[] {orderDetailId + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderModifier orderModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderModifier = new OrderModifier();
				orderModifier.setId(cursor.getInt(0));
				orderModifier.setOrderId(cursor.getInt(1));
				orderModifier.setOrderDetailId(cursor.getInt(2));
				orderModifier.setOrderOriginId(cursor.getInt(3));
				orderModifier.setUserId(cursor.getInt(4));
				orderModifier.setItemId(cursor.getInt(5));
				orderModifier.setModifierId(cursor.getInt(6));
				orderModifier.setModifierNum(cursor.getInt(7));
				orderModifier.setStatus(cursor.getInt(8));
				orderModifier.setModifierPrice(cursor.getString(9));
				orderModifier.setCreateTime(cursor.getLong(10));
				orderModifier.setUpdateTime(cursor.getLong(11));
				orderModifier.setPrinterId(cursor.getInt(12));
				orderModifier.setModifierItemPrice(cursor.getString(13));
				result.add(orderModifier);
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

	public static List<Integer> getOrderModifierIdsByOrderDetailId(int orderDetailId) {
		ArrayList<Integer> result = new ArrayList<>();
		String sql = "select modifierId from " + TableNames.OrderModifier
				+ " where orderDetailId = ? and status = " + ParamConst.ORDER_MODIFIER_STATUS_NORMAL;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db
					.rawQuery(
							sql,
							new String[] {orderDetailId + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderModifier orderModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result.add(cursor.getInt(0));
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

	public static ArrayList<OrderModifier> getOrderModifiers(OrderDetail orderDetail) {
		ArrayList<OrderModifier> result = new ArrayList<OrderModifier>();
		String sql = "select * from " + TableNames.OrderModifier
				+ " where orderDetailId = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db
					.rawQuery(
							sql,
							new String[] {orderDetail.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			OrderModifier orderModifier = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderModifier = new OrderModifier();
				orderModifier.setId(cursor.getInt(0));
				orderModifier.setOrderId(cursor.getInt(1));
				orderModifier.setOrderDetailId(cursor.getInt(2));
				orderModifier.setOrderOriginId(cursor.getInt(3));
				orderModifier.setUserId(cursor.getInt(4));
				orderModifier.setItemId(cursor.getInt(5));
				orderModifier.setModifierId(cursor.getInt(6));
				orderModifier.setModifierNum(cursor.getInt(7));
				orderModifier.setStatus(cursor.getInt(8));
				orderModifier.setModifierPrice(cursor.getString(9));
				orderModifier.setCreateTime(cursor.getLong(10));
				orderModifier.setUpdateTime(cursor.getLong(11));
				orderModifier.setPrinterId(cursor.getInt(12));
				orderModifier.setModifierItemPrice(cursor.getString(13));
				result.add(orderModifier);
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
	
	
	/*
	 * Get void Modifiers before settlement 
	 */
	public static Map<Integer, Map<String, String>> getModifiersByBusinessDateAndOrderDetailType(int itemId,
			long businessDate, int orderDetailType) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		Cursor cursor = null;
		String sql = "SELECT  om.modifierId, SUM(om.modifierNum), SUM(om.modifierPrice)  FROM  " 
		+ TableNames.OrderModifier
		+" om, " 
		+ TableNames.OrderDetail 
		+ " od , " 
		+ TableNames.Order 
		+ " o WHERE om.orderDetailId = od.id AND od.itemId = ? AND om.status = "
		+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
		+ "  AND  o.id = od.orderId AND o.businessDate = ? AND od.orderDetailType = ? group by om.modifierId ";
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(itemId), String.valueOf(businessDate), String.valueOf(orderDetailType)});
//			cursor = db.query(TableNames.OrderModifier,
//					new String[] { "modifierId, sum(itemNum), sum(realPrice)" },
//					"status = " + ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
//							+ "and orderDetailId in (select id from " + TableNames.OrderDetail + " where orderDetailType = " + ParamConst.ORDERDETAIL_TYPE_VOID
//							+ " and orderId in (select id from "
//							+ TableNames.Order + " where businessDate = ? and orderStatus ="
//							+ ParamConst.ORDER_STATUS_FINISHED + "))",
//					new String[] { String.valueOf(businessDate) }, "itemId",
//					"", "", "");
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
	
	
	/*
	 * Get void Modifiers before settlement 
	 */
	public static Map<Integer, Map<String, String>> getModifiersBySessionStatusAndOrderDetailType(int itemId,
			long businessDate, int orderDetailType, SessionStatus sessionStatus) {
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
		Cursor cursor = null;
		String sql = "SELECT  om.modifierId, SUM(om.modifierNum), SUM(om.modifierPrice)  FROM  " 
		+ TableNames.OrderModifier
		+" om, " 
		+ TableNames.OrderDetail 
		+ " od , " 
		+ TableNames.Order 
		+ " o WHERE om.orderDetailId = od.id AND od.itemId = ? AND om.status = "
		+ ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
		+ "  AND  o.id = od.orderId AND o.businessDate = ? and o.sessionStatus = ? and o.createTime > ? and o.orderStatus = "
		+ ParamConst.ORDER_STATUS_FINISHED 
		+ " AND od.orderDetailType = ? group by om.modifierId ";
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(itemId), String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()), String.valueOf(orderDetailType)});
//			cursor = db.query(TableNames.OrderModifier,
//					new String[] { "modifierId, sum(itemNum), sum(realPrice)" },
//					"status = " + ParamConst.ORDER_MODIFIER_STATUS_NORMAL 
//							+ "and orderDetailId in (select id from " + TableNames.OrderDetail + " where orderDetailType = " + ParamConst.ORDERDETAIL_TYPE_VOID
//							+ " and orderId in (select id from "
//							+ TableNames.Order + " where businessDate = ? and orderStatus ="
//							+ ParamConst.ORDER_STATUS_FINISHED + "))",
//					new String[] { String.valueOf(businessDate) }, "itemId",
//					"", "", "");
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
	
	public static void updateOrderModifierNum(OrderDetail orderDetail, int num){
		String sql = "update " + TableNames.OrderModifier + " set modifierNum = ? where orderDetailId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { num, orderDetail.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteOrderModifier(OrderModifier orderModifier) {
		String sql = "delete from " + TableNames.OrderModifier
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderModifier.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteOrderModifierByOrder(Order order) {
		String sql = "delete from " + TableNames.OrderModifier
				+ " where orderId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { order.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteOrderModifierByOrderOutsideOrderDetail(Order order) {
		String sql = "delete from " + TableNames.OrderModifier
				+ " where orderId = ? "
				+ "and orderDetailId not in "
				+ "(select id from "
				+ TableNames.OrderDetail
				+ " where orderId = ? )";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { order.getId(), order.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteOrderModifierByOrderDetail(OrderDetail orderDetail) {
		String sql = "delete from " + TableNames.OrderModifier
				+ " where orderDetailId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderDetail.getId().intValue() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteAllOrderModifier() {
		String sql = "delete from " + TableNames.OrderModifier;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
