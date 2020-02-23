package com.alfred.printer;

import java.io.Serializable;

public class PrintTscData implements Serializable{

	private static final long serialVersionUID = 8607945962987886031L;

	//Languate
	public static int LANG_EN = 0;
	public static int LANG_CN = 1;
	//content format

	public static int FORMAT_BAR = 8;
	public static int FORMAT_RESET = 9;
	public static int FORMAT_TXT = 10;
	public static int FORMAT_IMG = 11;
	public static int FORMAT_QR = 12;
	public static int FORMAT_CUT = 13;
	public static int FORMAT_DRAWER = 14;
	public static int FORMAT_FEED=15;
	public static int FORMAT_SING=16;
	public static int FORMAT_MIN_QR = 17;

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

    public int getFontsizeX() {
        return fontsizeX;
    }

    public void setFontsizeX(int fontsizeX) {
        this.fontsizeX = fontsizeX;
    }

    public int getFontsizeY() {
        return fontsizeY;
    }

    public void setFontsizeY(int fontsizeY) {
        this.fontsizeY = fontsizeY;
    }

    private int fontsizeX;

	private int fontsizeY;

	private String text;




	private int x;
	private int y;


	public PrintTscData() {
		super();
		this.dataFormat = -1;
		this.font = -1;
		this.fontsize = -1;
		this.text = null;


	}
	
	public void addText(int format, String txt, int fontSize, int font) {
		this.dataFormat = format;
		this.text = txt;
		this.fontsize = fontSize;
		this.font = font;
	}
	
	public void addImage(byte img[]){
		

		this.dataFormat = PrintTscData.FORMAT_IMG;
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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


}
