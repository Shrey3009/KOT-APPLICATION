package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;


public class SyncMsgSQL {

	public static void add(SyncMsg syncMsg) {
		if (syncMsg == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.SyncMsg
					+ "(id, orderId, msg_type, data, status, createTime, revenueId, businessDate, currCount, appOrderId, orderStatus,"
					+ "orderNum, billNo, reportNo)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { syncMsg.getId(), syncMsg.getOrderId(),
							syncMsg.getMsgType(), syncMsg.getData(),
							syncMsg.getStatus(), syncMsg.getCreateTime(),
							syncMsg.getRevenueId(), syncMsg.getBusinessDate(),
							syncMsg.getCurrCount(), syncMsg.getAppOrderId(),
							syncMsg.getOrderStatus(), syncMsg.getOrderNum(),
							syncMsg.getBillNo(), syncMsg.getReportNo()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<SyncMsg> getAllSyncMsg() {
		ArrayList<SyncMsg> result = new ArrayList<SyncMsg>();
		String sql = "select * from " + TableNames.SyncMsg;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			SyncMsg syncMsg = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
				result.add(syncMsg);
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
    
	/*for migration pupose. Be careful to use this function*/
	public static ArrayList<SyncMsg> getUnsentSyncMsg(int revenueId) {
		ArrayList<SyncMsg> result = new ArrayList<SyncMsg>();
		String sql = "select * from " + TableNames.SyncMsg
				+ " where status =  ? and revenueId=? ";
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { ParamConst.SYNC_MSG_UN_SEND + "", revenueId+"" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			SyncMsg syncMsg = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
				result.add(syncMsg);
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
	
	/*Sync 10 msg in cloud scheduler 
	 * This only used in SYNC Scheduler
	 * */
	public static ArrayList<SyncMsg> getTenUnsentSyncMsg(int revenueId, int msgType) {
		ArrayList<SyncMsg> result = new ArrayList<SyncMsg>();
		String sql = "select * from " + TableNames.SyncMsg
				+ " where status =  ?  and revenueId=? and msg_type = ? LIMIT 10";
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { ParamConst.SYNC_MSG_UN_SEND + "", 
									revenueId+"", msgType+"" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			SyncMsg syncMsg = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
				result.add(syncMsg);
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
	
	public static SyncMsg getSyncMsgById(String id, int revenueId, long created) {
		SyncMsg syncMsg = null;
		String sql = "select * from " + TableNames.SyncMsg
				+ " where id = ? and revenueId = ? and createTime = ?";
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {id, revenueId+"", created+""});
			int count = cursor.getCount();
			if (count < 1) {
				return syncMsg;
			}
			if (cursor.moveToFirst()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return syncMsg;
	}
		
	public static SyncMsg getSyncMsgByOrderIdBizDate(int orderId, long businessDate) {
		SyncMsg syncMsg = null;
		// 这边的11指的是同步到后台的log数据的类型  同pos里面 HttpApi的常量 LOG_DATA
		String sql = "select * from " + TableNames.SyncMsg
				+ " where orderId = ? and businessDate = ? and msg_type = 10";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { orderId + "", businessDate + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return syncMsg;
			}
			if (cursor.moveToFirst()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return syncMsg;
	}
	public static SyncMsg getSubPosSyncMsgByOrderIdBizDate(int orderId, long businessDate) {
		SyncMsg syncMsg = null;
		// 这边的11指的是同步到后台的log数据的类型  同pos里面 HttpApi的常量 LOG_DATA
		String sql = "select * from " + TableNames.SyncMsg
				+ " where orderId = ? and businessDate = ? and msg_type = 12";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { orderId + "", businessDate + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return syncMsg;
			}
			if (cursor.moveToFirst()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return syncMsg;
	}


	public static SyncMsg getSyncMsgByAppOrderId(int appOrderId, int orderStatus) {
		SyncMsg syncMsg = null;
		// 这边的11指的是同步到后台的log数据的类型  同pos里面 HttpApi的常量 LOG_DATA
		String sql = "select * from " + TableNames.SyncMsg
				+ " where appOrderId = ? and orderStatus = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { appOrderId + "", orderStatus + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return syncMsg;
			}
			if (cursor.moveToFirst()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return syncMsg;
	}


	public static SyncMsg getSyncMsgByOrderIdBizDateCurrCount(int orderId, long businessDate, int currCount) {
		SyncMsg syncMsg = null;
		// 这边的11指的是同步到后台的log数据的类型 同pos里面 HttpApi的常量 LOG_DATA
		String sql = "select * from " + TableNames.SyncMsg
				+ " where orderId = ? and businessDate = ? and msg_type = 11 and currCount = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { orderId + "", businessDate + "", currCount + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return syncMsg;
			}
			if (cursor.moveToFirst()) {
				syncMsg = new SyncMsg();
				syncMsg.setId(cursor.getString(0));
				syncMsg.setOrderId(cursor.getInt(1));
				syncMsg.setMsgType(cursor.getInt(2));
				syncMsg.setData(cursor.getString(3));
				syncMsg.setStatus(cursor.getInt(4));
				syncMsg.setCreateTime(cursor.getLong(5));
				syncMsg.setRevenueId(cursor.getInt(6));
				syncMsg.setBusinessDate(cursor.getLong(7));
				syncMsg.setCurrCount(cursor.getInt(8));
				syncMsg.setAppOrderId(cursor.getInt(9));
				syncMsg.setOrderStatus(cursor.getInt(10));
				syncMsg.setOrderNum(cursor.getInt(11));
				syncMsg.setBillNo(cursor.getInt(12));
				syncMsg.setReportNo(cursor.getInt(13));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return syncMsg;
	}
	
	
	public static int getSyncMsgCurrCountByOrderId(int orderId) {
		// 这边的11指的是同步到后台的log数据的类型 同pos里面 HttpApi的常量 LOG_DATA
		String sql = "select max(currCount) from " + TableNames.SyncMsg
				+ " where orderId = ? and msg_type = 11";
		Cursor cursor = null;
		int currCount = 0;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] { orderId + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return currCount;
			}
			if (cursor.moveToFirst()) {
				currCount = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return currCount;
	}
	
	public static void deleteSyncMsg(SyncMsg syncMsg) {
		String sql = "delete from " + TableNames.SyncMsg + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { syncMsg.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateSyncMsgStatus(int status, long businessDate){

		String sql = "update " + TableNames.SyncMsg + " set status = ? where businessDate = ? and msg_type <> 1001";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {status, businessDate});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void deleteSyncMsgByBusinessDate(long businessDate) {
		String sql = "delete from " + TableNames.SyncMsg + " where businessDate = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { businessDate+"" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
