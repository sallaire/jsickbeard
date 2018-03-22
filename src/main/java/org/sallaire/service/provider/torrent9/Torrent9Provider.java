package org.sallaire.service.provider.torrent9;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sallaire.dto.user.Quality;
import org.sallaire.service.provider.IProvider;
import org.sallaire.service.provider.Torrent;
import org.sallaire.service.util.RegexFilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Torrent9Provider implements IProvider {

	public static final String ID = "torrent9";

	private static final Logger LOGGER = LoggerFactory.getLogger(Torrent9Provider.class);

	@Autowired
	private RegexFilterConfiguration regex;

	@Override
	public Torrent findEpisode(Collection<String> names, String audioLang, Integer season, Integer number, Quality quality, List<String> excludedFiles) throws IOException {
		String[] urlParts = new String[4];
		urlParts[0] = "http://www.torrent9.red";
		urlParts[1] = "search_torrent";
		urlParts[2] = "fr".equals(audioLang) ? "series-francaise" : "series-vostfr";
		String episodeNumber = String.format("s%02de%02d", season, number);

		for (String name : names) {
			String htmlName = StringUtils.stripAccents(name).replaceAll("\\W", "-").toLowerCase();
			urlParts[3] = htmlName + "-" + episodeNumber + ".html";
			String url = StringUtils.join(urlParts, "/");
			LOGGER.debug("Searching for episode {}, {}, {} with url {}", name, season, number, url);
			Connection conn = Jsoup.connect(StringUtils.join(urlParts, "/"));
			conn.request().header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.90 Safari/537.36 Vivaldi/1.91.867.38");
			Elements searchResults = conn.get().select("table a");
			if (!searchResults.isEmpty()) {
				List<Element> filteredResults = filterResults(searchResults, name, quality, audioLang, season, number, excludedFiles);
				for (Element element : filteredResults) {
					// if results, get it
					String href = element.attr("href");
					if (href != null) {
						conn = Jsoup.connect("http://www.torrent9.cc" + href);
						conn.request().header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.90 Safari/537.36 Vivaldi/1.91.867.38");
						Elements torrentElements = conn.get().select("a.download");
						if (!torrentElements.isEmpty()) {
							String downloadUrl = "http://www.torrent9.cc" + torrentElements.get(0).attr("href");
							String fileName = StringUtils.substringBeforeLast(StringUtils.substringAfterLast(downloadUrl, "/"), ".");

							Path torrentPath = Files.createTempFile(null, null);
							LOGGER.debug("Downloading torrent with url [{}] to [{}]", downloadUrl, torrentPath);
							// Open a URL Stream
							conn = Jsoup.connect(downloadUrl);
							conn.request().header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.90 Safari/537.36 Vivaldi/1.91.867.38");
							Response torrent = conn.ignoreContentType(true).execute();
							// output here
							try (FileOutputStream out = (new FileOutputStream(torrentPath.toFile()))) {
								out.write(torrent.bodyAsBytes()); // resultImageResponse.body() is where the image's contents are.
							}

							return new Torrent(fileName, torrentPath);

						} else {
							LOGGER.warn("No download url could be found in download page {}", torrentElements);
						}
					} else {
						LOGGER.warn("No download url could be found in search result {}", element);
					}
				}
			} else {
				LOGGER.debug("No results found");
			}
		}
		return null;
	}

	private List<Element> filterResults(Elements results, String showName, Quality quality, String lang, Integer season, Integer number, List<String> excludedFiles) {
		// Filter not wanted files
		List<Element> filteredResults = results.stream().filter(t -> {
			if (excludedFiles.contains(t.text())) {
				LOGGER.debug("Exclude result {} because it's an unwanted one", t.text());
				return false;
			} else {
				return true;
			}
		}).collect(Collectors.toList());
		// Filter excluded files
		return filteredResults.stream().filter(t -> {
			return regex.matchEpisode(t.text(), showName, season, number, quality, lang);
		}).collect(Collectors.toList());
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void configurationChanged(Map<String, String> parameters) {
	}

}
