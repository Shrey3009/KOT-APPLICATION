package com.alfred.printer;

import android.content.Context;
import android.graphics.Bitmap;

import com.alfred.remote.printservice.PrintService;

import java.io.ByteArrayOutputStream;

public class ESCPOSPrinter {
    static private String TAG = "ESCPOSPrinter";
    /* ASCII codes */
    static byte  NUL = 0x00;
    static byte  LF = 0x0A;
    static byte  ESC = 0x1B;
    static byte  FS =  0x1C;
    static byte  GS =  0x1D;

    //barcode type
    static byte BARCODE_UPCA = 0;
    static byte  BARCODE_UPCE = 1;
    static byte  BARCODE_JAN13 = 2;
    static byte  BARCODE_JAN8 = 3;
    static byte  BARCODE_CODE39 = 4;
    static byte  BARCODE_ITF = 5;
    static byte  BARCODE_CODABAR = 6;

    /* Cut types */
    static byte CUT_FULL = 65;
    static byte CUT_PARTIAL = 66;

    /* Fonts */
    static public byte FONT_A = 0;
    static public byte FONT_B = 1;
    static public byte FONT_C = 2;

    /* Image sizing options */
    static byte IMG_DEFAULT = 0;
    static byte IMG_DOUBLE_WIDTH = 1;
    static byte IMG_DOUBLE_HEIGHT = 2;

    /* Justifications */
    static public byte JUSTIFY_LEFT = 0;
    static public byte JUSTIFY_CENTER = 1;
    static public byte JUSTIFY_RIGHT = 2;

    /* Print mode constants */
    static byte MODE_FONT_A = 0;
    static byte MODE_FONT_B = 1;
    static byte MODE_EMPHASIZED = 8;
    static byte MODE_DOUBLE_HEIGHT = 16;
    static byte MODE_DOUBLE_WIDTH = 32;
    static int  MODE_UNDERLINE = 128;

    /* Underline */
    static public byte UNDERLINE_NONE = 0;
    static public byte UNDERLINE_SINGLE = 1;
    static public byte UNDERLINE_DOUBLE = 2;

    private ByteArrayOutputStream out;

    int  connFlag = 0;
    //	revMsgThread revThred = null;
    Context mContext;
    //checkPrintThread cheThread = null;
    //alignment
    static int ALIGN_LEFT = -1;
    static int ALIGN_CENTER = 0;
    static int ALIGN_RIGHT = 1;
    private static final int WFPRINTER_REVMSG = 0x06;


    public ESCPOSPrinter(PrintService context) {
        super();
        this.mContext = context;
        out = new ByteArrayOutputStream();
    }


    public void reset()  throws Exception{
        byte[] tcmd = new byte[2];
        tcmd[0] = ESCPOSPrinter.ESC;
        tcmd[1] = 0x40;
        out.write(tcmd);
    }

    //tested
    public void setJustification (byte just) throws Exception{
        byte[] cmd = new byte[3];
        cmd[0] = ESCPOSPrinter.ESC;
        cmd[1] = 0x61;
        cmd[2] = just;
        out.write(cmd);
    }

    /**
     * Add text to the buffer.
     *
     * Text should either be followed by a line-break, or feed() should be called
     * after this to clear the print buffer.
     *
     * @param data $str Text to print
     */
    public void printText(String data) throws Exception{
//    	boolean result = true;
//    	checkStatus();
        if (data!=null&& data.length()>0) {
            byte[] byt = data.getBytes("gbk");
            out.write(byt);
        }

    }

    /*
     * Print buffer and next line
     * */
    public void printLF() throws Exception {
        byte[] tail = new byte[2];
        tail[0] = 0x0A;
        tail[1] = 0x0D;
        out.write(tail);
    }

    /* tested
     * FONT_A and FONT_B
     * */
    public void setFont(byte font) throws Exception {
        byte[] tcmd = new byte[3];
        tcmd[0] = ESCPOSPrinter.ESC;
        tcmd[1] = 0x4D;
        tcmd[2] = font;   //0.125mm line height
        out.write(tcmd);
    }

    /*
     * Tested
     * */
    public  void setFontSize(int size)  throws Exception {
        byte[] definedSize = {0x00, 0x11, 0x22, 0x33,0x44,0x55,0x66,0x77,0x01,0x10,0x02,0x20,0x12,0x21};
        byte[] cmd = new byte[3];
        cmd[0] = 0x1D;
        cmd[1] = 0x21;
//	    if (size == 2 ){
//			cmd[2] = 0x10;
//		}else
//		if(size == 3) {
//			cmd[2] = 0x12;
//		}else
        if (size>0&&size<definedSize.length){
            cmd[2] = definedSize[size-1];
        }else {
            cmd[2] = 0x00;
        }
        out.write(cmd);
    }

