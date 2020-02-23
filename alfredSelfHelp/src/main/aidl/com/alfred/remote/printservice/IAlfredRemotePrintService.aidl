package com.alfred.remote.printservice;

import com.alfred.remote.printservice.IAlfredRemotePrintServiceCallback;

interface IAlfredRemotePrintService {

	String getMessage();
	void setMessage(String name); 
	
	void configure(int country,int lang, int dollarsign);
	
	void clearPrint();
	void deleteOldPrinterMsg(String businessDate);

	void printKOT(String printer,String summary, String detail, String modifiers,
						boolean oneprint, boolean doublePrint, int kotFontSize, boolean isFire,int trainType);

	void printKioskKOT(String printer,String summary, String detail, String modifiers,
						boolean oneprint, boolean doublePrint, String orderNo, int kotFontSize,int trainType);

	void printBillSummary(String printer,String summary, String detail, String modifiers, int kotFontSize);


	void printBill(String printer, String title,
    							String order, String orderDetail,
    							String modifiers,String tax, String payment,
    							boolean doubleprint, boolean doubleReceipts,
    							String rounding, String currencySymbol, boolean openDrawer, boolean isDouble,String info,String orderNoStr,String promotiomData,String formatType,boolean isInstructions);

    	void printKioskBill(String printer, String title,
    							String order, String orderDetail,
    							String modifiers,String tax, String payment,
    							boolean doubleprint, boolean doubleReceipts,
    							String rounding, String orderNo, String currencySymbol, boolean openDrawer, boolean isDouble,String promotiomData,String formatType);


	void kickCashDrawer(String printer);

	void listPrinters(String type);

	void closeDiscovery();

	void registerCallBack(IAlfredRemotePrintServiceCallback cb);

	void printDaySalesReport(String xzType,String printer,String title, String report, String tax, String customPayment, String useropen, String sessionSales);

	void printDetailAnalysisReport(String xzType, String printer,
										String title, String daySaleSummary, String plu, String pluMod, String pluCombo, String category, String items);
	void printSummaryAnalysisReport(String xzType, String printer,
										String title, String plu, String pluMod, String category, String items,boolean isPluVoid);
	void printHourlyAnalysisReport(String xzType, String printer, String title, String hourly);
	void printVoidItemAnalysisReport(String xzType, String printer, String title, String voidItems);
	void printEntItemAnalysisReport(String xzType, String printer, String title, String voidItems);
	void printModifierDetailAnalysisReport(String xzType, String printer, String title, String pluMod,String category);
	void printComboDetailAnalysisReport(String xzType, String printer, String title, String plu, String pluMod);
	void printMonthlySaleReport(String printer, String title, int year, int month,String saleData);
	void printMonthlyPLUReport(String printer, String title,  int year, int month, String plu);
	void printStoredCardConsume(String printer, String title, String date, String cardNo, String action, String actionAmount, String balance);
    void printTableQRCode(String printer, String tableId, String title, String qrCodeText);
   void printTscBill(String printer,String title,String order,String orderdetail,String modifiers,String currencySymbol,String direction);
   void printCashInOut(String printer ,String cashinout,String title);
	void printAppOrderBill(String printer, String title,
							String order, String orderDetail,
							String modifiers,String tax, String payment,
							boolean doubleprint, boolean doubleReceipts,
							String rounding, String orderNo, String currencySymbol, boolean openDrawer, boolean isDouble,String info,String orderNoStr,String formatType);

    void printPromotionAnalysisReport(String xzType, String printer, String title, String reportDayPromotion);
    void printIpay88Qrcode(String printer, String id, String printerTitle,  String paymentMethod, String amount, inout byte[] qrCodeBitmap);
    void printTransferOrder(String printer, String fromRvcName, String toRvcName, String fromTable, String toTable, String fromOrder, String toOrder, String orderDetail, String modifier);

}