package org.sallaire.dto.configuration;

import java.io.Serializable;

public class ClientConfiguration implements Serializable {

	private static final long serialVersionUID = -3684413973050866576L;

	private String client;
	private String url;
	private String password;
	private Integer stopRatio;
	private Boolean moveShow;
	private String seasonPattern;
	private String downloadDirectory;
	private String label;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStopRatio() {
		return stopRatio;
	}

	public void setStopRatio(Integer stopRatio) {
		this.stopRatio = stopRatio;
	}

	public Boolean getMoveShow() {
		return moveShow;
	}

	public void setMoveShow(Boolean moveShow) {
		this.moveShow = moveShow;
	}

	public String getSeasonPattern() {
		return seasonPattern;
	}

	public void setSeasonPattern(String seasonPattern) {
		this.seasonPattern = seasonPattern;
	}

	public String getDownloadDirectory() {
		return downloadDirectory;
	}

	public void setDownloadDirectory(String downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
