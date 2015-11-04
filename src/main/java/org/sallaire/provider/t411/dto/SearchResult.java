package org.sallaire.provider.t411.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
	private Long id;
	private String name;
	private String rewriteName;
	private Integer seeders;
	private Integer leechers;
	private Integer comments;
	private boolean isVerified;
	private String added;
	private Long size;
	private Integer timesCompleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("rewritename")
	public String getRewriteName() {
		return rewriteName;
	}

	public void setRewriteName(String rewriteName) {
		this.rewriteName = rewriteName;
	}

	public Integer getSeeders() {
		return seeders;
	}

	public void setSeeders(Integer seeders) {
		this.seeders = seeders;
	}

	public Integer getLeechers() {
		return leechers;
	}

	public void setLeechers(Integer leechers) {
		this.leechers = leechers;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getAdded() {
		return added;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@JsonProperty("times_completed")
	public Integer getTimesCompleted() {
		return timesCompleted;
	}

	public void setTimesCompleted(Integer timesCompleted) {
		this.timesCompleted = timesCompleted;
	}

}
