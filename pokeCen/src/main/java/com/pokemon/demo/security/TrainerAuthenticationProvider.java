package com.pokemon.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pokemon.demo.service.TrainerService;
import com.pokemon.demo.vo.Trainer;

public class TrainerAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	
	@Autowired
	private TrainerService service;
	
	@Autowired
	PasswordEncoder passwordEncoder;
		
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			
		String id = (String) authentication.getPrincipal();
		String pw = (String) authentication.getCredentials();
		
		Trainer trainer = (Trainer) service.loadUserByUsername(id);
		
		// 패스워드가 일치하지 않을 경우
		if(!passwordEncoder.matches(pw, trainer.getPassword())) {
			throw new BadCredentialsException(id + "의 패스워드가 일치하지 않습니다.");
		}
		
		// 아이디가 사용 불가능할 경우
		if(!trainer.isEnabled()) {
			throw new BadCredentialsException(id + "는 사용불가능한 아이디입니다. 메일 인증을 해주세요.");
		}
		
		return new UsernamePasswordAuthenticationToken(id, pw, trainer.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		// return true;
		 return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
