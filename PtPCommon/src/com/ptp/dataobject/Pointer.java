package com.ptp.dataobject;

import java.io.Serializable;

public class Pointer implements Serializable, Cloneable{
	private static final long serialVersionUID = 8842618416858680960L;
	private Integer lineNumber;
	private Integer columnNumber;
	
	
	public Pointer() {
		lineNumber = TOP;
		columnNumber = START;
	}
	
	public Pointer(Integer line, Integer column) {
		lineNumber = line;
		columnNumber = column;
	}
	
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public Integer getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}
	public static final Integer TOP = 1;
	public static final Integer START = 1;
	
}
