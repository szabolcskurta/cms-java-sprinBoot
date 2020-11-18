package com.own.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping(value="/login" ,name="login")
	public  String login(Model model, String error, String logout) {
		 if (error != null)
	            model.addAttribute("error", "Your username and password is invalid.");
		return "admin/login";
	}

	
	
}
