package com.own.cms.controller;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.own.cms.entity.AppRole;
import com.own.cms.entity.AppUser;
import com.own.cms.exception.FileStorageException;
import com.own.cms.exception.UserNotfoundException;
import com.own.cms.repository.AppRoleRepository;
import com.own.cms.repository.AppUserRepository;
import com.own.cms.service.FileService;




@Controller
@RequestMapping(value = "/admin/user", name = "_admin_user")
public class UserController {

	@Autowired
	private AppUserRepository userRepo;
	@Autowired
	private AppRoleRepository roleRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private FileService fileService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@GetMapping(value = "/add", name = "_add")
	public String addUser(Model model) {

		AppUser user = new AppUser();

		model.addAttribute("user", user);
		return "admin/user/edit";
	}

	@PostMapping(value = "/add", name = "_save")
	public String saveUser(@Valid @ModelAttribute("user") AppUser appUser,Errors error) {
		
		if(appUser.getPlainPassword()==null ) {
			error.rejectValue("password", null,"The password field is Mandatory");
			
		}
		
		if (error.getErrorCount() > 0) {
			
			return "admin/user/edit";
		}
		
		AppRole role = roleRepo.findByName("ROLE_USER");
		List<AppRole> roles = new ArrayList<AppRole>();
		roles.add(role);
		
		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPlainPassword()));
		appUser.setRoles(roles);
		System.out.println(appUser);
		userRepo.save(appUser);
		return "redirect:/admin/dashboard";
	}

	@GetMapping(value = "/profile", name = "_profile")
	public String userProfile(Model model, Principal principal) {
		// User loginedUser = (User) ((Authentication) principal).getPrincipal();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		AppUser user = userRepo.findByUsername(auth.getName());

		model.addAttribute("user", user);
		return "admin/user/edit";
	}

	@PostMapping(value = "/profile", name = "_profile_update")
	public String userProfileUpdate(@Valid @ModelAttribute("user") AppUser appUser, Errors error,Model model, Principal principal) {
		
		if (error.getErrorCount() > 0) {
			
			return "admin/user/edit";
		}
		else {
		
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			AppUser user = userRepo.findByUsername(auth.getName());
			appUser.setPhoto(user.getPhoto());
			appUser.setRoles(user.getRoles());
			
			if(appUser.getPlainPassword() == null) {
				appUser.setPassword(user.getPassword());
			}else {
				appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPlainPassword()));
			}
			
			
			userRepo.save(appUser);

			Authentication newAuth = new UsernamePasswordAuthenticationToken(user, auth.getCredentials(),
					auth.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(newAuth);
		
 		   return "redirect:/admin/dashboard";
		}
	}
	@GetMapping(value = "/profile/{id}", name ="_profile_by_id")
	public String userProfileEdit(@PathVariable Long id,Model model) {
		// User loginedUser = (User) ((Authentication) principal).getPrincipal();
	

		Optional<AppUser> user = userRepo.findById(id);
		AppUser appUser = user.get();
		model.addAttribute("user", appUser);
		return "admin/user/edit";
	}
	@RequestMapping(value = "/upload/{userId}", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String upload(@RequestParam MultipartFile file,@PathVariable Long userId,Model model) {
		
		AppUser user = userRepo.findById(userId).get();

		fileService.uploadFile(file);

		File imgFile = new File("./upload/" + file.getOriginalFilename());

		if (imgFile.exists()) {
			user.setPhoto(file.getOriginalFilename());
			userRepo.save(user);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AppUser user2 = userRepo.findByUsername(auth.getName());
		Authentication newAuth = new UsernamePasswordAuthenticationToken(user2, auth.getCredentials(),
				auth.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(newAuth);
		model.addAttribute("user", user);
		return "admin/user/edit";
	}
	@RequestMapping(value="/list", name="_list",method = RequestMethod.GET)
	public String getAllUserList(){
		return "admin/user/list";
	}
	
	@RequestMapping(value="/delete/{id}", name="_delete",method = RequestMethod.GET)
	public String deleteUser(@PathVariable Long id){
		
		AppUser user = userRepo.findById(id).get();
		if(user==null) {
			throw new UserNotfoundException("User not found");
		}
		else {
			userRepo.delete(user);
		}
		return "admin/user/list";
	}	
	
	@ExceptionHandler(FileStorageException.class)
	public String handleStorageFileNotFound(FileStorageException e) {

		return "redirect:/failure.html";
	}

}
