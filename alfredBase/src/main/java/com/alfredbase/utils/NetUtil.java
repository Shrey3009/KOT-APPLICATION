package com.alfredbase.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class NetUtil {
	public static boolean isAvailable(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		return info != null && info.isAvailable();
	}

	public static boolean ping(String ip) {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process mIpAddrProcess = runtime
					.exec("/system/bin/ping -c 1 " + ip);
			int mExitValue = mIpAddrProcess.waitFor();
			if (mExitValue == 0) {
				return true;
			} else {
				return false;
			}
		} catch (InterruptedException ignore) {
			ignore.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return false;
	}

	// 6.0之后 检查Internet通不通 不需要ping 可以使用此方法
//	public static boolean isNetSystemUsable(Context context) {
//		boolean isNetUsable = false;
//		ConnectivityManager manager = (ConnectivityManager)
//				context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//			NetworkCapabilities networkCapabilities =
//					manager.getNetworkCapabilities(manager.getActiveNetwork());
//			isNetUsable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
//		}
//		return isNetUsable;
//	}

	public static Map<String, String> decodeUrl(String paramString) {
		int i = 0;
		Map<String, String> localBundle = new HashMap<String, String>();
		String[] arrayOfString1 = null;
		int j = 0;
		if (paramString != null) {
			arrayOfString1 = paramString.split("&");
			j = arrayOfString1.length;
		}
		for (;;) {
			if (i >= j) {
				return localBundle;
			}
			String[] arrayOfString2 = arrayOfString1[i].split("=");
			try {
				if (arrayOfString2.length == 2) {
					localBundle.put(
							URLDecoder.decode(arrayOfString2[0], "UTF-8"),
							URLDecoder.decode(arrayOfString2[1], "UTF-8"));
				} else if (arrayOfString2.length == 1) {
					localBundle.put(
							URLDecoder.decode(arrayOfString2[0], "UTF-8"), "");
				}
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
			i++;
		}
	}

	public static Map<String, String> parseUrl(String paramString) {
		String str = paramString.replace("fbconnect", "http");
		try {
			URL localURL = new URL(str);
			Map<String, String> localBundle = decodeUrl(localURL.getQuery());
			localBundle.putAll(decodeUrl(localURL.getRef()));
			return localBundle;
		} catch (MalformedURLException localMalformedURLException) {
		}
		return new HashMap<String, String>();
	}

	public static String ConvertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			System.out.println("Error=" + e.toString());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				System.out.println("Error=" + e.toString());
			}
		}
		return sb.toString();

	}

	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

}
