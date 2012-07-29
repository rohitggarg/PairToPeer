package com.ptp.androidui;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.ptp.androidui.enums.Method;
import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;

public class Utility {

	/*public static Result sendCommand(Command cmd,
			Method method) throws IOException {
		String url = "http://192.168.1.3:8080/PtPWeb";
		URL url1 = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
		conn.setRequestMethod(method.toString());
		conn.setDoOutput(true);
		cmd.setCommandId(login.uuid);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				conn.getOutputStream());
		objectOutputStream.writeObject(cmd);
		objectOutputStream.flush();
		objectOutputStream.close();
		Result result = null;
		try {
			 result = (Result) new ObjectInputStream(
					conn.getInputStream()).readObject();
		} catch (Exception e) {
			return null;
		}
		if (result.getCommandId().equals(cmd.getCommandId()))
			return result;
		else
			throw new IOException("Command execution failure");
	}*/
	public static Result sendCommand(String command, List<String> args, Method method) throws IOException, ClassNotFoundException {
		String url = "http://10.40.181.210:8080/PtPWeb";
		//String url = "http://192.168.1.6:8080/PtPWeb";
		URL url1 = new URL(url);
		Command cmd = new Command();
		HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
		connection.setRequestMethod(method.name());
		if(method!=Method.DELETE) {
		
		cmd.setCommand(command);
		cmd.setArguments(args);
		cmd.setCommandId(Login.uuid);
		connection.setDoOutput(true);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
		objectOutputStream.writeObject(cmd);
		} else {
		connection.setRequestProperty("command", command);
		connection.setRequestProperty("uuid", Login.uuid.toString());
		for (String string : args) {
		connection.setRequestProperty("toDelete", string);
		}
		}
		try {
		ObjectInputStream inStream = new ObjectInputStream(connection.getInputStream());
		Result readObject = (Result) inStream.readObject();
		if(readObject.getCommandId().equals(cmd.getCommandId())) {
		return readObject;
		} else throw new IOException("Id invalid");
		} catch(EOFException e) {
		return null;
		}
		}

	
}
