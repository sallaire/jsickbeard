package org.sallaire.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.sallaire.dto.log.LogLines;
import org.sallaire.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class LogController {

	@Autowired
	private LogService logService;

	@GetMapping("/log")
	public List<String> getLogNames() throws Exception {
		return logService.getLogNames();
	}

	@GetMapping(value = "/log", params = { "file", "from", "lines" })
	public LogLines getLog(@RequestParam("file") String file, @RequestParam("from") int from, @RequestParam("lines") int lines, @RequestParam("grep") Optional<String> grep) throws Exception {
		return logService.getLines(file, from, lines, grep);
	}
	
	@GetMapping("/log/tail")
	public SseEmitter tailLog(@RequestParam("file") String file, HttpSession httpSession) throws Exception {
		return logService.startTail(httpSession.getId(), file);
	}
}
