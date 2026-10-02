package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.Doc;
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
    //Returns 1 for success, -1 if the doctor already exists, 0 for internal errors
    //Used to save a NEW doctor record in the database
    @Transactional
    public int saveDoctor(Doctor doctor){
        int result;
        String message;
        //Check if the doctor already exists by email
        if (this.doctorRepository.findByEmail(doctor.getEmail())!=null){
            message=String.format("Can't save new doctor. Email %s already exists in the database",doctor.getEmail());
            logger.error(message);
            return -1;

        }
        this.doctorRepository.save(doctor);
        return 1;
    }
    //Returns 1 for success, -1 if the doctor already exists, 0 for internal errors
    //Used to update an existing doctor record in the database
    @Transactional
    public int updateDoctor(Doctor doctor){
        int result;
        String message;
        //Check if the doctor already exists by email
        if (this.doctorRepository.findByEmail(doctor.getEmail())==null){
            message=String.format("Doctor ID %d doesn't exist. Please, create one before updating it. ",doctor.getId());
            logger.error(message);
            return -1;

        }
        this.doctorRepository.save(doctor);
        return 1;
    }

    //retrieves a list of all doctors
    @Transactional
    public List<Doctor> getDoctors(){
        return this.doctorRepository.findAll();
    }

    //Returns 1 for success, -1 if the doctor already exists, 0 for internal errors
    //Used to  delete doctor record by ID in the database
    @Transactional
    public int deleteDoctor(Doctor doctor){
        int result;
        String message;
        //Check if the doctor already exists by email
        if (this.doctorRepository.findByEmail(doctor.getEmail())==null){
            message=String.format("Can't delete doctor with  ID %d since they don't exist. Please, create one before deleting it. ",doctor.getId());
            logger.error(message);
            return -1;

        }
        //delete all associated appointments and then delete the doctor
        this.appointmentRepository.deleteAllByDoctorId(doctor.getId());
        this.doctorRepository.delete(doctor);
        return 1;
    }

    //validates a doctor's login credentials
    @Transactional
    public ResponseEntity<Map<String, String>> validateDoctor(Login login){//login object contains email and password
        ResponseEntity<Map<String, String>> response;
        String message; //value of the ResponseEntity <Map>. The key is "message"
        Doctor doctor=doctorRepository.findByEmail(login.getIdentifier());

        if (doctor.getPassword().equals(login.getPassword())&&doctor.getEmail().equals(login.getIdentifier())){
            message="Doctor is valid: Doctor's email and password  match login credentials";
            return ResponseEntity.badRequest().body(Map.of("message", message));

        }else{
            message="Doctor's email and/or password don't match login credentials";
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }



    }

    @Transactional
    public Map<String, Object> findDoctorByName(String name){

        return Map.of("doctors",this.doctorRepository.findByNameLike(name));
    }
    //Returns a map with the  list of doctors (filtered by name, specialty, and availability during AM/PM)
    @Transactional
    public Map<String, Object> filterDoctorsByNameSpecialtyandTime(String name, String specialty,String amOrPm){

        List<Doctor> unFilteredDoctors;
        List<Doctor> filteredDoctors;
        Map<String, Object>  result=new HashMap<>();
        List<LocalTime> amTimes=new ArrayList<>();
        List<LocalTime> pmTimes=new ArrayList<>();
        unFilteredDoctors=doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name,specialty);
        filteredDoctors=this.amPMDoctorsSorted(unFilteredDoctors,amOrPm);
        return Map.of("doctors",Optional.of(filteredDoctors) );

    }

    /*-----------------------------PRIVATE METHODS-----------------------------*/
    private List<Doctor> amPMDoctorsSorted(List<Doctor> unFilteredDoctors, String amOrPm){
        List<Doctor> filteredDoctors=new ArrayList<>();

        for (Doctor doctor:unFilteredDoctors){
            List<LocalTime> availableTimes=this.timeBracketSplitter(doctor.getAvailableTimes());
            for (LocalTime slot:availableTimes){
                if (amOrPm.equalsIgnoreCase("am")){
                    if (slot.isBefore(LocalTime.NOON)){
                        filteredDoctors.add(doctor);
                    }

                }else if(amOrPm.equalsIgnoreCase("pm")) {
                    if (slot.isAfter(LocalTime.NOON)){
                        filteredDoctors.add(doctor);
                    }

                }
                else {
                    throw new IllegalArgumentException("Illegal parameter: function needs 'am' or 'pm'");
                }


            }
        }
        return filteredDoctors;

    }

    /**
     * Utility function that splits a List<String> availableTimes and returns a LocalTime
     * @param availableTimes Doctor's availability property expressed in Strings separated by a hyphen. Tipically retrieved using Doctor's getter. input are brackets such as "09:00-10:00" and output is 09:00
     * @return List<LocalTime> with all time slots without any hyphen
     */
    private List<LocalTime> timeBracketSplitter(List<String> availableTimes){
        List<LocalTime> slotList=new ArrayList<>();
        for (String bracket :availableTimes){ //bracket is a String with a divider, a slot is turned to LocalDateTime object
            String[] slots=bracket.split("-");
            slotList.add(LocalTime.parse(slots[0]));
            slotList.add(LocalTime.parse(slots[1]));


        }
        return slotList;

    }/**
     * Utility function that splits a List<String> availableTimes and returns a String
     * @param availableTimes Doctor's availability property expressed in Strings separated by a hyphen. Tipically retrieved using Doctor's getter. input are brackets such as "09:00-10:00" and output is 09:00
     * @return List<String> with all time slots without any hyphen
     */
    private List <String> timeBracketSplitterToString(List<String> availableTimes){
        List<String> slotList=new ArrayList<>();
        for (String bracket :availableTimes){ //bracket is a String with a divider, a slot is turned to LocalDateTime object
            String[] slots=bracket.split("-");
            slotList.add(slots[0]);
            slotList.add(slots[1]);


        }
        return slotList;

    }




}
