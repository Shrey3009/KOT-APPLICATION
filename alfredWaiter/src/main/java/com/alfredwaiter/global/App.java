package com.alfredwaiter.global;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alfred.remote.printservice.IAlfredRemotePrintService;
import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.UnCEHandler;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.LocalDevice;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RestaurantConfig;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.LocalRestaurantConfig;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrintReceiptInfo;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.CardsSettlementSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.NetsSettlementSQL;
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.VibrationUtil;
import com.alfredwaiter.R;
import com.alfredwaiter.activity.ConnectPOS;
import com.alfredwaiter.activity.EmployeeID;
import com.alfredwaiter.activity.Login;
import com.alfredwaiter.activity.SelectRevenue;
import com.alfredwaiter.activity.Welcome;
import com.alfredwaiter.http.server.WaiterHttpServer;
import com.alfredwaiter.view.WaiterReloginDialog;
import com.google.gson.Gson;
import com.moonearly.model.UdpMsg;
import com.moonearly.utils.service.TcpUdpFactory;
import com.moonearly.utils.service.UdpServiceCallBack;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class App extends BaseApplication {

    public static final int VIEW_EVENT_SET_QTY = 10;
    private static final String DATABASE_NAME = "com.alfredwaiter";
    public static App instance;
    private RevenueCenter revenueCenter;


    private int restaurantId;
    private User user;
    private WaiterDevice waiterdev;
    private MainPosInfo mainPosInfo;
    private WaiterHttpServer httpServer = null;
    private String pairingIp;
    private int kotNotificationQty = 0;
    private SessionStatus sessionStatus;
    private String fromTableName;
    private String toTableName;
    public String VERSION = "1.0.9";
    private Map<Integer, PrinterDevice> printerDevices = new ConcurrentHashMap<Integer, PrinterDevice>();
    private Observable<Object> observable;
    private Observable<String> observable1;
    private Observable<String> observable2;
    public boolean kot_print;
    private PrinterDevice localPrinterDevice;
    private List<OrderDetail> newOrderDetails;

    private Map<Integer, List<PrinterDevice>> map = new HashMap<Integer, List<PrinterDevice>>();

    private String currencySymbol = "$";
    public static boolean isleftMoved;
    private String formatType;

    private LocalRestaurantConfig localRestaurantConfig;
    private IntentFilter intentFilter;

    // Remote Print Service
    private IAlfredRemotePrintService mRemoteService = null;
    /*
     * Print Service call back
     */
//    private RemotePrintServiceCallback mCallback = new RemotePrintServiceCallback();

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packName = intent.getDataString().substring(8);
            //packName为所安装的程序的包名
            String name = context.getResources().getString(R.string.printer_app_name);
            if (packName.equals("com.alfred.remote.printservice")) {
                connectRemotePrintService();
                unregisterReceiver(receiver);
            }
        }
    };

    private ServiceConnection mRemoteConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteService = null;
            autoConnectRemotePrintService();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mRemoteService = IAlfredRemotePrintService.Stub
                    .asInterface(service);
            try {
//                mRemoteService.registerCallBack(mCallback);
                String txt = mRemoteService.getMessage();
                mRemoteService.setMessage("Alfred Main POS");
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    };

    public void autoConnectRemotePrintService() {
        int printVersionCode = 0;
        int posVersionCode = 0;
        try {
            posVersionCode = instance.getPackageManager().getPackageInfo(instance.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<PackageInfo> packageinfo = this.getPackageManager()
                .getInstalledPackages(0);

        int count = packageinfo.size();
        for (int i = 0; i < count; i++) {

            PackageInfo pinfo = packageinfo.get(i);
            ApplicationInfo appInfo = pinfo.applicationInfo;
            if (!((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)) {
                String name = pinfo.applicationInfo.packageName;

                if (name.equals("com.alfred.remote.printservice")) {
                    printVersionCode = pinfo.versionCode;
                }
            }
        }
        if (printVersionCode < posVersionCode) {
//            printerDialog();
        } else {
            connectRemotePrintService();
        }

    }

    /* Print service */
    public void connectRemotePrintService() {
        if (mRemoteService == null) {
            Intent bindIntent = new Intent(
                    "alfred.intent.action.bindPrintService");
            bindIntent.putExtra("PRINTERKEY", "fxxxkprinting");
            bindIntent.putExtra("isDouble", BH.IsDouble());
            bindIntent.setClassName("com.alfred.remote.printservice",
                    "com.alfred.remote.printservice.PrintService");

            bindService(bindIntent, mRemoteConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SQLExe.init(this, DATABASE_NAME, DATABASE_VERSION);
        update15to16();
        //HTTPServer.start();
        VERSION = getAppVersionName();
        httpServer = new WaiterHttpServer();
        BugseeHelper.init(this, "5cc128ed-939d-428a-9060-2813a324b2f2");
        startHttpServer();
        UnCEHandler catchExcep = new UnCEHandler(this, Welcome.class);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
        CrashReport.initCrashReport(getApplicationContext(), "900042909", isOpenLog);

        observable = RxBus.getInstance().register("showRelogin");
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object object) {
                if (getUser() != null && !TextUtils.isEmpty(Store.getString(instance, Store.EMPLOYEE_ID))) {
                    boolean flag = Store.getBoolean(getTopActivity(), Store.WAITER_SET_LOCK, true);
                    if (flag) {
                        WaiterReloginDialog reloginDialog = new WaiterReloginDialog(getTopActivity(), true);
                        reloginDialog.show();
                    }
                } else {
                    return;
                }
            }
        });

        observable1 = RxBus.getInstance().register(RxBus.RX_WIFI_STORE);
        observable1.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String object) {
                BaseActivity baseActivity = getTopActivity();
                if (baseActivity instanceof Welcome
                        || baseActivity instanceof ConnectPOS
                        || baseActivity instanceof Login
                        || baseActivity instanceof EmployeeID
                        || baseActivity instanceof SelectRevenue) {
                    return;
                }
                if (!object.equals(CommonUtil.getLocalIpAddress())) {
                    DialogFactory.showOneButtonCompelDialog(getTopActivity(), getString(R.string.warning), getString(R.string.ip_changed_relogin), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SyncCentre.getInstance().cancelAllRequests();
                            getTopActivity().dismissLoadingDialog();
                            Store.remove(getTopActivity(), Store.WAITER_USER);
                            getTopActivity().startActivity(new Intent(getTopActivity(), Welcome.class));

                        }
                    });
                }
            }
        });
        observable2 = RxBus.getInstance().register(RxBus.RX_GET_STOCK);
        observable2.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String object) {
                SyncCentre.getInstance().getStock(instance);
            }
        });
        wifiPolicyNever();
        TcpUdpFactory.startUdpServer(App.UDP_INDEX_WAITER, "Waiter", new UdpServiceCallBack() {
            @Override
            public void callBack(final UdpMsg udpMsg) {
                try {
                    if (udpMsg != null) {
                        if (udpMsg.getType() == 1) {
                            RxBus.getInstance().post(RxBus.RECEIVE_IP_ACTION, udpMsg);
                        } else {
                            JSONObject jsonObject = new JSONObject(udpMsg.getName());
                            RxBus.getInstance().post(jsonObject.getString("RX"), udpMsg.getName());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        connectRemotePrintService();
    }


    public void startHttpServer() {
        try {
            if (!httpServer.isAlive())
                this.httpServer.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopHttpServer() {
        if (this.httpServer != null)
            this.httpServer.stop();
    }


    public int getRestaurantId() {
        restaurantId = Store.getInt(this,
                Store.RESTAURANT_ID);

        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public RevenueCenter getRevenueCenter() {
        if (revenueCenter == null)
            revenueCenter = Store.getObject(this,
                    Store.CURRENT_REVENUE_CENTER, RevenueCenter.class);
        return revenueCenter;
    }

    public void setRevenueCenter(RevenueCenter revenueCenter) {
        this.revenueCenter = revenueCenter;
    }


    public User getUser() {
        if (user == null) {
            user = Store.getObject(App.instance, Store.WAITER_USER, User.class);
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        Store.saveObject(App.instance, Store.WAITER_USER, user);
    }

    @Override
    protected void onIPChanged() {
        super.onIPChanged();

    }

    public PrinterDevice getLocalPrinterDevice() {
        return this.printerDevices.get(123);
    }

    public Map<Integer, PrinterDevice> getPrinterDevices() {
        return this.printerDevices;
    }

    public void setPrinterDevices(Map<Integer, PrinterDevice> printerDevices) {
        this.printerDevices = printerDevices;
    }

    public void setPrinterDevice(Integer deviceid, PrinterDevice device) {
        this.printerDevices.put(deviceid, device);
    }

    public Map<Integer, List<PrinterDevice>> getMap() {
        return map;
    }

    public void setMap(Map<Integer, List<PrinterDevice>> map) {
        this.map = map;
    }

    public void removePrinterDevice(Integer deviceid) {
        for (Iterator<Map.Entry<Integer, PrinterDevice>> it = printerDevices
                .entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, PrinterDevice> entry = it.next();
            if (entry.getKey().intValue() == deviceid.intValue()) {
                it.remove();
            }
        }
    }

    public void loadPrinters() {
        List<LocalDevice> devices = CoreData.getInstance().getLocalDevices();
        for (LocalDevice item : devices) {
            int type = item.getDeviceType();
            // load physical printer
            if (type == ParamConst.DEVICE_TYPE_PRINTER) {
                int devid = item.getDeviceId();
                String ip = item.getIp();
                String mac = item.getMacAddress();
                String name = item.getDeviceName();
                String model = item.getDeviceMode();
                String printerName = item.getPrinterName();
                int isLable = item.getIsLablePrinter();
                PrinterDevice pdev = new PrinterDevice();
                pdev.setDevice_id(devid);
                pdev.setIP(ip);
                pdev.setMac(mac);
                pdev.setName(name);
                pdev.setModel(model);
                pdev.setPrinterName(printerName);
                pdev.setIsLablePrinter(isLable);
                pdev.setIsCahierPrinter(CoreData.getInstance()
                        .isCashierPrinter(devid));
                printerDevices.put(devid, pdev);
            }
        }
    }

    @Override
    protected void onNetworkConnectionUpdate() {
        super.onNetworkConnectionUpdate();

    }

    public WaiterDevice getWaiterdev() {
        if (waiterdev == null) {
            waiterdev = Store.getObject(this, Store.WAITER_DEVICE, WaiterDevice.class);
        }
        return waiterdev;
    }

    public void setWaiterdev(WaiterDevice waiterdev) {
        this.waiterdev = waiterdev;
        Store.saveObject(this, Store.WAITER_DEVICE, waiterdev);
    }

    public MainPosInfo getMainPosInfo() {
        if (mainPosInfo == null) {
            mainPosInfo = Store.getObject(this.getApplicationContext(), Store.MAINPOSINFO, MainPosInfo.class);
        }
        return mainPosInfo;
    }

    public void setMainPosInfo(MainPosInfo mainPosInfo) {
        this.mainPosInfo = mainPosInfo;
        Store.saveObject(this.getApplicationContext(), Store.MAINPOSINFO, mainPosInfo);
    }

    public String getPairingIp() {
        return pairingIp;
    }

    public void setPairingIp(String pairingIp) {
        this.pairingIp = pairingIp;
    }


    public int getKotNotificationQty() {
        return kotNotificationQty;
    }

    public void setKotNotificationQty(int kotNotificationQty) {
        this.kotNotificationQty = kotNotificationQty;
    }

    public SessionStatus getSessionStatus() {
        if (sessionStatus == null) {
            sessionStatus = Store.getObject(instance, Store.SESSION_STATUS, SessionStatus.class);
        }
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public String getFromTableName() {
        return fromTableName;
    }

    public void setFromTableName(String fromTableName) {
        this.fromTableName = fromTableName;
    }

    public String getToTableName() {
        return toTableName;
    }

    public void setToTableName(String toTableName) {
        this.toTableName = toTableName;
    }

    public void playVibration() {
        VibrationUtil.init(this);
        VibrationUtil.playVibratorOnce();
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

	public void setCurrencySymbol(String currencySymbol, boolean isDouble) {
		this.currencySymbol = currencySymbol;
		//BH.initFormart(isDouble);
	}

    public String getFormatType() {
        return formatType;
    }

    public void setNewOrderDetail(List<OrderDetail> orderDetails) {
        this.newOrderDetails = orderDetails;
    }

    public List<OrderDetail> getNewOrderDetail() {
        return newOrderDetails;
    }

    public void printBill(PrinterDevice printer, PrinterTitle title,
                          Order order, ArrayList<PrintOrderItem> orderItems,
                          ArrayList<PrintOrderModifier> orderModifiers,
                          List<Map<String, String>> taxes,
                          List<PaymentSettlement> settlement, RoundAmount roundAmount,
                          boolean openDrawer) {

        boolean isCashSettlement = false;
        List<PrintReceiptInfo> printReceiptInfos = new ArrayList<PrintReceiptInfo>();
        if (settlement != null) {
            for (PaymentSettlement paymentSettlement : settlement) {
                PrintReceiptInfo printReceiptInfo = new PrintReceiptInfo();
                printReceiptInfo.setPaidAmount(paymentSettlement
                        .getPaidAmount());
                printReceiptInfo.setPaymentTypeId(paymentSettlement
                        .getPaymentTypeId());
                switch (paymentSettlement.getPaymentTypeId().intValue()) {
                    case ParamConst.SETTLEMENT_TYPE_CASH:
                        printReceiptInfo.setCashChange(paymentSettlement
                                .getCashChange());
                        isCashSettlement = true;
                        break;
                    case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
                    case ParamConst.SETTLEMENT_TYPE_UNIPAY:
                    case ParamConst.SETTLEMENT_TYPE_VISA:
                    case ParamConst.SETTLEMENT_TYPE_DINNER_INTERMATIONAL:
                    case ParamConst.SETTLEMENT_TYPE_AMEX:
                    case ParamConst.SETTLEMENT_TYPE_JCB:
                        printReceiptInfo
                                .setCardNo(CardsSettlementSQL
                                        .getCardNoByPaymentIdAndPaymentSettlementId(
                                                paymentSettlement.getPaymentId()
                                                        .intValue(),
                                                paymentSettlement.getId()
                                                        .intValue()));
                        break;
                    case ParamConst.SETTLEMENT_TYPE_NETS:
                        printReceiptInfo
                                .setCardNo(NetsSettlementSQL
                                        .getNetsSettlementByPament(
                                                paymentSettlement.getPaymentId()
                                                        .intValue(),
                                                paymentSettlement.getId()
                                                        .intValue())
                                        .getReferenceNo()
                                        + "");
                        break;

                    default: {
                        PaymentMethod pamentMethod = PaymentMethodSQL.getPaymentMethodByPaymentTypeId(paymentSettlement.getPaymentTypeId().intValue());
                        if (pamentMethod != null && !TextUtils.isEmpty(pamentMethod.getNameOt())) {
                            printReceiptInfo.setPaymentTypeName(pamentMethod.getNameOt());
                        }
                    }
                    break;
                }
                printReceiptInfos.add(printReceiptInfo);
            }
        }
        if (mRemoteService == null) {
            printerDialog();
            return;
        }
        try {
            Map<String, String> roundingMap = new HashMap<String, String>();
            String total = order.getTotal();
            String rounding = "0.00";
            if (roundAmount != null) {
                total = BH.sub(BH.getBD(order.getTotal()),
                        BH.getBD(roundAmount.getRoundBalancePrice()), true)
                        .toString();
                rounding = BH.getBD(roundAmount.getRoundBalancePrice())
                        .toString();
            }
            if (order.getPromotion() != null) {
                total = BH.add(BH.formatMoneyBigDecimal(total),
                        BH.getBD(order.getPromotion()), true)
                        .toString();
            }
            roundingMap.put("Total", total);
            roundingMap.put("Rounding", rounding);
            Gson gson = new Gson();
            String prtStr = gson.toJson(printer);
            String prtTitle = gson.toJson(title);
            String orderStr = gson.toJson(order);
            String details = gson.toJson(orderItems);
            String mods = gson.toJson(orderModifiers);
            String tax = gson.toJson(taxes);
            String payment = gson.toJson(printReceiptInfos);
            String roundStr = gson.toJson(roundingMap);
            String apporders = "";

            mRemoteService.printBill(prtStr, prtTitle, orderStr, details,
                    mods, tax, payment,
                    false,
                    false,
                    roundStr,
                    getCurrencySymbol(),
                    openDrawer, BH.IsDouble(), "", apporders);
//            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isRevenueKiosk() {
        if (revenueCenter != null
                && revenueCenter.getIsKiosk() == ParamConst.REVENUECENTER_IS_KIOSK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isKotPrint() {
        return kot_print = Store.getBoolean(instance, Store.KOT_PRINT, true);
    }


    public void printKOT(PrinterDevice printer, KotSummary kotsummary, List<KotItemDetail> kotItemDetails,
                         List<KotItemModifier> kotItemModifiers) {

//        if (!isKotPrint()) {
//            return;
//        }

        if (mRemoteService == null) {
            printerDialog();
            return;
        }

        Gson gson = new Gson();
        String prtStr = gson.toJson(printer);
        String details = gson.toJson(kotItemDetails);
        String mods = gson.toJson(kotItemModifiers);
        String kotsumStr = gson.toJson(kotsummary);

        try {
            mRemoteService.printKOT(prtStr, kotsumStr, details, mods, true, false, 2, false, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void closeDiscovery() {
        if (mRemoteService == null) {
            return;
        }
        try {
            mRemoteService.closeDiscovery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void discoverPrinter(final Handler handler) {
        if (mRemoteService == null) {
            printerDialog();
            return;
        }

        Log.d("discoverPrinter", "1856");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    mCallback.setHandler(handler);
                    mRemoteService.listPrinters("0");
                    Log.d("discoverPrinter", "1860");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void printerDialog() {
        BaseActivity context = App.getTopActivity();
        if (context == null)
            return;
        DialogFactory.commonTwoBtnDialog(context, context.getResources()
                        .getString(R.string.print_down), context.getResources()
                        .getString(R.string.reconnect_print), context.getResources()
                        .getString(R.string.no),
                context.getResources().getString(R.string.yes), null,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        App.instance.tryConnectRemotePrintService();
                    }
                });
    }

    public LocalRestaurantConfig getLocalRestaurantConfig() {
        if (localRestaurantConfig == null) {
            this.localRestaurantConfig = LocalRestaurantConfig.getInstance();
        }
        return localRestaurantConfig;
    }

    public void setLocalRestaurantConfig(
            List<RestaurantConfig> restaurantConfigs) {
        this.localRestaurantConfig = getLocalRestaurantConfig();
        for (RestaurantConfig restaurantConfig : restaurantConfigs) {
            switch (restaurantConfig.getParaType().intValue()) {
                case ParamConst.SALE_SESSION_TYPE:
                    this.localRestaurantConfig
                            .setSessionConfigType(restaurantConfig);
                    break;
                case ParamConst.PRICE_TAX_INCLUSIVE:
                    this.localRestaurantConfig.setIncludedTax(restaurantConfig);
                    break;
                case ParamConst.ROUND_RULE_TYPE:
                    this.localRestaurantConfig.setRoundType(restaurantConfig);
                    break;
                case ParamConst.CURRENCY_TYPE:
                    this.localRestaurantConfig.setCurrencySymbol(restaurantConfig);
                    this.localRestaurantConfig.setCurrencySymbolType(restaurantConfig);
                    break;
                case ParamConst.DEF_DISCOUNT_TYPE:
                    this.localRestaurantConfig.setDiscountOption(restaurantConfig);
                    break;
                case ParamConst.SEND_FOOD_CARD_NUM:
                    this.localRestaurantConfig.setSendFoodCardNumList(restaurantConfig);
                    break;
                default:
                    break;
            }
        }
    }

    public void tryConnectRemotePrintService() {
        int printVersionCode = 0;
        int posVersionCode = 0;
        try {
            posVersionCode = instance.getPackageManager().getPackageInfo(instance.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<PackageInfo> packageinfo = this.getPackageManager()
                .getInstalledPackages(0);

        int count = packageinfo.size();
        for (int i = 0; i < count; i++) {

            PackageInfo pinfo = packageinfo.get(i);
            ApplicationInfo appInfo = pinfo.applicationInfo;
            if (!((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)) {
                String name = pinfo.applicationInfo.packageName;

                if (name.equals("com.alfred.remote.printservice")) {
                    printVersionCode = pinfo.versionCode;
                }
            }
        }
        boolean hasApk = copyApkFromAssets(this, "printServiceApk/print.apk", Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/print.apk");
        if (printVersionCode < posVersionCode && hasApk) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/print.apk"),
                    "application/vnd.android.package-archive");
            intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            intentFilter.addDataScheme("package");
            registerReceiver(receiver, intentFilter);
            this.startActivity(intent);
        } else {
            connectRemotePrintService();
        }

    }

    public boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
        BH.initFormart(formatType, getCurrencySymbol());
    }
}
