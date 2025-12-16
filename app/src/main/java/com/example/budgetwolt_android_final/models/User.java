package com.example.budgetwolt_android_final.models;



import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class User implements Serializable {

    protected int id;
    protected String username;
    protected String password;
    protected String name;
    protected String surname;
    protected String phoneNumber;
    protected LocalDate dateCreated;
    protected LocalDate dateModified;
    protected boolean isAdmin;

    public User(String username, String password, String name, String surname, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.isAdmin = false;
        this.dateCreated = LocalDate.now();
        this.dateModified = LocalDate.now();
    }

    public String toString(){
        return id + "\t" + username + "\t" + password + "\t" + name + "\t" + surname + "\t" + phoneNumber;
    }
}
