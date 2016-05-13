package org.sallaire.dao.db.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.sallaire.dto.user.Status;

@Entity
public class EpisodeStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private LocalDateTime downloadDate;
	private Status status;
	@ElementCollection
	private List<String> downloadedFiles;
	private String provider;

	@ManyToOne
	private Episode episode;
	@ManyToOne
	private TvShowConfiguration showConfiguration;

	public LocalDateTime getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(LocalDateTime downloadDate) {
		this.downloadDate = downloadDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<String> getDownloadedFiles() {
		return downloadedFiles;
	}

	public void setDownloadedFiles(List<String> downloadedFiles) {
		this.downloadedFiles = downloadedFiles;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Episode getEpisode() {
		return episode;
	}

	public void setEpisode(Episode episode) {
		this.episode = episode;
	}

	public TvShowConfiguration getShowConfiguration() {
		return showConfiguration;
	}

	public void setShowConfiguration(TvShowConfiguration showConfiguration) {
		this.showConfiguration = showConfiguration;
	}

}
