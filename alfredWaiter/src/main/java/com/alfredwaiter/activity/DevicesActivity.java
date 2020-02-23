package com.alfredwaiter.activity;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.LocalDevice;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.view.CustomListView;
import com.alfredbase.view.HorizontalListView;
import com.alfredwaiter.R;
import com.alfredwaiter.adapter.DeviceGroupAdapter;
import com.alfredwaiter.adapter.DevicesAdapter;
import com.alfredwaiter.global.App;
import com.alfredwaiter.popupwindow.SelectPrintWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备页
 */
public class DevicesActivity extends BaseActivity {


    public static final int ACTION_NEW_KDS_ADDED = 1041;
    public static final int ACTION_NEW_WAITER_ADDED = 1042;
    public static final int ACTION_ASSIGN_PRINTER_DEVICE = 10053;
    public static final int ASSIGN_PRINTER_DEVICE = 1;
    public static final int UNASSIGN_PRINTER_DEVICE = -1;
    public static final int MANUALLY_ADD_PRINTER = -100;

    final static String TAG = "DevicesActivity";

    private ImageButton btn_back;
    private TextView tv_title_name;
    private LinearLayout ll_print;
    //    private ImageView device_code_img;
//    private TextView devices_ip_tv;
//    private TextView devices_revenueCenter_tv;
    //打印机
    private LinearLayout devices_printe_lyt;
    //KDS
    private LinearLayout devices_transfer_lyt;


    //waiter
    private LinearLayout devices_waiter_lyt;
    private DevicesAdapter adapter;

    private HorizontalListView hv_printer_group;

    private CustomListView devices_customlistview;

    private DeviceGroupAdapter deviceGroupAdapter;

    private List<PrinterDevice> printerDBModelList = new ArrayList<PrinterDevice>();
    private List<Printer> printerDeptModelList = new ArrayList<Printer>();
    private int selectedViewId;
    private SelectPrintWindow selectPrintWindow;
    private int dex = 0;


    Map<Integer, List<PrinterDevice>> map = new HashMap<Integer, List<PrinterDevice>>();

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.devices_layout);
        map.clear();
        initUI();
        initData();
        //  new MyThread().start();

    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MANUALLY_ADD_PRINTER:
                    selectPrintWindow = new SelectPrintWindow(DevicesActivity.this, devices_customlistview, handler);
                    selectPrintWindow.show("TM-T81", "TM-T81");
                    break;
                case UNASSIGN_PRINTER_DEVICE: //解绑打印机
                    PrinterDevice device = (PrinterDevice) msg.obj;
                    unassignDevice(device);
                    break;
                case ACTION_ASSIGN_PRINTER_DEVICE: // 手动添加打印机
                    Gson gson = new Gson();
                    @SuppressWarnings("unchecked")
                    Map<String, String> printer = (Map<String, String>) gson
                            .fromJson((String) msg.obj,
                                    new TypeToken<Map<String, String>>() {
                                    }.getType());
                    if (printer != null) {
                        String assignToName = printer.get("assignTo");
                        if (assignToName != null && assignToName.length() > 0) {
                            String printerModel = printer.get("deviceName");
                            String printerName = printer.get("printerName");
                            LocalDevice localDevice = ObjectFactory
                                    .getInstance().getLocalDevice(assignToName,
                                            printerModel,
                                            ParamConst.DEVICE_TYPE_PRINTER,
                                            printerDeptModelList.get(dex).getId(),
                                            printer.get("printerIp"), "", printerName, printerDeptModelList.get(dex).getIsLablePrinter());
                            CoreData.getInstance().addLocalDevice(localDevice);

                            PrinterDevice prtDev = new PrinterDevice();
                            prtDev.setDevice_id(printerDeptModelList.get(dex).getId());
                            prtDev.setIP(printer.get("printerIp"));
                            prtDev.setIsCahierPrinter(printerDeptModelList.get(dex).getIsCashdrawer());
                            prtDev.setMac(localDevice.getMacAddress());
                            prtDev.setModel(printerModel);
                            prtDev.setName(assignToName);
                            prtDev.setPrinterName(printerName);
                            prtDev.setIsLablePrinter(printerDeptModelList.get(dex).getIsLablePrinter());
                            App.instance.setPrinterDevice(printerDeptModelList.get(dex).getId(), prtDev);
                            refreshPrinterDevices(null);
                            App.instance.discoverPrinter(handler);
                        }
                    }
                    break;
                case ACTION_NEW_KDS_ADDED: // 连接KDS设备
