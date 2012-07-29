package com.ptp.server.processors;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Editor;
import com.ptp.dataobject.Pointer;
import com.ptp.dataobject.Result;
import com.ptp.dataobject.ResultType;

public class FileHandler extends CommandProcessor {

	@Override
	public Result doPost(Command readCommand) throws Exception {
		String projectName = readCommand.getArguments().get(0);
		
		String fileName = projectName + File.separator + "src" + File.separator + readCommand.getArguments().get(1);
		Result result = new Result();
		result.setResultType(ResultType.MESSAGE);
		Editor resultTargetEditor = new Editor();
		resultTargetEditor.setPosition(new Pointer());
		File f = new File(ProjectHandler.rootDir, fileName);
		char[] buffer = new char[(int)f.length()];
		new FileReader(f).read(buffer);
		resultTargetEditor.setText(new String(buffer));
		result.setResultTargetEditor(resultTargetEditor);
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws Exception {
		String projectName = readCommand.getArguments().get(0);
		boolean delete = new File(ProjectHandler.rootDir, 
				projectName + File.separator + "src" + File.separator + readCommand.getArguments().get(1)).delete();
		Result result = new Result();
		result.setResultType(ResultType.MESSAGE);
		if(!delete) {
			result.setResultType(ResultType.ERROR);
			result.setResultString("Couldn't delete file");
		}
		return result;
	}

	@Override
	public Result doPut(Command readCommand) throws Exception {
		String projectName = readCommand.getArguments().get(0);
		String fileName = projectName + File.separator + "src" + File.separator + readCommand.getArguments().get(1);
		String fileData = readCommand.getArguments().get(2);
		FileWriter fileWriter = new FileWriter(new File(ProjectHandler.rootDir,fileName));
		fileWriter.write(fileData);
		fileWriter.flush();
		fileWriter.close();
		return new Result();
	}

}
