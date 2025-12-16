package com.example.budgetwolt_android_final.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Chat {

    private int id;
    private String name;
    private String chatText;
    private LocalDate dateCreated;
    private List<Review> messages;

    private FoodOrder foodOrder;

    public Chat(String name, FoodOrder foodOrder) {
        this.name = name;
        this.foodOrder = foodOrder;
        this.dateCreated = LocalDate.now();
        this.messages = new ArrayList<>();
    }
}
