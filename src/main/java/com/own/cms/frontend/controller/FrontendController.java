package com.own.cms.frontend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.dialect.pagination.FirstLimitHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.own.cms.entity.AppPage;
import com.own.cms.exception.ArticleNotFoundException;
import com.own.cms.exception.PageNotFoundException;
import com.own.cms.repository.AppPageRepository;

import javassist.NotFoundException;

@Controller
public class FrontendController {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(FrontendController.class);
	
	@Autowired
	private AppPageRepository pageRepository;
	
	@GetMapping(value = "/{url}", name = "_page")
	public String getPage(HttpServletRequest request,@PathVariable String url,Model model) {
	
				List<AppPage> allArticle = pageRepository.findAll();
				
				AppPage page = pageRepository.findByUrl(url);
				if(page == null) {
					throw new PageNotFoundException("Page not found");	
				}
				if(page.getArticle()==null) {
					throw new ArticleNotFoundException("Article not found");
				}
				
				model.addAttribute("menu",allArticle.toArray());
				model.addAttribute("content",page.getArticle().getContent());
				return "frontend/index.html";
			
			
	}
	
	
	@GetMapping(value = "/", name = "__home_page")
	public String getHomePage(Model model)
	{	
		
		
			AppPage page = pageRepository.findByHomepage(true);
			List<AppPage> allArticle = pageRepository.findAll();
			
			if(page == null) {
				
			}
			if(page.getArticle() ==  null) {
				throw new ArticleNotFoundException("Article not found");
			}
			
			model.addAttribute("menu",allArticle.toArray());
			model.addAttribute("content",page.getArticle().getContent());
			return "frontend/index.html";
		
	}
	
	@ExceptionHandler(PageNotFoundException.class)
    public String handleException(final Exception e,Model model) {
		model.addAttribute ("error",e.getMessage());
        return "frontend/404.html";
    }
	
	@ExceptionHandler(ArticleNotFoundException.class)
    public String handleExceptionArticle(final Exception e,Model model) {
		model.addAttribute ("error",e.getMessage());
        return "frontend/404.html";
	}
	
}
