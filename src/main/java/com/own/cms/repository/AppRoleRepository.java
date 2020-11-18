package com.own.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.own.cms.entity.AppRole;
import com.own.cms.entity.AppUser;

public interface AppRoleRepository extends JpaRepository<AppRole, Long>{
	
	AppRole findByName(String username);
}
