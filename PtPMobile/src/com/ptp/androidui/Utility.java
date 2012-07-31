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

	public static Result sendCommand(String command, List<String> args,
			Method method) throws IOException, ClassNotFoundException {
		String url = "http://10.0.2.2:8080/PtPWeb";
		URL serverUrl = new URL(url);
		Command cmd = new Command();
		HttpURLConnection serverSide = (HttpURLConnection) serverUrl
				.openConnection();
		serverSide.setRequestMethod(method.name());
		if (method != Method.DELETE) {

			cmd.setCommand(command);
			cmd.setArguments(args);
			cmd.setCommandId(Login.uuid);
			serverSide.setDoOutput(true);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					serverSide.getOutputStream());
			objectOutputStream.writeObject(cmd);
		} else {
			serverSide.setRequestProperty("command", command);
			serverSide.setRequestProperty("uuid", Login.uuid.toString());
			for (String string : args) {
				serverSide.setRequestProperty("toDelete", string);
			}
		}
		try {
			ObjectInputStream inStream = new ObjectInputStream(
					serverSide.getInputStream());
			Result readObject = (Result) inStream.readObject();
			if (readObject.getCommandId().equals(cmd.getCommandId())) {
				return readObject;
			} else
				throw new IOException("Id invalid");
		} catch (EOFException e) {
			return null;
		}
	}

}
