package org.sallaire.providers.t411;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(locations = "classpath:t411.properties", ignoreUnknownFields = false, prefix = "t411")
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
	private String langVf;
	private String langVo;

	private String qualityKey;
	private String qualitySD;
	private String quality720;
	private String quality1080;

	private String epsiodeFormat;

	private String voRegex;
	private String vfRegex;

	private String sdRegex;
	private String p720Regex;
	private String p1080Regex;

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

	public String getLangVf() {
		return langVf;
	}

	public void setLangVf(String langVf) {
		this.langVf = langVf;
	}

	public String getLangVo() {
		return langVo;
	}

	public void setLangVo(String langVo) {
		this.langVo = langVo;
	}

	public String getQualityKey() {
		return qualityKey;
	}

	public void setQualityKey(String qualityKey) {
		this.qualityKey = qualityKey;
	}

	public String getQualitySD() {
		return qualitySD;
	}

	public void setQualitySD(String qualitySD) {
		this.qualitySD = qualitySD;
	}

	public String getQuality720() {
		return quality720;
	}

	public void setQuality720(String quality720) {
		this.quality720 = quality720;
	}

	public String getQuality1080() {
		return quality1080;
	}

	public void setQuality1080(String quality1080) {
		this.quality1080 = quality1080;
	}

	public String getEpsiodeFormat() {
		return epsiodeFormat;
	}

	public void setEpsiodeFormat(String epsiodeFormat) {
		this.epsiodeFormat = epsiodeFormat;
	}

	public String getVoRegex() {
		return voRegex;
	}

	public void setVoRegex(String voRegex) {
		this.voRegex = voRegex;
	}

	public String getVfRegex() {
		return vfRegex;
	}

	public void setVfRegex(String vfRegex) {
		this.vfRegex = vfRegex;
	}

	public String getSdRegex() {
		return sdRegex;
	}

	public void setSdRegex(String sdRegex) {
		this.sdRegex = sdRegex;
	}

	public String getP720Regex() {
		return p720Regex;
	}

	public void setP720Regex(String p720Regex) {
		this.p720Regex = p720Regex;
	}

	public String getP1080Regex() {
		return p1080Regex;
	}

	public void setP1080Regex(String p1080Regex) {
		this.p1080Regex = p1080Regex;
	}

}
