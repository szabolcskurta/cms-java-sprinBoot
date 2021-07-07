package com.own.cms.controller;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.own.cms.entity.AppArticle;
import com.own.cms.entity.AppArticleDTO;
import com.own.cms.entity.AppRole;
import com.own.cms.entity.AppUser;
import com.own.cms.entity.AppUserDTO;
import com.own.cms.exception.ArticleNotFoundException;
import com.own.cms.exception.UserNotFoundException;
import com.own.cms.repository.AppArticleRepository;
import com.own.cms.repository.AppUserRepository;
import com.own.cms.service.FileService;

@Controller
@RequestMapping(value="/admin/article",name="article")
public class ArticleController implements ServletContextAware {
	
	@Autowired
	private AppArticleRepository articleRepo;
	@Autowired
	private AppUserRepository userRepo;
	@Autowired
	private FileService fileService;
	
	private ServletContext servletContext;
	
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
		
		return "redirect:/admin/article/list";
	}
	
	@GetMapping(value ="/edit/{id}" ,name="_edit")
	public String ediArticle(@PathVariable Long id,Model model){
		
		Optional<AppArticle> article = articleRepo.findById(id);
		AppArticle appArticle = article.get();
		model.addAttribute("article", appArticle);
		return "/admin/article/edit";
	}
	@PostMapping(value="/edit/{id}", name="_edit" )
	public String updateArticle(@Valid @ModelAttribute("article") AppArticle appArticle, Errors error,@PathVariable Long id){

		
		if( error.getErrorCount() > 0) {
				return "admin/article/edit";
		}else {
			
			AppArticle article = articleRepo.findById(id).get();
			appArticle.setCreatedBy(article.getCreatedBy());
			articleRepo.save(appArticle);
		}
		return "redirect:/admin/article/list";
	}
	
	
	@Transactional
	@RequestMapping(value="/delete/{id}", name="_delete",method = RequestMethod.POST)
	public ResponseEntity<Map<String,String>> deleteUser(@PathVariable Long id,RedirectAttributes redirAttrs){
		
		AppArticle article = articleRepo.findById(id).get();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		
		if(article==null) {
			throw new ArticleNotFoundException("article not found");
		}
		else {
			articleRepo.delete(article);
		}
		Map<String, String> message = new HashMap<String, String>();
			message.put("message", "Article Succcessfully deleted");
		redirAttrs.addFlashAttribute("message", "Article Deleted successfuly");
		return ResponseEntity.ok(message);
	}
	
	@RequestMapping(value = "/image/list", method = RequestMethod.GET)
	public String fileBrowser(@RequestParam(name = "CKEditorFuncNum") String ckEditorFuncNum,Model model) {
		File folder = new  File("upload");
		
		model.addAttribute("files",folder.listFiles());
		model.addAttribute("CKEditorFuncNum",ckEditorFuncNum);
		return "admin/filemanager/filebrowser";
	}
	
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<Map<String,String>> upload(@RequestParam("upload") MultipartFile upload) {
		
			System.out.println("file name: " +upload.getName());
		fileService.uploadFile(upload);
		
		Map<String, String> message = new HashMap<String, String>();
		message.put("uploaded", "1");
		message.put("fileName", upload.getOriginalFilename());
		message.put("url", "/upload/"+upload.getOriginalFilename());
	
		return ResponseEntity.ok(message);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
			this.servletContext = servletContext;
	}
	
}