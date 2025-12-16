package com.example.budgetwolt_android_final.models;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasicUser extends User  {
    protected String address;

    protected List<FoodOrder> myOrders;

    protected List<Review> myReviews;

    protected List<Review> feedback;

    public BasicUser(String username, String password, String name, String surname, String phoneNumber, String address) {
        super(username, password, name, surname, phoneNumber);
        this.address = address;
        this.myOrders = new ArrayList<FoodOrder>();
        this.myReviews = new ArrayList<Review>();
        this.feedback = new ArrayList<Review>();
    }
}
