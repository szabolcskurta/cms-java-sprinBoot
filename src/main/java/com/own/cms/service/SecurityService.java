package com.own.cms.service;

public interface SecurityService {
	String findLoggedInUsername();

    void autoLogin(String username, String password);
}

