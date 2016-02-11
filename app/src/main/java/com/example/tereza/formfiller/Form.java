package com.example.tereza.formfiller;

import android.content.Context;
import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by tereza on 04-02-2016.
 */
public class Form implements Comparable {

    private String name;
    private boolean filled;
    private int id;
    private ArrayList<Question> questions;

    public Form(String name, int id){
        this(name, id, false);
    }

    public Form(String name, int id, boolean filled) {
        this.name = name;
        this.filled = filled;
        this.id = id;
    }

    protected Form(Parcel in) {
        name = in.readString();
        filled = in.readByte() != 0;
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

//    //inflate from XML/json received from server
//    public void inflateForm(Context context) {
//        ArrayList<Question> ques = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            ques.add(new QueCheck("  " + (i + 1) + " check", new String[]{"a", "b", "c"}, context));
//            ques.add(new QueRadio("  " + (i + 1) + " radio", new String[]{"a", "b", "c"}, context));
//        }
//        this.questions = ques;
//    }

    @Override
    public int compareTo(Object another) {
        if(this.id == ((Form) another).id)
            return 0;
        else
            return -1;
    }

    public int getId() {
        return id;
    }
}
