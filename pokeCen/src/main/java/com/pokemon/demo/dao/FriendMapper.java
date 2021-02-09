package com.pokemon.demo.dao;

import java.util.HashMap;
import java.util.List;

import com.pokemon.demo.vo.Friend;
import com.pokemon.demo.vo.Trainer;

public interface FriendMapper {

	public List<Trainer> searchTrainerById(HashMap<String, String> searchMap);
	public List<Trainer> myFriendList(String id);
	public int insertFriend(Friend friend);
	public List<Trainer> myFollowerList(String id);
	public int acceptFollower(Friend friend);
	public int deleteFriend(Friend friend);
}
