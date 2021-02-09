package com.pokemon.demo.dao;

import java.util.List;

import com.pokemon.demo.vo.Trainer;

public interface TrainerMapper {
	
	public Trainer getTrainerById(String id);
	public Trainer getTrainerByEmail(String email);
	public Trainer getTrainerAuthKey(Trainer trainer);
	public List<String> getTrainerAuthority(String id);
	public void insertTrainer(Trainer trainer);
	public void insertTrainerAuthority(String id, String authority);
	public void updateAuthKey(Trainer trainer);
	public void confirmEmail(Trainer trainer);
	public void onlineStatus(Trainer trainer);
	public void updateTrainerData(Trainer trainer);

}
