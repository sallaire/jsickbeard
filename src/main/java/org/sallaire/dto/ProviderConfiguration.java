package org.sallaire.dto;

import java.io.Serializable;
import java.util.Map;

public class ProviderConfiguration implements Serializable {
	private static final long serialVersionUID = -6907592987109452147L;
	private boolean activated;
	private Integer order;

	private Map<String, String> parameters;

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

}
