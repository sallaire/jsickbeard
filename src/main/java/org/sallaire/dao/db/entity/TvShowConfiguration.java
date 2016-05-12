package org.sallaire.dao.db.entity;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.sallaire.dto.user.Quality;

@NodeEntity
public class TvShowConfiguration {
	@GraphId
	private Long id;

	private String quality;
	private String audioLang;

	@Relationship(type = "FOLLOWED_BY", direction = Relationship.UNDIRECTED)
	private Set<User> followers;

	@Relationship(type = "WITH_CONFIGURATION", direction = Relationship.UNDIRECTED)
	private Set<EpisodeStatus> episodes;

	private TvShow tvShow;

	public TvShowConfiguration() {
		followers = new HashSet<>();
		episodes = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Quality getQuality() {
		return Quality.valueOf(quality);
	}

	public void setQuality(Quality quality) {
		this.quality = quality.name();
	}

	public String getAudioLang() {
		return audioLang;
	}

	public void setAudioLang(String audioLang) {
		this.audioLang = audioLang;
	}

	public TvShow getTvShow() {
		return tvShow;
	}

	public void setTvShow(TvShow tvShow) {
		this.tvShow = tvShow;
	}

	public Set<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public void addFollower(User follower) {
		this.followers.add(follower);
		follower.getConfigurations().add(this);
	}

	public Set<EpisodeStatus> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Set<EpisodeStatus> episodes) {
		this.episodes = episodes;
	}

}
