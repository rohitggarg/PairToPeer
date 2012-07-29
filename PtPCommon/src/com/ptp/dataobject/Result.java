package com.ptp.dataobject;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class Result implements Serializable, Cloneable {
	private static final long serialVersionUID = 8774606504584141347L;
	private UUID commandId;
	private String resultType;
	private Map<String, Result> results;
	private String resultString;
	public UUID getCommandId() {
		return commandId;
	}
	public void setCommandId(UUID commandId) {
		this.commandId = commandId;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public Map<String, Result> getResults() {
		return results;
	}
	public void setResults(Map<String, Result> results) {
		this.results = results;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	public Editor getResultTargetEditor() {
		return resultTargetEditor;
	}
	public void setResultTargetEditor(Editor resultTargetEditor) {
		this.resultTargetEditor = resultTargetEditor;
	}
	private Editor resultTargetEditor;
}
