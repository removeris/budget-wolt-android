package com.example.budgetwolt_android_final.models;


public class Review {

    private int id;

    private BasicUser commentOwner;

    private BasicUser feedbackOwner;

    private Chat chat;
    private int rate;
    private String text;

    public Review(String text, BasicUser commentOwner, Chat chat) {
        this.text = text;
        this.commentOwner = commentOwner;
        this.chat = chat;
    }
}
