package com.pokemon.demo.service;

import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailSendService {

	@Autowired
	JavaMailSenderImpl mailSender;
	private int size;
	
	// 인증키  생성
	private String getKey(int size) {
		this.size = size;
		return getAuthCode();
	}
	
	// 인증코드 난수 발생
	private String getAuthCode() {
		Random random = new Random();
		StringBuffer str = new StringBuffer();
		int num = 0;
		
		while(str.length() < size) {
			num = random.nextInt(10); // 0~9 에서 랜덤값 추출
			str.append(num);
		}
		
		return str.toString();
	}
	
	// 인증메일 보내기
	public String sendAuthMail(String mail, String id) {
		
		// 6자리 난수 인증번호 생성
		String authKey = getKey(6);
		
		// 바로 아래 null이 뜸 - autowired 했음? 
		MimeMessage mailMessage = mailSender.createMimeMessage();
		String mailContent = "<h1>[이메일 인증]</h1><br><p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>"
                + "<a href='http://localhost:8888/signupConfirm?email=" 
                + mail + "&authKey=" + authKey + "&id=" + id + "' target='_blenk'>이메일 인증 확인</a>";
		
		// 인증 메일 보내기
        try {
            
            mailMessage.setSubject("회원가입 이메일 인증");
            mailMessage.setText(mailContent, "UTF-8", "html");
            mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
            mailSender.send(mailMessage);
            
        } catch (MessagingException e) {
            e.printStackTrace();
        } 
          return authKey;		
	}

}
