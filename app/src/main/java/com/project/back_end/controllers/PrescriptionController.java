package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {
    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    PrescriptionService prescriptionService;
    @Autowired
    Service service;
    @Autowired
    AppointmentService appointmentService;
    private Logger logger= LoggerFactory.getLogger(AppointmentService.class);



    /*-----------------------------PUBLIC METHODS-----------------------------*/

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @PathVariable("token") String token,
            @Valid @RequestBody Prescription prescription
            ){
        //token validation
        String message="Error with PrescriptionController.savePrescription: ";
        if (service.validateToken(token, "doctor").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("failure",message));
        }

        return prescriptionService.savePrescription(prescription);

    }



    //appointmentId: The ID of the appointment to retrieve the prescription for
    //token: The authentication token for the doctor
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String,Object>> getPrescription(
            @PathVariable("appointmentId") Long appointmentId,
            @PathVariable("token") String token
    ){
        //token validation
        String message="Error with PrescriptionController.savePrescription: ";
        if (service.validateToken(token, "doctor").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("failure",message));
        }

        return prescriptionService.getPrescription(appointmentId);
    }




}
