package com.pokemon.demo.vo;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
public class OAuthAttributes {
	
	private Map<String, Object> attributes;
	private String registrationId;
	private String nameAttributeKey;
	private String name;
	private String email;
	
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String id, String registrationId) {
		this.attributes = attributes;
		this.registrationId = registrationId;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
	}
	
	public static Trainer of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		if(registrationId.equals("naver")) return ofNaver(registrationId, userNameAttributeName, attributes);
		else if(registrationId.equals("kakao")) return ofKakao(registrationId, userNameAttributeName, attributes);
		
		return ofGoogle(registrationId, userNameAttributeName, attributes);
	}
	
	/*
	 * unchecked : 카카오 유저 정보 중첩 map
	 */
	@SuppressWarnings("unchecked")
	private static Trainer ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");

		/*
		return OAuthAttributes.builder().name((String)naverAccount.get("name"))
										.email((String)naverAccount.get("email"))
										.attributes(naverAccount)
										.nameAttributeKey(userNameAttributeName)
										.registrationId(registrationId)
										.build();
		*/
		
		Trainer trainer = new Trainer();
		trainer.setId((String)naverAccount.get("id"));
		trainer.setPw("naver");
		trainer.setName((String)naverAccount.get("name"));
		trainer.setEmail((String)naverAccount.get("email"));
		trainer.setPhone((String)naverAccount.get("mobile"));
		trainer.setEnabled(true);
		
		return trainer;
	}

	
	/*
	 * unchecked : 카카오 유저 정보 중첩 map
	 */
	@SuppressWarnings("unchecked")
	private static Trainer ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		
		/*
		profile.put("email", kakaoAccount.get("email"));
		profile.put("username", profile.get("nickname"));
		profile.put("id", attributes.get("id"));
		
		
		
		OAuthAttributes.builder().name((String)profile.get("username"))
										.email((String)profile.get("email"))
										.attributes(profile)
										.nameAttributeKey(userNameAttributeName)
										.registrationId(registrationId)
										.build();
		
		*/
		
		Trainer trainer = new Trainer();
		// 속성이 integer였음..
		trainer.setId(attributes.get("id").toString());
		trainer.setPw("kakao");
		trainer.setName((String)profile.get("nickname"));
		trainer.setEmail((String)kakaoAccount.get("email"));
		trainer.setEnabled(true);
		
		return trainer;
	}
	
	private static Trainer ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		
		Trainer trainer = new Trainer();
		
		trainer.setId(attributes.get("sub").toString());
		trainer.setPw("google");
		trainer.setName(attributes.get("name").toString());
		trainer.setEmail(attributes.get("email").toString());
		trainer.setEnabled(true);
		
		return trainer;

	}

	/*
	public Trainer NaverAttributeToTrainer(OAuthAttributes attributes) {
		
		Trainer trainer = new Trainer();
		trainer.setId((String)attributes.getAttributes().get("id"));
		trainer.setPw("naver");
		trainer.setName(attributes.getName());
		trainer.setEmail(attributes.getEmail());
		trainer.setPhone((String)attributes.getAttributes().get("mobile"));
		trainer.setEnabled(true);
		
		return trainer;
		
	}
	
	public Trainer KakaoAttributeToTrainer(OAuthAttributes attributes) {
		
		Trainer trainer = new Trainer();
		trainer.setId((String)attributes.getAttributes().get("id"));
		trainer.setPw("kakao");
		trainer.setName(attributes.getName());
		trainer.setEmail(attributes.getEmail());
		trainer.setEnabled(true);
		
		return trainer;
	}
	*/
}
