package model;

import android.content.Context;

import com.example.tereza.formfiller.Form;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by tereza on 07-02-2016.
 */
public class Server {

    private DBHelper dbHelper;

    public Server(Context context) {
        this.setContext(context);
        System.out.println("server created");
    }

    public Server() {
        System.out.println("server created");
    }

    public void setContext(Context context) {
        if(dbHelper == null) {
            this.dbHelper = new DBHelper(context);
            dbHelper.getWritableDatabase();
        }
    }

    public JSONArray getForms(String id_android, Context context) {
        try {
            this.setContext(context);
            return this.dbHelper.getFormsState();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sendFilledForm(int idForm, JSONArray form) {
        return this.dbHelper.updateForm(idForm, form);
    }

    public JSONArray getQuestionsOfForm(int idForm, Context context) {
        try {
            this.setContext(context);
            return dbHelper.getFormWIthQuestions(idForm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("unsuccessfull retrieving full form with questions from server");
        return null;
    }

}
