package com.alfred.remote.printservice;

import android.content.Context;
import android.util.Log;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.PrintManager;
import com.alfred.print.jobs.Priority;
import com.alfredbase.ParamConst;
import com.alfredbase.javabean.PrintQueueMsg;
import com.alfredbase.store.sql.PrintQueueMsgSQL;
import com.google.gson.Gson;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.Params;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PrinterQueueManager {
	
	private Context context;
    private ScheduledExecutorService scheduler = null;
    static final String TAG = "PrinterQueueManager";

    public PrinterQueueManager(Context mContext) {
		super();
		this.context = mContext;
		scheduler = Executors.newSingleThreadScheduledExecutor();
		clearSuccessedJob();

	}
    

    private PrintJob createJobFromQueue(PrintQueueMsg msg) {
    	String tag = "others";
    	PrintJob job = null;
    	Gson gson = new Gson();
    	if (msg != null) {
	    	Params param = new Params(Priority.MID).requireNetwork().persist().groupBy(msg.getMsgType());
			//Params params,String msgType, String uuid, Long bizDate, Long created		
	    	job = new PrintJob(param, msg.getMsgType(),msg.getMsgUUID(), msg.getBizDate(), msg.getCreated());
	    	//ArrayList<PrintData> data =  gson.fromJson(msg.getData(), new TypeToken<ArrayList<PrintData>>(){}.getType());
	    	job.setCharSize(msg.getCharSize());
	    	job.setPrinterIp(msg.getPrinterIp());
//	    	job.setData(null);
    	}
    	return job;
    }

    private void setupSyncScheduler() {
		//sync scheduler
    	scheduler.scheduleAtFixedRate
	      (new Runnable() {
	         public void run() {
	    	    	ArrayList<PrintQueueMsg> messages = PrintQueueMsgSQL.getTenUnsentPrintQueueMsg();
	    	    	Log.d(TAG, "PrintQueue run");
	    	    	for (PrintQueueMsg msg:messages) {
	    	             //add to print JOB
						Log.d(TAG, "PrintQueue size " + messages.size() + " now index:" + messages.indexOf(msg));
	    	    		PrintManager printMgr = ((PrintService)context).getPrintMgr();
	    	    		JobManager printJobMgr = printMgr.configureJobManager(msg.getPrinterIp());
	    	    		if (printJobMgr != null) {
							//TODO to test Create the same
		    	    		printMgr.addJob(msg.getPrinterIp(), createJobFromQueue(msg));	    	    			
	    	    		}
	        	}	        	 
	         }
	      }, 0, 30, TimeUnit.SECONDS);
    	
    }
    
    public String getDataUUID(String num){
    	return num+"-"+UUID.randomUUID().toString();
    }
    
    private void clearSuccessedJob() {
    	PrintQueueMsgSQL.deleteSuccessedMsgs();
    }
    public void start() {
       if (scheduler!=null)
		 setupSyncScheduler();
    }
    public void stop(){
    	if (scheduler!= null) 
    		scheduler.shutdown();
    	    clearSuccessedJob();
    }
    public void queuePrint(PrintQueueMsg msg) {
        if (msg != null) {
            String ip = msg.getPrinterIp();
            String uuid = msg.getMsgUUID();
            String type = msg.getMsgType();
            long bizDate = msg.getBizDate();
            long created = msg.getCreated();
            String data = msg.getData();
            
			if (PrintQueueMsgSQL.getMsgById(uuid, created) == null)  {
				if (data != null) {
						Gson gson = new Gson();
						PrintQueueMsg syncMsg = new PrintQueueMsg();
						syncMsg.setMsgUUID(uuid);
						syncMsg.setMsgType(type);
						syncMsg.setData(data);
						syncMsg.setCreated(created);
						syncMsg.setStatus(ParamConst.PRINTQUEUE_MSG_UN_SEND);
						syncMsg.setBizDate(bizDate);
						syncMsg.setPrinterIp(ip);
						PrintQueueMsgSQL.add(syncMsg);
				}
			}
        }
	}  
}
