package org.sallaire.dto.log;

import java.util.List;

public class LogLines {
	private List<LogLine> logLines;
	private int index;
	
	public LogLines(List<LogLine> logLines, int index) {
		super();
		this.logLines = logLines;
		this.index = index;
	}
	
	public List<LogLine> getLogLines() {
		return logLines;
	}
	public void setLogLines(List<LogLine> logLines) {
		this.logLines = logLines;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
