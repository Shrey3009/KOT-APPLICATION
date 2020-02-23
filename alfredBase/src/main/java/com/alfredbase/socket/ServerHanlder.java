package com.alfredbase.socket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.alfredbase.utils.LogUtil;

public class ServerHanlder extends IoHandlerAdapter {
	private static final String TAG = ServerHanlder.class.getSimpleName();

	// 当一个客端端连结进入时
	@Override
	public void sessionOpened(IoSession session) throws Exception {

	}

	// 当客户端发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		LogUtil.d(TAG, "收到-->" + message);
		if (SocketConst.HEART_BEAT.equals(message.toString())) {
			session.write(message);
			return;
		}
		Server.getInstance().receive(session, message);
	}

	// 当信息已经传送给客户端后触发此方法.
	@Override
	public void messageSent(IoSession session, Object message) {
		LogUtil.d(TAG, "已发送-->" + message);
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		LogUtil.d(TAG, session.getId() + "-->关闭");
	}

	// 当连接空闲时触发此方法.
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		LogUtil.d(TAG, session.getId() + "-->空闲");
		session.close(true);
	}

	// 当接口中其他方法抛出异常未被捕获时触发此方法
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		cause.printStackTrace();
		session.close(true);
	}

}