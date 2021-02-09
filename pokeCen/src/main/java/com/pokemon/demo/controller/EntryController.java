package com.pokemon.demo.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pokemon.demo.service.EntryService;
import com.pokemon.demo.vo.Box;
import com.pokemon.demo.vo.Entry;
import com.pokemon.demo.vo.Message;
import com.pokemon.demo.vo.Pokemon;

@Controller
public class EntryController {

	@Autowired
	EntryService service;
	
	private SimpMessagingTemplate template;
	
	private static final Logger logger = LoggerFactory.getLogger(EntryController.class);
	
	public EntryController(SimpMessagingTemplate template) {
		this.template = template;
	}
	
	@GetMapping("/myEntry")
	public String myEntry() {
		return "trainer/myEntry";
	}
	
	
	@ResponseBody
	@PostMapping("/pokemonList")
	public List<Pokemon> pokemonList() {
		
		return service.pokemonList();
	}
	
	@ResponseBody
	@PostMapping("/inputEntry")
	public String inputEntry(Entry entry, Principal principal) {
		
		entry.setTrainer_id(principal.getName());
		entry.setStatus("own");
		if(service.insertEntry(entry) > 0) {
			return "success";
		} 
		
		return "fail";
	}
	
	@ResponseBody
	@PostMapping("/myEntryList")
	public List<Entry> myEntryList(Principal principal) {
		
		Entry entry = new Entry();
		entry.setTrainer_id(principal.getName());
		entry.setStatus("own");
		return service.myEntryList(entry);
	}
	
	@ResponseBody
	@PostMapping("/inputEntryInBox")
	public String inputEntryInBox(Entry entry, Principal principal) {
		
		entry.setTrainer_id(principal.getName());
		entry.setStatus("box");
		
		// 박스의 
		Box box = service.getInsertableBox(principal.getName());
		
		// 박스가 다 찼거나, 아예 박스가 없거나
		if(box == null) {
			// 트레이너의 새 박스 만들기
			Box newBox = new Box();
			newBox.setTrainer_id(principal.getName());
			newBox.setTrainer_box_no(1);
			// 1마리 추가
			newBox.setAmount(1);
		
			try {
				// 박스  추가
				service.insertNewBox(newBox);
				// 박스 번호 셋팅
				entry.setBoxNo(service.getInsertableBox(principal.getName()).getBoxno());				
				// 포켓몬 추가
				service.insertEntry(entry);
			} catch (Exception e) {
				return "fail";
				// TODO: handle exception
			}
			
			return "success";

		}  else if(box.getAmount() >= 30) {
			
			try {
				box.setTrainer_box_no(box.getTrainer_box_no()+1);
				box.setAmount(1);
				// 박스  추가
				service.insertNewBox(box);
				// 포켓몬 추가
				entry.setBoxNo(box.getBoxno()+1);
				service.insertEntry(entry);
			} catch (Exception e) {
				return "fail";
				// TODO: handle exception
			}
			
			return "success";
			
		}	else {
		
			// 박스 만들 필요 없이 엔트리(상태:박스)에 포켓몬만 추가
			entry.setBoxNo(box.getBoxno());
			
			try {
				service.insertEntry(entry);
				// amount 숫자 update
				service.updateBoxAmount(box);
			} catch (Exception e) {
				return "fail";
				// TODO: handle exception
			}
			
			return "success";
		}
	}
	
	// index - 교환 제안
	@MessageMapping("/suggestExchange")
	public void suggestExchange(Message msg, Principal principal) {
		msg.setContent("ExSu:" + principal.getName() + "님이 " + msg.getTarget() + "에게 교환을 신청합니다.");
		msg.setId(principal.getName());
		
		template.convertAndSendToUser(msg.getTarget(), "/queue/receive", msg);
	}
	
	@MessageMapping("/replySuggest")
	public void replySuggest(Message msg, Principal principal) {
		String reply = msg.getContent();
		if(reply.equals("X")) {
			msg.setId(principal.getName());
			msg.setContent("ReDe:" + principal.getName() + "님이 교환을 거절하셨습니다.");
			template.convertAndSendToUser(msg.getTarget(), "/queue/receive", msg);
		} else {
			msg.setId(principal.getName());
			msg.setContent("ReAc:" + principal.getName() + "님이 교환을 수락하셨습니다.");
			template.convertAndSendToUser(msg.getTarget(), "/queue/receive", msg);
		}
	}
 
}