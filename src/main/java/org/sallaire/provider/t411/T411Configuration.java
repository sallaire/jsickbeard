package org.sallaire.provider.t411;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.sallaire.dto.TvShowConfiguration.Quality;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(locations = "classpath:t411.properties", ignoreUnknownFields = true, prefix = "t411")
public class T411Configuration {
	private String protocol;
	private String host;
	private String searchPath;

	private String categoryKey;
	private String category;

	private String subCategoryKey;
	private String subCategory;

	private String episodeKey;
	private Integer episode;

	private String seasonKey;
	private Integer season;

	private String langKey;
	private List<String> langVf;
	private List<String> langVo;

	private String qualityKey;
	private List<String> qualitySD;
	private List<String> quality720;
	private List<String> quality1080;

	private String epsiodeFormat;

	private List<Pattern> voRegex;
	private List<Pattern> vfRegex;

	private List<Pattern> sdRegex;
	private List<Pattern> p720Regex;
	private List<Pattern> p1080Regex;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSearchPath() {
		return searchPath;
	}

	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategoryKey() {
		return subCategoryKey;
	}

	public void setSubCategoryKey(String subCategoryKey) {
		this.subCategoryKey = subCategoryKey;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public Integer getEpisode() {
		return episode;
	}

	public void setEpisode(Integer episode) {
		this.episode = episode;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public String getEpisodeKey() {
		return episodeKey;
	}

	public void setEpisodeKey(String episodeKey) {
		this.episodeKey = episodeKey;
	}

	public String getSeasonKey() {
		return seasonKey;
	}

	public void setSeasonKey(String seasonKey) {
		this.seasonKey = seasonKey;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public List<String> getLangVf() {
		return langVf;
	}

	public void setLangVf(String langVf) {
		this.langVf = Arrays.asList(langVf.split(","));
	}

	public List<String> getLangVo() {
		return langVo;
	}

	public void setLangVo(String langVo) {
		this.langVo = Arrays.asList(langVo.split(","));
	}

	public List<String> getLangValue(String lang) {
		if ("fr".equals(lang)) {
			return getLangVf();
		} else {
			return getLangVo();
		}
	}

	public String getQualityKey() {
		return qualityKey;
	}

	public void setQualityKey(String qualityKey) {
		this.qualityKey = qualityKey;
	}

	public List<String> getQualitySD() {
		return qualitySD;
	}

	public void setQualitySD(String qualitySD) {
		this.qualitySD = Arrays.asList(qualitySD.split(","));
	}

	public List<String> getQuality720() {
		return quality720;
	}

	public void setQuality720(String quality720) {
		this.quality720 = Arrays.asList(quality720.split(","));
	}

	public List<String> getQuality1080() {
		return quality1080;
	}

	public void setQuality1080(String quality1080) {
		this.quality1080 = Arrays.asList(quality1080.split(","));
	}

	public List<String> getQualityValue(Quality quality) {
		switch (quality) {
		case SD:
			return getQualitySD();
		case P720:
			return getQuality720();
		case P1080:
			return getQuality1080();
		default:
			return new ArrayList<>();
		}
	}

	public String getEpsiodeFormat() {
		return epsiodeFormat;
	}

	public void setEpsiodeFormat(String epsiodeFormat) {
		this.epsiodeFormat = epsiodeFormat;
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

}
