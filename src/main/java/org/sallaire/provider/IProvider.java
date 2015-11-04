package org.sallaire.provider;

import java.io.IOException;

import org.sallaire.dto.TvShowConfiguration.Quality;

public interface IProvider {

	Torrent findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality) throws IOException;
}
