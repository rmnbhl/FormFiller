package activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tereza.structures.Form;
import com.example.tereza.structures.R;

import java.util.ArrayList;

import controler.Controller;

public class Activity_choose extends AppCompatActivity {

    private final String TAG = "CHOOSE ACTIVITY";

    private final Controller controller = Controller.getInstance();
    private ArrayList<Form> forms;
    private int selectedPosition;
    private String android_id;

    private FormAdapter formAdapter;
    private ListView formList;
    private Button buttConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        selectedPosition = 0;
        findViewsById();
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        this.forms = controller.getForms(this, android_id);
        formAdapter = new FormAdapter(Activity_choose.this, R.layout.layout_list_item_form, forms);
        adjustListView(formList);
        formList.setAdapter(formAdapter);
        setConfirmListener(this.buttConfirm);
    }

    @Override public void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList() {
        this.forms = controller.getForms(this, this.android_id);
        this.formAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    public void adjustListView(ListView lv) {
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.header_list_choice, lv, false);
        lv.addHeaderView(header, null, false);
    }

    private void setConfirmListener(Button b) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(forms.get(selectedPosition).isFilled())
                    new AlertDialog.Builder(Activity_choose.this)
                            .setTitle("Info")
                            .setMessage("Dotazník je možné vyplniť len raz.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                else {
                    Intent i = new Intent(v.getContext(), Activity_fill_form.class);
                    i.putExtra("ID_FORM", selectedPosition);
                    startActivity(i);
                }
            }
        });
    }

    public void findViewsById() {
        this.formList = (ListView) findViewById(R.id.list_forms);
        this.buttConfirm = (Button) findViewById(R.id.butt_confirm);
    }

    class FormAdapter extends ArrayAdapter<Form> {//change to <Train>
        FormAdapter(Context context, int resId, ArrayList<Form> forms) {
            super(context, resId, forms);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderItem viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.layout_list_item_form, parent, false);

                viewHolder = new ViewHolderItem();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.txt_v_form_name);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_form);
                viewHolder.position = position;
                convertView.setTag(viewHolder);
            }
            else
                viewHolder = (ViewHolderItem) convertView.getTag();

            Form form = forms.get(position);
            viewHolder.position = position;
            viewHolder.textView.setText(form.getName());
            viewHolder.checkBox.setChecked(form.isFilled());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = ((ViewHolderItem) view.getTag()).position;
                    formList.setSelection(selectedPosition);
                    notifyDataSetChanged();
                }
            });

            if (viewHolder.position == selectedPosition) {
                convertView.setBackgroundResource(android.R.color.darker_gray);
            }
            else {
                convertView.setBackgroundResource(android.R.color.transparent);
            }

            return (convertView);
        }

        class ViewHolderItem {
            CheckBox checkBox;
            TextView textView;
            int position;
        }
    }
}
