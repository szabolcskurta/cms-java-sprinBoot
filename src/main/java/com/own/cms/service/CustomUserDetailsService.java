package com.own.cms.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.own.cms.entity.AppRole;
import com.own.cms.entity.AppUser;
import com.own.cms.repository.AppUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AppUserRepository appUserRepository;

	public CustomUserDetailsService(AppUserRepository userRepository) {
		this.appUserRepository = userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AppUser user = appUserRepository.findByUsername(userName);

		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(),
					user.getPassword(),
					mapRolesToAuthorities(user.getRoles()));
		}else{
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

	private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection <AppRole> roles) {
		Collection < ? extends GrantedAuthority> mapRoles = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		return mapRoles;
	}



}