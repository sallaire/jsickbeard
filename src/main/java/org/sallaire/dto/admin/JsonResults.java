package org.sallaire.dto.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResults {
	private Map<String, String> nameById;
	private List<JsonResult> results;

	public List<JsonResult> getResults() {
		return results;
	}

	public void setResults(List<JsonResult> results) {
		this.results = results;
	}

	public JsonResults() {
		this.results = new ArrayList<>();
		this.nameById = new HashMap<>();
	}

	public Map<String, String> getNameById() {
		return nameById;
	}

	public void setNameById(Map<String, String> nameById) {
		this.nameById = nameById;
	}

}
