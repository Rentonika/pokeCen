package com.pokemon.demo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.pokemon.demo.service.TrainerService;
import com.pokemon.demo.vo.Trainer;

public class TrainerLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	TrainerService service;	
	
	private static final Logger logger = LoggerFactory.getLogger(TrainerAuthSuccessHandler.class);
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		 if (authentication != null && authentication.getDetails() != null) {        
			 try {
				 // 온라인 상태 변경
				 Trainer trainer = new Trainer(); 
				 trainer.setId(authentication.getName());
				 trainer.setOnline(false);
				 service.onlineStatus(trainer);
				 
				 // 세션 삭제
	             request.getSession().invalidate();
	             
	             logger.info("[오프라인] : " + trainer);
	            
			 } catch (Exception e) {
	                e.printStackTrace();
	            }
	     }
		 response.sendRedirect("/loginPage");   
	}
}
