package com.alfredbase.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLExe {
	private static DataHelper dataHelper;

	public static void init(Context _context, String name, int version) {
		if (dataHelper == null) {
			dataHelper = new DataHelper(_context, name, version);
		}
	}

	public synchronized static SQLiteDatabase getDB() {
		return dataHelper.getDb();
	}

	public synchronized static void closeDB() {
		if (dataHelper != null) {
			dataHelper.getDb().close();
			dataHelper = null;
		}
	}

}
