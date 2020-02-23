package com.alfredbase.socket;

import com.alfredbase.utils.LogUtil;

public abstract class PacketListenerImp implements PacketListener {
	private String TAG = PacketListenerImp.class.getSimpleName();
	@Override
	public void timeOut() {
		LogUtil.e(TAG, "应用层超时了");
	}

	@Override
	public void receive(Object object) {
		LogUtil.e(TAG, "应用层收到了--"+object);
	}

	@Override
	public void error(int type) {
		LogUtil.e(TAG, "应用层收到了错误码；"+type);
	}

}
