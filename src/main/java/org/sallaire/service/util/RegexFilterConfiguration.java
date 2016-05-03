package org.sallaire.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.sallaire.dto.user.Quality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(locations = "classpath:filter.properties", ignoreUnknownFields = true, prefix = "filter")
public class RegexFilterConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegexFilterConfiguration.class);

	private static final String EPISODE_SEP = "[.\\s-_]?";

	private String episodeFormat;
	private List<Pattern> voRegex;
	private List<Pattern> vfRegex;

	private List<Pattern> sdRegex;
	private List<Pattern> p720Regex;
	private List<Pattern> p1080Regex;

	public String getEpisodeFormat() {
		return episodeFormat;
	}

	public void setEpisodeFormat(String episodeFormat) {
		this.episodeFormat = episodeFormat;
	}

	public List<Pattern> getVoRegex() {
		return voRegex;
	}

	public void setVoRegex(String voRegex) {
		this.voRegex = convertStringToRegexs(voRegex);
	}

	public List<Pattern> getVfRegex() {
		return vfRegex;
	}

	public void setVfRegex(String vfRegex) {
		this.vfRegex = convertStringToRegexs(vfRegex);
	}

	public List<Pattern> getSdRegex() {
		return sdRegex;
	}

	public void setSdRegex(String sdRegex) {
		this.sdRegex = convertStringToRegexs(sdRegex);
	}

	public List<Pattern> getLangRegex(String lang) {
		if ("fr".equals(lang)) {
			return getVfRegex();
		} else {
			return getVoRegex();
		}
	}

	public List<Pattern> getEpisodeRegex(String name, Integer season, Integer number) {
		String pattern = name.replace(" ", EPISODE_SEP) + EPISODE_SEP + "S" + StringUtils.leftPad(season.toString(), 2, '0') + EPISODE_SEP + "E" + StringUtils.leftPad(number.toString(), 2, '0');
		return Arrays.asList(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
	}

	public List<Pattern> getP720Regex() {
		return p720Regex;
	}

	public void setP720Regex(String p720Regex) {
		this.p720Regex = convertStringToRegexs(p720Regex);
	}

	public List<Pattern> getP1080Regex() {
		return p1080Regex;
	}

	public void setP1080Regex(String p1080Regex) {
		this.p1080Regex = convertStringToRegexs(p1080Regex);
	}

	public List<Pattern> getQualityRegex(Quality quality) {
		switch (quality) {
		case SD:
			return getSdRegex();
		case P720:
			return getP720Regex();
		case P1080:
			return getP1080Regex();
		default:
			return new ArrayList<>();
		}
	}

	public List<Pattern> convertStringToRegexs(String input) {
		return Arrays.stream(input.split(",")).map(s -> Pattern.compile(s, Pattern.CASE_INSENSITIVE)).collect(Collectors.toList());
	}

	public boolean matchEpisode(String name, String showName, Integer season, Integer episode, Quality quality, String lang) {
		LOGGER.debug("Testing name [{}]", name);
		LOGGER.debug("Testing against show [{}] episode [S{}E{}] regex", showName, season, episode);
		if (!matchRegexs(name, getEpisodeRegex(showName, season, episode))) {
			return false;
		}
		LOGGER.debug("Testing against quality [{}] regex", quality);
		if (!matchRegexs(name, getQualityRegex(quality))) {
			return false;
		}
		LOGGER.debug("Testing against lang [{}] regex", lang);
		if (!matchRegexs(name, getLangRegex(lang))) {
			return false;
		}
		return true;
	}

	private static boolean matchRegexs(String name, List<Pattern> regexs) {
		for (Pattern rx : regexs) {
			if (rx.matcher(name).find()) {
				LOGGER.debug("{} match given regex {}", name, rx.toString());
				return true;
			}
		}
		LOGGER.debug(" {} doesn't match any pattern => reject it", name);
		return false;
	}
}
