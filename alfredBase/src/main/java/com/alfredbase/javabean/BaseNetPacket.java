package com.alfredbase.javabean;

public class BaseNetPacket {
	public int id;
	public String api;
	public String data;
	public int errorCode;

	public BaseNetPacket(int id, String api, String data, int errorCode) {
		this.id = id;
		this.api = api;
		this.data = data;
		this.errorCode = errorCode;
	}
}
