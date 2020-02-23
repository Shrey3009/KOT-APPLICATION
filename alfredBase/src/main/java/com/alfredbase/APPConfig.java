package com.alfredbase;

public interface APPConfig {

	/*Main POS HTTP Server*/
	public static final int HTTP_SERVER_PORT = 8182;
	
	/*KDS HTTP Server*/
	public static final int KDS_HTTP_SERVER_PORT = 8183;
	
	/*Waiter HTTP Server*/
	public static final int WAITER_HTTP_SERVER_PORT = 8184;
	/*CallNum HTTP Server*/
	public static final int CALLNUM_HTTP_SERVER_PORT = 8185;

	public static final String FILE_SERVER_START_FLAG = "/file";

	public static final String FILE_SERVER_DIRECTORY = "/mnt/sdcard/";

	public static final String UDP_IP = "228.0.0.1";

	public static final int UDP_PORT = 9998;

	/**
	 * UDP包最大尺寸暂定为50K
	 */
	public static final int UDP_BUFFER_SIZE = 1024 * 50;
}
