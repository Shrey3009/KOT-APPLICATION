package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.User;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class UserSQL {

	public static void addUser(User user) {
		if (user == null) {
			return;
		}
		try {
			String sql = "replace into "
					+ TableNames.User
					+ "(id,empId,  type,  status, accountName,  userName,  password,firstName,  lastName,  nickName,companyId,  createTime,  updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { user.getId(), user.getEmpId(),
							user.getType(), user.getStatus(),
							user.getAccountName(), user.getUserName(),
							user.getPassword(), user.getFirstName(),
							user.getLastName(), user.getNickName(),
							user.getCompanyId(), user.getCreateTime(),
							user.getUpdateTime() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addUsers(List<User> users) {
		if (users == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.User
					+ "(id,empId,  type,  status, accountName,  userName,  password,firstName,  lastName,  nickName,companyId,  createTime,  updateTime)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (User user : users) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							user.getId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
							user.getEmpId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							user.getType());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
							user.getStatus());
					SQLiteStatementHelper.bindString(sqLiteStatement, 5,
							user.getAccountName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 6,
							user.getUserName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 7,
							user.getPassword());
					SQLiteStatementHelper.bindString(sqLiteStatement, 8,
							user.getFirstName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 9,
							user.getLastName());
					SQLiteStatementHelper.bindString(sqLiteStatement, 10,
							user.getNickName());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
							user.getCompanyId());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
							user.getCreateTime());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
							user.getUpdateTime());

					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<User> getAllUser() {
		ArrayList<User> result = new ArrayList<User>();
		String sql = "select * from " + TableNames.User;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			User user = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				user = new User();
				user.setId(cursor.getInt(0));
				user.setEmpId(cursor.getInt(1));
				user.setType(cursor.getInt(2));
				user.setStatus(cursor.getInt(3));
				user.setAccountName(cursor.getString(4));
				user.setUserName(cursor.getString(5));
				user.setPassword(cursor.getString(6));
				user.setFirstName(cursor.getString(7));
				user.setLastName(cursor.getString(8));
				user.setNickName(cursor.getString(9));
				user.setCompanyId(cursor.getInt(10));
				user.setCreateTime(cursor.getLong(11));
				user.setUpdateTime(cursor.getLong(12));
				result.add(user);
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
	
	public static User getUserById(int userId) {
		User result = null;
		String sql = "select * from " + TableNames.User + " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql, new String[] {String.valueOf(userId)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			User user = null;
			if (cursor.moveToFirst()) {
				user = new User();
				user.setId(cursor.getInt(0));
				user.setEmpId(cursor.getInt(1));
				user.setType(cursor.getInt(2));
				user.setStatus(cursor.getInt(3));
				user.setAccountName(cursor.getString(4));
				user.setUserName(cursor.getString(5));
				user.setPassword(cursor.getString(6));
				user.setFirstName(cursor.getString(7));
				user.setLastName(cursor.getString(8));
				user.setNickName(cursor.getString(9));
				user.setCompanyId(cursor.getInt(10));
				user.setCreateTime(cursor.getLong(11));
				user.setUpdateTime(cursor.getLong(12));
				return user;
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
	public static void updateUserPassword(String password, User user){
		String sql = "update " + TableNames.User + " set password = ? where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {password, user.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteUser(User user) {
		String sql = "delete from " + TableNames.User + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { user.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllUser() {
		String sql = "delete from " + TableNames.User;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
