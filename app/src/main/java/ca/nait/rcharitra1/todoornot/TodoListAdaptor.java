package ca.nait.rcharitra1.todoornot;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class TodoListAdaptor extends  SimpleCursorAdapter  {


    static  final String TAG="TodoListAdapter";


    static final String[] columns = {DBManager.C_TITLE, DBManager.C_ID};
    static final int [] ids = {R.id.custom_cursor_row_title, R.id.custom_cursor_row_id};

    public TodoListAdaptor(Context context,Cursor cursor) {
        super(context, R.layout.custom_cursor_row, cursor, columns, ids);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

    }


}


class MyOnItemClickListener implements AdapterView.OnItemClickListener
{
    static ViewList activity;
    public MyOnItemClickListener(Context context)
    {
        activity = (ViewList) context;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView textView = (TextView) view.findViewById(R.id.custom_cursor_row_id);
        String text = textView.getText().toString();

        TextView textViewTitle = (TextView) view.findViewById(R.id.custom_cursor_row_title);
        String title = textViewTitle.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("CID", text);
        bundle.putString("TITLE", title);

        Intent intent = new Intent(activity, ViewTodoList.class);

        intent.putExtras(bundle);

        activity.startActivity(intent);




    }
}
