package com.alfred.remote.printservice;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.alfred.print.jobs.PrintManager;
import com.alfred.printer.ESCPrinter;
import com.alfredbase.PrinterDeviceConfig;
import com.alfredbase.javabean.PrintBean;
import com.alfredbase.store.Store;
import com.alfredbase.utils.BH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.alfredbase.javabean.PrintBean.PRINT_TYPE;

public class PrintService extends Service {

    public static final String ACTION_START = "com.alfred.print.ACTION_START";
    public static final String ACTION_SHUT_DOWN = "com.alfred.print.ACTION_SHUT_DOWN";
    public static final String ACTION_DISCOVER_PRINTER = "com.alfred.print.ACTION_DISCOVER_PRINTER";

    static final String TAG = "Alfred Print Service";

    private boolean mShutDown = false;

    public static PrintService instance;
    private PrintManager printJobMgr;
    private PrinterQueueManager pqMgr;

    public static String PRINT_DOLLAR_SIGN_SETTING = "DOLLAR_SIGN_SETTING";
    public static String PRINT_LANG_SETTING = "PRINT_LANG_SETTING";
    public static String PRINT_COUNTRY_SETTING = "PRINT_COUNTRY_SETTING";

    final Object lock = new Object();

    List<IAlfredRemotePrintServiceCallback> mCallbacks;


    private ArrayList<PrintBean> mBluetoothDevicesDatas = new ArrayList<>();
    BluetoothAdapter mBluetoothAdapter;
    private Callback callback;


    private static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";


    private Context mContext;
    private UsbDevice mUsbDevice;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;
    private Map<Integer, List<Integer>> usbMap = new HashMap<>();
    //IP Printer Handler
//    static Map<String, WIFIPrinterHandler> printerHandlers = new ConcurrentHashMap<String, WIFIPrinterHandler>();
    private Map<String, ESCPrinter> escPrinterMap = new HashMap<String, ESCPrinter>();

    @Override
    public void onCreate() {
        super.onCreate();
        this.printJobMgr = new PrintManager(this);
        this.pqMgr = new PrinterQueueManager(this);
        instance = this;
        mCallbacks = new ArrayList<IAlfredRemotePrintServiceCallback>();
        //广播注册

        this.printJobMgr.start();
        this.pqMgr.start();
        List<Integer> l1 = new ArrayList<>();
        l1.add(85);
        l1.add(23);
        List<Integer> l2 = new ArrayList<>();
        l2.add(22304);
        List<Integer> l3 = new ArrayList<>();
        l3.add(8963);
        l3.add(8965);
        List<Integer> l4 = new ArrayList<>();
        l4.add(30084);
        List<Integer> l5 = new ArrayList<>();
        l5.add(256);
        l5.add(512);
        l5.add(768);
        l5.add(1024);
        l5.add(1280);
        List<Integer> l6 = new ArrayList<>();
        l6.add(256);
        usbMap.put(1137, l1);
        usbMap.put(1155, l2);
        usbMap.put(1659, l3);
        usbMap.put(6790, l4);
        usbMap.put(26728, l5);
        usbMap.put(34918, l6);

        Log.d(TAG, "Creating Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind ");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if ("alfred.intent.action.bindPrintService".equals(intent.getAction()) &&
                    ("fxxxkprinting").equals((String) extras.get("PRINTERKEY"))) {
//				if (this.printJobMgr!=null)
//					this.printJobMgr.addPingJob();
                boolean isDouble = true;
                if (extras.containsKey("isDouble")) {
                    isDouble = extras.getBoolean("isDouble");
                }
                BH.initFormart(null, null);
                return new PrintServiceBinder(this);
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "Destroying Service");
        //this.printJobMgr.clear();
        this.printJobMgr.stop();
        this.mCallbacks.clear();
        //close all sockets
        this.pqMgr.stop();
        closeAllSockets();
        try {
            unregisterReceiver(mReceiver);
            unregisterReceiver(mUsbDeviceReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


        super.onDestroy();
        //this.printJobMgr = null;


    }


    public void closeAllSockets() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Set<String> key = escPrinterMap.keySet();
                for (Iterator it = key.iterator(); it.hasNext(); ) {
                    String ip = (String) it.next();
                    ESCPrinter escp = escPrinterMap.get(ip);
                    if (escp != null) {
                        escp.setConnected(false);
                        escp.close();
                    }
                }
            }
        }).start();

    }

    public PrintManager getPrintMgr() {
        return this.printJobMgr;
    }

    public PrinterQueueManager getPqMgr() {
        return pqMgr;
    }

    public boolean isTMU220(String model) {
        if (model != null && model.length() > 0) {
            if (model.toLowerCase().contains("tm-u220"))
                return true;
        }
        return false;
    }

    public boolean isTM88(String model) {
        if (model != null && model.length() > 0) {
            if (model.toLowerCase().contains("t88"))
                return true;
        }
        return false;
    }

    public boolean isV1sG(String model) {
        if (model != null && model.length() > 0) {
            if (model.toLowerCase().contains("v1s-g"))
                return true;
        }
        return false;
    }

    public String getDollarSignStr() {
        String sign = "$";
        Integer dollarSign = Store.getInt(this, PrintService.PRINT_DOLLAR_SIGN_SETTING);
        if (dollarSign != null) {
            if (dollarSign.intValue() == PrinterDeviceConfig.PRINT_DOLLAR_CNY)
                sign = "￥";
            if (dollarSign.intValue() == PrinterDeviceConfig.PRINT_DOLLAR_MYM)
                sign = "RM";
        }
        return sign;
    }

    public void configure(int country, int lang, int dollarsign) {
        Store.putInt(this, PrintService.PRINT_COUNTRY_SETTING, country);
        Store.putInt(this, PrintService.PRINT_LANG_SETTING, lang);
        Store.putInt(this, PrintService.PRINT_DOLLAR_SIGN_SETTING, dollarsign);
    }

    public void removePrinterByIP(String ip) {
        this.escPrinterMap.remove(ip);
    }


