package org.sallaire.dto.api;

import org.sallaire.dto.user.Quality;

public class TvShowConfigurationParam {
	private Quality quality;
	private String audio;

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

}
