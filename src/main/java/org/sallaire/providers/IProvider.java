package org.sallaire.providers;

import java.io.IOException;

import org.sallaire.dto.TvShowConfiguration.Quality;

public interface IProvider {

	void findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality) throws IOException;
}
