package com.example.budgetwolt_android_final.models;


import java.time.LocalDate;


public class Driver extends BasicUser {
    private String driverLicense;
    private LocalDate dateOfBirth;

    public Driver() {}
    public Driver(String username, String password, String name, String surname, String phoneNumber, String address, String driverLicense, LocalDate dateOfBirth) {
        super(username, password, name, surname, phoneNumber, address);
        this.driverLicense = driverLicense;
        this.dateOfBirth = dateOfBirth;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
