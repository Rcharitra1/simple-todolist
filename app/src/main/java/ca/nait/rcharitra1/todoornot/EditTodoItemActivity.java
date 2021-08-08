package ca.nait.rcharitra1.todoornot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditTodoItemActivity extends BaseActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static String TAG="EDITTEXTActivity";
    TextView textView;
    Button edit;
    Button delete;
    Button archive;
    EditText title;
    EditText description;
    CheckBox isComplete;
    DBManager dbManager;
    TextView createdDate;
    SQLiteDatabase database;
    Cursor cursor;
    String todoListId;
    View view;
    SharedPreferences prefs;
    String username;
    String password;


    AlertDialog.Builder builder;



    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo_item);
        textView = findViewById(R.id.edit_todo_item_title);
        title = findViewById(R.id.edit_view_todo_list_item_title);
        description = findViewById(R.id.edit_view_todo_list_item_description);
        isComplete = findViewById(R.id.todo_list_item_completed);
        createdDate = findViewById(R.id.create_date_time);
        edit = findViewById(R.id.edit_button_todo_list_item);
        delete = findViewById(R.id.delete_button_todo_list_item);
        archive = findViewById(R.id.archive_todo_list_item);
        builder = new AlertDialog.Builder(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        view = findViewById(R.id.edit_linear_layout);

        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));

        Bundle bundle = getIntent().getExtras();
        todoListId = bundle.getString("LISTID");

        String listTitle = textView.getText().toString();
        listTitle += " "+bundle.getString("TITLE");
        textView.setText(listTitle);

        dbManager = new DBManager(this);

        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        archive.setOnClickListener(this);


        String userKey = getResources().getString(R.string.preference_username);
        username = prefs.getString(userKey, "user");
        String passwordKey = getResources().getString(R.string.preference_password);
        password  = prefs.getString(passwordKey, "password");

        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");

        textView.setTextSize(Integer.parseInt(fontSize));


        Log.d(TAG, "onCreate: "+todoListId);
    }

    @Override
    protected void onResume()
    {
        database = dbManager.getReadableDatabase();
        String selection = DBManager.C_T_ID+ " =?";
        String [] selectionArg = {todoListId};
        cursor = database.query(DBManager.TABLE_NAME_ITEM, null, selection, selectionArg, null, null, null );

        startManagingCursor(cursor);


        if(cursor !=null)
        {
            cursor.moveToFirst();
            String titleText = cursor.getString(cursor.getColumnIndex(DBManager.C_T_TITLE));
            title.setText(titleText);


           description.setText(cursor.getString(cursor.getColumnIndex(DBManager.C_T_DESCRIPTION)));


            int getIsCompleted = cursor.getInt(cursor.getColumnIndex(DBManager.C_T_COMPLETED));

            if(getIsCompleted == 1)
            {
                isComplete.setChecked(true);
            }
            else
            {
                isComplete.setChecked(false);
            };

           String dateTime = cursor.getString(cursor.getColumnIndex(DBManager.C_T_CREATED));

           createdDate.setText(dateTime);


        }

        super.onResume();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.edit_button_todo_list_item:
            {

                if(description.getText().toString().trim().length()==0 || title.getText().toString().trim().length()==0)
                {
                    Toast.makeText(this, "Description and Title are required fields",Toast.LENGTH_LONG).show();
                }else
                {


                    ContentValues cv = new ContentValues();
                    cv.put(DBManager.C_T_COMPLETED, isComplete.isChecked());
                    cv.put(DBManager.C_T_DESCRIPTION, description.getText().toString().trim());
                    cv.put(DBManager.C_T_TITLE, title.getText().toString().trim());
                    String questionString = DBManager.C_T_ID+" =?";


                    Log.d(TAG, "onClick: "+cv);


                    database = dbManager.getWritableDatabase();
                    database.update(DBManager.TABLE_NAME_ITEM, cv, questionString, new String[]{todoListId});

                    startActivity(new Intent(this, ViewList.class));


                }
                break;
            }

            case R.id.delete_button_todo_list_item:
            {


                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               deleteToDoItem();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();

                break;
            }

            case R.id.archive_todo_list_item:
            {
                builder.setMessage("Do you want to archive this to do list?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                archiveToDoItem();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();
                break;
            }
        }

    }


    private void archiveToDoItem()
    {
       String url = "http://www.youcode.ca/Lab02Post.jsp";
       String title="";
       String listID="";
       String itemDescription = "";
       String isCompleted="";
       String createdDate = "";
       String selection = DBManager.C_T_ID+ " =?";
       String [] selectionArg = {todoListId};
       cursor = database.query(DBManager.TABLE_NAME_ITEM, null, selection, selectionArg, null, null, null );
       if(cursor !=null)
       {
            cursor.moveToNext();

            listID = cursor.getString(cursor.getColumnIndex(DBManager.C_T_FOREIGN_KEY));
            itemDescription= cursor.getString(cursor.getColumnIndex(DBManager.C_T_DESCRIPTION));
            isCompleted = cursor.getString(cursor.getColumnIndex(DBManager.C_T_COMPLETED));
            createdDate = cursor.getString(cursor.getColumnIndex(DBManager.C_T_CREATED));

       }

       selection = DBManager.C_ID+" =?";
       String [] selectionArgs = {listID};

       cursor=database.query(DBManager.TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

        if(cursor !=null)
        {
            cursor.moveToNext();
            title = cursor.getString(cursor.getColumnIndex(DBManager.C_TITLE));
        }

        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);


            Log.d(TAG, "archiveToDoItem: "+title+createdDate+isCompleted+username+password+itemDescription);
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("LIST_TITLE", title));
            postParameters.add(new BasicNameValuePair("CONTENT", itemDescription));
            postParameters.add(new BasicNameValuePair("COMPLETED_FLAG", isCompleted));
            postParameters.add(new BasicNameValuePair("CREATED_DATE", createdDate));
            postParameters.add(new BasicNameValuePair("ALIAS", username));
            postParameters.add(new BasicNameValuePair("PASSWORD", password));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            Log.d(TAG, "archiveToDoItem: "+formEntity);
            post.setEntity(formEntity);
            client.execute(post);

            deleteToDoItem();

            startActivity(new Intent(this, ViewList.class));


        }
        catch (Exception e)
        {
            Toast.makeText(this, "Archive failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }




    }
    private void deleteToDoItem()
    {
        database= dbManager.getWritableDatabase();
        String whereClause = DBManager.C_T_ID+" =?";
        String [] whereArgs = new String[] {todoListId};
        database.delete(DBManager.TABLE_NAME_ITEM, whereClause, whereArgs);

        startActivity(new Intent(this, ViewList.class));
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