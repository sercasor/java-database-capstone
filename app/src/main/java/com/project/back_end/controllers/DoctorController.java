package com.project.back_end.controllers;


import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${api.path}" + "doctor") //https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files.property-placeholders
public class DoctorController {

    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private DoctorService doctorService;
    @Autowired
    Service service;
    private Logger logger= LoggerFactory.getLogger(AppointmentService.class);



    /*-----------------------------PUBLIC METHODS-----------------------------*/
    //user //role of the user (admin, etc.)
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String,Object>> getDoctorAvailability(
            @PathVariable("user") String user,
            @PathVariable("doctorId") Long doctorId,
            @PathVariable("date") LocalDate date,
            @PathVariable("token") String token
    ){
        if (service.validateToken(token, user).getStatusCode().is4xxClientError()){
            logger.error("Error with DoctorController.getDoctorAvailability(): invalid token");
            return ResponseEntity.badRequest().body(Map.of("message","Invalid token"));
        }
        logger.info("DoctorController.getDoctorAvailability()  request succesfully");
        return ResponseEntity.ok().body(Map.of("availability",this.doctorService.getDoctorAvailability(doctorId, date)));

    }

    @GetMapping
    public Map<String, List<Doctor>>getDoctors(){
       return  Map.of("doctors",this.doctorService.getDoctors());

    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String,String>>saveDoctor(
            @Valid @RequestBody Doctor doctor,
            String token){
        String message="Error with DoctorController.saveDoctor() :";
        if (service.validateToken(token, "doctor").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message","Invalid token"));
        }

        switch (doctorService.saveDoctor(doctor)){
            case 0:
                message+= " Internal error";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case -1:
                message+= " Doctor already exists";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case 1:

                message="Doctor successfully saved";
                logger.info(message);
                return ResponseEntity.ok().body(Map.of("message",message));
            default:
                message+=" unknown error, return int doesn't follow conventions";
                logger.info(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
        }




    }
    @PostMapping("/login")
    public  ResponseEntity<Map<String, String>>doctorLogin(
            Login login
    ){
        return this.doctorService.validateDoctor(login);

    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String,String>>updateDoctor(
            Doctor doctor,
            @PathVariable("token")String token
    ){
        String message="Error with DoctorController.updateDoctor() :";
        if (service.validateToken(token, "doctor").getStatusCode().is4xxClientError()){
            message+= "invalid token";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message","Invalid token"));
        }

        switch (doctorService.updateDoctor(doctor)){
            case 0:
                message+= " Internal error";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case -1:
                message+= " Doctor already exists";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case 1:

                message="Doctor successfully updated";
                logger.info(message);
                return ResponseEntity.ok().body(Map.of("message",message));
            default:
                message+=" unknown error, return int doesn't follow conventions";
                logger.info(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
        }


    }
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String,String>>deleteDoctor(
            @PathVariable("token") String token,
            @PathVariable("id") Long id
    ){


        String message="Error in DoctorController.deleteDoctor(): ";
        if(service.validateToken(token, "admin").getStatusCode().is4xxClientError()){
            message+= " Forbidden request: you must have admin role";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message",message));
        }

        Optional<Doctor> doctor=this.doctorService.getDoctor(id);
        switch (this.doctorService.deleteDoctor(doctor.get())){
            case 0:
                message+= " Internal error";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case -1:
                message+= " Doctor already exists";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
            case 1:

                message="Doctor successfully updated";
                logger.info(message);
                return ResponseEntity.ok().body(Map.of("message",message));
            default:
                message+=" unknown error, return int doesn't follow conventions";
                logger.info(message);
                return ResponseEntity.badRequest().body(Map.of("message",message));
        }
    }

    @GetMapping("/filter/{name}/{time}/{specialty}")
    public  Map<String, Object>filter(
            @PathVariable("name") String name,
            @PathVariable("time") String time,
            @PathVariable("specialty") String specialty
    ){
        return service.filterDoctor(name,specialty,time); //time can be either "am" or "pm"
    }

}
