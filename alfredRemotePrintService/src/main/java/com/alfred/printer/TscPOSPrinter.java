package com.alfred.printer;

import android.content.Context;
import android.graphics.Bitmap;

import com.alfred.remote.printservice.PrintService;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class TscPOSPrinter {
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

	Vector<Byte> Command = null;

    public TscPOSPrinter(PrintService context) {
		super();
		this.mContext = context;
		this.Command = new Vector(4096, 1024);

    }

 // 绘制线条
	public void addBar(int x, int y, int width, int height) {
		new String();
		String str = "BAR " + x + "," + y + "," + width + "," + height + "\r\n";
		this.addStrToCommand(str);
	}
     //清空Byte 数据
	public void clrCommand() {
		this.Command.clear();
	}

     //  标签正确位置
	public void addHome() {
		new String();
		String str = "HOME\r\n";
		this.addStrToCommand(str);
	}
	private void addStrToCommand(String str) {
		byte[] bs = null;
		if (!str.equals("")) {
			try {
				bs = str.getBytes("gbk");
			} catch (UnsupportedEncodingException var4) {
				var4.printStackTrace();
			}

			for(int i = 0; i < bs.length; ++i) {
				this.Command.add(bs[i]);
			}
		}

	}


	public Vector<Byte> getCommand() {
		return this.Command;
	}

//    public void reset()  throws Exception{
//    	byte[] tcmd = new byte[2];
//        tcmd[0] = TscPOSPrinter.ESC;
//   	    tcmd[1] = 0x40;
//   	    out.write(tcmd);
//    }

	//设置打印区域大小
	public void addTsize(int width, int height, int gap)  throws Exception{

		this.addSize(width, height);
		this.addGap(gap);
	}

	public void addGap(int gap) {
		new String();
		String str = "GAP " + gap + " mm," + 0 + " mm" + "\r\n";
		this.addStrToCommand(str);
	}

	public void addSize(int width, int height) {
		new String();
		String str = "SIZE " + width + " mm," + height + " mm" + "\r\n";
		this.addStrToCommand(str);
	}

	// 设置原点坐标
	public void addReference(int x, int y) {
		new String();
		String str = "REFERENCE " + x + "," + y + "\r\n";
		this.addStrToCommand(str);
	}


	// 设置打印速度
	public void addSpeed() {
		new String();
		String str = "SPEED " + 1.5 + "\r\n";
		this.addStrToCommand(str);
	}


	// 设置打印浓度
	public void addDensity() {
		new String();
		String str = "DENSITY " + 9 + "\r\n";
		this.addStrToCommand(str);
	}
	// 设置打印方向
	public void addDirection(int dir) {
		new String();
		String str = "DIRECTION " + dir + "\r\n";
		this.addStrToCommand(str);
	}

	//加入打印标签命令
	public void addPrint() {
		new String();
		String str = "PRINT " + 1 + "," + 1 + "\r\n";
		this.addStrToCommand(str);
	}

	//清除打印缓冲区
	public void addCls() {
		new String();
		String str = "CLS\r\n";
		this.addStrToCommand(str);
	}

	 //添加打印的文字
	public void addText(int x, int y,  int Xscal, int Yscal, String text) {
		new String();
		String str = "TEXT " + x + "," + y + "," + "\"" + "TSS24.BF2" + "\"" + "," + 0 + "," + Xscal + "," + Yscal + "," + "\"" + text + "\"" + "\r\n";
		this.addStrToCommand(str);
	}



	//重置
	public void resetPrinter() {
		this.Command.add((byte) 27);
		this.Command.add((byte) 33);
		this.Command.add((byte) 82);
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





}

