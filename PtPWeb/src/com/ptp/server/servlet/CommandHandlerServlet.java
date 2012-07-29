package com.ptp.server.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;
import com.ptp.dataobject.ResultType;
import com.ptp.server.processors.CommandProcessor;
import com.ptp.server.processors.CompilationHandler;
import com.ptp.server.processors.ExecutionHandler;
import com.ptp.server.processors.FileHandler;
import com.ptp.server.processors.MessageSender;
import com.ptp.server.processors.PairingServlet;
import com.ptp.server.processors.ProjectHandler;
import com.ptp.server.processors.TypingHandler;
import com.ptp.server.processors.UsersServlet;
import com.ptp.server.util.ObjectUtilities;

/**
 * Servlet implementation class CommandHandlerServlet
 */
public class CommandHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final HashMap<String, CommandProcessor> commandProcessors;
       
	static {
		commandProcessors = new HashMap<String, CommandProcessor>();
		commandProcessors.put("connect", new UsersServlet());
		PairingServlet pairing = new PairingServlet();
		commandProcessors.put("pair", pairing);
		ProjectHandler project = new ProjectHandler();
		commandProcessors.put("project", project);
		FileHandler fileHandler = new FileHandler();
		commandProcessors.put("file", fileHandler);
		commandProcessors.put("message", new MessageSender());
		commandProcessors.put("compile", new CompilationHandler());
		commandProcessors.put("run", new ExecutionHandler());
		commandProcessors.put("type", new TypingHandler());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException(new UnsupportedOperationException());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Command readCommand = ObjectUtilities.readCommand(request);
			Result result = null;
			try{
				result = commandProcessors.get(readCommand.getCommand()).doPost(readCommand);
			} catch (ArrayIndexOutOfBoundsException e){
				result = createArgumentErrorResult(readCommand);
			}
			if(result!=null) {
				result.setCommandId(readCommand.getCommandId());
				ObjectUtilities.writeObject(result, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	private Result createArgumentErrorResult(Command readCommand) {
		Result result = new Result();
		result.setCommandId(readCommand.getCommandId());
		result.setResultType(ResultType.ERROR);
		result.setResultString("Too few arguments");
		return result;
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Command readCommand = ObjectUtilities.readCommandDelete(req);
			Result result = null;
			try{
				result = commandProcessors.get(readCommand.getCommand()).doDelete(readCommand);
			} catch (ArrayIndexOutOfBoundsException e){
				result = createArgumentErrorResult(readCommand);
			}
			if(result!=null) {
				result.setCommandId(readCommand.getCommandId());
				ObjectUtilities.writeObject(result, resp);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Command readCommand = ObjectUtilities.readCommand(req);
			Result result = null;
			try{
				result = commandProcessors.get(readCommand.getCommand()).doPut(readCommand);
			} catch (ArrayIndexOutOfBoundsException e){
				result = createArgumentErrorResult(readCommand);
			}
			if(result!=null) {
				result.setCommandId(readCommand.getCommandId());
				ObjectUtilities.writeObject(result, resp);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
