package com.example.budgetwolt_android_final.models;



import java.io.Serializable;
import java.time.LocalDate;



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

    public User() {}
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
