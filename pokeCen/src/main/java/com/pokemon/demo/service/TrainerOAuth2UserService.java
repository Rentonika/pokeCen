package com.pokemon.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.pokemon.demo.dao.TrainerDAO;
import com.pokemon.demo.vo.OAuthAttributes;
import com.pokemon.demo.vo.Trainer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TrainerOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

	@Autowired
	private final TrainerDAO dao;
	
	private final static Logger logger = LoggerFactory.getLogger(OAuth2UserService.class);
	// private final HttpSession session;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =  new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		// 소셜 아이디
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
	    
		// 로그인 진행시 키가 되는 pk 값
		String userNameAttributeName = userRequest.getClientRegistration()
	    											.getProviderDetails()
	    											.getUserInfoEndpoint()
	    											.getUserNameAttributeName();
		
		// attributes 객체 생성
		Trainer trainer = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

		// Trainer 로그인
		Trainer socialTrainer = saveOrUpdate(trainer);
		
		// return
		return socialTrainer;
	}
		
	/*
	 * 유저를 확인 시, enable 업데이트, 
	 * 유저를 미확인 시, 새로 insert
	 */
	private Trainer saveOrUpdate(Trainer trainer) {
		
		// 유저 확인 시, 
		Trainer socialTrainer = dao.getTrainerByEmail(trainer.getEmail());
		
		// 아닐 시, 새로 insert 후, 객체 반환
		if(socialTrainer == null) {
			
			// 권한 설정 및 유저 저장
			socialTrainer = dao.insertTrainer(trainer, "ROLE_USER");
			
			// 저장 후, 반환 시 권한 설정	
			List<GrantedAuthority> trainerAuthorities = new ArrayList<GrantedAuthority>();
			trainerAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			trainer.setAuthorities(trainerAuthorities);
			
			return socialTrainer;
		}
		
		// 확인한 유저의 신정보 업데이트 및 권한 설정 (이름, 전화번호 정보가 바뀌었을 때를 대비해)
		socialTrainer = dao.updateTrainerData(trainer);
		List<String> string_authorities = dao.getTrainerAuthority(socialTrainer.getId());
		List<GrantedAuthority> trainerAuthorities = new ArrayList<GrantedAuthority>();
			for(String s : string_authorities) {
				trainerAuthorities.add(new SimpleGrantedAuthority(s));
		}
				
		socialTrainer.setAuthorities(trainerAuthorities);
		
		return socialTrainer;
	
	}
 
}
