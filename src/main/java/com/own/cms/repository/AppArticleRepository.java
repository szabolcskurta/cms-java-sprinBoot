package com.own.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.own.cms.entity.AppArticle;

public interface AppArticleRepository  extends JpaRepository<AppArticle, Long> {
    
}
