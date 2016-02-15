package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import controler.Controller;
import com.example.tereza.formfiller.Form;
import com.example.tereza.formfiller.Question;
import com.example.tereza.formfiller.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class Activity_fill_form extends AppCompatActivity {

    private final Controller controller = Controller.getInstance();
    private final Context context = this;
    private ArrayList<Question> questions;
    private QuestionAdapter questionAdapter;
    private Form form;

    private ListView listQuestions;
    private Button buttConfirm;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_fill_form);
        findViewsById();
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void init() {
        Intent intent = getIntent();
        int idForm = intent.getIntExtra("ID_FORM", -1);
        AsyncTaskRunner runner = new AsyncTaskRunner(this);
        runner.execute("load", idForm + "");
    }

    public void finishFormLoading() {
        adjustListView();
        this.questions = form.getQuestions();
        questionAdapter = new QuestionAdapter(Activity_fill_form.this, R.layout.layout_list_item_form, questions);
        listQuestions.setAdapter(questionAdapter);
    }

    public void adjustListView() {
        buttConfirm = new Button(this);
        buttConfirm.setText("send");
        buttConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(Activity_fill_form.this, "",
                        "Odosielanie dotazníku na server. Prosím čakajte...", true);
                dialog.setIndeterminate(true);
                AsyncTaskRunner runner = new AsyncTaskRunner(context, dialog);

                runner.execute("send");
            }
        });
        TextView header = new TextView(this);
        header.setText(form.getName());
        header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        header.setTextSize(30);
        header.setTextColor(Color.DKGRAY);
        header.setPadding(5, 10, 5, 10);
        listQuestions.addHeaderView(header);
        listQuestions.addFooterView(buttConfirm);
    }

    public void findViewsById() {
        this.listQuestions = (ListView) findViewById(R.id.list_questions);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Activity_fill_form Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://activity/http/host/path")
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Activity_fill_form Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://activity/http/host/path")
        );
        client.disconnect();
    }

    class QuestionAdapter extends ArrayAdapter<Question> {
        Context mContext;
        int layoutResourceId;
        ArrayList<Question> questions = null;
        LayoutInflater mInflater = getLayoutInflater();
        int[] colors = new int[]{Color.rgb(161, 216, 218), Color.rgb(229, 238, 243)};

        QuestionAdapter(Context context, int resId, ArrayList<Question> questions) {
            super(context, resId, questions);
            this.layoutResourceId = resId;
            this.questions = questions;
            this.mContext = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final Question que = questions.get(position);
            View row = que.getLayout();
            row.setTag(position);
            que.setOnListener( //TODO
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            que.setAnswer(checkedId);
                        }
                    },
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            que.setAnswer(buttonView.getId());
                        }
                    });
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questions.get((int) v.getTag());

                }
            });
            row.setBackgroundColor(colors[position % colors.length]);
            return (row);
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        private Context context;
        private AlertDialog sendingDialog;
        private ProgressDialog dialog;

        public AsyncTaskRunner(Context context) {
            this.context = context;
        }

        public AsyncTaskRunner(Context context, ProgressDialog sendingDialog) {
            this.context = context;
            this.dialog = sendingDialog;
//            this.sendingDialog = sendingDialog;
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                switch (params[0]) {
                    case "load":
                        form = controller.getForm(this.context, Integer.parseInt(params[1]));
                        break;
                    case "send":
                        Thread.sleep(2500);
                        controller.sendFilledForm(form);
                        printAnswers(form);
                        break;
                }
                resp = params[0];

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        private void printAnswers(Form f) {
            ArrayList<Question> lquestions = f.getQuestions();
            Question q;
            System.out.println("ANSWERS:");
            for (int i = 0; i < lquestions.size(); i++) {
                q = lquestions.get(i);
                for (int j = 0; j < q.getAnswers().length; j++) {
                    if (q.getAnswers()[j])
                        System.out.println(q.getDescription() + ":\n " +
                                q.getOptions()[j][0]);
                }

            }
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            switch (result) {
                case "load":
                    finishFormLoading();
                    break;
                case "send":
                    this.dialog.dismiss();

                    new AlertDialog.Builder(context)
                        .setTitle("Odosielanie")
                        .setMessage("Dotazník úspešne odoslaný.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(context, Activity_choose.class);
                                startActivity(i);
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_upload)
                        .show();
            }
        }
    }
}