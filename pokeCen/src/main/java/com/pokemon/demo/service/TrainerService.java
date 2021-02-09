package com.pokemon.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pokemon.demo.dao.TrainerDAO;
import com.pokemon.demo.service.TrainerService;
import com.pokemon.demo.vo.Trainer;

@Service
public class TrainerService implements UserDetailsService {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	TrainerDAO dao;
	
	private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Trainer foundTrainer = dao.getTrainerById(id);
		if(foundTrainer == null) {	
			logger.info("[로그인 실패]" + id + " 해당 아이디를 찾지 못하였습니다.");
			throw new UsernameNotFoundException(id + "는 존재하지 않는 id입니다.");
		}
				
		// 권한 셋팅
		List<String> string_authorities = dao.getTrainerAuthority(id);
		List<GrantedAuthority> trainerAuthorities = new ArrayList<GrantedAuthority>();
			for(String s : string_authorities) {
				trainerAuthorities.add(new SimpleGrantedAuthority(s));
		}
				
		foundTrainer.setAuthorities(trainerAuthorities);
		logger.info("foundTrainer : " + foundTrainer);
		return foundTrainer; 
	
	}
			
	public Trainer saveTrainer(Trainer trainer, String role) {
				
		trainer.setPw(passwordEncoder.encode(trainer.getPassword()));
		return dao.insertTrainer(trainer, role);
	}
	
	// idCheck 때 사용
	public Trainer checkTrainerById(String id) {
		return dao.getTrainerById(id);
	}
	
	public void updateAuthKey(Trainer trainer) {
		dao.updateAuthKey(trainer);
	}
	
	public Trainer getTrainerAuthKey(Trainer trainer) {
		return dao.getTrainerAuthKey(trainer);
	}
	
	public void confirmEmail(Trainer trainer) {
		dao.confirmEmail(trainer);
	}
	
	public void onlineStatus(Trainer trainer) {
		dao.onlineStatus(trainer);
	}
	
}
