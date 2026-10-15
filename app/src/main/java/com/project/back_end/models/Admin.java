package com.project.back_end.models;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * System admins that have acces to manage the backend portal of the app.
 * They handle high-level operations such as user access, data review, and system maintenance.
 */

@Entity
public class Admin {
    
    /*-------------Private Attributes-------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "username cannot be null")
    // to prevent the password from being exposed in JSON responses.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias({"email", "username"}) // so requests can use email, username or identifier as the key without errors
    private String username;

    @NotNull(message = "password cannot be null")
    private String password;

    /*-------------Public methods-------------*/

    /**
     * Constructor with no parameters
     */
    public Admin() {
    }

    /**
     * Parameterized constructor. The no-argument constructor is implicitly provided
     * @param id
     * @param username
     * @param password
     */
    public Admin(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /*-------------Getters and setters-------------*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
