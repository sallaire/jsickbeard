package org.sallaire.client.deluge;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(locations = "classpath:deluge.properties", ignoreUnknownFields = true, prefix = "deluge")
public class DelugeConfiguration {
	private String path;
	private String authMethod;
	private String addTorrentMethod;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAuthMethod() {
		return authMethod;
	}

	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}

	public String getAddTorrentMethod() {
		return addTorrentMethod;
	}

	public void setAddTorrentMethod(String addTorrentMethod) {
		this.addTorrentMethod = addTorrentMethod;
	}
}
