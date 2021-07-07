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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import com.own.cms.entity.AppPage;
import com.own.cms.entity.AppArticleGroup;
@Entity
@Table( name="app_article")
public class AppArticle {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	@NotBlank(groups= {AppArticleGroup.class},message ="The field mandatory")
	private String title=" ";
	
	@Lob
	private String content;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column
	private Date createdAt;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date updatedAt;
	
	@ManyToOne(targetEntity = AppUser.class,cascade = {CascadeType.REFRESH,CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id",insertable = true,updatable = true,referencedColumnName = "id")
	private  AppUser createdBy;
	
	@OneToMany(mappedBy = "article" ,cascade = CascadeType.MERGE,targetEntity = AppPage.class)
	private List<AppPage> pageList = new ArrayList<AppPage>();
	 
	public AppArticle() {
		super();
		// TODO Auto-generated constructor stub
	}

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
		this.createdAt = new Date();
	}

	
	@PreUpdate
	public void preUpdate() {
		this.createdBy = this.getCreatedBy();
		this.updatedAt = new Date();
		this.pageList =this.getPageList();
	}
	
	@PreRemove
	public void setPageNull(){
		this.pageList.forEach(page->page.setArticle(null));
	}

	@Override
	public String toString() {
		return "AppArticle [id=" + id + ", title=" + title + ", content=" + content + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", createdBy=" + createdBy + ", pageList=" + pageList + "]";
	}
	
	
	
	
}
