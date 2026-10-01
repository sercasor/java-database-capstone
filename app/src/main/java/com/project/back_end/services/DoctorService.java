package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);
    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TokenService tokenService;
    private Logger logger=LoggerFactory.getLogger(AppointmentService.class);


    /*-----------------------------PUBLIC METHODS-----------------------------*/

    //TODO: como hacemos esto? los appointments indican las citas que ya tenemos, así que todo availableTime del Doctor que coincida con la hora de un appointment esya descartado.
    // Pero habria que traducir la hora a LocalDateTime tipo LocalDateTime.of("10:00") pero sin datos hardcodeados tras iterar el available time del Doctor (sacarlo con repo) y compararlo con el LocalDateTime de la List de appointments mediante doctorAppointments.contains()
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date){
        Optional<Doctor> doctorOptional=this.doctorRepository.findById(doctorId);
        List<Appointment> doctorAppointments;
        List<String> availableTimes;
        Doctor doctor;
        LocalDateTime startDate = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        LocalDateTime endDate = LocalDateTime.of(date, LocalTime.MAX);

        if (doctorOptional.isEmpty()){
            logger.error("Error when getting doctor's availability, null value");

        }

        doctorAppointments=this.appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startDate, endDate);
        for (Appointment appointment:doctorAppointments) {

            appointmentStatus=appointment.getStatus();
        }

        availableTimes









    }



}
