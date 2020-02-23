package com.alfredbase.socket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * @author
 * 
 *         基于Mina事件驱动模型的一个实现，主要用于消息接收和转发
 * 
 */
public class ClientHanlder extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		Client.getInstance().receive(session, message);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		cause.printStackTrace();
	}

}
