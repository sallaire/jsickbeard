package org.sallaire.service.client.deluge;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DelugeRequestBody<T> {
	private String method;
	private List<T> params = new ArrayList<>();
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<T> getParams() {
		return params;
	}

	public void setParams(List<T> params) {
		this.params = params;
	}
}
