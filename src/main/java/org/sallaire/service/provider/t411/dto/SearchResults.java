package org.sallaire.service.provider.t411.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResults {
	private String query;
	private Integer offset;
	private Integer limit;
	private Integer total;

	private List<SearchResult> torrents;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<SearchResult> getTorrents() {
		return torrents;
	}

	public void setTorrents(List<SearchResult> torrents) {
		this.torrents = torrents;
	}

}
