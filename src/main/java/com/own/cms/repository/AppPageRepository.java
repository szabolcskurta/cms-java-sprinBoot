package com.own.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.own.cms.entity.AppPage;

public interface AppPageRepository extends JpaRepository<AppPage, Long> {
	
	AppPage findByUrl(String url);
	AppPage findByisHomepage(boolean isHomepag);
	
	@Modifying
	@Query(value = "Update AppPage  SET isHomepage=false")
	void setHompagefalse();
}
