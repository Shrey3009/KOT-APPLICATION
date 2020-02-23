package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.EmpWorkLog;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class EmpWorkLogSQL {

	public static void update(EmpWorkLog empWorkLog) {
		if (empWorkLog == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.EmpWorkLog
					+ "(id, userId, empId, empName, loginTime, logoutTime,totalHours,status)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLExe.getDB()
					.execSQL(
							sql,
							new Object[] { empWorkLog.getId(),
									empWorkLog.getUserId(),
									empWorkLog.getEmpId(),
									empWorkLog.getEmpName(),
									empWorkLog.getLoginTime(),
									empWorkLog.getLogoutTime(),
									empWorkLog.getTotalHours(),
									empWorkLog.getStatus() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addEmpWorkLogList(List<EmpWorkLog> empWorkLogs) {
		if (empWorkLogs == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.EmpWorkLog
					+ "(id, userId, empId, empName, loginTime, logoutTime,totalHours,status)"
					+ " values (?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (EmpWorkLog empWorkLog : empWorkLogs) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							empWorkLog.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							empWorkLog.getUserId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							empWorkLog.getEmpId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							empWorkLog.getEmpName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
							empWorkLog.getLoginTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							empWorkLog.getLogoutTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							empWorkLog.getTotalHours());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
							empWorkLog.getStatus());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			db.endTransaction();
		}
	}

	public static ArrayList<EmpWorkLog> getAllEmpWorkLog() {
		ArrayList<EmpWorkLog> result = new ArrayList<EmpWorkLog>();
		String sql = "select * from " + TableNames.EmpWorkLog;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			EmpWorkLog empWorkLog = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				empWorkLog = new EmpWorkLog();
				empWorkLog.setId(cursor.getInt(0));
				empWorkLog.setUserId(cursor.getInt(1));
				empWorkLog.setEmpId(cursor.getInt(2));
				empWorkLog.setEmpName(cursor.getString(3));
				empWorkLog.setLoginTime(cursor.getLong(4));
				empWorkLog.setLogoutTime(cursor.getLong(5));
				empWorkLog.setTotalHours(cursor.getInt(6));
				empWorkLog.setStatus(cursor.getInt(7));
				result.add(empWorkLog);
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

	public static void deleteEmpWorkLog(EmpWorkLog empWorkLog) {
		String sql = "delete from " + TableNames.EmpWorkLog + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { empWorkLog.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
