package com.alfredbase.store.sql.cpsql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;

public class CPOrderSQL {

	public static void update(SQLiteDatabase db, Order order) throws Exception{
		if (order == null) {
			return;
		}
			String sql = "replace into "
					+ TableNames.CPOrder
					+ "(id,orderOriginId, userId, persons, orderStatus, subTotal, taxAmount, discountAmount,"
					+ " total, sessionStatus, restId, revenueId, placeId, tableId, createTime, updateTime,"
					+ "orderNo,businessDate,discount_rate,discount_type, discountPrice, inclusiveTaxName, inclusiveTaxPrice,"
					+ "inclusiveTaxPercentage, appOrderId,isTakeAway, tableName, orderRemark, discountCategoryId, numTag, subPosBeanId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(
					sql,
					new Object[] { order.getId(), order.getOrderOriginId(),
							order.getUserId(), order.getPersons(),
							order.getOrderStatus(), order.getSubTotal(),
							order.getTaxAmount(), order.getDiscountAmount(),
							order.getTotal(), order.getSessionStatus(),
							order.getRestId(), order.getRevenueId(), order.getPlaceId(),
							order.getTableId(), order.getCreateTime(),
							order.getUpdateTime(), order.getOrderNo(),
							order.getBusinessDate(), order.getDiscountRate(),
							order.getDiscountType(), order.getDiscountPrice(),
							order.getInclusiveTaxName(), order.getInclusiveTaxPrice(),
							order.getInclusiveTaxPercentage(), order.getAppOrderId(),
							order.getIsTakeAway(), order.getTableName(),
							order.getOrderRemark(), order.getDiscountCategoryId(),
							order.getNumTag(), order.getSubPosBeanId()});
	}




