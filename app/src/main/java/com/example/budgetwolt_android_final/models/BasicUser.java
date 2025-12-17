package com.example.budgetwolt_android_final.models;


import java.util.ArrayList;
import java.util.List;


public class BasicUser extends User  {
    protected String address;


    public BasicUser() {}
    public BasicUser(String username, String password, String name, String surname, String phoneNumber, String address) {
        super(username, password, name, surname, phoneNumber);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
