package com.alfred.printer;

import android.graphics.Bitmap;

import java.io.Serializable;

public class PrintData  implements Serializable{

	private static final long serialVersionUID = 8607945962987886031L;
	
	//Languate
	public static int LANG_EN = 0;
	public static int LANG_CN = 1;
	//content format
	public static int FORMAT_TXT = 10;
	public static int FORMAT_IMG = 11;
	public static int FORMAT_QR = 12;
	public static int FORMAT_CUT = 13;
	public static int FORMAT_DRAWER = 14;
	public static int FORMAT_FEED=15;
	public static int FORMAT_SING=16;
	public static int FORMAT_MIN_QR = 17;
	public static int FORMAT_QR_BITMAP = 18;

	//alignment 
	public static int ALIGN_LEFT=0;
	public static int ALIGN_CENTRE = 1;
	public static int ALIGN_RIGHT=2;
	
	//image format
	public static int IMAGE_PNG = 20;
	public static int IMAGE_JPG = 21;
	
	private int dataFormat;
	private int font ;
	private int fontsize;
	private int textAlign;
	private int textBold;
	private boolean underline;
	private int marginTop;
	private int language;
	private String text;
	private byte[] image;
	private int img_w;
	private int img_h;
	private String qrCode;
	private byte[] qrCodeBitmap;
	

	public PrintData() {
		super();
		this.dataFormat = -1;
		this.font = -1;
		this.fontsize = -1;
		this.text = null;
		this.textAlign = -1;
		this.marginTop = -1;
		this.textBold = -1;
		this.language = -1;
		this.underline = false;
	}
	
	public void addText(int format, String txt, int fontSize, int font) {
		this.dataFormat = format;
		this.text = txt;
		this.fontsize = fontSize;
		this.font = font;
	}
	
	public void addImage(byte img[]){
		
		this.image = img;
		this.dataFormat = PrintData.FORMAT_IMG;
	}
	
	public int getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(int dataFormat) {
		this.dataFormat = dataFormat;
	}

	public int getFont() {
		return font;
	}

	public void setFont(int font) {
		this.font = font;
	}

	public int getFontsize() {
		return fontsize;
	}

	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getImg_w() {
		return img_w;
	}

	public void setImg_w(int img_w) {
		this.img_w = img_w;
	}

	public int getImg_h() {
		return img_h;
	}

	public void setImg_h(int img_h) {
		this.img_h = img_h;
	}

	public int getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(int textAlign) {
		this.textAlign = textAlign;
	}

	public int getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	public int getTextBold() {
		return textBold;
	}

	public void setTextBold(int textBold) {
		this.textBold = textBold;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public byte[] getQrCodeBitmap() {
		return qrCodeBitmap;
	}

	public void setQrCodeBitmap(byte[] qrCodeBitmap) {
		this.qrCodeBitmap = qrCodeBitmap;
	}
}
