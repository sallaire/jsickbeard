package org.sallaire.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController {
	//
	// @Autowired
	// private DownloadService torrentService;
	//
	// @RequestMapping(value = "/settings/provider/{id}", method = RequestMethod.PUT)
	// public void saveProviderSettings(@PathVariable("id") String id, @RequestBody Map<String, String> allRequestParams) {
	// torrentService.saveProvider(id, allRequestParams);
	// }
	//
	// @RequestMapping(value = "/settings/provider/{id}", method = RequestMethod.GET)
	// public Map<String, String> getProviderSettings(@PathVariable("id") String id) {
	// return torrentService.getProviderConfiguration(id);
	// }
	//
	// @RequestMapping(value = "/settings/provider", method = RequestMethod.POST)
	// public void modifyProvidersSettings(@RequestBody LinkedHashMap<String, String> allRequestParams) {
	// LinkedHashMap<String, Boolean> params = new LinkedHashMap<>();
	// for (Entry<String, String> param : allRequestParams.entrySet()) {
	// params.put(param.getKey(), Boolean.valueOf(param.getValue()));
	// }
	// torrentService.saveProviders(params);
	// }
	//
	// @RequestMapping(value = "/settings/client", method = RequestMethod.PUT)
	// public void saveClientSettings(@RequestBody ClientConfiguration config) {
	// torrentService.saveClient(config);
	// }
	//
	// @RequestMapping(value = "/settings/client", method = RequestMethod.GET)
	// public ClientConfiguration getClientSettings() {
	// return torrentService.getClientConfiguration();
	// }
}
