package com.alfredkds.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Messenger;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class AlfredAlarmReceiver extends WakefulBroadcastReceiver {

	private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public AlfredAlarmReceiver() {
		super();
	}
    
    
    @Override
    public void onReceive(Context context, Intent intent) {   
        Intent service = new Intent(context, AlfredSchedulingService.class);
        service.setAction(AlfredSchedulingService.ACTION);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, Handler handler) {
    	Log.d(AlfredAlarmReceiver.class.getName(), "start Alarm");
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlfredAlarmReceiver.class);
        intent.putExtra("handler", new Messenger(handler));
        
        alarmIntent = PendingIntent.getBroadcast(context, 234321243, intent, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, alarmIntent);
        ComponentName receiver = new ComponentName(context, AlarmBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);  
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        ComponentName receiver = new ComponentName(context, AlarmBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);         
    }
}
