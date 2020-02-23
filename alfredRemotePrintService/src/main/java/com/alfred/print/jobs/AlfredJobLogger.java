package com.alfred.print.jobs;
import android.util.Log;

import com.alfred.remote.printservice.App;
import com.birbit.android.jobqueue.log.CustomLogger;

import java.util.Locale;


public class AlfredJobLogger implements CustomLogger {

	private String TAG = "JOBS";
	
    public AlfredJobLogger(String tAG) {
		super();
		TAG = tAG;
	}
    
    @Override
    public boolean isDebugEnabled() {
        return App.isOpenLog;
    }

    @Override
    public void d(String text, Object... args) {
        Log.d(TAG, String.format(Locale.US,text, args));
    }

    @Override
    public void e(Throwable t, String text, Object... args) {
        Log.e(TAG, String.format(Locale.US,text, args), t);
    }

    @Override
    public void e(String text, Object... args) {
        Log.e(TAG, String.format(Locale.US,text, args));
    }

    @Override
    public void v(String text, Object... args) {

    }
}
