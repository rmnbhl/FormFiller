package com.example.tereza.formfiller;

import android.app.Application;
import android.content.Context;

public class MyContext extends Application {
    private static MyContext instance = new MyContext();

    public MyContext() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

}

