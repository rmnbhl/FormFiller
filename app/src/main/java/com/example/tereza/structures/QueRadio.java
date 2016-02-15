package com.example.tereza.structures;

import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by tereza on 07-02-2016.
 */
public class QueRadio extends QuestionAb {

    private static QuestionType type = QuestionType.radio;
    private transient RadioGroup rg;

    public QueRadio(String description, String[][] options, Context context) {
        super(description, options, context);
    }

    public QuestionType getQuestionType() {
        return type;
    }

    @Override
    public void setOnListener(RadioGroup.OnCheckedChangeListener radioListener, CompoundButton.OnCheckedChangeListener checkboxListener) {
        rg.setOnCheckedChangeListener(radioListener);
    }

    @Override
    public void setAnswer(int index) {
        for (int i = 0; i < super.answers.length; i++) {
            super.answers[i] = false;
        }
        super.answers[index] = true;
        Log.e("Question answer", this.description + this.options[index][0]);
    }

    @Override
    public void setDynamicLayout() {
        super.setDynamicLayout();
        rg = new RadioGroup(super.context);
        for (int i = 0; i < super.options.length; i++) {
            RadioButton rb = new RadioButton(super.context);
            rb.setText(options[i][0]);
            rb.setTextSize(17);
            rb.setId(i);
            rb.setChecked(super.answers[i]);
            rg.addView(rb);
        }
        super.linLay.addView(rg);
    }
}
