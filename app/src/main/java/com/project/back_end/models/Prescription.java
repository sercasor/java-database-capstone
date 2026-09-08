package com.project.back_end.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("prescriptions")
public class Prescription {

    /*-------------Private attributes-------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; // String is commonly used for IDs in MongoDB

    @NotNull(message = "Patient's name is required")
    @Size(min = 3, max=100, message = "Name has to be longer than 2 characters and shorter than 100")
    private String patientName;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Medication is required")
    @Size(min = 3, max=100, message = "Medication has to be longer than 2 characters and shorter than 100")
    private String medication;

    @NotNull(message = "Dosage is required")
    private String dosage;

    //optional field
    @Size(max = 200, message = "Max length is 200 characters")
    private String doctorNotes;

    /*-------------Constructors-------------*/

    /**
     *  Parameterized constructor. The no-argument constructor is implicitly provided
     * @param id
     * @param patientName
     * @param appointmentId
     * @param medication
     * @param dosage
     * @param doctorNotes
     */
    public Prescription(String id, String patientName, Long appointmentId, String medication, String dosage, String doctorNotes) {
        this.id = id;
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
        this.doctorNotes = doctorNotes;
    }
    /*-------------Getters and setters-------------*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }


}
