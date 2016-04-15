package org.sallaire.dto.api;

import java.util.List;

import org.sallaire.dto.user.Quality;
import org.sallaire.dto.user.Status;

public class TvShowConfigurationParam {
	private String name;
	private String location;
	private Quality quality;
	private Status status;
	private String audio;
	private List<String> customNames;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public List<String> getCustomNames() {
		return customNames;
	}

	public void setCustomNames(List<String> customNames) {
		this.customNames = customNames;
	}

}
