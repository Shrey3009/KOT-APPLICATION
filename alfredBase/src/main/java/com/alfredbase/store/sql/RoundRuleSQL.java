package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.RoundRule;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class RoundRuleSQL {

	public static void update(RoundRule roundRule) {
		if (roundRule == null) {
			return;
		}
		try {
			String sql = "replace into " + TableNames.RoundRule
					+ "(id, country, status, ruleType )" + " values (?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { roundRule.getId(), roundRule.getCountry(),
							roundRule.getStatus(), roundRule.getRuleType() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addRoundRuleList(List<RoundRule> roundRules) {
		if (roundRules == null) {
			return;
		}
		SQLiteDatabase db = SQLExe.getDB();
		try {
			db.beginTransaction();
			String sql = "replace into " + TableNames.RoundRule
					+ "(id, country, status, ruleType )" + " values (?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(
					sql);
				for (RoundRule roundRule : roundRules) {
					SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
							roundRule.getId());
					SQLiteStatementHelper.bindString(sqLiteStatement, 2,
							roundRule.getCountry());
					SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
							roundRule.getStatus());
					SQLiteStatementHelper.bindString(sqLiteStatement, 4,
							roundRule.getRuleType());
					sqLiteStatement.executeInsert();
				}
				db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public static ArrayList<RoundRule> getAllRoundRule() {
		ArrayList<RoundRule> result = new ArrayList<RoundRule>();
		String sql = "select * from " + TableNames.RoundRule;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			RoundRule roundRule = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				roundRule = new RoundRule();
				roundRule.setId(cursor.getInt(0));
				roundRule.setCountry(cursor.getString(1));
				roundRule.setStatus(cursor.getInt(2));
				roundRule.setRuleType(cursor.getString(3));
				result.add(roundRule);
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

	public static void deleteRoundRule(RoundRule roundRule) {
		String sql = "delete from " + TableNames.RoundRule + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { roundRule.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllRoundRule() {
		String sql = "delete from " + TableNames.RoundRule;
		try {
			SQLExe.getDB().execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
