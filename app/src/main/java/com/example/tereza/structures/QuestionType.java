package com.example.tereza.structures;

/**
 * Created by tereza on 05-02-2016.
 */
public enum QuestionType {

    check(0),
    radio(1);

    private final int idType;

    QuestionType(int idType) {
        this.idType = idType;
    }

    public int getIdType() {
        return idType;
    }
}
