package org.sallaire.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.sallaire.dto.log.LogLine;
import org.sallaire.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SSELogTailer extends TailerListenerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSELogTailer.class);

	private List<SseEmitter> emitters;
	private String lastLevel = "debug";
	
	private Tailer parentThread;

	public SSELogTailer() {
		emitters = new ArrayList<>();
	}

	public void handle(String line) {

			if (StringUtils.isNotEmpty(line)) {
				String level = line.length() > 29 ? line.substring(24, 29).trim() : null;
				if (!LogService.LOG_LEVELS.contains(level)) {
					level = lastLevel;
				}
				
				for (SseEmitter emitter : emitters) {
					try {
						emitter.send(new LogLine(line, level), MediaType.APPLICATION_JSON);
					} catch (IOException | IllegalArgumentException | IllegalStateException e) {
						LOGGER.error("Unable to send message", e);
						emitter.completeWithError(e);
						removeEmitter(emitter);
					}
				}
				lastLevel = level;
			}
	}

	/**
	 * This method is called if the tailed file is not found.
	 */
	public void fileNotFound() {
		try {
			for (SseEmitter emitter : emitters) {
				emitter.send("File not found");
			}
		} catch (IOException e) {
			LOGGER.error("Unable to send message", e);
		}
		LOGGER.error("File not found");
	}

	public void handle(final Exception ex) {
		LOGGER.error("error", ex);
	}

	public void addEmitter(SseEmitter emitter) {
		emitters.add(emitter);
	}
	
	public Tailer getParentThread() {
		return parentThread;
	}

	public void setParentThread(Tailer parentThread) {
		this.parentThread = parentThread;
	}

	public synchronized void removeEmitter(SseEmitter emitter) {
		emitters.remove(emitter);
		if (emitters.isEmpty()) {
			parentThread.stop();
			parentThread = null;
		}
	}
}
