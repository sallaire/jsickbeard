package org.sallaire.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Episode implements Serializable {

	private static final long serialVersionUID = -7197568948202724063L;

	public enum Status {
		WANTED, SNATCHED, DOWNLOADED, SKIPPED, IGNORED, ARCHIVED, UNAIRED
	}

	private Long id;
	private String imdbId;
	private Long showId;
	private Integer season;
	private Integer episode;
	private String name;
	private LocalDate airDate;
	private String lang;
	private String description;
	private Status status;
	private List<String> fileNames;
	private LocalDate downloadDate;
	private Long lastUpdated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public Integer getEpisode() {
		return episode;
	}

	public void setEpisode(Integer episode) {
		this.episode = episode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getAirDate() {
		return airDate;
	}

	public void setAirDate(LocalDate airDate) {
		this.airDate = airDate;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public void addFileName(String fileName) {
		if (this.fileNames == null) {
			fileNames = new ArrayList<>();
		}
		fileNames.add(fileName);
	}

	public LocalDate getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(LocalDate downloadDate) {
		this.downloadDate = downloadDate;
	}

	public Long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "Episode [id=" + id + ", showId=" + showId + ", season=" + season + ", episode=" + episode + ", name=" + name + "]";
	}
}
