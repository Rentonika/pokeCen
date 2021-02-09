package com.pokemon.demo.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.pokemon.demo.vo.Trainer;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@GetMapping("/")
	public String main(UsernamePasswordAuthenticationToken token, HttpSession session) {
		
		session.setAttribute("loginId", token.getName());
		session.setAttribute("authority", token.getAuthorities().toString());
		
		logger.info("[로그인] : " + token.getName() + "님이 로그인하였습니다.");
		return "index";
	}
	
	@GetMapping("/socialLogin")
	public String socialMain(OAuth2AuthenticationToken token, HttpSession session) {
		
		session.setAttribute("name", ((Trainer)token.getPrincipal()).getName());
		session.setAttribute("loginId", token.getName());
		session.setAttribute("authority", token.getAuthorities().toString());
		
		logger.info("[소셜 로그인] : " + ((Trainer)token.getPrincipal()).getName() + "님이 로그인하였습니다.");
		return "index";
	}
}
