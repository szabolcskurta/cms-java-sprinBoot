package com.own.cms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name="app_page")
public class AppPage {
	 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column (unique = true)
	@NotBlank(message ="The title field mandatory")
	private String title;
	
	

	@Column (unique = true)
	@NotBlank(message ="The field mandatory")
	private String url;
	
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY,targetEntity = AppArticle.class) 
	@JoinColumn(name = "article_id", referencedColumnName ="id",nullable=true, insertable=true, updatable=true)
	private AppArticle article;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isHomepage;
	 
	

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
		this.article = article;
	}
	
	public boolean isHomepage() {
		return isHomepage;
	}

	public void setHomepage(boolean isHomepage) {
		this.isHomepage = isHomepage;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
