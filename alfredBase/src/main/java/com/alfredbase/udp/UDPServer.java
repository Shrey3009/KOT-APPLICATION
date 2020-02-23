package com.alfredbase.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.Queue;

import android.text.TextUtils;

import com.alfredbase.APPConfig;
import com.alfredbase.utils.LogUtil;

public class UDPServer {
	private static final String TAG = UDPServer.class.getSimpleName();

	private static UDPServer instance;

	private static Queue<String> sendQueue = new LinkedList<String>();

	private static boolean running = false;

	private InetAddress destAddress;

	private MulticastSocket multiSocket;

	public static UDPServer getInstance() {
		if (instance == null) {
			instance = new UDPServer();
			running = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (running) {
						if (sendQueue.size() == 0) {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							instance.sendMsg(sendQueue.poll());
						}
					}
				}
			}).start();
		}
		return instance;
	}

	private UDPServer() {
		try {
			destAddress = InetAddress.getByName(APPConfig.UDP_IP);
			if (!destAddress.isMulticastAddress()) {// 检测该地址是否是多播地址
				LogUtil.d(TAG, APPConfig.UDP_IP + "不能作为广播地址");
				return;
			}
			multiSocket = new MulticastSocket();
			multiSocket.setTimeToLive(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(String str) {
		sendQueue.offer(str);
	}

	private void sendMsg(String str) {
		try {
			if (!TextUtils.isEmpty(str)) {
				byte[] sendMSG = str.getBytes("UTF-8");
				if (sendMSG.length > APPConfig.UDP_BUFFER_SIZE) {
					LogUtil.d(TAG, "数据太大，超过约定的缓冲区");
					return;
				}
				LogUtil.d(TAG, "广播数据：" + str);
				DatagramPacket dp = new DatagramPacket(sendMSG, sendMSG.length,
						destAddress, APPConfig.UDP_PORT);
				multiSocket.send(dp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		running = false;
		if (multiSocket != null)
			multiSocket.close();
	}

}
