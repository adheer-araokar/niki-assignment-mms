package com.niki.assignment.core.implementations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.niki.assignment.datastore.datainterface.DataStoreInterface;

public class OutgoingMessageToClientThread implements Runnable {
	
	private ServerSocket destinationSocket;
	private static volatile DataStoreInterface dataStore;
	private String destinationClient;
	
	public OutgoingMessageToClientThread(ServerSocket inputSocket, String destination, DataStoreInterface inputDataStore){
		destinationSocket = inputSocket;
		destinationClient = destination;
		dataStore = inputDataStore;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		PrintWriter writer;
		OutputStream is = null;
		try {
			is = destinationSocket.accept().getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Integer lastReadIndexFromData;
		int lastReadIndex;
		ChatData allData;
		ArrayList<Message> messageData;
		try{
			OutputStreamWriter isr = new OutputStreamWriter(is);
			writer = new PrintWriter(isr);
			while(true){
				try {
					allData = dataStore.selectAllData();
					if(allData != null){
						if(allData.getLastRead() == null)
							lastReadIndex = -1;
						else {
							lastReadIndexFromData = allData.getLastRead().get(destinationClient);
							if(lastReadIndexFromData == null)
								lastReadIndex = -1;
							else
								lastReadIndex = lastReadIndexFromData.intValue();
						}
						if(allData.getData() != null)
							messageData = allData.getData().get(destinationClient);
						else
							messageData = new ArrayList<Message>();
						lastReadIndex = -1;
						if(lastReadIndex < messageData.size()-1){
							lastReadIndex++;
							while(lastReadIndex < messageData.size()){
								writer.println(messageData.get(lastReadIndex).getMessage());
								lastReadIndex++;
							}
							lastReadIndex--;
							dataStore.insertLastRead(destinationClient, lastReadIndex);
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				destinationSocket.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
