package com.alfred.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alfredbase.utils.SystemUtil;
import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.epson.eposprint.Print;

import java.util.ArrayList;

public class EPSONPrinter {

    /**
     *
     */
    public static final int FONT_A = Builder.FONT_A;
    public static final int FONT_B = Builder.FONT_B;
    public static final int FONT_C = Builder.FONT_C;

    public static final int LANG_EN = Builder.LANG_EN;
    public static final int LANG_CN_ZH = Builder.LANG_ZH_CN;
    public static final int LANG_CN_TW = Builder.LANG_ZH_TW;

    public final static int MSG_PRINTER_FOUND = 1;
    public final static boolean TXT_UNDERLINE = true;

    Context context;

    private int deviceType; //0: Thermal, 1: others
    private String ip;
    private String printerName;
    private int language;
    Print printer;

    //status monitor
    private int monitor = Print.TRUE;
    private int interval = Print.PARAM_DEFAULT;
    private int charSizeFontA = 48;

    //data
    private ArrayList<PrintData> data;

    public EPSONPrinter(Context context) {
        this.context = context;
        this.printer = new Print(context);
        this.data = new ArrayList<PrintData>();
    }

    public EPSONPrinter(Context context, int deviceType, String ip, String printerName) {

        this.deviceType = deviceType;
        this.ip = ip;
        this.printerName = printerName;

        this.context = context;
        this.data = new ArrayList<PrintData>();

        this.printer = new Print(context);
    }

    public ArrayList<String> discovery() {

        return null;
    }

    public boolean open() {
        boolean result = false;
        try {
            this.printer.openPrinter(this.deviceType, this.ip, this.monitor, this.interval);
            result = addPrintJob();
        } catch (EposException e) {
            int errStatus = e.getErrorStatus();
            printer = null;
        }
        return result;
    }

    public int checkStatus() {

        return 0;
    }

    public boolean addPrintJob() {
        boolean result = false;
        int[] status = new int[1];
        int[] battery = new int[1];
        String data = "GS(A";
        Builder builder = null;

        //Create a print document
        try {
            builder = new Builder("TM-T88V", Builder.MODEL_SOUTHASIA, this.context);
            builder.clearCommandBuffer();
            builder.addTextSmooth(Builder.TRUE);
            builder.addTextFont(Builder.FONT_A);
        } catch (EposException e9) {
            // TODO Auto-generated catch block
            e9.printStackTrace();
        }


        for (int i = 0; i < this.data.size(); i++) {

            PrintData toPrint = this.data.get(i);
            int isUnderline = Builder.FALSE;
            if (toPrint.isUnderline())
                isUnderline = Builder.TRUE;
            //format
            if (toPrint.getLanguage() == PrintData.LANG_CN) {
                try {
                    builder.addTextLang(Builder.LANG_ZH_CN);
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    if (SystemUtil.isZh(context)) {
                        builder.addTextLang(Builder.LANG_ZH_CN);
                    } else {
                        builder.addTextLang(Builder.LANG_EN);
                    }
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (toPrint.getTextBold() != -1)
                try {
                    builder.addTextStyle(Builder.FALSE, isUnderline,
                            Builder.TRUE, Builder.PARAM_UNSPECIFIED);
                } catch (EposException e8) {
                    // TODO Auto-generated catch block
                    e8.printStackTrace();
                }
            else
                try {
                    builder.addTextStyle(Builder.FALSE, isUnderline,
                            Builder.FALSE, Builder.PARAM_UNSPECIFIED);
                } catch (EposException e7) {
                    // TODO Auto-generated catch block
                    e7.printStackTrace();
                }

            if (toPrint.getFontsize() == -1)
                try {
                    builder.addTextSize(1, 1);
                } catch (EposException e6) {
                    // TODO Auto-generated catch block
                    e6.printStackTrace();
                }
            else
                try {
                    builder.addTextSize(toPrint.getFontsize(), toPrint.getFontsize());
                } catch (EposException e5) {
                    // TODO Auto-generated catch block
                    e5.printStackTrace();
                }

            if (toPrint.getTextAlign() == PrintData.ALIGN_RIGHT)
                try {
                    builder.addTextAlign(Builder.ALIGN_RIGHT);
                } catch (EposException e4) {
                    // TODO Auto-generated catch block
                    e4.printStackTrace();
                }
            else if (toPrint.getTextAlign() == PrintData.ALIGN_CENTRE)
                try {
                    builder.addTextAlign(Builder.ALIGN_CENTER);
                } catch (EposException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
                }
            else
                try {
                    builder.addTextAlign(Builder.ALIGN_LEFT);
                } catch (EposException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }

            if (toPrint.getMarginTop() != -1)
                try {
                    builder.addFeedUnit(toPrint.getMarginTop());
                } catch (EposException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            //content
            if (toPrint.getDataFormat() == PrintData.FORMAT_TXT) {
                try {
                    builder.addText(toPrint.getText());
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (toPrint.getDataFormat() == PrintData.FORMAT_IMG) {
                byte bitmapBytes[] = toPrint.getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                try {
                    builder.addImage(bitmap, 0, 0, Math.min(512, bitmap.getWidth()), bitmap.getHeight(), Builder.COLOR_1,
                            Builder.MODE_MONO, Builder.HALFTONE_DITHER, 1.0);
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (toPrint.getDataFormat() == PrintData.FORMAT_CUT) {
                //builder.addFeedLine(5);
                try {
                    builder.addCut(Builder.CUT_FEED);
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (toPrint.getDataFormat() == PrintData.FORMAT_QR) {
                try {
                    builder.addSymbol(
                            toPrint.getText(),
                            Builder.SYMBOL_QRCODE_MODEL_2,
                            Builder.LEVEL_H, 255,
                            255, 2018);
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (toPrint.getDataFormat() == PrintData.FORMAT_DRAWER) {
                try {
                    builder.addPulse(Builder.PARAM_DEFAULT, Builder.PULSE_200);
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (toPrint.getDataFormat() == PrintData.FORMAT_QR_BITMAP) {
                try {
                    byte[] b =toPrint.getQrCodeBitmap();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0, b.length);
                    builder.addImage(bitmap, 0, 0, Math.min(512, bitmap.getWidth()), bitmap.getHeight(), Builder.COLOR_1,
                            Builder.MODE_MONO, Builder.HALFTONE_DITHER, 1.0);
                } catch (EposException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        try {
            Builder conBuilder = new Builder("TM-T88V", Builder.MODEL_CHINESE, this.context);
            printer.sendData(conBuilder, 10000, status);
            if ((status[0] & Print.ST_OFF_LINE) != Print.ST_OFF_LINE) {
                int timeout = 10000;
                printer.sendData(builder, timeout, status);
                result = true;
            } else {
                result = false;
            }
        } catch (EposException e) {
            // TODO Auto-generated catch block
            int err = e.getErrorStatus();
            result = false;
            if (err == EposException.ERR_TIMEOUT)
                result = true;
            else
                e.printStackTrace();

        }

        try {
            printer.closePrinter();
        } catch (EposException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public void addContent(PrintData data) {
        if (data != null)
            this.data.add(data);
    }

    public ArrayList<PrintData> getData() {
        return data;
    }

    public void setData(ArrayList<PrintData> data) {
        this.data = data;
    }

}
