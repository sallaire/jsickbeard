package org.sallaire.dto.tvdb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Series")
public class TVDBSearchResult implements ISearchResult {
	private Long id;
	private String language;

	private String name;

	private String overview;

	private String firstAired;

	private String network;

	private Long imdbId;

	public TVDBSearchResult() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@XmlElement(name = "SeriesName")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "Overview")
	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	@XmlElement(name = "FirstAired")
	public String getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	@XmlElement(name = "Network")
	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	@XmlElement(name = "IMDB_ID")
	public Long getImdbId() {
		return imdbId;
	}

	public void setImdbId(Long imdbId) {
		this.imdbId = imdbId;
	}

	@Override
	public Long getRefId() {
		return imdbId;
	}

	@Override
	public String getDate() {
		return firstAired;
	}

}
