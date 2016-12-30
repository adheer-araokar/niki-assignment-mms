package com.niki.assignment.core.definitions;

public interface Connection {
	public Object createConnection()throws Exception;
	public Object getConnection(String source)throws Exception;
	public void destroyConnection(String source);
}