//	public WIFIPrinterHandlerFQ getPrinterHandler(String ip) {
//		WIFIPrinterHandlerFQ wfpHandler = null;
//		synchronized(lock) {
//		  wfpHandler = PrintService.printerHandlers.get(ip);
//		  if (wfpHandler == null) {
////			  Looper.prepare();
//			  wfpHandler = new WIFIPrinterHandlerFQ(ip);
////			  Looper.loop();
//			  PrintService.printerHandlers.put(ip, wfpHandler);
//		  }
//		}
//		return wfpHandler;
//	}


    public Map<String, ESCPrinter> getEscPrinterMap() {
        return escPrinterMap;
    }

    public void putEscPrinterMap(String id, ESCPrinter eSCPrinter) {
        this.escPrinterMap.put(id, eSCPrinter);
    }


    //搜索USB

    public void SearchUsb() {
//        Log.d("SearchBluetooth", "start");
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        // mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        //注册USB设备权限管理广播
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);

//        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbDeviceReceiver, filter);

        // 列出所有的USB设备，并且都请求获取USB权限

        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Log.d("typeUsb", " 33333333--" + deviceList.size());
        if (deviceList.size() > 0) {
            for (UsbDevice device : deviceList.values()) {
                if (usbMap.containsKey(device.getVendorId())) {
                    List<Integer> list = usbMap.get(device.getVendorId());
                    for (Integer item : list) {
                        if (device.getProductId() == item.intValue()) {
                            mUsbManager.requestPermission(device, mPermissionIntent);
                        }
                    }
                }


            }
        }


    }


    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("typeUsb", " 444444444444" + action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                // Log.d("typeUsb", " 111111111111111");
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) && usbDevice != null) {
                        Log.d("typeUsb", usbDevice.getProductId() + " --111111111111111--" + usbDevice.getVendorId());
                        // 获取USBDevice
//                        if(usbMap.containsKey(usbDevice.getVendorId())){
//                            List<Integer> list = usbMap.get(usbDevice.getVendorId());
//                            for(Integer item : list){
//                                if(usbDevice.getProductId() == item.intValue()){
                        mUsbDevice = usbDevice;
                        callback.getUsbDevices(mUsbDevice);
//                                    return;
//                                }
//                            }
//                        }

                    } else {
                        //Toast.makeText(context, "Permission denied for device " + usbDevice, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.d("typeUsb", " 222222222");
                if (mUsbDevice != null) {
                    if (mUsbDeviceConnection != null) {
                        mUsbDeviceConnection.close();
                    }
                }
            }
        }
    };

    //搜索蓝牙

    public void SearchBluetooth() {
        Log.d("SearchBluetooth", "start");
        mBluetoothDevicesDatas.clear();
        mBluetoothAdapter.startDiscovery();
        SearchUsb();
    }


    public Boolean registerReceiverBluetooth() {

//        try {
//            unregisterReceiver(mReceiver);
//            return  false;
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//
//            registerReceiver(mReceiver, filter);
//            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            return  true;
//        }
//        if(mReceiver != null ) {
//
//            Log.e("registerRe", " ----");
//
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//
//            registerReceiver(mReceiver, filter);
//            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            return false;
//        }else {
//            Log.e("registerReceiverBluetoo", " ----");
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return true;
//        }
        //  return false;

    }

    //	/**
