package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.R;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderDetailTax;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailTaxSQL {

	public static void addOrderDetailTax(OrderDetailTax orderDetailTax) {
		if (orderDetailTax == null) {
			return;
		}
		try {
			String sql = "insert into "
					+ TableNames.OrderDetailTax
					+ "(orderId, orderDetailId, taxId, taxName, taxPercentage, taxPrice, taxType, createTime, updateTime, indexId, orderSplitId, isActive)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderDetailTax.getOrderId(),
							orderDetailTax.getOrderDetailId(),
							orderDetailTax.getTaxId(),
							orderDetailTax.getTaxName(),
							orderDetailTax.getTaxPercentage(),
							orderDetailTax.getTaxPrice(),
							orderDetailTax.getTaxType(),
							orderDetailTax.getCreateTime(),
							orderDetailTax.getUpdateTime(),
							orderDetailTax.getIndexId(),
							orderDetailTax.getOrderSplitId(),
							orderDetailTax.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateOrderDetailTax(OrderDetailTax orderDetailTax) {
		if (orderDetailTax == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.OrderDetailTax
					+ "(id, orderId, orderDetailId, taxId, taxName, taxPercentage, taxPrice, taxType, createTime, updateTime, indexId, orderSplitId, isActive)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { orderDetailTax.getId(),
							orderDetailTax.getOrderId(),
							orderDetailTax.getOrderDetailId(),
							orderDetailTax.getTaxId(),
							orderDetailTax.getTaxName(),
							orderDetailTax.getTaxPercentage(),
							orderDetailTax.getTaxPrice(),
							orderDetailTax.getTaxType(),
							orderDetailTax.getCreateTime(),
							orderDetailTax.getUpdateTime(),
							orderDetailTax.getIndexId(),
							orderDetailTax.getOrderSplitId(),
							orderDetailTax.getIsActive()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addOrderDetailTaxList(
			List<OrderDetailTax> orderDetailTaxList) {
		if (orderDetailTaxList == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.OrderDetailTax
					+ "(orderId, orderDetailId, taxId, taxName, taxPercentage, taxPrice, taxType, createTime, updateTime, indexId, orderSplitId, isActive)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (OrderDetailTax orderDetailTax : orderDetailTaxList) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							orderDetailTax.getOrderId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							orderDetailTax.getOrderDetailId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							orderDetailTax.getTaxId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							orderDetailTax.getTaxName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							orderDetailTax.getTaxPercentage());
					SQLiteStatementHelper.bindString(sqLiteStatement, 6,
							orderDetailTax.getTaxPrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
							orderDetailTax.getTaxType());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							orderDetailTax.getCreateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							orderDetailTax.getUpdateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
							orderDetailTax.getIndexId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 11, 
							orderDetailTax.getOrderSplitId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12, 
							orderDetailTax.getIsActive());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static List<Map<String, String>> getTaxPriceSUMForPrint(Tax tax, Order order) {
		ArrayList<String> taxPriceSum = new ArrayList<String>();
		ArrayList<String> taxNames = new ArrayList<String>();
		ArrayList<String> taxPercentages = new ArrayList<String>();
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
//		String sql ="select sum(ot.taxPrice), ot.taxName, ot.taxPercentage from " + TableNames.OrderDetailTax + " ot, " + TableNames.TaxCategory + " tc" + " where ot.orderId = ? and ot.taxId = tc.taxId and tc.status = 1 group by ot.taxId order by tc.indexId";
		String sql ="select sum(ot.taxPrice), ot.taxName, ot.taxPercentage from " + TableNames.OrderDetailTax + " ot where ot.orderId = ? and ot.isActive <> " + ParamConst.ACTIVE_DELETE + " and  ot.taxId in ( select taxId from " + TableNames.TaxCategory + " where status = 1) group by ot.taxId order by ot.indexId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { order.getId() + "" });
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("taxPriceSum", cursor.getString(0));
				map.put("taxName", cursor.getString(1));
				map.put("taxPercentage", cursor.getString(2));
				result.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		if(tax != null){
			Map<String, String> map = new HashMap<String, String>();
			map.put("taxPriceSum", BH.getBD(order.getInclusiveTaxPrice()).toString());
			map.put("taxName", BaseApplication.getTopActivity().getResources().getString(R.string.inclusive) + tax.getTaxName());
			map.put("taxPercentage", tax.getTaxPercentage());
//			taxNames.add(BaseApplication.getTopActivity().getResources().getString(R.string.inclusive) + tax.getTaxName());
//			taxPercentages.add(tax.getTaxPercentage());
//			taxPriceSum.add(BH.mul(BH.getBD(tax.getTaxPercentage()), BH.div(BH.sub(BH.getBD(order.getSubTotal()), BH.getBD(order.getDiscountAmount()), false), BH.add(BH.getBD(1), BH.getBD(tax.getTaxPercentage()), false), false), true).toString());
			result.add(map);
		}
//		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
//		map.put("taxPriceSum", taxPriceSum);
//		map.put("taxNames", taxNames);
//		map.put("taxPercentages", taxPercentages);
		return result;
	}
	
	
	public static List<Map<String, String>> getOrderSplitTaxPriceSUMForPrint(Tax tax, OrderSplit orderSplit) {
		ArrayList<String> taxPriceSum = new ArrayList<String>();
		ArrayList<String> taxNames = new ArrayList<String>();
		ArrayList<String> taxPercentages = new ArrayList<String>();
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		String sql = "select sum(t.taxPrice), t.taxName, t.taxPercentage from "
				+ TableNames.OrderDetailTax
				+ " t, "
				+ TableNames.OrderDetail
				+ " o where o.id = t.orderDetailId and o.orderSplitId = ? and t.isActive <> " + ParamConst.ACTIVE_DELETE + " and o.orderId = ? and  t.taxId in ( select taxId from "
				+ TableNames.TaxCategory 
				+ " where status = 1) group by t.taxId order by t.indexId";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {orderSplit.getId() + "", orderSplit.getOrderId() + ""});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("taxPriceSum", cursor.getString(0));
				map.put("taxName", cursor.getString(1));
				map.put("taxPercentage", cursor.getString(2));
				result.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		if(tax != null){
			Map<String, String> map = new HashMap<String, String>();
		//	map.put("taxPriceSum", BH.mul(BH.getBDNoFormat(tax.getTaxPercentage()), BH.sub(BH.getBD(orderSplit.getSubTotal()), BH.getBD(orderSplit.getDiscountAmount()), false), true).toString());
			map.put("taxPriceSum", orderSplit.getInclusiveTaxPrice());
			map.put("taxName", BaseApplication.instance.getTopActivity().getResources().getString(R.string.inclusive) + tax.getTaxName());
			map.put("taxPercentage", tax.getTaxPercentage());
//			taxNames.add(BaseApplication.instance.getTopActivity().getResources().getString(R.string.inclusive) + tax.getTaxName());
//			taxPercentages.add(tax.getTaxPercentage());
//			taxPriceSum.add(BH.mul(BH.getBD(tax.getTaxPercentage()), BH.div(BH.sub(BH.getBD(orderSplit.getSubTotal()), BH.getBD(orderSplit.getDiscountAmount()), false), BH.add(BH.getBD(1), BH.getBD(tax.getTaxPercentage()), false), false), true).toString());
			result.add(map);
		}
//		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
//		map.put("taxPriceSum", taxPriceSum);
//		map.put("taxNames", taxNames);
//		map.put("taxPercentages", taxPercentages);
		return result;
	}

	public static String getRefundTax(long businessDate){
		String result = "";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			String sql = "select sum(odt.taxPrice) from "
					+ TableNames.OrderDetailTax
					+ " odt,"
					+ TableNames.Order
					+ " o where odt.orderId = o.id and o.businessDate = ? and odt.isActive = "
					+ ParamConst.ACTIVE_REFUND;
			cursor = db.rawQuery(sql, new String[] { String.valueOf(businessDate) });
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return result;
	}

	public static Map<String, Object> getTaxDetail(long businessDate) {
		ArrayList<String> taxPriceSum = new ArrayList<String>();
		ArrayList<String> taxNames = new ArrayList<String>();
		ArrayList<String> taxPercentages = new ArrayList<String>();
		ArrayList<Integer> taxIds = new ArrayList<Integer>();
		ArrayList<Integer> taxCounts = new ArrayList<Integer>();
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			String sql = "select sum(taxPrice), taxName, taxPercentage, taxId, count(*) from "
					+ TableNames.Order
					+ " o,"
					+ TableNames.OrderDetail
					+ " od";
			cursor = db.query(TableNames.OrderDetailTax,
					new String[] { "sum(taxPrice), taxName, taxPercentage, taxId, count(*)" },
					" isActive = " + ParamConst.ACTIVE_NOMAL + " and orderId in ( select id from " + TableNames.Order + "  where businessDate = ?) ", new String[] { String.valueOf(businessDate) },
					"taxId", "", "", "");
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				taxPriceSum.add(cursor.getString(0));
				taxNames.add(cursor.getString(1));
				taxPercentages.add(cursor.getString(2));
				taxIds.add(cursor.getInt(3));
				taxCounts.add(cursor.getInt(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taxPriceSum", taxPriceSum);
		map.put("taxNames", taxNames);
		map.put("taxPercentages", taxPercentages);
		map.put("taxIds", taxIds);
		map.put("taxCounts", taxCounts);
		return map;
	}

	public static String getRefundTax(long businessDate, SessionStatus sessionStatus){
		String result = "";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			String sql = "select sum(odt.taxPrice) from "
					+ TableNames.OrderDetailTax
					+ " odt, "
					+ TableNames.Order
					+ " o where odt.orderId = o.id and o.businessDate = ? and o.sessionStatus = ? and o.createTime > ? and odt.isActive = "
					+ ParamConst.ACTIVE_REFUND;
			cursor = db.rawQuery(sql, new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) });
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return result;
	}

	public static Map<String, Object> getTaxDetail(long businessDate, SessionStatus sessionStatus) {
		ArrayList<String> taxPriceSum = new ArrayList<String>();
		ArrayList<String> taxNames = new ArrayList<String>();
		ArrayList<String> taxPercentages = new ArrayList<String>();
		ArrayList<Integer> taxIds = new ArrayList<Integer>();
		ArrayList<Integer> taxCounts = new ArrayList<Integer>();
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		String  sql = "select sum(taxPrice), taxName, taxPercentage, taxId, count(*) from "
				+ TableNames.OrderDetailTax
				+ " where isActive = "
				+ ParamConst.ACTIVE_NOMAL
				+ " and orderId in ( select id from "
				+ TableNames.Order
				+ "  where businessDate = ? and sessionStatus = ? and createTime > ?) group by taxId";
		try {
			cursor = db.query(TableNames.OrderDetailTax,
					new String[] { "sum(taxPrice), taxName, taxPercentage, taxId, count(*)" },
					" isActive = " + ParamConst.ACTIVE_NOMAL + " and orderId in ( select id from " + TableNames.Order + "  where businessDate = ? and sessionStatus = ? and createTime > ?) ", new String[] { String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime()) },
					"taxId", "", "", "");
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				taxPriceSum.add(cursor.getString(0));
				taxNames.add(cursor.getString(1));
				taxPercentages.add(cursor.getString(2));
				taxIds.add(cursor.getInt(3));
				taxCounts.add(cursor.getInt(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taxPriceSum", taxPriceSum);
		map.put("taxNames", taxNames);
		map.put("taxPercentages", taxPercentages);
		map.put("taxIds", taxIds);
		map.put("taxCounts", taxCounts);
		return map;
	}


	public static ArrayList<OrderDetailTax> getAllOrderDetailTax(Order order) {
		ArrayList<OrderDetailTax> orderDetailTaxs = new ArrayList<OrderDetailTax>();
		String sql = "select * from " + TableNames.OrderDetailTax
				+ " where orderId = ? and isActive = " + ParamConst.ACTIVE_NOMAL;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { order.getId() + "" });
			OrderDetailTax orderDetailTax = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderDetailTax = new OrderDetailTax();
				orderDetailTax.setId(cursor.getInt(0));
				orderDetailTax.setOrderId(cursor.getInt(1));
				orderDetailTax.setOrderDetailId(cursor.getInt(2));
				orderDetailTax.setTaxId(cursor.getInt(3));
				orderDetailTax.setTaxName(cursor.getString(4));
				orderDetailTax.setTaxPercentage(cursor.getString(5));
				orderDetailTax.setTaxPrice(cursor.getString(6));
				orderDetailTax.setTaxType(cursor.getInt(7));
				orderDetailTax.setCreateTime(cursor.getLong(8));
				orderDetailTax.setUpdateTime(cursor.getLong(9));
				orderDetailTax.setIndexId(cursor.getInt(10));
				orderDetailTax.setOrderSplitId(cursor.getInt(11));
				orderDetailTax.setIsActive(cursor.getInt(12));
				orderDetailTaxs.add(orderDetailTax);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return orderDetailTaxs;
	}

	public static OrderDetailTax getOrderDetailTax(Order order,
			OrderDetail orderDetail, Tax tax) {
		String sql = "select * from " + TableNames.OrderDetailTax
				+ " where orderId = ? and orderDetailId = ? and taxId = ? and isActive = " + ParamConst.ACTIVE_NOMAL;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(
					sql,
					new String[] { order.getId() + "",
							orderDetail.getId() + "", tax.getId() + "" });

			if (cursor.getCount() <= 0) {
				return null;
			}
			OrderDetailTax orderDetailTax = null;
			if (cursor.moveToFirst()) {
				orderDetailTax = new OrderDetailTax();
				orderDetailTax.setId(cursor.getInt(0));
				orderDetailTax.setOrderId(cursor.getInt(1));
				orderDetailTax.setOrderDetailId(cursor.getInt(2));
				orderDetailTax.setTaxId(cursor.getInt(3));
				orderDetailTax.setTaxName(cursor.getString(4));
				orderDetailTax.setTaxPercentage(cursor.getString(5));
				orderDetailTax.setTaxPrice(cursor.getString(6));
				orderDetailTax.setTaxType(cursor.getInt(7));
				orderDetailTax.setCreateTime(cursor.getLong(8));
				orderDetailTax.setUpdateTime(cursor.getLong(9));
				orderDetailTax.setIndexId(cursor.getInt(10));
				orderDetailTax.setOrderSplitId(cursor.getInt(11));
				orderDetailTax.setIsActive(cursor.getInt(12));
				return orderDetailTax;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return null;
	}

	public static List<OrderDetailTax> getAllOrderDetail() {
		String sql = "select * from " + TableNames.OrderDetailTax
				+ " where isActive = " + ParamConst.ACTIVE_NOMAL;
		List<OrderDetailTax> orderDetailTaxes = new ArrayList<OrderDetailTax>();
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(
					sql,
					new String[] { });

			if (cursor.getCount() <= 0) {
				return null;
			}
			OrderDetailTax orderDetailTax = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				orderDetailTax = new OrderDetailTax();
				orderDetailTax.setId(cursor.getInt(0));
				orderDetailTax.setOrderId(cursor.getInt(1));
				orderDetailTax.setOrderDetailId(cursor.getInt(2));
				orderDetailTax.setTaxId(cursor.getInt(3));
				orderDetailTax.setTaxName(cursor.getString(4));
				orderDetailTax.setTaxPercentage(cursor.getString(5));
				orderDetailTax.setTaxPrice(cursor.getString(6));
				orderDetailTax.setTaxType(cursor.getInt(7));
				orderDetailTax.setCreateTime(cursor.getLong(8));
				orderDetailTax.setUpdateTime(cursor.getLong(9));
				orderDetailTax.setIndexId(cursor.getInt(10));
				orderDetailTax.setOrderSplitId(cursor.getInt(11));
				orderDetailTax.setIsActive(cursor.getInt(12));
				orderDetailTaxes.add(orderDetailTax);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return orderDetailTaxes;
	}

	public static OrderDetailTax getOrderDetailTaxId(int orderId,
												   int orderDetailId, int taxId) {
		String sql = "select * from " + TableNames.OrderDetailTax
				+ " where orderId = ? and orderDetailId = ? and taxId = ? and isActive = " + ParamConst.ACTIVE_NOMAL;
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(
					sql,
					new String[] { orderId + "",
							orderDetailId + "", taxId + "" });

			if (cursor.getCount() <= 0) {
				return null;
			}
			OrderDetailTax orderDetailTax = null;
			if (cursor.moveToFirst()) {
				orderDetailTax = new OrderDetailTax();
				orderDetailTax.setId(cursor.getInt(0));
				orderDetailTax.setOrderId(cursor.getInt(1));
				orderDetailTax.setOrderDetailId(cursor.getInt(2));
				orderDetailTax.setTaxId(cursor.getInt(3));
				orderDetailTax.setTaxName(cursor.getString(4));
				orderDetailTax.setTaxPercentage(cursor.getString(5));
				orderDetailTax.setTaxPrice(cursor.getString(6));
				orderDetailTax.setTaxType(cursor.getInt(7));
				orderDetailTax.setCreateTime(cursor.getLong(8));
				orderDetailTax.setUpdateTime(cursor.getLong(9));
				orderDetailTax.setIndexId(cursor.getInt(10));
				orderDetailTax.setOrderSplitId(cursor.getInt(11));
				orderDetailTax.setIsActive(cursor.getInt(12));
				return orderDetailTax;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return null;
	}
	
	public static void updateOrderDetailTaxActiveByOrder(int isActive, int orderId){
		String sql = "update " + TableNames.OrderDetailTax + " set isActive = ? where orderId = ?" ;
		if(isActive == ParamConst.ACTIVE_REFUND){
			sql = "update " + TableNames.OrderDetailTax + " set isActive = ? where orderId = ? and isActive <> " + ParamConst.ACTIVE_DELETE;
		}
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {isActive, orderId});
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public static void updateOrderDetailTaxActiveByOrderSplit(int isActive, int orderSplitId){
		String sql = "update " + TableNames.OrderDetailTax + " set isActive = ? where orderSplitId = ?" ;
		if(isActive == ParamConst.ACTIVE_REFUND){
			sql = "update " + TableNames.OrderDetailTax + " set isActive = ? where orderSplitId = ? and isActive <> " + ParamConst.ACTIVE_DELETE;
		}
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {isActive, orderSplitId});
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public static void updateOrderSplitIdbyOrderDetail(OrderDetail orderDetail){

		String sql = "update " + TableNames.OrderDetailTax + " set orderSplitId = ? where orderDetailId = ?" ;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {orderDetail.getOrderSplitId(), orderDetail.getId()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public static void updateOrderDetailTaxForTransation(OrderDetail newOrderDetail, OrderDetail oldOrderDetail){
		String sql = "update " + TableNames.OrderDetailTax + " set orderId = ? , orderDetailId = ? ,orderSplitId = ? where orderDetailId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {newOrderDetail.getOrderId().intValue(), newOrderDetail.getId().intValue(), newOrderDetail.getOrderSplitId().intValue(), oldOrderDetail.getId().intValue()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void deleteOrderDetailTax(OrderDetailTax orderDetailTax) {
		String sql = "delete from " + TableNames.OrderDetailTax
				+ " where id = ?";
		try {
			SQLExe.getDB()
					.execSQL(sql, new Object[] { orderDetailTax.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteOrderDetailTax(OrderDetail orderDetail) {
		String sql = "delete from " + TableNames.OrderDetailTax
				+ " where orderDetailId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderDetail.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteOrderDetailTax(Order order) {
		String sql = "delete from " + TableNames.OrderDetailTax
				+ " where orderId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { order.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteAllOrderDetailTax() {
		String sql = "delete from " + TableNames.OrderDetailTax;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
