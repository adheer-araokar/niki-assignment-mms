package com.niki.assignment.core.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketConnection {
	
	private static AtomicInteger portNos = new AtomicInteger(10500);
	private ConcurrentHashMap<Integer, ServerSocket> portSocketMap = new ConcurrentHashMap<Integer, ServerSocket>();
	private ConcurrentHashMap<String, Integer> sourcePortMap = new ConcurrentHashMap<String, Integer>();
	
	public SocketConnection(ConcurrentHashMap<Integer, ServerSocket> portSocket, ConcurrentHashMap<String, Integer> sourcePort){
		this.portSocketMap = portSocket;
		this.sourcePortMap = sourcePort;
	}

	public void createConnection(int port) throws IOException, BindException {
		this.portSocketMap.put(new Integer(port), new ServerSocket(port));
	}

	public ServerSocket getConnection(int sourcePort) {
		return (ServerSocket)this.portSocketMap.get(new Integer(sourcePort));
	}
	
	public int createConnectionForSource(String source) throws IOException {
		boolean ex = true;
		int port = 0;
		if(!this.sourcePortMap.containsKey(source)){
			while(ex){
				try{
					port = this.createConnectionForSourceAndListen(source);
					ex = false;
				}
				catch(BindException e){
					ex = true;
				}
				this.sourcePortMap.put(source, new Integer(portNos.intValue()));
			}
		}
		else {
			port = this.sourcePortMap.get(source).intValue();
		}
		//startListeningOnSocket(getConnection(port));
		return port;
	}
	
	public int createConnectionForSourceAndListen(String source) throws IOException, BindException {
		int port = portNos.incrementAndGet();
		this.createConnection(port);
		return port;
	}
	
	public static void startListeningOnSocket(Socket socket) throws IOException {
		//Create and start the threads for MMS from here
	}

	public ServerSocket getConnectionForSource(String source) {
		return (ServerSocket)this.portSocketMap.get((Integer)this.sourcePortMap.get(source));
	}
	
	public int getPortForSource(String source) {
		return ((Integer)this.sourcePortMap.get(source) == null ? -1 : ((Integer)this.sourcePortMap.get(source)).intValue());
	}
	
}
