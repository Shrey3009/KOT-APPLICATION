package com.test.alfred.taskqueue;


import android.app.Activity;
import android.os.Bundle;

import com.path.android.jobqueue.JobManager;

public class MainActivity  extends Activity  {
	   JobManager kdsJobManager;	
	   
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        kdsJobManager = MainApp.getInstance().getJobManager();
	        kdsJobManager.clear();
	       // kdsJobManager.addJob(new KotJobs("kot1"));

	   }
}

