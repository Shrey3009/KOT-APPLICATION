package com.alfred.printer;

import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.model.ReportEntItem;
import com.alfredbase.utils.BH;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EntItemReportPrint extends ReportBasePrint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1926671330908858662L;
	public final static int FIXED_COL3_SPACE = 2;
	public final static int FIXED_COL3_QTY = 10; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL3_TOTAL = 12; //in case of 48 dots width, QTY col = 12dots
	
	public static int COL3_ITEMNAME; // Width = CharSize/scale - FIXED_COL3_QTY/scale - 
	                                      // FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE * 2

	private ArrayList<ReportEntItem> reportEntItems;
	
	public EntItemReportPrint(String uuid, Long bizDate) {
		super("ENT",  uuid, bizDate);
	}
	
	public ArrayList<PrintData> getData() {
		return data;
	}
	
	public void print(ArrayList<ReportEntItem> reportEntItems) {
		this.reportEntItems = reportEntItems;
		getEntItemStr();
	}


	/* Four columns layout (Width = 48dots)
	 * |item Name    38/scale   | QTY 10/scale  |
	 * 
	 **/	
	private String GetThreeColHeader(String col1Title, String col2Title, String col3Title) {
		
		StringBuffer ret = new StringBuffer();
		EntItemReportPrint.COL3_ITEMNAME = this.charSize -  EntItemReportPrint.FIXED_COL3_QTY - EntItemReportPrint.FIXED_COL3_TOTAL;
		String title1 = StringUtil.padRight(col1Title, EntItemReportPrint.COL3_ITEMNAME);
		String title2 = StringUtil.padRight(col2Title, EntItemReportPrint.FIXED_COL3_QTY);
		String title3 = StringUtil.padLeft(col3Title, EntItemReportPrint.FIXED_COL3_TOTAL);
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
		
		EntItemReportPrint.COL3_ITEMNAME = (this.charSize -EntItemReportPrint.FIXED_COL3_TOTAL - EntItemReportPrint.FIXED_COL3_QTY)/charScale - 2*EntItemReportPrint.FIXED_COL3_SPACE;
		
		double ln1 = col1Content.length();
		try {
			ln1 = (col1Content.getBytes("GBK").length)/(EntItemReportPrint.COL3_ITEMNAME*1.0);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			ln1 = col1Content.length();
		}
		col1Lines = StringUtil.nearestTen(ln1);
		String col1PadContent = StringUtil.padRight(col1Content, col1Lines*EntItemReportPrint.COL3_ITEMNAME);
		ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, EntItemReportPrint.COL3_ITEMNAME);
		
		double ln2 = (col2Content.length())/(EntItemReportPrint.FIXED_COL3_QTY*1.0/charScale);
		col2Lines = StringUtil.nearestTen(ln2);
		String col2PadContent = StringUtil.padLeft(col2Content, col2Lines*EntItemReportPrint.FIXED_COL3_QTY/charScale);
		ArrayList<String> splittedCol2Content = StringUtil.splitEqually(col2PadContent, EntItemReportPrint.FIXED_COL3_QTY/charScale);

		double ln3 = (col3Content.length())/(EntItemReportPrint.FIXED_COL3_TOTAL*1.0/charScale);
		col3Lines = StringUtil.nearestTen(ln3);
		String col3PadContent = StringUtil.padLeft(col3Content, col3Lines*EntItemReportPrint.FIXED_COL3_TOTAL/charScale);
		ArrayList<String> splittedCol3Content = StringUtil.splitEqually(col3PadContent, EntItemReportPrint.FIXED_COL3_TOTAL/charScale);

		
		for (int i=0; i< Math.max(Math.max(col1Lines, col2Lines),col3Lines); i++) {
			if (i<col1Lines) {			
				result.append(splittedCol1Content.get(i));
			}else{
				result.append(StringUtil.padRight(" ", EntItemReportPrint.COL3_ITEMNAME));
			}
			//padding
			result.append(StringUtil.padRight(" ", EntItemReportPrint.FIXED_COL3_SPACE));
			
			if (i<col2Lines) {
				result.append(splittedCol2Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (EntItemReportPrint.FIXED_COL3_QTY)/charScale));
			}

			//padding
			result.append(StringUtil.padRight(" ", EntItemReportPrint.FIXED_COL3_SPACE));
			
			if (i<col3Lines) {
				result.append(splittedCol3Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (EntItemReportPrint.FIXED_COL3_TOTAL)/charScale));
			}			
			result.append(reNext);
		}
		return result.toString();
	}
	
	public void AddContentListHeader(String itemName, String qty, String price){
		PrintData header = new PrintData();
		header.setDataFormat(PrintData.FORMAT_TXT);
		header.setText(this.GetThreeColHeader(itemName, qty, price));
		this.data.add(header);
		addHortionalLine(this.charSize);
	}
	private void AddItem(String itemName, String qty, String price, int scale) {
		PrintData order = new PrintData();
		order.setDataFormat(PrintData.FORMAT_TXT);
		order.setFontsize(scale);
		order.setLanguage(PrintData.LANG_CN);
		order.setMarginTop(20);
		order.setText(this.GetThreeColContent(itemName, qty, price, scale));
		this.data.add(order);
	}	

	private void getEntItemStr() {
		for (int i = 0; i < reportEntItems.size(); i++) {
			ReportEntItem reportEntItem = reportEntItems.get(i);
			this.AddItem(reportEntItem.getItemName() + "",
					reportEntItem.getItemQty() + "",
					BH.formatMoney(reportEntItem.getAmount()).toString(),1);
			boolean printLine = false;
			if(reportEntItem.getComboModifiers().size() > 0){
				printLine = true;
			}
			if(printLine)
				addHortionalLine(this.charSize);
			List<ReportPluDayComboModifier> list = reportEntItem.getComboModifiers();
			for(ReportPluDayComboModifier reportPluDayComboModifier : list){
				this.AddItem(reportPluDayComboModifier.getModifierName() + "",
						reportPluDayComboModifier.getModifierCount() + "",
						BH.formatMoney(reportPluDayComboModifier.getModifierPrice()).toString(),1);
			}
			if(printLine)
				addHortionalLine(this.charSize);
		}
	}
	
	public void setData(ArrayList<PrintData> data) {
		this.data = data;
	}
}
