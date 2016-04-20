package info.movito.themoviedbapi.model.changes;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangedItem implements Serializable {

	private static final long serialVersionUID = -8008227271189827886L;

	@JsonProperty("id")
	private String id;
	@JsonProperty("action")
	private String action;
	@JsonProperty("time")
	private String time;
	@JsonProperty("iso_639_1")
	private String language;
	@JsonProperty("value")
	private Object value;
	@JsonProperty("original_value")
	private Object originalValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(Object originalValue) {
		this.originalValue = originalValue;
	}
}
