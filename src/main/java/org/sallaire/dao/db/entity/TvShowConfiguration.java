package org.sallaire.dao.db.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.sallaire.dto.user.Quality;

@Entity
public class TvShowConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Quality quality;
	private String audioLang;

	@ManyToMany(mappedBy = "configurations", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> followers;

	@OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EpisodeStatus> episodeStatus;

	@ManyToOne
	private TvShow tvShow;

	public TvShowConfiguration() {
		followers = new HashSet<>();
		episodeStatus = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
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
	}

	public Set<EpisodeStatus> getEpisodeStatus() {
		return episodeStatus;
	}

	public void setEpisodeStatus(Set<EpisodeStatus> episodeStatus) {
		this.episodeStatus = episodeStatus;
	}

}
