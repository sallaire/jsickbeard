package org.sallaire.dto.api;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dto.user.Quality;
import org.sallaire.dto.user.Status;

public class FullEpisode implements Serializable {

	private static final long serialVersionUID = 7719353467335332550L;

	private String showName;
	private String poster;
	private String banner;
	private Long showId;
	private Long episodeId;
	private Integer season;
	private Integer number;
	private String description;
	private LocalDate airDate;
	private String name;
	private Quality quality;
	private String lang;
	private Status status;
	private List<String> fileNames;

	private LocalDateTime downloadDate;

	public FullEpisode() {
	}

	public FullEpisode(TvShow show, Episode episode, EpisodeStatus episodeStatus) {
		setBanner(show.getBanner());
		setPoster(show.getPoster());
		setShowId(show.getId());
		setShowName(show.getName());
		setEpisodeId(episode.getId());
		setDescription(episode.getDescription());
		setName(episode.getName());
		setAirDate(episode.getAirDate());
		setSeason(episode.getSeason());
		setNumber(episode.getEpisode());
		if (episodeStatus != null) {
			setDownloadDate(episodeStatus.getDownloadDate());
			setFileNames(episodeStatus.getDownloadedFiles());
			setLang(episodeStatus.getShowConfiguration().getAudioLang());
			setQuality(episodeStatus.getShowConfiguration().getQuality());
			setStatus(episodeStatus.getStatus());
		}
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getAirDate() {
		return airDate;
	}

	public void setAirDate(LocalDate airDate) {
		this.airDate = airDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
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

	public LocalDateTime getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(LocalDateTime downloadDate) {
		this.downloadDate = downloadDate;
	}

	public Long getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(Long episodeId) {
		this.episodeId = episodeId;
	}

}
