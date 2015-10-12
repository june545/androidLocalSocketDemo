package com.exmaple.socket.local;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

public class SocketTest {
	private final String		TAG				= "Test";
	private final String		SOCKET_ADDRESS	= "socket_address";
	private LocalServerSocket	lss;
	private LocalSocket			sender;
	private LocalSocket			receiver;
	private int					bufferSize		= 10000;
	private byte[]				buffer			= new byte[1024];
	private boolean				threadRunning	= true;

	public SocketTest() {
		init();
		receive();
	}

	public void init() {
		try {
			lss = new LocalServerSocket(SOCKET_ADDRESS);

			receiver = new LocalSocket();
			receiver.connect(new LocalSocketAddress(SOCKET_ADDRESS));
			receiver.setReceiveBufferSize(bufferSize);
			receiver.setSendBufferSize(bufferSize);

			sender = lss.accept();
			sender.setReceiveBufferSize(bufferSize);
			sender.setSendBufferSize(bufferSize);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(final String msg) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.d(TAG, "start send msg");
				try {
					sender.getOutputStream().write(msg.getBytes());
					sender.getOutputStream().flush();
					sender.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.d(TAG, "end send msg");
			}
		}).start();

	}

	public void receive() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					InputStream is = receiver.getInputStream();
					DataInputStream dis = new DataInputStream(is);

					while (threadRunning) {
						//						Log.d(TAG, "thread listening ");
						StringBuffer sb = new StringBuffer();
						byte[] buff = new byte[1024];
						int count = -1;

						//						while ((count = dis.read(buff)) != -1) {
						count = dis.read(buff);
						if (count > 0) {
							sb.append(new String(buff, 0, count));
							Log.d(TAG, " data " + sb.toString());
						}
						
					}
				} catch (IOException e) {
					e.printStackTrace();
					threadRunning = false;
				}

			}
		}).start();
	}

	public void close() {
		threadRunning = false;
		if (sender != null) {
			try {
				sender.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
