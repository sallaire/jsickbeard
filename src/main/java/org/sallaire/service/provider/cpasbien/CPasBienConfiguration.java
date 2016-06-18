package org.sallaire.service.provider.cpasbien;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(ignoreUnknownFields = true, prefix = "cpasbien")
public class CPasBienConfiguration {

	private String url;
	private String recherche;
	private String langVf;
	private String langVo;

	public String getUrl() {
		return url;
	}

	public String getRecherche() {
		return recherche;
	}

	public String getLangVf() {
		return langVf;
	}

	public String getLangVo() {
		return langVo;
	}

	public String getLangValue(String lang) {
		if ("fr".equals(lang)) {
			return getLangVf();
		} else {
			return getLangVo();
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setRecherche(String recherche) {
		this.recherche = recherche;
	}

	public void setLangVf(String langVf) {
		this.langVf = langVf;
	}

	public void setLangVo(String langVo) {
		this.langVo = langVo;
	}

}
