package com.alfredbase.socket;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.alfredbase.DataCenterForServer;
import com.alfredbase.utils.LogUtil;

public class Server {
	private static final String TAG = Server.class.getSimpleName();
	private NioSocketAcceptor acceptor;
	private static Server instance;

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	private Server() {
		acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast(
				"protocol",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"), LineDelimiter.DEFAULT,
						LineDelimiter.DEFAULT)));
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, SocketConst.SERVER_SESSION_IDLE_TIME);
		acceptor.setHandler(new ServerHanlder());
	}

	public void start() {
		try {
			acceptor.setReuseAddress(true);
			acceptor.bind(new InetSocketAddress(SocketConst.PORT));
			LogUtil.d(TAG, "服务器开始监听");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			acceptor.unbind(new InetSocketAddress(SocketConst.PORT));
			acceptor.dispose();
			instance = null;
			LogUtil.d(TAG, "服务器停止监听");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receive(IoSession session, Object message) throws Exception {
		DataCenterForServer.getInstance().receive(session, message);
	}
}
