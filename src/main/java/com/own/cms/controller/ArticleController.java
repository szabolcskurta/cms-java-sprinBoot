package com.own.cms.controller;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.own.cms.entity.AppArticle;
import com.own.cms.entity.AppArticleDTO;
import com.own.cms.entity.AppRole;
import com.own.cms.entity.AppUser;
import com.own.cms.entity.AppUserDTO;
import com.own.cms.exception.ArticleNotFoundException;
import com.own.cms.exception.UserNotfoundException;
import com.own.cms.repository.AppArticleRepository;

@Controller
@RequestMapping(value="/admin/article",name="article")
public class ArticleController {
	
	@Autowired
	private AppArticleRepository articleRepo;
	
	@GetMapping(value="/list", name="_list")
	public String articleListview(){
		return "/admin/article/list";
	}
	
	@PostMapping(value ="/list" ,name="_list")
	public  ResponseEntity<Map<String,Object>> articleList() {
		
		List<AppArticle> allArticle = articleRepo.findAll();
		ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<List<AppArticleDTO>>(){}.getType();
	    
		PropertyMap<AppArticle, AppArticleDTO> articleMapping = new PropertyMap<AppArticle, AppArticleDTO>() {
			   protected void configure() {
			      map().setId(source.getId());
			      map().setCreatedAt(source.getCreatedAt());
			      map().setTitle(source.getTitle());
			      map().setCreateBy(source.getCreatedBy().getUsername());
			   }
			};
			modelMapper.addMappings(articleMapping);
			List<AppArticleDTO> articleDTOlist =  modelMapper.map(allArticle,listType);
			Map<String,Object> userDTOListMapped = new HashMap<String, Object>();
		    userDTOListMapped.put("data", articleDTOlist.toArray());
		    userDTOListMapped.put("recordsTotal", articleDTOlist.size());
		    userDTOListMapped.put("recordsFiltered", articleDTOlist.size());
		return ResponseEntity.ok(userDTOListMapped);
	}
	@GetMapping(value ="/add" ,name="_add")
	public String addArticle(Model model){
		AppArticle article = new AppArticle();
		model.addAttribute("article", article);
		return "/admin/article/edit";
	}
	
	@PostMapping(value ="/add" ,name="_add")
	public String saveArticle(@Valid @ModelAttribute("article") AppArticle appArticle,Errors error) {
		
		if(appArticle.getTitle()==null ) {
			error.rejectValue("title", null,"The field is Mandatory");
		}
		
		if (error.getErrorCount() > 0) {
			
			return "admin/article/edit";
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AppUser appUser = (AppUser) auth.getPrincipal();
		appArticle.setCreatedBy(appUser);

		articleRepo.save(appArticle);
		
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping(value ="/edit/{id}" ,name="_edit")
	public String ediArticle(@PathVariable Long id,Model model){
		
		Optional<AppArticle> article = articleRepo.findById(id);
		AppArticle appArticle = article.get();
		model.addAttribute("article", appArticle);
		return "/admin/article/edit";
	}
	@PostMapping(value="/edit/{id}", name="_edit" )
	public String updateArticle(@Valid @ModelAttribute("article") AppArticle appArticle, Errors error){
		if( error.getErrorCount() > 0) {
				return "admin/article/edit";
		}else {
			articleRepo.save(appArticle);
		}
		return "redirect:/admin/article/list";
	}
	
	@Transactional
	@RequestMapping(value="/delete/{id}", name="_delete",method = RequestMethod.POST)
	public String deleteUser(@PathVariable Long id,RedirectAttributes redirAttrs){
		
		AppArticle article = articleRepo.findById(id).get();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		
		if(article==null) {
			throw new ArticleNotFoundException("article not found");
		}
		else {
			articleRepo.delete(article);
		}
		
		redirAttrs.addFlashAttribute("message", "Article Deleted successfuly");
		return "redirect:/admin/article/list";
	}
}