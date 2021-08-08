package ca.nait.rcharitra1.todoornot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_home:
                {
                    startActivity(new Intent(this, MainActivity.class));
                    break;
            }
            case R.id.menu_create_todo_list:
            {
                startActivity(new Intent(this, CreateTodoListActivity.class));
                break;
            }
            case R.id.menu_view_lists:
            {
                startActivity(new Intent(this, ViewList.class));
                break;
            }

            case R.id.menu_view_archived_items:
            {
                startActivity(new Intent(this, ViewArchivedItems.class));
                break;
            }

            case R.id.shared_preferences:
            {
                startActivity(new Intent(this, PrefsActivity.class));
                break;
            }


        }
        return true;
    }



}