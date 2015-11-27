package org.sallaire.dto.user;

import java.io.Serializable;

import org.sallaire.dto.metadata.Episode;

public class EpisodeKey implements Serializable {
	private static final long serialVersionUID = -8762116583469141068L;
	private Long showId;
	private Integer season;
	private Integer number;
	private Quality quality;
	private String lang;

	public EpisodeKey() {
		super();
	}

	public EpisodeKey(TvShowConfiguration showConfig, Episode episode) {
		super();
		this.showId = episode.getShowId();
		this.season = episode.getSeason();
		this.number = episode.getEpisode();
		this.quality = showConfig.getQuality();
		this.lang = showConfig.getAudioLang();
	}

	public EpisodeKey(Long showId, Integer season, Integer number, Quality quality, String lang) {
		super();
		this.showId = showId;
		this.season = season;
		this.number = number;
		this.quality = quality;
		this.lang = lang;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((quality == null) ? 0 : quality.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime * result + ((showId == null) ? 0 : showId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EpisodeKey other = (EpisodeKey) obj;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (quality != other.quality)
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (showId == null) {
			if (other.showId != null)
				return false;
		} else if (!showId.equals(other.showId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EpisodeKey [showId=" + showId + ", season=" + season + ", number=" + number + ", quality=" + quality + ", lang=" + lang + "]";
	}

}
