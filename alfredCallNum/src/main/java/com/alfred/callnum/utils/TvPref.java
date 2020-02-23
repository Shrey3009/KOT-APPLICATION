package com.alfred.callnum.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.alfred.callnum.global.App;

public class TvPref {
	/* xml file name */
	public static final String PREFERENCE_FILE_PATH ="/data/data/com.puscene.pos.tv/shared_prefs/";
	public static final String PREFERENCE_FILE_NAME ="com_puscene_pos_tv_pref_info";
	
	private static final String IF_BIND_SSID          = "if_bind_ssid";
	private static final String BIND_AP_SSID          = "bind_ap_ssid";
	private static final String BIND_AP_PASS          = "bind_ap_pass";
	private static final String BIND_MYIP             = "bind_this_ip";
	private static final String DONT_PAUSE_VIDEO      = "dont_pause_video";
	

	private static final String IF_BIND_SSID_DAN          = "is_bind_ssid_dan";
	private static final String BIND_AP_SSID_DAN          = "bind_ssid_dan";
	
	
	private static final String BIND_AP_IP_DAN          = "bind_ip_dan";  //与POS机相关联，POS机的IP地址
	
	private static Context c;
	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor mEditor;
	
	public static void init() {
		c = App.instance;
		mSharedPreferences = c.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}
	

	public static boolean readBindSsidEn() {
		return mSharedPreferences.getBoolean(IF_BIND_SSID, false);
	}
	
	public static void saveBindSsidEn(boolean enable) {
		mEditor.putBoolean(IF_BIND_SSID, enable);
		mEditor.commit();
	}
	
	public static String readBindSSID() {
		return mSharedPreferences.getString(BIND_AP_SSID,"");
	}
	
	public static void saveBindSSID(String ssid) {
		mEditor.putString(BIND_AP_SSID, ssid);
		mEditor.commit();
	}
	
	public static String readBindPass() {
		return mSharedPreferences.getString(BIND_AP_PASS,"zhoubotong");
	}
	
	public static void saveBindPass(String pass) {
		mEditor.putString(BIND_AP_PASS, pass);
		mEditor.commit();
	}
	public static String readBindIP() {
		return mSharedPreferences.getString(BIND_MYIP,"192.168.123.113");
	}
	
	public static void saveBindIP(String ip) {
		mEditor.putString(BIND_MYIP, ip);
		mEditor.commit();
	}
	
	
	public static String getIp(){
		return mSharedPreferences.getString(BIND_AP_IP_DAN,"");
	}
	
	public static void setIp(String ip){
		mEditor.putString(BIND_AP_IP_DAN, ip);
		mEditor.commit();
	}

	private static final String SERVER_STATIC_IP          = "server_static_ip";
	private static final String SERVER_STATIC_IP_USED          = "server_static_ip_used";
	public static String readServerIP() {
		return mSharedPreferences.getString(SERVER_STATIC_IP,"");
	}
	
	public static void saveServerIP(String ip) {
		mEditor.putString(SERVER_STATIC_IP, ip);
		mEditor.commit();
	}
	public static boolean readServerStaticEn() {
		return mSharedPreferences.getBoolean(SERVER_STATIC_IP_USED, false);
	}
	public static void saveServerStaticEn(boolean enable) {
		mEditor.putBoolean(SERVER_STATIC_IP_USED, enable);
		mEditor.commit();
	}	
	
	//使用自定义等位标题栏
	private static final String CUST_WAIT_TITLE          = "custWaitTitle";
	private static final String CUST_WAIT_TITLE_USED          = "useCustWaitTitle";
	public static String readCustWaitTitle() {
		return mSharedPreferences.getString(CUST_WAIT_TITLE,"");
	}
	
	public static void saveCustWaitTitle(String custTitle) {
		mEditor.putString(CUST_WAIT_TITLE, custTitle);
		mEditor.commit();
	}
	public static boolean readCustWaitTitleEn() {
		return mSharedPreferences.getBoolean(CUST_WAIT_TITLE_USED, false);
	}
	public static void saveCustWaitTitleEn(boolean enable) {
		mEditor.putBoolean(CUST_WAIT_TITLE_USED, enable);
		mEditor.commit();
	}
	
	private static final String VIDEO_FILE = "video_file";
	public static String readVideoFile() {
		return mSharedPreferences.getString(VIDEO_FILE,"");
	}
	
	public static void saveVideoFile(String file) {
		mEditor.putString(VIDEO_FILE, file);
		mEditor.commit();
	}
	private static final String IMAGE_FILE = "images_file";
	public static String readImageFilePath() {
		return mSharedPreferences.getString(IMAGE_FILE,"");
	}
	
	public static void saveImageFilePath(String path) {
		mEditor.putString(IMAGE_FILE, path);
		mEditor.commit();
	}
	
	private static final String UDP_USED = "udp_used";
	private static final String PLAY_VIDEO_SPEED = "play_vspeed";
	private static final String PLAY_TEXTSIZE_RATIO = "qtextsize_ratio";
	private static final String PLAY_IMG_FILE = "if_play_img_queuing";
	private static final String QLAYOUT_WIDTH = "que_layout_width";//横屏
	private static final String QLAYOUT_HEIGHT = "que_layout_height";
	
