package com.own.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.own.cms.entity.AppUser;
import com.own.cms.entity.AppUserDTO;
import com.own.cms.repository.AppRoleRepository;
import com.own.cms.repository.AppUserRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import java.lang.reflect.Type;

@RestController
public class UserRestController {
	
	@Autowired
	private AppUserRepository userRepo;
	@Autowired
	private AppRoleRepository roleRepo;
	
	
	
	@RequestMapping(value="/admin/user/list", method = RequestMethod.POST)
	public Map<String, Object> getAllUser(){
	  
		List<AppUser> allUser =  userRepo.findAll();
		ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<List<AppUserDTO>>(){}.getType();
		List<AppUserDTO> userDTOlist =  modelMapper.map(allUser,listType);

	    Map<String,Object> userDTOListMapped = new HashMap<String, Object>();
	    userDTOListMapped.put("data", userDTOlist.toArray());
	    userDTOListMapped.put("recordsTotal", userDTOlist.size());
	    userDTOListMapped.put("recordsFiltered", userDTOlist.size());
	   return  userDTOListMapped;
	}

}
