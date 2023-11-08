package com.own.cms.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="app_role")
public class AppRole {
	
	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;

	    @ManyToMany(mappedBy = "roles")
	    private Set<AppUser> users;
	    
	  
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public Set<AppUser> getUsers() {
	        return users;
	    }

	    public void setUsers(Set<AppUser> users) {
	        this.users = users;
	    }

	   
}