    //tested
    public  void setUnderline(byte underline) throws Exception {
        byte[] tcmd = new byte[3];
        tcmd[0] = ESCPOSPrinter.ESC;
        tcmd[1] = 0x2D;
        tcmd[2] = underline;   //0.125mm line height
        out.write(tcmd);
    }

    public  void setBold(byte bold)  throws Exception{
        byte[] tcmd = new byte[3];
        tcmd[0] = ESCPOSPrinter.ESC;
        tcmd[1] = 0x45;
        tcmd[2] = bold;
        out.write(tcmd);

    }

    //tested
    public  void feed(byte lines) throws Exception {
//		if(lines > 2){
//			lines = (byte)(lines / 2);
//		}
//		byte[] result = new byte[lines];
//		for (int i = 0; i < lines; i++) {
//			result[i] = ESCPOSPrinter.LF;
//		}
//		out.write(result);
        if (lines <=1) {
            byte[] tcmd = new byte[1];
            tcmd[0] = ESCPOSPrinter.LF;
            out.write(tcmd);
        }else{
            byte[] tcmd = new byte[3];
            tcmd[0] = ESCPOSPrinter.ESC;
            tcmd[1] = 0x64;
            tcmd[2] = lines;   //0.125mm line height
            out.write(tcmd);
        }

    }

    public void sing() throws Exception {
//		byte[] cutcmd = new byte[4];
//		cutcmd[0]=ESCPOSPrinter.ESC;
//		cutcmd[1]=0x42;
//		cutcmd[2]=0x3;
//		cutcmd[3]=0x5;
//		out.write(cutcmd);
        byte[] cutcmd = new byte[5];
        cutcmd[0]=ESCPOSPrinter.ESC;
        cutcmd[1]=0x43;
        cutcmd[2]=0x10;
        cutcmd[3]=0x10;
        cutcmd[4]=0x2;
        out.write(cutcmd);
    }

    //tested
//    public  boolean margin(byte mm) {
//	       byte[] tcmd = new byte[3];
//	       tcmd[0] = ESCPOSPrinter.ESC;
//	       tcmd[1] = 0x4A;
//	       tcmd[2] = 0;   //0.125mm line height
//	       //out.write(tcmd);
//	       return true;
//    }

    //tested
    public  void cut()  throws Exception  {
        byte[] cutcmd = new byte[4];
        cutcmd[0]=ESCPOSPrinter.GS;
        cutcmd[1]=0x56;
        cutcmd[2]=0x42;
        cutcmd[3]=90;
        out.write(cutcmd);
        this.feed((byte) 1);
    }

    //
    public  void kickDrawer()  throws Exception {
        byte[] tcmd = new byte[5];
        tcmd[0] = ESCPOSPrinter.ESC;
        tcmd[1] = 0x70;
        tcmd[2] = 0x00;
        tcmd[3] = 0x40;
        tcmd[4] = 0x50;
        out.write(tcmd);
    }

    public  void kickDrawerForSunmi()  throws Exception {
        byte[] tcmd = new byte[5];
        tcmd[0] = 0x10;
        tcmd[1] = 0x14;
        tcmd[2] = 0x00;
        tcmd[3] = 0x00;
        tcmd[4] = 0x00;
        out.write(tcmd);
    }

    public  void kickDrawerForHisense()  throws Exception {
        byte[] tcmd = new byte[5];
        tcmd[0] = ESCPOSPrinter.ESC;
        tcmd[1] = 0x70;
        tcmd[2] = 0x00;
        tcmd[3] = (byte) 0x80;
        tcmd[4] = (byte) 0x80;
        out.write(tcmd);
    }

    public ByteArrayOutputStream getOut() {
        return out;
    }

    //	public  void printImage(int width, int height, byte[]bitmap) throws Exception {
//		byte[] sendData = null;
//    	int i = 0,s = 0,j = 0,index = 0,lines = 0;
//    	byte[] temp = new byte[(width / 8)*5];
//    	byte[] dHeader = new byte[8];
//    	boolean result = true;
//    	if(bitmap.length!=0){
//    		dHeader[0] = 0x1D;
//        	dHeader[1] = 0x76;
//        	dHeader[2] = 0x30;
//        	dHeader[3] = 0x00;
//        	dHeader[4] = (byte)(width/8);
//        	dHeader[5] = 0x00;
//        	dHeader[6] = (byte)(bitmap.length%256);
//        	dHeader[7] = (byte)(bitmap.length/256);
//        	result = out.write(dHeader);
//	    	for( i = 0 ; i < (bitmap.length/5)+1 ; i++ ){
//	    		s = 0;
//	    		if( i < bitmap.length/5 ){
//	    			lines = 5;
//	    		}else{
//	    			lines = bitmap.length%5;
//	    		}
//	    		for( j = 0 ; j < lines*(width / 8) ; j++ ){
//	    			temp[s++] = sendData[index++];
//	    		}
//	    		result = out.write(temp);
//	    		try {
//					Thread.sleep(60);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	    		for(j = 0 ; j <(width/8)*5 ; j++ ){
//				    temp[j] = 0;
//			    }
//	    	}
//    	}
//	  	if (result == false) {
//			throw new RuntimeException("Print Image Failed");
//		}
//    }

