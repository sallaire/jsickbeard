package org.sallaire.controller;

import java.util.List;

import org.sallaire.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

	@Autowired
	private LogService logService;

	@GetMapping("/logs")
	public List<String> getLogNames() throws Exception {
		return logService.getLogNames();
	}

	@GetMapping("/log")
	public List<String> getLog(@RequestParam("file") String file, @RequestParam("lines") int lines) throws Exception {
		return logService.getLines(file, lines);
	}
}
