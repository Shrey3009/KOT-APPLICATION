package com.alfred.printer;

import com.alfred.comparatorUtil.ComparatorPluDayComboModifier;
import com.alfred.comparatorUtil.ComparatorPluDayItem;
import com.alfred.remote.printservice.App;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.alfredbase.ParamConst;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.MonthlyPLUReport;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ObjectFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DetailAnalysisReportPrint extends ReportBasePrint{

	public final static int FIXED_COL4_SPACE = 2;
	public final static int FIXED_COL4_PRICE = 10; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL4_QTY = 6; //in case of 48 dots width, QTY col = 10dots
	public final static int FIXED_COL4_TOTAL = 10; //in case of 48 dots width, QTY col = 10dots

	public static int COL4_ITEMNAME; // Width = CharSize/scale - FIXED_COL2_QTY/scale -
	// FIXED_COL2_PRICE/scale- FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE *3

	private ArrayList<ReportPluDayItem> reportPluDayItems;
	private ArrayList<ReportPluDayModifier>  pluModifiers;
	private ArrayList<ItemCategory> itemCategorys;
	private ArrayList<ItemMainCategory> itemMainCategorys;
	private ArrayList<ReportPluDayComboModifier> comb;

	private String OP;
	private String reportNo;
	private String date;

	public DetailAnalysisReportPrint(String uuid,Long bizDate) {
		super("detail", uuid,bizDate);
	}

	public ArrayList<PrintData> getData() {
		return data;
	}

	public void print(ArrayList<ReportPluDayItem> plu, ArrayList<ReportPluDayModifier> modifier,
					  ArrayList<ReportPluDayComboModifier> comb,
					  ArrayList<ItemMainCategory> category, ArrayList<ItemCategory>items ) {
		this.reportPluDayItems = plu;
		this.itemMainCategorys = category;
		this.itemCategorys = items;
		this.pluModifiers = modifier;
		this.comb = comb;
		GetDetailAnalysisText();
	}

	public void print(ArrayList<MonthlyPLUReport> pluData) {
		for (int j = 0; j < pluData.size(); j++) {
			MonthlyPLUReport reportPluItem = pluData.get(j);

			if (reportPluItem.getIsModifier()==0) {
				if (j>0)
					this.addHortionalLine(this.charSize);

				this.AddItem(reportPluItem.getItemName(),
						String.valueOf(BH.getBD(reportPluItem.getItemPrice())),
						String.valueOf(reportPluItem.getSumItemNum()),
						String.valueOf(BH.getBD(reportPluItem.getSumRealPrice())), 1);

				this.addHortionalLine(this.charSize);
			}else{
				this.AddItem("("+reportPluItem.getItemName()+")",
						"("+String.valueOf(BH.getBD(reportPluItem.getItemPrice()))+")",
						"("+String.valueOf(reportPluItem.getSumItemNum())+")",
						"("+String.valueOf(BH.getBD(reportPluItem.getSumRealPrice()))+")", 1);

			}
		}
	}

	public void addSalesSummary(ReportDaySales reportDaySales) {
		//Item total Sale
		PrintData cashierPrint = new PrintData();
		String cashierLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.item_sales),
				this.FIXED_COL3_TOTAL);
		String cashierStr = cashierLabel+":"+reportDaySales.getItemSales()+reNext;
		cashierPrint.setDataFormat(PrintData.FORMAT_TXT);
		cashierPrint.setTextAlign(PrintData.ALIGN_LEFT);
		cashierPrint.setText(cashierStr);
		this.data.add(cashierPrint);

		//FOC
		String totalFOC = String.valueOf(BH.add(BH.getBD(reportDaySales.getFocItem()),
				BH.getBD(reportDaySales.getFocBill()), true));

		PrintData focPrint = new PrintData();
		String focStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.total_ent), this.FIXED_COL3_TOTAL);
		String padBillNo = focStr+":"+totalFOC+reNext;
		focPrint.setDataFormat(PrintData.FORMAT_TXT);
		focPrint.setTextAlign(PrintData.ALIGN_LEFT);
		focPrint.setText(padBillNo);
		this.data.add(focPrint);

		//Void
		String totalVoid = String.valueOf(BH.add(BH.getBD(reportDaySales.getItemVoid()),
				BH.getBD(reportDaySales.getBillVoid()), true));

		PrintData ttvPrint = new PrintData();
		String ttvLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.total_void), this.FIXED_COL3_TOTAL);
		String ttvStr = ttvLabel+":"+totalVoid+reNext;
		ttvPrint.setDataFormat(PrintData.FORMAT_TXT);
		ttvPrint.setTextAlign(PrintData.ALIGN_LEFT);
		ttvPrint.setText(ttvStr);
		this.data.add(ttvPrint);

		//disc
		String totalDisc =
				String.valueOf(BH.add(BH.getBD(reportDaySales.getDiscountPer()),
						BH.getBD(reportDaySales.getDiscount()), true));
		PrintData discPrint = new PrintData();
		String discLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.total_discount), this.FIXED_COL3_TOTAL);
		String discvStr = discLabel+":"+totalDisc+reNext;
		discPrint.setDataFormat(PrintData.FORMAT_TXT);
		discPrint.setTextAlign(PrintData.ALIGN_LEFT);
		discPrint.setText(discvStr);
		this.data.add(discPrint);

		//net sale
		PrintData netPrint = new PrintData();
		String netLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.nett_sales), this.FIXED_COL3_TOTAL);
		String netStr  = netLabel+":"+reportDaySales.getTotalSales()+reNext+reNext;
		netPrint.setDataFormat(PrintData.FORMAT_TXT);
		netPrint.setTextAlign(PrintData.ALIGN_LEFT);
		netPrint.setText(netStr);
		this.data.add(netPrint);

		//Total Pax
		PrintData paxPrint = new PrintData();
		String paxLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.total_of_paxs), this.FIXED_COL3_TOTAL);
		String paxStr  = paxLabel+":"+reportDaySales.getPersonQty()+reNext;
		paxPrint.setDataFormat(PrintData.FORMAT_TXT);
		paxPrint.setTextAlign(PrintData.ALIGN_LEFT);
		paxPrint.setText(paxStr);
		this.data.add(paxPrint);

		//avg Pax
		BigDecimal vagPaxs = BH.getBD(ParamConst.DOUBLE_ZERO);
		if(reportDaySales.getPersonQty().intValue() > 0){
			vagPaxs = BH.div(
					BH.getBD(reportDaySales.getTotalSales()),
					BH.getBD(reportDaySales.getPersonQty()),
					true);
		}

		PrintData paxAvgPrint = new PrintData();
		String paxAvgLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.avg_paxs), this.FIXED_COL3_TOTAL);
		String paxAvgStr  = paxAvgLabel+":"+vagPaxs.toString()+reNext;
		paxAvgPrint.setDataFormat(PrintData.FORMAT_TXT);
		paxAvgPrint.setTextAlign(PrintData.ALIGN_LEFT);
		paxAvgPrint.setText(paxAvgStr);
		this.data.add(paxAvgPrint);

		//Total bill
		PrintData tbPrint = new PrintData();
		String tbLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.total_of_bills), this.FIXED_COL3_TOTAL);
		String tbStr  = tbLabel+":"+reportDaySales.getTotalBills()+reNext;
		tbPrint.setDataFormat(PrintData.FORMAT_TXT);
		tbPrint.setTextAlign(PrintData.ALIGN_LEFT);
		tbPrint.setText(tbStr);
		this.data.add(tbPrint);

		//avg bill
		String avgBill = BH.div(
				BH.getBD(reportDaySales.getTotalSales()),
				BH.getBD(reportDaySales.getTotalBills()),
				true).toString();

		PrintData avgbPrint = new PrintData();
		String avgbLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.avg_bills), this.FIXED_COL3_TOTAL);
		String avgbStr  = avgbLabel+":"+avgBill+reNext;
		avgbPrint.setDataFormat(PrintData.FORMAT_TXT);
		avgbPrint.setTextAlign(PrintData.ALIGN_LEFT);
		avgbPrint.setText(avgbStr);
		this.data.add(avgbPrint);


		//addHortionalLine();
		addHortionaDoublelLine(this.charSize);
	}

	/* Four columns layout (Width = 48dots)
	 * |item Name    38/scale   | QTY 10/scale  |
	 *
	 **/
	private String GetFourColHeader(String col1Title, String col2Title,
									String col3Title, String col4Title) {

		int pad = 0;
		StringBuffer ret = new StringBuffer();
		BillPrint.COL4_ITEMNAME = this.charSize - BillPrint.FIXED_COL4_PRICE - BillPrint.FIXED_COL4_QTY - BillPrint.FIXED_COL4_TOTAL;
		if (App.instance.countryCode == ParamConst.CHINA)
			pad = 2;
		String title1 = StringUtil.padRight(col1Title, BillPrint.COL4_ITEMNAME-pad);
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
		Map<Integer, ArrayList<ReportPluDayComboModifier>> combMap = getCombItemMap(this.comb);

		ComparatorPluDayItem comparatorPluDayItem = new ComparatorPluDayItem();
		Collections.sort(reportPluDayItems, comparatorPluDayItem);
//		List<ReportPluDayItem> cop = new ArrayList<ReportPluDayItem>();
		int allQty = 0;
		BigDecimal allAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
//			boolean showMainCategory = true;
//			int mainCategoryId = 0;
		boolean lastLinePrinted = false;
//		BigDecimal categoryAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
//		String name = "";
//		int id = 0;
		Map<Integer, ReportPluDayItem> map = new HashMap<Integer, ReportPluDayItem>();
		for (int j = 0; j < reportPluDayItems.size(); j++) {

			ReportPluDayItem reportPluDayItem = reportPluDayItems.get(j);
			//ObjectFactory.getInstance().getReportPluDayItem(reportPluDayItem);
			if(map.containsKey(reportPluDayItem.getItemMainCategoryId().intValue())){
				ReportPluDayItem amountReportPluDayItem = map.get(reportPluDayItem.getItemMainCategoryId().intValue());
				BigDecimal amount = BH.add(BH.getBD(amountReportPluDayItem.getItemAmount()), BH.getBD(reportPluDayItem.getItemAmount()), false);
				amountReportPluDayItem.setItemAmount(amount.toString());
			}else{
				ReportPluDayItem rr = new ReportPluDayItem();
				rr.setItemMainCategoryId(reportPluDayItem.getItemMainCategoryId());
				rr.setItemMainCategoryName(reportPluDayItem.getItemMainCategoryName());
				rr.setItemAmount(BH.formatMoney(reportPluDayItem.getItemAmount()));
				map.put(reportPluDayItem.getItemMainCategoryId().intValue(), rr);
			}
//				if(mainCategoryId == 0 || mainCategoryId == reportPluDayItem.getItemMainCategoryId().intValue()){
//					categoryAmount = BH.add(categoryAmount,
//							BH.getBD(reportPluDayItem.getItemAmount()), true);
//					name = reportPluDayItem.getItemMainCategoryName();
//					id = reportPluDayItem.getItemMainCategoryId().intValue();
//
//					if(mainCategoryId == 0)
//						mainCategoryId = id;
//
//					if( j ==  reportPluDayItems.size() - 1){
//						ReportPluDayItem rr = new ReportPluDayItem();
//						rr.setItemMainCategoryId(id);
//						rr.setItemMainCategoryName(name);
//						rr.setItemAmount(categoryAmount.toString());
//						cop.add(rr);
//					}
//				}else{
//					ReportPluDayItem rr = new ReportPluDayItem();
//					rr.setItemMainCategoryId(id);
//					rr.setItemMainCategoryName(name);
//					rr.setItemAmount(categoryAmount.toString());
//					cop.add(rr);
//					name = reportPluDayItem.getItemMainCategoryName();
//					id = reportPluDayItem.getItemMainCategoryId().intValue();
//					mainCategoryId = id;
//					categoryAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
//					categoryAmount = BH.add(categoryAmount,
//							BH.getBD(reportPluDayItem.getItemAmount()), true);
//
//				}
		}

		Iterator<Map.Entry<Integer, ReportPluDayItem>> entries = map.entrySet().iterator();
		boolean isFirst = true;
		while (entries.hasNext()){
			Map.Entry<Integer, ReportPluDayItem> entry = entries.next();
			if(!isFirst){
				this.addHortionalLine(this.charSize);
			}
			isFirst = false;
			ReportPluDayItem amontReportPluDayItem = entry.getValue();
			this.AddItem(amontReportPluDayItem.getItemMainCategoryName(), "", "", BH.formatMoney(amontReportPluDayItem.getItemAmount()).toString(), 1);
			this.addHortionalLine(this.charSize);
			for (int j = 0; j < reportPluDayItems.size(); j++) {

				ReportPluDayItem reportPluDayItem = reportPluDayItems.get(j);
				if (amontReportPluDayItem.getItemMainCategoryId().intValue() == reportPluDayItem.getItemMainCategoryId().intValue()) {
					// Print comb modifier
					int itmId = reportPluDayItem.getItemDetailId().intValue();
					ArrayList<ReportPluDayComboModifier> comItems = combMap.get(itmId);
					if (comItems != null && comItems.size() > 0) {

						int mm = 0;
						this.AddItem(" "+reportPluDayItem.getItemName(), BH.formatMoney(reportPluDayItem.getItemPrice()),
								reportPluDayItem.getItemCount().toString(),
								BH.formatThree(reportPluDayItem.getItemAmount()), 1);
						lastLinePrinted = false;
						this.addHortionalLine(this.charSize);
						for (mm = 0; mm < comItems.size(); mm++) {
							ReportPluDayComboModifier pluModifier = comItems.get(mm);
							int count = pluModifier.getModifierCount().intValue() - pluModifier.getVoidModifierCount().intValue() - pluModifier.getBillVoidCount().intValue();
							if (count > 0) {
								BigDecimal modifierAmount = BH.mul(BH.getBD(1.00), BH.getBD(pluModifier.getModifierPrice()), true);
								BigDecimal modifierPrice = BH.mul(BH.getBD(1.00), BH.getBD(pluModifier.getModifierItemPrice()), true);
								this.AddItem(" (" + pluModifier.getModifierName() + ")",
										"(" +  BH.formatThree(modifierPrice.toString()) + ")", "(" + String.valueOf(count) + ")", "(" +  BH.formatThree(modifierAmount.toString())+ ")", 1);
								lastLinePrinted = false;
							}
						}
						if (mm == comItems.size() && mm > 0) {
							if (!lastLinePrinted)
								this.addHortionalLine(this.charSize);
							lastLinePrinted = true;
						}

					} else {

						this.AddItem(" "+reportPluDayItem.getItemName(), BH.formatMoney(reportPluDayItem.getItemPrice()),
								reportPluDayItem.getItemCount().toString(), "" +  BH.formatMoney(reportPluDayItem.getItemAmount()), 1);
						lastLinePrinted = false;
					}
					//END Comb modifier print
					allQty += reportPluDayItem.getItemCount();
					allAmount = BH.add(allAmount,
							BH.getBD(reportPluDayItem.getItemAmount()), true);
				}
			}
		}
//		for(ReportPluDayItem category : cop) {
//				if(cop.indexOf(category) != 0){
//					this.addHortionalLine(this.charSize);
//				}
//				this.AddItem(category.getItemMainCategoryName(), "", "", category.getItemAmount(), 1);
//				this.addHortionalLine(this.charSize);
//				for (int j = 0; j < reportPluDayItems.size(); j++) {
//
//					ReportPluDayItem reportPluDayItem = reportPluDayItems.get(j);
////					if (mainCategoryId != reportPluDayItem.getItemMainCategoryId().intValue()) {
////						if (mainCategoryId != 0) {
////							if (!lastLinePrinted)
////								this.addHortionalLine(this.charSize);
////							lastLinePrinted = true;
////						}
////						mainCategoryId = reportPluDayItem.getItemMainCategoryId().intValue();
////						showMainCategory = true;
////					}
////					if (showMainCategory) {
////						this.AddItem(reportPluDayItem.getItemMainCategoryName(), "", "", "", 1);
////						this.addHortionalLine(this.charSize);
////						showMainCategory = false;
////						lastLinePrinted = true;
////					}
//					if (category.getItemMainCategoryId().intValue() == reportPluDayItem.getItemMainCategoryId().intValue()) {
//						//Bob: Print comb modifier
//						int itmId = reportPluDayItem.getItemDetailId().intValue();
//						ArrayList<ReportPluDayComboModifier> comItems = combMap.get(itmId);
//						if (comItems != null && comItems.size() > 0) {
//
//							int mm = 0;
//							this.AddItem(" "+reportPluDayItem.getItemName(), reportPluDayItem.getItemPrice(),
//									reportPluDayItem.getItemCount().toString(),
//									reportPluDayItem.getItemAmount(), 1);
//							lastLinePrinted = false;
//							this.addHortionalLine(this.charSize);
//							for (mm = 0; mm < comItems.size(); mm++) {
//								ReportPluDayComboModifier pluModifier = comItems.get(mm);
//								int count = pluModifier.getModifierCount().intValue() - pluModifier.getVoidModifierCount().intValue() - pluModifier.getBillVoidCount().intValue();
//								if (count > 0) {
//									BigDecimal modifierAmount = BH.mul(BH.getBD(1.00), BH.getBD(pluModifier.getModifierPrice()), true);
//									BigDecimal modifierPrice = BH.mul(BH.getBD(1.00), BH.getBD(pluModifier.getModifierItemPrice()), true);
//									this.AddItem(" (" + pluModifier.getModifierName() + ")",
//											"(" + modifierPrice.toString() + ")", "(" + String.valueOf(count) + ")", "(" + modifierAmount.toString() + ")", 1);
//									lastLinePrinted = false;
//								}
//							}
//							if (mm == comItems.size() && mm > 0) {
//								if (!lastLinePrinted)
//									this.addHortionalLine(this.charSize);
//								lastLinePrinted = true;
//							}
//
//						} else {
//
//							this.AddItem(" "+reportPluDayItem.getItemName(), reportPluDayItem.getItemPrice(),
//									reportPluDayItem.getItemCount().toString(), "" + reportPluDayItem.getItemAmount(), 1);
//							lastLinePrinted = false;
//						}
//						//END Comb modifier print
//						allQty += reportPluDayItem.getItemCount();
//						allAmount = BH.add(allAmount,
//								BH.getBD(reportPluDayItem.getItemAmount()), true);
//					}
//				}
//			}
		if (allQty != 0) {
			this.addHortionalLine(this.charSize);
			this.AddItem(PrintService.instance.getResources().getString(R.string.total), "", allQty + "",  BH.formatMoney(allAmount.toString()), 1);
		}


	}

	/*
	 *  get code from Comb*/
