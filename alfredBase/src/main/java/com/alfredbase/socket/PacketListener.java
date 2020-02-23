package com.alfredbase.socket;

public interface PacketListener {
	void timeOut();

	void receive(Object object);

	void error(int type);

}
