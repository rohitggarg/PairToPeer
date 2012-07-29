package com.ptp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ptp.client.connection.ConnectionHandler;
import com.ptp.client.connection.enums.Method;
import com.ptp.dataobject.Result;

public class Runner extends Thread {
	private ConnectionHandler connectionHandler;

	public Runner(ConnectionHandler handler) {
		this.connectionHandler = handler;
	}

	public static final UUID sessionId = UUID.randomUUID();
	public static String url;

	public static void main(String[] args) {
		url = args[0];
		new Runner(new ConnectionHandler()).start();
	}

	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		try {
			while (!(line = br.readLine()).equals("exit")) {
				try {
					List<String> arg = parseArguments(line);
					Result result = connectionHandler.sendCommand(arg.get(0),
							new ArrayList<String>(arg.subList(2,
									arg.size())), Method.valueOf(arg.get(1)));
					if (result != null) {
						System.out.println("Result: " + result.getResultString());
						System.out.println("Results: " + result.getResults());
						System.out.println("ResultEditor: "
								+ result.getResultTargetEditor());
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					System.exit(1);
				}
			}
		} catch (IOException e) {
			System.exit(1);
		}
	}

	private List<String> parseArguments(String line) {
		List<String> command = new ArrayList<String>();
		String[] quotedArgs = line.split("\"");
		for (int i=0;i<quotedArgs.length;i++) {
			if(i%2 == 1)
				command.add(quotedArgs[i]);
			else {
				String[] spaceSeparatedCommand = quotedArgs[i].split(" ");
				for (String string : spaceSeparatedCommand) {
					if(string.length() > 0) {
						command.add(string);
					}
				}
			}
		}
		return command;
	}
}
