package org.sallaire.service.util;

import java.io.IOException;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketLogTailer extends TailerListenerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketLogTailer.class);

	private WebSocketSession session;

	public WebSocketLogTailer(WebSocketSession session) {
		this.session = session;
	}

	public void handle(String line) {

		try {
			if (StringUtils.isNotEmpty(line)) {
				session.sendMessage(new TextMessage(line));
			}
		} catch (IOException e) {
			LOGGER.error("Unable to send message", e);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Unable to send message", e);
		}
	}

	/**
	 * This method is called if the tailed file is not found.
	 */
	public void fileNotFound() {
		System.out.println("not found");
	}

	public void handle(final Exception ex) {
		LOGGER.error("error", ex);
	}

}
