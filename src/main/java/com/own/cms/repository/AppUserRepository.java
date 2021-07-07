package com.own.cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.own.cms.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{
	AppUser findByUsername(String username);
	AppUser findByEmail(String email);			
	AppUser findByResetPasswordToken(String token);
}
