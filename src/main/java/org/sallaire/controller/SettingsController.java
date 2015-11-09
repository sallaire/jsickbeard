package org.sallaire.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sallaire.dto.ClientConfiguration;
import org.sallaire.service.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController {

	@Autowired
	private TorrentService torrentService;

	@RequestMapping(value = "/settings/provider/{id}", method = RequestMethod.PUT)
	public void saveProviderSettings(@PathVariable("id") String id, @RequestParam Map<String, String> allRequestParams) {
		torrentService.saveProvider(id, allRequestParams);
	}

	@RequestMapping(value = "/settings/provider/{id}", method = RequestMethod.GET)
	public Map<String, String> getProviderSettings(@PathVariable("id") String id) {
		return torrentService.getProviderConfiguration(id);
	}

	@RequestMapping(value = "/settings/provider", method = RequestMethod.POST)
	public void modifyProvidersSettings(@RequestParam LinkedHashMap<String, String> allRequestParams) {
		LinkedHashMap<String, Boolean> params = new LinkedHashMap<>();
		for (Entry<String, String> param : allRequestParams.entrySet()) {
			params.put(param.getKey(), Boolean.valueOf(param.getValue()));
		}
		torrentService.saveProviders(params);
	}

	@RequestMapping(value = "/settings/client", method = RequestMethod.PUT)
	public void saveClientSettings(@RequestBody ClientConfiguration config) {
		torrentService.saveClient(config);
	}

	@RequestMapping(value = "/settings/client", method = RequestMethod.GET)
	public ClientConfiguration getClientSettings() {
		return torrentService.getClientConfiguration();
	}
}
