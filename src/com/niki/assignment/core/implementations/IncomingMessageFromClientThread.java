package com.niki.assignment.core.implementations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

import com.niki.assignment.datastore.datainterface.DataStoreInterface;



public class IncomingMessageFromClientThread implements Runnable {
	
	private ServerSocket sourceSocket;
	private static volatile DataStoreInterface dataStore;
	private String sourceClient;
	
	public IncomingMessageFromClientThread(ServerSocket inputSocket, String source, DataStoreInterface inputDataStore){
		sourceSocket = inputSocket;
		sourceClient = source;
		dataStore = inputDataStore;
	}
	
	@Override
	public void run() {
		
		String destination, message;
		try {
			InputStream is = null;
			is = sourceSocket.accept().getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while(true){
				try {
					if(sourceSocket.isClosed())
						break;
					Timestamp now = new Timestamp(System.currentTimeMillis());
					destination = br.readLine();
					if(destination.equalsIgnoreCase("EXIT"))
						break;
					message = br.readLine();
					Message messageObj = new Message(now, sourceClient, destination, message);
					dataStore.insertData(destination, messageObj);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				/*sourceSocket.close();*/
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
