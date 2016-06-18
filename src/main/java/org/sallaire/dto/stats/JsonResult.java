package org.sallaire.dto.stats;

import java.util.TreeMap;

public class JsonResult {
	private Long time;
	private TreeMap<String, Long> uploadedById;

	public JsonResult() {
		super();
		uploadedById = new TreeMap<>();
	}

	public JsonResult(Long time) {
		super();
		this.time = time;
		uploadedById = new TreeMap<>();
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public TreeMap<String, Long> getUploadedById() {
		return uploadedById;
	}

	public void setUploadedById(TreeMap<String, Long> uploadedById) {
		this.uploadedById = uploadedById;
	}

}
