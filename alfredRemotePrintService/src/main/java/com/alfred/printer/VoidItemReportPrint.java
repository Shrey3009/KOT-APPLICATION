package com.alfred.printer;

import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.model.ReportVoidItem;
import com.alfredbase.utils.BH;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class VoidItemReportPrint extends ReportBasePrint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1926671330908858662L;
	public final static int FIXED_COL3_SPACE = 2;
	public final static int FIXED_COL3_QTY = 10; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL3_TOTAL = 12; //in case of 48 dots width, QTY col = 12dots
	
	public static int COL3_ITEMNAME; // Width = CharSize/scale - FIXED_COL3_QTY/scale - 
	                                      // FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE * 2

	private ArrayList<ReportVoidItem> reportVoidItems;
	
	public VoidItemReportPrint(String uuid,Long bizDate) {
		super("VOID",uuid, bizDate);
	}
	
	public ArrayList<PrintData> getData() {
		return data;
	}
	
	public void print(ArrayList<ReportVoidItem> reportVoidItems) {
		this.reportVoidItems = reportVoidItems;
		getVoidItemStr();
	}


	/* Four columns layout (Width = 48dots)
	 * |item Name    38/scale   | QTY 10/scale  |
	 * 
	 **/	
	private String GetThreeColHeader(String col1Title, String col2Title, String col3Title) {
		
		StringBuffer ret = new StringBuffer();
		VoidItemReportPrint.COL3_ITEMNAME = this.charSize -  VoidItemReportPrint.FIXED_COL3_QTY - VoidItemReportPrint.FIXED_COL3_TOTAL;
		String title1 = StringUtil.padRight(col1Title, VoidItemReportPrint.COL3_ITEMNAME);
		String title2 = StringUtil.padRight(col2Title, VoidItemReportPrint.FIXED_COL3_QTY);
		String title3 = StringUtil.padLeft(col3Title, VoidItemReportPrint.FIXED_COL3_TOTAL);
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
		
		VoidItemReportPrint.COL3_ITEMNAME = (this.charSize -VoidItemReportPrint.FIXED_COL3_TOTAL - VoidItemReportPrint.FIXED_COL3_QTY)/charScale - 2*VoidItemReportPrint.FIXED_COL3_SPACE;
		
		double ln1 = col1Content.length();
		try {
			ln1 = (col1Content.getBytes("GBK").length)/(VoidItemReportPrint.COL3_ITEMNAME*1.0);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			ln1 = col1Content.length();
		}
		col1Lines = StringUtil.nearestTen(ln1);
		String col1PadContent = StringUtil.padRight(col1Content, col1Lines*VoidItemReportPrint.COL3_ITEMNAME);
		ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, VoidItemReportPrint.COL3_ITEMNAME);
		
		double ln2 = (col2Content.length())/(VoidItemReportPrint.FIXED_COL3_QTY*1.0/charScale);
		col2Lines = StringUtil.nearestTen(ln2);
		String col2PadContent = StringUtil.padLeft(col2Content, col2Lines*VoidItemReportPrint.FIXED_COL3_QTY/charScale);
		ArrayList<String> splittedCol2Content = StringUtil.splitEqually(col2PadContent, VoidItemReportPrint.FIXED_COL3_QTY/charScale);

		double ln3 = (col3Content.length())/(VoidItemReportPrint.FIXED_COL3_TOTAL*1.0/charScale);
		col3Lines = StringUtil.nearestTen(ln3);
		String col3PadContent = StringUtil.padLeft(col3Content, col3Lines*VoidItemReportPrint.FIXED_COL3_TOTAL/charScale);
		ArrayList<String> splittedCol3Content = StringUtil.splitEqually(col3PadContent, VoidItemReportPrint.FIXED_COL3_TOTAL/charScale);

		
		for (int i=0; i< Math.max(Math.max(col1Lines, col2Lines),col3Lines); i++) {
			if (i<col1Lines) {			
				result.append(splittedCol1Content.get(i));
			}else{
				result.append(StringUtil.padRight(" ", VoidItemReportPrint.COL3_ITEMNAME));
			}
			//padding
			result.append(StringUtil.padRight(" ", VoidItemReportPrint.FIXED_COL3_SPACE));
			
			if (i<col2Lines) {
				result.append(splittedCol2Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (VoidItemReportPrint.FIXED_COL3_QTY)/charScale));
			}

			//padding
			result.append(StringUtil.padRight(" ", VoidItemReportPrint.FIXED_COL3_SPACE));
			
			if (i<col3Lines) {
				result.append(splittedCol3Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (VoidItemReportPrint.FIXED_COL3_TOTAL)/charScale));
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

	private void getVoidItemStr() {
		for (int i = 0; i < reportVoidItems.size(); i++) {
			ReportVoidItem reportVoidItem = reportVoidItems.get(i);
			this.AddItem(reportVoidItem.getItemName() + "",
					reportVoidItem.getItemQty() + "",
					BH.formatMoney(reportVoidItem.getAmount()).toString(),1);
			boolean printLine = false;
			if(reportVoidItem.getComboModifiers().size() > 0){
				printLine = true;
			}
			if(printLine)
				addHortionalLine(this.charSize);
			List<ReportPluDayComboModifier> list = reportVoidItem.getComboModifiers();
			for(ReportPluDayComboModifier reportPluDayComboModifier : list){
				this.AddItem(reportPluDayComboModifier.getModifierName() + "",
						reportPluDayComboModifier.getVoidModifierCount() + "",
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
