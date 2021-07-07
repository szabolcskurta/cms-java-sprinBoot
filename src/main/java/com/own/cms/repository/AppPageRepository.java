package com.own.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.own.cms.entity.AppPage;

public interface AppPageRepository extends JpaRepository<AppPage, Long> {
	
	AppPage findByUrl(String url);
	AppPage findByHomepage(boolean isHomepag);
	
	@Modifying
	@Query(value = "Update AppPage p SET homepage=false where p.id <> :id")
	void setHompagefalse(@Param(value = "id") long id);
}
