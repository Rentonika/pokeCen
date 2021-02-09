package com.pokemon.demo.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 로그인 실패 핸들러 
 * @author rento
 *
 */
public class TrainerAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
    	// console 401 unauthorized 발생
    	// 에러 거슬려서 일단 주석
    	// response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  
    	if(exception instanceof UsernameNotFoundException) {
    		// 유저 아이디가 발견되지 않았을 때
    		request.setAttribute("failureMessage", exception.getMessage());
    	} else if (exception instanceof BadCredentialsException) {  		
            // id가 만료된 계정 혹은 비밀번호가 틀렸을 때
    		request.setAttribute("failureMessage", exception.getMessage());
    	}
    	
        // 값을 가지고 해당 주소로 이동 불가
    	// redirect는 attributes 값을 못가져감!
        // response.sendRedirect(request.getContextPath() + "/loginPage");
    	
    	// 로그인 페이지로 다시 포워딩 - POST 방식
    	// /loginPage, get, post 양쪽 방식으로 하는 게 거슬려서 일단 주석
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/loginPage");
    	dispatcher.forward(request, response);
    	
    }
}
