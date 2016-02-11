package com.example.tereza.formfiller;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tereza on 05-02-2016.
 */
public abstract class QuestionAb implements Question {

    protected String description;
    protected String[][] options;
    protected boolean[] answers;
    protected Context context;
    protected LinearLayout linLay;

    public QuestionAb(String description, String[][] options, Context context) {
        this.description = description;
        this.options = options;
        this.context = context;
        this.answers = new boolean[options.length];
    }

    public void setDynamicLayout() {
        linLay = new LinearLayout(context);
        linLay.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(context);
        tv.setTextSize(23);
        tv.setTextColor(Color.BLACK);
        tv.setText(description);
        linLay.addView(tv);
        linLay.setPadding(3, 10, 10, 3);
    }

    @Override
    public void setAnswer(int index) {
        this.answers[index] = true;
        Log.e("Question answer", this.description + this.options[0][0]);
    }

    public String[][] getOptions() {
        return options;
    }

    public LinearLayout getLayout() {
        this.setDynamicLayout();

        return this.linLay;
    }
}
