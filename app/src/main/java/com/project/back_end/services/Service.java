package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private  TokenService tokenService;
    @Autowired
    private  AdminRepository adminRepository;
    @Autowired
    private  DoctorRepository doctorRepository;
    @Autowired
    private  PatientRepository patientRepository;
    @Autowired
    private  DoctorService doctorService;
    @Autowired
    private  PatientService patientService;
    private Logger logger= LoggerFactory.getLogger(AppointmentService.class);
//TODO: HAY QUE HACER EL METODO
    //checks the validity of a token for a given user
public ResponseEntity<Map<String, String>> validateToken(String token, String role){
    String message;
    if (this.tokenService.validateToken(token,role)){
        message="Token is valid";
        logger.info(message);
        return ResponseEntity.ok().body(Map.of("success",message));

    }else {
        message="Token is invalid";
        logger.error(message);
        return ResponseEntity.badRequest().body(Map.of("failure",message));
    }

}
//This method validates the login credentials of an admin
//Returns a generated token if the admin is authenticated
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin){
        String message;
        try {
            Admin repoAdmin=adminRepository.findByUsername(receivedAdmin.getUsername());

            if (repoAdmin!=null&&receivedAdmin.getPassword().equals(repoAdmin.getPassword())){
                return ResponseEntity.ok().body(Map.of("token",this.tokenService.generateToken()));

            }else {
                message="Admin is invalid, could not be found";
                logger.error(message);
                return ResponseEntity.badRequest().body(Map.of("failure",message));
            }
        }
        catch (Exception e){
            message="Error with validateAdmin: "+e.getMessage();
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("failure",message));
        }



    }
    //filters doctors based on name, specialty, and available time
    //this method uses multiple if-else but it could be much more compact should a bitmask and a switch be used (kinda advanced). see https://www.baeldung.com/java-bitmasking
        public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasSpecialty = specialty != null && !specialty.isBlank();
        boolean hasTime = time != null && !time.isBlank();

        if (hasName && hasSpecialty && hasTime) {
            return this.doctorService.filterDoctorsByNameSpecialtyandTime(name, specialty, time);
        } else if (hasName && hasSpecialty) {
            return this.doctorService.filterDoctorByNameAndSpecialty(name, specialty);
        } else if (hasName && hasTime) {
            return this.doctorService.filterDoctorByNameAndTime(name, time);
        } else if (hasSpecialty && hasTime) {
            return this.doctorService.filterDoctorByTimeAndSpecialty(time, specialty);
        } else if (hasName) {
            return this.doctorService.findDoctorByName(name);
        } else if (hasSpecialty) {
            return this.doctorService.filterDoctorBySpecialty(specialty);
        } else if (hasTime) {
            return this.doctorService.filterDoctorByTime(time);
        } else {
            // if no parameters
            return Map.of("doctors",this.doctorService.getDoctors());
        }
    }

    //This method validates whether an appointment is available based on the doctor's schedule. It's important for ensuring patients are scheduled only during valid time slots.
    //1 if the appointment time is valid
    //0 if the time is unavailable
    //-1 if the doctor doesn't exist
    public int validateAppointment(Appointment appointment){
        Optional<Doctor> doctor=doctorRepository.findById(appointment.getDoctor().getId());
        List<String> availableTimes;
        if (doctor.isEmpty()){
            logger.error("Error with validateAppointment: Doctor doesn't exist in DB");
            return 0;
        }
        availableTimes=this.doctorService.getDoctorAvailability(doctor.get().getId(),appointment.getAppointmentTime().toLocalDate()); //output is 10:00,11:00, etc.
        if (availableTimes.contains(appointment.getAppointmentTime().toString())){
            logger.info("validateAppointment update: Appointment time is valid");
            return 1;
        }else {
            logger.error("Error with validateAppointment: Time is unavailable");
            return 0;
        }


    }




}


}
