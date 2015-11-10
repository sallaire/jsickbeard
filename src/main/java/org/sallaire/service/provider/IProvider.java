package org.sallaire.service.provider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.sallaire.dto.TvShowConfiguration.Quality;

public interface IProvider {

	String getId();

	Torrent findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality, List<String> excludedFiles) throws IOException;

	void configurationChanged(Map<String, String> parameters);
}
