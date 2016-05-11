package org.sallaire.dao.db.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.sallaire.dto.user.Status;

@RelationshipEntity(type = "WITH_CONFIGURATION")
public class EpisodeStatus {
	@GraphId
	private Long id;

	@Property
	private LocalDateTime downloadDate;
	@Property
	private String status;
	@Property
	private List<String> downloadedFiles;
	@Property
	private String provider;

	@StartNode
	private Episode episode;
	@EndNode
	private TvShowConfiguration showConfiguration;

	public LocalDateTime getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(LocalDateTime downloadDate) {
		this.downloadDate = downloadDate;
	}

	public Status getStatus() {
		return Status.valueOf(status);
	}

	public void setStatus(Status status) {
		this.status = status.name();
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
