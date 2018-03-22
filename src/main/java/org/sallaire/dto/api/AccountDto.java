package org.sallaire.dto.api;

import java.time.LocalDate;
import java.util.Map;

public class AccountDto {

	private String username;
	private LocalDate inscriptionDate;
	private int nbShows;
	private String bannerUrl;
	private Map<String, Long> showsByGenre;
	private Map<String, Long> showsByNetwork;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LocalDate getInscriptionDate() {
		return inscriptionDate;
	}
	public void setInscriptionDate(LocalDate inscriptionDate) {
		this.inscriptionDate = inscriptionDate;
	}
	public int getNbShows() {
		return nbShows;
	}
	public void setNbShows(int nbShows) {
		this.nbShows = nbShows;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public Map<String, Long> getShowsByGenre() {
		return showsByGenre;
	}
	public void setShowsByGenre(Map<String, Long> showsByGenre) {
		this.showsByGenre = showsByGenre;
	}
	public Map<String, Long> getShowsByNetwork() {
		return showsByNetwork;
	}
	public void setShowsByNetwork(Map<String, Long> showsByNetwork) {
		this.showsByNetwork = showsByNetwork;
	}
	
}
