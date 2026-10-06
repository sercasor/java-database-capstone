package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private Service service;
    private Logger logger= LoggerFactory.getLogger(AppointmentService.class);



    /*-----------------------------PUBLIC METHODS-----------------------------*/


    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String,Object>> getAppointments(
            @PathVariable("date")String date,
            @PathVariable("patientName")String patientName,
            @PathVariable("token")String token

    ){
        String message;
        String error="Error in AppointmentController.getAppointments(): ";
        //ensure only doctors can see appointment data
        if(this.service.validateToken(token,"doctor").getStatusCode().is4xxClientError()){
            message=error+ " Forbidden content: you must have Doctor role to access";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message","Forbidden content: you must be a Doctor to access this piece of content"));
        }
        //returns appointments for the given patient on the specified date
        logger.info("AppointmentController.getAppointments(): success on getting the list of apoointments");
        return ResponseEntity.ok().body(Map.of("appointments",this.appointmentService.getAppointments(patientName, LocalDate.parse(date),token)));



    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String,String>>bookAppointment(
            Appointment appointment,
            @PathVariable("token") String token
            ){
        String message;
        String error="Error in AppointmentController.bookAppointment(): ";
        if(service.validateToken(token, "patient").getStatusCode().is4xxClientError()){
            message=error+ " Forbidden request: you must have Patient role";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message",message));
        }
        switch (this.service.validateAppointment(appointment)){
            case 0:
                message=error+ " Time is unavailable";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case -1:
                message=error+ " Doctor doesn't exist";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case 1:
                appointmentService.bookAppointment(appointment);
                message="Appointment successfully booked";
                logger.info(message);
                return ResponseEntity.ok().body(Map.of("message",message));
            default:
                message=error+" unknown error, return int doesn't follow conventions";
                logger.info(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
        }



    }
    @PutMapping("/{token}")
    public ResponseEntity<Map<String,String>>updateAppointment(
            Appointment appointment,
            @PathVariable("token") String token
    ){
        String message;
        String error="Error in AppointmentController.updateAppointment(): ";
        if(service.validateToken(token, "patient").getStatusCode().is4xxClientError()){
            message=error+ " Forbidden request: you must have Patient role";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message",message));
        }
        switch (this.service.validateAppointment(appointment)){
            case 0:
                message=error+ " Time is unavailable";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case -1:
                message=error+ " Doctor doesn't exist";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case 1:
                this.appointmentService.updateAppointment(appointment);
                message="Appointment successfully updated";
                logger.info(message);
                return ResponseEntity.ok().body(Map.of("message",message));
            default:
                message=error+" unknown error, return int doesn't follow conventions";
                logger.info(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
        }

    }


    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String,String>>cancelAppointment(
            @PathVariable("token") String token,
            @PathVariable("id") String id
    ){
        String message;
        String error="Error in AppointmentController.cancelAppointment(): ";
        if(service.validateToken(token, "patient").getStatusCode().is4xxClientError()){
            message=error+ " Forbidden request: you must have Patient role";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message",message));
        }
        return this.appointmentService.cancelAppointment(Long.parseLong(id),token);


    }


}
