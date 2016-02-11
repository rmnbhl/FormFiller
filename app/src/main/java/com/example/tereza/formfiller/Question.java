package com.example.tereza.formfiller;

import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

/**
 * Created by tereza on 04-02-2016.
 */
public interface Question {

    QuestionType getQuestionType();
    LinearLayout getLayout();
    void setAnswer(int answerPosition);
    void setOnListener(RadioGroup.OnCheckedChangeListener radioListener, CompoundButton.OnCheckedChangeListener checkboxListener);
}