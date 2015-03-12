package org.sallaire.dao.db.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class TvShowConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String location;

	private int quality;

	private boolean flattenFolders;

	private String lang;

	private String audioLang;

	private boolean subtitles;

	private boolean frenchSearch;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "show_id")
	private TvShow tvShow;

	protected TvShowConfiguration() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public boolean isFlattenFolders() {
		return flattenFolders;
	}

	public void setFlattenFolders(boolean flattenFolders) {
		this.flattenFolders = flattenFolders;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getAudioLang() {
		return audioLang;
	}

	public void setAudioLang(String audioLang) {
		this.audioLang = audioLang;
	}

	public boolean isSubtitles() {
		return subtitles;
	}

	public void setSubtitles(boolean subtitles) {
		this.subtitles = subtitles;
	}

	public boolean isFrenchSearch() {
		return frenchSearch;
	}

	public void setFrenchSearch(boolean frenchSearch) {
		this.frenchSearch = frenchSearch;
	}

	public TvShow getTvShow() {
		return tvShow;
	}

	public void setTvShow(TvShow tvShow) {
		this.tvShow = tvShow;
	}

}
