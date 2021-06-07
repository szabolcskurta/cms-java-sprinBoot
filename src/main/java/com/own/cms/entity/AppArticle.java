package com.own.cms.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import com.own.cms.entity.AppPage;

@Entity
@Table( name="app_article")
public class AppArticle {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	@NotBlank(message ="The field mandatory")
	private String title;
	
	@Lob
	private String content;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column
	private Date createdAt;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date updatedAt;
	
	@ManyToOne(targetEntity = AppUser.class,fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id",nullable = true)
	private  AppUser createdBy;
	
	@OneToMany(mappedBy = "article" ,targetEntity = AppPage.class)
	private List<AppPage> pageList = new ArrayList<AppPage>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public AppUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(AppUser createdBy) {
		this.createdBy = createdBy;
	}
	
	
	public List<AppPage> getPageList() {
		return pageList;
	}

	public void setPageList(List<AppPage> pageList) {
		this.pageList = pageList;
	}

	@PrePersist
	public void prePersist() {
		if (this.createdAt== null ) 
			this.createdAt = new Date();
	}
	@PreUpdate
	public void prePerUpdate() {
		this.updatedAt = new Date();
	}
	
	@PreRemove
	public void setPageNull(){
		this.pageList.forEach(page->page.setArticle(null));
	}
	
	
}
