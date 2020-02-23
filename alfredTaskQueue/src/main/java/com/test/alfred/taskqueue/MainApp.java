package com.test.alfred.taskqueue;

import android.app.Application;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

public class MainApp extends Application {
    private static MainApp instance;
    private JobManager jobManager;
    private JobManager printerJobManager;
    
    private CustomLogger joblogger = new CustomLogger() {
        private static final String TAG = "JOBS";
        @Override
        public boolean isDebugEnabled() {
            return true;
        }

        @Override
        public void d(String text, Object... args) {
            Log.d(TAG, String.format(text, args));
        }

        @Override
        public void e(Throwable t, String text, Object... args) {
            Log.e(TAG, String.format(text, args), t);
        }

        @Override
        public void e(String text, Object... args) {
            Log.e(TAG, String.format(text, args));
        }
    };
    
    public MainApp() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configureJobManager();
        configurePrinterJobManager();
    }
   
    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
        .customLogger(joblogger)
        .id("kot_jobs")
        .minConsumerCount(1)//always keep at least one consumer alive
        .maxConsumerCount(3)//up to 3 consumers at a time
        .loadFactor(3)//3 jobs per consumer
        .consumerKeepAlive(120)//wait 2 minute
        .build();
        jobManager = new JobManager(this, configuration);
    }

    private void configurePrinterJobManager() {
        Configuration configuration = new Configuration.Builder(this)
        .customLogger(joblogger)
        .id("printer_jobs")
        .minConsumerCount(1)//always keep at least one consumer alive
        .maxConsumerCount(3)//up to 3 consumers at a time
        .loadFactor(3)//3 jobs per consumer
        .consumerKeepAlive(120)//wait 2 minute
        .build();
        printerJobManager = new JobManager(this,configuration);
    }
    
    public JobManager getJobManager() {
        return jobManager;
    }

    public JobManager getPrinterJobManager() {
        return printerJobManager;
    }
    
    public static MainApp getInstance() {
        return instance;
    }
    
}
