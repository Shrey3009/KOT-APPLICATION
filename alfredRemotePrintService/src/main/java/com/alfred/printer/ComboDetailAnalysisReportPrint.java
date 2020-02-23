package com.alfred.printer;

import com.alfred.comparatorUtil.ComparatorPluDayComboModifier;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.alfredbase.ParamConst;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.utils.BH;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class ComboDetailAnalysisReportPrint extends ReportBasePrint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4750936236905591163L;
	public final static int FIXED_COL4_SPACE = 2;
	public final static int FIXED_COL4_PRICE = 10; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL4_QTY = 6; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL4_TOTAL = 10; //in case of 48 dots width, QTY col = 10dots
	
	public static int COL4_ITEMNAME; // Width = CharSize/scale - FIXED_COL2_QTY/scale - 
	                                      // FIXED_COL2_PRICE/scale- FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE *3

	private ArrayList<ReportPluDayComboModifier>  pluModifiers;
	private ArrayList<ReportPluDayItem> items;
	
	private String OP;
	private String reportNo;
	private String date;
	
	public ComboDetailAnalysisReportPrint(String uuid, Long bizDate) {
		super("detail",uuid, bizDate);
	}
	
	public void print(ArrayList<ReportPluDayItem> items, ArrayList<ReportPluDayComboModifier> modifier) {
		this.items = items;
		this.pluModifiers = modifier;
		GetDetailAnalysisText();
	}


	/* Four columns layout (Width = 48dots)
	 * |item Name    38/scale   | QTY 10/scale  |
	 * 
	 **/	
	private String GetFourColHeader(String col1Title, String col2Title, 
			String col3Title, String col4Title) {
		
		StringBuffer ret = new StringBuffer();
		BillPrint.COL4_ITEMNAME = this.charSize - BillPrint.FIXED_COL4_PRICE - BillPrint.FIXED_COL4_QTY - BillPrint.FIXED_COL4_PRICE;
		String title1 = StringUtil.padRight(col1Title, BillPrint.COL4_ITEMNAME);
		String title2 = StringUtil.padRight(col2Title, BillPrint.FIXED_COL4_PRICE);
		String title3 = StringUtil.padRight(col3Title, BillPrint.FIXED_COL4_QTY);
		String title4 = StringUtil.padLeft(col4Title, BillPrint.FIXED_COL4_TOTAL);
		ret.append(title1).append(title2).append(title3).append(title4).append(reNext);

		return ret.toString();
	}
	
	/* Four columns layout (Width = 48dots)
	 * |item Name   Dynamical | Price |  |2| QTY 10/scale  | Total|
	 * 
	 **/
	private String GetFourColContent(String col1Content, String col2Content, 
										String col3Content, String col4Content,int charScale) {
		
		StringBuffer result = new StringBuffer();

		int col1Lines = 1;
		int col2Lines = 1;
		int col3Lines = 1;
		int col4Lines = 1;
		
		BillPrint.COL4_ITEMNAME = (this.charSize -BillPrint.FIXED_COL4_TOTAL - BillPrint.FIXED_COL4_QTY - BillPrint.FIXED_COL4_PRICE)/charScale - 3*BillPrint.FIXED_COL4_SPACE;
		
		double ln1 = col1Content.length();
		try {
			ln1 = (col1Content.getBytes("GBK").length)/(BillPrint.COL4_ITEMNAME*1.0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		col1Lines = StringUtil.nearestTen(ln1);
		String col1PadContent = StringUtil.padRight(col1Content, col1Lines*BillPrint.COL4_ITEMNAME);
		ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, BillPrint.COL4_ITEMNAME);
		
		double ln2 = (col2Content.length())/(BillPrint.FIXED_COL4_PRICE*1.0/charScale);
		col2Lines = StringUtil.nearestTen(ln2);
		String col2PadContent = StringUtil.padLeft(col2Content, col2Lines*BillPrint.FIXED_COL4_PRICE/charScale);
		ArrayList<String> splittedCol2Content = StringUtil.splitEqually(col2PadContent, BillPrint.FIXED_COL4_PRICE/charScale);

		double ln3 = (col3Content.length())/(BillPrint.FIXED_COL4_QTY*1.0/charScale);
		col3Lines = StringUtil.nearestTen(ln3);
		String col3PadContent = StringUtil.padLeft(col3Content, col3Lines*BillPrint.FIXED_COL4_QTY/charScale);
		ArrayList<String> splittedCol3Content = StringUtil.splitEqually(col3PadContent, BillPrint.FIXED_COL4_QTY/charScale);

		double ln4 = (col4Content.length())/(BillPrint.FIXED_COL4_TOTAL*1.0/charScale);
		col4Lines = StringUtil.nearestTen(ln4);
		String col4PadContent = StringUtil.padLeft(col4Content, col4Lines*BillPrint.FIXED_COL4_PRICE/charScale);
		ArrayList<String> splittedCol4Content = StringUtil.splitEqually(col4PadContent, BillPrint.FIXED_COL4_PRICE/charScale);

		
		for (int i=0; i< Math.max(Math.max(col1Lines, col2Lines), Math.max(col3Lines, col4Lines)); i++) {
			if (i<col1Lines) {			
				result.append(splittedCol1Content.get(i));
			}else{
				result.append(StringUtil.padRight(" ", BillPrint.COL4_ITEMNAME));
			}
			//padding
			result.append(StringUtil.padRight(" ", BillPrint.FIXED_COL4_SPACE));
			
			if (i<col2Lines) {
				result.append(splittedCol2Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (BillPrint.FIXED_COL4_PRICE)/charScale));
			}
			
			//padding
			result.append(StringUtil.padRight(" ", BillPrint.FIXED_COL4_SPACE));
			
			if (i<col3Lines) {
				result.append(splittedCol3Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (BillPrint.FIXED_COL4_QTY)/charScale));
			}

			//padding
			result.append(StringUtil.padRight(" ", BillPrint.FIXED_COL4_SPACE));
			
			if (i<col4Lines) {
				result.append(splittedCol4Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (BillPrint.FIXED_COL4_PRICE)/charScale));
			}			
			result.append(reNext);
		}
		return result.toString();
	}
	
	public void AddContentListHeader(String itemName, String price, String qty, String total){
		PrintData header = new PrintData();
		header.setDataFormat(PrintData.FORMAT_TXT);
		header.setText(this.GetFourColHeader(itemName, price, qty, total));
		this.data.add(header);
		addHortionalLine(this.charSize);
	}
	
	private void AddItem(String itemName, String price, String qty, String total, int scale) {
		PrintData order = new PrintData();
		order.setDataFormat(PrintData.FORMAT_TXT);
		order.setFontsize(scale);
		order.setLanguage(PrintData.LANG_CN);
		order.setMarginTop(20);
		order.setText(this.GetFourColContent(itemName, price,qty, total, scale));
		this.data.add(order);
	}
	
	/*
	 * Detail analysis Report*/
	private void GetDetailAnalysisText() {
		//StringBuffer sbr = new StringBuffer();
		int allQty = 0;
		BigDecimal allAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
//		for (int i = 0; i < items.size(); i++) {
//			ReportPluDayItem item = items.get(i);
		ComparatorPluDayComboModifier comparatorPluDayComboModifier = new ComparatorPluDayComboModifier();
		Collections.sort(pluModifiers, comparatorPluDayComboModifier);
			boolean showMainCategory = true;
			int itemId = 0;
			for (int j = 0; j < pluModifiers.size(); j++) {
				ReportPluDayComboModifier pluModifier = pluModifiers.get(j);
//				if (!IntegerUtils.isEmptyOrZero(pluModifier.getItemId()) && pluModifier.getItemId().intValue() == item.getItemDetailId().intValue()) {
				if(itemId != pluModifier.getItemId().intValue()){
					if(itemId != 0)
						this.addHortionalLine(this.charSize);
					itemId = pluModifier.getItemId().intValue();
					showMainCategory = true;
				}
					if (showMainCategory) {
						this.AddItem(pluModifier.getItemName(), "", "", "", 1);
						this.addHortionalLine(this.charSize);
						showMainCategory = false;
					}
					
					// 把void的去除
					int count = pluModifier.getModifierCount().intValue() - pluModifier.getVoidModifierCount().intValue() - pluModifier.getBillVoidCount().intValue();
					if (count>0) {
						BigDecimal modifierAmount = BH.getBD(pluModifier.getModifierPrice());
						this.AddItem(pluModifier.getModifierName(), pluModifier.getModifierItemPrice(), String.valueOf(count), pluModifier.getModifierPrice(), 1);
						allQty += count;
						allAmount = BH.add(allAmount,
								modifierAmount, true);
					}
//				}
//			}
//			if(!showMainCategory)
//			this.addHortionalLine(this.charSize);
		}
		if (allQty != 0) {
			this.addHortionalLine(this.charSize);
			this.AddItem(PrintService.instance.getResources().getString(R.string.total), "", allQty + "",  ""+ allAmount, 1);			
		}
	}
	
	public void setData(ArrayList<PrintData> data) {
		this.data = data;
	}
}
