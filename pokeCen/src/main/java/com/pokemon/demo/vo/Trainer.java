package com.pokemon.demo.vo;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class Trainer implements UserDetails, OAuth2User {

	private String TrainerNo;
	private String id;
	private String pw;
	private String name;
	private String country;
	private String phone;
	private String email;
	private String authKey;
	private boolean online;
	private boolean isEnabled;
	private Collection<? extends GrantedAuthority> authorities;
	
	// OAuth2 사용을 위한 필드
	// private DefaultOAuth2User defaultOAuth2User;
	private String nameAttributeKey;
	private Map<String, Object> attributes;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.pw;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		// return this.isEnable;
		// DB 값은 true 인데, 계속 false로 반환
		return this.isEnabled;
	}

}
