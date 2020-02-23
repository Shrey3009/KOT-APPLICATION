package com.alfredposclient.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alfred.remote.printservice.RemotePrintServiceCallback;
import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.LocalDevice;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.WaiterDevice;
import com.alfredbase.utils.JSONUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.SystemUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.JavaConnectJS;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.global.WebViewConfig;
import com.alfredposclient.popupwindow.SelectPrintWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicesHtml extends BaseActivity {
	public static final int ADD_PRINTER_DEVICE = 1;
	private WebView web;
	private JavaConnectJS javaConnectJS;
	private SelectPrintWindow selectPrintWindow;
	private Gson gson = new Gson();
	final static String TAG = "DevicesHtml";

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_common_web);
		List<Printer> kdss = CoreData.getInstance().getKDSPhysicalPrinters();
		List<LocalDevice> localDevices = CoreData.getInstance().getLocalDevices();
		List<Printer> phys = CoreData.getInstance().getPhysicalPrinters();
		List<PrinterGroup> groups = CoreData.getInstance().getPrinterGroups();
		List<Printer> printers = CoreData.getInstance().getPrinters();
		PrinterDevice device = App.instance.getCahierPrinter();

		web = (WebView) findViewById(R.id.web);
		selectPrintWindow = new SelectPrintWindow(context, findViewById(R.id.rl_root),handler);
		WebViewConfig.setDefaultConfig(web);
		javaConnectJS = new JavaConnectJS() {
			@Override
			@JavascriptInterface
			public void send(String action, String param) {
				if (JavaConnectJS.LOAD_DEVICES.equals(action)) {
					//Get device name configured at backend
					if (!TextUtils.isEmpty(param)) {
						handler.sendMessage(handler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_DEVICES, param));
					}
				}
				if(JavaConnectJS.ADD_PRINTER_DEVICE.equals(action)){
					if(!TextUtils.isEmpty(param)){
						handler.sendMessage(handler.obtainMessage(
								JavaConnectJS.ACTION_ADD_PRINTER_DEVICE, param));
					}
				}
				if (JavaConnectJS.LOAD_KDS_DEVICES.equals(action)) {
					if (!TextUtils.isEmpty(param)) {
						handler.sendMessage(handler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_KDS_DEVICES, param));
					}					
				}
				if (JavaConnectJS.LOAD_PRINTERS_DEVICES.equals(action)) {
					if (!TextUtils.isEmpty(param)) {
						handler.sendMessage(handler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_PRINTERS_DEVICES, param));
					}					
				}
				if (JavaConnectJS.LOAD_WAITER_DEVICES.equals(action)) {
					if (!TextUtils.isEmpty(param)) {
						handler.sendMessage(handler.obtainMessage(
								JavaConnectJS.ACTION_LOAD_WAITER_DEVICES, param));
					}					
				}				
				if (JavaConnectJS.CLICK_DISCOVER_PRINTERS.equals(action)) {
					refreshPrinters();
				}
				if (JavaConnectJS.CLICK_BACK.equals(action)) {
					handler.sendEmptyMessage(JavaConnectJS.ACTION_CLICK_BACK);
				}
				if (JavaConnectJS.ASSIGN_PRINTER_DEVICE.equals(action)) {
					handler.sendMessage(handler.obtainMessage(
							JavaConnectJS.ACTION_ASSIGN_PRINTER_DEVICE, param));
				}
				if (JavaConnectJS.UNASSIGN_PRINTER_DEVICE.equals(action)) {
					handler.sendMessage(handler.obtainMessage(
							JavaConnectJS.ACTION_UNASSIGN_PRINTER_DEVICE, param));
				}
			}
		};
		web.addJavascriptInterface(javaConnectJS, "JavaConnectJS");
		if(SystemUtil.isZh(context))
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "setting_device_zh.html");
		else
			web.loadUrl(WebViewConfig.ROOT_DIRECTORY + "setting_device.html");
	}

	private String getDevicesConfigureJsonStr() {
		Map<String, Object> map = new HashMap<String, Object>();
	
		map.put("kds", CoreData.getInstance().getKDSPhysicalPrinters());
		map.put("printers", CoreData.getInstance().getPhysicalPrinters());
		map.put("mainPosInfo", App.instance.getMainPosInfo());

		Gson gson = new Gson();
		String str = gson.toJson(map);
		Log.d(TAG, str);
		return JSONUtil.getJSONFromEncode(str);
	}
	
	private void refreshPrinters() {
		if (App.instance.countryCode == ParamConst.CHINA) {
			handler.sendEmptyMessage(RemotePrintServiceCallback.PRINTERS_DISCOVERIED);
		}else {
		   App.instance.discoverPrinter(handler);
		}
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JavaConnectJS.ACTION_LOAD_DEVICES:{
					String param = (String) msg.obj;
					web.loadUrl("javascript:JsConnectAndroid('" + JSONUtil.getJSCallBackName(param) + "','"
							+ getDevicesConfigureJsonStr() + "')");
				}
				break;
			case JavaConnectJS.ACTION_NEW_KDS_ADDED:{
					Gson gson = new Gson();
					KDSDevice kds = (KDSDevice)msg.obj;
					String kdsstr = gson.toJson(kds);
					web.loadUrl("javascript:JsConnectAndroid('NewKdsDeviceAdded','"
							+ JSONUtil.getJSONFromEncode(kdsstr) + "')");				
				}
				break;
			case JavaConnectJS.ACTION_LOAD_KDS_DEVICES:{
				   String param = (String) msg.obj;
				   Gson gson = new Gson();
				   Map<Integer, KDSDevice> data = App.instance.getKDSDevices();
				   List<KDSDevice> kds = new ArrayList<KDSDevice>();
				   for (Map.Entry<Integer, KDSDevice> entry : data.entrySet())
				   {
					   kds.add(entry.getValue());
				   }
				   String kdsstr = gson.toJson(kds);
				   web.loadUrl("javascript:JsConnectAndroid('" + JSONUtil.getJSCallBackName(param) + "','"
							+ JSONUtil.getJSONFromEncode(kdsstr) + "')");					   
				}
				break;
				
			case JavaConnectJS.ACTION_LOAD_WAITER_DEVICES: {
				   String param = (String) msg.obj;
				   Gson gson = new Gson();
				   Map<Integer, WaiterDevice> data = App.instance.getWaiterDevices();
				   List<WaiterDevice> wt = new ArrayList<WaiterDevice>();
				   for (Map.Entry<Integer, WaiterDevice> entry : data.entrySet())
				   {
					   wt.add(entry.getValue());
				   }				   
				   String kdsstr = gson.toJson(wt);
				   web.loadUrl("javascript:JsConnectAndroid('" + JSONUtil.getJSCallBackName(param) + "','"
							+ JSONUtil.getJSONFromEncode(kdsstr) + "')");					
				}
				break;
			case JavaConnectJS.ACTION_LOAD_PRINTERS_DEVICES: {
				   App.instance.discoverPrinter(handler);
				   handler.sendEmptyMessage(RemotePrintServiceCallback.PRINTERS_DISCOVERIED);
				}
				break;
			case JavaConnectJS.ACTION_NEW_WAITER_ADDED:{
				Gson gson = new Gson();
				WaiterDevice waiter = (WaiterDevice)msg.obj;
				String kdsstr = gson.toJson(waiter);
				web.loadUrl("javascript:JsConnectAndroid('NewWaiterDeviceAdded','"
						+ JSONUtil.getJSONFromEncode(kdsstr) + "')");
			}
			break;				
			case JavaConnectJS.ACTION_CLICK_BACK:
				DevicesHtml.this.finish();
				break;
			case RemotePrintServiceCallback.PRINTERS_DISCOVERIED: {
					Gson gson = new Gson();

					List<PrinterDevice> allprinters = new ArrayList<PrinterDevice>();
					//all assigned printers in POS.
					Map<Integer, PrinterDevice> data = App.instance.getPrinterDevices();
					for (Map.Entry<Integer, PrinterDevice> entry : data.entrySet())
					{
						PrinterDevice assigned = entry.getValue();
						allprinters.add(entry.getValue());
					}
					
					//all printers in the network, discovery from Printer Driver
					Map<String, String> printersDiscovered = (Map<String, String>) msg.obj;
					if (printersDiscovered!=null) {
						for (Map.Entry<String, String> entry : printersDiscovered.entrySet())
						{
							PrinterDevice tmppt = new PrinterDevice();
							tmppt.setIP(entry.getKey());
							tmppt.setName(entry.getValue());
							tmppt.setDevice_id(-1); //temp device: not assigned yet
							boolean existed = false;
							for (PrinterDevice item: allprinters) {
								if (item.getIP().equals(entry.getKey()))
									existed = true;
							}
							if (!existed)
							  allprinters.add(tmppt);
						}
					}
					
					String str = gson.toJson(allprinters);					
					web.loadUrl("javascript:JsConnectAndroid('RefreshPrinters','"
							+ JSONUtil.getJSONFromEncode(str) + "')");
				}
				break;
			//assign printer harware device
			case JavaConnectJS.ACTION_ASSIGN_PRINTER_DEVICE:{
					/* param
					{"printerName":printname,
		                   'printerIp':printip, 
		                   'assignTo':SELECTED_LEVEL_2,
		                   'js_callback':xxx};
		             */

				    Gson gson= new Gson();
					Map<String, String> printer = (Map<String, String>) gson.fromJson((String) msg.obj, 
															new TypeToken<Map<String, String>>(){}.getType());
					if (printer != null) {
						String assignToName = printer.get("assignTo");
						if (assignToName != null && assignToName.length()>0) {
							Printer prt = CoreData.getInstance().getPrinterByName(assignToName);
							if (prt!=null) {
					   		    //add to local device
								int cashDrawer = prt.getIsCashdrawer();
								String printerModel = printer.get("deviceName");
								String printerName = printer.get("printerName");
								LocalDevice localDevice = ObjectFactory.getInstance().getLocalDevice(assignToName, printerModel,
																				   		    			ParamConst.DEVICE_TYPE_PRINTER,
																				   		    			prt.getId(), 
																				   		    			printer.get("printerIp"),
																				   		    			"", printerName,-1);
					   		    CoreData.getInstance().addLocalDevice(localDevice);
					   		    App.instance.loadPrinters();
					   		    //add to printer
//					   		    PrinterDevice prtDev = new PrinterDevice();
//					   		    prtDev.setDevice_id(prt.getId());
//					   		    prtDev.setIP(printer.get("printerIp"));
//					   		    prtDev.setIsCahierPrinter(cashDrawer);
//					   		    prtDev.setMac(localDevice.getMacAddress());
//					   		    prtDev.setModel(printerModel);
//					   		    prtDev.setName(assignToName);
//					   		    App.instance.setPrinterDevice(prt.getId(), prtDev);
							}
						}
					}

					refreshPrinters();
					
			    }
				break;
			case JavaConnectJS.ACTION_UNASSIGN_PRINTER_DEVICE:{
//				{"localDevId":1,
//	                   'js_callback':onUnassignComplete};	
					Gson gson = new Gson();
					boolean success = false;

					@SuppressWarnings("unchecked")
					Map<String, String> param = (Map<String, String>) gson.fromJson((String) msg.obj, 
							new TypeToken<Map<String, String>>(){}.getType());					
					Integer localDevId = Integer.valueOf(param.get("deviceId"));
					if (localDevId>0) {
                            LocalDevice ldItem = CoreData.getInstance().getLocalDeviceByDeviceIdAndIP(localDevId,param.get("printerIp"));
				   		    App.instance.removePrinterDevice(localDevId);
                            if (ldItem!=null) {
						   		//remove from local device
					   		    CoreData.getInstance().removeLocalDeviceByDeviceIdAndIP(localDevId, param.get("printerIp"));
					   		    //remove printer
					   		    success = true;
                            }
					}
					refreshPrinters();
			}
				break;
			case JavaConnectJS.ACTION_ADD_PRINTER_DEVICE:
				/* param
				{'assignTo':SELECTED_LEVEL_2,
	                   'js_callback':xxx};
	             */
			 	@SuppressWarnings("unchecked")
				Map<String, String> printer = (Map<String, String>) gson.fromJson((String) msg.obj, 
						new TypeToken<Map<String, String>>(){}.getType());
				if (printer != null) {
					String assignToName = printer.get("assignTo");
					String js_callback = printer.get("js_callback");
					LogUtil.i(TAG, "assignToName:" + assignToName + ",js_callback:" + js_callback);
					if (assignToName !=null && assignToName.length() > 0 
							&& js_callback != null && js_callback.length() > 0) {
						selectPrintWindow.show(assignToName,js_callback);
					}else {
						UIHelp.showToast(context, context.getResources().getString(R.string.not_operations));
					}
				}
//				selectPrintWindow.show("cashier","RefreshPrinters");//TEST DATA
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
	}
	
	@Override
	public void httpRequestAction(int action,Object obj){
		switch (action) {
			case ParamConst.HTTP_REQ_CALLBACK_KDS_PAIRED:
		   		if (obj != null) {
		   			UIHelp.showToast(this, context.getResources().getString(R.string.kds_paired));
		   		    KDSDevice kds = (KDSDevice) obj;
		   		    handler.sendMessage(handler.obtainMessage(JavaConnectJS.ACTION_NEW_KDS_ADDED, kds));
		   		}
		   		break;
		   		
		   	case ParamConst.HTTP_REQ_CALLBACK_WAITER_PAIRED:
		   		if (obj != null) {
		   			UIHelp.showToast(this, context.getResources().getString(R.string.waiter_paired));
			   		WaiterDevice wds = (WaiterDevice)obj; 
		   		    handler.sendMessage(handler.obtainMessage(JavaConnectJS.ACTION_NEW_WAITER_ADDED, wds));
		   		}
		   		break;
		   	case ParamConst.HTTP_REQ_REFRESH_KDS_PAIRED:
		   		String kdsParam = "{\"js_callback\":\"RefreshKdsDevices\"}";
		   		handler.sendMessage(handler.obtainMessage(
						JavaConnectJS.ACTION_LOAD_KDS_DEVICES, kdsParam));
		   		break;
		   	case ParamConst.HTTP_REQ_REFRESH_WAITER_PAIRED:
		   		String waiterParam = "{\"js_callback\":\"RefreshWaiterDevices\"}";
		   		handler.sendMessage(handler.obtainMessage(
		   				JavaConnectJS.ACTION_LOAD_WAITER_DEVICES, waiterParam));
		   		break;
			}
	}

}
