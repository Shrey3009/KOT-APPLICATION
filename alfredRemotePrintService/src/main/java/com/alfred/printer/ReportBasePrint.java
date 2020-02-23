package com.alfred.printer;

import android.text.TextUtils;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.birbit.android.jobqueue.Params;

public class ReportBasePrint extends PrintJob{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2958025737558392180L;
	public final static int FIXED_COL3_SPACE = 2;
	public final static int FIXED_COL3_QTY = 10; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL3_TOTAL = 12; //in case of 48 dots width, QTY col = 12dots
	
	public static int COL3_ITEMNAME; // Width = CharSize/scale - FIXED_COL3_QTY/scale - 
	                                      // FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE * 2

	protected ReportBasePrint(String groupName, 
							String uuid, Long bizDate) {
		super(new Params(Priority.MID).requireNetwork().persist().groupBy(groupName), groupName,  uuid, bizDate);
	}
	
	public void AddReportHeader(String restaurant, String reportType, String reportName) {
		//restaurant name
		PrintData rInfo = new PrintData();
		rInfo.setDataFormat(PrintData.FORMAT_TXT);
		rInfo.setFontsize(1);
		rInfo.setTextAlign(PrintData.ALIGN_CENTRE);
		rInfo.setText(restaurant);
		this.data.add(rInfo);	

		if (!TextUtils.isEmpty(reportType)) {
			//X Reading / Z Reading
			StringBuilder ctbuf = new StringBuilder();
			ctbuf.append(reNext).append(reportType);
			PrintData radd = new PrintData();
			radd.setDataFormat(PrintData.FORMAT_TXT);
			radd.setFontsize(1);
			radd.setMarginTop(10);
			radd.setTextAlign(PrintData.ALIGN_CENTRE);
			radd.setText(ctbuf.toString());
			this.data.add(radd);
		}
		
		if (!TextUtils.isEmpty(reportName)) {
	        //report name
			StringBuilder rpbuf = new StringBuilder();
			rpbuf.append(reNext).append(reportName).append(reNext);
			PrintData reportNamePrint = new PrintData();
			reportNamePrint.setDataFormat(PrintData.FORMAT_TXT);
			reportNamePrint.setFontsize(1);
			reportNamePrint.setMarginTop(10);
			reportNamePrint.setTextAlign(PrintData.ALIGN_CENTRE);
			reportNamePrint.setText(rpbuf.toString());
			this.data.add(reportNamePrint);
		}
		addHortionalLine(this.charSize);
	}
	
	public void AddHeader(String op, String reportNo, String dateTime, String bizDate,int trainType) {
		
		PrintData cashierPrint = new PrintData();
		String cashierLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.op),
				this.FIXED_COL3_TOTAL);
		String cashierStr = cashierLabel+":"+op+reNext;
		cashierPrint.setDataFormat(PrintData.FORMAT_TXT);
		cashierPrint.setTextAlign(PrintData.ALIGN_LEFT);
		cashierPrint.setText(cashierStr);
		this.data.add(cashierPrint);
		String trainString = "";
		if(trainType==1){
			trainString=PrintService.instance.getResources().getString(R.string.training);
		}
		//report NO
		PrintData billNoPrint = new PrintData();
		String billNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.report_no_), this.FIXED_COL3_TOTAL);
		String padBillNo = billNoStr+":"+reportNo+trainString+reNext;
		billNoPrint.setDataFormat(PrintData.FORMAT_TXT);
		billNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
		billNoPrint.setText(padBillNo);
		this.data.add(billNoPrint);

//		if(trainType==1){
//			PrintData trainPrint = new PrintData();
//			String trainLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.mode), this.FIXED_COL3_TOTAL);
//			String trainStr = trainLabel + ":" + PrintService.instance.getResources().getString(R.string.train) + reNext;
//			trainPrint.setDataFormat(PrintData.FORMAT_TXT);
//			trainPrint.setTextAlign(PrintData.ALIGN_LEFT);
//			trainPrint.setText(trainStr);
//			this.data.add(trainPrint);
//		}
		//Business Date
		PrintData bizDatePrint = new PrintData();
		String bdateLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.business_date), this.FIXED_COL3_TOTAL);
		String bdateStr = bdateLabel+":"+bizDate+reNext;
		bizDatePrint.setDataFormat(PrintData.FORMAT_TXT);
		bizDatePrint.setTextAlign(PrintData.ALIGN_LEFT);
		bizDatePrint.setText(bdateStr);
		this.data.add(bizDatePrint);

		
//		//Date
//		PrintData datePrint = new PrintData();
//		String dateLabel = StringUtil.padRight("Printed at", this.FIXED_COL3_TOTAL);
//		String dateStr = dateLabel+":"+dateTime+"\r\n";
//		datePrint.setDataFormat(PrintData.FORMAT_TXT);
//		datePrint.setTextAlign(PrintData.ALIGN_LEFT);
//		datePrint.setText(dateStr);
//		this.data.add(datePrint);
		
		//addHortionalLine();
		addHortionaDoublelLine(this.charSize);
	}


	public void AddHortionaDoublelLine() {


//		//Date
//		PrintData datePrint = new PrintData();
//		String dateLabel = StringUtil.padRight("Printed at", this.FIXED_COL3_TOTAL);
//		String dateStr = dateLabel+":"+dateTime+"\r\n";
//		datePrint.setDataFormat(PrintData.FORMAT_TXT);
//		datePrint.setTextAlign(PrintData.ALIGN_LEFT);
//		datePrint.setText(dateStr);
//		this.data.add(datePrint);

		//addHortionalLine();
		addHortionaDoublelLine(this.charSize);
	}
	//for monthly report only
	public void AddHeader(String op, String startDate, String endDate) {
		
		PrintData cashierPrint = new PrintData();
		String cashierLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.op),
				this.FIXED_COL3_TOTAL);
		String cashierStr = cashierLabel+":"+op+reNext;
		cashierPrint.setDataFormat(PrintData.FORMAT_TXT);
		cashierPrint.setTextAlign(PrintData.ALIGN_LEFT);
		cashierPrint.setText(cashierStr);
		this.data.add(cashierPrint);
		
		//report NO
		PrintData billNoPrint = new PrintData();
		String billNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.monthly_start_date), this.FIXED_COL3_TOTAL);
		String padBillNo = billNoStr+":"+startDate+reNext;
		billNoPrint.setDataFormat(PrintData.FORMAT_TXT);
		billNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
		billNoPrint.setText(padBillNo);
		this.data.add(billNoPrint);
		
		//Business Date
		PrintData bizDatePrint = new PrintData();
		String bdateLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.monthly_end_date), this.FIXED_COL3_TOTAL);
		String bdateStr = bdateLabel+":"+endDate+reNext;
		bizDatePrint.setDataFormat(PrintData.FORMAT_TXT);
		bizDatePrint.setTextAlign(PrintData.ALIGN_LEFT);
		bizDatePrint.setText(bdateStr);
		this.data.add(bizDatePrint);

		addHortionaDoublelLine(this.charSize);
	}
	
	public void getDollarSign(String sign, int lang) {
		PrintData datePrint = new PrintData();
		String dateStr = sign;
		datePrint.setDataFormat(PrintData.FORMAT_TXT);
		datePrint.setTextAlign(PrintData.ALIGN_LEFT);
		datePrint.setLanguage(lang);
		datePrint.setText(dateStr);
		this.data.add(datePrint);		
	}
	
	public void AddFooter(String op) {
		
		addHortionalLine(this.charSize);
		addSingleLineText(this.charSize, op, 0);
		AddCut();		
	}	
}
