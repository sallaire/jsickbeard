package org.sallaire.dto.api;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.user.Quality;

public class ShowDto {

	private Long id;
	private String imdbId;
	private String name;
	private String originalName;
	private String originalLang;
	private String description;
	private List<String> network;
	private List<String> countries;
	private String genre;
	private Integer runtime;
	private DayOfWeek airDay;
	private LocalTime airTime;
	private String status;
	private LocalDate firstAired;
	private String banner;
	private String fanart;
	private String poster;
	private TreeMap<Integer, List<EpisodeDto>> episodes = new TreeMap<>();
	private Quality quality;
	private String audioLang;
	private List<String> customNames;
	private boolean isFollowed;
	private Integer nbSeasons;
	private Integer nbEpisodes;

	public ShowDto() {
	}

	public ShowDto(SearchResult searchResult, boolean isFollowed) {
		this.id = searchResult.getId();
		if (StringUtils.isNotEmpty(searchResult.getFirstAired())) {
			this.firstAired = LocalDate.parse(searchResult.getFirstAired());
		}
		this.countries = searchResult.getCountries();
		this.name = searchResult.getName();
		this.poster = searchResult.getImage();
		this.isFollowed = isFollowed;
	}

	public ShowDto(TvShow tvShow, TvShowConfiguration tvShowConfig) {
		if (tvShowConfig != null) {
			this.quality = tvShowConfig.getQuality();
			this.audioLang = tvShowConfig.getAudioLang();
		}
		this.isFollowed = true;
		this.id = tvShow.getId();
		this.imdbId = tvShow.getImdbId();
		this.name = tvShow.getName();
		this.originalName = tvShow.getOriginalName();
		this.originalLang = tvShow.getOriginalLang();
		this.description = tvShow.getDescription();
		this.network = new ArrayList<>(tvShow.getNetwork());
		this.countries = new ArrayList<>(tvShow.getCountries());
		this.genre = tvShow.getGenre();
		this.runtime = tvShow.getRuntime();
		this.airDay = tvShow.getAirDay();
		this.airTime = tvShow.getAirTime();
		this.status = tvShow.getStatus();
		this.firstAired = tvShow.getFirstAired();
		this.banner = tvShow.getBanner();
		this.fanart = tvShow.getFanart();
		this.poster = tvShow.getPoster();
		this.nbSeasons = tvShow.getNbSeasons();
		this.nbEpisodes = tvShow.getNbEpisodes();
	}

	public TreeMap<Integer, List<EpisodeDto>> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(TreeMap<Integer, List<EpisodeDto>> episodes) {
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

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
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
