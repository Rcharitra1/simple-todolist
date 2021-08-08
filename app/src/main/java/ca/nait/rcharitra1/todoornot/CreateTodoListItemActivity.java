package ca.nait.rcharitra1.todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTodoListItemActivity extends BaseActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    DBManager dbManager;
    SQLiteDatabase database;
    Button button;
    EditText description;
    EditText title;
    TextView date;
    String cid;
    View view;
    SharedPreferences prefs;
    TextView titleMain;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo_list_item);
        dbManager = new DBManager(this);
        database = dbManager.getWritableDatabase();

        Bundle bundle=getIntent().getExtras();
        cid = bundle.getString("CID");
        Log.d("Check", "onCreate: "+cid);
        button = findViewById(R.id.create_button_todolistitem);
        title = findViewById(R.id.edit_view_todo_list_item_title);
        description = findViewById(R.id.edit_view_todo_list_item_description);
        //get shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);




        view = findViewById(R.id.create_todo_item_linear_layout);


        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));

        titleMain = findViewById(R.id.create_todo_list_title);

        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");

        titleMain.setTextSize(Integer.parseInt(fontSize));

        date = findViewById(R.id.date_time);

        date.setText(Calendar.getInstance(Locale.getDefault()).getTime().toString());

        button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.create_button_todolistitem:
            {
                if(title.getText().toString().trim().length()==0 || description.getText().toString().trim().length()==0)
                {
                    Toast.makeText(this, "Title and description are required", Toast.LENGTH_LONG).show();
                }else
                {
                    try {
                        ContentValues values = new ContentValues();
                        values.put(DBManager.C_T_TITLE, title.getText().toString().trim());
                        values.put(DBManager.C_T_FOREIGN_KEY, Integer.parseInt(cid));
                        values.put(DBManager.C_T_DESCRIPTION, description.getText().toString().trim());
                        values.put(DBManager.C_T_COMPLETED, false);

                        database.insertOrThrow(DBManager.TABLE_NAME_ITEM,null,  values);

                        Log.d("ANOTHERCHECK", "onClick: "+values);

                        startActivity(new Intent(this, ViewList.class));

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));
        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");

        titleMain.setTextSize(Integer.parseInt(fontSize));
    }
}