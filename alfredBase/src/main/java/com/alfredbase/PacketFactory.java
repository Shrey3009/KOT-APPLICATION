package com.alfredbase;

import com.alfredbase.javabean.BaseNetPacket;
import com.alfredbase.socket.Packet;
import com.alfredbase.socket.PacketListener;
import com.google.gson.Gson;

public class PacketFactory {
	private static PacketFactory instance;

	public static PacketFactory getInstance() {
		if (instance == null) {
			instance = new PacketFactory();
		}
		return instance;
	}

	public Packet getTestPacket(String content, PacketListener listener) {
		int id = Packet.getNewId();
		BaseNetPacket baseNetPacket = new BaseNetPacket(id, API.TEST, content,
				ErrorCode.NONE);
		Gson gson = new Gson();
		return new Packet(id, gson.toJson(baseNetPacket), listener);
	}
}
