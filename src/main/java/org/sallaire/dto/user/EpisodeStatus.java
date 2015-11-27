package org.sallaire.dto.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EpisodeStatus implements Serializable {
	private static final long serialVersionUID = 6082327171469321689L;

	private EpisodeKey episodeKey;
	private Status status;
	private List<String> fileNames;
	private LocalDate downloadDate;

	public EpisodeStatus() {
		super();
	}

	public EpisodeStatus(EpisodeKey episodeKey, Status status) {
		super();
		this.episodeKey = episodeKey;
		this.status = status;
	}

	public EpisodeKey getEpisodeKey() {
		return episodeKey;
	}

	public void setEpisodeKey(EpisodeKey episodeKey) {
		this.episodeKey = episodeKey;
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

	public void addFileName(String fileName) {
		if (fileNames == null) {
			fileNames = new ArrayList<>();
		}
		fileNames.add(fileName);
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public LocalDate getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(LocalDate downloadDate) {
		this.downloadDate = downloadDate;
	}

	@Override
	public String toString() {
		return "EpisodeStatus [episodeKey=" + episodeKey + ", status=" + status + ", fileNames=" + fileNames + ", downloadDate=" + downloadDate + "]";
	}
}