//	private void GetCombDetailAnalysisText() {
//		//StringBuffer sbr = new StringBuffer();
//		int allQty = 0;
//		BigDecimal allAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
//		ComparatorPluDayComboModifier comparatorPluDayComboModifier = new ComparatorPluDayComboModifier();
//		Collections.sort(comb, comparatorPluDayComboModifier);
//			boolean showMainCategory = true;
//			int itemId = 0;
//			for (int j = 0; j < comb.size(); j++) {
//				ReportPluDayComboModifier pluModifier = comb.get(j);
//				if(itemId != pluModifier.getItemId().intValue()){
//					if(itemId != 0)
//						this.addHortionalLine(this.charSize);
//					itemId = pluModifier.getItemId().intValue();
//					showMainCategory = true;
//				}
//					if (showMainCategory) {
//						this.AddItem(pluModifier.getItemName(), "", "", "", 1);
//						this.addHortionalLine(this.charSize);
//						showMainCategory = false;
//					}
//					//Bob: 把void的去除
//					int count = pluModifier.getModifierCount().intValue() - pluModifier.getVoidModifierCount().intValue() - pluModifier.getBillVoidCount().intValue();
//					if (count>0) {
//						BigDecimal modifierAmount = BH.mul(BH.getBD(count), BH.getBD(pluModifier.getModifierPrice()), true);
//						this.AddItem(pluModifier.getModifierName(), pluModifier.getModifierPrice(), String.valueOf(count), ""
//								+ modifierAmount.toString(), 1);
//						allQty += count;
//						allAmount = BH.add(allAmount,
//								modifierAmount, true);
//					}
//		}
//		if (allQty != 0) {
//			this.addHortionalLine(this.charSize);
//			this.AddItem(PrintService.instance.getResources().getString(R.string.total), "", allQty + "",  ""+ allAmount, 1);			
//		}
//	}
	private Map<Integer, ArrayList<ReportPluDayComboModifier>> getCombItemMap(ArrayList<ReportPluDayComboModifier> comb) {

		Map<Integer, ArrayList<ReportPluDayComboModifier>> result = new HashMap<Integer, ArrayList<ReportPluDayComboModifier>>();
		if (comb !=null && comb.size()>0) {
			ComparatorPluDayComboModifier comparatorPluDayComboModifier = new ComparatorPluDayComboModifier();
			Collections.sort(comb, comparatorPluDayComboModifier);
			int itemId = 0;

			for (int j = 0; j < comb.size(); j++) {
				ArrayList<ReportPluDayComboModifier> itemCombo = null;
				ReportPluDayComboModifier pluModifier = comb.get(j);
				if(itemId != pluModifier.getItemId().intValue()){
					itemId = pluModifier.getItemId().intValue();
					if (result.get(itemId) == null) {
						result.put(itemId, new ArrayList<ReportPluDayComboModifier>());
					}
				}
				itemCombo = result.get(itemId);
				int count = pluModifier.getModifierCount().intValue() - pluModifier.getVoidModifierCount().intValue() - pluModifier.getBillVoidCount().intValue();
				if (count>0)
					itemCombo.add(pluModifier);
			}
		}
		return result;
	}

	private boolean isVoidEntItem() {
		boolean ret = false;

		return ret;
	}
	public void setData(ArrayList<PrintData> data) {
		this.data = data;
	}
}
