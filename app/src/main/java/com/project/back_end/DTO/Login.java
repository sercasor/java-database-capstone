package com.project.back_end.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Login {
    @JsonAlias({"email", "username"}) // so requests can use email, username or identifier as the key without errors
    private String identifier; //email for  Doctor/Patient or userName for Admin
    private String password;//hashed before being stored and compared during authentication

    /*------------------------------------CONSTRUCTOR------------------------------------*/

    //No explicit constructor is defined for this class, as it relies on the default constructor provided by Java.

    /*------------------------------------GETTERS AND SETTERS------------------------------------*/

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
