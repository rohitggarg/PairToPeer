package com.ptp.server.processors;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;
import com.ptp.dataobject.ResultType;
import com.ptp.server.constant.AppConstants;

/**
 * Servlet implementation class ProjectHandler
 */
public class ProjectHandler extends CommandProcessor {

	private static final String FS_ROOT_DIRECTORY = "fs.root.directory";
	private static final long serialVersionUID = 1L;
	public static File rootDir = new File(ResourceBundle.getBundle(
			AppConstants.CONFIGURATION).getString(FS_ROOT_DIRECTORY));

	static {
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
	}

	private Result doDelete(File path) throws IOException {
		Result result = null;
		if (path.isDirectory()) {
			for (File child : path.listFiles()) {
				doDelete(child);
			}
		}
		if (!path.delete()) {
			result = new Result();
			result.setResultString("Cannot delete path");
			result.setResultType(ResultType.ERROR);
		}
		return result;
	}

	private void setupJava(File project) {
		new File(project, "src").mkdir();
		new File(project, "bin").mkdir();
	}

	@Override
	public Result doPost(Command readCommand) {
		String projectName = readCommand.getArguments().get(0);
		File file = new File(rootDir, projectName);
		Result result = createTree(file);
		return result;
	}

	private Result createTree(File file) {
		Result result = new Result();
		result.setResults(new HashMap<String, Result>());
		for (File subFile : file.listFiles()) {
			if(subFile.isDirectory()) {
				result.getResults().put(subFile.getAbsolutePath(), createTree(subFile));
			}
			else {
				result.getResults().put(subFile.getAbsolutePath(), null);
			}
		}
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws IOException {
		String projectName = readCommand.getArguments().get(0);
		return doDelete(new File(rootDir, projectName));
	}

	@Override
	public Result doPut(Command readCommand) {
		Result result = new Result();
		String projectName = readCommand.getArguments().get(0);
		File project = new File(rootDir, projectName);
		if(!project.mkdir()) {
			result = new Result();
			result.setResultString("Cannot create project directory");
			result.setResultType(ResultType.ERROR);
		} else {
			setupJava(project);
		}
		return result;
	}

}
