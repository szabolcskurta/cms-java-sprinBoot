package com.own.cms.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.own.cms.entity.AppUser;
import com.own.cms.exception.FileStorageException;
import com.own.cms.repository.AppUserRepository;
import com.own.cms.service.FileService;


@Controller
@RequestMapping(value = "/admin", name = "admin")
public class AdminController {


	@Autowired
	private AppUserRepository userRepo;
	

	@GetMapping(value = "/dashboard", name = "_dashbord")
	public String Dashboard(Model model, Principal principal, Authentication authentication) {
		// User loginedUser = (User) ((Authentication) principal).getPrincipal();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
		AppUser user = userRepo.findByUsername(auth.getName());
		model.addAttribute("user", user);
		return "admin/dashboard";
	}
    

}
