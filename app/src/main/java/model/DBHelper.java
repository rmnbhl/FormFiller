package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.example.tereza.structures.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    public static final String TEST_TABLE_NAME = "test";
    public static final String TEST_COLUMN_NAME = "nazov";
    public static final String TEST_COLUMN_ID = "id_testu";
    public static final String TEST_COLUMN_FILLED = "vyplneny";

    public static final String OTAZKA_TABLE_NAME = "otazka";
    public static final String OTAZKA_COLUMN_ID = "id_otazka";
    public static final String OTAZKA_COLUMN_TEXT = "text";
    public static final String OTAZKA_COLUMN_ID_TEST = "id_testu";
    //0 - multiple opt - check box, 1 - single option - radio button
    public static final String OTAZKA_COLUMN_QUESTION_TYPE = "typ_otazky";

    public static final String MOZNOST_TABLE_NAME = "moznost";
    public static final String MOZNOST_COLUMN_ID = "id_moznost";
    public static final String MOZNOST_COLUMN_TEXT = "text";
    public static final String MOZNOST_COLUMN_ID_QUESTION = "id_otazka";


    private Context context;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        System.out.println("DBHelper created");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DBHelper onCreate");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS `moznost` (" +
                        "`id_moznost` char(20) NOT NULL," +
                        "`id_otazka` int(11) NOT NULL," +
                        "`text` char(200) NOT NULL," +
                        "`body` int(11) NOT NULL," +
                        "`nazov_obr` char(20) DEFAULT NULL" +
                        ");"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS `test` (" +
                        "'nazov' char(20) NOT NULL," +
                        "`id_testu` int(11) NOT NULL," +
                        "'vyplneny' boolean DEFAULT false" +
                        ");"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS `otazka` ( " +
                        "`id_otazka` int(11) NOT NULL," +
                        "`id_testu` int(11) NOT NULL," +
                        "`text` char(200) NOT NULL," +
                        "`typ_odpovedi` char(1) NOT NULL," +
                        "`typ_otazky` char(1) NOT NULL," +
                        "`nazov_obr` char(20) DEFAULT NULL" +
                        ");");

        insertFromFile(this.context, R.raw.insert, db);
        this.updateQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public int insertFromFile(Context context, int resourceId, SQLiteDatabase db) {
        // Reseting Counter
        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and there's no other stuff)
        try {
            while (insertReader.ready()) {
                String insertStmt = insertReader.readLine();
                db.execSQL(insertStmt);
                result++;
            }
            insertReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // returning number of inserted rows
        return result;
    }

    public void updateQuestions(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.OTAZKA_COLUMN_QUESTION_TYPE, "1");
        db.update(this.OTAZKA_TABLE_NAME, contentValues, this.OTAZKA_COLUMN_ID_TEST + " = '1'", null);
    }

    /**
     *  return state(id, name, filled) of forms - without questions
     */
    public JSONArray getFormsState() throws JSONException {
        JSONArray jAforms = new JSONArray();
        JSONObject jOform;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from test", null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            jOform = new JSONObject();
            System.out.println("getFormsState " + res.getString(res.getColumnIndex(TEST_COLUMN_FILLED)));
            jOform.put("id_form", res.getInt(res.getColumnIndex(TEST_COLUMN_ID)));
            jOform.put("name", res.getString(res.getColumnIndex(TEST_COLUMN_NAME)));
            jOform.put("filled", res.getString(res.getColumnIndex(TEST_COLUMN_FILLED)));

            jAforms.put(jOform);
            res.moveToNext();
        }
        return jAforms;
    }

    public JSONArray getFormWIthQuestions(int idTest) throws JSONException {
        JSONArray jAform = new JSONArray();
        JSONObject jOquestion;
        JSONObject jOoption;
        JSONArray jAoptions;
        Cursor res;
        int[] questionsId = getQuestionsIdOfForm(idTest);
        SQLiteDatabase db = this.getReadableDatabase();

        for (int i = 0; i < questionsId.length; i++) {
            res = db.rawQuery("select o.id_otazka, o.text as ot_text, o.typ_otazky, m.id_moznost, m.text as m_text from otazka o " +
                    "join moznost m where o.id_otazka = m.id_otazka and " +
                    "o.id_otazka = '" + questionsId[i] + "';", null);
            res.moveToFirst();
            jOquestion = new JSONObject();
            jAoptions = new JSONArray();

            jOquestion.put("text", res.getString(res.getColumnIndex("ot_text")));
            jOquestion.put("question_type", res.getString(res.getColumnIndex(OTAZKA_COLUMN_QUESTION_TYPE)));
            for (int j = 0; j < res.getCount(); j++) {
                jOoption = new JSONObject();
                jOoption.put("id", res.getString(res.getColumnIndex(MOZNOST_COLUMN_ID)));
                jOoption.put("text", res.getString(res.getColumnIndex("m_text")));

                jAoptions.put(jOoption);
                res.moveToNext();
            }
            jOquestion.put("options", jAoptions);
            jAform.put(jOquestion);
        }

        return jAform;
    }

    /**
     * return set of question - option depend to given test ID
     * @param idTest
     * @return
     */
    private int[] getQuestionsIdOfForm(int idTest) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id_otazka from otazka o where o.id_testu = '" + idTest + "';", null);
        int[] id = new int[res.getCount()];
        res.moveToFirst();

        for (int i = 0; i < id.length; i++) {
            id[i] = res.getInt(res.getColumnIndex(OTAZKA_COLUMN_ID));
            res.moveToNext();
        }

        return id;
    }

    public boolean updateForm(int idForm, String form) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("vyplneny", "true");
        db.update("test", contentValues, "id_testu = " + idForm, null);//new String[]{Integer.toString(idForm)}

        System.out.println("update of form " + idForm);
        Cursor res = db.rawQuery("select * from test", null);
        res.moveToFirst();
        return true;
    }

}