    //=
//	class revMsgThread extends Thread {
//		@Override
//		public void run() {
//			try {
//				Message msg = new Message();
//				int revData;
//				while(true)
//	            {
//					revData = wfComm.revByte();               //read data from WIFI Printer
//					if(revData != -1){
//
//						msg = mHandler.obtainMessage(WFPRINTER_REVMSG);
//		                msg.obj = revData;
//		                mHandler.sendMessage(msg);
//					}
//				    Thread.sleep(20);
//	            }
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				Log.d("POS Printer Receiver","Failed to read data");
//			}
//		}
//	}

//	public void printBitmap( Bitmap bm) throws IOException {
//		int width = bm.getWidth();
//		int height = bm.getHeight();
//		int bytesWidth = width / 8;
//		byte[] var11 = new byte[bytesWidth * height];
//
//		for(int command = 0; command < height; ++command) {
//			for(int x = 0; x < bytesWidth; ++x) {
//				byte b = 0;
//
//				for(int i = 0; i < 8; ++i) {
//					int color = bm.getPixel(x * 8 + i, command);
//					if((color & 16777215) < 11312284 && (color & 16777215) > 0) {
//						b = (byte)(b | 1 << 7 - i);
//					}
//				}
//
//				var11[command * bytesWidth + x] = b;
//			}
//		}
//
//		byte[] var12 = new byte[]{(byte)29, (byte)118, (byte)48, (byte)0, (byte)(width >> 3), (byte)(width >> 11), (byte)height, (byte)(height >> 8)};
//		out.write(var12);
//		out.write(var11);
//	}


    public void printBitmap(Bitmap bm) throws Exception{
        int width = bm.getWidth();
        int height = bm.getHeight();
        byte[] pixels = null;
        final int bytesWidth = width / 8;// x方向压缩
        pixels = new byte[bytesWidth * height];// 压缩后图像缓存
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < bytesWidth; x++) {

                // x方向压缩8:1
                byte b = 0;
                for (int i = 0; i < 8; i++) {
                    int color = bm.getPixel(x * 8 + i, y);
                    if ((color & 0xffffff) < 0xac9c9c && (color & 0xffffff)  > 0/* matrix.get(x, y*8+i) */) {
//					if ((color & 0xffffff) == 0) {
                        b |= 1 << (7 - i);// 使用大端
                    }
                }
                pixels[y * bytesWidth + x] = b;
            }
        }
        byte[] command = new byte[8];
        command[0] = 0x1d;
        command[1] = 0x76;
        command[2] = 0x30;
        command[3] = 0x00;
        command[4] = (byte) (width >> 3);// xL
        command[5] = (byte) (width >> 11);// xH
        command[6] = (byte) (height);// yL
        command[7] = (byte) (height >> 8);// yH
        out.write(command);
        out.write(pixels);
        feed((byte)1);
    }

    public void printQRBitmap(Bitmap bm) throws Exception{
        int width = bm.getWidth();
        int height = bm.getHeight();
        byte[] pixels = null;
        final int bytesWidth = width / 8;// x方向压缩
        pixels = new byte[bytesWidth * height];// 压缩后图像缓存
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < bytesWidth; x++) {

                // x方向压缩8:1
                byte b = 0;
                for (int i = 0; i < 8; i++) {
                    int color = bm.getPixel(x * 8 + i, y);
//					if ((color & 0xffffff) < 0xac9c9c && (color & 0xffffff)  > 0/* matrix.get(x, y*8+i) */) {
                    if ((color & 0xffffff) == 0) {
                        b |= 1 << (7 - i);// 使用大端
                    }
                }
                pixels[y * bytesWidth + x] = b;
            }
        }
        byte[] command = new byte[8];
        command[0] = 0x1d;
        command[1] = 0x76;
        command[2] = 0x30;
        command[3] = 0x00;
        command[4] = (byte) (width >> 3);// xL
        command[5] = (byte) (width >> 11);// xH
        command[6] = (byte) (height);// yL
        command[7] = (byte) (height >> 8);// yH
        out.write(command);
        out.write(pixels);
    }
}

