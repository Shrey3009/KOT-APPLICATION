package com.alfredbase.store.sql.cpsql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class CPRoundAmountSQL {

	public static void update(RoundAmount roundAmount) {
		if (roundAmount == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.CPRoundAmount
					+ "(id, orderId, billNo, roundBeforePrice, roundAlfterPrice, roundBalancePrice,restId,revenueId,tableId,businessDate ,createTime,updateTime, orderSplitId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { roundAmount.getId(),
							roundAmount.getOrderId(), roundAmount.getBillNo(),
							roundAmount.getRoundBeforePrice(),
							roundAmount.getRoundAlfterPrice(),
							roundAmount.getRoundBalancePrice(),
							roundAmount.getRestId(),
							roundAmount.getRevenueId(),
							roundAmount.getTableId(),
							roundAmount.getBusinessDate(),
							roundAmount.getCreateTime(),
							roundAmount.getUpdateTime(),
							roundAmount.getOrderSplitId()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void update(SQLiteDatabase db, RoundAmount roundAmount) {
		if (roundAmount == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.CPRoundAmount
					+ "(id, orderId, billNo, roundBeforePrice, roundAlfterPrice, roundBalancePrice,restId,revenueId,tableId,businessDate ,createTime,updateTime, orderSplitId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			db.execSQL(
					sql,
					new Object[] { roundAmount.getId(),
							roundAmount.getOrderId(), roundAmount.getBillNo(),
							roundAmount.getRoundBeforePrice(),
							roundAmount.getRoundAlfterPrice(),
							roundAmount.getRoundBalancePrice(),
							roundAmount.getRestId(),
							roundAmount.getRevenueId(),
							roundAmount.getTableId(),
							roundAmount.getBusinessDate(),
							roundAmount.getCreateTime(),
							roundAmount.getUpdateTime(),
							roundAmount.getOrderSplitId()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addRoundAmountList(List<RoundAmount> roundAmounts) {
		if (roundAmounts == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.CPRoundAmount
					+ "(id, orderId, billNo, roundBeforePrice, roundAlfterPrice, roundBalancePrice,restId,"
					+ "revenueId,tableId,businessDate ,createTime,updateTime, orderSplitId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (RoundAmount roundAmount : roundAmounts) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							roundAmount.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							roundAmount.getOrderId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							roundAmount.getBillNo());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							roundAmount.getRoundBeforePrice());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							roundAmount.getRoundAlfterPrice());
					SQLiteStatementHelper.bindDouble(sqLiteStatement, 6,
							roundAmount.getRoundBalancePrice());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
							roundAmount.getRestId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
							roundAmount.getRevenueId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
							roundAmount.getTableId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
							roundAmount.getBusinessDate());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
							roundAmount.getCreateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							roundAmount.getUpdateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
							roundAmount.getOrderSplitId());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<RoundAmount> getAllRoundAmount() {
		ArrayList<RoundAmount> result = new ArrayList<RoundAmount>();
		String sql = "select * from " + TableNames.CPRoundAmount;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			RoundAmount roundAmount = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				roundAmount = new RoundAmount();
				roundAmount.setId(cursor.getInt(0));
				roundAmount.setOrderId(cursor.getInt(1));
				roundAmount.setBillNo(cursor.getInt(2));
				roundAmount.setRoundBeforePrice(cursor.getString(3));
				roundAmount.setRoundAlfterPrice(cursor.getString(4));
				roundAmount.setRoundBalancePrice(cursor.getDouble(5));
				roundAmount.setRestId(cursor.getInt(6));
				roundAmount.setRevenueId(cursor.getInt(7));
				roundAmount.setTableId(cursor.getInt(8));
				roundAmount.setBusinessDate(cursor.getLong(9));
				roundAmount.setCreateTime(cursor.getLong(10));
				roundAmount.setUpdateTime(cursor.getLong(11));
				roundAmount.setOrderSplitId(cursor.getInt(12));
				result.add(roundAmount);
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

	public static RoundAmount getRoundAmount(Order order) {
		RoundAmount roundAmount = null;
		String sql = "select * from " + TableNames.CPRoundAmount
				+ " where orderId = ? and orderSplitId = 0";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { order.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return roundAmount;
			}
			if (cursor.moveToFirst()) {
				roundAmount = new RoundAmount();
				roundAmount.setId(cursor.getInt(0));
				roundAmount.setOrderId(cursor.getInt(1));
				roundAmount.setBillNo(cursor.getInt(2));
				roundAmount.setRoundBeforePrice(cursor.getString(3));
				roundAmount.setRoundAlfterPrice(cursor.getString(4));
				roundAmount.setRoundBalancePrice(cursor.getDouble(5));
				roundAmount.setRestId(cursor.getInt(6));
				roundAmount.setRevenueId(cursor.getInt(7));
				roundAmount.setTableId(cursor.getInt(8));
				roundAmount.setBusinessDate(cursor.getLong(9));
				roundAmount.setCreateTime(cursor.getLong(10));
				roundAmount.setUpdateTime(cursor.getLong(11));
				roundAmount.setOrderSplitId(cursor.getInt(12));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return roundAmount;
	}
	public static List<RoundAmount> getRoundAmountForSync(Order order) {
		List<RoundAmount> result = new ArrayList<>();
		String sql = "select * from " + TableNames.CPRoundAmount
				+ " where orderId = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { order.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			RoundAmount roundAmount = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				roundAmount = new RoundAmount();
				roundAmount.setId(cursor.getInt(0));
				roundAmount.setOrderId(cursor.getInt(1));
				roundAmount.setBillNo(cursor.getInt(2));
				roundAmount.setRoundBeforePrice(cursor.getString(3));
				roundAmount.setRoundAlfterPrice(cursor.getString(4));
				roundAmount.setRoundBalancePrice(cursor.getDouble(5));
				roundAmount.setRestId(cursor.getInt(6));
				roundAmount.setRevenueId(cursor.getInt(7));
				roundAmount.setTableId(cursor.getInt(8));
				roundAmount.setBusinessDate(cursor.getLong(9));
				roundAmount.setCreateTime(cursor.getLong(10));
				roundAmount.setUpdateTime(cursor.getLong(11));
				roundAmount.setOrderSplitId(cursor.getInt(12));
				result.add(roundAmount);
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

	public static RoundAmount getRoundAmount(OrderSplit orderSplit) {
		RoundAmount roundAmount = null;
		String sql = "select * from " + TableNames.CPRoundAmount
				+ " where orderSplitId = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { orderSplit.getId() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return roundAmount;
			}
			if (cursor.moveToFirst()) {
				roundAmount = new RoundAmount();
				roundAmount.setId(cursor.getInt(0));
				roundAmount.setOrderId(cursor.getInt(1));
				roundAmount.setBillNo(cursor.getInt(2));
				roundAmount.setRoundBeforePrice(cursor.getString(3));
				roundAmount.setRoundAlfterPrice(cursor.getString(4));
				roundAmount.setRoundBalancePrice(cursor.getDouble(5));
				roundAmount.setRestId(cursor.getInt(6));
				roundAmount.setRevenueId(cursor.getInt(7));
				roundAmount.setTableId(cursor.getInt(8));
				roundAmount.setBusinessDate(cursor.getLong(9));
				roundAmount.setCreateTime(cursor.getLong(10));
				roundAmount.setUpdateTime(cursor.getLong(11));
				roundAmount.setOrderSplitId(cursor.getInt(12));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return roundAmount;
	}
	
	public static RoundAmount getRoundAmountByOrderAndBill(Order order, OrderBill bill) {
		RoundAmount roundAmount = null;
		String sql = "select * from " + TableNames.CPRoundAmount
				+ " where orderId = ? AND billNo = ? and orderSplitId = 0";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { order.getId() + "", bill.getBillNo() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return roundAmount;
			}
			if (cursor.moveToFirst()) {
				roundAmount = new RoundAmount();
				roundAmount.setId(cursor.getInt(0));
				roundAmount.setOrderId(cursor.getInt(1));
				roundAmount.setBillNo(cursor.getInt(2));
				roundAmount.setRoundBeforePrice(cursor.getString(3));
				roundAmount.setRoundAlfterPrice(cursor.getString(4));
				roundAmount.setRoundBalancePrice(cursor.getDouble(5));
				roundAmount.setRestId(cursor.getInt(6));
				roundAmount.setRevenueId(cursor.getInt(7));
				roundAmount.setTableId(cursor.getInt(8));
				roundAmount.setBusinessDate(cursor.getLong(9));
				roundAmount.setCreateTime(cursor.getLong(10));
				roundAmount.setUpdateTime(cursor.getLong(11));
				roundAmount.setOrderSplitId(cursor.getInt(12));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return roundAmount;
	}
	
	public static RoundAmount getRoundAmountByOrderSplitAndBill(OrderSplit orderSplit, OrderBill bill) {
		RoundAmount roundAmount = null;
		String sql = "select * from " + TableNames.CPRoundAmount
				+ " where orderSplitId = ? AND billNo = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { orderSplit.getId() + "", bill.getBillNo() + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return roundAmount;
			}
			if (cursor.moveToFirst()) {
				roundAmount = new RoundAmount();
				roundAmount.setId(cursor.getInt(0));
				roundAmount.setOrderId(cursor.getInt(1));
				roundAmount.setBillNo(cursor.getInt(2));
				roundAmount.setRoundBeforePrice(cursor.getString(3));
				roundAmount.setRoundAlfterPrice(cursor.getString(4));
				roundAmount.setRoundBalancePrice(cursor.getDouble(5));
				roundAmount.setRestId(cursor.getInt(6));
				roundAmount.setRevenueId(cursor.getInt(7));
				roundAmount.setTableId(cursor.getInt(8));
				roundAmount.setBusinessDate(cursor.getLong(9));
				roundAmount.setCreateTime(cursor.getLong(10));
				roundAmount.setUpdateTime(cursor.getLong(11));
				roundAmount.setOrderSplitId(cursor.getInt(12));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return roundAmount;
	}
	
	public static double getSumRoundWhenSplitByOrder(Order order){
		double totalBalancePrice = 0.00;
		String sql = "select sum(roundBalancePrice) from " + TableNames.CPRoundAmount +" where orderId = ? and orderSplitId <> 0";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] {String.valueOf(order.getId().intValue())});
			int count = cursor.getCount();
			if (count < 1) {
				return 0.00;
			}
			if (cursor.moveToFirst()) {
				totalBalancePrice = cursor.getDouble(0);
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
				}
			}
		return totalBalancePrice;
	}
	
	public static double getAllRoundBalancePriceByTime(long businessDate){
		double totalBalancePrice = 0.00;
		String sql = "select sum(roundBalancePrice) from " + TableNames.CPRoundAmount +" where businessDate = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] {String.valueOf(businessDate)});
			int count = cursor.getCount();
			if (count < 1) {
				return 0.00;
			}
			if (cursor.moveToFirst()) {
				totalBalancePrice = cursor.getDouble(0);
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
				}
			}
		return totalBalancePrice;
	}
	// TODO
	public static double getAllRoundBalancePriceByTime(long businessDate, SessionStatus sessionStatus){
		double totalBalancePrice = 0.00;
		String sql = "select sum(roundBalancePrice) from " + TableNames.CPRoundAmount +" where orderId in ( select id from " + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ? )";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] {String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())});
			int count = cursor.getCount();
			if (count < 1) {
				return 0.00;
			}
			if (cursor.moveToFirst()) {
				totalBalancePrice = cursor.getDouble(0);
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
				}
			}
		return totalBalancePrice;
	}
	
	public static void deleteRoundAmount(RoundAmount roundAmount) {
		String sql = "delete from " + TableNames.CPRoundAmount + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { roundAmount.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteRoundAmount(OrderSplit orderSplit) {
		String sql = "delete from " + TableNames.CPRoundAmount + " where orderSplitId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { orderSplit.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteRoundAmount(Order order) {
		String sql = "delete from " + TableNames.CPRoundAmount + " where orderId = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { order.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteRoundAmount(SQLiteDatabase db, Order order) {
		String sql = "delete from " + TableNames.CPRoundAmount + " where orderId = ?";
		try {
			db.execSQL(sql, new Object[] { order.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
