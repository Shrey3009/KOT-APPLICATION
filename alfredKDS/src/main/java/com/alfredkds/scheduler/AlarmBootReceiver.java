package com.alfredkds.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmBootReceiver extends BroadcastReceiver {
    AlfredAlarmReceiver alarm = new AlfredAlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context, null);
        }
    }
}

