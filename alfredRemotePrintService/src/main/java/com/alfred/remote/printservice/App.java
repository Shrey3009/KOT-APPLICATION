package com.alfred.remote.printservice;

import com.alfredbase.BaseApplication;
import com.alfredbase.store.SQLExe;
import com.alfredbase.utils.CommonUtil;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends BaseApplication {
//    public static App instance;
//    private PrintJobManager printJobMgr;  
	public final String VERSION = "1.0.6";

	private static final String DATABASE_NAME = "com.alfred.fb.printerservice";
	String oldIp;
	
	@Override
	public void onCreate() {

		super.onCreate();
		SQLExe.init(this, DATABASE_NAME, DATABASE_VERSION);
		oldIp = CommonUtil.getLocalIpAddress();
		CrashReport.initCrashReport(getApplicationContext(), "900042907", isOpenLog);
		update15to16();
//		this.printJobMgr = new PrintJobManager(this);
//		instance = this;
	}

//	public PrintJobManager getPrintJobMgr() {
//		return printJobMgr;
//	}
	/*Fire IP Change Event*/
	protected void onIPChanged() {
		 String newip = CommonUtil.getLocalIpAddress();
		 if (!newip.equals(oldIp)) {
			 PrintService.instance.closeAllSockets();
			 oldIp = newip;
		 }
	}
	
	/*Connected or Discounted*/
	protected void onNetworkConnectionUpdate () {
		int m = 0;
		m=9;
		
	}

	@Override
	protected void onAppQuit() {
		super.onAppQuit();
	}
}
