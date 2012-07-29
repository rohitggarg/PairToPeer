package com.ptp.dataobject;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Command implements Serializable, Cloneable {
	private static final long serialVersionUID = -1085755367636226958L;
	private UUID commandId;
	private String command;
	private List<String> arguments;
	public UUID getCommandId() {
		return commandId;
	}
	public void setCommandId(UUID commandId) {
		this.commandId = commandId;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public List<String> getArguments() {
		return arguments;
	}
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
}
