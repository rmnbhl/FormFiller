package com.example.tereza.formfiller;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by tereza on 05-02-2016.
 */
public class QueCheck extends QuestionAb {

    private static final QuestionType type = QuestionType.check;
    private CheckBox cb;

    public QueCheck(String description, String[][] options, Context context) {
        super(description, options, context);
    }

    public QuestionType getQuestionType() {
        return type;
    }

    @Override
    public void setOnListener(RadioGroup.OnCheckedChangeListener radioListener, CompoundButton.OnCheckedChangeListener checkboxListener) {
        cb.setOnCheckedChangeListener(checkboxListener);
    }

    @Override
    public void setDynamicLayout() {
        super.setDynamicLayout();
        for (int i = 0; i < super.options.length; i++) {
            cb = new CheckBox(super.context);
            cb.setText(options[i][0]);
            cb.setTextSize(17);
            cb.setId(i);
            super.linLay.addView(cb);
        }
    }

}