//	public static ArrayList<Order> getAllOrderByTime(long businessDate,SessionStatus sessionStatus) {
//		ArrayList<Order> result = new ArrayList<Order>();
//		Cursor cursor = null;
//		SQLiteDatabase db = SQLExe.getDB();
//		try {
//			cursor = db.query(TableNames.CPOrder,
//					new String[] { " * " }, "businessDate = ? and sessionStatus = ? and createTime > ? and orderStatus = " + ParamConst.ORDER_STATUS_FINISHED,
//					new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime() )}, "", "", "",
//					"");
//			int count = cursor.getCount();
//			if (count < 1) {
//				return result;
//			}
//			Order order = null;
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				order = new Order();
//				order.setId(cursor.getInt(0));
//				order.setOrderOriginId(cursor.getInt(1));
//				order.setUserId(cursor.getInt(2));
//				order.setPersons(cursor.getInt(3));
//				order.setOrderStatus(cursor.getInt(4));
//				order.setSubTotal(cursor.getString(5));
//				order.setTaxAmount(cursor.getString(6));
//				order.setDiscountAmount(cursor.getString(7));
//				order.setTotal(cursor.getString(8));
//				order.setSessionStatus(cursor.getInt(9));
//				order.setRestId(cursor.getInt(10));
//				order.setRevenueId(cursor.getInt(11));
//				order.setPlaceId(cursor.getInt(12));
//				order.setTableId(cursor.getInt(13));
//				order.setCreateTime(cursor.getLong(14));
//				order.setUpdateTime(cursor.getLong(15));
//				order.setOrderNo(cursor.getInt(16));
//				order.setBusinessDate(cursor.getLong(17));
//				order.setDiscountRate(cursor.getString(18));
//				order.setDiscountType(cursor.getInt(19));
//				order.setDiscountPrice(cursor.getString(20));
//				order.setInclusiveTaxName(cursor.getString(21));
//				order.setInclusiveTaxPrice(cursor.getString(22));
//				order.setInclusiveTaxPercentage(cursor.getString(23));
//				order.setAppOrderId(cursor.getInt(24));
//				order.setIsTakeAway(cursor.getInt(25));
//				order.setTableName(cursor.getString(26));
//				order.setOrderRemark(cursor.getString(27));
//				order.setDiscountCategoryId(cursor.getString(28));
//				order.setNumTag(cursor.getString(29));
//				order.setSubPosBeanId(cursor.getInt(30));
//				result.add(order);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return result;
//	}

	public static Order getOrder(Integer orderID) {
		Order order = null;
		String sql = "select * from " + TableNames.CPOrder + " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB()
					.rawQuery(sql, new String[] { orderID + "" });
			if (cursor.moveToFirst()) {
				order = new Order();
				order.setId(cursor.getInt(0));
				order.setOrderOriginId(cursor.getInt(1));
				order.setUserId(cursor.getInt(2));
				order.setPersons(cursor.getInt(3));
				order.setOrderStatus(cursor.getInt(4));
				order.setSubTotal(cursor.getString(5));
				order.setTaxAmount(cursor.getString(6));
				order.setDiscountAmount(cursor.getString(7));
				order.setTotal(cursor.getString(8));
				order.setSessionStatus(cursor.getInt(9));
				order.setRestId(cursor.getInt(10));
				order.setRevenueId(cursor.getInt(11));
				order.setPlaceId(cursor.getInt(12));
				order.setTableId(cursor.getInt(13));
				order.setCreateTime(cursor.getLong(14));
				order.setUpdateTime(cursor.getLong(15));
				order.setOrderNo(cursor.getInt(16));
				order.setBusinessDate(cursor.getLong(17));
				order.setDiscountRate(cursor.getString(18));
				order.setDiscountType(cursor.getInt(19));
				order.setDiscountPrice(cursor.getString(20));
				order.setInclusiveTaxName(cursor.getString(21));
				order.setInclusiveTaxPrice(cursor.getString(22));
				order.setInclusiveTaxPercentage(cursor.getString(23));
				order.setAppOrderId(cursor.getInt(24));
				order.setIsTakeAway(cursor.getInt(25));
				order.setTableName(cursor.getString(26));
				order.setOrderRemark(cursor.getString(27));
				order.setDiscountCategoryId(cursor.getString(28));
				order.setNumTag(cursor.getString(29));
				order.setSubPosBeanId(cursor.getInt(30));
				return order;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return order;
	}


	public static ArrayList<Order> getFinishedOrdersBySession(SessionStatus sessionStatus, long businessDate, long closeTime) {
		ArrayList<Order> result = new ArrayList<Order>();
		String sql = "select * from "
				+ TableNames.CPOrder
				+ " where sessionStatus = ? and businessDate = ? and orderStatus = "
				+ParamConst.ORDER_STATUS_FINISHED
				+ " and updateTime < ?  and createTime > ?";
		SQLiteDatabase db = SQLExe.getDB();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(sessionStatus.getSession_status()), String.valueOf(businessDate),
													String.valueOf(closeTime), String.valueOf(sessionStatus.getTime())});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Order order = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				order = new Order();
				order.setId(cursor.getInt(0));
				order.setOrderOriginId(cursor.getInt(1));
				order.setUserId(cursor.getInt(2));
				order.setPersons(cursor.getInt(3));
				order.setOrderStatus(cursor.getInt(4));
				order.setSubTotal(cursor.getString(5));
				order.setTaxAmount(cursor.getString(6));
				order.setDiscountAmount(cursor.getString(7));
				order.setTotal(cursor.getString(8));
				order.setSessionStatus(cursor.getInt(9));
				order.setRestId(cursor.getInt(10));
				order.setRevenueId(cursor.getInt(11));
				order.setPlaceId(cursor.getInt(12));
				order.setTableId(cursor.getInt(13));
				order.setCreateTime(cursor.getLong(14));
				order.setUpdateTime(cursor.getLong(15));
				order.setOrderNo(cursor.getInt(16));
				order.setBusinessDate(cursor.getLong(17));
				order.setDiscountRate(cursor.getString(18));
				order.setDiscountType(cursor.getInt(19));
				order.setDiscountPrice(cursor.getString(20));
				order.setInclusiveTaxName(cursor.getString(21));
				order.setInclusiveTaxPrice(cursor.getString(22));
				order.setInclusiveTaxPercentage(cursor.getString(23));
				order.setAppOrderId(cursor.getInt(24));
				order.setIsTakeAway(cursor.getInt(25));
				order.setTableName(cursor.getString(26));
				order.setOrderRemark(cursor.getString(27));
				order.setDiscountCategoryId(cursor.getString(28));
				order.setNumTag(cursor.getString(29));
				order.setSubPosBeanId(cursor.getInt(30));
				result.add(order);
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


	public static void deleteOrder(Order order) {
		String sql = "delete from " + TableNames.CPOrder + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { order.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void updateOrderStatus( int orderStatus, int id){

		String sql = "update " + TableNames.CPOrder + " set orderStatus = ? where id = ?" ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {orderStatus, id});
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public static void deleteAllOrder(){

		String sql = "delete from " + TableNames.CPOrder ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { });
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
