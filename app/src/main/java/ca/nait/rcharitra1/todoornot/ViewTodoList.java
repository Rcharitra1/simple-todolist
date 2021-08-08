package ca.nait.rcharitra1.todoornot;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.TextView;

public class ViewTodoList extends BaseActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    static  final String TAG="ViewToList";

    DBManager dbManager;
    SQLiteDatabase database;
    TextView textView;
    ListView listView;
    String id;
    Button button;
    Cursor cursor;
    View view;
    SharedPreferences prefs;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todo_list);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        view = findViewById(R.id.view_todo_item_linear_layout);

        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));

        title = findViewById(R.id.title_todolist);

        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");

        title.setTextSize(Integer.parseInt(fontSize));

        Bundle bundle = getIntent().getExtras();

        id = bundle.getString("CID");
        String title = bundle.getString("TITLE");

        textView = findViewById(R.id.title_todolist);
        textView.setText(title);
        listView = findViewById(R.id.list_view_todolist_items);


        button = findViewById(R.id.create_todo_list_items);

        button.setOnClickListener(this);

        Log.d(TAG, "onCreate: "+id);
        dbManager = new DBManager(this);

    }

    @Override
    protected void onResume() {

        database = dbManager.getReadableDatabase();

        String []columns = {DBManager.C_T_CREATED, DBManager.C_T_COMPLETED, DBManager.C_T_TITLE, DBManager.C_T_ID, DBManager.C_T_DESCRIPTION};
        String selection = DBManager.C_T_FOREIGN_KEY+ " =?";
        String [] selectionArg = {id};
        cursor = database.query(DBManager.TABLE_NAME_ITEM, columns, selection, selectionArg, null, null, null );
        startManagingCursor(cursor);
        listView.setAdapter(new TodoListItemAdapter(this, cursor));

        Log.d(TAG, "onResume: "+cursor.getCount());

        listView.setOnItemClickListener(new MyOnListItemClickListener(this));


        super.onResume();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.create_todo_list_items:
            {

                Bundle bundle = new Bundle();
                bundle.putString("CID", id);
                Intent intent = new Intent(this, CreateTodoListItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
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