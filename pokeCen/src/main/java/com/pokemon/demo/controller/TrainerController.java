package com.pokemon.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pokemon.demo.service.MailSendService;
import com.pokemon.demo.service.TrainerService;
import com.pokemon.demo.vo.Trainer;

@Controller
public class TrainerController {
	
	private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

	@Autowired
	TrainerService service;
	
	@Autowired
	MailSendService mailService;
	
	@RequestMapping(value = "loginPage" , method = {RequestMethod.GET, RequestMethod.POST})
	public String loginPage() {
		
		return "trainer/loginPage";
	}
	
	@ResponseBody
	@PostMapping("/idCheck")
	public String idCheck(String id) {
		
		Trainer selectedTrainer = null; 
		selectedTrainer = service.checkTrainerById(id);
		if(selectedTrainer == null) {
			return "success";
		}
		
		logger.info("[가입자 존재] " + selectedTrainer.getId());
		return "fail";
	}
	
	
	@GetMapping("/signup")
	public String signup() {	
		return "trainer/signup";
	}
	
	@PostMapping("/signup")
	public String signup(Trainer trainer, RedirectAttributes rttr) {
		
		Trainer newTrainer = service.saveTrainer(trainer, "ROLE_USER");
		logger.info("[신규 가입자] " + newTrainer);
		if(newTrainer != null) {
		
			// 임의의 authKey 생성 & 이메일 발송
	        String authKey = mailService.sendAuthMail(newTrainer.getEmail(), newTrainer.getId());
	        
	        // 임의의 authKey update
	        newTrainer.setAuthKey(authKey);
	        service.updateAuthKey(newTrainer);
	        
	        // 메세지 전송
	        rttr.addFlashAttribute("emailMessage", "인증 메일이 발송되었습니다. 메일을 확인해주세요.");  	
		}
		
        
		return "redirect:/loginPage";
	}
	
	@GetMapping("/signupConfirm")
	public String signupConfirm(Trainer trainer, RedirectAttributes rttr) {
		
		logger.info("이메일 정보 : " + trainer);
		// 이메일 인증
		Trainer confirmedTrainer = service.getTrainerAuthKey(trainer);
		if(confirmedTrainer != null) {
			service.confirmEmail(confirmedTrainer);
			logger.info("[이메일 인증] " + confirmedTrainer.getId() + " 계정 활성화" );
			rttr.addFlashAttribute("emailMessage", "인증이 완료되었습니다. 가입을 축하드립니다.");
		}
		
		return "redirect:/loginPage";
	}
	
	@GetMapping("/denialedPage")
	public String accesDenialed() {
		return "denial/denialedPage";
	}
	
	@GetMapping("/myPage")
	public String myPage() {
		return "trainer/myPage";
	}
	
	// 관리자 계정 생성
	@GetMapping("/create")
	public void create() {
		
		Trainer trainer = new Trainer();
		trainer.setId("admin");
		trainer.setPw("admin");
		trainer.setName("어드민");
		trainer.setCountry("태초마을");
		trainer.setEmail("admin@kico.co.jp");
		trainer.setPhone("01094443249");
		trainer.setEnabled(true);
		
		service.saveTrainer(trainer, "ROLE_ADMIN");
	}
	
}
