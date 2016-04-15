package org.sallaire.dto.api;

import java.util.Collection;

import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.user.TvShowConfiguration;

public class FullShow {
	private Collection<FullEpisode> episodes;
	private TvShow show;
	private TvShowConfiguration showConfig;

	public FullShow(TvShow show, TvShowConfiguration showConfig, Collection<FullEpisode> episodes) {
		this.episodes = episodes;
		this.show = show;
		this.showConfig = showConfig;
	}

	public Collection<FullEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Collection<FullEpisode> episodes) {
		this.episodes = episodes;
	}

	public TvShow getShow() {
		return show;
	}

	public void setShow(TvShow show) {
		this.show = show;
	}

	public TvShowConfiguration getShowConfig() {
		return showConfig;
	}

	public void setShowConfig(TvShowConfiguration showConfig) {
		this.showConfig = showConfig;
	}

}
