package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.PrintQueueMsg;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;


public class PrintQueueMsgSQL {
//msgUUID TEXT PRIMARY KEY, charSize INTEGER, printerIp TEXT, data TEXT, status INTEGER, created LONG, bizDate LONG
	public static void add(PrintQueueMsg prtMsg) {
		if (prtMsg == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.PrintQueue
					+ "(msgUUID, msgType,charSize, printerIp, data, status, created, bizDate)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { prtMsg.getMsgUUID(), prtMsg.getMsgType(), prtMsg.getCharSize(),
							prtMsg.getPrinterIp(), prtMsg.getData(),
							prtMsg.getStatus(), prtMsg.getCreated(),prtMsg.getBizDate()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<PrintQueueMsg> getAllMsgs() {
		ArrayList<PrintQueueMsg> result = new ArrayList<PrintQueueMsg>();
		String sql = "select * from " + TableNames.PrintQueue;
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			PrintQueueMsg prtMsg = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				prtMsg = new PrintQueueMsg();
				prtMsg.setMsgUUID(cursor.getString(0));
				prtMsg.setMsgType(cursor.getString(1));
				prtMsg.setCharSize(cursor.getInt(2));
				prtMsg.setPrinterIp(cursor.getString(3));
				prtMsg.setData(cursor.getString(4));
				prtMsg.setStatus(cursor.getInt(5));
				prtMsg.setCreated(cursor.getLong(6));
				prtMsg.setBizDate(cursor.getLong(7));
				result.add(prtMsg);
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
	
	public static ArrayList<PrintQueueMsg> getTenUnsentPrintQueueMsg() {
		ArrayList<PrintQueueMsg> result = new ArrayList<PrintQueueMsg>();
		String sql = "select * from " + TableNames.PrintQueue
				+ " where status =  ?  LIMIT 10 ";
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql,
					new String[] { ParamConst.PRINTQUEUE_MSG_UN_SEND + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			PrintQueueMsg prtMsg = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				prtMsg = new PrintQueueMsg();
				prtMsg.setMsgUUID(cursor.getString(0));
				prtMsg.setMsgType(cursor.getString(1));
				prtMsg.setCharSize(cursor.getInt(2));
				prtMsg.setPrinterIp(cursor.getString(3));
				prtMsg.setData(cursor.getString(4));
				prtMsg.setStatus(cursor.getInt(5));
				prtMsg.setCreated(cursor.getLong(6));
				prtMsg.setBizDate(cursor.getLong(7));
				result.add(prtMsg);
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
	public static PrintQueueMsg getMsgById(String id, long created) {
		PrintQueueMsg prtMsg = null;
		String sql = "select * from " + TableNames.PrintQueue
				+ " where msgUUID= ? and created = ? ";
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {id, created+""});
			int count = cursor.getCount();
			if (count < 1) {
				return prtMsg;
			}
			if (cursor.moveToFirst()) {
				prtMsg = new PrintQueueMsg();
				prtMsg.setMsgUUID(cursor.getString(0));
				prtMsg.setMsgType(cursor.getString(1));
				prtMsg.setCharSize(cursor.getInt(2));
				prtMsg.setPrinterIp(cursor.getString(3));
				prtMsg.setData(cursor.getString(4));
				prtMsg.setStatus(cursor.getInt(5));
				prtMsg.setCreated(cursor.getLong(6));
				prtMsg.setBizDate(cursor.getLong(7));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return prtMsg;
	}
	public static PrintQueueMsg getUnsentMsgById(String id, long created) {
		PrintQueueMsg prtMsg = null;
		String sql = "select * from " + TableNames.PrintQueue
				+ " where msgUUID= ? and created = ? and status=?";
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {id, created+"", ParamConst.PRINTQUEUE_MSG_UN_SEND + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return prtMsg;
			}
			if (cursor.moveToFirst()) {
				prtMsg = new PrintQueueMsg();
				prtMsg.setMsgUUID(cursor.getString(0));
				prtMsg.setMsgType(cursor.getString(1));
				prtMsg.setCharSize(cursor.getInt(2));
				prtMsg.setPrinterIp(cursor.getString(3));
				prtMsg.setData(cursor.getString(4));
				prtMsg.setStatus(cursor.getInt(5));
				prtMsg.setCreated(cursor.getLong(6));
				prtMsg.setBizDate(cursor.getLong(7));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return prtMsg;
	}
	
	public static PrintQueueMsg getQueuedMsgById(String id, long created) {
		PrintQueueMsg prtMsg = null;
		String sql = "select * from " + TableNames.PrintQueue
				+ " where msgUUID= ? and created = ? and status=?";
		;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {id, created+"", ParamConst.PRINTQUEUE_MSG_QUEUED + ""});
			int count = cursor.getCount();
			if (count < 1) {
				return prtMsg;
			}
			if (cursor.moveToFirst()) {
				prtMsg = new PrintQueueMsg();
				prtMsg.setMsgUUID(cursor.getString(0));
				prtMsg.setMsgType(cursor.getString(1));
				prtMsg.setCharSize(cursor.getInt(2));
				prtMsg.setPrinterIp(cursor.getString(3));
				prtMsg.setData(cursor.getString(4));
				prtMsg.setStatus(cursor.getInt(5));
				prtMsg.setCreated(cursor.getLong(6));
				prtMsg.setBizDate(cursor.getLong(7));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return prtMsg;
	}

	public static void updatePrintQueueMsgStatus(int status, String id, long created){
		String sql = "update " + TableNames.PrintQueue + " set status = ? where msgUUID= ? and created = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { status+"", id, created+"" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void deleteSuccessedMsgs() {
		String sql = "delete from " + TableNames.PrintQueue + " where status = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { ParamConst.PRINTQUEUE_MSG_SUCCESS+"" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllMsgs() {
		String sql = "delete from " + TableNames.PrintQueue;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deletePrintQueueMsgByBusinessDate(long businessDate) {
		String sql = "delete from " + TableNames.PrintQueue + " where bizDate < ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { businessDate+"" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
