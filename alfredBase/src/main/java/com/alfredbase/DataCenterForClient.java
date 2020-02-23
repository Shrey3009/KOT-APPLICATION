package com.alfredbase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.alfredbase.javabean.BaseNetPacket;
import com.alfredbase.socket.Client;
import com.alfredbase.socket.Packet;
import com.google.gson.Gson;

/**
 * 
 * @author
 * 
 *         1.客户端进行网络数据交互的数据中心，提供网络交互能力
 * 
 *         2.维护一个发送队列，并check数据接收是否超时
 * 没有用
 */
public class DataCenterForClient {
	private static DataCenterForClient instance;

	private Map<Integer, Packet> packets = new ConcurrentHashMap<Integer, Packet>();

	public static DataCenterForClient getInstance() {
		if (instance == null) {
			instance = new DataCenterForClient();
			instance.startTimeoutCheck();
		}
		return instance;
	}

	public void start() {
		Client.getInstance().startConnect();
	}

	public void stop() {
		Client.getInstance().stop();
	}

	public void send(Packet packet) {
		packets.put(packet.id, packet);
		Client.getInstance().send(packet);
	}

	public void receive(IoSession session, Object message) {
		Gson gson = new Gson();
		BaseNetPacket baseNetPacket = gson.fromJson(message.toString(),
				BaseNetPacket.class);
		Packet packet = null;
		for (Map.Entry<Integer, Packet> entry : packets.entrySet()) {
			packet = entry.getValue();
			if (packet.id == baseNetPacket.id) {
				break;
			}
			packet = null;
		}
		// 主动发出请求返回的数据
		if (packet != null) {
			// 错误数据通知
			if (baseNetPacket.errorCode != ErrorCode.NONE) {
				packet.listener.error(baseNetPacket.errorCode);
				return;
			}
			// 正常数据通知
			packet.listener.receive(baseNetPacket.data);
			packets.remove(packet.id);
			return;
		}

		// 服务器通知的数据，或者超时的数据
	}

	private void startTimeoutCheck() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Packet packet = null;
				while (BaseApplication.sub_thread_running) {
					for (Map.Entry<Integer, Packet> entry : packets.entrySet()) {
						packet = entry.getValue();
						if (packet.isTimeout()) {
							packet.listener.timeOut();
							break;
						}
						packet = null;
					}
					if (packet != null) {
						packets.remove(packet.id);
						packet = null;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
