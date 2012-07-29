package com.ptp.client.connection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.ptp.client.Runner;
import com.ptp.client.connection.enums.Method;
import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;

public class ConnectionHandler {
	private URL url;
	public ConnectionHandler() {
		try {
			url = new URL("http://"+Runner.url+"/PtPWeb");
		} catch (MalformedURLException e) {
			System.out.println("URL invalid");
			System.exit(1);
		}
	}
	public Result sendCommand(String command, List<String> args, Method method) throws IOException, ClassNotFoundException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method.name());
		if(method!=Method.DELETE) {
			Command cmd = new Command();
			cmd.setCommand(command);
			cmd.setArguments(args);
			cmd.setCommandId(Runner.sessionId);
			connection.setDoOutput(true);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
			objectOutputStream.writeObject(cmd);
		} else {
			connection.setRequestProperty("command", command);
			connection.setRequestProperty("uuid", Runner.sessionId.toString());
			for (String string : args) {
				connection.setRequestProperty("toDelete", string);
			}
		}
		try {
			ObjectInputStream inStream = new ObjectInputStream(connection.getInputStream());
			Result readObject = (Result) inStream.readObject();
			if(readObject.getCommandId().equals(Runner.sessionId)) {
				return readObject;
			} else throw new IOException("Id invalid");
		} catch(EOFException e) {
			return null;
		}
	}
}
