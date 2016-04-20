package org.sallaire.dto.api;

import java.util.List;

import org.sallaire.dto.user.Status;

public class UpdateEpisodeStatusParam {
	private Status status;
	private List<Long> ids;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

}
