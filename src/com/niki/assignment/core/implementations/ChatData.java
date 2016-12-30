package com.niki.assignment.core.implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatData implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> LastRead;
	private HashMap<String, ArrayList<Message>> Data;
	
	public HashMap<String, Integer> getLastRead() {
		return LastRead;
	}
	public void setLastRead(HashMap<String, Integer> lastRead) {
		LastRead = lastRead;
	}
	public HashMap<String, ArrayList<Message>> getData() {
		return Data;
	}
	public void setData(HashMap<String, ArrayList<Message>> data) {
		Data = data;
	}
	
}
