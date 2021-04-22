package com.example.mypantry;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


//import com.example.mypantry.dummy.DummyContent;
import com.example.mypantry.ui.login.LoginActivity;
import com.example.mypantry.Notification;

import java.security.AuthProvider;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import static java.security.AccessController.getContext;

public class Notification extends AppCompatActivity {
    String CHANNEL_ID;
    int importance;

    public Notification(String channel_id) {
        CHANNEL_ID= channel_id;
        importance =  NotificationManager.IMPORTANCE_DEFAULT;

    }


    public Notification(String channel_id,int importance) {
        CHANNEL_ID = channel_id;
        this.importance =importance;

    }
    public void createNotificationChannel(String name,String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            //String description = getString(R.string.channel_description);
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
        //    notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotify(android.content.Context packageContext , Class<?> cls ,int id, String title, String description){

        Intent newIntent = new Intent(packageContext, cls);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        newIntent.putExtra("CALLER","notifyService");
        PendingIntent pendingIntent = PendingIntent.getActivity(packageContext, 0, newIntent, 0);

        NotificationManagerCompat nm = NotificationManagerCompat.from(packageContext);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(packageContext, CHANNEL_ID);
        mBuilder.setContentTitle(title).setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher_round).setPriority(importance)
                .setContentIntent(pendingIntent);
        nm.notify(id, mBuilder.build());

    }

}
