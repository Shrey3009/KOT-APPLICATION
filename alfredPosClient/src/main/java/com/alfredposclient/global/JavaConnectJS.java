package com.alfredposclient.global;

public interface JavaConnectJS {
	
	public static final String CLICK_BACK = "SystemBack";
	public static final int ACTION_CLICK_BACK = 10000;
	
	public static final String CLICK_TABLE = "SelectTable";
	public static final int ACTION_CLICK_TABLE = 10001;
	public static final int ACTION_CLICK_TABLE_TRANSFER = 10002;
	public static final int ACTION_CLICK_TABLE_APPORDER = 10032;
	
	public static final String CLICK_REFRESH = "clickRefresh";
	public static final int ACTION_CLICK_REFRESH = 10003;
	
	public static final String CLICK_TODAY = "clickToday";
	public static final int ACTION_CLICK_TODAY = 10004;
	
	public static final String CLICK_ALL = "clickAll";
	public static final int ACTION_CLICL_ALL = 10005;
	
	
	public static final String CLICK_PRINT = "ClickPrint";
	public static final int ACTION_CLICK_PRINT = 10006;
	
	public static final String CLICK_X = "ClickX";
	public static final int ACTION_CLICK_X = 10007;
	
	public static final String CLICK_Z = "ClickZ";
	public static final int ACTION_CLICK_Z = 10008;
	
	public static final String LOAD_TABLES = "LoadTables";
	public static final int ACTION_LOAD_TABLES = 10009;
	
	public static final String LOAD_DEVICES = "LoadDevices";
	public static final int ACTION_LOAD_DEVICES = 10010;
	
	public static final String LOAD_ORDERS = "LoadOrders";
	public static final int ACTION_LOAD_ORDERS = 10011;
	
	public static final String LOAD_ORDER_DETAILS = "LoadOrderDetails";
	public static final int ACTION_LOAD_ORDER_DETAILS = 10012;
	
	public static final String EDIT_ORDER_DETAILS = "EditOrderDetails";
	public static final int ACTION_EDIT_ORDER_DETAILS = 10013;
	
	public static final String LOAD_XZ_REPORT = "LoadXZReport";
	public static final int ACTION_LOAD_XZ_REPORT = 10014;
	
	public static final String LOAD_BOH_SETTLEMENT_LIST = "LoadBohSettlementList";
	public static final int ACTION_LOAD_BOH_SETTLEMENT_LIST = 10015;
	
	public static final String CLICK_BOH_SAVE = "SaveBOHPayment";
	public static final int ACTION_CLICK_BOH_SAVE = 10016;
	
	public static final String LOAD_BILL_LIST = "LoadBillList";
	public static final int ACTION_LOAD_BILL_LIST = 10017;
	
	public static final String LOAD_BILL_DETAILS = "LoadBillDetails";
	public static final int ACTION_LOAD_BILL_DETAILS = 10018;
	
	public static final String CLICK_PRINT_BILL = "PrintBill";
	public static final int ACTION_CLICK_PRINT_BILL = 10019;
	
	public static final String LOAD_SETTLEMENT_LIST = "LoadSettlementList";
	public static final int ACTION_LOAD_SETTLEMENT_LIST = 10020;
	
	public static final String CLICK_EDIT_SETTLEMENT = "EditSettlement";
	public static final int ACTION_CLICK_EDIT_SETTLEMENT = 10021;
	
	public static final String LOAD_DASHBOARD = "LoadDashboard";
	public static final int ACTION_LOAD_DASHBOARD = 10022;
	
	public static final String LOAD_LOCAL_BOH_SETTLEMENT_LIST = "LoadLocalBohSettlementList";
	public static final int ACTION_LOAD_LOCAL_BOH_SETTLEMENT_LIST = 10023;
	
	public static final String CLICK_LOCAL_BOH_SAVE = "SaveLocalBOHPayment";
	public static final int ACTION_CLICK_LOCAL_BOH_SAVE = 10024;
	
	public static final String CLICK_DISCOVER_PRINTERS = "ClickDiscoverPrinters";
	public static final int ACTION_CLICK_DISCOVER_PRINTERS = 10029;

	//new for XZReport
	public static final String LOAD_ALL_XZ_REPORT = "LoadSaleDetailReport";
	public static final int ACTION_LOAD_ALL_XZ_REPORT = 10030;
	public static final String DOWNLOAD_Z_REPORT= "DownloadZReport";
	public static final int ACTION_DOWNLOAD_Z_REPORT = 10031;
	
	public static final String CLICK_CASH_SAVE = "ClickCashSave";
	public static final int ACTION_CLICK_CASH_SAVE = 2000;
	
	public static final int ACTION_NEW_KDS_ADDED = 1041;
	public static final int ACTION_NEW_WAITER_ADDED = 1042;

	public static final String LOAD_CASH_DEFAULT= "Loadcashdefault";
	public static final int ACTION_LOAD_CASH_DEFAULT= 1043;
	//for device setting
	public static final String LOAD_KDS_DEVICES = "LoadKdsDevices";
	public static final int ACTION_LOAD_KDS_DEVICES = 10050;
	
	public static final String LOAD_PRINTERS_DEVICES = "LoadPrinters";
	public static final int ACTION_LOAD_PRINTERS_DEVICES = 10051;
	
	public static final String LOAD_WAITER_DEVICES = "LoadWaiterDevices";
	public static final int ACTION_LOAD_WAITER_DEVICES = 10052;	
	
	public static final String ASSIGN_PRINTER_DEVICE = "AssignPrinterDevice";
	public static final int ACTION_ASSIGN_PRINTER_DEVICE = 10053;				
	public static final String UNASSIGN_PRINTER_DEVICE = "UnassignPrinterDevice";
	public static final int ACTION_UNASSIGN_PRINTER_DEVICE = 10054;	
	
	public static final String ADD_PRINTER_DEVICE = "Add_printer_device";
	public static final int ACTION_ADD_PRINTER_DEVICE = 10055;
	
	/*VOID and ENT PLU*/
	public static final String LOAD_VOID_PLU="LoadVoidPlu";
	public static final int ACTION_LOAD_VOID_PLU = 10056;
	public static final String LOAD_ENT_PLU="LoadVoidPlu";
	public static final int ACTION_LOAD_ENT_PLU = 10057;
	
	//monthly report
	public static final String LOAD_MONTHLY_SALES_REPORT="LoadMonthlySaleReport";
	public static final int ACTION_LOAD_MONTHLY_SALES_REPORT = 10058;
	
	public static final String LOAD_MONTHLY_PLU_REPORT="LoadMonthlyPLUReport";
	public static final int ACTION_LOAD_MONTHLY_PLU_REPORT = 10059;
	public static final int ACTION_CLICK_TABLE_ITEM = 10060;



	
	
	public void send(String action, String param);
}
