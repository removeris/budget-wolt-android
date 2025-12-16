package com.example.budgetwolt_android_final.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Restaurant extends BasicUser {

    protected List<Cuisine> dishes;
    protected String workHours;
    protected double rating;
    private List<FoodOrder> foodOrders;

    public Restaurant(String username, String password, String name, String surname, String phoneNumber, String address, String workHours) {
        super(username, password, name, surname, phoneNumber, address);
        this.workHours = workHours;
        this.rating = 0.0;
        this.dishes = new ArrayList<Cuisine>();
    }
}
