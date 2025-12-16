package com.example.budgetwolt_android_final.models;


import java.time.LocalDate;
import java.util.List;



public class FoodOrder {

    private int id;
    private String title;

    private BasicUser buyer;

    private List<Cuisine> items;
    private double price;

    private Chat chat;

    private OrderStatus status;

    private Restaurant restaurant;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public FoodOrder(String title, BasicUser buyer, List<Cuisine> items, double price, OrderStatus status, Restaurant restaurant) {
        this.title = title;
        this.buyer = buyer;
        this.items = items;
        this.price = price;
        this.status = status;
        this.restaurant = restaurant;
        dateCreated = LocalDate.now();
        dateUpdated = LocalDate.now();
    }

    public String toString() {
        return "ID: " + id + " " + title + " " + price;
    }
}
