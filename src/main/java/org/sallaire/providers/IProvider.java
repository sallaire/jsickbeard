package org.sallaire.providers;

import org.sallaire.dto.TvShowConfiguration.Quality;

public interface IProvider {

	void findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality);
}
