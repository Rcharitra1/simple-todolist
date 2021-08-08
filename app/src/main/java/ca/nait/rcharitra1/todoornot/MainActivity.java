package ca.nait.rcharitra1.todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener{

    Button createBtn;
    Button viewlistBtn;
    Button viewArchivedBtn;
    SharedPreferences prefs;
    View mainView;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createBtn = findViewById(R.id.create_button);
        createBtn.setOnClickListener(this);


        viewlistBtn = findViewById(R.id.button_view_lists);
        viewlistBtn.setOnClickListener(this);

        viewArchivedBtn = findViewById(R.id.button_view_archived);
        viewArchivedBtn.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        mainView = findViewById(R.id.main_activity_linear_layout);

        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        mainView.setBackgroundColor(Color.parseColor(bgColor));

        title = findViewById(R.id.main_title);

        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");


        title.setTextSize(Integer.parseInt(fontSize));



        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.create_button:
            {
                startActivity(new Intent(this, CreateTodoListActivity.class));
                break;
            }

            case R.id.button_view_lists:
            {
                startActivity(new Intent(this, ViewList.class));
                break;
            }

            case R.id.button_view_archived:
            {
                startActivity(new Intent(this, ViewArchivedItems.class));
                break;
            }
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        mainView.setBackgroundColor(Color.parseColor(bgColor));

        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");
        title.setTextSize(Integer.parseInt(fontSize));
    }
}