//	 * 通过广播搜索蓝牙设备
//	 */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 把搜索的设置添加到集合中
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //已经匹配的设备
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

                    Log.d("type", " 已经匹配的设备");
                    addBluetoothDevice(device);

                    //没有匹配的设备
                } else {

                    Log.d("type", " 没有匹配的设备");
                    addBluetoothDevice(device);
                }

                //搜索完成
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                Log.e("BluetoothAdapterend", "   " + mBluetoothDevicesDatas.size());

                for (int i = 0; i < mBluetoothDevicesDatas.size(); i++) {
                    callback.getBluetoothDevices(mBluetoothDevicesDatas.get(i));
                    //    callback.getBluetoothDevices(mBluetoothDevicesDatas.get(i));
                }
                //   mBluetoothAdapter.cancelDiscovery();
                unregisterReceiver(mReceiver);
                //    mReceiver=null;

                //    abortBroadcast();
            }
        }


    };


    //		/**
//		 * 添加数据
//		 * @param device 蓝牙设置对象
//		 */
    @SuppressLint("NewApi")
    private void addBluetoothDevice(BluetoothDevice device) {


        Log.d("type", " 添加蓝牙");
        for (int i = 0; i < mBluetoothDevicesDatas.size(); i++) {
            if (device.getAddress().equals(mBluetoothDevicesDatas.get(i).getAddress())) {
                mBluetoothDevicesDatas.remove(i);
                Log.d("addBluetoothDevice", " ---remove");
            }
        }
        if (device.getBondState() == BluetoothDevice.BOND_BONDED && device.getBluetoothClass().getDeviceClass() == PRINT_TYPE) {


            mBluetoothDevicesDatas.add(0, new PrintBean(device));
            Log.d("addBluetoothDevice", " -BOND_BONDED--" + mBluetoothDevicesDatas.size());

//                    for (int i = 0; i < mBluetoothDevicesDatas.size(); i++) {
//
//
//                        if (device.getAddress().equals(mBluetoothDevicesDatas.get(i).getAddress())) {
//                            mBluetoothDevicesDatas.remove(i);
//                            Log.d("addBluetoothDevice", " ---remove");
//                        } else {
//                            callback.getBluetoothDevices(mBluetoothDevicesDatas.get(i));
//                        }
//                    }


        } else {


            Log.d("type", " ;;;;" + device.getBluetoothClass().getDeviceClass() + "--" + device.getName() + "---" + device.getAddress() + "---" + device.getUuids() + "" + device.getBluetoothClass().describeContents());

            //  device.getBluetoothClass().getMajorDeviceClass()
            if (device.getBluetoothClass().getDeviceClass() == PRINT_TYPE) {


                mBluetoothDevicesDatas.add(new PrintBean(device));

                for (int i = 0; i < mBluetoothDevicesDatas.size(); i++) {

                    Log.d("addBluetoothDevice", " ==service更新==" + mBluetoothDevicesDatas.size());

//                        if (device.getAddress().equals(mBluetoothDevicesDatas.get(i).getAddress())) {
//                            callback.getBluetoothDevices(mBluetoothDevicesDatas.get(i));
////                            mBluetoothDevicesDatas.remove(i);
////                            Log.d("PRINT_TYPE", " ---mBluetoothDevicesDatas");
//                        }else {
//                            callback.getBluetoothDevices(mBluetoothDevicesDatas.get(i));
//                        }
                }


                // PrintBean pb = new PrintBean(device);

                //
            }

            Log.d("addBluetoothDevice", " ====" + mBluetoothDevicesDatas.size());
        }
    }

    //	**
//			* 提供接口回调方法
//     * @param callback
//     */
    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public static interface Callback {
        /**
         * 得到实时更新的数据
         *
         * @return
         */
        void getBluetoothDevices(PrintBean pd);

        void getUsbDevices(UsbDevice ud);
    }
}
