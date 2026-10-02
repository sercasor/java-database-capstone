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
import java.util.*;

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

    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date){
        Optional<Doctor> doctorOptional=this.doctorRepository.findById(doctorId);
        List<Appointment> doctorAppointments;
        Set<LocalDateTime> bookedSlots= new HashSet<>(); //from Appointment.appointmentTime
        Set<LocalDateTime> availableSlots= new HashSet<>(); //from Doctor.availability --> formated
        List<String> availableTimes; //from Doctor.availability (unformated)
        Set<LocalDateTime> freeSlotsFiltered = new HashSet<>(); //filtered
        Doctor doctor;
        LocalDateTime startDate = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        LocalDateTime endDate = LocalDateTime.of(date, LocalTime.MAX);
        List<String> result=new ArrayList<>();

        if (doctorOptional.isEmpty()) {
            logger.error("Doctor not found with ID: {}", doctorId);
            return Collections.emptyList();
        }

        //all booked slots (appointments are always either scheduled or completed) are placed in a Set
        doctorAppointments=this.appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startDate, endDate);
        for (Appointment appointment:doctorAppointments) {
            bookedSlots.add(appointment.getAppointmentTime());

        }

        //all free slots are retrieved and converted to LocalDateTime to compare them with the booked slots in order to filter them out (to get available ones only)
        availableTimes=doctorOptional.get().getAvailableTimes(); // output are brackets such as "09:00-10:00"
        for (String bracket :availableTimes){ //bracket is a String with a divider, a slot is turned to LocalDateTime object
            String[] slots=bracket.split("-");
            availableSlots.add(LocalDateTime.parse(slots[0]));
            availableSlots.add(LocalDateTime.parse(slots[1]));

        }

        //freeSlots is filled with all the times that are actually free upon Collection comparison
        for(LocalDateTime slot :availableSlots){
            if(!bookedSlots.contains(slot)){
                freeSlotsFiltered.add(slot); //the result but not in order
            }
        }

        //An ordered List is returned
        for (LocalDateTime freeSlot : freeSlotsFiltered) {
            result.add(Integer.toString(freeSlot.getHour()));
        }
        Collections.sort(result);
        return result;



    }



}
