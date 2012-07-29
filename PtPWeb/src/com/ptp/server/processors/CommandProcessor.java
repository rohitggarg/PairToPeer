package com.ptp.server.processors;

import com.ptp.dataobject.Command;
import com.ptp.dataobject.Result;

public abstract class CommandProcessor {

	public abstract Result doPost(Command readCommand) throws Exception;

	public abstract Result doDelete(Command readCommand) throws Exception;

	public abstract Result doPut(Command readCommand) throws Exception;

}
