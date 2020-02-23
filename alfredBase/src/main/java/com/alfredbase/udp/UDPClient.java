package com.alfredbase.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import com.alfredbase.APPConfig;
import com.alfredbase.utils.LogUtil;

/**
 * 
 * @author
 * 
 *         组播IP地址就是D类IP地址，即224.0.0.0至239.255.255.255之间的IP地址
 * 
 */
public class UDPClient {
	private static final String TAG = UDPClient.class.getSimpleName();
	private InetAddress receiveAddress;

	private boolean running = true;

	private MulticastSocket receiveMulticast;

	private DatagramPacket dp = null;

	private static UDPClient instance;

	public static UDPClient getInstance() {
		if (instance == null) {
			instance = new UDPClient();
		}
		return instance;
	}

	private UDPClient() {
		try {
			receiveAddress = InetAddress.getByName(APPConfig.UDP_IP);

			if (!receiveAddress.isMulticastAddress()) {
				LogUtil.d(TAG, APPConfig.UDP_IP + "不能作为广播地址");
				return;
			}

			receiveMulticast = new MulticastSocket(APPConfig.UDP_PORT);

			receiveMulticast.joinGroup(receiveAddress);

			receiveMulticast.setSoTimeout(1000 * 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (running) {
					dp = new DatagramPacket(
							new byte[APPConfig.UDP_BUFFER_SIZE],
							APPConfig.UDP_BUFFER_SIZE);
					try {
						receiveMulticast.receive(dp);
						String result = new String(dp.getData(), 0,
								dp.getLength(), "UTF-8");
						LogUtil.d(TAG, "收到广播消息：" + result);
					} catch (SocketTimeoutException e) {
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	public void stop() {
		if (receiveMulticast != null) {
			running = false;
			try {
				receiveMulticast.leaveGroup(receiveAddress);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				receiveMulticast.close();
			}
		}

	}

}
