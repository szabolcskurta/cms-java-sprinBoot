package com.own.cms.controller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.own.cms.entity.AppArticle;
import com.own.cms.entity.AppArticleDTO;
import com.own.cms.entity.AppPage;
import com.own.cms.entity.AppPageDTO;
import com.own.cms.entity.AppPageGroup;
import com.own.cms.entity.AppPageType;
import com.own.cms.exception.PageNotFoundException;
import com.own.cms.exception.UserNotFoundException;
import com.own.cms.repository.AppPageRepository;
import com.own.cms.repository.AppArticleRepository;

@Controller
@RequestMapping(value = "/admin/page", name = "admin_page")
public class PageController {

	@Autowired
	private AppPageRepository pageRepository;
	@Autowired
	private AppArticleRepository articleRepository;

	@GetMapping(value = "/list", name = "_list")
	public String pageListview() {
		return "/admin/page/list";
	}

	@PostMapping(value = "/list", name = "_list")
	public ResponseEntity<Map<String, Object>> pageList() {

		List<AppPage> allArticle = pageRepository.findAll();
		ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<List<AppPageDTO>>() {
		}.getType();

//		PropertyMap<AppArticle, AppArticleDTO> articleMapping = new PropertyMap<AppArticle, AppArticleDTO>() {
//			   protected void configure() {
//			      map().setId(source.getId());
//			      map().setCreatedAt(source.getCreatedAt());
//			      map().setTitle(source.getTitle());
//			      map().setCreateBy(source.getCreatedBy().getUsername());
//			   }
//			};
//			modelMapper.addMappings(articleMapping);
		List<AppArticleDTO> pageDTOlist = modelMapper.map(allArticle, listType);
		Map<String, Object> userDTOListMapped = new HashMap<String, Object>();
		userDTOListMapped.put("data", pageDTOlist.toArray());
		userDTOListMapped.put("recordsTotal", pageDTOlist.size());
		userDTOListMapped.put("recordsFiltered", pageDTOlist.size());
		return ResponseEntity.ok(userDTOListMapped);
	}

	@GetMapping(value = "/add", name = "_add")
	public String addPage(Model model) {
		AppPage page = new AppPage();

		List<AppArticle> allArticle = articleRepository.findAll();
		model.addAttribute("page", page);
		model.addAttribute("articleList", allArticle);
		return "admin/page/edit";
	}

	@Transactional
	@PostMapping(value = "/add", name = "_save")
	public String savePage(@Validated({ AppPageGroup.class }) @ModelAttribute("page") AppPage page,Errors error) {
	
		if (error.getErrorCount() > 0) {
			System.out.println("eror:"+error.getAllErrors().toString());
			return "admin/page/edit";
		}
		
		if (page.isHomepage()) {
			pageRepository.setHompagefalse(page.getId());
		}
	
		pageRepository.save(page);
		return "redirect:/admin/page/list";
	}

	@GetMapping(value = "/edit/{id}", name = "_edit")
	public String ediArticle(@PathVariable Long id, Model model) {

		Optional<AppPage> page = pageRepository.findById(id);
		List<AppArticle> allArticle = articleRepository.findAll();
		AppPage appPage = page.get();
		model.addAttribute("page", appPage);
		model.addAttribute("articleList", allArticle);
		return "/admin/page/edit";
	}

	@Transactional
	@PostMapping(value = "/edit/{id}", name = "_edit")
	public String updateArticle(@Valid @ModelAttribute("page") AppPage page, Model model, Errors error,@PathVariable Long id) {
		
		if (error.getErrorCount() > 0) {
			List<AppArticle> allArticle = articleRepository.findAll();
			model.addAttribute("articleList", allArticle);
			return "admin/article/edit";
		} else {
		
			System.out.println("page: "+page.toString());
			AppArticle article = articleRepository.getOne(page.getArticle().getId());
			page.setArticle(article);
			if (page.isHomepage()) {
				pageRepository.setHompagefalse(page.getId());
					
			}
			if(page.getPageType() == AppPageType.LINK_TYPE) {
				page.setArticle(null);
			}
			pageRepository.saveAndFlush(page);
			
		}
		return "redirect:/admin/page/list";
	}

	@Transactional
	@RequestMapping(value = "/delete/{id}", name = "_delete", method = RequestMethod.POST)
	public String deleteUser(@PathVariable Long id, RedirectAttributes redirAttrs) {

		AppPage article = pageRepository.findById(id).get();

		if (article == null) {
			throw new PageNotFoundException("Page not found");
		} else {
			pageRepository.delete(article);
		}

		redirAttrs.addFlashAttribute("message", "Article Deleted successfuly");
		return "redirect:/admin/article/list";
	}

	@ModelAttribute
	public void addAttributes(Map<String, Object> model) {
		List<AppPage> allArticle = pageRepository.findAll();
		model.put("articleList", allArticle);
		model.put("pagetype", AppPageType.values());
	}

}
