package com.ptp.server.processors;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.tools.ToolProvider;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;

public class CompilationHandler extends CommandProcessor {

	@Override
	public Result doPost(Command readCommand) throws Exception {
		String projectName = readCommand.getArguments().get(0);
		ByteArrayOutputStream errStream = new ByteArrayOutputStream();
		ToolProvider.getSystemJavaCompiler().run(
				null,
				null,
				errStream,"-d",ProjectHandler.rootDir.getAbsolutePath() + File.separator
				+ projectName + File.separator + "bin",
				ProjectHandler.rootDir.getAbsolutePath() + File.separator
						+ projectName + File.separator + "src" + File.separator
						+ readCommand.getArguments().get(1));
		Result result = new Result();
		result.setResultString(errStream.toString("UTF-8"));
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws Exception {
		String projectName = readCommand.getArguments().get(0);
		File bin = new File(ProjectHandler.rootDir,projectName + File.separator + "bin");
		for (File child : bin.listFiles()) {
			child.delete();
		}
		return null;
	}

	@Override
	public Result doPut(Command readCommand) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
