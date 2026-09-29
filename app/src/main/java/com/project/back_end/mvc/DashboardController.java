package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//This class handles routing to admin and doctor dashboard pages based on token validation (authenticated users).
@Controller
public class DashboardController {
    /*----------------------PRIVATE ATTRIBUTES---------------------*/
    @Autowired
    private Service service;
    /*----------------------PUBLIC METHODS---------------------*/
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token){
        if (this.service.validateToken(token, "admin").isEmpty()){
            return "admin/adminDashboard"; //returns the template

        }else {
            return "redirect:/"; //redirects to root (login/home)
        }

    }
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token){
        if (this.service.validateToken(token, "doctor").isEmpty()){
            return "doctor/doctorDashboard"; //returns the template

        }else {
            return "redirect:/"; //redirects to root (login/home)
        }

    }



}
