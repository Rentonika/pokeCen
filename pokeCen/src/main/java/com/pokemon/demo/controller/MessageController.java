package com.pokemon.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.pokemon.demo.vo.Message;

@Controller
public class MessageController{

	private SimpMessagingTemplate template;
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	public MessageController(SimpMessagingTemplate template) {
		this.template = template;
	}
	
	@MessageMapping("/start")
	public void start(Message msg) {
		
		logger.info("통신 시작 : " + msg);
		template.convertAndSendToUser(msg.getTarget(), "/queue/receiveNote", msg);
	}
	
	
	@MessageMapping("/test")
	public void test(Message msg) {
		
		logger.info("버튼 : " + msg);
		template.convertAndSend("/topic/receiveStart", msg);
	}
	
	@MessageMapping("/out")
	public void out(Message msg) {
		
		template.convertAndSendToUser(msg.getTarget(), "/queue/receiveNote", msg);
	}
	
}
