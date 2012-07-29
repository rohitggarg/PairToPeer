package com.ptp.server.processors;

import java.util.HashMap;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Editor;
import com.ptp.dataobject.Pointer;
import com.ptp.dataobject.Result;
import com.ptp.dataobject.ResultType;
import com.ptp.server.util.EditorTextSyncer;

/**
 * Servlet implementation class PairingServlet
 */
public class PairingServlet extends CommandProcessor{
	private static final long serialVersionUID = 1L;

	public static HashMap<String, EditorTextSyncer> sessions;
    
    static {
    	sessions = new HashMap<String, EditorTextSyncer>();
    }

	@Override
	public Result doPost(Command readCommand) throws Exception {
		String parameter = readCommand.getArguments().get(0);
		EditorTextSyncer editorTextSyncer = sessions.get(parameter);
		Editor editor = new Editor();
		editor.setUserId(readCommand.getCommandId());
		editor.setText(editorTextSyncer.getEditor().getText());
		editorTextSyncer.addSession(editor);
		Result result = new Result();
		result.setResultTargetEditor(editor);
		result.setResultType(ResultType.USER);
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws Exception {
		String parameter = readCommand.getArguments().get(0);
		sessions.remove(parameter);
		return null;
	}

	@Override
	public Result doPut(Command readCommand) throws Exception {
		String parameter = readCommand.getArguments().get(0);
		Editor editor = new Editor();
		editor.setUserId(readCommand.getCommandId());
		editor.setText("You can start typing here...");
		editor.setPosition(new Pointer());
		sessions.put(parameter, new EditorTextSyncer(editor));
		Result result = new Result();
		result.setResultTargetEditor(editor);
		result.setResultType(ResultType.USER);
		return result;
	}

}
