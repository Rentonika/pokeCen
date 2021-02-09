package com.pokemon.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.pokemon.demo.service.TrainerOAuth2UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// 비밀번호를 암호화 할 때 사용할 인코더를 미리 빈으로 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 접근 불가 페이지 핸들러 빈 등록
	@Bean 
	public AccessDeniedHandler accessDeniedHandler() { 
		return new TrainerAccessDeniedHandler(); 
	}
	
	 // 로그인 검증을 수행 빈 등록
	 @Bean 
	 public AuthenticationProvider authenticationProvider() { 
	 	return new	TrainerAuthenticationProvider(); 
	 }
	 
	 // 로그인 성공시, 접속 상태 변경을 위한 핸들러
	 @Bean
	 public TrainerAuthSuccessHandler authSuccessHandler() {
		 return new TrainerAuthSuccessHandler();
	 }
	  
	 // 로그인 실패시 핸들러 빈 등록 
	 @Bean public TrainerAuthFailureHandler authFailureHandler() { 
		 return new TrainerAuthFailureHandler(); 
	 }
	 
	 // 로그아웃 시, 접속 상태 변경을 위한 핸들러
	 @Bean
	 public TrainerLogoutSuccessHandler logoutSuccessHandler() {
		 return new TrainerLogoutSuccessHandler();
	 }
	 
	 // 세션 파기시, 온/오프 상태 업데이트를 위한 설정
	 @Bean
	 public TrainerSecuritySessionExpiredStrategy sessionExpiredStrategy() {
		 return new TrainerSecuritySessionExpiredStrategy();
	 }
	 
	 // OAuth 로그인
	 @Autowired
	 private final TrainerOAuth2UserService oAuth2UserService;
	 
	 // OAuth 로그인 성공 핸들러, 접속 상태 변경을 위함
	 @Bean
	 public TrainerOAuthSuccessHandler oAuthSuccessHandler() {
		 return new TrainerOAuthSuccessHandler();
	 }
	 
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	
		// 모든 페이지 잠금 풀림
		// web.ignoring().antMatchers("/**");
		
		// resource 영역만 해제
		web.ignoring().antMatchers("/resources/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		// crof 설정 해제, js 사용을 위함
		http
			.csrf().disable();
			
		// 세션 관련 설정	
		http
			.sessionManagement()
			.maximumSessions(1)	// 중복 로그인 불가
			.maxSessionsPreventsLogin(false) // true -> 세션이 남아있을 시, 로그인이 안됨.
			.expiredSessionStrategy(sessionExpiredStrategy()); // 세션 만료시, DB 업데이트 / 
		
		// 리소스 외의 페이지 인증/비인증/인증권한을 설정
		http
			.authorizeRequests()
				.antMatchers("/loginPage", "/signup", "/create", "/idCheck", "/signupConfirm", "/stomp/**").permitAll() // 누구나 접근 가능
				.antMatchers("/").hasAnyRole("USER", "ADMIN") // USER, ADMIN만 접근 가능
				.antMatchers("/settings", "/trainerManagement").hasRole("ADMIN") // ADMIN만 접근 가능
				.anyRequest().authenticated() // 나머지 요청은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
			.and()
				.formLogin()
					.loginPage("/loginPage") // 로그인 페이지 링크
					.loginProcessingUrl("/login") // 로그인 요청 url
					.usernameParameter("id")
					.passwordParameter("pw")
					.failureHandler(authFailureHandler()) // 로그인 실패 핸들러
					.successHandler(authSuccessHandler()) // 로그인 성공 핸들러
					//.defaultSuccessUrl("/")	// 로그인 성공 후 리다이렉트 주소, 살릴 시 핸들러가 먹지 않음		
			.and()
				.logout()
					.logoutUrl("/logout") // 로그아웃 요청 url
					.logoutSuccessHandler(logoutSuccessHandler())
					.logoutSuccessUrl("/loginPage") // 로그아웃 성공시 리다이렉트 주소
					.invalidateHttpSession(true) // 세션 날리기				
			.and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler()) // 접근 불가 홈페이지 접근 시
			.and()
				.authenticationProvider(authenticationProvider()); // 유저 로그인 검증
		
		// OAuth2 로그인
		http 
			.oauth2Login()
			.successHandler(oAuthSuccessHandler()) // 로그인 성공 핸들러
			.userInfoEndpoint()
			.userService(oAuth2UserService); // 유저 로그인 검증
			
	}
	
	
}

