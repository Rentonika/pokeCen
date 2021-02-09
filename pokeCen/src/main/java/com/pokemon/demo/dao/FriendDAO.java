package com.pokemon.demo.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pokemon.demo.vo.Friend;
import com.pokemon.demo.vo.Trainer;

@Repository
public class FriendDAO {

	@Autowired
	SqlSession session;
	
	public List<Trainer> searchTrainerById(HashMap<String, String> searchMap) {
		FriendMapper mapper = session.getMapper(FriendMapper.class);
		return mapper.searchTrainerById(searchMap);
	}
	
	public List<Trainer> myFriendList(String id) {
		FriendMapper mapper = session.getMapper(FriendMapper.class);
		return mapper.myFriendList(id);
	}
	
	public int insertFriend(Friend friend) {
		FriendMapper mapper = session.getMapper(FriendMapper.class);
		return mapper.insertFriend(friend);
	}
	
	public List<Trainer> myFollowerList(String id) {
		FriendMapper mapper = session.getMapper(FriendMapper.class);
		return mapper.myFollowerList(id);
	}
	
	public int acceptFollower(Friend friend) {
		FriendMapper mapper = session.getMapper(FriendMapper.class);
		return mapper.acceptFollower(friend);
	}
	
	public int denyFollower(Friend friend) {
		FriendMapper mapper = session.getMapper(FriendMapper.class);
		return mapper.deleteFriend(friend);
	}
}
