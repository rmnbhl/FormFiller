package model;

import android.content.Context;
import android.util.Log;

import com.example.tereza.formfiller.Form;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * contexts parameters necessary just for purposes local DB - will be replaced by server DB
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

    /**
     * return forms without questions - only id, name, filled(state)
     * @param id_android - for authorization in db to find out, if form was already filled from that device
     * @param context
     * @return
     */
    public JSONArray getForms(String id_android, Context context) {
        try {
            this.setContext(context);
            return this.dbHelper.getFormsState();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param idForm
     * @param form
     * @return
     */
    public boolean sendFilledForm(int idForm, String form) {
        return this.dbHelper.updateForm(idForm, form);
    }

    /**
     * return form questions depend on form's id
     * @param idForm
     * @param context
     * @return
     */
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