//                    KDSDevice kds = (KDSDevice)msg.obj;
//                    Map<Integer, KDSDevice> map1 = new HashMap<Integer, KDSDevice>();
//                    map1.put(kds.getDevice_id(), kds);
//                    App.instance.addKDSDevice(kds.getDevice_id(), kds);
                    if (selectedViewId == R.id.devices_transfer_lyt) {
                        MViews(selectedViewId);
                    }
                    break;
                case ACTION_NEW_WAITER_ADDED: // 连接Waiter设备
//                    WaiterDevice waiter = (WaiterDevice)msg.obj;
//                    Map<Integer, WaiterDevice> map2 = new HashMap<Integer, WaiterDevice>();
//                    map2.put(waiter.getWaiterId(), waiter);
//                    App.instance.setWaiterDevices(map2);
                    if (selectedViewId == R.id.devices_waiter_lyt) {
                        MViews(selectedViewId);
                    }
                    break;
//                case RemotePrintServiceCallback.PRINTERS_DISCOVERIED: // 同一局域网内搜索可绑定的打印机
//                    Log.d("refreshPrinterDevices", " ---同一局域网内搜索可绑定的打印机---");
//                    refreshPrinterDevices((Map<String, String>) msg.obj);
//
//
//                    break;

//                case RemotePrintServiceCallback.B_RINTERS_DISCOVERIED: // 搜索到的蓝牙打印机
//                    Log.d("refreshPrinterDevices", " ---搜索到的蓝牙打印机---");
//                    BluetoothPrinterDevices((List<PrinterDevice>) msg.obj);
//                    //refreshPrinterDevices((Map<String, String>) msg.obj);
//                    break;

                case ASSIGN_PRINTER_DEVICE: // 绑定打印机


                    PrinterDevice printerDevice = (PrinterDevice) msg.obj;
                    Printer prt = printerDeptModelList.get(dex);
                    printerDevice.setDevice_id(prt.getId());
                    printerDevice.setIsCahierPrinter(prt.getIsCashdrawer());
                    printerDevice.setPrinterName(prt.getPrinterName());
                    printerDevice.setIsLablePrinter(prt.getIsLablePrinter());
//                    List<PrinterDevice> list = new ArrayList<PrinterDevice>();
//                    list.add(printerDevice);
//                    adapter.setList(list, 1);
//                    App.instance.setPrinterDevice(prt.getId(), printerDevice);

                    Log.d("ASSIGN_PRINTER_DEVICE", " ---绑定打印机---IsLablePrinter---" + printerDeptModelList.get(dex).getIsLablePrinter());
                    LocalDevice localDevice = ObjectFactory.getInstance().getLocalDevice(printerDevice.getName(), printerDevice.getModel(),
                            ParamConst.DEVICE_TYPE_PRINTER,
                            printerDeptModelList.get(dex).getId(),
                            printerDevice.getIP(),
                            printerDevice.getMac(),
                            printerDevice.getPrinterName(), printerDeptModelList.get(dex).getIsLablePrinter());
                    CoreData.getInstance().addLocalDevice(localDevice);
                    App.instance.loadPrinters();
                    map.clear();
                    refreshPrinterDevices(null);

                    App.instance.closeDiscovery();
                    App.instance.discoverPrinter(handler);
                    break;
                default:
                    break;
            }
        }
    };


    // 显示连接蓝牙的打印机
