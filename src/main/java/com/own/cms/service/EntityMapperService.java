package com.own.cms.service;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Service;

import com.own.cms.entity.AppArticle;
import com.own.cms.entity.AppArticleDTO;

@Service
public class EntityMapperService {


	public void map(Object from,Object to, List<Object> object) {
		
		
	
		
	}
}
