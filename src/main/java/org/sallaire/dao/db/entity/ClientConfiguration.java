package org.sallaire.dao.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClientConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