//
//    private void BluetoothPrinterDevices(List<PrinterDevice> plist) {
////        List<PrinterDevice> devices;
////        Map<Integer, List<PrinterDevice>> map = new HashMap<Integer, List<PrinterDevice>>();
////        for (PrinterDevice entry : plist) {
////            devices = new ArrayList<PrinterDevice>();
////
////            map.put(entry.getDevice_id(), devices);
////        }
//
//        if (adapter != null) {
//
//            // Log.d("printerDBModelList", " ---printerDBModelList1---"+printerDBModelList.size());
//            adapter.setList(plist, 10);
//        } else {
//            adapter = new DevicesAdapter(this, plist, handler);
//            // Log.d("printerDBModelList", " ---printerDBModelList2---"+printerDBModelList.size());
//            devices_customlistview.setAdapter(adapter);
//        }
//
//        Log.d("refreshPrinterDevices", " ---显示蓝牙选项---" + plist.get(0).getType());
//        //  adapter.setList(printerDBModelList, 1);
//    }

    // 搜索可连接的打印机
    private void refreshPrinterDevices(Map<String, String> printersDiscovered) {
        Map<Integer, PrinterDevice> data = App.instance.getPrinterDevices();


        //     Map<Integer, List<PrinterDevice>> map = new HashMap<Integer, List<PrinterDevice>>();
        for (Map.Entry<Integer, PrinterDevice> entry : data.entrySet()) {
            List<PrinterDevice> devices = new ArrayList<PrinterDevice>();
            devices.add(entry.getValue());
            map.put(entry.getKey(), devices);
        }


        if (printersDiscovered != null) {


            for (Map.Entry<String, String> entry : printersDiscovered.entrySet()) {


                PrinterDevice tmppt = new PrinterDevice();
                //  UIHelp.showToast(this, entry.getKey());

                if (entry.getKey().contains(":")) {
                    tmppt.setIP(entry.getKey());
                    tmppt.setName(entry.getValue());
                    tmppt.setDevice_id(-1);
                    tmppt.setType("2");

                    Log.d("refreshPrinterDevices", " ---包含该字符串---" + entry.getKey());
                    System.out.println("包含该字符串");
                } else if (entry.getKey().contains(",")) {
                    tmppt.setIP(entry.getKey());
                    tmppt.setName(entry.getValue());
                    tmppt.setDevice_id(-1);
                    Log.d("refreshPrinterDevices", " ---USB---" + entry.getKey());
                } else {
                    tmppt.setIP(entry.getKey());
                    tmppt.setName(entry.getValue());
                    tmppt.setDevice_id(-1);
                    Log.d("refreshPrinterDevices", " ---不包含该字符串---" + entry.getKey());
                }
                if (!map.containsKey(tmppt.getDevice_id())) {
                    List<PrinterDevice> list = new ArrayList<PrinterDevice>();
                    // 获取所有键值对对象的集合
                    Log.d("refreshPrinterDevices", " ---获取所有键值对对象的集合---" + entry.getKey());
                    // 遍历键值对对象的集合，得到每一个键值对对象
//                    Map<Integer, List<PrinterDevice>> m = new ConcurrentHashMap<Integer, List<PrinterDevice>>();
//                    m.putAll(map);


                    list.add(tmppt);
                    map.put(tmppt.getDevice_id(), list);
                } else {

                    List<PrinterDevice> printerDevices = map.get(tmppt.getDevice_id());
                    Log.d("refreshPrinterDevices", " ---获取所有键值对对象的集合111---");
//                    Iterator iterator = map.keySet().iterator();
//                    while (iterator.hasNext()) {
//                        int key = (Integer) iterator.next();
//                        if(map.get(key).get(0).getIP().equals(tmppt.getIP())){
//                            Log.d("refreshPrinterDevices", " ---获取所有键值对对象的集合remove---" );
//                            iterator.remove();        //添加该行代码
//                            map.remove(key);
//                        }
//                    }
//
                    boolean is = true;
                    for (int i = 0; i < printerDevices.size(); i++) {
                        PrinterDevice printerDevice = printerDevices.get(i);
                        if (printerDevice != null && printerDevice.getIP().equals(tmppt.getIP())) {
                            Log.d("refreshPrinterDevices", " ---获取所有键值对对象的集合remove---");
                            is = false;
                        }
                    }
                    if (is) {
                        printerDevices.add(tmppt);
                    }

                }
            }
        }

        App.instance.setMap(map);


        Printer printer1 = printerDeptModelList.get(dex);

        if (map.containsKey(printer1.getId())) {


            printerDBModelList = map.get(printer1.getId());

        } else {


            printerDBModelList = map.get(-1);

//            if(TextUtils.isEmpty(printerDBModelList.size()+"")) {
//                Log.d("refreshPrinterDevices", " ---2222222---" + printerDBModelList.size());
//            }


        }
        if (selectedViewId == R.id.devices_printe_lyt) {
            if (adapter != null) {

                //     Log.d("printerDBModelList", " ---printerDBModelList1---"+printerDBModelList.size()+"--"+printerDBModelList.get(0).getIP());


                adapter.setList(printerDBModelList, 1);
            } else {
                adapter = new DevicesAdapter(this, printerDBModelList, handler);
                // Log.d("printerDBModelList", " ---printerDBModelList2---"+printerDBModelList.size());
                devices_customlistview.setAdapter(adapter);
            }
        }
    }

    private void unassignDevice(PrinterDevice device) {
        App.instance.closeDiscovery();
        if (device == null)
            return;
        int localDevId = device.getDevice_id();
        if (localDevId > 0) {
            LocalDevice ldItem = CoreData.getInstance()
                    .getLocalDeviceByDeviceIdAndIP(localDevId, device.getIP());
            App.instance.removePrinterDevice(localDevId);
            if (ldItem != null) {
                CoreData.getInstance().removeLocalDeviceByDeviceIdAndIP(
                        localDevId, device.getIP());
            }
            map.clear();
            refreshPrinterDevices(null);
            App.instance.discoverPrinter(handler);
        }
    }


    public class MyThread extends Thread {

        //继承Thread类，并改写其run方法
        private final static String TAG = "My Thread ===> ";

        public void run() {
            Log.d(TAG, "run");
            App.instance.discoverPrinter(handler);
//            Bitmap bitmap = BarcodeUtil.createQRImage(CommonUtil.getLocalIpAddress());
//            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.scanner_logo);
//            Bitmap mBitmap = BarcodeUtil.addLogo(bitmap, logo);
//            device_code_img.setImageBitmap(mBitmap);
        }
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.btn_back:
                DevicesActivity.this.finish();
                break;
            case R.id.devices_printe_lyt:
                selectedViewId = v.getId();
                float f1 = devices_printe_lyt.getY();
                initAnimation(f1);
                MViews(R.id.devices_printe_lyt);
                break;
            case R.id.devices_transfer_lyt:
                selectedViewId = v.getId();
                float f5 = devices_transfer_lyt.getY();
                initAnimation(f5);
                MViews(R.id.devices_transfer_lyt);
                break;
            case R.id.devices_waiter_lyt:
                selectedViewId = v.getId();
                float f9 = devices_waiter_lyt.getY();
                initAnimation(f9);
                MViews(R.id.devices_waiter_lyt);
                break;
            default:
                break;
        }
    }

    private void MViews(int id) {
        switch (id) {
            case R.id.devices_printe_lyt:
                hv_printer_group.setVisibility(View.VISIBLE);
                List<PrinterDevice> devices = new ArrayList<PrinterDevice>();
                Map<Integer, List<PrinterDevice>> hash = App.instance.getMap();
                if (hash.containsKey(printerDeptModelList.get(dex).getId())) {
                    devices = hash.get(printerDeptModelList.get(dex).getId());
                } else {
                    List<PrinterDevice> list = hash.get(-1);
                    if (list != null) {
                        for (PrinterDevice device : list) {
                            PrinterDevice printerDevice = new PrinterDevice();
                            printerDevice.setDevice_id(-1);
                            printerDevice.setName(device.getName());
                            printerDevice.setGroupId(device.getGroupId());
                            printerDevice.setIP(device.getIP());
                            printerDevice.setMac(device.getMac());
                            printerDevice.setModel(device.getModel());
                            devices.add(printerDevice);
                        }
                    }
                }
                if (adapter != null) {
                    adapter.setList(devices, 1);
                } else {
                    adapter = new DevicesAdapter(DevicesActivity.this, devices, handler);
                    devices_customlistview.setAdapter(adapter);
                }
                break;
//            case R.id.devices_transfer_lyt:
//                hv_printer_group.setVisibility(View.GONE);
//                Map<Integer, KDSDevice> map = App.instance.getKDSDevices();
//                List<PrinterDevice> KDSDevices = new ArrayList<PrinterDevice>();
//                if (map.size() > 0) {
//                    List<KDSDevice> kdsDevices = new ArrayList<KDSDevice>(map.values());
//                    for (KDSDevice device : kdsDevices) {
//                        PrinterDevice printerDevice = new PrinterDevice();
//                        printerDevice.setDevice_id(device.getDevice_id());
//                        printerDevice.setModel(device.getName());
//                        printerDevice.setName(device.getName());
//                        printerDevice.setIP(device.getIP());
//                        printerDevice.setMac(device.getMac());
//
//                        if (device.getIP().indexOf(":") != -1) {
//                            printerDevice.setType("2");
//                        } else {
//                            printerDevice.setType("1");
//                        }
//                        KDSDevices.add(printerDevice);
//                    }
//                }
//                if (adapter != null) {
//                    adapter.setList(KDSDevices, 2);
//                } else {
//                    adapter = new DevicesAdapter(DevicesActivity.this, KDSDevices, handler);
//                    devices_customlistview.setAdapter(adapter);
//                }
//                break;
//            case R.id.devices_waiter_lyt:
//                hv_printer_group.setVisibility(View.GONE);
//                Map<Integer, WaiterDevice> wmap = App.instance.getWaiterDevices();
//                List<PrinterDevice> WaiterDevices = new ArrayList<PrinterDevice>();
//                if (wmap.size() > 0) {
//                    List<WaiterDevice> waiterDevices = new ArrayList<WaiterDevice>(wmap.values());
//                    for (WaiterDevice device : waiterDevices) {
//                        PrinterDevice printerDevice = new PrinterDevice();
//                        printerDevice.setDevice_id(device.getWaiterId());
//                        printerDevice.setIP(device.getIP());
//                        printerDevice.setMac(device.getMac());
//
//                        if (device.getIP().indexOf(":") != -1) {
//                            printerDevice.setType("2");
//                        } else {
//                            printerDevice.setType("1");
//                        }
//                        WaiterDevices.add(printerDevice);
//                    }
//                }
//                if (adapter != null) {
//                    adapter.setList(WaiterDevices, 2);
//                } else {
//                    adapter = new DevicesAdapter(DevicesActivity.this, WaiterDevices, handler);
//                    devices_customlistview.setAdapter(adapter);
//                }
//                break;
        }
    }


