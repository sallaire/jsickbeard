package org.sallaire.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.input.Tailer;
import org.sallaire.service.util.WebSocketLogTailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class LogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

	private static final String LOG_DIR = "logs";

	@Value("${jackbeard.home}")
	private String home;

	private static final Map<String, Tailer> THREADS = Collections.synchronizedMap(new HashMap<>());

	public void startTail(WebSocketSession session, String file) {
		String sessionId = session.getId();
		if (THREADS.containsKey(sessionId)) {
			THREADS.get(sessionId).stop();
		}
		Tailer logThread = new Tailer(Paths.get(home, LOG_DIR, file).toFile(), new WebSocketLogTailer(session), 1000, true);
		new Thread(logThread).start();
		THREADS.put(sessionId, logThread);
	}

	public void stopTail(WebSocketSession session) {
		Tailer thread = THREADS.get(session.getId());
		if (thread != null) {
			thread.stop();
			THREADS.remove(session.getId());
		}
	}

	public List<String> getLogNames() {
		return Arrays.asList(Paths.get(home, LOG_DIR).toFile().list());
	}

	public List<String> getLines(String file, int nbLines) {
		List<String> lines = new ArrayList<>();
		Path logPath = Paths.get(home, LOG_DIR, file);
		try (ReversedLinesFileReader reader = new ReversedLinesFileReader(logPath.toFile(), Charset.forName("UTF-8"))) {
			int i = 0;
			String line = null;
			while (i < nbLines && (line = reader.readLine()) != null) {
				lines.add(line);
				i++;
			}
		} catch (IOException e) {
			LOGGER.error("Unable to read log file {}", logPath);
		}
		Collections.reverse(lines);
		return lines;
	}

}
