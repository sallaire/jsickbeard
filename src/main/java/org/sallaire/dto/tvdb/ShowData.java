package org.sallaire.dto.tvdb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Data")
public class ShowData {
	private ShowInfo showInfo;
	private List<EpisodeInfo> episodes;

	@XmlElement(name = "Series", type = ShowInfo.class)
	public ShowInfo getShowInfo() {
		return showInfo;
	}

	public void setShowInfo(ShowInfo showInfo) {
		this.showInfo = showInfo;
	}

	@XmlElement(name = "Episode", type = EpisodeInfo.class)
	public List<EpisodeInfo> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<EpisodeInfo> episodes) {
		this.episodes = episodes;
	}
}
