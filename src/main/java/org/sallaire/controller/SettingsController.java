package org.sallaire.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sallaire.dao.db.entity.ClientConfiguration;
import org.sallaire.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController {

	@Autowired
	private DownloadService torrentService;

	@PutMapping(value = "/settings/provider/{id}")
	public void saveProviderSettings(@PathVariable("id") String id, @RequestBody Map<String, String> allRequestParams) {
		torrentService.saveProvider(id, allRequestParams);
	}

	@GetMapping(value = "/settings/provider/{id}")
	public Map<String, String> getProviderSettings(@PathVariable("id") String id) {
		return torrentService.getProviderConfiguration(id);
	}

	@PostMapping(value = "/settings/provider")
	public void modifyProvidersSettings(@RequestBody LinkedHashMap<String, String> allRequestParams) {
		LinkedHashMap<String, Boolean> params = new LinkedHashMap<>();
		for (Entry<String, String> param : allRequestParams.entrySet()) {
			params.put(param.getKey(), Boolean.valueOf(param.getValue()));
		}
		torrentService.saveProviders(params);
	}

	@PutMapping(value = "/settings/client")
	public void saveClientSettings(@RequestBody ClientConfiguration config) {
		torrentService.saveClient(config);
	}

	@GetMapping(value = "/settings/client")
	public ClientConfiguration getClientSettings() {
		return torrentService.getClientConfiguration();
	}
}
