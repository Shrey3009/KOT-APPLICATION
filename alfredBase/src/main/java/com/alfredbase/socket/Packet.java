package com.alfredbase.socket;

public class Packet {
	private static int global_unique_seq = 0;
	// 全局唯一的包标识符
	public int id;
	public String content;
	public PacketListener listener;
	private long sendTime = System.currentTimeMillis();

	public Packet() {
	}

	public Packet(int id, String content, PacketListener listener) {
		this.id = id;
		this.content = content;
		this.listener = listener;
	}

	public static int getNewId() {
		global_unique_seq++;
		global_unique_seq &= 0x7FFFFFFF;
		return global_unique_seq;
	}

	public boolean isTimeout() {
		// 超时时间暂时定为10秒
		if (System.currentTimeMillis() - sendTime >= 10 * 1000) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public String toString() {
		return "Packet [id=" + id + ", content=" + content + ", listener="
				+ listener + ", sendTime=" + sendTime + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Packet))
			return false;
		Packet packet = (Packet) o;
		if (packet.id == this.id)
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
