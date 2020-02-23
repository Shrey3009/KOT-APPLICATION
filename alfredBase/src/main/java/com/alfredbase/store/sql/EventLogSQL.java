package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.EventLog;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;

/**
 * Created by Arif S. on 6/18/19
 */
public class EventLogSQL {

    public static void update(EventLog eventLog) {
        if (eventLog == null) {
            return;
        }
        String sql = "replace into "
                + TableNames.EventLog
                + " (id, custId,createdDate,event)"
                + "values (?,?,?,?)";
        try {
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{eventLog.getId(),
                            eventLog.getCustId(),
                            eventLog.getCreatedDate(),
                            eventLog.getEvent()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<EventLog> getAllEventLog() {
        ArrayList<EventLog> result = new ArrayList<>();
        String sql = "select * from " + TableNames.EventLog;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            EventLog eventLog = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                eventLog = new EventLog();
                eventLog.setId(cursor.getInt(0));
                eventLog.setCustId(cursor.getInt(1));
                eventLog.setCreatedDate(cursor.getInt(2));
                eventLog.setEvent(cursor.getString(3));
                result.add(eventLog);
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
}
