package com.example.budgetwolt_android_final.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cuisine {

    private int id;
    private String name;
    private List<FoodOrder> foodOrders;
    private Restaurant restaurant;
    private String ingredients;
    private double price;
    private String instructions;
    private boolean isSpicy;
    private boolean isVegan;

    public Cuisine(String name, double price, String ingredients, String instructions, boolean isSpicy, boolean isVegan, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.instructions = instructions;
        this.isSpicy = isSpicy;
        this.isVegan = isVegan;
        this.ingredients = ingredients;
        this.restaurant = restaurant;
    }

    public String toString(){
        return this.name;
    }
}
