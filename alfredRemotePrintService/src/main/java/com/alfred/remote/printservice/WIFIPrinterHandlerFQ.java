package com.alfred.remote.printservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alfred.print.jobs.WifiCommunication;
import com.alfred.printer.ESCPrinter;


public class WIFIPrinterHandlerFQ extends Handler {
	private String ip;
	private WIFIPrintCallback printerCbk ;
	private ESCPrinter printer;
    static final String TAG = "WIFIPrinterHandler";
	
	public WIFIPrinterHandlerFQ(String ip) {
		super(PrintService.instance.getMainLooper());
		this.ip = ip;
	}

	public void setPrinterCbk(WIFIPrintCallback cbk) {
		this.printerCbk = cbk;
	}
	@Override
	public void handleMessage(Message msg) {
      switch (msg.what) {
          case WifiCommunication.WFPRINTER_CONNECTED:
        	Log.d(TAG, ip + " Connected");
        	String aa = PrintService.instance.getString(R.string.wifi_printer_connect_success);
          	//Toast.makeText(PrintService.instance, aa, Toast.LENGTH_SHORT).show();
          	printerCbk.onConnected();

          	break;
          case WifiCommunication.WFPRINTER_DISCONNECTED:
          	Log.d(TAG, ip + " disconnected");
	        String ab = PrintService.instance.getString(R.string.wifi_printer_disconnect_success);

          	//Toast.makeText(PrintService.instance, ab,
            //          Toast.LENGTH_SHORT).show();
          	printerCbk.onDisconnected();
          	break;
          	
          case WifiCommunication.SEND_FAILED:
        	  Log.d(TAG, ip + " send failed");
		      String ac = PrintService.instance.getString(R.string.wifi_printer_send_failed);
		      printerCbk.onSendFailed();

          	break;
          case WifiCommunication.WFPRINTER_CONNECTEDERR:
        	  Log.d(TAG, ip + " connection error");        	  
		        String ad = PrintService.instance.getString(R.string.wifi_printer_connect_error);

        	   // Toast.makeText(PrintService.instance, ad,
               //       Toast.LENGTH_SHORT).show();
          	break;
          case 0x06:
		        String ae = PrintService.instance.getString(R.string.wifi_printer_nopaper);

        	  byte revData = (byte)Integer.parseInt(msg.obj.toString());
          	if(((revData >> 6) & 0x01) == 0x01)
          		Toast.makeText(PrintService.instance, ae,Toast.LENGTH_SHORT).show();    
              break;
          default:
              break;
      }
	}

	public ESCPrinter getPrinter() {
		return printer;
	}

	public void setPrinter(ESCPrinter printer) {
		this.printer = printer;
	}
}
