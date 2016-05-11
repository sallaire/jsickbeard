// package org.sallaire.controller;
//
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.stereotype.Controller;
//
// @Controller
// public class LogController {
//
// @MessageMapping("/hello")
// @SendTo("/topic/greetings")
// public Greeting greeting() throws Exception {
// Thread.sleep(3000); // simulated delay
// return new Greeting("Hello !");
// }
// }
