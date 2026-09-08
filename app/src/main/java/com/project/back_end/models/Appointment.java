package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * represents a scheduled meeting between a doctor and a patient
 */
@Entity
public class Appointment {

    /*-------------Private Attributes-------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) //Eager is a nice since the appointment and doctor will be frequently accessed together
    @NotNull(message = "A doctor has to be associated to this appointment")
    // @JoinColumn(name=) not needed since Doctor has no "appointment" attribute
    private Doctor doctor;

    //many Appointments can be linked to ONE patient
    @ManyToOne(fetch = FetchType.EAGER) //Eager is a nice since the appointment and patient will be frequently accessed together
    @NotNull(message = "A patient has to be associated to this appointment")
    // @JoinColumn(name=) not needed since Patient has no "appointment" attribute
    private Patient patient;

    @Future
    private LocalDateTime appointmentTime;

    @NotNull(message = "the appointment status has to be specified")
    private int status; //0 for scheduled, 1 for completed

    /*-------------Private methods-------------*/
    @Transient
    private LocalDateTime getEndTime(){
        LocalDateTime endTime;
        endTime=this.appointmentTime.plusHours(1);
        return endTime;
    };

    private LocalDate getAppointmentDate(){
        return this.appointmentTime.toLocalDate();
    }

    private LocalTime getAppointmentTimeOnly(){
        return this.appointmentTime.toLocalTime();
    };

    /*-------------PUBLIC METHODS-------------*/


    /*-------------Constructors-------------*/

    /**
     * Parameterized constructor. The no-argument constructor is implicitly provided
     * @param id
     * @param doctor
     * @param patient
     * @param appointmentTime
     * @param status
     */
    public Appointment(Long id, Doctor doctor, Patient patient, LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    /*-------------Getters and setters-------------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

