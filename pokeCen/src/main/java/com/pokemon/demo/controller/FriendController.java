package com.pokemon.demo.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pokemon.demo.service.FriendService;
import com.pokemon.demo.vo.Friend;
import com.pokemon.demo.vo.Trainer;

@Controller
public class FriendController {

	private static final Logger logger = LoggerFactory.getLogger(FriendController.class);
	
	@Autowired
	FriendService service;
	
	@ResponseBody
	@PostMapping("/searchFriend")
	public List<Trainer> searchFriend(String searchId, Principal principal) {
		List<Trainer> searchList = service.searchTrainerById(principal.getName(), searchId);
		return searchList;
	}
	
	// 내 친구 리스트 가져오기
	@ResponseBody
	@PostMapping("/myFriendList")
	public List<Trainer> myFriendList(Principal principal){
		return service.myFriendList(principal.getName());
	}
	
	// 친구 신청 리스트 가져오기
	@ResponseBody
	@PostMapping("/myFollowerList")
	public List<Trainer> myFollowerList(Principal principal) {
		return service.myFollowerList(principal.getName());
	}
	
	@ResponseBody
	@PostMapping("/followFriend")
	public String followFriend(String friend_id, Principal principal) {
		
		Friend friend = new Friend();
		friend.setId(principal.getName());
		friend.setFriend_id(friend_id);
		friend.setActive(true); // 내 친구 셋팅
		
		Friend targetedFriend = new Friend();
		targetedFriend.setId(friend_id);
		targetedFriend.setFriend_id(principal.getName());
		targetedFriend.setActive(false); // 상대방 친구 셋팅
		
		int cnt = 0;
		cnt += service.insertFriend(friend);
		cnt += service.insertFriend(targetedFriend);
		
		if(cnt > 1) {
			return "success";
		} 
		
		return "fail";
	}
	
	@ResponseBody
	@PostMapping("/acceptFollower")
	public String acceptFollower(String friend_id, Principal principal) {
		
		Friend friend = new Friend();
		friend.setId(principal.getName());
		friend.setFriend_id(friend_id);
		
		int result = service.acceptFollower(friend);
		if(result > 0) return "success";
		return "fail";
	}

	@ResponseBody
	@PostMapping("/denyFollower")
	public String denyFollower(String friend_id, Principal principal) {
		
		Friend friend = new Friend();
		friend.setId(principal.getName());
		friend.setFriend_id(friend_id);
		
		Friend followerFriend = new Friend();
		followerFriend.setId(friend_id);
		followerFriend.setFriend_id(principal.getName());
		
		int cnt = 0;
		cnt += service.denyFollower(friend);
		cnt += service.denyFollower(followerFriend);
		
		if(cnt > 1) return "success";
		else return "fail";
		
	}
}
