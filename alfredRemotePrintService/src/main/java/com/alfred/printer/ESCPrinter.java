package com.alfred.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.alfred.print.jobs.WifiCommunication;
import com.alfred.remote.printservice.App;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.WIFIPrintCallback;
import com.alfredbase.utils.BitmapUtil;
import com.alfredbase.utils.MachineUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.content.ContentValues.TAG;


public class ESCPrinter implements WIFIPrintCallback {

    /**
     */
//	public static final int FONT_A = ESCPOSPrinter.FONT_A;
//	public static final int FONT_B = ESCPOSPrinter.FONT_B;
//	public static final int FONT_C = ESCPOSPrinter.FONT_C;
//
//
//	public final static int MSG_PRINTER_FOUND = 1;
//	public final static boolean TXT_UNDERLINE = true;

    Context context;

    private String ip;
    ESCPOSPrinter printer;

    TscPOSPrinter tprinter;

    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;

    //data
//	private ArrayList<PrintData> data;

    private boolean connected = false;

    WifiCommunication wfComm = null;

    public static final int DEFAULT_PORT = 9100;


    public ESCPrinter(String ip) {

//		hdl.setPrinterCbk(this);

        this.ip = ip;

        this.printer = new ESCPOSPrinter((PrintService) context);
    }

    public ESCPrinter(String ip, int labe) {

        this.ip = ip;

        this.tprinter = new TscPOSPrinter((PrintService) context);


    }

    public ArrayList<String> discovery() {

        return null;
    }

    public boolean reconnect() {
        return open();
    }

    public boolean open() {
        if (wfComm == null) {
            wfComm = new WifiCommunication();
        }
        return wfComm.initSocket(ip, DEFAULT_PORT);
    }

    public boolean isConnected() {
        boolean ret = false;
        if (wfComm != null) {
            ret = wfComm.isConnected();
        }
        return ret;
    }


    //tested
    public synchronized void close() {
        Log.d(TAG, "printer (" + ip + ") close");
        if (wfComm != null) {
            wfComm.close();
            wfComm = null;
        }
        this.connected = false;
    }


//	public boolean ping(){
//		Boolean connected = NetUtil.ping(ip);
//		return connected;
//	}

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void onConnected() {
        this.connected = true;
    }

//	public  void checkStatus()  throws Exception{
//		byte[] tcmd = new byte[3];
//		tcmd[0] = 0x10;
//		tcmd[1] = 0x04;
//		tcmd[2] = 0x04;
//		boolean result = wfComm.checkStatus(tcmd);
//		this.printer.getOut().reset();
////		wfComm.close();
//		if (!result) {
//			throw new RuntimeException("Print action Failed");
//		}
//	}


