package com.ptp.server.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ptp.dataobject.Command;

public class ObjectUtilities {
	public static void writeObject(Object obj, HttpServletResponse response) throws IOException {
		new ObjectOutputStream(response.getOutputStream()).writeObject(obj);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	public static Command readCommand (HttpServletRequest request) throws IOException, ClassNotFoundException {
		Object o = new ObjectInputStream(request.getInputStream()).readObject();
		if(o instanceof Command) {
			return (Command) o;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Command readCommandDelete(HttpServletRequest req) {
		
		Enumeration<String> parameters = req.getHeaders("toDelete");
		String command = req.getHeader("command");
		Command cmd = new Command();
		cmd.setCommandId(UUID.fromString(req.getHeader("uuid")));
		cmd.setCommand(command);
		List<String> arguments = null;
		if(parameters != null) {
			arguments = new ArrayList<String>();
			while(parameters.hasMoreElements()) {
				arguments.add(parameters.nextElement());
			}
		}
		cmd.setArguments(arguments);
		return cmd;
	}
}
