package ca.nait.rcharitra1.todoornot;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


public class TodoListItemAdapter extends  SimpleCursorAdapter  {


    static  final String TAG="TodoListItemAdapter";

    static final String[] columns = {DBManager.C_T_TITLE, DBManager.C_T_ID, DBManager.C_T_COMPLETED, DBManager.C_T_CREATED, DBManager.C_T_DESCRIPTION};
    static final int [] ids = {R.id.custom_cursor_item_title, R.id.custom_cursor_item_id, R.id.custom_cursor_item_completed, R.id.custom_cursor_item_date, R.id.custom_cursor_item_description};

    public TodoListItemAdapter(Context context, Cursor cursor) {
        super(context, R.layout.custom_cursor_item, cursor, columns, ids);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        View customRow = (View) view.findViewById(R.id.relative_layout_item);


        TextView completed = (TextView) view.findViewById(R.id.custom_cursor_item_completed);
        String completedValue = completed.getText().toString();


        TextView description = (TextView) view.findViewById(R.id.custom_cursor_item_description);

        String descriptionValue = description.getText().toString();


        TextView createdDate = (TextView) view.findViewById(R.id.custom_cursor_item_date);
        String createdDateValue = createdDate.getText().toString();

        createdDateValue = createdDateValue.substring(0, 16);

        createdDate.setText(createdDateValue);

        if(completedValue.equals("1"))
        {
            customRow.setBackgroundColor(Color.GREEN);
            completed.setText("Completed");
        }else
        {
            customRow.setBackgroundColor(Color.YELLOW);
            completed.setText("Pending");
        }


        if(descriptionValue.length()>=20)
        {
            descriptionValue = descriptionValue.substring(0, 17);

            descriptionValue+="...";
            description.setText(descriptionValue);
        }


    }


}


class MyOnListItemClickListener implements AdapterView.OnItemClickListener
{
    static ViewTodoList activity1;
    public MyOnListItemClickListener(Context context)
    {
        activity1 = (ViewTodoList) context;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView textView = (TextView) view.findViewById(R.id.custom_cursor_item_id);
        String idTv = textView.getText().toString();

        TextView textView1 = (TextView) view.findViewById(R.id.custom_cursor_item_title);
        String title = textView1.getText().toString();


        Log.d("CEHC", "onItemClick: "+idTv+"c"+title);

        Bundle bundle = new Bundle();
        bundle.putString("LISTID", idTv);
        bundle.putString("TITLE", title);

        Intent intent = new Intent(activity1, EditTodoItemActivity.class);

        intent.putExtras(bundle);

        activity1.startActivity(intent);




    }
}
