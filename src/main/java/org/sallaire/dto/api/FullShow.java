package org.sallaire.dto.api;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.user.Quality;

public class FullShow {

	private Long id;
	private String imdbId;
	private String name;
	private String originalName;
	private String originalLang;
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
	private Collection<FullEpisode> episodes;
	private Quality quality;
	private String audioLang;
	private List<String> customNames;

	public FullShow() {

	}

	public FullShow(TvShow tvShow, TvShowConfiguration tvShowConfig) {
		if (tvShowConfig != null) {
			this.quality = tvShowConfig.getQuality();
			this.audioLang = tvShowConfig.getAudioLang();
		}
		this.id = tvShow.getId();
		this.imdbId = tvShow.getImdbId();
		this.name = tvShow.getName();
		this.originalName = tvShow.getOriginalName();
		this.originalLang = tvShow.getOriginalLang();
		this.description = tvShow.getDescription();
		this.network = new ArrayList<>(tvShow.getNetwork());
		this.genre = tvShow.getGenre();
		this.runtime = tvShow.getRuntime();
		this.airDay = tvShow.getAirDay();
		this.airTime = tvShow.getAirTime();
		this.status = tvShow.getStatus();
		this.firstAired = tvShow.getFirstAired();
		this.banner = tvShow.getBanner();
		this.fanart = tvShow.getFanart();
		this.poster = tvShow.getPoster();
		this.episodes = new ArrayList<>(tvShow.getEpisodes().size());
	}

	public Collection<FullEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Collection<FullEpisode> episodes) {
		this.episodes = episodes;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getOriginalLang() {
		return originalLang;
	}

	public void setOriginalLang(String originalLang) {
		this.originalLang = originalLang;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public LocalTime getAirTime() {
		return airTime;
	}

	public void setAirTime(LocalTime airTime) {
		this.airTime = airTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	public String getAudioLang() {
		return audioLang;
	}

	public void setAudioLang(String audioLang) {
		this.audioLang = audioLang;
	}

	public List<String> getCustomNames() {
		return customNames;
	}

	public void setCustomNames(List<String> customNames) {
		this.customNames = customNames;
	}

}
