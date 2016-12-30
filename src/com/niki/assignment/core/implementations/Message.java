package com.niki.assignment.core.implementations;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private Timestamp messageTS;
	private String Source;
	private String destination;
	private String message;
	
	public Message(Timestamp messageTS, String source, String destination, String message) {
		super();
		this.messageTS = messageTS;
		this.Source = source;
		this.destination = destination;
		this.message = message;
	}
	
	public Timestamp getMessageTS() {
		return messageTS;
	}
	public void setMessageTS(Timestamp messageTS) {
		this.messageTS = messageTS;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
