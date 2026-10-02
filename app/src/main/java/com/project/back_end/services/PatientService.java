package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PatientService {

    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private TokenService tokenService;
    private Logger logger= LoggerFactory.getLogger(AppointmentService.class);



    /*-----------------------------PUBLIC METHODS-----------------------------*/
    //Saves a new patient to the database
    //Returns 1 on success, and 0 on failure (for example, exception)
    public int createPatient(Patient patient){//The patient object to be saved

        try {
            this.patientRepository.save(patient); //never returns null so no if sttatement required
            return 1;
        } catch (Exception e) {
            logger.error("Patient saving error: {}",e.getMessage());
            return 0;
        }

    }

    //Retrieves a list of appointments for a specific patient
    //Returns a response containing a list of appointments or an error message.
    //The method checks if the provided patient ID matches the one decoded from the token (by email). If there's a mismatch, it returns an Unauthorized status.
    //If the IDs match, it retrieves the patient's appointments and returns them as a list of AppointmentDTO objects.
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token){

    try {
        Optional<Patient> patientOptional=this.patientRepository.findById(id);
        boolean patientIsAuthorized=patientOptional.get().getEmail().equals(this.tokenService.extractEmail(token));
        if(!patientIsAuthorized){
            String message= "Error in getPatientAppointment method: unauthorized patient";
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }

        return ResponseEntity.ok().body(Map.of("appointments",appointmentListToDTO(this.appointmentRepository.findByPatientId(id))));

    } catch (Exception e) {
        String message=String.format("Error in getPatientAppointment method: %s",e.getMessage());
        logger.error(message);
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }




    }
    //Filters appointments by condition (past or future) for a specific patient
    //The method checks the condition value (past or future) and filters appointments accordingly. It uses the status (1 for past and 0 for future) to determine the filtering criteria.
    //Returns the filtered appointments or an error message
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id){
        try {
            //Optional<Patient> patientOptional=this.patientRepository.findById(id);
            List<Appointment> appointments=this.appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id,Integer.parseInt(condition));
            return ResponseEntity.ok().body(Map.of("appointments",appointmentListToDTO(appointments)));

        } catch (Exception e) {
            String message=String.format("Error in filterByCondition method: %s",e.getMessage());
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }

    }
    //Filters the patient's appointments by doctor's name. fetches appointments where the doctor's name matches the provided name and the patient ID matches the given patientId
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId){
        try {
            //Optional<Patient> patientOptional=this.patientRepository.findById(id);
            List<Appointment> appointments=this.appointmentRepository.filterByDoctorNameAndPatientId(name,patientId);
            return ResponseEntity.ok().body(Map.of("appointments",appointmentListToDTO(appointments)));

        } catch (Exception e) {
            String message=String.format("Error in filterByDoctor method: %s",e.getMessage());
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }
    }

    //Filters the patient's appointments by doctor's name and appointment condition (past or future)
    //The method combines the filtering criteria of both the doctor's name and the condition (past or future).
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId){

        try {
            //Optional<Patient> patientOptional=this.patientRepository.findById(id);
            List<Appointment> appointments=this.appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(name,patientId,Integer.parseInt(condition));
            return ResponseEntity.ok().body(Map.of("appointments",appointmentListToDTO(appointments)));

        } catch (Exception e) {
            String message=String.format("Error in filterByDoctorAndCondition method: %s",e.getMessage());
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }

    }


    //Fetches the patient's details based on the provided JWT token
    //The method extracts the email from the token and retrieves the corresponding patient from the database. The patient details are then returned as part of the response.
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token){
        try {
            Optional<Patient> patientOptional= Optional.ofNullable(this.patientRepository.findByEmail(this.tokenService.extractEmail(token)));
            List<String> patientDetails=new ArrayList<>();
            patientDetails.add(patientOptional.get().getName());
            patientDetails.add(patientOptional.get().getId().toString());
            patientDetails.add(patientOptional.get().getEmail());
            patientDetails.add(patientOptional.get().getPhone());
            patientDetails.add(patientOptional.get().getAddress());
            return ResponseEntity.ok().body(Map.of("patient",patientDetails));

        } catch (Exception e) {
            String message=String.format("Error in getPatientDetails method: %s",e.getMessage());
            logger.error(message);
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }

    }


    /*-----------------------------PRIVATE METHODS-----------------------------*/
    //converts a  List <Appointment> to a List<AppointmentDTO>, for convenience, performance and safety (exposing non-sensitive data)
    private List<AppointmentDTO> appointmentListToDTO(List<Appointment> appointments){
        List<AppointmentDTO> appointmentDTOS=new ArrayList<>();
        for (Appointment appointment:appointments){
            AppointmentDTO appointmentDTO=new AppointmentDTO(
                    appointment.getId(),
                    appointment.getDoctor().getId(),
                    appointment.getDoctor().getName(),
                    appointment.getPatient().getId(),
                    appointment.getPatient().getName(),
                    appointment.getPatient().getEmail(),
                    appointment.getPatient().getAddress(),
                    appointment.getAppointmentTime(),
                    appointment.getStatus());
            appointmentDTOS.add(appointmentDTO);

        }
        return appointmentDTOS;
    }


}
