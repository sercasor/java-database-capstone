package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Patient {

    /*-------------Private attributes-------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Patient's name is required")
    @Size(min = 3, max=100, message = "Name has to be longer than 2 characters and shorter than 100")
    private String name;

    @NotNull(message = "Email is required")
    @Email //validates email format adequacy
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 6, message = "Password has to be at least  6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //ensures that the password is not serialized in the response (hidden from the frontend)
    private String password;

    @NotNull(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$") //the phone number must be exactly 10 digits long
    private String phone;

    @NotNull(message = "Address is required")
    @Size(max = 255, message = "max address length is 255 characters")
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

  

}
