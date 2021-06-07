package com.own.cms.entity;

public class AppPageDTO {
   long id;
   String url;
   boolean isHomepage;
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
	public boolean isHomepage() {
		return isHomepage;
	}
	public void setHomepage(boolean isHomepage) {
		this.isHomepage = isHomepage;
	}

   
}
