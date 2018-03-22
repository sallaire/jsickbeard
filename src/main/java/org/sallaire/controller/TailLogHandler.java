//package org.sallaire.controller;
//
//import org.sallaire.service.LogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//public class TailLogHandler extends TextWebSocketHandler {
//
//	@Autowired
//	LogService logService;
//
//	@Override
//	public void handleTextMessage(WebSocketSession session, TextMessage message) {
//		if ("stop".equals(message.getPayload())) {
//			logService.stopTail(session);
//		} else {
//			logService.startTail(session, message.getPayload());
//		}
//
//	}
//
//	@Override
//	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		logService.stopTail(session);
//	}
//
//}
