package com.alfred.printer;

import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.alfredbase.ParamConst;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.utils.BH;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class SummaryAnalysisReportPrint extends ReportBasePrint{

	public final static int FIXED_COL3_SPACE = 2;
	public final static int FIXED_COL3_QTY = 10; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL3_TOTAL = 12; //in case of 48 dots width, QTY col = 12dots
	
	public static int COL3_ITEMNAME; // Width = CharSize/scale - FIXED_COL3_QTY/scale - 
	                                      // FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE * 2

	private ArrayList<ReportPluDayItem> reportPluDayItems;
	private ArrayList<ItemCategory> itemCategorys;
	private ArrayList<ItemMainCategory> itemMainCategorys;
	private ArrayList<ReportPluDayModifier> pluModifiers;
	private  boolean isPluVoid;
	
	
	public SummaryAnalysisReportPrint(String uuid, Long bizDate) {
		super("summary",uuid,  bizDate);
	}
	
	public ArrayList<PrintData> getData() {
		return data;
	}
	
	public void print(ArrayList<ReportPluDayItem> plu, ArrayList<ReportPluDayModifier> modifier,
			ArrayList<ItemMainCategory> category, ArrayList<ItemCategory>items ,boolean isPluVoid) {
		this.reportPluDayItems = plu;
		this.itemMainCategorys = category;
		this.itemCategorys = items;
		this.pluModifiers = modifier;
		this.isPluVoid=isPluVoid;
		getSummaryAnalysis();
	}

	/* Four columns layout (Width = 48dots)
	 * |item Name    38/scale   | QTY 10/scale  |
	 * 
	 **/	
	private String GetThreeColHeader(String col1Title, String col2Title, String col3Title) {
		
		StringBuffer ret = new StringBuffer();
		SummaryAnalysisReportPrint.COL3_ITEMNAME = this.charSize -  SummaryAnalysisReportPrint.FIXED_COL3_QTY - SummaryAnalysisReportPrint.FIXED_COL3_TOTAL;
		String title1 = StringUtil.padRight(col1Title, SummaryAnalysisReportPrint.COL3_ITEMNAME);
		String title2 = StringUtil.padRight(col2Title, SummaryAnalysisReportPrint.FIXED_COL3_QTY);
		String title3 = StringUtil.padLeft(col3Title, SummaryAnalysisReportPrint.FIXED_COL3_TOTAL);
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
		
		SummaryAnalysisReportPrint.COL3_ITEMNAME = (this.charSize -SummaryAnalysisReportPrint.FIXED_COL3_TOTAL - SummaryAnalysisReportPrint.FIXED_COL3_QTY)/charScale - 2*SummaryAnalysisReportPrint.FIXED_COL3_SPACE;
		
		double ln1 = col1Content.length();
		try {
			ln1 = (col1Content.getBytes("GBK").length)/(SummaryAnalysisReportPrint.COL3_ITEMNAME*1.0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		col1Lines = StringUtil.nearestTen(ln1);
		String col1PadContent = StringUtil.padRight(col1Content, col1Lines*SummaryAnalysisReportPrint.COL3_ITEMNAME);
		ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, SummaryAnalysisReportPrint.COL3_ITEMNAME);
		
		double ln2 = (col2Content.length())/(SummaryAnalysisReportPrint.FIXED_COL3_QTY*1.0/charScale);
		col2Lines = StringUtil.nearestTen(ln2);
		String col2PadContent = StringUtil.padLeft(col2Content, col2Lines*SummaryAnalysisReportPrint.FIXED_COL3_QTY/charScale);
		ArrayList<String> splittedCol2Content = StringUtil.splitEqually(col2PadContent, SummaryAnalysisReportPrint.FIXED_COL3_QTY/charScale);

		double ln3 = (col3Content.length())/(SummaryAnalysisReportPrint.FIXED_COL3_TOTAL*1.0/charScale);
		col3Lines = StringUtil.nearestTen(ln3);
		String col3PadContent = StringUtil.padLeft(col3Content, col3Lines*SummaryAnalysisReportPrint.FIXED_COL3_TOTAL/charScale);
		ArrayList<String> splittedCol3Content = StringUtil.splitEqually(col3PadContent, SummaryAnalysisReportPrint.FIXED_COL3_TOTAL/charScale);

		
		for (int i=0; i< Math.max(Math.max(col1Lines, col2Lines),col3Lines); i++) {
			if (i<col1Lines) {			
				result.append(splittedCol1Content.get(i));
			}else{
				result.append(StringUtil.padRight(" ", SummaryAnalysisReportPrint.COL3_ITEMNAME));
			}
			//padding
			result.append(StringUtil.padRight(" ", SummaryAnalysisReportPrint.FIXED_COL3_SPACE));
			
			if (i<col2Lines) {
				result.append(splittedCol2Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (SummaryAnalysisReportPrint.FIXED_COL3_QTY)/charScale));
			}

			//padding
			result.append(StringUtil.padRight(" ", SummaryAnalysisReportPrint.FIXED_COL3_SPACE));
			
			if (i<col3Lines) {
				result.append(splittedCol3Content.get(i));
			}else {
				result.append(StringUtil.padLeft(" ", (SummaryAnalysisReportPrint.FIXED_COL3_TOTAL)/charScale));
			}			
			result.append(reNext);
		}
		return result.toString();
	}
	
	public void AddContentListHeader(String itemName, String qty, String total){
		PrintData header = new PrintData();
		header.setDataFormat(PrintData.FORMAT_TXT);
		header.setText(this.GetThreeColHeader(itemName, qty, total));
		this.data.add(header);
		addHortionalLine(this.charSize);
	}
	private void AddItem(String itemName, String qty, String total, int scale) {
		PrintData order = new PrintData();
		order.setDataFormat(PrintData.FORMAT_TXT);
		order.setFontsize(scale);
		order.setLanguage(PrintData.LANG_CN);
		order.setMarginTop(20);
		order.setText(this.GetThreeColContent(itemName, qty, total, scale));
		this.data.add(order);
	}	
	private void getSummaryAnalysis() {
		
		if (itemMainCategorys == null || itemCategorys == null
				|| reportPluDayItems == null) {
			return ;
		}

		int allQty = 0;
		BigDecimal allAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
		for (int i = 0; i < itemMainCategorys.size(); i++) {
			ItemMainCategory itemMainCategory = itemMainCategorys.get(i);
			boolean showMainCategory = true;
			for (int k = 0; k < itemCategorys.size(); k++) {
				ItemCategory itemCategory = itemCategorys.get(k);
				String name = null;
				int qty = 0;
				BigDecimal amount = BH.getBD(ParamConst.DOUBLE_ZERO);
				for (int j = 0; j < reportPluDayItems.size(); j++) {
					ReportPluDayItem reportPluDayItem = reportPluDayItems
							.get(j);
					if (itemMainCategory.getId().equals(
							reportPluDayItem.getItemMainCategoryId())) {
						if (showMainCategory) {
							this.addHortionalLine(this.charSize);	
							this.AddItem(reportPluDayItem.getItemMainCategoryName(), "", "", 1);
							this.addHortionalLine(this.charSize);							
							showMainCategory = false;
						}
						if (itemCategory.getId().equals(
								reportPluDayItem.getItemCategoryId())) {
							if(isPluVoid) {
								name = reportPluDayItem.getItemCategoryName();
								qty += reportPluDayItem.getItemCount();
								amount = BH.add(amount,
										BH.getBD(reportPluDayItem.getItemAmount()),
										true);
							}else {

								name = reportPluDayItem.getItemCategoryName();
								qty += reportPluDayItem.getItemCount();
								amount = BH.add(amount,
										BH.getBD(reportPluDayItem.getItemAmount()),
										true);
							}
						}
					}

				}
				if (qty != 0) {
					this.AddItem(name, qty+"", "" + BH.formatMoney(amount.toString()).toString(), 1);
					allQty += qty;
					allAmount = BH.add(allAmount, amount, true);
				}
			}
		}

/*		
		if (this.pluModifiers.size()>0) {
			this.AddItem("Modifiers Analysis", "", "", 1);
		}
		//modifier PLU report
		for (int i = 0; i < this.pluModifiers.size(); i++) {
			ReportPluDayModifier mod = this.pluModifiers.get(i);
			String name = mod.getModifierName();
			int qty = mod.getModifierCount();
			allQty += qty;
			String amount = mod.getModifierPrice();

			allAmount = BH.add(allAmount, BH.getBD(amount), true);
			this.AddItem(name, qty+"", amount, 1);
		}
*/
		if (allQty != 0) {
			this.addHortionalLine(this.charSize);
			this.AddItem(PrintService.instance.getResources().getString(R.string.total), allQty+"", "" + BH.formatMoney(allAmount.toString()).toString(), 1);
		}		
	}
	
	public void setData(ArrayList<PrintData> data) {
		this.data = data;
	}
}
