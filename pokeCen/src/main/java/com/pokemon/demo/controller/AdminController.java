package com.pokemon.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/settings")
	public String settings() {
		
		return "admin/settings";
	}
	
	@GetMapping("/trainerManagement")
	public String management() {
		
		return "admin/trainerManagement";
	}
}
