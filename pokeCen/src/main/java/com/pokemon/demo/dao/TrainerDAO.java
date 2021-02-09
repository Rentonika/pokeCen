package com.pokemon.demo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pokemon.demo.vo.Trainer;

@Repository
public class TrainerDAO {
	
	@Autowired
	SqlSession session;
	
	public Trainer getTrainerById(String id) {
		TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		return mapper.getTrainerById(id);	
	}
	
	public Trainer getTrainerByEmail(String email) {
		TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		return mapper.getTrainerByEmail(email);
	}
	
	public Trainer getTrainerAuthKey(Trainer trainer) {
		TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		return mapper.getTrainerAuthKey(trainer);
	}
	
	public List<String> getTrainerAuthority(String id) {
		TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		return mapper.getTrainerAuthority(id);
	}
	
	public Trainer insertTrainer(Trainer trainer, String role) {
		 TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		 mapper.insertTrainer(trainer);
		 mapper.insertTrainerAuthority(trainer.getId(), role);
		 return trainer;
	}
	
	public void updateAuthKey(Trainer trainer) {
		 TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		 mapper.updateAuthKey(trainer);
	}
	
	public void confirmEmail(Trainer trainer) {
		TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		mapper.confirmEmail(trainer);
	}
	
	public void onlineStatus(Trainer trainer) {
		TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		mapper.onlineStatus(trainer);
	}
	
	public Trainer updateTrainerData(Trainer trainer) {
		TrainerMapper mapper = session.getMapper(TrainerMapper.class);
		mapper.updateTrainerData(trainer);
		return trainer;
	}
	
}
