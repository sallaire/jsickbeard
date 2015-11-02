package org.sallaire.dto.tvdb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Items")
public class UpdateItems {
	private List<Long> showIds;
	private Long time;

	@XmlElement(name = "Series", type = Long.class)
	public List<Long> getShowIds() {
		return showIds;
	}

	public void setShowIds(List<Long> showIds) {
		this.showIds = showIds;
	}

	@XmlElement(name = "Time")
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}
