package org.sallaire.dto.user;

import java.io.Serializable;
import java.util.List;

public class TvShowConfiguration implements Serializable {

	private static final long serialVersionUID = -3605866887324566453L;

	private long showId;

	private String location;

	private Quality quality;

	private String lang;

	private String audioLang;

	private boolean paused;

	private List<String> customNames;

	public TvShowConfiguration() {
		super();
	}

	public long getId() {
		return showId;
	}

	public void setId(long id) {
		this.showId = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getAudioLang() {
		return audioLang;
	}

	public void setAudioLang(String audioLang) {
		this.audioLang = audioLang;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public List<String> getCustomNames() {
		return customNames;
	}

	public void setCustomNames(List<String> customNames) {
		this.customNames = customNames;
	}

}
