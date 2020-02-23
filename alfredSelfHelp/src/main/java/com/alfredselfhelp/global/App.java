package com.alfredselfhelp.global;

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
import com.alfred.remote.printservice.RemotePrintServiceCallback;
import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ConsumingRecords;
import com.alfredbase.javabean.LocalDevice;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderPromotion;
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
import com.alfredbase.store.sql.PaymentMethodSQL;
import com.alfredbase.store.sql.PromotionDataSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TimeUtil;
import com.alfredselfhelp.R;
import com.alfredselfhelp.activity.MenuActivity;
import com.alfredselfhelp.utils.TvPref;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

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
    private static final String TAG = App.class.getSimpleName();
    public static App instance;
    boolean mbPlayIMG;
    public String VERSION;
    private String posIp;
    private MainPosInfo mainPosInfo;
    public static final int HANDLER_REFRESH_CALL = 1;
    public static final int HANDLER_REFRESH_CALL_ON = 2;

    public static final int HANDLER_REFRESH_TIME = 5;

    private String pairingIp, ip;
    private WaiterDevice waiterdev;
    private User user;
    private RevenueCenter revenueCenter;

    private Long businessDate;
    private Long lastBusinessDate;
    private int indexOfRevenueCenter;

    public static boolean isleftMoved;
    private String currencySymbol = "$";
    private LocalRestaurantConfig localRestaurantConfig;

    private static final String DATABASE_NAME = "com.alfredselfhelp";
    private SessionStatus sessionStatus;
    private IntentFilter intentFilter;
    // Remote Print Service
    private IAlfredRemotePrintService mRemoteService = null;
    private RemotePrintServiceCallback mCallback = new RemotePrintServiceCallback();
    private Map<Integer, PrinterDevice> printerDevices = new ConcurrentHashMap<>();
    private Map<Integer, List<PrinterDevice>> map = new HashMap<Integer, List<PrinterDevice>>();
    private Observable<Object> observable;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        VERSION = getAppVersionName();
        SQLExe.init(this, DATABASE_NAME, DATABASE_VERSION);
        TvPref.init();
        mbPlayIMG = TvPref.readPlayIMGEn();

        BugseeHelper.init(this, "f252f398-4474-43c3-b287-38e6ce235894");
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(APPPATH);
        CrashReport.initCrashReport(getApplicationContext(), "4a949e77d4", isOpenLog, strategy);


        observable = RxBus.getInstance().register("kpmTime");
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object object) {
//
                BaseActivity activity = App.getTopActivity();
                if (activity instanceof MenuActivity) {
                    App.getTopActivity().httpRequestAction(HANDLER_REFRESH_TIME, null);
                }
            }
        });
    }


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

    /*
     * Remote Print Service Connection
     */
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
                mRemoteService.registerCallBack(mCallback);
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
            printerDialog();
        } else {
            connectRemotePrintService();
        }

    }


    public void remoteStoredCard(PrinterDevice printer, ConsumingRecords consumingRecords, String balance) {
        if (mRemoteService == null) {
            printerDialog();
            return;
        }
        String action = "Top-up";
        if (consumingRecords.getConsumingType().intValue() == ParamConst.STORED_CARD_ACTION_TOP_UP) {
            action = "Top-up";
        } else if (consumingRecords.getConsumingType().intValue() == ParamConst.STORED_CARD_ACTION_PAY) {
            action = "Pay";
        } else if (consumingRecords.getConsumingType().intValue() == ParamConst.STORED_CARD_ACTION_REFUND) {
            action = "Refund";
        }
        try {
            Gson gson = new Gson();
            String prtStr = gson.toJson(printer);
            mRemoteService.printStoredCardConsume(prtStr, "StoredCard Amount", TimeUtil.getTimeFormat(consumingRecords.getConsumingTime()), consumingRecords.getCardId() + "", action, consumingRecords.getConsumingAmount(), balance);
        } catch (RemoteException e) {
            e.printStackTrace();
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

    public String getPairingIp() {
        pairingIp = Store.getString(App.instance, "kip");
        return pairingIp;
    }

    public void setPairingIp(String pairingIp) {

        Store.putString(App.instance, "kip", pairingIp);

    }


    public MainPosInfo getMainPosInfo() {
        if (mainPosInfo == null) {
            mainPosInfo = Store.getObject(this.getApplicationContext(), Store.MAINPOSINFO, MainPosInfo.class);
        }
        return mainPosInfo;
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
                    this.localRestaurantConfig.setFormatType(restaurantConfig);
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

    public void setMainPosInfo(MainPosInfo mainPosInfo) {
        this.mainPosInfo = mainPosInfo;
        Store.saveObject(this.getApplicationContext(), Store.MAINPOSINFO, mainPosInfo);
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


    public User getUser() {
        if (user == null) {
            user = Store.getObject(App.instance, Store.KPM_USER, User.class);
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        Store.saveObject(App.instance, Store.KPM_USER, user);
    }

    public boolean getPlayIMGEn() {
        return mbPlayIMG;
    }


    public String getPosIp() {

        ip = Store.getString(App.instance, Store.KPM_IP);

        return ip;
    }

    public void setPosIp(String posIp) {
        Store.putString(App.instance, Store.KPM_IP, posIp);

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

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol, boolean isDouble) {
        this.currencySymbol = currencySymbol;
       // BH.initFormart(isDouble);
    }

    public LocalRestaurantConfig getLocalRestaurantConfig() {
        if (localRestaurantConfig == null) {
            this.localRestaurantConfig = LocalRestaurantConfig.getInstance();
        }
        return localRestaurantConfig;
    }

    public Long getBusinessDate() {
        if (businessDate == null) {
            businessDate = (Long) Store.getLong(this, Store.BUSINESS_DATE);
        }
        // if all data is null, set today is businessDate
        if (businessDate == Store.DEFAULT_LONG_TYPE) {
            businessDate = TimeUtil.getNewBusinessDate();
        }
        return businessDate;
    }

    public void setBusinessDate(Long businessDate) {
        this.businessDate = businessDate;
    }

    public Long getLastBusinessDate() {
        if (lastBusinessDate == null) {
            lastBusinessDate = Store.getLong(this, Store.LAST_BUSINESSDATE);
        }
        // if all data is null, set today is businessDate
        if (lastBusinessDate == null
                || lastBusinessDate == Store.DEFAULT_LONG_TYPE) {
            lastBusinessDate = TimeUtil.getNewBusinessDate();
        }
        return lastBusinessDate;
    }

    public void setLastBusinessDate(Long lastBusinessDate) {
        this.lastBusinessDate = lastBusinessDate;
    }

    public int getIndexOfRevenueCenter() {
        if (revenueCenter != null) {
            indexOfRevenueCenter = revenueCenter.getId();
        }
        return indexOfRevenueCenter;
    }

    @Override
    public void onTerminate() {
        RfidApiCentre.getInstance().onDestroy();

        if (observable != null) {
            RxBus.getInstance().unregister("showRelogin", observable);
        }
        //     TcpUdpFactory.stopUdpServer();
        super.onTerminate();
    }

    public Map<Integer, PrinterDevice> getPrinterDevices() {
        return printerDevices;
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

    public void removePrinterDevice(Integer deviceid) {
        for (Iterator<Map.Entry<Integer, PrinterDevice>> it = printerDevices
                .entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, PrinterDevice> entry = it.next();
            if (entry.getKey().intValue() == deviceid.intValue()) {
                it.remove();
            }
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
                    mCallback.setHandler(handler);
                    mRemoteService.listPrinters("0");
                    Log.d("discoverPrinter", "1860");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void remoteBillPrint(PrinterDevice printer, PrinterTitle title,
                                Order order, List<PrintOrderItem> orderItems,
                                List<PrintOrderModifier> orderModifiers,
                                List<Map<String, String>> taxes,
                                List<PaymentSettlement> settlement,
                                RoundAmount roundAmount, String cardNum) {
        boolean openDrawer = false;
        if (mRemoteService == null) {
            printerDialog();
            return;
        }
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
                        break;
                    case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
                    case ParamConst.SETTLEMENT_TYPE_UNIPAY:
                    case ParamConst.SETTLEMENT_TYPE_VISA:
                    case ParamConst.SETTLEMENT_TYPE_DINNER_INTERMATIONAL:
                    case ParamConst.SETTLEMENT_TYPE_AMEX:
                    case ParamConst.SETTLEMENT_TYPE_JCB:
                    case ParamConst.SETTLEMENT_TYPE_NETS:
                        String num = "";
                        if(!TextUtils.isEmpty(cardNum)){
                                num = cardNum;
                        }
                        printReceiptInfo.setCardNo(num);
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
        try {
            Map<String, String> roundingMap = new HashMap<String, String>();
            BigDecimal total = BH.getBD(order.getTotal());
            String rounding = "0.00";
            if (roundAmount != null) {
                total = BH.sub(BH.getBD(order.getTotal()),
                        BH.getBD(roundAmount.getRoundBalancePrice()), true);
                rounding = BH.getBD(roundAmount.getRoundBalancePrice())
                        .toString();
            }
//
//            if(order.getPromotion()!=null){
//                total = BH.add(BH.getBD(order.getTotal()),
//                        BH.getBD(order.getPromotion()), true);
//            }
            List<OrderPromotion>  promotionData= PromotionDataSQL.getPromotionDataOrOrderid(order.getId());

            roundingMap.put("Total", total.toString());
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
            String proStr=gson.toJson(promotionData);
            mRemoteService.printKioskBill(prtStr, prtTitle, orderStr,
                    details, mods, tax, payment, false, false, roundStr,
                    null, getLocalRestaurantConfig().getCurrencySymbol(),
                    openDrawer, BH.IsDouble(),proStr,App.instance.getLocalRestaurantConfig().getFormatType());

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public PrinterDevice getCahierPrinter() {
        for (Map.Entry<Integer, PrinterDevice> dev : printerDevices.entrySet()) {
            Integer key = dev.getKey();
            PrinterDevice devPrinter = dev.getValue();
            if (devPrinter.getIsCahierPrinter() > 0) {
                return devPrinter;
            }
        }
        return null;
    }

}
