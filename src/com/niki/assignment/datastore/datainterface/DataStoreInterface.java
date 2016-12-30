package com.niki.assignment.datastore.datainterface;

import com.niki.assignment.core.implementations.ChatData;
import com.niki.assignment.core.implementations.Message;

public interface DataStoreInterface {
	public void insertData(String destination, Message message);
	public void insertLastRead(String destination, int lastRead);
	public void insertAllData(ChatData data);
	public ChatData selectAllData();
}
