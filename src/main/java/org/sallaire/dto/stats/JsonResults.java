package org.sallaire.dto.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class JsonResults {
	private TreeMap<String, String> nameById;
	private List<JsonResult> results;

	public List<JsonResult> getResults() {
		return results;
	}

	public void setResults(List<JsonResult> results) {
		this.results = results;
	}

	public JsonResults() {
		this.results = new ArrayList<>();
		this.nameById = new TreeMap<>();
	}

	public TreeMap<String, String> getNameById() {
		return nameById;
	}

	public void setNameById(TreeMap<String, String> nameById) {
		this.nameById = nameById;
	}

}
