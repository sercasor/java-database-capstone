package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    private Logger logger= LoggerFactory.getLogger(AppointmentService.class);

    /*-----------------------------PUBLIC METHODS-----------------------------*/

    /**
     * attempts to save the prescription to the database using the prescriptionRepository.
     * If successful, it returns a 201 Created status with a message "Prescription saved". If there is an error, it returns a 500 Internal Server Error with a generic error message.
     * @param prescription The prescription object to be saved
     * @return ResponseEntity with either 201 success code or a 500 error code
     */
    @Transactional
    public ResponseEntity<Map<String, String>>  savePrescription(Prescription prescription){
        String message;
        try {
            message="Prescription saved";
            this.prescriptionRepository.save(prescription);
            return ResponseEntity.ok().body(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid parameter in savePrescription: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error occurred when saving a prescription: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }

    }

    /**
     * attempts to fetch the prescription from the database using the prescriptionRepository.findByAppointmentId(appointmentId) method.
     * @param appointmentId Appointment ID
     * @return  If successful, it returns the prescription as part of the response with a 200 OK status. If there is an error, it returns a 500 Internal Server Error with an error message.
     */
    @Transactional
    public ResponseEntity<Map<String, Object>>  getPrescription(Long appointmentId){

        try {
            List<Prescription> prescriptions=this.prescriptionRepository.findByAppointmentId(appointmentId);
            if (prescriptions==null){
                String message="Invalid parameter in getPrescription, can't be null";
                logger.error(message);
                return ResponseEntity.internalServerError().body(Map.of("error", message));
            }
            return ResponseEntity.ok().body(Map.of("prescriptions", prescriptions));
        } catch (Exception e) {
            logger.error("Error occurred with getPrescription: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }

    }

    /*-----------------------------PRIVATE METHODS-----------------------------*/
}
