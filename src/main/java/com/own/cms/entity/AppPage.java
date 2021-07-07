package com.own.cms.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Cascade;
import org.springframework.validation.annotation.Validated;
import com.own.cms.entity.AppPageGroup;
@Entity
@Table(name="app_page")
public class AppPage {
	 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column (unique = true)
	@NotBlank(groups = {AppPageGroup.class},message ="The title field mandatory")
	private String pageTitle;
	
	

	@Column (unique = true)
	@NotBlank(groups = {AppPageGroup.class},message ="The field mandatory")
	private String url;
	
	
	@ManyToOne(optional = true,cascade = CascadeType.MERGE)
	@JoinColumn(name="article_id",referencedColumnName = "id",nullable = true,insertable = true,updatable = true)
	private AppArticle article;
	
	@Column(name ="homepage",columnDefinition = "boolean default false")
	private  boolean homepage;
	
	@Enumerated(EnumType.STRING)
	private AppPageType pageType = AppPageType.ARTICLE_TYPE;
	

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public AppArticle getArticle() {
		
		return article;
	}

	public void setArticle(AppArticle article) {
		
		this.article =article;
	}
	  
	



	public boolean isHomepage() {
		return homepage;
	}

	public void setHomepage(boolean homepage) {
		this.homepage = homepage;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	public AppPageType getPageType() {
		return this.pageType;
	}

	public void setPageType(AppPageType pageType) {
		this.pageType = pageType;
	}
	

	@PrePersist
	public void prePersist() {
		if (this.article.getId()<0 ) { 
			this.setArticle(null);
		}

	}
	
	@PreUpdate
	public void prePerUpdate() {
		
	}

	@Override
	public String toString() {
		return "AppPage [id=" + id + ", pageTitle=" + pageTitle + ", url=" + url + ", article=" + article
				+ ", homepage=" + homepage + ", pageType=" + pageType + "]";
	}


	

	

	
}
