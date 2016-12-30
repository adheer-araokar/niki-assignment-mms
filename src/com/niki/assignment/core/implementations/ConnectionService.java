package com.niki.assignment.core.implementations;

import javax.activation.FileDataSource;
/*import com.sun.jersey.api.*;*/
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.json.*;

import com.niki.assignment.core.implementations.MobileConnection;
import com.niki.assignment.datastore.dataimpl.FileDataStore;
import com.niki.assignment.datastore.datainterface.DataStoreInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Path("/connection")
public class ConnectionService {
	
	private static ConcurrentHashMap<Integer, ServerSocket> portSocket = new ConcurrentHashMap<Integer, ServerSocket>();
	private static ConcurrentHashMap<String, Integer> sourcePort = new ConcurrentHashMap<String, Integer>();
	private static SocketConnection socketConnectionObj = new SocketConnection(portSocket, sourcePort);
	/*private static volatile DataStoreInterface dataStore = loadProperties();*/
	private static DataStoreInterface dataStore = new FileDataStore("D:\\Eclipse-Mars-Practice-Workspace\\niki-assignment-mms-2\\src\\com\\resources\\testdata\\inputFile.data");
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/create")
	public MobileConnection createConnection(MobileConnection inputConnection) throws Exception {
		String source = inputConnection.getSource();
		int port = socketConnectionObj.createConnectionForSource(source);
		inputConnection.setPort(port);
		
		new Thread(new IncomingMessageFromClientThread(socketConnectionObj.getConnectionForSource(source), source, dataStore)).start();
		new Thread(new OutgoingMessageToClientThread(socketConnectionObj.getConnectionForSource(source), source, dataStore)).start();
		
		return inputConnection;
	}
	
	public static DataStoreInterface loadProperties(){
		DataStoreInterface dataSource = null;
		Properties prop = new Properties();
		InputStream input = null;

		try {
			
	        ClassLoader classLoader = ConnectionService.class.getClassLoader();
	        File file = new File(classLoader.getResource("config.properties").getFile());
	        input = new FileInputStream(file);

			prop.load(input);
			String dataStoreType = prop.getProperty("dataStoreType");
			if(dataStoreType.equalsIgnoreCase("File")){
				dataSource = new FileDataStore(prop.getProperty("path"));
			}
			else if(dataStoreType.equalsIgnoreCase("DB")){
				prop.getProperty("dbConnectionString");
				prop.getProperty("tableName");
				//Create a DBDataStoreInterface and pass properties to it.
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataSource;
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/list")
	public MobileConnection listConnection(MobileConnection inputConnection) throws Exception {
		String source = inputConnection.getSource();
		int port = socketConnectionObj.getPortForSource(source);
		inputConnection.setPort(port);
		return inputConnection;
	}

}
