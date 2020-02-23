package com.alfred.printer;

import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.alfredbase.javabean.MonthlySalesReport;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TimeUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class MonthlySalesReportPrint extends ReportBasePrint{

	public final static int FIXED_COL3_SPACE = 2;
	public final static int FIXED_COL3_QTY = 10; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL3_TOTAL = 12; //in case of 48 dots width, QTY col = 12dots
	
	public static int COL3_ITEMNAME; // Width = CharSize/scale - FIXED_COL3_QTY/scale - 
	                                      // FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE * 2

	private String OP;
	private String reportNo;
	private String startDate;
	private String endDate;
	
	private ArrayList<MonthlySalesReport> reportSales;

	public MonthlySalesReportPrint(String uuid) {
		super("monthlyReport", uuid, 0L);
	}
	
	public ArrayList<PrintData> getData() {
		return data;
	}
	
	public void print(ArrayList<MonthlySalesReport> reportSales ) {
		this.reportSales = reportSales;
		GetReportDaySalesText();
	}


	/* Four columns layout (Width = 48dots)
	 * |item Name    38/scale   | QTY 10/scale  |
	 * 
	 **/	
	private String getThreeColHeader(String col1Title, String col2Title, String col3Title) {
		
		StringBuffer ret = new StringBuffer();
		MonthlySalesReportPrint.COL3_ITEMNAME = this.charSize -  MonthlySalesReportPrint.FIXED_COL3_QTY - MonthlySalesReportPrint.FIXED_COL3_TOTAL;
		String title1 = StringUtil.padRight(col1Title, MonthlySalesReportPrint.COL3_ITEMNAME);
		String title2 = StringUtil.padRight(col2Title, MonthlySalesReportPrint.FIXED_COL3_QTY);
		String title3 = StringUtil.padLeft(col3Title, MonthlySalesReportPrint.FIXED_COL3_TOTAL);
		ret.append(title1).append(title2).append(title3).append(reNext);

		return ret.toString();
	}
	
	/* Four columns layout (Width = 48dots)
	 * |item Name   Dynamical |2| QTY  scale  | Total|
	 * 
	 **/
	private String GetThreeColContent(String col1Content, String col2Content, 
										String col3Content, int charScale) {
		
		StringBuffer result = new StringBuffer();

		int col1Lines = 1;
		int col2Lines = 1;
		int col3Lines = 1;
		
		MonthlySalesReportPrint.COL3_ITEMNAME = (this.charSize -MonthlySalesReportPrint.FIXED_COL3_TOTAL - MonthlySalesReportPrint.FIXED_COL3_QTY)/charScale - 2*MonthlySalesReportPrint.FIXED_COL3_SPACE;
		
		double ln1 = col1Content.length();
		try {
			ln1 = (col1Content.getBytes("GBK").length)/(MonthlySalesReportPrint.COL3_ITEMNAME*1.0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		col1Lines = StringUtil.nearestTen(ln1);
		String col1PadContent = StringUtil.padRight(col1Content, col1Lines*MonthlySalesReportPrint.COL3_ITEMNAME);
		ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, MonthlySalesReportPrint.COL3_ITEMNAME);
		
		double ln2 = (col2Content.length())/(MonthlySalesReportPrint.FIXED_COL3_QTY*1.0/charScale);
		col2Lines = StringUtil.nearestTen(ln2);
		String col2PadContent = StringUtil.padLeft(col2Content, col2Lines*MonthlySalesReportPrint.FIXED_COL3_QTY/charScale);
		ArrayList<String> splittedCol2Content = StringUtil.splitEqually(col2PadContent, MonthlySalesReportPrint.FIXED_COL3_QTY/charScale);

		double ln3 = (col3Content.length())/(MonthlySalesReportPrint.FIXED_COL3_TOTAL*1.0/charScale);
		col3Lines = StringUtil.nearestTen(ln3);
		String col3PadContent = StringUtil.padLeft(col3Content, col3Lines*MonthlySalesReportPrint.FIXED_COL3_TOTAL/charScale);
		ArrayList<String> splittedCol3Content = StringUtil.splitEqually(col3PadContent, MonthlySalesReportPrint.FIXED_COL3_TOTAL/charScale);

		
		for (int i=0; i< Math.max(Math.max(col1Lines, col2Lines),col3Lines); i++) {
			if (i<col1Lines) {			
				result.append(splittedCol1Content.get(i));
			}else{
				result.append(StringUtil.padRight(" ", MonthlySalesReportPrint.COL3_ITEMNAME));
			}
			//padding
			result.append(StringUtil.padRight(" ", MonthlySalesReportPrint.FIXED_COL3_SPACE));
			
			if (i<col2Lines) {
				result.append(splittedCol2Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (MonthlySalesReportPrint.FIXED_COL3_QTY)/charScale));
			}

			//padding
			result.append(StringUtil.padRight(" ", MonthlySalesReportPrint.FIXED_COL3_SPACE));
			
			if (i<col3Lines) {
				result.append(splittedCol3Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (MonthlySalesReportPrint.FIXED_COL3_TOTAL)/charScale));
			}			
			result.append(reNext);
		}
		return result.toString();
	}
	
	public void AddContentListHeader(String itemName, String qty, String total){
		PrintData header = new PrintData();
		header.setDataFormat(PrintData.FORMAT_TXT);
		header.setText(this.getThreeColHeader(itemName, qty, total));
		this.data.add(header);
		addHortionalLine(this.charSize);
	}
	public void addItemWithLang(String itemName, String qty, String amount, int lan, int scale) {
		PrintData kot = new PrintData();
		kot.setDataFormat(PrintData.FORMAT_TXT);
		kot.setFontsize(scale);
		kot.setLanguage(lan);
		kot.setText(this.GetThreeColContent(itemName, qty, amount, scale));
		this.data.add(kot);
	}
	
	public void addItem(String itemName, String qty, String amount, int scale) {
		PrintData kot = new PrintData();
		kot.setDataFormat(PrintData.FORMAT_TXT);
		kot.setFontsize(scale);
		kot.setLanguage(PrintData.LANG_CN);
		kot.setText(this.GetThreeColContent(itemName, qty, amount, scale));
		this.data.add(kot);
	}
	public void addSectionHeader(String name) {
		String centerName = StringUtil.padCenterWithDash(name, this.charSize);
		PrintData kot = new PrintData();
		kot.setDataFormat(PrintData.FORMAT_TXT);
		kot.setFontsize(1);
		kot.setTextBold(1);
		kot.setTextAlign(PrintData.ALIGN_CENTRE);
		kot.setLanguage(PrintData.LANG_CN);
		kot.setText(centerName+reNext);
		this.data.add(kot);
	}	
	/*
	 * Day Sales Report*/
	private void GetReportDaySalesText() {
		//StringBuffer sbr = new StringBuffer();
		for (MonthlySalesReport item : reportSales) {
			String bizDate = TimeUtil.getPrintDate(item.getBusinessDate());

			BigDecimal grossSale = BH.getBD(String.valueOf(item.getItemSales()));
			BigDecimal nettSale = BH.getBD(String.valueOf(item.getTotalSales()));
			int pax = item.getPersonQty();
			
			this.addItem(bizDate, "", "", 1);
			this.addItem(PrintService.instance.getResources().getString(R.string.pax), "", ""+pax, 1);	
			this.addItem(PrintService.instance.getResources().getString(R.string.item_sales), "", grossSale.toString(), 1);
			this.addItem(PrintService.instance.getResources().getString(R.string.nett_sales), "", nettSale.toString(), 1);	
			this.addHortionalLine(this.charSize);
			
		}
	}
	
	public void setData(ArrayList<PrintData> data) {
		this.data = data;
	}
}
