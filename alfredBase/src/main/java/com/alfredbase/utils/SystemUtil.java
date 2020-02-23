package com.alfredbase.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

import java.util.Locale;

public class SystemUtil {
	private static String TAG = SystemUtil.class.getSimpleName();
	public static double getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		LogUtil.d(TAG, mi.availMem / (1024 * 1024)+"---->M");
		return mi.availMem / (1024 * 1024);
	}
	
	public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

	public static String decode(String unicodeStr) {
		if (unicodeStr == null) {
			return null;
		}
		StringBuffer retBuf = new StringBuffer();
		int maxLoop = unicodeStr.length();
		for (int i = 0; i < maxLoop; i++) {
			if (unicodeStr.charAt(i) == '\\') {
				if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
					try {
						retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
						i += 5;
					} catch (NumberFormatException localNumberFormatException) {
						retBuf.append(unicodeStr.charAt(i));
					}
				else
					retBuf.append(unicodeStr.charAt(i));
			} else {
				retBuf.append(unicodeStr.charAt(i));
			}
		}
		return retBuf.toString();
	}

}
