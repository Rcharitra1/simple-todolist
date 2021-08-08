package ca.nait.rcharitra1.todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewArchivedItems extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    final static String TAG="ViewArchivedItems";
    ArrayList<HashMap<String, String>> archived = new ArrayList<HashMap<String, String>>();
    ListView listView;
    SharedPreferences prefs;
    String username;
    String password;
    View view;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_archived_items);
        Log.d(TAG, "onCreate: ");

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        listView = findViewById(R.id.list_view_archived);
        view = findViewById(R.id.archive_linear_layout);
        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));
        String userKey = getResources().getString(R.string.preference_username);
        username = prefs.getString(userKey, "user");
        String passwordKey = getResources().getString(R.string.preference_password);
        password  = prefs.getString(passwordKey, "password");

        title = findViewById(R.id.view_archived_title);
        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");
        title.setTextSize(Integer.parseInt(fontSize));
        displayArchivedItems();
    }

    private void getFromCloud()
    {
        String url = "http://www.youcode.ca/Lab02Get.jsp?ALIAS="+username+"&PASSWORD="+password;
        BufferedReader in =null;

        try
        {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
            in = new BufferedReader(inputStreamReader);
            String line ="";
            Log.d(TAG, "getFromCloud: ");
            while ((line = in.readLine())!=null)
            {
                Log.d(TAG, "getFromCloud: ");
                HashMap<String, String> temp = new HashMap<String, String>();

                line = line.substring(0, 16);
                temp.put("POST_DATE", line);
                Log.d(TAG, "getFromCloud: "+line);
                line = in.readLine();
                temp.put("LIST_TITLE", line);
                Log.d(TAG, "getFromCloud: "+line);
                line = in.readLine();

                if(line.length()>=20)
                {
                    line = line.substring(0, 17);
                    line+= "...";
                }
                temp.put("CONTENT", line);
                Log.d(TAG, "getFromCloud: "+line);
                line = in.readLine();

                if(line.trim().equals("1"))
                {
                    line = "finished";
                }else
                {
                    line = "pending";
                }
                temp.put("COMPLETED", line);
                Log.d(TAG, "getFromCloud: "+line);
                archived.add(temp);

            }
            in.close();
        }catch (Exception e)
        {
            Toast.makeText(this, "No records received", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void displayArchivedItems()
    {

        String [] keys = new String[]{"POST_DATE", "LIST_TITLE", "CONTENT", "COMPLETED"};
        int [] ids = new int []{R.id.custom_archived_date, R.id.custom_archived_list_title, R.id.custom_archived_title, R.id.custom_archived_completed};
        SimpleAdapter adapter = new SimpleAdapter(this, archived, R.layout.custom_archive_row, keys, ids);

        getFromCloud();


        listView.setAdapter(adapter);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String bgColorKey= getResources().getString(R.string.preference_main_bg);
        String bgColor = prefs.getString(bgColorKey, "#FFEB3B");
        view.setBackgroundColor(Color.parseColor(bgColor));
        String fontSizeKey = getResources().getString(R.string.preference_font_size);
        String fontSize = prefs.getString(fontSizeKey, "12");
        title.setTextSize(Integer.parseInt(fontSize));
    }
}