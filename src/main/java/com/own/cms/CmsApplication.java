package com.own.cms;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.own.cms.controller.AdminController;


@SpringBootApplication
public class CmsApplication {
	
	public static final String uploadingDir = System.getProperty("user.dir") + "/upload/";
	public static void main(String[] args) {
		new File(uploadingDir).mkdirs();
		SpringApplication.run(CmsApplication.class, args);
	}

}
