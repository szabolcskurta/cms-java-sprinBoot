package com.own.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping(value ="/" ,name="home")
	public String home(){
		return "frontend/index";
	}

}
