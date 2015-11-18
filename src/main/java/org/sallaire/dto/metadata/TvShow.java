package org.sallaire.dto.metadata;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TvShow implements Serializable {

	private static final long serialVersionUID = 3366129342784157252L;

	private Long id;
	private String imdbId;
	private String name;
	private String description;
	private List<String> network;
	private String genre;
	private Integer runtime;
	private DayOfWeek airDay;
	private LocalTime airTime;
	private String status;
	private LocalDate firstAired;
	private String banner;
	private String fanart;
	private String poster;
	private Long lastUpdated;

	public TvShow() {
		super();
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public List<String> getNetwork() {
		return network;
	}

	public void setNetwork(List<String> network) {
		this.network = network;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	public DayOfWeek getAirDay() {
		return airDay;
	}

	public void setAirDay(DayOfWeek airDay) {
		this.airDay = airDay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalTime getAirTime() {
		return airTime;
	}

	public void setAirTime(LocalTime airTime) {
		this.airTime = airTime;
	}

	public LocalDate getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(LocalDate firstAired) {
		this.firstAired = firstAired;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getFanart() {
		return fanart;
	}

	public void setFanart(String fanart) {
		this.fanart = fanart;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public Long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
