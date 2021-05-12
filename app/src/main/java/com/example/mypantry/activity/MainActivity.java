package com.example.mypantry.activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;


import com.example.mypantry.AuthToken;
import com.example.mypantry.DBManager;
import com.example.mypantry.data.ITEM;
import com.example.mypantry.ItemRecyclerViewAdapter;
import com.example.mypantry.Notification;
import com.example.mypantry.R;
import com.example.mypantry.dummy.DummyItem;
import com.example.mypantry.ui.login.ListItem;
import com.example.mypantry.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private Notification not;
    private static DBManager db = null;
    private List<ListItem> test = null;
    private RecyclerView recyclerView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(test == null && recyclerView == null) {
            test = new ArrayList<>();
            recyclerView = findViewById(R.id.list);
            db = new DBManager(this);
        }
/*
        db.save("Caffe", "122211211211", "Oggi");
        db.save("The", "98239374934", "25/05/20");
        db.save("Pasta", "1222112134989895", "Oggi");
        db.save("Computer", "1222543511211", "Oggi");
        db.save("Stampante", "12434511211", "Oggi");
        db.save("Cartuccia per stampanti", "132432211211", "Oggi");
        db.save("Acqua", "1222113293041", "Oggi");
*/
        //Action Click listener on btnShare
        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        checkDB();

        if(!AuthToken.isNull()){
            Log.e("USERNAME ", AuthToken.username);
            Log.e("TOKEN ", AuthToken.token);
            Button btnLogin = findViewById(R.id.Login);
            btnLogin.setText(AuthToken.username);
            btnLogin.setClickable(false);
        }
    }


    public void loginAction(View view) {
        Intent intent = new Intent();
        ComponentName component =
            new ComponentName(this, LoginActivity.class);
        intent.setComponent(component);
        startActivity(intent);

    }

    public void newItemAction(View view){

        Intent intent = new Intent();
        ComponentName component =
                new ComponentName(this, ActivitySearch.class);
        intent.setComponent(component);
        startActivity(intent);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void checkDB(){
        try {
            test.clear();

            Cursor cursor = db.query();
            while(cursor.moveToNext()){

                String subject = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_SUBJECT));
                int id = cursor.getInt(cursor.getColumnIndex(ITEM.FIELD_ID));
                String text = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_TEXT));
//        String date = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_DATE));

                test.add(new ListItem(id, new DummyItem(text,subject)));
            }

        }catch (CursorIndexOutOfBoundsException e){
            Log.e("Error : checkDB", String.valueOf(e));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(test, db, this);

        recyclerView.setAdapter(adapter);

    }




}