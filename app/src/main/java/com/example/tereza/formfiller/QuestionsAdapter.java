package com.example.tereza.formfiller;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by tereza on 06-02-2016.
 */
public class QuestionsAdapter extends BaseAdapter {

    private ArrayList<Question> questions;
    private Context mContext;
    private int layoutResourceId;
    private LayoutInflater mInflater;
    private RecyclerView.ViewHolder holder;

    int[] colors = new int[] { Color.rgb(161, 216, 218), Color.rgb(229, 238, 243)};

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Question que = questions.get(position);
        View row = convertView;
//        if (row == null)
//            row = que.getLayout();
////                row = que.getDynamicLayout();

      row = que.getLayout();
      return (row);
    }

    QuestionsAdapter(Context context, int resId, ArrayList<Question> questions) {
        super();
        this.layoutResourceId = resId;
        this.questions = questions;
        this.mContext = context;
    }

    public QuestionsAdapter(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public int getViewTypeCount() {
        return QuestionType.values().length;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return questions.get(position).getQuestionType().getIdType();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }
}
