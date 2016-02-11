package controler;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;

import com.example.tereza.formfiller.Form;
import com.example.tereza.formfiller.QueCheck;
import com.example.tereza.formfiller.QueRadio;
import com.example.tereza.formfiller.Question;
import com.example.tereza.formfiller.QuestionType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import activity.Activity_fill_form;
import model.Server;


/**
 * Created by tereza on 04-02-2016.
 */
public class Controller {

    private ArrayList<Form> forms;
    private Server server;

    private static final Controller holder = new Controller();
    public static Controller getInstance() {return holder;}

    public Controller() {
        this.server = new Server();
    }

    public ArrayList<Form> getForms(Context context) {
            JSONArray jAforms = server.getForms("device_id", context);
            try {
                buildForms(jAforms);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return forms;
    }

    public Form getForm(Context context, int idForm) {
        Form form = getFormById(idForm);
        form.setQuestions(this.getQuestionsOfForm(context, idForm));

        return form;
    }

    //build JSONArray from form
    public boolean sendFilledForm(Form f) {
        return server.sendFilledForm(f.getId(), null);
    }

    public ArrayList<Question> getQuestionsOfForm(Context context, int idForm) {
        ArrayList<Question> questions = new ArrayList<>();
        JSONArray jAquestions = this.server.getQuestionsOfForm(idForm, context);
        JSONObject que;
        String[][] options;
        JSONArray jAoptions;
        String questionType;

        try {
            for (int i = 0; i < jAquestions.length(); i++) {
                que = jAquestions.getJSONObject(i);
                jAoptions = que.getJSONArray("options");
                questionType = que.getString("question_type");
                options = new String[jAoptions.length()][2];
                for (int j = 0; j < jAoptions.length(); j++) {
                    options[j][0] = jAoptions.getJSONObject(j).getString("text");
                    options[j][1] = jAoptions.getJSONObject(j).getString("id");
                }

                switch (questionType) {
                    case "0" :
                        questions.add(new QueCheck(que.getString("text"), options, context));
                        break;
                    case "1" :
                        questions.add(new QueRadio(que.getString("text"), options, context));
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public Form getFormById(int idForm) {
        for (int i = 0; i < forms.size(); i++) {
            if(this.forms.get(i).getId() == idForm)
                return forms.get(i);
        }
        System.out.println("form with " + idForm + "does not exist");
        return null;
    }

//    public ArrayList<Form> getFormsState(Context context) {
//        this.server = new Server(context);
//
//    }

    public void setForms(ArrayList<Form> forms) {
        this.forms = forms;
    }

    //request to server
    private void buildForms(JSONArray jAforms) throws JSONException {
        this.forms = new ArrayList<>();
        JSONObject jsonobject;
        for (int i = 0; i < jAforms.length(); i++) {
            jsonobject = jAforms.getJSONObject(i);
            this.forms.add(new Form(
                    jsonobject.getString("name"),
                    jsonobject.getInt("id_form"),
                    Boolean.valueOf(jsonobject.getString("filled"))
            ));
        }
    }



}