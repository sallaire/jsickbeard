package org.sallaire.dao.db.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class TvShow {

	@Id
	private Long id;
	private String imdbId;
	private String name;
	private String originalName;
	private String originalLang;
	@Lob
	private String description;
	@ElementCollection
	private List<String> network;
	@ElementCollection
	private List<String> countries;
	@ElementCollection
	private List<String> genres;
	private Integer runtime;
	private DayOfWeek airDay;
	private LocalTime airTime;
	private String status;
	private LocalDate firstAired;
	private String banner;
	private String fanart;
	private String poster;
	@ElementCollection
	private List<String> customNames;
	private Integer nbSeasons;
	private Integer nbEpisodes;

	@OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TvShowConfiguration> configurations;

	@OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Episode> episodes;

	public TvShow() {
		super();
		episodes = new ArrayList<>();
	}

	public Set<TvShowConfiguration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(Set<TvShowConfiguration> configurations) {
		this.configurations = configurations;
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

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
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

	public List<String> getCustomNames() {
		return customNames;
	}

	public void setCustomNames(List<String> customNames) {
		this.customNames = customNames;
	}

	public List<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}

	public void addEpisode(Episode episode) {
		episodes.add(episode);
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public void fromShow(TvShow other) {
		this.airDay = other.airDay;
		this.airTime = other.airTime;
		this.banner = other.banner;
		this.description = other.description;
		this.fanart = other.fanart;
		this.firstAired = other.firstAired;
		this.genres = new ArrayList<>(other.genres);
		this.imdbId = other.imdbId;
		this.name = other.name;
		this.network = new ArrayList<>(other.network);
		this.originalLang = other.originalLang;
		this.originalName = other.originalName;
		this.poster = other.poster;
		this.runtime = other.runtime;
		this.status = other.status;
		this.countries = other.countries;
		this.nbSeasons = other.nbSeasons;
		this.nbEpisodes = other.nbEpisodes;
	}

	public Integer getNbSeasons() {
		return nbSeasons;
	}

	public void setNbSeasons(Integer nbSeasons) {
		this.nbSeasons = nbSeasons;
	}

	public Integer getNbEpisodes() {
		return nbEpisodes;
	}

	public void setNbEpisodes(Integer nbEpisodes) {
		this.nbEpisodes = nbEpisodes;
	}
}
