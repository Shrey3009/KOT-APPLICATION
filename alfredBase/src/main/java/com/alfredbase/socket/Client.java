package com.alfredbase.socket;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.alfredbase.BaseApplication;
import com.alfredbase.DataCenterForClient;
import com.alfredbase.HandlerWhat;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.NetUtil;

/**
 * 
 * @author
 * 
 *         1.Mina长连接的一个封装，完成长连接的初始化，启动，关闭
 * 
 *         2.心跳机制，并且维护长连接的存在
 * 
 */
public class Client {
	private static final String TAG = Client.class.getSimpleName();
	private static Client instance;

	private NioSocketConnector connector;

	private IoSession session;

	private int heartCount = 0;

	private Client() {
		connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"),
						LineDelimiter.DEFAULT, LineDelimiter.DEFAULT));
		chain.addLast("protocol", filter);
		connector.setHandler(new ClientHanlder());
		connector.setConnectTimeoutCheckInterval(3000);
	}

	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	public void startConnect() {
		LogUtil.d(TAG, "开始连接");
		if (!NetUtil.isAvailable(BaseApplication.instance)) {
			BaseApplication.sendMessage(HandlerWhat.NO_NET, null);
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				ConnectFuture future = connector.connect(new InetSocketAddress(
						SocketConst.HOST, SocketConst.PORT));
				future.addListener(new IoFutureListener<IoFuture>() {
					@Override
					public void operationComplete(IoFuture arg0) {
						LogUtil.d(TAG, "连接成功");
						session = arg0.getSession();
						startPing();
					}
				});
			}
		}).start();
	}

	public void send(Packet packet) {
		if (session != null && session.isConnected()) {
			session.write(packet.content);
		}
	}

	public void receive(IoSession session, Object message) throws Exception {
		heartCount = 0;
		LogUtil.d(TAG, "收到 " + "-->" + message);
		if (SocketConst.HEART_BEAT.equals(message)) {
			return;
		}
		DataCenterForClient.getInstance().receive(session, message);
	}

	public void stop() {
		if (session != null) {
			session.close(true);
		}
		connector.dispose();
		instance = null;
		LogUtil.d(TAG, "客户端退出");
	}

	private boolean pingIsRunning = false;

	private void startPing() {
		if (!pingIsRunning) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					pingIsRunning = true;
					while (BaseApplication.sub_thread_running) {
						if (heartCount >= 1) {
							heartCount = 0;
							session.close(false);
							// 重连
							startConnect();
						} else {
							heartCount++;
							session.write(SocketConst.HEART_BEAT);
						}

						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					pingIsRunning = false;
				}
			}).start();
		}
	}

}
