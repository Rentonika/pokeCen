package com.pokemon.demo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.pokemon.demo.service.TrainerService;
import com.pokemon.demo.vo.Trainer;


public class TrainerAuthSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	TrainerService service;
	
	private static final Logger logger = LoggerFactory.getLogger(TrainerAuthSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub

		 Trainer trainer = new Trainer();
		 trainer.setId(authentication.getName());
		 trainer.setOnline(true);
		 service.onlineStatus(trainer);
		 
		 logger.info("[온라인] : " + trainer);
		 
		 response.sendRedirect("/"); 
	}
}
