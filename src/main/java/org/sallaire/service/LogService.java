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
import java.util.Optional;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.input.Tailer;
import org.sallaire.dto.log.LogLine;
import org.sallaire.dto.log.LogLines;
import org.sallaire.service.util.SSELogTailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.WebSocketSession;

@Service
public class LogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

	private static final String LOG_DIR = "logs";
	
	public static final List<String> LOG_LEVELS = Arrays.asList("DEBUG", "INFO", "WARN", "ERROR");

	@Value("${jackbeard.home}")
	private String home;

//	private static final Map<String, Tailer> THREADS = Collections.synchronizedMap(new HashMap<>());
	private static final Map<String, SSELogTailer> THREADS = Collections.synchronizedMap(new HashMap<>());

	public void startTail(WebSocketSession session, String file) {
//		String sessionId = session.getId();
//		if (THREADS.containsKey(sessionId)) {
//			THREADS.get(sessionId).stop();
//		}
//		Tailer logThread = new Tailer(Paths.get(home, LOG_DIR, file).toFile(), new WebSocketLogTailer(session), 1000, true);
//		new Thread(logThread).start();
//		THREADS.put(sessionId, logThread);
	}
	
	public SseEmitter startTail(String sessionId, String file) {
		SSELogTailer logTailer = null;
		if (THREADS.containsKey(file)) {
			logTailer = THREADS.get(file);
		} else {
			logTailer = new SSELogTailer();
			THREADS.put(file, logTailer);
		}
		
		
		SseEmitter emitter = new SseEmitter();
		logTailer.addEmitter(emitter);		
		if (logTailer.getParentThread() == null) {
			Tailer logThread = new Tailer(Paths.get(home, LOG_DIR, file).toFile(), logTailer, 1000, true);
			new Thread(logThread).start();
			logTailer.setParentThread(logThread);
		}
		
		return emitter;
	}

	public void stopTail(WebSocketSession session) {
//		Tailer thread = THREADS.get(session.getId());
//		if (thread != null) {
//			thread.stop();
//			THREADS.remove(session.getId());
//		}
	}

	public List<String> getLogNames() {
		return Arrays.asList(Paths.get(home, LOG_DIR).toFile().list());
	}

	public LogLines getLines(String file, int from, int nbLines,  Optional<String> grep) {
		List<String> availableFiles = Arrays.asList(Paths.get(home, LOG_DIR).toFile().list());
		if (availableFiles.contains(file)) {
			List<LogLine> lines = new ArrayList<>();
			Path logPath = Paths.get(home, LOG_DIR, file);
			boolean needLevel = false;
			int lastLine = from;
			try (ReversedLinesFileReader reader = new ReversedLinesFileReader(logPath.toFile(), Charset.forName("UTF-8"))) {
				int i = 0;
				String line = null;
				while ((i < (from + nbLines) || needLevel) && (line = reader.readLine()) != null) {
					if (i >= from) {
						String level = line.length() > 29 ? line.substring(24, 29).trim() : null;
						if (!LOG_LEVELS.contains(level)) {
							level = null;
							needLevel = true;
						} else {
							if (needLevel) {
								completeLevel(lines, level);
							}
							needLevel = false;
						}
						if (i < from + nbLines) {
							lastLine++;
							if (grep.isPresent()) {
								if (line.contains(grep.get())) {
									lines.add(new LogLine(line, level));
								} else {
									i--;
								}
							} else {
								lines.add(new LogLine(line, level));
							}
						}
					}
					i++;
				}
			} catch (IOException e) {
				LOGGER.error("Unable to read log file {}", logPath);
			}
		
			return new LogLines(lines, lastLine);
		} else {
			return null;
		}
	}

	private void completeLevel(List<LogLine> lines, String level) {
		int i = lines.size() - 1;
		while (i > -1 && lines.get(i).getLevel() == null) {
			lines.get(i--).setLevel(level);
		}
	}
}
