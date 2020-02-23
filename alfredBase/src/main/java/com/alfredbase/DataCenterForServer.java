package com.alfredbase;

import org.apache.mina.core.session.IoSession;

import com.alfredbase.socket.Packet;
import com.alfredbase.socket.Server;

public class DataCenterForServer {
	private static DataCenterForServer instance;

	public static DataCenterForServer getInstance() {
		if (instance == null) {
			instance = new DataCenterForServer();
		}
		return instance;
	}

	public void start() {
		Server.getInstance().start();
	}

	public void stop() {
		Server.getInstance().stop();
	}

	public void send(String token, Packet packet) {
		// Server.getInstance().send(packet);
	}

	public void receive(IoSession session, Object message) {
		// 暂时做大写化处理以后返回数据
		session.write(message.toString());
	}
}