    public boolean setUSBData(List<PrintTscData> data, int direction, boolean isTian) {
        boolean result = true;
        try {

            this.tprinter.addHome();
            //	this.tprinter.resetPrinter();

            if (isTian) {
                this.tprinter.addTsize(35, 25, 1);//设置打印区域大小
            } else {
                this.tprinter.addTsize(40, 30, 1);//设置打印区域大小
            }
            this.tprinter.addReference(0, 0); // 设置原点坐标
            this.tprinter.addSpeed();// 设置打印速度
            this.tprinter.addDensity();// 设置打印浓度
            this.tprinter.addDirection(direction);// 设置打印方向
            this.tprinter.addCls();// 清除打印缓冲区
            for (int i = 0; i < data.size(); i++) {
                PrintTscData toPrint = data.get(i);

                if (toPrint.getDataFormat() == PrintTscData.FORMAT_RESET) {
                    this.tprinter.addPrint();

                    if (i < data.size() - 1) {
                        sendUsbData();

                        this.tprinter.addHome();
                        if (isTian) {
                            this.tprinter.addTsize(35, 25, 1);//设置打印区域大小
                        } else {
                            this.tprinter.addTsize(40, 30, 1);//设置打印区域大小
                        }
                        this.tprinter.addReference(0, 0); // 设置原点坐标
                        this.tprinter.addSpeed();// 设置打印速度
                        this.tprinter.addDensity();// 设置打印浓度
                        this.tprinter.addDirection(direction);// 设置打印方向
                        this.tprinter.addCls();// 清除打印缓冲区

                    } else {
                        sendUsbData();

                    }

                } else {

                    if (toPrint.getDataFormat() == PrintTscData.FORMAT_BAR) {
                        this.tprinter.addBar(toPrint.getX(), toPrint.getY(), 500, 5);
                    } else if (toPrint.getDataFormat() == PrintTscData.FORMAT_TXT) {
                        this.tprinter.addText(toPrint.getX(), toPrint.getY(), toPrint.getFontsizeX(), toPrint.getFontsizeY(), toPrint.getText());
                    }
                }
            }


            //	this.tprinter.resetPrinter();
//

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            this.close();
            result = false;
            return result;
        }


        try {
            //kickdrawer no need wait for a long time

            if (data.size() < 2) {
                Thread.sleep(100);
            } else if (data.size() < 40) {
                Thread.sleep(1000);
            } else {
                Thread.sleep(data.size() * 40);
            }

            close();
            Thread.sleep(200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public boolean setTscData(List<PrintTscData> data, int direction, Boolean isTian) {
        boolean result = true;
        try {

            this.tprinter.addHome();
            //	this.tprinter.resetPrinter();
            if (isTian) {
                this.tprinter.addTsize(35, 25, 1);//设置打印区域大小
            } else {
                this.tprinter.addTsize(40, 30, 1);//设置打印区域大小
            }


            this.tprinter.addReference(0, 0); // 设置原点坐标
            this.tprinter.addSpeed();// 设置打印速度
            this.tprinter.addDensity();// 设置打印浓度
            this.tprinter.addDirection(direction);// 设置打印方向
            this.tprinter.addCls();// 清除打印缓冲区
            for (int i = 0; i < data.size(); i++) {
                PrintTscData toPrint = data.get(i);

                if (toPrint.getDataFormat() == PrintTscData.FORMAT_RESET) {
                    this.tprinter.addPrint();

                    if (i < data.size() - 1) {
                        sendTData();

                        this.tprinter.addHome();
                        if (isTian) {
                            this.tprinter.addTsize(35, 25, 1);//设置打印区域大小
                        } else {
                            this.tprinter.addTsize(40, 30, 1);//设置打印区域大小
                        }

                        this.tprinter.addReference(0, 0); // 设置原点坐标
                        this.tprinter.addSpeed();// 设置打印速度
                        this.tprinter.addDensity();// 设置打印浓度
                        this.tprinter.addDirection(direction);// 设置打印方向
                        this.tprinter.addCls();// 清除打印缓冲区

                    } else {
                        sendTData();
                    }

                } else {

                    if (toPrint.getDataFormat() == PrintTscData.FORMAT_BAR) {

                        this.tprinter.addBar(toPrint.getX(), toPrint.getY(), 500, 5);

                    } else if (toPrint.getDataFormat() == PrintTscData.FORMAT_TXT) {
                        this.tprinter.addText(toPrint.getX(), toPrint.getY(), toPrint.getFontsizeX(), toPrint.getFontsizeY(), toPrint.getText());
                    }
                }
            }


            //	this.tprinter.resetPrinter();
//

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            this.close();
            result = false;
            return result;
        }


        try {
            //kickdrawer no need wait for a long time

            if (data.size() < 2) {
                Thread.sleep(100);
            } else if (data.size() < 40) {
                Thread.sleep(1000);
            } else {
                Thread.sleep(data.size() * 40);
            }

            close();
            Thread.sleep(200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


    public void UsbPrint(final byte[] bytes) {
        UsbDevice mUsbDevice = null;
        String[] temp = null;
        temp = ip.split(",");

        String vendorId = temp[0];
        String productId = temp[1];

        mUsbManager = (UsbManager) App.instance.getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Log.d("UsbPrint", " -----" + deviceList.size());

        for (UsbDevice device : deviceList.values()) {
            if (device.getProductId() == Integer.valueOf(productId).intValue() && device.getVendorId() == Integer.valueOf(vendorId).intValue()) {
                mUsbDevice = device;
            }

        }


        if (mUsbDevice != null) {
            UsbInterface usbInterface = mUsbDevice.getInterface(0);
            for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
                final UsbEndpoint ep = usbInterface.getEndpoint(i);
                if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                        mUsbDeviceConnection = mUsbManager.openDevice(mUsbDevice);
                        if (mUsbDeviceConnection != null) {
                            //Toast.makeText(mContext, "Device connected", Toast.LENGTH_SHORT).show();
                            mUsbDeviceConnection.claimInterface(usbInterface, true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    int b = mUsbDeviceConnection.bulkTransfer(ep, bytes, bytes.length, 100000);
                                    Log.i("Return Status", "b-->" + b);
                                }
                            }).start();


                            mUsbDeviceConnection.releaseInterface(usbInterface);
                            break;
                        }
                    }
                }
            }
        } else {
            //		Toast.makeText(mContext, "No available USB print device", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean setData(List<PrintData> data) {
        boolean result = true;
        boolean isKickDrawer = false;
        try {
//			checkStatus();
            this.printer.reset();

            for (int i = 0; i < data.size(); i++) {
                PrintData toPrint = data.get(i);
                byte isUnderline = ESCPOSPrinter.UNDERLINE_NONE;
                if (toPrint.isUnderline())
                    isUnderline = ESCPOSPrinter.UNDERLINE_SINGLE;

                if (toPrint.getTextBold() != -1) {
                    this.printer.setBold((byte) 1);
                } else {
                    this.printer.setBold((byte) 0);
                }

                if (toPrint.getFontsize() == -1)
                    this.printer.setFontSize(1);
                else
                    this.printer.setFontSize(toPrint.getFontsize());

                if (toPrint.getTextAlign() == PrintData.ALIGN_RIGHT)
                    this.printer.setJustification(ESCPOSPrinter.JUSTIFY_RIGHT);
                else if (toPrint.getTextAlign() == PrintData.ALIGN_CENTRE)
                    this.printer.setJustification(ESCPOSPrinter.JUSTIFY_CENTER);
                else
                    this.printer.setJustification(ESCPOSPrinter.JUSTIFY_LEFT);

                if (toPrint.getDataFormat() == PrintData.FORMAT_FEED) {
                    if (toPrint.getMarginTop() != -1)
                        this.printer.feed((byte) toPrint.getMarginTop());
                }

                if (toPrint.getDataFormat() == PrintData.FORMAT_SING) {
                    this.printer.sing();
                }

                //content
                if (toPrint.getDataFormat() == PrintData.FORMAT_TXT) {
                    this.printer.printText(toPrint.getText());

                } else if (toPrint.getDataFormat() == PrintData.FORMAT_IMG) {
                    byte bitmapBytes[] = toPrint.getImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                    this.printer.printBitmap(bitmap);
                } else if (toPrint.getDataFormat() == PrintData.FORMAT_CUT) {
                    this.printer.cut();
                } else if (toPrint.getDataFormat() == PrintData.FORMAT_QR) {
                    String qrCode = toPrint.getQrCode();
                    qrCode = URLEncoder.encode(qrCode, "UTF-8");
                    Map<EncodeHintType, ErrorCorrectionLevel> map = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                    map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
                    QRCodeWriter writer = new QRCodeWriter();
                    BitMatrix matrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, 500, 500, map);
                    Bitmap bitmap = BitmapUtil.bitMatrix2Bitmap(matrix);
                    this.printer.printQRBitmap(bitmap);
                } else if (toPrint.getDataFormat() == PrintData.FORMAT_QR_BITMAP) {
                    byte[] b =toPrint.getQrCodeBitmap();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0, b.length);
                    this.printer.printQRBitmap(bitmap);
                } else if (toPrint.getDataFormat() == PrintData.FORMAT_DRAWER) {
                    if (ip.equals(WifiCommunication.localIPAddress)) {
                        if(MachineUtil.isHisense()){
                            this.printer.kickDrawerForHisense();
                        }else {
                            this.printer.kickDrawerForSunmi();
                        }
                    } else {
                        this.printer.kickDrawer();
                    }

                    isKickDrawer = true;
                }
            }
            sendData();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            this.close();
            result = false;
            return result;
        }
        //printer.close();
        try {
            //kickdrawer no need wait for a long time

            if (data.size() < 2 && isKickDrawer) {
                Thread.sleep(100);
            } else if (data.size() < 40) {
                Thread.sleep(1000);
            } else {
                Thread.sleep(data.size() * 40);
            }

            close();
            Thread.sleep(200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public void sendTData() {

        this.tprinter.resetPrinter();
        byte[] b = ByteTo_byte(this.tprinter.getCommand());
        boolean result = wfComm.sndByte(b);
        this.tprinter.clrCommand();
        try {
            Thread.sleep(400);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.close();
        if (!result) {
            throw new RuntimeException("Print action Failed");
        }
    }

    public void sendUsbData() {

        this.tprinter.resetPrinter();
        byte[] b = ByteTo_byte(this.tprinter.getCommand());
        UsbPrint(b);
        //this.close();
        this.tprinter.clrCommand();
        try {
            Thread.sleep(400);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] ByteTo_byte(Vector<Byte> vector) {
        int len = vector.size();
        byte[] data = new byte[len];

        for (int i = 0; i < len; ++i) {
            data[i] = ((Byte) vector.get(i)).byteValue();
        }

        return data;
    }

    public void sendData() {
        boolean result = wfComm.sndByte(this.printer.getOut().toByteArray());
        //boolean	result=true;
        this.printer.getOut().reset();
//		wfComm.close();
        if (!result) {
            throw new RuntimeException("Print action Failed");
        }
    }

    @Override
    public void onDisconnected() {
        close();
        PrintService.instance.removePrinterByIP(this.ip);
    }

    @Override
    public void onSendFailed() {
        close();
        PrintService.instance.removePrinterByIP(this.ip);
    }

}
