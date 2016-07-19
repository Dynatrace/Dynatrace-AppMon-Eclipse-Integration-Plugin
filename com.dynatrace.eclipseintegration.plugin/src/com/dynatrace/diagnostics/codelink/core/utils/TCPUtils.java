package com.dynatrace.diagnostics.codelink.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.SocketFactory;

final class TCPUtils {
	
	private static final byte[] PING_MESSAGE = new byte[] { -1, -1, -1, -1 };
	
	private TCPUtils() {}
	
	/** Check if Port is available
	  * 
	  * @param port
	  * @return
	  * @author markus.poechtrager
	 */
	public static boolean isPortAvailable(int port) {
		if (port < 1 || port > 65535) {
			return false;
		}
		
		ServerSocket dummySocket = null;
		
		try {
			dummySocket = new ServerSocket(port);
			return true;
		}
		catch (IOException e) {
			return false;
		}
		finally {
			if (dummySocket != null) {
				try {
					dummySocket.close();
				}
				catch (IOException e1) {}
			}			
		}
	}
	
	/** Check if Plugin Listener is Running on specified port
	  * 
	  * @param pingMessage
	  * @param port 
	  * @return
	  * @author markus.poechtrager
	 */
	public static boolean isPluginAvailable(int port) {
		Socket client = null;
		try {
			client = SocketFactory.getDefault().createSocket(InetAddress.getLocalHost(), port);
			client.setTcpNoDelay(true);
			InputStream is = client.getInputStream();
			OutputStream os = client.getOutputStream();
			// send ping request
			os.write(PING_MESSAGE);
			client.setSoTimeout(100);
			// read pong
			byte[] pongResponse = new byte[4];
			int in = is.read(pongResponse, 0, 4);
			if (in == 4) {
				return true;
			}
		}
		catch (IOException e) {			
		}
		finally {
			if (client != null) {
				try {
					client.close();
				}
				catch (IOException e) {}
			}
		}
		return false;
	}
}
