package org.sallaire.dao.db.entity;

import java.sql.Time;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TvShow {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private long tvdbId;

	private long tvrId;

	private String tvrName;

	private long imdbId;

	private String network;

	private String genre;

	private int runtime;

	private int airDay;

	private Time airDate;

	private String status;

	private boolean paused;

	private String year;

	private boolean airByDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tvShow")
	private TvShowConfiguration configuration;

	protected TvShow() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTvdbId() {
		return tvdbId;
	}

	public void setTvdbId(long tvdbId) {
		this.tvdbId = tvdbId;
	}

	public long getTvrId() {
		return tvrId;
	}

	public void setTvrId(long tvrId) {
		this.tvrId = tvrId;
	}

	public String getTvrName() {
		return tvrName;
	}

	public void setTvrName(String tvrName) {
		this.tvrName = tvrName;
	}

	public long getImdbId() {
		return imdbId;
	}

	public void setImdbId(long imdbId) {
		this.imdbId = imdbId;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public int getAirDay() {
		return airDay;
	}

	public void setAirDay(int airDay) {
		this.airDay = airDay;
	}

	public Time getAirDate() {
		return airDate;
	}

	public void setAirDate(Time airDate) {
		this.airDate = airDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public boolean isAirByDate() {
		return airByDate;
	}

	public void setAirByDate(boolean airByDate) {
		this.airByDate = airByDate;
	}

	public TvShowConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(TvShowConfiguration configuration) {
		this.configuration = configuration;
	}

}
