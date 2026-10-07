package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {


    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    PatientService patientService;
    @Autowired
    Service service;
    @Autowired
    TokenService tokenService;
    private Logger logger= LoggerFactory.getLogger(AppointmentService.class);



    /*-----------------------------PUBLIC METHODS-----------------------------*/
    //Validates the token using service.validateToken(). If the token is valid, fetches the patient details using patientService.getPatientDetails()
    @GetMapping("/{token}")
    public ResponseEntity<Map<String,Object>> getPatient(
            @PathVariable("token") String token
    ){

        //token validation
        String message="Error with PatientController.getPatient: ";
        if (service.validateToken(token, "patient").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("failure",message));
        }

        //patient retrieval
        return patientService.getPatientDetails(token);
    }


    @PostMapping()
    public ResponseEntity<Map<String,String>> createPatient(
            @Valid @RequestBody Patient patient
    ){
        //patient validation
        String message;
        if(this.service.validatePatient(patient)){
            message= "Error with PatientController.createPatient: patient already exists";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("failure",message));
        }else{
            //patient creation
            if (this.patientService.createPatient(patient)==1){
                message="Signup successful";
                logger.info(message);
                return  ResponseEntity.ok(Map.of("success",message));
            }else{
                message= "Error with PatientController.createPatient: patient is null, needs adequate and complete patient info for saving";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("failure",message));
            }


        }


    }

    //login: The login credentials (email, password)
    @PostMapping("/login")
    public  ResponseEntity<Map<String, String>>login(Login login){
        return this.service.validatePatientLogin(login);

    }
    //id: The ID of the patient
    //token: The authentication token for the patient
    @GetMapping("/{id}/{token}")
    public  ResponseEntity<Map<String, Object>>getPatientAppointment(
            @PathVariable("id") Long id,
            @PathVariable("token") String token
    ){
        //token validation
        String message="Error with PatientController.getPatientAppointment: ";
        if (service.validateToken(token, "patient").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("failure",message));
        }

        return this.patientService.getPatientAppointments(id,token);
    }
    @GetMapping("/filter/{condition}/{name}/{token}")
    public HttpEntity<Map<String, Object>> filterPatientAppointment(
            @PathVariable("condition") String condition,
            @PathVariable("name") String name,
            @PathVariable("token") String token
    ){
        //token validation
        String message="Error with PatientController.filterPatientAppointment: ";
        if (service.validateToken(token, "patient").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("failure",message));
        }
        return this.service.filterPatient(condition, name, token);
    }

    /**
     * Validates a Patient token and returns a Response with a success/failure message
     * This function is useful for multiple methods that require a preliminary token validation
     * UPDATE: this function turns out to be redundant as an if-else statement is needed in order for subsequent actions to take place. Perhaps we can refactor/reimagine it so redundant code is avoided
     * @param token User JWT token containing an identifier (username/email)
     * @param method Method(function) of the current class to help trace errors
     * @return Response with success/failure message
     */
    private ResponseEntity<Map<String,Object>> tokenIsValid(String token, String method){
        String message="Error with PatientController."+ method +": ";
        if (service.validateToken(token, "patient").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.ok().body(Map.of("failure",message));
        }else{
            message="Success with PatientController."+ method +": ";
            logger.info(message);
            return ResponseEntity.ok().body(Map.of("success",message));
        }
    }




}


