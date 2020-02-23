package com.alfred.printer;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.birbit.android.jobqueue.Params;

import java.util.ArrayList;

public class KickDrawerPrint extends PrintJob{

    public KickDrawerPrint(String uuid, Long bizDate) {
		super(new Params(Priority.MID).requireNetwork().persist().groupBy("drawer"),
				"drawer",  uuid, bizDate);
	}
	
	public ArrayList<PrintData> getData() {
		return data;
	}
	
	public void addKickOut() {
		PrintData drawer = new PrintData();
		drawer.setDataFormat(PrintData.FORMAT_DRAWER);
		this.data.add(drawer);		
	}

	public void setData(ArrayList<PrintData> data) {
		this.data = data;
	}

}
