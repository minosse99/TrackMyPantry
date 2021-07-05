package com.example.mypantry;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mypantry.activity.MainActivity;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, MainActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        newIntent.putExtra("CALLER","notifyService");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"TRACKPANTRY")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Aggiorna la tua Dispensa!")
                .setContentText("É un po che non aggiorni la tua dispensa ")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager  = NotificationManagerCompat.from(context);
        notificationManager.notify(312,builder.build());
    }

}
