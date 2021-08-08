package ca.nait.rcharitra1.todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ViewList extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    TodoListAdaptor adaptor;
    SQLiteDatabase database;
    Cursor cursor;
    DBManager dbManager;
    ListView listView;
    SharedPreferences prefs;

    View view;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        dbManager = new DBManager(this);

        listView = findViewById(R.id.list_view_messages);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        view = findViewById(R.id.view_todo_linear_layout);

        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));

        title = findViewById(R.id.view_list_title_text);

        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");

        title.setTextSize(Integer.parseInt(fontSize));


    }

    @Override
    protected void onResume() {

        database = dbManager.getReadableDatabase();
        cursor = database.query(DBManager.TABLE_NAME, null, null, null, null, null, DBManager.C_ID+" desc");
        startManagingCursor(cursor);

        adaptor = new TodoListAdaptor(this, cursor);
        listView.setAdapter(adaptor);
        if(cursor.getCount()!=0)
        {
            listView.setOnItemClickListener(new MyOnItemClickListener(this));
        }

        super.onResume();
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