	public static boolean readPlayIMGEn() {
		return mSharedPreferences.getBoolean(PLAY_IMG_FILE, true);
	}
	public static void savePlayIMGEn(boolean enable) {
		mEditor.putBoolean(PLAY_IMG_FILE, enable);
		mEditor.commit();
	}
	public static boolean readSaveMem() {
		return mSharedPreferences.getBoolean("save_mem", true);
	}
	public static void saveSaveMem(boolean enable) {
		mEditor.putBoolean("save_mem", enable);
		mEditor.commit();
	}
	public static boolean readHideGetNum() {
		return mSharedPreferences.getBoolean("hide_getNum", false);
	}
	public static void saveHideGetNum(boolean enable) {
		mEditor.putBoolean("hide_getNum", enable);
		mEditor.commit();
	}
	public static boolean readDisPause() {
		return mSharedPreferences.getBoolean(DONT_PAUSE_VIDEO, false);
	}
	public static void saveDisPause(boolean enable) {
		mEditor.putBoolean(DONT_PAUSE_VIDEO, enable);
		mEditor.commit();
	}
	public static boolean readUdpProtocolEn() {
		return mSharedPreferences.getBoolean(UDP_USED, false);
	}
	public static void saveUdpProtocolEn(boolean enable) {
		mEditor.putBoolean(UDP_USED, enable);
		mEditor.commit();
	}

	public static void saveTextSizeRatio(int ratio) {
		mEditor.putInt(PLAY_TEXTSIZE_RATIO, ratio);
		mEditor.commit();
	}
	public static int readTextSizeRatio() {
		return mSharedPreferences.getInt(PLAY_TEXTSIZE_RATIO, 10);//默认1.0
	}
	
	public static void savePlaySpeed(int speed) {
		mEditor.putInt(PLAY_VIDEO_SPEED, speed);
		mEditor.commit();
	}
	public static int readPlaySpeed() {
		return mSharedPreferences.getInt(PLAY_VIDEO_SPEED, 4);
	}
	public static void saveQLayoutWidth(int width) {
		mEditor.putInt(QLAYOUT_WIDTH, width);
		mEditor.commit();
	}
	public static int readQLayoutWidth() {
		return mSharedPreferences.getInt(QLAYOUT_WIDTH, 320);
	}
	public static int readQLayoutHeight() {
		return mSharedPreferences.getInt(QLAYOUT_HEIGHT, 640);
	}
	public static void saveQLayoutHeight(int h) {
		mEditor.putInt(QLAYOUT_HEIGHT, h);
		mEditor.commit();
	}
	private static final String PLAYING_VIDEO_FILE = "playing_video";
	public static String readPlayingFile() {
		return mSharedPreferences.getString(PLAYING_VIDEO_FILE,"");
	}
	
	public static void savePlayingFile(String file) {
		mEditor.putString(PLAYING_VIDEO_FILE, file);
		mEditor.commit();
	}
	private static final String EX_STORAGE_PATH = "ex_storage_path";
	public static String readExstoragePath() {
		return mSharedPreferences.getString(EX_STORAGE_PATH,"");
	}
	
	public static void saveExstoragePath(String path) {
		mEditor.putString(EX_STORAGE_PATH, path);
		mEditor.commit();
	}
	private static final String AD_VIDEO_FILE = "ad_video";
	public static String readADFile() {
		return mSharedPreferences.getString(AD_VIDEO_FILE,"");
	}
	
	public static void saveADFile(String file) {
		mEditor.putString(AD_VIDEO_FILE, file);
		mEditor.commit();
	}
	

	/* exception collection */
	private static final String KEY_LOG_NEED_ZIP = "LogNeedZip";
	private static final String KEY_LOG_UL_MILS  = "LogULMils";
	public static boolean logNeedZip() {
		return mSharedPreferences.getBoolean(KEY_LOG_NEED_ZIP, false);
	}
	
	public static void setLogNeedZip(boolean status) {
		mEditor.putBoolean(KEY_LOG_NEED_ZIP, status);
		mEditor.commit();
	}
	
	public static long lastLogULMils() {
		return mSharedPreferences.getLong(KEY_LOG_UL_MILS, 0);
	}
	
	public static void setLogULMils() {
		mEditor.putLong(KEY_LOG_UL_MILS, System.currentTimeMillis());
	}
	

	public static boolean readIfDanBind() {
		return mSharedPreferences.getBoolean(IF_BIND_SSID_DAN, false);
	}
	
	public static void saveIfDanBind(boolean enable) {
		mEditor.putBoolean(IF_BIND_SSID_DAN, enable);
		mEditor.commit();
	}
	public static String readBindSsidDan() {
		return mSharedPreferences.getString(BIND_AP_SSID_DAN,"");
	}
	
	public static void saveBindSsidDan(String ssid) {
		mEditor.putString(BIND_AP_SSID_DAN, ssid);
		mEditor.commit();
	}

	private static final String VIDEO_VOL = "video_volume";
	public static void saveVideoVol(int v) {
		mEditor.putInt(VIDEO_VOL, v);
		mEditor.commit();
	}
	public static int readVideoVol() {
		return mSharedPreferences.getInt(VIDEO_VOL, -1);
	}

	private static final String VIDEO_MARGIN_ADJUST = "video_adjust";
	public static void saveVideoAdjust(int adjust) {
		mEditor.putInt(VIDEO_MARGIN_ADJUST, adjust);
		mEditor.commit();
	}
	public static int readVideoAdjust() {
		return mSharedPreferences.getInt(VIDEO_MARGIN_ADJUST, 0);
	}
	
	private static final String COLOR_PICKER = "color_picker";
	public static void saveColor(int adjust) {
		mEditor.putInt(COLOR_PICKER, adjust);
		mEditor.commit();
	}
	public static int readColor() {
		return mSharedPreferences.getInt(COLOR_PICKER, Color.BLACK);
	}
}
