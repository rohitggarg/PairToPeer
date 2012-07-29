package com.ptp.server.processors;

import java.util.ArrayList;
import java.util.HashMap;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Editor;
import com.ptp.dataobject.Pointer;
import com.ptp.dataobject.Result;
import com.ptp.server.util.EditorTextSyncer;

public class TypingHandler extends CommandProcessor {

	@Override
	public Result doPost(Command readCommand) throws Exception {
		EditorTextSyncer editorTextSyncer = PairingServlet.sessions.get(readCommand.getArguments().get(0));
		ArrayList<Editor> poll = editorTextSyncer.poll(readCommand.getCommandId());
		Result result = new Result();
		result.setResults(new HashMap<String, Result>());
		Integer i = 0;
		for (Editor editor : poll) {
			Result value = new Result();
			value.setResultTargetEditor(editor);
			result.getResults().put(i.toString(), value);
		}
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws Exception {
		EditorTextSyncer editorTextSyncer = PairingServlet.sessions.get(readCommand.getArguments().get(0));
		editorTextSyncer.removeSession(readCommand.getCommandId());
		return null;
	}

	@Override
	public Result doPut(Command readCommand) throws Exception {
		EditorTextSyncer editorTextSyncer = PairingServlet.sessions.get(readCommand.getArguments().get(0));
		Editor e = new Editor();
		e.setUserId(readCommand.getCommandId());
		e.setText(readCommand.getArguments().get(1));
		e.setPosition(new Pointer(new Integer(readCommand.getArguments().get(2)), new Integer(readCommand.getArguments().get(3))));
		editorTextSyncer.type(e);
		return null;
	}
}
