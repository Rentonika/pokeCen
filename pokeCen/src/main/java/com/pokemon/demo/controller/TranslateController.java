package com.pokemon.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TranslateController {

	@GetMapping("/translatePage")
	public String translatePage() {
		
		return "trainer/translatePage";
	}
	
}
