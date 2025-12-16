package com.example.budgetwolt_android_final.models;



import java.util.ArrayList;
import java.util.List;


public class Restaurant extends BasicUser {

    protected List<Cuisine> dishes;
    protected String workHours;
    protected double rating;
    private List<FoodOrder> foodOrders;

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Restaurant(String username, String password, String name, String surname, String phoneNumber, String address, String workHours) {
        super(username, password, name, surname, phoneNumber, address);
        this.workHours = workHours;
        this.rating = 0.0;
        this.dishes = new ArrayList<Cuisine>();
    }
}
