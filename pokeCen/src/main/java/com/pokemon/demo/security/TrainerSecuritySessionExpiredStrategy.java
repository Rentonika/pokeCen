package com.pokemon.demo.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.pokemon.demo.service.TrainerService;

public class TrainerSecuritySessionExpiredStrategy implements SessionInformationExpiredStrategy{

	@Autowired
	TrainerService service;
	
	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletResponse response = event.getResponse();
		HttpServletRequest request = event.getRequest();
			
		// DB에 온/오프 상태 변경
		/*
		 * String expiredId = (String) event.getSessionInformation().getPrincipal();
		 * Trainer trainer = new Trainer(); trainer.setOnline(false);
		 * trainer.setId(expiredId); service.onlineStatus(trainer);
		 */
		
		// request 메세지 설정
		request.setAttribute("failureMessage", "세션이 만료되었습니다. 다시 로그인해주세요.");
		
	  	RequestDispatcher dispatcher = request.getRequestDispatcher("/loginPage");
    	dispatcher.forward(request, response);
	}

}
