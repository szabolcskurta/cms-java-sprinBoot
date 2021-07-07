package com.own.cms.controller;

import java.io.File;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
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
import org.springframework.validation.annotation.Validated;
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

import com.own.cms.entity.AppPage;
import com.own.cms.entity.AppRole;
import com.own.cms.entity.AppUser;
import com.own.cms.entity.AppUserDTO;
import com.own.cms.entity.AppUserGroup;
import com.own.cms.exception.FileStorageException;
import com.own.cms.exception.UserNotFoundException;
import com.own.cms.repository.AppRoleRepository;
import com.own.cms.repository.AppUserRepository;
import com.own.cms.service.AppUserService;
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
	
	@Autowired
	private AppUserService userService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	@RequestMapping(value="/list", name="_list",method = RequestMethod.GET)
	public String getAllUserList(){
		return "admin/user/list";
	}
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getAllUser(){
		
		Map<String,Object> userDTOListMapped =userService.userList(); 
	    return ResponseEntity.ok(userDTOListMapped);
	}

	@GetMapping(value = "/add", name = "_add")
	public String addUser(Model model) {

		AppUser user = new AppUser();

		model.addAttribute("user", user);
		return "admin/user/edit";
	}

	@PostMapping(value = "/add", name = "_save")
	public String saveUser(@Validated(AppUserGroup.class) @ModelAttribute("user") AppUser appUser,Errors error,RedirectAttributes redirAttrs) {
		
		if(appUser.getPlainPassword()==null ) {
			error.rejectValue("password", null,"The password field is Mandatory");
		}
		
		if (error.getErrorCount() > 0) {

			return "admin/user/edit";
		}

		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPlainPassword()));
		System.out.println(appUser);
		userRepo.save(appUser);
		redirAttrs.addFlashAttribute("message", "User Added successfuly");
		return "redirect:/admin/user/list";
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
	public String userProfileUpdate(@Validated(AppUserGroup.class) @ModelAttribute("user") AppUser appUser, Errors error,Model model, Principal principal) {
		
		if (error.getErrorCount() > 0) {
			
			return "admin/user/edit";
		}
		else {
		
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			AppUser user = userRepo.findByUsername(auth.getName());
			appUser.setPhoto(user.getPhoto());
			//appUser.setRoles(user.getRoles());
			
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
	@PostMapping(value = "/profile/{id}", name = "_profile_update")
	public String userUpdate(@Validated(AppUserGroup.class) @ModelAttribute("user") AppUser appUser, Errors error,@PathVariable Long id,RedirectAttributes atts) {
		AppUser user = userRepo.findById(id).get();
		if (error.getErrorCount() > 0) {
			
			return "admin/user/edit";
		}
		else {
		
			
			appUser.setPhoto(user.getPhoto());
			if(appUser.getPlainPassword() == null) {
				appUser.setPassword(user.getPassword());
			}
			else {
				appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPlainPassword()));
			}
			userRepo.save(appUser);

			atts.addFlashAttribute("message", "User "+ appUser.getUsername() +" successfully updated");
			
 		   return "redirect:/admin/user/list";
		}
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
	
	
	@Transactional
	@RequestMapping(value="/delete/{id}", name="_delete",method = RequestMethod.POST)
	public ResponseEntity<Map<String,String>> deleteUser(@PathVariable Long id,RedirectAttributes redirAttrs){
		
		AppUser user = userRepo.findById(id).get();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AppUser loggedinUser =(AppUser) auth.getPrincipal();
		if(loggedinUser.getId()==id) {
			
		}
		if(user==null) {
			throw new UserNotFoundException("User not found");
		}
		else {
			userRepo.delete(user);
		}
		
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "User Deleted successfully");
		
		return ResponseEntity.ok(message);
		
		
	}	
	
	@ModelAttribute
	public void addAttributes(Map<String, Object> model) {
		List<AppRole> roleList = roleRepo.findAll();
		model.put("roleList", roleList);
	}
	
	@ExceptionHandler(FileStorageException.class)
	public String handleStorageFileNotFound(FileStorageException e) {

		return "redirect:/failure.html";
	}
	

}
