package com.ptp.server.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;
import com.ptp.dataobject.ResultType;
import com.ptp.server.util.EditorTextSyncer;

public class MessageSender extends CommandProcessor {
	
	HashMap<UUID, ArrayList<String>> messages = new HashMap<UUID, ArrayList<String>>();

	@Override
	public Result doPost(Command readCommand) throws Exception {
		ArrayList<String> arrayList = messages.remove(readCommand.getCommandId());
		String messages = "";
		for (String string : arrayList) {
			messages += string + "\n";
		}
		Result result = new Result();
		result.setResultString(messages);
		result.setResultType(ResultType.MESSAGE);
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws Exception {
		messages.remove(readCommand.getCommandId());
		return null;
	}

	@Override
	public Result doPut(Command readCommand) throws Exception {
		String userName = readCommand.getArguments().get(0);
		EditorTextSyncer editorTextSyncer = PairingServlet.sessions.get(userName);
		ArrayList<String> arrayList = messages.get(editorTextSyncer.getEditor().getUserId());
		if(arrayList == null) {
			arrayList = new ArrayList<String>();
			messages.put(editorTextSyncer.getEditor().getUserId(), arrayList);
		}
		arrayList.add(readCommand.getArguments().get(1));
		return null;
	}

}
