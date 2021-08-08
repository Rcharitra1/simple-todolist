package ca.nait.rcharitra1.todoornot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class DBManager extends SQLiteOpenHelper {
    static final String TAG="DBManager";
    static final String DB_NAME = "todoornot.db";
    static final int DB_VERSION = 3;
    static final String TABLE_NAME = "todoornot";
    static final String C_ID = BaseColumns._ID;
    static final String C_TITLE = "title";

    static final String TABLE_NAME_ITEM = "todoornotitem";
    static final String C_T_ID = BaseColumns._ID;
    static final String C_T_TITLE = "title";
    static final String C_T_DESCRIPTION = "description";
    static final String C_T_FOREIGN_KEY= "todolistid";
    static final String C_T_COMPLETED = "completed";
    static final String C_T_CREATED = "created";


    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "create table "+TABLE_NAME+" ("+C_ID+ " INTEGER primary key AUTOINCREMENT, "+C_TITLE+" text )";
        String toDoSql = "create table "+TABLE_NAME_ITEM+ " ("+C_T_ID+ " INTEGER primary key AUTOINCREMENT, " +C_T_TITLE+ " text, "+C_T_DESCRIPTION+ " text, "+
                C_T_CREATED+" datetime DEFAULT CURRENT_TIMESTAMP, "+C_T_COMPLETED + " bit, "
                +C_T_FOREIGN_KEY+" integer )";

        db.execSQL(sql);
        db.execSQL(toDoSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists "+TABLE_NAME_ITEM);
        db.execSQL("drop table if exists " +TABLE_NAME);
        onCreate(db);
    }
}
