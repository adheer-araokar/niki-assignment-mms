package com.niki.assignment.datastore.dataimpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.niki.assignment.core.implementations.ChatData;
import com.niki.assignment.core.implementations.Message;
import com.niki.assignment.datastore.datainterface.DataStoreInterface;

public class FileDataStore implements DataStoreInterface {
	
	String dataStoreFilePath;
	
	public FileDataStore(String path){
		this.dataStoreFilePath = path;
	}
	
	public String getDataStoreFilePath(){
		return this.dataStoreFilePath;
	}
	
	@Override
	public synchronized void insertData(String destination, Message message) {
		OutputStream ops = null;
        ObjectOutputStream objOps = null;
        boolean duplicate = false;
        ChatData allFileData = this.selectAllData();
		HashMap<String, ArrayList<Message>> fileData;
		HashMap<String, Integer> fileDataLastRead;
		ArrayList<Message> tmal;
		if(allFileData == null){
			fileData = new HashMap<String, ArrayList<Message>>();
			fileDataLastRead = new HashMap<String, Integer>();
			tmal = new ArrayList<Message>();
			tmal.add(message);
			fileData.put(destination, tmal);
		}
		else{
			fileData = allFileData.getData();
			fileDataLastRead = allFileData.getLastRead();
			tmal = fileData.get(destination);
			if(tmal == null)
				tmal = new ArrayList<Message>();
			if(tmal.size() > 0){
				Message prev = tmal.get(tmal.size()-1);
				if(prev.getMessage().equalsIgnoreCase(message.getMessage()) && (message.getMessageTS().getTime()-prev.getMessageTS().getTime()) <= 5000){
					System.out.println("This message is a duplicate");
					duplicate = true;
				}
			}
			else{
				tmal.add(message);
				fileData.put(destination, tmal);
			}
		}
		if(!duplicate){
	        try {
	        	ChatData result = new ChatData();
	        	result.setData(fileData);
	        	result.setLastRead(fileDataLastRead);
	            ops = new FileOutputStream(this.dataStoreFilePath, false);
	            objOps = new ObjectOutputStream(ops);
	            objOps.writeObject(result);
	            objOps.flush();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally{
	            try{
	                if(objOps != null) objOps.close();
	            } catch (Exception ex){
	                 
	            }
	        }
		}
	}
	
	@Override
	public synchronized void insertLastRead(String destination, int lastRead) {
		OutputStream ops = null;
        ObjectOutputStream objOps = null;
        ChatData allFileData = this.selectAllData();
        HashMap<String, ArrayList<Message>> fileData;
        HashMap<String, Integer> fileDataLastRead;
        if(allFileData == null){
        	fileData = new HashMap<String, ArrayList<Message>>();
        	fileDataLastRead = new HashMap<String, Integer>();
        }
        else{
        	fileData = allFileData.getData();
        	if(fileData == null)
        		fileData = new HashMap<String, ArrayList<Message>>();
    		fileDataLastRead = allFileData.getLastRead();
    		if(fileDataLastRead == null)
    			fileDataLastRead = new HashMap<String, Integer>();
        }
		fileDataLastRead.put(destination, new Integer(lastRead));
        try {
        	ChatData result = new ChatData();
        	result.setData(fileData);
        	result.setLastRead(fileDataLastRead);
            ops = new FileOutputStream(this.dataStoreFilePath, false);
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(result);
            objOps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(objOps != null) objOps.close();
            } catch (Exception ex){
                 
            }
        }
	}
	
	@Override
	public synchronized void insertAllData(ChatData data) {
		OutputStream ops = null;
        ObjectOutputStream objOps = null;
        try {
            ops = new FileOutputStream(this.dataStoreFilePath, false);
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(data);
            objOps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(objOps != null) objOps.close();
            } catch (Exception ex){
                 
            }
        }
	}

	@Override
	public synchronized ChatData selectAllData() {
		ChatData result = null;
		
		InputStream fileIs = null;
        ObjectInputStream objIs = null;
        try {
            fileIs = new FileInputStream(this.dataStoreFilePath);
            if(fileIs.available() > 0)
            {
            	objIs = new ObjectInputStream(fileIs);
            	result = (ChatData) objIs.readObject();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(objIs != null) objIs.close();
            } catch (Exception ex){
                 
            }
        }
		
		return result;
	}

}
