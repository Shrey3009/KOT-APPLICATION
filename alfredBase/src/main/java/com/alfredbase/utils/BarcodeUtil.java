package com.alfredbase.utils;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * @time
 * @tags   
 * @todo TODO
 */
public class BarcodeUtil {

	/**
	 * 解析扫到的条形码
	 * @param orderNoStr
	 * @return  int[0]:订单号
	 * 			int[1]:点菜数量
	 * 			int[2]:菜的序号
	 * 			int[3]:日期
	 */
	public static int[] parsingBarcode(String orderNoStr){
		int[] noArr = new int[4];
		if (orderNoStr != null && orderNoStr.length() == 9) {
			try {
				noArr[0] = Integer.parseInt(orderNoStr.substring(0, 3), 16);
				noArr[1] = Integer.parseInt(orderNoStr.substring(3, 5), 16);
				noArr[2] = Integer.parseInt(orderNoStr.substring(5, 7), 16);
				noArr[3] = Integer.parseInt(orderNoStr.substring(7, 9), 16);
			} catch (Exception e) {
				return null;
			}
		}
		return noArr;
	}
	
	/**
	 * 生成长度为九的条形码
	 * 		十进制转成十六进制
	 * @param params
	 * 
	 * 1、orderNo 订单号
	 * 2、count 菜总数
	 * 3、index 菜的序号
	 * 4、day  日期
 	 * @return
	 */
	public static String getBarcode(int orderNo, int count, int index, int day){
		StringBuffer barSB = new StringBuffer();
		try {
			barSB.append(buwei(Integer.toHexString(orderNo), 3));
			barSB.append(buwei(Integer.toHexString(count), 2));
			barSB.append(buwei(Integer.toHexString(index), 2));
			barSB.append(buwei(Integer.toHexString(day), 2));
		} catch (Exception e) {
			e.printStackTrace();
			return "000000000";
		}
		return barSB.toString();
	}
	
	/**
	 * 长度不足时用“0”补位
	 * @param value   值
	 * @param length  额定长度
	 * @return
	 */
	public static String buwei(String value, int length){
		if (value != null ) {
			return getZero(length-value.length())+value;
		}
		return getZero(length);
		
	}
	
	/**
	 * 生成count个“0”
	 * @param count
	 * @return
	 */
	public static String getZero(int count){
		if (count < 1) {
			return "";
		}
		StringBuffer ab = new StringBuffer();
		for (int i = 0; i < count; i++) {
			ab.append("0");
		}
		return ab.toString();
	}
	
	/**
	 * 生成二维码 要转换的地址或字符串,可以是中文
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap createQRImage(String url) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, 1500, 1500, hints);
			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();

			int[] pixels = new int[width * height];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在二维码中间添加Logo图案
	 */
	public static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		//获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();
		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		//logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 9 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}
}
