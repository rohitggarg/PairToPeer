package com.ptp.server.processors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;

public class ExecutionHandler extends CommandProcessor {

	@Override
	public Result doPost(Command readCommand) throws Exception {
		String projectName = readCommand.getArguments().get(0);
		String className = readCommand.getArguments().get(1);
		Class<?> loadClass = new PtPClassLoader(projectName).loadClass(className);
		File tempFile = new File("temp.out");
		PrintStream orig = System.out;
		System.setOut(new PrintStream(tempFile));
		loadClass.getMethod("main", String[].class).invoke(null, (Object)(readCommand.getArguments().subList(1, readCommand.getArguments().size()).toArray(new String[1])));
		System.setOut(orig);
		char[] buffer = new char[(int) tempFile.length()];
		new FileReader(tempFile).read(buffer);
		Result result = new Result();
		result.setResultString(new String(buffer));
		tempFile.delete();
		return result;
	}

	@Override
	public Result doDelete(Command readCommand) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result doPut(Command readCommand) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	class PtPClassLoader extends ClassLoader{
		
		private File binDir;

		public PtPClassLoader(String projectName) {
			this.binDir = new File(new File(ProjectHandler.rootDir,projectName), "bin");
		}
		
		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			
			
			try {
				File file = new File(binDir, name + ".class");
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] b = new byte[(int) file.length()];
				fileInputStream.read(b);
				return defineClass(name, b, 0, b.length);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
			return super.loadClass(name);
		}
		
	}
}
