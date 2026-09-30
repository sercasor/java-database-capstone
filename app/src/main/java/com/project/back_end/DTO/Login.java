package com.project.back_end.DTO;

public class Login {
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
