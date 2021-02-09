package com.pokemon.demo.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokemon.demo.dao.FriendDAO;
import com.pokemon.demo.vo.Friend;
import com.pokemon.demo.vo.Trainer;

@Service
public class FriendService {

	@Autowired
	FriendDAO dao;
	
	// 친구 찾기
	public List<Trainer> searchTrainerById(String id, String searchId) {
		HashMap<String, String> searchMap = new HashMap<>();
		searchMap.put("id", id);
		searchMap.put("searchId", searchId);
		return dao.searchTrainerById(searchMap);
	}
	
	public List<Trainer> myFriendList(String id) {
		return dao.myFriendList(id);
	}
	
	public int insertFriend(Friend friend) {
		return dao.insertFriend(friend);
	}
	
	public List<Trainer> myFollowerList(String id) {
		return dao.myFollowerList(id);
	}
	
	public int acceptFollower(Friend friend) {
		return dao.acceptFollower(friend);
	}
	
	public int denyFollower(Friend friend) {
		return dao.denyFollower(friend);
	}
	
}
