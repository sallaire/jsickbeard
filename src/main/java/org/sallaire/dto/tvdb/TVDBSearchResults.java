package org.sallaire.dto.tvdb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Data")
public class TVDBSearchResults {
	
	List<TVDBSearchResult> results;

	public TVDBSearchResults() {
		super();
	}

	@XmlElement(name = "Series", type = TVDBSearchResult.class)
	public List<TVDBSearchResult> getResults() {
		return results;
	}

	public void setResults(List<TVDBSearchResult> results) {
		this.results = results;
	}
}
