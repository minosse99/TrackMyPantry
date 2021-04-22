package com.example.mypantry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mypantry.data.ITEM;

public class ItemDBHelper extends SQLiteOpenHelper {

    public ItemDBHelper(Context context) {
        super(context, "pantryDB", null , 1);
    }

        @Override
        public void onCreate (SQLiteDatabase db)
        {
            String q = "CREATE TABLE " + ITEM.TBL_NAME +
                    " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ITEM.FIELD_SUBJECT + " TEXT," +
                    ITEM.FIELD_TEXT + " TEXT," +
                    ITEM.FIELD_DATE + " TEXT)";
            db.execSQL(q);
        }
        @Override
        public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){}

}
