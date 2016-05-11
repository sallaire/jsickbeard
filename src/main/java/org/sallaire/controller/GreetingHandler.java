// package org.sallaire.controller;
//
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Map;
//
// import org.springframework.web.socket.CloseStatus;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;
//
// public class GreetingHandler extends TextWebSocketHandler {
//
// private static final Map<String, LogThread> THREADS = Collections.synchronizedMap(new HashMap<>());
//
// @Override
// public void afterConnectionEstablished(WebSocketSession session) throws Exception {
// LogThread logThread = new LogThread(session);
// new Thread(logThread).start();
// THREADS.put(session.getId(), logThread);
// }
//
// @Override
// public void handleTextMessage(WebSocketSession session, TextMessage message) {
// try {
// Thread.sleep(3000);
// TextMessage msg = new TextMessage("Hello !");
// session.sendMessage(msg);
// } catch (Exception e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } // simulated delay
// }
//
// @Override
// public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
// LogThread thread = THREADS.get(session.getId());
// thread.stop();
// THREADS.remove(session.getId());
// }
//
// public class LogThread implements Runnable {
//
// private WebSocketSession session;
// private boolean pause = false;
// private boolean run = true;
//
// public LogThread(WebSocketSession session) {
// this.session = session;
// }
//
// public void stop() {
// run = false;
// }
//
// @Override
// public void run() {
// while (true && run) {
// try {
// Thread.sleep(3000);
// TextMessage msg = new TextMessage("{\"Timestamp\":2015071602553825, \"Line\":\"2015-07-16 21:55:38.2576|INFO||This is information.\"}");
// session.sendMessage(msg);
// } catch (Exception e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
// }
// }
//
// }
// }
