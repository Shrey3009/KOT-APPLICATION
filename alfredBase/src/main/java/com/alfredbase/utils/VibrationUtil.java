package com.alfredbase.utils;

import android.content.Context;
import android.os.Vibrator;

public class VibrationUtil {
	private static Vibrator vibrator;
	private static long[] pattern = { 100, 400, 100, 400, 100, 400};

	public static void init(Context context) {
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public static void playVibratorOnce() {
		if(vibrator.hasVibrator())
		vibrator.vibrate(pattern, -1);
	}

	
	public static void playVibratorByTime(long time){
		if(vibrator.hasVibrator())
			vibrator.vibrate(time);
	}

	public static void cancel() {
		if(vibrator != null){
			vibrator.cancel();
		}
	}
}