//    class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            //这是在后台子线程中执行的
//            Bitmap mBitmap = null;
//            try {
//                Bitmap bitmap = BarcodeUtil.createQRImage(CommonUtil.getLocalIpAddress());
//                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.scanner_logo);
//                mBitmap = BarcodeUtil.addLogo(bitmap, logo);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return mBitmap;
//        }
//
//        @Override
//        protected void onCancelled() {
//            //当任务被取消时回调
//            super.onCancelled();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            //更新进度
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            //当任务执行完成是调用,在UI线程
//            if (bitmap != null) {
//                device_code_img.setImageBitmap(bitmap);
//            }
//        }
//    }

    private void initData() {

        //本机IP地址和MAC地址
        printerDBModelList = new ArrayList<>(App.instance.getPrinterDevices().values());
//        if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
//            printerDeptModelList = PrinterSQL.getAllPrinterByType(1);
//        } else {
        printerDeptModelList = PrinterSQL.getCashierPrinter();
//        }
        if (printerDeptModelList.size() > 0) {
            deviceGroupAdapter = new DeviceGroupAdapter(this, printerDeptModelList);
            hv_printer_group.setAdapter(deviceGroupAdapter);
        }

//        refreshPrinterDevices(null);
        App.instance.discoverPrinter(handler);
        if (deviceGroupAdapter != null)
            deviceGroupAdapter.setSelectIndex(dex);
        registEvent();
    }


    private void initUI() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        ll_print = (LinearLayout) findViewById(R.id.ll_print);
        ll_print.setVisibility(View.GONE);

        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        tv_title_name.setText("Devices");
        hv_printer_group = (HorizontalListView) findViewById(R.id.hv_printer_group);

