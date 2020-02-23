package com.alfredbase.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class RingUtil {
	private static SoundPool soundPool;
	private static int ring;
	public void init(final Context context, final int ResourcesId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
				ring = soundPool.load(context, ResourcesId, 0);
			}
		}).start();
	}

	public void playRingOnce() {
		try {
			// 播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率最低0.5最高为2，1代表正常速度
			soundPool.play(ring, 1, 1, 0, 0, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playRingTwice(){
		try {
			soundPool.play(ring, 1, 1, 0, 1, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void stopFindNearby() {
		try {
			if (ring != 0) {
				soundPool.stop(ring);
				ring = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
