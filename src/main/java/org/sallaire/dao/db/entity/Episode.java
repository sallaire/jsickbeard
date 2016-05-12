package org.sallaire.dao.db.entity;

import java.time.LocalDate;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Episode {

	@GraphId
	private Long id;
	private Long sourceId;
	private String imdbId;
	private Integer season;
	private Integer episode;
	private String name;
	private LocalDate airDate;
	private String description;

	private TvShow tvShow;

	@Relationship(type = "WITH_CONFIGURATION", direction = Relationship.UNDIRECTED)
	private Set<EpisodeStatus> status;

	public Long getId() {
		return id;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public TvShow getTvShow() {
		return tvShow;
	}

	public void setTvShow(TvShow tvShow) {
		this.tvShow = tvShow;
		tvShow.addEpisode(this);
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public Integer getEpisode() {
		return episode;
	}

	public void setEpisode(Integer episode) {
		this.episode = episode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getAirDate() {
		return airDate;
	}

	public void setAirDate(LocalDate airDate) {
		this.airDate = airDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<EpisodeStatus> getStatus() {
		return status;
	}

	public void setStatus(Set<EpisodeStatus> status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Episode [id=" + id + ", showId=" + tvShow.getId() + ", season=" + season + ", episode=" + episode + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((episode == null) ? 0 : episode.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime * result + ((tvShow == null) ? 0 : tvShow.hashCode());
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
		Episode other = (Episode) obj;
		if (episode == null) {
			if (other.episode != null)
				return false;
		} else if (!episode.equals(other.episode))
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (tvShow == null) {
			if (other.tvShow != null)
				return false;
		} else if (!tvShow.equals(other.tvShow))
			return false;
		return true;
	}

}
