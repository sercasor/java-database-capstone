package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Doctor {

    /*-------------Private attributes-------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doctor's name is required")
    @Size(min = 3, max=100, message = "Name has to be longer than 2 characters and shorter than 100")
    private String name;

    @NotNull(message = "Doctor's specialty is required")
    @Size(min = 3, max=100, message = "Specialty has to be longer than 2 characters and shorter than 100")
    private String specialty;

    @NotNull(message = "Doctor's email is required")
    @Email //validates email format adequacy
    private String email;

    @NotNull(message = "Doctor's password is required")
    @Size(min = 6, message = "Password has to be at least  6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //ensures that the password is not serialized in the response (hidden from the frontend)
    private String password;

    @NotNull(message = "Doctor's phone is required")
    @Pattern(regexp = "^[0-9]{10}$") //the phone number must be exactly 10 digits long
    private String phone;


    @ElementCollection // ensures that the list of time slots is stored as a separate collection in the database.
    //Each time slot is represented as a string (e.g.: "09:00-10:00")
    private List<String> availableTimes;

    /*-------------Getters and setters-------------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

}

