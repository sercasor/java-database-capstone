package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentService {

    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private TokenService tokenService; //for extracting tokens from the request
    @Autowired
    private com.project.back_end.services.Service service;
    private Logger logger=LoggerFactory.getLogger(AppointmentService.class);

    /*-----------------------------PUBLIC METHODS-----------------------------*/
    @Transactional
    public int bookAppointment(Appointment appointment){
        try {
            appointmentRepository.save(appointment);

            return 1;
        }
         catch (Exception e) {
            logger.error("Appointment couldn't be saved! Exception: " + e.getMessage());
            return 0;
        }
    }
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        ResponseEntity<Map<String, String>> response;
        try {
            Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointment.getId());

            if (appointmentOptional.isEmpty()) {
                logger.error("Error when updating appointment: Appointment not found");
                return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found."));
            }

            Appointment existingAppointment = appointmentOptional.get();

            // existing appointment patient must match received object's
            if (!existingAppointment.getPatient().getId().equals(appointment.getPatient().getId())) {
                logger.error("Error when updating appointment: patient mismatch");
                return ResponseEntity.badRequest().body(Map.of("message", "You are not authorized to update this appointment."));
            }

            // Doctor must exist and be available for the new slot
            boolean isValid = service.validateAppointment(appointment); // TODO: ajustar firma cuando tengamos la clase Service

            if (!isValid) {
                logger.error("Error when updating appointment: request is not valid");
                return ResponseEntity.badRequest().body(Map.of("message", "The requested time is not available for this doctor."));
            }

            appointmentRepository.save(appointment);
            response = ResponseEntity.ok(Map.of("message", "Appointment updated successfully."));

        } catch (Exception e) {
            logger.error("Error found when updating appointment: " + e.getMessage());
            response = ResponseEntity.internalServerError().body(Map.of("message", "Unexpected error while updating appointment."));
        }
        return response;
    }
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        try {
            Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

            if (appointmentOptional.isEmpty()) {
                logger.error("Error when cancelling appointment: Appointment not found");
                return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found."));
            }

            if (tokenService.validateToken(token, "patient").isEmpty() == false) {
                // validateToken returns a  map with  "message" when it's INVALID
                logger.error("Error when cancelling appointment: Token is invalid");
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid token."));
            }

            Appointment appointment = appointmentOptional.get();
            String emailFromToken = tokenService.extractEmail(token);
            Patient patientFromToken = patientRepository.findByEmail(emailFromToken);

            if (patientFromToken == null || !appointment.getPatient().getId().equals(patientFromToken.getId())) {
                logger.error("Error when cancelling appointment: patient mismatch");
                return ResponseEntity.badRequest().body(Map.of("message", "You are not authorized to cancel this appointment."));
            }

            appointmentRepository.delete(appointment);
            logger.info("Appointment cancelled");
            return ResponseEntity.ok(Map.of("message", "Appointment cancelled successfully."));

        } catch (Exception e) {
            logger.error("Error found when cancelling appointment: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "Unexpected error while cancelling appointment."));
        }
    }

    //This method retrieves a list of appointments for a specific doctor on a specific date.
    @Transactional
    public Map<String, Object> getAppointments(String pname, LocalDate date, String token) {
        Map<String, Object> result = new HashMap<>();

        String emailFromToken = tokenService.extractEmail(token);
        Doctor doctorFromToken = doctorRepository.findByEmail(emailFromToken);

        if (doctorFromToken == null) {
            logger.error("Error when trying to get appointments: doctor not found for this token");
            result.put("appointments", new ArrayList<>());
            return result;
        }

        LocalDateTime startDate = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        LocalDateTime endDate = LocalDateTime.of(date, LocalTime.MAX);

        List<Appointment> appointmentList = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorFromToken.getId(), startDate, endDate);

        if (appointmentList.isEmpty()) {
            logger.info("No appointments found for doctor " + doctorFromToken.getId() + " on " + date);
        }

        if (pname != null && !pname.trim().isEmpty()) {
            List<Appointment> appointmentForPatientList = new ArrayList<>();
            for (Appointment appointment : appointmentList) {
                if (appointment.getPatient().getName().equalsIgnoreCase(pname)) {
                    appointmentForPatientList.add(appointment);
                }
            }
            result.put("appointments", appointmentForPatientList);
        } else {
            result.put("appointments", appointmentList);
        }

        return result;
    }

    @Transactional
    public ResponseEntity<Map<String, String>> changeStatus(int status, Long id) {
        try {
            Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

            if (appointmentOptional.isPresent()) {
                Appointment appointment = appointmentOptional.get();
                appointment.setStatus(status);
                appointmentRepository.save(appointment);
                logger.info("Appointment status changed successfully");
                return ResponseEntity.ok(Map.of("message", "Appointment status updated successfully."));
            } else {
                logger.error("Error when changing appointment status: Appointment not found");
                return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found."));
            }
        } catch (Exception e) {
            logger.error("Error when changing appointment status: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "Unexpected error while updating status."));
        }
    }





}
