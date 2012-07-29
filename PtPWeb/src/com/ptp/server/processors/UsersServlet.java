package com.ptp.server.processors;

import java.util.HashMap;
import java.util.Map;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Editor;
import com.ptp.dataobject.Result;
import com.ptp.server.util.EditorTextSyncer;

public class UsersServlet extends CommandProcessor{
	
	@Override
	public Result doPost(Command readCommand) throws Exception {
		Result result = new Result();
		result.setResultString("List of users");
		Map<String, Result> results = new HashMap<String, Result>();
		result.setResults(results);
		for (String currentUser : PairingServlet.sessions.keySet()) {
			results.put(currentUser, null);
		}
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws Exception {
		PairingServlet.sessions.remove(readCommand.getArguments().get(0));
		return null;
	}

	@Override
	public Result doPut(Command readCommand) throws Exception {
		Editor editor = new Editor();
		editor.setUserId(readCommand.getCommandId());
		PairingServlet.sessions.put(readCommand.getArguments().get(0), new EditorTextSyncer(editor));
		return null;
	}

}
