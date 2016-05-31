package org.sallaire.dto.admin;

import java.util.HashMap;
import java.util.Map;

public class JsonResult {
	private Long time;
	private Map<String, Long> uploadedById;

	public JsonResult() {
		super();
		uploadedById = new HashMap<>();
	}

	public JsonResult(Long time) {
		super();
		this.time = time;
		uploadedById = new HashMap<>();
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Map<String, Long> getUploadedById() {
		return uploadedById;
	}

	public void setUploadedById(Map<String, Long> uploadedById) {
		this.uploadedById = uploadedById;
	}

}
