package com.example.tereza.formfiller;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tereza on 04-02-2016.
 */
public class Form {

    @SerializedName("name")
    private String name;
    @SerializedName("filled")
    private boolean filled;
    @SerializedName("id")
    private int id;
    @SerializedName("questions")
    private ArrayList<Question> questions;

    public Form(String name, int id){
        this(name, id, false);
    }

    public Form(String name, int id, boolean filled) {
        this.name = name;
        this.filled = filled;
        this.id = id;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public int getId() {
        return id;
    }
}
