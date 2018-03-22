package org.sallaire.dto.log;

public class LogLine {
	private String message;
	private String level;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public LogLine(String message, String level) {
		super();
		this.message = message;
		this.level = level;
	}
	
	
}
