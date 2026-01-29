package com.example.budgetwolt_android_final.models;

import java.util.List;


public class Cuisine {

    private int id;
    private String name;
    private String ingredients;
    private double price;
    private boolean isSpicy;
    private boolean isVegan;
    private int quantity = 0;

    public Cuisine(String name, double price, String ingredients, String instructions, boolean isSpicy, boolean isVegan, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.isSpicy = isSpicy;
        this.isVegan = isVegan;
        this.ingredients = ingredients;
    }

    public String toString(){
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSpicy() {
        return isSpicy;
    }

    public void setSpicy(boolean spicy) {
        isSpicy = spicy;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
