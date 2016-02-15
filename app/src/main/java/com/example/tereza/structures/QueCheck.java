package com.example.tereza.structures;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

/**
 * Created by tereza on 05-02-2016.
 */
public class QueCheck extends QuestionAb {

    private static final QuestionType type = QuestionType.check;
    private transient CheckBox[] cb;

    public QueCheck(String description, String[][] options, Context context) {
        super(description, options, context);
        this.cb = new CheckBox[options.length];
    }

    public QuestionType getQuestionType() {
        return type;
    }

    @Override
    public void setAnswer(int index) {
        answers[index] = !answers[index];
        Log.e("Question answer", this.description + " " + this.options[index][0]);
    }

    @Override
    public void setOnListener(RadioGroup.OnCheckedChangeListener radioListener, CompoundButton.OnCheckedChangeListener checkboxListener) {
        for (int i = 0; i < cb.length; i++) {
            cb[i].setOnCheckedChangeListener(checkboxListener);
        }
    }

    @Override
    public void setDynamicLayout() {
        super.setDynamicLayout();
        for (int i = 0; i < super.options.length; i++) {
            cb[i] = new CheckBox(super.context);
            cb[i].setText(options[i][0]);
            cb[i].setTextSize(17);
            cb[i].setChecked(super.answers[i]);
            cb[i].setId(i);
            super.linLay.addView(cb[i]);
        }
    }

}
