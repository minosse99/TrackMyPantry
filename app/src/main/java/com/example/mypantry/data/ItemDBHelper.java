package com.example.mypantry.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mypantry.data.DB_ITEM;

public class ItemDBHelper extends SQLiteOpenHelper {

    public ItemDBHelper(Context context) {
        super(context, "pantryDB", null , 2);
    }

        @Override
        public void onCreate (SQLiteDatabase db)
        {

            String q = "CREATE TABLE " + DB_ITEM.TBL_NAME +
                    " ( "+ DB_ITEM.FIELD_PRODUCTID+" TEXT PRIMARY KEY," +
                    DB_ITEM.FIELD_QUANTITY+" NUMBER,"+
                    DB_ITEM.FIELD_SUBJECT + " TEXT," +
                    DB_ITEM.FIELD_TEXT + " TEXT," +
                    DB_ITEM.FIELD_DATE + " TEXT)";
            db.execSQL(q);
        }
        @Override
        public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){}

}