//        device_code_img = (ImageView) findViewById(R.id.device_code_img);

//        devices_ip_tv = (TextView) findViewById(R.id.devices_ip_tv);

//        devices_revenueCenter_tv = (TextView) findViewById(R.id.devices_revenueCenter_tv);
//        if (App.instance.getPosType() == ParamConst.POS_TYPE_MAIN) {
//            devices_revenueCenter_tv.setText(App.instance.getMainPosInfo().getName());
//        } else {
//            devices_revenueCenter_tv.setText("");
//        }
        devices_printe_lyt = (LinearLayout) findViewById(R.id.devices_printe_lyt);
        selectedViewId = R.id.devices_printe_lyt;
        devices_transfer_lyt = (LinearLayout) findViewById(R.id.devices_transfer_lyt);
        devices_waiter_lyt = (LinearLayout) findViewById(R.id.devices_waiter_lyt);
        devices_customlistview = (CustomListView) findViewById(R.id.devices_customlistview);
        devices_customlistview.setDividerWidth(20);
        devices_customlistview.setDividerHeight(20);
        findViewById(R.id.devices_transfer_lyt).setVisibility(View.GONE);
        findViewById(R.id.devices_waiter_lyt).setVisibility(View.GONE);
    }

    private void registEvent() {

        btn_back.setOnClickListener(this);
        ll_print.setOnClickListener(this);
        devices_printe_lyt.setOnClickListener(this);
        devices_transfer_lyt.setOnClickListener(this);
        devices_waiter_lyt.setOnClickListener(this);

        hv_printer_group.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                dex = position;
                deviceGroupAdapter.setSelectIndex(position);
                deviceGroupAdapter.notifyDataSetChanged();
                devices_customlistview.removeAllViews();
                List<PrinterDevice> devices = new ArrayList<PrinterDevice>();
                Map<Integer, List<PrinterDevice>> map = App.instance.getMap();
                if (map.size() > 0) {
                    if (map.containsKey(printerDeptModelList.get(position).getId())) {
                        devices = map.get(printerDeptModelList.get(position).getId());
                    } else {
                        List<PrinterDevice> list = map.get(-1);
                        if (list != null) {
                            for (PrinterDevice device : list) {
                                PrinterDevice printerDevice = new PrinterDevice();
                                printerDevice.setDevice_id(-1);
                                printerDevice.setName(device.getName());
                                printerDevice.setGroupId(device.getGroupId());
                                printerDevice.setIP(device.getIP());
                                printerDevice.setMac(device.getMac());
                                printerDevice.setModel(device.getModel());

                                if (device.getIP().indexOf(":") != -1) {
                                    printerDevice.setType("2");
                                } else {
                                    printerDevice.setType("1");
                                }
                                devices.add(printerDevice);
                            }
                        }
                    }
                }
                if (adapter != null) {
                    adapter.setList(devices, 1);
                } else {
                    adapter = new DevicesAdapter(DevicesActivity.this, devices, handler);
                    devices_customlistview.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initAnimation(float values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.device_view), "translationY",
                values);
        animator.setDuration(300);
        animator.start();
    }

    @Override
    public void httpRequestAction(int action, Object obj) {
//        switch (action) {
//            case ParamConst.HTTP_REQ_CALLBACK_KDS_PAIRED:
//                if (obj != null) {
//                    UIHelp.showToast(this, context.getResources().getString(R.string.kds_paired));
//                    handler.sendEmptyMessage(ACTION_NEW_KDS_ADDED);
//                }
//                break;
//            case ParamConst.HTTP_REQ_CALLBACK_WAITER_PAIRED:
//                if (obj != null) {
//                    UIHelp.showToast(this, context.getResources().getString(R.string.waiter_paired));
//                    handler.sendEmptyMessage(ACTION_NEW_WAITER_ADDED);
//                }
//                break;
//            case ParamConst.HTTP_REQ_REFRESH_KDS_PAIRED:
//                handler.sendEmptyMessage(JavaConnectJS.ACTION_NEW_KDS_ADDED);
//                break;
//            case ParamConst.HTTP_REQ_REFRESH_WAITER_PAIRED:
//                handler.sendEmptyMessage(JavaConnectJS.ACTION_NEW_WAITER_ADDED);
//                break;
//        }
    }


    @Override
    protected void onPause() {
        App.instance.closeDiscovery();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        App.instance.closeDiscovery();
        super.onDestroy();
    }
}
