package com.alfredbase.utils;

import java.math.BigDecimal;

import android.database.sqlite.SQLiteStatement;

public class SQLiteStatementHelper {

	public static void bindLong(SQLiteStatement sqLiteStatement, int index,
			Integer value) {
		if (value == null) {
			sqLiteStatement.bindNull(index);
		} else {
			sqLiteStatement.bindLong(index, value);
		}
	}
	
	public static void bindLong(SQLiteStatement sqLiteStatement, int index,
			Long value) {
		if (value == null) {
			sqLiteStatement.bindNull(index);
		} else {
			sqLiteStatement.bindLong(index, value);
		}
	}

	public static void bindString(SQLiteStatement sqLiteStatement, int index,
			String value) {
		if (value == null) {
			sqLiteStatement.bindNull(index);
		} else {
			sqLiteStatement.bindString(index, value);
		}
	}
	public static void bindDouble(SQLiteStatement sqLiteStatement, int index,
			Double value) {
		if (value == null) {
			sqLiteStatement.bindNull(index);
		} else {
			sqLiteStatement.bindDouble(index, value.doubleValue());
		}
	}
	public static void bindString(SQLiteStatement sqLiteStatement, int index,
			BigDecimal value) {
		if (value == null) {
			sqLiteStatement.bindNull(index);
		} else {
			sqLiteStatement.bindString(index, value.toString());
		}
	}
}
