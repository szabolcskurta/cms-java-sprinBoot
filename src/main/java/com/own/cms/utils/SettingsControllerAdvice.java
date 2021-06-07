package com.own.cms.utils;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.own.cms.controller.UserRestController;



@ControllerAdvice(assignableTypes = UserRestController.class)
public class SettingsControllerAdvice {

    @ModelAttribute("classActiveSettings")
    public String cssActivePage() {
        return "active";
    }

}
