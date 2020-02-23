package com.alfredposclient.jobs;
import android.util.Log;

import com.alfredposclient.global.App;
import com.path.android.jobqueue.log.CustomLogger;

import java.util.Locale;

public class AlfredJobLogger implements CustomLogger {

	private String TAG = "JOBS";
	
    public AlfredJobLogger(String tAG) {
		super();
		TAG = tAG;
	}
    
    @Override
    public boolean isDebugEnabled() {
        return App.isDebug;
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
}
