package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * DTOs are POJOs used for privacy and performance.
 * It is meant to simplify and format data transferred to/from the frontend, decoupling it from internal database models.
 * Some info: https://www.freecodecamp.org/news/what-are-dtos-java/
 */
public class AppointmentDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientAddress;
    private LocalDateTime appointmentTime;
    private int status; //Status can indicate if the appointment is "Scheduled:0", "Completed:1", or other statuses (e.g., "Canceled") as needed.
    private LocalDate appointmentDate; //custom getter
    private LocalTime appointmentTimeOnly; //custom getter
    private LocalTime endTime; //custom getter - initial time +1 hour

    /*------------------------------------CONSTRUCTOR------------------------------------*/
    public AppointmentDTO(Long id, Long doctorId, String doctorName, Long patientId, String patientName, String patientEmail, String patientAddress, LocalDateTime appointmentTime, int status) {
        //standard fields
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        //custom getter fields
        this.appointmentDate=appointmentTime.toLocalDate();
        this.appointmentTimeOnly=appointmentTime.toLocalTime();
        this.endTime=appointmentTime.toLocalTime().plusHours(1);
    }


    /*------------------------------------GETTERS AND SETTERS------------------------------------*/
    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentTimeOnly(LocalTime appointmentTimeOnly) {
        this.appointmentTimeOnly = appointmentTimeOnly;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
