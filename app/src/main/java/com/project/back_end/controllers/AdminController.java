
package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class AdminController {
    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private com.project.back_end.services.Service service;

    /*-----------------------------PUBLIC METHODS-----------------------------*/
    //the api path is retrieved using a property placeholder. More info: https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files.property-placeholders
    @RequestMapping("${api.path}" + "admin")
    @PostMapping
    public ResponseEntity<Map<String, String>> adminLogin(Admin admin) {
        return this.service.validateAdmin(admin);
    }
}

