
package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//the api path is retrieved using a property placeholder. More info: https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files.property-placeholders
@RequestMapping("${api.path}" + "admin")
public class AdminController {
    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private com.project.back_end.services.Service service;

    /*-----------------------------PUBLIC METHODS-----------------------------*/

    @PostMapping
    public ResponseEntity<Map<String, String>> adminLogin(@Valid @RequestBody Admin admin) {
        return this.service.validateAdmin(admin);
    }
}

