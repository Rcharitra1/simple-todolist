package ca.nait.rcharitra1.todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTodoListActivity extends BaseActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    EditText text;
    Button button;
    DBManager dbManager;
    SQLiteDatabase database;
    View view;
    SharedPreferences prefs;
    TextView title;
    static final String TAG="CreateTodoList";

    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo_list);
        text = findViewById(R.id.edit_view_todo_list_name);
        button = findViewById(R.id.create_button_todolist);
        dbManager = new DBManager(this);


        //get shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        view = findViewById(R.id.create_activity_linear_layout);
        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));

        title = findViewById(R.id.create_todo_title);
        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");
        title.setTextSize(Integer.parseInt(fontSize));


        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.create_button_todolist:
            {
                String title = text.getText().toString();

                if(title.trim().length()==0)
                {
                    Toast.makeText(this, "Todo list ", Toast.LENGTH_LONG).show();
                }else
                {
                    database = dbManager.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DBManager.C_TITLE, title);
                    try{
                        database.insertOrThrow(DBManager.TABLE_NAME,null, values);

                        Cursor cursor = database.query(DBManager.TABLE_NAME, null, null,null,null, null, DBManager.C_ID+" desc", "1");
                        String id = "-1";
                        if(cursor != null)
                        {
                            cursor.moveToNext();
                            id = cursor.getString(cursor.getColumnIndex(DBManager.C_ID));
                        }
                        Intent intent = new Intent(this, CreateTodoListItemActivity.class);

                        Log.d(TAG, "onClick: "+id);
                        Bundle bundle = new Bundle();
                        bundle.putString("CID", id);
                        intent.putExtras(bundle);


                        startActivity(intent);

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
        title.setTextSize(Integer.parseInt(fontSize));
    }
}